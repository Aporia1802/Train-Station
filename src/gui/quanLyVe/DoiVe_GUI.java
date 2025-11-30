package gui.quanLyVe;

import bus.QuanLyDatVe_BUS;
import entity.ChuyenTau;
import entity.Ve;
import gui.components.ChonChoNgoi;
import gui.components.ThanhToan;
import gui.components.ChonChuyenTau;
import gui.components.TimVeDoi;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

/**
 * GUI Quản lý vé thống nhất - Xử lý cả Đặt vé và Đổi vé
 * Chỉ khác nhau bước 1, các bước sau dùng chung
 */
public class DoiVe_GUI extends javax.swing.JPanel {
    private final QuanLyDatVe_BUS bus;
    
    // Animation
    private JLayeredPane slidePane;
    private int currentIndex = 0;
    private JPanel[] panels;
    private Animator animator;
    private boolean forward = true;
    private float fraction = 0f;
    private JPanel currentPanel, nextPanel;

    // Components
    private JPanel buoc1Panel; 
    private ChonChoNgoi chonChoNgoi;
    private ThanhToan thanhToan;
    
    // State
    private boolean isDoiVe = true; // false = Đặt vé, true = Đổi vé
    private Ve veCanDoi = null;

    /**
     * Constructor - mặc định là Đặt vé
     */
    public DoiVe_GUI() {
        this(true);
    }
    
    /**
     * Constructor có tham số
     * @param isDoiVe true = Đổi vé, false = Đặt vé
     */
    public DoiVe_GUI(boolean isDoiVe) {
        this.isDoiVe = isDoiVe;
        setLayout(new BorderLayout());
        
        bus = new QuanLyDatVe_BUS();
        
        // Khởi tạo components dùng chung
        chonChoNgoi = new ChonChoNgoi(bus);
        thanhToan = new ThanhToan(bus);
        
        // Khởi tạo bước 1 tùy theo chế độ
        if (isDoiVe) {
            buoc1Panel = new TimVeDoi(bus);
        } else {
            buoc1Panel = new ChonChuyenTau(bus);
        }
        
        thanhToan.setOnThanhToanThanhCong(() -> {
            resetToiDau();
        });

        // Setup animation
        setupAnimation();
        
        // Setup navigation
        initNavigation();
    }
    
    /**
     * Setup animation slider
     */
    private void setupAnimation() {
        panels = new JPanel[]{buoc1Panel, chonChoNgoi, thanhToan};
        
        slidePane = new JLayeredPane();
        slidePane.setLayout(null);
        slidePane.setDoubleBuffered(true);
        add(slidePane, BorderLayout.CENTER);

        JPanel first = panels[0];
        first.setBounds(0, 0, getWidth(), getHeight());
        slidePane.add(first, Integer.valueOf(0));

        animator = new Animator(600);
        animator.setResolution(5);
        animator.setAcceleration(0.2f);
        animator.setDeceleration(0.2f);
        animator.addTarget(new TimingTargetAdapter() {
            @Override
            public void timingEvent(float f) {
                fraction = (float) (0.5 - 0.5 * Math.cos(Math.PI * f));
                updateAnimation();
            }

            @Override
            public void end() {
                finishAnimation();
            }
        });
    }



