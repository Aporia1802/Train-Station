/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Ve_DAO;
import entity.Ve;

/**
 *
 * @author CÔNG HOÀNG
 */
public class QuanLyVe_BUS {
    private final Ve_DAO veDAO = new Ve_DAO();
    
    public Ve getVeById(String maVe) {
        return veDAO.getOne(maVe);
    }
}
