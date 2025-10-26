/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Tau_DAO;
import entity.Tau;
import java.util.ArrayList;

/**
 *
 * @author CÔNG HOÀNG
 */
public class QuanLyTau_BUS {
    private final Tau_DAO tauDao = new Tau_DAO();

    public ArrayList<Tau> getAllTau(){
        
        ArrayList<Tau> dsNV = tauDao.getAll(); 
        return dsNV;
    }
    
//    public ArrayList<NhanVien> filter(String chucVu, String trangThai){
//        ArrayList<NhanVien> dsNV = nhanVienDao.filterByComboBox(chucVu, trangThai);
//        return dsNV;
//        
//    }
//    
//    public ArrayList<NhanVien> getNhanVienbySDT(String sdt){
//        ArrayList<NhanVien> nhanVien = nhanVienDao.getNhanVienBySoDienThoai(sdt);
//        return nhanVien;
//    }
}
