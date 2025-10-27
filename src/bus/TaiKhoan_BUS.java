/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.TaiKhoan_DAO;

/**
 *
 * @author Laptopone
 */
public class TaiKhoan_BUS {

    private TaiKhoan_DAO dao = new TaiKhoan_DAO();

    public boolean doiMatKhau(String tenDangNhap, String matKhauMoi) {
        return dao.updatePassword(tenDangNhap, matKhauMoi);
    }

}
