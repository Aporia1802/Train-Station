/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.ChuyenTau_DAO;
import dao.Ve_DAO;
import entity.ChuyenTau;
import entity.Ve;
import java.time.LocalDate;

/**
 *
 * @author CÔNG HOÀNG
 */
public class QuanLyDatVe_BUS {
    private final Ve_DAO veDAO = new Ve_DAO();
    private final ChuyenTau_DAO ctDAO = new ChuyenTau_DAO();
    
    public Ve getVeById(String maVe) {
        return veDAO.getOne(maVe);
    }
    
    public ChuyenTau timKiemChuyenTau(String gaDi, String gaDen, boolean isMotChieu, LocalDate ngayDI, LocalDate ngayVe) {
        return ctDAO.timKiemChuyenTau(gaDi, gaDen, isMotChieu, ngayDI, ngayVe);
    }
}
