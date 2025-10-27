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
public class HanhKhach {
    private String maHanhKhach;
    private String tenHanhKhach;
    private String cccd;
    private LocalDate ngaySinh;
    
    public static final String TENHANHKHACH_EMPTY = "Tên hành khách không được rỗng!";
    public static final String TENHANHKHACH_INVALID = "Tên hành khách chỉ được chứa kí tự chữ và khoảng trắng!";
    public static final String NGAYSINH_INVALID = "Phải trước ngày hiện tại!";
    
    public HanhKhach() {
    }
    
    public HanhKhach(String maHanhKhach, String tenHanhKhach, String cccd, LocalDate ngaySinh) throws Exception {
        setMaHanhKhach(maHanhKhach);
        setTenHanhKhach(tenHanhKhach);
        setCccd(cccd);
        setNgaySinh(ngaySinh);
    }
    
    public String getMaHanhKhach() {
        return maHanhKhach;
    }
    
    public void setMaHanhKhach(String maHanhKhach) {
        this.maHanhKhach = maHanhKhach;
    }
    
    public String getTenHanhKhach() {
        return tenHanhKhach;
    }
    
    public void setTenHanhKhach(String tenHanhKhach) throws Exception {
        tenHanhKhach = tenHanhKhach.trim();
        if (tenHanhKhach.isEmpty()) {
            throw new Exception(TENHANHKHACH_EMPTY);
        }
        if (!tenHanhKhach.matches("^[\\p{L} ]+$")) {
            throw new Exception(TENHANHKHACH_INVALID);
        }
        this.tenHanhKhach = tenHanhKhach;
    }
    
    public String getCccd() {
        return cccd;
    }
    
    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) throws Exception{
        if(ngaySinh.isAfter(LocalDate.now())) {
            throw new Exception(NGAYSINH_INVALID);
        }
        this.ngaySinh = ngaySinh;
    }
    
    
}
