/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author CÔNG HOÀNG
 */
public class KhoangTau {
    private String maKhoangTau;
    private String tenKhoangTau;
    private int sucChua;
    private ToaTau toaTau;
    
    public static final String TENKHOANG_EMPTY = "Tên khoang không được rỗng!";
    public static final String SUCCHUA_INVALID = "Sức chứa phải lớn hơn 0!";
    public static final String TOATAU_NULL = "Toa tàu không được null!";
    
    public KhoangTau() {
    }
    
    public KhoangTau(String maKhoangTau, int sucChua, ToaTau toaTau) throws Exception {
        setMaKhoangTau(maKhoangTau);
        setSucChua(sucChua);
        setToaTau(toaTau);
    }
    
    public String getMaKhoangTau() {
        return maKhoangTau;
    }
    
    public void setMaKhoangTau(String maKhoangTau) {
        this.maKhoangTau = maKhoangTau;
    }
    
    public String getTenKhoangTau() {
        return tenKhoangTau;
    }
    
    public void setTenKhoangTau(String tenKhoangTau) throws Exception {
        tenKhoangTau = tenKhoangTau.trim();
        if (tenKhoangTau.isEmpty()) {
            throw new Exception(TENKHOANG_EMPTY);
        }
        this.tenKhoangTau = tenKhoangTau;
    }
    
    public int getSucChua() {
        return sucChua;
    }
    
    public void setSucChua(int sucChua) throws Exception {
        if (sucChua <= 0) {
            throw new Exception(SUCCHUA_INVALID);
        }
        this.sucChua = sucChua;
    }
    
    public ToaTau getToaTau() {
        return toaTau;
    }
    
    public void setToaTau(ToaTau toaTau) throws Exception {
        if (toaTau == null) {
            throw new Exception(TOATAU_NULL);
        }
        this.toaTau = toaTau;
    }
}
