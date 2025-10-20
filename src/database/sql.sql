-- Tạo database
CREATE DATABASE QuanLyBanVeTau;
GO

USE QuanLyBanVeTau;
GO

-- 1. BẢNG NHÂN VIÊN
CREATE TABLE NhanVien (
    maNV VARCHAR(20) PRIMARY KEY,
    tenNV NVARCHAR(50) NOT NULL,
    gioiTinh BIT NOT NULL,
    ngaySinh DATE NOT NULL,
    email VARCHAR(50),
    soDienThoai VARCHAR(10) NOT NULL UNIQUE,
    cccd VARCHAR(12) NOT NULL,
    diaChi NVARCHAR(50) NOT NULL,
    chucVu NVARCHAR(20) NOT NULL,
    trangThai BIT NOT NULL,
);

-- 2. BẢNG TÀI KHOẢN
CREATE TABLE TaiKhoan (
    tenDangNhap VARCHAR(10) PRIMARY KEY,
    matKhau NVARCHAR(50) NOT NULL,
    maNV VARCHAR(20) NOT NULL,
    -- Ràng buộc
    CONSTRAINT FK_TK_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
);

-- 3. BẢNG KHÁCH HÀNG
CREATE TABLE KhachHang (
    maKH VARCHAR(20) PRIMARY KEY,
    tenKH NVARCHAR(50) NOT NULL,
    soDienThoai VARCHAR(10) NOT NULL,
    cccd VARCHAR(12) NOT NULL UNIQUE,
    ngaySinh DATE NOT NULL,
    gioiTinh BIT NOT NULL
);

-- 4. BẢNG GA TÀU
CREATE TABLE GaTau (
    maGa VARCHAR(20) PRIMARY KEY,
    tenGa NVARCHAR(50) NOT NULL,
    diaChi NVARCHAR(50) NOT NULL,
    soDienThoai VARCHAR(10) NOT NULL,
);

-- 5. BẢNG TÀU
CREATE TABLE Tau (
    maTau VARCHAR(20) PRIMARY KEY,
	tenTau NVARCHAR(50),
    soToaTau INT NOT NULL,
    sucChua INT NOT NULL,
    ngayHoatDong DATE NOT NULL,
    trangThai VARCHAR(20) NOT NULL,
);

-- 6. BẢNG TOA TÀU
CREATE TABLE ToaTau (
    maToaTau VARCHAR(20) PRIMARY KEY,
    soKhoangTau INT NOT NULL,
    soHieuToa INT NOT NULL,
    soCho INT NOT NULL,
    maTau VARCHAR(20) NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_TT_Tau FOREIGN KEY (maTau) REFERENCES Tau(maTau),
);


-- 7. BẢNG KHOANG TÀU
CREATE TABLE KhoangTau (
    maKhoangTau VARCHAR(20) PRIMARY KEY,
    soHieuKhoang INT NOT NULL,
    soChua INT NOT NULL,
    maToaTau VARCHAR(20) NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_KT_ToaTau FOREIGN KEY (maToaTau) REFERENCES ToaTau(maToaTau),
);

-- 8. BẢNG LOẠI GHẾ
CREATE TABLE LoaiGhe (
    maLoaiGhe VARCHAR(20) PRIMARY KEY,
    tenLoaiGhe NVARCHAR(50) NOT NULL,
    moTa NVARCHAR(100),
    heSoGhe FLOAT NOT NULL,
);

-- 9. BẢNG GHẾ
CREATE TABLE Ghe (
    maGhe VARCHAR(20) PRIMARY KEY,
    soGhe INT NOT NULL,
    trangThaiGhe NVARCHAR(50) NOT NULL,
    maLoaiGhe VARCHAR(20) NOT NULL,
    maKhoangTau VARCHAR(20) NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_G_LoaiGhe FOREIGN KEY (maLoaiGhe) REFERENCES LoaiGhe(maLoaiGhe),
    CONSTRAINT FK_G_KhoangTau FOREIGN KEY (maKhoangTau) REFERENCES KhoangTau(maKhoangTau),
);

