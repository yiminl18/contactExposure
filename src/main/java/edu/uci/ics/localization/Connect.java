package edu.uci.ics.localization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect implements AutoCloseable {
    Connection connection;

    public Connect(String type) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String userServer = null, pwdServer = null, userLocal = null, pwdLocal = null;

            try {
                BufferedReader br =new BufferedReader(new FileReader("credential.txt"));
                userServer = br.readLine();
                pwdServer = br.readLine();
                userLocal = br.readLine();
                pwdLocal = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(type.equals("local")){
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/Contact?useSSL=false&serverTimezone=PST", userLocal,
                        pwdLocal);
            }
            if(type.equals("server")){
                connection = DriverManager.getConnection(
                        "jdbc:mysql://sensoria-mysql.ics.uci.edu:3306/tippersdb_restored?useSSL=false&serverTimezone=PST",
                        userServer, pwdServer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection get() {
        return connection;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

