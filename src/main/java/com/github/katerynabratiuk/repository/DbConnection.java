package com.github.katerynabratiuk.repository;


import com.github.katerynabratiuk.config.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DbConfig.get("db.url"),
                DbConfig.get("db.username"),
                DbConfig.get("db.password")
        );
    }
}