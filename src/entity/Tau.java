/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enums.TrangThaiTau;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author CÔNG HOÀNG
 */
public class Tau {
    private String maTau;
    private String tenTau;
    private int soToaTau;
    private LocalDate ngayHoatDong;
    private TrangThaiTau trangThai;
    
    public static final String SOTOA_INVALID = "Số toa tàu phải lớn hơn 0!";

    public Tau() {
    }
    
    
    public Tau(String maTau) {
        setMaTau(maTau);
    }
    
    public Tau(String maTau, String tenTau, int soToaTau, LocalDate ngayHoatDong, 
              TrangThaiTau trangThai) throws Exception {
        setMaTau(maTau);
        setTenTau(tenTau);
        setSoToaTau(soToaTau);
        setNgayHoatDong(ngayHoatDong);
        setTrangThai(trangThai);
    }
    
    public String getMaTau() {
        return maTau;
    }
    
    public void setMaTau(String maTau) {
        this.maTau = maTau;
    }
    
    public String getTenTau() {
        return tenTau;
    }

    public void setTenTau(String tenTau) {
        this.tenTau = tenTau;
    }
    
    public int getSoToaTau() {
        return soToaTau;
    }
    
    public void setSoToaTau(int soToaTau) throws Exception {
        if (soToaTau <= 0) {
            throw new Exception(SOTOA_INVALID);
        }
        this.soToaTau = soToaTau;
    }

    public LocalDate getNgayHoatDong() {
        return ngayHoatDong;
    }
    
    public void setNgayHoatDong(LocalDate ngayHoatDong) {
        this.ngayHoatDong = ngayHoatDong;
    }
    
    public TrangThaiTau getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiTau trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.maTau);
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
        final Tau other = (Tau) obj;
        return Objects.equals(this.maTau, other.maTau);
    }
}
