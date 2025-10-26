/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.quanLy;

import bus.QuanLyGaTau_BUS;
import entity.GaTau;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PC
 */
public class QuanLyGaTau_GUI extends javax.swing.JPanel {
    private QuanLyGaTau_BUS bus;
    private DefaultTableModel tblModel_thongTinGa;
    /**
     * Creates new form QuanLiGaTau2_GUI
     */
    public QuanLyGaTau_GUI() {
        initComponents();
        init();
    }
    
    private void init() {
        bus = new QuanLyGaTau_BUS();
        
//      setModel
        tblModel_thongTinGa = new DefaultTableModel(new String[] {"Mã ga", "Tên ga", "Địa chỉ", "Số điện thoại"}, 0);
        tbl_thongTinGa.setModel(tblModel_thongTinGa);
        
        getTableData(bus.getAllGaTau());
    }
    
    private void getTableData(ArrayList<GaTau> dsGaTau) {
        tblModel_thongTinGa.setRowCount(0);
        for(GaTau gaTau : dsGaTau) {
            String[] newRow = {gaTau.getMaGa(), gaTau.getTenGa(), gaTau.getDiaChi(), gaTau.getSoDienThoai()};
            tblModel_thongTinGa.addRow(newRow);
        }
    }
    
    private void getThongTinGa() {
        int row = tbl_thongTinGa.getSelectedRow(); // lấy dòng được chọn
        if (row != -1) {
            // Lấy dữ liệu từng cột trong dòng đó
            String maGa = tbl_thongTinGa.getValueAt(row, 0).toString();
            String tenGa = tbl_thongTinGa.getValueAt(row, 1).toString();
            String diaChi = tbl_thongTinGa.getValueAt(row, 2).toString();
            String sdt = tbl_thongTinGa.getValueAt(row, 3).toString();

            // Đổ lên textfield
            txt_maGa.setText(maGa);
            txt_tenGa.setText(tenGa);
            txt_diaChi.setText(diaChi);
            txt_soDienThoai.setText(sdt);
        }
    }
    
    private void handleActionXoaTrang() {
        tbl_thongTinGa.clearSelection();
        txt_maGa.setText("");
        txt_tenGa.setText("");
        txt_diaChi.setText("");
        txt_soDienThoai.setText("");
    }
    
    private void handleActionLamMoi() {
    
    tbl_thongTinGa.clearSelection();
    txt_maGa.setText("");
    txt_tenGa.setText("");
    txt_diaChi.setText("");
    txt_soDienThoai.setText("");
    txt_timKiem.setText("Nhập mã hoặc tên ga cần tìm...");
    txt_timKiem.setForeground(Color.GRAY);
    ArrayList<GaTau> dsGaTau = bus.getAllGaTau();
    getTableData(dsGaTau);
    
    }
    
