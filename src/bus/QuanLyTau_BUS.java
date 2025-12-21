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
        ArrayList<Tau> dsTau = tauDao.filterByTrangThai(trangThai);
        return dsTau;
    }
    
    public ArrayList<Tau> getTauTheoMa(String maTau) throws Exception{
        ArrayList<Tau> dsTau = tauDao.getTauByMaTau(maTau);
        return dsTau;
    }
    
    public Boolean themTau(Tau tau) throws Exception {
      return tauDao.create(tau);
        
    }
    
    public String generateID() {
        // Lấy mã lớn nhất
        String maxID = tauDao.getMaxID();

        if(maxID == null || maxID.equals("")) {
            return "TAU-001";
        }

        // Tách phần số - xử lý cả trường hợp có dấu "-"
        String numPart = maxID.replaceAll("[^0-9]", ""); // Chỉ lấy số
        int num = Integer.parseInt(numPart);

        // Tăng lên một đơn vị
        num++;

        // Tạo mã mới với format TAU-XXX
        String newID = String.format("TAU-%03d", num);

        return newID;
    }
    
    public Boolean capNhatTau(Tau tau) {
        return tauDao.update(tau.getMaTau(), tau);
    }
}
