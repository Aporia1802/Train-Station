/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.components;

import entity.ChuyenTau;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author CÔNG HOÀNG
 */
public class ThongTinChuyenTau extends javax.swing.JPanel {
    private final ChuyenTau chuyenTau;
    private boolean isSelected = false;
    private ActionListener selectionListener;
    /**
     * Creates new form ChonChuyenTau
     * @param chuyenTau
     */
    public ThongTinChuyenTau(ChuyenTau chuyenTau) {
        initComponents();
        this.chuyenTau = chuyenTau;
        init();
        setupClickListener();
    }
    
    private void init() {
        String tenTau = chuyenTau.getTau().getTenTau();
        lbl_soHieuTau.setText(tenTau.substring(tenTau.lastIndexOf(" ") + 1));
        lbl_soChoTrong.setText(String.valueOf(chuyenTau.getSoGheConTrong()));
        lbl_thoiGianDi.setText(chuyenTau.getThoiGianDi().toLocalTime().toString());
        lbl_thoiGianDen.setText(chuyenTau.getThoiGianDen().toLocalTime().toString());
        lbl_ngayDi.setText(chuyenTau.getThoiGianDi().toLocalDate().toString());
        lbl_ngayDen.setText(chuyenTau.getThoiGianDen().toLocalDate().toString());
        lbl_thoiGianDiChuyen.setText(String.valueOf(chuyenTau.getTuyenDuong().getThoiGianDiChuyen()) + " tiếng");
    }
    
