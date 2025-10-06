/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import entity.NhanVien;
import entity.TaiKhoan;

/**
 *
 * @author CÔNG HOÀNG
 */
public class Login_BUS {
    private final TaiKhoan_DAO taiKhoanDAO = new TaiKhoan_DAO();
    private final NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
    
    public NhanVien login(String tenDangNhap, String matKhau) throws Exception {
        TaiKhoan taiKhoan = taiKhoanDAO.getOne(tenDangNhap);
        
        if (taiKhoan == null) {
             throw new Exception("Tài khoản hoặc mật khẩu không chính xác!");
        }
        
        if(!matKhau.equals(taiKhoan.getMatKhau())) {
            throw new Exception("Tài khoản hoặc mật khẩu không chính xác!");
        }
        
        if (!nhanVienDAO.getOne(taiKhoan.getNhanVien().getMaNV()).isTrangThai()) {
            throw new Exception("Tài khoản đã bị vô hiệu hóa do nhân viên đã thôi việc!");
        }
        
        return nhanVienDAO.getOne(taiKhoan.getNhanVien().getMaNV());
    }
}
