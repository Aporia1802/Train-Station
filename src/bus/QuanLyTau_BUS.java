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
        
        ArrayList<Tau> dsTau = tauDao.getAll(); 
        return dsTau;
    }
    
public ArrayList<Tau> filter(String trangThai){
        ArrayList<Tau> dsTau = tauDao.filterByComboBoxTrangThai(trangThai);
        return dsTau;
        
    }
    
    public ArrayList<Tau> getTauTheoMa(String maTau){
        ArrayList<Tau> dsTau = tauDao.getTauByMaTau(maTau);
        return dsTau;
    }
    
    public Boolean themNhanVien(Tau tau) throws Exception {
      return tauDao.create(tau);
        
    }
    
    public Boolean capNhatNhanVien(Tau tau) {
        return tauDao.update(tau.getMaTau(), tau);
    }
}
