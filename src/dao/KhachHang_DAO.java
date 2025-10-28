package dao;

import database.ConnectDB;
import entity.KhachHang;
import interfaces.DAOBase;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class KhachHang_DAO implements DAOBase<KhachHang> {

    @Override
    public KhachHang getOne(String id) {
        KhachHang kh = null;
        String sql = "SELECT * FROM KhachHang WHERE maKH = ?";
        
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                kh = getData(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kh;
    }
    
    public KhachHang getByCCCD(String cccd) {
        KhachHang kh = null;
        String sql = "SELECT * FROM KhachHang WHERE cccd = ?";
        
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, cccd);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                kh = getData(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kh;
    }

    @Override
    public ArrayList<KhachHang> getAll() {
        ArrayList<KhachHang> dsKH = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                dsKH.add(getData(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsKH;
    }

    @Override
    public String generateID() {
        String newID = "KH001";
        String sql = "SELECT TOP 1 maKH FROM KhachHang ORDER BY maKH DESC";
        
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            if (rs.next()) {
                String lastID = rs.getString("maKH");
                int num = Integer.parseInt(lastID.substring(2));
                newID = String.format("KH%03d", num + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newID;
    }

    @Override
    public Boolean create(KhachHang object) {
        int n = 0;
        String sql = "INSERT INTO KhachHang (maKH, tenKH, soDienThoai, cccd, ngaySinh, gioiTinh) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, object.getMaKH());
            st.setString(2, object.getTenKH());
            st.setString(3, object.getSoDienThoai());
            st.setString(4, object.getCccd());
            st.setDate(5, Date.valueOf(object.getNgaySinh()));
            st.setBoolean(6, object.isGioiTinh());
            
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, KhachHang newObject) {
        int n = 0;
        String sql = "UPDATE KhachHang SET tenKH = ?, soDienThoai = ?, cccd = ?, ngaySinh = ?, gioiTinh = ? WHERE maKH = ?";
        
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, newObject.getTenKH());
            st.setString(2, newObject.getSoDienThoai());
            st.setString(3, newObject.getCccd());
            st.setDate(4, Date.valueOf(newObject.getNgaySinh()));
            st.setBoolean(5, newObject.isGioiTinh());
            st.setString(6, id);
            
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean delete(String id) {
        int n = 0;
        String sql = "DELETE FROM KhachHang WHERE maKH = ?";
        
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, id);
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    
    private KhachHang getData(ResultSet rs) throws SQLException, Exception {
        String maKH = rs.getString("maKH");
        String tenKH = rs.getString("tenKH");
        String soDienThoai = rs.getString("soDienThoai");
        String cccd = rs.getString("cccd");
        LocalDate ngaySinh = rs.getDate("ngaySinh").toLocalDate();
        boolean gioiTinh = rs.getBoolean("gioiTinh");
        
        return new KhachHang(maKH, tenKH, soDienThoai, cccd, ngaySinh, gioiTinh);
    }
}