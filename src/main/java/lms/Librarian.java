package lms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Librarian extends User {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public void addBook(Connection connection, Statement statement) throws IOException {
        try {
            System.out.println("Enter the book details:");
            System.out.print("Title: ");
            String title = input.readLine();
            System.out.print("Author(s): ");
            String author = input.readLine();
            String[] authors = author.split(",");
            boolean uniqueISBN = false;
            String isbn = "";
            do {
                System.out.print("ISBN: ");
                isbn = input.readLine();
                String query = "SELECT title from \"book\" where isbn = '" + isbn + "'";
                statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if (rs.next()) {
                    System.out.println("Book with the entered ISBN already exists in the database");
                    System.out.println("Book title" + rs.getString("title"));
                } else {
                    uniqueISBN = true;
                }
            } while (uniqueISBN == false);
            System.out.print("Year: ");
            String yearStr = input.readLine();
            int year = Integer.parseInt(yearStr);
            String query = "INSERT INTO book(title, author, isbn, year) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);
            // Execute the query
            pst.setString(1, title);
            Array authorArray = connection.createArrayOf("VARCHAR", authors);
            pst.setArray(2, authorArray);
            pst.setString(3, isbn);
            pst.setInt(4, year);
            pst.executeUpdate();
            System.out.println("Book added");
        } catch (SQLException e) {
            System.out.println("Error occurred while adding the book: " + e.getMessage());
        }
    }
    public void displayStudents(Connection connection, Statement statement) {
        try {
            statement = connection.createStatement();
            String query = "SELECT * FROM \"User\" WHERE user_type = 'Student'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                System.out.println("Student name: " + name);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while fetching students: " + e.getMessage());
        }

    }

    public void displayTransaction(Connection con, Statement statement) {
        try {
            statement = con.createStatement();
            String query = "SELECT * FROM \"transaction\"";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int transaction_id = resultSet.getInt("transaction_id");
                int book_id = resultSet.getInt("book_id");
                int user_id = resultSet.getInt("user_id");
                String transaction_type = resultSet.getString("transaction_type");
                String transaction_date = resultSet.getString("transaction_date");
                System.out.println("Transaction id: "+ transaction_id);
                System.out.println("Book id: "+book_id);
                System.out.println("User id: "+user_id);
                System.out.println("Transaction type: "+transaction_type);
                System.out.println("Transaction date: "+transaction_date);
                System.out.println();

            }
        } catch (SQLException e) {
            System.out.println("Error occurred while fetching transactions: " + e.getMessage());
        }
    }
}
