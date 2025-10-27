/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.HanhKhach_DAO;
import entity.HanhKhach;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Laptopone
 */
public class TraCuuHanhKhach_BUS {

    private HanhKhach_DAO dao;

    public TraCuuHanhKhach_BUS() {
        dao = new HanhKhach_DAO();
    }

    public ArrayList<HanhKhach> getAllHanhKhach() {
        return (ArrayList<HanhKhach>) dao.getAll();
    }

    public ArrayList<HanhKhach> timKiemHanhKhach(String maHK, String tenHK, String cccd) {
        return dao.filterHanhKhach(maHK, tenHK, cccd, null);
    }

}
