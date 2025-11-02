/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author CÔNG HOÀNG
 */
public class ChuyenTau {
    private String maChuyenTau;
    private TuyenDuong tuyenDuong;
    private LocalDateTime thoiGianDi;
    private LocalDateTime thoiGianDen;
    private Tau tau;
    private int soGheDaDat;
    private int soGheConTrong;
    
    // Các hằng số thông báo lỗi
    public static final String TUYENDUONG_EMPTY = "Tuyến đường không được rỗng!";
    public static final String THOIGIANDEN_INVALID = "Thời gian đến phải sau thời gian đi!";
    public static final String TAU_EMPTY = "Tàu không được rỗng!";
    public static final String SOGHEDADAT_INVALID = "Số ghế đã đặt không hợp lệ!";

    public ChuyenTau() {
    }
    
    public ChuyenTau(String maChuyenTau) {
        this.maChuyenTau = maChuyenTau;
    }
    
    public ChuyenTau(String maChuyenTau, TuyenDuong tuyenDuong, LocalDateTime thoiGianDi, 
                     LocalDateTime thoiGianDen, Tau tau) throws Exception {
        setMaChuyenTau(maChuyenTau);
        setTuyenDuong(tuyenDuong);
        setThoiGianDi(thoiGianDi);
        setThoiGianDen(thoiGianDen);
        setTau(tau);
    }
    
    // Getter và Setter
    public String getMaChuyenTau() {
        return maChuyenTau;
    }
    
    public void setMaChuyenTau(String maChuyenTau) {
        this.maChuyenTau = maChuyenTau;
    }
    
    public TuyenDuong getTuyenDuong() {
        return tuyenDuong;
    }
    
    public void setTuyenDuong(TuyenDuong tuyenDuong) throws Exception {
        if (tuyenDuong == null) {
            throw new Exception(TUYENDUONG_EMPTY);
        }
        this.tuyenDuong = tuyenDuong;
    }
    
    public LocalDateTime getThoiGianDi() {
        return thoiGianDi;
    }
    
    public void setThoiGianDi(LocalDateTime thoiGianDi) throws Exception {
        this.thoiGianDi = thoiGianDi;
    }
    
    public LocalDateTime getThoiGianDen() {
        return thoiGianDen;
    }
    
    public void setThoiGianDen(LocalDateTime thoiGianDen) throws Exception {
        if (thoiGianDen == null || (this.thoiGianDi != null && thoiGianDen.isBefore(this.thoiGianDi))) {
            throw new Exception(THOIGIANDEN_INVALID);
        }
        this.thoiGianDen = thoiGianDen;
    }
    
    public Tau getTau() {
        return tau;
    }
    
    public void setTau(Tau tau) throws Exception {
        if (tau == null) {
            throw new Exception(TAU_EMPTY);
        }
        this.tau = tau;
    }
    
    public int getSoGheDaDat() {
        return soGheDaDat;
    }
    
    public void setSoGheDaDat(int soGheDaDat) {
        this.soGheDaDat = soGheDaDat;
    }
    
    public int getSoGheConTrong() {
        return soGheConTrong;
    }
    
    public void setSoGheConTrong(int soGheControng) {
        this.soGheConTrong = soGheControng;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.maChuyenTau);
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
        final ChuyenTau other = (ChuyenTau) obj;
        return Objects.equals(this.maChuyenTau, other.maChuyenTau);
    }
    
    
}
