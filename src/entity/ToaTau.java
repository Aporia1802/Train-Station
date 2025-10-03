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
    private String tenToa;
    private int soKhoangTau;
    private Tau tau;
    
    public static final String TENTOA_EMPTY = "Tên toa không được rỗng!";
    public static final String SOKHOANG_INVALID = "Số khoang tàu phải lớn hơn 0!";
    public static final String TAU_NULL = "Tàu không được null!";
    
    public ToaTau() {
    }
    
    public ToaTau(String maToa, String tenToa, int soKhoangTau, Tau tau) throws Exception {
        setMaToa(maToa);
        setTenToa(tenToa);
        setSoKhoangTau(soKhoangTau);
        setTau(tau);
    }
    
    public String getMaToa() {
        return maToa;
    }
    
    public void setMaToa(String maToa) {
        this.maToa = maToa;
    }
    
    public String getTenToa() {
        return tenToa;
    }
    
    public void setTenToa(String tenToa) throws Exception {
        tenToa = tenToa.trim();
        if (tenToa.isEmpty()) {
            throw new Exception(TENTOA_EMPTY);
        }
        this.tenToa = tenToa;
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