-- 10. BẢNG TUYẾN ĐƯỜNG
CREATE TABLE TuyenDuong (
    maTuyenDuong VARCHAR(20) PRIMARY KEY,
    quangDuong float NOT NULL,
    soTienMotKm float NOT NULL,
    gaDi VARCHAR(20) NOT NULL,
    gaDen VARCHAR(20) NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_TGDC_GaDi FOREIGN KEY (gaDi) REFERENCES GaTau(maGa),
    CONSTRAINT FK_TGDC_GaDen FOREIGN KEY (gaDen) REFERENCES GaTau(maGa),
);


-- 11. BẢNG CHUYẾN TÀU
CREATE TABLE ChuyenTau (
    maChuyenTau VARCHAR(20) PRIMARY KEY,
    thoiGianDi DATETIME NOT NULL,
    thoiGianDen DATETIME NOT NULL,
    soGheDaDat INT NOT NULL DEFAULT 0,
    soGheConTrong INT NOT NULL,
    maTau VARCHAR(20) NOT NULL,
    maTuyenDuong VARCHAR(20) NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_CD_ThoiGianDiChuyen FOREIGN KEY (maTuyenDuong) REFERENCES TuyenDuong(maTuyenDuong),
    CONSTRAINT FK_CD_Tau FOREIGN KEY (maTau) REFERENCES Tau(maTau),
);

-- 12. BẢNG LOẠI VÉ
CREATE TABLE LoaiVe (
    maLoaiVe VARCHAR(20) PRIMARY KEY,
    tenLoaiVe NVARCHAR(50) NOT NULL,
    moTa NVARCHAR(1000),
    heSoLoaiVe FLOAT NOT NULL,
);


-- 13. BẢNG HÀNH KHÁCH
CREATE TABLE HanhKhach (
    maHanhKhach VARCHAR(20) PRIMARY KEY,
    tenHanhKhach NVARCHAR(50) NOT NULL,
    cccd VARCHAR(12) NOT NULL,
    ngaySinh INT NOT NULL,
);

-- 14. BẢNG KHUYẾN MÃI
CREATE TABLE KhuyenMai (
    maKhuyenMai VARCHAR(20) PRIMARY KEY,
    tenKhuyenMai NVARCHAR(50) NOT NULL,
    heSoKhuyenMai FLOAT NOT NULL,
    ngayBatDau DATETIME NOT NULL,
    ngayKetThuc DATETIME NOT NULL,
    tongTienToiThieu FLOAT,
    tienKhuyenMaiToiDa FLOAT,
    trangThai BIT NOT NULL,
);

-- 15. BẢNG HÓA ĐƠN
CREATE TABLE HoaDon (
    maHoaDon VARCHAR(20) PRIMARY KEY,
    maNhanVien VARCHAR(20) NOT NULL,
    maKhachHang VARCHAR(20) NOT NULL,
    ngayLapHoaDon DATETIME NOT NULL DEFAULT GETDATE(),
    VAT FLOAT NOT NULL DEFAULT 0.1,
    maKhuyenMai VARCHAR(20),
    tongTien FLOAT NOT NULL,
    thanhTien FLOAT NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_HD_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNV),
    CONSTRAINT FK_HD_KhachHang FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKH),
    CONSTRAINT FK_HD_KhuyenMai FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai(maKhuyenMai),
);

-- 16. BẢNG VÉ
CREATE TABLE Ve (
    maVe VARCHAR(20) PRIMARY KEY,
    maLoaiVe VARCHAR(20) NOT NULL,
    maChuyenTau VARCHAR(20) NOT NULL,
    maHanhKhach VARCHAR(20) NOT NULL,
    maGhe VARCHAR(20) NOT NULL,
    maHoaDon VARCHAR(20) NOT NULL,
    trangThai NVARCHAR(20) NOT NULL,
    giaVe MONEY NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_V_LoaiVe FOREIGN KEY (maLoaiVe) REFERENCES LoaiVe(maLoaiVe),
    CONSTRAINT FK_V_ChuyenTau FOREIGN KEY (maChuyenTau) REFERENCES ChuyenTau(maChuyenTau),
    CONSTRAINT FK_V_HanhKhach FOREIGN KEY (maHanhKhach) REFERENCES HanhKhach(maHanhKhach),
    CONSTRAINT FK_V_Ghe FOREIGN KEY (maGhe) REFERENCES Ghe(maGhe),
    CONSTRAINT FK_V_HoaDon FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon),
);



