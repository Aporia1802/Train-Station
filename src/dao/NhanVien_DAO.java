/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import static database.ConnectDB.conn;
import entity.NhanVien;
import interfaces.DAOBase;
import java.sql.Statement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.SQLException;


/**
 *
 * @author CÔNG HOÀNG
 */
public class NhanVien_DAO implements DAOBase<NhanVien> {

    @Override
    public NhanVien getOne(String id) {
        NhanVien nhanVien = null;

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("Select * from NhanVien where maNV = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String tenNV = rs.getString("tenNV");
                Boolean gioiTinh = rs.getBoolean("gioiTinh");
                LocalDate ngaySinh = rs.getDate("ngaySinh").toLocalDate();
                String email = rs.getString("email");
                String soDienThoai = rs.getString("soDienThoai");
                String cccd = rs.getString("cccd");
                String chucVu = rs.getString("chucVu");
                Boolean trangThai = rs.getBoolean("trangThai");
                String diaChi = rs.getString("diaChi");

                nhanVien = new NhanVien(id, tenNV, gioiTinh, ngaySinh, email, soDienThoai, cccd, diaChi, chucVu, trangThai);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nhanVien;
    }
    
    

    @Override
    public ArrayList<NhanVien> getAll() {
        ArrayList<NhanVien> dsNV = new ArrayList<>();
        try {
            String sql = "SELECT * FROM NhanVien";
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                dsNV.add(getData(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsNV;
    }
    
    public NhanVien getData(ResultSet rs) throws SQLException, Exception {
    // Lấy dữ liệu từng cột trong ResultSet
        String maNV = rs.getString("maNV");
        String tenNV = rs.getString("tenNV");
        boolean gioiTinh = rs.getBoolean("gioiTinh");
        LocalDate ngaySinh = rs.getDate("ngaySinh").toLocalDate();
        String email = rs.getString("email");
        String soDienThoai = rs.getString("soDienThoai");
        String cccd = rs.getString("cccd");
        String diaChi = rs.getString("diaChi");
        String chucVu = rs.getString("chucVu");
        boolean trangThai = rs.getBoolean("trangThai");

    // Trả về đối tượng NhanVien
        return new NhanVien(maNV, tenNV, gioiTinh, ngaySinh, email, soDienThoai, cccd, diaChi, chucVu, trangThai);
}
    
    public ArrayList<NhanVien> filterByComboBox(String chucVu, String trangThai) {
       ArrayList<NhanVien> dsNV = new ArrayList<>();
       StringBuilder sql = new StringBuilder("SELECT * FROM NhanVien WHERE 1=1");

       // Nếu chọn chức vụ thực sự
       if (chucVu != null && !chucVu.equals("Chức vụ")) {
          sql.append(" AND LTRIM(RTRIM(chucVu)) LIKE ?");

       }

       // Nếu chọn trạng thái thực sự
       if (trangThai != null && !trangThai.equals("Trạng thái")) {
           sql.append(" AND trangThai = ?");
       }

       try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
           int index = 1;

           if (chucVu != null && !chucVu.equals("Chức vụ")) {
               ps.setString(index++, "%" + chucVu.trim() + "%");
           }

           if (trangThai != null && !trangThai.equals("Trạng thái")) {
               // Chuyển "Đang làm" -> true, "Đã nghỉ" -> false
               ps.setBoolean(index++, trangThai.equals("Đang làm"));
           }

           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
               dsNV.add(getData(rs));
           }

       } catch (Exception e) {
           e.printStackTrace();
       }

       return dsNV;
   }


    public ArrayList<NhanVien> getNhanVienBySoDienThoai(String soDienThoai) {
        ArrayList<NhanVien> dsNhanVien = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE soDienThoai LIKE ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + soDienThoai + "%"); // tìm gần đúng (có thể nằm giữa)

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dsNhanVien.add(getData(rs)); // getData là hàm chuyển ResultSet → đối tượng NhanVien
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsNhanVien;
}
   public ArrayList<NhanVien> timKiemNhanVien(String maNV, String tenNV, String cccd, String sdt, String gioiTinh, String trangThai) {
    ArrayList<NhanVien> dsNhanVien = new ArrayList<>();
    StringBuilder sql = new StringBuilder("SELECT * FROM NhanVien WHERE 1=1");

    // Chỉ thêm điều kiện khi người dùng có nhập dữ liệu
    if (maNV != null && !maNV.trim().isEmpty()) {
        sql.append(" AND maNV LIKE ?");
    }
    if (tenNV != null && !tenNV.trim().isEmpty()) {
        sql.append(" AND tenNV LIKE ?");
    }
    if (cccd != null && !cccd.trim().isEmpty()) {
        sql.append(" AND cccd LIKE ?");
    }
    if (sdt != null && !sdt.trim().isEmpty()) {
        sql.append(" AND soDienThoai LIKE ?");
    }
    if (gioiTinh != null && !gioiTinh.equals("Tất cả")) {
        sql.append(" AND gioiTinh = ?");
    }
    if (trangThai != null && !trangThai.equals("Tất cả")) {
        sql.append(" AND trangThai = ?");
    }

    try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
        int index = 1;

        if (maNV != null && !maNV.trim().isEmpty()) {
            ps.setString(index++, "%" + maNV.trim() + "%");
        }
        if (tenNV != null && !tenNV.trim().isEmpty()) {
            ps.setString(index++, "%" + tenNV.trim() + "%");
        }
        if (cccd != null && !cccd.trim().isEmpty()) {
            ps.setString(index++, "%" + cccd.trim() + "%");
        }
        if (sdt != null && !sdt.trim().isEmpty()) {
            ps.setString(index++, "%" + sdt.trim() + "%");
        }
        if (gioiTinh != null && !gioiTinh.equals("Tất cả")) {
            ps.setBoolean(index++, gioiTinh.equalsIgnoreCase("Nam"));
        }
        if (trangThai != null && !trangThai.equals("Tất cả")) {
            ps.setBoolean(index++, trangThai.equals("Đang làm"));
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            dsNhanVien.add(getData(rs));
        }
        rs.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    return dsNhanVien;
}



    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(NhanVien object) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean update(String id, NhanVien newObject) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(
                    "UPDATE NhanVien SET tenNV=?, ngaySinh=?, email=?, diaChi=? WHERE maNV=?");
            st.setString(1, newObject.getTenNV());
            st.setDate(2, Date.valueOf(newObject.getNgaySinh()));
            st.setString(3, newObject.getEmail());
            st.setString(4, newObject.getDiaChi());
            st.setString(5, id);
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
