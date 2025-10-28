package dao;

import database.ConnectDB;
import entity.*;
import enums.TrangThaiGhe;
import enums.TrangThaiTau;
import enums.TrangThaiVe;
import interfaces.DAOBase;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO for Ve entity - uses reflection to bypass constructor validation
 * @author CÔNG HOÀNG
 */
public class Ve_DAO implements DAOBase<Ve> {

    private static final Map<String, Field> fieldCache = new HashMap<>();

    public ArrayList<Ve> search(String maVe, String hoTen, String cccd, LocalDate ngayDi) {
        ArrayList<Ve> dsVe = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT v.maVe, v.trangThai AS trangThaiVe, v.giaVe, " +
            "hk.maHanhKhach, hk.tenHanhKhach, hk.cccd, hk.ngaySinh, " +
            "g.maGhe, g.soGhe, g.trangThaiGhe, " +
            "lg.maLoaiGhe, lg.tenLoaiGhe, lg.heSoGhe, " +
            "t.maTau, t.tenTau, " +
            "ct.maChuyenTau, ct.thoiGianDi, ct.thoiGianDen, " +
            "tt.soHieuToa, " +
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

    private Ve getDataSimple(ResultSet rs) {
        try {
            Ve ve = new Ve();
            
            // Thông tin vé
            setField(ve, "maVe", rs.getString("maVe"));
            setField(ve, "giaVe", rs.getDouble("giaVe"));
            
            // Trạng thái vé 
            int trangThaiVeInt = rs.getInt("trangThaiVe");
            TrangThaiVe trangThaiVe = TrangThaiVe.fromInt(trangThaiVeInt);
            setField(ve, "trangThai", trangThaiVe);
            
            // Hành khách
            HanhKhach hk = new HanhKhach();
            setField(hk, "maHanhKhach", rs.getString("maHanhKhach"));
            setField(hk, "tenHanhKhach", rs.getString("tenHanhKhach"));
            setField(hk, "cccd", rs.getString("cccd"));
            
            Date ngaySinhDate = rs.getDate("ngaySinh");
            if (ngaySinhDate != null) {
                setField(hk, "ngaySinh", ngaySinhDate.toLocalDate());
            }
            
            setField(ve, "hanhKhach", hk);
            
            // Loại ghế
            LoaiGhe loaiGhe = new LoaiGhe();
            setField(loaiGhe, "maLoaiGhe", rs.getString("maLoaiGhe"));
            setField(loaiGhe, "tenLoaiGhe", rs.getString("tenLoaiGhe"));
            setField(loaiGhe, "heSoLoaiGhe", rs.getDouble("heSoGhe"));
            
            // Toa tàu (để lấy số hiệu toa)
            ToaTau toaTau = new ToaTau();
            setField(toaTau, "soHieuToa", rs.getInt("soHieuToa"));
            
            // Khoang tàu
            KhoangTau khoangTau = new KhoangTau();
            setField(khoangTau, "toaTau", toaTau);
            
            // Ghế
            Ghe ghe = new Ghe();
            setField(ghe, "maGhe", rs.getString("maGhe"));
            setField(ghe, "soGhe", rs.getInt("soGhe"));
            
            int trangThaiGheInt = rs.getInt("trangThaiGhe");
            TrangThaiGhe trangThaiGhe = TrangThaiGhe.fromInt(trangThaiGheInt);
            setField(ghe, "trangThaiGhe", trangThaiGhe);
            
            setField(ghe, "loaiGhe", loaiGhe);
            setField(ghe, "khoangTau", khoangTau);
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
            
            setField(ve, "chuyenTau", ct);
            
            return ve;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 
    private void setField(Object obj, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException {
        try {
            String key = obj.getClass().getName() + "." + fieldName;
            Field field = fieldCache.get(key);
            
            if (field == null) {
                field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                fieldCache.put(key, field);
            }
            
            field.set(obj, value);
            
        } catch (NoSuchFieldException e) {
            
        } 
    }

    public ArrayList<Ve> timKiemVe(String maVe, String hoTen, String cccd, LocalDate ngayDi) {
        return search(maVe, hoTen, cccd, ngayDi);
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
        int n = 0;
        String sql = "UPDATE Ve SET trangThai = ? WHERE maVe = ?";
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setInt(1, 3); 
            st.setString(2, maVe);
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }
}