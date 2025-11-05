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
import java.util.Calendar;
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
public class ChonChuyenTau extends javax.swing.JPanel {
    private final QuanLyDatVe_BUS bus;
    private ChuyenTau chuyenDiDaChon = null;
    private ChuyenTau chuyenVeDaChon = null;
    private ThongTinChuyenTau panelChuyenDiDangChon = null;
    private ThongTinChuyenTau panelChuyenVeDangChon = null;
    /**
     * Creates new form DatVe_GUI
     * @param bus
     */
    public ChonChuyenTau(QuanLyDatVe_BUS bus) {
        initComponents();
        this.bus = bus;
        init();
       
    }
    
    private void init() {
        loadDataToCbo(cbo_gaDi);
        loadDataToCbo(cbo_gaDen);
        date_ngayVe.setEnabled(false);
        date_ngayDi.setDate(new Date());
        date_ngayVe.setDate(new Date());
        cbo_gaDi.setSelectedItem("Sài Gòn");
        cbo_gaDen.setSelectedItem("Hà Nội");
        pnl_chieuDi.setVisible(false);
        pnl_chieuVe.setVisible(false);
//        Calendar cal = Calendar.getInstance();
//        date_ngayDi.getJCalendar().setMinSelectableDate(cal.getTime());
//        date_ngayVe.getJCalendar().setMinSelectableDate(cal.getTime());
//        date_ngayDi.addPropertyChangeListener("date", evt -> {
//            if (date_ngayDi.getDate() != null) {
//                date_ngayVe.setDate(date_ngayDi.getDate());
//                date_ngayVe.getJCalendar().setMinSelectableDate(date_ngayDi.getDate());
//            }
//        });
    }
    
//  Load danh sách ga tàu
    private void loadDataToCbo(JComboBox<String> cbo) {
        ArrayList<GaTau> dsGaTau = bus.getAllGaTau();

        // Tạo EventList dữ liệu
        EventList<String> eventList = new BasicEventList<>();
        for (GaTau gaTau : dsGaTau) {
            eventList.add(gaTau.getTenGa());
        }

        // Custom filter để hỗ trợ tiếng Việt (bỏ dấu khi so sánh)
        TextFilterator<String> filterator = (List<String> baseList, String element) -> {
            baseList.add(removeVietnameseAccents(element)); // bỏ dấu khi lọc
        };

        // Cài đặt AutoCompleteSupport
        AutoCompleteSupport<String> support = AutoCompleteSupport.install(cbo, eventList, filterator);

        // Cấu hình filter cho phép tìm chuỗi chứa
        support.setFilterMode(TextMatcherEditor.CONTAINS);
        support.setStrict(false); // Cho phép gõ giá trị mới
        support.setSelectsTextOnFocusGain(true);

        // Fix xung đột với FlatLaf
        cbo.putClientProperty("JComponent.minimumWidth", null);
        cbo.putClientProperty(AutoCompleteSupport.class, support);
        
        cbo.setSelectedIndex(0);
    }

    
//  Hàm loại bỏ dấu tiếng Việt (để so khớp không phân biệt dấu)
    private static String removeVietnameseAccents(String text) {
        if (text == null) return "";
        String temp = Normalizer.normalize(text, Normalizer.Form.NFD);
        return temp.replaceAll("\\p{M}", "").replaceAll("Đ", "D").replaceAll("đ", "d");
    }
    
    
    private ArrayList<ChuyenTau> dsChuyenDi(String gaDi, String gaDen, LocalDate ngayDi) throws Exception {
        return bus.timKiemChuyenTau(gaDi, gaDen, ngayDi);
    }

    private ArrayList<ChuyenTau> dsChuyenVe(String gaDi, String gaDen, LocalDate ngayVe) throws Exception {
        return bus.timKiemChuyenTau(gaDen, gaDi, ngayVe);
    }

