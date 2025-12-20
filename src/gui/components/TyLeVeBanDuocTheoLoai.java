package gui.components;

import dao.ThongKe_DAO;
import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Field;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import raven.chart.data.pie.DefaultPieDataset;
import raven.chart.pie.PieChart;

public class TyLeVeBanDuocTheoLoai extends JPanel {

    private PieChart tyLeVeBanTheoLoaiBieuDo;
    private ThongKe_DAO thongKeDAO;

    public TyLeVeBanDuocTheoLoai() {
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        thongKeDAO = new ThongKe_DAO();
        initComponents();
        
        // Load dữ liệu sau khi khởi tạo giao diện
        if (!java.beans.Beans.isDesignTime()) {
            loadData();
        }
        
    }

    private void initComponents() {
        // Sử dụng MigLayout
        setLayout(new MigLayout("insets 10, gap 10", "", ""));
        
        // Tạo biểu đồ Pie: Tỷ lệ loại vé bán được
        tyLeVeBanTheoLoaiBieuDo = new PieChart();
        tyLeVeBanTheoLoaiBieuDo.setOpaque(false);
        tyLeVeBanTheoLoaiBieuDo.setBackground(new Color(255, 255, 255));
        
        // Tiêu đề biểu đồ
        JLabel headerPie = new JLabel("Tỷ Lệ Loại Vé Bán Được (7 Ngày Gần Nhất)");
        headerPie.setFont(headerPie.getFont().deriveFont(Font.BOLD, 15));
        tyLeVeBanTheoLoaiBieuDo.setHeader(headerPie);
        
        // Có thể bỏ comment dòng này nếu muốn hiển thị dạng Donut Chart
        // tyLeVeBanTheoLoaiBieuDo.setChartType(PieChart.ChartType.DONUT_CHART);
        
        // Định nghĩa màu sắc cho các loại vé
        tyLeVeBanTheoLoaiBieuDo.getChartColor().addColor(new Color(52, 152, 219));   // Xanh dương
        tyLeVeBanTheoLoaiBieuDo.getChartColor().addColor(new Color(46, 204, 113));   // Xanh lá
        tyLeVeBanTheoLoaiBieuDo.getChartColor().addColor(new Color(241, 196, 15));   // Vàng
        tyLeVeBanTheoLoaiBieuDo.getChartColor().addColor(new Color(230, 126, 34));   // Cam
        tyLeVeBanTheoLoaiBieuDo.getChartColor().addColor(new Color(155, 89, 182));   // Tím
        tyLeVeBanTheoLoaiBieuDo.getChartColor().addColor(new Color(231, 76, 60));    // Đỏ
        tyLeVeBanTheoLoaiBieuDo.getChartColor().addColor(new Color(26, 188, 156));   // Xanh ngọc
        tyLeVeBanTheoLoaiBieuDo.getChartColor().addColor(new Color(149, 165, 166));  // Xám
        
        // Làm trong suốt panel legend
        try {
            Field fieldLegend = PieChart.class.getDeclaredField("panelLegend");
            fieldLegend.setAccessible(true);
            Object legendObj = fieldLegend.get(tyLeVeBanTheoLoaiBieuDo);
            if (legendObj instanceof JPanel) {
                JPanel panelLegend = (JPanel) legendObj;
                panelLegend.setOpaque(false);
                panelLegend.setBackground(new Color(0, 0, 0, 0));
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        
        add(tyLeVeBanTheoLoaiBieuDo, "grow");
    }
    
    /**
     * Tải dữ liệu từ database và hiển thị lên biểu đồ
     * Sử dụng các phương thức DAO đã được cập nhật với cấu trúc: HoaDon -> Ve -> LoaiVe
     */
    public void loadData() {
        try {
            // Lấy dữ liệu tỷ lệ và số lượng loại vé từ DAO
            Map<String, Double> tyLeData = thongKeDAO.getTyLeLoaiVe7NgayGanNhat();
            Map<String, Integer> soLuongData = thongKeDAO.getSoLuongLoaiVe7NgayGanNhat();
            
            // Kiểm tra nếu không có dữ liệu
            if (tyLeData == null || tyLeData.isEmpty()) {
                // Hiển thị thông báo không có dữ liệu
                DefaultPieDataset<String> emptyDataset = new DefaultPieDataset<>();
                emptyDataset.addValue("Không có dữ liệu", 100);
                tyLeVeBanTheoLoaiBieuDo.setDataset(emptyDataset);
                return;
            }
            
            // Tạo dataset cho biểu đồ
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            
            // Thêm dữ liệu vào dataset
            for (Map.Entry<String, Double> entry : tyLeData.entrySet()) {
                String loaiVe = entry.getKey();
                Double tyLe = entry.getValue();
                Integer soLuong = soLuongData.get(loaiVe);
                
                // Format: "Tên loại vé (số lượng)"
                String label = loaiVe + " (" + soLuong + ")";
                dataset.addValue(label, tyLe);
            }
            
            // Cập nhật dataset cho biểu đồ
            tyLeVeBanTheoLoaiBieuDo.setDataset(dataset);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Hiển thị thông báo lỗi
            DefaultPieDataset<String> errorDataset = new DefaultPieDataset<>();
            errorDataset.addValue("Lỗi tải dữ liệu", 100);
            tyLeVeBanTheoLoaiBieuDo.setDataset(errorDataset);
        }
    }
    
    /**
     * Làm mới dữ liệu biểu đồ
     * Gọi phương thức này khi cần cập nhật dữ liệu mới
     */
    public void refresh() {
        loadData();
        repaint();
    }
}
