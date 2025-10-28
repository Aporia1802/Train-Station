/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.quanLyVe;

import bus.QuanLyDatVe_BUS;
import entity.ChuyenTau;
import entity.Ghe;
import entity.HanhKhach;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhoangTau;
import entity.NhanVien;
import entity.ToaTau;
import entity.Ve;
import gui.components.ChonChoNgoi;
import gui.components.ChonChuyenTau;
import gui.components.ThongTinVe;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import main.Application;
import raven.toast.Notifications;
/**
 *
 * @author CÔNG HOÀNG
 */
public class DatVe_GUI extends javax.swing.JPanel {
    private QuanLyDatVe_BUS bus;
    private NhanVien nhanVien = null;
    private ChuyenTau chuyenTau = null;
    private ArrayList<HanhKhach> dsHanhKhach = new ArrayList();
    private ArrayList<Ghe> dsGhe = new ArrayList();
    private HoaDon hoaDon = null;
    private ArrayList<Ve> dsVe = new ArrayList();
    private KhachHang khachHang = null;
    /**
     * Creates new form DatVe_GUI
     */
    public DatVe_GUI() {
        initComponents();
        init();
       
    }
    
    private void init() {
        bus = new QuanLyDatVe_BUS();
        pnl_dsChuyenTau.setVisible(false);
        pnl_chonChoNgoi.setVisible(false);
        pnl_nhapThongTin.setVisible(false);
        this.nhanVien = Application.nhanVien;
        txt_gaDi.setText("Sài Gòn");
        txt_gaDen.setText("Hà Nội");
        LocalDate localDate = LocalDate.of(2025, 10, 30);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        date_ngayDi.setDate(date);

    }
    
    private void renderFormNhapThongTin() {
        
    }
    
    private void renderChoNgoi(ChuyenTau ct) {
        pnl_choNgoi.removeAll();

        ArrayList<ToaTau> dsToaTau = bus.getToaTau(ct.getTau().getMaTau());
        for (ToaTau toaTau : dsToaTau) {
            ChonChoNgoi toa = new ChonChoNgoi();
            toa.setData(toaTau);
            
        // Lấy danh sách khoang của toa này
        ArrayList<KhoangTau> dsKhoang = bus.getKhoangTau(toaTau.getMaToa());

        for (KhoangTau khoang : dsKhoang) {
            JPanel pnlKhoang = new JPanel();
            pnlKhoang.setBorder(BorderFactory.createTitledBorder("Khoang " + khoang.getSoHieuKhoang()));

            // Panel chứa các ghế trong khoang
            JPanel pnlGhe = new JPanel(new GridLayout(4, 2, 10, 10)); // 6 ghế, tùy bạn chỉnh

            // Lấy danh sách ghế trong khoang
            ArrayList<Ghe> dsGhe = bus.getGhe(khoang.getMaKhoangTau());

            for (Ghe ghe : dsGhe) {
                JButton btn = new JButton(String.valueOf(ghe.getSoGhe()));
                btn.setPreferredSize(new Dimension(50, 40));

                boolean isTrong = ghe.getTrangThaiGhe().compare(1);

                if (isTrong) {
                    btn.setBackground(new Color(204, 255, 204)); // xanh nhạt (trống)
                } else {
                    btn.setBackground(new Color(200, 200, 200)); // xám (đã đặt)
                    btn.setEnabled(false);
                }

                // Khi click chọn hoặc bỏ chọn ghế
                btn.addActionListener(e -> {
                    if (!isTrong) return; // ghế đã đặt thì bỏ qua

                    Color mauHienTai = btn.getBackground();

                    // Nếu ghế đang trống chọn
                    if (mauHienTai.equals(new Color(204, 255, 204))) {
                        btn.setBackground(new Color(123, 17, 19)); // xanh dương: đang chọn
                        btn.setForeground(new Color(255, 255, 255));

                        // thêm thông tin vé
                        pnl_thongTin.add(new ThongTinVe(
                            "Toa " + toaTau.getSoHieuToa() + " ghế " + ghe.getSoGhe(), ghe.getMaGhe()
                        ));
                        pnl_thongTin.revalidate();
                        pnl_thongTin.repaint();

                    } else { // Nếu đang chọn bỏ chọn
                        btn.setBackground(new Color(204, 255, 204)); // quay lại màu trống
                        btn.setForeground(new Color(0, 0, 0));

                        // Xóa form thông tin vé tương ứng
                        for (Component comp : pnl_thongTin.getComponents()) {
                            if (comp instanceof ThongTinVe) {
                                ThongTinVe ve = (ThongTinVe) comp;
                                if (ve.getMaGhe().equals(ghe.getMaGhe())) {
                                    pnl_thongTin.remove(comp);
                                    break;
                                }
                            }
                        }

                        pnl_thongTin.revalidate();
                        pnl_thongTin.repaint();
                    }
                });

                pnlGhe.add(btn);
            }

            pnlKhoang.add(pnlGhe);
            toa.setContent(pnlKhoang);
        }
            pnl_choNgoi.add(toa);
            pnl_choNgoi.add(Box.createVerticalStrut(10)); // cách giữa các panel
        }
            pnl_contain.revalidate();
            pnl_contain.repaint();
    }
    
    
    private void renderDanhSachChuyenTau(ArrayList<ChuyenTau> dsChuyenTau) {
        pnl_contain.removeAll();

        for (ChuyenTau ct : dsChuyenTau) {
            ChonChuyenTau item = new ChonChuyenTau();
            item.setData(ct);

            // Lắng nghe sự kiện "chọn"
            item.addPropertyChangeListener("chonChuyenTau", evt -> {
                chuyenTau = bus.getChuyenTauById(item.getMaChuyenTau());
                renderChoNgoi(chuyenTau);
                pnl_chonChoNgoi.setVisible(true);
                pnl_nhapThongTin.setVisible(true);
            });

            pnl_contain.add(item);
            pnl_contain.add(Box.createVerticalStrut(10)); // cách giữa các panel
        }
        pnl_dsChuyenTau.setVisible(true);
        pnl_contain.revalidate();
        pnl_contain.repaint();
    }
    
