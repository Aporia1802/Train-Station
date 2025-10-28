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
    public Boolean capNhatTrangThaiGhe(String maGhe) {
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
    
    public ArrayList<Ghe> getGheTheoKhoangTau(String maKhoangTau) {
         ArrayList<Ghe> dsGhe = new ArrayList<>();
        String sql = "SELECT * FROM Ghe WHERE maKhoangTau = ? ORDER BY soGhe";

        try  {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
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
            g.setLoaiGhe(new LoaiGhe(rs.getString("maLoaiGhe")));
            g.setKhoangTau(new KhoangTau(rs.getString("maKhoangTau")));
            return g;
        } catch (Exception e) {
            e.printStackTrace();
        return null;
        }
    }
}
