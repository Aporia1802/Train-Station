/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author CÔNG HOÀNG
 */
public class LoaiVe {
    private String maLoaiVe;
    private String tenLoaiVe;
    private String moTa;
    private double heSoLoaiVe;
    
    public LoaiVe() {
    }
    
    public LoaiVe(String maLoaiVe, String tenLoaiVe, String moTa, double heSoLoaiVe) throws Exception {
        setMaLoaiVe(maLoaiVe);
        setTenLoaiVe(tenLoaiVe);
        setMoTa(moTa);
        setHeSoLoaiVe(heSoLoaiVe);
    }
    
    public String getMaLoaiVe() {
        return maLoaiVe;
    }
    
    public void setMaLoaiVe(String maLoaiVe) throws Exception {
        this.maLoaiVe = maLoaiVe;
    }
    
    public String getTenLoaiVe() {
        return tenLoaiVe;
    }
    
    public void setTenLoaiVe(String tenLoaiVe) throws Exception {
        this.tenLoaiVe = tenLoaiVe;
    }
    
    public double getHeSoLoaiVe() {
        return heSoLoaiVe;
    }
    
    public void setHeSoLoaiVe(double heSoLoaiVe) throws Exception {
        this.heSoLoaiVe = heSoLoaiVe;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    
    
}
