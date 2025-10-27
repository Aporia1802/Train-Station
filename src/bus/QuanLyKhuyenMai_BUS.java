/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.KhuyenMai_DAO;
import entity.KhuyenMai;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author CÔNG HOÀNG
 */
public class QuanLyKhuyenMai_BUS {

    private final KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAO();
    
    public KhuyenMai getById(String maKhuyenMai) {
        return khuyenMaiDAO.getOne(maKhuyenMai);
    }

    public ArrayList<KhuyenMai> getAllKhuyenMai() {
        ArrayList<KhuyenMai> dsKhuyenMai = khuyenMaiDAO.getAll();
        return dsKhuyenMai;
    }

    public ArrayList<KhuyenMai> filter(String maKM, String tenKM, Double heSoKhuyenMai, LocalDate ngayBatDau, LocalDate ngayKetThuc, Boolean trangThai) {
        ArrayList<KhuyenMai> dsKhuyenMai = khuyenMaiDAO.filterKhuyenMai(maKM, tenKM, heSoKhuyenMai, ngayBatDau, ngayKetThuc, trangThai);
        return dsKhuyenMai;
    }

    public ArrayList<KhuyenMai> getGaTauByKeyword(String keyword) {
        ArrayList<KhuyenMai> dsKhuyenMai = khuyenMaiDAO.getKhuyenMaiByKeyword(keyword);
        return dsKhuyenMai;
    }

    public ArrayList<KhuyenMai> filterByTrangThai(String trangThai) {
        return khuyenMaiDAO.filterByTrangThai(trangThai);
    }

    public boolean themKhuyenMai(KhuyenMai km) {
        return khuyenMaiDAO.create(km);
    }

    public boolean capNhatKhuyenMai(KhuyenMai km) {
        return khuyenMaiDAO.update(km.getMaKhuyenMai(), km);
    }

    public String generateID() {
        String maxID = khuyenMaiDAO.getMaxID();
        
        if(maxID.equals("")) {
            return "KM001";
        }
        
//      Tách phần số
        int num = Integer.parseInt(maxID.substring(2));
        
//      Tăng lên một đơn vị
        num++;
        
//      Tạo mã mới 
        String newID = String.format("KM%03d", num);
        
        return newID;
    }

}
