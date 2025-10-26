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
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public ArrayList getAll() {
        ArrayList<Tau> dsTau = new ArrayList();

        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("Select * from GaTau");
            while (rs.next()) {
                if(rs != null) {
                    dsTau.add(getData(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsTau;
    }
    
    public Tau getData(ResultSet rs) throws SQLException, Exception {
       // Lấy dữ liệu từng cột trong ResultSet
       String maTau = rs.getString("maTau");
       String tenTau = rs.getString("tenTau");
       int soToaTau = rs.getInt("soToaTau");
       int sucChua = rs.getInt("sucChua");
       LocalDate ngayHoatDong = rs.getDate("ngayHoatDong").toLocalDate();

       // Nếu cột trangThai trong DB lưu là chuỗi (VD: 'DANG_HOAT_DONG', 'BAO_TRI', 'NGUNG_HOAT_DONG')
       TrangThaiTau trangThai = TrangThaiTau.valueOf(rs.getString("trangThai"));

       // Trả về đối tượng Tau
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
