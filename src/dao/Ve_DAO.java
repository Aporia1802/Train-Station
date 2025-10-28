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
 * DAO for Ve entity - Clean version
 * @author hwangquy
 */
public class Ve_DAO implements DAOBase<Ve> {

    public ArrayList<Ve> timKiemVe(String maVe, String hoTen, String cccd, LocalDate ngayDi) {
        ArrayList<Ve> dsVe = new ArrayList<>();

        String sql = """
            SELECT v.*, hk.*, g.*, lg.*, kt.*, tt.soHieuToa, t.*, 
                   ct.maChuyenTau, ct.thoiGianDi, ct.thoiGianDen, 
                   td.maTuyenDuong,
                   gaDi.maGa AS maGaDi, gaDi.tenGa AS tenGaDi,
                   gaDen.maGa AS maGaDen, gaDen.tenGa AS tenGaDen
            FROM Ve v
            JOIN HanhKhach hk ON v.maHanhKhach = hk.maHanhKhach
            JOIN Ghe g ON v.maGhe = g.maGhe
            JOIN LoaiGhe lg ON g.maLoaiGhe = lg.maLoaiGhe
            JOIN KhoangTau kt ON g.maKhoangTau = kt.maKhoangTau
            JOIN ToaTau tt ON kt.maToaTau = tt.maToaTau
            JOIN Tau t ON tt.maTau = t.maTau
            JOIN ChuyenTau ct ON v.maChuyenTau = ct.maChuyenTau
            JOIN TuyenDuong td ON ct.maTuyenDuong = td.maTuyenDuong
            JOIN GaTau gaDi ON td.gaDi = gaDi.maGa
            JOIN GaTau gaDen ON td.gaDen = gaDen.maGa
            WHERE (? IS NULL OR v.maVe LIKE ?)
              AND (? IS NULL OR hk.tenHanhKhach LIKE ?)
              AND (? IS NULL OR hk.cccd LIKE ?)
              AND (? IS NULL OR CAST(ct.thoiGianDi AS DATE) = ?)
        """;

        try (PreparedStatement ps = ConnectDB.conn.prepareStatement(sql)) {
        
            ps.setString(1, isEmpty(maVe) ? null : maVe);
            ps.setString(2, "%" + (maVe == null ? "" : maVe.trim()) + "%");
            ps.setString(3, isEmpty(hoTen) ? null : hoTen);
            ps.setString(4, "%" + (hoTen == null ? "" : hoTen.trim()) + "%");
            ps.setString(5, isEmpty(cccd) ? null : cccd);
            ps.setString(6, "%" + (cccd == null ? "" : cccd.trim()) + "%");
            ps.setObject(7, ngayDi);
            ps.setObject(8, ngayDi);

            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                try {
                    Ve ve = mapResultSetToVe(rs);
                    dsVe.add(ve);
                } catch (Exception e) {
                    System.err.println("️ Lỗi " + rs.getString("maVe") + ": " + e.getMessage());
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsVe;
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private Ve mapResultSetToVe(ResultSet rs) throws Exception {
        // Parse Ga tàu
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
        
        // Parse Tuyến đường
        TuyenDuong tuyenDuong = new TuyenDuong(
            rs.getString("maTuyenDuong"),
            gaDi,
            gaDen,
            1.0,
            1.0
        );
        
        // Parse Tàu
        Tau tau = new Tau(
            rs.getString("maTau"),
            rs.getString("tenTau"),
            1, 1,
            LocalDate.now(),
            TrangThaiTau.HOAT_DONG
        );
        
        // Parse Chuyến tàu
        ChuyenTau chuyenTau = new ChuyenTau(
            rs.getString("maChuyenTau"),
            tuyenDuong,
            rs.getTimestamp("thoiGianDi").toLocalDateTime(),
            rs.getTimestamp("thoiGianDen").toLocalDateTime(),
            tau
        );
        
        // Parse Hành khách
        HanhKhach hanhKhach = new HanhKhach(
            rs.getString("maHanhKhach"),
            rs.getString("tenHanhKhach"),
            rs.getString("cccd"),
            rs.getDate("ngaySinh").toLocalDate()
        );
        
        // Parse Loại ghế
        LoaiGhe loaiGhe = new LoaiGhe(
            rs.getString("maLoaiGhe"),
            rs.getString("tenLoaiGhe"),
            "N/A",
            rs.getDouble("heSoGhe")
        );
        
        // Parse Toa tàu
        ToaTau toaTau = new ToaTau(
            "TT-TEMP",
            rs.getInt("soHieuToa"),
            1, 1, tau
        );
        
        // Parse Khoang tàu
        KhoangTau khoangTau = new KhoangTau(rs.getString("maKhoangTau"));
        khoangTau.setToaTau(toaTau);
        
        // Parse Ghế
        Ghe ghe = new Ghe(
            rs.getString("maGhe"),
            rs.getInt("soGhe"),
            TrangThaiGhe.fromInt(rs.getInt("trangThaiGhe")),
            loaiGhe,
            khoangTau
        );
        
        // Parse Loại vé
        LoaiVe loaiVe = new LoaiVe(
            "LV-NL",
            "Người lớn",
            "N/A",
            1.0
        );
        
        // Parse Hóa đơn (giả)
        HoaDon hoaDon = new HoaDon("HD-TEMP");
        
        // Tạo vé
        return new Ve(
            rs.getString("maVe"),
            chuyenTau,
            hanhKhach,
            ghe,
            hoaDon,
            TrangThaiVe.fromInt(rs.getInt("trangThai")),
            loaiVe,
            rs.getDouble("giaVe")
        );
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

  
    public Boolean capNhatTrangThaiVe(String maVe) {
        String sql = "UPDATE Ve SET trangThai = 3 WHERE maVe = ?";
        
        try (PreparedStatement ps = ConnectDB.conn.prepareStatement(sql)) {
            ps.setString(1, maVe);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
}