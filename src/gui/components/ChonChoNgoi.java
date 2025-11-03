/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.components;

import bus.QuanLyDatVe_BUS;
import entity.ChuyenTau;
import entity.Ghe;
import entity.KhoangTau;
import entity.ToaTau;
import gui.custom.RoundedButton;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author CÔNG HOÀNG
 */
public class ChonChoNgoi extends javax.swing.JPanel {
    private final QuanLyDatVe_BUS bus;
    private ChuyenTau chuyenDi;
    private ChuyenTau chuyenVe;
    private boolean isKhuHoi;
    /**
     * Creates new form ChonGhe
     * @param bus
     */
    public ChonChoNgoi(QuanLyDatVe_BUS bus) {
        initComponents();
        this.bus = bus;
    }
    
    
    /**
     * Load và render danh sách ghế
     */
    public void loadDanhSachGhe(ChuyenTau chuyenDi, ChuyenTau chuyenVe, boolean isKhuHoi) 
            throws Exception {
        this.chuyenDi = chuyenDi;
        this.chuyenVe = chuyenVe;
        this.isKhuHoi = isKhuHoi;
    
        // Cập nhật tiêu đề
        String tieuDe = String.format("Chiều đi: ngày %s từ %s đến %s",
            chuyenDi.getThoiGianDi().toLocalDate(),
            chuyenDi.getTuyenDuong().getGaDi().getTenGa(),
            chuyenDi.getTuyenDuong().getGaDen().getTenGa()
        );
        jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createTitledBorder(null, tieuDe, 
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
            javax.swing.border.TitledBorder.DEFAULT_POSITION, 
            new java.awt.Font("Segoe UI", 1, 18), 
            new java.awt.Color(123, 17, 19)),
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
    
        // Render ghế
        renderDanhSachToa(chuyenDi);
    
        // Reset form
        txt_hoTen.setText("");
        txt_sdt.setText("");
        txt_cccd.setText("");
    }

    /**
     * Render danh sách toa tàu
     */
    private void renderDanhSachToa(ChuyenTau chuyen) throws Exception {
        jPanel8.removeAll();
    
        // Lấy danh sách toa theo tàu của chuyến tàu
        ArrayList<ToaTau> dsToa = bus.getDanhSachToaTau(chuyen.getTau());
    
        for (ToaTau toa : dsToa) {
            // Tạo panel toa tương ứng với loại toa
            JPanel panelToa = createPanelTheoLoaiToa(toa, chuyen);
            jPanel8.add(panelToa);
        }
    
        jPanel8.revalidate();
        jPanel8.repaint();
    }

    /**
     * Tạo panel theo loại toa
     */
    private JPanel createPanelTheoLoaiToa(ToaTau toa, ChuyenTau chuyen) throws Exception {
        String loaiToa = toa.getLoaiToa();
    
        if (loaiToa.contains("NGOI_MEM")) {
            return createToaNgoiMem(toa, chuyen);
        } else if (loaiToa.contains("GIUONG_NAM_KHOANG_4")) {
            return createToaGiuongNam4(toa, chuyen);
        } else if (loaiToa.contains("GIUONG_NAM_KHOANG_6")) {
            return createToaGiuongNam6(toa, chuyen);
        }
    
        // Mặc định trả về toa ngồi mềm
        return createToaNgoiMem(toa, chuyen);
    }

    /**
     * Tạo toa ngồi mềm
     */
    private ToaNgoiMem createToaNgoiMem(ToaTau toa, ChuyenTau chuyen) throws Exception {
        ToaNgoiMem panel = new ToaNgoiMem();
    
        // Cập nhật tiêu đề toa
        updateTieuDeToa(panel, toa);
    
        // Load và gán sự kiện cho các button ghế
        setupGheChoToaNgoiMem(panel, toa, chuyen);
    
        return panel;
    }

    /**
     * Tạo toa giường nằm 4
     */
    private ToaGiuongNam4 createToaGiuongNam4(ToaTau toa, ChuyenTau chuyen) throws Exception {
        ToaGiuongNam4 panel = new ToaGiuongNam4();
    
        // Cập nhật tiêu đề
        updateTieuDeToa(panel, toa);
    
        // Setup ghế
        setupGheChoToaGiuongNam4(panel, toa, chuyen);
    
        return panel;
    }