    private void handleTimKiem() {
        try {
            String gaDi = cbo_gaDi.getSelectedItem().toString();
            String gaDen = cbo_gaDen.getSelectedItem().toString();
            LocalDate ngayDi = convertDateToLocalDate(date_ngayDi.getDate());
        
        // Tìm và hiển thị chuyến đi
            ArrayList<ChuyenTau> dsChuyenDi = dsChuyenDi(gaDi, gaDen, ngayDi);
            if (!hienThiDanhSachChuyen(dsChuyenDi, pnl_chieuDi, pnl_dsChuyenDi, 
                                     lbl_tieuDeChuyenDi, ngayDi, gaDi, gaDen, "đi")) {
                return;
            }
        
        //  Xử lý chuyến về nếu là khứ hồi
            if (rad_khuHoi.isSelected()) {
                LocalDate ngayVe = convertDateToLocalDate(date_ngayVe.getDate());
                ArrayList<ChuyenTau> dsChuyenVe = dsChuyenVe(gaDi, gaDen, ngayVe);
                hienThiDanhSachChuyen(dsChuyenVe, pnl_chieuVe, pnl_dsChuyenVe, 
                                   lbl_tieuDeChuyenVe, ngayVe, gaDen, gaDi, "về");
            } else {
                resetPanelChieuVe();
            }
        } catch (Exception ex) {
            Logger.getLogger(ChonChuyenTau.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, 
                "Có lỗi xảy ra khi tìm kiếm chuyến tàu. Vui lòng thử lại!",
                "Lỗi", 
            JOptionPane.ERROR_MESSAGE);
        }
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
        // Nếu click vào panel đang được chọn -> Bỏ chọn
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

        buttonGroup1 = new javax.swing.ButtonGroup();
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
        jPanel10 = new javax.swing.JPanel();
        pnl_chonChuyenTau = new javax.swing.JPanel();
        pnl_chieuDi = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        lbl_tieuDeChuyenDi = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnl_dsChuyenDi = new javax.swing.JPanel();
        pnl_chieuVe = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        lbl_tieuDeChuyenVe = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pnl_dsChuyenVe = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        jPanel17 = new javax.swing.JPanel();
        cbo_gaDi = new javax.swing.JComboBox<>();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        jPanel18 = new javax.swing.JPanel();
        cbo_gaDen = new javax.swing.JComboBox<>();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        jPanel6 = new javax.swing.JPanel();
        rad_motChieu = new javax.swing.JRadioButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(30, 0), new java.awt.Dimension(30, 0), new java.awt.Dimension(30, 32767));
        rad_khuHoi = new javax.swing.JRadioButton();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 20));
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        date_ngayDi = new com.toedter.calendar.JDateChooser();
        filler12 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        date_ngayVe = new com.toedter.calendar.JDateChooser();
        filler10 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        jPanel8 = new javax.swing.JPanel();
        btn_timKiem = new javax.swing.JButton();
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

        jPanel10.setLayout(new java.awt.BorderLayout());

        pnl_chonChuyenTau.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
        pnl_chonChuyenTau.setPreferredSize(new java.awt.Dimension(250, 400));
        pnl_chonChuyenTau.setLayout(new java.awt.GridLayout(2, 0));

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
        jScrollPane1.setViewportView(pnl_dsChuyenDi);

        pnl_dsChuyenDi.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jScrollPane1.setViewportView(pnl_dsChuyenDi);

        pnl_chieuDi.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pnl_chonChuyenTau.add(pnl_chieuDi);

        pnl_chieuVe.setBackground(new java.awt.Color(255, 255, 255));
        pnl_chieuVe.setLayout(new java.awt.BorderLayout());

        jPanel21.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 4, 10, 0));
        jPanel21.setLayout(new java.awt.GridLayout(1, 0));

        lbl_tieuDeChuyenVe.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_tieuDeChuyenVe.setText("Chiều về: ngày 05/11/2025 từ Hà Nội đến Sài Gòn");
        jPanel21.add(lbl_tieuDeChuyenVe);

        pnl_chieuVe.add(jPanel21, java.awt.BorderLayout.PAGE_START);

        jScrollPane2.setBorder(null);

        pnl_dsChuyenVe.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jScrollPane2.setViewportView(pnl_dsChuyenVe);

        pnl_chieuVe.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        pnl_chonChuyenTau.add(pnl_chieuVe);

        jPanel10.add(pnl_chonChuyenTau, java.awt.BorderLayout.CENTER);

        jPanel16.setPreferredSize(new java.awt.Dimension(350, 610));

        jPanel15.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin hành trình", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(123, 17, 19)), javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20))); // NOI18N
        jPanel15.setMaximumSize(new java.awt.Dimension(2147483647, 400));
        jPanel15.setMinimumSize(new java.awt.Dimension(283, 400));
        jPanel15.setPreferredSize(new java.awt.Dimension(350, 620));
        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15, javax.swing.BoxLayout.Y_AXIS));

        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(123, 17, 19));
        jLabel1.setText("Ga đi");
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel3.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel3);
        jPanel15.add(filler2);

        jPanel17.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel17.setLayout(new java.awt.BorderLayout());

        cbo_gaDi.setMaximumSize(new java.awt.Dimension(32767, 50));
        cbo_gaDi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_gaDiActionPerformed(evt);
            }
        });
        jPanel17.add(cbo_gaDi, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel17);
        jPanel15.add(filler4);

        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(123, 17, 19));
        jLabel2.setText("Ga đến");
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel4.add(jLabel2, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel4);
        jPanel15.add(filler3);

        jPanel18.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel18.setLayout(new java.awt.BorderLayout());

        cbo_gaDen.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel18.add(cbo_gaDen, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel18);
        jPanel15.add(filler5);

        jPanel6.setMaximumSize(new java.awt.Dimension(2032767, 30));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        buttonGroup1.add(rad_motChieu);
        rad_motChieu.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        rad_motChieu.setForeground(new java.awt.Color(123, 17, 19));
        rad_motChieu.setSelected(true);
        rad_motChieu.setText("Một chiều");
        rad_motChieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rad_motChieuActionPerformed(evt);
            }
        });
        jPanel6.add(rad_motChieu);
        jPanel6.add(filler1);

        buttonGroup1.add(rad_khuHoi);
        rad_khuHoi.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        rad_khuHoi.setForeground(new java.awt.Color(123, 17, 19));
        rad_khuHoi.setText("Khứ hồi");
        rad_khuHoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rad_khuHoiActionPerformed(evt);
            }
        });
        jPanel6.add(rad_khuHoi);

        jPanel15.add(jPanel6);
        jPanel15.add(filler7);

        jPanel5.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(123, 17, 19));
        jLabel3.setText("Ngày đi");
        jLabel3.setPreferredSize(new java.awt.Dimension(200, 16));
        jPanel5.add(jLabel3, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel5);
        jPanel15.add(filler6);

        date_ngayDi.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        jPanel15.add(date_ngayDi);
        jPanel15.add(filler12);

        jPanel7.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(123, 17, 19));
        jLabel5.setText("Ngày về");
        jLabel5.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel7.add(jLabel5, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel7);
        jPanel15.add(filler9);

        date_ngayVe.setEnabled(false);
        date_ngayVe.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        jPanel15.add(date_ngayVe);
        jPanel15.add(filler10);

        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 60));
        jPanel8.setPreferredSize(new java.awt.Dimension(90, 55));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 5));

        btn_timKiem.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btn_timKiem.setText("Tìm kiếm");
        btn_timKiem.setPreferredSize(new java.awt.Dimension(120, 45));
        btn_timKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_timKiemActionPerformed(evt);
            }
        });
        jPanel8.add(btn_timKiem);

        jPanel15.add(jPanel8);

        jPanel16.add(jPanel15);

        jPanel10.add(jPanel16, java.awt.BorderLayout.LINE_START);

        add(jPanel10, java.awt.BorderLayout.CENTER);

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

    private void btn_timKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_timKiemActionPerformed
        // TODO add your handling code here:
        handleTimKiem();
    }//GEN-LAST:event_btn_timKiemActionPerformed

    private void rSMaterialButtonCircle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rSMaterialButtonCircle1ActionPerformed

    private void rSMaterialButtonCircle4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rSMaterialButtonCircle4ActionPerformed

    private void rSMaterialButtonCircle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rSMaterialButtonCircle2ActionPerformed

    private void btn_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_nextActionPerformed

    private void cbo_gaDiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_gaDiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_gaDiActionPerformed

    private void rad_motChieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rad_motChieuActionPerformed
        // TODO add your handling code here:
        date_ngayVe.setEnabled(false);
    }//GEN-LAST:event_rad_motChieuActionPerformed

    private void rad_khuHoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rad_khuHoiActionPerformed
        // TODO add your handling code here:
        date_ngayVe.setEnabled(true);
    }//GEN-LAST:event_rad_khuHoiActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_next;
    private javax.swing.JButton btn_timKiem;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbo_gaDen;
    private javax.swing.JComboBox<String> cbo_gaDi;
    private com.toedter.calendar.JDateChooser date_ngayDi;
    private com.toedter.calendar.JDateChooser date_ngayVe;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler10;
    private javax.swing.Box.Filler filler12;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_tieuDeChuyenDi;
    private javax.swing.JLabel lbl_tieuDeChuyenVe;
    private javax.swing.JPanel pnl_chieuDi;
    private javax.swing.JPanel pnl_chieuVe;
    private javax.swing.JPanel pnl_chonChuyenTau;
    private javax.swing.JPanel pnl_dsChuyenDi;
    private javax.swing.JPanel pnl_dsChuyenVe;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle1;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle4;
    private javax.swing.JRadioButton rad_khuHoi;
    private javax.swing.JRadioButton rad_motChieu;
    private gui.custom.RoundedButton roundedButton1;
    private gui.custom.RoundedButton roundedButton2;
    private gui.custom.RoundedButton roundedButton3;
    // End of variables declaration//GEN-END:variables
}
