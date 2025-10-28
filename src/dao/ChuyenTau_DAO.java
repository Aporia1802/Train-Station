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

public class ChuyenTau_DAO implements DAOBase {

    @Override
    public ChuyenTau getOne(String id) {
        String sql = "Select *, gdi.maGa AS maGaDi, gdi.tenGa AS tenGaDi," 
                      + "gden.maGa AS maGaDen, gden.tenGa AS tenGaDen from ChuyenTau ct"
                      + " JOIN Tau t ON t.maTau = ct.maTau"
                      + " JOIN TuyenDuong td ON td.maTuyenDuong = ct.maTuyenDuong"
                      + " JOIN GaTau gdi ON gdi.maGa = td.gaDi"
                      + " JOIN GaTau gden ON gden.maGa = td.gaDen where maChuyenTau = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                ChuyenTau ct = getData(rs);
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
        
        String sql = "Select *, gdi.maGa AS maGaDi, gdi.tenGa AS tenGaDi," 
                      + "gden.maGa AS maGaDen, gden.tenGa AS tenGaDen from ChuyenTau ct"
                      + " JOIN Tau t ON t.maTau = ct.maTau"
                      + " JOIN TuyenDuong td ON td.maTuyenDuong = ct.maTuyenDuong"
                      + " JOIN GaTau gdi ON gdi.maGa = td.gaDi"
                      + " JOIN GaTau gden ON gden.maGa = td.gaDen";

        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                dsChuyenTau.add(getData(rs));
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
        String sql = "INSERT INTO ChuyenTau (maChuyenTau, thoiGianDi, thoiGianDen, soGheDaDat, soGheConTrong, maTau, maTuyenDuong)"
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
    
    public ArrayList<ChuyenTau> timKiemChuyenTau(String gaDi, String gaDen, LocalDate ngayDi) throws Exception {
        ArrayList<ChuyenTau> dsChuyenTau = new ArrayList<>();
    
        try {

            String sql = """ 
                            SELECT *, gdi.maGa AS maGaDi, gdi.tenGa AS tenGaDi, 
                            gden.maGa AS maGaDen, gden.tenGa AS tenGaDen 
                            FROM ChuyenTau ct
                            JOIN Tau t ON ct.maTau = t.maTau
                            JOIN TuyenDuong td ON ct.maTuyenDuong = td.maTuyenDuong
                            JOIN GaTau gdi ON td.gaDi = gdi.maGa
                            JOIN GaTau gden ON td.gaDen = gden.maGa
                            WHERE (gdi.tenGa = ?)
                            AND (gden.tenGa = ?)
                            AND (CAST(ct.thoiGianDi AS DATE)) = ? 
                                                                    """;
                                                             
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, gaDi);
            st.setString(2, gaDen);
            st.setDate(3, java.sql.Date.valueOf(ngayDi));
        
            ResultSet rs = st.executeQuery();
            // Lấy danh sách chuyến tàu chiều đi
            while (rs.next()) {
                dsChuyenTau.add(getData(rs));
            }
        
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return dsChuyenTau;
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
    
    public boolean capNhatSoGheKhiDat(String maChuyenTau, int soGheDat) {
    int n = 0;
    String sql = """
                UPDATE ChuyenTau
                SET soGheDaDat = soGheDaDat + ?,
                soGheConTrong = soGheConTrong - ?
                WHERE maChuyenTau = ?
                AND soGheConTrong >= ?
                """;
    try {
        PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
        st.setInt(1, soGheDat);
        st.setInt(2, soGheDat);
        st.setString(3, maChuyenTau);
        st.setInt(4, soGheDat);
        n = st.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return n > 0;
}
}