    /**
     * Thiết lập listener khi click vào panel
     */
    private void setupClickListener() {
        pnl_contain.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // Thêm MouseListener cho panel chính
        pnl_contain.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (selectionListener != null) {
                    selectionListener.actionPerformed(
                        new java.awt.event.ActionEvent(ThongTinChuyenTau.this, 
                                                       ActionEvent.ACTION_PERFORMED, 
                                                       "SELECT")
                    );
                }
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!isSelected) {
                    pnl_contain.setBackground(new java.awt.Color(245, 245, 245));
                    pnl_header.setBackground(new java.awt.Color(245, 245, 245));
                    pnl_center.setBackground(new java.awt.Color(245, 245, 245));
                    separate.setBackground(new java.awt.Color(245, 245, 245));
                    pnl_thongTinDen.setBackground(new java.awt.Color(245, 245, 245));
                    pnl_thongTinDi.setBackground(new java.awt.Color(245, 245, 245));
                    pnl_thoiGianDiChuyen.setBackground(new java.awt.Color(245, 245, 245));
                }
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isSelected) {
                    pnl_contain.setBackground(java.awt.Color.WHITE);
                    pnl_header.setBackground(java.awt.Color.WHITE);
                    pnl_center.setBackground(java.awt.Color.WHITE);
                    separate.setBackground(java.awt.Color.WHITE);
                    pnl_thongTinDen.setBackground(java.awt.Color.WHITE);
                    pnl_thongTinDi.setBackground(java.awt.Color.WHITE);
                    pnl_thoiGianDiChuyen.setBackground(java.awt.Color.WHITE);
                }
            }
        });
    }
    
    /**
     * Đặt listener khi chọn chuyến tàu
     */
    public void setSelectionListener(ActionListener listener) {
        this.selectionListener = listener;
    }
    
    /**
     * Đánh dấu chuyến tàu được chọn
     */
    public void setSelected(boolean selected) {
        this.isSelected = selected;
        if (selected) {
            pnl_contain.setBackground(new Color(235, 250, 240));
            pnl_header.setBackground(new Color(235, 250, 240));
            pnl_center.setBackground(new Color(235, 250, 240));
            separate.setBackground(new Color(235, 250, 240));
            pnl_thongTinDen.setBackground(new Color(235, 250, 240));
            pnl_thongTinDi.setBackground(new Color(235, 250, 240));
            pnl_thoiGianDiChuyen.setBackground(new Color(235, 250, 240));
        } else {
            pnl_contain.setBackground(java.awt.Color.WHITE);
            pnl_header.setBackground(java.awt.Color.WHITE);
            pnl_center.setBackground(java.awt.Color.WHITE);
            separate.setBackground(java.awt.Color.WHITE);
            pnl_thongTinDen.setBackground(java.awt.Color.WHITE);
            pnl_thongTinDi.setBackground(java.awt.Color.WHITE);
            pnl_thoiGianDiChuyen.setBackground(java.awt.Color.WHITE);
        }
    }
    
    public ChuyenTau getChuyenTau() {
        return chuyenTau;
    }
    
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_contain = new gui.custom.PanelShadow();
        pnl_header = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbl_soHieuTau = new javax.swing.JLabel();
        lbl_soChoTrong = new javax.swing.JLabel();
        pnl_center = new javax.swing.JPanel();
        separate = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        pnl_thongTin = new javax.swing.JPanel();
        pnl_thongTinDi = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lbl_thoiGianDi = new javax.swing.JLabel();
        lbl_ngayDi = new javax.swing.JLabel();
        pnl_thoiGianDiChuyen = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        lbl_thoiGianDiChuyen = new gui.custom.RoundedTextField();
        pnl_thongTinDen = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lbl_thoiGianDen = new javax.swing.JLabel();
        lbl_ngayDen = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(320, 200));
        setMinimumSize(new java.awt.Dimension(320, 200));
        setPreferredSize(new java.awt.Dimension(320, 200));
        setLayout(new java.awt.BorderLayout());

        pnl_contain.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnl_contain.setMaximumSize(new java.awt.Dimension(320, 200));
        pnl_contain.setMinimumSize(new java.awt.Dimension(320, 200));
        pnl_contain.setPreferredSize(new java.awt.Dimension(320, 200));
        pnl_contain.setLayout(new java.awt.BorderLayout());

        pnl_header.setPreferredSize(new java.awt.Dimension(100, 60));
        pnl_header.setLayout(new java.awt.GridLayout(2, 2));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Số hiệu tàu");
        pnl_header.add(jLabel1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Số lượng chỗ trống");
        pnl_header.add(jLabel2);

        lbl_soHieuTau.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbl_soHieuTau.setForeground(new java.awt.Color(123, 17, 19));
        lbl_soHieuTau.setText("SE1");
        pnl_header.add(lbl_soHieuTau);

        lbl_soChoTrong.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbl_soChoTrong.setForeground(new java.awt.Color(123, 17, 19));
        lbl_soChoTrong.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_soChoTrong.setText("100");
        pnl_header.add(lbl_soChoTrong);

        pnl_contain.add(pnl_header, java.awt.BorderLayout.PAGE_START);

        pnl_center.setBackground(new java.awt.Color(255, 255, 255));
        pnl_center.setLayout(new java.awt.BorderLayout());

        separate.setLayout(new javax.swing.BoxLayout(separate, javax.swing.BoxLayout.LINE_AXIS));

        jLabel8.setText("--------------------------------------------------------------------------------------------------------------------------------------------------");
        separate.add(jLabel8);

        pnl_center.add(separate, java.awt.BorderLayout.PAGE_START);

        pnl_thongTin.setLayout(new java.awt.GridLayout(1, 3));

        pnl_thongTinDi.setLayout(new java.awt.GridLayout(3, 1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Thời gian đi");
        pnl_thongTinDi.add(jLabel5);

        lbl_thoiGianDi.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_thoiGianDi.setForeground(new java.awt.Color(123, 17, 19));
        lbl_thoiGianDi.setText("06:00");
        pnl_thongTinDi.add(lbl_thoiGianDi);

        lbl_ngayDi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_ngayDi.setText("01/11/2025");
        pnl_thongTinDi.add(lbl_ngayDi);

        pnl_thongTin.add(pnl_thongTinDi);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/train.png"))); // NOI18N
        jLabel14.setPreferredSize(new java.awt.Dimension(200, 40));
        pnl_thoiGianDiChuyen.add(jLabel14);

        lbl_thoiGianDiChuyen.setEditable(false);
        lbl_thoiGianDiChuyen.setBackground(new java.awt.Color(76, 175, 80));
        lbl_thoiGianDiChuyen.setForeground(new java.awt.Color(255, 255, 255));
        lbl_thoiGianDiChuyen.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        lbl_thoiGianDiChuyen.setText("30 tiếng");
        lbl_thoiGianDiChuyen.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        lbl_thoiGianDiChuyen.setEnabled(false);
        lbl_thoiGianDiChuyen.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbl_thoiGianDiChuyen.setPreferredSize(new java.awt.Dimension(90, 42));
        lbl_thoiGianDiChuyen.setRound(15);
        pnl_thoiGianDiChuyen.add(lbl_thoiGianDiChuyen);

        pnl_thongTin.add(pnl_thoiGianDiChuyen);

        pnl_thongTinDen.setLayout(new java.awt.GridLayout(3, 1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Thời gian đến");
        pnl_thongTinDen.add(jLabel9);

        lbl_thoiGianDen.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_thoiGianDen.setForeground(new java.awt.Color(123, 17, 19));
        lbl_thoiGianDen.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_thoiGianDen.setText("06:00");
        pnl_thongTinDen.add(lbl_thoiGianDen);

        lbl_ngayDen.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_ngayDen.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_ngayDen.setText("01/11/2025");
        pnl_thongTinDen.add(lbl_ngayDen);

        pnl_thongTin.add(pnl_thongTinDen);

        pnl_center.add(pnl_thongTin, java.awt.BorderLayout.CENTER);

        pnl_contain.add(pnl_center, java.awt.BorderLayout.CENTER);

        add(pnl_contain, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lbl_ngayDen;
    private javax.swing.JLabel lbl_ngayDi;
    private javax.swing.JLabel lbl_soChoTrong;
    private javax.swing.JLabel lbl_soHieuTau;
    private javax.swing.JLabel lbl_thoiGianDen;
    private javax.swing.JLabel lbl_thoiGianDi;
    private gui.custom.RoundedTextField lbl_thoiGianDiChuyen;
    private javax.swing.JPanel pnl_center;
    private gui.custom.PanelShadow pnl_contain;
    private javax.swing.JPanel pnl_header;
    private javax.swing.JPanel pnl_thoiGianDiChuyen;
    private javax.swing.JPanel pnl_thongTin;
    private javax.swing.JPanel pnl_thongTinDen;
    private javax.swing.JPanel pnl_thongTinDi;
    private javax.swing.JPanel separate;
    // End of variables declaration//GEN-END:variables
}
