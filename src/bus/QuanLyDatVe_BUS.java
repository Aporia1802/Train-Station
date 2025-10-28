/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.ChuyenTau_DAO;
import dao.Ghe_DAO;
import dao.HanhKhach_DAO;
import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import dao.KhoangTau_DAO;
import dao.LoaiVe_DAO;
import dao.ToaTau_DAO;
import dao.Ve_DAO;
import entity.ChuyenTau;
import entity.Ghe;
import entity.HanhKhach;
import entity.HoaDon;
import entity.KhoangTau;
import entity.LoaiVe;
import entity.ToaTau;
import entity.KhachHang;
import entity.Ve;
import entity.KhuyenMai;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author CÔNG HOÀNG
 */
public class QuanLyDatVe_BUS {
    private final Ve_DAO veDAO = new Ve_DAO();
    private final ChuyenTau_DAO ctDAO = new ChuyenTau_DAO();
    private final Ghe_DAO gheDAO = new Ghe_DAO();
    private final ToaTau_DAO toaTauDAO = new ToaTau_DAO();
    private final KhoangTau_DAO khoangTauDAO = new KhoangTau_DAO();
    private final HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
    private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();
    private final HanhKhach_DAO hanhKhachDAO = new HanhKhach_DAO();
    private final LoaiVe_DAO loaiVeDAO = new LoaiVe_DAO();
       
    public ChuyenTau getChuyenTauById(String maChuyenTau) {
        return ctDAO.getOne(maChuyenTau);
    }
    
    public ArrayList<ChuyenTau> timKiemChuyenTau(String gaDi, String gaDen, LocalDate ngayDi) throws Exception {
        return ctDAO.timKiemChuyenTau(gaDi, gaDen, ngayDi);
    }
    
    public ArrayList<ToaTau> getToaTau(String maTau) {
        return toaTauDAO.getToaTauTheoTau(maTau);
    }
    
    public ArrayList<KhoangTau> getKhoangTau(String maToaTau) {
        return khoangTauDAO.getKhoangTauTheoToa(maToaTau);
    }
    
    public ArrayList<Ghe> getGhe(String maKhoangTau) {
        return gheDAO.getGheTheoKhoangTau(maKhoangTau);
    }
    
    /**
     * Tạo hoặc lấy khách hàng từ database
     */
    public KhachHang taoHoacLayKhachHang(String hoTen, String cccd, String sdt, LocalDate ngaySinh, boolean gioiTinh) throws Exception {
        // Kiểm tra khách hàng đã tồn tại chưa
        KhachHang kh = khachHangDAO.getByCCCD(cccd);
        
        if (kh == null) {
            // Tạo mới
            String maKH = khachHangDAO.generateID();
            kh = new KhachHang(maKH, hoTen, sdt, cccd, ngaySinh, gioiTinh);
            
            if (!khachHangDAO.create(kh)) {
                throw new Exception("Không thể tạo khách hàng!");
            }
        }
        
        return kh;
    }
    
    /**
     * Tạo hành khách
     */
    public HanhKhach taoHanhKhach(String hoTen, String cccd, LocalDate ngaySinh) throws Exception {
        String maHK = hanhKhachDAO.generateID();
        HanhKhach hk = new HanhKhach(maHK, hoTen, cccd, ngaySinh);
        
        if (!hanhKhachDAO.create(hk)) {
            throw new Exception("Không thể tạo hành khách!");
        }
        
        return hk;
    }
    
    /**
     * Đặt vé - thực hiện toàn bộ quy trình
     */
    public boolean datVe(HoaDon hoaDon, ArrayList<Ve> dsVe) throws Exception {
        try {
            // 1. Tạo hóa đơn
            if (!hoaDonDAO.create(hoaDon)) {
                throw new Exception("Không thể tạo hóa đơn!");
            }
            
            // 2. Tạo từng vé và cập nhật ghế
            for (Ve ve : dsVe) {
                // Tạo vé
                if (!veDAO.create(ve)) {
                    throw new Exception("Không thể tạo vé!");
                }
                
                // Cập nhật trạng thái ghế (từ trống -> đã đặt)
                if (!gheDAO.capNhatTrangThaiGhe(ve.getGhe().getMaGhe())) {
                    throw new Exception("Không thể cập nhật trạng thái ghế!");
                }
            }
            
            // 3. Cập nhật số ghế đã đặt và còn trống của chuyến tàu
            String maChuyenTau = dsVe.get(0).getChuyenTau().getMaChuyenTau();
            if (!ctDAO.capNhatSoGheKhiDat(maChuyenTau, dsVe.size())) {
                throw new Exception("Không thể cập nhật số ghế chuyến tàu!");
            }
            
            return true;
            
        } catch (Exception e) {
            // Nếu có lỗi, rollback bằng cách không commit
            throw new Exception("Lỗi khi đặt vé: " + e.getMessage());
        }
    }
    
    /**
     * Tính giá vé
     */
    public double tinhGiaVe(ChuyenTau chuyenTau, Ghe ghe, LoaiVe loaiVe) {
        // Giá cơ bản = quãng đường * giá/km
        double giaCoBan = chuyenTau.getTuyenDuong().getQuangDuong() 
                        * chuyenTau.getTuyenDuong().getSoTienMotKm();
        
        // Nhân với hệ số loại ghế
        double giaTheoGhe = giaCoBan * ghe.getLoaiGhe().getHeSoLoaiGhe();
        
        // Nhân với hệ số loại vé
        double giaTheoLoaiVe = giaTheoGhe * loaiVe.getHeSoLoaiVe();
        
        return giaTheoLoaiVe;
    }
    
    /**
     * Tính tổng tiền và thành tiền của hóa đơn
     */

    public double[] tinhTienHoaDon(ArrayList<Ve> dsVe, KhuyenMai khuyenMai) {
        double tongTien = 0;
        
        // Tính tổng tiền các vé
        for (Ve ve : dsVe) {
            tongTien += ve.getGiaVe();
        }
        
        // Áp dụng VAT
        double tienVAT = tongTien * 0.1;
        double tongTienSauVAT = tongTien + tienVAT;
        
        // Áp dụng khuyến mãi (nếu có)
        double tienGiam = 0;
        if (khuyenMai != null) {
            tienGiam = tongTienSauVAT * khuyenMai.getHeSoKhuyenMai();
            
            // Kiểm tra giảm tối đa
            if (tienGiam > khuyenMai.getTienKhuyenMaiToiDa()) {
                tienGiam = khuyenMai.getTienKhuyenMaiToiDa();
            }
        }
        
        double thanhTien = tongTienSauVAT - tienGiam;
        
        return new double[]{tongTien, thanhTien};
    }
    
    public String generateMaVe() {
    return veDAO.generateID();
}

    public String generateMaHoaDon() {
        return hoaDonDAO.generateID();
    }
    
    public LoaiVe getLoaiVeByTen(String tenLoaiVe) {
        String maLoaiVe = "";
        switch(tenLoaiVe) {
            case "Người lớn":
                maLoaiVe = "LV-NL";
                break;
            case "Trẻ em":
                maLoaiVe = "LV-TE";
                break;
            case "Sinh viên":
                maLoaiVe = "LV-HSSV";
                break;
            case "Người cao tuổi":
                maLoaiVe = "LV-NC";
                break;
        }
        return loaiVeDAO.getOne(maLoaiVe);
    }
}
