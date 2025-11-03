/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.ChuyenTau_DAO;
import dao.GaTau_DAO;
import dao.Ghe_DAO;
import dao.ToaTau_DAO;
import dao.Ve_DAO;
import entity.ChuyenTau;
import entity.GaTau;
import entity.Ghe;
import entity.ToaTau;
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
    private final ToaTau_DAO toaTauDAO = new ToaTau_DAO();
    private final Ghe_DAO gheDAO = new Ghe_DAO();
    
    private ArrayList<Ghe> danhSachGheDaChon = new ArrayList();

    public ArrayList<ToaTau> getDanhSachToaTau(ChuyenTau chuyen) throws Exception {
        // Gọi DAO
        return toaTauDAO.getByTau(chuyen.getTau().getMaTau());
    }

//    public ArrayList<Ghe> getDanhSachGhe(ToaTau toa, ChuyenTau chuyen) throws Exception {
//        // Gọi DAO - lấy tất cả ghế của toa này
//        return gheDAO.getByToa(toa.getMaToa());
//    }

    public boolean isGheDaDat(Ghe ghe, ChuyenTau chuyen) {
        // Kiểm tra ghế đã được đặt trong chuyến này chưa
        return veDAO.isGheDaDat(ghe.getMaGhe(), chuyen.getMaChuyenTau());
    }

    public boolean isDaChonGhe(Ghe ghe) {
        return danhSachGheDaChon.stream()
        .anyMatch(g -> g.getMaGhe().equals(ghe.getMaGhe()));
    }

    public void themGheDaChon(Ghe ghe) {
        if (!isDaChonGhe(ghe)) {
            danhSachGheDaChon.add(ghe);
        }
    }

    public void xoaGheDaChon(Ghe ghe) {
        danhSachGheDaChon.removeIf(g -> g.getMaGhe().equals(ghe.getMaGhe()));
    }

    public ArrayList<Ghe> getDanhSachGheDaChon() {
        return danhSachGheDaChon;
    }
    
    public ArrayList<GaTau> getAllGaTau() {
        return gaTauDAO.getAll();
    }
    
    public ArrayList<ChuyenTau> timKiemChuyenTau(String gaDi, String gaDen, LocalDate ngay) throws Exception {
        return ctDAO.timKiemChuyenTau(gaDi, gaDen, ngay);
    }
}
