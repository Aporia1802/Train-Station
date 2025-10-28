/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Ve_DAO;
import entity.Ve;
import java.time.LocalDate;
import java.util.ArrayList;

public class TraCuuVe_BUS {
    private Ve_DAO dao;

    public TraCuuVe_BUS() {
        this.dao = new Ve_DAO();
    }
    
    public ArrayList<Ve> getAllVe() {
        return dao.getAll();
    }
    
    public ArrayList<Ve> timKiemVe(String maVe, String hoTen, String cccd, LocalDate ngayDi) {
        return dao.timKiemVe(maVe, hoTen, cccd, ngayDi);
    }
}