-- THÊM DỮ LIỆU MẪU

-- 1. NHÂN VIÊN
INSERT INTO NhanVien (maNV, tenNV, gioiTinh, ngaySinh, email, soDienThoai, cccd, diaChi, chucVu, trangThai) VALUES
('NV-0-95-123-001', N'Nguyễn Văn An', 0, '1995-05-15', 'conghoangho1802@gmail.com', '0901234567', '079095123456', 'abc 123', N'Nhân viên bán vé', 1),
('NV-1-98-234-002', N'Trần Thị Bình', 1, '1998-08-20', 'conghoangho1802@gmail.com', '0912345678', '079098234567', 'abc 123', N'Nhân viên quản lý', 1)

-- 2. TÀI KHOẢN
INSERT INTO TaiKhoan (tenDangNhap, matKhau, maNV) VALUES
('0901234567', N'3c86e8c03745cf7fc0e3a327d6660a8c', 'NV-0-95-123-001'), --RypLmY9B
('0912345678', N'c0a55f024dd3b67a4d44210d66e7a0ea', 'NV-1-98-234-002') --6uZUqeMV


-- 3. KHÁCH HÀNG
INSERT INTO KhachHang (maKH, tenKH, soDienThoai, cccd, ngaySinh, gioiTinh) VALUES
('KH-001', N'Nguyễn Minh Tuấn', '0901111111', '079095001001', '1995-01-15', 0),
('KH-002', N'Trần Thị Hoa', '0902222222', '079096002002', '1996-03-20', 1),
('KH-003', N'Lê Văn Bình', '0903333333', '079097003003', '1997-05-25', 0),
('KH-004', N'Phạm Thị Chi', '0904444444', '079098004004', '1998-07-30', 1),
('KH-005', N'Hoàng Văn Đức', '0905555555', '079099005005', '1999-09-10', 0),
('KH-006', N'Vũ Thị Lan', '0906666666', '079000006006', '2000-11-15', 1),
('KH-007', N'Đặng Văn Minh', '0907777777', '079001007007', '2001-02-20', 0),
('KH-008', N'Bùi Thị Nga', '0908888888', '079002008008', '2002-04-25', 1);

-- 4. GA TÀU
INSERT INTO GaTau (maGa, tenGa, diaChi, soDienThoai) VALUES
('GA-SG', N'Ga Sài Gòn', N'1 Nguyễn Thông, Q3, TP.HCM', '0283822625'),
('GA-HN', N'Ga Hà Nội', N'120 Lê Duẩn, Hoàn Kiếm, Hà Nội', '0243942594'),
('GA-DN', N'Ga Đà Nẵng', N'791 Hai Phòng, Hải Châu, Đà Nẵng', '0236382381'),
('GA-NT', N'Ga Nha Trang', N'17 Thái Nguyên, Nha Trang, Khánh Hòa', '0258352211'),
('GA-HP', N'Ga Hải Phòng', N'75 Lương Khánh Thiện, Ngô Quyền, Hải Phòng', '0312522192'),
('GA-HUE', N'Ga Huế', N'2 Bùi Thị Xuân, Huế', '0234382217'),
('GA-QN', N'Ga Quảng Ngãi', N'56 Quang Trung, Quảng Ngãi', '0553826661'),
('GA-VT', N'Ga Vũng Tàu', N'1 Nam Kỳ Khởi Nghĩa, Vũng Tàu', '0643856527');

-- 5. TÀU
INSERT INTO Tau (maTau, soToaTau, sucChua, ngayHoatDong, trangThai) VALUES
('SE1', 12, 600, '2020-01-01', N'Hoạt động'),
('SE2', 12, 600, '2020-01-01', N'Hoạt động'),
('SE3', 10, 500, '2020-06-01', N'Hoạt động'),
('SE4', 10, 500, '2020-06-01', N'Hoạt động'),
('SE5', 8, 400, '2021-01-01', N'Hoạt động'),
('SE6', 8, 400, '2021-01-01', N'Hoạt động'),
('SE7', 12, 600, '2021-06-01', N'Bảo trì'),
('SE8', 10, 500, '2022-01-01', N'Hoạt động');