    private void handleDatVe() {
        
    }
    
    private void handleTImKiem() {
        try {
            String gaDi = txt_gaDi.getText().trim();
            String gaDen = txt_gaDen.getText().trim();
            LocalDate ngayDi = date_ngayDi.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            ArrayList<ChuyenTau> dsChuyenTau = bus.timKiemChuyenTau(gaDi, gaDen, ngayDi);
            if(dsChuyenTau.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Không tìm thấy chuyến tàu phù hợp!");
            } else {
                renderDanhSachChuyenTau(dsChuyenTau);
            }
        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không tìm thấy chuyến tàu phù hợp!");
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        panelShadow1 = new gui.custom.PanelShadow();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_gaDi = new javax.swing.JTextField();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_gaDen = new javax.swing.JTextField();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        jPanel6 = new javax.swing.JPanel();
        rad_motChieu = new javax.swing.JRadioButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(30, 0), new java.awt.Dimension(30, 0), new java.awt.Dimension(30, 32767));
        rad_khuHoi = new javax.swing.JRadioButton();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        date_ngayDi = new com.toedter.calendar.JDateChooser();
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        date_ngayVe = new com.toedter.calendar.JDateChooser();
        jPanel8 = new javax.swing.JPanel();
        btn_timKiem = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        pnl_dsChuyenTau = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        pnl_contain = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        pnl_chonChoNgoi = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnl_choNgoi = new javax.swing.JPanel();
        pnl_nhapThongTin = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        btn_datVe = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pnl_thongTin = new javax.swing.JPanel();
        pnl_thongTinKhachHang = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txt_hoTen = new javax.swing.JTextField();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jLabel6 = new javax.swing.JLabel();
        txt_cccd = new javax.swing.JTextField();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        jPanel14 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txt_sdt = new javax.swing.JTextField();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jLabel8 = new javax.swing.JLabel();
        date_ngaySinh = new com.toedter.calendar.JDateChooser();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 400));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jPanel2.setMaximumSize(new java.awt.Dimension(500, 32767));
        jPanel2.setLayout(new java.awt.BorderLayout());

        panelShadow1.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelShadow1.setPreferredSize(new java.awt.Dimension(470, 249));
        panelShadow1.setLayout(new javax.swing.BoxLayout(panelShadow1, javax.swing.BoxLayout.Y_AXIS));

        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 60));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Ga đi: ");
        jLabel1.setPreferredSize(new java.awt.Dimension(70, 16));
        jPanel3.add(jLabel1);

        txt_gaDi.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        jPanel3.add(txt_gaDi);

        panelShadow1.add(jPanel3);
        panelShadow1.add(filler3);

        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 60));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setText("Ga đến: ");
        jLabel2.setPreferredSize(new java.awt.Dimension(70, 16));
        jPanel4.add(jLabel2);

        txt_gaDen.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        jPanel4.add(txt_gaDen);

        panelShadow1.add(jPanel4);
        panelShadow1.add(filler5);

        jPanel6.setMaximumSize(new java.awt.Dimension(2032767, 30));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        buttonGroup1.add(rad_motChieu);
        rad_motChieu.setSelected(true);
        rad_motChieu.setText("Một chiều");
        jPanel6.add(rad_motChieu);
        jPanel6.add(filler1);

        buttonGroup1.add(rad_khuHoi);
        rad_khuHoi.setText("Khứ hồi");
        jPanel6.add(rad_khuHoi);

        panelShadow1.add(jPanel6);
        panelShadow1.add(filler7);

        jPanel5.setMaximumSize(new java.awt.Dimension(32767, 60));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        jLabel3.setText("Ngày đi:");
        jLabel3.setPreferredSize(new java.awt.Dimension(70, 16));
        jPanel5.add(jLabel3);

        date_ngayDi.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        jPanel5.add(date_ngayDi);

        panelShadow1.add(jPanel5);
        panelShadow1.add(filler8);

        jPanel7.setMaximumSize(new java.awt.Dimension(32767, 60));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));

        jLabel5.setText("Ngày về: ");
        jLabel5.setPreferredSize(new java.awt.Dimension(70, 16));
        jPanel7.add(jLabel5);

        date_ngayVe.setEnabled(false);
        date_ngayVe.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        jPanel7.add(date_ngayVe);

        panelShadow1.add(jPanel7);

        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 45));
        jPanel8.setPreferredSize(new java.awt.Dimension(90, 55));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 5));

        btn_timKiem.setText("Tìm kiếm");
        btn_timKiem.setPreferredSize(new java.awt.Dimension(90, 40));
        btn_timKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_timKiemActionPerformed(evt);
            }
        });
        jPanel8.add(btn_timKiem);

        panelShadow1.add(jPanel8);

        jPanel2.add(panelShadow1, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2);

        jPanel9.setLayout(new java.awt.BorderLayout());

        pnl_dsChuyenTau.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
        pnl_dsChuyenTau.setPreferredSize(new java.awt.Dimension(250, 400));
        pnl_dsChuyenTau.setLayout(new java.awt.BorderLayout());

        jPanel20.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 4, 10, 0));
        jPanel20.setLayout(new java.awt.GridLayout(1, 0));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel31.setText("Chọn chuyến tàu");
        jPanel20.add(jLabel31);

        pnl_dsChuyenTau.add(jPanel20, java.awt.BorderLayout.PAGE_START);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setLayout(new java.awt.BorderLayout());
        pnl_dsChuyenTau.add(jPanel12, java.awt.BorderLayout.CENTER);

        jScrollPane3.setBorder(null);
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        pnl_contain.setPreferredSize(null);
        pnl_contain.setLayout(new javax.swing.BoxLayout(pnl_contain, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane3.setViewportView(pnl_contain);

        pnl_dsChuyenTau.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel9.add(pnl_dsChuyenTau, java.awt.BorderLayout.PAGE_START);

        jPanel1.add(jPanel9);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel11.setLayout(new java.awt.BorderLayout());

        pnl_chonChoNgoi.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 0, 10));
        pnl_chonChoNgoi.setPreferredSize(new java.awt.Dimension(250, 200));
        pnl_chonChoNgoi.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setLayout(new java.awt.BorderLayout());

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel35.setText("Chọn chỗ ngồi");
        jPanel25.add(jLabel35, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);

        pnl_choNgoi.setBackground(new java.awt.Color(255, 255, 255));
        pnl_choNgoi.setPreferredSize(null);
        pnl_choNgoi.setLayout(new javax.swing.BoxLayout(pnl_choNgoi, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(pnl_choNgoi);

        jPanel25.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pnl_chonChoNgoi.add(jPanel25);

        pnl_nhapThongTin.setBackground(new java.awt.Color(255, 255, 255));
        pnl_nhapThongTin.setLayout(new java.awt.BorderLayout());

        jPanel10.setPreferredSize(new java.awt.Dimension(100, 50));
        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 3));

        btn_datVe.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_datVe.setText("Đặt vé");
        btn_datVe.setPreferredSize(new java.awt.Dimension(100, 45));
        btn_datVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_datVeActionPerformed(evt);
            }
        });
        jPanel10.add(btn_datVe);

        pnl_nhapThongTin.add(jPanel10, java.awt.BorderLayout.PAGE_END);

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel36.setText("Nhập thông tin");
        pnl_nhapThongTin.add(jLabel36, java.awt.BorderLayout.PAGE_START);

        jScrollPane2.setBorder(null);

        pnl_thongTin.setBackground(new java.awt.Color(255, 255, 255));
        pnl_thongTin.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_thongTin.setPreferredSize(null);
        pnl_thongTin.setLayout(new javax.swing.BoxLayout(pnl_thongTin, javax.swing.BoxLayout.Y_AXIS));

        pnl_thongTinKhachHang.setBackground(new java.awt.Color(255, 255, 255));
        pnl_thongTinKhachHang.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10))); // NOI18N
        pnl_thongTinKhachHang.setMaximumSize(new java.awt.Dimension(32767, 160));
        pnl_thongTinKhachHang.setLayout(new javax.swing.BoxLayout(pnl_thongTinKhachHang, javax.swing.BoxLayout.Y_AXIS));

        jPanel13.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.LINE_AXIS));

        jLabel4.setText("Họ tên:");
        jLabel4.setPreferredSize(new java.awt.Dimension(60, 16));
        jPanel13.add(jLabel4);
        jPanel13.add(txt_hoTen);
        jPanel13.add(filler2);

        jLabel6.setText("CCCD:");
        jLabel6.setPreferredSize(new java.awt.Dimension(60, 16));
        jPanel13.add(jLabel6);
        jPanel13.add(txt_cccd);

        pnl_thongTinKhachHang.add(jPanel13);
        pnl_thongTinKhachHang.add(filler4);

        jPanel14.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.LINE_AXIS));

        jLabel7.setText("SĐT:");
        jLabel7.setPreferredSize(new java.awt.Dimension(60, 16));
        jPanel14.add(jLabel7);
        jPanel14.add(txt_sdt);
        jPanel14.add(filler6);

        jLabel8.setText("NS:");
        jLabel8.setMaximumSize(new java.awt.Dimension(60, 16));
        jLabel8.setMinimumSize(new java.awt.Dimension(60, 16));
        jLabel8.setPreferredSize(new java.awt.Dimension(60, 16));
        jPanel14.add(jLabel8);

        date_ngaySinh.setMaximumSize(txt_hoTen.getMaximumSize());
        date_ngaySinh.setPreferredSize(txt_hoTen.getPreferredSize());
        jPanel14.add(date_ngaySinh);

        pnl_thongTinKhachHang.add(jPanel14);

        pnl_thongTin.add(pnl_thongTinKhachHang);

        jScrollPane2.setViewportView(pnl_thongTin);

        pnl_nhapThongTin.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        pnl_chonChoNgoi.add(pnl_nhapThongTin);

        jPanel11.add(pnl_chonChoNgoi, java.awt.BorderLayout.CENTER);

        add(jPanel11, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_timKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_timKiemActionPerformed
        // TODO add your handling code here:
        handleTImKiem();
    }//GEN-LAST:event_btn_timKiemActionPerformed

    private void btn_datVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_datVeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_datVeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_datVe;
    private javax.swing.JButton btn_timKiem;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.toedter.calendar.JDateChooser date_ngayDi;
    private com.toedter.calendar.JDateChooser date_ngaySinh;
    private com.toedter.calendar.JDateChooser date_ngayVe;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private gui.custom.PanelShadow panelShadow1;
    private javax.swing.JPanel pnl_choNgoi;
    private javax.swing.JPanel pnl_chonChoNgoi;
    private javax.swing.JPanel pnl_contain;
    private javax.swing.JPanel pnl_dsChuyenTau;
    private javax.swing.JPanel pnl_nhapThongTin;
    private javax.swing.JPanel pnl_thongTin;
    private javax.swing.JPanel pnl_thongTinKhachHang;
    private javax.swing.JRadioButton rad_khuHoi;
    private javax.swing.JRadioButton rad_motChieu;
    private javax.swing.JTextField txt_cccd;
    private javax.swing.JTextField txt_gaDen;
    private javax.swing.JTextField txt_gaDi;
    private javax.swing.JTextField txt_hoTen;
    private javax.swing.JTextField txt_sdt;
    // End of variables declaration//GEN-END:variables
}
