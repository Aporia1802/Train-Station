/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.components;

import bus.QuanLyDatVe_BUS;
import entity.ChuyenTau;
import entity.Ghe;
import entity.HoaDonDoi;
import entity.NhanVien;
import entity.Ve;
import java.awt.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import main.Application;
import raven.toast.Notifications;
import utils.CreateExchangeOrder;
import utils.CreateTicket;
import utils.FormatUtil;

/**
 *
 * @author CÔNG HOÀNG
 */
public class ThanhToan extends javax.swing.JPanel {
    private final QuanLyDatVe_BUS bus;
    private final NhanVien nhanVien = Application.nhanVien;
    
    // Thông tin chung
    private String hoTenKhach;
    private String sdtKhach;
    private String cccdKhach;
    private java.util.List<ThongTinVe.ThongTinHanhKhach> dsHanhKhach;
    private ChuyenTau chuyenDi;
    private ChuyenTau chuyenVe;
    private boolean isKhuHoi;
    private double tongTienThanhToan = 0;
    
    // Riêng cho Đổi vé
    private Ve veCanDoi = null;
    private static final double PHI_DOI_VE = 20000;
    
    // Callback
    private Runnable onThanhToanThanhCong;
    
    public ThanhToan(QuanLyDatVe_BUS bus) {
        initComponents();
        this.bus = bus;
    }
   
    public JButton previous() {
        return btn_previous;
    }
    
    /**
     * Set vé cần đổi (chỉ dùng cho chế độ Đổi vé)
     */
    public void setVeCanDoi(Ve veCanDoi) {
        this.veCanDoi = veCanDoi;
    }
    
    /**
     * Kiểm tra có phải đang đổi vé không
     */
    private boolean isDoiVe() {
        return veCanDoi != null;
    }
    
    /**
     * Đặt callback khi thanh toán thành công
     */
    public void setOnThanhToanThanhCong(Runnable callback) {
        this.onThanhToanThanhCong = callback;
    }
   

