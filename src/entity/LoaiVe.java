/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Objects;

/**
 *
 * @author CÔNG HOÀNG
 */
public class LoaiVe {
    private String maLoaiVe;
    private String tenLoaiVe;
    private String moTa;
    private double heSoLoaiVe;
    
    public static final String TENLOAIVE_EMPTY = "Tên loại vé không được rỗng!";
    public static final String MOTA_EMPTY = "Mô tả không được rỗng!";
    public static final String HESOLOAIVE_INVALID = "Hệ số loại vé phải lơn hơn 0!";
    
    public LoaiVe() {
    }
    
    public LoaiVe(String maLoaiVe) {
        setMaLoaiVe(maLoaiVe);
    }
    
    public LoaiVe(String maLoaiVe, String tenLoaiVe, String moTa, double heSoLoaiVe) throws Exception {
        setMaLoaiVe(maLoaiVe);
        setTenLoaiVe(tenLoaiVe);
        setMoTa(moTa);
        setHeSoLoaiVe(heSoLoaiVe);
    }
    
    public String getMaLoaiVe() {
        return maLoaiVe;
    }
    
    public void setMaLoaiVe(String maLoaiVe) {
        this.maLoaiVe = maLoaiVe;
    }
    
    public String getTenLoaiVe() {
        return tenLoaiVe;
    }
    
    public void setTenLoaiVe(String tenLoaiVe) throws Exception {
        tenLoaiVe = tenLoaiVe.trim();
        if (tenLoaiVe.isEmpty()) {
            throw new Exception(TENLOAIVE_EMPTY);
        }
        this.tenLoaiVe = tenLoaiVe;
    }
    
    public double getHeSoLoaiVe() {
        return heSoLoaiVe;
    }
    
    public void setHeSoLoaiVe(double heSoLoaiVe) throws Exception {
        if(heSoLoaiVe < 0) {
            throw new Exception(HESOLOAIVE_INVALID);
        }
        this.heSoLoaiVe = heSoLoaiVe;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) throws Exception {
        moTa = moTa.trim();
        if (tenLoaiVe.isEmpty()) {
            throw new Exception(MOTA_EMPTY);
        }
        this.moTa = moTa;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.maLoaiVe);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LoaiVe other = (LoaiVe) obj;
        return Objects.equals(this.maLoaiVe, other.maLoaiVe);
    }
    
    
}
