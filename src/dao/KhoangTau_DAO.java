/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.KhoangTau;
import entity.ToaTau;
import java.util.ArrayList;
import java.sql.*;
/**
 *
 * @author CÔNG HOÀNG
 */
public class KhoangTau_DAO {
    public ArrayList<KhoangTau> getKhoangTauTheoToa(String maToaTau) {
        ArrayList<KhoangTau> dsKhoangTau = new ArrayList<>();
        String sql = "SELECT * FROM KhoangTau WHERE maToaTau = ? ORDER BY soHieuKhoang";

        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, maToaTau);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                KhoangTau kt = getKhoangTauFromResultSet(rs);
                if (kt != null) dsKhoangTau.add(kt);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsKhoangTau;  
    }
    
    public KhoangTau getKhoangTauFromResultSet(ResultSet rs) {
        try {
            KhoangTau kt = new KhoangTau();
            kt.setMaKhoangTau(rs.getString("maKhoangTau"));
            kt.setSoHieuKhoang(rs.getInt("soHieuKhoang"));
            kt.setSucChua(rs.getInt("soGhe"));
            kt.setToaTau(new ToaTau(rs.getString("maToaTau")));
            return kt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
