/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.ChuyenTau_DAO;
import dao.GaTau_DAO;
import dao.Ve_DAO;
import entity.ChuyenTau;
import entity.GaTau;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author CÔNG HOÀNG
 */
public class QuanLyDatVe_BUS {
    private final Ve_DAO veDAO = new Ve_DAO();
    private final ChuyenTau_DAO ctDAO = new ChuyenTau_DAO();
    private final GaTau_DAO gaTauDAO = new GaTau_DAO();
    
    public ArrayList<GaTau> getAllGaTau() {
        return gaTauDAO.getAll();
    }
    
    public ArrayList<ChuyenTau> timKiemChuyenTau(String gaDi, String gaDen, LocalDate ngay) throws Exception {
        return ctDAO.timKiemChuyenTau(gaDi, gaDen, ngay);
    }
}
