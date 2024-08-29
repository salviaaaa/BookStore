/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.project.bookstore.controller;

/**
 *
 * @author salvia
 */
import com.project.bookstore.model.Book;
import com.project.bookstore.model.Transaction;
import com.project.bookstore.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookController {
    private DatabaseConnection databaseConnection;

    public BookController(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public List<Book> getBooks() throws SQLException {
        List<Book> books = new ArrayList<>();

        String query = "SELECT * FROM books";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                books.add(new Book(
                        resultSet.getInt("book_id"),
                        resultSet.getString("book_name"),
                        resultSet.getInt("price"),
                        resultSet.getInt("stock")
                ));
            }
        }

        return books;
    }
    
    public Book getOneBook(int bookId) throws SQLException {
        String query = "SELECT * FROM books WHERE book_id = ?";
        Book book = null;

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    book = new Book(
                            resultSet.getInt("book_id"),
                            resultSet.getString("book_name"),
                            resultSet.getInt("price"),
                            resultSet.getInt("stock")
                    );
                }
            }
        }

        return book;
    }
    
    public List<Book> searchBook(String searchText) {
        List<Book> books = new ArrayList<>();

        try {
            String query = "SELECT * FROM books WHERE book_name LIKE ?";
            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, "%" + searchText + "%");
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        books.add(new Book(
                                resultSet.getInt("book_id"),
                                resultSet.getString("book_name"),
                                resultSet.getInt("price"),
                                resultSet.getInt("stock")
                        ));
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Database connection failed!");
        }

        return books;
    }

    public void updateBook(int bookId, String bookName, int price, int stock) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            databaseConnection = new DatabaseConnection();
            connection = databaseConnection.getConnection();
            String query = "UPDATE books SET book_name = ?, price = ?, stock = ? WHERE book_id = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, bookName);
            statement.setInt(2, price);
            statement.setInt(3, stock);
            statement.setInt(4, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
            public void addBook(String bookName, int price, int stock) {
            Connection connection = null;
            PreparedStatement statement = null;
            try {
                databaseConnection = new DatabaseConnection();
                connection = databaseConnection.getConnection();
                String query = "INSERT INTO books (book_name, price, stock) VALUES (?, ?, ?)";
                statement = connection.prepareStatement(query);
                statement.setString(1, bookName);
                statement.setInt(2, price);
                statement.setInt(3, stock);
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    
        public void deleteBook(int bookIdInputHere) {
            Connection connection = null;
            PreparedStatement statement = null;
            try {
                databaseConnection = new DatabaseConnection();
                connection = databaseConnection.getConnection();
                String query = "DELETE FROM books WHERE book_id = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, bookIdInputHere);
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    
public boolean buyBook(int bookId, int quantity, int userId, int cashUsed) throws SQLException {
    boolean success = false;
    String query = "SELECT * FROM books WHERE book_id = ?";
    boolean creditIsUsed = false;

    try (Connection connection = databaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setInt(1, bookId);
        ResultSet resultSet = statement.executeQuery();
        
        
        if (!resultSet.next()) {
            System.out.println("Book not found with the ID");
            return success;
        }

        int stock = resultSet.getInt("stock");
        String bookName = resultSet.getString("book_name");

        if (quantity > stock) {
            System.out.println("Quantity is more than stock");
            return success;
        }
        
        int price = resultSet.getInt("price");
        
        if (cashUsed == -1) {
            System.out.println("Credit or Debit card is used!");
            cashUsed = price * quantity;
            creditIsUsed = true;
        }

        if (cashUsed < price * quantity) {
            System.out.println("Cash not enough to buy");
            return success;
        }

        query = "UPDATE books SET stock = stock - ? WHERE book_id = ?";

        try (PreparedStatement updateStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            updateStatement.setInt(1, quantity);
            updateStatement.setInt(2, bookId);
            updateStatement.executeUpdate();
            connection.commit();
        }

        success = true;

        if (success) {
            int total = price * quantity;
            int cashChange = cashUsed - total;
            if (creditIsUsed) { cashUsed = 0; }
            
            Transaction transaction = new Transaction(
                    bookName,
                    price,
                    quantity,
                    total,
                    cashUsed,
                    cashChange,
                    getCurrentDateTime()
            );

            TransactionController transactionController = new TransactionController();
            transactionController.addTransaction(transaction, bookId, userId);
        }
    }

    return success;
}

private String getCurrentDateTime() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return now.format(formatter);
}

}