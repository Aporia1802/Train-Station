/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.Tau;
import entity.ToaTau;
import interfaces.DAOBase;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author CÔNG HOÀNG
 */
public class ToaTau_DAO implements DAOBase<ToaTau>{
    
    public ArrayList<ToaTau> getToaTauTheoTau(String maTau) {
        ArrayList<ToaTau> dsToaTau = new ArrayList<>();
        String sql = "SELECT * FROM ToaTau WHERE maTau = ? ORDER BY soHieuToa";

        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, maTau);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ToaTau toa = getData(rs);
                if (toa != null) dsToaTau.add(toa);
            }
        } catch (Exception e) {
        e.printStackTrace();
    }

    return dsToaTau;
    }
    
     /**
     * Lấy danh sách toa tàu theo mã tàu
     * @param maTau
     * @return 
     * @throws java.lang.Exception 
     */
    public ArrayList<ToaTau> getByTau(String maTau) throws Exception {
        ArrayList<ToaTau> dsToa = new ArrayList<>();
        String sql = "SELECT tt.*, " +
                    "FROM ToaTau tt " +
                    "WHERE tt.maTau = ? " +
                    "ORDER BY tt.soHieuToa";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, maTau);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                // Tạo ToaTau
                ToaTau toa = new ToaTau();
                toa.setMaToa(rs.getString("maToa"));
                toa.setSoKhoangTau(rs.getInt("soKhoangTau"));
                toa.setSoHieuToa(rs.getInt("soHieuToa"));
                toa.setLoaiToa(rs.getString("loaiToa"));
                
                dsToa.add(toa);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return dsToa;
    }
    
    public ToaTau getData(ResultSet rs) {
        try {
            ToaTau toa = new ToaTau();
            toa.setMaToa(rs.getString("maToaTau"));
            toa.setSoKhoangTau(rs.getInt("soKhoangTau"));
            toa.setSoHieuToa(rs.getInt("soHieuToa"));
            toa.setTau(new Tau(rs.getString("maTau")));
            return toa;
        } catch (Exception e) {
            e.printStackTrace();
        return null;
        }
    }

    @Override
    public ToaTau getOne(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<ToaTau> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(ToaTau object) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean update(String id, ToaTau newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
