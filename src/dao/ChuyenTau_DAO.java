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
import java.time.LocalDateTime;

public class ChuyenTau_DAO implements DAOBase {

    @Override
    public ChuyenTau getOne(String id) {
        String sql = "SELECT * FROM ChuyenTau ct "
                   + "JOIN Tau t ON t.maTau = ct.maTau "
                   + "JOIN TuyenDuong td ON td.maTuyenDuong = ct.maTuyenDuong "
                   + "WHERE ct.maChuyenTau = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                ChuyenTau ct = getData(rs);
                rs.close();
                ps.close();
                return ct;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<ChuyenTau> getAll() {
        ArrayList<ChuyenTau> dsChuyenTau = new ArrayList<>();
        
        String sql = "SELECT * FROM ChuyenTau ct "
                   + "JOIN Tau t ON t.maTau = ct.maTau "
                   + "JOIN TuyenDuong td ON td.maTuyenDuong = ct.maTuyenDuong";

        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                ChuyenTau ct = getData(rs);
                if (ct != null) {
                    dsChuyenTau.add(ct);
                }
            }
            
            rs.close();
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsChuyenTau;
    }

    public ArrayList<ChuyenTau> getChuyenTauByKeyword(String keyword) {
        ArrayList<ChuyenTau> dsChuyenTau = new ArrayList<>();
        
        String sql = "SELECT * FROM ChuyenTau ct "
                   + "JOIN Tau t ON t.maTau = ct.maTau "
                   + "JOIN TuyenDuong td ON td.maTuyenDuong = ct.maTuyenDuong "
                   + "WHERE ct.maChuyenTau LIKE ? OR t.maTau LIKE ? OR td.maTuyenDuong LIKE ?";

        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ChuyenTau ct = getData(rs);
                if (ct != null) {
                    dsChuyenTau.add(ct);
                }
            }
            
            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsChuyenTau;
    }

    @Override
    public String generateID() {
        String newID = "CT-001";
        String sql = "SELECT TOP 1 maChuyenTau FROM ChuyenTau ORDER BY maChuyenTau DESC";
        
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            if (rs.next()) {
                String lastID = rs.getString("maChuyenTau");
                String[] parts = lastID.split("-");
                
                if (parts.length >= 2) {
                    try {
                        int number = Integer.parseInt(parts[parts.length - 1]);
                        newID = String.format("CT-%03d", number + 1);
                    } catch (NumberFormatException e) {
                        newID = "CT-" + System.currentTimeMillis();
                    }
                }
            }
            
            rs.close();
            st.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return newID;
    }

    @Override
    public Boolean create(Object object) {
        ChuyenTau ct = (ChuyenTau) object;
        String sql = "INSERT INTO ChuyenTau (maChuyenTau, thoiGianDi, thoiGianDen, soGheDaDat, soGheConTrong, maTau, maTuyenDuong) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, ct.getMaChuyenTau());
            ps.setTimestamp(2, Timestamp.valueOf(ct.getThoiGianDi()));
            ps.setTimestamp(3, Timestamp.valueOf(ct.getThoiGianDen()));
            ps.setInt(4, ct.getSoGheDaDat());
            ps.setInt(5, ct.getSoGheConTrong());
            ps.setString(6, ct.getTau().getMaTau());
            ps.setString(7, ct.getTuyenDuong().getMaTuyenDuong());
            
            int result = ps.executeUpdate();
            ps.close();
            
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean update(String id, Object newObject) {
        ChuyenTau ct = (ChuyenTau) newObject;
        String sql = "UPDATE ChuyenTau SET thoiGianDi = ?, thoiGianDen = ?, soGheDaDat = ?, soGheConTrong = ?, maTau = ?, maTuyenDuong = ? "
                   + "WHERE maChuyenTau = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(ct.getThoiGianDi()));
            ps.setTimestamp(2, Timestamp.valueOf(ct.getThoiGianDen()));
            ps.setInt(3, ct.getSoGheDaDat());
            ps.setInt(4, ct.getSoGheConTrong());
            ps.setString(5, ct.getTau().getMaTau());
            ps.setString(6, ct.getTuyenDuong().getMaTuyenDuong());
            ps.setString(7, id);
            
            int result = ps.executeUpdate();
            ps.close();
            
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean delete(String id) {
        String sql = "DELETE FROM ChuyenTau WHERE maChuyenTau = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, id);
            
            int result = ps.executeUpdate();
            ps.close();
            
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private ChuyenTau getData(ResultSet rs) throws SQLException, Exception { 
        ChuyenTau ct = new ChuyenTau();
        Tau tau = new Tau(rs.getString("maTau"));
        TuyenDuong td = new TuyenDuong(rs.getString("maTuyenDuong"));
      
        // Lấy thông tin Tàu
        tau.setTenTau(rs.getString("tenTau"));
        tau.setSoToaTau(rs.getInt("soToaTau"));
        tau.setSucChua(rs.getInt("sucChua"));
        tau.setNgayHoatDong(rs.getDate("ngayHoatDong").toLocalDate());
        
        // Convert trạng thái
        String trangThaiStr = rs.getString("trangThai");
        TrangThaiTau trangThai = TrangThaiTau.fromString(trangThaiStr);
        tau.setTrangThai(trangThai);

        // Lấy thông tin Tuyến đường
        td.setQuangDuong(rs.getDouble("quangDuong"));
        td.setSoTienMotKm(rs.getDouble("soTienMotKm"));
        td.setGaDi(new GaTau(rs.getString("gaDi")));
        td.setGaDen(new GaTau(rs.getString("gaDen")));

        // Lấy thông tin Chuyến tàu
        ct.setMaChuyenTau(rs.getString("maChuyenTau"));
        
        // Set thời gian không validation (cho phép load dữ liệu cũ)
        LocalDateTime thoiGianDi = rs.getTimestamp("thoiGianDi").toLocalDateTime();
        LocalDateTime thoiGianDen = rs.getTimestamp("thoiGianDen").toLocalDateTime();
        
        ct.setThoiGianDi(thoiGianDi, false); // Không validate
        ct.setThoiGianDen(thoiGianDen);
        
        ct.setSoGheDaDat(rs.getInt("soGheDaDat"));
        ct.setSoGheConTrong(rs.getInt("soGheConTrong"));
        ct.setTau(tau);
        ct.setTuyenDuong(td);
        
        return ct;
    }
}