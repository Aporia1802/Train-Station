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
    trangThai BIT NOT NULL
);

-- 2. BẢNG TÀI KHOẢN
CREATE TABLE TaiKhoan (
    tenDangNhap VARCHAR(10) PRIMARY KEY,
    matKhau NVARCHAR(50) NOT NULL,
    maNV VARCHAR(20) NOT NULL,
    -- Ràng buộc
    CONSTRAINT FK_TK_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
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
    soDienThoai VARCHAR(10) NOT NULL
);

-- 5. BẢNG TÀU
CREATE TABLE Tau (
    maTau VARCHAR(20) PRIMARY KEY,
    tenTau NVARCHAR(50) NOT NULL,
    soToaTau INT NOT NULL,
    sucChua INT NOT NULL,
    ngayHoatDong DATE NOT NULL,
    trangThai INT NOT NULL
);

-- 6. BẢNG TOA TÀU
CREATE TABLE ToaTau (
    maToaTau VARCHAR(20) PRIMARY KEY,
    soKhoangTau INT NOT NULL,
    soHieuToa INT NOT NULL,
    soCho INT NOT NULL,
    maTau VARCHAR(20) NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_TT_Tau FOREIGN KEY (maTau) REFERENCES Tau(maTau)
);


-- 7. BẢNG KHOANG TÀU
CREATE TABLE KhoangTau (
    maKhoangTau VARCHAR(20) PRIMARY KEY,
    soHieuKhoang INT NOT NULL,
    soChua INT NOT NULL,
    maToaTau VARCHAR(20) NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_KT_ToaTau FOREIGN KEY (maToaTau) REFERENCES ToaTau(maToaTau)
);

-- 8. BẢNG LOẠI GHẾ
CREATE TABLE LoaiGhe (
    maLoaiGhe VARCHAR(20) PRIMARY KEY,
    tenLoaiGhe NVARCHAR(50) NOT NULL,
    moTa NVARCHAR(100),
    heSoGhe FLOAT NOT NULL
);

-- 9. BẢNG GHẾ
CREATE TABLE Ghe (
    maGhe VARCHAR(20) PRIMARY KEY,
    soGhe INT NOT NULL,
    trangThaiGhe INT NOT NULL,
    maLoaiGhe VARCHAR(20) NOT NULL,
    maKhoangTau VARCHAR(20) NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_G_LoaiGhe FOREIGN KEY (maLoaiGhe) REFERENCES LoaiGhe(maLoaiGhe),
    CONSTRAINT FK_G_KhoangTau FOREIGN KEY (maKhoangTau) REFERENCES KhoangTau(maKhoangTau)
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
    CONSTRAINT FK_TGDC_GaDen FOREIGN KEY (gaDen) REFERENCES GaTau(maGa)
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
    CONSTRAINT FK_CD_Tau FOREIGN KEY (maTau) REFERENCES Tau(maTau)
);

-- 12. BẢNG LOẠI VÉ
CREATE TABLE LoaiVe (
    maLoaiVe VARCHAR(20) PRIMARY KEY,
    tenLoaiVe NVARCHAR(50) NOT NULL,
    moTa NVARCHAR(1000),
    heSoLoaiVe FLOAT NOT NULL
);


-- 13. BẢNG HÀNH KHÁCH
CREATE TABLE HanhKhach (
    maHanhKhach VARCHAR(20) PRIMARY KEY,
    tenHanhKhach NVARCHAR(50) NOT NULL,
    cccd VARCHAR(12) NOT NULL,
    ngaySinh DATE NOT NULL
);

-- 14. BẢNG KHUYẾN MÃI
CREATE TABLE KhuyenMai (
    maKhuyenMai VARCHAR(20) PRIMARY KEY,
    tenKhuyenMai NVARCHAR(50) NOT NULL,
    heSoKhuyenMai FLOAT NOT NULL,
    ngayBatDau DATE NOT NULL,
    ngayKetThuc DATE NOT NULL,
    tongTienToiThieu FLOAT,
    tienKhuyenMaiToiDa FLOAT,
    trangThai BIT NOT NULL
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
    CONSTRAINT FK_HD_KhuyenMai FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai(maKhuyenMai)
);

