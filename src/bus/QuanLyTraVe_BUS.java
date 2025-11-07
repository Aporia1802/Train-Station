/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.ChuyenTau_DAO;
import dao.Ghe_DAO;
import dao.HoaDonTra_DAO;
import dao.Ve_DAO;
import entity.HoaDonTra;
import entity.Ve;

/**
 *
 * @author CÔNG HOÀNG
 */
public class QuanLyTraVe_BUS {
    private final HoaDonTra_DAO hoaDonTraDAO = new HoaDonTra_DAO();
    private final Ve_DAO veDAO = new Ve_DAO();
    private final Ghe_DAO gheDAO = new Ghe_DAO();
    private final ChuyenTau_DAO chuyenTauDAO = new ChuyenTau_DAO();
    
    public Ve getVeById(String maVe) {
        return veDAO.getOne(maVe);
    }
    
    public String generateID() {
//      Lấy mã lớn nhất
        String maxID = hoaDonTraDAO.getMaxID();
        
        if(maxID.equals("")) {
            return "HDT001";
        }
        
//      Tách phần số
        int num = Integer.parseInt(maxID.substring(3));
        
//      Tăng lên một đơn vị
        num++;
        
//      Tạo mã mới 
        String newID = String.format("HDT%03d", num);
        
        return newID;
    }
    
    public boolean create(HoaDonTra hdt) throws Exception {
//      Tạo và lưu hóa đơn trả
        if (!hoaDonTraDAO.create(hdt)) {
            throw new Exception("Không thể lưu hóa đơn trả!");
        }


        return true;
    }
    
}
