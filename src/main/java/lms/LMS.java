package lms;

import java.sql.Statement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LMS {

    public static void main(String[] args) throws IOException {
        String dbUrl = "jdbc:postgresql://localhost:5432/booknest_db";
        String username = "postgres";
        String password = "april1";
        // Scanner scanner = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ;
        try {
            Connection con = DriverManager.getConnection(dbUrl, username, password);
            if (con != null) {
                System.out.println("Database connected");
            }
            Statement st = con.createStatement();
            LibraryDatabase libraryDatabase = new LibraryDatabase();
            Librarian librarian = new Librarian();
            Account account = new Account();
            String currentUsername = account.starter(con);
            // String currentUserId =
            System.out.println("The current username is: " + currentUsername);
            String query = "SELECT user_type FROM \"User\" WHERE username = '" + currentUsername + "'";
            st.executeQuery(query);
            ResultSet rs = st.executeQuery(query);

            String userType = "";
            while (rs.next()) {
                userType = rs.getString("user_type");
            }
            System.out.println("The userType: " + userType);
            int choice = 0;
            do {
                if (userType.equals("Librarian")) {
                    System.out.println("\nLibrary Management System");
                    System.out.println("1. Display all books");
                    System.out.println("2. Search for a book");
                    System.out.println("3. Add a book");
                    System.out.println("4. Display students");
                    System.out.println("5. Display transactions");
                    System.out.println("6. Add Student");
                    System.out.println("7. Exit");
                    System.out.print("Enter your choice: ");
                    String choiceStr = reader.readLine();
                    // reader.readLine(); // Consume newline character
                    choice = Integer.parseInt(choiceStr);
                    switch (choice) {
                        case 1:
                            System.out.println("\nAll Books:");
                            libraryDatabase.displayBooks(con);
                            break;
                        case 2:
                            System.out.print("Enter title or author to search: ");
                            String keyword = reader.readLine();
                            System.out.println("\nSearch Results:");
                            libraryDatabase.searchBook(keyword, con);
                            break;
                        case 3:
                            librarian.addBook(con, st);
                            break;
                        case 4:
                            librarian.displayStudents(con, st);
                            break;
                        case 5:
                            librarian.displayTransaction(con, st);
                            break;
                        case 6:
                            //add student
                        case 7:
                            System.out.println("Exiting...");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
                if (userType.equals("Student")) {
                    System.out.println("\nLibrary Management System");
                    System.out.println("1. Borrow a book");
                    System.out.println("2. Return a book");
                    System.out.println("6. Exit");
                    System.out.print("Enter your choice: ");
                    String choiceStr = reader.readLine();
                    choice = Integer.parseInt(choiceStr);
                    switch (choice) {
                        case 1:
                            System.out.print("Enter title or author to borrow: ");
                            String borrowKeyword = reader.readLine();
                            libraryDatabase.borrowBooks(borrowKeyword, con, currentUsername);
                            break;
                        case 2:
                            libraryDatabase.returnBooks(con, currentUsername);
                            break;
                        case 7:
                            System.out.println("Exiting...");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            } while (choice != 7);
            // con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
