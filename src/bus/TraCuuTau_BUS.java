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
public class TraCuuTau_BUS {
    private final Tau_DAO dao = new Tau_DAO();
    
    public ArrayList<Tau> getAllTau() {
        return dao.getAll();
    }
    
    public ArrayList<Tau> timKiemTau(String maTau, String tenTau, int trangThai) {
        return dao.search(maTau, tenTau, trangThai);
    }
}
