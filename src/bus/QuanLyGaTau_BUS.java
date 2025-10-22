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
    private final GaTau_DAO gaTauDAO = new GaTau_DAO();
    
    public ArrayList<GaTau> getAllGaTau() {
        ArrayList<GaTau> dsGaTau = gaTauDAO.getAll();
        return dsGaTau;
    }
    
    public ArrayList<GaTau> filter(String maGa, String tenGa, String diaChi, String soDienThoai) {
        ArrayList<GaTau> dsGaTau = gaTauDAO.filter(maGa, tenGa, diaChi, soDienThoai);
        return dsGaTau;
    }
    
    public ArrayList<GaTau> getGaTauByKeyword(String keyword) {
        ArrayList<GaTau> gaTau = gaTauDAO.getGaTauByKeyword(keyword);
        return gaTau;
    }
}
