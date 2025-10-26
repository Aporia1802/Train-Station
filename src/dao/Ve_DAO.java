/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.*;
import enums.TrangThaiGhe;
import interfaces.DAOBase;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ve_DAO implements DAOBase {

    private static final Map<String, Field> fieldCache = new HashMap<>();

    public ArrayList<Ve> search(String maVe, String hoTen, String cccd, LocalDate ngayDi) {
        ArrayList<Ve> dsVe = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT v.maVe, v.trangThai, v.giaVe, " +
            "hk.maHanhKhach, hk.tenHanhKhach, hk.cccd, hk.ngaySinh, " +
            "g.maGhe, g.soGhe, g.trangThaiGhe, " +
            "lg.maLoaiGhe, lg.tenLoaiGhe, lg.heSoGhe, " +
            "t.maTau, t.tenTau, " +
            "ct.maChuyenTau, ct.thoiGianDi, ct.thoiGianDen, " +
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
            
            System.out.println("🔍 SQL: " + sql.toString());
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Ve ve = getDataSimple(rs);
                if (ve != null) {
                    dsVe.add(ve);
                }
            }
            
            System.out.println("✅ Tìm được " + dsVe.size() + " vé");
            
            rs.close();
            ps.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return dsVe;
    }

    /**
     * ✅ Parse dữ liệu từ ResultSet (dùng reflection để bypass validation)
     */
    private Ve getDataSimple(ResultSet rs) {
        try {
            Ve ve = new Ve();
            
            // Thông tin vé
            setField(ve, "maVe", rs.getString("maVe"));
            setField(ve, "trangThai", rs.getString("trangThai"));
            setField(ve, "giaVe", rs.getDouble("giaVe"));
            
            // Hành khách
            HanhKhach hk = new HanhKhach();
            setField(hk, "maHanhKhach", rs.getString("maHanhKhach"));
            setField(hk, "tenHanhKhach", rs.getString("tenHanhKhach"));
            setField(hk, "cccd", rs.getString("cccd"));
            int namSinh = rs.getInt("ngaySinh");
            LocalDate ngaySinh = LocalDate.of(namSinh, 1, 1); // 01/01/năm
            setField(hk, "ngaySinh", ngaySinh);
        
            setField(ve, "hanhKhach", hk);
            
            // Loại ghế
            LoaiGhe loaiGhe = new LoaiGhe();
            setField(loaiGhe, "maLoaiGhe", rs.getString("maLoaiGhe"));
            setField(loaiGhe, "tenLoaiGhe", rs.getString("tenLoaiGhe"));
            setField(loaiGhe, "heSoLoaiGhe", rs.getDouble("heSoGhe"));
            
            // Ghế
            Ghe ghe = new Ghe();
        setField(ghe, "maGhe", rs.getString("maGhe"));
        setField(ghe, "soGhe", rs.getInt("soGhe"));
        String trangThaiGheStr = rs.getString("trangThaiGhe");
        TrangThaiGhe trangThaiGhe = TrangThaiGhe.fromString(trangThaiGheStr);
        setField(ghe, "trangThaiGhe", trangThaiGhe);
        
        setField(ghe, "loaiGhe", loaiGhe);
        setField(ve, "ghe", ghe);
            
            // Tàu
            Tau tau = new Tau();
            setField(tau, "maTau", rs.getString("maTau"));
            setField(tau, "tenTau", rs.getString("tenTau"));
            
            // Ga đi
            GaTau gaDi = new GaTau();
            setField(gaDi, "maGa", rs.getString("maGaDi"));
            setField(gaDi, "tenGa", rs.getString("tenGaDi"));
            
            // Ga đến
            GaTau gaDen = new GaTau();
            setField(gaDen, "maGa", rs.getString("maGaDen"));
            setField(gaDen, "tenGa", rs.getString("tenGaDen"));
            
            // Tuyến đường
            TuyenDuong td = new TuyenDuong();
            setField(td, "gaDi", gaDi);
            setField(td, "gaDen", gaDen);
            
            // Chuyến tàu
            ChuyenTau ct = new ChuyenTau();
            setField(ct, "maChuyenTau", rs.getString("maChuyenTau"));
            setField(ct, "thoiGianDi", rs.getTimestamp("thoiGianDi").toLocalDateTime());
            setField(ct, "thoiGianDen", rs.getTimestamp("thoiGianDen").toLocalDateTime());
            setField(ct, "tau", tau);
            setField(ct, "tuyenDuong", td);
            
            setField(ve, "chuyenDi", ct);
            
            return ve;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * ✅ Helper method: Set field trực tiếp bằng reflection (có cache)
     */
    private void setField(Object obj, String fieldName, Object value) {
        try {
            String key = obj.getClass().getName() + "." + fieldName;
            Field field = fieldCache.get(key);
            
            if (field == null) {
                field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                fieldCache.put(key, field);
            }
            
            field.set(obj, value);
            
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("⚠️ Không thể set field '" + fieldName + "' cho " + 
                obj.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    @Override
    public Ve getOne(String id) {
        ArrayList<Ve> result = search(id, null, null, null);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public ArrayList<Ve> getAll() {
        return search(null, null, null, null);
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean update(String id, Object newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}