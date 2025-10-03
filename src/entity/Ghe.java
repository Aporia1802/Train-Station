/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author CÔNG HOÀNG
 */
public class Ghe {
    private String maGhe;
    private String viTri;
    private String trangThaiGhe;
    private LoaiGhe loaiGhe;
    private KhoangTau khoangTau;
    
    public static final String TRANGTHAI_INVALID = "Trạng thái ghế không hợp lệ!";
    public static final String LOAIGHE_NULL = "Loại ghế không được null!";
    public static final String KHOANGTAU_NULL = "Khoang tàu không được null!";
    
    public Ghe() {
    }
    
    public Ghe(String maGhe, String viTri, String trangThaiGhe, 
               LoaiGhe loaiGhe, KhoangTau khoangTau) throws Exception {
        setMaGhe(maGhe);
        setViTri(viTri);
        setTrangThaiGhe(trangThaiGhe);
        setLoaiGhe(loaiGhe);
        setKhoangTau(khoangTau);
    }
    
    public String getMaGhe() {
        return maGhe;
    }
    
    public void setMaGhe(String maGhe) {
        this.maGhe = maGhe;
    }
    
    public String getViTri() {
        return viTri;
    }
    
    public void setViTri(String viTri) {
        this.viTri = viTri;
    }
    
    public String getTrangThaiGhe() {
        return trangThaiGhe;
    }
    
    public void setTrangThaiGhe(String trangThaiGhe) throws Exception {
        if (!trangThaiGhe.equals("Trống") && !trangThaiGhe.equals("Đã đặt") && 
            !trangThaiGhe.equals("Đang giữ chỗ")) {
            throw new Exception(TRANGTHAI_INVALID);
        }
        this.trangThaiGhe = trangThaiGhe;
    }
    
    public LoaiGhe getLoaiGhe() {
        return loaiGhe;
    }
    
    public void setLoaiGhe(LoaiGhe loaiGhe) throws Exception {
        if (loaiGhe == null) {
            throw new Exception(LOAIGHE_NULL);
        }
        this.loaiGhe = loaiGhe;
    }
    
    public KhoangTau getKhoangTau() {
        return khoangTau;
    }
    
    public void setKhoangTau(KhoangTau khoangTau) throws Exception {
        if (khoangTau == null) {
            throw new Exception(KHOANGTAU_NULL);
        }
        this.khoangTau = khoangTau;
    }
}
