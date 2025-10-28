/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.quanLy;

import bus.QuanLyNhanVien_BUS;
import entity.NhanVien;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import main.Application;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.*;
import raven.toast.Notifications;

/**
 *
 * @author LAPTOP DELL
 */
public class QuanLyNhanVien_GUI extends javax.swing.JPanel {

    /**
     * Creates new form QuanLyNhanVien_GUI
     */
    private QuanLyNhanVien_BUS bus;
    private DefaultTableModel tblModel_thongtinNhanVien;
    private static CellStyle cellStyleFormatNumber = null;
    
    
    public QuanLyNhanVien_GUI() {
        initComponents();
        init();
    }

   private void init(){
       bus = new QuanLyNhanVien_BUS();
       tblModel_thongtinNhanVien = new DefaultTableModel(new String[]{"Mã NV","Họ tên","Giới tính","Ngày sinh","Email","SDT","CCCD","Địa chỉ","Chức vụ","Trạng thái"},0);
       tbl_nhanVien.setModel(tblModel_thongtinNhanVien);
       getTableData(bus.getAllNhanVien());
   }
   
  
   private void getTableData(ArrayList<NhanVien> dsNV){
       tblModel_thongtinNhanVien.setRowCount(0);
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
       for(NhanVien nv : dsNV){
           String gioiTinhstr = nv.isGioiTinh() ? "Nam":"Nữ";
           String trangThaistr = nv.isTrangThai()?"Đang làm":"Đã nghỉ";
           String ngaySinhstr = "";
           if(nv.getNgaySinh() != null){
               ngaySinhstr = nv.getNgaySinh().format(formatter);
           }
           String[] newRow = {nv.getMaNV(),nv.getTenNV(),gioiTinhstr,ngaySinhstr,nv.getEmail(),nv.getSoDienThoai(),nv.getCccd(),nv.getDiaChi(),nv.getChucVu(),trangThaistr};
           tblModel_thongtinNhanVien.addRow(newRow);
       }
   }
    private void getThongTinNhanVien() {
    int row = tbl_nhanVien.getSelectedRow(); // lấy dòng được chọn
    if (row != -1) {
        // Lấy dữ liệu từ từng cột
        String maNV = tbl_nhanVien.getValueAt(row, 0).toString();
        String hoTen = tbl_nhanVien.getValueAt(row, 1).toString();
        String gioiTinh = tbl_nhanVien.getValueAt(row, 2).toString();
        String ngaySinhStr = tbl_nhanVien.getValueAt(row, 3).toString();
        String email = tbl_nhanVien.getValueAt(row, 4).toString();
        String sdt = tbl_nhanVien.getValueAt(row, 5).toString();
        String cccd = tbl_nhanVien.getValueAt(row, 6).toString();
        String diaChi = tbl_nhanVien.getValueAt(row, 7).toString();
        String chucVu = tbl_nhanVien.getValueAt(row, 8).toString().trim();
        String trangThai = tbl_nhanVien.getValueAt(row, 9).toString();
      

        // Gán dữ liệu lên các ô nhập liệu
        txt_maNV.setText(maNV);
        txt_hoTen.setText(hoTen);
        txt_email.setText(email);
        txt_sdt.setText(sdt);
        txt_cccd.setText(cccd);
        txt_diaChi.setText(diaChi);
        
        for (int i = 0; i < cbo_cv.getItemCount(); i++) {
             String item = cbo_cv.getItemAt(i).toString().trim();
             if (item.equalsIgnoreCase(chucVu)) {
                 cbo_cv.setSelectedIndex(i);
                 break;
             }
         }
        // Giới tính
        if (gioiTinh.equalsIgnoreCase("Nam")) {
            rad_Nam.setSelected(true);
        } else {
            rad_Nu.setSelected(true);
        }

        // Trạng thái
        if (trangThai.equalsIgnoreCase("Đang làm")) {
            rad_tt1.setSelected(true);
        } else {
            rad_tt2.setSelected(true);
        }

        // Ngày sinh
        if (ngaySinhStr != null && !ngaySinhStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(ngaySinhStr, formatter);
            java.util.Date ngaySinh = java.sql.Date.valueOf(localDate);
            txt_ngaySinh.setDate(ngaySinh); // Gán vào JDateChooser
        } else {
            txt_ngaySinh.setDate(null); // Nếu trống thì xóa giá trị
        }
            }
        }
    