    /**
     * Tạo toa giường nằm 6
     */
    private ToaGiuongNam6 createToaGiuongNam6(ToaTau toa, ChuyenTau chuyen) throws Exception {
        ToaGiuongNam6 panel = new ToaGiuongNam6();
    
        // Cập nhật tiêu đề
        updateTieuDeToa(panel, toa);
    
        // Setup ghế
        setupGheChoToaGiuongNam6(panel, toa, chuyen);

        return panel;
    }

    /**
     * Cập nhật tiêu đề toa
     */
    private void updateTieuDeToa(JPanel panel, ToaTau toa) {
        Component[] components = panel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                Component[] subComps = ((JPanel) comp).getComponents();
                for (Component subComp : subComps) {
                    if (subComp instanceof JPanel) {
                        Component[] labels = ((JPanel) subComp).getComponents();
                        for (Component label : labels) {
                            if (label instanceof JLabel && label.getName() == null) {
                                JLabel lbl = (JLabel) label;
                                String text = lbl.getText();
                                if (text.contains("TOA SỐ")) {
                                    lbl.setText("TOA SỐ " + toa.getSoHieuToa() + ": " + 
                                              formatLoaiToa(toa.getLoaiToa()));
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Format tên loại toa để hiển thị
     */
    private String formatLoaiToa(String loaiToa) {
        return loaiToa.replace("_", " ");
    }

    /**
     * Setup ghế cho toa ngồi mềm
     */
    private void setupGheChoToaNgoiMem(ToaNgoiMem panel, ToaTau toa, ChuyenTau chuyen) 
            throws Exception {
        // Lấy danh sách ghế từ toa tàu qua BUS
        ArrayList<Ghe> dsGhe = bus.getDanhSachGheTheoToa(toa);
    
        // Lấy tất cả RoundedButton trong panel
        ArrayList<RoundedButton> buttons = getAllButtons(panel);
    
        // Map ghế với button
        int gheIndex = 0;
        for (RoundedButton btn : buttons) {
            if (gheIndex >= dsGhe.size()) {
                // Ẩn button nếu không có ghế tương ứng
                btn.setVisible(false);
                continue;
            }
            
            Ghe ghe = dsGhe.get(gheIndex);
            
            // Set text là số ghế
            btn.setText(String.valueOf(ghe.getSoGhe()));
            
            // Kiểm tra trạng thái ghế
            if (bus.isGheDaDat(ghe, chuyen)) {
                // Ghế đã đặt - màu xám, disabled
                btn.setBackground(new java.awt.Color(146, 146, 146));
                btn.setEnabled(false);
            } else if (bus.isDaChonGhe(ghe)) {
                // Ghế đang chọn - màu đỏ
                btn.setBackground(new java.awt.Color(252, 90, 90));
                btn.addActionListener(e -> handleChonGhe(btn, ghe));
            } else {
                // Ghế trống - màu trắng
                btn.setBackground(java.awt.Color.WHITE);
                btn.addActionListener(e -> handleChonGhe(btn, ghe));
            }
            
            gheIndex++;
        }
    }

    /**
     * Setup ghế cho toa giường nằm 4
     */
    private void setupGheChoToaGiuongNam4(ToaGiuongNam4 panel, ToaTau toa, ChuyenTau chuyen) 
            throws Exception {
        // Lấy danh sách khoang của toa
        ArrayList<KhoangTau> dsKhoang = bus.getDanhSachKhoangTheoToa(toa);
        
        // Lấy tất cả button
        ArrayList<RoundedButton> buttons = getAllButtons(panel);
        
        int buttonIndex = 0;
        
        // Duyệt qua từng khoang
        for (KhoangTau khoang : dsKhoang) {
            // Lấy danh sách ghế của khoang
            ArrayList<Ghe> dsGhe = bus.getDanhSachGheTheoKhoang(khoang);
            
            // Map ghế với button
            for (Ghe ghe : dsGhe) {
                if (buttonIndex >= buttons.size()) break;
                
                RoundedButton btn = buttons.get(buttonIndex);
                btn.setText(String.valueOf(ghe.getSoGhe()));
                
                if (bus.isGheDaDat(ghe, chuyen)) {
                    btn.setBackground(new java.awt.Color(146, 146, 146));
                    btn.setEnabled(false);
                } else if (bus.isDaChonGhe(ghe)) {
                    btn.setBackground(new java.awt.Color(252, 90, 90));
                    btn.addActionListener(e -> handleChonGhe(btn, ghe));
                } else {
                    btn.setBackground(java.awt.Color.WHITE);
                    btn.addActionListener(e -> handleChonGhe(btn, ghe));
                }
                
                buttonIndex++;
            }
        }
        
        // Ẩn các button thừa
        while (buttonIndex < buttons.size()) {
            buttons.get(buttonIndex).setVisible(false);
            buttonIndex++;
        }
    }

/**
     * Setup ghế cho toa giường nằm 6
     */
    private void setupGheChoToaGiuongNam6(ToaGiuongNam6 panel, ToaTau toa, ChuyenTau chuyen) 
            throws Exception {
        // Lấy danh sách khoang của toa
        ArrayList<KhoangTau> dsKhoang = bus.getDanhSachKhoangTheoToa(toa);
        
        // Lấy tất cả button
        ArrayList<RoundedButton> buttons = getAllButtons(panel);
        
        int buttonIndex = 0;
        
        // Duyệt qua từng khoang
        for (KhoangTau khoang : dsKhoang) {
            // Lấy danh sách ghế của khoang
            ArrayList<Ghe> dsGhe = bus.getDanhSachGheTheoKhoang(khoang);
            
            // Map ghế với button
            for (Ghe ghe : dsGhe) {
                if (buttonIndex >= buttons.size()) break;
                
                RoundedButton btn = buttons.get(buttonIndex);
                btn.setText(String.valueOf(ghe.getSoGhe()));
                
                if (bus.isGheDaDat(ghe, chuyen)) {
                    btn.setBackground(new java.awt.Color(146, 146, 146));
                    btn.setEnabled(false);
                } else if (bus.isDaChonGhe(ghe)) {
                    btn.setBackground(new java.awt.Color(252, 90, 90));
                    btn.addActionListener(e -> handleChonGhe(btn, ghe));
                } else {
                    btn.setBackground(java.awt.Color.WHITE);
                    btn.addActionListener(e -> handleChonGhe(btn, ghe));
                }
                
                buttonIndex++;
            }
        }
        
        // Ẩn các button thừa
        while (buttonIndex < buttons.size()) {
            buttons.get(buttonIndex).setVisible(false);
            buttonIndex++;
        }
    }

    /**
     * Lấy tất cả button trong panel
     */
    private ArrayList<RoundedButton> getAllButtons(JPanel panel) {
        ArrayList<RoundedButton> buttons = new ArrayList<>();
        getAllButtonsRecursive(panel, buttons);
        return buttons;
    }

    private void getAllButtonsRecursive(Container container, ArrayList<RoundedButton> buttons) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof RoundedButton) {
                buttons.add((RoundedButton) comp);
            } else if (comp instanceof Container) {
                getAllButtonsRecursive((Container) comp, buttons);
            }
        }
    }

    /**
     * Xử lý khi chọn ghế
     */
    private void handleChonGhe(RoundedButton btn, Ghe ghe) {
        if (bus.isDaChonGhe(ghe)) {
            // Bỏ chọn
            btn.setBackground(java.awt.Color.WHITE);
            bus.xoaGheDaChon(ghe);
        } else {
            // Chọn ghế
            btn.setBackground(new java.awt.Color(252, 90, 90));
            bus.themGheDaChon(ghe);
        }
    }
    
    public JButton next() {
        return btn_next;
    }
    
    public JButton previous() {
        return btn_previous;
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
        pnl_chonChoNgoi = new javax.swing.JPanel();
        pnl_nhapThongTin = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pnl_thongTin = new javax.swing.JPanel();
        pnl_thongTinKhachHang = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txt_hoTen = new javax.swing.JTextField();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        jPanel14 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txt_sdt = new javax.swing.JTextField();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jLabel6 = new javax.swing.JLabel();
        txt_cccd = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        roundedButton4 = new gui.custom.RoundedButton();
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jLabel3 = new javax.swing.JLabel();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(40, 0), new java.awt.Dimension(40, 0), new java.awt.Dimension(30, 32767));
        roundedButton5 = new gui.custom.RoundedButton();
        filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jLabel11 = new javax.swing.JLabel();
        filler10 = new javax.swing.Box.Filler(new java.awt.Dimension(40, 0), new java.awt.Dimension(40, 0), new java.awt.Dimension(30, 32767));
        roundedButton6 = new gui.custom.RoundedButton();
        filler11 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jLabel12 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jPanel15 = new javax.swing.JPanel();
        btn_previous = new javax.swing.JButton();
        btn_next = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        jPanel11.setPreferredSize(new java.awt.Dimension(915, 100));
        jPanel11.setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(300, 60));

        rSMaterialButtonCircle1.setBackground(new java.awt.Color(184, 184, 184));
        rSMaterialButtonCircle1.setForeground(new java.awt.Color(0, 0, 0));
        rSMaterialButtonCircle1.setText("1");
        rSMaterialButtonCircle1.setFont(new java.awt.Font("Roboto Medium", 1, 17)); // NOI18N
        rSMaterialButtonCircle1.setPreferredSize(new java.awt.Dimension(40, 40));
        rSMaterialButtonCircle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle1ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonCircle1);

        roundedButton1.setBackground(new java.awt.Color(212, 212, 212));
        roundedButton1.setForeground(new java.awt.Color(0, 0, 0));
        roundedButton1.setText("Chọn chuyến tàu");
        roundedButton1.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        roundedButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        roundedButton1.setPreferredSize(new java.awt.Dimension(230, 58));
        jPanel1.add(roundedButton1);

        jPanel11.add(jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(300, 60));

        rSMaterialButtonCircle2.setBackground(new java.awt.Color(123, 17, 19));
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

        roundedButton2.setBackground(new java.awt.Color(123, 17, 19));
        roundedButton2.setForeground(new java.awt.Color(255, 255, 255));
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

        pnl_chonChoNgoi.setPreferredSize(new java.awt.Dimension(600, 200));
        pnl_chonChoNgoi.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        pnl_nhapThongTin.setBackground(new java.awt.Color(255, 255, 255));
        pnl_nhapThongTin.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhập thông tin", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(123, 17, 19))); // NOI18N
        pnl_nhapThongTin.setPreferredSize(new java.awt.Dimension(280, 153));
        pnl_nhapThongTin.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBorder(null);

        pnl_thongTin.setBackground(new java.awt.Color(255, 255, 255));
        pnl_thongTin.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_thongTin.setLayout(new javax.swing.BoxLayout(pnl_thongTin, javax.swing.BoxLayout.Y_AXIS));

        pnl_thongTinKhachHang.setBackground(new java.awt.Color(255, 255, 255));
        pnl_thongTinKhachHang.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(123, 17, 19)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10))); // NOI18N
        pnl_thongTinKhachHang.setMaximumSize(new java.awt.Dimension(32767, 160));
        pnl_thongTinKhachHang.setMinimumSize(new java.awt.Dimension(335, 160));
        pnl_thongTinKhachHang.setPreferredSize(new java.awt.Dimension(288, 160));
        pnl_thongTinKhachHang.setLayout(new javax.swing.BoxLayout(pnl_thongTinKhachHang, javax.swing.BoxLayout.Y_AXIS));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.LINE_AXIS));

        jLabel4.setText("Họ tên:");
        jLabel4.setPreferredSize(new java.awt.Dimension(50, 16));
        jPanel13.add(jLabel4);

        txt_hoTen.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        jPanel13.add(txt_hoTen);

        pnl_thongTinKhachHang.add(jPanel13);
        pnl_thongTinKhachHang.add(filler4);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.LINE_AXIS));

        jLabel7.setText("SĐT:");
        jLabel7.setPreferredSize(new java.awt.Dimension(50, 16));
        jPanel14.add(jLabel7);

        txt_sdt.setMaximumSize(txt_hoTen.getMaximumSize());
        txt_sdt.setPreferredSize(txt_hoTen.getPreferredSize());
        jPanel14.add(txt_sdt);
        jPanel14.add(filler6);

        jLabel6.setText("CCCD:");
        jLabel6.setPreferredSize(new java.awt.Dimension(60, 16));
        jPanel14.add(jLabel6);

        txt_cccd.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        jPanel14.add(txt_cccd);

        pnl_thongTinKhachHang.add(jPanel14);

        pnl_thongTin.add(pnl_thongTinKhachHang);

        jScrollPane2.setViewportView(pnl_thongTin);

        pnl_nhapThongTin.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        pnl_chonChoNgoi.add(pnl_nhapThongTin);

        add(pnl_chonChoNgoi, java.awt.BorderLayout.LINE_END);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chiều đi: ngày 02/11/2025 từ Sài Gòn đến Hà Nội", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(123, 17, 19)), javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0))); // NOI18N
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(756, 100));
        jPanel5.setLayout(new java.awt.GridLayout(2, 1));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(123, 17, 19));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("CHỌN VỊ TRÍ");
        jPanel5.add(jLabel2);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        roundedButton4.setBackground(new java.awt.Color(252, 90, 90));
        roundedButton4.setBorder(null);
        roundedButton4.setToolTipText("");
        roundedButton4.setEnabled(false);
        roundedButton4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        roundedButton4.setMaximumSize(new java.awt.Dimension(40, 40));
        roundedButton4.setMinimumSize(new java.awt.Dimension(40, 40));
        roundedButton4.setPreferredSize(new java.awt.Dimension(40, 40));
        jPanel6.add(roundedButton4);
        jPanel6.add(filler8);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Ghế đang chọn");
        jPanel6.add(jLabel3);
        jPanel6.add(filler7);

        roundedButton5.setBackground(new java.awt.Color(146, 146, 146));
        roundedButton5.setToolTipText("");
        roundedButton5.setEnabled(false);
        roundedButton5.setMaximumSize(new java.awt.Dimension(40, 40));
        roundedButton5.setMinimumSize(new java.awt.Dimension(40, 40));
        roundedButton5.setPreferredSize(new java.awt.Dimension(40, 40));
        jPanel6.add(roundedButton5);
        jPanel6.add(filler9);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setText("Ghế đã đặt");
        jPanel6.add(jLabel11);
        jPanel6.add(filler10);

        roundedButton6.setToolTipText("");
        roundedButton6.setEnabled(false);
        roundedButton6.setMaximumSize(new java.awt.Dimension(40, 40));
        roundedButton6.setMinimumSize(new java.awt.Dimension(40, 40));
        roundedButton6.setPreferredSize(new java.awt.Dimension(40, 40));
        jPanel6.add(roundedButton6);
        jPanel6.add(filler11);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setText("Ghế còn trống");
        jPanel6.add(jLabel12);

        jPanel5.add(jPanel6);

        jPanel3.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel48.setBackground(new java.awt.Color(255, 255, 255));
        jPanel48.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        jPanel48.setPreferredSize(new java.awt.Dimension(681, 50));
        jPanel48.setLayout(new javax.swing.BoxLayout(jPanel48, javax.swing.BoxLayout.LINE_AXIS));

        jPanel49.setBackground(new java.awt.Color(255, 255, 255));
        jPanel49.setLayout(new java.awt.BorderLayout());

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("TOA SỐ 2: GIƯỜNG NẰM KHOANG 6 ĐIỀU HÒA");
        jPanel49.add(jLabel5, java.awt.BorderLayout.CENTER);

        jCheckBox3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox3.setText("  Chọn tất cả");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });
        jPanel49.add(jCheckBox3, java.awt.BorderLayout.LINE_START);

        jPanel48.add(jPanel49);

        jPanel4.add(jPanel48, java.awt.BorderLayout.PAGE_START);

        jPanel8.add(jPanel4);

        jScrollPane1.setViewportView(jPanel8);

        jPanel7.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setPreferredSize(new java.awt.Dimension(100, 60));
        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 5));

        btn_previous.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_previous.setText("Quay lại");
        btn_previous.setPreferredSize(new java.awt.Dimension(140, 50));
        btn_previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_previousActionPerformed(evt);
            }
        });
        jPanel15.add(btn_previous);

        btn_next.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_next.setText("Tiếp tục");
        btn_next.setPreferredSize(new java.awt.Dimension(140, 50));
        btn_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nextActionPerformed(evt);
            }
        });
        jPanel15.add(btn_next);

        add(jPanel15, java.awt.BorderLayout.PAGE_END);
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

    private void btn_previousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_previousActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_previousActionPerformed

    private void btn_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_nextActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_next;
    private javax.swing.JButton btn_previous;
    private javax.swing.Box.Filler filler10;
    private javax.swing.Box.Filler filler11;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler8;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnl_chonChoNgoi;
    private javax.swing.JPanel pnl_nhapThongTin;
    private javax.swing.JPanel pnl_thongTin;
    private javax.swing.JPanel pnl_thongTinKhachHang;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle1;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle4;
    private gui.custom.RoundedButton roundedButton1;
    private gui.custom.RoundedButton roundedButton2;
    private gui.custom.RoundedButton roundedButton3;
    private gui.custom.RoundedButton roundedButton4;
    private gui.custom.RoundedButton roundedButton5;
    private gui.custom.RoundedButton roundedButton6;
    private javax.swing.JTextField txt_cccd;
    private javax.swing.JTextField txt_hoTen;
    private javax.swing.JTextField txt_sdt;
    // End of variables declaration//GEN-END:variables
}
