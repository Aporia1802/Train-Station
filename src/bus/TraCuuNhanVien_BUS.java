/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.NhanVien_DAO;
import entity.NhanVien;
import java.util.ArrayList;

/**
 *
 * @author CÔNG HOÀNG
 */
public class TraCuuNhanVien_BUS {
    private final NhanVien_DAO dao = new NhanVien_DAO();
    
    public ArrayList<NhanVien> getAllNhanVien() {
        return dao.getAll();
    }
    
    public ArrayList<NhanVien> timNhanVien(String maNV, String tenNV, String cccd, String sdt, String gioiTinh, String trangThai) {
        return dao.timKiemNhanVien(maNV, tenNV, cccd, sdt, gioiTinh, trangThai);
    }
}
