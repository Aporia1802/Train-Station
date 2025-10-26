/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.traCuu;


import bus.TraCuuVe_BUS;
import entity.Ve;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
/**
 *
 * @author PC
 */
public class TraCuuVe_GUI extends javax.swing.JPanel {
  private DefaultTableModel tblModel;
    private TraCuuVe_BUS bus;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public TraCuuVe_GUI() {
        initComponents();
        init();
    }
    
    private void init() {
        bus = new TraCuuVe_BUS();
        
        String[] columns = {"Mã vé", "Tàu", "Ga đi", "Ga đến", "Ngày đi", "Ngày đến", 
                           "Hành khách", "Số ghế", "Trạng thái", "Giá vé"};
        tblModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbl_thongTinVe.setModel(tblModel);
        tbl_thongTinVe.setRowHeight(30);
        
        javax.swing.table.DefaultTableCellRenderer centerRenderer = 
            new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        
        for (int i = 0; i < tbl_thongTinVe.getColumnCount(); i++) {
            if (i != 6) {
                tbl_thongTinVe.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
         try {
            tbl_thongTinVe.getColumnModel().getColumn(0).setPreferredWidth(100);
            tbl_thongTinVe.getColumnModel().getColumn(1).setPreferredWidth(80);
            tbl_thongTinVe.getColumnModel().getColumn(2).setPreferredWidth(120);
            tbl_thongTinVe.getColumnModel().getColumn(3).setPreferredWidth(120);
            tbl_thongTinVe.getColumnModel().getColumn(4).setPreferredWidth(130);
            tbl_thongTinVe.getColumnModel().getColumn(5).setPreferredWidth(130);
            tbl_thongTinVe.getColumnModel().getColumn(6).setPreferredWidth(150);
            tbl_thongTinVe.getColumnModel().getColumn(7).setPreferredWidth(80);
            tbl_thongTinVe.getColumnModel().getColumn(8).setPreferredWidth(100);
            tbl_thongTinVe.getColumnModel().getColumn(9).setPreferredWidth(120);
        } catch (Exception e) {
            System.err.println("️ Không thể set column width: " + e.getMessage());
        }
            date_ngayDi.setDateFormatString("dd/MM/yyyy");
         
    }
    
    private void loadTableData(ArrayList<Ve> dsVe) {
        tblModel.setRowCount(0);
        
        for (Ve ve : dsVe) {
            try {
                String[] row = {
                    ve.getMaVe() != null ? ve.getMaVe() : "",
                    ve.getChuyenTau() != null && ve.getChuyenTau().getTau() != null ? 
                        ve.getChuyenTau().getTau().getTenTau() : "",
                    ve.getChuyenTau() != null && ve.getChuyenTau().getTuyenDuong() != null && 
                        ve.getChuyenTau().getTuyenDuong().getGaDi() != null ?
                        ve.getChuyenTau().getTuyenDuong().getGaDi().getTenGa() : "",
                    ve.getChuyenTau() != null && ve.getChuyenTau().getTuyenDuong() != null && 
                        ve.getChuyenTau().getTuyenDuong().getGaDen() != null ?
                        ve.getChuyenTau().getTuyenDuong().getGaDen().getTenGa() : "",
                    ve.getChuyenTau() != null && ve.getChuyenTau().getThoiGianDi() != null ?
                        ve.getChuyenTau().getThoiGianDi().format(dateTimeFormatter) : "",
                    ve.getChuyenTau() != null && ve.getChuyenTau().getThoiGianDen() != null ?
                        ve.getChuyenTau().getThoiGianDen().format(dateTimeFormatter) : "",
                    ve.getHanhKhach() != null ? ve.getHanhKhach().getTenHanhKhach() : "",
                    ve.getGhe() != null ? String.valueOf(ve.getGhe().getSoGhe()) : "",
                    ve.getTrangThai() != null ? ve.getTrangThai().toString() : "",
                    String.format("%,.0f đ", ve.getGiaVe())
                };
                tblModel.addRow(row);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    private void handleTimKiem() {
        try {
            String maVe = txt_maVe.getText().trim();
            String hoTen = txt_hoTen.getText().trim();
            String cccd = txt_cccd.getText().trim();
            LocalDate ngayDi = null;
            Date selectedDate = date_ngayDi.getDate();
            if (selectedDate != null) {
                ngayDi = selectedDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            }
            
            if (maVe.isEmpty() && hoTen.isEmpty() && cccd.isEmpty() && ngayDi == null) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập ít nhất 1 điều kiện tìm kiếm!",
                    "Thiếu thông tin",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                txt_maVe.requestFocus();
                return;
            }
            
            if (!cccd.isEmpty() && !cccd.matches("^\\d{12}$")) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "CCCD phải có đúng 12 số!\nVí dụ: 079095123456",
                    "Lỗi định dạng",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                txt_cccd.requestFocus();
                txt_cccd.selectAll();
                return;
            }
                      ArrayList<Ve> dsVe = bus.searchVe(maVe, hoTen, cccd, ngayDi);
            
            if (dsVe.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Không tìm thấy vé nào phù hợp với điều kiện tìm kiếm!",
                    "Không có kết quả",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
                tblModel.setRowCount(0);
            } else {
                loadTableData(dsVe);
                javax.swing.JOptionPane.showMessageDialog(this,
                    " Tìm thấy " + dsVe.size() + " vé!",
                    "Kết quả tìm kiếm",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                " Lỗi: " + e.getMessage(),
                "Lỗi hệ thống",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void handleXoaTrang() {
        txt_maVe.setText("");
        txt_hoTen.setText("");
        txt_cccd.setText("");
        
        try {
            date_ngayDi.setDate(null);
        } catch (Exception e) {
         
        }
        
        tblModel.setRowCount(0);
        txt_maVe.requestFocus();
        
        System.out.println(" Đã xóa trắng form");
    
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_header = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        pnl_ga = new javax.swing.JPanel();
        lbl_maVe = new javax.swing.JLabel();
        txt_maVe = new javax.swing.JTextField();
        lbl_next2 = new javax.swing.JLabel();
        lbl_hoTen = new javax.swing.JLabel();
        txt_hoTen = new javax.swing.JTextField();
        pnl_ngay = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txt_cccd = new javax.swing.JTextField();
        lbl_next1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        date_ngayDi = new com.toedter.calendar.JDateChooser();
        pnl_timKiem1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnTimKiem = new javax.swing.JButton();
        btn_xoaTrang = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_thongTinVe = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        pnl_header.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_header.setPreferredSize(new java.awt.Dimension(1366, 250));
        pnl_header.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin vé"), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        pnl_ga.setMaximumSize(new java.awt.Dimension(65736, 70));
        pnl_ga.setMinimumSize(new java.awt.Dimension(1000, 25));
        pnl_ga.setPreferredSize(new java.awt.Dimension(100, 70));
        pnl_ga.setLayout(new javax.swing.BoxLayout(pnl_ga, javax.swing.BoxLayout.LINE_AXIS));

        lbl_maVe.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_maVe.setText("Mã vé:");
        lbl_maVe.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_ga.add(lbl_maVe);

        txt_maVe.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        pnl_ga.add(txt_maVe);

        lbl_next2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_next2.setPreferredSize(new java.awt.Dimension(70, 16));
        pnl_ga.add(lbl_next2);

        lbl_hoTen.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbl_hoTen.setText("Họ tên:");
        lbl_hoTen.setPreferredSize(new java.awt.Dimension(100, 25));
        pnl_ga.add(lbl_hoTen);

        txt_hoTen.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        pnl_ga.add(txt_hoTen);

        jPanel2.add(pnl_ga);

        pnl_ngay.setMaximumSize(new java.awt.Dimension(65679, 70));
        pnl_ngay.setMinimumSize(new java.awt.Dimension(1000, 25));
        pnl_ngay.setLayout(new javax.swing.BoxLayout(pnl_ngay, javax.swing.BoxLayout.LINE_AXIS));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("CCCD:");
        jLabel4.setPreferredSize(new java.awt.Dimension(100, 25));
        pnl_ngay.add(jLabel4);

        txt_cccd.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        pnl_ngay.add(txt_cccd);

        lbl_next1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_next1.setPreferredSize(new java.awt.Dimension(70, 16));
        pnl_ngay.add(lbl_next1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Ngày đi: ");
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 20));
        pnl_ngay.add(jLabel2);

        date_ngayDi.setMaximumSize(txt_hoTen.getMaximumSize());
        date_ngayDi.setPreferredSize(txt_hoTen.getPreferredSize());
        pnl_ngay.add(date_ngayDi);

        jPanel2.add(pnl_ngay);

        pnl_timKiem1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 1, 1, 1));
        pnl_timKiem1.setMaximumSize(new java.awt.Dimension(32767, 60));
        pnl_timKiem1.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnTimKiem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.setPreferredSize(new java.awt.Dimension(100, 40));
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });
        jPanel3.add(btnTimKiem);

        btn_xoaTrang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_xoaTrang.setText("Xóa trắng");
        btn_xoaTrang.setPreferredSize(new java.awt.Dimension(100, 40));
        btn_xoaTrang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaTrangActionPerformed(evt);
            }
        });
        jPanel3.add(btn_xoaTrang);

