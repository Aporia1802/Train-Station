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
    private int soHieuKhoang;
    private int sucChua;
    private ToaTau toaTau;
    
    public static final String TENKHOANG_EMPTY = "Tên khoang không được rỗng!";
    public static final String SUCCHUA_INVALID = "Sức chứa phải lớn hơn 0!";
    public static final String SOHIEU_INVALID = "Số hiệu khoang phải lớn hơn 0!";
    public static final String TOATAU_NULL = "Toa tàu không được null!";
    
    public KhoangTau(String maKhoangTau) {
        setMaKhoangTau(maKhoangTau);
    }
        public KhoangTau() {
    }
    
    public KhoangTau(String maKhoangTau, int soHieuKhoang, int sucChua, ToaTau toaTau) throws Exception {
        setMaKhoangTau(maKhoangTau);
        setSoHieuKhoang(soHieuKhoang);
        setSucChua(sucChua);
        setToaTau(toaTau);
    }
    
    public String getMaKhoangTau() {
        return maKhoangTau;
    }
    
    public void setMaKhoangTau(String maKhoangTau) {
        this.maKhoangTau = maKhoangTau;
    }

    public int getSoHieuKhoang() {
        return soHieuKhoang;
    }

    public void setSoHieuKhoang(int soHieuKhoang) throws Exception{
        if(soHieuKhoang < 0) {
            throw new Exception(SOHIEU_INVALID);
        }
        this.soHieuKhoang = soHieuKhoang;
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
