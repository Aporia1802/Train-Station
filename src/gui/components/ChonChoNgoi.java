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
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utils.FormatUtil;

/**
 *
 * @author CÔNG HOÀNG
 */
public class ChonChoNgoi extends javax.swing.JPanel {
    private final QuanLyDatVe_BUS bus;
    ChuyenTau chuyenDi;
    ChuyenTau chuyenVe;
    private boolean isKhuHoi;
    private String maChuyenHienTai = "";
    private boolean dangXemChieuVe = false; // theo dõi đang xem chiều nào
    private javax.swing.JLabel lbl_headerChieuDi;
    private javax.swing.JLabel lbl_headerChieuVe;
    private final Map<String, ChuyenTau> gheToChuyenMap = new HashMap<>();
    private final Map<String, PanelVeData> thongTinDaNhapMap = new HashMap<>();
    private boolean autoUpdateUI = true;
    
    /**
     * Creates new form ChonGhe
     * @param bus
     */
    public ChonChoNgoi(QuanLyDatVe_BUS bus) {
        initComponents();
        this.bus = bus;
        initEvent();
    }
    
    /**
    * Lưu lại thông tin đã nhập trước khi xóa panel
    */
   private void luuThongTinDaNhap() {
       Component[] components = pnl_thongTin.getComponents();
       for (Component comp : components) {
           if (comp instanceof ThongTinVe) {
               ThongTinVe panelThongTin = (ThongTinVe) comp;
               ThongTinVe.ThongTinHanhKhach thongTin = panelThongTin.getThongTin();

               // Lưu cả tiêu đề hiện tại
               String tieuDeHienTai = panelThongTin.getTieuDe();
               String maGhe = thongTin.getMaGhe();

               thongTinDaNhapMap.put(maGhe, new PanelVeData(tieuDeHienTai, thongTin));
           }
       }
   }
    
    // Class inner để lưu đầy đủ thông tin
    private static class PanelVeData {
        String tieuDe;
        ThongTinVe.ThongTinHanhKhach thongTin;

        public PanelVeData(String tieuDe, ThongTinVe.ThongTinHanhKhach thongTin) {
            this.tieuDe = tieuDe;
            this.thongTin = thongTin;
        }
    }

