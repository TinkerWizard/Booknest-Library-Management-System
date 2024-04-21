package lms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Account {
    public String starter(Connection connection) {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String username = "";
            System.out.println("1. Sign Up");
            System.out.println("2. Sign In");
            System.out.println("Choose yours: ");
            String choiceStr = reader.readLine();
            int choice = Integer.parseInt(choiceStr);
            if (choice == 1) {
                username = signUp();
    
            } else if (choice == 2) {
                // show options
                username = signIn(connection);
            } else {
                System.out.println("Exiting...!");
            }
            return username;
        }
        catch(IOException e)
        {
            System.out.println("An error occurred while reading input: " + e.getMessage());
            return "";
        }
    }

    public static String signUp() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            String username = reader.readLine();
            String password = reader.readLine();
            String user_type = "";
            String name = reader.readLine();
            String email = reader.readLine();
            return username;
        } catch (Exception e) {
            System.out.println("An error occurred while reading input: " + e.getMessage());
            return "";
        }
    }

    public static String signIn( Connection connection ) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter username:");
            String username = reader.readLine();
            System.out.println("Enter password:");
            String password = reader.readLine();
            try {
                // Construct the SQL query with a prepared statement
                String query = "SELECT * FROM \"User\" WHERE username = ?";
    
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, username);
    
                // Execute the query
                ResultSet rs = pstmt.executeQuery();
    
                if (rs.next()) {
                    // User found, now check the password
                    String storedPassword = rs.getString("password");
                    if (password.equals(storedPassword)) {
                        // Passwords match, user authenticated
                        System.out.println("User authenticated!");
                        return username;
                    } else {
                        // Passwords don't match
                        System.out.println("Incorrect password!");
                    }
                } else {
                    // User not found
                    System.out.println("User not found!");
                }
                // Close the result set and prepared statement
                rs.close();
                pstmt.close();
                return "";
            } catch (SQLException e) {
                // Handle any SQL errors
                e.printStackTrace();
                return "";
            }
        } catch (Exception e) {
            System.out.println("An error occurred while reading input: " + e.getMessage());
            return "";
        }
    }
}