-- 16. BẢNG VÉ
CREATE TABLE Ve (
    maVe VARCHAR(20) PRIMARY KEY,
    maLoaiVe VARCHAR(20) NOT NULL,
    maChuyenTau VARCHAR(20) NOT NULL,
    maHanhKhach VARCHAR(20) NOT NULL,
    maGhe VARCHAR(20) NOT NULL,
    maHoaDon VARCHAR(20) NOT NULL,
    trangThai INT NOT NULL,
    giaVe MONEY NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_V_LoaiVe FOREIGN KEY (maLoaiVe) REFERENCES LoaiVe(maLoaiVe),
    CONSTRAINT FK_V_ChuyenTau FOREIGN KEY (maChuyenTau) REFERENCES ChuyenTau(maChuyenTau),
    CONSTRAINT FK_V_HanhKhach FOREIGN KEY (maHanhKhach) REFERENCES HanhKhach(maHanhKhach),
    CONSTRAINT FK_V_Ghe FOREIGN KEY (maGhe) REFERENCES Ghe(maGhe),
    CONSTRAINT FK_V_HoaDon FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon)
);

-- 17. BẢNG HÓA ĐƠN TRẢ
CREATE TABLE HoaDonTra (
    maHDT VARCHAR(20) PRIMARY KEY,
    ngayTra DATETIME NOT NULL,
    maVe VARCHAR(20) NOT NULL,
    maNV VARCHAR(20) NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_HDT_Ve FOREIGN KEY (maVe) REFERENCES Ve(maVe),
    CONSTRAINT FK_HDT_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);



-- THÊM DỮ LIỆU MẪU

-- 1. NHÂN VIÊN
INSERT INTO NhanVien (maNV, tenNV, gioiTinh, ngaySinh, email, soDienThoai, cccd, diaChi, chucVu, trangThai) VALUES
('NV001', N'Nguyễn Văn An', 0, '1995-05-15', 'nguyenvanan1505@gmail.com', '0901234567', '079095123456', N'79 Bờ Bao Tân Thắng', N'Nhân viên bán vé', 1),
('NV002', N'Trần Thị Bình', 1, '1998-08-20', 'tranthibinh2008@gmai.com', '0912345678', '079098234567', N'20 Nguyễn Văn Trỗi', N'Nhân viên quản lý', 1),
('NV003', N'Hồ Công Hoàng', 1, '2005-02-18', 'conghoangho1802@gmail.com', '0349573425', '079098234568', N'36 Nguyễn Văn Bảo', N'Nhân viên quản lý', 1),
('NV004', N'Tôn Thị Sen', 1, '2000-10-30', 'conghoangho1802@gmail.com', '0938635712', '079098234569', N'12 Nguyễn Trãi', N'Nhân viên bán vé', 0);

-- 2. TÀI KHOẢN
INSERT INTO TaiKhoan (tenDangNhap, matKhau, maNV) VALUES
('0901234567', N'3c86e8c03745cf7fc0e3a327d6660a8c', 'NV001'), --RypLmY9B
('0912345678', N'c0a55f024dd3b67a4d44210d66e7a0ea', 'NV002'), --6uZUqeMV
('0349573425', N'c0a55f024dd3b67a4d44210d66e7a0ea', 'NV003'), --6uZUqeMV
('0938635712', N'3c86e8c03745cf7fc0e3a327d6660a8c', 'NV004'); --6uZUqeMV


-- 3. KHÁCH HÀNG
INSERT INTO KhachHang (maKH, tenKH, soDienThoai, cccd, ngaySinh, gioiTinh) VALUES
('KH001', N'Nguyễn Minh Tuấn', '0901111111', '079095001001', '1995-01-15', 0),
('KH002', N'Trần Thị Hoa', '0902222222', '079096002002', '1996-03-20', 1),
('KH003', N'Lê Văn Bình', '0903333333', '079097003003', '1997-05-25', 0),
('KH004', N'Phạm Thị Chi', '0904444444', '079098004004', '1998-07-30', 1),
('KH005', N'Hoàng Văn Đức', '0905555555', '079099005005', '1999-09-10', 0),
('KH006', N'Vũ Thị Lan', '0906666666', '079000006006', '2000-11-15', 1),
('KH007', N'Đặng Văn Minh', '0907777777', '079001007007', '2001-02-20', 0),
('KH008', N'Bùi Thị Nga', '0908888888', '079002008008', '2002-04-25', 1);

