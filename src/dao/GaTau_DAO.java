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
public class GaTau_DAO implements DAOBase<GaTau> {
    
    @Override
    public GaTau getOne(String id) {
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
    
//  Tim kiếm ga tàu bằng mã hoặc tên
    public ArrayList<GaTau> getGaTauByKeyword(String keyword) {
        ArrayList<GaTau> dsGaTau = new ArrayList<>();
        String sql = """
                    SELECT * FROM GaTau WHERE 
                    MaGa LIKE ? 
                    OR TenGa LIKE ?
                                    """;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, "%" + keyword + "%");
            st.setString(2, "%" + keyword + "%");

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                dsGaTau.add(getData(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsGaTau;
    }
     
//  Tìm kiếm ga tàu theo nhiều tiêu chí 
    public ArrayList<GaTau> filter(String maGa, String tenGa, String diaChi, String soDienThoai) {
        ArrayList<GaTau> dsGaTau = new ArrayList<>();
        
        
        String sql = """
                     SELECT * FROM GaTau WHERE 
                        (? IS NULL OR MaGa = ?)
                        AND (? IS NULL OR TenGa LIKE ?)
                        AND (? IS NULL OR DiaChi LIKE ?)
                        AND (? IS NULL OR SoDienThoai LIKE ?)""";
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);

            st.setString(1, maGa.isEmpty() ? null : maGa);
            st.setString(2, maGa);
            st.setString(3, tenGa.isEmpty() ? null : tenGa);
            st.setString(4, "%" + tenGa + "%");
            st.setString(5, diaChi.isEmpty() ? null : diaChi);
            st.setString(6, "%" + diaChi + "%");
            st.setString(7, soDienThoai.isEmpty() ? null : soDienThoai);
            st.setString(8, "%" + soDienThoai + "%");

            ResultSet rs = st.executeQuery();
            
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
    
    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public String getMaxID() {
        String maxID = ""; // giá trị mặc định nếu bảng rỗng
    
        try{
            String sql = "SELECT TOP 1 * FROM GaTau ORDER BY MaGa DESC";
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                maxID = rs.getString("maGa");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxID;
    }
    
    @Override
    public Boolean create(GaTau object) {
        int n = 0;
        String sql = "INSERT INTO GaTau (MaGa, TenGa, DiaChi, SoDienThoai) VALUES (?, ?, ?, ?)";
        
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, object.getMaGa());
            st.setString(2, object.getTenGa());
            st.setString(3, object.getDiaChi());
            st.setString(4, object.getSoDienThoai());
            
            n = st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return n > 0;
    }

    @Override
    public Boolean update(String id, GaTau newObject) {
        int n = 0;
        String sql = """
                        UPDATE GaTau
                        SET tenGa = ?, diaChi = ?, soDienThoai = ?
                        WHERE maGa = ?
                                        """;
    
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, newObject.getTenGa());
            st.setString(2, newObject.getDiaChi());
            st.setString(3, newObject.getSoDienThoai());
            st.setString(4, newObject.getMaGa());
        
            n = st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    
    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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


}