-- 6. TOA TÀU (ví dụ cho tàu SE1)
INSERT INTO ToaTau (maToaTau, soKhoangTau, soHieuToa, soCho, maTau) VALUES
('TT-SE1-01', 8, 1, 64, 'SE1'),
('TT-SE1-02', 8, 2, 64, 'SE1'),
('TT-SE1-03', 8, 3, 64, 'SE1'),
('TT-SE1-04', 6, 4, 48, 'SE1'),
('TT-SE1-05', 6, 5, 48, 'SE1'),
('TT-SE2-01', 8, 1, 64, 'SE2'),
('TT-SE2-02', 8, 2, 64, 'SE2'),
('TT-SE3-01', 8, 1, 64, 'SE3');

-- 7. KHOANG TÀU (ví dụ cho toa TT-SE1-01)
INSERT INTO KhoangTau (maKhoangTau, soHieuKhoang, soChua, maToaTau) VALUES
('KT-SE1-01-1', 1, 4, 'TT-SE1-01'),
('KT-SE1-01-2', 2, 4, 'TT-SE1-01'),
('KT-SE1-01-3', 3, 6, 'TT-SE1-01'),
('KT-SE1-01-4', 4, 6, 'TT-SE1-01'),
('KT-SE1-02-1', 1, 4, 'TT-SE1-02'),
('KT-SE1-02-2', 2, 4, 'TT-SE1-02'),
('KT-SE2-01-1', 1, 4, 'TT-SE2-01'),
('KT-SE2-01-2', 2, 4, 'TT-SE2-01');

-- 8. LOẠI GHẾ
INSERT INTO LoaiGhe (maLoaiGhe, tenLoaiGhe, moTa, heSoGhe) VALUES
('LG-NM', N'Ngồi mềm', N'Ghế ngồi mềm có điều hòa', 1.0),
('LG-GN4', N'Giường nằm 4 chỗ', N'Giường nằm 4 chỗ/khoang, có điều hòa', 1.5),
('LG-GN6', N'Giường nằm 6 chỗ', N'Giường nằm 6 chỗ/khoang, có điều hòa', 1.2);

-- 9. GHẾ (ví dụ cho khoang KT-SE1-01-1)
INSERT INTO Ghe (maGhe, soGhe, trangThaiGhe, maLoaiGhe, maKhoangTau) VALUES
('G-SE1-01-1-01', 1, N'Trống', 'LG-GN4', 'KT-SE1-01-1'),
('G-SE1-01-1-02', 2, N'Trống', 'LG-GN4', 'KT-SE1-01-1'),
('G-SE1-01-1-03', 3, N'Trống', 'LG-GN4', 'KT-SE1-01-1'),
('G-SE1-01-1-04', 4, N'Trống', 'LG-GN4', 'KT-SE1-01-1'),
('G-SE1-01-2-01', 1, N'Trống', 'LG-GN4', 'KT-SE1-01-2'),
('G-SE1-01-2-02', 2, N'Trống', 'LG-GN4', 'KT-SE1-01-2'),
('G-SE1-01-2-03', 3, N'Trống', 'LG-GN4', 'KT-SE1-01-2'),
('G-SE1-01-2-04', 4, N'Trống', 'LG-GN4', 'KT-SE1-01-2'),
('G-SE1-01-3-01', 1, N'Trống', 'LG-GN6', 'KT-SE1-01-3'),
('G-SE1-01-3-02', 2, N'Trống', 'LG-GN6', 'KT-SE1-01-3'),
('G-SE1-01-3-03', 3, N'Trống', 'LG-GN6', 'KT-SE1-01-3'),
('G-SE1-01-3-04', 4, N'Trống', 'LG-GN6', 'KT-SE1-01-3'),
('G-SE1-01-3-05', 5, N'Trống', 'LG-GN6', 'KT-SE1-01-3'),
('G-SE1-01-3-06', 6, N'Trống', 'LG-GN6', 'KT-SE1-01-3');

