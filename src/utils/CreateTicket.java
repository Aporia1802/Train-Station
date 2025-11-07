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

import entity.Ve;

public class CreateTicket {
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

    public static void taoVe(Ve ve) {
        DecimalFormat df = new DecimalFormat("#,###");
        String defaultFolderPath = "src/data/";
        String fileName = ve.getMaVe() + "_ve.pdf";

        Document document = new Document(new Rectangle(400, 650));

        try {
            PdfWriter.getInstance(document, new FileOutputStream(defaultFolderPath + fileName));
            document.open();

            // Logo và tiêu đề công ty
            Paragraph companyName = new Paragraph("NHÀ GA SỐ 9 3/4", getFont(16, Font.BOLD));
            companyName.setAlignment(Element.ALIGN_CENTER);
            document.add(companyName);

            Paragraph address = new Paragraph("12 Nguyễn Văn Bảo, Phường 4, Quận Gò Vấp\nTP. Hồ Chí Minh", getFont(9, Font.NORMAL));
            address.setAlignment(Element.ALIGN_CENTER);
            document.add(address);

            document.add(new Paragraph("\n"));

            // Tiêu đề vé
            Paragraph title = new Paragraph("VÉ TÀU HỎA", getFont(20, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            // Mã vé
            Paragraph ticketCode = new Paragraph("Mã vé: " + ve.getMaVe(), getFont(11, Font.BOLD));
            ticketCode.setAlignment(Element.ALIGN_CENTER);
            document.add(ticketCode);

            document.add(new Paragraph("===========================================================", getFont(10, Font.NORMAL)));

            // Thông tin hành khách
            Paragraph passengerTitle = new Paragraph("\nTHÔNG TIN HÀNH KHÁCH", getFont(12, Font.BOLD));
            document.add(passengerTitle);
            document.add(new Paragraph("Họ tên: " + ve.getHanhKhach().getTenHanhKhach(), getFont(11, Font.NORMAL)));
            document.add(new Paragraph("CCCD: " + ve.getHanhKhach().getCccd(), getFont(11, Font.NORMAL)));
            document.add(new Paragraph("Loại vé: " + ve.getLoaiVe().getTenLoaiVe(), getFont(11, Font.NORMAL)));

            document.add(new Paragraph("===========================================================", getFont(10, Font.NORMAL)));

            // Thông tin chuyến tàu
            Paragraph tripTitle = new Paragraph("\nTHÔNG TIN CHUYẾN TÀU", getFont(12, Font.BOLD));
            document.add(tripTitle);
            
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
            
            // Bảng tuyến đường
            PdfPTable routeTable = new PdfPTable(new float[]{1, 0.3f, 1});
            routeTable.setWidthPercentage(100);
            routeTable.setSpacingBefore(5f);
            routeTable.setSpacingAfter(5f);
            
            // Ga đi
            PdfPCell departCell = new PdfPCell();
            departCell.setBorder(Rectangle.NO_BORDER);
            Paragraph departStation = new Paragraph(ve.getChuyenTau().getTuyenDuong().getGaDi().getTenGa(), getFont(12, Font.BOLD));
            departStation.setAlignment(Element.ALIGN_CENTER);
            Paragraph departTime = new Paragraph(timeFormatter.format(ve.getChuyenTau().getThoiGianDi()), getFont(10, Font.NORMAL));
            departTime.setAlignment(Element.ALIGN_CENTER);
            departCell.addElement(departStation);
            departCell.addElement(departTime);
            routeTable.addCell(departCell);
            
            // Mũi tên
            PdfPCell arrowCell = createCell("→", getFont(16, Font.BOLD), Element.ALIGN_CENTER);
            routeTable.addCell(arrowCell);
            
            // Ga đến
            PdfPCell arriveCell = new PdfPCell();
            arriveCell.setBorder(Rectangle.NO_BORDER);
            Paragraph arriveStation = new Paragraph(ve.getChuyenTau().getTuyenDuong().getGaDen().getTenGa(), getFont(12, Font.BOLD));
            arriveStation.setAlignment(Element.ALIGN_CENTER);
            Paragraph arriveTime = new Paragraph(timeFormatter.format(ve.getChuyenTau().getThoiGianDen()), getFont(10, Font.NORMAL));
            arriveTime.setAlignment(Element.ALIGN_CENTER);
            arriveCell.addElement(arriveStation);
            arriveCell.addElement(arriveTime);
            routeTable.addCell(arriveCell);
            
            document.add(routeTable);
            
            // Thông tin chi tiết chuyến
            document.add(new Paragraph("Ngày đi: " + dateFormatter.format(ve.getChuyenTau().getThoiGianDi()), getFont(11, Font.NORMAL)));
            document.add(new Paragraph("Mã chuyến: " + ve.getChuyenTau().getMaChuyenTau(), getFont(10, Font.NORMAL)));
            document.add(new Paragraph("Tàu: " + ve.getChuyenTau().getTau().getTenTau() + " (Số hiệu: " + ve.getChuyenTau().getTau().getTenTau()+ ")", getFont(10, Font.NORMAL)));

            document.add(new Paragraph("===========================================================", getFont(10, Font.NORMAL)));

            // Thông tin chỗ ngồi
            Paragraph seatTitle = new Paragraph("\nTHÔNG TIN CHỖ NGỒI", getFont(12, Font.BOLD));
            document.add(seatTitle);
            
            // Bảng thông tin ghế
            PdfPTable seatTable = new PdfPTable(3);
            seatTable.setWidthPercentage(100);
            seatTable.setSpacingBefore(5f);
            
            // Toa
            PdfPCell toaCell = new PdfPCell();
            toaCell.setBorder(Rectangle.BOX);
            toaCell.setPadding(8);
            Paragraph toaLabel = new Paragraph("TOA", getFont(9, Font.NORMAL));
            toaLabel.setAlignment(Element.ALIGN_CENTER);
            Paragraph toaValue = new Paragraph(String.valueOf(ve.getGhe().getKhoangTau().getToaTau().getSoHieuToa()), getFont(14, Font.BOLD));
            toaValue.setAlignment(Element.ALIGN_CENTER);
            toaCell.addElement(toaLabel);
            toaCell.addElement(toaValue);
            seatTable.addCell(toaCell);
            
            
            // Ghế
            PdfPCell gheCell = new PdfPCell();
            gheCell.setBorder(Rectangle.BOX);
            gheCell.setPadding(8);
            Paragraph gheLabel = new Paragraph("GHẾ", getFont(9, Font.NORMAL));
            gheLabel.setAlignment(Element.ALIGN_CENTER);
            Paragraph gheValue = new Paragraph(String.valueOf(ve.getGhe().getSoGhe()), getFont(14, Font.BOLD));
            gheValue.setAlignment(Element.ALIGN_CENTER);
            gheCell.addElement(gheLabel);
            gheCell.addElement(gheValue);
            seatTable.addCell(gheCell);
            
            document.add(seatTable);
            
            document.add(new Paragraph("\nLoại ghế: " + ve.getGhe().getLoaiGhe().getTenLoaiGhe(), getFont(10, Font.NORMAL)));

            document.add(new Paragraph("===========================================================", getFont(10, Font.NORMAL)));

            // Giá vé
            Paragraph priceTitle = new Paragraph("\nGIÁ VÉ", getFont(12, Font.BOLD));
            document.add(priceTitle);
            Paragraph price = new Paragraph(df.format(ve.getGiaVe()) + " đ", getFont(16, Font.BOLD));
            price.setAlignment(Element.ALIGN_CENTER);
            document.add(price);

            document.add(new Paragraph("\n"));

            // Lưu ý
            Paragraph note = new Paragraph("LƯU Ý:", getFont(9, Font.BOLD));
            document.add(note);
            document.add(new Paragraph("• Có mặt trước giờ tàu chạy 30 phút", getFont(8, Font.NORMAL)));
            document.add(new Paragraph("• Xuất trình CCCD/CMND và vé khi lên tàu", getFont(8, Font.NORMAL)));
            document.add(new Paragraph("• Giữ vé trong suốt hành trình", getFont(8, Font.NORMAL)));

            document.add(new Paragraph("\n"));

            // Mã QR Code
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.MARGIN, 1);
            BitMatrix qrCodeMatrix = new MultiFormatWriter().encode(ve.getMaVe(), BarcodeFormat.QR_CODE, 120, 120, hintMap);
            MatrixToImageWriter.writeToStream(qrCodeMatrix, "PNG", baos);
            Image qrCodeImage = Image.getInstance(baos.toByteArray());
            qrCodeImage.setAlignment(Element.ALIGN_CENTER);
            document.add(qrCodeImage);

            // Footer
            Paragraph footer = new Paragraph("\nChúc quý khách có chuyến đi an toàn và vui vẻ!", getFont(9, Font.ITALIC));
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