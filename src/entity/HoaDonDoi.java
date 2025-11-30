/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.LocalDateTime;

/**
 *
 * @author CÔNG HOÀNG
 */
public class HoaDonDoi {
    private String maHoaDonDoi;
    private LocalDateTime ngayDoi;
    private NhanVien nhanVien;
    private Ve ve;
    private double phiDoi;
    
    public HoaDonDoi() {
        this.phiDoi = 20000;
    }
    
    public HoaDonDoi(String maHoaDonDoi, LocalDateTime ngayDoi, 
                     NhanVien nhanVien, Ve ve) {
        this.maHoaDonDoi = maHoaDonDoi;
        this.ngayDoi = ngayDoi;
        this.nhanVien = nhanVien;
        this.ve = ve;
        this.phiDoi = 20000;
    }
    
    // Getters & Setters
    public String getMaHoaDonDoi() { return maHoaDonDoi; }
    public void setMaHoaDonDoi(String maHoaDonDoi) { 
        this.maHoaDonDoi = maHoaDonDoi; 
    }
    
    public LocalDateTime getNgayDoi() { return ngayDoi; }
    public void setNgayDoi(LocalDateTime ngayDoi) { 
        this.ngayDoi = ngayDoi; 
    }
    
    public NhanVien getNhanVien() { return nhanVien; }
    public void setNhanVien(NhanVien nhanVien) { 
        this.nhanVien = nhanVien; 
    }
    
    public Ve getVe() { return ve; }
    public void setVe(Ve ve) { this.ve = ve; }
    
    public double getPhiDoi() { return phiDoi; }
    public void setPhiDoi(double phiDoi) { 
        this.phiDoi = phiDoi; 
    }
}
