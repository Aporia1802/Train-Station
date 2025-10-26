/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.HoaDonTra;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author CÔNG HOÀNG
 */
public class HoaDonTra_DAO {
    public String getMaxID() {
        String maxID = ""; // giá trị mặc định nếu bảng rỗng
    
        try{
            String sql = "SELECT TOP 1 * FROM HoaDonTra ORDER BY MaHDT DESC";
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                maxID = rs.getString("maHDT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxID;
    }
    
    public Boolean create(HoaDonTra object) {
        int n = 0;
        String sql = "INSERT INTO HoaDonTra (MaHDT, ngayTra, maVe, maNV) VALUES (?, ?, ?, ?)";
        
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, object.getMaHoaDonTra());
            st.setTimestamp(2, java.sql.Timestamp.valueOf(object.getNgayTra()));
            st.setString(3, object.getVe().getMaVe());
            st.setString(4, object.getNhanVien().getMaNV());
            
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return n > 0;
    }
}
