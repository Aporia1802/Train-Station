/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.NhanVien;
import interfaces.DAOBase;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author CÔNG HOÀNG
 */
public class NhanVien_DAO implements DAOBase<NhanVien>{
    
    @Override
    public NhanVien getOne(String id) {
        NhanVien nhanVien = null;

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("Select * from NhanVien where maNV = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String tenNV = rs.getString("tenNV");
                Boolean gioiTinh = rs.getBoolean("gioiTinh");
                LocalDate ngaySinh = rs.getDate("ngaySinh").toLocalDate();
                String email = rs.getString("email");
                String soDienThoai = rs.getString("soDienThoai");
                String cccd = rs.getString("cccd");
                String chucVu = rs.getString("chucVu");
                Boolean trangThai = rs.getBoolean("trangThai");
                String diaChi = "";

                nhanVien = new NhanVien(id, tenNV, gioiTinh, ngaySinh, email, soDienThoai, cccd, diaChi, chucVu, trangThai);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nhanVien;
    }

    @Override
    public ArrayList<NhanVien> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(NhanVien object) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean update(String id, NhanVien newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