-- 10. TUYẾN ĐƯỜNG
INSERT INTO TuyenDuong (maTuyenDuong, quangDuong, soTienMotKm, gaDi, gaDen) VALUES
('TD-SG-HN', 1726, 15, 'GA-SG', 'GA-HN'),
('TD-HN-SG', 1726, 15, 'GA-HN', 'GA-SG'),
('TD-SG-DN', 964, 15, 'GA-SG', 'GA-DN'),
('TD-DN-SG', 964, 15, 'GA-DN', 'GA-SG'),
('TD-SG-NT', 411, 15, 'GA-SG', 'GA-NT'),
('TD-NT-SG', 411, 15, 'GA-NT', 'GA-SG'),
('TD-HN-HP', 102, 15, 'GA-HN', 'GA-HP'),
('TD-HP-HN', 102, 15, 'GA-HP', 'GA-HN'),
('TD-DN-HUE', 105, 15, 'GA-DN', 'GA-HUE'),
('TD-HUE-DN', 105, 15, 'GA-HUE', 'GA-DN');

-- 11. CHUYẾN TÀU
INSERT INTO ChuyenTau (maChuyenTau, thoiGianDi, thoiGianDen, soGheDaDat, soGheConTrong, maTau, maTuyenDuong) VALUES
('CT-SE1-241020', '2024-10-20 19:00:00', '2024-10-21 14:00:00', 150, 450, 'SE1', 'TD-SG-HN'),
('CT-SE2-241020', '2024-10-20 19:30:00', '2024-10-21 14:30:00', 180, 420, 'SE2', 'TD-HN-SG'),
('CT-SE3-241021', '2024-10-21 06:00:00', '2024-10-21 19:30:00', 200, 300, 'SE3', 'TD-SG-DN'),
('CT-SE4-241021', '2024-10-21 22:00:00', '2024-10-22 07:30:00', 120, 380, 'SE4', 'TD-SG-NT'),
('CT-SE5-241022', '2024-10-22 08:00:00', '2024-10-22 19:00:00', 90, 310, 'SE5', 'TD-DN-SG'),
('CT-SE6-241022', '2024-10-22 14:00:00', '2024-10-22 16:30:00', 50, 350, 'SE6', 'TD-HN-HP'),
('CT-SE1-241023', '2024-10-23 19:00:00', '2024-10-24 14:00:00', 100, 500, 'SE1', 'TD-SG-HN'),
('CT-SE2-241023', '2024-10-23 20:00:00', '2024-10-24 15:00:00', 130, 470, 'SE2', 'TD-HN-SG');

-- 12. LOẠI VÉ
INSERT INTO LoaiVe (maLoaiVe, tenLoaiVe, moTa, heSoLoaiVe) VALUES
('LV-NL', N'Người lớn', N'Vé dành cho người lớn (từ 10 tuổi trở lên)', 1.0),
('LV-TE', N'Trẻ em', N'Vé dành cho trẻ em (từ 6-9 tuổi), giảm 25%', 0.75),
('LV-HSSV', N'Học sinh/Sinh viên', N'Vé dành cho học sinh/sinh viên có thẻ, giảm 10%', 0.9),
('LV-NC', N'Người cao tuổi', N'Vé dành cho người cao tuổi (từ 60 tuổi trở lên), giảm 15%', 0.85);

-- 13. HÀNH KHÁCH
INSERT INTO HanhKhach (maHanhKhach, tenHanhKhach, cccd, ngaySinh) VALUES
('HK-001', N'Nguyễn Minh Tuấn', '079095001001', 1995),
('HK-002', N'Trần Thị Hoa', '079096002002', 1996),
('HK-003', N'Lê Văn Bình', '079097003003', 1997),
('HK-004', N'Phạm Thị Chi', '079098004004', 1998),
('HK-005', N'Hoàng Văn Đức', '079099005005', 1999),
('HK-006', N'Vũ Thị Lan', '079000006006', 2000),
('HK-007', N'Nguyễn Văn Giang', '079001007007', 2015),
('HK-008', N'Trần Thị Hạnh', '079002008008', 1960);

