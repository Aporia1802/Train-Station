package gui.bookTicket;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class DatVe_GUI extends JPanel {

    private JPanel contentPanel;
    private JPanel stepIndicatorPanel;
    private JButton btnPrevious, btnNext;
    private CardLayout cardLayout;

    private int currentStep = 1;
    private final int TOTAL_STEPS = 3;

    public DatVe_GUI() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout(0, 15));
        setBackground(Color.WHITE);

        // Tiêu đề
        JLabel lblTitle = new JLabel("Chào Nhân Viên ABCXYZ - Đặt Vé", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(33, 33, 33));
        add(lblTitle, BorderLayout.NORTH);

        // Nội dung chính
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        contentPanel.add(createStep1Panel(), "step1");
        contentPanel.add(createStep2Panel(), "step2");
        contentPanel.add(createStep3Panel(), "step3");

        add(contentPanel, BorderLayout.CENTER);

        // Step indicator
        stepIndicatorPanel = createStepIndicator();
        add(stepIndicatorPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Navigation panel
        add(createNavigationPanel(), BorderLayout.SOUTH);

        refreshStepUI();
    }

    // Indicator các bước
    private JPanel createStepIndicator() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(new Color(240, 240, 240));

        for (int i = 1; i <= TOTAL_STEPS; i++) {
            JLabel lbl = new JLabel(" " + i + " ", JLabel.CENTER);
            lbl.setOpaque(true);
            lbl.setBorder(new LineBorder(Color.GRAY));
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lbl.setPreferredSize(new Dimension(35, 35));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);

            if (i == currentStep) {
                lbl.setBackground(new Color(66, 133, 244));
                lbl.setForeground(Color.WHITE);
            } else {
                lbl.setBackground(Color.WHITE);
                lbl.setForeground(Color.GRAY);
            }

            panel.add(lbl);
            if (i < TOTAL_STEPS) {
                panel.add(new JLabel("→"));
            }
        }

        return panel;
    }

    // Các bước
    private JPanel createStep1Panel() {
        return new ThongTinHanhTrinh_GUI();
    }

    private JPanel createStep2Panel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Bước 2: Chi tiết vé", JLabel.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(lbl, BorderLayout.NORTH);

        JTextArea area = new JTextArea("Thông tin vé, ghế ngồi, hành khách...");
        area.setEditable(false);
        area.setBorder(BorderFactory.createTitledBorder("Chi tiết vé"));
        panel.add(area, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStep3Panel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Bước 3: Thanh toán", JLabel.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(lbl, BorderLayout.NORTH);

        JTextArea area = new JTextArea("Thông tin thanh toán sẽ hiển thị ở đây...");
        area.setEditable(false);
        area.setBorder(BorderFactory.createTitledBorder("Thanh toán"));
        panel.add(area, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        navPanel.setBackground(Color.WHITE);

        btnPrevious = new JButton("Quay lại");
        btnNext = new JButton("Tiếp theo");

        btnPrevious.setEnabled(false);

        btnPrevious.addActionListener(e -> previousStep());
        btnNext.addActionListener(e -> nextStep());

        navPanel.add(btnPrevious);
        navPanel.add(btnNext);

        return navPanel;
    }

    private void refreshStepUI() {
        if (cardLayout != null && contentPanel != null) {
            cardLayout.show(contentPanel, "step" + currentStep);
        }

        if (stepIndicatorPanel != null) {
            remove(stepIndicatorPanel);
        }
        stepIndicatorPanel = createStepIndicator();
        add(stepIndicatorPanel, BorderLayout.BEFORE_FIRST_LINE);

        if (btnPrevious != null)
            btnPrevious.setEnabled(currentStep > 1);

        if (btnNext != null) {
            if (currentStep == TOTAL_STEPS)
                btnNext.setText("✓ Hoàn thành");
            else
                btnNext.setText("Tiếp theo ▶");
        }

        revalidate();
        repaint();
    }

    private void nextStep() {
        if (currentStep < TOTAL_STEPS) {
            currentStep++;
            refreshStepUI();
        } else {
            JOptionPane.showMessageDialog(this, "Đặt vé thành công!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void previousStep() {
        if (currentStep > 1) {
            currentStep--;
            refreshStepUI();
        }
    }
}
