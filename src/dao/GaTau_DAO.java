package dao;

import database.ConnectDB;
import entity.GaTau;
import interfaces.DAOBase;
import java.sql.*;
import java.util.ArrayList;

public class GaTau_DAO implements DAOBase {

    @Override
    public GaTau getOne(String id) {
        String sql = "SELECT * FROM GaTau WHERE maGa = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, id);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                GaTau ga = getData(rs);
                rs.close();
                ps.close();
                return ga;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
public ArrayList<GaTau> getGaTauByKeyword(String keyword) {
        ArrayList<GaTau> dsGaTau = new ArrayList<>();
        String sql = "SELECT * FROM GaTau WHERE maGa LIKE ? OR tenGa LIKE ? OR diaChi LIKE ? OR soDienThoai LIKE ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                GaTau ga = getData(rs);
                if (ga != null) {
                    dsGaTau.add(ga);
                }
            }
            
            rs.close();
            ps.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return dsGaTau;
    }
    @Override
    public ArrayList<GaTau> getAll() {
        ArrayList<GaTau> dsGaTau = new ArrayList<>();
        String sql = "SELECT * FROM GaTau";
        
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                GaTau ga = getData(rs);
                if (ga != null) {
                    dsGaTau.add(ga);
                }
            }
            
            rs.close();
            st.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return dsGaTau;
    }

    public GaTau getData(ResultSet rs) {
        try {
            String maGa = rs.getString("maGa");
            String tenGa = rs.getString("tenGa");
            String diaChi = rs.getString("diaChi");
            String soDienThoai = rs.getString("soDienThoai");
            
            GaTau ga = new GaTau(maGa);
            
            try {
                if (tenGa != null && !tenGa.trim().isEmpty()) {
                    ga.setTenGa(tenGa);
                }
                if (diaChi != null && !diaChi.trim().isEmpty()) {
                    ga.setDiaChi(diaChi);
                }
                if (soDienThoai != null && !soDienThoai.trim().isEmpty()) {
                    ga.setSoDienThoai(soDienThoai);
                }
            } catch (Exception e) {
                System.err.println("⚠️ Lỗi set thuộc tính ga " + maGa + ": " + e.getMessage());
            }
            
            return ga;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean create(Object object) {
        GaTau ga = (GaTau) object;
        String sql = "INSERT INTO GaTau (maGa, tenGa, diaChi, soDienThoai) VALUES (?, ?, ?, ?)";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, ga.getMaGa());
            ps.setString(2, ga.getTenGa());
            ps.setString(3, ga.getDiaChi());
            ps.setString(4, ga.getSoDienThoai());
            
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
        GaTau ga = (GaTau) newObject;
        String sql = "UPDATE GaTau SET tenGa = ?, diaChi = ?, soDienThoai = ? WHERE maGa = ?";
        
        try {
            PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
            ps.setString(1, ga.getTenGa());
            ps.setString(2, ga.getDiaChi());
            ps.setString(3, ga.getSoDienThoai());
            ps.setString(4, id);
            
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
        String sql = "DELETE FROM GaTau WHERE maGa = ?";
        
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

 public String generateID(String tenGa) {
    if (tenGa == null || tenGa.trim().isEmpty()) {
        return "GA-XXX";
    }
    
    // ✅ Loại bỏ từ "Ga" ở đầu (case-insensitive)
    String cleaned = tenGa.trim();
    if (cleaned.toLowerCase().startsWith("ga ")) {
        cleaned = cleaned.substring(3).trim(); // Bỏ 3 ký tự "Ga "
    } else if (cleaned.toLowerCase().startsWith("gà ")) {
        cleaned = cleaned.substring(3).trim(); // Bỏ "Gà " (có dấu)
    }
    
    // Nếu sau khi bỏ "Ga" mà rỗng → trả về mã mặc định
    if (cleaned.isEmpty()) {
        return "GA-XXX";
    }
    
    // Bỏ dấu, uppercase
    String normalized = removeDiacritics(cleaned);
    
    // Lấy chữ cái đầu mỗi từ
    String[] words = normalized.split("\\s+");
    StringBuilder maGa = new StringBuilder("GA-");
    
    for (String word : words) {
        if (!word.isEmpty()) {
            maGa.append(word.charAt(0));
        }
    }
    
    String result = maGa.toString().toUpperCase();
    
    // Kiểm tra trùng mã
    if (checkMaGaExists(result)) {
        int counter = 2;
        while (checkMaGaExists(result + "-" + counter)) {
            counter++;
        }
        result = result + "-" + counter;
    }
    
    return result;
}

private boolean checkMaGaExists(String maGa) {
    String sql = "SELECT COUNT(*) FROM GaTau WHERE maGa = ?";
    try {
        PreparedStatement ps = ConnectDB.conn.prepareStatement(sql);
        ps.setString(1, maGa);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            boolean exists = rs.getInt(1) > 0;
            rs.close();
            ps.close();
            return exists;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

private String removeDiacritics(String str) {
    String result = str;
    
    String[][] mapping = {
        {"à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a"},
        {"è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e"},
        {"ì|í|ị|ỉ|ĩ", "i"},
        {"ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o"},
        {"ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u"},
        {"ỳ|ý|ỵ|ỷ|ỹ", "y"},
        {"đ", "d"},
        {"À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ", "A"},
        {"È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ", "E"},
        {"Ì|Í|Ị|Ỉ|Ĩ", "I"},
        {"Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ", "O"},
        {"Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ", "U"},
        {"Ỳ|Ý|Ỵ|Ỷ|Ỹ", "Y"},
        {"Đ", "D"}
    };
    
    for (String[] pair : mapping) {
        result = result.replaceAll(pair[0], pair[1]);
    }
    
    return result;
}
/**
 *  Lọc ga tàu theo nhiều điều kiện
 */
public ArrayList<GaTau> filter(String maGa, String tenGa, String diaChi, String soDienThoai) {
    ArrayList<GaTau> dsGaTau = new ArrayList<>();
    StringBuilder sql = new StringBuilder("SELECT * FROM GaTau WHERE 1=1");
    ArrayList<String> params = new ArrayList<>();
    

    if (maGa != null && !maGa.trim().isEmpty()) {
        sql.append(" AND maGa LIKE ?");
        params.add("%" + maGa + "%");
    }
    
    if (tenGa != null && !tenGa.trim().isEmpty()) {
        sql.append(" AND tenGa LIKE ?");
        params.add("%" + tenGa + "%");
    }
    
    if (diaChi != null && !diaChi.trim().isEmpty()) {
        sql.append(" AND diaChi LIKE ?");
        params.add("%" + diaChi + "%");
    }
    
    if (soDienThoai != null && !soDienThoai.trim().isEmpty()) {
        sql.append(" AND soDienThoai LIKE ?");
        params.add("%" + soDienThoai + "%");
    }
    
    try {
        PreparedStatement ps = ConnectDB.conn.prepareStatement(sql.toString());

        for (int i = 0; i < params.size(); i++) {
            ps.setString(i + 1, params.get(i));
        }
        
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            GaTau ga = getData(rs);
            if (ga != null) {
                dsGaTau.add(ga);
            }
        }
        
        rs.close();
        ps.close();
        
    } catch (Exception e) {
        e.printStackTrace();
    }
    return dsGaTau;
}

    @Override
    public String generateID() {
        return "GA-XXX";
    }


}