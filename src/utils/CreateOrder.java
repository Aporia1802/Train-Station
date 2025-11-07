package utils;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import entity.HoaDon;
import entity.Ve;

public class CreateOrder {
    private static Font getFont(float size, int style) {
        try {
            String fontPath = "resources/fonts/Roboto-Regular.ttf";
            com.itextpdf.text.pdf.BaseFont baseFont = com.itextpdf.text.pdf.BaseFont.createFont(fontPath, com.itextpdf.text.pdf.BaseFont.IDENTITY_H, com.itextpdf.text.pdf.BaseFont.EMBEDDED);
            return new Font(baseFont, size, style);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font(Font.FontFamily.TIMES_ROMAN, size, style);
        }
    }

    public static void taoHD(ArrayList<Ve> danhSachVe, HoaDon hd) {
        DecimalFormat df = new DecimalFormat("#,###");
        String defaultFolderPath = "src/data/";
        String fileName = hd.getMaHoaDon() + "_hoadon.pdf";

        Document document = new Document(new Rectangle(400, 900));

        try {
            PdfWriter.getInstance(document, new FileOutputStream(defaultFolderPath + fileName));
            document.open();

            // Logo và Tiêu đề công ty
            Paragraph companyName = new Paragraph("NHÀ GA SỐ 9 3/4", getFont(16, Font.BOLD));
            companyName.setAlignment(Element.ALIGN_CENTER);
            document.add(companyName);

            // Địa chỉ và thông tin liên hệ
            Paragraph address = new Paragraph("12 Nguyễn Văn Bảo, Phường 4, Quận Gò Vấp\nTP. Hồ Chí Minh", getFont(10, Font.NORMAL));
            address.setAlignment(Element.ALIGN_CENTER);
            document.add(address);
            
            Paragraph contact = new Paragraph("Hotline: 1900 xxxx | Email: support@nhaga934.vn", getFont(9, Font.NORMAL));
            contact.setAlignment(Element.ALIGN_CENTER);
            document.add(contact);
            
            document.add(new Paragraph("\n"));

            // Tiêu đề hóa đơn
            Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN", getFont(18, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            // Thông tin hóa đơn
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            LocalDateTime ngayLap = hd.getNgayLapHoaDon() != null ? hd.getNgayLapHoaDon() : LocalDateTime.now();
            
            document.add(new Paragraph("Mã hóa đơn: " + hd.getMaHoaDon(), getFont(11, Font.BOLD)));
            document.add(new Paragraph("Ngày lập: " + dateFormatter.format(ngayLap), getFont(10, Font.NORMAL)));
            document.add(new Paragraph("Nhân viên: " + (hd.getNhanVien() != null ? hd.getNhanVien().getTenNV() : "Hệ thống"), getFont(10, Font.NORMAL)));

            document.add(new Paragraph("===========================================================", getFont(10, Font.NORMAL)));

            // Thông tin khách hàng
            Paragraph customerTitle = new Paragraph("\nTHÔNG TIN KHÁCH HÀNG", getFont(12, Font.BOLD));
            document.add(customerTitle);
            document.add(new Paragraph("Họ tên: " + hd.getKhachHang().getTenKH(), getFont(11, Font.NORMAL)));
            document.add(new Paragraph("Số điện thoại: " + hd.getKhachHang().getSoDienThoai(), getFont(11, Font.NORMAL)));
            document.add(new Paragraph("CCCD: " + hd.getKhachHang().getCccd(), getFont(11, Font.NORMAL)));

            document.add(new Paragraph("===========================================================", getFont(10, Font.NORMAL)));

            // Thông tin chuyến tàu
            Paragraph tripTitle = new Paragraph("\nTHÔNG TIN CHUYẾN ĐI", getFont(12, Font.BOLD));
            document.add(tripTitle);
            
            Ve veFirst = danhSachVe.get(0);
            document.add(new Paragraph("Mã chuyến: " + veFirst.getChuyenTau().getMaChuyenTau(), getFont(11, Font.NORMAL)));
            document.add(new Paragraph("Tàu: " + veFirst.getChuyenTau().getTau().getTenTau() + " (Số hiệu: " + veFirst.getChuyenTau().getTau().getTenTau() + ")", getFont(11, Font.NORMAL)));
            document.add(new Paragraph("Tuyến: " + veFirst.getChuyenTau().getTuyenDuong().getGaDi().getTenGa() + " → " + veFirst.getChuyenTau().getTuyenDuong().getGaDen().getTenGa(), getFont(11, Font.NORMAL)));
            
            DateTimeFormatter tripDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.ENGLISH);
            document.add(new Paragraph("Khởi hành: " + tripDateFormatter.format(veFirst.getChuyenTau().getThoiGianDi()), getFont(11, Font.NORMAL)));
            document.add(new Paragraph("Dự kiến đến: " + tripDateFormatter.format(veFirst.getChuyenTau().getThoiGianDen()), getFont(11, Font.NORMAL)));

            document.add(new Paragraph("===========================================================", getFont(10, Font.NORMAL)));

            // Bảng chi tiết vé
            Paragraph ticketTitle = new Paragraph("\nCHI TIẾT VÉ", getFont(12, Font.BOLD));
            document.add(ticketTitle);
            document.add(new Paragraph("\n"));
            
            PdfPTable bangVe = new PdfPTable(new float[]{1.5f, 1.5f, 1f, 1.2f});
            bangVe.setWidthPercentage(100);
            
            // Header
            bangVe.addCell(createCell("Hành khách", getFont(10, Font.BOLD), Element.ALIGN_LEFT));
            bangVe.addCell(createCell("Loại vé", getFont(10, Font.BOLD), Element.ALIGN_CENTER));
            bangVe.addCell(createCell("Ghế", getFont(10, Font.BOLD), Element.ALIGN_CENTER));
            bangVe.addCell(createCell("Giá", getFont(10, Font.BOLD), Element.ALIGN_RIGHT));
            
            // Dữ liệu
            double tongTienVe = 0;
            for (Ve ve : danhSachVe) {
                bangVe.addCell(createCell(ve.getHanhKhach().getTenHanhKhach(), getFont(10, Font.NORMAL), Element.ALIGN_LEFT));
                bangVe.addCell(createCell(ve.getLoaiVe().getTenLoaiVe(), getFont(10, Font.NORMAL), Element.ALIGN_CENTER));
                bangVe.addCell(createCell(String.valueOf(ve.getGhe().getSoGhe()), getFont(10, Font.NORMAL), Element.ALIGN_CENTER));
                bangVe.addCell(createCell(df.format(ve.getGiaVe()), getFont(10, Font.NORMAL), Element.ALIGN_RIGHT));
                tongTienVe += ve.getGiaVe();
            }
            
            document.add(bangVe);
            document.add(new Paragraph("===========================================================", getFont(10, Font.NORMAL)));

            // Bảng thanh toán
            PdfPTable bangThanhToan = new PdfPTable(new float[]{3, 1.5f});
            bangThanhToan.setWidthPercentage(100);
            bangThanhToan.setSpacingBefore(10f);
            
            // Tạm tính
            bangThanhToan.addCell(createCell("Tạm tính (" + danhSachVe.size() + " vé):", getFont(11, Font.NORMAL), Element.ALIGN_LEFT));
            bangThanhToan.addCell(createCell(df.format(tongTienVe) + " đ", getFont(11, Font.NORMAL), Element.ALIGN_RIGHT));
            
            // VAT
            double tienVAT = tongTienVe * hd.getVAT();
            bangThanhToan.addCell(createCell("VAT (" + (int)(hd.getVAT() * 100) + "%):", getFont(11, Font.NORMAL), Element.ALIGN_LEFT));
            bangThanhToan.addCell(createCell(df.format(tienVAT) + " đ", getFont(11, Font.NORMAL), Element.ALIGN_RIGHT));
            
            // Khuyến mãi (nếu có)
            double tienKhuyenMai = 0;
            if (hd.getKhuyenMai() != null) {
                tienKhuyenMai = tongTienVe * hd.getKhuyenMai().getHeSoKhuyenMai();
                bangThanhToan.addCell(createCell("Khuyến mãi (" + hd.getKhuyenMai().getTenKhuyenMai() + "):", getFont(11, Font.NORMAL), Element.ALIGN_LEFT));
                bangThanhToan.addCell(createCell("-" + df.format(tienKhuyenMai) + " đ", getFont(11, Font.NORMAL), Element.ALIGN_RIGHT));
            } else {
                bangThanhToan.addCell(createCell("Khuyến mãi:", getFont(11, Font.NORMAL), Element.ALIGN_LEFT));
                bangThanhToan.addCell(createCell("0 đ", getFont(11, Font.NORMAL), Element.ALIGN_RIGHT));
            }
            
            // Dòng kẻ
            PdfPCell lineCell = new PdfPCell(new Phrase(""));
            lineCell.setColspan(2);
            lineCell.setBorderWidthTop(1);
            lineCell.setBorderWidthBottom(0);
            lineCell.setBorderWidthLeft(0);
            lineCell.setBorderWidthRight(0);
            bangThanhToan.addCell(lineCell);
            
            // Tổng tiền
            bangThanhToan.addCell(createCell("TỔNG TIỀN:", getFont(13, Font.BOLD), Element.ALIGN_LEFT));
            bangThanhToan.addCell(createCell(df.format(hd.getTongTien()) + " đ", getFont(13, Font.BOLD), Element.ALIGN_RIGHT));
            
            document.add(bangThanhToan);
            document.add(new Paragraph("\n"));

            // Phương thức thanh toán
            document.add(new Paragraph("Phương thức: Tiền mặt/Chuyển khoản", getFont(10, Font.ITALIC)));
            document.add(new Paragraph("Trạng thái: Đã thanh toán", getFont(10, Font.ITALIC)));

            document.add(new Paragraph("\n"));

            // Lưu ý
            Paragraph note = new Paragraph("LƯU Ý:", getFont(10, Font.BOLD));
            document.add(note);
            document.add(new Paragraph("- Vui lòng có mặt trước giờ tàu chạy 30 phút", getFont(9, Font.NORMAL)));
            document.add(new Paragraph("- Xuất trình CCCD/CMND khi lên tàu", getFont(9, Font.NORMAL)));
            document.add(new Paragraph("- Giữ vé và hóa đơn để kiểm tra khi cần", getFont(9, Font.NORMAL)));

            document.add(new Paragraph("\n"));

            // Mã QR Code
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.MARGIN, 1);
            BitMatrix qrCodeMatrix = new MultiFormatWriter().encode(hd.getMaHoaDon(), BarcodeFormat.QR_CODE, 120, 120, hintMap);
            MatrixToImageWriter.writeToStream(qrCodeMatrix, "PNG", baos);
            Image qrCodeImage = Image.getInstance(baos.toByteArray());
            qrCodeImage.setAlignment(Element.ALIGN_CENTER);
            document.add(qrCodeImage);

            // Footer
            Paragraph footer = new Paragraph("\nCảm ơn quý khách đã sử dụng dịch vụ!\nChúc quý khách có chuyến đi vui vẻ!", getFont(10, Font.ITALIC));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            
            // Mở file PDF
            try {
                if (Desktop.isDesktopSupported()) {
                    File pdfFile = new File(defaultFolderPath + fileName);
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("Desktop is not supported.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (DocumentException | IOException | WriterException e) {
            e.printStackTrace();
        }
    }
    
    private static PdfPCell createCell(String content, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingTop(3);
        cell.setPaddingBottom(3);
        return cell;
    }
}