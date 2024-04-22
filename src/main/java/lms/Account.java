package lms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Account {
    public String starter(Connection connection) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String username = "";
            System.out.println("1. Sign Up");
            System.out.println("2. Sign In");
            System.out.println("Choose yours: ");
            String choiceStr = reader.readLine();
            int choice = Integer.parseInt(choiceStr);
            if (choice == 1) {
                username = signUp(connection);

            } else if (choice == 2) {
                // show options
                username = signIn(connection);
            } else {
                System.out.println("Exiting...!");
            }
            return username;
        } catch (IOException e) {
            System.out.println("An error occurred while reading input: " + e.getMessage());
            return "";
        }
    }

    public static String signUp(Connection con) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            boolean usernameAvailable = false;
            String username = "";
            do {
                Statement st = con.createStatement();
                System.out.println("Enter username:");
                username = reader.readLine();
                String query = "SELECT username FROM \"User\" WHERE username = '" + username + "'";
                ResultSet rs = st.executeQuery(query);
                if (!rs.next()) {
                    System.out.println("Username Available");
                    usernameAvailable = true;
                } else {
                    System.out.println("Try another username");
                }
            } while (usernameAvailable == false);
            System.out.println("Enter password:");
            String password = reader.readLine();
            String user_type = "Librarian";
            System.out.println("Enter Name:");
            String name = reader.readLine();
            boolean emailAvailable = false;
            String email = "";
            do {
                Statement st = con.createStatement();
                System.out.println("Enter email:");
                email = reader.readLine();
                String query = "SELECT email FROM \"User\" WHERE email = '" + email + "'";
                ResultSet rs = st.executeQuery(query);
                if (!rs.next()) {
                    System.out.println("Email Available");
                    emailAvailable = true;
                } else {
                    System.out.println("Try another email");
                }
            } while (emailAvailable == false);
            String query = "INSERT INTO \"User\" (username, password, user_type, name, email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, user_type);
            pst.setString(4, name);
            pst.setString(5, email);
            pst.executeUpdate();
            return username;
        } catch (Exception e) {
            System.out.println("An error occurred while reading input: " + e.getMessage());
            return "";
        }
    }

    public static String signIn(Connection connection) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String username = "";
            boolean isUsernameFound = false;
            do {
                System.out.println("Enter username:");
                username = reader.readLine();
                String query = "SELECT username FROM \"User\" WHERE username = '" + username + "'";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    isUsernameFound = true;
                } else {
                    System.out.println("Username doesn\'t exist!");
                }
            } while (isUsernameFound == false);
            try {
                String password = "";
                boolean doesPasswordMatch = false;
                do {
                    System.out.println("Enter password:");
                    password = reader.readLine();
                    String query = "SELECT * FROM \"User\" WHERE username = ?";
                    PreparedStatement pstmt = connection.prepareStatement(query);
                    pstmt.setString(1, username);
                    ResultSet rs = pstmt.executeQuery();
    
                    if (rs.next()) {
                        // User found, now check the password
                        String storedPassword = rs.getString("password");
                        if (password.equals(storedPassword)) {
                            // Passwords match, user authenticated
                            System.out.println("User authenticated!");
                            doesPasswordMatch = true;
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
                } while (doesPasswordMatch == false);
                // Construct the SQL query with a prepared statement
                // Execute the query
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
