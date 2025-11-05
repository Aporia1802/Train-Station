/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.components;

import dao.LoaiVe_DAO;
import entity.ChuyenTau;
import entity.Ghe;
import gui.custom.RoundedButton;
import java.awt.Font;
import java.util.Date;
import javax.swing.BorderFactory;
import utils.FormatUtil;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author CÔNG HOÀNG
 */
public class ThongTinVe extends javax.swing.JPanel {
    private Ghe ghe;
    private ChuyenTau ct;
    private LoaiVe_DAO dao;
    /**
     * Creates new form ThongTinVe
     */
    
    public class ThongTinHanhKhach {
        private String maGhe;
        private String hoTen;
        private String CCCD;
        private Date ngaySinh;
        private String loaiVe;
        public void setMaGhe(String maGhe) { this.maGhe = ghe.getMaGhe(); }
        public void setHoTen(String hoTen) { this.hoTen = hoTen; }
        public void setCCCD(String CCCD) { this.CCCD = CCCD; }
        public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }
        public void setLoaiVe(String loaiVe) { this.loaiVe = loaiVe; }

        public String getMaGhe() { return maGhe; }
        public String getHoTen() { return hoTen; }
        public String getCCCD() { return CCCD; }
        public Date getNgaySinh() { return ngaySinh; }
        public String getLoaiVe() { return loaiVe; }
    }

    public ThongTinVe(String thongTin, Ghe ghe, ChuyenTau ct) {
        initComponents();
        pnl_thongTinHK.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                null,
                thongTin,
                 javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        this.ghe = ghe;
        this.ct = ct;
        dao = new LoaiVe_DAO();
        lbl_giaVe.setText("Giá vé: " + FormatUtil.formatCurrency(ct.getTuyenDuong().tinhGiaVeCoBan() * ghe.getLoaiGhe().getHeSoLoaiGhe()));
        btn_bin.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        ChonChoNgoi parent = timParentChonChoNgoi(ThongTinVe.this);
        if (parent == null) return;

        // 1. XÓA GHẾ KHỎI BUS + MAP
        parent.xoaGheDaChon(ghe);

        // 2. XÓA PANEL THÔNG TIN
        parent.xoaPanelThongTinVe(ghe);

        // 3. TÌM VÀ CẬP NHẬT NÚT GHẾ TRÊN SƠ ĐỒ
        JPanel toaPanel = timToaPanelChuaGhe();
        if (toaPanel != null) {
            RoundedButton btnGhe = parent.findButtonBySoGhe(toaPanel, ghe.getSoGhe());
            if (btnGhe != null) {
                btnGhe.setBackground(Color.WHITE);
                // Xóa listener cũ
                for (java.awt.event.ActionListener al : btnGhe.getActionListeners()) {
                    btnGhe.removeActionListener(al);
                }
                // Thêm lại listener chọn ghế
                btnGhe.addActionListener(e -> parent.handleChonGhe(btnGhe, ghe));
            }

            // Đồng bộ checkbox
            try {
                ChuyenTau chuyen = parent.isGheThuocChuyenVe(ghe) ? parent.chuyenVe : parent.chuyenDi;
                parent.syncCheckboxChonTatCa(toaPanel, ghe.getKhoangTau().getToaTau(), chuyen);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // 4. XÓA PANEL NÀY KHỎI GIAO DIỆN
        Container container = getParent();
        if (container != null) {
            container.remove(ThongTinVe.this);
            int idx = container.getComponentZOrder(ThongTinVe.this);
            if (idx + 1 < container.getComponentCount() && 
                container.getComponent(idx + 1) instanceof Box.Filler) {
                container.remove(idx + 1);
            }
            container.revalidate();
            container.repaint();
        }
    }
});
    }
    
    public Ghe getGhe() {
        return this.ghe;
    }
    
    public ChuyenTau getChuyenTau() {
        return ct; // "chuyen" là biến trong constructor của mày
    }
    
    public ThongTinHanhKhach getThongTin() {
        ThongTinHanhKhach info = new ThongTinHanhKhach();
        info.setMaGhe(ghe.getMaGhe());
        info.setHoTen(txt_hoTenHK.getText());
        info.setCCCD(txt_cccdHK.getText());
        info.setNgaySinh(date_ngaySinhHK.getDate());
        info.setLoaiVe((String) cbo_loaiVe.getSelectedItem());
        return info;
    }
    
    private void xoaGheVaPanel() {
    // 1. XÓA KHỎI BUS
    ChonChoNgoi parent = timParentChonChoNgoi(this);
    if (parent != null) {
        parent.xoaGheDaChon(ghe); // Dùng method có sẵn
        parent.xoaPanelThongTinVe(ghe);

        // 2. CẬP NHẬT LẠI NÚT GHẾ (tìm và đổi màu về trắng)
        JPanel toaPanel = timToaPanelChuaGhe();
        if (toaPanel != null) {
            RoundedButton btnGhe = parent.findButtonBySoGhe(toaPanel, ghe.getSoGhe());
            if (btnGhe != null) {
                btnGhe.setBackground(Color.WHITE);
                // XÓA listener cũ, thêm lại listener chọn ghế
                for (java.awt.event.ActionListener al : btnGhe.getActionListeners()) {
                    btnGhe.removeActionListener(al);
                }
                btnGhe.addActionListener(e -> parent.handleChonGhe(btnGhe, ghe));
            }
            // Đồng bộ checkbox
            try {
                parent.syncCheckboxChonTatCa(toaPanel, ghe.getKhoangTau().getToaTau(),
                    parent.isGheThuocChuyenVe(ghe) ? parent.chuyenVe : parent.chuyenDi);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // 3. XÓA PANEL NÀY KHỎI GIAO DIỆN
    Container container = getParent();
        if (container != null) {
            container.remove(this);
            // Xóa filler nếu có
            int index = container.getComponentZOrder(this);
            if (index + 1 < container.getComponentCount() &&
                container.getComponent(index + 1) instanceof Box.Filler) {
                container.remove(index + 1);
            }
            container.revalidate();
            container.repaint();
        }
    }

    private JPanel timToaPanelChuaGhe() {
    ChonChoNgoi parent = timParentChonChoNgoi(this);
    if (parent == null) return null;

    // Duyệt tất cả panel toa trong jPanel8
    for (Component comp : parent.jPanel8.getComponents()) {
        if (!(comp instanceof JPanel)) continue;
        JPanel panel = (JPanel) comp;

        // Tìm nút có text = số ghế
        RoundedButton btn = parent.findButtonBySoGhe(panel, ghe.getSoGhe());
        if (btn != null && btn.isEnabled()) {
            return panel;
        }
    }
    return null;
}
    
    private ChonChoNgoi timParentChonChoNgoi(Component comp) {
    while (comp != null) {
        if (comp instanceof ChonChoNgoi) {
            return (ChonChoNgoi) comp;
        }
        comp = comp.getParent();
    }
    return null;
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_thongTinHK = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txt_hoTenHK = new javax.swing.JTextField();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jLabel9 = new javax.swing.JLabel();
        txt_cccdHK = new javax.swing.JTextField();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        jPanel17 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        date_ngaySinhHK = new com.toedter.calendar.JDateChooser();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jLabel1 = new javax.swing.JLabel();
        cbo_loaiVe = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        lbl_giaVe = new javax.swing.JLabel();
        btn_bin = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(2147483647, 200));
        setLayout(new java.awt.BorderLayout());

        pnl_thongTinHK.setBackground(new java.awt.Color(255, 255, 255));
        pnl_thongTinHK.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin hành hách", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10))); // NOI18N
        pnl_thongTinHK.setMaximumSize(new java.awt.Dimension(32767, 200));
        pnl_thongTinHK.setMinimumSize(new java.awt.Dimension(401, 200));
        pnl_thongTinHK.setPreferredSize(new java.awt.Dimension(411, 200));
        pnl_thongTinHK.setLayout(new javax.swing.BoxLayout(pnl_thongTinHK, javax.swing.BoxLayout.Y_AXIS));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel16.setLayout(new javax.swing.BoxLayout(jPanel16, javax.swing.BoxLayout.LINE_AXIS));

        jLabel5.setText("Họ tên:");
        jLabel5.setPreferredSize(new java.awt.Dimension(70, 16));
        jPanel16.add(jLabel5);

        txt_hoTenHK.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        jPanel16.add(txt_hoTenHK);
        jPanel16.add(filler3);

        jLabel9.setText("CCCD:");
        jLabel9.setPreferredSize(new java.awt.Dimension(70, 16));
        jPanel16.add(jLabel9);

        txt_cccdHK.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        jPanel16.add(txt_cccdHK);

        pnl_thongTinHK.add(jPanel16);
        pnl_thongTinHK.add(filler5);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel17.setLayout(new javax.swing.BoxLayout(jPanel17, javax.swing.BoxLayout.LINE_AXIS));

        jLabel10.setText("Ngày sinh:");
        jLabel10.setMaximumSize(new java.awt.Dimension(60, 16));
        jLabel10.setMinimumSize(new java.awt.Dimension(60, 16));
        jLabel10.setPreferredSize(new java.awt.Dimension(70, 16));
        jPanel17.add(jLabel10);

        date_ngaySinhHK.setEnabled(false);
        date_ngaySinhHK.setMaximumSize(txt_hoTenHK.getMaximumSize());
        date_ngaySinhHK.setPreferredSize(txt_hoTenHK.getPreferredSize());
        jPanel17.add(date_ngaySinhHK);
        jPanel17.add(filler1);

        jLabel1.setText("Loại vé:");
        jLabel1.setPreferredSize(new java.awt.Dimension(70, 16));
        jPanel17.add(jLabel1);

        cbo_loaiVe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Người lớn", "Trẻ em", "Sinh viên", "Người cao tuổi" }));
        cbo_loaiVe.setMaximumSize(txt_hoTenHK.getMaximumSize());
        cbo_loaiVe.setPreferredSize(txt_hoTenHK.getPreferredSize());
        cbo_loaiVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_loaiVeActionPerformed(evt);
            }
        });
        jPanel17.add(cbo_loaiVe);

        pnl_thongTinHK.add(jPanel17);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel4.setPreferredSize(new java.awt.Dimension(100, 50));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        lbl_giaVe.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_giaVe.setForeground(new java.awt.Color(255, 51, 0));
        lbl_giaVe.setText("Giá vé: ");
        lbl_giaVe.setMaximumSize(new java.awt.Dimension(600, 25));
        jPanel4.add(lbl_giaVe);

        btn_bin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_bin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/trash.png"))); // NOI18N
        btn_bin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_bin.setMaximumSize(new java.awt.Dimension(40, 50));
        jPanel4.add(btn_bin);

        pnl_thongTinHK.add(jPanel4);

        add(pnl_thongTinHK, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void cbo_loaiVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_loaiVeActionPerformed
        // TODO add your handling code here:
        String loaiVe = (String) cbo_loaiVe.getSelectedItem();

    // 1. BẬT/TẮT Ô NHẬP
    if ("Trẻ em".equals(loaiVe)) {
        date_ngaySinhHK.setEnabled(true);
        txt_cccdHK.setEnabled(false);
        txt_cccdHK.setText("");
    } else {
        date_ngaySinhHK.setEnabled(false);
        txt_cccdHK.setEnabled(true);
    }

    // 2. TÍNH GIÁ VÉ THEO LOẠI
    double giaGoc = ct.getTuyenDuong().tinhGiaVeCoBan() * ghe.getLoaiGhe().getHeSoLoaiGhe();
    double giam = 0;

    switch (loaiVe) {
        case "Trẻ em" -> giam = 0.5;        // 50%
        case "Sinh viên" -> giam = 0.2;     // 20%
        case "Người cao tuổi" -> giam = 0.3; // 30%
        default -> giam = 0;                // Người lớn
    }

    double giaThuc = giaGoc * (1 - giam);

    // 3. HIỂN THỊ GIÁ + GIẢM
    String text = "Giá vé: " + FormatUtil.formatCurrency(giaThuc);
    lbl_giaVe.setText(text);
    }//GEN-LAST:event_cbo_loaiVeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btn_bin;
    private javax.swing.JComboBox<String> cbo_loaiVe;
    private com.toedter.calendar.JDateChooser date_ngaySinhHK;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lbl_giaVe;
    private javax.swing.JPanel pnl_thongTinHK;
    private javax.swing.JTextField txt_cccdHK;
    private javax.swing.JTextField txt_hoTenHK;
    // End of variables declaration//GEN-END:variables
}
