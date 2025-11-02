package dao;

import database.ConnectDB;
import static database.ConnectDB.conn;
import entity.Tau;
import enums.TrangThaiTau;
import interfaces.DAOBase;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tau_DAO implements DAOBase<Tau>{

    @Override
    public Tau getOne(String id) {

        String sql = "SELECT * FROM Tau WHERE maTau = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, id);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Tau tau = getData(rs);
                return tau;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(Tau_DAO.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(Tau_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dsTau;
    }
    
     public ArrayList<Tau> getTauByMaTau(String maTau) throws Exception {
        ArrayList<Tau> dsTau = new ArrayList<>();
        String sql = "SELECT * FROM Tau WHERE maTau LIKE ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,maTau);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dsTau.add(getData(rs)); // getData là hàm chuyển ResultSet → đối tượng Tau
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return dsTau;
}
     
    public ArrayList<Tau> filterByTrangThai(String trangThaiStr) {
        ArrayList<Tau> dsTau = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Tau WHERE 1=1");

        // Nếu người dùng chọn thật sự (không phải "Trạng thái" hoặc "Tất cả")
        if (trangThaiStr != null && !trangThaiStr.equalsIgnoreCase("Trạng thái") && !trangThaiStr.equalsIgnoreCase("Tất cả")) {
            sql.append(" AND trangThai = ?");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;

            // Gán giá trị trạng thái nếu người dùng chọn
            if (trangThaiStr != null && !trangThaiStr.equalsIgnoreCase("Trạng thái") && !trangThaiStr.equalsIgnoreCase("Tất cả")) {
                TrangThaiTau tt = TrangThaiTau.fromDisplay(trangThaiStr);
                if (tt != null) {
                    ps.setInt(index++, tt.getValue()); // lưu trong DB là số: 1 / 2 / 3
                }
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dsTau.add(getData(rs));
            }
        } catch (Exception e) {
        throw new RuntimeException(e.getMessage());
    }
    return dsTau;
}




    public Tau getData(ResultSet rs) throws SQLException, Exception {
        String maTau = rs.getString("maTau");
        String tenTau = rs.getString("tenTau");
        int soToaTau = rs.getInt("soToaTau");
        java.sql.Date ngayHoatDongSQL = rs.getDate("ngayHoatDong");
        LocalDate ngayHoatDong = ngayHoatDongSQL != null ? 
        ngayHoatDongSQL.toLocalDate() : null;   
        Tau tau = new Tau(maTau);
        tau.setTenTau(tenTau);
        tau.setSoToaTau(soToaTau);
        tau.setNgayHoatDong(ngayHoatDong);
        TrangThaiTau trangThai = TrangThaiTau.fromInt(rs.getInt("trangThai"));
        tau.setTrangThai(trangThai);
        return tau;
    }
    
    public ArrayList<Tau> search(String maTau, String tenTau, int trangThai) {
         ArrayList<Tau> dsTau = new ArrayList<>();
        String sql = "SELECT * FROM Tau WHERE 1=1";

        if (maTau != null && !maTau.trim().isEmpty()) {
            sql += " AND maTau LIKE ?";
        }
        if (tenTau != null && !tenTau.trim().isEmpty()) {
            sql += " AND tenTau LIKE ?";
        }
        if (trangThai != 0) {
            sql += " AND trangThai = ?";
        }

        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);

            int index = 1;
            if (maTau != null && !maTau.trim().isEmpty()) {
                ps.setString(index++, "%" + maTau + "%");
            }
            if (tenTau != null && !tenTau.trim().isEmpty()) {
                ps.setString(index++, "%" + tenTau + "%");
            }
            if (trangThai != 0) {
                ps.setInt(index++, trangThai);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dsTau.add(getData(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return dsTau;
    }

    @Override
    public Boolean create(Tau object) {
        int n = 0;
        String sql = "INSERT INTO Tau (maTau, tenTau, soToaTau, ngayHoatDong, trangThai) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = ConnectDB.conn.prepareStatement(sql)) {
            st.setString(1, object.getMaTau());
            st.setString(2, object.getTenTau());
            st.setInt(3, object.getSoToaTau());
            st.setDate(4, Date.valueOf(object.getNgayHoatDong()));
            st.setInt(5, object.getTrangThai().getValue());
            n = st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return n > 0;
    }

  
    @Override
    public Boolean update(String id, Tau newObject) {
        int n = 0;
        String sql = "UPDATE Tau SET tenTau=?, soToaTau=?, ngayHoatDong=?, trangThai=? WHERE maTau=?";
        try (PreparedStatement st = ConnectDB.conn.prepareStatement(sql)) {
            st.setString(1, newObject.getTenTau());
            st.setInt(2, newObject.getSoToaTau());
            st.setDate(3, Date.valueOf(newObject.getNgayHoatDong()));
            st.setInt(4, newObject.getTrangThai().getValue());
            st.setString(5, id);
            n = st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return n > 0;
    }

    

    @Override
    public Boolean delete(String id) {
        int n = 0;
        String sql = "DELETE FROM Tau WHERE maTau = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, id);
            
            n = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return n > 0;
    }
    
     public String getMaxID() {
        String maxID = ""; // giá trị mặc định nếu bảng rỗng
    
        try{
            String sql = "SELECT TOP 1 * FROM Tau ORDER BY MaTau DESC";
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                maxID = rs.getString("maTau");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return maxID;
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
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return newID;
    }
}
