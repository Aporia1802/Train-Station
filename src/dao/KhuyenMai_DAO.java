/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import static database.ConnectDB.conn;
import entity.KhuyenMai;
import interfaces.DAOBase;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author CÔNG HOÀNG
 */
public class KhuyenMai_DAO implements DAOBase {

    @Override
    public Object getOne(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList getAll() {
        ArrayList<KhuyenMai> dsKhuyenMai = new ArrayList();

        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("Select * from KhuyenMai");
            while (rs.next()) {
                if (rs != null) {
                    dsKhuyenMai.add(getData(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsKhuyenMai;
    }

    public ArrayList<KhuyenMai> getKhuyenMaiByKeyword(String keyword) {
        ArrayList<KhuyenMai> dsKhuyenMai = new ArrayList<>();
        String sql = """
                    SELECT * FROM KhuyenMai WHERE 
                    MaKhuyenMai LIKE ? 
                    OR TenKhuyenMai LIKE ?
                    """;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dsKhuyenMai.add(getData(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsKhuyenMai;
    }

    public ArrayList<KhuyenMai> filterKhuyenMai(String maKM, String tenKM, Double heSoKhuyenMai, LocalDate ngayBatDau, LocalDate ngayKetThuc, Boolean trangThai) {
        ArrayList<KhuyenMai> list = new ArrayList<>();

        String sql = """
                SELECT * FROM KhuyenMai
                WHERE ( ? = '' OR MaKhuyenMai LIKE ? )
                    AND ( ? = '' OR TenKhuyenMai LIKE ? )
                    AND ( ? IS NULL OR HeSoKhuyenMai = ? )
                    AND ( ? IS NULL OR NgayBatDau >= ? )
                    AND ( ? IS NULL OR NgayKetThuc <= ? )
                    AND ( ? IS NULL OR TrangThai = ? )
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKM == null ? "" : maKM);
            ps.setString(2, "%" + (maKM == null ? "" : maKM) + "%");

            ps.setString(3, tenKM == null ? "" : tenKM);
            ps.setString(4, "%" + (tenKM == null ? "" : tenKM) + "%");

            if (heSoKhuyenMai == null) {
                ps.setNull(5, java.sql.Types.DOUBLE);
                ps.setNull(6, java.sql.Types.DOUBLE);
            } else {
                ps.setDouble(5, heSoKhuyenMai);
                ps.setDouble(6, heSoKhuyenMai);
            }

            if (ngayBatDau == null) {
                ps.setNull(7, java.sql.Types.DATE);
                ps.setNull(8, java.sql.Types.DATE);
            } else {
                ps.setDate(7, java.sql.Date.valueOf(ngayBatDau));
                ps.setDate(8, java.sql.Date.valueOf(ngayBatDau));
            }

            if (ngayKetThuc == null) {
                ps.setNull(9, java.sql.Types.DATE);
                ps.setNull(10, java.sql.Types.DATE);
            } else {
                ps.setDate(9, java.sql.Date.valueOf(ngayKetThuc));
                ps.setDate(10, java.sql.Date.valueOf(ngayKetThuc));
            }

            if (trangThai == null) {
                ps.setNull(11, java.sql.Types.BOOLEAN);
                ps.setNull(12, java.sql.Types.BOOLEAN);
            } else {
                ps.setBoolean(11, trangThai);
                ps.setBoolean(12, trangThai);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(getData(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public KhuyenMai getData(ResultSet rs) throws SQLException, Exception {
        String maKM = rs.getString("MaKhuyenMai");
        String tenKM = rs.getString("TenKhuyenMai");
        double heSo = rs.getDouble("HeSoKhuyenMai");
        LocalDate ngayBatDau = rs.getDate("NgayBatDau").toLocalDate();
        LocalDate ngayKetThuc = rs.getDate("NgayKetThuc").toLocalDate();
        double tongTienToiThieu = rs.getDouble("TongTienToiThieu");
        double tienKMToiDa = rs.getDouble("TienKhuyenMaiToiDa");
        boolean trangThai = rs.getBoolean("TrangThai");

        return new KhuyenMai(maKM, tenKM, heSo, ngayBatDau, ngayKetThuc, tongTienToiThieu, tienKMToiDa, trangThai);

    }

    public ArrayList<KhuyenMai> filterByTrangThai(String trangThai) {
        ArrayList<KhuyenMai> dsKM = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE TrangThai = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1, trangThai.equalsIgnoreCase("Còn hiệu lực"));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                dsKM.add(getData(rs));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dsKM;
    }

    public String getLastMaKhuyenMai() {
        String sql = "SELECT TOP 1 MaKhuyenMai FROM KhuyenMai ORDER BY MaKhuyenMai DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("MaKhuyenMai");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(Object object) {
        KhuyenMai km = (KhuyenMai) object;
        String sql = """
        INSERT INTO KhuyenMai (MaKhuyenMai, TenKhuyenMai, HeSoKhuyenMai, NgayBatDau, NgayKetThuc, TongTienToiThieu, TienKhuyenMaiToiDa, TrangThai)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, km.getMaKhuyenMai());
            ps.setString(2, km.getTenKhuyenMai());
            ps.setDouble(3, km.getHeSoKhuyenMai());
            ps.setDate(4, java.sql.Date.valueOf(km.getNgayBatDau()));
            ps.setDate(5, java.sql.Date.valueOf(km.getNgayKetThuc()));
            ps.setDouble(6, km.getTongTienToiThieu());
            ps.setDouble(7, km.getTienKhuyenMaiToiDa());
            ps.setBoolean(8, km.getTrangThai());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean update(String id, Object newObject) {
        KhuyenMai km = (KhuyenMai) newObject;
        String sql = """
        UPDATE KhuyenMai 
        SET TenKhuyenMai = ?, HeSoKhuyenMai = ?, NgayBatDau = ?, NgayKetThuc = ?, 
            TongTienToiThieu = ?, TienKhuyenMaiToiDa = ?, TrangThai = ?
        WHERE MaKhuyenMai = ?
    """;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, km.getTenKhuyenMai());
            ps.setDouble(2, km.getHeSoKhuyenMai());
            ps.setDate(3, java.sql.Date.valueOf(km.getNgayBatDau()));
            ps.setDate(4, java.sql.Date.valueOf(km.getNgayKetThuc()));
            ps.setDouble(5, km.getTongTienToiThieu());
            ps.setDouble(6, km.getTienKhuyenMaiToiDa());
            ps.setBoolean(7, km.getTrangThai());
            ps.setString(8, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
