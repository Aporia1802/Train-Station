/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.ChuyenTau_DAO;
import dao.GaTau_DAO;
import dao.Ghe_DAO;
import dao.KhoangTau_DAO;
import dao.ToaTau_DAO;
import dao.Ve_DAO;
import entity.ChuyenTau;
import entity.GaTau;
import entity.Ghe;
import entity.Ve;
import entity.KhoangTau;
import entity.Tau;
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
    private final KhoangTau_DAO khoangTauDAO = new KhoangTau_DAO();
    
    // Danh sách ghế đang được chọn
    private ArrayList<Ghe> dsGheDaChon = new ArrayList<>();
    
     /**
     * Lấy danh sách toa tàu theo tàu
     */
    public ArrayList<ToaTau> getDanhSachToaTau(Tau tau) throws Exception {
        return toaTauDAO.getToaTauTheoMaTau(tau.getMaTau());
    }
    
    /**
     * Lấy danh sách khoang theo toa tàu
     */
    public ArrayList<KhoangTau> getDanhSachKhoangTheoToa(ToaTau toa) throws Exception {
        return khoangTauDAO.getKhoangTauTheoMaToa(toa.getMaToa());
    }
    
    /**
     * Lấy danh sách ghế theo toa tàu (cho toa ngồi mềm - không có khoang)
     */
    public ArrayList<Ghe> getDanhSachGheTheoToa(ToaTau toa) throws Exception {
        ArrayList<Ghe> dsGhe = new ArrayList<>();
        
        // Lấy tất cả khoang của toa
        ArrayList<KhoangTau> dsKhoang = getDanhSachKhoangTheoToa(toa);
        
        // Lấy tất cả ghế từ các khoang
        for (KhoangTau khoang : dsKhoang) {
            dsGhe.addAll(gheDAO.getGheTheoMaKhoang(khoang.getMaKhoangTau()));
        }
        
        // Sắp xếp theo số ghế
        dsGhe.sort((g1, g2) -> Integer.compare(g1.getSoGhe(), g2.getSoGhe()));
        
        return dsGhe;
    }
    
    /**
     * Lấy danh sách ghế theo khoang tàu
     */
    public ArrayList<Ghe> getDanhSachGheTheoKhoang(KhoangTau khoang) throws Exception {
        ArrayList<Ghe> dsGhe = gheDAO.getGheTheoMaKhoang(khoang.getMaKhoangTau());
        
        // Sắp xếp theo số ghế
        dsGhe.sort((g1, g2) -> Integer.compare(g1.getSoGhe(), g2.getSoGhe()));
        
        return dsGhe;
    }
    
    /**
     * Kiểm tra ghế đã được đặt trong chuyến tàu hay chưa
     */
    public boolean isGheDaDat(Ghe ghe, ChuyenTau chuyen) throws Exception {
        // Lấy danh sách vé của chuyến tàu
        ArrayList<Ve> dsVe = veDAO.getVeTheoMaChuyenTau(chuyen.getMaChuyenTau());
        
        // Kiểm tra xem có vé nào sử dụng ghế này không
        for (Ve ve : dsVe) {
            if (ve.getGhe().getMaGhe().equals(ghe.getMaGhe())) {
                // Kiểm tra trạng thái vé (chỉ tính vé còn hiệu lực)
                // Trạng thái: 0-Đã đặt, 1-Đã thanh toán, 2-Đã hủy
                if (ve.getTrangThai().compare(0) || ve.getTrangThai().compare(1)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Kiểm tra ghế đã được chọn trong session hiện tại
     */
    public boolean isDaChonGhe(Ghe ghe) {
        for (Ghe g : dsGheDaChon) {
            if (g.getMaGhe().equals(ghe.getMaGhe())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Thêm ghế vào danh sách đã chọn
     */
    public void themGheDaChon(Ghe ghe) {
        if (!isDaChonGhe(ghe)) {
            dsGheDaChon.add(ghe);
        }
    }
    
    /**
     * Xóa ghế khỏi danh sách đã chọn
     */
    public void xoaGheDaChon(Ghe ghe) {
        dsGheDaChon.removeIf(g -> g.getMaGhe().equals(ghe.getMaGhe()));
    }
    
    /**
     * Lấy danh sách ghế đã chọn
     */
    public ArrayList<Ghe> getDanhSachGheDaChon() {
        return new ArrayList<>(dsGheDaChon);
    }
    
    /**
     * Xóa toàn bộ danh sách ghế đã chọn
     */
    public void clearDanhSachGheDaChon() {
        dsGheDaChon.clear();
    }
    
    /**
     * Đếm số ghế đã chọn
     */
    public int getSoGheDaChon() {
        return dsGheDaChon.size();
    }
    
    public ArrayList<GaTau> getAllGaTau() {
        return gaTauDAO.getAll();
    }
    
    public ArrayList<ChuyenTau> timKiemChuyenTau(String gaDi, String gaDen, LocalDate ngay) throws Exception {
        return ctDAO.timKiemChuyenTau(gaDi, gaDen, ngay);
    }
    
    public Ghe timGheTheoMa(String maGhe) {
    // Tìm trong danh sách ghế đã chọn
     for (Ghe ghe : getDanhSachGheDaChon()) {
            if (ghe.getMaGhe().equals(maGhe)) {
                return ghe;
            }
        }
        return null;
    }
    
}
