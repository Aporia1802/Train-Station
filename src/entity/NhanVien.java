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
public class NhanVien {
    private String maNV;
    private String tenNV;
    private boolean gioiTinh;
    private LocalDate ngaySinh;
    private String email;
    private String soDienThoai;
    private String cccd;
    private String diaChi;
    private String chucVu;
    private boolean trangThai;
    
//  Thông báo lỗi
    public static final String TENNV_EMPTY = "Họ tên không được rỗng!";
    public static final String TENNV_INVALID = "Họ tên chỉ được chứa kí tự chữ và khoảng trắng!";
    public static final String SDT_EMPTY = "Số điện thoại không được rỗng!";
    public static final String SDT_INVALID = "Số điện thoại không hợp lệ!";
    public static final String NGAYSINH_INVALID = "Nhân viên phải đủ 18 tuổi trở lên!";
    public static final String EMAIL_INVALID = "Email không hợp lệ!";
    public static final String CCCD_EMPTY = "Số cccd không được rỗng!";
    public static final String CCCD_INVALID = "Số cccd chỉ chứa tối đa là 12 số!";
    
    public NhanVien() {
    }

    public NhanVien(String maNV) {
        this.maNV = maNV;
    }
    
    public NhanVien(String maNV, String tenNV, boolean gioiTinh, LocalDate ngaySinh, String email, String soDienThoai, String cccd, String diaChi, String chucVu, boolean trangThai) throws Exception {
        setMaNV(maNV);
        setTenNV(tenNV);
        setGioiTinh(gioiTinh);
        setNgaySinh(ngaySinh);
        setEmail(email);
        setSoDienThoai(soDienThoai);
        setCccd(cccd);
        setDiaChi(diaChi);
        setChucVu(chucVu);
        setTrangThai(trangThai);
        
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) throws Exception {
        tenNV = tenNV.trim();
        if (tenNV.isEmpty()) {
            throw new Exception(TENNV_EMPTY);
        }
        if (!tenNV.matches("^[\\p{L} ]+$")) {
            throw new Exception(TENNV_INVALID);
        }
        this.tenNV = tenNV;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) throws Exception {
        if(LocalDate.now().getYear() - ngaySinh.getYear() < 18)
            throw new Exception("Nhân viên phải đủ 18 tuổi trở lên");
        this.ngaySinh = ngaySinh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception{
        email = email.trim();
        if(!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}$")) {
            throw new Exception(EMAIL_INVALID);
        }
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) throws Exception{
        soDienThoai = soDienThoai.trim();
        if(soDienThoai.isEmpty()) {
            throw new Exception(SDT_EMPTY);
        }
        if(!soDienThoai.matches("^(02|03|05|07|08|09)\\d{8}$")) {
            throw new Exception(SDT_INVALID);
        }
        this.soDienThoai = soDienThoai;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) throws Exception {
        cccd = cccd.trim();
        if(cccd.isEmpty()) {
            throw new Exception(CCCD_EMPTY);
        }
        if(!cccd.matches("\\d{12}")) {
            throw new Exception(CCCD_INVALID);
        }
        this.cccd = cccd;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.maNV);
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
        final NhanVien other = (NhanVien) obj;
        return Objects.equals(this.maNV, other.maNV);
    }
}
