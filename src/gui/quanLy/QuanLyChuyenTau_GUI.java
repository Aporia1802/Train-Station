/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.quanLy;

import bus.QuanLyChuyenTau_BUS;
import dao.Tau_DAO;
import dao.TuyenDuong_DAO;
import entity.ChuyenTau;
import entity.GaTau;
import entity.Tau;
import entity.TuyenDuong;
import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.time.ZoneId; 

/**
 *
 * @author PC
 */
public class QuanLyChuyenTau_GUI extends javax.swing.JPanel {

    private DefaultTableModel tblModel_thongTinChuyenTau;
    private QuanLyChuyenTau_BUS bus;
    private DateTimeFormatter dateTimeFormatter;
    private ArrayList<ChuyenTau> dsChuyenTau;

    /**
     * Creates new form QuanLyChuyenDi_GUI
     */
    public QuanLyChuyenTau_GUI() {
        initComponents();
        init();
    }
  private void init() {
    bus = new QuanLyChuyenTau_BUS();
    dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
 
    tblModel_thongTinChuyenTau = new DefaultTableModel(
        new String[] {"Mã chuyến tàu", "Tuyến đường", "Thời gian đi", "Thời gian đến", "Tàu", "Ghế đã đặt", "Ghế còn trống"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    tbl_thongTinCT.setModel(tblModel_thongTinChuyenTau);
    getTableData(bus.getAllChuyenTau());
    loadComboBoxData();
      loadComboBoxTuyenDuong();  
        loadComboBoxTau(); 
    setupPlaceholder();
}
    private void loadComboBoxGaDiDen() {
        ArrayList<ChuyenTau> dsChuyenTau = bus.getAllChuyenTau();
        ArrayList<String> dsGaDi = new ArrayList<>();
        ArrayList<String> dsGaDen = new ArrayList<>();

        dsGaDi.add("Tất cả");
        dsGaDen.add("Tất cả");
        for (ChuyenTau ct : dsChuyenTau) {
            if (ct.getTuyenDuong() != null) {
                GaTau gaDi = ct.getTuyenDuong().getGaDi();
                GaTau gaDen = ct.getTuyenDuong().getGaDen();

                if (gaDi != null && gaDi.getMaGa() != null && !dsGaDi.contains(gaDi.getMaGa())) {
                    dsGaDi.add(gaDi.getMaGa());
                }

                if (gaDen != null && gaDen.getMaGa() != null && !dsGaDen.contains(gaDen.getMaGa())) {
                    dsGaDen.add(gaDen.getMaGa());
                }
            }
        }

        cb_GaDi.setModel(new DefaultComboBoxModel<>(dsGaDi.toArray(new String[0])));
        cb_GaDen.setModel(new DefaultComboBoxModel<>(dsGaDen.toArray(new String[0])));
    }
  private void loadComboBoxTuyenDuong() {
    try {
        TuyenDuong_DAO tuyenDuongDAO = new TuyenDuong_DAO();
        ArrayList<TuyenDuong> dsTuyenDuong = tuyenDuongDAO.getAll();

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("-- Chọn tuyến đường --");

        for (TuyenDuong td : dsTuyenDuong) {
            if (td != null) {
        
                String tenGaDi = (td.getGaDi() != null && td.getGaDi().getTenGa() != null) 
                               ? td.getGaDi().getTenGa() 
                               : td.getGaDi() != null ? td.getGaDi().getMaGa() : "N/A";
                
                String tenGaDen = (td.getGaDen() != null && td.getGaDen().getTenGa() != null) 
                                ? td.getGaDen().getTenGa() 
                                : td.getGaDen() != null ? td.getGaDen().getMaGa() : "N/A";
         
                String display = td.getMaTuyenDuong() + " - " + tenGaDi + " → " + tenGaDen;
                model.addElement(display);
            }
        }

        cb_TuyenDuong.setModel(model);

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    private void loadComboBoxTau() {
        try {
            Tau_DAO tauDAO = new Tau_DAO();
            ArrayList<Tau> dsTau = tauDAO.getAll();

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("-- Chọn tàu --");

            for (Tau tau : dsTau) {
                String display = tau.getMaTau() + " - " +
                        tau.getTenTau() + " (" +
                        tau.getSucChua() + " chỗ)";
                model.addElement(display);
            }

            cb_Tau.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
      private String getMaFromComboBox(javax.swing.JComboBox<String> comboBox) {
        String selected = (String) comboBox.getSelectedItem();
        if (selected == null || selected.startsWith("--")) {
            return null;
        }
        return selected.split(" - ")[0];
    }
        private void setComboBoxByMa(javax.swing.JComboBox<String> comboBox, String ma) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            String item = comboBox.getItemAt(i);
            if (item.startsWith(ma + " - ")) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.setSelectedIndex(0);
    }
         private int getSucChuaTau(String maTau) {
        try {
            Tau_DAO tauDAO = new Tau_DAO();
            Tau tau = tauDAO.getOne(maTau);
            return tau != null ? tau.getSucChua() : 600;
        } catch (Exception e) {
            return 600;
        }
    }
private void loadComboBoxData() {
    ArrayList<ChuyenTau> dsChuyenTau = bus.getAllChuyenTau();
    ArrayList<String> dsGaDi = new ArrayList<>();
    ArrayList<String> dsGaDen = new ArrayList<>();
    
    dsGaDi.add("Tất cả");
    dsGaDen.add("Tất cả");
    for (ChuyenTau ct : dsChuyenTau) {
        if (ct.getTuyenDuong() != null) {
            GaTau gaDi = ct.getTuyenDuong().getGaDi();
            GaTau gaDen = ct.getTuyenDuong().getGaDen();    
            if (gaDi != null && gaDi.getMaGa() != null && !dsGaDi.contains(gaDi.getMaGa())) {
                dsGaDi.add(gaDi.getMaGa());
            }
            
            if (gaDen != null && gaDen.getMaGa() != null && !dsGaDen.contains(gaDen.getMaGa())) {
                dsGaDen.add(gaDen.getMaGa());
            }
        }
    }
    
    cb_GaDi.setModel(new DefaultComboBoxModel<>(dsGaDi.toArray(new String[0])));
    cb_GaDen.setModel(new DefaultComboBoxModel<>(dsGaDen.toArray(new String[0])));
}
  private void setupPlaceholder() {
    txt_timKiem.setText("Nhập mã chuyến tàu cần tìm...");
    txt_timKiem.setForeground(Color.GRAY);
    
    txt_timKiem.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (txt_timKiem.getText().equals("Nhập mã chuyến tàu cần tìm...")) {
                txt_timKiem.setText("");
                txt_timKiem.setForeground(Color.BLACK);
            }
        }
        public void focusLost(java.awt.event.FocusEvent evt) {
            if (txt_timKiem.getText().isEmpty()) {
                txt_timKiem.setText("Nhập mã chuyến tàu cần tìm...");
                txt_timKiem.setForeground(Color.GRAY);
            }
        }
    });
}
  
  private void getTableData(ArrayList<ChuyenTau> dsChuyenTau) {
        tblModel_thongTinChuyenTau.setRowCount(0);
        
        for (ChuyenTau ct : dsChuyenTau) {
            Object[] row = {
                ct.getMaChuyenTau(),
                ct.getTuyenDuong().getMaTuyenDuong(),
                ct.getThoiGianDi().format(dateTimeFormatter),
                ct.getThoiGianDen().format(dateTimeFormatter),
                ct.getTau().getMaTau(),
                ct.getSoGheDaDat(),
                ct.getSoGheConTrong()
            };
            tblModel_thongTinChuyenTau.addRow(row);
        }
}
     private void getThongTinChuyenTau() {
        int row = tbl_thongTinCT.getSelectedRow();
        if (row == -1) return;

        try {
            txt_maCT.setText(tbl_thongTinCT.getValueAt(row, 0).toString());
            String maTuyenDuong = tbl_thongTinCT.getValueAt(row, 1).toString();
            setComboBoxByMa(cb_TuyenDuong, maTuyenDuong);
            String maTau = tbl_thongTinCT.getValueAt(row, 4).toString();
            setComboBoxByMa(cb_Tau, maTau);

            txt_soGheDaDat.setText(tbl_thongTinCT.getValueAt(row, 5).toString());
            txt_soGheTrong.setText(tbl_thongTinCT.getValueAt(row, 6).toString());
            LocalDateTime thoiGianDi = LocalDateTime.parse(tbl_thongTinCT.getValueAt(row, 2).toString(), dateTimeFormatter);
            LocalDateTime thoiGianDen = LocalDateTime.parse(tbl_thongTinCT.getValueAt(row, 3).toString(), dateTimeFormatter);

            date_thoiGianDi.setDate(Date.from(thoiGianDi.atZone(ZoneId.systemDefault()).toInstant()));
            date_thoiGianDen.setDate(Date.from(thoiGianDen.atZone(ZoneId.systemDefault()).toInstant()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
 private void handleActionXoaTrang() {
        tbl_thongTinCT.clearSelection();
        txt_maCT.setText("");
        cb_TuyenDuong.setSelectedIndex(0);  
        cb_Tau.setSelectedIndex(0);  
        txt_soGheDaDat.setText("");
        txt_soGheTrong.setText("");
        date_thoiGianDi.setDate(new Date());
        date_thoiGianDen.setDate(new Date());
    }
         private void handleActionLamMoi() {
        handleActionXoaTrang();
        getTableData(bus.getAllChuyenTau());
        txt_timKiem.setText("Nhập mã chuyến tàu cần tìm...");
        txt_timKiem.setForeground(Color.GRAY);
        cb_GaDi.setSelectedIndex(0);
        cb_GaDen.setSelectedIndex(0);
    }
      private void handleActionTimKiem() {
        String keyword = txt_timKiem.getText().trim();
        
        if (keyword.equals("Nhập mã chuyến tàu cần tìm...") || keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }
        
        ArrayList<ChuyenTau> ketQua = bus.getChuyenTauByKeyword(keyword);
        
        if (ketQua.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến tàu nào!");
            handleActionLamMoi();
        } else {
            getTableData(ketQua);
        }
    }
  private void handleActionThem() {
        String maChuyenTau = bus.generateMaChuyenTau();

        String maTuyenDuong = getMaFromComboBox(cb_TuyenDuong);
        String maTau = getMaFromComboBox(cb_Tau);

        if (maTuyenDuong == null || maTau == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tuyến đường và tàu!");
            return;
        }
        if (date_thoiGianDi.getDate() == null || date_thoiGianDen.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thời gian đi và đến!");
            return;
        }
        try {
            LocalDateTime thoiGianDi = date_thoiGianDi.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime thoiGianDen = date_thoiGianDen.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            if (thoiGianDi.isBefore(LocalDateTime.now())) {
                JOptionPane.showMessageDialog(this, "Thời gian đi phải sau thời gian hiện tại!");
                return;
            }
            if (thoiGianDen.isBefore(thoiGianDi) || thoiGianDen.isEqual(thoiGianDi)) {
                JOptionPane.showMessageDialog(this, "Thời gian đến phải sau thời gian đi!");
                return;
            }

            ChuyenTau chuyenTau = new ChuyenTau(maChuyenTau, new TuyenDuong(maTuyenDuong), thoiGianDi, thoiGianDen, new Tau(maTau));
            chuyenTau.setSoGheDaDat(0);
            chuyenTau.setSoGheConTrong(getSucChuaTau(maTau)); 

            int confirm = JOptionPane.showConfirmDialog(this,
                "Thêm chuyến tàu mới?\n" +
                "Mã: " + maChuyenTau + "\n" +
                "Tuyến: " + maTuyenDuong + "\n" +
                "Tàu: " + maTau + "\n" +
                "Khởi hành: " + thoiGianDi.format(dateTimeFormatter) + "\n" +
                "Đến: " + thoiGianDen.format(dateTimeFormatter),
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (bus.createChuyenTau(chuyenTau)) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công!\nMã: " + maChuyenTau);
                    handleActionLamMoi();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
}
      private void handleActionCapNhat() {
    int row = tbl_thongTinCT.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn chuyến tàu cần cập nhật!");
        return;
    }
    String maChuyenTau = txt_maCT.getText().trim();
    String maTuyenDuong = getMaFromComboBox(cb_TuyenDuong);
    String maTau = getMaFromComboBox(cb_Tau);
    
    if (maChuyenTau.isEmpty() || maTuyenDuong.isEmpty() || maTau.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
        return;
    }
    if (date_thoiGianDi.getDate() == null || date_thoiGianDen.getDate() == null) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn thời gian đi và đến!");
        return;
    }
    try {
        LocalDateTime thoiGianDi = date_thoiGianDi.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime thoiGianDen = date_thoiGianDen.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();      
        if (thoiGianDen.isBefore(thoiGianDi) || thoiGianDen.isEqual(thoiGianDi)) {
            JOptionPane.showMessageDialog(this, "Thời gian đến phải sau thời gian đi!");
            return;
        }
        ChuyenTau chuyenTauCu = bus.getChuyenTauByMa(maChuyenTau);
        
        if (chuyenTauCu == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến tàu!");
            return;
        }
        ChuyenTau chuyenTau = new ChuyenTau(maChuyenTau, new TuyenDuong(maTuyenDuong), thoiGianDi, thoiGianDen, new Tau(maTau));
        chuyenTau.setSoGheDaDat(chuyenTauCu.getSoGheDaDat());
        chuyenTau.setSoGheConTrong(chuyenTauCu.getSoGheConTrong());
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Xác nhận cập nhật chuyến tàu " + maChuyenTau + "?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (bus.updateChuyenTau(maChuyenTau, chuyenTau)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                handleActionLamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        e.printStackTrace();
    }
}
    private void handleActionLoc() {
        String maGaDi = cb_GaDi.getSelectedItem().toString();
        String maGaDen = cb_GaDen.getSelectedItem().toString();
        
        if (maGaDi.equals("Tất cả") && maGaDen.equals("Tất cả")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một ga để lọc!");
            return;
        }
        
        ArrayList<ChuyenTau> ketQua = bus.locTheoGaDiGaDen(maGaDi, maGaDen);
        
        if (ketQua.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến tàu phù hợp!");
            handleActionLamMoi();
        } else {
            getTableData(ketQua);
            JOptionPane.showMessageDialog(this, "Tìm thấy " + ketQua.size() + " chuyến tàu!");
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

        jLabel1 = new javax.swing.JLabel();
        pnl_top = new javax.swing.JPanel();
        pnl_timKiem = new javax.swing.JPanel();
        txt_timKiem = new javax.swing.JTextField();
        pnl_btnTimKiem = new javax.swing.JPanel();
        btn_timKiem = new javax.swing.JButton();
        pnl_cta = new javax.swing.JPanel();
        cb_GaDi = new javax.swing.JComboBox<>();
        cb_GaDen = new javax.swing.JComboBox<>();
        btn_Loc = new javax.swing.JButton();
        btn_Reset = new javax.swing.JButton();
        pnl_center = new javax.swing.JPanel();
        spl_container = new javax.swing.JSplitPane();
        pnl_container = new javax.swing.JPanel();
        pnl_thongTinNhanVien = new javax.swing.JPanel();
        pnl_MaNV = new javax.swing.JPanel();
        lbl_maNV = new javax.swing.JLabel();
        txt_maCT = new javax.swing.JTextField();
        pnl_chucVu = new javax.swing.JPanel();
        lbl_chucVu = new javax.swing.JLabel();
        date_thoiGianDi = new com.toedter.calendar.JDateChooser();
        pnl_chucVu1 = new javax.swing.JPanel();
        lbl_chucVu1 = new javax.swing.JLabel();
        date_thoiGianDen = new com.toedter.calendar.JDateChooser();
        pnl_diaChi1 = new javax.swing.JPanel();
        lbl_diaChi2 = new javax.swing.JLabel();
        txt_soGheDaDat = new javax.swing.JTextField();
        pnl_diaChi2 = new javax.swing.JPanel();
        lbl_diaChi3 = new javax.swing.JLabel();
        txt_soGheTrong = new javax.swing.JTextField();
        pnl_diaChi3 = new javax.swing.JPanel();
        lbl_diaChi4 = new javax.swing.JLabel();
        cb_TuyenDuong = new javax.swing.JComboBox<>();
        pnl_diaChi4 = new javax.swing.JPanel();
        lbl_diaChi5 = new javax.swing.JLabel();
        cb_Tau = new javax.swing.JComboBox<>();
        pnl_btnGroup = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btn_xoaTrang = new javax.swing.JButton();
        btn_capNhat = new javax.swing.JButton();
        btn_themCT = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_thongTinCT = new javax.swing.JTable();

        jLabel1.setText("jLabel1");

        setLayout(new java.awt.BorderLayout());

        pnl_top.setBackground(new java.awt.Color(255, 255, 255));
        pnl_top.setMinimumSize(new java.awt.Dimension(20, 20));
        pnl_top.setPreferredSize(new java.awt.Dimension(1366, 50));
        pnl_top.setLayout(new javax.swing.BoxLayout(pnl_top, javax.swing.BoxLayout.LINE_AXIS));

        pnl_timKiem.setLayout(new javax.swing.BoxLayout(pnl_timKiem, javax.swing.BoxLayout.LINE_AXIS));

        txt_timKiem.setPreferredSize(new java.awt.Dimension(500, 30));
        txt_timKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_timKiemActionPerformed(evt);
            }
        });
        pnl_timKiem.add(txt_timKiem);

        pnl_btnTimKiem.setLayout(new java.awt.BorderLayout());

        btn_timKiem.setText("Tìm kiếm");
        btn_timKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_timKiemActionPerformed(evt);
            }
        });
        pnl_btnTimKiem.add(btn_timKiem, java.awt.BorderLayout.CENTER);

        pnl_timKiem.add(pnl_btnTimKiem);

        pnl_top.add(pnl_timKiem);

        pnl_cta.setPreferredSize(new java.awt.Dimension(500, 50));
        pnl_cta.setLayout(new java.awt.GridLayout(1, 0));

        cb_GaDi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ga đi" }));
        cb_GaDi.setToolTipText("");
        pnl_cta.add(cb_GaDi);

        cb_GaDen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ga đến", "DN", "DL", "LD" }));
        pnl_cta.add(cb_GaDen);

        btn_Loc.setText("Lọc");
        btn_Loc.setMaximumSize(new java.awt.Dimension(100, 50));
        btn_Loc.setPreferredSize(new java.awt.Dimension(100, 50));
        btn_Loc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LocActionPerformed(evt);
            }
        });
        pnl_cta.add(btn_Loc);

