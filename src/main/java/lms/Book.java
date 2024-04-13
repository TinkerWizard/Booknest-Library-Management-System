package lms;

public class Book {
    private long book_id;
    private String title;
    private String author;
    private String isbn;
    private int year;

    public Book(long book_id, String title, String author,String isbn, int year) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
    }
    public long getbookId()
    {
        return book_id;
    }
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "Book Id: " + book_id + ", Title: " + title + ", Author: " + author + ", ISBN: " + isbn + ", Year: " + year;
    }
}
