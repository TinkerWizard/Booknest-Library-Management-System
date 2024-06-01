package lms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LibraryDatabase {
    private BufferedReader input;
    Student studentObj = new Student();

    public LibraryDatabase() {
        // this.books = new ArrayList<>();
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    public void displayBooks(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM book";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                Array authorArray = resultSet.getArray("author");
                String[] authors = (String[]) authorArray.getArray();
                String isbn = resultSet.getString("isbn");
                int year = resultSet.getInt("year");

                System.out.println("Book ID: " + bookId);
                System.out.println("Title: " + title);
                System.out.print("Author(s): ");
                for (String author : authors) {
                    System.out.print(author + ", ");
                }
                System.out.println();
                System.out.println("ISBN: " + isbn);
                System.out.println("Year: " + year);
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while fetching books: " + e.getMessage());
        }
    }

    public void searchBook(String keyword, Connection connection) {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM book WHERE title LIKE '%" + keyword + "%'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int year = resultSet.getInt("year");
                System.out.println("Book id: " + bookId);
                System.out.println("Title: " + title);
                System.out.println("Author(s): " + author);
                System.out.println("Year: " + year);
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while fetching books: " + e.getMessage());
        }
    }

    public void borrowBooks(String keyword, Connection connection, String username) throws IOException {
        searchBook(keyword, connection);
        System.out.println("Enter the book id of the book you would like to borrow: ");
        String bookIdString = input.readLine();
        int bookId = Integer.parseInt(bookIdString);
        // System.out.println("The username");
        int user_id = getUserId(username, connection);// dummy initial value

        String transactionType = "Borrow";
        Date transactionDate = new Date(System.currentTimeMillis());
        String updateQuery = "INSERT INTO \"transaction\" (book_id, user_id, transaction_type, transaction_date) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(updateQuery);
            pst.setInt(1, bookId);
            pst.setInt(2, user_id);
            pst.setString(3, transactionType);
            pst.setDate(4, transactionDate);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void returnBooks(Connection connection, String username) {
        int user_id = getUserId(username, connection);
        // display the books borrowed by the user_id
        String query = "SELECT * FROM \"transaction\" WHERE transaction_type = 'Borrow' AND user_id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, user_id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int book_id = rs.getInt("book_id");
                String bookName = getBookName(connection, book_id);
                System.out.println("Title: " + bookName);
                System.out.println("Book id: " + book_id);
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Select the book id of the book you would like to return:");
            String returnBookIdStr = input.readLine();
            int returnBookId = Integer.parseInt(returnBookIdStr);
            String transactionType = "Return";
            Date transactionDate = new Date(System.currentTimeMillis());
            String updateQuery = "INSERT INTO \"transaction\" (book_id, user_id, transaction_type, transaction_date) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(updateQuery);
            pst.setInt(1, returnBookId);
            pst.setInt(2, user_id);
            pst.setString(3, transactionType);
            pst.setDate(4, transactionDate);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getUserId(String username, Connection connection) {
        try {
            String findUserId = "SELECT user_id FROM \"User\" WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(findUserId);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getBookName(Connection connection, int book_id) {
        try {
            String findBookTitle = "SELECT * FROM \"book\" WHERE book_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(findBookTitle);
            preparedStatement.setInt(1, book_id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                return rs.getString("title");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
