
import java.sql.*;

import com.mysql.jdbc.Driver;


public class Login {
	private static String connectionString="jdbc:mysql://10.21.64.23:3306"; //connection url
	private static final String GET_USERS_SQL="SELECT * from USERS";
	private static Connection connection = null;  
    private static Statement statement = null;   
    private static ResultSet resultSet = null; 
    /**
     * Connects to DB and inserts if username is available
     * 
     * @param username
     * @param password
     * @return 1 on success </br> 0 on password failure </br> -1 on username failure
     * @throws SQLException 
     * 
     */
    public static int register(String username, String password) throws SQLException
    {
    	openConnection();
    	if(password.length()<5)
    	{
    		closeConnection();
    		return 0;
    	}
    	//check to see if username is available
    	resultSet = statement.executeQuery(GET_USERS_SQL);
    	while(resultSet.next())
    	{
    		if(resultSet.getString(1)==username)
    			return -1;
    	}
    	//insert new user into table
    	final String insertQuery = "INSERT INTO USERS VALUES ('"+username+"', '"+password+"');";
    	statement.executeUpdate(insertQuery);
    	return 1;
    }
    
    /**
     * 
     * @param username
     * @param password
     * @return true on success </br> false on failure
     * @throws SQLException 
     */
    public static boolean login(String username, String password) throws SQLException
    {
    	openConnection();
    	resultSet = statement.executeQuery(GET_USERS_SQL+"WHERE username= '"+username+"';");
    	while(resultSet.next())
    	{
    		if(resultSet.getString(2)==password)
    		{
    			closeConnection();
    			return true;
    		}
    	}
    	closeConnection();
    	return false;
    }
    private static void openConnection() throws SQLException
    {
    	 connection = DriverManager.getConnection(connectionString,"test_user", "test_user");  
         statement = connection.createStatement();  
    }
    private static void closeConnection()
    {
    	if (resultSet != null) try { resultSet.close(); } catch(Exception e) {}  
        if (statement != null) try { statement.close(); } catch(Exception e) {}  
        if (connection != null) try { connection.close(); } catch(Exception e) {}  
    }
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		register("test", "test");
		
		if(login("test", "test"))
			System.out.println("Success");
	}

}
