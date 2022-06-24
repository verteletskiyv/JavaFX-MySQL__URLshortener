package com.study.urlshortener_fx;

import java.sql.*;

public class DB {

    private final String HOST = "localhost";
    private final String PORT = "3306";
    private final String DB_NAME = "diploma";
    private final String LOGIN = "root";
    private final String PASS = "";

    private Connection dbConn = null;

    private Connection getDBConnection() throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:mysql://" +HOST+ ":" +PORT+ "/" +DB_NAME;
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConn = DriverManager.getConnection(connStr, LOGIN, PASS);
        return dbConn;
    }

    public void isConnected() throws SQLException, ClassNotFoundException {
        dbConn = getDBConnection();
        System.out.println(dbConn.isValid(1000));
    }

    public void addLink(String url_long, String url_short) {
        String sql = "INSERT INTO `links` (`url_full`, `url_short`) VALUES (?, ?)";

        try (PreparedStatement prSt = getDBConnection().prepareStatement(sql)) {
            prSt.setString(1, url_long);
            prSt.setString(2, url_short);
            prSt.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getLinks() {
        String sql = "SELECT * FROM `links`";
        Statement statement = null;
        try {
            statement = getDBConnection().createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isExistsLink(String url_short) {
        String sql = "SELECT `id` FROM `links` WHERE `url_short` = ?";
        try (PreparedStatement prSt = getDBConnection().prepareStatement(sql)) {
            prSt.setString(1, url_short);
            ResultSet res = prSt.executeQuery();
            return res.next();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
