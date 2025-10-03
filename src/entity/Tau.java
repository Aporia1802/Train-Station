/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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
    private String hangSanXuat;
    private LocalDate namSanXuat;
    
    public static final String SOTOA_INVALID = "Số toa tàu phải lớn hơn 0!";
    public static final String HANGSANXUAT_EMPTY = "Hãng sản xuất không được rỗng!";
    public static final String NAMSANXUAT_INVALID = "Năm sản xuất phải trước ngày hiện tại!";
    
    public Tau() {
    }
    
    public Tau(int soToaTau, int sucChua, LocalDate ngayHoatDong, 
               String hangSanXuat, LocalDate namSanXuat) throws Exception {
        setSoToaTau(soToaTau);
        setSucChua(sucChua);
        setNgayHoatDong(ngayHoatDong);
        setHangSanXuat(hangSanXuat);
        setNamSanXuat(namSanXuat);
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
    
    public String getHangSanXuat() {
        return hangSanXuat;
    }
    
    public void setHangSanXuat(String hangSanXuat) throws Exception {
        hangSanXuat = hangSanXuat.trim();
        if (hangSanXuat.isEmpty()) {
            throw new Exception(HANGSANXUAT_EMPTY);
        }
        this.hangSanXuat = hangSanXuat;
    }
    
    public LocalDate getNamSanXuat() {
        return namSanXuat;
    }
    
    public void setNamSanXuat(LocalDate namSanXuat) throws Exception {
        if (namSanXuat.isAfter(LocalDate.now())) {
            throw new Exception(NAMSANXUAT_INVALID);
        }
        this.namSanXuat = namSanXuat;
    }
}
