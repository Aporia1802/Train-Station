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
public class TraCuuGaTau_BUS {
    private final GaTau_DAO gaTauDAO = new GaTau_DAO();
    
    public ArrayList<GaTau> getAllGaTau() {
        return gaTauDAO.getAll();
    }
    
    public ArrayList<GaTau> filter(String maGa, String tenGa, String diaChi, String soDienThoai) {
        return gaTauDAO.filter(maGa, tenGa, diaChi, soDienThoai);
    }
}
