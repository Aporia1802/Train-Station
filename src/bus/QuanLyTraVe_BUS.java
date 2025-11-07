package bus;

import dao.ChuyenTau_DAO;
import dao.Ghe_DAO;
import dao.HoaDonTra_DAO;
import dao.Ve_DAO;
import entity.HoaDonTra;
import entity.Ve;

/**
 * Business Logic Layer cho chức năng Trả vé
 * @author CÔNG HOÀNG
 */
public class QuanLyTraVe_BUS {
    private final HoaDonTra_DAO hoaDonTraDAO = new HoaDonTra_DAO();
    private final Ve_DAO veDAO = new Ve_DAO();
    private final Ghe_DAO gheDAO = new Ghe_DAO();
    private final ChuyenTau_DAO chuyenTauDAO = new ChuyenTau_DAO();
    
    public Ve getVeById(String maVe) {
        return veDAO.getOne(maVe);
    }
    
    public String generateID() {
        // Lấy mã lớn nhất
        String maxID = hoaDonTraDAO.getMaxID();
        
        if(maxID.equals("")) {
            return "HDT001";
        }
        
        // Tách phần số
        int num = Integer.parseInt(maxID.substring(3));
        
        // Tăng lên một đơn vị
        num++;
        
        // Tạo mã mới 
        String newID = String.format("HDT%03d", num);
        
        return newID;
    }
    
    /**
     * Thực hiện trả vé theo quy trình
     * @param hdt Hóa đơn trả
     * @return true nếu thành công
     * @throws Exception 
     */
    public boolean create(HoaDonTra hdt) throws Exception {
        
        // 1. Tạo và lưu hóa đơn trả
        if (!hoaDonTraDAO.create(hdt)) {
            throw new Exception("Không thể lưu hóa đơn trả!");
        }
        
        // 2. Cập nhật trạng thái vé thành "Đã hủy" (trạng thái = 3)
        Ve ve = hdt.getVe();
        if (!veDAO.updateTrangThaiVe(ve.getMaVe(), 3)) {
            throw new Exception("Không thể cập nhật trạng thái vé!");
        }
        
        // 3. Cập nhật số ghế của chuyến tàu (giảm ghế đã đặt, tăng ghế còn trống)
        String maChuyenTau = ve.getChuyenTau().getMaChuyenTau();
        if (!chuyenTauDAO.capNhatSoGheKhiHuy(maChuyenTau)) {
            throw new Exception("Không thể cập nhật số ghế chuyến tàu!");
        }
        
        return true;
    }
    
    /**
     * Lấy thông tin hóa đơn trả để in
     * @param maHDT Mã hóa đơn trả
     * @return HoaDonTra
     */
    public HoaDonTra getHoaDonTraById(String maHDT) {
        return hoaDonTraDAO.getOne(maHDT);
    }
}