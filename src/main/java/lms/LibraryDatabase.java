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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LibraryDatabase {
    private BufferedReader input;
    private ArrayList<Book> books;
    Student studentObj = new Student();

    public LibraryDatabase() {
        // this.books = new ArrayList<>();
        input = new BufferedReader(new InputStreamReader(System.in));
    }

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
        // feteching the current and date. Then converting it to a string
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
            // TODO: handle exception
            e.printStackTrace();
        }

    }
    public int getUserId(String username, Connection connection)
    {
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
            // TODO: handle exception
            e.printStackTrace();
        }
        return 0;
    }
}
