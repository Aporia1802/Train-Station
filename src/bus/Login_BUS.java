/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import entity.NhanVien;
import entity.TaiKhoan;
import static utils.HashPassword.comparePasswords;
import static utils.HashPassword.hashPassword;
import static utils.RandomPassword.RandomPassword;
import static utils.SendEmail.sendEmail;

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
        
        if(!comparePasswords(matKhau, taiKhoan.getMatKhau())) {
            throw new Exception("Tài khoản hoặc mật khẩu không chính xác!");
        }
        
        if (!nhanVienDAO.getOne(taiKhoan.getNhanVien().getMaNV()).isTrangThai()) {
            throw new Exception("Tài khoản đã bị vô hiệu hóa do nhân viên đã thôi việc!");
        }
        
        return nhanVienDAO.getOne(taiKhoan.getNhanVien().getMaNV());
    }
    
    public void resetPassword(String tenDangNhap, String email) throws Exception {
        TaiKhoan taiKhoan = taiKhoanDAO.getOne(tenDangNhap);
        
        if(taiKhoan == null) {
             throw new Exception("Tài khoản hoặc email không chính xác!");
        }
        
        NhanVien nhanVien = nhanVienDAO.getOne(taiKhoan.getNhanVien().getMaNV());
        
        if(!nhanVien.isTrangThai()) {
            throw new Exception("Tài khoản đã bị vô hiệu hóa do nhân viên đã thôi việc!");
        }
        
        if(!email.equals((nhanVien.getEmail()))) {
            throw new Exception("Tài khoản hoặc email không chính xác!");
        }
        
//      Sinh mật khẩu mới
        String matKhauMoi = RandomPassword();
        String matKhauHash = hashPassword(matKhauMoi);

//      Lưu mật khẩu cũ để rollback khi cần
        String matKhauCu = taiKhoan.getMatKhau();
        
        boolean updated = taiKhoanDAO.updatePassword(tenDangNhap, matKhauHash);
        
//      Cập nhật DB
        if (!updated) {
           throw new Exception("Không thể cập nhật mật khẩu trong cơ sở dữ liệu!");
        }

        try {
//      Gửi email
        sendEmail(email, "Reset mật khẩu",
                "Mật khẩu mới của bạn là: " + matKhauMoi + 
                ". Vui lòng đổi mật khẩu ngay sau khi đăng nhập.");
        } catch (Exception e) {
            // Rollback về mật khẩu cũ nếu email thất bại
            taiKhoanDAO.updatePassword(tenDangNhap, matKhauCu);
            throw new Exception("Gửi email thất bại, mật khẩu không thay đổi!");
        }   
    }
}
