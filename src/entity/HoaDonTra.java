/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * @author CÔNG HOÀNG
 */
public class HoaDonTra {
    private String maHoaDonTra;
    private LocalDateTime ngayTra;
    private NhanVien nhanVien;
    private Ve ve;
    private double soTienHoanTra;

    public HoaDonTra() {
    }

    public HoaDonTra(String maHoaDonTra, LocalDateTime ngayTra, NhanVien nhanVien, Ve ve) {
        setMaHoaDonTra(maHoaDonTra);
        setNgayTra(ngayTra);
        setNhanVien(nhanVien);
        setVe(ve);
        setSoTienHoanTra();
    }

    public String getMaHoaDonTra() {
        return maHoaDonTra;
    }

    public void setMaHoaDonTra(String maHoaDonTra) {
        this.maHoaDonTra = maHoaDonTra;
    }

    public LocalDateTime getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(LocalDateTime ngayTra) {
        this.ngayTra = ngayTra;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public Ve getVe() {
        return ve;
    }

    public void setVe(Ve ve) {
        this.ve = ve;
    }

    public double getSoTienHoanTra() {
        return soTienHoanTra;
    }
    
    public double phiHuy() {
        double phiHuy = 0;
        LocalDateTime gioTauChay = ve.getChuyenTau().getThoiGianDi();
        LocalDateTime gioHienTai = LocalDateTime.now();

        // Tính khoảng thời gian giữa hai thời điểm
        Duration duration = Duration.between(gioHienTai, gioTauChay);
        long hours = duration.toHours();
        
        if(hours >= 24) {
            phiHuy = this.ve.getGiaVe() * 0.1;
        } 
        else if(hours >= 4 && hours < 24) {
            phiHuy = this.ve.getGiaVe() * 0.2;
        }
        
        return phiHuy;
    }

    public void setSoTienHoanTra() {
        this.soTienHoanTra = this.ve.getGiaVe() - phiHuy();
    }
}
