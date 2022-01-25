package edu.uci.ics.localization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect implements AutoCloseable {
    Connection connection;

    public Connect(String type, String server) {
        if(server.equals("mysql")){
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
        else if(server.equals("postgres")){
            try {
                Class.forName("org.postgresql.Driver");
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try{
                if(type.equals("server")){
                    connection = DriverManager
                            .getConnection("jdbc:postgresql://wideid.ics.uci.edu:9001/uci_deid_wifi_connectivity",
                                    "postgres", "privacyisimportant");

                }
                else if(type.equals("local")){
                    System.out.println("Not available for now!");
                    connection = DriverManager
                            .getConnection("jdbc:postgresql://localhost:5432/testdb",
                                    "postgres", "privacyisimportant");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                System.exit(0);
            }
            System.out.println("Connect to PostgreSQL successfully");
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

