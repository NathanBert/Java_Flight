package com.flight.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import com.flight.model.User;

public class UserDAO {
    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO User (fname, lname, email, phone, user_type, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFName());
            stmt.setString(2, user.getLName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getUserType());
            String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
            stmt.setString(6, hashed);

            stmt.executeUpdate();
        }
    }

    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM User WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setFName(rs.getString("fname"));
                    user.setLName(rs.getString("lname"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setUserType(rs.getString("user_type"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }
        }
        return null;
    }


    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM User WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setFName(rs.getString("fname"));
                    user.setLName(rs.getString("lname"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setUserType(rs.getString("user_type"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }
        }
        return null;
    }

    public boolean findIfExist(String email) throws SQLException {

        if (findByEmail(email) == null)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public List<User> findAll() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()){
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFName(rs.getString("fname"));
                user.setLName(rs.getString("lname"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setUserType(rs.getString("user_type"));
                user.setPassword(rs.getString("password"));
                list.add(user);
            }
        }
        return list;
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE User SET fname = ?, lname = ?, email = ?, phone = ?, user_type = ? WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFName());
            stmt.setString(2, user.getLName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getUserType());
            stmt.setInt(6, user.getUserId());
            String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
            stmt.setString(7, hashed);
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM User WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Connection getConnection() {
        return conn;
    }
    public void setConnection(Connection conn) {
        this.conn = conn;
    }
}