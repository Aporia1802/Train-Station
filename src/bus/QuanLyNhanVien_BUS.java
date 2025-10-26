/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.NhanVien_DAO;
import entity.NhanVien;
import java.util.ArrayList;

/**
 *
 * @author CÔNG HOÀNG
 */
public class QuanLyNhanVien_BUS {
    private final NhanVien_DAO nhanVienDao = new NhanVien_DAO();

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


    
}

