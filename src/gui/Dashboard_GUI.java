package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.text.DecimalFormat;

public class Dashboard_GUI extends JPanel {
    private Color primaryBlue = new Color(37, 99, 235);
    private Color lightBlue = new Color(147, 197, 253);
    private Color green = new Color(34, 197, 94);
    private Color pink = new Color(236, 72, 153);
    private Color purple = new Color(168, 85, 247);
    private Color red = new Color(239, 68, 68);
    private Color bgLight = new Color(248, 250, 252);
    
    public Dashboard_GUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(bgLight);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Top metrics panel
        JPanel topPanel = createTopMetricsPanel();
        
        // Middle panel with charts
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 15, 15));
        middlePanel.setBackground(bgLight);
        middlePanel.add(createSalesTypePanel());
        middlePanel.add(createRevenueChartPanel());
        
        // Bottom panel with more charts
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        bottomPanel.setBackground(bgLight);
        bottomPanel.add(createRoutesPanel());
        bottomPanel.add(createCancellationPanel());
        bottomPanel.add(createRevenueByTypePanel());
        
        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTopMetricsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 15));
        panel.setBackground(bgLight);
        
        panel.add(createMetricCard("Số Vé Bán (Vé)", "3,354", "+12%", green, 
            new int[]{20, 35, 30, 45, 40, 50, 48, 55}));
        panel.add(createMetricCard("Giờ Làm Việc (Giờ)", "3,354", "+8%", primaryBlue,
            new int[]{30, 25, 40, 35, 50, 45, 55, 52}));
        panel.add(createMetricCard("Tiền Bán Được (VND)", "95%", "+5%", new Color(234, 179, 8),
            new int[]{40, 42, 38, 45, 43, 48, 46, 50}));
        panel.add(createMetricCard("Số Vé Bọn Được Cao Nhất\nTrong Một Ca", "500", "+15%", purple,
            new int[]{25, 30, 35, 32, 40, 38, 45, 48}));
        
        return panel;
    }
    
    private JPanel createMetricCard(String title, String value, String change, Color color, int[] trendData) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw trend line
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
                int w = getWidth();
                int h = getHeight();
                int padding = 20;
                
                Path2D path = new Path2D.Double();
                path.moveTo(padding, h - padding);
                
                for (int i = 0; i < trendData.length; i++) {
                    double x = padding + (w - 2 * padding) * i / (double)(trendData.length - 1);
                    double y = h - padding - (trendData[i] * (h - 100) / 60.0);
                    if (i == 0) path.moveTo(x, y);
                    else path.lineTo(x, y);
                }
                
                path.lineTo(w - padding, h - padding);
                path.closePath();
                g2.fill(path);
                
                // Draw trend line stroke
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2));
                Path2D line = new Path2D.Double();
                for (int i = 0; i < trendData.length; i++) {
                    double x = padding + (w - 2 * padding) * i / (double)(trendData.length - 1);
                    double y = h - padding - (trendData[i] * (h - 100) / 60.0);
                    if (i == 0) line.moveTo(x, y);
                    else line.lineTo(x, y);
                }
                g2.draw(line);
            }
        };
        
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("<html>" + title.replace("\n", "<br>") + "</html>");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        titleLabel.setForeground(new Color(100, 116, 139));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(new Color(30, 41, 59));
        
        JLabel changeLabel = new JLabel(change);
        changeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        changeLabel.setForeground(green);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(valueLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(changeLabel);
        
        card.add(textPanel, BorderLayout.NORTH);
        
        return card;
    }
    
    private JPanel createSalesTypePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel title = new JLabel("Tỷ Lệ Loại Vé Bán Được");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                int centerX = w / 2;
                int centerY = h / 2 - 20;
                int radius = Math.min(w, h) / 3;
                int innerRadius = radius / 2;
                
                // Draw donut chart
                double[] values = {55, 20, 25};
                Color[] colors = {primaryBlue, pink, lightBlue};
                String[] labels = {"Loại Vé", "Vé Thức Em", "Vé Học Sinh - Sinh Viên"};
                
                double startAngle = 0;
                for (int i = 0; i < values.length; i++) {
                    double angle = values[i] * 360 / 100;
                    g2.setColor(colors[i]);
                    Arc2D arc = new Arc2D.Double(centerX - radius, centerY - radius, 
                        radius * 2, radius * 2, startAngle, angle, Arc2D.PIE);
                    g2.fill(arc);
                    startAngle += angle;
                }
                
                // Draw inner circle
                g2.setColor(Color.WHITE);
                g2.fillOval(centerX - innerRadius, centerY - innerRadius, 
                    innerRadius * 2, innerRadius * 2);
                
                // Center text
                g2.setColor(new Color(30, 41, 59));
                g2.setFont(new Font("Arial", Font.BOLD, 32));
                String centerText = "55%";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(centerText, centerX - fm.stringWidth(centerText) / 2, centerY + 10);
                
                // Legend
                int legendY = h - 60;
                int legendX = 30;
                for (int i = 0; i < labels.length; i++) {
                    g2.setColor(colors[i]);
                    g2.fillOval(legendX, legendY + i * 20, 10, 10);
                    g2.setColor(new Color(100, 116, 139));
                    g2.setFont(new Font("Arial", Font.PLAIN, 11));
                    g2.drawString(labels[i], legendX + 15, legendY + i * 20 + 9);
                }
            }
        };
        chartPanel.setBackground(Color.WHITE);
        panel.add(chartPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRevenueChartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel title = new JLabel("Thống Kê Doanh Thu");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                int padding = 40;
                int barWidth = 30;
                
                int[] data = {100, 200, 150, 250, 280, 240, 260, 120, 320, 340, 380};
                String[] labels = {"3 Jun", "4 Jun", "5 Jun", "6 Jun", "7 Jun", "8 Jun", 
                    "9 Jun", "10 Jun", "11 Jun", "12 Jun", "13 Jun", "14 Jun"};
                
                int maxValue = 400;
                int chartHeight = h - 2 * padding;
                int chartWidth = w - 2 * padding;
                int spacing = chartWidth / (data.length + 1);
                
                // Draw bars
                for (int i = 0; i < data.length; i++) {
                    int barHeight = (int) (data[i] * chartHeight / (double) maxValue);
                    int x = padding + (i + 1) * spacing - barWidth / 2;
                    int y = h - padding - barHeight;
                    
                    g2.setColor(i == data.length - 1 ? primaryBlue : lightBlue);
                    g2.fillRoundRect(x, y, barWidth, barHeight, 5, 5);
                    
                    // Draw labels
                    if (i % 2 == 0 && i < labels.length) {
                        g2.setColor(new Color(100, 116, 139));
                        g2.setFont(new Font("Arial", Font.PLAIN, 10));
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(labels[i], x + barWidth/2 - fm.stringWidth(labels[i])/2, h - padding + 15);
                    }
                }
            }
        };
        chartPanel.setBackground(Color.WHITE);
        panel.add(chartPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRoutesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel title = new JLabel("Chuyến Đi Được Ưu Chuộng");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        
        String[][] routes = {
            {"Từ TPHCM đến Hà Nội", "Đã bán 623 lần", purple.toString()},
            {"Từ TPHCM đến Đà Nẵng", "Đã bán 123 lần", purple.toString()},
            {"Từ Hà Nội đến Đà Nẵng", "Đã bán 40 lần", new Color(71, 85, 105).toString()},
            {"Từ Hà Nội đến TPHCM", "Đã bán 20 lần", red.toString()}
        };
        
        for (String[] route : routes) {
            JPanel item = new JPanel(new BorderLayout(10, 0));
            item.setBackground(Color.WHITE);
            item.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            
            JPanel dotPanel = new JPanel();
            dotPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            dotPanel.setBackground(Color.WHITE);
            JLabel dot = new JLabel("●");
            dot.setFont(new Font("Arial", Font.PLAIN, 20));
            dot.setForeground(route[0].contains("TPHCM đến Hà Nội") ? purple : 
                route[0].contains("TPHCM đến Đà Nẵng") ? purple : 
                route[0].contains("Hà Nội đến Đà Nẵng") ? new Color(71, 85, 105) : red);
            dotPanel.add(dot);
            
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setBackground(Color.WHITE);
            
            JLabel routeLabel = new JLabel(route[0]);
            routeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            
            JLabel countLabel = new JLabel(route[1]);
            countLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            countLabel.setForeground(new Color(100, 116, 139));
            
            textPanel.add(routeLabel);
            textPanel.add(countLabel);
            
            item.add(dotPanel, BorderLayout.WEST);
            item.add(textPanel, BorderLayout.CENTER);
            
            listPanel.add(item);
        }
        
        panel.add(listPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCancellationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel title = new JLabel("Tỷ Lệ Hủy Vé (%)");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel badge = new JLabel("90% ✓");
        badge.setFont(new Font("Arial", Font.BOLD, 11));
        badge.setForeground(green);
        badge.setOpaque(true);
        badge.setBackground(new Color(220, 252, 231));
        badge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(title);
        headerPanel.add(badge);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                int centerX = w / 2;
                int centerY = h / 2;
                int radius = Math.min(w, h) / 3;
                int innerRadius = radius / 2;
                
                // Draw donut
                g2.setColor(green);
                g2.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 90, 324);
                
                g2.setColor(red);
                g2.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 54, 36);
                
                g2.setColor(Color.WHITE);
                g2.fillOval(centerX - innerRadius, centerY - innerRadius, innerRadius * 2, innerRadius * 2);
                
                // Center text
                g2.setColor(new Color(30, 41, 59));
                g2.setFont(new Font("Arial", Font.BOLD, 28));
                String text = "90%";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(text, centerX - fm.stringWidth(text) / 2, centerY + 10);
                
                // Legend
                int legendY = h - 50;
                g2.setColor(green);
                g2.fillOval(centerX - 80, legendY, 10, 10);
                g2.setColor(new Color(100, 116, 139));
                g2.setFont(new Font("Arial", Font.PLAIN, 11));
                g2.drawString("Vé bình thường", centerX - 65, legendY + 9);
                g2.drawString("1423 Vé", centerX - 65, legendY + 22);
                
                g2.setColor(red);
                g2.fillOval(centerX - 80, legendY + 35, 10, 10);
                g2.setColor(new Color(100, 116, 139));
                g2.drawString("Vé bị hủy", centerX - 65, legendY + 44);
                g2.drawString("143 Vé", centerX - 65, legendY + 57);
            }
        };
        chartPanel.setBackground(Color.WHITE);
        panel.add(chartPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRevenueByTypePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel title = new JLabel("Biểu Đồ Doanh Thu Theo Loại Ghế");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                int padding = 40;
                int barWidth = 60;
                
                String[] labels = {"Ghế Ngồi Mềm", "Giường Nằm Khoang 4", "Giường Nằm Khoang 6"};
                int[] values = {62267, 115125, 840000};
                Color[] colors = {lightBlue, new Color(147, 197, 253), primaryBlue};
                
                int maxValue = 900000;
                int chartHeight = h - 2 * padding;
                int spacing = (w - 2 * padding) / (labels.length + 1);
                
                for (int i = 0; i < values.length; i++) {
                    int barHeight = (int) (values[i] * chartHeight / (double) maxValue);
                    int x = padding + (i + 1) * spacing - barWidth / 2;
                    int y = h - padding - barHeight;
                    
                    g2.setColor(colors[i]);
                    g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);
                    
                    // Value on top
                    g2.setColor(new Color(30, 41, 59));
                    g2.setFont(new Font("Arial", Font.BOLD, 11));
                    String valueStr = new DecimalFormat("#,###").format(values[i]);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(valueStr, x + barWidth/2 - fm.stringWidth(valueStr)/2, y - 5);
                    
                    // Label
                    g2.setColor(new Color(100, 116, 139));
                    g2.setFont(new Font("Arial", Font.PLAIN, 10));
                    String[] words = labels[i].split(" ");
                    int yOffset = h - padding + 15;
                    for (String word : words) {
                        g2.drawString(word, x + barWidth/2 - g2.getFontMetrics().stringWidth(word)/2, yOffset);
                        yOffset += 12;
                    }
                }
            }
        };
        chartPanel.setBackground(Color.WHITE);
        panel.add(chartPanel, BorderLayout.CENTER);
        
        return panel;
    }
}