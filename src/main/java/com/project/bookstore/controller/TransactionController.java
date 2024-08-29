package com.project.bookstore.controller;

import com.project.bookstore.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.project.bookstore.model.Transaction;
import java.sql.PreparedStatement;

public class TransactionController {
public List<Transaction> getAllTx(int userId) throws SQLException {
    List<Transaction> txList = new ArrayList<>();
    String query = "SELECT t.tx_id, b.book_name, t.price, t.quantity, t.total, t.cash_used, t.cash_change, t.date_time "
                   + "FROM transactions t "
                   + "JOIN books b ON t.book_id = b.book_id "
                   + "WHERE t.user_id = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        preparedStatement.setInt(1, userId);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Transaction tx = new Transaction();
                tx.setTxId(resultSet.getInt("tx_id"));
                tx.setBookName(resultSet.getString("book_name"));
                tx.setPrice(resultSet.getInt("price"));
                tx.setQuantity(resultSet.getInt("quantity"));
                tx.setTotal(resultSet.getInt("total"));
                tx.setCashUsed(resultSet.getInt("cash_used"));
                tx.setCashChange(resultSet.getInt("cash_change"));
                tx.setDateTime(resultSet.getString("date_time"));
                txList.add(tx);
            }
        }
    }

    return txList;
}
    
    public void addTransaction(Transaction transaction, int book_id, int user_id) throws SQLException {
        String query = "INSERT INTO transactions (book_id, user_id, price, quantity, total, cash_used, cash_change, date_time) VALUES (?,?,?,?,?,?,?,?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, book_id);
            statement.setInt(2, user_id);
            statement.setInt(3, transaction.getPrice());
            statement.setInt(4, transaction.getQuantity());
            statement.setInt(5, transaction.getTotal());
            statement.setInt(6, transaction.getCashUsed());
            statement.setInt(7, transaction.getCashChange());
            statement.setString(8, transaction.getDateTime());

            statement.executeUpdate();
        }
    }
    
            public void deleteTx(int txIdInputHere) throws SQLException {
                String query = "DELETE FROM transactions WHERE tx_id = ?";

                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement statement = connection.prepareStatement(query)) {

                    statement.setInt(1, txIdInputHere);
                    statement.executeUpdate();
                }
            }
    
}