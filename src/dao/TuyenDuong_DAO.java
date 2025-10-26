package dao;

import database.ConnectDB;
import entity.GaTau;
import entity.TuyenDuong;
import interfaces.DAOBase;
import java.sql.*;
import java.util.ArrayList;

public class TuyenDuong_DAO implements DAOBase {

    private GaTau_DAO gaTauDAO;

    public TuyenDuong_DAO() {
        this.gaTauDAO = new GaTau_DAO();
    }

    @Override
    public TuyenDuong getOne(String id) {
        String sql = "SELECT * FROM TuyenDuong WHERE maTuyenDuong = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, id);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TuyenDuong td = getData(rs);
                rs.close();
                ps.close();
                return td;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<TuyenDuong> getAll() {
        ArrayList<TuyenDuong> dsTuyenDuong = new ArrayList<>();
        String sql = "SELECT * FROM TuyenDuong";
        
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                TuyenDuong td = getData(rs);
                if (td != null) {
                    dsTuyenDuong.add(td);
                }
            }
            
            System.out.println("✅ DAO load được " + dsTuyenDuong.size() + " tuyến đường");
            
            rs.close();
            st.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return dsTuyenDuong;
    }

    public TuyenDuong getData(ResultSet rs) {
        try {
            String maTuyenDuong = rs.getString("maTuyenDuong");
            String maGaDi = rs.getString("gaDi");
            String maGaDen = rs.getString("gaDen");
            double quangDuong = rs.getDouble("quangDuong");
            double soTienMotKm = rs.getDouble("soTienMotKm");
            
            TuyenDuong td = new TuyenDuong(maTuyenDuong);

            GaTau gaDi = gaTauDAO.getOne(maGaDi);
            GaTau gaDen = gaTauDAO.getOne(maGaDen);
            
            System.out.println("✅ Parse TD: " + maTuyenDuong + 
                             " | Ga đi: " + maGaDi + 
                             " | Ga đến: " + maGaDen);
            
            if (gaDi == null) {
                System.err.println("   ⚠️ Không load được ga đi: " + maGaDi + ", tạo mặc định");
                gaDi = new GaTau(maGaDi);
                gaDi.setMaGa(maGaDi);
            }
            
            if (gaDen == null) {
                System.err.println("   ⚠️ Không load được ga đến: " + maGaDen + ", tạo mặc định");
                gaDen = new GaTau(maGaDen);
                gaDen.setMaGa(maGaDen);
            }
           
            try {
                td.setGaDi(gaDi);
                td.setGaDen(gaDen);
            } catch (Exception e) {
                System.err.println("   ⚠️ Lỗi set ga cho " + maTuyenDuong + ": " + e.getMessage());
                return td;
            }
            
            try {
                td.setQuangDuong(quangDuong);
                td.setSoTienMotKm(soTienMotKm);
            } catch (Exception e) {
                System.err.println("   ⚠️ Lỗi set quãng đường/giá cho " + maTuyenDuong + ": " + e.getMessage());
            }
            
            System.out.println("   ✅ Đã set: Ga đi = " + gaDi.getMaGa() + ", Ga đến = " + gaDen.getMaGa());
            
            return td;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean create(Object object) {
        TuyenDuong td = (TuyenDuong) object;
        String sql = "INSERT INTO TuyenDuong (maTuyenDuong, gaDi, gaDen, quangDuong, soTienMotKm) VALUES (?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, td.getMaTuyenDuong());
            ps.setString(2, td.getGaDi().getMaGa());
            ps.setString(3, td.getGaDen().getMaGa());
            ps.setDouble(4, td.getQuangDuong());
            ps.setDouble(5, td.getSoTienMotKm());
            
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
        TuyenDuong td = (TuyenDuong) newObject;
        String sql = "UPDATE TuyenDuong SET gaDi = ?, gaDen = ?, quangDuong = ?, soTienMotKm = ? WHERE maTuyenDuong = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, td.getGaDi().getMaGa());
            ps.setString(2, td.getGaDen().getMaGa());
            ps.setDouble(3, td.getQuangDuong());
            ps.setDouble(4, td.getSoTienMotKm());
            ps.setString(5, id);
            
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
        String sql = "DELETE FROM TuyenDuong WHERE maTuyenDuong = ?";
        
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
        throw new UnsupportedOperationException("Not supported yet.");
    }
}