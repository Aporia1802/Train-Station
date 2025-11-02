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
public class KhachHang {
private String maKH;
    private String tenKH;
    private String soDienThoai;
    private String cccd;
    private LocalDate ngaySinh;
    
    // Thông báo lỗi
    public static final String TENKH_EMPTY = "Tên khách hàng không được rỗng!";
    public static final String TENKH_INVALID = "Tên khách hàng chỉ được chứa kí tự chữ và khoảng trắng!";
    public static final String SDT_EMPTY = "Số điện thoại không được rỗng!";
    public static final String SDT_INVALID = "Số điện thoại không hợp lệ!";
    public static final String CCCD_EMPTY = "Số CCCD không được rỗng!";
    public static final String CCCD_INVALID = "Số CCCD phải chứa 12 chữ số!";
    
    public KhachHang() {
    }
    
    public KhachHang(String maKH) {
        setMaKH(maKH);
    }
    
    public KhachHang(String maKH, String tenKH, String soDienThoai, String cccd, 
                     LocalDate ngaySinh) throws Exception {
        setMaKH(maKH);
        setTenKH(tenKH);
        setSoDienThoai(soDienThoai);
        setCccd(cccd);
        setNgaySinh(ngaySinh);
    }
    
    public String getMaKH() {
        return maKH;
    }
    
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }
    
    public String getTenKH() {
        return tenKH;
    }
    
    public void setTenKH(String tenKH) throws Exception {
        tenKH = tenKH.trim();
        if (tenKH.isEmpty()) {
            throw new Exception(TENKH_EMPTY);
        }
        if (!tenKH.matches("^[\\p{L} ]+$")) {
            throw new Exception(TENKH_INVALID);
        }
        this.tenKH = tenKH;
    }
    
    public String getSoDienThoai() {
        return soDienThoai;
    }
    
    public void setSoDienThoai(String soDienThoai) throws Exception {
        soDienThoai = soDienThoai.trim();
        if (soDienThoai.isEmpty()) {
            throw new Exception(SDT_EMPTY);
        }
        if (!soDienThoai.matches("^(02|03|05|07|08|09)\\d{8}$")) {
            throw new Exception(SDT_INVALID);
        }
        this.soDienThoai = soDienThoai;
    }
    
    public String getCccd() {
        return cccd;
    }
    
    public void setCccd(String cccd) throws Exception {
        cccd = cccd.trim();
        if (cccd.isEmpty()) {
            throw new Exception(CCCD_EMPTY);
        }
        if (!cccd.matches("\\d{12}")) {
            throw new Exception(CCCD_INVALID);
        }
        this.cccd = cccd;
    }
    
    public LocalDate getNgaySinh() {
        return ngaySinh;
    }
    
    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.maKH);
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
        final KhachHang other = (KhachHang) obj;
        return Objects.equals(this.maKH, other.maKH);
    }
    
    
}
