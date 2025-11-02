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
public class TuyenDuong {
    private String maTuyenDuong;
    private GaTau gaDi;
    private GaTau gaDen;
    private double quangDuong;
    private double soTienMotKm;
    
    // Các hằng số thông báo lỗi
    public static final String GADI_EMPTY = "Ga đi không được rỗng!";
    public static final String GADEN_EMPTY = "Ga đến không được rỗng!";
    public static final String QUANGDUONG_INVALID = "Quãng đường phải lớn hơn 0!";
    public static final String SOTIEN_INVALID = "Số tiền một km phải lớn hơn 0!";

    public TuyenDuong() {
    }
    
    public TuyenDuong(String maTuyenDuong) {
        this.maTuyenDuong = maTuyenDuong;
    }
    
    public TuyenDuong(String maTuyenDuong, GaTau gaDi, GaTau gaDen, double quangDuong, double soTienMotKm) throws Exception {
        setMaTuyenDuong(maTuyenDuong);
        setGaDi(gaDi);
        setGaDen(gaDen);
        setQuangDuong(quangDuong);
        setSoTienMotKm(soTienMotKm);
    }
    
    // Getter và Setter
    public String getMaTuyenDuong() {
        return maTuyenDuong;
    }
    
    public void setMaTuyenDuong(String maTuyenDuong) {
        this.maTuyenDuong = maTuyenDuong;
    }
    
    
    public GaTau getGaDi() {
        return gaDi;
    }
    
    public void setGaDi(GaTau gaDi) throws Exception {
        if (gaDi == null) {
            throw new Exception(GADI_EMPTY);
        }
        this.gaDi = gaDi;
    }
    
    public GaTau getGaDen() {
        return gaDen;
    }
    
    public void setGaDen(GaTau gaDen) throws Exception {
        if (gaDen == null) {
            throw new Exception(GADEN_EMPTY);
        }
        this.gaDen = gaDen;
    }
    
    public double getQuangDuong() {
        return quangDuong;
    }
    
    public void setQuangDuong(double quangDuong) throws Exception {
        if (quangDuong <= 0) {
            throw new Exception(QUANGDUONG_INVALID);
        }
        this.quangDuong = quangDuong;
    }
    
    public double getSoTienMotKm() {
        return soTienMotKm;
    }
    
    public void setSoTienMotKm(double soTienMotKm) throws Exception {
        if (soTienMotKm <= 0) {
            throw new Exception(SOTIEN_INVALID);
        }
        this.soTienMotKm = soTienMotKm;
    }
    
    /**
     * Tính giá vé cơ bản = quãng đường * số tiền một km
     * @return giá vé cơ bản
     */
    public double tinhGiaVeCoBan() {
        return quangDuong * soTienMotKm;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.maTuyenDuong);
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
        final TuyenDuong other = (TuyenDuong) obj;
        return Objects.equals(this.maTuyenDuong, other.maTuyenDuong);
    }
}