    private void handleActionTimKiem() {
        String keyword = txt_timKiem.getText().trim();
        getTableData(bus.getGaTauByKeyword(keyword));
    }
    private void handleActionThem() {
    try {
       
        String tenGa = txt_tenGa.getText().trim();
        String diaChi = txt_diaChi.getText().trim();
        String soDienThoai = txt_soDienThoai.getText().trim();
        if (tenGa.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập tên ga!");
            txt_tenGa.requestFocus();
            return;
        }
          String maGa = bus.generateMaGa(tenGa);
        if (diaChi.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ!");
            txt_diaChi.requestFocus();
            return;
        }
        if (soDienThoai.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!");
            txt_soDienThoai.requestFocus();
            return;
        }
        GaTau gaTau = new GaTau(maGa, tenGa, diaChi, soDienThoai);
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
            "Thêm ga mới?\n" +
            "Mã: " + maGa + "\n" +
            "Tên: " + tenGa + "\n" +
            "Địa chỉ: " + diaChi + "\n" +
            "SĐT: " + soDienThoai,
            "Xác nhận",
            javax.swing.JOptionPane.YES_NO_OPTION);
        
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            if (bus.createGaTau(gaTau)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Thêm thành công!\nMã: " + maGa);
                handleActionLamMoi();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        }
        
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        e.printStackTrace();
    }
}
private void handleActionCapNhat() {
    try {
        int row = tbl_thongTinGa.getSelectedRow();
        if (row == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn ga cần cập nhật!");
            return;
        }
        
        String maGa = txt_maGa.getText().trim();
        String tenGa = txt_tenGa.getText().trim();
        String diaChi = txt_diaChi.getText().trim();
        String soDienThoai = txt_soDienThoai.getText().trim();
        
        // Validate
        if (maGa.isEmpty() || tenGa.isEmpty() || diaChi.isEmpty() || soDienThoai.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
            return;
        }
        
        // Tạo đối tượng GaTau mới
        GaTau gaTau = new GaTau(maGa, tenGa, diaChi, soDienThoai);
        
        // Xác nhận
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
            "Xác nhận cập nhật ga " + maGa + "?",
            "Xác nhận",
            javax.swing.JOptionPane.YES_NO_OPTION);
        
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            if (bus.updateGaTau(maGa, gaTau)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                handleActionLamMoi();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        }
        
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        e.printStackTrace();
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

        pnl_center = new javax.swing.JPanel();
        spl_container = new javax.swing.JSplitPane();
        pnl_container = new javax.swing.JPanel();
        pnl_thongTinNhanVien = new javax.swing.JPanel();
        pnl_MaNV = new javax.swing.JPanel();
        lbl_maNV = new javax.swing.JLabel();
        txt_maGa = new javax.swing.JTextField();
        pnl_hoTen1 = new javax.swing.JPanel();
        lbl_hoTen1 = new javax.swing.JLabel();
        txt_tenGa = new javax.swing.JTextField();
        pnl_ngaySinh = new javax.swing.JPanel();
        pnl_chucVu1 = new javax.swing.JPanel();
        pnl_hoTen = new javax.swing.JPanel();
        lbl_hoTen = new javax.swing.JLabel();
        txt_diaChi = new javax.swing.JTextField();
        pnl_diaChi = new javax.swing.JPanel();
        lbl_diaChi = new javax.swing.JLabel();
        txt_soDienThoai = new javax.swing.JTextField();
        pnl_btnGroup = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btn_xoaTrang = new javax.swing.JButton();
        btn_capNhat = new javax.swing.JButton();
        btn_themGa = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_thongTinGa = new javax.swing.JTable();
        pnl_top = new javax.swing.JPanel();
        pnl_timKiem = new javax.swing.JPanel();
        txt_timKiem = new javax.swing.JTextField();
        btn_timKiem = new javax.swing.JButton();
        btn_lamMoi = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        pnl_center.setLayout(new javax.swing.BoxLayout(pnl_center, javax.swing.BoxLayout.LINE_AXIS));

        spl_container.setResizeWeight(0.8);
        spl_container.setMinimumSize(new java.awt.Dimension(805, 416));
        spl_container.setPreferredSize(new java.awt.Dimension(1055, 718));

        pnl_container.setBackground(new java.awt.Color(255, 255, 255));
        pnl_container.setMinimumSize(new java.awt.Dimension(400, 379));
        pnl_container.setPreferredSize(new java.awt.Dimension(250, 100));
        pnl_container.setLayout(new java.awt.BorderLayout());

        pnl_thongTinNhanVien.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin ga tàu"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnl_thongTinNhanVien.setPreferredSize(new java.awt.Dimension(250, 223));
        pnl_thongTinNhanVien.setLayout(new javax.swing.BoxLayout(pnl_thongTinNhanVien, javax.swing.BoxLayout.Y_AXIS));

        pnl_MaNV.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        pnl_MaNV.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_MaNV.setPreferredSize(new java.awt.Dimension(230, 50));
        pnl_MaNV.setLayout(new javax.swing.BoxLayout(pnl_MaNV, javax.swing.BoxLayout.X_AXIS));

        lbl_maNV.setText("Mã ga :");
        lbl_maNV.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_maNV.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_maNV.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_MaNV.add(lbl_maNV);

        txt_maGa.setEnabled(false);
        txt_maGa.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_maGa.setPreferredSize(new java.awt.Dimension(64, 40));
        pnl_MaNV.add(txt_maGa);

        pnl_thongTinNhanVien.add(pnl_MaNV);

        pnl_hoTen1.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        pnl_hoTen1.setPreferredSize(new java.awt.Dimension(164, 50));
        pnl_hoTen1.setLayout(new javax.swing.BoxLayout(pnl_hoTen1, javax.swing.BoxLayout.LINE_AXIS));

        lbl_hoTen1.setText("Tên ga :");
        lbl_hoTen1.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_hoTen1.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_hoTen1.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_hoTen1.add(lbl_hoTen1);

        txt_tenGa.setMaximumSize(txt_maGa.getMaximumSize());
        pnl_hoTen1.add(txt_tenGa);

        pnl_thongTinNhanVien.add(pnl_hoTen1);

        pnl_ngaySinh.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_ngaySinh.setLayout(new javax.swing.BoxLayout(pnl_ngaySinh, javax.swing.BoxLayout.LINE_AXIS));
        pnl_thongTinNhanVien.add(pnl_ngaySinh);

        pnl_chucVu1.setMaximumSize(new java.awt.Dimension(32817, 50));
        pnl_chucVu1.setPreferredSize(new java.awt.Dimension(230, 50));
        pnl_chucVu1.setLayout(new javax.swing.BoxLayout(pnl_chucVu1, javax.swing.BoxLayout.LINE_AXIS));

        pnl_hoTen.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        pnl_hoTen.setPreferredSize(new java.awt.Dimension(164, 50));
        pnl_hoTen.setLayout(new javax.swing.BoxLayout(pnl_hoTen, javax.swing.BoxLayout.LINE_AXIS));

        lbl_hoTen.setText("Địa chỉ :");
        lbl_hoTen.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_hoTen.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_hoTen.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_hoTen.add(lbl_hoTen);

        txt_diaChi.setMaximumSize(txt_maGa.getMaximumSize());
        pnl_hoTen.add(txt_diaChi);

        pnl_chucVu1.add(pnl_hoTen);

        pnl_thongTinNhanVien.add(pnl_chucVu1);

        pnl_diaChi.setMaximumSize(new java.awt.Dimension(1147483692, 50));
        pnl_diaChi.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_diaChi.setPreferredSize(new java.awt.Dimension(164, 50));
        pnl_diaChi.setLayout(new javax.swing.BoxLayout(pnl_diaChi, javax.swing.BoxLayout.LINE_AXIS));

        lbl_diaChi.setText("Số điện thoại");
        lbl_diaChi.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_diaChi.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_diaChi.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_diaChi.add(lbl_diaChi);

        txt_soDienThoai.setMaximumSize(txt_maGa.getMaximumSize());
        txt_soDienThoai.setMinimumSize(new java.awt.Dimension(64, 30));
        pnl_diaChi.add(txt_soDienThoai);

        pnl_thongTinNhanVien.add(pnl_diaChi);

        pnl_container.add(pnl_thongTinNhanVien, java.awt.BorderLayout.CENTER);

        pnl_btnGroup.setMaximumSize(new java.awt.Dimension(260, 100));
        pnl_btnGroup.setMinimumSize(new java.awt.Dimension(220, 100));
        pnl_btnGroup.setLayout(new java.awt.BorderLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 0));
        jPanel5.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        btn_xoaTrang.setText("Xóa trắng");
        btn_xoaTrang.setMaximumSize(new java.awt.Dimension(120, 50));
        btn_xoaTrang.setPreferredSize(new java.awt.Dimension(120, 50));
        btn_xoaTrang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaTrangActionPerformed(evt);
            }
        });
        jPanel5.add(btn_xoaTrang);

        btn_capNhat.setText("Cập nhật");
        btn_capNhat.setMaximumSize(new java.awt.Dimension(120, 50));
        btn_capNhat.setPreferredSize(new java.awt.Dimension(120, 50));
        btn_capNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhatActionPerformed(evt);
            }
        });
        jPanel5.add(btn_capNhat);

        pnl_btnGroup.add(jPanel5, java.awt.BorderLayout.CENTER);

        btn_themGa.setText("Thêm ga mới");
        btn_themGa.setMaximumSize(new java.awt.Dimension(139, 23));
        btn_themGa.setMinimumSize(new java.awt.Dimension(139, 23));
        btn_themGa.setPreferredSize(new java.awt.Dimension(72, 50));
        btn_themGa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themGaActionPerformed(evt);
            }
        });
        pnl_btnGroup.add(btn_themGa, java.awt.BorderLayout.PAGE_END);

        pnl_container.add(pnl_btnGroup, java.awt.BorderLayout.PAGE_END);

        spl_container.setRightComponent(pnl_container);

        tbl_thongTinGa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã ga", "Tên ga", "Địa chỉ", "Số điện thoại"
            }
        ));
        tbl_thongTinGa.setShowGrid(true);
        tbl_thongTinGa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_thongTinGaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_thongTinGa);

        spl_container.setLeftComponent(jScrollPane1);

        pnl_center.add(spl_container);

        add(pnl_center, java.awt.BorderLayout.CENTER);

        pnl_top.setBackground(new java.awt.Color(255, 255, 255));
        pnl_top.setMinimumSize(new java.awt.Dimension(20, 20));
        pnl_top.setPreferredSize(new java.awt.Dimension(1366, 50));
        pnl_top.setLayout(new javax.swing.BoxLayout(pnl_top, javax.swing.BoxLayout.LINE_AXIS));

        pnl_timKiem.setLayout(new javax.swing.BoxLayout(pnl_timKiem, javax.swing.BoxLayout.LINE_AXIS));

        txt_timKiem.setForeground(new java.awt.Color(153, 153, 153));
        txt_timKiem.setText("Nhập mã hoặc tên ga cần tìm...");
        txt_timKiem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_timKiemFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_timKiemFocusLost(evt);
            }
        });
        pnl_timKiem.add(txt_timKiem);

        btn_timKiem.setText("Tìm kiếm");
        btn_timKiem.setMaximumSize(new java.awt.Dimension(79, 55));
        btn_timKiem.setPreferredSize(new java.awt.Dimension(100, 23));
        btn_timKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_timKiemActionPerformed(evt);
            }
        });
        pnl_timKiem.add(btn_timKiem);

        btn_lamMoi.setText("Làm mới");
        btn_lamMoi.setMaximumSize(new java.awt.Dimension(77, 55));
        btn_lamMoi.setPreferredSize(new java.awt.Dimension(100, 23));
        btn_lamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_lamMoiActionPerformed(evt);
            }
        });
        pnl_timKiem.add(btn_lamMoi);

        pnl_top.add(pnl_timKiem);

        add(pnl_top, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents
    
    private void btn_themGaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themGaActionPerformed
        // TODO add your handling code here:han
        handleActionThem();
    }//GEN-LAST:event_btn_themGaActionPerformed

    private void btn_capNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhatActionPerformed
        // TODO add your handling code here:
        handleActionCapNhat();
    }//GEN-LAST:event_btn_capNhatActionPerformed

    private void btn_xoaTrangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaTrangActionPerformed
        // TODO add your handling code here:
        handleActionXoaTrang();
    }//GEN-LAST:event_btn_xoaTrangActionPerformed

    private void btn_timKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_timKiemActionPerformed
        // TODO add your handling code here:
        handleActionTimKiem();
    }//GEN-LAST:event_btn_timKiemActionPerformed

    private void btn_lamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_lamMoiActionPerformed
        // TODO add your handling code here:
        handleActionLamMoi();
    }//GEN-LAST:event_btn_lamMoiActionPerformed

    private void tbl_thongTinGaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_thongTinGaMouseClicked
        // TODO add your handling code here:
        getThongTinGa();
    }//GEN-LAST:event_tbl_thongTinGaMouseClicked

    private void txt_timKiemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_timKiemFocusGained
        // TODO add your handling code here:
        if (txt_timKiem.getText().equals("Nhập mã hoặc tên ga cần tìm...")) {
            txt_timKiem.setText("");
            txt_timKiem.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txt_timKiemFocusGained

    private void txt_timKiemFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_timKiemFocusLost
        // TODO add your handling code here:
        if (txt_timKiem.getText().equals("")) {
            txt_timKiem.setText("Nhập mã hoặc tên ga cần tìm...");
            txt_timKiem.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_txt_timKiemFocusLost

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_capNhat;
    private javax.swing.JButton btn_lamMoi;
    private javax.swing.JButton btn_themGa;
    private javax.swing.JButton btn_timKiem;
    private javax.swing.JButton btn_xoaTrang;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_diaChi;
    private javax.swing.JLabel lbl_hoTen;
    private javax.swing.JLabel lbl_hoTen1;
    private javax.swing.JLabel lbl_maNV;
    private javax.swing.JPanel pnl_MaNV;
    private javax.swing.JPanel pnl_btnGroup;
    private javax.swing.JPanel pnl_center;
    private javax.swing.JPanel pnl_chucVu1;
    private javax.swing.JPanel pnl_container;
    private javax.swing.JPanel pnl_diaChi;
    private javax.swing.JPanel pnl_hoTen;
    private javax.swing.JPanel pnl_hoTen1;
    private javax.swing.JPanel pnl_ngaySinh;
    private javax.swing.JPanel pnl_thongTinNhanVien;
    private javax.swing.JPanel pnl_timKiem;
    private javax.swing.JPanel pnl_top;
    private javax.swing.JSplitPane spl_container;
    private javax.swing.JTable tbl_thongTinGa;
    private javax.swing.JTextField txt_diaChi;
    private javax.swing.JTextField txt_maGa;
    private javax.swing.JTextField txt_soDienThoai;
    private javax.swing.JTextField txt_tenGa;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
