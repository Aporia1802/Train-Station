/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.components;

import bus.QuanLyDatVe_BUS;
import entity.ChuyenTau;
import java.awt.Component;
import java.time.ZoneId;
import javax.swing.JButton;
import javax.swing.JPanel;
import utils.FormatUtil;

/**
 *
 * @author CÔNG HOÀNG
 */
public class ThanhToan extends javax.swing.JPanel {
    private final QuanLyDatVe_BUS bus;
    private String hoTenKhach;
    private String sdtKhach;
    private String cccdKhach;
    private java.util.List<ThongTinVe.ThongTinHanhKhach> dsHanhKhach;
    private entity.ChuyenTau chuyenDi;
    private entity.ChuyenTau chuyenVe;
    private boolean isKhuHoi;
    /**
     * Creates new form ThanhToan
     * @param bus
     */
    public ThanhToan(QuanLyDatVe_BUS bus) {
        initComponents();
        this.bus = bus;
    }
   
    public JButton previous() {
        return btn_previous;
    }
   
    // Trong ThanhToan.java, sửa lại method hienThiDuLieu:
    public void hienThiDuLieu(String hoTen, String sdt, String cccd,
                          java.util.List<ThongTinVe.ThongTinHanhKhach> dsHK,
                          entity.ChuyenTau chuyenDi, entity.ChuyenTau chuyenVe, boolean khuHoi) {
        // LƯU THÔNG TIN
        this.hoTenKhach = hoTen;
        this.sdtKhach = sdt;
        this.cccdKhach = cccd;
        this.dsHanhKhach = dsHK;
        this.chuyenDi = chuyenDi;
        this.chuyenVe = chuyenVe;
        this.isKhuHoi = khuHoi;

        jLabel6.setText(hoTen);
        jLabel8.setText(sdt);
        jPanel13.removeAll();


        // PHÂN LOẠI VÉ THEO CHIỀU
        java.util.ArrayList<ThongTinVe.ThongTinHanhKhach> dsVeDi = new java.util.ArrayList<>();
        java.util.ArrayList<ThongTinVe.ThongTinHanhKhach> dsVeVe = new java.util.ArrayList<>();

        String maChuyenDi = (chuyenDi != null) ? chuyenDi.getMaChuyenTau() : null;
        String maChuyenVe = (chuyenVe != null) ? chuyenVe.getMaChuyenTau() : null;

        for (ThongTinVe.ThongTinHanhKhach hk : dsHK) {
            String maChuyenCuaVe = hk.getMaChuyenTau(); 
            System.out.println("Xử lý vé: " + hk.getMaGhe() + " (mã chuyến: " + maChuyenCuaVe + ")");

            if (maChuyenCuaVe != null) {
                if (maChuyenCuaVe.equals(maChuyenDi)) {
                    dsVeDi.add(hk);
                    System.out.println(" -> Vé chiều đi");
                } else if (chuyenVe != null && maChuyenCuaVe.equals(maChuyenVe)) {
                    dsVeVe.add(hk);
                    System.out.println(" -> Vé chiều về");
                } else {
                    System.out.println(" -> Vé không thuộc chuyến nào, bỏ qua");
                }
            }
        }

        double tongTien = 0;

        // === HIỂN THỊ CHIỀU ĐI ===
        if (!dsVeDi.isEmpty()) {
            // HEADER CHIỀU ĐI
            javax.swing.JLabel lblHeaderDi = new javax.swing.JLabel("Chiều đi");
            lblHeaderDi.setFont(new java.awt.Font("Segoe UI", 1, 16));
            lblHeaderDi.setForeground(new java.awt.Color(123, 17, 19));
            lblHeaderDi.setBackground(java.awt.Color.WHITE);
            lblHeaderDi.setOpaque(true);
            lblHeaderDi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            lblHeaderDi.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(123, 17, 19)),
                javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 15)
            ));
            lblHeaderDi.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 40));
            lblHeaderDi.setAlignmentX(Component.LEFT_ALIGNMENT);

            jPanel13.add(lblHeaderDi);
            jPanel13.add(javax.swing.Box.createVerticalStrut(10));


            // DANH SÁCH VÉ CHIỀU ĐI
            for (ThongTinVe.ThongTinHanhKhach hk : dsVeDi) {
                entity.Ghe ghe = bus.timGheTheoMa(hk.getMaGhe());
                if (ghe == null) continue;

                double giaVe = tinhGiaVe(hk, ghe, chuyenDi);
                tongTien += giaVe;

                JPanel pnlVe = taoThongTinVe(hk, ghe, chuyenDi, giaVe);
                jPanel13.add(pnlVe);
                jPanel13.add(javax.swing.Box.createVerticalStrut(10));
            }
        }

        // HIỂN THỊ CHIỀU VỀ
        if (!dsVeVe.isEmpty() && chuyenVe != null) {
            // Thêm khoảng cách giữa 2 chiều
            jPanel13.add(javax.swing.Box.createVerticalStrut(20));
            javax.swing.JLabel lblHeaderVe = new javax.swing.JLabel("Chiều về");
            lblHeaderVe.setFont(new java.awt.Font("Segoe UI", 1, 16));
            lblHeaderVe.setForeground(new java.awt.Color(123, 17, 19));
            lblHeaderVe.setBackground(java.awt.Color.WHITE);
            lblHeaderVe.setOpaque(true);
            lblHeaderVe.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            lblHeaderVe.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(123, 17, 19)),
                javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 15)
            ));
            lblHeaderVe.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 40));
            lblHeaderVe.setAlignmentX(Component.LEFT_ALIGNMENT);

            jPanel13.add(lblHeaderVe);
            jPanel13.add(javax.swing.Box.createVerticalStrut(10));

            // DANH SÁCH VÉ CHIỀU VỀ
            for (ThongTinVe.ThongTinHanhKhach hk : dsVeVe) {
                entity.Ghe ghe = bus.timGheTheoMa(hk.getMaGhe());
                if (ghe == null) continue;

                double giaVe = tinhGiaVe(hk, ghe, chuyenVe);
                tongTien += giaVe;

                JPanel pnlVe = taoThongTinVe(hk, ghe, chuyenVe, giaVe);
                jPanel13.add(pnlVe);
                jPanel13.add(javax.swing.Box.createVerticalStrut(10));
            }
        }

        // HIỂN THỊ TỔNG TIỀN
        jLabel13.setText(utils.FormatUtil.formatCurrency(tongTien));
        jLabel2.setText(utils.FormatUtil.formatCurrency(1.1 * tongTien));
        jLabel9.setText("Tổng tiền: " + utils.FormatUtil.formatCurrency(1.1 * tongTien));

        // TÍNH TIỀN THỐI
        final double TONG_TIEN = 1.1 * tongTien;

        jTextField1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { tinhTienThoi(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { tinhTienThoi(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { tinhTienThoi(); }

            void tinhTienThoi() {
                try {
                    String text = jTextField1.getText().replaceAll("[^0-9]", "");
                    double khachDua = text.isEmpty() ? 0 : Double.parseDouble(text);
                    double thoi = khachDua - TONG_TIEN;
                    jTextField2.setText(thoi >= 0 ? utils.FormatUtil.formatCurrency(thoi) : "0");
                } catch (Exception ex) {
                    jTextField2.setText("0");
                }
            }
        });

        jPanel13.revalidate();
        jPanel13.repaint();
    }

    // TÍNH GIÁ VÉ
    private double tinhGiaVe(ThongTinVe.ThongTinHanhKhach hk, entity.Ghe ghe, entity.ChuyenTau ct) {
        double giaGoc = ct.getTuyenDuong().tinhGiaVeCoBan();
        double heSoGhe = ghe.getLoaiGhe().getHeSoLoaiGhe();
        double heSoLoaiVe = layHeSoLoaiVe(hk.getLoaiVe());
        return giaGoc * heSoGhe * heSoLoaiVe;
    }
    // TẠO LABEL
    private javax.swing.JLabel taoLabel(String text, int size, boolean bold) {
        javax.swing.JLabel l = new javax.swing.JLabel(text);
        l.setFont(new java.awt.Font("Segoe UI", bold ? java.awt.Font.BOLD : java.awt.Font.PLAIN, size));
        l.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        return l;
    }
    // HỆ SỐ LOẠI VÉ
    private double layHeSoLoaiVe(String loaiVe) {
        switch(loaiVe) {
            case "Trẻ em": return 0.5;
            case "Sinh viên": return 0.8;
            case "Người cao tuổi": return 0.7;
            default: return 1.0; // Người lớn
        }
    }

        // Tạo panel thông tin vé
        private JPanel taoThongTinVe(ThongTinVe.ThongTinHanhKhach hk, entity.Ghe ghe, 
                                 entity.ChuyenTau ct, double giaVe) {
        JPanel p = new JPanel();
        p.setBackground(new java.awt.Color(255, 255, 255));
        p.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(200, 200, 200)),
            javax.swing.BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        p.setLayout(new javax.swing.BoxLayout(p, javax.swing.BoxLayout.Y_AXIS));
        p.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 170));

        // Thông tin ghế và toa
        int soHieuToa = ghe.getKhoangTau().getToaTau().getSoHieuToa();
        int soGhe = ghe.getSoGhe();
        String loaiGhe = ghe.getLoaiGhe().getTenLoaiGhe();

        // Tạo các label
        javax.swing.JLabel lblHoTen = taoLabel("Họ tên: " + hk.getHoTen(), 15, true);

        javax.swing.JLabel lblDoiTuong = taoLabel("Đối tượng: " + hk.getLoaiVe(), 15, false);

        String giayCCCD = (hk.getCCCD() == null || hk.getCCCD().isEmpty()) ? "Trẻ em" : hk.getCCCD();
        javax.swing.JLabel lblGiayTo = taoLabel("Số giấy tờ: " + giayCCCD, 15, false);

        javax.swing.JLabel lblHanhTrinh = taoLabel("Hành trình: " + ct.getTuyenDuong().getGaDi().getTenGa() + " → " + ct.getTuyenDuong().getGaDen().getTenGa() + " " + FormatUtil.formatDateTime(ct.getThoiGianDi()), 15, true);

        javax.swing.JLabel lblViTri = taoLabel("Vị trí: Toa " + soHieuToa + " - Ghế " + soGhe + " (" + loaiGhe + ")", 15, false);

        // Label giá vé
        javax.swing.JLabel lblGia = taoLabel("Giá vé: " + utils.FormatUtil.formatCurrency(giaVe), 16, true);
        lblGia.setForeground(new java.awt.Color(255, 51, 0));

        // Thêm vào panel
        p.add(lblHoTen);
        p.add(javax.swing.Box.createVerticalStrut(5));
        p.add(lblDoiTuong);
        p.add(javax.swing.Box.createVerticalStrut(3));
        p.add(lblGiayTo);
        p.add(javax.swing.Box.createVerticalStrut(3));
        p.add(lblHanhTrinh);
        p.add(javax.swing.Box.createVerticalStrut(3));
        p.add(lblViTri);
        p.add(javax.swing.Box.createVerticalStrut(8));
        p.add(lblGia);

        return p;
    }
        
    // Phương thức xử lý thanh toán
    private void xuLyThanhToan() {
        // 1. Kiểm tra dữ liệu đầu vào (ví dụ: tiền nhận đủ không, từ jTextField1)
        double tienNhan = 0;
        try {
            tienNhan = Double.parseDouble(jTextField1.getText().replaceAll("[^0-9.]", "")); // Giả sử jTextField1 là ô nhập tiền nhận
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền hợp lệ!");
            return;
        }

        double tongTien = Double.parseDouble(jLabel2.getText().replaceAll("[^0-9.]", "")); // Tổng tiền từ label
        if (tienNhan < tongTien) {
            JOptionPane.showMessageDialog(this, "Số tiền nhận không đủ!");
            return;
        }

        // 2. Tạo KhachHang
        KhachHang khachHang = new KhachHang();
        KhachHang_DAO khachHangDAO = new KhachHang_DAO(); // Giả sử bạn có DAO này
        khachHang.setMaKH(khachHangDAO.generateID()); // Sinh ID
        khachHang.setTenKH(hoTenKhach);
        khachHang.setSoDienThoai(sdtKhach);
        khachHang.setCccd(cccdKhach);
        khachHangDAO.create(khachHang); // Lưu vào DB

        // 3. Tạo list HanhKhach và Ve
        ArrayList<Ve> dsVe = new ArrayList<>();
        HanhKhach_DAO hanhKhachDAO = new HanhKhach_DAO(); // Giả sử có DAO
        Ve_DAO veDAO = new Ve_DAO();
        Ghe_DAO gheDAO = new Ghe_DAO();

        double tongTienVe = 0;
        for (ThongTinVe.ThongTinHanhKhach hk : dsHanhKhach) {
            // Tạo HanhKhach
            HanhKhach hanhKhach = new HanhKhach();
            hanhKhach.setMaHanhKhach(hanhKhachDAO.generateID());
            hanhKhach.setTenHanhKhach(hk.getHoTen());
            hanhKhach.setCccd(hk.getCCCD());
            hanhKhach.setNgaySinh(hk.getNgaySinh() != null ? hk.getNgaySinh().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null);
            hanhKhachDAO.create(hanhKhach);

            // Tìm ghế
            Ghe ghe = bus.timGheTheoMa(hk.getMaGhe());
            if (ghe == null) continue;

            // Xác định chuyến tàu
            ChuyenTau chuyenTau = hk.getMaChuyenTau().equals(chuyenDi.getMaChuyenTau()) ? chuyenDi : chuyenVe;

            // Tạo Ve
            Ve ve = new Ve();
            ve.setMaVe(veDAO.generateID());
            ve.setChuyenTau(chuyenTau);
            ve.setHanhKhach(hanhKhach);
            ve.setGhe(ghe);
            // ve.setHoaDon(hoaDon); // Sẽ set sau khi tạo HoaDon
            ve.setTrangThai(TrangThaiVe.DA_THANH_TOAN); // Hoặc từInt(1)

            // Tìm loại vé
            LoaiVe loaiVe = bus.timLoaiVeTheoTen(hk.getLoaiVe()); // Giả sử bus có method này
            ve.setLoaiVe(loaiVe);

            // Tính giá vé
            double giaVe = tinhGiaVe(hk, ghe, chuyenTau); // Method có sẵn trong class của bạn
            ve.setGiaVe(giaVe);
            tongTienVe += giaVe;

            veDAO.create(ve); // Lưu vé (chưa có maHoaDon)
            dsVe.add(ve);

            // Cập nhật trạng thái ghế
            gheDAO.capNhatTrangThaiGhe(ghe.getMaGhe()); // Đặt thành đã đặt (2)
        }

        // 4. Tạo HoaDon
        HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon(hoaDonDAO.generateID());
        hoaDon.setNhanVien(nhanVienDangNhap); // Nhân viên hiện tại
        hoaDon.setKhachHang(khachHang);
        hoaDon.setNgayLapHoaDon(LocalDateTime.now());
        hoaDon.setVAT(0.1); // 10% VAT
        hoaDon.setKhuyenMai(null); // Nếu có, lấy từ input
        hoaDon.setTongTien(tongTienVe);
        hoaDon.setThanhTien(tongTienVe * (1 + hoaDon.getVAT())); // Tổng sau VAT và khuyến mãi
        hoaDonDAO.create(hoaDon);

        // 5. Cập nhật maHoaDon cho từng Ve
        for (Ve ve : dsVe) {
            ve.setHoaDon(hoaDon);
            veDAO.update(ve.getMaVe(), ve); // Cập nhật vé với maHoaDon
        }

        // 6. In hóa đơn và vé
        CreateOrder.taoHD(dsVe, hoaDon); // In hóa đơn PDF
        for (Ve ve : dsVe) {
            CreateTicket.taoVe(ve); // In từng vé PDF
        }

        // 7. Thông báo thành công và reset form nếu cần
        JOptionPane.showMessageDialog(this, "Thanh toán thành công! Hóa đơn và vé đã được in.");
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