-- 4. GA TÀU
INSERT INTO GaTau (maGa, tenGa, diaChi, soDienThoai) VALUES
('GA001', N'Sài Gòn', N'1 Nguyễn Thông, Q3, TP.HCM', '0283822625'),
('GA002', N'Hà Nội', N'120 Lê Duẩn, Hoàn Kiếm, Hà Nội', '0243942594'),
('GA003', N'Đà Nẵng', N'791 Hai Phòng, Hải Châu, Đà Nẵng', '0236382381'),
('GA004', N'Nha Trang', N'17 Thái Nguyên, Nha Trang, Khánh Hòa', '0258352211'),
('GA005', N'Hải Phòng', N'75 Lương Khánh Thiện, Ngô Quyền, Hải Phòng', '0312522192'),
('GA006', N'Huế', N'2 Bùi Thị Xuân, Huế', '0234382217'),
('GA007', N'Quảng Ngãi', N'56 Quang Trung, Quảng Ngãi', '0553826661'),
('GA008', N'Vũng Tàu', N'1 Nam Kỳ Khởi Nghĩa, Vũng Tàu', '0643856527');

-- 5. TÀU
INSERT INTO Tau (maTau, tenTau, soToaTau, sucChua, ngayHoatDong, trangThai) VALUES
('SE1', N'Thống Nhất', 12, 600, '2020-01-01', 1),
('SE2', N'Thống Nhất', 12, 600, '2020-01-01', 1),
('SE4', N'Thống Nhất', 10, 500, '2020-06-01', 1),
('SE3', N'Thống Nhất', 10, 500, '2020-06-01', 1),
('SE5', N'Thống Nhất', 8, 400, '2021-01-01', 1),
('SE6', N'Thống Nhất', 8, 400, '2021-01-01', 1),
('SE7', N'Thống Nhất', 12, 600, '2021-06-01', 2),
('SE8', N'Thống Nhất', 10, 500, '2022-01-01', 1);

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
('LG-GN4', N'Giường nằm 4 chỗ', N'Giường nằm 4 chỗ/khoang, có điều hòa', 1.4),
('LG-GN6', N'Giường nằm 6 chỗ', N'Giường nằm 6 chỗ/khoang, có điều hòa', 1.2);

-- 9. GHẾ (ví dụ cho khoang KT-SE1-01-1)
INSERT INTO Ghe (maGhe, soGhe, trangThaiGhe, maLoaiGhe, maKhoangTau) VALUES
('G-SE1-01-1-01', 1, 1, 'LG-GN4', 'KT-SE1-01-1'),
('G-SE1-01-1-02', 2, 1, 'LG-GN4', 'KT-SE1-01-1'),
('G-SE1-01-1-03', 3, 1, 'LG-GN4', 'KT-SE1-01-1'),
('G-SE1-01-1-04', 4, 1, 'LG-GN4', 'KT-SE1-01-1'),
('G-SE1-01-2-01', 1, 1, 'LG-GN4', 'KT-SE1-01-2'),
('G-SE1-01-2-02', 2, 1, 'LG-GN4', 'KT-SE1-01-2'),
('G-SE1-01-2-03', 3, 1, 'LG-GN4', 'KT-SE1-01-2'),
('G-SE1-01-2-04', 4, 1, 'LG-GN4', 'KT-SE1-01-2'),
('G-SE1-01-3-01', 1, 1, 'LG-GN6', 'KT-SE1-01-3'),
('G-SE1-01-3-02', 2, 1, 'LG-GN6', 'KT-SE1-01-3'),
('G-SE1-01-3-03', 3, 1, 'LG-GN6', 'KT-SE1-01-3'),
('G-SE1-01-3-04', 4, 1, 'LG-GN6', 'KT-SE1-01-3'),
('G-SE1-01-3-05', 5, 1, 'LG-GN6', 'KT-SE1-01-3'),
('G-SE1-01-3-06', 6, 1, 'LG-GN6', 'KT-SE1-01-3');

-- 10. TUYẾN ĐƯỜNG
INSERT INTO TuyenDuong (maTuyenDuong, quangDuong, soTienMotKm, gaDi, gaDen) VALUES
('TD-SG-HN', 1726, 700, 'GA001', 'GA002'),
('TD-HN-SG', 1726, 700, 'GA002', 'GA001');

