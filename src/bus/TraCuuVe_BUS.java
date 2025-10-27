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
    
    public ArrayList<Ve> searchVe(String maVe, String hoTen, String cccd, LocalDate ngayDi) {
        try {
           
            if ((maVe == null || maVe.trim().isEmpty()) &&
                (hoTen == null || hoTen.trim().isEmpty()) &&
                (cccd == null || cccd.trim().isEmpty()) &&
                ngayDi == null) {
                
                System.out.println("️ Chưa nhập điều kiện tìm kiếm");
                return new ArrayList<>();
            }
            
            return dao.search(maVe, hoTen, cccd, ngayDi);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Ve getVeByMa(String maVe) {
        try {
            if (maVe == null || maVe.trim().isEmpty()) {
                return null;
            }
            return dao.getOne(maVe);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Ve> getAllVe() {
        try {
            return dao.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
