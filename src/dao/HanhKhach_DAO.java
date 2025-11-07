/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import static database.ConnectDB.conn;
import entity.HanhKhach;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CÔNG HOÀNG
 */
public class HanhKhach_DAO {

    public HanhKhach getOne(String id) {
        HanhKhach hanhKhach = null;
        String sql = "SELECT * FROM HanhKhach WHERE maHanhKhach = ?";
        try { 
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
               hanhKhach = getData(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hanhKhach;
    }

    public List<HanhKhach> getAll() {
        ArrayList<HanhKhach> dsHanhKhach = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM HanhKhach");
            while (rs.next()) {
                dsHanhKhach.add(getData(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsHanhKhach;
    }

    public ArrayList<HanhKhach> getHanhKhachByKeyword(String keyword) {
        ArrayList<HanhKhach> dsHanhKhach = new ArrayList<>();
        String sql = """
            SELECT * FROM HanhKhach 
            WHERE MaHanhKhach LIKE ? 
               OR TenHanhKhach LIKE ?
               OR CCCD LIKE ?
        """;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dsHanhKhach.add(getData(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsHanhKhach;
    }

    // Lọc hành khách theo nhiều tiêu chí
    public ArrayList<HanhKhach> filterHanhKhach(String maHanhKhach, String tenHanhKhach, String cccd, LocalDate ngaySinh) {
        ArrayList<HanhKhach> list = new ArrayList<>();

        String sql = """
            SELECT * FROM HanhKhach
            WHERE ( ? = '' OR MaHanhKhach LIKE ? )
              AND ( ? = '' OR TenHanhKhach LIKE ? )
              AND ( ? = '' OR CCCD LIKE ? )
              AND ( ? IS NULL OR NgaySinh = ? )
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHanhKhach == null ? "" : maHanhKhach);
            ps.setString(2, "%" + (maHanhKhach == null ? "" : maHanhKhach) + "%");

            ps.setString(3, tenHanhKhach == null ? "" : tenHanhKhach);
            ps.setString(4, "%" + (tenHanhKhach == null ? "" : tenHanhKhach) + "%");

            ps.setString(5, cccd == null ? "" : cccd);
            ps.setString(6, "%" + (cccd == null ? "" : cccd) + "%");

            if (ngaySinh == null) {
                ps.setNull(7, java.sql.Types.DATE);
                ps.setNull(8, java.sql.Types.DATE);
            } else {
                ps.setDate(7, java.sql.Date.valueOf(ngaySinh));
                ps.setDate(8, java.sql.Date.valueOf(ngaySinh));
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

    // Lấy dữ liệu từ ResultSet chuyển thành đối tượng HanhKhach
    private HanhKhach getData(ResultSet rs) throws SQLException, Exception {
        String maHanhKhach = rs.getString("MaHanhKhach");
        String tenHanhKhach = rs.getString("TenHanhKhach");
        String cccd = rs.getString("CCCD");
        LocalDate ngaySinh = rs.getDate("NgaySinh").toLocalDate();

        HanhKhach hk = new HanhKhach();
        hk.setMaHanhKhach(maHanhKhach);
        hk.setTenHanhKhach(tenHanhKhach);
        hk.setCccd(cccd);
        hk.setNgaySinh(ngaySinh);

        return hk;
    }
    
    // Thêm vào HanhKhach_DAO.java

    public String generateID() {
        String newID = "HK-001";
        String sql = "SELECT TOP 1 maHanhKhach FROM HanhKhach ORDER BY maHanhKhach DESC";

        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                String lastID = rs.getString("maHanhKhach");
                int num = Integer.parseInt(lastID.substring(3));
                newID = String.format("HK-%03d", num + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newID;
    }

    public Boolean create(HanhKhach object) {
    int n = 0;
    String sql = "INSERT INTO HanhKhach (maHanhKhach, tenHanhKhach, cccd, ngaySinh) VALUES (?, ?, ?, ?)";
    
    try {
        PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
        st.setString(1, object.getMaHanhKhach());
        st.setString(2, object.getTenHanhKhach());
        st.setString(3, object.getCccd());
        if(object.getNgaySinh() == null) {
            st.setDate(4, null);
        } else {
            st.setDate(4, java.sql.Date.valueOf(object.getNgaySinh()));
        }
       
        n = st.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return n > 0;
}
}