        btn_Reset.setText("Reset");
        btn_Reset.setMaximumSize(new java.awt.Dimension(100, 50));
        btn_Reset.setPreferredSize(new java.awt.Dimension(100, 50));
        btn_Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ResetActionPerformed(evt);
            }
        });
        pnl_cta.add(btn_Reset);

        pnl_top.add(pnl_cta);

        add(pnl_top, java.awt.BorderLayout.PAGE_START);

        pnl_center.setLayout(new javax.swing.BoxLayout(pnl_center, javax.swing.BoxLayout.LINE_AXIS));

        spl_container.setResizeWeight(0.8);
        spl_container.setMinimumSize(new java.awt.Dimension(805, 416));
        spl_container.setPreferredSize(new java.awt.Dimension(1055, 718));

        pnl_container.setBackground(new java.awt.Color(255, 255, 255));
        pnl_container.setMinimumSize(new java.awt.Dimension(400, 379));
        pnl_container.setPreferredSize(new java.awt.Dimension(250, 100));
        pnl_container.setLayout(new java.awt.BorderLayout());

        pnl_thongTinNhanVien.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin chuyến tàu"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnl_thongTinNhanVien.setLayout(new javax.swing.BoxLayout(pnl_thongTinNhanVien, javax.swing.BoxLayout.Y_AXIS));

        pnl_MaNV.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        pnl_MaNV.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_MaNV.setPreferredSize(new java.awt.Dimension(230, 50));
        pnl_MaNV.setLayout(new javax.swing.BoxLayout(pnl_MaNV, javax.swing.BoxLayout.X_AXIS));

        lbl_maNV.setText("Mã chuyến :");
        lbl_maNV.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_maNV.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_maNV.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_MaNV.add(lbl_maNV);

        txt_maCT.setEnabled(false);
        txt_maCT.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_maCT.setPreferredSize(new java.awt.Dimension(64, 40));
        pnl_MaNV.add(txt_maCT);

        pnl_thongTinNhanVien.add(pnl_MaNV);

        pnl_chucVu.setMaximumSize(new java.awt.Dimension(32817, 50));
        pnl_chucVu.setPreferredSize(new java.awt.Dimension(230, 50));
        pnl_chucVu.setLayout(new javax.swing.BoxLayout(pnl_chucVu, javax.swing.BoxLayout.LINE_AXIS));

        lbl_chucVu.setText("Thời gian đi :");
        lbl_chucVu.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_chucVu.add(lbl_chucVu);

        date_thoiGianDi.setEnabled(false);
        date_thoiGianDi.setMaximumSize(txt_maCT.getMaximumSize());
        date_thoiGianDi.setMinimumSize(new java.awt.Dimension(141, 0));
        pnl_chucVu.add(date_thoiGianDi);

        pnl_thongTinNhanVien.add(pnl_chucVu);

        pnl_chucVu1.setMaximumSize(new java.awt.Dimension(32817, 50));
        pnl_chucVu1.setPreferredSize(new java.awt.Dimension(230, 50));
        pnl_chucVu1.setLayout(new javax.swing.BoxLayout(pnl_chucVu1, javax.swing.BoxLayout.LINE_AXIS));

        lbl_chucVu1.setText("Thời gian đến :");
        lbl_chucVu1.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_chucVu1.add(lbl_chucVu1);

        date_thoiGianDen.setMaximumSize(txt_maCT.getMaximumSize());
        date_thoiGianDen.setMinimumSize(new java.awt.Dimension(141, 0));
        pnl_chucVu1.add(date_thoiGianDen);

        pnl_thongTinNhanVien.add(pnl_chucVu1);

        pnl_diaChi1.setMaximumSize(new java.awt.Dimension(1147483692, 50));
        pnl_diaChi1.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_diaChi1.setPreferredSize(new java.awt.Dimension(164, 50));
        pnl_diaChi1.setLayout(new javax.swing.BoxLayout(pnl_diaChi1, javax.swing.BoxLayout.LINE_AXIS));

        lbl_diaChi2.setText("Số ghế đã đặt :");
        lbl_diaChi2.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_diaChi2.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_diaChi2.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_diaChi1.add(lbl_diaChi2);

        txt_soGheDaDat.setEnabled(false);
        txt_soGheDaDat.setMaximumSize(txt_maCT.getMaximumSize());
        txt_soGheDaDat.setMinimumSize(new java.awt.Dimension(64, 30));
        pnl_diaChi1.add(txt_soGheDaDat);

        pnl_thongTinNhanVien.add(pnl_diaChi1);

        pnl_diaChi2.setMaximumSize(new java.awt.Dimension(1147483692, 50));
        pnl_diaChi2.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_diaChi2.setPreferredSize(new java.awt.Dimension(164, 50));
        pnl_diaChi2.setLayout(new javax.swing.BoxLayout(pnl_diaChi2, javax.swing.BoxLayout.LINE_AXIS));

        lbl_diaChi3.setText("Số ghế trống :");
        lbl_diaChi3.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_diaChi3.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_diaChi3.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_diaChi2.add(lbl_diaChi3);

        txt_soGheTrong.setEnabled(false);
        txt_soGheTrong.setMaximumSize(txt_maCT.getMaximumSize());
        txt_soGheTrong.setMinimumSize(new java.awt.Dimension(64, 30));
        pnl_diaChi2.add(txt_soGheTrong);

        pnl_thongTinNhanVien.add(pnl_diaChi2);

        pnl_diaChi3.setMaximumSize(new java.awt.Dimension(1147483692, 50));
        pnl_diaChi3.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_diaChi3.setPreferredSize(new java.awt.Dimension(164, 50));
        pnl_diaChi3.setLayout(new javax.swing.BoxLayout(pnl_diaChi3, javax.swing.BoxLayout.LINE_AXIS));

        lbl_diaChi4.setText("Tuyến đường :");
        lbl_diaChi4.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_diaChi4.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_diaChi4.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_diaChi3.add(lbl_diaChi4);
        pnl_diaChi3.add(cb_TuyenDuong);

        pnl_thongTinNhanVien.add(pnl_diaChi3);

        pnl_diaChi4.setMaximumSize(new java.awt.Dimension(1147483692, 50));
        pnl_diaChi4.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_diaChi4.setPreferredSize(new java.awt.Dimension(164, 50));
        pnl_diaChi4.setLayout(new javax.swing.BoxLayout(pnl_diaChi4, javax.swing.BoxLayout.LINE_AXIS));

        lbl_diaChi5.setText("Tàu :");
        lbl_diaChi5.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_diaChi5.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_diaChi5.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_diaChi4.add(lbl_diaChi5);
        pnl_diaChi4.add(cb_Tau);

        pnl_thongTinNhanVien.add(pnl_diaChi4);

        pnl_container.add(pnl_thongTinNhanVien, java.awt.BorderLayout.CENTER);

        pnl_btnGroup.setMaximumSize(new java.awt.Dimension(260, 100));
        pnl_btnGroup.setMinimumSize(new java.awt.Dimension(220, 100));
        pnl_btnGroup.setLayout(new java.awt.BorderLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 2, 0));
        jPanel5.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel5.setMinimumSize(new java.awt.Dimension(167, 50));
        jPanel5.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        btn_xoaTrang.setText("Xóa trắng");
        btn_xoaTrang.setMaximumSize(new java.awt.Dimension(120, 50));
        btn_xoaTrang.setPreferredSize(new java.awt.Dimension(120, 50));
        btn_xoaTrang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaTrangActionPerformed(evt);
            }
        });
        jPanel5.add(btn_xoaTrang);

        btn_capNhat.setText("Cập nhật");
        btn_capNhat.setMaximumSize(new java.awt.Dimension(120, 50));
        btn_capNhat.setPreferredSize(new java.awt.Dimension(120, 50));
        btn_capNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_capNhatActionPerformed(evt);
            }
        });
        jPanel5.add(btn_capNhat);

        pnl_btnGroup.add(jPanel5, java.awt.BorderLayout.CENTER);

        btn_themCT.setText("Thêm chuyến tàu");
        btn_themCT.setMaximumSize(new java.awt.Dimension(139, 50));
        btn_themCT.setMinimumSize(new java.awt.Dimension(139, 50));
        btn_themCT.setPreferredSize(new java.awt.Dimension(72, 50));
        btn_themCT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themCTActionPerformed(evt);
            }
        });
        pnl_btnGroup.add(btn_themCT, java.awt.BorderLayout.SOUTH);

        pnl_container.add(pnl_btnGroup, java.awt.BorderLayout.PAGE_END);

        spl_container.setRightComponent(pnl_container);

        tbl_thongTinCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã chuyến ", "Tuyến đường", "Thời gian đi", "Thời gian đến", "Tàu", "Số ghế đặt", "Số ghế trống"
            }
        ));
        tbl_thongTinCT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_thongTinCTMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_thongTinCT);

        spl_container.setLeftComponent(jScrollPane1);

        pnl_center.add(spl_container);

        add(pnl_center, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_capNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhatActionPerformed
        // TODO add your handling code here:
         handleActionCapNhat();
    }//GEN-LAST:event_btn_capNhatActionPerformed

    private void txt_timKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_timKiemActionPerformed
        // TODO add your handling code here:
        handleActionTimKiem();
    }//GEN-LAST:event_txt_timKiemActionPerformed

    private void btn_themCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themCTActionPerformed
        // TODO add your handling code here:
           handleActionThem();
    }//GEN-LAST:event_btn_themCTActionPerformed

    private void btn_xoaTrangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaTrangActionPerformed
        // TODO add your handling code here:
         handleActionXoaTrang();
    }//GEN-LAST:event_btn_xoaTrangActionPerformed

    private void btn_timKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_timKiemActionPerformed
        // TODO add your handling code here:
          handleActionTimKiem();
    }//GEN-LAST:event_btn_timKiemActionPerformed

    private void tbl_thongTinCTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_thongTinCTMouseClicked
        // TODO add your handling code here:
        
        getThongTinChuyenTau();
    }//GEN-LAST:event_tbl_thongTinCTMouseClicked

    private void btn_LocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LocActionPerformed
        // TODO add your handling code here:
        handleActionLoc();
    }//GEN-LAST:event_btn_LocActionPerformed

    private void btn_ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ResetActionPerformed
        // TODO add your handling code here:
        handleActionLamMoi();
    }//GEN-LAST:event_btn_ResetActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Loc;
    private javax.swing.JButton btn_Reset;
    private javax.swing.JButton btn_capNhat;
    private javax.swing.JButton btn_themCT;
    private javax.swing.JButton btn_timKiem;
    private javax.swing.JButton btn_xoaTrang;
    private javax.swing.JComboBox<String> cb_GaDen;
    private javax.swing.JComboBox<String> cb_GaDi;
    private javax.swing.JComboBox<String> cb_Tau;
    private javax.swing.JComboBox<String> cb_TuyenDuong;
    private com.toedter.calendar.JDateChooser date_thoiGianDen;
    private com.toedter.calendar.JDateChooser date_thoiGianDi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_chucVu;
    private javax.swing.JLabel lbl_chucVu1;
    private javax.swing.JLabel lbl_diaChi2;
    private javax.swing.JLabel lbl_diaChi3;
    private javax.swing.JLabel lbl_diaChi4;
    private javax.swing.JLabel lbl_diaChi5;
    private javax.swing.JLabel lbl_maNV;
    private javax.swing.JPanel pnl_MaNV;
    private javax.swing.JPanel pnl_btnGroup;
    private javax.swing.JPanel pnl_btnTimKiem;
    private javax.swing.JPanel pnl_center;
    private javax.swing.JPanel pnl_chucVu;
    private javax.swing.JPanel pnl_chucVu1;
    private javax.swing.JPanel pnl_container;
    private javax.swing.JPanel pnl_cta;
    private javax.swing.JPanel pnl_diaChi1;
    private javax.swing.JPanel pnl_diaChi2;
    private javax.swing.JPanel pnl_diaChi3;
    private javax.swing.JPanel pnl_diaChi4;
    private javax.swing.JPanel pnl_thongTinNhanVien;
    private javax.swing.JPanel pnl_timKiem;
    private javax.swing.JPanel pnl_top;
    private javax.swing.JSplitPane spl_container;
    private javax.swing.JTable tbl_thongTinCT;
    private javax.swing.JTextField txt_maCT;
    private javax.swing.JTextField txt_soGheDaDat;
    private javax.swing.JTextField txt_soGheTrong;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables

   
}
