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
    soVe INT NOT NULL,
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

-- Insert dữ liệu mẫu cho bảng Nhân viên
INSERT INTO NhanVien (maNV, tenNV, gioiTinh, ngaySinh, email, soDienThoai, cccd, diaChi, chucVu, trangThai) VALUES
('NV-0-95-123-001', N'Nguyễn Văn An', 0, '1995-05-15', 'conghoangho1802@gmail.com', '0901234567', '079095123456', 'abc 123', N'Nhân viên bán vé', 1),
('NV-1-98-234-002', N'Trần Thị Bình', 1, '1998-08-20', 'conghoangho1802@gmail.com', '0912345678', '079098234567', 'abc 123', N'Nhân viên quản lý', 1)

-- Insert dữ liệu mẫu cho bảng TaiKhoan
INSERT INTO TaiKhoan (tenDangNhap, matKhau, maNV) VALUES
('0901234567', N'3c86e8c03745cf7fc0e3a327d6660a8c', 'NV-0-95-123-001'), --RypLmY9B
('0912345678', N'c0a55f024dd3b67a4d44210d66e7a0ea', 'NV-1-98-234-002') --6uZUqeMV
