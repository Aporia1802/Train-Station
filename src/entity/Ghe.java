/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enums.TrangThaiGhe;

/**
 *
 * @author CÔNG HOÀNG
 */
public class Ghe {
    private String maGhe;
    private int soGhe;
    private TrangThaiGhe trangThaiGhe;
    private LoaiGhe loaiGhe;
    private KhoangTau khoangTau;
    
    public static final String LOAIGHE_NULL = "Loại ghế không được null!";
    public static final String KHOANGTAU_NULL = "Khoang tàu không được null!";
    
    public Ghe() {
    }
    
    public Ghe(String maGhe, int soGhe, TrangThaiGhe trangThaiGhe, 
               LoaiGhe loaiGhe, KhoangTau khoangTau) throws Exception {
        setMaGhe(maGhe);
        setSoGhe(soGhe);
        setTrangThaiGhe(trangThaiGhe);
        setLoaiGhe(loaiGhe);
        setKhoangTau(khoangTau);
    }
    
    public String getMaGhe() {
        return maGhe;
    }
    
    public void setMaGhe(String maGhe) {
        this.maGhe = maGhe;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }
    
    public TrangThaiGhe getTrangThaiGhe() {
        return trangThaiGhe;
    }

    public void setTrangThaiGhe(TrangThaiGhe trangThaiGhe) {
        this.trangThaiGhe = trangThaiGhe;
    }
    
    
    public LoaiGhe getLoaiGhe() {
        return loaiGhe;
    }
    
    public void setLoaiGhe(LoaiGhe loaiGhe) throws Exception {
        if (loaiGhe == null) {
            throw new Exception(LOAIGHE_NULL);
        }
        this.loaiGhe = loaiGhe;
    }
    
    public KhoangTau getKhoangTau() {
        return khoangTau;
    }
    
    public void setKhoangTau(KhoangTau khoangTau) throws Exception {
        if (khoangTau == null) {
            throw new Exception(KHOANGTAU_NULL);
        }
        this.khoangTau = khoangTau;
    }
}
