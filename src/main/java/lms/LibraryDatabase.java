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
import java.util.ArrayList;

public class LibraryDatabase {
    private BufferedReader input;
    private ArrayList<Book> books;
    Student studentObj = new Student();

    public LibraryDatabase() {
        // this.books = new ArrayList<>();
        input=new BufferedReader(new InputStreamReader(System.in));
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
                }
                else
                {
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
        // boolean found = false;
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT title, author FROM book WHERE title LIKE '%" + keyword + "%'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                System.out.println("Title: " + title);
                System.out.println("Author(s): " + author);
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while fetching books: " + e.getMessage());
        }
    }

    public void borrowBooks(String keyword) throws IOException {
        ArrayList<Book> borrowBooksSearchResults = new ArrayList<>();
        boolean found = false;
        int i = 1;
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(i + ". " + book);
                i++;
                borrowBooksSearchResults.add(book);
                found = true;
            }
        }
        if (found) {
            System.out.println("Enter the choice of book to borrow:");
            int bookChoice = input.read();
            studentObj.borrowedBooks.add(borrowBooksSearchResults.get(bookChoice - 1));
            studentObj.displayBorrowedBooks();
        }
        borrowBooksSearchResults.clear();
        if (!found) {
            System.out.println("Book not found.");
        }
    }
}
