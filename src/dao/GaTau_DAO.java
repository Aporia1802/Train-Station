/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import static database.ConnectDB.conn;
import entity.GaTau;
import interfaces.DAOBase;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author CÔNG HOÀNG
 */
public class GaTau_DAO implements DAOBase{

    @Override
    public Object getOne(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList getAll() {
        ArrayList<GaTau> dsGaTau = new ArrayList();

        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("Select * from GaTau");
            while (rs.next()) {
                if(rs != null) {
                    dsGaTau.add(getData(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsGaTau;
    }
    
    public ArrayList<GaTau> getGaTauByKeyword(String keyword) {
        ArrayList<GaTau> dsGaTau = new ArrayList<>();
        String sql = """
                    SELECT * FROM GaTau WHERE 
                    MaGa LIKE ? 
                    OR TenGa LIKE ?
                                    """;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dsGaTau.add(getData(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsGaTau;
    }
     
    
    public ArrayList<GaTau> filter(String maGa, String tenGa, String diaChi, String soDienThoai) {
        ArrayList<GaTau> dsGaTau = new ArrayList<>();
        
        
        String sql = """
                     SELECT * FROM GaTau WHERE 
                        (? IS NULL OR MaGa = ?)
                        AND (? IS NULL OR TenGa LIKE ?)
                        AND (? IS NULL OR DiaChi LIKE ?)
                        AND (? IS NULL OR SoDienThoai LIKE ?)""";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, maGa.isEmpty() ? null : maGa);
            ps.setString(2, maGa);
            ps.setString(3, tenGa.isEmpty() ? null : tenGa);
            ps.setString(4, "%" + tenGa + "%");
            ps.setString(5, diaChi.isEmpty() ? null : diaChi);
            ps.setString(6, "%" + diaChi + "%");
            ps.setString(7, soDienThoai.isEmpty() ? null : soDienThoai);
            ps.setString(8, "%" + soDienThoai + "%");

            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                if (rs != null) {
                    dsGaTau.add(getData(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsGaTau;
    }
    
    public GaTau getData(ResultSet rs) throws SQLException, Exception{
        GaTau gaTau = null;
        
        String maGa = rs.getString("maGa");
        String tenGa = rs.getString("tenGa");
        String diaChi = rs.getString("diaChi");
        String soDienThoai = rs.getString("soDienThoai");
        
        gaTau = new GaTau(maGa, tenGa, diaChi, soDienThoai);
        
        return gaTau;
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
}
