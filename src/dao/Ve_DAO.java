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

    @Override
    public String generateID() {
         String prefix = "VE";
    String sql = "SELECT MAX(maVe) FROM Ve";
    String newId = "";

    try {
        PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            String lastId = rs.getString(1); // VD: VE0023
            if (lastId != null) {
                int number = Integer.parseInt(lastId.substring(prefix.length()));
                number++;
                newId = prefix + String.format("%04d", number); // -> VE0024
            } else {
                newId = prefix + "0001";
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        newId = prefix + "0001";
    }

    return newId;
    }

    @Override
    public Boolean create(Ve object) {
        int n = 0;
        String sql = "INSERT INTO Ve (maVe, maLoaiVe, maChuyenTau, maHanhKhach, maGhe, maHoaDon, trangThai, giaVe)"
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, object.getMaVe());
            st.setString(2, object.getLoaiVe().getMaLoaiVe());
            st.setString(3, object.getChuyenTau().getMaChuyenTau());
            st.setString(4, object.getHanhKhach().getMaHanhKhach());
            st.setString(5, object.getGhe().getMaGhe());
            st.setString(6, object.getHoaDon().getMaHoaDon());
            st.setInt(7, object.getTrangThai().getValue());
            st.setDouble(8, object.getGiaVe());
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
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
    
    public Ve getData(ResultSet rs) throws SQLException, Exception {
        Ve ve = null;
            // LOẠI VÉ 
            LoaiVe loaiVe = new LoaiVe(
                    rs.getString("maLoaiVe"),
                    rs.getString("tenLoaiVe"),
                    rs.getString("moTa"),
                    rs.getDouble("heSoLoaiVe")
            );
            
            // HÀNH KHÁCH
            HanhKhach hanhKhach = new HanhKhach(
                    rs.getString("maHanhKhach"),
                    rs.getString("tenHanhKhach"),
                    rs.getString("cccd"),
                    rs.getDate("ngaySinh").toLocalDate()
            );
            
            //GHẾ & LOẠI GHẾ
            LoaiGhe loaiGhe = new LoaiGhe(
                    rs.getString("maLoaiGhe"),
                    rs.getString("tenLoaiGhe"),
                    rs.getString("moTa"),
                    rs.getDouble("heSoGhe")
            );
            
            Tau tau = new Tau(
                    rs.getString("maTau"),
                    rs.getString("tenTau"),
                    rs.getInt("soToaTau"),
                    rs.getInt("sucChua"),
                    rs.getDate("ngayHoatDong").toLocalDate(),
                    TrangThaiTau.fromInt(rs.getInt("trangThai"))
            );
            
            ToaTau toaTau = new ToaTau(
                    rs.getString("maToaTau"),
                    rs.getInt("soHieuToa"),
                    rs.getInt("soKhoangTau"),
                    rs.getInt("soCho"),
                    tau
            );
            
            KhoangTau khoangTau = new KhoangTau(
                    rs.getString("maKhoangTau"),
                    rs.getInt("soHieuKhoang"),
                    rs.getInt("sucChua"),
                    toaTau
            );
           
            Ghe ghe = new Ghe(
                    rs.getString("maGhe"),
                    rs.getInt("soGhe"),
                    TrangThaiGhe.fromInt(rs.getInt("trangThaiGhe")),
                    loaiGhe,
                    khoangTau
            );
            
            // ======= Bảng CHUYẾN TÀU & TUYẾN ĐƯỜNG & GA =======
            GaTau gaDi = new GaTau(
                    rs.getString("maGaDi"),
                    rs.getString("tenGaDi"),
                    rs.getString("diaChiGaDi"),
                    rs.getString("sdtGaDi")
            );
            
            GaTau gaDen = new GaTau(
                    rs.getString("maGaDen"),
                    rs.getString("tenGaDen"),
                    rs.getString("diaChiGaDen"),
                    rs.getString("sdtGaDen")
            );
            
            TuyenDuong tuyenDuong = new TuyenDuong(
                    rs.getString("maTuyenDuong"),
                    gaDi,
                    gaDen,
                    rs.getDouble("quangDuong"),
                    rs.getDouble("soTienMotKm")
            );
            
            ChuyenTau chuyenTau = new ChuyenTau(
                    rs.getString("maChuyenTau"),
                    tuyenDuong,
                    rs.getTimestamp("thoiGianDi").toLocalDateTime(),
                    rs.getTimestamp("thoiGianDen").toLocalDateTime(),
                    tau
            );
            
            KhuyenMai khuyenMai = null;
            if (rs.getString("maKhuyenMai") != null) {
                khuyenMai = new KhuyenMai(
                    rs.getString("maKhuyenMai"),
                    rs.getString("tenKhuyenMai"),
                    rs.getDouble("heSoKhuyenMai"),
                    rs.getDate("ngayBatDau").toLocalDate(),
                    rs.getDate("ngayKetThuc").toLocalDate(),
                    rs.getDouble("tongTienToiThieu"),
                    rs.getDouble("tienKhuyenMaiToiDa"),
                    rs.getBoolean("trangThai")
                );
            }
            
            NhanVien nhanVien = new NhanVien(
                    rs.getString("maNV"),
                    rs.getString("tenNV"),
                    rs.getBoolean("gioiTinh"),
                    rs.getDate("ngaySinh").toLocalDate(),
                    rs.getString("email"),
                    rs.getString("soDienThoai"),
                    rs.getString("cccd"),
                    rs.getString("diaChi"),
                    rs.getString("chucVu"),
                    rs.getBoolean("trangThai")
            );
            
            KhachHang khachHang = new KhachHang(
                    rs.getString("maKH"),
                    rs.getString("tenKH"),
                    rs.getString("soDienThoai"),
                    rs.getString("cccd"),
                    rs.getDate("ngaySinh").toLocalDate(),
                    rs.getBoolean("gioiTinh")
            );
            
            HoaDon hoaDon = new HoaDon(
                    rs.getString("maHoaDon"),
                    nhanVien,
                    khachHang,
                    rs.getTimestamp("ngayLapHoaDon").toLocalDateTime(),
                    khuyenMai
            );
            
            //VÉ (chính)
            ve = new Ve(
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
    
    public ArrayList<Ve> timKiemVe(String maVe, String hoTen, String cccd, LocalDate ngayDi) {
        ArrayList<Ve> dsVe = new ArrayList<>();

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