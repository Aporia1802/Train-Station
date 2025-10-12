/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Objects;

/**
 *
 * @author CÔNG HOÀNG
 */
public class TaiKhoan {
    private String tenDangNhap;
    private String matKhau;
    private NhanVien nhanVien;
    
    public static final String MATKHAU_INVALID="Mật khẩu phải ít nhất 8 kí tự (Bao gồm chữ hoa, chữ thường và số)!";

//  Khi tạo mới tài khoản
    public TaiKhoan(String tenDangNhap, String matKhau, NhanVien nhanVien) throws Exception {
        this.tenDangNhap = tenDangNhap;
        setMatKhau(matKhau);
        setNhanVien(nhanVien);
    }
    
//  Khi đổi mật khẩu, và đăng nhập
    public TaiKhoan(String matKhau, NhanVien nhanVien) throws Exception{
        setMatKhau(matKhau);
        setNhanVien(nhanVien);
    }
    
    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
    
    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) throws Exception {
//        matKhau = matKhau.trim();
//        if(!matKhau.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$")) {
//            throw new Exception(MATKHAU_INVALID);
//        }
        this.matKhau = matKhau;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final TaiKhoan other = (TaiKhoan) obj;
        return Objects.equals(this.tenDangNhap, other.tenDangNhap);
    }
    
    
}
