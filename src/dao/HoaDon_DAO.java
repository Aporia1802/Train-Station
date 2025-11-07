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
        String prefix = "HD-";
        String sql = "SELECT MAX(maHoaDon) FROM HoaDon";
        String newId = prefix + "00001";

        try (PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString(1);
                if (lastId != null && lastId.startsWith(prefix)) {
                    String numberPart = lastId.substring(prefix.length());
                    try {
                        int number = Integer.parseInt(numberPart);
                        number++;
                        newId = prefix + String.format("%05d", number);
                    } catch (NumberFormatException e) {
                        newId = prefix + "00001";
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
            st.setTimestamp(4, java.sql.Timestamp.valueOf(object.getNgayLapHoaDon()));
            st.setDouble(5, object.getVAT());
        
            if (object.getKhuyenMai() != null) {
                st.setString(6, object.getKhuyenMai().getMaKhuyenMai());
            } else {
                st.setNull(6, java.sql.Types.VARCHAR); // Hoặc setString(6, null)
            }
        
            st.setDouble(7, object.getTongTien());
            st.setDouble(8, object.getThanhTien());

            n = st.executeUpdate();
            st.close(); // ← Nên thêm
        
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
