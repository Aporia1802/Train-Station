/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.HoaDon;
import interfaces.DAOBase;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author CÔNG HOÀNG
 */
public class HoaDon_DAO implements DAOBase<HoaDon>{

    @Override
    public HoaDon getOne(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<HoaDon> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String generateID() {
         String prefix = "HD";
    String sql = "SELECT MAX(maHoaDon) FROM HoaDon";
    String newId = "";

    try {
        PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            String lastId = rs.getString(1); // VD: HD0005
            if (lastId != null) {
                int number = Integer.parseInt(lastId.substring(prefix.length())); // -> 5
                number++;
                newId = prefix + String.format("%04d", number); // -> HD0006
            } else {
                newId = prefix + "0001";
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        newId = prefix + "0001";
    }

    return newId;
    }

    @Override
    public Boolean create(HoaDon object) {
        int n = 0;
        String sql = "INSERT INTO HoaDon (maHoaDon, maNhanVien, maKhachHang, ngayLapHoaDon, VAT, maKhuyenMai, tongTien, thanhTien) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, object.getMaHoaDon());
            st.setString(2, object.getNhanVien().getMaNV());
            st.setString(3, object.getKhachHang().getMaKH());
            st.setTimestamp(4, java.sql.Timestamp.valueOf(object.getNgayLapHoaDon())); // LocalDateTime
            st.setDouble(5, object.getVAT());
            st.setString(6, object.getKhuyenMai().getMaKhuyenMai());
            st.setDouble(7, object.getTongTien());
            st.setDouble(8, object.getThanhTien());

            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    return n > 0;
    }

    @Override
    public Boolean update(String id, HoaDon newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