-- 14. KHUYẾN MÃI
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, heSoKhuyenMai, ngayBatDau, ngayKetThuc, tongTienToiThieu, tienKhuyenMaiToiDa, trangThai) VALUES
('KM-KHAIGIANG', N'Khuyến mãi khai giảng', 0.15, '2024-09-01', '2024-09-30', 500000, 200000, 0),
('KM-TETDL', N'Khuyến mãi Tết Dương lịch', 0.20, '2024-12-25', '2025-01-05', 1000000, 500000, 1),
('KM-30THANG4', N'Giảm giá 30/4 - 1/5', 0.10, '2025-04-28', '2025-05-02', 300000, 150000, 1),
('KM-QUOCTE', N'Ngày Quốc tế Thiếu nhi', 0.25, '2025-05-28', '2025-06-05', 200000, 300000, 1),
('KM-THANG10', N'Khuyến mãi tháng 10', 0.12, '2024-10-01', '2024-10-31', 400000, 200000, 1);

-- 15. HÓA ĐƠN
INSERT INTO HoaDon (maHoaDon, maNhanVien, maKhachHang, ngayLapHoaDon, VAT, maKhuyenMai, tongTien, thanhTien) VALUES
('HD-001', 'NV-0-95-123-001', 'KH-001', '2024-10-15 10:30:00', 0.1, 'KM-THANG10', 1200000, 1158000),
('HD-002', 'NV-0-95-123-001', 'KH-002', '2024-10-16 14:20:00', 0.1, NULL, 800000, 880000),
('HD-003', 'NV-0-95-123-001', 'KH-003', '2024-10-17 09:15:00', 0.1, 'KM-THANG10', 1800000, 1901600),
('HD-004', 'NV-0-95-123-001', 'KH-004', '2024-10-18 16:45:00', 0.1, NULL, 600000, 660000),
('HD-005', 'NV-0-95-123-001', 'KH-005', '2024-10-19 11:00:00', 0.1, 'KM-THANG10', 2400000, 2467200);

-- 16. VÉ
INSERT INTO Ve (maVe, maLoaiVe, maChuyenTau, maHanhKhach, maGhe, maHoaDon, trangThai, giaVe) VALUES
('V-001', 'LV-NL', 'CT-SE1-241020', 'HK-001', 'G-SE1-01-1-01', 'HD-001', N'Đã thanh toán', 600000),
('V-002', 'LV-NL', 'CT-SE1-241020', 'HK-002', 'G-SE1-01-1-02', 'HD-001', N'Đã thanh toán', 600000),
('V-003', 'LV-HSSV', 'CT-SE2-241020', 'HK-003', 'G-SE1-01-2-01', 'HD-002', N'Đã thanh toán', 800000),
('V-004', 'LV-NL', 'CT-SE3-241021', 'HK-004', 'G-SE1-01-2-02', 'HD-003', N'Đã thanh toán', 600000),
('V-005', 'LV-NL', 'CT-SE3-241021', 'HK-005', 'G-SE1-01-2-03', 'HD-003', N'Đã thanh toán', 600000),
('V-006', 'LV-NL', 'CT-SE3-241021', 'HK-006', 'G-SE1-01-2-04', 'HD-003', N'Đã thanh toán', 600000),
('V-007', 'LV-TE', 'CT-SE4-241021', 'HK-007', 'G-SE1-01-3-01', 'HD-004', N'Đã thanh toán', 600000),
('V-008', 'LV-NL', 'CT-SE5-241022', 'HK-001', 'G-SE1-01-3-02', 'HD-005', N'Đã thanh toán', 600000),
('V-009', 'LV-NL', 'CT-SE5-241022', 'HK-002', 'G-SE1-01-3-03', 'HD-005', N'Đã thanh toán', 600000),
('V-010', 'LV-NL', 'CT-SE5-241022', 'HK-003', 'G-SE1-01-3-04', 'HD-005', N'Đã thanh toán', 600000),
('V-011', 'LV-NC', 'CT-SE5-241022', 'HK-008', 'G-SE1-01-3-05', 'HD-005', N'Đã thanh toán', 600000);