    /**
     * Khởi tạo navigation cho cả đặt vé và đổi vé
     */
    private void initNavigation() {
        if (isDoiVe) {
            // LOGIC ĐỔI VÉ
            TimVeDoi timVeDoi = (TimVeDoi) buoc1Panel;

            timVeDoi.next().addActionListener(e -> {
                if (validateBuoc1DoiVe(timVeDoi)) {
                    try {
                        veCanDoi = timVeDoi.getVeCanDoi();

                        //  Set vé cần đổi vào ChonChoNgoi 
                        chonChoNgoi.setVeCanDoi(veCanDoi);

                        // Load ghế của chuyến thay thế
                        chonChoNgoi.loadDanhSachGhe(
                            timVeDoi.getChuyenDiDaChon(), 
                            null, 
                            false
                        );

                        // Tự động điền thông tin khách hàng từ vé cũ
                        if (veCanDoi != null && veCanDoi.getHoaDon() != null) {
                            chonChoNgoi.getTxt_hoTen().setText(
                                veCanDoi.getHoaDon().getKhachHang().getTenKH()
                            );
                            chonChoNgoi.getTxt_sdt().setText(
                                veCanDoi.getHoaDon().getKhachHang().getSoDienThoai()
                            );
                            chonChoNgoi.getTxt_cccd().setText(
                                veCanDoi.getHoaDon().getKhachHang().getCccd()
                            );
                        }

                        next();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this,
                            "Có lỗi khi tải thông tin ghế: " + ex.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

        } else {
            // LOGIC ĐẶT VÉ (giữ nguyên)
            ChonChuyenTau chonChuyenTau = (ChonChuyenTau) buoc1Panel;

            chonChuyenTau.next().addActionListener(e -> {
                if (validateBuoc1DatVe(chonChuyenTau)) {
                    try {
                        chonChoNgoi.loadDanhSachGhe(
                            chonChuyenTau.getChuyenDiDaChon(), 
                            chonChuyenTau.getChuyenVeDaChon(), 
                            chonChuyenTau.isKhuHoi()
                        );
                        next();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this,
                            "Có lỗi khi tải thông tin ghế: " + ex.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }

        // BƯỚC 2 -> 3: CHUNG CHO CẢ HAI
        chonChoNgoi.next().addActionListener(e -> {
            if (!chonChoNgoi.validateThongTin()) {
                return;
            }

            String hoTen = chonChoNgoi.getHoTenKhachChinh();
            String sdt = chonChoNgoi.getSoDienThoai();
            String cccd = chonChoNgoi.getCCCD();

            java.util.ArrayList<gui.components.ThongTinVe.ThongTinHanhKhach> dsHK = 
                chonChoNgoi.getDanhSachThongTinHanhKhach();

            ChuyenTau chuyenDi, chuyenVe;
            boolean khuHoi;

            if (isDoiVe) {
                TimVeDoi timVeDoi = (TimVeDoi) buoc1Panel;
                chuyenDi = timVeDoi.getChuyenDiDaChon();
                chuyenVe = null;
                khuHoi = false;
            } else {
                ChonChuyenTau chonChuyenTau = (ChonChuyenTau) buoc1Panel;
                chuyenDi = chonChuyenTau.getChuyenDiDaChon();
                chuyenVe = chonChuyenTau.getChuyenVeDaChon();
                khuHoi = chonChuyenTau.isKhuHoi();
            }
            
            // Nếu là đổi vé, set thêm thông tin vé cũ vào ThanhToan
            if (isDoiVe) {
                thanhToan.setVeCanDoi(veCanDoi);
            }
            
            thanhToan.hienThiDuLieu(hoTen, sdt, cccd, dsHK, chuyenDi, chuyenVe, khuHoi);

            
            next();
        });

        // QUAY LẠI: CHUNG CHO CẢ HAI
        chonChoNgoi.previous().addActionListener(e -> previous());
        thanhToan.previous().addActionListener(e -> previous());
    }
    
    /**
     * Validate bước 1 - Đặt vé
     */
    private boolean validateBuoc1DatVe(ChonChuyenTau chonChuyenTau) {
        if (chonChuyenTau.getChuyenDiDaChon() == null) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn chuyến tàu đi!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
    /**
     * Validate bước 1 - Đổi vé
     */
    private boolean validateBuoc1DoiVe(TimVeDoi timVeDoi) {
        if (timVeDoi.getVeCanDoi() == null) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng tìm và chọn vé cần đổi!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (timVeDoi.getChuyenDiDaChon() == null) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn chuyến tàu thay thế!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }

    // ========== ANIMATION METHODS ==========
    
    private void next() {
        if (!animator.isRunning() && currentIndex < panels.length - 1) {
            forward = true;
            startAnimation(panels[currentIndex], panels[currentIndex + 1]);
        }
    }

    private void previous() {
        if (!animator.isRunning() && currentIndex > 0) {
            forward = false;
            startAnimation(panels[currentIndex], panels[currentIndex - 1]);
        }
    }

    private void startAnimation(JPanel current, JPanel next) {
        this.currentPanel = current;
        this.nextPanel = next;

        int w = slidePane.getWidth();
        int h = slidePane.getHeight();
        if (w == 0 || h == 0) {
            SwingUtilities.invokeLater(() -> startAnimation(current, next));
            return;
        }

        current.setBounds(0, 0, w, h);
        next.setBounds(forward ? w : -w, 0, w, h);

        slidePane.removeAll();
        slidePane.add(current, Integer.valueOf(0));
        slidePane.add(next, Integer.valueOf(1));

        animator.start();
    }

    private void updateAnimation() {
        int w = slidePane.getWidth();
        int offset = (int) (w * fraction);
        if (forward) {
            currentPanel.setLocation(-offset, 0);
            nextPanel.setLocation(w - offset, 0);
        } else {
            currentPanel.setLocation(offset, 0);
            nextPanel.setLocation(-w + offset, 0);
        }
        slidePane.repaint();
    }

    private void finishAnimation() {
        currentIndex += forward ? 1 : -1;
        slidePane.removeAll();
        JPanel current = panels[currentIndex];
        current.setBounds(0, 0, slidePane.getWidth(), slidePane.getHeight());
        slidePane.add(current, Integer.valueOf(0));
        slidePane.repaint();
    }
    
    /**
     * Reset về bước đầu
     */
    public void resetToiDau() {
        bus.clearDanhSachGheDaChon();
        veCanDoi = null;

        try {
            // Tạo lại components
            chonChoNgoi = new ChonChoNgoi(bus);
            thanhToan = new ThanhToan(bus);

            if (isDoiVe) {
                buoc1Panel = new TimVeDoi(bus);
            } else {
                buoc1Panel = new ChonChuyenTau(bus);
            }

            // ===== QUAN TRỌNG: Reset vé cần đổi trong ChonChoNgoi =====
            chonChoNgoi.resetVeCanDoi();

            thanhToan.setOnThanhToanThanhCong(() -> resetToiDau());

            panels[0] = buoc1Panel;
            panels[1] = chonChoNgoi;
            panels[2] = thanhToan;

            initNavigation();

            currentIndex = 0;
            slidePane.removeAll();
            JPanel first = panels[0];
            first.setBounds(0, 0, slidePane.getWidth(), slidePane.getHeight());
            slidePane.add(first, Integer.valueOf(0));
            slidePane.revalidate();
            slidePane.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Có lỗi khi reset form: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void doLayout() {
        super.doLayout();
        if (slidePane != null && slidePane.getComponentCount() > 0) {
            Component c = slidePane.getComponent(0);
            c.setBounds(0, 0, getWidth(), getHeight());
        }
    }
}