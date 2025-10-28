/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.LocalDate;

/**
 *
 * @author CÔNG HOÀNG
 */
public class HoaDon {
    private String maHoaDon;
    private LocalDate ngayLapHoaDon;
    private final double VAT = 0.1;
    private KhuyenMai khuyenMai;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private double tongTien;
    private double thanhTien;
    
    public static final String NHANVIEN_NULL = "Nhân viên không được null!";
    public static final String KHACHHANG_NULL = "Khách hàng không được null!";
    public static final String NGAYLAP_INVALID = "Ngày lập hóa đơn phải là ngày hiện tại!";
    
    public HoaDon(String hdtemp) {
    }
    
    public HoaDon(String maHoaDon, NhanVien nhanVien, KhachHang khachHang,
                  LocalDate ngayLapHoaDon, KhuyenMai khuyenMai) throws Exception {
        setMaHoaDon(maHoaDon);
        setNhanVien(nhanVien);
        setKhachHang(khachHang);
        setNgayLapHoaDon(ngayLapHoaDon);
        setKhuyenMai(khuyenMai);
    }
    
    public String getMaHoaDon() {
        return maHoaDon;
    }
    
    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }
    
    public NhanVien getNhanVien() {
        return nhanVien;
    }
    
    public void setNhanVien(NhanVien nhanVien) throws Exception {
        if (nhanVien == null) {
            throw new Exception(NHANVIEN_NULL);
        }
        this.nhanVien = nhanVien;
    }
    
    public KhachHang getKhachHang() {
        return khachHang;
    }
    
    public void setKhachHang(KhachHang khachHang) throws Exception {
        if (khachHang == null) {
            throw new Exception(KHACHHANG_NULL);
        }
        this.khachHang = khachHang;
    }
    
    public LocalDate getNgayLapHoaDon() {
        return ngayLapHoaDon;
    }
    
    public void setNgayLapHoaDon(LocalDate ngayLapHoaDon) throws Exception {
//        if (!ngayLapHoaDon.equals(LocalDate.now())) {
//            throw new Exception(NGAYLAP_INVALID);
//        }
        this.ngayLapHoaDon = ngayLapHoaDon;
    }

    public double getVAT() {
        return VAT;
    }
    
    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }
    
    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }
    
    public double getTongTien() {
        return tongTien;
    }
    
    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }
    
    
}
