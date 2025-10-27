/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import entity.NhanVien;
import entity.TaiKhoan;
import java.util.ArrayList;

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
    
    public ArrayList<NhanVien> getNhanVienbySDT(String sdt){
        ArrayList<NhanVien> nhanVien = nhanVienDao.getNhanVienBySoDienThoai(sdt);
        return nhanVien;
    }
    
    public ArrayList<NhanVien> timNhanVien(String maNV, String tenNV, String cccd, String sdt, String gioiTinh, String trangThai){
        ArrayList<NhanVien> nhanVien = nhanVienDao.timKiemNhanVien(maNV,tenNV,cccd,sdt,gioiTinh,trangThai);
        return nhanVien;
    }
    
    public String generateID() {
//      Lấy mã lớn nhất
        String maxID = nhanVienDao.getMaxID();
        
        if(maxID.equals("")) {
            return "NV001";
        }
        
//      Tách phần số
        int num = Integer.parseInt(maxID.substring(2));
        
//      Tăng lên một đơn vị
        num++;
        
//      Tạo mã mới 
        String newID = String.format("NV%03d", num);
        
        return newID;
    }

    public Boolean themNhanVien(NhanVien nhanVien) throws Exception {
        nhanVienDao.create(nhanVien);
        TaiKhoan taiKhoan = new TaiKhoan(nhanVien.getSoDienThoai(), "123456", nhanVien);
        return taiKhoanDao.create(taiKhoan);
    }
    
    public Boolean capNhatNhanVien(NhanVien nhanVien) {
        return nhanVienDao.update(nhanVien.getMaNV(), nhanVien);
    }
    
}

