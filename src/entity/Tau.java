/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enums.TrangThaiTau;
import java.time.LocalDate;

/**
 *
 * @author CÔNG HOÀNG
 */
public class Tau {
    private String maTau;
    private int soToaTau;
    private int sucChua;
    private LocalDate ngayHoatDong;
    private TrangThaiTau trangThai;
    
    public static final String SOTOA_INVALID = "Số toa tàu phải lớn hơn 0!";
    
    public Tau(String maTau) {
        setMaTau(maTau);
    }
    
    public Tau(int soToaTau, int sucChua, LocalDate ngayHoatDong, 
              TrangThaiTau trangThai) throws Exception {
        setSoToaTau(soToaTau);
        setSucChua(sucChua);
        setNgayHoatDong(ngayHoatDong);
        setTrangThai(trangThai);
    }
    
    public String getMaTau() {
        return maTau;
    }
    
    public void setMaTau(String maTau) {
        this.maTau = maTau;
    }
    
    public int getSoToaTau() {
        return soToaTau;
    }
    
    public void setSoToaTau(int soToaTau) throws Exception {
        if (soToaTau <= 0) {
            throw new Exception(SOTOA_INVALID);
        }
        this.soToaTau = soToaTau;
    }
    
    public int getSucChua() {
        return sucChua;
    }
    
    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }

    public LocalDate getNgayHoatDong() {
        return ngayHoatDong;
    }
    
    public void setNgayHoatDong(LocalDate ngayHoatDong) {
        this.ngayHoatDong = ngayHoatDong;
    }
    
    public TrangThaiTau getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiTau trangThai) {
        this.trangThai = trangThai;
    }
}
