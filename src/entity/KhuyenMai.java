/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author CÔNG HOÀNG
 */
public class KhuyenMai {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private double heSoKhuyenMai;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private double tongTienToiThieu;
    private double tienKhuyenMaiToiDa;
    private Boolean trangThai;
    
    public static final String TENKHUYENMAI_EMPTY = "Tên khuyến mãi không được rỗng!";
    public static final String HESO_INVALID = "Hệ số khuyến mãi phải từ 0.1 đến 100!";
    public static final String NGAYBATDAU_INVALID = "Ngày bắt đầu phải nhỏ hơn ngày kết thúc!";
    public static final String NGAYKETTHUC_INVALID = "Ngày kết thúc phải lớn hơn ngày bắt đầu!";
    public static final String TONGTIEN_INVALID = "Tổng tiền tối thiểu phải lớn hơn 0!";
    
    public KhuyenMai() {
    }
    
    public KhuyenMai(String khuyenMai) {
        setMaKhuyenMai(maKhuyenMai);
    }
    
    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, double heSoKhuyenMai,
                     LocalDate ngayBatDau, LocalDate ngayKetThuc,
                     double tongTienToiThieu, double tienKhuyenMaiToiDa, Boolean trangThai) throws Exception {
        setMaKhuyenMai(maKhuyenMai);
        setTenKhuyenMai(tenKhuyenMai);
        setHeSoKhuyenMai(heSoKhuyenMai);
        setNgayBatDau(ngayBatDau);
        setNgayKetThuc(ngayKetThuc);
        setTongTienToiThieu(tongTienToiThieu);
        setTienKhuyenMaiToiDa(tienKhuyenMaiToiDa);
        setTrangThai(trangThai);
    }
    
    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }
    
    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }
    
    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }
    
    public void setTenKhuyenMai(String tenKhuyenMai) throws Exception {
        tenKhuyenMai = tenKhuyenMai.trim();
        if (tenKhuyenMai.isEmpty()) {
            throw new Exception(TENKHUYENMAI_EMPTY);
        }
        this.tenKhuyenMai = tenKhuyenMai;
    }
    
    public double getHeSoKhuyenMai() {
        return heSoKhuyenMai;
    }
    
    public void setHeSoKhuyenMai(double heSoKhuyenMai) throws Exception {
        if (heSoKhuyenMai < 0 || heSoKhuyenMai > 1) {
            throw new Exception(HESO_INVALID);
        }
        this.heSoKhuyenMai = heSoKhuyenMai;
    }
    
    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }
    
    public void setNgayBatDau(LocalDate ngayBatDau) throws Exception {
        if (ngayKetThuc != null && ngayBatDau.isAfter(ngayKetThuc)) {
            throw new Exception(NGAYBATDAU_INVALID);
        }
        this.ngayBatDau = ngayBatDau;
    }
    
    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }
    
    public void setNgayKetThuc(LocalDate ngayKetThuc) throws Exception {
        if (ngayBatDau != null && ngayKetThuc.isBefore(ngayBatDau)) {
            throw new Exception(NGAYKETTHUC_INVALID);
        }
        this.ngayKetThuc = ngayKetThuc;
    }
    
    public double getTongTienToiThieu() {
        return tongTienToiThieu;
    }
    
    public void setTongTienToiThieu(double tongTienToiThieu) throws Exception {
        if (tongTienToiThieu <= 0) {
            throw new Exception(TONGTIEN_INVALID);
        }
        this.tongTienToiThieu = tongTienToiThieu;
    }
    
    public double getTienKhuyenMaiToiDa() {
        return tienKhuyenMaiToiDa;
    }
    
    public void setTienKhuyenMaiToiDa(double tienKhuyenMaiToiDa) {
        this.tienKhuyenMaiToiDa = tienKhuyenMaiToiDa;
    }
    
    public Boolean getTrangThai() {
        return trangThai;
    }
    
    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.maKhuyenMai);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KhuyenMai other = (KhuyenMai) obj;
        return Objects.equals(this.maKhuyenMai, other.maKhuyenMai);
    }
    
    
}
