/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.ChuyenTau;
import entity.GaTau;
import entity.Tau;
import entity.TuyenDuong;
import enums.TrangThaiTau;
import interfaces.DAOBase;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author CÔNG HOÀNG
 */
public class ChuyenTau_DAO implements DAOBase{

    @Override
    public Object getOne(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList getAll() {
        ArrayList<ChuyenTau> dsChuyenTau = new ArrayList();

        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("Select * from ChuyenTau ct"
                                            + "JOIN Tau t ON t.maTau =  ct.maTau "
                                            + "JOIN TuyenDuong td ON td.maTuyenDuong = ct.maTuyenDuong");
            while (rs.next()) {
                if(rs != null) {
                    dsChuyenTau.add(getData(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsChuyenTau;
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean update(String id, Object newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    private ChuyenTau getData(ResultSet rs) throws SQLException, Exception { 
        ChuyenTau ct = new ChuyenTau();
        Tau tau = new Tau();
        TuyenDuong td = new TuyenDuong();
      
        // Lấy thông tin Tàu
        tau.setMaTau(rs.getString("maTau"));
        tau.setTenTau(rs.getString("tenTau"));
        tau.setSoToaTau(rs.getInt("soToaTau"));
        tau.setSucChua(rs.getInt("sucChua"));
        tau.setNgayHoatDong(rs.getDate("ngayHoatDong").toLocalDate());
        tau.setTrangThai(TrangThaiTau.valueOf(rs.getString("trangThai").toUpperCase()));

        // Lấy thông tin Tuyến đường
        td.setMaTuyenDuong(rs.getString("maTuyenDuong"));
        td.setQuangDuong(rs.getFloat("quangDuong"));
        td.setSoTienMotKm(rs.getFloat("soTienMotKm"));
        td.setGaDi(new GaTau(rs.getString("gaDi")));
        td.setGaDen(new GaTau(rs.getString("gaDen")));

        // Lấy thông tin Chuyến tàu
        ct.setMaChuyenTau(rs.getString("maChuyenTau"));
        ct.setThoiGianDi(rs.getTimestamp("thoiGianDi").toLocalDateTime());
        ct.setThoiGianDen(rs.getTimestamp("thoiGianDeb").toLocalDateTime());
        ct.setSoGheDaDat(rs.getInt("soGheDaDat"));
        ct.setSoGheConTrong(rs.getInt("soGheConTrong"));
        ct.setTau(tau);
        ct.setTuyenDuong(td);
        
        return ct;
    }
}
