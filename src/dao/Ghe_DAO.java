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
    
    public ArrayList<Ghe> getGheTheoKhoangTau(String maKhoangTau) {
    ArrayList<Ghe> dsGhe = new ArrayList<>();
   
    String sql = """
        SELECT g.*, lg.tenLoaiGhe, lg.moTa, lg.heSoLoaiGhe,
               kt.maKhoangTau, kt.soHieuKhoang, kt.soGhe,
               tt.maToaTau, tt.soHieuToa, tt.soKhoangTau, tt.maTau
        FROM Ghe g
        JOIN LoaiGhe lg ON g.maLoaiGhe = lg.maLoaiGhe
        JOIN KhoangTau kt ON g.maKhoangTau = kt.maKhoangTau
        JOIN ToaTau tt ON kt.maToaTau = tt.maToaTau
        WHERE g.maKhoangTau = ?
        ORDER BY g.soGhe
    """;

    try (PreparedStatement st = ConnectDB.conn.prepareStatement(sql)) {
        st.setString(1, maKhoangTau);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            Ghe g = getData(rs);
            if (g != null) dsGhe.add(g);
        }

    } catch (Exception e) {
        e.printStackTrace();
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

        // ← SỬA: Tạo ToaTau đầy đủ thông tin
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
        kt.setToaTau(tt); // 
        
        g.setKhoangTau(kt);

        return g;

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
}
