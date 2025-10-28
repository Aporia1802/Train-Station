/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author CÔNG HOÀNG
 */
public class ToaTau {
    private String maToa;
    private int soHieuToa;
    private int soKhoangTau;
    private int soCho;
    private Tau tau;
    
    public static final String TENTOA_EMPTY = "Tên toa không được rỗng!";
    public static final String SOHIEU_INVALID = "Số hiêu toa tàu phải lớn hơn 0!";
    public static final String SOKHOANG_INVALID = "Số khoang tàu phải lớn hơn 0!";
    public static final String SOCHO_INVALID = "Số chỗ trong toa phải lơn hơn 0!";
    public static final String TAU_NULL = "Tàu không được null!";

    
    public ToaTau() {
    }

    public ToaTau(String maToa) {
        this.maToa = maToa;
    }

    public ToaTau(String maToa, int soHieuToa, int soKhoangTau, int soCho, Tau tau) throws Exception{
        setMaToa(maToa);
        setSoHieuToa(soHieuToa);
        setSoKhoangTau(soKhoangTau);
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

    public int getSoCho() {
        return soCho;
    }

    public void setSoCho(int soCho) throws Exception{
        if(soCho < 0) {
            throw new Exception(SOCHO_INVALID);
        }
        this.soCho = soCho;
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
}
