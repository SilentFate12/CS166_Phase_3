/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class ProfNetwork {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of Messenger
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public ProfNetwork (String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end ProfNetwork

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
          List<String> record = new ArrayList<String>();
         for (int i=1; i<=numCol; ++i)
            record.add(rs.getString (i));
         result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       if(rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            ProfNetwork.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      ProfNetwork esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Messenger object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new ProfNetwork (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("3. Recover password");
            System.out.println("4. Search people");
            System.out.println("5. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
	       case 3: RecoverPassword(esql); break;
	       case 4: SearchPeople(esql); break;
	       case 5: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. Goto Friend List");
		System.out.println("2. Goto Friend Profile");
                System.out.println("3. Update Profile");
		System.out.println("4. Change Password");
                System.out.println("5. Write a new message");
		System.out.println("6. View current messages");
                System.out.println("7. Send Friend Request");
		System.out.println("8. Accept/Reject connection requests");
                System.out.println(".........................");
                System.out.println("9. Log out");
                switch (readChoice()){
                   case 1: FriendList(esql, authorisedUser); break;
                   case 2: FriendProfile(esql); break;
                   case 3: UpdateProfile(esql); break;
		   case 4: UpdatePassword(esql); break;
                   case 5: SendMessage(esql); break;
		   case 6: ViewMessages(esql); break;
		   case 7: SendConnectionRequest(esql); break;
		   case 8: DecideRequests(esql); break;
                   case 9: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user with privided login, passowrd and phoneNum
    * An empty block and contact list would be generated and associated with a user
    **/
   public static void CreateUser(ProfNetwork esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();
         System.out.print("\tEnter user email: ");
         String email = in.readLine();

	 //Creating empty contact\block lists for a user
	 String query = String.format("INSERT INTO USR (userId, password, email, contact_list) VALUES ('%s','%s','%s')", login, password, email);

         esql.executeUpdate(query);
         System.out.println ("User successfully created!");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end

   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(ProfNetwork esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM USR WHERE userId = '%s' AND password = '%s'", login, password);
         int userNum = esql.executeQuery(query);
	 if (userNum > 0)
		return login;
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

// Rest of the functions definition go in here
/*
 * case 1: FriendList(esql); break;
 * case 2: FriendProfile(esql); break;
 * case 3: UpdateProfile(esql); break;
 * case 4: UpdatePassword(esql); break;
 * case 5: SendMessage(esql); break;
 * case 6: ViewMessages(esql); break;
 * case 7: SendConnectionRequest(esql); break;
 * case 8: DecideRequests(esql); break;
 */
   public static String FriendList(ProfNetwork esql, String authorisedUser){
      try{
         String query = "SELECT * FROM Connection C WHERE C.userId = " + authorisedUser
	 + " AND C.status = Accepted";
         int userNum = esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   } 
   public static String FriendProfile(ProfNetwork esql, String authorisedUser){
      try{
	 System.out.println("Input the name of the friend you'd like to visit: ");
	 String friendName = in.readLine();
         String query = "SELECT * FROM User U2 WHERE U2.userID EXISTS IN(SELECT C.userID From Connections C WHERE C.userID EXISTS IN" + 
		 "(SELECT U.userID FROM User U WHERE U.name LIKE %" + friendName + "%))";
         int userNum = esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end
   public static String UpdateProfile(ProfNetwork esql, String authorisedUser){
      try{
	 
	 System.out.println("What do you want to update? \n" +
			    "***************************\n" +
			    "OPTIONS:\n" +
			    "1 for Email\n" +
			    "2 for Name\n" +
			    "3 for Date of Birth\n" +
			    "4 for Work-Related Options\n" +
			    "5 for School-Related Options\n" +
			    "6 to exit."; 
	 switch(readChoice()) {
		 case 1: System.out.println("Enter your new email: ");
		 	 String newEmail = in.readLine();
		 	 String emailQuery = "UPDATE User SET email = " +
				 newEmail + " WHERE userId = " + authorisedUser;
		 	 int userNum = esql.executeQuery(emailQuery);
		 	 System.out.println("Email updated!");
		 	 break;
		 case 2: System.out.println("Enter your new name: ");
		 	 String newName = in.readLine();
		 	 String nameQuery = "UPDATE User SET name = " +
				 newName + " WHERE userId = " + authorisedUser;
		 	 int userNum = esql.executeQuery(nameQuery);
		 	 System.out.println("Name updated!");
		 	 break;
		 case 3: System.out.println("Enter your new date of birth: ");
		 	 String newDOB = in.readLine();
		 	 String DOBQuery = "UPDATE User SET dateofbirth = " +
				 newDOB + " WHERE userId = " + authorisedUser;
		 	 int userNum = esql.executeQuery(DOBQuery);
		 	 System.out.println("Date of Birth updated!");
		 	 break;
		 case 4: System.out.println("Which part of your Work would you like to change?\n" +
					    "*************************************************\n" +
					    "OPTIONS:\n" +
					    "1 for Adding a New Work Experience\n" +
					    "2 for Updating a Previous Work Experience\n" +
					    "3 to exit";
			 switch(readChoice()) {
				 case 1: System.out.println("Please input your company: ");
				 	 String company = in.readLine();
				  	 System.out.println("Please input your role in the company: ");
				 	 String role = in.readLine();
				 	 System.out.println("Please input your starting date: ");
				 	 String startDate = in.readLine();
				 	 System.out.println("If you worked at a location, please enter it. Otherwise just press [ENTER]: ");
				 	 String location = in.readLine();
				 	 System.out.println("If you already left this job, please enter your ending date." +
							     " Otherwise just press [ENTER]: ");
				 	 String endDate = in.readLine();
				 	 String createWorkExperience = "INSERT INTO Work_Experience(" +
						 "userId, company, role, location, startdate, enddate)" +
						 " VALUES(" + authorisedUser + ", " + company + ", " +
						 role + ", " + location + ", " + startDate + ", " +
						 endDate + ")";
				 	 int userNum = esql.executeQuery(createWorkExperience);
				 	 System.out.println("Work Experience Created!");
				 	 break;
				 case 2: System.out.println("Which previous company are you updating for?: ");
				 	 String prevCompany = in.readLine();
				 	 break;
				 default: System.out.println("Please select one of the three options.");
				 	  break;
			 }
		 
		 	 break;
		 case 5: break;
		 case 6: break;
		 default: System.out.println("Unrecognized input, exiting...");
			  break;
	 }
	 
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end
   public static String UpdatePassword(ProfNetwork esql){
      try{
	 boolean tryingToLogin = true;
	 System.out.println("To verify it's you before allowing you to change your password, please log in again: ");
	 while(tryingToLogin) { 
	 	String authUser = LogIn(esql);
	 	if (authUser != null) {
			 System.out.println("User recognized! Please input your new password: ");
			 String newPass = in.readLine();
			 String passQuery = "UPDATE User SET password = " + newPass + "WHERE userId = " + authUser;
			 int userNum = esql.executeUpdate(passQuery);
			 System.out.println("Password successfully updated!");
			 tryingToLogin = false;
		} else {
			bool deciding = true;
			while(deciding) {
				System.out.println("User not recognized. Do you want to try logging in again? (1 = Yes, 2 = No): ");
				switch(readChoice()) {
					case 1: deciding = false; break;
					case 2: deciding = false;
						tryingToLogin = false;
						break;
					default: System.out.println("Please enter either 1 for yes or 2 for no.");
						 break;
				}
			}
		}
	 }
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end
   public static String SendMessage(ProfNetwork esql, String authorisedUser){
      try{
         String query = "SELECT COUNT(*) FROM Message";
         int userNum = esql.executeQueryAndPrintResult(query)+1;
	 System.out.println("Please Enter Message Recipiant");
	 String reciever=in.readLine();
	 //check for valid receiver
	 String query= "SELECT CONVERT(TIME,GETDATE())";
	 timestamp t=esql.esql.executeQueryAndPrintResult(query);
	 System.out.println("Please Enter Message(500 character limit)");
	 //check for valid length under 500 char
	 String message= in.readLine();
	 String status="Sent";
	 String query=String.format("INSERT INTO MESSAGE(msgId,senderId,receiverId,contents,sendTime,deleteStatus,status)" + 
				    " VALUES ('%s','%s','%s''%s','%s','%s','%s')",  userNum,senderId,receiverId,message,0,status);
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end
  public static String ViewMessages(ProfNetwork esql, String authorisedUser){
      try{
	 System.out.println("Displaying messages...");
	 System.out.println("**********************");
         String query = "SELECT * FROM Message WHERE senderId = " + authorisedUser +
		 " AND deletestatus = 0";
         int userNum = esql.executeQueryAndPrintResult(query);
	 boolean deciding = true;
	 while(deciding) {
		 System.out.println("Do you want to delete any messages? (1 for Yes, 2 for No): ");
		 switch(readChoice()) {
			 case 1: System.out.println("Which message do you want to delete? (Enter full message ID here): ");
				 String mID = in.readLine();
				 String messageQuery = "DELETE FROM Message WHERE messageId = " + mID;
				 int userNum = esql.executeQuery(messageQuery);
				 System.out.println("Message successfully deleted!");
				 deciding = false;
				 break;
			 case 2: deciding = false; break;
			 default: System.out.println("Please enter 1 for yes or 2 for no.");
				  break;
		 }
	 }
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end
   public static String SendConnectionRequest(ProfNetwork esql, String authorisedUser){
      try{
         String query = "SELECT * FROM Connection WHERE userId = " + authorisedUser;
         int userNum = esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end
   public static String DecideRequests(ProfNetwork esql, String authorisedUser){
      try{
         String query = "SELECT * FROM Connection WHERE userId = " + authorisedUser +
		 " AND status != Accepted AND status != Declined";
         int numOfResults = esql.executeQueryAndPrintResult(query);
	 boolean deciding = true;
	 while(deciding) {
		 System.out.println("Select a connection to accept or decline using its connection ID " +
				    "(If you wish to exit instead, Simply press [Enter]): ";
		 String connectionID = in.readLine();
		 if (connectionID != null) {
			 boolean deciding2 = true;
			 while(deciding2) {
				 System.out.println("What do you want to do with the connection? (1 for Accept, 2 for Decline, 3 to exit): ");
				 switch(readChoice()) {
					 case 1: String acceptQuery = "UPDATE Connection SET status = Accepted WHERE connectionId = " +
						 connectionID;
						 int userNum = esql.executeQuery(acceptQuery);
						 System.out.println("Connection Accepted!");
						 deciding2 = false;
						 break;
					 case 2: String declineQuery = "UPDATE Connection SET status = Declined WHERE connectionId = " +
						 connectionID;
						 int userNum = esql.executeQuery(declineQuery);
						 System.out.println("Connection Declined.");
						 deciding2 = false;
						 break;
					 case 3: deciding2 = false; break;
					 default: System.out.println("Unrecognized Input, please input a valid answer.");
						  break;
				 }
			 }
		 }
		 else {
			deciding = false;
		 }
	 }
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

}//end ProfNetwork
