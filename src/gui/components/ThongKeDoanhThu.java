/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.components;

import dao.ThongKe_DAO;
import com.raven.chart.ModelChart;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import javax.swing.JOptionPane;
/**
 *
 * @author CÔNG HOÀNG
 */
public class ThongKeDoanhThu extends javax.swing.JPanel {
    private ThongKe_DAO thongKeDAO;
    private DecimalFormat df;
    private NumberFormat currencyFormat;
    /**
     * Creates new form ThongKeDoanhThu
     */
    public ThongKeDoanhThu() {
        initComponents();
        
        thongKeDAO = new ThongKe_DAO();
        
        // Khởi tạo format cho số và tiền tệ
        df = new DecimalFormat("#,##0.00");
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        // Thiết lập biểu đồ
        setupChart();
        
        // Hiển thị dữ liệu mặc định (theo ngày)
        hienThiDoanhThuTheoNgay();
    }
    
    private void setupChart() {
        // Thiết lập các thuộc tính cơ bản cho biểu đồ
        blankPlotChart1.addLegend("Doanh thu", new Color(12, 84, 175));
        blankPlotChart1.setForeground(new Color(54, 69, 79));
        jLabel1.setText("Biểu đồ doanh thu");
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
    }

    private void hienThiDoanhThuTheoNgay() {
        try {
            Map<String, Double> duLieu = thongKeDAO.getDoanhThu7NgayGanNhat();
            
            // Xóa dữ liệu cũ
            blankPlotChart1.clear();
            
            if (duLieu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu để hiển thị", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Thêm dữ liệu vào biểu đồ
            for (Map.Entry<String, Double> entry : duLieu.entrySet()) {
                String ngay = entry.getKey();
                double doanhThu = entry.getValue();
                
                // Chỉ lấy ngày/tháng để hiển thị (bỏ năm)
                String label = ngay.substring(5); // Lấy từ MM-dd
                
                blankPlotChart1.addData(new ModelChart(label, new double[]{doanhThu}));
            }
            
            // Cập nhật label
            jLabel1.setText("Doanh thu (triệu đồng)");
            
            // Vẽ lại biểu đồ
            blankPlotChart1.start();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi hiển thị dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hienThiDoanhThuTheoThang() {
        try {
            Map<String, Double> duLieu = thongKeDAO.getDoanhThuTheoThang();
            
            // Xóa dữ liệu cũ
            blankPlotChart1.clear();
            
            if (duLieu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu để hiển thị", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Mảng tên tháng
            String[] tenThang = {"", "Th1", "Th2", "Th3", "Th4", "Th5", "Th6", "Th7", "Th8", "Th9", "Th10", "Th11", "Th12"};
            
            // Thêm dữ liệu vào biểu đồ
            for (Map.Entry<String, Double> entry : duLieu.entrySet()) {
                String thang = entry.getKey();
                double doanhThu = entry.getValue();
                
                // Chuyển đổi số tháng thành label
                int soThang = Integer.parseInt(thang);
                String label = tenThang[soThang];
                
                blankPlotChart1.addData(new ModelChart(label, new double[]{doanhThu}));
            }
            
            // Cập nhật label
            jLabel1.setText("Doanh thu theo tháng năm " + java.time.Year.now().getValue());
            
            // Vẽ lại biểu đồ
            blankPlotChart1.start();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi hiển thị dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hienThiDoanhThuTheoNam() {
        try {
            Map<String, Double> duLieu = thongKeDAO.getDoanhThuTheoNam();
            
            // Xóa dữ liệu cũ
            blankPlotChart1.clear();
            
            if (duLieu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu để hiển thị", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Thêm dữ liệu vào biểu đồ
            for (Map.Entry<String, Double> entry : duLieu.entrySet()) {
                String nam = entry.getKey();
                double doanhThu = entry.getValue();
                
                blankPlotChart1.addData(new ModelChart(nam, new double[]{doanhThu}));
            }
            
            // Cập nhật label
            jLabel1.setText("Doanh thu 5 năm gần nhất");
            
            // Vẽ lại biểu đồ
            blankPlotChart1.start();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi hiển thị dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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

        setLayout(new java.awt.BorderLayout());

        panelShadow1.setRadius(30);
        panelShadow1.setShadowColor(new java.awt.Color(102, 204, 255));
        panelShadow1.setShadowOpacity(0.4F);
        panelShadow1.setShadowSize(4);
        panelShadow1.setShadowType(raven2.panel.PanelShadow.ShadowType.BOT);

        jLabel1.setText("jLabel1");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Theo ngày", "Theo tháng", "Theo năm" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelShadow1Layout = new javax.swing.GroupLayout(panelShadow1);
        panelShadow1.setLayout(panelShadow1Layout);
        panelShadow1Layout.setHorizontalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShadow1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(blankPlotChart1, javax.swing.GroupLayout.DEFAULT_SIZE, 1005, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelShadow1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        panelShadow1Layout.setVerticalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShadow1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blankPlotChart1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(panelShadow1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        int selectedIndex = jComboBox1.getSelectedIndex();
        
        switch (selectedIndex) {
            case 0: // Theo ngày
                hienThiDoanhThuTheoNgay();
                break;
            case 1: // Theo tháng
                hienThiDoanhThuTheoThang();
                break;
            case 2: // Theo năm
                hienThiDoanhThuTheoNam();
                break;
            default:
                break;
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.raven.chart.Chart blankPlotChart1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private raven2.panel.PanelShadow panelShadow1;
    // End of variables declaration//GEN-END:variables
}
