/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.Ghe;
import entity.KhoangTau;
import entity.LoaiGhe;
import entity.Tau;
import entity.ToaTau;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author CÔNG HOÀNG
 */
public class Ghe_DAO {
    public Boolean capNhatTrangThaiGheTrong(String maGhe) {
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
    
    public Boolean capNhatTrangThaiGhe(String maGhe) {
        int n = 0;
         String sql = "UPDATE Ghe SET trangThaiGhe = ? WHERE maGhe = ?";
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setInt(1, 2);
            st.setString(2, maGhe);
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    
    /**
     * Lấy danh sách ghế theo mã khoang
     */
    public ArrayList<Ghe> getGheTheoMaKhoang(String maKhoangTau) throws Exception {
    ArrayList<Ghe> dsGhe = new ArrayList<>();
    String sql = "SELECT g.*, lg.tenLoaiGhe, lg.moTa, lg.heSoLoaiGhe, " +
                 "kt.maKhoangTau, kt.soHieuKhoang, kt.soGhe as soGheKhoang, " +
                 "tt.maToaTau, tt.soHieuToa, tt.soKhoangTau, tt.loaiToa " +
                 "FROM Ghe g " +
                 "INNER JOIN LoaiGhe lg ON g.maLoaiGhe = lg.maLoaiGhe " +
                 "INNER JOIN KhoangTau kt ON g.maKhoangTau = kt.maKhoangTau " +
                 "INNER JOIN ToaTau tt ON kt.maToaTau = tt.maToaTau " +
                 "WHERE g.maKhoangTau = ? " +
                 "ORDER BY g.soGhe";
    
    try {
        PreparedStatement pstmt = ConnectDB.conn.prepareStatement(sql);
        pstmt.setString(1, maKhoangTau);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            LoaiGhe loaiGhe = new LoaiGhe();
            loaiGhe.setMaLoaiGhe(rs.getString("maLoaiGhe"));
            loaiGhe.setTenLoaiGhe(rs.getString("tenLoaiGhe"));
            loaiGhe.setMoTa(rs.getString("moTa"));
            loaiGhe.setHeSoLoaiGhe(rs.getFloat("heSoLoaiGhe"));
            
            ToaTau toaTau = new ToaTau();
            toaTau.setMaToa(rs.getString("maToaTau"));
            toaTau.setSoHieuToa(rs.getInt("soHieuToa"));
            toaTau.setSoKhoangTau(rs.getInt("soKhoangTau"));
            toaTau.setLoaiToa(rs.getString("loaiToa"));
            
            KhoangTau khoang = new KhoangTau();
            khoang.setMaKhoangTau(rs.getString("maKhoangTau"));
            khoang.setSoHieuKhoang(rs.getInt("soHieuKhoang"));
            khoang.setSucChua(rs.getInt("soGheKhoang"));
            khoang.setToaTau(toaTau);
            
            Ghe ghe = new Ghe();
            ghe.setMaGhe(rs.getString("maGhe"));
            ghe.setSoGhe(rs.getInt("soGhe"));
            ghe.setLoaiGhe(loaiGhe);
            ghe.setKhoangTau(khoang);
            
            dsGhe.add(ghe);
        }
    } catch(Exception e) {
        e.printStackTrace();
        throw e;
    }
    
    return dsGhe;
}
    
    /**
     * Lấy danh sách tất cả ghế của toa (qua các khoang)
     */
    // File: Ghe_DAO.java - Sửa method getGheTheoMaToa

public ArrayList<Ghe> getGheTheoMaToa(String maToaTau) throws Exception {
    ArrayList<Ghe> dsGhe = new ArrayList<>();
    String sql = "SELECT g.*, lg.tenLoaiGhe, lg.moTa, lg.heSoLoaiGhe, " +
                 "kt.maKhoangTau, kt.soHieuKhoang, kt.soGhe as soGheKhoang, " +
                 "tt.maToaTau, tt.soHieuToa, tt.soKhoangTau, tt.loaiToa " +
                 "FROM Ghe g " +
                 "INNER JOIN LoaiGhe lg ON g.maLoaiGhe = lg.maLoaiGhe " +
                 "INNER JOIN KhoangTau kt ON g.maKhoangTau = kt.maKhoangTau " +
                 "INNER JOIN ToaTau tt ON kt.maToaTau = tt.maToaTau " +
                 "WHERE kt.maToaTau = ? " +
                 "ORDER BY kt.soHieuKhoang, g.soGhe";
    
    try {
        PreparedStatement pstmt = ConnectDB.conn.prepareStatement(sql);
        pstmt.setString(1, maToaTau);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            // Tạo đối tượng LoaiGhe
            LoaiGhe loaiGhe = new LoaiGhe();
            loaiGhe.setMaLoaiGhe(rs.getString("maLoaiGhe"));
            loaiGhe.setTenLoaiGhe(rs.getString("tenLoaiGhe"));
            loaiGhe.setMoTa(rs.getString("moTa"));
            loaiGhe.setHeSoLoaiGhe(rs.getFloat("heSoLoaiGhe"));
            
            // Tạo đối tượng ToaTau ĐẦY ĐỦ
            ToaTau toaTau = new ToaTau();
            toaTau.setMaToa(rs.getString("maToaTau"));
            toaTau.setSoHieuToa(rs.getInt("soHieuToa"));
            toaTau.setSoKhoangTau(rs.getInt("soKhoangTau"));
            toaTau.setLoaiToa(rs.getString("loaiToa"));
            
            // Tạo đối tượng KhoangTau với ToaTau đầy đủ
            KhoangTau khoang = new KhoangTau();
            khoang.setMaKhoangTau(rs.getString("maKhoangTau"));
            khoang.setSoHieuKhoang(rs.getInt("soHieuKhoang"));
            khoang.setSucChua(rs.getInt("soGheKhoang"));
            khoang.setToaTau(toaTau); // ← SET ToaTau vào KhoangTau
            
            // Tạo đối tượng Ghe
            Ghe ghe = new Ghe();
            ghe.setMaGhe(rs.getString("maGhe"));
            ghe.setSoGhe(rs.getInt("soGhe"));
            ghe.setLoaiGhe(loaiGhe);
            ghe.setKhoangTau(khoang);
            
            dsGhe.add(ghe);
        }
    } catch(Exception e) {
        e.printStackTrace();
        throw e;
    }
    
    return dsGhe;
}
    public Ghe getData(ResultSet rs) {
    try {
        Ghe g = new Ghe();
        g.setMaGhe(rs.getString("maGhe"));
        g.setSoGhe(rs.getInt("soGhe"));

        // Loại ghế đầy đủ thông tin
        LoaiGhe lg = new LoaiGhe();
        lg.setMaLoaiGhe(rs.getString("maLoaiGhe"));
        lg.setTenLoaiGhe(rs.getString("tenLoaiGhe"));
        lg.setMoTa(rs.getString("moTa"));
        lg.setHeSoLoaiGhe(rs.getDouble("heSoLoaiGhe"));
        g.setLoaiGhe(lg);

        // Tạo ToaTau đầy đủ thông tin
        ToaTau tt = new ToaTau();
        tt.setMaToa(rs.getString("maToaTau"));
        tt.setSoHieuToa(rs.getInt("soHieuToa"));
        tt.setSoKhoangTau(rs.getInt("soKhoangTau"));
        
        // Tạo Tau (chỉ có maTau)
        Tau tau = new Tau(rs.getString("maTau"));
        tt.setTau(tau);

        // Khoang tàu với ToaTau đầy đủ
        KhoangTau kt = new KhoangTau();
        kt.setMaKhoangTau(rs.getString("maKhoangTau"));
        kt.setSoHieuKhoang(rs.getInt("soHieuKhoang"));
        kt.setSucChua(rs.getInt("soGhe"));
        kt.setToaTau(tt);
        
        g.setKhoangTau(kt);

        return g;

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
}
