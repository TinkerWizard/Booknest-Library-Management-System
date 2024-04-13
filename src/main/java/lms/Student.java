package lms;

import java.util.ArrayList;
import java.util.HashMap;

public class Student extends User{
    ArrayList<Book> borrowedBooks = new ArrayList<>();
    public void displayBorrowedBooks()
    {
        if (borrowedBooks.isEmpty()) {
            System.out.println("No borrowed books");
        }
        else
        {
            System.out.println("Borrow books:");
            for (Book book : borrowedBooks) {
                System.out.println(book);
            }
        }
    }

}