    public NhanVien getFormData() throws Exception {
        String maNV = txt_maNV.getText().trim();
        String tenNV = txt_hoTen.getText().trim();
        String email = txt_email.getText().trim();
        String sdt = txt_sdt.getText().trim();
        String cccd = txt_cccd.getText().trim();
        String diaChi = txt_diaChi.getText().trim();
        String chucVu = cbo_cv.getSelectedItem().toString();
        
        // Giới tính
        boolean gioiTinh = rad_Nam.isSelected(); // true = Nam, false = Nữ

        // Ngày sinh
        LocalDate ngaySinh = txt_ngaySinh.getDate().toInstant()
                                 .atZone(ZoneId.systemDefault())
                                 .toLocalDate();

        // Trạng thái
        boolean trangThai = rad_tt1.isSelected(); // true = Đang làm, false = Nghỉ


        // Tạo đối tượng NhanVien
        NhanVien nv = new NhanVien();
        nv.setMaNV(maNV);
        nv.setTenNV(tenNV);
        nv.setGioiTinh(gioiTinh);
        nv.setNgaySinh(ngaySinh);
        nv.setEmail(email);
        nv.setSoDienThoai(sdt);
        nv.setCccd(cccd);
        nv.setDiaChi(diaChi);
        nv.setChucVu(chucVu);
        nv.setTrangThai(trangThai);

        return nv;
    } 



    private void handleActionXoaTrang() {
        tbl_nhanVien.clearSelection();

        // Xóa nội dung trong các ô nhập liệu
        txt_maNV.setText("");
        txt_hoTen.setText("");
        txt_diaChi.setText("");
        txt_email.setText("");
        txt_sdt.setText("");
        txt_cccd.setText("");

        // Đặt lại giới tính về mặc định (Nam)
        rad_Nam.setSelected(true);
        rad_Nu.setSelected(false);

        // Xóa ngày sinh
        if (txt_ngaySinh.getDate() != null) {
            txt_ngaySinh.setDate(new Date());
        }

        if (cbo_cv.getItemCount() > 0) {
            cbo_cv.setSelectedIndex(0); // "Tất cả" hoặc "Nam"
        }

        // Đặt trạng thái về mặc định (Đang làm)
        rad_tt1.setSelected(true);
        rad_tt2.setSelected(false);

        // Đưa focus về ô đầu tiên (nếu muốn)
        txt_maNV.requestFocus();
    }
    
    private void handleActionTimKiem() {
        String sdt = txt_timKiem.getText().trim();
        getTableData(bus.getNhanVienbySDT(sdt));
    }
      
    private void handleActionLoc() {
    //  Lấy giá trị từ combobox
        String chucVu = (String) cbo_chucVu.getSelectedItem();
        String trangThai = (String) cbo_trangThai.getSelectedItem();
        getTableData(bus.filter(chucVu, trangThai));
    }
    
    private void handleActionLamMoi() {
        tbl_nhanVien.clearSelection();
        getTableData(bus.getAllNhanVien());
        txt_timKiem.setText("Nhập sdt or cccd cần tìm...");
        txt_timKiem.setForeground(Color.GRAY);
    }
    
