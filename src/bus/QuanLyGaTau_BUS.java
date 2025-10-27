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
    private GaTau_DAO gaTauDAO = new GaTau_DAO();

    public ArrayList<GaTau> getAllGaTau() {
        return gaTauDAO.getAll();
    }

    public GaTau getGaTauByMa(String maGa) {
        try {
            if (maGa == null || maGa.trim().isEmpty()) {
                return null;
            }
            return gaTauDAO.getOne(maGa);
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
            return gaTauDAO.getGaTauByKeyword(keyword.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


     public ArrayList<GaTau> filter(String maGa, String tenGa, String tinhThanh, String soDienThoai) {
        return gaTauDAO.filter(maGa, tenGa, tinhThanh, soDienThoai);
    }
    
    public Boolean updateThongTinGa(GaTau gaTau) {
        return gaTauDAO.update(gaTau.getMaGa(), gaTau);
    }
    
    public String generateID() {
//      Lấy mã lớn nhất
        String maxID = gaTauDAO.getMaxID();
        
        if(maxID.equals("")) {
            return "GA001";
        }
        
//      Tách phần số
        int num = Integer.parseInt(maxID.substring(2));
        
//      Tăng lên một đơn vị
        num++;
        
//      Tạo mã mới 
        String newID = String.format("GA%03d", num);
        
        return newID;
    }
    
    public Boolean themGaTau(GaTau gaTau) {
        return gaTauDAO.create(gaTau);
    }
}
