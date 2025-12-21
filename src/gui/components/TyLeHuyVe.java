package gui.components;

import dao.ThongKe_DAO;
import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import raven.chart.data.pie.DefaultPieDataset;
import raven.chart.pie.PieChart;

/**
 * Component hiển thị tỷ lệ hủy vé bằng biểu đồ tròn
 * @author Administrator
 */
public class TyLeHuyVe extends JPanel {
    private PieChart pieChart;
    private ThongKe_DAO thongKeDAO;
    
    public TyLeHuyVe() {
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        thongKeDAO = new ThongKe_DAO();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new MigLayout("insets 10, gap 10", "", ""));
        
        // Tạo biểu đồ tròn
        pieChart = new PieChart();
        pieChart.setOpaque(false);
        pieChart.setBackground(new Color(255, 255, 255));
        
        // Header
        JLabel headerPie = new JLabel("Tỷ Lệ Hủy Vé");
        headerPie.setFont(headerPie.getFont().deriveFont(Font.BOLD, 15));
        pieChart.setHeader(headerPie);
        
        // Cấu hình biểu đồ
        pieChart.setChartType(PieChart.ChartType.DONUT_CHART);
        pieChart.getChartColor().addColor(new Color(0x4caf50)); // Xanh lá - Vé bán thành công
        pieChart.getChartColor().addColor(new Color(0xf44336)); // Đỏ - Vé bị hủy
        
        // Làm trong suốt panel legend
        try {
            Field fieldLegend = PieChart.class.getDeclaredField("panelLegend");
            fieldLegend.setAccessible(true);
            Object legendObj = fieldLegend.get(pieChart);
            if (legendObj instanceof JPanel) {
                JPanel panelLegend = (JPanel) legendObj;
                panelLegend.setOpaque(false);
                panelLegend.setBackground(new Color(0, 0, 0, 0));
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        
        add(pieChart, "grow");
    }
    
    /**
     * Load dữ liệu tỷ lệ hủy vé 7 ngày gần nhất
     */
    public void loadData() {
        try {
            // Lấy tổng số vé bán và vé hủy trong 7 ngày
            Map<String, Integer> soVeBan = thongKeDAO.getSoVeBan7NgayGanNhat();
            Map<String, Integer> soVeHuy = thongKeDAO.getSoVeBiHuy7NgayGanNhat();
            
            // Tính tổng
            int tongVeBan = soVeBan.values().stream().mapToInt(Integer::intValue).sum();
            int tongVeHuy = soVeHuy.values().stream().mapToInt(Integer::intValue).sum();
            int tongVeThanhCong = tongVeBan - tongVeHuy;
            
            // Tính tỷ lệ %
            double tyLeThanhCong = tongVeBan > 0 ? (tongVeThanhCong * 100.0 / tongVeBan) : 0;
            double tyLeHuy = tongVeBan > 0 ? (tongVeHuy * 100.0 / tongVeBan) : 0;
            
            // Format
            DecimalFormat df = new DecimalFormat("#0.0");
            
            // Tạo dataset
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            dataset.addValue("Thành công (" + df.format(tyLeThanhCong) + "%)", tongVeThanhCong);
            dataset.addValue("Bị hủy (" + df.format(tyLeHuy) + "%)", tongVeHuy);
            
            // Cập nhật biểu đồ
            pieChart.setDataset(dataset);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi load dữ liệu tỷ lệ hủy vé: " + e.getMessage());
            
            // Hiển thị dữ liệu mặc định nếu có lỗi
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            dataset.addValue("Thành công (0%)", 0);
            dataset.addValue("Bị hủy (0%)", 0);
            pieChart.setDataset(dataset);
        }
    }
    
    /**
     * Refresh dữ liệu biểu đồ
     */
    public void refresh() {
        loadData();
        repaint();
    }
}