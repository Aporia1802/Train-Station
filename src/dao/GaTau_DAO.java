package dao;

import database.ConnectDB;
import entity.GaTau;
import interfaces.DAOBase;
import java.sql.*;
import java.util.ArrayList;

public class GaTau_DAO implements DAOBase {

    @Override
    public GaTau getOne(String id) {
        String sql = "SELECT * FROM GaTau WHERE maGa = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, id);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                GaTau ga = getData(rs);
                rs.close();
                ps.close();
                return ga;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<GaTau> getAll() {
        ArrayList<GaTau> dsGaTau = new ArrayList<>();
        String sql = "SELECT * FROM GaTau";
        
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                GaTau ga = getData(rs);
                if (ga != null) {
                    dsGaTau.add(ga);
                }
            }
            
            rs.close();
            st.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return dsGaTau;
    }

    public GaTau getData(ResultSet rs) {
        try {
            String maGa = rs.getString("maGa");
            String tenGa = rs.getString("tenGa");
            String diaChi = rs.getString("diaChi");
            String soDienThoai = rs.getString("soDienThoai");
            
            GaTau ga = new GaTau(maGa);
            
            try {
                if (tenGa != null && !tenGa.trim().isEmpty()) {
                    ga.setTenGa(tenGa);
                }
                if (diaChi != null && !diaChi.trim().isEmpty()) {
                    ga.setDiaChi(diaChi);
                }
                if (soDienThoai != null && !soDienThoai.trim().isEmpty()) {
                    ga.setSoDienThoai(soDienThoai);
                }
            } catch (Exception e) {
                System.err.println("⚠️ Lỗi set thuộc tính ga " + maGa + ": " + e.getMessage());
            }
            
            return ga;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean create(Object object) {
        GaTau ga = (GaTau) object;
        String sql = "INSERT INTO GaTau (maGa, tenGa, diaChi, soDienThoai) VALUES (?, ?, ?, ?)";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, ga.getMaGa());
            ps.setString(2, ga.getTenGa());
            ps.setString(3, ga.getDiaChi());
            ps.setString(4, ga.getSoDienThoai());
            
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
        GaTau ga = (GaTau) newObject;
        String sql = "UPDATE GaTau SET tenGa = ?, diaChi = ?, soDienThoai = ? WHERE maGa = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, ga.getTenGa());
            ps.setString(2, ga.getDiaChi());
            ps.setString(3, ga.getSoDienThoai());
            ps.setString(4, id);
            
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
        String sql = "DELETE FROM GaTau WHERE maGa = ?";
        
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
        String newID = "GA-001";
        String sql = "SELECT TOP 1 maGa FROM GaTau ORDER BY maGa DESC";
        
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            if (rs.next()) {
                String lastID = rs.getString("maGa");
                String[] parts = lastID.split("-");
                
                if (parts.length >= 2) {
                    try {
                        int number = Integer.parseInt(parts[parts.length - 1]);
                        newID = String.format("GA-%03d", number + 1);
                    } catch (NumberFormatException e) {
                        newID = "GA-" + System.currentTimeMillis();
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

    public ArrayList<GaTau> filter(String maGa, String tenGa, String diaChi, String soDienThoai) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ArrayList<GaTau> getGaTauByKeyword(String keyword) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}