package se4351.studybuddy;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;



public class Login {
    static private String connectionString;
    private static final String GET_USERS_SQL="SELECT * from USERS";
    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet resultSet = null;

    Login(String conString)
    {
        connectionString = conString;
    }
    /**
     * Connects to DB and inserts if username is available
     *
     * @param username
     * @param password
     * @return true on success </br> false on failure
     *
     */
    public static boolean register(String username, String password)
    {
        try{
            openConnection();
            //check to see if username is available
            resultSet = statement.executeQuery(GET_USERS_SQL);
            while(resultSet.next())
            {
                if(resultSet.getString(1).equals(username))
                    return false;
            }
            //Generate salt and hash the password
            byte[] salt = generateSalt();
            byte[] hash = hashPassword(password, salt);
            //insert new user into table
            final String insertQuery = "INSERT INTO USERS VALUES ('"+username+"', '"+new String(hash)+"', '"+new String(salt)+"');";
            statement.executeUpdate(insertQuery);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param username
     * @param password
     * @return true on success </br> false on failure
     */
    public static boolean login(String username, String password) {
        try{
            openConnection();
            resultSet = statement.executeQuery(GET_USERS_SQL+" WHERE username='"+username+"';");
            while(resultSet.next())
            {
                if(checkPassword(password, resultSet.getString(2), resultSet.getString(3)))
                {
                    closeConnection();
                    return true;
                }
            }
            closeConnection();
            return false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    private static void openConnection() throws SQLException
    {
        connection = DriverManager.getConnection(connectionString);
        statement = connection.createStatement();
    }
    private static void closeConnection()
    {
        if (resultSet != null) try { resultSet.close(); } catch(Exception e) {}
        if (statement != null) try { statement.close(); } catch(Exception e) {}
        if (connection != null) try { connection.close(); } catch(Exception e) {}
    }
    /**
     * Generates a random 32 byte salt for passwords
     *
     * @return a random 32 byte array
     */
    private static byte[] generateSalt()
    {
        SecureRandom rand = new SecureRandom();
        byte[] salt = new byte[36];
        rand.nextBytes(salt);
        return salt;
    }

    private static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        PBEKeySpec key = new PBEKeySpec(password.toCharArray(), salt, 100, 256);
        SecretKeyFactory keyGen = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return keyGen.generateSecret(key).getEncoded();
    }
    private static boolean checkPassword(String password, String hash, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        byte[] hashPass = hashPassword(password,salt.getBytes());
        return new String(hashPass).equals(hash);
    }

}
