package dao;

import database.ConnectDB;
import entity.Tau;
import enums.TrangThaiTau;
import interfaces.DAOBase;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Tau_DAO implements DAOBase {

    @Override
    public Tau getOne(String id) {

        String sql = "SELECT * FROM Tau WHERE maTau = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, id);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Tau tau = getData(rs);
                rs.close();
                ps.close();
                return tau;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Tau> getAll() {
        ArrayList<Tau> dsTau = new ArrayList<>();
        String sql = "SELECT * FROM Tau";
        
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                Tau tau = getData(rs);
                if (tau != null) {
                    dsTau.add(tau);
                }
            }
            
            rs.close();
            st.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return dsTau;
    }

    public Tau getData(ResultSet rs) {
        try {
            String maTau = rs.getString("maTau");
            String tenTau = rs.getString("tenTau");
            int soToaTau = rs.getInt("soToaTau");
            int sucChua = rs.getInt("sucChua");
            
            java.sql.Date ngayHoatDongSQL = rs.getDate("ngayHoatDong");
            LocalDate ngayHoatDong = ngayHoatDongSQL != null ? 
                ngayHoatDongSQL.toLocalDate() : null;
            
            String trangThaiStr = rs.getString("trangThai");
            
            Tau tau = new Tau(maTau);
            tau.setTenTau(tenTau);
            
            try {
                tau.setSoToaTau(soToaTau);
            } catch (Exception e) {
                System.err.println("⚠️ Số toa tàu không hợp lệ: " + soToaTau);
            }
            
            tau.setSucChua(sucChua);
            tau.setNgayHoatDong(ngayHoatDong);
            
            // Convert trạng thái
            if (trangThaiStr != null && !trangThaiStr.trim().isEmpty()) {
                TrangThaiTau trangThai = TrangThaiTau.fromString(trangThaiStr);
                tau.setTrangThai(trangThai);
            }
            
            return tau;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean create(Object object) {
        Tau tau = (Tau) object;
        String sql = "INSERT INTO Tau (maTau, tenTau, soToaTau, sucChua, ngayHoatDong, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, tau.getMaTau());
            ps.setString(2, tau.getTenTau());
            ps.setInt(3, tau.getSoToaTau());
            ps.setInt(4, tau.getSucChua());
            ps.setDate(5, Date.valueOf(tau.getNgayHoatDong()));
            ps.setString(6, tau.getTrangThai().getDisplay());
            
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
        Tau tau = (Tau) newObject;
        String sql = "UPDATE Tau SET tenTau = ?, soToaTau = ?, sucChua = ?, ngayHoatDong = ?, trangThai = ? WHERE maTau = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, tau.getTenTau());
            ps.setInt(2, tau.getSoToaTau());
            ps.setInt(3, tau.getSucChua());
            ps.setDate(4, Date.valueOf(tau.getNgayHoatDong()));
            ps.setString(5, tau.getTrangThai().getDisplay());
            ps.setString(6, id);
            
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
        String sql = "DELETE FROM Tau WHERE maTau = ?";
        
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

    @Override
    public String generateID() {
        String newID = "SE1";
        String sql = "SELECT TOP 1 maTau FROM Tau ORDER BY maTau DESC";
        
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            if (rs.next()) {
                String lastID = rs.getString("maTau");
                String numberPart = lastID.replaceAll("[^0-9]", "");
                
                try {
                    int number = Integer.parseInt(numberPart);
                    newID = "SE" + (number + 1);
                } catch (NumberFormatException e) {
                    newID = "SE" + System.currentTimeMillis();
                }
            }
            
            rs.close();
            st.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return newID;
    }
}
