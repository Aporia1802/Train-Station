/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.quanLyVe;

import bus.QuanLyDatVe_BUS;
import gui.components.ChonChoNgoi;
import gui.components.ChonChuyenTau;
import gui.components.ThanhToan;
import gui.components.ThongTinVe;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

/**
 *
 * @author CÔNG HOÀNG
 */
public class DatVe_GUI extends javax.swing.JPanel {
    private QuanLyDatVe_BUS bus;
    private JPanel container;
    private int currentIndex = 0;
    private JPanel[] panels;
    private Animator animator;
    private boolean forward = true;
    private float fraction = 0f;
    private JLayeredPane slidePane;

    ChonChuyenTau chonChuyenTau;
    ChonChoNgoi chonChoNgoi;
    ThanhToan thanhToan;

    public DatVe_GUI() {
        setLayout(new BorderLayout());
        
        bus = new QuanLyDatVe_BUS();
        
        chonChuyenTau = new ChonChuyenTau(bus);
        chonChoNgoi = new ChonChoNgoi(bus);
        thanhToan = new ThanhToan(bus);
        
        thanhToan.setOnThanhToanThanhCong(() -> {
            resetToiDau();
        });

        // Tạo mảng panel
        panels = new JPanel[]{chonChuyenTau, chonChoNgoi, thanhToan};

        // Tạo vùng chứa hiệu ứng
        slidePane = new JLayeredPane();
        slidePane.setLayout(null);
        slidePane.setDoubleBuffered(true); 
        add(slidePane, BorderLayout.CENTER);

        // Gắn panel đầu tiên
        JPanel first = panels[0];
        first.setBounds(0, 0, getWidth(), getHeight());
        slidePane.add(first, Integer.valueOf(0));

        // Animator mượt hơn
        animator = new Animator(600);
        animator.setResolution(5);
        animator.setAcceleration(0.2f);
        animator.setDeceleration(0.2f);
        animator.addTarget(new TimingTargetAdapter() {
        @Override
        public void timingEvent(float f) {
            // easing mượt kiểu "ease in-out"
            fraction = (float) (0.5 - 0.5 * Math.cos(Math.PI * f));
            updateAnimation();
        }

        @Override
        public void end() {
            finishAnimation();
        }
    });

    initNavigation();
}

    private void initNavigation() {
        // Xử lý nút Next ở màn hình chọn chuyến tàu
        chonChuyenTau.next().addActionListener(e -> {
            if (validateChonChuyenTau()) {
                try {
                    // KHÔNG XÓA ghế cũ → giữ lại khi quay lại
                    chonChoNgoi.loadDanhSachGhe(
                        chonChuyenTau.getChuyenDiDaChon(), 
                        chonChuyenTau.getChuyenVeDaChon(), 
                        chonChuyenTau.isKhuHoi()
                    );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                next();
            }
        });
        
        chonChoNgoi.next().addActionListener(e -> {
            // 1. VALIDATE TRƯỚC KHI CHUYỂN
            if (!chonChoNgoi.validateThongTin()) {
                return; // Dừng lại nếu validate fail
            }

            // 2. LẤY THÔNG TIN
            String hoTen = chonChoNgoi.getHoTenKhachChinh();
            String sdt = chonChoNgoi.getSoDienThoai();
            String cccd = chonChoNgoi.getCCCD();

            java.util.ArrayList<ThongTinVe.ThongTinHanhKhach> dsHK = 
                chonChoNgoi.getDanhSachThongTinHanhKhach();

            // 3. ĐẨY DỮ LIỆU VÀO THANH TOÁN
            thanhToan.hienThiDuLieu(
                hoTen, sdt, cccd,
                dsHK,
                chonChuyenTau.getChuyenDiDaChon(),
                chonChuyenTau.getChuyenVeDaChon(),
                chonChuyenTau.isKhuHoi()
            );

            // 4. CHUYỂN SANG BƯỚC 3
            next();
        });
        
        chonChoNgoi.previous().addActionListener(e -> previous());
        thanhToan.previous().addActionListener(e -> previous());
    }
    
    private boolean validateChonChuyenTau() {
    //  Kiểm tra đã chọn chuyến đi chưa
        if (chonChuyenTau.getChuyenDiDaChon() == null) {
            javax.swing.JOptionPane.showMessageDialog(this, 
            "Vui lòng chọn chuyến tàu đi!",
            "Thông báo",
            javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }


    private void next() {
        if (!animator.isRunning() && currentIndex < panels.length - 1) {
            forward = true;
            startAnimation(panels[currentIndex], panels[currentIndex + 1]);
        }
    }

    private void previous() {
        if (!animator.isRunning() && currentIndex > 0) {
            forward = false;
            startAnimation(panels[currentIndex], panels[currentIndex - 1]);
        }
    }

    private JPanel currentPanel, nextPanel;

    private void startAnimation(JPanel current, JPanel next) {
        this.currentPanel = current;
        this.nextPanel = next;

        int w = slidePane.getWidth();
        int h = slidePane.getHeight();
        if (w == 0 || h == 0) {
            // Trì hoãn nếu panel chưa layout xong
            SwingUtilities.invokeLater(() -> startAnimation(current, next));
            return;
        }

        // Cập nhật kích thước panel
        current.setBounds(0, 0, w, h);
        next.setBounds(forward ? w : -w, 0, w, h);

        slidePane.removeAll();
        slidePane.add(current, Integer.valueOf(0));
        slidePane.add(next, Integer.valueOf(1));

        animator.start();
    }

    private void updateAnimation() {
        int w = slidePane.getWidth();
        int offset = (int) (w * fraction);
        if (forward) {
            currentPanel.setLocation(-offset, 0);
            nextPanel.setLocation(w - offset, 0);
        } else {
            currentPanel.setLocation(offset, 0);
            nextPanel.setLocation(-w + offset, 0);
        }
        slidePane.repaint();
    }

    private void finishAnimation() {
        currentIndex += forward ? 1 : -1;
        slidePane.removeAll();
        JPanel current = panels[currentIndex];
        current.setBounds(0, 0, slidePane.getWidth(), slidePane.getHeight());
        slidePane.add(current, Integer.valueOf(0));
        slidePane.repaint();
    }
    
    /**
     * Reset toàn bộ quy trình đặt vé về bước đầu tiên
     */
    public void resetToiDau() {
        // 1. Reset BUS - xóa tất cả ghế đã chọn
        bus.clearDanhSachGheDaChon();
        
        // 2. Reset các panel
        try {
            // Reset ChonChuyenTau - tạo mới
            chonChuyenTau = new ChonChuyenTau(bus);
            
            // Reset ChonChoNgoi - tạo mới
            chonChoNgoi = new ChonChoNgoi(bus);
            
            // Reset ThanhToan - tạo mới
            thanhToan = new ThanhToan(bus);
            
            // Cập nhật lại mảng panels
            panels[0] = chonChuyenTau;
            panels[1] = chonChoNgoi;
            panels[2] = thanhToan;
            
            // 3. Khởi tạo lại navigation
            initNavigation();
            
            // 4. Quay về màn hình đầu tiên
            currentIndex = 0;
            slidePane.removeAll();
            JPanel first = panels[0];
            first.setBounds(0, 0, slidePane.getWidth(), slidePane.getHeight());
            slidePane.add(first, Integer.valueOf(0));
            slidePane.revalidate();
            slidePane.repaint();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Có lỗi khi reset form: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void doLayout() {
        super.doLayout();
        // Cập nhật kích thước panel khi resize
        if (slidePane.getComponentCount() > 0) {
            Component c = slidePane.getComponent(0);
            c.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