    private void handleCapNhat() {
        try {
            if(tbl_nhanVien.getSelectedRow() == -1) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Chưa chọn nhân viên cần thay đổi thông tin!");
                return;
            }
            
            NhanVien nv = getFormData();
            if(bus.capNhatNhanVien(nv)) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật thành công!");
                getTableData(bus.getAllNhanVien());
                handleActionXoaTrang();
            } 
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Cập nhật thất bại!");
        }
    }
    
    private void handleThemNV() {
        try {
           String maNV  = bus.generateID();
            
            NhanVien nv = getFormData();
            nv.setMaNV(maNV);
            if(bus.themNhanVien(nv)) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm mới thành công!");
                getTableData(bus.getAllNhanVien());
                handleActionXoaTrang();
            } 
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Thêm thất bại!");
        }
    }
    
    private void createExcel(ArrayList<NhanVien> dsNhanVien, String filePath) {
        try {
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            SXSSFSheet sheet = workbook.createSheet("Danh Sách Nhân Viên");
            
            sheet.trackAllColumnsForAutoSizing();
            int rowIndex = 0;
            writeHeader(sheet, rowIndex);
            rowIndex = 4;
            for (NhanVien nv: dsNhanVien) {
                SXSSFRow row = sheet.createRow(rowIndex);
                writeEmployee(nv, row);
                rowIndex++;
            }
            writeFooter(sheet, rowIndex);
            
            createOutputFile(workbook, filePath);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Xuất file thành công!");
         
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private static void writeHeader(SXSSFSheet sheet, int rowIndex) {
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

        String[] title = {"MÃ NHÂN VIÊN", "TÊN NHÂN VIÊN", "GIỚI TÍNH", "NGÀY SINH", "CHỨC VỤ", "TRẠNG THÁI", "SỐ ĐIỆN THOẠI", "CĂN CƯỚC CÔNG DÂN", "ĐỊA CHỈ"};

        CellStyle cellStyle = createStyleForHeader(sheet);
        CellStyle headerCellStyle = createStyleForTitle(sheet);
        
        SXSSFRow headerRow = sheet.createRow(rowIndex++);
        SXSSFCell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("DANH SÁCH NHÂN VIÊN");
        headerCell.setCellStyle(headerCellStyle);
        
        SXSSFRow dateRow = sheet.createRow(rowIndex++);
        SXSSFCell dateCell = dateRow.createCell(0);
        dateCell.setCellValue("Ngày in: " + LocalDate.now());
        
        SXSSFRow empRow = sheet.createRow(rowIndex++);
        SXSSFCell empCell = empRow.createCell(0);
        empCell.setCellValue("Nhân viên in: " + Application.nhanVien.getTenNV());
        
        SXSSFRow row = sheet.createRow(rowIndex++);
        
        // Create cells
        SXSSFCell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("MÃ NHÂN VIÊN");
        for (int i = 0; i < title.length - 1; i++) {
            cell = row.createCell(i+1);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(title[i+1]);
        }        
    }
    private void writeEmployee(NhanVien nv, SXSSFRow row) {
        if (cellStyleFormatNumber == null) {
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            SXSSFWorkbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
 
        SXSSFCell cell = row.createCell(0);
        cell.setCellValue(nv.getMaNV());
 
        cell = row.createCell(1);
        cell.setCellValue(nv.getTenNV());
 
        cell = row.createCell(2);
        String gender = "Nam";
        if(!nv.isGioiTinh())
            gender = "Nữ";
        cell.setCellValue(gender);
        
 
        cell = row.createCell(3);
        cell.setCellValue(nv.getNgaySinh().toString());
        
        cell = row.createCell(4);
        cell.setCellValue(nv.getChucVu());
        
        cell = row.createCell(5);
        String status = "Đã nghỉ";
        if(nv.isTrangThai())
            status = "Đang làm việc";
        cell.setCellValue(status);
        
        cell = row.createCell(6);
        cell.setCellValue(nv.getSoDienThoai());
        
        cell = row.createCell(7);
        cell.setCellValue(nv.getCccd());
        
        cell = row.createCell(8);
        cell.setCellValue(nv.getDiaChi());
    }
    private void writeFooter(SXSSFSheet sheet, int rowIndex) {
        SXSSFRow row = sheet.createRow(rowIndex);
        SXSSFCell cell = row.createCell(9, CellType.FORMULA);
        cell.setCellFormula("COUNT(A2:A11)");
    }
    private static CellStyle createStyleForTitle(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 20); // font size
        font.setColor(IndexedColors.GREEN.getIndex()); // text color
        
 
        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }
    private static CellStyle createStyleForHeader(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.VIOLET.getIndex()); // text color
 
        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        return cellStyle;
    }
    private static void createOutputFile(SXSSFWorkbook workbook, String excelFilePath) throws FileNotFoundException, IOException {
        try (OutputStream os = new FileOutputStream(excelFilePath)) {
            workbook.write(os);
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        pnl_top = new javax.swing.JPanel();
        pnl_timKiem = new javax.swing.JPanel();
        txt_timKiem = new javax.swing.JTextField();
        pnl_btnTimKiem = new javax.swing.JPanel();
        btn_timKiem = new javax.swing.JButton();
        pnl_cta = new javax.swing.JPanel();
        cbo_chucVu = new javax.swing.JComboBox<>();
        cbo_trangThai = new javax.swing.JComboBox<>();
        btn_Loc = new javax.swing.JButton();
        btn_Reset = new javax.swing.JButton();
        pnl_center = new javax.swing.JPanel();
        spl_container = new javax.swing.JSplitPane();
        src_nhanVien = new javax.swing.JScrollPane();
        tbl_nhanVien = new javax.swing.JTable();
        pnl_container = new javax.swing.JPanel();
        pnl_thongTinNhanVien = new javax.swing.JPanel();
        pnl_MaNV = new javax.swing.JPanel();
        lbl_maNV = new javax.swing.JLabel();
        txt_maNV = new javax.swing.JTextField();
        pnl_hoTen = new javax.swing.JPanel();
        lbl_hoTen = new javax.swing.JLabel();
        txt_hoTen = new javax.swing.JTextField();
        pnl_gioiTinh = new javax.swing.JPanel();
        lbl_gioiTinh = new javax.swing.JLabel();
        pnl_radGroup = new javax.swing.JPanel();
        rad_Nam = new javax.swing.JRadioButton();
        rad_Nu = new javax.swing.JRadioButton();
        pnl_ngaySinh = new javax.swing.JPanel();
        lbl_ngaySinh = new javax.swing.JLabel();
        txt_ngaySinh = new com.toedter.calendar.JDateChooser();
        pnl_email = new javax.swing.JPanel();
        lbl_email = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        pnl_sdt = new javax.swing.JPanel();
        lbl_sdt = new javax.swing.JLabel();
        txt_sdt = new javax.swing.JTextField();
        pnl_cccd = new javax.swing.JPanel();
        lbl_cccd = new javax.swing.JLabel();
        txt_cccd = new javax.swing.JTextField();
        pnl_diaChi = new javax.swing.JPanel();
        lbl_diaChi = new javax.swing.JLabel();
        txt_diaChi = new javax.swing.JTextField();
        pnl_chucVu = new javax.swing.JPanel();
        lbl_chucVu = new javax.swing.JLabel();
        cbo_cv = new javax.swing.JComboBox<>();
        pnl_trangThai = new javax.swing.JPanel();
        lbl_trangThai = new javax.swing.JLabel();
        pnl_radGroup2 = new javax.swing.JPanel();
        rad_tt1 = new javax.swing.JRadioButton();
        rad_tt2 = new javax.swing.JRadioButton();
        pnl_btnGroup = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btn_xoaTrang = new javax.swing.JButton();
        btn_capNhat = new javax.swing.JButton();
        btn_themNV = new javax.swing.JButton();
        btn_xuatExcel = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1366, 768));
        setLayout(new java.awt.BorderLayout());

        pnl_top.setBackground(new java.awt.Color(255, 255, 255));
        pnl_top.setMinimumSize(new java.awt.Dimension(20, 20));
        pnl_top.setPreferredSize(new java.awt.Dimension(1366, 50));
        pnl_top.setLayout(new javax.swing.BoxLayout(pnl_top, javax.swing.BoxLayout.LINE_AXIS));

        pnl_timKiem.setPreferredSize(new java.awt.Dimension(579, 30));
        pnl_timKiem.setLayout(new javax.swing.BoxLayout(pnl_timKiem, javax.swing.BoxLayout.LINE_AXIS));

        txt_timKiem.setPreferredSize(new java.awt.Dimension(500, 30));
        pnl_timKiem.add(txt_timKiem);

        pnl_btnTimKiem.setPreferredSize(new java.awt.Dimension(79, 23));
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

        cbo_chucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chức vụ", "Nhân viên quản lý", "Nhân viên bán vé" }));
        cbo_chucVu.setToolTipText("");
        pnl_cta.add(cbo_chucVu);

        cbo_trangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Trạng thái", "Đang làm", "Đã nghỉ" }));
        pnl_cta.add(cbo_trangThai);

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

        spl_container.setResizeWeight(0.6);
        spl_container.setMinimumSize(new java.awt.Dimension(805, 416));
        spl_container.setPreferredSize(new java.awt.Dimension(1055, 718));

        src_nhanVien.setMinimumSize(new java.awt.Dimension(400, 20));
        src_nhanVien.setPreferredSize(new java.awt.Dimension(800, 402));

        tbl_nhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã NV", "Họ tên", "Giới tính", "Ngày sinh", "Email", "SDT", "CCCD", "Địa chỉ", "Chức vụ", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_nhanVien.setMaximumSize(new java.awt.Dimension(1000, 80));
        tbl_nhanVien.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbl_nhanVien.setShowGrid(false);
        tbl_nhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_nhanVienMouseClicked(evt);
            }
        });
        src_nhanVien.setViewportView(tbl_nhanVien);

        spl_container.setLeftComponent(src_nhanVien);

        pnl_container.setBackground(new java.awt.Color(255, 255, 255));
        pnl_container.setMinimumSize(new java.awt.Dimension(400, 379));
        pnl_container.setPreferredSize(new java.awt.Dimension(250, 100));
        pnl_container.setLayout(new java.awt.BorderLayout());

        pnl_thongTinNhanVien.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin nhân viên"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnl_thongTinNhanVien.setLayout(new javax.swing.BoxLayout(pnl_thongTinNhanVien, javax.swing.BoxLayout.Y_AXIS));

        pnl_MaNV.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        pnl_MaNV.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_MaNV.setPreferredSize(new java.awt.Dimension(230, 50));
        pnl_MaNV.setLayout(new javax.swing.BoxLayout(pnl_MaNV, javax.swing.BoxLayout.X_AXIS));

        lbl_maNV.setText("Mã NV:");
        lbl_maNV.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_maNV.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_maNV.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_MaNV.add(lbl_maNV);

        txt_maNV.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txt_maNV.setEnabled(false);
        txt_maNV.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_maNV.setPreferredSize(new java.awt.Dimension(64, 40));
        pnl_MaNV.add(txt_maNV);

        pnl_thongTinNhanVien.add(pnl_MaNV);

        pnl_hoTen.setMaximumSize(pnl_MaNV.getMaximumSize());
        pnl_hoTen.setMinimumSize(pnl_MaNV.getMinimumSize());
        pnl_hoTen.setPreferredSize(pnl_MaNV.getPreferredSize());
        pnl_hoTen.setLayout(new javax.swing.BoxLayout(pnl_hoTen, javax.swing.BoxLayout.LINE_AXIS));

        lbl_hoTen.setText("Họ tên:");
        lbl_hoTen.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_hoTen.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_hoTen.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_hoTen.add(lbl_hoTen);

        txt_hoTen.setMaximumSize(txt_maNV.getMaximumSize());
        txt_hoTen.setPreferredSize(txt_maNV.getPreferredSize());
        pnl_hoTen.add(txt_hoTen);

        pnl_thongTinNhanVien.add(pnl_hoTen);

        pnl_gioiTinh.setMaximumSize(pnl_MaNV.getMaximumSize());
        pnl_gioiTinh.setPreferredSize(pnl_MaNV.getPreferredSize());
        pnl_gioiTinh.setLayout(new javax.swing.BoxLayout(pnl_gioiTinh, javax.swing.BoxLayout.X_AXIS));

        lbl_gioiTinh.setText("Giới tính:");
        lbl_gioiTinh.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_gioiTinh.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_gioiTinh.setPreferredSize(lbl_maNV.getPreferredSize());
        pnl_gioiTinh.add(lbl_gioiTinh);

        pnl_radGroup.setMaximumSize(new java.awt.Dimension(32767, 30));
        pnl_radGroup.setMinimumSize(new java.awt.Dimension(103, 30));
        pnl_radGroup.setPreferredSize(new java.awt.Dimension(103, 30));
        pnl_radGroup.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 4));

        buttonGroup1.add(rad_Nam);
        rad_Nam.setText("Nam");
        pnl_radGroup.add(rad_Nam);

        buttonGroup1.add(rad_Nu);
        rad_Nu.setText("Nữ");
        pnl_radGroup.add(rad_Nu);

        pnl_gioiTinh.add(pnl_radGroup);

        pnl_thongTinNhanVien.add(pnl_gioiTinh);

        pnl_ngaySinh.setMaximumSize(pnl_MaNV.getMaximumSize());
        pnl_ngaySinh.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_ngaySinh.setPreferredSize(pnl_MaNV.getPreferredSize());
        pnl_ngaySinh.setLayout(new javax.swing.BoxLayout(pnl_ngaySinh, javax.swing.BoxLayout.LINE_AXIS));

        lbl_ngaySinh.setText("Ngày sinh:");
        lbl_ngaySinh.setMaximumSize(new java.awt.Dimension(100, 16));
        lbl_ngaySinh.setPreferredSize(lbl_maNV.getPreferredSize());
        pnl_ngaySinh.add(lbl_ngaySinh);

        txt_ngaySinh.setMaximumSize(txt_maNV.getMaximumSize());
        txt_ngaySinh.setMinimumSize(new java.awt.Dimension(141, 0));
        txt_ngaySinh.setPreferredSize(txt_maNV.getPreferredSize());
        pnl_ngaySinh.add(txt_ngaySinh);

        pnl_thongTinNhanVien.add(pnl_ngaySinh);

        pnl_email.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        pnl_email.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_email.setPreferredSize(new java.awt.Dimension(230, 50));
        pnl_email.setLayout(new javax.swing.BoxLayout(pnl_email, javax.swing.BoxLayout.X_AXIS));

        lbl_email.setText("Email:");
        lbl_email.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_email.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_email.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_email.add(lbl_email);

        txt_email.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txt_email.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_email.setPreferredSize(new java.awt.Dimension(64, 40));
        pnl_email.add(txt_email);

        pnl_thongTinNhanVien.add(pnl_email);

        pnl_sdt.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        pnl_sdt.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_sdt.setPreferredSize(new java.awt.Dimension(230, 50));
        pnl_sdt.setLayout(new javax.swing.BoxLayout(pnl_sdt, javax.swing.BoxLayout.LINE_AXIS));

        lbl_sdt.setText("SDT:");
        lbl_sdt.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_sdt.add(lbl_sdt);

        txt_sdt.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_sdt.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_sdt.add(txt_sdt);

        pnl_thongTinNhanVien.add(pnl_sdt);

        pnl_cccd.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        pnl_cccd.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_cccd.setPreferredSize(new java.awt.Dimension(230, 50));
        pnl_cccd.setLayout(new javax.swing.BoxLayout(pnl_cccd, javax.swing.BoxLayout.LINE_AXIS));

        lbl_cccd.setText("CCCD:");
        lbl_cccd.setPreferredSize(lbl_maNV.getPreferredSize());
        pnl_cccd.add(lbl_cccd);

        txt_cccd.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_cccd.setPreferredSize(new java.awt.Dimension(64, 40));
        pnl_cccd.add(txt_cccd);

        pnl_thongTinNhanVien.add(pnl_cccd);

        pnl_diaChi.setMaximumSize(pnl_MaNV.getMaximumSize());
        pnl_diaChi.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_diaChi.setPreferredSize(pnl_MaNV.getPreferredSize());
        pnl_diaChi.setLayout(new javax.swing.BoxLayout(pnl_diaChi, javax.swing.BoxLayout.LINE_AXIS));

        lbl_diaChi.setText("Địa chỉ:");
        lbl_diaChi.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_diaChi.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_diaChi.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_diaChi.add(lbl_diaChi);

        txt_diaChi.setMaximumSize(txt_maNV.getMaximumSize());
        txt_diaChi.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_diaChi.setPreferredSize(txt_maNV.getPreferredSize());
        pnl_diaChi.add(txt_diaChi);

        pnl_thongTinNhanVien.add(pnl_diaChi);

        pnl_chucVu.setMaximumSize(new java.awt.Dimension(32817, 50));
        pnl_chucVu.setPreferredSize(new java.awt.Dimension(230, 50));
        pnl_chucVu.setLayout(new javax.swing.BoxLayout(pnl_chucVu, javax.swing.BoxLayout.LINE_AXIS));

        lbl_chucVu.setText("Chức vụ :");
        lbl_chucVu.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_chucVu.add(lbl_chucVu);

        cbo_cv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Nhân viên quản lý", "Nhân viên bán vé" }));
        cbo_cv.setMaximumSize(new java.awt.Dimension(32767, 40));
        cbo_cv.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_chucVu.add(cbo_cv);

        pnl_thongTinNhanVien.add(pnl_chucVu);

        pnl_trangThai.setMaximumSize(new java.awt.Dimension(100000, 50));
        pnl_trangThai.setPreferredSize(new java.awt.Dimension(230, 50));
        pnl_trangThai.setLayout(new javax.swing.BoxLayout(pnl_trangThai, javax.swing.BoxLayout.LINE_AXIS));

        lbl_trangThai.setText("Trạng thái:");
        lbl_trangThai.setPreferredSize(lbl_maNV.getPreferredSize());
        pnl_trangThai.add(lbl_trangThai);

        pnl_radGroup2.setMaximumSize(new java.awt.Dimension(32767, 40));
        pnl_radGroup2.setPreferredSize(new java.awt.Dimension(156, 40));
        pnl_radGroup2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        buttonGroup2.add(rad_tt1);
        rad_tt1.setText("Đang làm");
        pnl_radGroup2.add(rad_tt1);

        buttonGroup2.add(rad_tt2);
        rad_tt2.setText("Đã nghỉ");
        pnl_radGroup2.add(rad_tt2);

        pnl_trangThai.add(pnl_radGroup2);

        pnl_thongTinNhanVien.add(pnl_trangThai);

        pnl_container.add(pnl_thongTinNhanVien, java.awt.BorderLayout.CENTER);

        pnl_btnGroup.setPreferredSize(new java.awt.Dimension(10, 100));
        pnl_btnGroup.setLayout(new java.awt.BorderLayout());

        jPanel5.setMaximumSize(new java.awt.Dimension(32767, 100));
        jPanel5.setMinimumSize(new java.awt.Dimension(281, 100));
        jPanel5.setLayout(new java.awt.GridLayout(2, 2, 3, 3));

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

        btn_themNV.setText("Thêm nhân viên");
        btn_themNV.setMaximumSize(new java.awt.Dimension(139, 23));
        btn_themNV.setMinimumSize(new java.awt.Dimension(139, 23));
        btn_themNV.setPreferredSize(new java.awt.Dimension(72, 50));
        btn_themNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themNVActionPerformed(evt);
            }
        });
        jPanel5.add(btn_themNV);

        btn_xuatExcel.setText("Xuất Excel");
        btn_xuatExcel.setMaximumSize(new java.awt.Dimension(120, 50));
        btn_xuatExcel.setPreferredSize(new java.awt.Dimension(120, 50));
        btn_xuatExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xuatExcelActionPerformed(evt);
            }
        });
        jPanel5.add(btn_xuatExcel);

        pnl_btnGroup.add(jPanel5, java.awt.BorderLayout.CENTER);

        pnl_container.add(pnl_btnGroup, java.awt.BorderLayout.PAGE_END);

        spl_container.setRightComponent(pnl_container);

        pnl_center.add(spl_container);

        add(pnl_center, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_capNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_capNhatActionPerformed
        // TODO add your handling code here:
        handleCapNhat();
    }//GEN-LAST:event_btn_capNhatActionPerformed

    private void btn_xoaTrangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaTrangActionPerformed
        handleActionXoaTrang();
    }//GEN-LAST:event_btn_xoaTrangActionPerformed

    private void tbl_nhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_nhanVienMouseClicked
        getThongTinNhanVien();
    }//GEN-LAST:event_tbl_nhanVienMouseClicked

    private void btn_timKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_timKiemActionPerformed
        handleActionTimKiem();
    }//GEN-LAST:event_btn_timKiemActionPerformed

    private void btn_LocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LocActionPerformed
        handleActionLoc();
    }//GEN-LAST:event_btn_LocActionPerformed

    private void btn_ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ResetActionPerformed
        handleActionLamMoi();
    }//GEN-LAST:event_btn_ResetActionPerformed

    private void btn_themNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themNVActionPerformed
        handleThemNV();
    }//GEN-LAST:event_btn_themNVActionPerformed

    private void btn_xuatExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xuatExcelActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn đường dẫn và tên file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Hiển thị hộp thoại và kiểm tra nếu người dùng chọn OK
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                // Lấy đường dẫn và tên file được chọn
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                // Gọi phương thức để tạo file Excel với đường dẫn và tên file đã chọn
                createExcel(bus.getAllNhanVien(), filePath+".xlsx");
                Desktop.getDesktop().open(new File(filePath+".xlsx"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btn_xuatExcelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Loc;
    private javax.swing.JButton btn_Reset;
    private javax.swing.JButton btn_capNhat;
    private javax.swing.JButton btn_themNV;
    private javax.swing.JButton btn_timKiem;
    private javax.swing.JButton btn_xoaTrang;
    private javax.swing.JButton btn_xuatExcel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> cbo_chucVu;
    private javax.swing.JComboBox<String> cbo_cv;
    private javax.swing.JComboBox<String> cbo_trangThai;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lbl_cccd;
    private javax.swing.JLabel lbl_chucVu;
    private javax.swing.JLabel lbl_diaChi;
    private javax.swing.JLabel lbl_email;
    private javax.swing.JLabel lbl_gioiTinh;
    private javax.swing.JLabel lbl_hoTen;
    private javax.swing.JLabel lbl_maNV;
    private javax.swing.JLabel lbl_ngaySinh;
    private javax.swing.JLabel lbl_sdt;
    private javax.swing.JLabel lbl_trangThai;
    private javax.swing.JPanel pnl_MaNV;
    private javax.swing.JPanel pnl_btnGroup;
    private javax.swing.JPanel pnl_btnTimKiem;
    private javax.swing.JPanel pnl_cccd;
    private javax.swing.JPanel pnl_center;
    private javax.swing.JPanel pnl_chucVu;
    private javax.swing.JPanel pnl_container;
    private javax.swing.JPanel pnl_cta;
    private javax.swing.JPanel pnl_diaChi;
    private javax.swing.JPanel pnl_email;
    private javax.swing.JPanel pnl_gioiTinh;
    private javax.swing.JPanel pnl_hoTen;
    private javax.swing.JPanel pnl_ngaySinh;
    private javax.swing.JPanel pnl_radGroup;
    private javax.swing.JPanel pnl_radGroup2;
    private javax.swing.JPanel pnl_sdt;
    private javax.swing.JPanel pnl_thongTinNhanVien;
    private javax.swing.JPanel pnl_timKiem;
    private javax.swing.JPanel pnl_top;
    private javax.swing.JPanel pnl_trangThai;
    private javax.swing.JRadioButton rad_Nam;
    private javax.swing.JRadioButton rad_Nu;
    private javax.swing.JRadioButton rad_tt1;
    private javax.swing.JRadioButton rad_tt2;
    private javax.swing.JSplitPane spl_container;
    private javax.swing.JScrollPane src_nhanVien;
    private javax.swing.JTable tbl_nhanVien;
    private javax.swing.JTextField txt_cccd;
    private javax.swing.JTextField txt_diaChi;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_hoTen;
    private javax.swing.JTextField txt_maNV;
    private com.toedter.calendar.JDateChooser txt_ngaySinh;
    private javax.swing.JTextField txt_sdt;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
