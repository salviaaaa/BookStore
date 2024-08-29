/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.bookstore.controller;

import com.project.bookstore.model.User;
import com.project.bookstore.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserController {
    private static final String CHECK_USERNAME_QUERY = "SELECT * FROM bookstore.users WHERE username = ?";
    private static final String LOGIN_QUERY = "SELECT * FROM bookstore.users WHERE username = ? AND password = ?";
    private static final String REGISTER_QUERY = "INSERT INTO bookstore.users (username, password) VALUES (?, ?)";

    public User login(String username, String password) {
        User user = null;

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(LOGIN_QUERY);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                user = new User(userId, username, password);
            }
        } catch (SQLException e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, e);
        }

        return user;
    }

public boolean register(String username, String password) {
    boolean success = false;

    try (Connection connection = DatabaseConnection.getConnection()) {
        PreparedStatement statement = connection.prepareStatement(CHECK_USERNAME_QUERY);
        statement.setString(1, username);

        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next()) {
            statement = connection.prepareStatement(REGISTER_QUERY);
            statement.setString(1, username);
            statement.setString(2, password);

            int rowsAffected = statement.executeUpdate();
            success = rowsAffected > 0;
        } 
    } catch (SQLException e) {
        Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, e);
    }

    return success;
}

}
