/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author CÔNG HOÀNG
 */
public class LoaiGhe {
    private String maLoaiGhe;
    private String tenLoaiGhe;
    private String moTa;
    private double heSoLoaiGhe;
    
    public static final String TENLOAIGHE_EMPTY = "Tên loại ghế không được rỗng!";
    
    public LoaiGhe() {
    }

    public LoaiGhe(String maLoaiGhe) {
        this.maLoaiGhe = maLoaiGhe;
    }
    
    
    public LoaiGhe(String maLoaiGhe, String tenLoaiGhe, String moTa, double heSoLoaiGhe) throws Exception {
        setMaLoaiGhe(maLoaiGhe);
        setTenLoaiGhe(tenLoaiGhe);
        setMoTa(moTa);
        setHeSoLoaiGhe(heSoLoaiGhe);
    }
    
    public String getMaLoaiGhe() {
        return maLoaiGhe;
    }
    
    public void setMaLoaiGhe(String maLoaiGhe) {
        this.maLoaiGhe = maLoaiGhe;
    }
    
    public String getTenLoaiGhe() {
        return tenLoaiGhe;
    }
    
    public void setTenLoaiGhe(String tenLoaiGhe) throws Exception {
        tenLoaiGhe = tenLoaiGhe.trim();
        if (tenLoaiGhe.isEmpty()) {
            throw new Exception(TENLOAIGHE_EMPTY);
        }
        this.tenLoaiGhe = tenLoaiGhe;
    }
    
    public String getMoTa() {
        return moTa;
    }
    
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    
    public double getHeSoLoaiGhe() {
        return heSoLoaiGhe;
    }
    
    public void setHeSoLoaiGhe(double heSoLoaiGhe) {
        this.heSoLoaiGhe = heSoLoaiGhe;
    }
}
