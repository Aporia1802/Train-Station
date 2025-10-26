/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.ChuyenTau;
import entity.GaTau;
import entity.Tau;
import entity.TuyenDuong;
import enums.TrangThaiTau;
import interfaces.DAOBase;
import java.util.ArrayList;
import java.sql.*;
import java.time.LocalDate;

/**
 *
 * @author CÔNG HOÀNG
 */
public class ChuyenTau_DAO implements DAOBase{

    @Override
    public Object getOne(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList getAll() {
        ArrayList<ChuyenTau> dsChuyenTau = new ArrayList();

        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("Select *, gdi.maGa AS maGaDi, gdi.tenGa AS tenGaDi," +
                                            "gden.maGa AS maGaDen, gden.tenGa AS tenGaDen from ChuyenTau ct"
                                            + " JOIN Tau t ON t.maTau = ct.maTau"
                                            + " JOIN TuyenDuong td ON td.maTuyenDuong = ct.maTuyenDuong"
                                            + " JOIN GaTau gdi ON gdi.maGa = td.gaDi"
                                            + " JOIN GaTau gden ON gden.maGa = td.gaDen");
            while (rs.next()) {
                if(rs != null) {
                    dsChuyenTau.add(getData(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsChuyenTau;
    }
    
    public ArrayList<ChuyenTau> filter(String tenGaDi, String tenGaDen, LocalDate ngayDi) throws Exception {
        ArrayList<ChuyenTau> dsChuyenTau = new ArrayList();
        String sql = """
                        SELECT *, gdi.maGa AS maGaDi, gdi.tenGa AS tenGaDi, 
                        gden.maGa AS maGaDen, gden.tenGa AS tenGaDen 
                        FROM ChuyenTau ct
                        JOIN Tau t ON ct.maTau = t.maTau
                        JOIN TuyenDuong td ON ct.maTuyenDuong = td.maTuyenDuong
                        JOIN GaTau gdi ON td.gaDi = gdi.maGa
                        JOIN GaTau gden ON td.gaDen = gden.maGa
                        WHERE 
                        (gdi.tenGa = ?)
                        AND (gden.tenGa = ?)
                        AND (CAST(ct.thoiGianDi AS DATE) = ?)
                    """;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, tenGaDi);
            st.setString(2, tenGaDen);
            st.setDate(3, java.sql.Date.valueOf(ngayDi)); 
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                dsChuyenTau.add(getData(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsChuyenTau;
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
    
    
    public boolean capNhatSoGheKhiHuy(String maChuyenTau) {
        int n = 0;
        String sql = """
                    UPDATE ChuyenTau
                    SET soGheDaDat = soGheDaDat - 1,
                    soGheConTrong = soGheConTrong + 1
                    WHERE maChuyenTau = ?
                    AND soGheDaDat > 0
                                        """;
            try {
                PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
                st.setString(1, maChuyenTau);
                n = st.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        return n > 0;
    }

    
    private ChuyenTau getData(ResultSet rs) throws SQLException, Exception { 
        ChuyenTau ct = new ChuyenTau();
        Tau tau = new Tau();
        TuyenDuong td = new TuyenDuong();
        GaTau gaDi = new GaTau();
        GaTau gaDen = new GaTau();
      
        // Lấy thông tin Tàu
        tau.setMaTau(rs.getString("maTau"));
        tau.setTenTau(rs.getString("tenTau"));
        tau.setSoToaTau(rs.getInt("soToaTau"));
        tau.setSucChua(rs.getInt("sucChua"));
        tau.setNgayHoatDong(rs.getDate("ngayHoatDong").toLocalDate());
        tau.setTrangThai(TrangThaiTau.fromInt(rs.getInt("trangThai")));
        
        // Thông tin Ga đi
        gaDi.setMaGa(rs.getString("maGaDi"));
        gaDi.setTenGa(rs.getString("tenGaDi"));

        // Thông tin Ga đến
        gaDen.setMaGa(rs.getString("maGaDen"));
        gaDen.setTenGa(rs.getString("tenGaDen"));

        // Lấy thông tin Tuyến đường
        td.setMaTuyenDuong(rs.getString("maTuyenDuong"));
        td.setQuangDuong(rs.getFloat("quangDuong"));
        td.setSoTienMotKm(rs.getFloat("soTienMotKm"));
        td.setGaDi(gaDi);
        td.setGaDen(gaDen);

        // Lấy thông tin Chuyến tàu
        ct.setMaChuyenTau(rs.getString("maChuyenTau"));
        ct.setThoiGianDi(rs.getTimestamp("thoiGianDi").toLocalDateTime());
        ct.setThoiGianDen(rs.getTimestamp("thoiGianDen").toLocalDateTime());
        ct.setSoGheDaDat(rs.getInt("soGheDaDat"));
        ct.setSoGheConTrong(rs.getInt("soGheConTrong"));
        ct.setTau(tau);
        ct.setTuyenDuong(td);
        
        return ct;
    }
}
