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
public class Ghe {
    private String maGhe;
    private int soGhe;
    private LoaiGhe loaiGhe;
    private KhoangTau khoangTau;
    
    public static final String LOAIGHE_NULL = "Loại ghế không được rỗng!";
    public static final String KHOANGTAU_NULL = "Khoang tàu không được rỗng!";
    
    public Ghe() {
    }
    
    public Ghe(String maGhe) {
        setMaGhe(maGhe);
    }
    
    public Ghe(String maGhe, int soGhe, LoaiGhe loaiGhe, KhoangTau khoangTau) throws Exception {
        setMaGhe(maGhe);
        setSoGhe(soGhe);
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.maGhe);
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
        final Ghe other = (Ghe) obj;
        return Objects.equals(this.maGhe, other.maGhe);
    }
    
    
}
