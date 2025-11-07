/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.components;

import bus.QuanLyDatVe_BUS;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import entity.ChuyenTau;
import entity.GaTau;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import utils.FormatUtil;

/**
 *
 * @author CÔNG HOÀNG
 */
public class TimVeDoi extends javax.swing.JPanel {
    private QuanLyDatVe_BUS bus;
    /**
     * Creates new form TimVeDoi
     */
    public TimVeDoi(QuanLyDatVe_BUS bus) {
        initComponents();
        this.bus = bus;
    }
    
    private void init() {
        
    }
   
    
    private ArrayList<ChuyenTau> dsChuyenDi(String gaDi, String gaDen, LocalDate ngayDi) throws Exception {
        return bus.timKiemChuyenTau(gaDi, gaDen, ngayDi);
    }

    private ArrayList<ChuyenTau> dsChuyenVe(String gaDi, String gaDen, LocalDate ngayVe) throws Exception {
        return bus.timKiemChuyenTau(gaDen, gaDi, ngayVe);
    }

    private void handleTimKiem() {
        
    }

/**
 * Chuyển đổi Date sang LocalDate
 */
    private LocalDate convertDateToLocalDate(Date date) {
        return date.toInstant()
               .atZone(ZoneId.systemDefault())
               .toLocalDate();
    }

/**
 * Hiển thị danh sách chuyến tàu lên giao diện
 * @return true nếu có chuyến tàu, false nếu không tìm thấy
 */
    private boolean hienThiDanhSachChuyen(ArrayList<ChuyenTau> dsChuyen, 
                                       JPanel pnlChuyen, 
                                       JPanel pnlDsChuyen,
                                       JLabel lblTieuDe,
                                       LocalDate ngay,
                                       String gaDi, 
                                       String gaDen,
                                       String loaiChuyen) {
        if (dsChuyen.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Không tìm thấy tàu cho chuyến " + loaiChuyen + " của bạn",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    
        String tieuDe = String.format("Chiều %s: ngày %s từ %s đến %s", 
                                  loaiChuyen, 
                                  FormatUtil.formatDate(ngay), 
                                  gaDi, 
                                  gaDen);
        lblTieuDe.setText(tieuDe);
        
        pnlChuyen.setVisible(true);
        
        // Truyền thêm tham số để biết là chuyến đi hay về
        boolean isChuyenDi = loaiChuyen.equals("đi");
        capNhatDanhSachChuyen(pnlDsChuyen, dsChuyen, isChuyenDi);
    
        return true;
    }
    
    /**
     * Cập nhật danh sách chuyến tàu vào panel với khả năng chọn
     */
    private void capNhatDanhSachChuyen(JPanel panel, ArrayList<ChuyenTau> dsChuyen, boolean isChuyenDi) {
        panel.removeAll();
    
        for (ChuyenTau chuyenTau : dsChuyen) {
            ThongTinChuyenTau thongTinPanel = new ThongTinChuyenTau(chuyenTau);
            
            // Thêm listener để xử lý khi chọn
            thongTinPanel.setSelectionListener(e -> {
                handleChonChuyenTau(thongTinPanel, isChuyenDi);
            });
            
            panel.add(thongTinPanel);
        }
    
        panel.revalidate();
        panel.repaint();
    }
    
    /**
     * Xử lý khi chọn một chuyến tàu
     */

    private void handleChonChuyenTau(ThongTinChuyenTau selectedPanel, boolean isChuyenDi) {
        if (isChuyenDi) {
            // Nếu click vào panel đang được chọn -> Bỏ chọn
            if (panelChuyenDiDangChon == selectedPanel) {
                selectedPanel.setSelected(false);
                panelChuyenDiDangChon = null;
                chuyenDiDaChon = null;
            } else {
            //  Bỏ chọn panel cũ nếu có
                if (panelChuyenDiDangChon != null) {
                    panelChuyenDiDangChon.setSelected(false);
                }
            
            // Chọn panel mới
                selectedPanel.setSelected(true);
                panelChuyenDiDangChon = selectedPanel;
                chuyenDiDaChon = selectedPanel.getChuyenTau();
            }
        } else {
            
        // Xử lý cho chuyến về
        // Nếu click vào panel đang được chọn - Bỏ chọn
            if (panelChuyenVeDangChon == selectedPanel) {
                selectedPanel.setSelected(false);
                panelChuyenVeDangChon = null;
                chuyenVeDaChon = null;
            } else {
            //  Bỏ chọn panel cũ nếu có
                if (panelChuyenVeDangChon != null) {
                    panelChuyenVeDangChon.setSelected(false);
                }
            
            //  Chọn panel mới
                selectedPanel.setSelected(true);
                panelChuyenVeDangChon = selectedPanel;
                chuyenVeDaChon = selectedPanel.getChuyenTau();
            }
        }
    }
    // Getter methods để lấy thông tin chuyến tàu đã chọn
    public ChuyenTau getChuyenDiDaChon() {
        return chuyenDiDaChon;
    }
    
    public ChuyenTau getChuyenVeDaChon() {
        return chuyenVeDaChon;
    }
    
    public boolean isKhuHoi() {
        return rad_khuHoi.isSelected();
    }
    
    /**
        * Reset panel chiều về về trạng thái ban đầu
    */
    private void resetPanelChieuVe() {
        // Ẩn panel chiều về
        pnl_chieuVe.setVisible(false);
    
        // Xóa danh sách chuyến về
        pnl_dsChuyenVe.removeAll();
        pnl_dsChuyenVe.revalidate();
        pnl_dsChuyenVe.repaint();
    
        // Reset các biến liên quan đến chuyến về
        chuyenVeDaChon = null;
        panelChuyenVeDangChon = null;
    
        // Reset tiêu đề (tùy chọn)
        lbl_tieuDeChuyenVe.setText("");
    }
    
    public JButton next() {
        return btn_next;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel11 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        rSMaterialButtonCircle1 = new rojerusan.RSMaterialButtonCircle();
        roundedButton1 = new gui.custom.RoundedButton();
        jPanel2 = new javax.swing.JPanel();
        rSMaterialButtonCircle2 = new rojerusan.RSMaterialButtonCircle();
        roundedButton2 = new gui.custom.RoundedButton();
        jPanel9 = new javax.swing.JPanel();
        rSMaterialButtonCircle4 = new rojerusan.RSMaterialButtonCircle();
        roundedButton3 = new gui.custom.RoundedButton();
        pnl_chonChuyenTau = new javax.swing.JPanel();
        pnl_chieuDi = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        lbl_tieuDeChuyenDi = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnl_dsChuyenDi = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        pnl_quyDinh = new gui.custom.PanelShadow();
        jLabel1 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        jLabel2 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        jLabel3 = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 20));
        pnl_timKiem = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        btn_timKiem = new javax.swing.JButton();
        pnl_thongTinVe = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        pnl = new gui.custom.PanelShadow();
        jLabel20 = new javax.swing.JLabel();
        lbl_hoTen = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lbl_cccd = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lbl_tau = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lbl_soGhe = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lbl_hanhTrinh = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lbl_ngayDi = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lbl_gioKhoiHanh = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        btn_next = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        jPanel11.setPreferredSize(new java.awt.Dimension(915, 100));
        jPanel11.setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(300, 60));

        rSMaterialButtonCircle1.setBackground(new java.awt.Color(123, 17, 19));
        rSMaterialButtonCircle1.setText("1");
        rSMaterialButtonCircle1.setFont(new java.awt.Font("Roboto Medium", 1, 17)); // NOI18N
        rSMaterialButtonCircle1.setPreferredSize(new java.awt.Dimension(40, 40));
        rSMaterialButtonCircle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle1ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonCircle1);

        roundedButton1.setBackground(new java.awt.Color(123, 17, 19));
        roundedButton1.setForeground(new java.awt.Color(255, 255, 255));
        roundedButton1.setText("Chọn chuyến tàu");
        roundedButton1.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        roundedButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        roundedButton1.setPreferredSize(new java.awt.Dimension(230, 58));
        jPanel1.add(roundedButton1);

        jPanel11.add(jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(300, 60));

        rSMaterialButtonCircle2.setBackground(new java.awt.Color(184, 184, 184));
        rSMaterialButtonCircle2.setForeground(new java.awt.Color(0, 0, 0));
        rSMaterialButtonCircle2.setText("2");
        rSMaterialButtonCircle2.setFont(new java.awt.Font("Roboto Medium", 1, 17)); // NOI18N
        rSMaterialButtonCircle2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rSMaterialButtonCircle2.setPreferredSize(new java.awt.Dimension(40, 40));
        rSMaterialButtonCircle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle2ActionPerformed(evt);
            }
        });
        jPanel2.add(rSMaterialButtonCircle2);

        roundedButton2.setBackground(new java.awt.Color(212, 212, 212));
        roundedButton2.setForeground(new java.awt.Color(0, 0, 0));
        roundedButton2.setText("Chọn chỗ ngồi");
        roundedButton2.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        roundedButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        roundedButton2.setPreferredSize(new java.awt.Dimension(230, 58));
        jPanel2.add(roundedButton2);

        jPanel11.add(jPanel2);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setMaximumSize(new java.awt.Dimension(300, 60));

        rSMaterialButtonCircle4.setBackground(new java.awt.Color(184, 184, 184));
        rSMaterialButtonCircle4.setForeground(new java.awt.Color(0, 0, 0));
        rSMaterialButtonCircle4.setText("3");
        rSMaterialButtonCircle4.setFont(new java.awt.Font("Roboto Medium", 1, 17)); // NOI18N
        rSMaterialButtonCircle4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rSMaterialButtonCircle4.setPreferredSize(new java.awt.Dimension(40, 40));
        rSMaterialButtonCircle4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle4ActionPerformed(evt);
            }
        });
        jPanel9.add(rSMaterialButtonCircle4);

        roundedButton3.setBackground(new java.awt.Color(212, 212, 212));
        roundedButton3.setForeground(new java.awt.Color(0, 0, 0));
        roundedButton3.setText("Thanh toán");
        roundedButton3.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        roundedButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        roundedButton3.setPreferredSize(new java.awt.Dimension(230, 58));
        jPanel9.add(roundedButton3);

        jPanel11.add(jPanel9);

        add(jPanel11, java.awt.BorderLayout.PAGE_START);

        pnl_chonChuyenTau.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
        pnl_chonChuyenTau.setPreferredSize(new java.awt.Dimension(250, 400));
        pnl_chonChuyenTau.setLayout(new java.awt.BorderLayout());

        pnl_chieuDi.setBackground(new java.awt.Color(255, 255, 255));
        pnl_chieuDi.setMaximumSize(new java.awt.Dimension(980, 2147483647));
        pnl_chieuDi.setPreferredSize(new java.awt.Dimension(980, 245));
        pnl_chieuDi.setLayout(new java.awt.BorderLayout());

        jPanel20.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 4, 10, 0));
        jPanel20.setLayout(new java.awt.GridLayout(1, 0));

        lbl_tieuDeChuyenDi.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_tieuDeChuyenDi.setText("Chiều đi: ngày 02/11/2025 từ Sài Gòn đến Hà Nội");
        jPanel20.add(lbl_tieuDeChuyenDi);

        pnl_chieuDi.add(jPanel20, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setBorder(null);

        pnl_dsChuyenDi.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jScrollPane1.setViewportView(pnl_dsChuyenDi);

        pnl_chieuDi.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pnl_chonChuyenTau.add(pnl_chieuDi, java.awt.BorderLayout.CENTER);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 20, 20));
        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 300));
        jPanel3.setPreferredSize(new java.awt.Dimension(1366, 240));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        pnl_quyDinh.setBackground(new java.awt.Color(217, 237, 247));
        pnl_quyDinh.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 20));
        pnl_quyDinh.setPreferredSize(new java.awt.Dimension(100, 200));
        pnl_quyDinh.setShadowOpacity(0.2F);
        pnl_quyDinh.setShadowSize(4);
        pnl_quyDinh.setLayout(new javax.swing.BoxLayout(pnl_quyDinh, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("- Hành khách phải thực hiện thay đổi trước khi tàu khởi hành ít nhất 24h.");
        pnl_quyDinh.add(jLabel1);
        pnl_quyDinh.add(filler1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("- Mức phí thay đổi là 20.000 đồng. ");
        pnl_quyDinh.add(jLabel2);
        pnl_quyDinh.add(filler2);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("- Nếu dưới 24 giờ trước giờ tàu chạy, việc đổi vé sẽ không được áp dụng.");
        pnl_quyDinh.add(jLabel3);

        jPanel3.add(pnl_quyDinh);
        jPanel3.add(filler4);

        pnl_timKiem.setMaximumSize(new java.awt.Dimension(32767, 60));
        pnl_timKiem.setPreferredSize(new java.awt.Dimension(1346, 50));
        pnl_timKiem.setLayout(new java.awt.BorderLayout());

        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 100));
        jPanel4.setMinimumSize(new java.awt.Dimension(0, 100));
        jPanel4.setPreferredSize(new java.awt.Dimension(1326, 60));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Mã vé:");
        jLabel5.setPreferredSize(new java.awt.Dimension(80, 16));
        jPanel4.add(jLabel5);

        jTextField1.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        jTextField1.setPreferredSize(new java.awt.Dimension(600, 50));
        jPanel4.add(jTextField1);

        btn_timKiem.setText("Tìm kiếm");
        btn_timKiem.setPreferredSize(new java.awt.Dimension(100, 50));
        btn_timKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_timKiemActionPerformed(evt);
            }
        });
        jPanel4.add(btn_timKiem);

        pnl_timKiem.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel3.add(pnl_timKiem);

        pnl_chonChuyenTau.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        pnl_thongTinVe.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 0, 10));
        pnl_thongTinVe.setPreferredSize(new java.awt.Dimension(500, 218));
        pnl_thongTinVe.setLayout(new java.awt.BorderLayout());

        jPanel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 4, 10, 0));
        jPanel10.setLayout(new java.awt.GridLayout(1, 0));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Thông tin vé");
        jPanel10.add(jLabel6);

        pnl_thongTinVe.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        pnl.setBackground(new java.awt.Color(255, 255, 255));
        pnl.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
        pnl.setShadowOpacity(0.2F);
        pnl.setShadowSize(4);
        pnl.setLayout(new java.awt.GridLayout(7, 2));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel20.setText("Họ tên:");
        pnl.add(jLabel20);

        lbl_hoTen.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_hoTen.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_hoTen.setText("jLabel21");
        pnl.add(lbl_hoTen);

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel22.setText("CCCD:");
        pnl.add(jLabel22);

        lbl_cccd.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_cccd.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_cccd.setText("jLabel23");
        pnl.add(lbl_cccd);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel8.setText("Tàu:");
        pnl.add(jLabel8);

        lbl_tau.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_tau.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_tau.setText("jLabel9");
        pnl.add(lbl_tau);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel17.setText("Số ghế:");
        pnl.add(jLabel17);

        lbl_soGhe.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_soGhe.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_soGhe.setText("jLabel19");
        pnl.add(lbl_soGhe);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel11.setText("Hành trình:");
        pnl.add(jLabel11);

        lbl_hanhTrinh.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_hanhTrinh.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_hanhTrinh.setText("jLabel12");
        pnl.add(lbl_hanhTrinh);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel13.setText("Ngày đi:");
        pnl.add(jLabel13);

        lbl_ngayDi.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_ngayDi.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_ngayDi.setText("jLabel14");
        pnl.add(lbl_ngayDi);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel15.setText("Giờ khởi hành:");
        pnl.add(jLabel15);

        lbl_gioKhoiHanh.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_gioKhoiHanh.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_gioKhoiHanh.setText("jLabel16");
        pnl.add(lbl_gioKhoiHanh);

        pnl_thongTinVe.add(pnl, java.awt.BorderLayout.CENTER);

        pnl_chonChuyenTau.add(pnl_thongTinVe, java.awt.BorderLayout.LINE_START);
        pnl_thongTinVe.getAccessibleContext().setAccessibleName("");

        add(pnl_chonChuyenTau, java.awt.BorderLayout.CENTER);

        jPanel14.setPreferredSize(new java.awt.Dimension(100, 60));
        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 5));

        btn_next.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_next.setText("Tiếp tục");
        btn_next.setPreferredSize(new java.awt.Dimension(140, 50));
        btn_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nextActionPerformed(evt);
            }
        });
        jPanel14.add(btn_next);

        add(jPanel14, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void rSMaterialButtonCircle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rSMaterialButtonCircle1ActionPerformed

    private void rSMaterialButtonCircle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rSMaterialButtonCircle2ActionPerformed

    private void rSMaterialButtonCircle4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rSMaterialButtonCircle4ActionPerformed

    private void btn_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_nextActionPerformed

    private void btn_timKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_timKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_timKiemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_next;
    private javax.swing.JButton btn_timKiem;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lbl_cccd;
    private javax.swing.JLabel lbl_gioKhoiHanh;
    private javax.swing.JLabel lbl_hanhTrinh;
    private javax.swing.JLabel lbl_hoTen;
    private javax.swing.JLabel lbl_ngayDi;
    private javax.swing.JLabel lbl_soGhe;
    private javax.swing.JLabel lbl_tau;
    private javax.swing.JLabel lbl_tieuDeChuyenDi;
    private gui.custom.PanelShadow pnl;
    private javax.swing.JPanel pnl_chieuDi;
    private javax.swing.JPanel pnl_chonChuyenTau;
    private javax.swing.JPanel pnl_dsChuyenDi;
    private gui.custom.PanelShadow pnl_quyDinh;
    private javax.swing.JPanel pnl_thongTinVe;
    private javax.swing.JPanel pnl_timKiem;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle1;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle4;
    private gui.custom.RoundedButton roundedButton1;
    private gui.custom.RoundedButton roundedButton2;
    private gui.custom.RoundedButton roundedButton3;
    // End of variables declaration//GEN-END:variables
}
