/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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
    public TaiKhoan(String tenDangNhap, String matKhau, NhanVien nhanVien) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.nhanVien = nhanVien;
    }
//  Khi đổi mật khẩu
    public TaiKhoan(String matKhau, NhanVien nhanVien) throws Exception{
        setMatKhau(matKhau);
        this.nhanVien = nhanVien;
    }
    
    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) throws Exception {
        matKhau = matKhau.trim();
        if(!matKhau.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)[A-Za-z\\\\d]{8,}")) {
            throw new Exception(MATKHAU_INVALID);
        }
        this.matKhau = matKhau;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }
}
