/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  CÔNG HOÀNG
 * Created: Oct 10, 2025
 */
-- Tạo database
CREATE DATABASE QuanLyBanVeTau;
GO

USE QuanLyBanVeTau;
GO

-- 1. BẢNG NHÂN VIÊN
CREATE TABLE NhanVien (
    maNV VARCHAR(15) PRIMARY KEY,
    tenNV NVARCHAR(50) NOT NULL,
    gioiTinh BIT NOT NULL,
    ngaySinh DATE NOT NULL,
    email VARCHAR(50),
    soDienThoai VARCHAR(10) NOT NULL,
    cccd VARCHAR(12) NOT NULL UNIQUE,
	diaChi NVARCHAR(50) NOT NULL,
    chucVu NVARCHAR(20) NOT NULL,
    trangThai BIT NOT NULL,
);

-- 2. BẢNG TÀI KHOẢN
CREATE TABLE TaiKhoan (
    tenDangNhap VARCHAR(10) PRIMARY KEY,
    matKhau NVARCHAR(255) NOT NULL,
    nhanVien VARCHAR(15) NOT NULL,
    -- Ràng buộc
    CONSTRAINT FK_TK_NhanVien FOREIGN KEY (nhanVien) REFERENCES NhanVien(maNV),
);

-- 3. BẢNG KHÁCH HÀNG
CREATE TABLE KhachHang (
    maKH VARCHAR(17) PRIMARY KEY,
    tenKH NVARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(10) NOT NULL,
    cccd VARCHAR(12) NOT NULL UNIQUE,
);

-- 4. BẢNG GA TÀU
CREATE TABLE GaTau (
    maGa VARCHAR(10) PRIMARY KEY,
    tenGa NVARCHAR(50) NOT NULL,
    diaChi NVARCHAR(50) NOT NULL,
    soDienThoai VARCHAR(10),
);

-- 5. BẢNG TÀU
CREATE TABLE Tau (
    maTau VARCHAR(10) PRIMARY KEY,
    tenTau NVARCHAR(50) NOT NULL,
    soToaTau INT NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT CHK_TAU_MaTau CHECK (maTau LIKE 'TAU-[0-9][0-9][0-9]'),
    CONSTRAINT CHK_TAU_SoToaTau CHECK (soToaTau > 0)
);

-- 6. BẢNG TOA TÀU
CREATE TABLE ToaTau (
    maToaTau VARCHAR(10) PRIMARY KEY,
    tenToaTau NVARCHAR(100) NOT NULL,
    soKhoangTau INT NOT NULL,
    maTau VARCHAR(10) NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_TT_Tau FOREIGN KEY (maTau) REFERENCES Tau(maTau),
    CONSTRAINT CHK_TT_MaToaTau CHECK (maToaTau LIKE 'TT-[0-9][0-9][0-9]-[0-9][0-9]'),
    CONSTRAINT CHK_TT_SoKhoangTau CHECK (soKhoangTau > 0)
);


-- 7. BẢNG KHOANG TÀU
CREATE TABLE KhoangTau (
    maKhoangTau VARCHAR(10) PRIMARY KEY,
    tenKhoangTau NVARCHAR(100) NOT NULL,
    soGhe INT NOT NULL,
    maToaTau VARCHAR(10) NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_KT_ToaTau FOREIGN KEY (maToaTau) REFERENCES ToaTau(maToaTau),
);

-- 8. BẢNG LOẠI GHẾ
CREATE TABLE LoaiGhe (
    maLoaiGhe VARCHAR(12) PRIMARY KEY,
    tenLoaiGhe NVARCHAR(100) NOT NULL,
    moTa NVARCHAR(100),
    heSoGhe FLOAT NOT NULL,
);

-- 9. BẢNG GHẾ
CREATE TABLE Ghe (
    maGhe VARCHAR(16) PRIMARY KEY,
    viTri VARCHAR(7) NOT NULL,
    trangThaiGhe NVARCHAR(12) NOT NULL,
    maLoaiGhe VARCHAR(12) NOT NULL,
    maKhoangTau VARCHAR(10) NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_G_LoaiGhe FOREIGN KEY (maLoaiGhe) REFERENCES LoaiGhe(maLoaiGhe),
    CONSTRAINT FK_G_KhoangTau FOREIGN KEY (maKhoangTau) REFERENCES KhoangTau(maKhoangTau),
);

-- 10. BẢNG THỜI GIAN DI CHUYỂN
CREATE TABLE ThoiGianDiChuyen (
    maThoiGianDiChuyen VARCHAR(25) PRIMARY KEY,
    gaDi VARCHAR(10) NOT NULL,
    gaDen VARCHAR(10) NOT NULL,
    thoiGianDi INT NOT NULL, -- Thời gian di chuyển (phút)
    
    -- Ràng buộc
    CONSTRAINT FK_TGDC_GaDi FOREIGN KEY (gaDi) REFERENCES GaTau(maGa),
    CONSTRAINT FK_TGDC_GaDen FOREIGN KEY (gaDen) REFERENCES GaTau(maGa),
);


