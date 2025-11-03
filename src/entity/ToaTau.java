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
public class ToaTau {
    private String maToa;
    private int soHieuToa;
    private int soKhoangTau;
    private String loaiToa;
    private Tau tau;
    
    public static final String TENTOA_EMPTY = "Tên toa không được rỗng!";
    public static final String SOHIEU_INVALID = "Số hiêu toa tàu phải lớn hơn 0!";
    public static final String SOKHOANG_INVALID = "Số khoang tàu phải lớn hơn 0!";
    public static final String TAU_NULL = "Tàu không được null!";

    
    public ToaTau() {
    }

    public ToaTau(String maToa) {
        this.maToa = maToa;
    }

    public ToaTau(String maToa, int soHieuToa, int soKhoangTau, String loaiToa, Tau tau) throws Exception{
        setMaToa(maToa);
        setSoHieuToa(soHieuToa);
        setSoKhoangTau(soKhoangTau);
        setLoaiToa(loaiToa);
        setTau(tau);
    }
    
    public String getMaToa() {
        return maToa;
    }
    
    public void setMaToa(String maToa) {
        this.maToa = maToa;
    }
    
    public int getSoKhoangTau() {
        return soKhoangTau;
    }
    
    public void setSoKhoangTau(int soKhoangTau) throws Exception {
        if (soKhoangTau <= 0) {
            throw new Exception(SOKHOANG_INVALID);
        }
        this.soKhoangTau = soKhoangTau;
    }
    
    public int getSoHieuToa() {
        return soHieuToa;
    }

    public void setSoHieuToa(int soHieuToa) throws Exception {
        if(soHieuToa < 0) {
            throw new Exception(SOHIEU_INVALID);
        }
        this.soHieuToa = soHieuToa;
    }
    
    public Tau getTau() {
        return tau;
    }
    
    public void setTau(Tau tau) throws Exception {
        if (tau == null) {
            throw new Exception(TAU_NULL);
        }
        this.tau = tau;
    }

    public String getLoaiToa() {
        return loaiToa;
    }

    public void setLoaiToa(String loaiToa) {
        this.loaiToa = loaiToa;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.maToa);
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
        final ToaTau other = (ToaTau) obj;
        return Objects.equals(this.maToa, other.maToa);
    }
    
    
}
