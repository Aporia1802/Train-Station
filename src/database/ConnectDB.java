/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.*;

/**
 *
 * @author CÔNG HOÀNG
 */
public class ConnectDB {
    public static Connection conn = null;

    public static void connect() throws SQLException {

        String url = "jdbc:sqlserver://localhost:1433;databasename=QuanLyBanVeTau";
        String user = "sa";
        String password = "123456789"; 

        conn = DriverManager.getConnection(url, user, password);

    }

    public static void disconnect() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }
}
