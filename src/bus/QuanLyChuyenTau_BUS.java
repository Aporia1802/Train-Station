package bus;

import dao.ChuyenTau_DAO;
import dao.Tau_DAO;
import dao.TuyenDuong_DAO;
import entity.ChuyenTau;
import entity.Tau;
import entity.TuyenDuong;
import java.util.ArrayList;

public class QuanLyChuyenTau_BUS {
    private ChuyenTau_DAO dao;

    public QuanLyChuyenTau_BUS() {
        this.dao = new ChuyenTau_DAO();
    }

    public ArrayList<ChuyenTau> getAllChuyenTau() {
        try {
            return dao.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public ChuyenTau getChuyenTauByMa(String maChuyenTau) {
        try {
            if (maChuyenTau == null || maChuyenTau.trim().isEmpty()) {
                return null;
            }
            return dao.getOne(maChuyenTau);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<ChuyenTau> getChuyenTauByKeyword(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return new ArrayList<>();
            }
            return dao.getChuyenTauByKeyword(keyword.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public ArrayList<ChuyenTau> locTheoGaDiGaDen(String maGaDi, String maGaDen) {
        try {
            ArrayList<ChuyenTau> all = getAllChuyenTau();
            ArrayList<ChuyenTau> result = new ArrayList<>();
            
            for (ChuyenTau ct : all) {
                if (ct.getTuyenDuong() == null) continue;
                
                boolean matchGaDi = maGaDi == null || 
                                   maGaDi.isEmpty() || 
                                   maGaDi.equals("Tất cả") ||
                                   (ct.getTuyenDuong().getGaDi() != null && 
                                    ct.getTuyenDuong().getGaDi().getMaGa().equals(maGaDi));
                
                boolean matchGaDen = maGaDen == null || 
                                    maGaDen.isEmpty() || 
                                    maGaDen.equals("Tất cả") ||
                                    (ct.getTuyenDuong().getGaDen() != null && 
                                     ct.getTuyenDuong().getGaDen().getMaGa().equals(maGaDen));
                
                if (matchGaDi && matchGaDen) {
                    result.add(ct);
                }
            }
            
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean createChuyenTau(ChuyenTau chuyenTau) {
        try {
            if (chuyenTau == null) {
                return false;
            }
            
            String error = validateChuyenTau(chuyenTau);
            if (error != null) {
                System.err.println("❌ " + error);
                return false;
            }
            
            return dao.create(chuyenTau);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateChuyenTau(String maChuyenTau, ChuyenTau chuyenTauMoi) {
        try {
            if (maChuyenTau == null || maChuyenTau.trim().isEmpty() || chuyenTauMoi == null) {
                return false;
            }
            
            String error = validateChuyenTau(chuyenTauMoi);
            if (error != null) {
                System.err.println("❌ " + error);
                return false;
            }
            
            return dao.update(maChuyenTau, chuyenTauMoi);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String validateChuyenTau(ChuyenTau chuyenTau) {
        if (chuyenTau == null) {
            return "Chuyến tàu không được null!";
        }
        
        if (chuyenTau.getMaChuyenTau() == null || chuyenTau.getMaChuyenTau().trim().isEmpty()) {
            return "Mã chuyến tàu không được rỗng!";
        }
        
        if (chuyenTau.getTuyenDuong() == null) {
            return "Tuyến đường không được null!";
        }
        
        if (chuyenTau.getTau() == null) {
            return "Tàu không được null!";
        }
        
        if (chuyenTau.getThoiGianDi() == null) {
            return "Thời gian đi không được null!";
        }
        
        if (chuyenTau.getThoiGianDen() == null) {
            return "Thời gian đến không được null!";
        }
        
        if (chuyenTau.getThoiGianDen().isBefore(chuyenTau.getThoiGianDi())) {
            return "Thời gian đến phải sau thời gian đi!";
        }
        
        return null;
    }

  
    public TuyenDuong getTuyenDuongByMa(String maTuyenDuong) {
        try {
            TuyenDuong_DAO tuyenDuongDAO = new TuyenDuong_DAO();
            return tuyenDuongDAO.getOne(maTuyenDuong);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Tau getTauByMa(String maTau) {
        try {
            Tau_DAO tauDAO = new Tau_DAO();
            return tauDAO.getOne(maTau);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lấy tất cả tàu
     */
    public ArrayList<Tau> getAllTau() {
        try {
            Tau_DAO tauDAO = new Tau_DAO();
            return tauDAO.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Lấy tất cả tuyến đường
     */
    public ArrayList<TuyenDuong> getAllTuyenDuong() {
        try {
            TuyenDuong_DAO tuyenDuongDAO = new TuyenDuong_DAO();
            return tuyenDuongDAO.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

  
    public String generateMaChuyenTau() {
        try {
            return dao.generateID();
        } catch (Exception e) {
            e.printStackTrace();
            return "CT-" + System.currentTimeMillis();
        }
    }
}