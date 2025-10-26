/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.GaTau_DAO;
import entity.GaTau;
import java.util.ArrayList;

/**
 *
 * @author CÔNG HOÀNG
 */
public class QuanLyGaTau_BUS {
    private GaTau_DAO dao;

    public QuanLyGaTau_BUS() {
        this.dao = new GaTau_DAO();
    }

    public ArrayList<GaTau> getAllGaTau() {
        try {
            return dao.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public GaTau getGaTauByMa(String maGa) {
        try {
            if (maGa == null || maGa.trim().isEmpty()) {
                return null;
            }
            return dao.getOne(maGa);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<GaTau> getGaTauByKeyword(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return getAllGaTau();
            }
            return dao.getGaTauByKeyword(keyword.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean createGaTau(GaTau gaTau) {
        try {
            if (gaTau == null) {
                return false;
            }
            
            String error = validateGaTau(gaTau);
            if (error != null) {
                System.err.println("❌ " + error);
                return false;
            }
            
            return dao.create(gaTau);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateGaTau(String maGa, GaTau gaTauMoi) {
        try {
            if (maGa == null || maGa.trim().isEmpty() || gaTauMoi == null) {
                return false;
            }
            
            String error = validateGaTau(gaTauMoi);
            if (error != null) {
                System.err.println("❌ " + error);
                return false;
            }
            
            return dao.update(maGa, gaTauMoi);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


  
    private String validateGaTau(GaTau gaTau) {
        if (gaTau == null) {
            return "Ga tàu không được null!";
        }
        
        if (gaTau.getMaGa() == null || gaTau.getMaGa().trim().isEmpty()) {
            return "Mã ga không được rỗng!";
        }
        
        if (gaTau.getTenGa() == null || gaTau.getTenGa().trim().isEmpty()) {
            return "Tên ga không được rỗng!";
        }
        
        if (gaTau.getDiaChi() == null || gaTau.getDiaChi().trim().isEmpty()) {
            return "Địa chỉ không được rỗng!";
        }
        
        if (gaTau.getSoDienThoai() == null || gaTau.getSoDienThoai().trim().isEmpty()) {
            return "Số điện thoại không được rỗng!";
        }
 
        if (!gaTau.getSoDienThoai().matches("^(02|03|05|07|08|09)\\d{8}$")) {
            return "Số điện thoại không hợp lệ! (VD: 0901234567)";
        }
        
        return null;
    }

 public String generateMaGa(String tenGa) {
        try {
            return dao.generateID(tenGa);
        } catch (Exception e) {
            e.printStackTrace();
            return "GA-" + System.currentTimeMillis();
        }
    }
    
     public ArrayList<GaTau> filter(String maGa, String tenGa, String tinhThanh, String soDienThoai) {
        try {
            return dao.filter(maGa, tenGa, tinhThanh, soDienThoai);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
