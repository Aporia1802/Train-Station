package gui.custom;

import entity.*;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class XacNhanThanhToan extends JDialog {
    private JTextField txt_tienKhachDua;
    private JLabel lbl_tongTien;
    private JLabel lbl_vat;
    private JLabel lbl_giamGia;
    private JLabel lbl_thanhTien;
    private JLabel lbl_tienThua;
    private JTextArea txt_thongTinVe;
    private JButton btn_thanhToan;
    private JButton btn_huy;
    
    private boolean daThanhToan = false;
    private double tongTien;
    private double thanhTien;
    private DecimalFormat df = new DecimalFormat("#,###");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public XacNhanThanhToan(Frame parent, ChuyenTau chuyenTau, 
                                    ArrayList<Ve> dsVe, KhachHang khachHang,
                                    double tongTien, double thanhTien) {
        super(parent, "Xác nhận thanh toán", true);
        this.tongTien = tongTien;
        this.thanhTien = thanhTien;
        
        initComponents(chuyenTau, dsVe, khachHang);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents(ChuyenTau chuyenTau, ArrayList<Ve> dsVe, KhachHang khachHang) {
        setLayout(new BorderLayout(10, 10));
        setSize(600, 700);
        
        // Panel thông tin
        JPanel pnl_thongTin = new JPanel();
        pnl_thongTin.setLayout(new BoxLayout(pnl_thongTin, BoxLayout.Y_AXIS));
        pnl_thongTin.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Tiêu đề
        JLabel lbl_title = new JLabel("XÁC NHẬN THÔNG TIN ĐẶT VÉ");
        lbl_title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl_title.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnl_thongTin.add(lbl_title);
        pnl_thongTin.add(Box.createVerticalStrut(20));
        
        // Thông tin khách hàng
        JPanel pnl_khachHang = createInfoPanel("Thông tin khách hàng",
            "Họ tên: " + khachHang.getTenKH() + "\n" +
            "CCCD: " + khachHang.getCccd() + "\n" +
            "SĐT: " + khachHang.getSoDienThoai()
        );
        pnl_thongTin.add(pnl_khachHang);
        pnl_thongTin.add(Box.createVerticalStrut(10));
        
        // Thông tin chuyến tàu
        JPanel pnl_chuyenTau = createInfoPanel("Thông tin chuyến tàu",
            "Mã chuyến: " + chuyenTau.getMaChuyenTau() + "\n" +
            "Tàu: " + chuyenTau.getTau().getTenTau() + "\n" +
            "Ga đi: " + chuyenTau.getTuyenDuong().getGaDi().getTenGa() + "\n" +
            "Ga đến: " + chuyenTau.getTuyenDuong().getGaDen().getTenGa() + "\n" +
            "Thời gian đi: " + chuyenTau.getThoiGianDi().format(dtf) + "\n" +
            "Thời gian đến: " + chuyenTau.getThoiGianDen().format(dtf)
        );
        pnl_thongTin.add(pnl_chuyenTau);
        pnl_thongTin.add(Box.createVerticalStrut(10));
        
        // Thông tin vé
        StringBuilder veInfo = new StringBuilder();
        for (int i = 0; i < dsVe.size(); i++) {
            Ve ve = dsVe.get(i);
            veInfo.append("Vé ").append(i + 1).append(": ")
                  .append(ve.getHanhKhach().getTenHanhKhach())
                  .append(" - Toa ").append(ve.getGhe().getKhoangTau().getToaTau().getSoHieuToa())
                  .append(" - Ghế ").append(ve.getGhe().getSoGhe())
                  .append(" - ").append(ve.getLoaiVe().getTenLoaiVe())
                  .append(" - ").append(df.format(ve.getGiaVe())).append(" VNĐ\n");
        }
        
        JPanel pnl_ve = createInfoPanel("Chi tiết vé (" + dsVe.size() + " vé)", veInfo.toString());
        pnl_thongTin.add(pnl_ve);
        
        // Panel thanh toán
        JPanel pnl_thanhToan = new JPanel();
        pnl_thanhToan.setLayout(new BoxLayout(pnl_thanhToan, BoxLayout.Y_AXIS));
        pnl_thanhToan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Thanh toán",
                0, 0, new Font("Segoe UI", Font.BOLD, 14)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Tổng tiền
        JPanel pnl_tongTien = new JPanel(new BorderLayout());
        pnl_tongTien.add(new JLabel("Tổng tiền:"), BorderLayout.WEST);
        lbl_tongTien = new JLabel(df.format(tongTien) + " VNĐ");
        lbl_tongTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnl_tongTien.add(lbl_tongTien, BorderLayout.EAST);
        pnl_thanhToan.add(pnl_tongTien);
        pnl_thanhToan.add(Box.createVerticalStrut(5));
        
        // VAT
        JPanel pnl_vat = new JPanel(new BorderLayout());
        pnl_vat.add(new JLabel("VAT (10%):"), BorderLayout.WEST);
        lbl_vat = new JLabel(df.format(tongTien * 0.1) + " VNĐ");
        pnl_vat.add(lbl_vat, BorderLayout.EAST);
        pnl_thanhToan.add(pnl_vat);
        pnl_thanhToan.add(Box.createVerticalStrut(5));
        
        // Giảm giá
        JPanel pnl_giamGia = new JPanel(new BorderLayout());
        pnl_giamGia.add(new JLabel("Giảm giá:"), BorderLayout.WEST);
        double giamGia = tongTien * 1.1 - thanhTien;
        lbl_giamGia = new JLabel("- " + df.format(giamGia) + " VNĐ");
        lbl_giamGia.setForeground(new Color(220, 53, 69));
        pnl_giamGia.add(lbl_giamGia, BorderLayout.EAST);
        pnl_thanhToan.add(pnl_giamGia);
        pnl_thanhToan.add(Box.createVerticalStrut(10));
        
        // Thành tiền
        JPanel pnl_thanhTien = new JPanel(new BorderLayout());
        pnl_thanhTien.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        JLabel lbl_title_thanhTien = new JLabel("Thành tiền:");
        lbl_title_thanhTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pnl_thanhTien.add(lbl_title_thanhTien, BorderLayout.WEST);
        lbl_thanhTien = new JLabel(df.format(thanhTien) + " VNĐ");
        lbl_thanhTien.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl_thanhTien.setForeground(new Color(220, 53, 69));
        pnl_thanhTien.add(lbl_thanhTien, BorderLayout.EAST);
        pnl_thanhToan.add(pnl_thanhTien);
        pnl_thanhToan.add(Box.createVerticalStrut(15));
        
        // Tiền khách đưa
        JPanel pnl_tienKhach = new JPanel(new BorderLayout(10, 0));
        pnl_tienKhach.add(new JLabel("Tiền khách đưa:"), BorderLayout.WEST);
        txt_tienKhachDua = new JTextField();
        txt_tienKhachDua.setPreferredSize(new Dimension(200, 35));
        txt_tienKhachDua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tinhTienThua();
            }
        });
        pnl_tienKhach.add(txt_tienKhachDua, BorderLayout.CENTER);
        pnl_thanhToan.add(pnl_tienKhach);
        pnl_thanhToan.add(Box.createVerticalStrut(10));
        
        // Tiền thừa
        JPanel pnl_tienThua = new JPanel(new BorderLayout());
        pnl_tienThua.add(new JLabel("Tiền thừa:"), BorderLayout.WEST);
        lbl_tienThua = new JLabel("0 VNĐ");
        lbl_tienThua.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl_tienThua.setForeground(new Color(40, 167, 69));
        pnl_tienThua.add(lbl_tienThua, BorderLayout.EAST);
        pnl_thanhToan.add(pnl_tienThua);
        
        pnl_thongTin.add(Box.createVerticalStrut(10));
        pnl_thongTin.add(pnl_thanhToan);
        
        // Buttons
        JPanel pnl_buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btn_huy = new JButton("Hủy");
        btn_huy.setPreferredSize(new Dimension(100, 40));
        btn_huy.addActionListener(e -> {
            daThanhToan = false;
            dispose();
        });
        
        btn_thanhToan = new JButton("Thanh toán");
        btn_thanhToan.setPreferredSize(new Dimension(120, 40));
        btn_thanhToan.setBackground(new Color(40, 167, 69));
        btn_thanhToan.setForeground(Color.WHITE);
        btn_thanhToan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn_thanhToan.addActionListener(e -> xacNhanThanhToan());
        
        pnl_buttons.add(btn_huy);
        pnl_buttons.add(btn_thanhToan);
        
        // Add to dialog
        JScrollPane scrollPane = new JScrollPane(pnl_thongTin);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        add(pnl_buttons, BorderLayout.SOUTH);
    }
    
    private JPanel createInfoPanel(String title, String content) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                title,
                0, 0, new Font("Segoe UI", Font.BOLD, 13)
            ),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setBackground(panel.getBackground());
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(textArea, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void tinhTienThua() {
        try {
            String text = txt_tienKhachDua.getText().trim().replaceAll("[,.]", "");
            if (text.isEmpty()) {
                lbl_tienThua.setText("0 VNĐ");
                return;
            }
            
            double tienKhach = Double.parseDouble(text);
            double tienThua = tienKhach - thanhTien;
            
            if (tienThua < 0) {
                lbl_tienThua.setText("Chưa đủ tiền");
                lbl_tienThua.setForeground(Color.RED);
            } else {
                lbl_tienThua.setText(df.format(tienThua) + " VNĐ");
                lbl_tienThua.setForeground(new Color(40, 167, 69));
            }
        } catch (NumberFormatException e) {
            lbl_tienThua.setText("Số tiền không hợp lệ");
            lbl_tienThua.setForeground(Color.RED);
        }
    }
    
    private void xacNhanThanhToan() {
        try {
            String text = txt_tienKhachDua.getText().trim().replaceAll("[,.]", "");
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng nhập số tiền khách đưa!", 
                    "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
                txt_tienKhachDua.requestFocus();
                return;
            }
            
            double tienKhach = Double.parseDouble(text);
            if (tienKhach < thanhTien) {
                JOptionPane.showMessageDialog(this, 
                    "Số tiền khách đưa chưa đủ!", 
                    "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
                txt_tienKhachDua.requestFocus();
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận thanh toán " + df.format(thanhTien) + " VNĐ?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                daThanhToan = true;
                dispose();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Số tiền không hợp lệ!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            txt_tienKhachDua.requestFocus();
        }
    }
    
    public boolean isDaThanhToan() {
        return daThanhToan;
    }
}