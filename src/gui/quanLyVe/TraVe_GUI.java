/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.quanLyVe;

import bus.QuanLyTraVe_BUS;
import entity.HoaDonTra;
import entity.NhanVien;
import entity.Ve;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;
import main.Application;
import raven.toast.Notifications;
import utils.CreateReturnOrder;
import utils.FormatUtil;

/**
 *
 * @author CÔNG HOÀNG
 */
public class TraVe_GUI extends javax.swing.JPanel {
    private QuanLyTraVe_BUS bus;
    private final NhanVien nhanVien = Application.nhanVien;
    private Ve ve;
    private HoaDonTra hdt;
    /**
     * Creates new form TraVe_GUI
     */
    public TraVe_GUI() {
        initComponents();
        init();
    }
    
    private void init() {
        bus = new QuanLyTraVe_BUS();
        pnl_chiTietHoanTien.setVisible(false);
        pnl_thongBao.setVisible(false);
        pnl_thongTinVe.setVisible(false);
        txt_timKiem.setText("V001");
    }
    
    private void handleTimKiem() {
        String maVe = txt_timKiem.getText().trim();

        if(maVe.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Cần nhập mã vé cần trả!");
            return;
        }

        ve = bus.getVeById(maVe);

        // Reset tất cả panel trước khi hiển thị kết quả mới
        pnl_thongTinVe.setVisible(false);
        pnl_thongBao.setVisible(false);
        pnl_chiTietHoanTien.setVisible(false);

        if(ve == null) {
            pnl_thongBao.setVisible(true);
            lbl_thongBao.setText("Không tìm thấy vé");
            lbl_chiTietThongBao.setText("Vui lòng kiểm tra lại mã vé");
            return;
        }

        // Hiển thị thông tin vé
        hienThiThongTinVe();
        pnl_thongTinVe.setVisible(true);

        // Kiểm tra điều kiện trả vé
        if(!kiemTraDieuKienTraVe()) {
            // Nếu không đủ điều kiện, chỉ hiển thị thông báo
            pnl_thongBao.setVisible(true);
            return;
        }

        // Nếu đủ điều kiện, tạo hóa đơn và hiển thị chi tiết hoàn tiền
        hdt = createHoaDonTra();
        hienThiChiTietHoanTien();
        pnl_chiTietHoanTien.setVisible(true);
    }

    private void hienThiThongTinVe() {
        lbl_maVe.setText(ve.getMaVe());
        lbl_tau.setText(ve.getChuyenTau().getTau().getMaTau() + " - " + ve.getChuyenTau().getTau().getTenTau());
        lbl_hanhTrinh.setText(ve.getChuyenTau().getTuyenDuong().getGaDi().getTenGa() + " - " + ve.getChuyenTau().getTuyenDuong().getGaDen().getTenGa());
        lbl_ngayDi.setText(FormatUtil.formatDate(ve.getChuyenTau().getThoiGianDi().toLocalDate()));
        lbl_gioKhoiHanh.setText(ve.getChuyenTau().getThoiGianDi().toLocalTime().toString());
        lbl_soGhe.setText("Toa " + ve.getGhe().getKhoangTau().getToaTau().getSoHieuToa() + " chỗ " + ve.getGhe().getSoGhe());
        lbl_hoTen.setText(ve.getHanhKhach().getTenHanhKhach());
        lbl_cccd.setText(ve.getHanhKhach().getCccd());
    }

    private boolean kiemTraDieuKienTraVe() {
        // Kiểm tra trạng thái vé đã sử dụng
        if(ve.getTrangThai().compare(2)) {
            lbl_thongBao.setText("Không đủ điều kiện trả vé");
            lbl_chiTietThongBao.setText("Vé đã được sử dụng!");
            return false;
        }

        // Kiểm tra trạng thái vé đã hủy
        if(ve.getTrangThai().compare(3)) {
            lbl_thongBao.setText("Không đủ điều kiện trả vé");
            lbl_chiTietThongBao.setText("Vé đã bị hủy không thể đổi trả!");
            return false;
        }

        // Kiểm tra thời gian trước giờ tàu chạy
        LocalDateTime gioTauChay = ve.getChuyenTau().getThoiGianDi();
        LocalDateTime gioHienTai = LocalDateTime.now();
        Duration duration = Duration.between(gioHienTai, gioTauChay);
        long hours = duration.toHours();

        if(hours < 4) {
            lbl_thongBao.setText("Không đủ điều kiện trả vé");
            lbl_chiTietThongBao.setText("Vé chỉ có thể trả trước 4 giờ tàu chạy (tàu chạy lúc " + FormatUtil.formatDateTime(gioTauChay) + ")");
            return false;
        }

        return true;
    }

