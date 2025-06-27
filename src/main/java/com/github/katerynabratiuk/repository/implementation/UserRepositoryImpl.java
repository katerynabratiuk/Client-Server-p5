package com.github.katerynabratiuk.repository.implementation;

import com.github.katerynabratiuk.entity.User;
import com.github.katerynabratiuk.repository.DbConnection;
import com.github.katerynabratiuk.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public User get(String login) {
        try(Connection connection = DbConnection.getConnection()) {

            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Store_user WHERE login = ?");
            pstmt.setString(1, login);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next())
            {
                return new User(rs.getString("login"), rs.getString("password"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
