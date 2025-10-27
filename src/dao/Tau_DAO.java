/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import static database.ConnectDB.conn;
import entity.Tau;
import enums.TrangThaiTau;
import interfaces.DAOBase;
import java.sql.Statement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.SQLException;
/**
 *
 * @author CÔNG HOÀNG
 */
public class Tau_DAO implements DAOBase<Tau>{

    
    @Override
    public Tau getOne(String id) {
      Tau tau = null;

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("Select * from Tau where maTau = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String tenTau = rs.getString("tenTau");
                int soToaTau = rs.getInt("soToaTau");
                int sucChua = rs.getInt("sucChua");
                LocalDate ngayHoatDong = rs.getDate("ngayHoatDong").toLocalDate();
                TrangThaiTau trangThai = TrangThaiTau.valueOf(rs.getString("trangThai"));
                tau = new Tau(id, tenTau, soToaTau, sucChua, ngayHoatDong, trangThai);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tau;
    }


     @Override
    public ArrayList<Tau> getAll() {
        ArrayList<Tau> dsTau = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Tau";
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                dsTau.add(getData(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsTau;
    }
    
  public Tau getData(ResultSet rs) throws SQLException, Exception {
    String maTau = rs.getString("maTau");
    String tenTau = rs.getString("tenTau");
    int soToaTau = rs.getInt("soToaTau");
    int sucChua = rs.getInt("sucChua");
    LocalDate ngayHoatDong = null;
    java.sql.Date d = rs.getDate("ngayHoatDong");
    if (d != null) ngayHoatDong = d.toLocalDate();

    String trangThaiStr = rs.getString("trangThai");
    TrangThaiTau trangThai = TrangThaiTau.fromDisplay(trangThaiStr);

    if (trangThai == null) {
        // gán mặc định để không làm vỡ luồng xử lý
        trangThai = TrangThaiTau.HOAT_DONG;
    }

    return new Tau(maTau, tenTau, soToaTau, sucChua, ngayHoatDong, trangThai);
}



    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Boolean create(Tau object) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean update(String id, Tau newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
