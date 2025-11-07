package dao;

import database.ConnectDB;
import entity.HoaDonTra;
import entity.NhanVien;
import entity.Ve;
import java.sql.*;

/**
 * DAO cho Hóa đơn trả
 */
public class HoaDonTra_DAO {
    
    /**
     * Lấy mã hóa đơn trả lớn nhất
     */
    public String getMaxID() {
        String sql = "SELECT TOP 1 maHDT FROM HoaDonTra ORDER BY maHDT DESC";
        
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            if (rs.next()) {
                return rs.getString("maHDT");
            }
            
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "";
    }
    
    /**
     * Tạo hóa đơn trả mới
     */
    public boolean create(HoaDonTra hdt) {
        String sql = "INSERT INTO HoaDonTra (maHDT, ngayTra, maVe, maNV) " +
                     "VALUES (?, ?, ?, ?)";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, hdt.getMaHoaDonTra());
            ps.setTimestamp(2, Timestamp.valueOf(hdt.getNgayTra()));
            ps.setString(3, hdt.getVe().getMaVe());
            ps.setString(4, hdt.getNhanVien().getMaNV());
            
            
            int result = ps.executeUpdate();
            ps.close();
            
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lấy thông tin hóa đơn trả theo mã
     */
    public HoaDonTra getOne(String maHDT) {
        String sql = "SELECT hdt.*, nv.tenNhanVien, v.* " +
                     "FROM HoaDonTra hdt " +
                     "JOIN NhanVien nv ON hdt.maNhanVien = nv.maNhanVien " +
                     "JOIN Ve v ON hdt.maVe = v.maVe " +
                     "WHERE hdt.maHoaDonTra = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, maHDT);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                // Tạo đối tượng NhanVien
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("maNhanVien"));
                nv.setTenNV(rs.getString("tenNhanVien"));
                
                // Lấy thông tin vé (cần Ve_DAO để lấy đầy đủ)
                Ve_DAO veDAO = new Ve_DAO();
                Ve ve = veDAO.getOne(rs.getString("maVe"));
                
                // Tạo HoaDonTra
                HoaDonTra hdt = new HoaDonTra();
                hdt.setMaHoaDonTra(rs.getString("maHoaDonTra"));
                hdt.setNgayTra(rs.getTimestamp("ngayTraVe").toLocalDateTime());
                hdt.setNhanVien(nv);
                hdt.setVe(ve);
                
                return hdt;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}