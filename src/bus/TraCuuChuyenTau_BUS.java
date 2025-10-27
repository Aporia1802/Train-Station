/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.ChuyenTau_DAO;
import dao.GaTau_DAO;
import entity.ChuyenTau;
import entity.GaTau;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author CÔNG HOÀNG
 */
public class TraCuuChuyenTau_BUS {
    GaTau_DAO gaTauDAO = new GaTau_DAO();
    ChuyenTau_DAO chuyenTauDAO = new ChuyenTau_DAO();
    
    public ArrayList<GaTau> getAllGaTau() {
        return gaTauDAO.getAll();
    }
    
    public ArrayList<ChuyenTau> getAllChuyenTau() {
        return chuyenTauDAO.getAll();
    }
    
    public ArrayList<ChuyenTau> filter(String tenGaDi, String tenGaDen, LocalDate ngayDi) throws Exception {
        return chuyenTauDAO.filter(tenGaDi, tenGaDen, ngayDi);
    }
}
