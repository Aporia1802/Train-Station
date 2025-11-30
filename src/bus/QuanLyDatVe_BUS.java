package bus;

import dao.*;
import entity.*;
import enums.TrangThaiVe;
import gui.components.ThongTinVe;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import main.Application;
import utils.CreateOrder;
import utils.CreateTicket;

public class QuanLyDatVe_BUS {
    private final Ve_DAO veDAO = new Ve_DAO();
    private final ChuyenTau_DAO ctDAO = new ChuyenTau_DAO();
    private final GaTau_DAO gaTauDAO = new GaTau_DAO();
    private final ToaTau_DAO toaTauDAO = new ToaTau_DAO();
    private final Ghe_DAO gheDAO = new Ghe_DAO();
    private final KhoangTau_DAO khoangTauDAO = new KhoangTau_DAO();
    private final LoaiVe_DAO loaiVeDAO = new LoaiVe_DAO();
    private final HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
    private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();
    private final HanhKhach_DAO hanhKhachDAO = new HanhKhach_DAO();
    private final HoaDonDoi_DAO hoaDonDoiDAO = new HoaDonDoi_DAO();
    
    private NhanVien nhanVien = Application.nhanVien;
    private ArrayList<Ghe> dsGheDaChon = new ArrayList<>();
    
    // ========== QUẢN LÝ GHẾ ==========
    
    public ArrayList<ToaTau> getDanhSachToaTau(Tau tau) throws Exception {
        return toaTauDAO.getToaTauTheoMaTau(tau.getMaTau());
    }
    
    public ArrayList<KhoangTau> getDanhSachKhoangTheoToa(ToaTau toa) throws Exception {
        return khoangTauDAO.getKhoangTauTheoMaToa(toa.getMaToa());
    }
    
    public ArrayList<Ghe> getDanhSachGheTheoToa(ToaTau toa) throws Exception {
        ArrayList<Ghe> dsGhe = new ArrayList<>();
        ArrayList<KhoangTau> dsKhoang = getDanhSachKhoangTheoToa(toa);
        
        for (KhoangTau khoang : dsKhoang) {
            dsGhe.addAll(gheDAO.getGheTheoMaKhoang(khoang.getMaKhoangTau()));
        }
        
        dsGhe.sort((g1, g2) -> Integer.compare(g1.getSoGhe(), g2.getSoGhe()));
        return dsGhe;
    }
    
    public ArrayList<Ghe> getDanhSachGheTheoKhoang(KhoangTau khoang) throws Exception {
        ArrayList<Ghe> dsGhe = gheDAO.getGheTheoMaKhoang(khoang.getMaKhoangTau());
        dsGhe.sort((g1, g2) -> Integer.compare(g1.getSoGhe(), g2.getSoGhe()));
        return dsGhe;
    }
    
