package lms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.sql.Connection;
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

    public void addBook(Statement statement) throws IOException {
        try {
            System.out.println("Enter the book details:");
            System.out.print("Title: ");
            String title = input.readLine();
            System.out.print("Author: ");
            String author = input.readLine();
            System.out.print("ISBN: ");
            String isbn = input.readLine();
            System.out.print("Year: ");
            int year = Integer.parseInt(input.readLine());

            String query = "INSERT INTO book(title, author, isbn, year) VALUES (" +
                    title + ", " + author + ", " + isbn + ", " + year + ")";

            // Execute the query
            statement.executeUpdate(query);

            System.out.println("Book added");
        } catch (SQLException e) {
            System.out.println("Error occurred while adding the book: " + e.getMessage());
        } finally {
            // Close the BufferedReader
            try {
                input.close();
            } catch (IOException e) {
                System.out.println("Error occurred while closing the input stream: " + e.getMessage());
            }
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
