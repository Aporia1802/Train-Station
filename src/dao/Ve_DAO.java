/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.ChuyenTau;
import entity.GaTau;
import entity.Ghe;
import entity.HanhKhach;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhoangTau;
import entity.KhuyenMai;
import entity.LoaiGhe;
import entity.LoaiVe;
import entity.NhanVien;
import entity.Tau;
import entity.ToaTau;
import entity.TuyenDuong;
import entity.Ve;
import enums.TrangThaiTau;
import enums.TrangThaiVe;
import interfaces.DAOBase;
import java.util.ArrayList;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CÔNG HOÀNG
 */
public class Ve_DAO implements DAOBase<Ve>{

    @Override
    public Ve getOne(String id) {
        Ve ve = null;

        try {
            String sql = "SELECT v.*, " +
                    "lv.*, " +
                    "hk.maHanhKhach, hk.tenHanhKhach, hk.cccd AS cccdHanhKhach, hk.ngaySinh AS ngaySinhHanhKhach, " +
                    "g.*, lg.*, kt.*, tt.*, t.*, ct.*, td.*, " +
                    "gdi.maGa AS maGaDi, gdi.tenGa AS tenGaDi, gdi.diaChi AS diaChiGaDi, gdi.soDienThoai AS sdtGaDi, " +
                    "gden.maGa AS maGaDen, gden.tenGa AS tenGaDen, gden.diaChi AS diaChiGaDen, gden.soDienThoai AS sdtGaDen, " +
                    "hd.*, " +
                    "nv.maNV, nv.tenNV, nv.gioiTinh AS gioiTinhNV, nv.ngaySinh AS ngaySinhNV, nv.email, nv.soDienThoai AS sdtNV, nv.cccd AS cccdNV, nv.diaChi AS diaChiNV, nv.chucVu, nv.trangThai AS trangThaiNV, " +
                    "kh.maKH, kh.tenKH, kh.soDienThoai AS sdtKH, kh.cccd AS cccdKH, kh.ngaySinh AS ngaySinhKH, " +
                    "km.* " +
                    "FROM Ve v " +
                    "JOIN LoaiVe lv ON v.maLoaiVe = lv.maLoaiVe " +
                    "JOIN HanhKhach hk ON v.maHanhKhach = hk.maHanhKhach " +
                    "JOIN Ghe g ON v.maGhe = g.maGhe " +
                    "JOIN LoaiGhe lg ON g.maLoaiGhe = lg.maLoaiGhe " +
                    "JOIN KhoangTau kt ON g.maKhoangTau = kt.maKhoangTau " +
                    "JOIN ToaTau tt ON kt.maToaTau = tt.maToaTau " +
                    "JOIN Tau t ON tt.maTau = t.maTau " +
                    "JOIN ChuyenTau ct ON v.maChuyenTau = ct.maChuyenTau " +
                    "JOIN TuyenDuong td ON ct.maTuyenDuong = td.maTuyenDuong " +
                    "JOIN GaTau gdi ON td.gaDi = gdi.maGa " +
                    "JOIN GaTau gden ON td.gaDen = gden.maGa " +
                    "JOIN HoaDon hd ON v.maHoaDon = hd.maHoaDon " +
                    "JOIN NhanVien nv ON hd.maNhanVien = nv.maNV " +
                    "JOIN KhachHang kh ON hd.maKhachHang = kh.maKH " +
                    "LEFT JOIN KhuyenMai km ON hd.maKhuyenMai = km.maKhuyenMai where v.maVe = ?";

            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                ve = getData(rs);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return ve;
    }

    @Override
    public ArrayList<Ve> getAll() {
        ArrayList<Ve> dsVe = new ArrayList<>();
        String sql = "SELECT v.*, " +
                    "lv.*, " +
                    "hk.maHanhKhach, hk.tenHanhKhach, hk.cccd AS cccdHanhKhach, hk.ngaySinh AS ngaySinhHanhKhach, " +
                    "g.*, lg.*, kt.*, tt.*, t.*, ct.*, td.*, " +
                    "gdi.maGa AS maGaDi, gdi.tenGa AS tenGaDi, gdi.diaChi AS diaChiGaDi, gdi.soDienThoai AS sdtGaDi, " +
                    "gden.maGa AS maGaDen, gden.tenGa AS tenGaDen, gden.diaChi AS diaChiGaDen, gden.soDienThoai AS sdtGaDen, " +
                    "hd.*, " +
                    "nv.maNV, nv.tenNV, nv.gioiTinh AS gioiTinhNV, nv.ngaySinh AS ngaySinhNV, nv.email, nv.soDienThoai AS sdtNV, nv.cccd AS cccdNV, nv.diaChi AS diaChiNV, nv.chucVu, nv.trangThai AS trangThaiNV, " +
                    "kh.maKH, kh.tenKH, kh.soDienThoai AS sdtKH, kh.cccd AS cccdKH, kh.ngaySinh AS ngaySinhKH, " +
                    "km.* " +
                    "FROM Ve v " +
                    "JOIN LoaiVe lv ON v.maLoaiVe = lv.maLoaiVe " +
                    "JOIN HanhKhach hk ON v.maHanhKhach = hk.maHanhKhach " +
                    "JOIN Ghe g ON v.maGhe = g.maGhe " +
                    "JOIN LoaiGhe lg ON g.maLoaiGhe = lg.maLoaiGhe " +
                    "JOIN KhoangTau kt ON g.maKhoangTau = kt.maKhoangTau " +
                    "JOIN ToaTau tt ON kt.maToaTau = tt.maToaTau " +
                    "JOIN Tau t ON tt.maTau = t.maTau " +
                    "JOIN ChuyenTau ct ON v.maChuyenTau = ct.maChuyenTau " +
                    "JOIN TuyenDuong td ON ct.maTuyenDuong = td.maTuyenDuong " +
                    "JOIN GaTau gdi ON td.gaDi = gdi.maGa " +
                    "JOIN GaTau gden ON td.gaDen = gden.maGa " +
                    "JOIN HoaDon hd ON v.maHoaDon = hd.maHoaDon " +
                    "JOIN NhanVien nv ON hd.maNhanVien = nv.maNV " +
                    "JOIN KhachHang kh ON hd.maKhachHang = kh.maKH " +
                    "LEFT JOIN KhuyenMai km ON hd.maKhuyenMai = km.maKhuyenMai ";

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                dsVe.add(getData(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) { 
            Logger.getLogger(Ve_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dsVe;
    }

    @Override
    public String generateID() {
        String prefix = "V";
        String sql = "SELECT MAX(maVe) FROM Ve";
        String newId = "";

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String lastId = rs.getString(1); 
                if (lastId != null) {
                    int number = Integer.parseInt(lastId.substring(prefix.length()));
                    number++;
                    newId = prefix + String.format("%03d", number);
                } else {
                    newId = prefix + "001";
                }
            }
        } catch (NumberFormatException | SQLException e) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, Ve newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public Boolean capNhatTrangThaiVe(String maVe) {
        int n = 0;
         String sql = "UPDATE Ve SET trangThai = ? WHERE maVe = ?";
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setInt(1, 3);
            st.setString(2, maVe);
            n = st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return n > 0;
    }
    
    public Ve getData(ResultSet rs) throws SQLException, Exception {
        // LOẠI VÉ 
        LoaiVe loaiVe = new LoaiVe(
                    rs.getString("maLoaiVe"),
                    rs.getString("tenLoaiVe"),
                    rs.getString("moTa"),
                    rs.getDouble("heSoLoaiVe")
            );
            
            // Hành khách
            HanhKhach hanhKhach = new HanhKhach(
                rs.getString("maHanhKhach"),
                rs.getString("tenHanhKhach"),
                rs.getString("cccdHanhKhach"),
                rs.getDate("ngaySinhHanhKhach").toLocalDate()  // ← Dùng alias
            );

            
            //GHẾ & LOẠI GHẾ
            LoaiGhe loaiGhe = new LoaiGhe(
                    rs.getString("maLoaiGhe"),
                    rs.getString("tenLoaiGhe"),
                    rs.getString("moTa"),
                    rs.getDouble("heSoLoaiGhe")
            );
            
            Tau tau = new Tau(
                    rs.getString("maTau"),
                    rs.getString("tenTau"),
                    rs.getInt("soToaTau"),
                    rs.getDate("ngayHoatDong").toLocalDate(),
                    TrangThaiTau.fromInt(rs.getInt("trangThai"))
            );
            
            ToaTau toaTau = new ToaTau(
                    rs.getString("maToaTau"),
                    rs.getInt("soHieuToa"),
                    rs.getInt("soKhoangTau"),
                    tau
            );
            
            KhoangTau khoangTau = new KhoangTau(
                    rs.getString("maKhoangTau"),
                    rs.getInt("soHieuKhoang"),
                    rs.getInt("soGhe"),
                    toaTau
            );
           
            Ghe ghe = new Ghe(
                    rs.getString("maGhe"),
                    rs.getInt("soGhe"),
                    loaiGhe,
                    khoangTau
            );
            
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
            
            // Nhân viên
            NhanVien nhanVien = new NhanVien(
                rs.getString("maNV"),
                rs.getString("tenNV"),
                rs.getBoolean("gioiTinhNV"),
                rs.getDate("ngaySinhNV").toLocalDate(),  // ← Dùng alias
                rs.getString("email"),
                rs.getString("sdtNV"),
                rs.getString("cccdNV"),
                rs.getString("diaChiNV"),
                rs.getString("chucVu"),
                rs.getBoolean("trangThaiNV")
            );
            
            // Khách hàng
            KhachHang khachHang = new KhachHang(
                rs.getString("maKH"),
                rs.getString("tenKH"),
                rs.getString("sdtKH"),
                rs.getString("cccdKH"),
                rs.getDate("ngaySinhKH").toLocalDate()
            );
            
            HoaDon hoaDon = new HoaDon(
                    rs.getString("maHoaDon"),
                    nhanVien,
                    khachHang,
                    rs.getTimestamp("ngayLapHoaDon").toLocalDateTime(),
                    khuyenMai
            );
            
            //VÉ 
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
    
    public ArrayList<Ve> timKiemVe(String maVe, String hoTen, String cccd, LocalDate ngayDi) {
        ArrayList<Ve> dsVe = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT * FROM Ve v
            JOIN HanhKhach hk ON v.maHanhKhach = hk.maHanhKhach
            JOIN ChuyenTau ct ON v.maChuyenTau = ct.maChuyenTau
            WHERE 1=1
                        """);

        // Xây dựng SQL linh hoạt (chỉ thêm điều kiện khi có dữ liệu)
        if (maVe != null && !maVe.trim().isEmpty()) {
            sql.append(" AND v.maVe = ?");
        }
        
        if (hoTen != null && !hoTen.trim().isEmpty()) {
            sql.append(" AND hk.tenHanhKhach LIKE ?");
        }
        
        if (cccd != null && !cccd.trim().isEmpty()) {
            sql.append(" AND hk.soCCCD = ?");
        }
        
        if (ngayDi != null) {
            sql.append(" AND CAST(ct.thoiGianDi AS DATE) = ?");
        }

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql.toString());

            int index = 1;
            if (maVe != null && !maVe.trim().isEmpty()) {
                st.setString(index++, maVe.trim());
            }
            
            if (hoTen != null && !hoTen.trim().isEmpty()) {
                st.setString(index++, "%" + hoTen.trim() + "%");
            }
            
            if (cccd != null && !cccd.trim().isEmpty()) {
                st.setString(index++, cccd.trim());
            }
            
            if (ngayDi != null) {
                st.setDate(index++, java.sql.Date.valueOf(ngayDi));
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                dsVe.add(getData(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsVe;
    }
}