-- 11. BẢNG CHUYẾN ĐI
CREATE TABLE ChuyenDi (
    maChuyenDi VARCHAR(20) PRIMARY KEY,
    maThoiGianDiChuyen VARCHAR(25) NOT NULL,
    thoiGianKhoiHanh DATETIME NOT NULL,
    thoiGianDenDuTinh DATETIME NOT NULL,
    maTau VARCHAR(10) NOT NULL,
    soGheDaDat INT NOT NULL DEFAULT 0,
    soGheConTrong INT NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_CD_ThoiGianDiChuyen FOREIGN KEY (maThoiGianDiChuyen) REFERENCES ThoiGianDiChuyen(maThoiGianDiChuyen),
    CONSTRAINT FK_CD_Tau FOREIGN KEY (maTau) REFERENCES Tau(maTau),
);

-- 12. BẢNG LOẠI VÉ
CREATE TABLE LoaiVe (
    maLoaiVe VARCHAR(5) PRIMARY KEY,
    tenLoaiVe NVARCHAR(100) NOT NULL,
    moTaLoaiVe NVARCHAR(1000),
    heSoLoaiVe FLOAT NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT CHK_LV_MaLoaiVe CHECK (maLoaiVe IN ('LV-TE', 'LV-HSSV', 'LV-NL')),
    CONSTRAINT CHK_LV_HeSoLoaiVe CHECK (heSoLoaiVe > 0)
);

-- 13. BẢNG HÀNH KHÁCH
CREATE TABLE HanhKhach (
    maHanhKhach VARCHAR(16) PRIMARY KEY,
    tenHanhKhach NVARCHAR(100) NOT NULL,
    cccd VARCHAR(12) NOT NULL,
    namSinh INT NOT NULL,
);

-- 14. BẢNG KHUYẾN MÃI
CREATE TABLE KhuyenMai (
    maKhuyenMai VARCHAR(15) PRIMARY KEY,
    tenKhuyenMai NVARCHAR(100) NOT NULL,
    heSoKhuyenMai FLOAT NOT NULL,
    ngayBatDau DATETIME NOT NULL,
    ngayKetThuc DATETIME NOT NULL,
    tongTienToiThieu FLOAT,
    tienKhuyenMaiToiDa FLOAT,
    trangThai NVARCHAR(20) NOT NULL,
);

-- 15. BẢNG HÓA ĐƠN
CREATE TABLE HoaDon (
    maHoaDon VARCHAR(22) PRIMARY KEY,
    nhanVien VARCHAR(15) NOT NULL,
    khachHang VARCHAR(17) NOT NULL,
    ngayLapHoaDon DATETIME NOT NULL DEFAULT GETDATE(),
    soVe INT NOT NULL,
    VAT FLOAT NOT NULL DEFAULT 0.1,
    khuyenMai VARCHAR(15),
    tongTien FLOAT NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_HD_NhanVien FOREIGN KEY (nhanVien) REFERENCES NhanVien(maNV),
    CONSTRAINT FK_HD_KhachHang FOREIGN KEY (khachHang) REFERENCES KhachHang(maKH),
    CONSTRAINT FK_HD_KhuyenMai FOREIGN KEY (khuyenMai) REFERENCES KhuyenMai(maKhuyenMai),
);

-- 16. BẢNG VÉ
CREATE TABLE Ve (
    maVe VARCHAR(52) PRIMARY KEY,
    maLoaiVe VARCHAR(5) NOT NULL,
    maChuyenDi VARCHAR(20) NOT NULL,
    maHanhKhach VARCHAR(16) NOT NULL,
    maGhe VARCHAR(16) NOT NULL,
    maHoaDon VARCHAR(22) NOT NULL,
    trangThai NVARCHAR(13) NOT NULL,
    giaVe MONEY NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_V_LoaiVe FOREIGN KEY (maLoaiVe) REFERENCES LoaiVe(maLoaiVe),
    CONSTRAINT FK_V_ChuyenDi FOREIGN KEY (maChuyenDi) REFERENCES ChuyenDi(maChuyenDi),
    CONSTRAINT FK_V_HanhKhach FOREIGN KEY (maHanhKhach) REFERENCES HanhKhach(maHanhKhach),
    CONSTRAINT FK_V_Ghe FOREIGN KEY (maGhe) REFERENCES Ghe(maGhe),
    CONSTRAINT FK_V_HoaDon FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon),
);

-- THÊM DỮ LIỆU MẪU

-- Insert dữ liệu mẫu cho bảng Nhân viên
INSERT INTO NhanVien (maNV, tenNV, gioiTinh, ngaySinh, email, soDienThoai, cccd, diaChi, chucVu, trangThai) VALUES
('NV-0-95-123-001', N'Nguyễn Văn An', 0, '1995-05-15', 'conghoangho1802@gmail.com', '0901234567', '079095123456', 'abc 123', N'Nhân viên bán vé', 1),
('NV-1-98-234-002', N'Trần Thị Bình', 1, '1998-08-20', 'conghoangho1802@gmail.com', '0912345678', '079098234567', 'abc 123', N'Nhân viên quản lý', 1),


-- Insert dữ liệu mẫu cho bảng TaiKhoan
INSERT INTO TaiKhoan (tenDangNhap, matKhau, nhanVien) VALUES
('0901234567', N'3c86e8c03745cf7fc0e3a327d6660a8c', 'NV-0-95-123-001'),
('0912345678', N'c0a55f024dd3b67a4d44210d66e7a0ea', 'NV-1-98-234-002'),

