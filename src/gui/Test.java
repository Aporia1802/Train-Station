/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author CÔNG HOÀNG
 */
public class Test extends JFrame{
     public Test() {
        // Thiết lập JFrame
        setTitle("Đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 650);
        setLocationRelativeTo(null); // Căn giữa màn hình

        // Thêm Login_GUI (JPanel)
        Login_GUI loginPanel = new Login_GUI();
        this.setContentPane(loginPanel);

        // Hiển thị
        setVisible(true);
    }

    public static void main(String[] args) {
        // Đặt Look and Feel FlatLaf (nếu muốn)
        try {
            javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            
            
        } catch (Exception ex) {
            System.err.println("Không thể khởi tạo FlatLaf");
        }

        // Chạy giao diện
        java.awt.EventQueue.invokeLater(() -> {
            new Test();
        });
    }
}
