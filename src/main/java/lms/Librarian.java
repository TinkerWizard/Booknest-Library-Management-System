package lms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Librarian extends User {
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
