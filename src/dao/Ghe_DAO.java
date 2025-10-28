/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.Ghe;
import entity.KhoangTau;
import entity.LoaiGhe;
import enums.TrangThaiGhe;
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
        SELECT g.*, lg.tenLoaiGhe, lg.moTa, lg.heSoGhe
        FROM Ghe g
        JOIN LoaiGhe lg ON g.maLoaiGhe = lg.maLoaiGhe
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
        g.setTrangThaiGhe(TrangThaiGhe.fromInt(rs.getInt("trangThaiGhe")));

        // Loại ghế đầy đủ thông tin
        LoaiGhe lg = new LoaiGhe();
        lg.setMaLoaiGhe(rs.getString("maLoaiGhe"));
        lg.setTenLoaiGhe(rs.getString("tenLoaiGhe"));
        lg.setMoTa(rs.getString("moTa"));
        lg.setHeSoLoaiGhe(rs.getDouble("heSoGhe"));

        g.setLoaiGhe(lg);

        // Khoang tàu
        KhoangTau kt = new KhoangTau();
        kt.setMaKhoangTau(rs.getString("maKhoangTau"));
        g.setKhoangTau(kt);

        return g;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