-- 11. CHUYẾN TÀU
INSERT INTO ChuyenTau (maChuyenTau, thoiGianDi, thoiGianDen, soGheDaDat, soGheConTrong, maTau, maTuyenDuong) VALUES
('CT-SE1-241020', '2025-10-30 19:00:00', '2024-10-21 14:00:00', 150, 450, 'SE1', 'TD-SG-HN'),
('CT-SE2-241020', '2024-10-20 19:30:00', '2024-10-21 14:30:00', 180, 420, 'SE2', 'TD-HN-SG');

-- 12. LOẠI VÉ
INSERT INTO LoaiVe (maLoaiVe, tenLoaiVe, moTa, heSoLoaiVe) VALUES
('LV-NL', N'Người lớn', N'Vé dành cho người lớn (từ 10 tuổi trở lên)', 1.0),
('LV-TE', N'Trẻ em', N'Vé dành cho trẻ em (từ 6-9 tuổi), giảm 25%', 0.75),
('LV-HSSV', N'Học sinh/Sinh viên', N'Vé dành cho học sinh/sinh viên có thẻ, giảm 10%', 0.9),
('LV-NC', N'Người cao tuổi', N'Vé dành cho người cao tuổi (từ 60 tuổi trở lên), giảm 15%', 0.85);

-- 13. HÀNH KHÁCH
INSERT INTO HanhKhach (maHanhKhach, tenHanhKhach, cccd, ngaySinh) VALUES
('HK001', N'Nguyễn Minh Tuấn', '079095001001', '2005-02-18'),
('HK002', N'Trần Thị Hoa', '079096002002', '2005-02-18'),
('HK003', N'Lê Văn Bình', '079097003003', '2005-02-18'),
('HK004', N'Phạm Thị Chi', '079098004004', '2005-02-18'),
('HK005', N'Hoàng Văn Đức', '079099005005', '2005-02-18'),
('HK006', N'Vũ Thị Lan', '079000006006', '2005-02-18'),
('HK007', N'Nguyễn Văn Giang', '079001007007', '2005-02-18'),
('HK008', N'Trần Thị Hạnh', '079002008008', '2005-02-18');

-- 14. KHUYẾN MÃI
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, heSoKhuyenMai, ngayBatDau, ngayKetThuc, tongTienToiThieu, tienKhuyenMaiToiDa, trangThai) VALUES
('KM001', N'Khuyến mãi khai giảng', 0.15, '2024-09-01', '2024-09-30', 500000, 200000, 0),
('KM002', N'Khuyến mãi Tết Dương lịch', 0.2, '2024-12-25', '2025-01-05', 1000000, 500000, 0),
('KM003', N'Giảm giá 30/4 - 1/5', 0.1, '2025-04-28', '2025-05-02', 300000, 150000, 0),
('KM004', N'Ngày Quốc tế Thiếu nhi', 0.25, '2025-05-28', '2025-06-05', 200000, 300000, 0),
('KM005', N'Khuyến mãi tháng 10', 0.12, '2024-10-01', '2024-10-31', 400000, 200000, 1);

-- 15. HÓA ĐƠN
INSERT INTO HoaDon (maHoaDon, maNhanVien, maKhachHang, ngayLapHoaDon, VAT, maKhuyenMai, tongTien, thanhTien) VALUES
('HD001', 'NV003', 'KH001', '2024-10-15 10:30:00', 0.1, 'KM001', 1200000, 1158000),
('HD002', 'NV004', 'KH002', '2024-10-16 14:20:00', 0.1, NULL, 800000, 880000),
('HD003', 'NV003', 'KH003', '2024-10-17 09:15:00', 0.1, 'KM005', 1800000, 1901600),
('HD004', 'NV003', 'KH004', '2024-10-18 16:45:00', 0.1, NULL, 600000, 660000),
('HD005', 'NV003', 'KH005', '2024-10-19 11:00:00', 0.1, 'KM005', 2400000, 2467200);

-- 16. VÉ
INSERT INTO Ve (maVe, maLoaiVe, maChuyenTau, maHanhKhach, maGhe, maHoaDon, trangThai, giaVe) VALUES
('V001', 'LV-NL', 'CT-SE1-241020', 'HK001', 'G-SE1-01-1-01', 'HD001', 1, 600000),
('V002', 'LV-NL', 'CT-SE1-241020', 'HK002', 'G-SE1-01-1-02', 'HD001', 2, 600000),
('V003', 'LV-HSSV', 'CT-SE2-241020', 'HK003', 'G-SE1-01-2-01', 'HD002', 3, 800000)