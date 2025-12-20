package gui.components;

import dao.ThongKe_DAO;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TopChuyenDiDuocYeuThich extends JPanel {
    
    private ArrayList<String> routeNames;
    private ArrayList<String> ticketCounts;
    
    // Màu sắc cho các chấm tròn (thứ hạng 1-5)
    private final Color[] dotColors = {
        new Color(255, 215, 0),    // Vàng - Top 1
        new Color(192, 192, 192),  // Bạc - Top 2
        new Color(205, 127, 50),   // Đồng - Top 3
        new Color(100, 149, 237),  // Xanh dương - Top 4
        new Color(147, 112, 219)   // Tím - Top 5
    };

    // Cấu hình layout
    private final String title = "Top 5 Chuyến Đi Được Ưa Chuộng";
    private final int padding = 20;
    private final int dotSize = 16;
    private final int gapBetweenDotAndText = 12;
    private final int lineSpacing = 15;
    private final int textLineSpacing = 8;
    private final int rankNumberWidth = 30;

    private double[] dotYs;
    
    public TopChuyenDiDuocYeuThich() {
        setOpaque(true);
        setBackground(Color.WHITE);
        
        // Khởi tạo danh sách
        routeNames = new ArrayList<>();
        ticketCounts = new ArrayList<>();

        if (java.beans.Beans.isDesignTime()) {
            // Dữ liệu mẫu cho chế độ design
            setupSampleData();
        } else {
            // Tải dữ liệu thật khi chạy ứng dụng
            loadData();
        }
        
        dotYs = new double[routeNames.size()];
        revalidate();
        repaint();
    }
    
    /**
     * Thiết lập dữ liệu mẫu cho chế độ design
     */
    private void setupSampleData() {
        routeNames.add("Từ Hà Nội đến Sài Gòn");
        routeNames.add("Từ Đà Nẵng đến Huế");
        routeNames.add("Từ Sài Gòn đến Nha Trang");
        routeNames.add("Từ Hà Nội đến Hải Phòng");
        routeNames.add("Từ Đà Nẵng đến Quy Nhơn");
        
        ticketCounts.add("245 vé đã bán");
        ticketCounts.add("198 vé đã bán");
        ticketCounts.add("176 vé đã bán");
        ticketCounts.add("154 vé đã bán");
        ticketCounts.add("132 vé đã bán");
    }

    /**
     * Tải dữ liệu thực từ database
     */
    public void loadData() {
        try {
            ThongKe_DAO dao = new ThongKe_DAO();
            List<Map<String, Object>> topList = dao.getTop5TuyenDuongDuocYeuThich();
            
            routeNames.clear();
            ticketCounts.clear();
            
            if (topList != null && !topList.isEmpty()) {
                for (Map<String, Object> item : topList) {
                    String gaDi = (String) item.get("tenGaDi");
                    String gaDen = (String) item.get("tenGaDen");
                    int soVe = (int) item.get("soVeBan");
                    
                    routeNames.add("Từ " + gaDi + " đến " + gaDen);
                    ticketCounts.add(soVe + " vé đã bán");
                }
            } else {
                // Không có dữ liệu
                routeNames.add("Chưa có dữ liệu thống kê");
                ticketCounts.add("0 vé");
            }
            
            dotYs = new double[routeNames.size()];
            revalidate();
            repaint();
            
        } catch (Exception e) {
            System.err.println("Lỗi khi tải dữ liệu Top Chuyến Đi: " + e.getMessage());
            e.printStackTrace();
            
            // Hiển thị thông báo lỗi
            routeNames.clear();
            ticketCounts.clear();
            routeNames.add("Không thể tải dữ liệu");
            ticketCounts.add("Vui lòng kiểm tra kết nối");
            dotYs = new double[routeNames.size()];
            revalidate();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Vẽ nền và viền
        int arc = 10;
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));
        g2.setColor(new Color(200, 200, 200));

        // Thiết lập font
        Font titleFont = new Font("Dialog", Font.BOLD, 16);
        Font routeFont = new Font("Dialog", Font.BOLD, 14);
        Font ticketFont = new Font("Dialog", Font.PLAIN, 14);
        g2.setFont(titleFont);

        // Vẽ tiêu đề
        FontMetrics titleMetrics = g2.getFontMetrics();
        int titleHeight = titleMetrics.getHeight();
        int x = padding;
        int y = padding + titleMetrics.getAscent();
        g2.setColor(Color.BLACK);
        g2.setFont(titleFont);
        g2.drawString(title, x, y);

        // Vẽ các tuyến đường
        g2.setFont(routeFont);
        FontMetrics routeMetrics = g2.getFontMetrics();
        int routeHeight = routeMetrics.getHeight();
        y += titleHeight + lineSpacing;

        // Tính trước tọa độ y của các chấm tròn
        for (int i = 0; i < routeNames.size(); i++) {
            int yStart = y;
            y += routeHeight + textLineSpacing;
            int yDot = yStart + (y - yStart) / 2;
            dotYs[i] = yDot;
            y += routeHeight + lineSpacing;
        }

        // Vẽ đường thẳng nối các chấm tròn trước (để nó nằm dưới)
        if (dotYs.length > 1) {
            g2.setColor(new Color(231, 239, 255));
            g2.setStroke(new java.awt.BasicStroke(1));
            g2.draw(new Line2D.Double(x + dotSize / 2, dotYs[0], x + dotSize / 2, dotYs[dotYs.length - 1]));
        }

        // Reset y để vẽ các chấm tròn và văn bản lên trên
        y = padding + titleMetrics.getAscent() + titleHeight + lineSpacing;

        for (int i = 0; i < routeNames.size(); i++) {
            int yStart = y;
                
            // Vẽ tên tuyến đường
            g2.setFont(routeFont);
            g2.setColor(Color.BLACK);
            int textX = x + dotSize + gapBetweenDotAndText;
            g2.drawString(routeNames.get(i), textX, y + routeMetrics.getAscent() / 2);
            g2.setFont(ticketFont);
            g2.setColor(new Color(152, 152, 152));
            // Vẽ số vé đã bán ở dòng tiếp theo
            y += routeHeight + textLineSpacing;
            g2.drawString(ticketCounts.get(i), textX, y + routeMetrics.getAscent() / 2);

            // Vẽ chấm tròn ở giữa hai dòng
            // Sử dụng modulo để lặp lại màu nếu vượt quá số lượng màu trong dotColors
            g2.setColor(dotColors[i % dotColors.length]);
            g2.fill(new Ellipse2D.Double(x, dotYs[i] - dotSize / 2, dotSize, dotSize));

            // Chuyển đến tuyến đường tiếp theo
            y += routeHeight + lineSpacing;
        }

        g2.dispose();
    }

    @Override
    public java.awt.Dimension getPreferredSize() {
        Font titleFont = new Font("Dialog", Font.BOLD, 16);
        Font routeFont = new Font("Dialog", Font.BOLD, 14);
        FontMetrics titleMetrics = getFontMetrics(titleFont);
        FontMetrics routeMetrics = getFontMetrics(routeFont);

        // Tính chiều rộng
        int maxWidth = titleMetrics.stringWidth(title);
        for (int i = 0; i < routeNames.size(); i++) {
            int routeWidth = routeMetrics.stringWidth(routeNames.get(i));
            int ticketWidth = routeMetrics.stringWidth(ticketCounts.get(i));
            maxWidth = Math.max(maxWidth, Math.max(routeWidth, ticketWidth) + dotSize + gapBetweenDotAndText);
        }
        maxWidth += 2 * padding;

        // Tính chiều cao
        int titleHeight = titleMetrics.getHeight();
        int routeHeight = routeMetrics.getHeight();
        int totalHeight = padding + titleHeight + lineSpacing + (routeHeight * 2 + textLineSpacing + lineSpacing) * routeNames.size() + padding;

        return new java.awt.Dimension(maxWidth, totalHeight);
    }

    @Override
    public java.awt.Dimension getMinimumSize() {
        return new java.awt.Dimension(200, 100); // Kích thước tối thiểu
    }
    
    /**
     * Refresh dữ liệu - gọi phương thức này khi cần cập nhật
     */
    public void refresh() {
        loadData();
    }
}