/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.LoaiVe;
import interfaces.DAOBase;
import java.util.ArrayList;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CÔNG HOÀNG
 */
public class LoaiVe_DAO implements DAOBase<LoaiVe>{

    @Override
    public LoaiVe getOne(String id) {
        LoaiVe loaiVe = null;
        String sql = "SELECT * FROM LoaiVe WHERE maLoaiVe = ?";

        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                loaiVe = getData(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(LoaiVe_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loaiVe;
    }
    
        public LoaiVe getLoaiVe(String tenLoai) {
        LoaiVe loaiVe = null;
        String sql = "SELECT * FROM LoaiVe WHERE tenLoaiVe = ?";

        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, tenLoai);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                    loaiVe = getData(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(LoaiVe_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loaiVe;
    }
    
    private LoaiVe getData(ResultSet rs) throws SQLException, Exception {
        String maLoaiVe = rs.getString("maLoaiVe");
        String tenLoaiVe = rs.getString("tenLoaiVe");
        String moTa = rs.getString("moTa");
        Double heSo = rs.getDouble("heSoLoaiVe");
        
        return new LoaiVe(maLoaiVe, tenLoaiVe, moTa, heSo);
    }

    @Override
    public ArrayList<LoaiVe> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(LoaiVe object) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean update(String id, LoaiVe newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
