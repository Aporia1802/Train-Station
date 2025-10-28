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
    
    public Boolean themTau(Tau tau) throws Exception {
      return tauDao.create(tau);
        
    }
    
    public String generateID() {
//      Lấy mã lớn nhất
        String maxID = tauDao.getMaxID();
        
        if(maxID.equals("")) {
            return "SE1";
        }
        
//      Tách phần số
        int num = Integer.parseInt(maxID.substring(2));
        
//      Tăng lên một đơn vị
        num++;
        
//      Tạo mã mới 
        String newID = String.format("SE%01d", num);
        
        return newID;
    }
    
    public Boolean capNhatTau(Tau tau) {
        return tauDao.update(tau.getMaTau(), tau);
    }
}
