/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import entity.NhanVien;
import entity.TaiKhoan;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import static utils.HashPassword.hashPassword;

/**
 *
 * @author CÔNG HOÀNG
 */
public class QuanLyNhanVien_BUS {
    private final NhanVien_DAO nhanVienDao = new NhanVien_DAO();
    private final TaiKhoan_DAO taiKhoanDao = new TaiKhoan_DAO();

    public ArrayList<NhanVien> getAllNhanVien(){
        
        ArrayList<NhanVien> dsNV = nhanVienDao.getAll(); 
        return dsNV;
    }
    
    public ArrayList<NhanVien> filter(String chucVu, String trangThai){
        ArrayList<NhanVien> dsNV = nhanVienDao.filterByComboBox(chucVu, trangThai);
        return dsNV;
        
    }
    
    public ArrayList<NhanVien> getNhanVienbySDT(String maNV){
        ArrayList<NhanVien> nhanVien = nhanVienDao.getNhanVienByMaNV(maNV);
        return nhanVien;
    }
    
    public ArrayList<NhanVien> timNhanVien(String maNV, String tenNV, String cccd, String sdt, String gioiTinh, String trangThai){
        ArrayList<NhanVien> nhanVien = nhanVienDao.timKiemNhanVien(maNV,tenNV,cccd,sdt,gioiTinh,trangThai);
        return nhanVien;
    }
    
    public String generateID(boolean gioiTinh, LocalDate ngaySinh) {
        // Lấy số thứ tự lớn nhất từ 3 ký tự cuối của tất cả mã nhân viên
        int maxSTT = nhanVienDao.getMaxSTT(); // Trả về số thứ tự lớn nhất

        // Tăng lên một đơn vị
        int stt = maxSTT + 1;

        // Xác định X (1 = Nam, 0 = Nữ)
        String x = gioiTinh ? "1" : "0";

        // Format ngày sinh thành DDMMYYYY
        // KHÔNG DÙNG SimpleDateFormat với LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String ngaySinhStr = ngaySinh.format(formatter);

        // Tạo mã mới theo format NVXDDMMYYYYOOO
        String newID = String.format("NV%s%s%03d", x, ngaySinhStr, stt);

        return newID;
    }

    public Boolean themNhanVien(NhanVien nhanVien) throws Exception {
        nhanVienDao.create(nhanVien);
        TaiKhoan taiKhoan = new TaiKhoan(nhanVien.getSoDienThoai(), hashPassword("Mk12345@"), nhanVien);
        return taiKhoanDao.create(taiKhoan);
    }
    
    public Boolean capNhatNhanVien(NhanVien nhanVien) {
        return nhanVienDao.update(nhanVien.getMaNV(), nhanVien);
    }
    
    public boolean checkEmailExists(String email) {
        return nhanVienDao.checkEmailExists(email);
    }

    public boolean checkSDTExists(String sdt) {
        return nhanVienDao.checkSDTExists(sdt);
    }

    public boolean checkCCCDExists(String cccd) {
        return nhanVienDao.checkCCCDExists(cccd);
    }

    public boolean checkEmailExistsExceptThis(String email, String maNV) {
        return nhanVienDao.checkEmailExistsExceptThis(email, maNV);
    }

    public boolean checkSDTExistsExceptThis(String sdt, String maNV) {
        return nhanVienDao.checkSDTExistsExceptThis(sdt, maNV);
    }

    public boolean checkCCCDExistsExceptThis(String cccd, String maNV) {
        return nhanVienDao.checkCCCDExistsExceptThis(cccd, maNV);
    }
}

