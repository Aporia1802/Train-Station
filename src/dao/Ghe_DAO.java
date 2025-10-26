/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import java.sql.*;

/**
 *
 * @author CÔNG HOÀNG
 */
public class Ghe_DAO {
    public Boolean capNhatTrangThaiGhe(String maGhe) {
        int n = 0;
         String sql = "UPDATE Ghe SET trangThaiGhe = ? WHERE maGhe = ?";
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setInt(1, 1);
            st.setString(2, maGhe);
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }
}
