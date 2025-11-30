/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.HoaDonDoi;
import java.sql.*;

/**
 *
 * @author CÔNG HOÀNG
 */
public class HoaDonDoi_DAO {
    public String generateID() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaHoaDonDoi, 4, LEN(MaHoaDonDoi)) AS INT)) " +
                    "FROM HoaDonDoi";
        
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int maxNum = rs.getInt(1);
                return String.format("HDD%03d", maxNum + 1);
            }
            return "HDD001";
            
        } catch (SQLException e) {
            e.printStackTrace();
            return "HDD001";
        }
    }
    
    public boolean insert(HoaDonDoi hdd) {
        String sql = "INSERT INTO HoaDonDoi (MaHoaDonDoi, NgayDoi, MaNhanVien, MaVe, PhiDoi) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement stmt = ConnectDB.conn.prepareStatement(sql);
            stmt.setString(1, hdd.getMaHoaDonDoi());
            stmt.setTimestamp(2, Timestamp.valueOf(hdd.getNgayDoi()));
            stmt.setString(3, hdd.getNhanVien().getMaNV());
            stmt.setString(4, hdd.getVe().getMaVe());
            stmt.setDouble(5, hdd.getPhiDoi());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
