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
    
    /**
     * Lấy danh sách toa tàu theo mã tàu
     */
    public ArrayList<ToaTau> getToaTauTheoMaTau(String maTau) throws Exception {
        ArrayList<ToaTau> dsToa = new ArrayList<>();
        String sql = "SELECT * FROM ToaTau WHERE maTau = ? ORDER BY soHieuToa";
        
        try  {
            PreparedStatement pstmt = ConnectDB.conn.prepareStatement(sql);
            pstmt.setString(1, maTau);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ToaTau toa = new ToaTau();
                toa.setMaToa(rs.getString("maToaTau"));
                toa.setSoKhoangTau(rs.getInt("soKhoangTau"));
                toa.setSoHieuToa(rs.getInt("soHieuToa"));
                
                // Lấy loaiToa và kiểm tra null
                String loaiToa = rs.getString("loaiToa");
                if (loaiToa == null) {
                    System.err.println("WARNING: loaiToa is NULL for maToaTau: " + toa.getMaToa());
                    loaiToa = "NGOI_MEM"; // Giá trị mặc định
                }
                toa.setLoaiToa(loaiToa);
                
                // Lấy thông tin tàu
                Tau tau = new Tau();
                tau.setMaTau(rs.getString("maTau"));
                toa.setTau(tau);
                
                dsToa.add(toa);
                
                // Debug log
                System.out.println("Loaded ToaTau: " + toa.getMaToa() + 
                                 ", soHieuToa: " + toa.getSoHieuToa() + 
                                 ", loaiToa: " + toa.getLoaiToa());
            }
        } catch (SQLException e) {
            System.err.println("Error in getToaTauTheoMaTau: " + e.getMessage());
            throw e;
        }
        
        if (dsToa.isEmpty()) {
            System.err.println("WARNING: No ToaTau found for maTau: " + maTau);
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
