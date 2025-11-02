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
public class LoaiGhe {
    private String maLoaiGhe;
    private String tenLoaiGhe;
    private String moTa;
    private double heSoLoaiGhe;
    
    public static final String TENLOAIGHE_EMPTY = "Tên loại ghế không được rỗng!";
    public static final String MOTA_EMPTY = "Mô tả không được rỗng!";
    public static final String HESOLOAIGHE_INVALID = "Hệ số loại ghế phải lơn hơn 0!";
    
    public LoaiGhe() {
    }

    public LoaiGhe(String maLoaiGhe) {
        this.maLoaiGhe = maLoaiGhe;
    }
    
    
    public LoaiGhe(String maLoaiGhe, String tenLoaiGhe, String moTa, double heSoLoaiGhe) throws Exception {
        setMaLoaiGhe(maLoaiGhe);
        setTenLoaiGhe(tenLoaiGhe);
        setMoTa(moTa);
        setHeSoLoaiGhe(heSoLoaiGhe);
    }
    
    public String getMaLoaiGhe() {
        return maLoaiGhe;
    }
    
    public void setMaLoaiGhe(String maLoaiGhe) {
        this.maLoaiGhe = maLoaiGhe;
    }
    
    public String getTenLoaiGhe() {
        return tenLoaiGhe;
    }
    
    public void setTenLoaiGhe(String tenLoaiGhe) throws Exception {
        tenLoaiGhe = tenLoaiGhe.trim();
        if (tenLoaiGhe.isEmpty()) {
            throw new Exception(TENLOAIGHE_EMPTY);
        }
        this.tenLoaiGhe = tenLoaiGhe;
    }
    
    public String getMoTa() {
        return moTa;
    }
    
    public void setMoTa(String moTa) throws Exception {
        moTa = moTa.trim();
        if(moTa.equals("")) {
            throw new Exception(MOTA_EMPTY);
        }
        this.moTa = moTa;
    }
    
    public double getHeSoLoaiGhe() {
        return heSoLoaiGhe;
    }
    
    public void setHeSoLoaiGhe(double heSoLoaiGhe) throws Exception {
        if(heSoLoaiGhe < 0) {
            throw new Exception(HESOLOAIGHE_INVALID);
        }
        this.heSoLoaiGhe = heSoLoaiGhe;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.maLoaiGhe);
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
        final LoaiGhe other = (LoaiGhe) obj;
        return Objects.equals(this.maLoaiGhe, other.maLoaiGhe);
    }
    
    
}
