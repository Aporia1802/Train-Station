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
    private double heSoLoaiVe;
    
    public static final String MALOAIVE_INVALID = "Mã loại vé không hợp lệ!";
    public static final String TENLOAIVE_EMPTY = "Tên loại vé không được rỗng!";
    public static final String HESOVE_INVALID = "Hệ số vé phải lớn hơn 0 và nhỏ hơn hoặc bằng 1!";
    
    public LoaiVe() {
    }
    
    public LoaiVe(String maLoaiVe, String tenLoaiVe, double heSoLoaiVe) throws Exception {
        setMaLoaiVe(maLoaiVe);
        setTenLoaiVe(tenLoaiVe);
        setHeSoLoaiVe(heSoLoaiVe);
    }
    
    public String getMaLoaiVe() {
        return maLoaiVe;
    }
    
    public void setMaLoaiVe(String maLoaiVe) throws Exception {
        if (!maLoaiVe.equals("LV-TE") && !maLoaiVe.equals("LV-HSSV") && 
            !maLoaiVe.equals("LV-NL")) {
            throw new Exception(MALOAIVE_INVALID);
        }
        this.maLoaiVe = maLoaiVe;
    }
    
    public String getTenLoaiVe() {
        return tenLoaiVe;
    }
    
    public void setTenLoaiVe(String tenLoaiVe) throws Exception {
        tenLoaiVe = tenLoaiVe.trim();
        if (tenLoaiVe.isEmpty()) {
            throw new Exception(TENLOAIVE_EMPTY);
        }
        this.tenLoaiVe = tenLoaiVe;
    }
    
    public double getHeSoLoaiVe() {
        return heSoLoaiVe;
    }
    
    public void setHeSoLoaiVe(double heSoLoaiVe) throws Exception {
        if (heSoLoaiVe <= 0 || heSoLoaiVe > 1) {
            throw new Exception(HESOVE_INVALID);
        }
        this.heSoLoaiVe = heSoLoaiVe;
    }
}