    public boolean isGheDaDat(Ghe ghe, ChuyenTau chuyen) throws Exception {
        ArrayList<Ve> dsVe = veDAO.getVeTheoMaChuyenTau(chuyen.getMaChuyenTau());
        
        for (Ve ve : dsVe) {
            if (ve.getGhe().getMaGhe().equals(ghe.getMaGhe())) {
                if (ve.getTrangThai().compare(0) || ve.getTrangThai().compare(1)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isDaChonGhe(Ghe ghe) {
        for (Ghe g : dsGheDaChon) {
            if (g.getMaGhe().equals(ghe.getMaGhe())) {
                return true;
            }
        }
        return false;
    }
    
    public void themGheDaChon(Ghe ghe) {
        if (!isDaChonGhe(ghe)) {
            dsGheDaChon.add(ghe);
        }
    }
    
    public void xoaGheDaChon(Ghe ghe) {
        dsGheDaChon.removeIf(g -> g.getMaGhe().equals(ghe.getMaGhe()));
    }
    
    public ArrayList<Ghe> getDanhSachGheDaChon() {
        return new ArrayList<>(dsGheDaChon);
    }
    
    public void clearDanhSachGheDaChon() {
        dsGheDaChon.clear();
    }
    
    public int getSoGheDaChon() {
        return dsGheDaChon.size();
    }
    
    // ========== TÌM KIẾM ==========
    
    public ArrayList<GaTau> getAllGaTau() {
        return gaTauDAO.getAll();
    }
    
    public ArrayList<ChuyenTau> timKiemChuyenTau(String gaDi, String gaDen, LocalDate ngay) throws Exception {
        return ctDAO.timKiemChuyenTau(gaDi, gaDen, ngay);
    }
    
    public Ghe timGheTheoMa(String maGhe) {
        for (Ghe ghe : getDanhSachGheDaChon()) {
            if (ghe.getMaGhe().equals(maGhe)) {
                return ghe;
            }
        }
        return null;
    }
    
    public LoaiVe timLoaiVeTheoTen(String tenLoaiVe) {
        return loaiVeDAO.getLoaiVe(tenLoaiVe);
    }

    public ArrayList<LoaiVe> getAllLoaiVe() {
        return loaiVeDAO.getAll();
    }
    
    // ========== XỬ LÝ THANH TOÁN - LOGIC NGHIỆP VỤ ==========
    
    public boolean xuLyThanhToan(String hoTenKhach, String sdtKhach, String cccdKhach,
                                 java.util.List<ThongTinVe.ThongTinHanhKhach> dsHanhKhach,
                                 ChuyenTau chuyenDi, ChuyenTau chuyenVe, boolean isKhuHoi,
                                 String maKhuyenMai) {
        try {
            // BƯỚC 1: Validate nghiệp vụ
            if (!validateThanhToan(dsHanhKhach, chuyenDi, chuyenVe)) {
                return false;
            }
            
            // BƯỚC 2: Xử lý khách hàng
            KhachHang khachHang = xuLyKhachHang(hoTenKhach, sdtKhach, cccdKhach);
            if (khachHang == null) {
                System.err.println("Không thể tạo/lấy thông tin khách hàng");
                return false;
            }
            
            // ===== QUAN TRỌNG: Generate ID đầu tiên một lần duy nhất =====
            String firstId = veDAO.generateID();
            String prefix = "VE-";
            int startNumber = extractNumber(firstId, prefix);
            
            // BƯỚC 3: Tạo danh sách vé và tính tổng tiền
            ArrayList<Ve> dsVe = new ArrayList<>();
            double tongTienVe = 0.0;
            int index = 0; // Đếm số thứ tự vé
            
            for (ThongTinVe.ThongTinHanhKhach hk : dsHanhKhach) {
                // Tạo hành khách
                HanhKhach hanhKhach = taoHanhKhach(hk);
                if (hanhKhach == null) {
                    System.err.println("Không thể tạo hành khách: " + hk.getHoTen());
                    rollbackVe(dsVe);
                    return false;
                }
                
                // Tìm ghế
                Ghe ghe = getThongTinGhe(hk.getMaGhe());
                if (ghe == null) {
                    System.err.println("Không tìm thấy ghế: " + hk.getMaGhe());
                    rollbackVe(dsVe);
                    return false;
                }
                
                // Xác định chuyến tàu
                String maChuyen = hk.getMaChuyenTau();
                ChuyenTau chuyen = null;
                
                if (maChuyen.equals(chuyenDi.getMaChuyenTau())) {
                    chuyen = chuyenDi;
                } else if (isKhuHoi && chuyenVe != null && maChuyen.equals(chuyenVe.getMaChuyenTau())) {
                    chuyen = chuyenVe;
                }
                
                if (chuyen == null) {
                    System.err.println("Không xác định được chuyến tàu cho vé: " + hk.getMaGhe());
                    rollbackVe(dsVe);
                    return false;
                }
                
                // Tìm loại vé
                LoaiVe loaiVe = timLoaiVeTheoTen(hk.getLoaiVe());
                if (loaiVe == null) {
                    System.err.println("Không tìm thấy loại vé: " + hk.getLoaiVe());
                    rollbackVe(dsVe);
                    return false;
                }
                
                // Tính giá vé
                double giaVe = tinhGiaVe(hk, ghe, chuyen);
                tongTienVe += giaVe;
                
                // ===== Generate mã vé tuần tự từ startNumber =====
                String maVe = prefix + String.format("%05d", startNumber + index);
                index++;
                
                // Tạo vé
                Ve ve = new Ve();
                ve.setMaVe(maVe);
                ve.setChuyenTau(chuyen);
                ve.setHanhKhach(hanhKhach);
                ve.setGhe(ghe);
                ve.setTrangThai(TrangThaiVe.DA_DAT);
                ve.setLoaiVe(loaiVe);
                ve.setGiaVe(giaVe);
                
                dsVe.add(ve);
            }
            
            // BƯỚC 4: Tính tổng tiền (bao gồm VAT và khuyến mãi)
            double vat = 0.1; // 10%
            double giamGia = 0.0; // TODO: Xử lý khuyến mãi nếu có
            double thanhTien = tongTienVe;
            double tongTien = tongTienVe + (tongTienVe * vat) - giamGia;
            
            // BƯỚC 5: Tạo hóa đơn
            HoaDon hoaDon = taoHoaDon(khachHang, thanhTien, tongTien, vat);
            if (hoaDon == null) {
                rollbackVe(dsVe);
                return false;
            }
            
            // BƯỚC 6: Cập nhật hóa đơn cho vé và lưu vào DB
            for (Ve ve : dsVe) {
                ve.setHoaDon(hoaDon);
                if (!veDAO.create(ve)) {
                    rollbackVe(dsVe);
                    rollbackHoaDon(hoaDon);
                    return false;
                }
            }
            
            // BƯỚC 7: In hóa đơn và vé
            try {
                CreateOrder.taoHD(dsVe, hoaDon);
                for (Ve ve : dsVe) {
                    CreateTicket.taoVe(ve);
                }
            } catch (Exception e) {
                System.err.println("Lỗi khi in hóa đơn/vé: " + e.getMessage());
            }
            
            // BƯỚC 8: Clear danh sách ghế đã chọn
            clearDanhSachGheDaChon();
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi trong quá trình thanh toán: " + e.getMessage());
            return false;
        }
    }
    
    // Hàm helper để trích xuất số từ mã vé
    private int extractNumber(String id, String prefix) {
        try {
            String numberPart = id.substring(prefix.length());
            return Integer.parseInt(numberPart);
        } catch (Exception e) {
            return 1;
        }
    }
    
    public Ghe getThongTinGhe(String maGhe) {
        return gheDAO.getGheById(maGhe);
    }
    
    /**
     * Validate dữ liệu trước khi thanh toán
     */
    private boolean validateThanhToan(java.util.List<ThongTinVe.ThongTinHanhKhach> dsHanhKhach,
                                     ChuyenTau chuyenDi, ChuyenTau chuyenVe) {
        if (nhanVien == null) {
            System.err.println("Không có thông tin nhân viên đăng nhập");
            return false;
        }
        
        if (dsHanhKhach == null || dsHanhKhach.isEmpty()) {
            System.err.println("Danh sách hành khách trống");
            return false;
        }
        
        if (chuyenDi == null) {
            System.err.println("Chưa chọn chuyến tàu");
            return false;
        }
        
        // Kiểm tra ghế có bị trùng không
        try {
            for (ThongTinVe.ThongTinHanhKhach hk : dsHanhKhach) {
                Ghe ghe = timGheTheoMa(hk.getMaGhe());
                if (ghe == null) {
                    System.err.println("Không tìm thấy ghế: " + hk.getMaGhe());
                    return false;
                }
                
                String maChuyen = hk.getMaChuyenTau();
                ChuyenTau chuyen = maChuyen.equals(chuyenDi.getMaChuyenTau()) 
                    ? chuyenDi 
                    : chuyenVe;
                
                if (chuyen != null && isGheDaDat(ghe, chuyen)) {
                    System.err.println("Ghế " + ghe.getSoGhe() + " đã được đặt");
                    return false;
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi validate ghế: " + e.getMessage());
            return false;
        }
        
        return true;
    }
    
    /**
     * Xử lý khách hàng (tạo mới hoặc lấy từ DB)
     */
    private KhachHang xuLyKhachHang(String hoTen, String sdt, String cccd) {
        try {
            // Kiểm tra khách hàng đã tồn tại chưa
            KhachHang khachHang = null;
            
            if (cccd != null && !cccd.trim().isEmpty()) {
                khachHang = khachHangDAO.getByCCCD(cccd);
            }
            
            // Nếu chưa có thì tạo mới
            if (khachHang == null) {
                String maKH = khachHangDAO.generateID();
                khachHang = new KhachHang(maKH, hoTen, sdt, cccd);
                
                if (!khachHangDAO.create(khachHang)) {
                    return null;
                }
            }
            
            return khachHang;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Tạo hành khách
     */
    private HanhKhach taoHanhKhach(ThongTinVe.ThongTinHanhKhach hk) {
        try {
            String maHK = hanhKhachDAO.generateID();
            
            LocalDate ngaySinh = null;
            if (hk.getNgaySinh() != null) {
                ngaySinh = hk.getNgaySinh().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            }
            
            HanhKhach hanhKhach = new HanhKhach(maHK, hk.getHoTen(), hk.getCCCD(), ngaySinh);
            
            if (!hanhKhachDAO.create(hanhKhach)) {
                return null;
            }
            
            return hanhKhach;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Tạo hóa đơn
     */
    private HoaDon taoHoaDon(KhachHang khachHang, double thanhTien, double tongTien, double vat) {
        try {
            String maHD = hoaDonDAO.generateID();
            HoaDon hoaDon = new HoaDon(maHD, nhanVien, khachHang, LocalDateTime.now(), null);
            hoaDon.setThanhTien(thanhTien);
            hoaDon.setTongTien(tongTien);
            
            if (!hoaDonDAO.create(hoaDon)) {
                return null;
            }
            
            return hoaDon;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Tính giá vé theo loại hành khách, loại ghế và tuyến đường
     */
    public double tinhGiaVe(ThongTinVe.ThongTinHanhKhach hk, Ghe ghe, ChuyenTau ct) {
        double giaGoc = ct.getTuyenDuong().tinhGiaVeCoBan();
        double heSoGhe = ghe.getLoaiGhe().getHeSoLoaiGhe();
        double heSoLoaiVe = layHeSoLoaiVe(hk.getLoaiVe());
        
        return giaGoc * heSoGhe * heSoLoaiVe;
    }
    
    /**
     * Lấy hệ số loại vé
     */
    private double layHeSoLoaiVe(String loaiVe) {
        switch(loaiVe) {
            case "Trẻ em": 
                return 0.5;
            case "Sinh viên": 
                return 0.8;
            case "Người cao tuổi": 
                return 0.7;
            default: 
                return 1.0; // Người lớn
        }
    }
    
    // ========== ROLLBACK METHODS ==========
    
    /**
     * Rollback các vé đã tạo (nếu có lỗi)
     */
    private void rollbackVe(ArrayList<Ve> dsVe) {
        for (Ve ve : dsVe) {
            try {
                if (ve.getMaVe() != null) {
                    veDAO.delete(ve.getMaVe());
                }
            } catch (Exception e) {
                System.err.println("Lỗi khi rollback vé: " + ve.getMaVe());
            }
        }
    }
    
    /**
     * Rollback hóa đơn (nếu có lỗi)
     */
    private void rollbackHoaDon(HoaDon hoaDon) {
        try {
            if (hoaDon != null && hoaDon.getMaHoaDon() != null) {
                hoaDonDAO.delete(hoaDon.getMaHoaDon());
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi rollback hóa đơn: " + hoaDon.getMaHoaDon());
        }
    }
    
    /**
     * Lấy vé theo mã - Dùng cho Đổi vé
     */
    public Ve getVeById(String maVe) {
        Ve_DAO veDAO = new Ve_DAO();
        return veDAO.getOne(maVe);
    }
    
    /**
     * Tạo mã hóa đơn đổi mới
     */
    public String generateHoaDonDoiID() {
        return hoaDonDoiDAO.generateID();
    }
    
    /**
     * Tạo hóa đơn đổi
     */
    public boolean createHoaDonDoi(HoaDonDoi hdd) {
        return hoaDonDoiDAO.insert(hdd);
    }
    
    /**
     * Hủy vé (đổi trạng thái = 3)
     */
    public boolean huyVe(Ve ve) {
        return veDAO.updateTrangThaiVe(ve.getMaVe(), 3);
    }
    
    /**
     * Khôi phục vé (đổi lại trạng thái = 1) - Dùng khi rollback
     */
    public boolean khoiPhucVe(Ve ve) {
        return veDAO.updateTrangThaiVe(ve.getMaVe(), 1);
    }
    
    /**
 * Xử lý thanh toán VÀ trả về danh sách vé đã tạo
 * Dùng cho trường hợp đổi vé (cần lấy danh sách vé để in)
 */
public ArrayList<Ve> xuLyThanhToanVaLayVe(String hoTenKhach, String sdtKhach, String cccdKhach,
                             java.util.List<ThongTinVe.ThongTinHanhKhach> dsHanhKhach,
                             ChuyenTau chuyenDi, ChuyenTau chuyenVe, boolean isKhuHoi,
                             String maKhuyenMai) {
    try {
        // BƯỚC 1: Validate nghiệp vụ
        if (!validateThanhToan(dsHanhKhach, chuyenDi, chuyenVe)) {
            return null;
        }
        
        // BƯỚC 2: Xử lý khách hàng
        KhachHang khachHang = xuLyKhachHang(hoTenKhach, sdtKhach, cccdKhach);
        if (khachHang == null) {
            System.err.println("Không thể tạo/lấy thông tin khách hàng");
            return null;
        }
        
        // ===== Generate ID đầu tiên một lần duy nhất =====
        String firstId = veDAO.generateID();
        String prefix = "VE-";
        int startNumber = extractNumber(firstId, prefix);
        
        // BƯỚC 3: Tạo danh sách vé và tính tổng tiền
        ArrayList<Ve> dsVe = new ArrayList<>();
        double tongTienVe = 0.0;
        int index = 0;
        
        for (ThongTinVe.ThongTinHanhKhach hk : dsHanhKhach) {
            // Tạo hành khách
            HanhKhach hanhKhach = taoHanhKhach(hk);
            if (hanhKhach == null) {
                System.err.println("Không thể tạo hành khách: " + hk.getHoTen());
                rollbackVe(dsVe);
                return null;
            }
            
            // Tìm ghế
            Ghe ghe = getThongTinGhe(hk.getMaGhe());
            if (ghe == null) {
                System.err.println("Không tìm thấy ghế: " + hk.getMaGhe());
                rollbackVe(dsVe);
                return null;
            }
            
            // Xác định chuyến tàu
            String maChuyen = hk.getMaChuyenTau();
            ChuyenTau chuyen = null;
            
            if (maChuyen.equals(chuyenDi.getMaChuyenTau())) {
                chuyen = chuyenDi;
            } else if (isKhuHoi && chuyenVe != null && maChuyen.equals(chuyenVe.getMaChuyenTau())) {
                chuyen = chuyenVe;
            }
            
            if (chuyen == null) {
                System.err.println("Không xác định được chuyến tàu cho vé: " + hk.getMaGhe());
                rollbackVe(dsVe);
                return null;
            }
            
            // Tìm loại vé
            LoaiVe loaiVe = timLoaiVeTheoTen(hk.getLoaiVe());
            if (loaiVe == null) {
                System.err.println("Không tìm thấy loại vé: " + hk.getLoaiVe());
                rollbackVe(dsVe);
                return null;
            }
            
            // Tính giá vé
            double giaVe = tinhGiaVe(hk, ghe, chuyen);
            tongTienVe += giaVe;
            
            // Generate mã vé tuần tự
            String maVe = prefix + String.format("%05d", startNumber + index);
            index++;
            
            // Tạo vé
            Ve ve = new Ve();
            ve.setMaVe(maVe);
            ve.setChuyenTau(chuyen);
            ve.setHanhKhach(hanhKhach);
            ve.setGhe(ghe);
            ve.setTrangThai(TrangThaiVe.DA_DAT);
            ve.setLoaiVe(loaiVe);
            ve.setGiaVe(giaVe);
            
            dsVe.add(ve);
        }
        
        // BƯỚC 4: Tính tổng tiền (bao gồm VAT và khuyến mãi)
        double vat = 0.1; // 10%
        double giamGia = 0.0;
        double thanhTien = tongTienVe;
        double tongTien = tongTienVe + (tongTienVe * vat) - giamGia;
        
        // BƯỚC 5: Tạo hóa đơn
        HoaDon hoaDon = taoHoaDon(khachHang, thanhTien, tongTien, vat);
        if (hoaDon == null) {
            rollbackVe(dsVe);
            return null;
        }
        
        // BƯỚC 6: Cập nhật hóa đơn cho vé và lưu vào DB
        for (Ve ve : dsVe) {
            ve.setHoaDon(hoaDon);
            if (!veDAO.create(ve)) {
                rollbackVe(dsVe);
                rollbackHoaDon(hoaDon);
                return null;
            }
        }
        
        // BƯỚC 7: Clear danh sách ghế đã chọn
        clearDanhSachGheDaChon();
        
        // TRẢ VỀ danh sách vé đã tạo thành công
        return dsVe;
        
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Lỗi trong quá trình thanh toán: " + e.getMessage());
        return null;
    }
}
    
}