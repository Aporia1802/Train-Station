package utils;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
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

import entity.HoaDonTra;
import entity.Ve;

public class CreateReturnOrder {
    private HoaDonTra hoaDonTra;
    
    public CreateReturnOrder(HoaDonTra hoaDonTra) {
        this.hoaDonTra = hoaDonTra;
    }
    
    private static Font getFont(float size, int style) {
        try {
            String fontPath = "resources/fonts/Roboto-Regular.ttf";
            com.itextpdf.text.pdf.BaseFont baseFont = com.itextpdf.text.pdf.BaseFont.createFont(
                fontPath, 
                com.itextpdf.text.pdf.BaseFont.IDENTITY_H, 
                com.itextpdf.text.pdf.BaseFont.EMBEDDED
            );
            return new Font(baseFont, size, style);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font(Font.FontFamily.TIMES_ROMAN, size, style);
        }
    }
    
    public void xuatHoaDonTra() {
        DecimalFormat df = new DecimalFormat("#,###");
        String defaultFolderPath = "src/data/";
        String fileName = hoaDonTra.getMaHoaDonTra() + "_hoadontra.pdf";

        Document document = new Document(new Rectangle(400, 900));

        try {
            PdfWriter.getInstance(document, new FileOutputStream(defaultFolderPath + fileName));
            document.open();

            Ve ve = hoaDonTra.getVe();
            
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

            // Tiêu đề hóa đơn trả
            Paragraph title = new Paragraph("HÓA ĐƠN TRẢ VÉ", getFont(18, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            // Thông tin hóa đơn trả
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            
            document.add(new Paragraph("Mã hóa đơn trả: " + hoaDonTra.getMaHoaDonTra(), getFont(11, Font.BOLD)));
            document.add(new Paragraph("Ngày trả vé: " + dateFormatter.format(hoaDonTra.getNgayTra()), getFont(10, Font.NORMAL)));
            document.add(new Paragraph("Nhân viên xử lý: " + hoaDonTra.getNhanVien().getTenNV(), getFont(10, Font.NORMAL)));

            document.add(new Paragraph("===========================================================", getFont(10, Font.NORMAL)));

            // Thông tin hành khách
            Paragraph customerTitle = new Paragraph("\nTHÔNG TIN HÀNH KHÁCH", getFont(12, Font.BOLD));
            document.add(customerTitle);
            document.add(new Paragraph("Họ tên: " + ve.getHanhKhach().getTenHanhKhach(), getFont(11, Font.NORMAL)));
            document.add(new Paragraph("CCCD: " + ve.getHanhKhach().getCccd(), getFont(11, Font.NORMAL)));

            document.add(new Paragraph("===========================================================", getFont(10, Font.NORMAL)));

            // Thông tin vé bị trả
            Paragraph ticketTitle = new Paragraph("\nTHÔNG TIN VÉ BỊ TRẢ", getFont(12, Font.BOLD));
            document.add(ticketTitle);
            
            document.add(new Paragraph("Mã vé: " + ve.getMaVe(), getFont(11, Font.NORMAL)));
            document.add(new Paragraph("Loại vé: " + ve.getLoaiVe().getTenLoaiVe(), getFont(11, Font.NORMAL)));
            document.add(new Paragraph("Mã chuyến: " + ve.getChuyenTau().getMaChuyenTau(), getFont(11, Font.NORMAL)));
            document.add(new Paragraph("Tàu: " + ve.getChuyenTau().getTau().getTenTau() + " (" + ve.getChuyenTau().getTau().getMaTau() + ")", getFont(11, Font.NORMAL)));
            document.add(new Paragraph("Tuyến: " + ve.getChuyenTau().getTuyenDuong().getGaDi().getTenGa() + " → " + ve.getChuyenTau().getTuyenDuong().getGaDen().getTenGa(), getFont(11, Font.NORMAL)));
            
            DateTimeFormatter tripDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.ENGLISH);
            document.add(new Paragraph("Khởi hành: " + tripDateFormatter.format(ve.getChuyenTau().getThoiGianDi()), getFont(11, Font.NORMAL)));
            document.add(new Paragraph("Dự kiến đến: " + tripDateFormatter.format(ve.getChuyenTau().getThoiGianDen()), getFont(11, Font.NORMAL)));
            
            document.add(new Paragraph("Số ghế: Toa " + ve.getGhe().getKhoangTau().getToaTau().getSoHieuToa() + " - Chỗ " + ve.getGhe().getSoGhe(), getFont(11, Font.NORMAL)));

            document.add(new Paragraph("===========================================================", getFont(10, Font.NORMAL)));

            // Bảng chi tiết hoàn tiền
            Paragraph refundTitle = new Paragraph("\nCHI TIẾT HOÀN TIỀN", getFont(12, Font.BOLD));
            document.add(refundTitle);
            document.add(new Paragraph("\n"));
            
            PdfPTable bangHoanTien = new PdfPTable(new float[]{3, 1.5f});
            bangHoanTien.setWidthPercentage(100);
            bangHoanTien.setSpacingBefore(10f);
            
            // Giá vé gốc
            bangHoanTien.addCell(createCell("Giá vé gốc:", getFont(11, Font.NORMAL), Element.ALIGN_LEFT));
            bangHoanTien.addCell(createCell(df.format(ve.getGiaVe()) + " đ", getFont(11, Font.NORMAL), Element.ALIGN_RIGHT));
            
            // Phí hủy
            double phiHuy = hoaDonTra.phiHuy();
            bangHoanTien.addCell(createCell("Phí hủy vé:", getFont(11, Font.NORMAL), Element.ALIGN_LEFT));
            PdfPCell phiHuyCell = createCell("-" + df.format(phiHuy) + " đ", getFont(11, Font.NORMAL), Element.ALIGN_RIGHT);
            phiHuyCell.getPhrase().getFont().setColor(255, 0, 0); // Màu đỏ
            bangHoanTien.addCell(phiHuyCell);
            
            // Dòng kẻ
            PdfPCell lineCell = new PdfPCell(new Phrase(""));
            lineCell.setColspan(2);
            lineCell.setBorderWidthTop(1);
            lineCell.setBorderWidthBottom(0);
            lineCell.setBorderWidthLeft(0);
            lineCell.setBorderWidthRight(0);
            bangHoanTien.addCell(lineCell);
            
            // Số tiền hoàn trả
            bangHoanTien.addCell(createCell("SỐ TIỀN HOÀN TRẢ:", getFont(13, Font.BOLD), Element.ALIGN_LEFT));
            PdfPCell hoantienCell = createCell(df.format(hoaDonTra.getSoTienHoanTra()) + " đ", getFont(13, Font.BOLD), Element.ALIGN_RIGHT);
            hoantienCell.getPhrase().getFont().setColor(0, 153, 51); // Màu xanh lá
            bangHoanTien.addCell(hoantienCell);
            
            document.add(bangHoanTien);
            document.add(new Paragraph("\n"));

            // Phương thức hoàn tiền
            document.add(new Paragraph("Phương thức: Hoàn tiền mặt tại quầy", getFont(10, Font.ITALIC)));
            document.add(new Paragraph("Trạng thái: Đã xử lý", getFont(10, Font.ITALIC)));

            document.add(new Paragraph("\n"));

            // Lưu ý
            Paragraph note = new Paragraph("LƯU Ý:", getFont(10, Font.BOLD));
            document.add(note);
            document.add(new Paragraph("- Vé đã trả không thể khôi phục", getFont(9, Font.NORMAL)));
            document.add(new Paragraph("- Số tiền hoàn trả đã được chi trả tại quầy", getFont(9, Font.NORMAL)));
            document.add(new Paragraph("- Giữ hóa đơn này để làm chứng từ", getFont(9, Font.NORMAL)));

            document.add(new Paragraph("\n"));

            // Mã QR Code
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.MARGIN, 1);
            BitMatrix qrCodeMatrix = new MultiFormatWriter().encode(
                hoaDonTra.getMaHoaDonTra(), 
                BarcodeFormat.QR_CODE, 
                120, 
                120, 
                hintMap
            );
            MatrixToImageWriter.writeToStream(qrCodeMatrix, "PNG", baos);
            Image qrCodeImage = Image.getInstance(baos.toByteArray());
            qrCodeImage.setAlignment(Element.ALIGN_CENTER);
            document.add(qrCodeImage);

            // Chữ ký
            document.add(new Paragraph("\n"));
            PdfPTable signatureTable = new PdfPTable(2);
            signatureTable.setWidthPercentage(100);
            
            PdfPCell nhanVienCell = createCell("Nhân viên\n\n\n\n(Ký và ghi rõ họ tên)", getFont(10, Font.NORMAL), Element.ALIGN_CENTER);
            PdfPCell khachHangCell = createCell("Khách hàng\n\n\n\n(Ký và ghi rõ họ tên)", getFont(10, Font.NORMAL), Element.ALIGN_CENTER);
            
            signatureTable.addCell(nhanVienCell);
            signatureTable.addCell(khachHangCell);
            document.add(signatureTable);

            // Footer
            Paragraph footer = new Paragraph("\nCảm ơn quý khách đã sử dụng dịch vụ!\nChúc quý khách một ngày tốt lành!", getFont(10, Font.ITALIC));
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