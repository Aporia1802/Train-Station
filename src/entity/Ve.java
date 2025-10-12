/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author CÔNG HOÀNG
 */
public class Ve {
      private String maVe;
    private ChuyenTau chuyenDi;
    private HanhKhach hanhKhach;
    private Ghe ghe;
    private HoaDon hoaDon;
    private String trangThai;
    private double giaVe;
    private LoaiVe loaiVe;
    
    public static final String CHUYENDI_NULL = "Chuyến đi không được null!";
    public static final String HANHKHACH_NULL = "Hành khách không được null!";
    public static final String GHE_NULL = "Ghế không được null!";
    public static final String HOADON_NULL = "Hóa đơn không được null!";
    public static final String TRANGTHAI_INVALID = "Trạng thái vé không hợp lệ!";
    public static final String GIAVE_INVALID = "Giá vé phải lớn hơn 0!";
    public static final String LOAIVE_NULL = "Loại vé không được null!";
    
    public Ve() {
    }
    
    public Ve(String maVe, ChuyenTau chuyenDi, HanhKhach hanhKhach, 
              Ghe ghe, HoaDon hoaDon, String trangThai, 
              double giaVe, LoaiVe loaiVe) throws Exception {
        setMaVe(maVe);
        setChuyenDi(chuyenDi);
        setHanhKhach(hanhKhach);
        setGhe(ghe);
        setHoaDon(hoaDon);
        setTrangThai(trangThai);
        setGiaVe(giaVe);
        setLoaiVe(loaiVe);
    }
    
    public String getMaVe() {
        return maVe;
    }
    
    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }
    
    public ChuyenTau getChuyenDi() {
        return chuyenDi;
    }
    
    public void setChuyenDi(ChuyenTau chuyenDi) throws Exception {
        if (chuyenDi == null) {
            throw new Exception(CHUYENDI_NULL);
        }
        this.chuyenDi = chuyenDi;
    }
    
    public HanhKhach getHanhKhach() {
        return hanhKhach;
    }
    
    public void setHanhKhach(HanhKhach hanhKhach) throws Exception {
        if (hanhKhach == null) {
            throw new Exception(HANHKHACH_NULL);
        }
        this.hanhKhach = hanhKhach;
    }
    
    public Ghe getGhe() {
        return ghe;
    }
    
    public void setGhe(Ghe ghe) throws Exception {
        if (ghe == null) {
            throw new Exception(GHE_NULL);
        }
        this.ghe = ghe;
    }
    
    public HoaDon getHoaDon() {
        return hoaDon;
    }
    
    public void setHoaDon(HoaDon hoaDon) throws Exception {
        if (hoaDon == null) {
            throw new Exception(HOADON_NULL);
        }
        this.hoaDon = hoaDon;
    }
    
    public String getTrangThai() {
        return trangThai;
    }
    
    public void setTrangThai(String trangThai) throws Exception {
        if (!trangThai.equals("Đã hoàn thành") && !trangThai.equals("Đã đặt") && 
            !trangThai.equals("Đã hủy")) {
            throw new Exception(TRANGTHAI_INVALID);
        }
        this.trangThai = trangThai;
    }
    
    public double getGiaVe() {
        return giaVe;
    }
    
    public void setGiaVe(double giaVe) throws Exception {
        if (giaVe <= 0) {
            throw new Exception(GIAVE_INVALID);
        }
        this.giaVe = giaVe;
    }
    
    public LoaiVe getLoaiVe() {
        return loaiVe;
    }
    
    public void setLoaiVe(LoaiVe loaiVe) throws Exception {
        if (loaiVe == null) {
            throw new Exception(LOAIVE_NULL);
        }
        this.loaiVe = loaiVe;
    }
}