    /**
     * Hiển thị dữ liệu thanh toán - CẢI TIẾN
     */
    public void hienThiDuLieu(String hoTen, String sdt, String cccd,
                      java.util.List<ThongTinVe.ThongTinHanhKhach> dsHK,
                      ChuyenTau chuyenDi, ChuyenTau chuyenVe, boolean khuHoi) {
        // Lưu thông tin
        this.hoTenKhach = hoTen;
        this.sdtKhach = sdt;
        this.cccdKhach = cccd;
        this.dsHanhKhach = dsHK;
        this.chuyenDi = chuyenDi;
        this.chuyenVe = chuyenVe;
        this.isKhuHoi = khuHoi;

        // Hiển thị thông tin khách
        jLabel6.setText(hoTen);
        jLabel8.setText(sdt);

        // Clear panel vé
        jPanel13.removeAll();

        // Phân loại vé theo chiều
        java.util.ArrayList<ThongTinVe.ThongTinHanhKhach> dsVeDi = new java.util.ArrayList<>();
        java.util.ArrayList<ThongTinVe.ThongTinHanhKhach> dsVeVe = new java.util.ArrayList<>();

        String maChuyenDi = (chuyenDi != null) ? chuyenDi.getMaChuyenTau() : null;
        String maChuyenVe = (chuyenVe != null) ? chuyenVe.getMaChuyenTau() : null;

        for (ThongTinVe.ThongTinHanhKhach hk : dsHK) {
            String maChuyenCuaVe = hk.getMaChuyenTau();
            if (maChuyenCuaVe != null) {
                if (maChuyenCuaVe.equals(maChuyenDi)) {
                    dsVeDi.add(hk);
                } else if (chuyenVe != null && maChuyenCuaVe.equals(maChuyenVe)) {
                    dsVeVe.add(hk);
                }
            }
        }

        double tongTienVeMoi = 0;
        double giaVeCu = 0;

        // ========== XỬ LÝ ĐẶC BIỆT CHO ĐỔI VÉ ==========
        if (isDoiVe()) {
            // Tính giá vé cũ
            giaVeCu = tinhGiaVeCu(veCanDoi);

            // Thêm thông tin vé cũ
            javax.swing.JLabel lblVeCu = taoHeader("Vé cũ");
            jPanel13.add(lblVeCu);
            jPanel13.add(javax.swing.Box.createVerticalStrut(10));

            JPanel pnlVeCu = taoThongTinVeCu(veCanDoi, giaVeCu);
            jPanel13.add(pnlVeCu);
            jPanel13.add(javax.swing.Box.createVerticalStrut(20));
        }

        // Hiển thị chiều đi (VÉ MỚI)
        if (!dsVeDi.isEmpty()) {
            String headerText = isDoiVe() ? "Vé mới" : "Chiều đi";
            javax.swing.JLabel lblHeaderDi = taoHeader(headerText);
            jPanel13.add(lblHeaderDi);
            jPanel13.add(javax.swing.Box.createVerticalStrut(10));

            for (ThongTinVe.ThongTinHanhKhach hk : dsVeDi) {
                Ghe ghe = bus.timGheTheoMa(hk.getMaGhe());
                if (ghe == null) continue;

                double giaVe = bus.tinhGiaVe(hk, ghe, chuyenDi);
                tongTienVeMoi += giaVe;

                JPanel pnlVe = taoThongTinVe(hk, ghe, chuyenDi, giaVe);
                jPanel13.add(pnlVe);
                jPanel13.add(javax.swing.Box.createVerticalStrut(10));
            }
        }

        // Hiển thị chiều về
        if (!dsVeVe.isEmpty() && chuyenVe != null) {
            jPanel13.add(javax.swing.Box.createVerticalStrut(20));

            javax.swing.JLabel lblHeaderVe = taoHeader("Chiều về");
            jPanel13.add(lblHeaderVe);
            jPanel13.add(javax.swing.Box.createVerticalStrut(10));

            for (ThongTinVe.ThongTinHanhKhach hk : dsVeVe) {
                Ghe ghe = bus.timGheTheoMa(hk.getMaGhe());
                if (ghe == null) continue;

                double giaVe = bus.tinhGiaVe(hk, ghe, chuyenVe);
                tongTienVeMoi += giaVe;

                JPanel pnlVe = taoThongTinVe(hk, ghe, chuyenVe, giaVe);
                jPanel13.add(pnlVe);
                jPanel13.add(javax.swing.Box.createVerticalStrut(10));
            }
        }

        // ========== TÍNH TỔNG TIỀN ==========
        double thanhTien = 0;

        if (isDoiVe()) {
            // Công thức đổi vé: (Vé mới - Vé cũ) + Phí đổi
            double chenhLech = tongTienVeMoi - giaVeCu;

            // Thêm phí đổi vé
            JPanel pnlPhiDoi = taoPanelPhiDoi();
            jPanel13.add(pnlPhiDoi);

            thanhTien = chenhLech + PHI_DOI_VE;

            // Nếu chênh lệch âm (vé mới rẻ hơn), khách chỉ trả phí đổi
            if (thanhTien < 0) {
                thanhTien = PHI_DOI_VE;
            }

            btn_xacNhan.setText("Xác nhận đổi vé");
        } else {
            thanhTien = tongTienVeMoi;
            btn_xacNhan.setText("In hóa đơn và vé");
        }

        // Hiển thị tổng tiền
        jLabel13.setText(FormatUtil.formatCurrency(thanhTien));
        tongTienThanhToan = 1.1 * thanhTien; // +10% VAT
        jLabel2.setText(FormatUtil.formatCurrency(tongTienThanhToan));

        // ===== CẬP NHẬT LABEL TỔNG TIỀN =====
        String labelTongTien = isDoiVe() 
            ? "Tổng tiền đổi vé: " + FormatUtil.formatCurrency(tongTienThanhToan)
            : "Tổng tiền: " + FormatUtil.formatCurrency(tongTienThanhToan);
        jLabel9.setText(labelTongTien);

        // Tính tiền thối
        setupTienThoi(tongTienThanhToan);

        jPanel13.revalidate();
        jPanel13.repaint();
    }

    
    /**
     * Tạo header cho nhóm vé
     */
    private javax.swing.JLabel taoHeader(String text) {
        javax.swing.JLabel lbl = new javax.swing.JLabel(text);
        lbl.setFont(new java.awt.Font("Segoe UI", 1, 16));
        lbl.setForeground(new java.awt.Color(123, 17, 19));
        lbl.setBackground(java.awt.Color.WHITE);
        lbl.setOpaque(true);
        lbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(123, 17, 19)),
            javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 15)
        ));
        lbl.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 40));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
    
    /**
     * Tạo panel hiển thị thông tin vé
     */
    private JPanel taoThongTinVe(ThongTinVe.ThongTinHanhKhach hk, Ghe ghe, 
                             ChuyenTau ct, double giaVe) {
        JPanel p = new JPanel();
        p.setBackground(new java.awt.Color(255, 255, 255));
        p.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(200, 200, 200)),
            javax.swing.BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        p.setLayout(new javax.swing.BoxLayout(p, javax.swing.BoxLayout.Y_AXIS));
        p.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 170));

        // ===== THÊM DÒNG NÀY ĐỂ CĂN TRÁI =====
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        int soHieuToa = ghe.getKhoangTau().getToaTau().getSoHieuToa();
        int soGhe = ghe.getSoGhe();
        String loaiGhe = ghe.getLoaiGhe().getTenLoaiGhe();

        p.add(taoLabel("Họ tên: " + hk.getHoTen(), 15, true));
        p.add(javax.swing.Box.createVerticalStrut(5));
        p.add(taoLabel("Đối tượng: " + hk.getLoaiVe(), 15, false));
        p.add(javax.swing.Box.createVerticalStrut(3));

        String giayCCCD = (hk.getCCCD() == null || hk.getCCCD().isEmpty()) ? "Trẻ em" : hk.getCCCD();
        p.add(taoLabel("Số giấy tờ: " + giayCCCD, 15, false));
        p.add(javax.swing.Box.createVerticalStrut(3));

        p.add(taoLabel("Hành trình: " + 
            ct.getTuyenDuong().getGaDi().getTenGa() + " → " + 
            ct.getTuyenDuong().getGaDen().getTenGa() + " " + 
            FormatUtil.formatDateTime(ct.getThoiGianDi()), 15, true));
        p.add(javax.swing.Box.createVerticalStrut(3));

        p.add(taoLabel("Vị trí: Toa " + soHieuToa + 
            " - Ghế " + soGhe + " (" + loaiGhe + ")", 15, false));
        p.add(javax.swing.Box.createVerticalStrut(8));

        javax.swing.JLabel lblGia = taoLabel("Giá vé: " + FormatUtil.formatCurrency(giaVe), 16, true);
        lblGia.setForeground(new java.awt.Color(255, 51, 0));
        p.add(lblGia);

        return p;
    }
    
    /**
     * Tạo panel thông tin vé cũ (cho đổi vé)
     */
    private JPanel taoThongTinVeCu(Ve ve, double giaVe) {
        JPanel p = new JPanel();
        p.setBackground(new java.awt.Color(254, 226, 226));
        p.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(200, 100, 100)),
            javax.swing.BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        p.setLayout(new javax.swing.BoxLayout(p, javax.swing.BoxLayout.Y_AXIS));
        p.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 180));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(taoLabel("Mã vé: " + ve.getMaVe(), 15, true));
        p.add(javax.swing.Box.createVerticalStrut(5));
        p.add(taoLabel("Hành trình: " + 
            ve.getChuyenTau().getTuyenDuong().getGaDi().getTenGa() + " → " + 
            ve.getChuyenTau().getTuyenDuong().getGaDen().getTenGa(), 15, false));
        p.add(javax.swing.Box.createVerticalStrut(3));
        p.add(taoLabel("Khởi hành: " + 
            FormatUtil.formatDateTime(ve.getChuyenTau().getThoiGianDi()), 15, false));
        p.add(javax.swing.Box.createVerticalStrut(3));
        p.add(taoLabel("Ghế: Toa " + ve.getGhe().getKhoangTau().getToaTau().getSoHieuToa() + 
            " - Ghế " + ve.getGhe().getSoGhe(), 15, false));
        p.add(javax.swing.Box.createVerticalStrut(8));

        // Hiển thị giá vé cũ
        javax.swing.JLabel lblGia = taoLabel("Giá vé: " + FormatUtil.formatCurrency(giaVe), 16, true);
        lblGia.setForeground(new java.awt.Color(34, 197, 94)); // Màu xanh
        p.add(lblGia);
        p.add(javax.swing.Box.createVerticalStrut(5));

        javax.swing.JLabel lblNote = taoLabel("Vé này sẽ bị hủy sau khi đổi", 14, true);
        lblNote.setForeground(new java.awt.Color(185, 28, 28));
        p.add(lblNote);

        return p;
    }

    /**
     * Tạo panel hiển thị phí đổi vé
     */
    private JPanel taoPanelPhiDoi() {
        JPanel p = new JPanel();
        p.setBackground(new java.awt.Color(255, 255, 255));
        p.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(200, 200, 200)),
            javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        p.setLayout(new javax.swing.BoxLayout(p, javax.swing.BoxLayout.LINE_AXIS));
        p.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 50));

        javax.swing.JLabel lblText = taoLabel("Phí đổi vé:", 15, true);
        p.add(lblText);
        p.add(javax.swing.Box.createHorizontalGlue());
        
        javax.swing.JLabel lblPhi = taoLabel(FormatUtil.formatCurrency(PHI_DOI_VE), 16, true);
        lblPhi.setForeground(new java.awt.Color(255, 102, 0));
        p.add(lblPhi);

        return p;
    }
    
    private javax.swing.JLabel taoLabel(String text, int size, boolean bold) {
        javax.swing.JLabel l = new javax.swing.JLabel(text);
        l.setFont(new java.awt.Font("Segoe UI", bold ? java.awt.Font.BOLD : java.awt.Font.PLAIN, size));
        l.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        return l;
    }
    
    /**
     * Setup tính tiền thối tự động
     */
    private void setupTienThoi(final double TONG_TIEN) {
        jTextField1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { tinhTienThoi(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { tinhTienThoi(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { tinhTienThoi(); }

            void tinhTienThoi() {
                try {
                    String text = jTextField1.getText().replaceAll("[^0-9]", "");
                    double khachDua = text.isEmpty() ? 0 : Double.parseDouble(text);
                    double thoi = khachDua - TONG_TIEN;
                    jTextField2.setText(thoi >= 0 ? FormatUtil.formatCurrency(thoi) : "0");
                } catch (Exception ex) {
                    jTextField2.setText("0");
                }
            }
        });
    }
    
    /**
     * Xử lý thanh toán - Tự động phân biệt Đặt vé / Đổi vé
     */
    private void xuLyThanhToan() {
        try {
            // Validate chung
            if (!kiemTraDuLieuHopLe()) {
                return;
            }

            // Nếu là đổi vé, validate riêng
            if (isDoiVe() && !validateDoiVe()) {
                return;
            }

            // Validate tiền khách đưa
            String tienText = jTextField1.getText().replaceAll("[^0-9]", "");
            if (tienText.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng nhập số tiền khách đưa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            double tienNhan = Double.parseDouble(tienText);

            if (tienNhan < tongTienThanhToan) {
                JOptionPane.showMessageDialog(this, 
                    "Số tiền nhận không đủ!\nCần: " + FormatUtil.formatCurrency(tongTienThanhToan) +
                    "\nKhách đưa: " + FormatUtil.formatCurrency(tienNhan),
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Xác nhận
            String message = isDoiVe() 
                ? "Xác nhận đổi vé:\n" +
                  "Vé cũ: " + veCanDoi.getMaVe() + " (sẽ bị hủy)\n" +
                  "Chuyến mới: " + chuyenDi.getMaChuyenTau() + "\n" +
                  "Phí đổi: " + FormatUtil.formatCurrency(PHI_DOI_VE) + "\n" +
                  "Tổng tiền: " + FormatUtil.formatCurrency(tongTienThanhToan)
                : "Xác nhận thanh toán:\n" +
                  "Khách hàng: " + hoTenKhach + "\n" +
                  "Tổng tiền: " + FormatUtil.formatCurrency(tongTienThanhToan) + "\n" +
                  "Tiền nhận: " + FormatUtil.formatCurrency(tienNhan) + "\n" +
                  "Tiền thối: " + jTextField2.getText();

            int confirm = JOptionPane.showConfirmDialog(this, message,
                isDoiVe() ? "Xác nhận đổi vé" : "Xác nhận thanh toán",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Xử lý theo loại giao dịch
            boolean thanhCong;
            if (isDoiVe()) {
                thanhCong = xuLyDoiVe();
            } else {
                thanhCong = xuLyDatVe();
            }

            // Thông báo kết quả
            if (thanhCong) {
                String successMsg = isDoiVe() 
                    ? "Đổi vé thành công!\nVé cũ đã được hủy.\nVé mới đã được in."
                    : "Thanh toán thành công!\nHóa đơn và vé đã được in.";

                Notifications.getInstance().show(Notifications.Type.SUCCESS, 
                    successMsg.split("\n")[0]);

                JOptionPane.showMessageDialog(this, successMsg,
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);

                // Reset
                if (onThanhToanThanhCong != null) {
                    onThanhToanThanhCong.run();
                } else {
                    resetForm();
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Số tiền không hợp lệ!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Có lỗi xảy ra: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Xử lý đặt vé bình thường
     */
    private boolean xuLyDatVe() throws Exception {
        return bus.xuLyThanhToan(
            hoTenKhach, sdtKhach, cccdKhach,
            dsHanhKhach, chuyenDi, chuyenVe,
            isKhuHoi, null
        );
    }
    
    /**
     * Xử lý đổi vé
     */
    private boolean xuLyDoiVe() throws Exception {
        // VALIDATE TRƯỚC KHI XỬ LÝ
        if (!validateDoiVe()) {
            return false;
        }

        // 1. Tạo hóa đơn đổi
        String maHDD = bus.generateHoaDonDoiID();
        LocalDateTime ngayDoi = LocalDateTime.now();
        HoaDonDoi hdd = new HoaDonDoi(maHDD, ngayDoi, nhanVien, veCanDoi);

        // 2. Lưu hóa đơn đổi
        if (!bus.createHoaDonDoi(hdd)) {
            throw new Exception("Không thể tạo hóa đơn đổi!");
        }

        // 3. Hủy vé cũ
        if (!bus.huyVe(veCanDoi)) {
            throw new Exception("Không thể hủy vé cũ!");
        }

        // 4. Tạo vé mới và lấy danh sách vé mới
        ArrayList<Ve> danhSachVeMoi = bus.xuLyThanhToanVaLayVe(
            hoTenKhach, sdtKhach, cccdKhach,
            dsHanhKhach, chuyenDi, null,
            false, null
        );

        if (danhSachVeMoi == null || danhSachVeMoi.isEmpty()) {
            // Rollback: khôi phục vé cũ
            bus.khoiPhucVe(veCanDoi);
            throw new Exception("Không thể tạo vé mới!");
        }

        // DOUBLE CHECK: Đảm bảo chỉ có 1 vé được tạo
        if (danhSachVeMoi.size() != 1) {
            // Rollback tất cả
            for (Ve ve : danhSachVeMoi) {
                bus.huyVe(ve);
            }
            bus.khoiPhucVe(veCanDoi);
            throw new Exception("Lỗi hệ thống: Tạo nhiều hơn 1 vé khi đổi!");
        }

        // 5. In hóa đơn đổi và vé
        int printChoice = JOptionPane.showConfirmDialog(this,
            "Bạn có muốn xuất hóa đơn đổi vé và vé không?",
            "Xuất hóa đơn",
            JOptionPane.YES_NO_OPTION);

        if (printChoice == JOptionPane.YES_OPTION) {
            try {
                // In hóa đơn đổi
                CreateExchangeOrder exchangePrinter = new CreateExchangeOrder(hdd, danhSachVeMoi);
                exchangePrinter.xuatHoaDonDoi();

                // In vé mới (chỉ 1 vé)
                CreateTicket ticketPrinter = new CreateTicket();
                ticketPrinter.taoVe(danhSachVeMoi.get(0));

            } catch (Exception e) {
                System.err.println("Lỗi khi in: " + e.getMessage());
                // Không throw exception vì vé đã đổi thành công
            }
        }

        return true;
    }
    
    private boolean kiemTraDuLieuHopLe() {
        if (hoTenKhach == null || hoTenKhach.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Thiếu thông tin họ tên khách hàng!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (sdtKhach == null || sdtKhach.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Thiếu thông tin số điện thoại!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (dsHanhKhach == null || dsHanhKhach.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Chưa có thông tin hành khách!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (chuyenDi == null) {
            JOptionPane.showMessageDialog(this, 
                "Chưa chọn chuyến tàu!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void resetForm() {
        jTextField1.setText("");
        jTextField2.setText("0");
        tongTienThanhToan = 0;
        veCanDoi = null;

        jPanel13.removeAll();
        jPanel13.revalidate();
        jPanel13.repaint();

        jLabel6.setText("");
        jLabel8.setText("");
        jLabel13.setText("0");
        jLabel2.setText("0");
        jLabel9.setText("Tổng tiền: 0");
    }
    
    /**
    * Tính giá vé cũ
    */
   private double tinhGiaVeCu(Ve ve) {
       if (ve == null) return 0;

       // Lấy thông tin từ vé cũ
       Ghe ghe = ve.getGhe();
       ChuyenTau chuyenTau = ve.getChuyenTau();
       ThongTinVe ttVe = new ThongTinVe();
       ThongTinVe.ThongTinHanhKhach hk = ttVe.createThongTinHanhKhach();
       hk.setHoTen(ve.getHoaDon().getKhachHang().getTenKH());
       hk.setLoaiVe(ve.getLoaiVe().getTenLoaiVe());
       hk.setCCCD(ve.getHoaDon().getKhachHang().getCccd());
       hk.setMaGhe(ghe.getMaGhe());
       hk.setMaChuyenTau(chuyenTau.getMaChuyenTau());

       return bus.tinhGiaVe(hk, ghe, chuyenTau);
    }
   
    private boolean validateDoiVe() {
        // Kiểm tra có đang đổi vé không
        if (!isDoiVe()) {
            JOptionPane.showMessageDialog(this,
                "Không có thông tin vé cần đổi!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // QUAN TRỌNG: Chỉ được đổi 1 vé duy nhất
        if (dsHanhKhach == null || dsHanhKhach.size() != 1) {
            JOptionPane.showMessageDialog(this,
                "Chỉ được đổi 1 vé cho 1 hành khách!\n" +
                "Vui lòng chọn đúng 1 ghế để đổi.",
                "Lỗi nghiệp vụ",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra vé cũ còn hợp lệ không
        if (veCanDoi.getTrangThai().compare(3)) { // Đã hủy
            JOptionPane.showMessageDialog(this,
                "Vé đã bị hủy, không thể đổi!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra thời gian đổi vé (ví dụ: phải trước giờ khởi hành 24h)
        LocalDateTime gioKhoiHanh = veCanDoi.getChuyenTau().getThoiGianDi();
        LocalDateTime gioHienTai = LocalDateTime.now();
        long hoursUntilDeparture = java.time.Duration.between(gioHienTai, gioKhoiHanh).toHours();

        if (hoursUntilDeparture < 24) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Vé sắp khởi hành (còn " + hoursUntilDeparture + " giờ).\n" +
                "Đổi vé gần giờ khởi hành có thể bị thu phí cao hơn.\n" +
                "Bạn có chắc muốn tiếp tục?",
                "Cảnh báo",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return false;
            }
        }

        return true;
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
        jPanel15 = new javax.swing.JPanel();
        btn_previous = new javax.swing.JButton();
        btn_xacNhan = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        jTextField1 = new javax.swing.JTextField();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel13 = new javax.swing.JPanel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 5));
        jPanel14 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

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

        rSMaterialButtonCircle4.setBackground(new java.awt.Color(123, 17, 19));
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

        roundedButton3.setBackground(new java.awt.Color(123, 17, 19));
        roundedButton3.setForeground(new java.awt.Color(255, 255, 255));
        roundedButton3.setText("Thanh toán");
        roundedButton3.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        roundedButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        roundedButton3.setPreferredSize(new java.awt.Dimension(230, 58));
        roundedButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton3ActionPerformed(evt);
            }
        });
        jPanel9.add(roundedButton3);

        jPanel11.add(jPanel9);

        add(jPanel11, java.awt.BorderLayout.PAGE_START);

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

        btn_xacNhan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_xacNhan.setText("In hóa đơn và vé");
        btn_xacNhan.setPreferredSize(new java.awt.Dimension(180, 50));
        btn_xacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xacNhanActionPerformed(evt);
            }
        });
        jPanel15.add(btn_xacNhan);

        add(jPanel15, java.awt.BorderLayout.PAGE_END);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tính tiền", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(123, 17, 19)), javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10))); // NOI18N
        jPanel4.setPreferredSize(new java.awt.Dimension(400, 100));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Tổng thanh toán");
        jPanel6.add(jLabel1);

        jPanel4.add(jPanel6);
        jPanel4.add(filler1);

        jPanel5.setBackground(new java.awt.Color(0, 204, 51));
        jPanel5.setMaximumSize(new java.awt.Dimension(32767, 80));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jLabel2.setBackground(new java.awt.Color(0, 204, 102));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("3.600.000");
        jPanel5.add(jLabel2, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel5);
        jPanel4.add(filler2);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Tiền khách đưa");
        jPanel7.add(jLabel3);

        jPanel4.add(jPanel7);
        jPanel4.add(filler3);

        jTextField1.setMaximumSize(new java.awt.Dimension(2147483647, 60));
        jPanel4.add(jTextField1);
        jPanel4.add(filler4);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.LINE_AXIS));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Tiền thối lại");
        jPanel8.add(jLabel4);

        jPanel4.add(jPanel8);

        jTextField2.setEnabled(false);
        jTextField2.setMaximumSize(new java.awt.Dimension(2147483647, 60));
        jPanel4.add(jTextField2);

        jPanel3.add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setMaximumSize(new java.awt.Dimension(32767, 250));
        jPanel12.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(null);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin vé", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(123, 17, 19)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(jPanel13);

        jPanel12.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel12, java.awt.BorderLayout.CENTER);
        jPanel10.add(filler5, java.awt.BorderLayout.PAGE_START);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin hóa đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(123, 17, 19)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        jPanel14.setPreferredSize(new java.awt.Dimension(374, 300));
        jPanel14.setLayout(new java.awt.BorderLayout());

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel17.setLayout(new java.awt.GridLayout(0, 2));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel5.setText("Họ tên");
        jPanel17.add(jLabel5);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Hồ Công Hoàng");
        jPanel17.add(jLabel6);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel7.setText("Số điện thoại");
        jPanel17.add(jLabel7);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("0349573425");
        jPanel17.add(jLabel8);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel10.setText("Thành tiền");
        jPanel17.add(jLabel10);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("3.600.000");
        jPanel17.add(jLabel13);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel12.setText("Khuyến mãi");
        jPanel17.add(jLabel12);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 255, 51));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("0");
        jPanel17.add(jLabel15);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel14.setText("VAT");
        jPanel17.add(jLabel14);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("10%");
        jPanel17.add(jLabel16);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 51, 0));
        jLabel9.setText("Tổng tiền: 3.600.000");
        jPanel17.add(jLabel9);

        jPanel14.add(jPanel17, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel14, java.awt.BorderLayout.PAGE_END);

        jPanel3.add(jPanel10, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);
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

    private void btn_xacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xacNhanActionPerformed
        // TODO add your handling code here:
        xuLyThanhToan();
    }//GEN-LAST:event_btn_xacNhanActionPerformed

    private void roundedButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundedButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_roundedButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_previous;
    private javax.swing.JButton btn_xacNhan;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle1;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle4;
    private gui.custom.RoundedButton roundedButton1;
    private gui.custom.RoundedButton roundedButton2;
    private gui.custom.RoundedButton roundedButton3;
    // End of variables declaration//GEN-END:variables
}
