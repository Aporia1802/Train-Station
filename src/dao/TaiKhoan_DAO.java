/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.NhanVien;
import entity.TaiKhoan;
import interfaces.DAOBase;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 *
 * @author CÔNG HOÀNG
 */
public class TaiKhoan_DAO implements DAOBase<TaiKhoan> {
    
//   Lấy tài khoản dựa vào tên đăng nhập
    @Override
    public TaiKhoan getOne(String id) {
        TaiKhoan res = null;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("Select * from TaiKhoan where tenDangNhap = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String maNV = rs.getString("maNV");
                String matKhau = rs.getString("matKhau");
                res = new TaiKhoan(matKhau, new NhanVien(maNV));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } 
        return res;
    }

    @Override
    public ArrayList<TaiKhoan> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(TaiKhoan object) {
        int n = 0;
        String sql = "INSERT INTO TaiKhoan (tenDangNhap, matKhau, maNV) VALUES (?, ?, ?)";
    
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, object.getTenDangNhap());
            st.setString(2, object.getMatKhau());
            st.setString(3, object.getNhanVien().getMaNV());
        
            n = st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, TaiKhoan newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public Boolean updatePassword(String taiKhoan, String matKhauMoi) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("update taiKhoan set "
                    + "matKhau = ? where tenDangNhap = ?");
            st.setString(1, matKhauMoi);
            st.setString(2, taiKhoan);

            n = st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return n > 0;
    }
}
