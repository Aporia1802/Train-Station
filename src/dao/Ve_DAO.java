package dao;

import database.ConnectDB;
import entity.*;
import enums.TrangThaiGhe;
import enums.TrangThaiTau;
import enums.TrangThaiVe;
import interfaces.DAOBase;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * DAO for Ve entity - Simplified version
 * @author CÔNG HOÀNG
 */
public class Ve_DAO implements DAOBase<Ve> {

    public ArrayList<Ve> timKiemVe(String maVe, String hoTen, String cccd, LocalDate ngayDi) {
        ArrayList<Ve> dsVe = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(
            "SELECT v.*, hk.*, g.*, lg.*, kt.*, tt.soHieuToa, t.*, " +
            "ct.maChuyenTau, ct.thoiGianDi, ct.thoiGianDen, " +
            "td.maTuyenDuong, " +
            "gaDi.maGa AS maGaDi, gaDi.tenGa AS tenGaDi, " +
            "gaDen.maGa AS maGaDen, gaDen.tenGa AS tenGaDen " +
            "FROM Ve v " +
            "JOIN HanhKhach hk ON v.maHanhKhach = hk.maHanhKhach " +
            "JOIN Ghe g ON v.maGhe = g.maGhe " +
            "JOIN LoaiGhe lg ON g.maLoaiGhe = lg.maLoaiGhe " +
            "JOIN KhoangTau kt ON g.maKhoangTau = kt.maKhoangTau " +
            "JOIN ToaTau tt ON kt.maToaTau = tt.maToaTau " +
            "JOIN Tau t ON tt.maTau = t.maTau " +
            "JOIN ChuyenTau ct ON v.maChuyenTau = ct.maChuyenTau " +
            "JOIN TuyenDuong td ON ct.maTuyenDuong = td.maTuyenDuong " +
            "JOIN GaTau gaDi ON td.gaDi = gaDi.maGa " +
            "JOIN GaTau gaDen ON td.gaDen = gaDen.maGa " +
            "WHERE 1=1"
        );
        
        ArrayList<Object> params = new ArrayList<>();
        
        if (maVe != null && !maVe.trim().isEmpty()) {
            sql.append(" AND v.maVe LIKE ?");
            params.add("%" + maVe.trim() + "%");
        }
        
        if (hoTen != null && !hoTen.trim().isEmpty()) {
            sql.append(" AND hk.tenHanhKhach LIKE ?");
            params.add("%" + hoTen.trim() + "%");
        }
        
        if (cccd != null && !cccd.trim().isEmpty()) {
            sql.append(" AND hk.cccd LIKE ?");
            params.add("%" + cccd.trim() + "%");
        }
        
        if (ngayDi != null) {
            sql.append(" AND CAST(ct.thoiGianDi AS DATE) = ?");
            params.add(Date.valueOf(ngayDi));
        }
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql.toString());
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                try {
                    Ve ve = mapResultSetToVe(rs);
                    dsVe.add(ve);
                } catch (Exception e) {
                    System.err.println("⚠️ Lỗi parse vé: " + e.getMessage());
                }
            }
            
            rs.close();
            ps.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsVe;
    }

   private Ve mapResultSetToVe(ResultSet rs) throws Exception {
   
    GaTau gaDi = new GaTau(
        rs.getString("maGaDi"),
        rs.getString("tenGaDi"),
        "N/A", 
        "0000000000"  
    );
    
    GaTau gaDen = new GaTau(
        rs.getString("maGaDen"),
        rs.getString("tenGaDen"),
        "N/A",
        "0000000000"  
    );
    
    TuyenDuong tuyenDuong = new TuyenDuong(
        rs.getString("maTuyenDuong"),
        gaDi,
        gaDen,
        1.0, 
        1.0  
    );
    
    Tau tau = new Tau(
        rs.getString("maTau"),
        rs.getString("tenTau"),
        1,
        1,
        LocalDate.now(),
        TrangThaiTau.HOAT_DONG
    );
    
    ChuyenTau chuyenTau = new ChuyenTau(
        rs.getString("maChuyenTau"),
        tuyenDuong,
        rs.getTimestamp("thoiGianDi").toLocalDateTime(),
        rs.getTimestamp("thoiGianDen").toLocalDateTime(),
        tau
    );
    
    HanhKhach hanhKhach = new HanhKhach(
        rs.getString("maHanhKhach"),
        rs.getString("tenHanhKhach"),
        rs.getString("cccd"),
        rs.getDate("ngaySinh").toLocalDate()
    );
    
    LoaiGhe loaiGhe = new LoaiGhe(
        rs.getString("maLoaiGhe"),
        rs.getString("tenLoaiGhe"),
        "N/A", 
        rs.getDouble("heSoGhe")
    );
    
    ToaTau toaTau = new ToaTau(
        "TT-TEMP",
        rs.getInt("soHieuToa"),
        1, 
        1, 
        tau
    );
     KhoangTau khoangTau = new KhoangTau(rs.getString("maKhoangTau"));
    khoangTau.setToaTau(toaTau);
    
    Ghe ghe = new Ghe(
        rs.getString("maGhe"),
        rs.getInt("soGhe"),
        TrangThaiGhe.fromInt(rs.getInt("trangThaiGhe")),
        loaiGhe,
        khoangTau
    );
    
    LoaiVe loaiVe = new LoaiVe(
        "LV-NL", 
        "Người lớn",
        "N/A",
        1.0
    );
    HoaDon hoaDon = new HoaDon("HD-TEMP"); 
    
    // Tạo vé
    Ve ve = new Ve(
        rs.getString("maVe"),
        chuyenTau,
        hanhKhach,
        ghe,
        hoaDon, 
        TrangThaiVe.fromInt(rs.getInt("trangThai")),
        loaiVe,
        rs.getDouble("giaVe")
    );
    
    return ve;
   }
    @Override
    public Ve getOne(String id) {
        ArrayList<Ve> result = timKiemVe(id, null, null, null);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public ArrayList<Ve> getAll() {
        return timKiemVe(null, null, null, null);
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean create(Ve object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean update(String id, Ve newObject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
  
    public Boolean capNhatTrangThaiVe(String maVe) {
        String sql = "UPDATE Ve SET trangThai = 3 WHERE maVe = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, maVe);
            
            int rows = ps.executeUpdate();
            ps.close();
            
            return rows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}