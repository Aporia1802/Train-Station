/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui.components;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;

/**
 *
 * @author CÔNG HOÀNG
 */
public class GheButton extends JButton {
    private boolean daChon = false;

    public GheButton(int soGhe) {
        super(String.valueOf(soGhe));
        setBackground(Color.LIGHT_GRAY);
        setFocusPainted(false);
        setPreferredSize(new Dimension(40, 40));

        addActionListener(e -> toggleChon());
    }

    private void toggleChon() {
        daChon = !daChon;
        if (daChon) {
            setBackground(new Color(0, 153, 0)); // xanh khi chọn
        } else {
            setBackground(Color.LIGHT_GRAY); // xám khi bỏ chọn
        }
    }

    public boolean isDaChon() {
        return daChon;
    }
}
