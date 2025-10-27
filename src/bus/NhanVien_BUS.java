/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.NhanVien_DAO;
import entity.NhanVien;

/**
 *
 * @author Laptopone
 */
public class NhanVien_BUS {

    private NhanVien_DAO dao = new NhanVien_DAO();

    public NhanVien getNhanVien(String maNV) {
        return dao.getOne(maNV);
    }

    public boolean updateThongTin(NhanVien nv) {
        return dao.update(nv.getMaNV(), nv);
    }

}
