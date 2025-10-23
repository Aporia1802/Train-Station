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
public class TraCuuChuyenTau_BUS {
    GaTau_DAO gaTauDAO = new GaTau_DAO();
    
    public ArrayList<GaTau> getAllGaTau() {
        return gaTauDAO.getAll();
    }
}
