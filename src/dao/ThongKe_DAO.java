/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 *
 * @author CÔNG HOÀNG
 */

public class ThongKe_DAO {
    
    // Lấy số vé bán được trong 7 ngày gần nhất
    public Map<String, Integer> getSoVeBan7NgayGanNhat() {
        Map<String, Integer> ketQua = new LinkedHashMap<>();
        
        try {
            String sql = "SELECT CONVERT(VARCHAR(10), ngayLapHoaDon, 23) AS ngay, " +
                        "COUNT(v.maVe) AS soVe " +
                        "FROM HoaDon hd " +
                        "JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                        "WHERE ngayLapHoaDon >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE)) " +
                        "AND ngayLapHoaDon < DATEADD(DAY, 1, CAST(GETDATE() AS DATE)) " +
                        "GROUP BY CONVERT(VARCHAR(10), ngayLapHoaDon, 23) " +
                        "ORDER BY ngay ASC";
            
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                String ngay = rs.getString("ngay");
                int soVe = rs.getInt("soVe");
                ketQua.put(ngay, soVe);
            }
            
            rs.close();
            st.close();
            
            // Đảm bảo có đủ 7 ngày (bổ sung các ngày thiếu với giá trị 0)
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            for (int i = 6; i >= 0; i--) {
                cal.setTime(new java.util.Date());
                cal.add(Calendar.DAY_OF_MONTH, -i);
                String ngay = sdf.format(cal.getTime());
                
                if (!ketQua.containsKey(ngay)) {
                    ketQua.put(ngay, 0);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ketQua;
    }
    
    // Lấy số vé bị hủy trong 7 ngày gần nhất
    public Map<String, Integer> getSoVeBiHuy7NgayGanNhat() {
        Map<String, Integer> ketQua = new LinkedHashMap<>();
        
        try {
            String sql = "SELECT CONVERT(VARCHAR(10), ngayLapHoaDon, 23) AS ngay, " +
                        "COUNT(v.maVe) AS soVe " +
                        "FROM HoaDon hd " +
                        "JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                        "WHERE v.trangThai = 3 " +
                        "AND ngayLapHoaDon >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE)) " +
                        "AND ngayLapHoaDon < DATEADD(DAY, 1, CAST(GETDATE() AS DATE)) " +
                        "GROUP BY CONVERT(VARCHAR(10), ngayLapHoaDon, 23) " +
                        "ORDER BY ngay ASC";
            
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                String ngay = rs.getString("ngay");
                int soVe = rs.getInt("soVe");
                ketQua.put(ngay, soVe);
            }
            
            rs.close();
            st.close();
            
            // Đảm bảo có đủ 7 ngày (bổ sung các ngày thiếu với giá trị 0)
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            for (int i = 6; i >= 0; i--) {
                cal.setTime(new java.util.Date());
                cal.add(Calendar.DAY_OF_MONTH, -i);
                String ngay = sdf.format(cal.getTime());
                
                if (!ketQua.containsKey(ngay)) {
                    ketQua.put(ngay, 0);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ketQua;
    }
    
    // Lấy số vé hoàn thành trong 7 ngày gần nhất
    public Map<String, Integer> getSoVeHoanThanh7NgayGanNhat() {
        Map<String, Integer> ketQua = new LinkedHashMap<>();
        
        try {
            String sql = "SELECT CONVERT(VARCHAR(10), ngayLapHoaDon, 23) AS ngay, " +
                        "COUNT(v.maVe) AS soVe " +
                        "FROM HoaDon hd " +
                        "JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                        "WHERE v.trangThai = 2 " +
                        "AND ngayLapHoaDon >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE)) " +
                        "AND ngayLapHoaDon < DATEADD(DAY, 1, CAST(GETDATE() AS DATE)) " +
                        "GROUP BY CONVERT(VARCHAR(10), ngayLapHoaDon, 23) " +
                        "ORDER BY ngay ASC";
            
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                String ngay = rs.getString("ngay");
                int soVe = rs.getInt("soVe");
                ketQua.put(ngay, soVe);
            }
            
            rs.close();
            st.close();
            
            // Đảm bảo có đủ 7 ngày (bổ sung các ngày thiếu với giá trị 0)
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            for (int i = 6; i >= 0; i--) {
                cal.setTime(new java.util.Date());
                cal.add(Calendar.DAY_OF_MONTH, -i);
                String ngay = sdf.format(cal.getTime());
                
                if (!ketQua.containsKey(ngay)) {
                    ketQua.put(ngay, 0);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ketQua;
    }
    
    public Map<String, Double> getTyLeTangTruongDoanhThu7NgayGanNhat() {
        Map<String, Double> ketQua = new LinkedHashMap<>();
        try {
            // Lấy doanh thu theo ngày trong 6 ngày (5 ngày + 1 ngày trước đó để tính tỷ lệ)
            String sql = "SELECT CONVERT(VARCHAR(10), ngayLapHoaDon, 23) AS ngay, " +
                        "SUM(tongTien) AS doanhThu " +
                        "FROM HoaDon " +
                        "WHERE ngayLapHoaDon >= DATEADD(DAY, -5, CAST(GETDATE() AS DATE)) " +
                        "AND ngayLapHoaDon < DATEADD(DAY, 1, CAST(GETDATE() AS DATE)) " +
                        "GROUP BY CONVERT(VARCHAR(10), ngayLapHoaDon, 23) " +
                        "ORDER BY ngay ASC";

            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // Lưu doanh thu theo ngày
            Map<String, Double> doanhThuTheoNgay = new LinkedHashMap<>();
            while (rs.next()) {
                String ngay = rs.getString("ngay");
                double doanhThu = rs.getDouble("doanhThu");
                doanhThuTheoNgay.put(ngay, doanhThu);
            }

            rs.close();
            st.close();

            // Đảm bảo có đủ 6 ngày và tính tỷ lệ tăng trưởng
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // Lấy doanh thu của ngày thứ 6 (ngày -5) để làm cơ sở so sánh
            cal.setTime(new java.util.Date());
            cal.add(Calendar.DAY_OF_MONTH, -5);
            String ngayThu6 = sdf.format(cal.getTime());
            double doanhThuNgayTruoc = doanhThuTheoNgay.getOrDefault(ngayThu6, 0.0);

            // Tính tỷ lệ cho 5 ngày gần nhất (từ ngày -4 đến ngày 0)
            for (int i = 4; i >= 0; i--) {
                cal.setTime(new java.util.Date());
                cal.add(Calendar.DAY_OF_MONTH, -i);
                String ngay = sdf.format(cal.getTime());

                double doanhThuNgayHienTai = doanhThuTheoNgay.getOrDefault(ngay, 0.0);

                // Tính tỷ lệ tăng trưởng so với ngày trước
                double tyLeTangTruong = 0.0;
                if (doanhThuNgayTruoc > 0) {
                    tyLeTangTruong = ((doanhThuNgayHienTai - doanhThuNgayTruoc) / doanhThuNgayTruoc) * 100;
                } else if (doanhThuNgayHienTai > 0) {
                    tyLeTangTruong = 100.0; // Tăng 100% nếu ngày trước = 0
                }
                // Nếu cả 2 ngày đều = 0 thì tyLeTangTruong = 0.0

                ketQua.put(ngay, tyLeTangTruong);
                doanhThuNgayTruoc = doanhThuNgayHienTai; // Cập nhật cho lần lặp tiếp theo
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ketQua;
    }
    
    /**
     * Lấy tỷ lệ phần trăm các loại vé được bán trong 7 ngày gần nhất
     * @return Map với key là tên loại vé, value là phần trăm
     */
    public Map<String, Double> getTyLeLoaiVe7NgayGanNhat() {
        Map<String, Double> ketQua = new LinkedHashMap<>();

        try {
            // Đếm tổng số vé bán được trong 7 ngày
            String sqlTongVe = "SELECT COUNT(v.maVe) AS tongVe " +
                              "FROM HoaDon hd " +
                              "JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                              "WHERE ngayLapHoaDon >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE)) " +
                              "AND ngayLapHoaDon < DATEADD(DAY, 1, CAST(GETDATE() AS DATE))";

            Statement stTong = ConnectDB.conn.createStatement();
            ResultSet rsTong = stTong.executeQuery(sqlTongVe);

            int tongVe = 0;
            if (rsTong.next()) {
                tongVe = rsTong.getInt("tongVe");
            }
            rsTong.close();
            stTong.close();

            // Nếu không có vé nào được bán
            if (tongVe == 0) {
                return ketQua;
            }

            // Đếm số vé theo từng loại
            String sql = "SELECT lv.tenLoaiVe, COUNT(v.maVe) AS soVe " +
                        "FROM HoaDon hd " +
                        "JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                        "JOIN LoaiVe lv ON v.maLoaiVe = lv.maLoaiVe " +
                        "WHERE ngayLapHoaDon >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE)) " +
                        "AND ngayLapHoaDon < DATEADD(DAY, 1, CAST(GETDATE() AS DATE)) " +
                        "GROUP BY lv.tenLoaiVe " +
                        "ORDER BY soVe DESC";

            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String loaiVe = rs.getString("tenLoaiVe");
                int soVe = rs.getInt("soVe");
                double tyLe = (soVe * 100.0) / tongVe;
                ketQua.put(loaiVe, tyLe);
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ketQua;
    }
    
    /**
    * Lấy số lượng vé của từng loại được bán trong 7 ngày gần nhất
    * @return Map với key là tên loại vé, value là số lượng vé
    */
    public Map<String, Integer> getSoLuongLoaiVe7NgayGanNhat() {
        Map<String, Integer> ketQua = new LinkedHashMap<>();

        try {
            String sql = "SELECT lv.tenLoaiVe, COUNT(v.maVe) AS soVe " +
                        "FROM HoaDon hd " +
                        "JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                        "JOIN LoaiVe lv ON v.maLoaiVe = lv.maLoaiVe " +
                        "WHERE ngayLapHoaDon >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE)) " +
                        "AND ngayLapHoaDon < DATEADD(DAY, 1, CAST(GETDATE() AS DATE)) " +
                        "GROUP BY lv.tenLoaiVe " +
                        "ORDER BY soVe DESC";

            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String loaiVe = rs.getString("tenLoaiVe");
                int soVe = rs.getInt("soVe");
                ketQua.put(loaiVe, soVe);
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ketQua;
    }
    
    
    /**
    * Lấy doanh thu theo ngày trong 7 ngày gần nhất
    * @return Map với key là ngày (yyyy-MM-dd), value là doanh thu
    */
    public Map<String, Double> getDoanhThu7NgayGanNhat() {
        Map<String, Double> ketQua = new LinkedHashMap<>();

        try {
            String sql = "SELECT CONVERT(VARCHAR(10), ngayLapHoaDon, 23) AS ngay, " +
                        "SUM(tongTien) AS doanhThu " +
                        "FROM HoaDon " +
                        "WHERE ngayLapHoaDon >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE)) " +
                        "AND ngayLapHoaDon < DATEADD(DAY, 1, CAST(GETDATE() AS DATE)) " +
                        "GROUP BY CONVERT(VARCHAR(10), ngayLapHoaDon, 23) " +
                        "ORDER BY ngay ASC";

            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String ngay = rs.getString("ngay");
                double doanhThu = rs.getDouble("doanhThu");
                ketQua.put(ngay, doanhThu);
            }

            rs.close();
            st.close();

            // Đảm bảo có đủ 7 ngày (bổ sung các ngày thiếu với giá trị 0)
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Map<String, Double> ketQuaDayDu = new LinkedHashMap<>();
            for (int i = 6; i >= 0; i--) {
                cal.setTime(new java.util.Date());
                cal.add(Calendar.DAY_OF_MONTH, -i);
                String ngay = sdf.format(cal.getTime());

                ketQuaDayDu.put(ngay, ketQua.getOrDefault(ngay, 0.0));
            }

            return ketQuaDayDu;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ketQua;
    }
    
    /**
    * Lấy top 5 chuyến đi được ưa thích nhất (bán được nhiều vé nhất) trong 30 ngày gần nhất
    * @return List chứa thông tin top 5 chuyến đi
    */
   public List<Map<String, Object>> getTop5ChuyenDiDuocYeuThich() {
       List<Map<String, Object>> ketQua = new ArrayList<>();

       try {
           String sql = "SELECT TOP 5 " +
                       "ct.maChuyenTau, " +
                       "gaDi.tenGa AS tenGaDi, " +
                       "gaDen.tenGa AS tenGaDen, " +
                       "COUNT(v.maVe) AS soVeBan " +
                       "FROM Ve v " +
                       "JOIN ChuyenTau ct ON v.maChuyenTau = ct.maChuyenTau " +
                       "JOIN TuyenDuong td ON ct.maTuyenDuong = td.maTuyenDuong " +
                       "JOIN GaTau gaDi ON td.gaDi = gaDi.maGa " +
                       "JOIN GaTau gaDen ON td.gaDen = gaDen.maGa " +
                       "JOIN HoaDon hd ON v.maHoaDon = hd.maHoaDon " +
                       "WHERE hd.ngayLapHoaDon >= DATEADD(DAY, -29, CAST(GETDATE() AS DATE)) " +
                       "AND hd.ngayLapHoaDon < DATEADD(DAY, 1, CAST(GETDATE() AS DATE)) " +
                       "AND v.trangThai != 3 " +  // Loại trừ vé bị hủy
                       "GROUP BY ct.maChuyenTau, gaDi.tenGa, gaDen.tenGa " +
                       "ORDER BY soVeBan DESC";

           Statement st = ConnectDB.conn.createStatement();
           ResultSet rs = st.executeQuery(sql);

           while (rs.next()) {
               Map<String, Object> item = new LinkedHashMap<>();
               item.put("maChuyenTau", rs.getString("maChuyenTau"));
               item.put("tenGaDi", rs.getString("tenGaDi"));
               item.put("tenGaDen", rs.getString("tenGaDen"));
               item.put("soVeBan", rs.getInt("soVeBan"));
               ketQua.add(item);
           }

           rs.close();
           st.close();

       } catch (Exception e) {
           e.printStackTrace();
       }

       return ketQua;
   }

   /**
    * Lấy top 5 tuyến đường được ưa thích nhất (bán được nhiều vé nhất) trong 30 ngày gần nhất
    * Phiên bản này gom theo tuyến đường thay vì chuyến tàu cụ thể
    * @return List chứa thông tin top 5 tuyến đường
    */
    public List<Map<String, Object>> getTop5TuyenDuongDuocYeuThich() {
        List<Map<String, Object>> ketQua = new ArrayList<>();

        try {
            String sql = "SELECT TOP 5 " +
                        "td.maTuyenDuong, " +
                        "gaDi.tenGa AS tenGaDi, " +
                        "gaDen.tenGa AS tenGaDen, " +
                        "COUNT(v.maVe) AS soVeBan " +
                        "FROM Ve v " +
                        "JOIN ChuyenTau ct ON v.maChuyenTau = ct.maChuyenTau " +
                        "JOIN TuyenDuong td ON ct.maTuyenDuong = td.maTuyenDuong " +
                        "JOIN GaTau gaDi ON td.gaDi = gaDi.maGa " +
                        "JOIN GaTau gaDen ON td.gaDen = gaDen.maGa " +
                        "JOIN HoaDon hd ON v.maHoaDon = hd.maHoaDon " +
                        "WHERE hd.ngayLapHoaDon >= DATEADD(DAY, -29, CAST(GETDATE() AS DATE)) " +
                        "AND hd.ngayLapHoaDon < DATEADD(DAY, 1, CAST(GETDATE() AS DATE)) " +
                        "AND v.trangThai != 3 " +  // Loại trừ vé bị hủy
                        "GROUP BY td.maTuyenDuong, gaDi.tenGa, gaDen.tenGa " +
                        "ORDER BY soVeBan DESC";

            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("maTuyenDuong", rs.getString("maTuyenDuong"));
                item.put("tenGaDi", rs.getString("tenGaDi"));
                item.put("tenGaDen", rs.getString("tenGaDen"));
                item.put("soVeBan", rs.getInt("soVeBan"));
                ketQua.add(item);
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ketQua;
    }
   
    /**
    * Lấy số lượng hành khách theo ngày trong 7 ngày gần nhất
    * @return Map với key là ngày (yyyy-MM-dd), value là số lượng hành khách
    */
    public Map<String, Integer> getSoLuongHanhKhach7NgayGanNhat() {
        Map<String, Integer> ketQua = new LinkedHashMap<>();

        try {
            String sql = "SELECT CONVERT(VARCHAR(10), hd.ngayLapHoaDon, 23) AS ngay, " +
                        "COUNT(DISTINCT v.maHanhKhach) AS soLuongHanhKhach " +
                        "FROM HoaDon hd " +
                        "JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                        "WHERE hd.ngayLapHoaDon >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE)) " +
                        "AND hd.ngayLapHoaDon < DATEADD(DAY, 1, CAST(GETDATE() AS DATE)) " +
                        "AND v.trangThai != 3 " +  // Loại trừ vé bị hủy
                        "GROUP BY CONVERT(VARCHAR(10), hd.ngayLapHoaDon, 23) " +
                        "ORDER BY ngay ASC";

            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String ngay = rs.getString("ngay");
                int soLuong = rs.getInt("soLuongHanhKhach");
                ketQua.put(ngay, soLuong);
            }

            rs.close();
            st.close();

            // Đảm bảo có đủ 7 ngày (bổ sung các ngày thiếu với giá trị 0)
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Map<String, Integer> ketQuaDayDu = new LinkedHashMap<>();
            for (int i = 6; i >= 0; i--) {
                cal.setTime(new java.util.Date());
                cal.add(Calendar.DAY_OF_MONTH, -i);
                String ngay = sdf.format(cal.getTime());

                ketQuaDayDu.put(ngay, ketQua.getOrDefault(ngay, 0));
            }

            return ketQuaDayDu;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ketQua;
    }

    /**
     * Lấy tổng số lượng hành khách trong 7 ngày gần nhất
     * @return Tổng số hành khách
     */
    public int getTongSoLuongHanhKhach7NgayGanNhat() {
        int tongSoLuong = 0;

        try {
            String sql = "SELECT COUNT(DISTINCT v.maHanhKhach) AS tongHanhKhach " +
                        "FROM HoaDon hd " +
                        "JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                        "WHERE hd.ngayLapHoaDon >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE)) " +
                        "AND hd.ngayLapHoaDon < DATEADD(DAY, 1, CAST(GETDATE() AS DATE)) " +
                        "AND v.trangThai != 3";

            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                tongSoLuong = rs.getInt("tongHanhKhach");
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tongSoLuong;
    }
    
    // Thêm các phương thức sau vào class ThongKe_DAO

    /**
     * Lấy doanh thu theo tháng trong năm hiện tại
     * @return Map với key là tháng (01-12), value là doanh thu
     */
    public Map<String, Double> getDoanhThuTheoThang() {
        Map<String, Double> ketQua = new LinkedHashMap<>();

        try {
            String sql = "SELECT FORMAT(ngayLapHoaDon, 'MM') AS thang, " +
                        "SUM(tongTien) AS doanhThu " +
                        "FROM HoaDon " +
                        "WHERE YEAR(ngayLapHoaDon) = YEAR(GETDATE()) " +
                        "GROUP BY FORMAT(ngayLapHoaDon, 'MM') " +
                        "ORDER BY thang ASC";

            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String thang = rs.getString("thang");
                double doanhThu = rs.getDouble("doanhThu");
                ketQua.put(thang, doanhThu);
            }

            rs.close();
            st.close();

            // Đảm bảo có đủ 12 tháng
            for (int i = 1; i <= 12; i++) {
                String thang = String.format("%02d", i);
                if (!ketQua.containsKey(thang)) {
                    ketQua.put(thang, 0.0);
                }
            }

            // Sắp xếp lại theo thứ tự tháng
            Map<String, Double> ketQuaSorted = new LinkedHashMap<>();
            for (int i = 1; i <= 12; i++) {
                String thang = String.format("%02d", i);
                ketQuaSorted.put(thang, ketQua.get(thang));
            }

            return ketQuaSorted;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ketQua;
    }

    /**
     * Lấy doanh thu theo năm trong 5 năm gần nhất
     * @return Map với key là năm, value là doanh thu
     */
    public Map<String, Double> getDoanhThuTheoNam() {
        Map<String, Double> ketQua = new LinkedHashMap<>();

        try {
            String sql = "SELECT YEAR(ngayLapHoaDon) AS nam, " +
                        "SUM(tongTien) AS doanhThu " +
                        "FROM HoaDon " +
                        "WHERE YEAR(ngayLapHoaDon) >= YEAR(GETDATE()) - 4 " +
                        "GROUP BY YEAR(ngayLapHoaDon) " +
                        "ORDER BY nam ASC";

            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String nam = String.valueOf(rs.getInt("nam"));
                double doanhThu = rs.getDouble("doanhThu");
                ketQua.put(nam, doanhThu);
            }

            rs.close();
            st.close();

            // Đảm bảo có đủ 5 năm
            Calendar cal = Calendar.getInstance();
            int namHienTai = cal.get(Calendar.YEAR);

            Map<String, Double> ketQuaDayDu = new LinkedHashMap<>();
            for (int i = 4; i >= 0; i--) {
                String nam = String.valueOf(namHienTai - i);
                ketQuaDayDu.put(nam, ketQua.getOrDefault(nam, 0.0));
            }

            return ketQuaDayDu;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ketQua;
    }

    /**
     * Lấy doanh thu theo tháng của một năm cụ thể
     * @param year Năm cần thống kê
     * @return Map với key là tháng (01-12), value là doanh thu
     */
    public Map<String, Double> getDoanhThuTheoThangCuaNam(int year) {
        Map<String, Double> ketQua = new LinkedHashMap<>();

        try {
            String sql = "SELECT FORMAT(ngayLapHoaDon, 'MM') AS thang, " +
                        "SUM(tongTien) AS doanhThu " +
                        "FROM HoaDon " +
                        "WHERE YEAR(ngayLapHoaDon) = ? " +
                        "GROUP BY FORMAT(ngayLapHoaDon, 'MM') " +
                        "ORDER BY thang ASC";

            PreparedStatement pst = ConnectDB.conn.prepareStatement(sql);
            pst.setInt(1, year);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String thang = rs.getString("thang");
                double doanhThu = rs.getDouble("doanhThu");
                ketQua.put(thang, doanhThu);
            }

            rs.close();
            pst.close();

            // Đảm bảo có đủ 12 tháng
            Map<String, Double> ketQuaDayDu = new LinkedHashMap<>();
            for (int i = 1; i <= 12; i++) {
                String thang = String.format("%02d", i);
                ketQuaDayDu.put(thang, ketQua.getOrDefault(thang, 0.0));
            }

            return ketQuaDayDu;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ketQua;
    }
    
    /**
    * Lấy doanh thu hôm nay và hôm qua để so sánh
    * @return Map với key là "homNay" và "homQua", value là doanh thu
    */
   public Map<String, Double> getDoanhThuHomNayVaHomQua() {
       Map<String, Double> ketQua = new LinkedHashMap<>();

       try {
           // Lấy doanh thu hôm nay và hôm qua
           String sql = "SELECT " +
                       "CONVERT(VARCHAR(10), ngayLapHoaDon, 23) AS ngay, " +
                       "SUM(tongTien) AS doanhThu " +
                       "FROM HoaDon " +
                       "WHERE ngayLapHoaDon >= DATEADD(DAY, -1, CAST(GETDATE() AS DATE)) " +
                       "AND ngayLapHoaDon < DATEADD(DAY, 1, CAST(GETDATE() AS DATE)) " +
                       "GROUP BY CONVERT(VARCHAR(10), ngayLapHoaDon, 23) " +
                       "ORDER BY ngay ASC";

           Statement st = ConnectDB.conn.createStatement();
           ResultSet rs = st.executeQuery(sql);

           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
           Calendar cal = Calendar.getInstance();

           // Ngày hôm qua
           cal.setTime(new java.util.Date());
           cal.add(Calendar.DAY_OF_MONTH, -1);
           String ngayHomQua = sdf.format(cal.getTime());

           // Ngày hôm nay
           cal.setTime(new java.util.Date());
           String ngayHomNay = sdf.format(cal.getTime());

           // Khởi tạo giá trị mặc định
           ketQua.put("homQua", 0.0);
           ketQua.put("homNay", 0.0);

           while (rs.next()) {
               String ngay = rs.getString("ngay");
               double doanhThu = rs.getDouble("doanhThu");

               if (ngay.equals(ngayHomQua)) {
                   ketQua.put("homQua", doanhThu);
               } else if (ngay.equals(ngayHomNay)) {
                   ketQua.put("homNay", doanhThu);
               }
           }

           rs.close();
           st.close();

       } catch (Exception e) {
           e.printStackTrace();
       }

       return ketQua;
   }

    /**
     * Tính tỷ lệ thay đổi doanh thu hôm nay so với hôm qua
     * @return Tỷ lệ phần trăm (dương = tăng, âm = giảm)
     */
    public double getTyLeThayDoiDoanhThuHomNay() {
        try {
            Map<String, Double> duLieu = getDoanhThuHomNayVaHomQua();
            double doanhThuHomQua = duLieu.getOrDefault("homQua", 0.0);
            double doanhThuHomNay = duLieu.getOrDefault("homNay", 0.0);

            if (doanhThuHomQua > 0) {
                return ((doanhThuHomNay - doanhThuHomQua) / doanhThuHomQua) * 100;
            } else if (doanhThuHomNay > 0) {
                return 100.0; // Tăng 100% nếu hôm qua = 0
            }
            return 0.0;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
    
    /**
 * Lấy số lượng hành khách theo tháng trong năm hiện tại
 * @return Map với key là tháng (01-12), value là số lượng hành khách
 */
public Map<String, Integer> getSoLuongHanhKhachTheoThang() {
    Map<String, Integer> ketQua = new LinkedHashMap<>();
    
    try {
        String sql = "SELECT FORMAT(hd.ngayLapHoaDon, 'MM') AS thang, " +
                    "COUNT(DISTINCT v.maHanhKhach) AS soLuongHanhKhach " +
                    "FROM HoaDon hd " +
                    "JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                    "WHERE YEAR(hd.ngayLapHoaDon) = YEAR(GETDATE()) " +
                    "AND v.trangThai != 3 " +
                    "GROUP BY FORMAT(hd.ngayLapHoaDon, 'MM') " +
                    "ORDER BY thang ASC";
        
        Statement st = ConnectDB.conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while (rs.next()) {
            String thang = rs.getString("thang");
            int soLuong = rs.getInt("soLuongHanhKhach");
            ketQua.put(thang, soLuong);
        }
        
        rs.close();
        st.close();
        
        // Đảm bảo có đủ 12 tháng
        Map<String, Integer> ketQuaDayDu = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            String thang = String.format("%02d", i);
            ketQuaDayDu.put(thang, ketQua.getOrDefault(thang, 0));
        }
        
        return ketQuaDayDu;
        
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return ketQua;
}

    /**
     * Lấy số lượng hành khách theo năm trong 5 năm gần nhất
     * @return Map với key là năm, value là số lượng hành khách
     */
    public Map<String, Integer> getSoLuongHanhKhachTheoNam() {
        Map<String, Integer> ketQua = new LinkedHashMap<>();

        try {
            String sql = "SELECT YEAR(hd.ngayLapHoaDon) AS nam, " +
                        "COUNT(DISTINCT v.maHanhKhach) AS soLuongHanhKhach " +
                        "FROM HoaDon hd " +
                        "JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                        "WHERE YEAR(hd.ngayLapHoaDon) >= YEAR(GETDATE()) - 4 " +
                        "AND v.trangThai != 3 " +
                        "GROUP BY YEAR(hd.ngayLapHoaDon) " +
                        "ORDER BY nam ASC";

            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String nam = String.valueOf(rs.getInt("nam"));
                int soLuong = rs.getInt("soLuongHanhKhach");
                ketQua.put(nam, soLuong);
            }

            rs.close();
            st.close();

            // Đảm bảo có đủ 5 năm
            Calendar cal = Calendar.getInstance();
            int namHienTai = cal.get(Calendar.YEAR);

            Map<String, Integer> ketQuaDayDu = new LinkedHashMap<>();
            for (int i = 4; i >= 0; i--) {
                String nam = String.valueOf(namHienTai - i);
                ketQuaDayDu.put(nam, ketQua.getOrDefault(nam, 0));
            }

            return ketQuaDayDu;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ketQua;
    }
    
    /**
 * Lấy tỷ lệ hủy vé theo ngày trong 7 ngày gần nhất
 * @return Map với key là ngày (yyyy-MM-dd), value là tỷ lệ % hủy vé
 */
public Map<String, Double> getTyLeHuyVe7NgayGanNhat() {
    Map<String, Double> ketQua = new LinkedHashMap<>();
    
    try {
        // Lấy tổng số vé và số vé bị hủy theo ngày
        String sql = "SELECT " +
                    "CONVERT(VARCHAR(10), hd.ngayLapHoaDon, 23) AS ngay, " +
                    "COUNT(v.maVe) AS tongVe, " +
                    "SUM(CASE WHEN v.trangThai = 3 THEN 1 ELSE 0 END) AS veHuy " +
                    "FROM HoaDon hd " +
                    "JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                    "WHERE hd.ngayLapHoaDon >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE)) " +
                    "AND hd.ngayLapHoaDon < DATEADD(DAY, 1, CAST(GETDATE() AS DATE)) " +
                    "GROUP BY CONVERT(VARCHAR(10), hd.ngayLapHoaDon, 23) " +
                    "ORDER BY ngay ASC";
        
        Statement st = ConnectDB.conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        while (rs.next()) {
            String ngay = rs.getString("ngay");
            int tongVe = rs.getInt("tongVe");
            int veHuy = rs.getInt("veHuy");
            
            // Tính tỷ lệ %
            double tyLe = tongVe > 0 ? (veHuy * 100.0 / tongVe) : 0.0;
            ketQua.put(ngay, tyLe);
        }
        
        rs.close();
        st.close();
        
        // Đảm bảo có đủ 7 ngày (bổ sung các ngày thiếu với giá trị 0)
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        Map<String, Double> ketQuaDayDu = new LinkedHashMap<>();
        for (int i = 6; i >= 0; i--) {
            cal.setTime(new java.util.Date());
            cal.add(Calendar.DAY_OF_MONTH, -i);
            String ngay = sdf.format(cal.getTime());
            
            ketQuaDayDu.put(ngay, ketQua.getOrDefault(ngay, 0.0));
        }
        
        return ketQuaDayDu;
        
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return ketQua;
}

/**
 * Lấy tỷ lệ hủy vé hôm nay
 * @return Tỷ lệ % hủy vé hôm nay
 */
public double getTyLeHuyVeHomNay() {
    try {
        String sql = "SELECT " +
                    "COUNT(v.maVe) AS tongVe, " +
                    "SUM(CASE WHEN v.trangThai = 3 THEN 1 ELSE 0 END) AS veHuy " +
                    "FROM HoaDon hd " +
                    "JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                    "WHERE CAST(hd.ngayLapHoaDon AS DATE) = CAST(GETDATE() AS DATE)";
        
        Statement st = ConnectDB.conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        if (rs.next()) {
            int tongVe = rs.getInt("tongVe");
            int veHuy = rs.getInt("veHuy");
            
            rs.close();
            st.close();
            
            return tongVe > 0 ? (veHuy * 100.0 / tongVe) : 0.0;
        }
        
        rs.close();
        st.close();
        
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return 0.0;
}

    /**
     * Lấy tổng số vé bị hủy trong 7 ngày gần nhất
     * @return Tổng số vé bị hủy
     */
    public int getTongSoVeHuy7NgayGanNhat() {
        int tongSoVeHuy = 0;

        try {
            String sql = "SELECT COUNT(v.maVe) AS tongVeHuy " +
                        "FROM HoaDon hd " +
                        "JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                        "WHERE v.trangThai = 3 " +
                        "AND hd.ngayLapHoaDon >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE)) " +
                        "AND hd.ngayLapHoaDon < DATEADD(DAY, 1, CAST(GETDATE() AS DATE))";

            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                tongSoVeHuy = rs.getInt("tongVeHuy");
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tongSoVeHuy;
    }
}
