/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.ChuyenTau_DAO;
import dao.Ghe_DAO;
import dao.HoaDon_DAO;
import dao.KhoangTau_DAO;
import dao.ToaTau_DAO;
import dao.Ve_DAO;
import entity.ChuyenTau;
import entity.Ghe;
import entity.HoaDon;
import entity.KhoangTau;
import entity.ToaTau;
import entity.Ve;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author CÔNG HOÀNG
 */
public class QuanLyDatVe_BUS {
    private final Ve_DAO veDAO = new Ve_DAO();
    private final ChuyenTau_DAO ctDAO = new ChuyenTau_DAO();
    private final Ghe_DAO gheDAO = new Ghe_DAO();
    private final ToaTau_DAO toaTauDAO = new ToaTau_DAO();
    private final KhoangTau_DAO khoangTauDAO = new KhoangTau_DAO();
    private final HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
       
    public ChuyenTau getChuyenTauById(String maChuyenTau) {
        return ctDAO.getOne(maChuyenTau);
    }
    
    public ArrayList<ChuyenTau> timKiemChuyenTau(String gaDi, String gaDen, LocalDate ngayDi) throws Exception {
        return ctDAO.timKiemChuyenTau(gaDi, gaDen, ngayDi);
    }
    
    public ArrayList<ToaTau> getToaTau(String maTau) {
        return toaTauDAO.getToaTauTheoTau(maTau);
    }
    
    public ArrayList<KhoangTau> getKhoangTau(String maToaTau) {
        return khoangTauDAO.getKhoangTauTheoToa(maToaTau);
    }
    
    public ArrayList<Ghe> getGhe(String maKhoangTau) {
        return gheDAO.getGheTheoKhoangTau(maKhoangTau);
    }
    
    public Boolean taoHoaDon(HoaDon hoaDon) {
        return hoaDonDAO.create(hoaDon);
    }
    
    public Boolean taoVe(Ve ve) {
        return veDAO.create(ve);
    }
    
}
