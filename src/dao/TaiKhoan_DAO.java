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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CÔNG HOÀNG
 */
public class TaiKhoan_DAO implements DAOBase<TaiKhoan> {
    
//   Lấy tài khoản dựa vào tên đăng nhập là số điện thoại
    @Override
    public TaiKhoan getOne(String id) {
        TaiKhoan res = null;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("Select * from TaiKhoan where tenDangNhap = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String maNV = rs.getString("nhanVien");
                String matKhau = rs.getString("matKhau");
                res = new TaiKhoan(matKhau, new NhanVien(maNV));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(TaiKhoan_DAO.class.getName()).log(Level.SEVERE, null, ex);
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }
}