    private HoaDonTra createHoaDonTra() {
        String maHDT = bus.generateID();
        LocalDateTime ngayTra = LocalDateTime.now();
        return new HoaDonTra(maHDT, ngayTra, nhanVien, ve);
    }

    private void hienThiChiTietHoanTien() { 
        lbl_giaVe.setText(FormatUtil.formatCurrency(ve.getGiaVe()));
        lbl_phiHuy.setText(FormatUtil.formatCurrency(hdt.phiHuy()));
        lbl_soTienHoan.setText(FormatUtil.formatCurrency(hdt.getSoTienHoanTra()));
    }

    private void handleDongThongBao() {
        // Reset tất cả panel và input
        pnl_thongTinVe.setVisible(false);
        pnl_thongBao.setVisible(false);
        pnl_chiTietHoanTien.setVisible(false);
        txt_timKiem.setText("");

        // Reset biến ve và hdt
        ve = null;
        hdt = null;
    }

    private void handleTraVe() {
        // Kiểm tra hdt có tồn tại không
        if(hdt == null) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Không có thông tin hóa đơn trả!");
            return;
        }

        int n = JOptionPane.showConfirmDialog(
            null,
            "Bạn có chắc muốn trả vé không? Vé đã trả sẽ không thể phục hồi",
            "Xác nhận trả vé",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if(n == JOptionPane.YES_OPTION) {
            try {
                if(bus.create(hdt)) {
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Trả vé thành công!");

                    // Hỏi có muốn in hóa đơn không
                    int printChoice = JOptionPane.showConfirmDialog(
                        null,
                        "Bạn có muốn xuất hóa đơn trả vé PDF không?",
                        "Xuất hóa đơn",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    );

                    if(printChoice == JOptionPane.YES_OPTION) {
                        // Xuất hóa đơn PDF
                        CreateReturnOrder printer = new CreateReturnOrder(hdt);
                        printer.xuatHoaDonTra();
                    }

                    // Reset giao diện
                    handleDongThongBao();
                }
            } catch (Exception ex) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể thực hiện trả vé: " + ex.getMessage());
                ex.printStackTrace();
            }
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

        jPanel1 = new javax.swing.JPanel();
        pnl_quyDinh = new gui.custom.PanelShadow();
        jLabel1 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        jLabel2 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        jLabel3 = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        jLabel4 = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 20));
        pnl_timKiem = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txt_timKiem = new javax.swing.JTextField();
        btn_timKiem = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        pnl_thongTinVe = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        pnl = new gui.custom.PanelShadow();
        jLabel18 = new javax.swing.JLabel();
        lbl_maVe = new javax.swing.JLabel();
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
        pnl_chiTietHoanTien = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        btn_traVe = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        panelShadow1 = new gui.custom.PanelShadow();
        jLabel32 = new javax.swing.JLabel();
        lbl_giaVe = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        lbl_phiHuy = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lbl_soTienHoan = new javax.swing.JLabel();
        pnl_thongBao = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        btn_dong = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        panelShadow2 = new gui.custom.PanelShadow();
        lbl_thongBao = new javax.swing.JLabel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 20), new java.awt.Dimension(32767, 20));
        lbl_chiTietThongBao = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 20, 20));
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 300));
        jPanel1.setPreferredSize(new java.awt.Dimension(1366, 275));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        pnl_quyDinh.setBackground(new java.awt.Color(217, 237, 247));
        pnl_quyDinh.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 20));
        pnl_quyDinh.setPreferredSize(new java.awt.Dimension(100, 200));
        pnl_quyDinh.setShadowOpacity(0.2F);
        pnl_quyDinh.setShadowSize(4);
        pnl_quyDinh.setLayout(new javax.swing.BoxLayout(pnl_quyDinh, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("- Trước giờ tàu chạy từ 24 giờ trở lên: Mức phí là 10% giá vé.");
        pnl_quyDinh.add(jLabel1);
        pnl_quyDinh.add(filler1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("- Trước giờ tàu chạy từ 4 giờ đến dưới 24 giờ: Mức phí là 20% giá vé. ");
        pnl_quyDinh.add(jLabel2);
        pnl_quyDinh.add(filler2);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("- Dưới 4 giờ trước giờ tàu chạy: Không được hoàn trả vé.");
        pnl_quyDinh.add(jLabel3);
        pnl_quyDinh.add(filler3);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("- Số tiền hoàn trả sẽ được trả trực tiếp tại quầy");
        pnl_quyDinh.add(jLabel4);

        jPanel1.add(pnl_quyDinh);
        jPanel1.add(filler4);

        pnl_timKiem.setMaximumSize(new java.awt.Dimension(32767, 60));
        pnl_timKiem.setPreferredSize(new java.awt.Dimension(1346, 50));
        pnl_timKiem.setLayout(new java.awt.BorderLayout());

        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 100));
        jPanel3.setMinimumSize(new java.awt.Dimension(0, 100));
        jPanel3.setPreferredSize(new java.awt.Dimension(1326, 60));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Mã vé:");
        jLabel5.setPreferredSize(new java.awt.Dimension(80, 16));
        jPanel3.add(jLabel5);

        txt_timKiem.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        txt_timKiem.setPreferredSize(new java.awt.Dimension(600, 50));
        jPanel3.add(txt_timKiem);

        btn_timKiem.setText("Tìm kiếm");
        btn_timKiem.setPreferredSize(new java.awt.Dimension(100, 50));
        btn_timKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_timKiemActionPerformed(evt);
            }
        });
        jPanel3.add(btn_timKiem);

        pnl_timKiem.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel1.add(pnl_timKiem);

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.LINE_AXIS));

        pnl_thongTinVe.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 0, 10));
        pnl_thongTinVe.setLayout(new java.awt.BorderLayout());

        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 4, 10, 0));
        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel6.setText("Thông tin vé");
        jPanel9.add(jLabel6);

        pnl_thongTinVe.add(jPanel9, java.awt.BorderLayout.PAGE_START);

        pnl.setBackground(new java.awt.Color(255, 255, 255));
        pnl.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
        pnl.setShadowOpacity(0.2F);
        pnl.setShadowSize(4);
        pnl.setLayout(new java.awt.GridLayout(8, 2));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel18.setText("Mã vé:");
        pnl.add(jLabel18);

        lbl_maVe.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbl_maVe.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_maVe.setText("jLabel10");
        pnl.add(lbl_maVe);

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

        jPanel14.add(pnl_thongTinVe);

        pnl_chiTietHoanTien.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 0, 10));
        pnl_chiTietHoanTien.setPreferredSize(new java.awt.Dimension(250, 480));
        pnl_chiTietHoanTien.setLayout(new java.awt.BorderLayout());

        jPanel12.setPreferredSize(new java.awt.Dimension(600, 250));
        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel13.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 0));
        jPanel13.setPreferredSize(new java.awt.Dimension(600, 70));
        jPanel13.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        btn_traVe.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btn_traVe.setText("Xác nhận trả vé");
        btn_traVe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_traVeActionPerformed(evt);
            }
        });
        jPanel13.add(btn_traVe);

        jPanel12.add(jPanel13, java.awt.BorderLayout.PAGE_START);

        pnl_chiTietHoanTien.add(jPanel12, java.awt.BorderLayout.CENTER);

        jPanel5.setPreferredSize(new java.awt.Dimension(106, 220));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 4, 10, 0));
        jPanel11.setLayout(new java.awt.GridLayout(1, 0));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel28.setText("Chi tiết hoàn tiền");
        jPanel11.add(jLabel28);

        jPanel5.add(jPanel11, java.awt.BorderLayout.PAGE_START);

        panelShadow1.setBackground(new java.awt.Color(255, 255, 255));
        panelShadow1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
        panelShadow1.setShadowOpacity(0.2F);
        panelShadow1.setShadowSize(4);
        panelShadow1.setLayout(new java.awt.GridLayout(3, 2));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel32.setText("Giá vé gốc:");
        panelShadow1.add(jLabel32);

        lbl_giaVe.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbl_giaVe.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_giaVe.setText("1.200.000");
        panelShadow1.add(lbl_giaVe);

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel30.setText("Phí hủy vé: ");
        panelShadow1.add(jLabel30);

        lbl_phiHuy.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbl_phiHuy.setForeground(new java.awt.Color(255, 51, 51));
        lbl_phiHuy.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_phiHuy.setText("-240.000");
        panelShadow1.add(lbl_phiHuy);

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel33.setText("Số tiền hoàn lại:");
        panelShadow1.add(jLabel33);

        lbl_soTienHoan.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbl_soTienHoan.setForeground(new java.awt.Color(0, 204, 51));
        lbl_soTienHoan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_soTienHoan.setText("960.000");
        panelShadow1.add(lbl_soTienHoan);

        jPanel5.add(panelShadow1, java.awt.BorderLayout.CENTER);

        pnl_chiTietHoanTien.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel14.add(pnl_chiTietHoanTien);

        pnl_thongBao.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 0, 10));
        pnl_thongBao.setPreferredSize(new java.awt.Dimension(250, 480));
        pnl_thongBao.setLayout(new java.awt.BorderLayout());

        jPanel15.setPreferredSize(new java.awt.Dimension(600, 250));
        jPanel15.setLayout(new java.awt.BorderLayout());

        jPanel16.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 0));
        jPanel16.setPreferredSize(new java.awt.Dimension(600, 70));
        jPanel16.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        btn_dong.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btn_dong.setText("Đóng");
        btn_dong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dongActionPerformed(evt);
            }
        });
        jPanel16.add(btn_dong);

        jPanel15.add(jPanel16, java.awt.BorderLayout.PAGE_START);

        pnl_thongBao.add(jPanel15, java.awt.BorderLayout.CENTER);

        jPanel6.setPreferredSize(new java.awt.Dimension(106, 150));
        jPanel6.setLayout(new java.awt.BorderLayout());

        panelShadow2.setBackground(new java.awt.Color(254, 226, 226));
        panelShadow2.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 20));
        panelShadow2.setShadowOpacity(0.2F);
        panelShadow2.setShadowSize(4);
        panelShadow2.setLayout(new javax.swing.BoxLayout(panelShadow2, javax.swing.BoxLayout.Y_AXIS));

        lbl_thongBao.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_thongBao.setForeground(new java.awt.Color(153, 27, 27));
        lbl_thongBao.setText("Không đủ điều kiện trả vé");
        panelShadow2.add(lbl_thongBao);
        panelShadow2.add(filler5);

        lbl_chiTietThongBao.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbl_chiTietThongBao.setForeground(new java.awt.Color(185, 28, 28));
        panelShadow2.add(lbl_chiTietThongBao);

        jPanel6.add(panelShadow2, java.awt.BorderLayout.CENTER);

        jPanel17.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 4, 10, 0));
        jPanel17.setLayout(new java.awt.GridLayout(1, 0));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel29.setText("Thông báo");
        jPanel17.add(jLabel29);

        jPanel6.add(jPanel17, java.awt.BorderLayout.PAGE_START);

        pnl_thongBao.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jPanel14.add(pnl_thongBao);

        jPanel4.add(jPanel14, java.awt.BorderLayout.CENTER);

        add(jPanel4, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_dongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dongActionPerformed
        // TODO add your handling code here:
        handleDongThongBao();
    }//GEN-LAST:event_btn_dongActionPerformed

    private void btn_timKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_timKiemActionPerformed
        // TODO add your handling code here:
        handleTimKiem();
    }//GEN-LAST:event_btn_timKiemActionPerformed

    private void btn_traVeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_traVeActionPerformed
        // TODO add your handling code here:
        handleTraVe();
    }//GEN-LAST:event_btn_traVeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_dong;
    private javax.swing.JButton btn_timKiem;
    private javax.swing.JButton btn_traVe;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lbl_cccd;
    private javax.swing.JLabel lbl_chiTietThongBao;
    private javax.swing.JLabel lbl_giaVe;
    private javax.swing.JLabel lbl_gioKhoiHanh;
    private javax.swing.JLabel lbl_hanhTrinh;
    private javax.swing.JLabel lbl_hoTen;
    private javax.swing.JLabel lbl_maVe;
    private javax.swing.JLabel lbl_ngayDi;
    private javax.swing.JLabel lbl_phiHuy;
    private javax.swing.JLabel lbl_soGhe;
    private javax.swing.JLabel lbl_soTienHoan;
    private javax.swing.JLabel lbl_tau;
    private javax.swing.JLabel lbl_thongBao;
    private gui.custom.PanelShadow panelShadow1;
    private gui.custom.PanelShadow panelShadow2;
    private gui.custom.PanelShadow pnl;
    private javax.swing.JPanel pnl_chiTietHoanTien;
    private gui.custom.PanelShadow pnl_quyDinh;
    private javax.swing.JPanel pnl_thongBao;
    private javax.swing.JPanel pnl_thongTinVe;
    private javax.swing.JPanel pnl_timKiem;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
