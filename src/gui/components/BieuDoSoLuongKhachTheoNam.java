/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.components;

import com.raven.chart.ModelChart;
import dao.ThongKe_DAO;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;


/**
 *
 * @author Administrator
 */
public class BieuDoSoLuongKhachTheoNam extends javax.swing.JPanel {
    private ThongKe_DAO thongKeDAO;
    private DecimalFormat df = new DecimalFormat("#,##0");
    private String loaiThongKe = "Ngày"; // Mặc định là Ngày
    /**
     * Creates new form BieuDoDoanhThuTheoThang
     */
    public BieuDoSoLuongKhachTheoNam() {
        initComponents();
        
        thongKeDAO = new ThongKe_DAO();
        
        // Cấu hình biểu đồ
        blankPlotChart1.addLegend("Số Hành Khách", Color.decode("#00C9FF"));
        
        // Cập nhật label
        jLabel1.setText("Số Lượng Hành Khách 7 Ngày Gần Nhất");
        jLabel1.setFont(jLabel1.getFont().deriveFont(java.awt.Font.BOLD, 16));
        
        // Thêm sự kiện cho ComboBox
        jComboBox1.addActionListener((ActionEvent e) -> {
            loaiThongKe = (String) jComboBox1.getSelectedItem();
            refresh();
        });
        
        // Load dữ liệu
        if (!java.beans.Beans.isDesignTime()) {
            loadData();
        }
    }
    
      /**
     * Tải dữ liệu số lượng hành khách theo loại thống kê
     */
    public void loadData() {
        try {
            blankPlotChart1.clear();
            
            switch (loaiThongKe) {
                case "Ngày":
                    loadDataTheoNgay();
                    jLabel1.setText("Số Lượng Hành Khách 7 Ngày Gần Nhất");
                    break;
                case "Tháng":
                    loadDataTheoThang();
                    jLabel1.setText("Số Lượng Hành Khách Theo Tháng Năm Nay");
                    break;
                case "Năm":
                    loadDataTheoNam();
                    jLabel1.setText("Số Lượng Hành Khách 5 Năm Gần Nhất");
                    break;
                default:
                    loadDataTheoNgay();
            }
            
            blankPlotChart1.start();
            
        } catch (Exception e) {
            e.printStackTrace();
            blankPlotChart1.addData(new ModelChart("Lỗi tải dữ liệu", new double[]{0}));
        }
    }
    
    /**
     * Load dữ liệu theo ngày (7 ngày gần nhất)
     */
    private void loadDataTheoNgay() {
        Map<String, Integer> hanhKhachData = thongKeDAO.getSoLuongHanhKhach7NgayGanNhat();
        
        if (hanhKhachData == null || hanhKhachData.isEmpty()) {
            blankPlotChart1.addData(new ModelChart("Không có dữ liệu", new double[]{0}));
            return;
        }
        
        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfOutput = new SimpleDateFormat("dd/MM");
        
        for (Map.Entry<String, Integer> entry : hanhKhachData.entrySet()) {
            String ngay = entry.getKey();
            int soHanhKhach = entry.getValue();
            
            try {
                String ngayHienThi = sdfOutput.format(sdfInput.parse(ngay));
                blankPlotChart1.addData(new ModelChart(ngayHienThi, new double[]{soHanhKhach}));
            } catch (Exception e) {
                blankPlotChart1.addData(new ModelChart(ngay, new double[]{soHanhKhach}));
            }
        }
    }
    
    /**
     * Load dữ liệu theo tháng (12 tháng trong năm hiện tại)
     */
    private void loadDataTheoThang() {
        Map<String, Integer> hanhKhachData = thongKeDAO.getSoLuongHanhKhachTheoThang();
        
        if (hanhKhachData == null || hanhKhachData.isEmpty()) {
            blankPlotChart1.addData(new ModelChart("Không có dữ liệu", new double[]{0}));
            return;
        }
        
        String[] tenThang = {"", "Th1", "Th2", "Th3", "Th4", "Th5", "Th6", 
                            "Th7", "Th8", "Th9", "Th10", "Th11", "Th12"};
        
        for (Map.Entry<String, Integer> entry : hanhKhachData.entrySet()) {
            String thang = entry.getKey();
            int soHanhKhach = entry.getValue();
            
            try {
                int thangInt = Integer.parseInt(thang);
                String thangHienThi = tenThang[thangInt];
                blankPlotChart1.addData(new ModelChart(thangHienThi, new double[]{soHanhKhach}));
            } catch (Exception e) {
                blankPlotChart1.addData(new ModelChart(thang, new double[]{soHanhKhach}));
            }
        }
    }
    
    /**
     * Load dữ liệu theo năm (5 năm gần nhất)
     */
    private void loadDataTheoNam() {
        Map<String, Integer> hanhKhachData = thongKeDAO.getSoLuongHanhKhachTheoNam();
        
        if (hanhKhachData == null || hanhKhachData.isEmpty()) {
            blankPlotChart1.addData(new ModelChart("Không có dữ liệu", new double[]{0}));
            return;
        }
        
        for (Map.Entry<String, Integer> entry : hanhKhachData.entrySet()) {
            String nam = entry.getKey();
            int soHanhKhach = entry.getValue();
            blankPlotChart1.addData(new ModelChart(nam, new double[]{soHanhKhach}));
        }
    }
    
    /**
     * Làm mới dữ liệu biểu đồ
     */
    public void refresh() {
        blankPlotChart1.clear();
        loadData();
        repaint();
    }
    
    /**
     * Lấy tổng số hành khách theo loại thống kê hiện tại
     */
    public int getTongSoHanhKhach() {
        switch (loaiThongKe) {
            case "Ngày":
                return thongKeDAO.getTongSoLuongHanhKhach7NgayGanNhat();
            case "Tháng":
            case "Năm":
                // Có thể thêm phương thức tính tổng theo tháng/năm nếu cần
                return 0;
            default:
                return 0;
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

        panelShadow1 = new raven2.panel.PanelShadow();
        blankPlotChart1 = new com.raven.chart.Chart();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();

        panelShadow1.setRadius(30);
        panelShadow1.setShadowColor(new java.awt.Color(102, 204, 255));
        panelShadow1.setShadowOpacity(0.4F);
        panelShadow1.setShadowSize(4);
        panelShadow1.setShadowType(raven2.panel.PanelShadow.ShadowType.BOT);

        jLabel1.setText("jLabel1");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ngày", "Tháng", "Năm" }));

        javax.swing.GroupLayout panelShadow1Layout = new javax.swing.GroupLayout(panelShadow1);
        panelShadow1.setLayout(panelShadow1Layout);
        panelShadow1Layout.setHorizontalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelShadow1Layout.createSequentialGroup()
                .addGroup(panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelShadow1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(blankPlotChart1, javax.swing.GroupLayout.DEFAULT_SIZE, 1189, Short.MAX_VALUE))
                    .addGroup(panelShadow1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33))
        );
        panelShadow1Layout.setVerticalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShadow1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(blankPlotChart1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelShadow1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelShadow1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.raven.chart.Chart blankPlotChart1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private raven2.panel.PanelShadow panelShadow1;
    // End of variables declaration//GEN-END:variables

    
}
