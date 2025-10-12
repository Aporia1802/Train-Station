/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author CÔNG HOÀNG
 */
public class GaTau {
    private String maGa;
    private String tenGa;
    private String diaChi;
    private String soDienThoai;
    
    public static final String TENGA_EMPTY = "Tên ga không được rỗng!";
    public static final String DIACHI_EMPTY = "Địa chỉ không được rỗng!";
    public static final String SDT_INVALID = "Số điện thoại không hợp lệ!";
    public static final String SDT_EMPTY = "Số điện thoại không được rỗng!";
    
    public GaTau() {
    }
    
    public GaTau(String maGa) {
        this.maGa = maGa;
    }
    
    public GaTau(String tenGa, String diaChi, String soDienThoai) throws Exception {
        setTenGa(tenGa);
        setDiaChi(diaChi);
        setSoDienThoai(soDienThoai);
    }
    
    public String getMaGa() {
        return maGa;
    }
    
    public void setMaGa(String maGa) {
        this.maGa = maGa;
    }
    
    public String getTenGa() {
        return tenGa;
    }
    
    public void setTenGa(String tenGa) throws Exception {
        tenGa = tenGa.trim();
        if (tenGa.isEmpty()) {
            throw new Exception(TENGA_EMPTY);
        }
        this.tenGa = tenGa;
    }
    
    public String getDiaChi() {
        return diaChi;
    }
    
    public void setDiaChi(String diaChi) throws Exception {
        diaChi = diaChi.trim();
        if (diaChi.isEmpty()) {
            throw new Exception(DIACHI_EMPTY);
        }
        this.diaChi = diaChi;
    }
    
    public String getSoDienThoai() {
        return soDienThoai;
    }
    
    public void setSoDienThoai(String soDienThoai) throws Exception {
        soDienThoai = soDienThoai.trim();
        if (soDienThoai.isEmpty()) {
            throw new Exception(SDT_EMPTY);
        }
        if (!soDienThoai.matches("^(02|03|05|07|08|09)\\d{8}$")) {
            throw new Exception(SDT_INVALID);
        }
        this.soDienThoai = soDienThoai;
    }

}