    // HÀM CẬP NHẬT 1 LẦN
    private void capNhatToanBoGiaoDien() {
        if (!autoUpdateUI) return;
        try {
            khoiPhucThongTinVe();
            renderDanhSachToa(dangXemChieuVe ? chuyenVe : chuyenDi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
    * Ghi nhận ghế thuộc chuyến nào khi thêm vào
    */
    private void themGheDaChon(Ghe ghe, ChuyenTau chuyen) {
        bus.themGheDaChon(ghe);
        gheToChuyenMap.put(ghe.getMaGhe(), chuyen);
    }

    /**
     * Xóa ghế và thông tin mapping
     */
    void xoaGheDaChon(Ghe ghe) {
        bus.xoaGheDaChon(ghe);
        gheToChuyenMap.remove(ghe.getMaGhe());
    }

    /**
     * Xác định ghế thuộc chuyến nào - DỰA VÀO MAP
     */
    boolean isGheThuocChuyenVe(Ghe ghe) {
        ChuyenTau chuyenCuaGhe = gheToChuyenMap.get(ghe.getMaGhe());
        if (chuyenCuaGhe == null || chuyenVe == null) {
            return false;
        }
        return chuyenCuaGhe.getMaChuyenTau().equals(chuyenVe.getMaChuyenTau());
    }
    
    private void initThongTinPanels() {
        // CHIỀU ĐI
        lbl_headerChieuDi = new JLabel("Chiều đi");
        lbl_headerChieuDi.setFont(new java.awt.Font("Segoe UI", 1, 16));
        lbl_headerChieuDi.setForeground(new java.awt.Color(123, 17, 19));
        lbl_headerChieuDi.setBackground(Color.WHITE);
        lbl_headerChieuDi.setOpaque(true);

        lbl_headerChieuDi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_headerChieuDi.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(123, 17, 19)),
            javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 15)
        ));
        lbl_headerChieuDi.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 40));
        lbl_headerChieuDi.setAlignmentX(Integer.MAX_VALUE);

        // CHIỀU VỀ
        lbl_headerChieuVe = new JLabel("Chiều về");
        lbl_headerChieuVe.setFont(new java.awt.Font("Segoe UI", 1, 16));
        lbl_headerChieuVe.setForeground(new java.awt.Color(123, 17, 19));
        lbl_headerChieuVe.setBackground(Color.WHITE);
        lbl_headerChieuVe.setOpaque(true);

        lbl_headerChieuVe.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_headerChieuVe.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(123, 17, 19)),
            javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 15)
        ));
        lbl_headerChieuVe.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 40));
        lbl_headerChieuVe.setAlignmentX(Integer.MAX_VALUE); 
    }
    
    private void initEvent() {
        btn_chuyenVe.addActionListener(e -> {
            try {
                if (!dangXemChieuVe && chuyenVe != null && isKhuHoi) {
                //  SANG CHIỀU VỀ
                    dangXemChieuVe = true;
                    btn_chuyenVe.setText("Chọn chiều đi");

                //  CẬP NHẬT TIÊU ĐỀ CHÍNH
                    String tieuDe = String.format("Chiều về: ngày %s từ %s → %s",
                        FormatUtil.formatDateTime(chuyenVe.getThoiGianDi()),
                        chuyenVe.getTuyenDuong().getGaDi().getTenGa(),
                        chuyenVe.getTuyenDuong().getGaDen().getTenGa()
                    );
                    
                    jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                                    javax.swing.BorderFactory.createTitledBorder(null, tieuDe,
                                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                    new java.awt.Font("Segoe UI", 1, 18),
                                    new java.awt.Color(123, 17, 19)),
                                    javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0)
                     ));

                    renderDanhSachToa(chuyenVe);

                } else {
                // QUAY LẠI CHIỀU ĐI
                   dangXemChieuVe = false;
                   btn_chuyenVe.setText("Chọn chuyến về");

                // CẬP NHẬT LẠI TIÊU ĐỀ CHIỀU ĐI
                    String tieuDeDi = String.format("Chiều đi: ngày %s từ %s → %s",
                        FormatUtil.formatDateTime(chuyenDi.getThoiGianDi()),
                        chuyenDi.getTuyenDuong().getGaDi().getTenGa(),
                        chuyenDi.getTuyenDuong().getGaDen().getTenGa()
                    );
                
                    jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                                  javax.swing.BorderFactory.createTitledBorder(null, tieuDeDi,
                                  javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                  javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                  new java.awt.Font("Segoe UI", 1, 18),
                                  new java.awt.Color(123, 17, 19)),
                                  javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0)
                    ));

                    loadDanhSachGhe(chuyenDi, chuyenVe, isKhuHoi);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
    
    /**
     * Load và render danh sách ghế
     */
    public void loadDanhSachGhe(ChuyenTau chuyenDi, ChuyenTau chuyenVe, boolean isKhuHoi) throws Exception {
        this.chuyenDi = chuyenDi;
        this.chuyenVe = chuyenVe;
        this.isKhuHoi = isKhuHoi;
        
        // Reset trạng thái
        dangXemChieuVe = false;
        btn_chuyenVe.setText("Chọn chuyến về");
        btn_chuyenVe.setVisible(isKhuHoi && chuyenVe != null);
        
        // CẬP NHẬT TIÊU ĐỀ 
        String tieuDeDi = String.format("Chiều đi: ngày %s từ %s → %s",
            FormatUtil.formatDateTime(chuyenDi.getThoiGianDi()),
            chuyenDi.getTuyenDuong().getGaDi().getTenGa(),
            chuyenDi.getTuyenDuong().getGaDen().getTenGa()
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createTitledBorder(null, tieuDeDi,
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Segoe UI", 1, 18),
                new java.awt.Color(123, 17, 19)),
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        renderDanhSachToa(chuyenDi);
    }
    
    /**
     * Render danh sách toa tàu
     */
    private void renderDanhSachToa(ChuyenTau chuyen) throws Exception {
        jPanel8.removeAll();

        ArrayList<ToaTau> dsToa = bus.getDanhSachToaTau(chuyen.getTau());

        for (int i = 0; i < dsToa.size(); i++) {
            ToaTau toa = dsToa.get(i);

            JPanel panelToa = createPanelTheoLoaiToa(toa, chuyen);
            jPanel8.add(panelToa);

            if (i < dsToa.size() - 1) {
                javax.swing.Box.Filler filler = new javax.swing.Box.Filler(
                    new java.awt.Dimension(0, 20), 
                    new java.awt.Dimension(0, 20), 
                    new java.awt.Dimension(32767, 20)
                );
                jPanel8.add(filler);
            }
        }

        jPanel8.revalidate();
        jPanel8.repaint();  

        // KHÔI PHỤC LẠI THÔNG TIN VÉ SAU KHI RENDER
        khoiPhucThongTinVe();
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
        
         // Setup checkbox chọn tất cả
        setupChonTatCaCheckbox(panel, toa, chuyen);
        
        syncCheckboxChonTatCa(panel, toa, chuyen);
    
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
        
         // Setup checkbox chọn tất cả
        setupChonTatCaCheckbox(panel, toa, chuyen);
        
        syncCheckboxChonTatCa(panel, toa, chuyen);
    
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
        
         // Setup checkbox chọn tất cả
        setupChonTatCaCheckbox(panel, toa, chuyen);
        
        syncCheckboxChonTatCa(panel, toa, chuyen);

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
        if (loaiToa.contains("NGOI_MEM")) {
            return "NGỒI MỀM ĐIỀU HÒA";
        } else if (loaiToa.contains("GIUONG_NAM_KHOANG_4")) {
            return "GIƯỜNG NẰM KHOANG 4 ĐIỀU HÒA";
        } else if (loaiToa.contains("GIUONG_NAM_KHOANG_6")) {
            return "GIƯỜNG NẰM KHOANG 6 ĐIỀU HÒA";
        }
        return null;
    }

    /**
     * Setup ghế cho toa ngồi mềm
     */
    
    private void setupGheChoToaNgoiMem(ToaNgoiMem panel, ToaTau toa, ChuyenTau chuyen) throws Exception {
        ArrayList<Ghe> dsGhe = bus.getDanhSachGheTheoToa(toa);
        ArrayList<RoundedButton> buttons = getAllButtons(panel);

        int gheIndex = 0;
        int buttonIndex = 0;

        for (RoundedButton btn : buttons) {
            // Xóa tất cả listener cũ
            for (java.awt.event.ActionListener al : btn.getActionListeners()) {
                btn.removeActionListener(al);
            }

            // VÁCH NGĂN
            if (buttonIndex == 32 || buttonIndex == 33) {
                btn.setBackground(new java.awt.Color(146, 146, 146));
                btn.setEnabled(false);
                btn.setVisible(true);
                btn.setText("");
                buttonIndex++;
                continue;
            }

            // HẾT GHẾ → ẨN
            if (gheIndex >= dsGhe.size()) {
                btn.setVisible(false);
                buttonIndex++;
                continue;
            }

            // GÁN GHẾ THẬT
            Ghe ghe = dsGhe.get(gheIndex++);
            btn.setText(String.valueOf(ghe.getSoGhe()));
            btn.setVisible(true);

            if (bus.isGheDaDat(ghe, chuyen)) {
                // Ghế đã được đặt bởi người khác
                btn.setBackground(new java.awt.Color(146, 146, 146));
                btn.setEnabled(false);
            } else if (bus.isDaChonGhe(ghe)) {
                // Ghế đã được user chọn trước đó - KHÔI PHỤC TRẠNG THÁI
                btn.setBackground(new java.awt.Color(252, 90, 90));
                btn.setEnabled(true);
                btn.addActionListener(e -> handleChonGhe(btn, ghe));

                // ĐẢM BẢO CÓ TRONG MAP (nếu chưa có)
                if (!gheToChuyenMap.containsKey(ghe.getMaGhe())) {
                    gheToChuyenMap.put(ghe.getMaGhe(), chuyen);
                }
            } else {
                // Ghế trống
                btn.setBackground(java.awt.Color.WHITE);
                btn.setEnabled(true);
                btn.addActionListener(e -> handleChonGhe(btn, ghe));
            }

            buttonIndex++;
        }
    }

    /**
     * Setup ghế cho toa giường nằm 4
     */
    
    private void setupGheChoToaGiuongNam4(ToaGiuongNam4 panel, ToaTau toa, ChuyenTau chuyen) throws Exception {
        ArrayList<KhoangTau> dsKhoang = bus.getDanhSachKhoangTheoToa(toa);
        ArrayList<RoundedButton> buttons = getAllButtons(panel);
        int buttonIndex = 0;

        for (KhoangTau khoang : dsKhoang) {
            ArrayList<Ghe> dsGhe = bus.getDanhSachGheTheoKhoang(khoang);
            dsGhe.sort(Comparator.comparingInt(Ghe::getSoGhe));

            int gheMoiTang = 2;
            int soTang = 2;

            for (int tang = soTang - 1; tang >= 0; tang--) {
                int start = tang * gheMoiTang;
                int end = Math.min(start + gheMoiTang, dsGhe.size());
                List<Ghe> gheTang = dsGhe.subList(start, end);

                for (Ghe ghe : gheTang) {
                    if (buttonIndex >= buttons.size()) break;

                    RoundedButton btn = buttons.get(buttonIndex);

                    // Xóa listener cũ
                    for (java.awt.event.ActionListener al : btn.getActionListeners()) {
                        btn.removeActionListener(al);
                    }

                    btn.setText(String.valueOf(ghe.getSoGhe()));
                    btn.setVisible(true);

                    if (bus.isGheDaDat(ghe, chuyen)) {
                        btn.setBackground(new java.awt.Color(146, 146, 146));
                        btn.setEnabled(false);
                    } else if (bus.isDaChonGhe(ghe)) {
                        // KHÔI PHỤC trạng thái đã chọn
                        btn.setBackground(new java.awt.Color(252, 90, 90));
                        btn.setEnabled(true);
                        btn.addActionListener(e -> handleChonGhe(btn, ghe));

                        // ĐẢM BẢO CÓ TRONG MAP
                        if (!gheToChuyenMap.containsKey(ghe.getMaGhe())) {
                            gheToChuyenMap.put(ghe.getMaGhe(), chuyen);
                        }
                    } else {
                        btn.setBackground(java.awt.Color.WHITE);
                        btn.setEnabled(true);
                        btn.addActionListener(e -> handleChonGhe(btn, ghe));
                    }

                    buttonIndex++;
                }
            }
        }

        // Ẩn các nút thừa
        while (buttonIndex < buttons.size()) {
            buttons.get(buttonIndex).setVisible(false);
            buttonIndex++;
        }
    }


    /**
     * Setup ghế cho toa giường nằm 6
     */
    
    private void setupGheChoToaGiuongNam6(ToaGiuongNam6 panel, ToaTau toa, ChuyenTau chuyen) throws Exception {
        ArrayList<KhoangTau> dsKhoang = bus.getDanhSachKhoangTheoToa(toa);
        ArrayList<RoundedButton> buttons = getAllButtons(panel);
        int buttonIndex = 0;

        for (KhoangTau khoang : dsKhoang) {
            ArrayList<Ghe> dsGhe = bus.getDanhSachGheTheoKhoang(khoang);
            dsGhe.sort(Comparator.comparingInt(Ghe::getSoGhe));

            int gheMoiTang = 2;
            int soTang = (int) Math.ceil(dsGhe.size() / (double) gheMoiTang);

            for (int tang = soTang - 1; tang >= 0; tang--) {
                int start = tang * gheMoiTang;
                int end = Math.min(start + gheMoiTang, dsGhe.size());
                List<Ghe> gheTang = dsGhe.subList(start, end);

                for (Ghe ghe : gheTang) {
                    if (buttonIndex >= buttons.size()) break;

                    RoundedButton btn = buttons.get(buttonIndex);

                    // Xóa listener cũ
                    for (java.awt.event.ActionListener al : btn.getActionListeners()) {
                        btn.removeActionListener(al);
                    }

                    btn.setText(String.valueOf(ghe.getSoGhe()));
                    btn.setVisible(true);

                    if (bus.isGheDaDat(ghe, chuyen)) {
                        btn.setBackground(new java.awt.Color(146, 146, 146));
                        btn.setEnabled(false);
                    } else if (bus.isDaChonGhe(ghe)) {
                        // KHÔI PHỤC trạng thái đã chọn
                        btn.setBackground(new java.awt.Color(252, 90, 90));
                        btn.setEnabled(true);
                        btn.addActionListener(e -> handleChonGhe(btn, ghe));

                        // ĐẢM BẢO CÓ TRONG MAP
                        if (!gheToChuyenMap.containsKey(ghe.getMaGhe())) {
                            gheToChuyenMap.put(ghe.getMaGhe(), chuyen);
                        }
                    } else {
                        btn.setBackground(java.awt.Color.WHITE);
                        btn.setEnabled(true);
                        btn.addActionListener(e -> handleChonGhe(btn, ghe));
                    }

                    buttonIndex++;
                }
            }
        }

        // Ẩn các nút thừa
        while (buttonIndex < buttons.size()) {
            buttons.get(buttonIndex).setVisible(false);
            buttonIndex++;
        }
    }
    
    /**
    * Khôi phục lại các panel thông tin vé từ danh sách ghế đã chọn
    * Gọi sau khi đổi chuyến để hiển thị lại thông tin
    */
   private void khoiPhucThongTinVe() {
       luuThongTinDaNhap();
       // Xóa tất cả trừ panel khách hàng
       pnl_thongTin.removeAll();
       pnl_thongTin.add(pnl_thongTinKhachHang);
       pnl_thongTin.add(javax.swing.Box.createVerticalStrut(10));

       initThongTinPanels();

       // Phân loại ghế theo chuyến
       java.util.ArrayList<entity.Ghe> dsGheDi = new java.util.ArrayList<>();
       java.util.ArrayList<entity.Ghe> dsGheVe = new java.util.ArrayList<>();

       for (entity.Ghe ghe : bus.getDanhSachGheDaChon()) {
           if (isGheThuocChuyenVe(ghe)) {
               dsGheVe.add(ghe);
           } else {
               dsGheDi.add(ghe);
           }
       }

       // Thêm header và ghế chiều đi
       if (!dsGheDi.isEmpty()) {
           pnl_thongTin.add(javax.swing.Box.createVerticalStrut(8));
           pnl_thongTin.add(lbl_headerChieuDi);
           pnl_thongTin.add(javax.swing.Box.createVerticalStrut(10));

           for (entity.Ghe ghe : dsGheDi) {
               themPanelThongTinVeSauKhiKhoiPhuc(ghe, chuyenDi);
           }
       }

       // Thêm header và ghế chiều về
       if (!dsGheVe.isEmpty() && chuyenVe != null) {
           pnl_thongTin.add(javax.swing.Box.createVerticalStrut(8));
           pnl_thongTin.add(lbl_headerChieuVe);
           pnl_thongTin.add(javax.swing.Box.createVerticalStrut(10));

           for (entity.Ghe ghe : dsGheVe) {
               themPanelThongTinVeSauKhiKhoiPhuc(ghe, chuyenVe);
           }
       }

       pnl_thongTin.revalidate();
       pnl_thongTin.repaint();
   }
   
   /**
    * Thêm panel thông tin vé (dùng khi khôi phục)
    */
   private void themPanelThongTinVeSauKhiKhoiPhuc(Ghe ghe, ChuyenTau chuyen) {
       String tieuDe;
       String maGhe = ghe.getMaGhe();

       // KIỂM TRA XEM ĐÃ CÓ TIÊU ĐỀ LƯU TRƯỚC ĐÓ CHƯA
       if (thongTinDaNhapMap.containsKey(maGhe)) {
           // SỬ DỤNG TIÊU ĐỀ ĐÃ LƯU
           tieuDe = thongTinDaNhapMap.get(maGhe).tieuDe;
       } else {
           // TẠO TIÊU ĐỀ MỚI
           String tenTau = chuyen.getTau().getTenTau(); 
           tieuDe = String.format("%s %s → %s - %s - Toa %d - Ghế %d",
               tenTau.substring(tenTau.lastIndexOf(" ") + 1),
               chuyen.getTuyenDuong().getGaDi().getTenGa(),
               chuyen.getTuyenDuong().getGaDen().getTenGa(),
               utils.FormatUtil.formatDateTime(chuyen.getThoiGianDi()),
               ghe.getKhoangTau().getToaTau().getSoHieuToa(),
               ghe.getSoGhe()
           );
       }

       gui.components.ThongTinVe panelThongTin = new gui.components.ThongTinVe(tieuDe, ghe, chuyen);
       panelThongTin.setName("THONGTIN_" + ghe.getMaGhe());

       // KHÔI PHỤC THÔNG TIN ĐÃ NHẬP (NẾU CÓ)
       if (thongTinDaNhapMap.containsKey(maGhe)) {
           ThongTinVe.ThongTinHanhKhach thongTinCu = thongTinDaNhapMap.get(maGhe).thongTin;
           panelThongTin.setThongTin(thongTinCu);
       }

       pnl_thongTin.add(panelThongTin);
       pnl_thongTin.add(javax.swing.Box.createVerticalStrut(10));
   }

    /**
     * Lấy tất cả button trong panel
     */
    
    private ArrayList<RoundedButton> getAllButtons(JPanel panel) {
        ArrayList<RoundedButton> buttons = new ArrayList<>();
        getAllButtonsRecursive(panel, buttons);
        return buttons;
    }

    void getAllButtonsRecursive(Container container, ArrayList<RoundedButton> buttons) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof RoundedButton) {
                buttons.add((RoundedButton) comp);
            } else if (comp instanceof Container) {
                getAllButtonsRecursive((Container) comp, buttons);
            }
        }
    }

    /**
    * Xử lý khi chọn ghế - CẬP NHẬT
    */
    
    void handleChonGhe(RoundedButton btn, Ghe ghe) {
        if (bus.isDaChonGhe(ghe)) {
            // Bỏ chọn
            btn.setBackground(java.awt.Color.WHITE);
            xoaGheDaChon(ghe); // Dùng method mới
            xoaPanelThongTinVe(ghe);
        } else {
            // XÁC ĐỊNH CHUYẾN DỰA VÀO ĐANG XEM CHIỀU NÀO
            ChuyenTau chuyenDangXem = dangXemChieuVe ? chuyenVe : chuyenDi;

            btn.setBackground(new java.awt.Color(252, 90, 90));
            themGheDaChon(ghe, chuyenDangXem); // Truyền cả chuyến tàu
            themPanelThongTinVe(ghe, chuyenDangXem);
        }

        // ĐỒNG BỘ CHECKBOX
        try {
            JPanel toaPanel = (JPanel) btn.getParent().getParent().getParent().getParent();
            syncCheckboxChonTatCa(toaPanel, 
                                 ghe.getKhoangTau().getToaTau(), 
                                 dangXemChieuVe ? chuyenVe : chuyenDi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
    * Thêm panel thông tin vé vào form
    */
    
    private void themPanelThongTinVe(Ghe ghe, ChuyenTau chuyen) {
        String tieuDe;
        String maGhe = ghe.getMaGhe();

        // KIỂM TRA XEM ĐÃ CÓ TIÊU ĐỀ LƯU TRƯỚC ĐÓ CHƯA
        if (thongTinDaNhapMap.containsKey(maGhe)) {
            // SỬ DỤNG TIÊU ĐỀ ĐÃ LƯU
            tieuDe = thongTinDaNhapMap.get(maGhe).tieuDe;
        } else {
            // TẠO TIÊU ĐỀ MỚI
            String tenTau = chuyen.getTau().getTenTau(); 
            tieuDe = String.format("%s %s → %s - %s - Toa %d - Ghế %d",
                tenTau.substring(tenTau.lastIndexOf(" ") + 1),
                chuyen.getTuyenDuong().getGaDi().getTenGa(),
                chuyen.getTuyenDuong().getGaDen().getTenGa(),
                FormatUtil.formatDateTime(chuyen.getThoiGianDi()),
                ghe.getKhoangTau().getToaTau().getSoHieuToa(),
                ghe.getSoGhe()
            );
        }

        ThongTinVe panelThongTin = new ThongTinVe(tieuDe, ghe, chuyen);
        panelThongTin.setName("THONGTIN_" + ghe.getMaGhe());

        // XÁC ĐỊNH CHÍNH XÁC GHẾ THUỘC CHIỀU NÀO
        boolean isChieuVe = (chuyen == chuyenVe);

        if (isChieuVe) {
            // KIỂM TRA HEADER CHIỀU VỀ
            if (lbl_headerChieuVe.getParent() == null) {
                pnl_thongTin.add(javax.swing.Box.createVerticalStrut(8));
                pnl_thongTin.add(lbl_headerChieuVe);
                pnl_thongTin.add(javax.swing.Box.createVerticalStrut(10));
            }

            // THÊM VÉ VÀO CUỐI (SAU TẤT CẢ VÉ CHIỀU VỀ KHÁC)
            pnl_thongTin.add(panelThongTin);
            pnl_thongTin.add(javax.swing.Box.createVerticalStrut(10));

        } else {
            // KIỂM TRA HEADER CHIỀU ĐI
            if (lbl_headerChieuDi.getParent() == null) {
                int insertIndex = getIndexAfterKhachHang();
                pnl_thongTin.add(javax.swing.Box.createVerticalStrut(8), insertIndex);
                pnl_thongTin.add(lbl_headerChieuDi, insertIndex + 1);
                pnl_thongTin.add(javax.swing.Box.createVerticalStrut(10), insertIndex + 2);
            }

            // THÊM VÉ SAU HEADER CHIỀU ĐI (TRƯỚC HEADER CHIỀU VỀ NẾU CÓ)
            int insertPos = timViTriThemVeChieuDi();
            pnl_thongTin.add(panelThongTin, insertPos);
            pnl_thongTin.add(javax.swing.Box.createVerticalStrut(10), insertPos + 1);
        }

        // Refresh UI
        pnl_thongTin.revalidate();
        pnl_thongTin.repaint();
        
        if (autoUpdateUI) capNhatToanBoGiaoDien();
    }

    /**
     * Tìm vị trí để thêm vé chiều đi (sau vé chiều đi cuối cùng, trước header chiều về)
     */
    private int timViTriThemVeChieuDi() {
        Component[] components = pnl_thongTin.getComponents();

        // Tìm vị trí của header chiều về
        for (int i = 0; i < components.length; i++) {
            if (components[i] == lbl_headerChieuVe) {
                // Thêm trước Box.Filler của header chiều về
                if (i > 0 && components[i - 1] instanceof javax.swing.Box.Filler) {
                    return i - 1;
                }
                return i;
            }
        }

        // Nếu không có header chiều về, thêm vào cuối
        return pnl_thongTin.getComponentCount();
    }

    // HÀM HỖ TRỢ
    private int getIndexAfterKhachHang() {
        Component[] components = pnl_thongTin.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] == pnl_thongTinKhachHang) {
                // Bỏ qua Box.Filler
                if (i + 1 < components.length && 
                    components[i + 1] instanceof javax.swing.Box.Filler) {
                    return i + 2;
                }
                return i + 1;
            }
        }
        return pnl_thongTin.getComponentCount();
    }

    /**
     * Xóa panel thông tin vé
     */
    public void xoaPanelThongTinVe(Ghe ghe) {
        String nameToFind = "THONGTIN_" + ghe.getMaGhe();
        Component[] components = pnl_thongTin.getComponents();

        for (int i = 0; i < components.length; i++) {
            if (components[i].getName() != null && components[i].getName().equals(nameToFind)) {
                // XÓA KHỎI MAP KHI BỎ CHỌN GHẾ
                thongTinDaNhapMap.remove(ghe.getMaGhe());

                pnl_thongTin.remove(i);
                if (i < pnl_thongTin.getComponentCount() && 
                    pnl_thongTin.getComponent(i) instanceof javax.swing.Box.Filler) {
                    pnl_thongTin.remove(i);
                }
                break;
            }
        }
        kiemTraVaXoaHeader();

        pnl_thongTin.revalidate();
        pnl_thongTin.repaint();

        if (autoUpdateUI) capNhatToanBoGiaoDien();
    }

    // KIỂM TRA VÀ XÓA HEADER NẾU KHÔNG CÒN GHẾ CỦA CHIỀU ĐÓ
    private void kiemTraVaXoaHeader() {
        boolean coGheDi = false;
        boolean coGheVe = false;

        // Đếm ghế mỗi chiều BẰNG PHƯƠNG THỨC HELPER
        for (Ghe ghe : bus.getDanhSachGheDaChon()) {
            if (isGheThuocChuyenVe(ghe)) {
                coGheVe = true;
            } else {
                coGheDi = true;
            }
        }

        // Xóa header chiều đi nếu không còn ghế
        if (!coGheDi && lbl_headerChieuDi.getParent() != null) {
            xoaHeaderVaFiller(lbl_headerChieuDi);
        }

        // Xóa header chiều về nếu không còn ghế
        if (!coGheVe && lbl_headerChieuVe.getParent() != null) {
            xoaHeaderVaFiller(lbl_headerChieuVe);
        }   
    }

    // XÓA HEADER VÀ CÁC FILLER XUNG QUANH
    private void xoaHeaderVaFiller(javax.swing.JLabel header) {
        Component[] components = pnl_thongTin.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] == header) {
                // Xóa Filler trước header
                if (i > 0 && components[i - 1] instanceof javax.swing.Box.Filler) {
                    pnl_thongTin.remove(i - 1);
                    i--; // Điều chỉnh index
                }
                // Xóa header
                pnl_thongTin.remove(header);

                // Xóa Filler sau header
                if (i < pnl_thongTin.getComponentCount() && 
                    pnl_thongTin.getComponent(i) instanceof javax.swing.Box.Filler) {
                    pnl_thongTin.remove(i);
                }
                break;
            }
        }
    }

    /**
     * Lấy danh sách thông tin hành khách đã nhập
     */
    public ArrayList<ThongTinVe.ThongTinHanhKhach> getDanhSachThongTinHanhKhach() {
        ArrayList<ThongTinVe.ThongTinHanhKhach> dsThongTin = new ArrayList<>();

        Component[] components = pnl_thongTin.getComponents();
        for (Component comp : components) {
            if (comp instanceof ThongTinVe) {
                ThongTinVe panelThongTin = (ThongTinVe) comp;
                dsThongTin.add(panelThongTin.getThongTin());
            }
        }
        return dsThongTin;
    }

    /**
     * Setup sự kiện cho checkbox "Chọn tất cả" của từng toa
    */
    private void setupChonTatCaCheckbox(JPanel panelToa, ToaTau toa, ChuyenTau chuyen) {
        // Tìm checkbox trong panel toa
        JCheckBox chkChonTatCa = findCheckboxInPanel(panelToa);

        if (chkChonTatCa != null) {
            chkChonTatCa.addActionListener(e -> {
                try {
                    handleChonTatCaToa(chkChonTatCa.isSelected(), toa, chuyen, panelToa);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    /**
     * Tìm checkbox trong panel
     */
    
    private JCheckBox findCheckboxInPanel(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JCheckBox) {
                return (JCheckBox) comp;
            } else if (comp instanceof Container) {
                JCheckBox found = findCheckboxInPanel((Container) comp);
                if (found != null) return found;
            }
        }
        return null;
    }

    // Thêm vào class
    RoundedButton findButtonBySoGhe(JPanel panel, int soGhe) {
        return getAllButtons(panel).stream()
                .filter(btn -> {
                    String text = btn.getText();
                    if (text == null || text.trim().isEmpty()) {
                        return false;
                    }
                    try {
                        return soGhe == Integer.parseInt(text.trim());
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .findFirst()
                .orElse(null);
    }

    private void handleChonTatCaToa(boolean isSelected, ToaTau toa, ChuyenTau chuyen, JPanel panelToa) throws Exception {
    // TẮT CẬP NHẬT TỰ ĐỘNG
    boolean oldAutoUpdate = autoUpdateUI;
    autoUpdateUI = false;

    for (Ghe ghe : bus.getDanhSachGheTheoToa(toa)) {
        if (bus.isGheDaDat(ghe, chuyen)) continue;
        RoundedButton btn = findButtonBySoGhe(panelToa, ghe.getSoGhe());
        if (btn == null || !btn.isEnabled()) continue;

        if (isSelected) {
            if (!bus.isDaChonGhe(ghe)) {
                btn.setBackground(new java.awt.Color(252, 90, 90));
                themGheDaChon(ghe, chuyen);
                themPanelThongTinVe(ghe, chuyen);
            }
        } else {
            if (bus.isDaChonGhe(ghe)) {
                btn.setBackground(Color.WHITE);
                xoaGheDaChon(ghe);
                xoaPanelThongTinVe(ghe); // Không vẽ lại
            }
        }
    }

    // BẬT LẠI + VẼ LẠI 1 LẦN DUY NHẤT
    autoUpdateUI = true;
    capNhatToanBoGiaoDien();
}

    /**
     * Sync trạng thái checkbox "Chọn tất cả" theo ghế đã chọn
     */
    
    void syncCheckboxChonTatCa(JPanel panelToa, ToaTau toa, ChuyenTau chuyen) throws Exception {
        JCheckBox chkChonTatCa = findCheckboxInPanel(panelToa);
        if (chkChonTatCa == null) return;

        ArrayList<Ghe> dsGhe = bus.getDanhSachGheTheoToa(toa);

        // Đếm số ghế trống và số ghế đã chọn
        int tongGheTrong = 0;
        int tongGheDaChon = 0;

        for (Ghe ghe : dsGhe) {
            if (!bus.isGheDaDat(ghe, chuyen)) {
                tongGheTrong++;
                if (bus.isDaChonGhe(ghe)) {
                    tongGheDaChon++;
                }
            }
        }

        // Bỏ listener cũ trước khi update
        for (java.awt.event.ActionListener al : chkChonTatCa.getActionListeners()) {
            chkChonTatCa.removeActionListener(al);
        }

        // Set trạng thái checkbox
        chkChonTatCa.setSelected(tongGheTrong > 0 && tongGheDaChon == tongGheTrong);

        // Thêm lại listener
        chkChonTatCa.addActionListener(e -> {
            try {
                handleChonTatCaToa(chkChonTatCa.isSelected(), toa, chuyen, panelToa);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
    
    /**
    * Validate toàn bộ thông tin trước khi chuyển sang thanh toán
    */
   public boolean validateThongTin() {
       // 1. Validate thông tin khách hàng chính
       String hoTen = txt_hoTen.getText().trim();
       String sdt = txt_sdt.getText().trim();
       String cccd = txt_cccd.getText().trim();

       if (hoTen.isEmpty()) {
           javax.swing.JOptionPane.showMessageDialog(this, 
               "Vui lòng nhập họ tên khách hàng!", 
               "Lỗi", 
               javax.swing.JOptionPane.ERROR_MESSAGE);
           txt_hoTen.requestFocus();
           return false;
       }

       if (sdt.isEmpty()) {
           javax.swing.JOptionPane.showMessageDialog(this, 
               "Vui lòng nhập số điện thoại!", 
               "Lỗi", 
               javax.swing.JOptionPane.ERROR_MESSAGE);
           txt_sdt.requestFocus();
           return false;
       }

       // Validate format số điện thoại (10 số, bắt đầu bằng 0)
       if (!sdt.matches("^0\\d{9}$")) {
           javax.swing.JOptionPane.showMessageDialog(this, 
               "Số điện thoại không hợp lệ! (10 chữ số, bắt đầu bằng 0)", 
               "Lỗi", 
               javax.swing.JOptionPane.ERROR_MESSAGE);
           txt_sdt.requestFocus();
           return false;
       }

       if (cccd.isEmpty()) {
           javax.swing.JOptionPane.showMessageDialog(this, 
               "Vui lòng nhập số CCCD!", 
               "Lỗi", 
               javax.swing.JOptionPane.ERROR_MESSAGE);
           txt_cccd.requestFocus();
           return false;
       }

       // Validate format CCCD (12 chữ số)
       if (!cccd.matches("^\\d{12}$")) {
           javax.swing.JOptionPane.showMessageDialog(this, 
               "Số CCCD không hợp lệ! (12 chữ số)", 
               "Lỗi", 
               javax.swing.JOptionPane.ERROR_MESSAGE);
           txt_cccd.requestFocus();
           return false;
       }

       // 2. Kiểm tra đã chọn ghế chưa
       if (bus.getDanhSachGheDaChon().isEmpty()) {
           javax.swing.JOptionPane.showMessageDialog(this, 
               "Vui lòng chọn ít nhất một ghế!", 
               "Lỗi", 
               javax.swing.JOptionPane.ERROR_MESSAGE);
           return false;
       }

       // 3. Validate thông tin hành khách cho từng ghế
       ArrayList<ThongTinVe.ThongTinHanhKhach> dsThongTin = getDanhSachThongTinHanhKhach();

       for (int i = 0; i < dsThongTin.size(); i++) {
           ThongTinVe.ThongTinHanhKhach tt = dsThongTin.get(i);

           if (tt.getHoTen() == null || tt.getHoTen().trim().isEmpty()) {
               javax.swing.JOptionPane.showMessageDialog(this, 
                   "Vui lòng nhập họ tên cho hành khách vé số " + (i + 1) + "!", 
                   "Lỗi", 
                   javax.swing.JOptionPane.ERROR_MESSAGE);
               return false;
           }

           // Kiểm tra CCCD nếu không phải trẻ em
           if (!"Trẻ em".equals(tt.getLoaiVe())) {
               if (tt.getCCCD() == null || tt.getCCCD().trim().isEmpty()) {
                   javax.swing.JOptionPane.showMessageDialog(this, 
                       "Vui lòng nhập CCCD cho hành khách vé số " + (i + 1) + "!", 
                       "Lỗi", 
                       javax.swing.JOptionPane.ERROR_MESSAGE);
                   return false;
               }

               // Validate format CCCD
               if (!tt.getCCCD().matches("^\\d{12}$")) {
                   javax.swing.JOptionPane.showMessageDialog(this, 
                       "CCCD của hành khách vé số " + (i + 1) + " không hợp lệ! (12 chữ số)", 
                       "Lỗi", 
                       javax.swing.JOptionPane.ERROR_MESSAGE);
                   return false;
               }
           }

           if (tt.getLoaiVe() == null || tt.getLoaiVe().trim().isEmpty()) {
               javax.swing.JOptionPane.showMessageDialog(this, 
                   "Vui lòng chọn loại vé cho hành khách vé số " + (i + 1) + "!", 
                   "Lỗi", 
                   javax.swing.JOptionPane.ERROR_MESSAGE);
               return false;
           }   
       }

       return true;
   }

    public String getHoTenKhachChinh() {
        return txt_hoTen.getText().trim();
    }

    public String getSoDienThoai() {
        return txt_sdt.getText().trim();
    }

    public String getCCCD() {
        return txt_cccd.getText().trim();
    }

    public javax.swing.JPanel getPanelThongTin() {
        return pnl_thongTin;
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
        pnl_contain = new javax.swing.JPanel();
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
        btn_chuyenVe = new javax.swing.JButton();
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

        pnl_contain.setPreferredSize(new java.awt.Dimension(650, 200));
        pnl_contain.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        pnl_nhapThongTin.setBackground(new java.awt.Color(255, 255, 255));
        pnl_nhapThongTin.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhập thông tin", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(123, 17, 19))); // NOI18N
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

        jLabel4.setText("Tên:");
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

        pnl_contain.add(pnl_nhapThongTin);

        add(pnl_contain, java.awt.BorderLayout.LINE_END);

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
        jScrollPane1.setViewportView(jPanel8);

        jPanel7.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(10, 50));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 5));

        btn_chuyenVe.setText("Chọn chuyến về");
        btn_chuyenVe.setPreferredSize(new java.awt.Dimension(130, 45));
        jPanel4.add(btn_chuyenVe);

        jPanel3.add(jPanel4, java.awt.BorderLayout.PAGE_END);

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_chuyenVe;
    private javax.swing.JButton btn_next;
    private javax.swing.JButton btn_previous;
    private javax.swing.Box.Filler filler10;
    private javax.swing.Box.Filler filler11;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler8;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnl_contain;
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