        pnl_timKiem1.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.add(pnl_timKiem1);

        pnl_header.add(jPanel2, java.awt.BorderLayout.CENTER);

        add(pnl_header, java.awt.BorderLayout.PAGE_START);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel1.setLayout(new java.awt.BorderLayout());

        tbl_thongTinVe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã vé", "Tàu", "Ga đi", "Ga đến", "Ngày đi", "Ngày đến", "Hành khách", "Số ghế", "Trạng thái", "Giá vé"
            }
        ));
        jScrollPane1.setViewportView(tbl_thongTinVe);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_xoaTrangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaTrangActionPerformed
        // TODO add your handling code here:
        handleXoaTrang();
    }//GEN-LAST:event_btn_xoaTrangActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        handleTimKiem();
    }//GEN-LAST:event_btnTimKiemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btn_xoaTrang;
    private com.toedter.calendar.JDateChooser date_ngayDi;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_hoTen;
    private javax.swing.JLabel lbl_maVe;
    private javax.swing.JLabel lbl_next1;
    private javax.swing.JLabel lbl_next2;
    private javax.swing.JPanel pnl_ga;
    private javax.swing.JPanel pnl_header;
    private javax.swing.JPanel pnl_ngay;
    private javax.swing.JPanel pnl_timKiem1;
    private javax.swing.JTable tbl_thongTinVe;
    private javax.swing.JTextField txt_cccd;
    private javax.swing.JTextField txt_hoTen;
    private javax.swing.JTextField txt_maVe;
    // End of variables declaration//GEN-END:variables
}
