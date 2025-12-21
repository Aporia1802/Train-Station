CREATE DATABASE QuanLyBanVeTau;
GO

USE QuanLyBanVeTau;
GO

-- 1. BẢNG NHÂN VIÊN
CREATE TABLE NhanVien (
    maNV VARCHAR(14) PRIMARY KEY,
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
    tenDangNhap VARCHAR(10) NOT NULL PRIMARY KEY,
    matKhau NVARCHAR(50) NOT NULL,
    maNV VARCHAR(14) NOT NULL

    -- Ràng buộc
    CONSTRAINT FK_TK_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);

-- 3. BẢNG KHÁCH HÀNG
CREATE TABLE KhachHang (
    maKH VARCHAR(15) PRIMARY KEY,
    tenKH NVARCHAR(50) NOT NULL,
    soDienThoai VARCHAR(10) NOT NULL,
    cccd VARCHAR(12) NOT NULL UNIQUE,
);

-- 4. BẢNG GA TÀU
CREATE TABLE GaTau (
    maGa VARCHAR(6) PRIMARY KEY,
    tenGa NVARCHAR(50) NOT NULL,
    diaChi NVARCHAR(1000) NOT NULL,
    soDienThoai VARCHAR(10) NOT NULL
);

-- 5. BẢNG TÀU
CREATE TABLE Tau (
    maTau VARCHAR(7) PRIMARY KEY,
    tenTau NVARCHAR(50) NOT NULL,
    soToaTau INT NOT NULL,
    ngayHoatDong DATE NOT NULL,
    trangThai INT NOT NULL
);

-- 6. BẢNG TOA TÀU
CREATE TABLE ToaTau (
    maToaTau VARCHAR(20) PRIMARY KEY,
    soKhoangTau INT NOT NULL,
    soHieuToa INT NOT NULL,
	loaiToa VARCHAR(100) NOT NULL,
    maTau VARCHAR(7) NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_TT_Tau FOREIGN KEY (maTau) REFERENCES Tau(maTau)
);


-- 7. BẢNG KHOANG TÀU
CREATE TABLE KhoangTau (
    maKhoangTau VARCHAR(20) PRIMARY KEY,
    soHieuKhoang INT NOT NULL,
    soGhe INT NOT NULL,
    maToaTau VARCHAR(20) NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_KT_ToaTau FOREIGN KEY (maToaTau) REFERENCES ToaTau(maToaTau)
);

-- 8. BẢNG LOẠI GHẾ
CREATE TABLE LoaiGhe (
    maLoaiGhe VARCHAR(20) PRIMARY KEY,
    tenLoaiGhe NVARCHAR(50) NOT NULL,
    moTa NVARCHAR(100),
    heSoLoaiGhe FLOAT NOT NULL
);

-- 9. BẢNG GHẾ
CREATE TABLE Ghe (
    maGhe VARCHAR(20) PRIMARY KEY,
    soGhe INT NOT NULL,
    maLoaiGhe VARCHAR(20) NOT NULL,
    maKhoangTau VARCHAR(20),
    
    -- Ràng buộc
    CONSTRAINT FK_G_LoaiGhe FOREIGN KEY (maLoaiGhe) REFERENCES LoaiGhe(maLoaiGhe),
    CONSTRAINT FK_G_KhoangTau FOREIGN KEY (maKhoangTau) REFERENCES KhoangTau(maKhoangTau)
);

-- 10. BẢNG TUYẾN ĐƯỜNG
CREATE TABLE TuyenDuong (
    maTuyenDuong VARCHAR(20) PRIMARY KEY,
	thoiGianDiChuyen int NOT NULL,
    quangDuong float NOT NULL,
    soTienMotKm float NOT NULL,
    gaDi VARCHAR(6) NOT NULL,
    gaDen VARCHAR(6) NOT NULL,
    
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
    maTau VARCHAR(7) NOT NULL,
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
    cccd VARCHAR(12),
    ngaySinh DATE
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
    maNhanVien VARCHAR(14) NOT NULL,
    maKhachHang VARCHAR(15) NOT NULL,
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
    maNV VARCHAR(14) NOT NULL,
    
    -- Ràng buộc
    CONSTRAINT FK_HDT_Ve FOREIGN KEY (maVe) REFERENCES Ve(maVe),
    CONSTRAINT FK_HDT_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);

CREATE TABLE HoaDonDoi (
    maHoaDonDoi VARCHAR(20) PRIMARY KEY,
    ngayDoi DATETIME NOT NULL,
    maNhanVien VARCHAR(14) NOT NULL,
    maVe VARCHAR(20) NOT NULL,
    phiDoi DECIMAL(18,2) DEFAULT 20000,
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maVe) REFERENCES Ve(maVe)
);

-- THÊM DỮ LIỆU MẪU

-- 1. NHÂN VIÊN
INSERT INTO NhanVien (maNV, tenNV, gioiTinh, ngaySinh, email, soDienThoai, cccd, diaChi, chucVu, trangThai) VALUES
('NV115051995001', N'Lưu Văn Quốc', 1, '1995-05-15', 'luuvanquoc1505@gmail.com', '0901234560', '079095123456', N'79 Gò Vấp', N'Nhân viên quản lý', 1),
('NV120081998002', N'Nguyễn Huy Hoàng', 1, '1998-08-20', 'nguyenhuyhoang2008@gmail.com', '0912345678', '079098234567', N'20 Nguyễn Văn Trỗi', N'Nhân viên bán vé', 1),
('NV118022005003', N'Hồ Công Hoàng', 1, '2005-02-18', 'conghoangho1802@gmail.com', '0349573425', '079098234568', N'36 Nguyễn Văn Bảo', N'Nhân viên quản lý', 1),
('NV130102000004', N'Lê Minh Phúc', 1, '2000-10-30', 'leminhphuc3010@gmail.com', '0938635712', '079098234569', N'12 Nguyễn Trãi', N'Nhân viên bán vé', 1),
('NV025031997005', N'Trần Thị Hương', 0, '1997-03-25', 'tranthihuong2503@gmail.com', '0923456789', '079097345678', N'45 Lê Văn Sỹ', N'Nhân viên bán vé', 0),
('NV112121999006', N'Phạm Đức Anh', 1, '1999-12-12', 'phamducanh1212@gmail.com', '0934567890', '079099456789', N'88 Điện Biên Phủ', N'Nhân viên quản lý', 1),
('NV008061996007', N'Võ Thị Mai', 0, '1996-06-08', 'vothimai0806@gmail.com', '0945678901', '079096567890', N'15 Phan Xích Long', N'Nhân viên bán vé', 1),
('NV120092001008', N'Đặng Văn Hải', 1, '2001-09-20', 'dangvanhai2009@gmail.com', '0956789012', '079001678901', N'67 Hoàng Văn Thụ', N'Nhân viên bán vé', 0),
('NV014112003009', N'Nguyễn Thị Lan', 0, '2003-11-14', 'nguyenthilan1411@gmail.com', '0967890123', '079003789012', N'23 Trần Hưng Đạo', N'Nhân viên bán vé', 1),
('NV107042002010', N'Bùi Quang Minh', 1, '2002-04-07', 'buiquangminh0704@gmail.com', '0978901234', '079002890123', N'51 Cách Mạng Tháng 8', N'Nhân viên quản lý', 1);
GO

-- 2. TÀI KHOẢN
INSERT INTO TaiKhoan (tenDangNhap, matKhau, maNV) VALUES
('0901234560', N'b169b4c9a796634684a5fa0f9546e33a', 'NV115051995001'), --RypLmY9B
('0912345678', N'b169b4c9a796634684a5fa0f9546e33a', 'NV120081998002'), --6uZUqeMV
('0349573425', N'b169b4c9a796634684a5fa0f9546e33a', 'NV118022005003'), --6uZUqeMV
('0938635712', N'b169b4c9a796634684a5fa0f9546e33a', 'NV130102000004'); --6uZUqeMV

INSERT INTO KhachHang (maKH, tenKH, soDienThoai, cccd) VALUES
('KH001', N'Nguyễn Văn An', '0912345678', '012345678123'),
('KH002', N'Trần Thị Mai', '0987654321', '012345678456'),
('KH003', N'Phạm Văn Khôi', '0938765432', '012345678789'),
('KH004', N'Lê Thị Hồng', '0901234562', '012345679012'),
('KH005', N'Vũ Văn Nam', '0978123456', '012345679345'),
('KH006', N'Hoàng Thị Lan', '0967234567', '012345679678'),
('KH007', N'Đỗ Văn Cường', '0945123456', '012345679901'),
('KH008', N'Bùi Thị Hoa', '0934123456', '012345680234'),
('KH009', N'Ngô Văn Minh', '0917234567', '012345680567'),
('KH010', N'Lý Thị Hạnh', '0908345678', '012345680890'),
('KH011', N'Tô Văn Hòa', '0923456712', '012345681321'),
('KH012', N'Đặng Thị Huyền', '0968456723', '012345681654'),
('KH013', N'Trịnh Văn Thái', '0939784561', '012345681987'),
('KH014', N'Cao Thị Nhàn', '0978564123', '012345682210'),
('KH015', N'Mai Văn Quang', '0945123678', '012345682543'),
('KH016', N'Hà Thị Ngọc', '0909781234', '012345682876'),
('KH017', N'Đinh Văn Bình', '0934234567', '012345683109'),
('KH018', N'Lâm Thị Tuyết', '0912347865', '012345683432'),
('KH019', N'Kiều Văn Sơn', '0987123564', '012345683765'),
('KH020', N'Châu Thị Xuân', '0978432123', '012345683098'),
('KH021', N'Trần Thị Mai', '0987432110', '079325241039'),
('KH022', N'Lê Văn Hùng', '0918543201', '048209991028'),
('KH023', N'Nguyễn Thanh Phong', '0903456789', '001215002117'),
('KH024', N'Đặng Thị Ngọc', '0976444499', '031320045742'),
('KH025', N'Vũ Minh Tuấn', '0937238123', '079219905561'),
('KH026', N'Phạm Hồng Nhung', '0965123456', '079319950098'),
('KH027', N'Ngô Bá Thành', '0324341234', '001223994014'),
('KH028', N'Trần Văn Dũng', '0854678901', '048224006728'),
('KH029', N'Lê Thị Quỳnh', '0945234561', '031321994683'),
('KH030', N'Đỗ Văn Quang', '0987654321', '001220030325'),
('KH031', N'Phan Thị Hồng', '0937771234', '079320010129'),
('KH032', N'Nguyễn Minh Trí', '0925667123', '048215993874'),
('KH033', N'Lê Hoàng Nhật', '0975456777', '031225006496'),
('KH034', N'Vũ Thị Lan', '0904223344', '001319990387'),
('KH035', N'Phạm Văn Đức', '0978665234', '079218001908'),
('KH036', N'Nguyễn Thị Dung', '0968123123', '048321984472'),
('KH037', N'Hoàng Văn Khánh', '0927888999', '031222993139'),
('KH038', N'Bùi Thị Thanh', '0911223344', '001319988803'),
('KH039', N'Lương Văn Bình', '0945332112', '079217001370'),
('KH040', N'Đặng Thị Hoa', '0908123456', '048320991264'),
('KH041', N'Trần Văn Kiên', '0933221100', '031221002157'),
('KH042', N'Phan Thị Tuyết', '0899667788', '001219983681'),
('KH043', N'Nguyễn Hữu Tài', '0987432123', '079223994249'),
('KH044', N'Trịnh Thị Hiền', '0912567890', '048222005904'),
('KH045', N'Đỗ Văn Hòa', '0932457891', '031221991703'),
('KH046', N'Vũ Thị Nhàn', '0964789123', '001323981418'),
('KH047', N'Trần Minh Nhật', '0903678901', '079218007320'),
('KH048', N'Hoàng Thị Tuyết', '0923888999', '048224003999'),
('KH049', N'Lê Văn Đạt', '0931234567', '031219990485'),
('KH050', N'Ngô Thị Thanh', '0974123981', '001220008777'),
('KH051', N'Phạm Văn Sơn', '0968327123', '079222996616'),
('KH052', N'Nguyễn Hồng Nhung', '0901234123', '048319985335'),
('KH053', N'Đinh Văn An', '0937651230', '031224999225'),
('KH054', N'Đỗ Thị Huyền', '0987364920', '001223991112'),
('KH055', N'Nguyễn Văn Quý', '0928765432', '079223995440'),
('KH056', N'Trần Thị Thanh', '0912673498', '048219003718'),
('KH057', N'Lê Hữu Nam', '0908967345', '031223990358'),
('KH058', N'Bùi Thị Ngọc', '0978123490', '001220006801'),
('KH059', N'Phạm Minh Tuấn', '0984237890', '079221004690'),
('KH060', N'Nguyễn Hoàng Nam', '0912345678', '079219994124'),
('KH061', N'Trần Thị Hải', '0909876543', '048320004685'),
('KH062', N'Phạm Văn Khôi', '0932123456', '031222003912'),
('KH063', N'Đỗ Thị Mai', '0978765432', '001219993041'),
('KH064', N'Lê Quang Dũng', '0961234987', '079218991278'),
('KH065', N'Ngô Thị Ngọc', '0945123890', '048224991594'),
('KH066', N'Bùi Văn Hòa', '0928341234', '031220000312'),
('KH067', N'Vũ Thị Thúy', '0909567123', '001319992467'),
('KH068', N'Trần Đức Minh', '0976347891', '079223997726'),
('KH069', N'Lê Thị Hồng', '0918772345', '048323982053'),
('KH070', N'Nguyễn Văn Sơn', '0984567890', '031225005841'),
('KH071', N'Đinh Thị Lan', '0937891234', '001220009320'),
('KH072', N'Phan Văn Lực', '0967123456', '079319985492'),
('KH073', N'Hoàng Thị Minh', '0901982345', '048321990633'),
('KH074', N'Nguyễn Hữu Phước', '0974123987', '031218994184'),
('KH075', N'Trần Thị Xuân', '0912349876', '001319987909'),
('KH076', N'Lê Văn Hoàng', '0987771234', '079220004236'),
('KH077', N'Đỗ Thị Hạnh', '0931236547', '048220999674'),
('KH078', N'Vũ Hồng Sơn', '0961234560', '031221002095'),
('KH079', N'Nguyễn Thị Kim', '0947890123', '001323992349'),
('KH080', N'Phạm Văn An', '0921456789', '079217999760'),
('KH081', N'Ngô Thị Duyên', '0912987654', '048224004430'),
('KH082', N'Lê Minh Nhật', '0901678345', '031220004815'),
('KH083', N'Trần Thị Hoa', '0978888881', '001222008109'),
('KH084', N'Hoàng Văn Thái', '0987456321', '079218990508'),
('KH085', N'Bùi Thị Thanh', '0965432187', '048219002668'),
('KH086', N'Nguyễn Văn Quý', '0933214890', '031222991292'),
('KH087', N'Đỗ Thị Phượng', '0909647382', '001223987700'),
('KH088', N'Trịnh Văn Long', '0912123123', '079222003429'),
('KH089', N'Phan Thị Quỳnh', '0983432123', '048223006318'),
('KH090', N'Nguyễn Hữu Lâm', '0972123123', '031223992835'),
('KH091', N'Vũ Thị Nhung', '0908887776', '001221007112'),
('KH092', N'Lê Văn Tài', '0935567890', '079218994545'),
('KH093', N'Trần Thị Hà', '0964345678', '048221009689'),
('KH094', N'Ngô Minh Quân', '0949876543', '031219003423'),
('KH095', N'Đỗ Thị Hồng', '0918888882', '001320006209'),
('KH096', N'Bùi Văn Trí', '0984445566', '079217990571'),
('KH097', N'Phạm Thị Huệ', '0901345678', '048220007338'),
('KH098', N'Nguyễn Minh Đạt', '0921123456', '031223991711'),
('KH099', N'Lê Thị Bích', '0979456781', '001219995480'),
('KH100', N'Đinh Nhật Linh', '0739219562', '048202241405'),
('KH101', N'Huỳnh Hải Nam', '0375382517', '031182998569'),
('KH102', N'Ngô Hữu Hà', '0550613436', '079199832983'),
('KH103', N'Bùi Ngọc Thảo', '0739279625', '079302151526'),
('KH104', N'Phạm Ngọc Đức', '0706332877', '048201236872'),
('KH105', N'Nguyễn Thanh Tú', '0348271911', '001300998160'),
('KH106', N'Lê Minh Hưng', '0381092637', '001225991381'),
('KH107', N'Hoàng Hữu Phát', '0395834567', '048219004214'),
('KH108', N'Ngô Minh Thư', '0769231543', '031221005630'),
('KH109', N'Trần Thị Nhàn', '0932145634', '001301001784'),
('KH110', N'Vũ Hồng Kỳ', '0912384756', '079222994913'),
('KH111', N'Nguyễn Đình Quang', '0907631452', '048222001287'),
('KH112', N'Trần Nhật Hào', '0381763452', '001319000148'),
('KH113', N'Lê Thị Thùy Dương', '0976528712', '031225996509'),
('KH114', N'Đoàn Minh Hiếu', '0921843543', '079219004732'),
('KH115', N'Nguyễn Thị Xuân', '0362918273', '048224003284'),
('KH116', N'Bùi Hồng Sơn', '0942173645', '048319995456'),
('KH117', N'Phan Thị Yến', '0701423765', '001301006601'),
('KH118', N'Ngô Tuấn Vũ', '0765439821', '031217993812'),
('KH119', N'Lê Văn Trường', '0901273645', '079220994364'),
('KH120', N'Vũ Ngọc Hương', '0932876543', '048220996137'),
('KH121', N'Nguyễn Thanh Hà', '0912938475', '001323990289'),
('KH122', N'Đặng Thị Tuyết', '0962819384', '031224990478'),
('KH123', N'Hoàng Đình Tài', '0987651234', '079218994901'),
('KH124', N'Nguyễn Thị Ngọc', '0909271843', '048221002516'),
('KH125', N'Bùi Văn Huy', '0928317654', '001220007639'),
('KH126', N'Phan Thị Mai', '0971239845', '031221001784'),
('KH127', N'Ngô Văn Hạnh', '0938127364', '048222996247'),
('KH128', N'Trần Thị Tuyết Mai', '0765381745', '001225995315'),
('KH129', N'Lê Minh Hòa', '0942173846', '079223990801'),
('KH130', N'Nguyễn Thị Hồng', '0901982374', '048221993934'),
('KH131', N'Vũ Thị Hạnh', '0912736458', '001318001472'),
('KH132', N'Đỗ Hồng Quân', '0981324871', '031219999158'),
('KH133', N'Trần Thanh Bình', '0938123456', '048220994603'),
('KH134', N'Ngô Thị Yến Nhi', '0961234890', '079219996720'),
('KH135', N'Phạm Hữu Nhân', '0709273645', '001323998397'),
('KH136', N'Bùi Thị Hoa', '0912378456', '048224007114'),
('KH137', N'Nguyễn Hữu Lộc', '0921873456', '031218002268'),
('KH138', N'Lê Thị Ngân', '0942173648', '001221004429'),
('KH139', N'Trần Quốc Dũng', '0909273845', '048223993753'),
('KH140', N'Vũ Đình An', '0981376452', '079220002305'),
('KH141', N'Đinh Ngọc Diệp', '0912876345', '031219990690'),
('KH142', N'Nguyễn Thị Lan', '0967381234', '001319000872'),
('KH143', N'Hoàng Văn Hưng', '0909871234', '048221004234'),
('KH144', N'Phạm Hồng Nhung', '0975381273', '079219997148'),
('KH145', N'Lê Quốc Huy', '0938476521', '001323994521'),
('KH146', N'Trần Thị Kim Dung', '0909384721', '031220001693'),
('KH147', N'Nguyễn Văn Lâm', '0923481273', '048224999305'),
('KH148', N'Đỗ Minh Tâm', '0912736481', '079217998719'),
('KH149', N'Ngô Hữu Nghĩa', '0987623415', '001220994834'),
('KH150', N'Trần Quốc Việt', '0938127345', '048222996694'),
('KH151', N'Lê Thị Kim Ngân', '0909873124', '031220006518'),
('KH152', N'Nguyễn Hoàng Nam', '0967128432', '001322999387'),
('KH153', N'Đặng Thị Hiền', '0972183456', '079224996249'),
('KH154', N'Phan Minh Châu', '0923847365', '048219007180'),
('KH155', N'Hoàng Thanh Bình', '0912375842', '001301999831'),
('KH156', N'Ngô Hữu Tài', '0987138462', '079218006374'),
('KH157', N'Vũ Thị Thanh', '0908374126', '048223006652'),
('KH158', N'Trần Đình Huy', '0921837465', '001319006109'),
('KH159', N'Nguyễn Ngọc Thảo', '0942173648', '031224007475'),
('KH160', N'Bùi Đức Trọng', '0971827364', '079217994206'),
('KH161', N'Lê Hồng Nhung', '0938712645', '048225996718'),
('KH162', N'Nguyễn Thị Thu Hà', '0909723487', '001323004291'),
('KH163', N'Phạm Hữu Đạt', '0912873649', '079221999624'),
('KH164', N'Trần Nhật Hoàng', '0968723645', '048220007842'),
('KH165', N'Đoàn Thị Như Quỳnh', '0923847163', '001300995397'),
('KH166', N'Ngô Văn Toàn', '0908374632', '031225005105'),
('KH167', N'Lý Hoàng Minh', '0938712365', '079220998789'),
('KH168', N'Trịnh Thị Duyên', '0987263412', '048221007524'),
('KH169', N'Phan Văn Lực', '0908127345', '001323994342'),
('KH170', N'Nguyễn Thị Kiều Trang', '0972381764', '031218996609'),
('KH171', N'Đặng Trọng Nhân', '0912873645', '048223999458'),
('KH172', N'Lê Thanh Ngọc', '0963821743', '079221996703'),
('KH173', N'Vũ Hoài Thương', '0938476523', '001301995816'),
('KH174', N'Nguyễn Hữu Nghĩa', '0921387462', '048224993280'),
('KH175', N'Hoàng Thị Lan', '0909283471', '001219005134'),
('KH176', N'Trần Minh Tâm', '0912387462', '031223005761'),
('KH177', N'Ngô Nhật Hào', '0972183746', '079223005487'),
('KH178', N'Phạm Thị Thanh Hương', '0938126345', '048225004673'),
('KH179', N'Lê Văn Đạt', '0962378145', '001319006362'),
('KH180', N'Nguyễn Thị Mỹ Linh', '0908273615', '031221003509'),
('KH181', N'Trần Quốc Đạt', '0981234765', '001220002218'),
('KH182', N'Phan Thị Mai Hương', '0912837465', '079222994867'),
('KH183', N'Đỗ Văn Hoàng', '0942837465', '048224998430'),
('KH184', N'Nguyễn Thị Duyên', '0923847162', '001301005739'),
('KH185', N'Vũ Trọng Tấn', '0961837462', '031218001195'),
('KH186', N'Bùi Hồng Sơn', '0932187463', '079221004622'),
('KH187', N'Trần Thị Hạnh', '0908723645', '048223007318'),
('KH188', N'Lê Văn Nghĩa', '0912837465', '001323999483'),
('KH189', N'Nguyễn Hữu Dũng', '0942134765', '031220006708'),
('KH190', N'Phan Thị Hoàng Yến', '0972381462', '048221997891'),
('KH191', N'Ngô Minh Thắng', '0923847461', '079220007367'),
('KH192', N'Vũ Thị Lệ Quyên', '0961823746', '001322004259'),
('KH193', N'Trịnh Văn Long', '0901283764', '048225005685'),
('KH194', N'Nguyễn Thị Như Quỳnh', '0912837462', '031224996126'),
('KH195', N'Hoàng Thanh Tùng', '0972813746', '001318001418'),
('KH196', N'Đặng Thị Thanh Huyền', '0938471623', '079223001542'),
('KH197', N'Nguyễn Thị Hồng Gấm', '0938273645', '001319005214'),
('KH198', N'Lê Văn Thịnh', '0909827364', '048224004761'),
('KH199', N'Trần Thị Kim Yến', '0961827364', '031222999385'),
('KH200', N'Phạm Hữu Nghĩa', '0928374652', '079218998498'),
('KH201', N'Đỗ Thị Thanh Hà', '0912873645', '001301007670'),
('KH202', N'Nguyễn Quốc Cường', '0902837465', '048221007153'),
('KH203', N'Lê Thị Thu Thủy', '0948273641', '001323006487'),
('KH204', N'Vũ Minh Khang', '0961283745', '031220004731'),
('KH205', N'Trần Văn Hiếu', '0982736451', '079224005214'),
('KH206', N'Hoàng Thị Tuyết Mai', '0938127364', '048225005638'),
('KH207', N'Ngô Văn Lộc', '0912837462', '001322006320'),
('KH208', N'Phan Thị Tuyền', '0909283746', '031223997489'),
('KH209', N'Trịnh Văn Đông', '0938127364', '079219994764'),
('KH210', N'Nguyễn Hữu Phát', '0921387462', '001300006291'),
('KH211', N'Đinh Thị Bích Vân', '0981237465', '048224999670'),
('KH212', N'Phạm Văn Tuấn', '0902378465', '031225996547'),
('KH213', N'Đặng Thanh Hùng', '0972837465', '001318005183'),
('KH214', N'Nguyễn Thị Nhàn', '0932173645', '079222007924'),
('KH215', N'Trần Quốc Hưng', '0921387452', '048220004308'),
('KH216', N'Vũ Thị Như Hoa', '0961827346', '001301006492'),
('KH217', N'Lê Hồng Quân', '0908723641', '031218996715'),
('KH218', N'Phan Thị Quỳnh Anh', '0912378462', '048223007367'),
('KH219', N'Nguyễn Thanh Tùng', '0981237456', '001323994280'),
('KH220', N'Đỗ Minh Đức', '0962378146', '001322006681'),
('KH221', N'Trịnh Thị Thanh Trúc', '0908127345', '048225005354'),
('KH222', N'Lê Anh Tuấn', '0938471623', '031224007720'),
('KH223', N'Vũ Thị Bảo Ngọc', '0912837462', '001319005198'),
('KH224', N'Ngô Quốc Việt', '0923847162', '079220997583'),
('KH225', N'Nguyễn Thị Tuyết Hương', '0972183746', '048222999427'),
('KH226', N'Nguyễn Thị Ngọc Mai', '0938123456', '001319002291'),
('KH227', N'Phạm Văn Đức', '0918234567', '079221004764'),
('KH228', N'Lê Thị Ngọc Diệp', '0971234567', '048225006839'),
('KH229', N'Trần Hữu Thắng', '0921234567', '031218997715'),
('KH230', N'Đinh Thị Bích Thủy', '0909123456', '001323006426'),
('KH231', N'Vũ Minh Phương', '0968123456', '048224005307'),
('KH232', N'Ngô Quang Vinh', '0989123456', '079220004978'),
('KH233', N'Nguyễn Văn Thái', '0911123456', '031221006182'),
('KH234', N'Phan Thị Mỹ Linh', '0932123456', '001300005550'),
('KH235', N'Lê Hồng Hạnh', '0907123456', '048222007667'),
('KH236', N'Trần Quốc Dũng', '0977123456', '079219004903'),
('KH237', N'Nguyễn Thị Hồng Nhung', '0919123456', '001322004184'),
('KH238', N'Đặng Minh Khoa', '0929123456', '031223006350'),
('KH239', N'Phạm Thanh Vân', '0964123456', '048225003761'),
('KH240', N'Ngô Trọng Tín', '0987123456', '001318007289'),
('KH241', N'Lê Văn Lâm', '0939123456', '079224006470'),
('KH242', N'Trần Thị Kim Tuyến', '0906123456', '031222994318'),
('KH243', N'Nguyễn Thị Tuyết Mai', '0911123478', '001323007285'),
('KH244', N'Phạm Hữu Duy', '0938223478', '048221006364'),
('KH245', N'Lê Minh Thắng', '0967823478', '031218005947'),
('KH246', N'Ngô Thanh Hà', '0971323478', '079225004710'),
('KH247', N'Trịnh Văn Sơn', '0917823478', '001322994157'),
('KH248', N'Vũ Thị Diễm Hương', '0928323478', '048224006804'),
('KH249', N'Nguyễn Quốc Huy', '0987323478', '031219994329'),
('KH250', N'Đặng Thị Kim Liên', '0909323478', '001320005493'),
('KH251', N'Phan Văn Thịnh', '0938123489', '048220004759'),
('KH252', N'Lê Thị Phương Anh', '0918123489', '001319005235'),
('KH253', N'Nguyễn Văn Tài', '0978123489', '079224996678'),
('KH254', N'Trần Thị Hạnh', '0908123489', '031220006364'),
('KH255', N'Đỗ Hữu Nghĩa', '0968123489', '048223995901'),
('KH256', N'Nguyễn Thanh Lam', '0919123489', '001301005138'),
('KH257', N'Vũ Thị Thu Trang', '0929123489', '031221007421'),
('KH258', N'Phạm Quốc Cường', '0981123489', '048225006587'),
('KH259', N'Ngô Kim Anh', '0909123489', '001323997314'),
('KH260', N'Lê Thị Bảo Hân', '0937123489', '079223005286'),
('KH261', N'Trần Hữu Trí', '0912123490', '001322997805'),
('KH262', N'Nguyễn Thị Bích Trâm', '0928123490', '048224005394'),
('KH263', N'Phan Minh Quân', '0908123490', '031225006278'),
('KH264', N'Đặng Thị Ánh Tuyết', '0978123490', '001319006507'),
('KH265', N'Vũ Quốc Đạt', '0967123490', '048221997693'),
('KH266', N'Lê Thanh Hiền', '0919123490', '031220007124'),
('KH267', N'Nguyễn Thị Ngọc Hân', '0938123490', '079220004379'),
('KH268', N'Ngô Văn Lợi', '0989123490', '001323996840'),
('KH269', N'Phạm Thị Như Quỳnh', '0908123490', '048225007643'),
('KH270', N'Đinh Văn Long', '0912123490', '031222006265'),
('KH271', N'Nguyễn Văn Hậu', '0913123491', '001322997347'),
('KH272', N'Trần Thị Mỹ Duyên', '0939123491', '048225006591'),
('KH273', N'Lê Minh Tân', '0928123491', '031223007218'),
('KH274', N'Phạm Hồng Nhung', '0978123491', '001319996879'),
('KH275', N'Vũ Quốc Cường', '0968123491', '048221994642'),
('KH276', N'Ngô Thị Hồng Hạnh', '0909123491', '031220005375'),
('KH277', N'Đặng Văn Tuấn', '0918123491', '079224006210'),
('KH278', N'Nguyễn Thị Lan Hương', '0938123491', '001321007903'),
('KH279', N'Lê Hữu Phước', '0987123492', '048223996487'),
('KH280', N'Phạm Thị Mai', '0907123492', '001322007126'),
('KH281', N'Trần Minh Hiếu', '0917123492', '031218007712'),
('KH282', N'Ngô Hồng Nhung', '0971123492', '079220005864'),
('KH283', N'Đinh Thị Hằng', '0969123492', '001319996358'),
('KH284', N'Nguyễn Quốc Đạt', '0929123492', '048224007493'),
('KH285', N'Lê Văn Long', '0931123492', '031222007230'),
('KH286', N'Trần Thị Bích Hường', '0909123492', '001323005607'),
('KH287', N'Phan Văn Giang', '0912123493', '079225007782'),
('KH288', N'Nguyễn Thị Kim Yến', '0928123493', '001320005143'),
('KH289', N'Vũ Hữu Thành', '0938123493', '048222997468'),
('KH290', N'Lê Thị Diễm Hương', '0908123493', '031221007325'),
('KH291', N'Ngô Minh Tâm', '0978123493', '001321997999'),
('KH292', N'Trần Thị Thảo', '0968123493', '048225005517'),
('KH293', N'Phạm Văn Hòa', '0918123493', '031224007281'),
('KH294', N'Đinh Thị Thu Hằng', '0989123493', '001322996630'),
('KH295', N'Nguyễn Hữu Nhân', '0929123493', '079223007719'),
('KH296', N'Lê Thanh Tú', '0913123494', '001319996382'),
('KH297', N'Trần Thị Kim Chi', '0909123494', '048221007674'),
('KH298', N'Nguyễn Đức Anh', '0979123494', '031223007230'),
('KH299', N'Phạm Thị Thúy Hằng', '0969123494', '079224006816'),
('KH300', N'Đỗ Văn Khoa', '0938123494', '001323997499'),
('KH301', N'Lê Minh Ngọc', '0912123494', '048224005127'),
('KH302', N'Nguyễn Thị Hoài Thu', '0908123494', '031220006358'),
('KH303', N'Trần Quốc Hưng', '0989123494', '001321996968'),
('KH304', N'Ngô Thị Hồng Loan', '0928123494', '048225007205'),
('KH305', N'Phạm Văn Tài', '0978123495', '031222994741'),
('KH306', N'Nguyễn Thị Thanh Trúc', '0908123495', '001320006392'),
('KH307', N'Trần Hữu Minh', '0918123495', '048224005564'),
('KH308', N'Lê Thị Mai Hương', '0938123495', '031218007832'),
('KH309', N'Ngô Thị Cẩm Vân', '0969123495', '001319006317'),
('KH310', N'Phạm Quốc Thắng', '0929123495', '079223994670'),
('KH311', N'Nguyễn Thanh Phong', '0989123495', '048221007289'),
('KH312', N'Trần Thị Thu Hà', '0909123495', '001323006156'),
('KH313', N'Lê Hữu Nghĩa', '0917123495', '031219006417'),
('KH314', N'Đặng Thị Hồng Gấm', '0932123495', '048223005784'),
('KH315', N'Nguyễn Văn Toàn', '0912345621', '001320996294'),
('KH316', N'Trần Thị Kim Liên', '0938456721', '031223006871'),
('KH317', N'Lê Hữu Trí', '0909654781', '048224996160'),
('KH318', N'Phạm Thị Cẩm Nhung', '0967893412', '079225007473'),
('KH319', N'Ngô Văn Hùng', '0978945631', '001322997528'),
('KH320', N'Đỗ Thị Thảo Vy', '0987654321', '031221007614'),
('KH321', N'Nguyễn Hữu Duy', '0918934567', '048223007347'),
('KH322', N'Trần Thị Thu Cúc', '0909345671', '001319997785'),
('KH323', N'Phan Văn Lực', '0938123456', '079224006912'),
('KH324', N'Nguyễn Quốc Việt', '0912983746', '001323997203'),
('KH325', N'Lê Thị Hồng Nhung', '0909182736', '048222996658'),
('KH326', N'Vũ Minh Tân', '0928934561', '031224007431'),
('KH327', N'Trần Thanh Hằng', '0987345216', '001320006156'),
('KH328', N'Ngô Văn Tuấn', '0938456723', '048223006389'),
('KH329', N'Đặng Thị Kim Dung', '0919238475', '031219007627'),
('KH330', N'Phạm Văn Minh', '0978234567', '001321007281'),
('KH331', N'Lê Thị Thúy Hằng', '0909238457', '079223006304'),
('KH332', N'Nguyễn Quốc Hùng', '0912345763', '048224007709'),
('KH333', N'Trần Thị Ngọc Lan', '0938123499', '001322996345'),
('KH334', N'Phạm Văn Lâm', '0912873456', '031221007562'),
('KH335', N'Lê Thị Như Quỳnh', '0909345781', '048225006684'),
('KH336', N'Ngô Văn Khánh', '0978123945', '001319996229'),
('KH337', N'Đặng Thị Thanh Huyền', '0967234591', '079225005413'),
('KH338', N'Nguyễn Hoàng Anh', '0928934567', '001320007758'),
('KH339', N'Trần Văn Hòa', '0912834567', '031222007132'),
('KH340', N'Phạm Thị Thu Hà', '0938123451', '048221996605'),
('KH341', N'Lê Quang Trường', '0987456721', '001319007926'),
('KH342', N'Nguyễn Hữu Tài', '0912938475', '079224006174'),
('KH343', N'Trần Thị Bích Vân', '0938123492', '001322996388'),
('KH344', N'Đặng Văn Phong', '0909874561', '031220006753'),
('KH345', N'Lê Thị Mai Anh', '0912345672', '048223006601'),
('KH346', N'Ngô Thị Hồng Ánh', '0978934561', '001321007842'),
('KH347', N'Phan Văn Bảo', '0928745631', '031223005291'),
('KH348', N'Vũ Minh Hằng', '0909837456', '048222996430'),
('KH349', N'Nguyễn Thị Thuỳ Trang', '0987123456', '001320007308'),
('KH350', N'Trần Văn Hạnh', '0912837456', '079223006679'),
('KH351', N'Phạm Thị Cẩm Tiên', '0938456722', '001319997574'),
('KH352', N'Lê Hữu Nghĩa', '0912834561', '048224007185'),
('KH353', N'Nguyễn Thị Hồng Tươi', '0909238475', '001322996723'),
('KH354', N'Trần Minh Cường', '0912938471', '031219006294'),
('KH355', N'Phạm Thị Kim Oanh', '0987654312', '001320006516'),
('KH356', N'Lê Văn Trí', '0967345217', '048223006370'),
('KH357', N'Ngô Thị Lệ Quyên', '0912837457', '001321006879'),
('KH358', N'Nguyễn Quốc Thái', '0938123453', '031224007467'),
('KH359', N'Trần Thị Tuyết Mai', '0909874563', '079225007134'),
('KH360', N'Phan Thị Kiều', '0978234562', '048221006608'),
('KH361', N'Đỗ Văn Lâm', '0912983457', '001319997352'),
('KH362', N'Nguyễn Hữu Nghĩa', '0909872345', '001322007712'),
('KH363', N'Trần Thị Bảo Trân', '0912839475', '031219997493'),
('KH364', N'Lê Văn Nam', '0928934563', '048224006821'),
('KH365', N'Ngô Thị Hồng Nhung', '0978123456', '001321007154'),
('KH366', N'Phạm Văn Khoa', '0909456123', '001320007375'),
('KH367', N'Đặng Thị Cẩm Tú', '0987456723', '048223007684'),
('KH368', N'Nguyễn Quốc Bảo', '0938123459', '031221006232'),
('KH369', N'Trần Thị Mỹ Linh', '0912839476', '079224006503'),
('KH370', N'Lê Minh Thành', '0909238476', '001319997947'),
('KH371', N'Nguyễn Thị Bích Hạnh', '0987654323', '001322996138'),
('KH372', N'Phan Văn Tuấn', '0912837465', '031220007618'),
('KH373', N'Trần Thị Lệ Hằng', '0909456723', '048221006435'),
('KH374', N'Ngô Văn Hùng', '0912345673', '001320007789'),
('KH375', N'Phạm Thị Kim Chi', '0978945612', '079223007274'),
('KH376', N'Lê Hữu Dũng', '0967893421', '001321007350'),
('KH377', N'Trần Thị Mỹ Duyên', '0912837453', '031222007681'),
('KH378', N'Nguyễn Văn Khang', '0938123498', '048223007927'),
('KH379', N'Phạm Minh Hoàng', '0909872346', '001319997519'),
('KH380', N'Lê Thị Như Quỳnh', '0909238474', '001322997241'),
('KH381', N'Trần Hữu Nghĩa', '0912839478', '031223006482'),
('KH382', N'Ngô Thị Thanh Mai', '0938123454', '048224006653'),
('KH383', N'Phạm Văn Hòa', '0909874564', '001321007132'),
('KH384', N'Nguyễn Thị Hồng Nhung', '0978934564', '001319997381'),
('KH385', N'Trần Quốc Trung', '0912983745', '079225007703'),
('KH386', N'Đặng Văn Lộc', '0987654315', '048222007578'),
('KH387', N'Lê Thị Minh Tuyết', '0909238465', '001320007804'),
('KH388', N'Nguyễn Hữu Tấn', '0938456729', '001319007159'),
('KH389', N'Phạm Thị Bảo Yến', '0912837451', '001322007476'),
('KH390', N'Ngô Văn Thành', '0909874568', '048223006825'),
('KH391', N'Trần Thị Mai Anh', '0967893424', '001320007243'),
('KH392', N'Phạm Quốc Khánh', '0912839477', '031222006639'),
('KH393', N'Lê Hữu Phúc', '0928734567', '001321007912'),
('KH394', N'Trần Thị Hồng Vân', '0909345782', '079223006308'),
('KH395', N'Nguyễn Văn Hưng', '0938123497', '048224006783'),
('KH396', N'Đỗ Thị Mỹ Linh', '0978945633', '001319997527'),
('KH397', N'Trần Hữu Đạt', '0987123498', '001320007167'),
('KH398', N'Phan Thị Xuân', '0912837452', '031221007391'),
('KH399', N'Nguyễn Thị Minh Thư', '0909872349', '001322007710'),
('KH400', N'Lê Văn Duy', '0967893422', '048222006252'),
('KH401', N'Phạm Thị Quỳnh Hoa', '0912839470', '001321007630'),
('KH402', N'Ngô Hữu Phát', '0909238470', '031223006853'),
('KH403', N'Trần Thị Thuỳ Trang', '0938123452', '001320007407'),
('KH404', N'Nguyễn Minh Đạt', '0978934565', '001319996149'),
('KH405', N'Lê Thị Hồng Diễm', '0928734561', '048221007698'),
('KH406', N'Phan Văn Hào', '0912345674', '001322007384'),
('KH407', N'Trần Thị Cẩm Nhung', '0909345780', '079225007594'),
('KH408', N'Nguyễn Hữu Tường', '0987654324', '001319006217');
GO

-- 4. GA TÀU
INSERT INTO GaTau (maGa, tenGa, diaChi, soDienThoai) VALUES
('GA-001', N'An Hòa', N'Lý Thái Tổ, Phường An Hòa, Thành phố Huế, Tỉnh Thừa Thiên Huế', '0987654321'),
('GA-002', N'Ấm Thượng', N'Khu 8, Thị trấn Hạ Hòa, Huyện Hạ Hòa, Tỉnh Phú Thọ', '0912345678'),
('GA-003', N'Bảo Sơn', N'Xã Bảo Sơn, Huyện Lục Nam, Tỉnh Bắc Giang', '0978123456'),
('GA-004', N'Bắc Thủy', N'Quốc Lộ 1 Cũ, Xã Bắc Thủy, Huyện Chi Lăng, Tỉnh Lạng Sơn', '0901234563'),
('GA-005', N'Bàn Cờ', N'T36 Khu 10 – Quang Trung, Thị Xã Uông Bí, Tỉnh Quảng Ninh', '0965432187'),
('GA-006', N'Bắc Giang', N'Xương Giang, Phường Trần Phú, Thành phố Bắc Giang, Tỉnh Bắc Giang', '0938765432'),
('GA-007', N'Bảo Hà', N'Xã Bảo Hà, Huyện Bảo Yên, Tỉnh Lào Cai', '0923456789'),
('GA-008', N'Biên Hòa', N'Đường Hưng Đạo Vương, Phường Trung Dũng, Thành phố Biên Hòa, Tỉnh Đồng Nai', '0911987654'),
('GA-009', N'Bắc Lệ', N'Thôn Bắc Lệ, Xã Tân Thành, Huyện Hữu Lũng, Tỉnh Lạng Sơn', '0943210987'),
('GA-010', N'Bắc Ninh', N'Phường Ninh Xá, Thành phố Bắc Ninh, Tỉnh Bắc Ninh', '0954321098'),
('GA-011', N'Bồng Sơn', N'Quốc lộ 1, Khu phố 1, Phường Bồng Sơn, Thị xã Hoài Nhơn, Tỉnh Bình Định', '0932109876'),
('GA-012', N'Bỉm Sơn', N'Thị xã Bỉm Sơn, Tỉnh Thanh Hóa', '0921098765'),
('GA-013', N'Bản Thí', N'Xã Vân Thủy, Huyện Chi Lăng, Tỉnh Lạng Sơn', '0913456789'),
('GA-014', N'Chí Chủ', N'Số 312, Xã Chí Tiên, Huyện Thanh Ba, Tỉnh Phú Thọ', '0976543210'),
('GA-015', N'Cẩm Giàng', N'ĐT388, Xã Kim Giang, Huyện Cẩm Giàng, Tỉnh Hải Dương', '0986543212'),
('GA-016', N'Cầu Giát', N'Xã Quỳnh Giang, Huyện Quỳnh Lưu, Tỉnh Nghệ An', '0967891234'),
('GA-017', N'Cầu Hai', N'Xã Lộc Trì, Huyện Phú Lộc, Tỉnh Thừa Thiên Huế', '0953217890'),
('GA-018', N'Chi Lăng', N'Xã Chi Lăng, Huyện Chi Lăng, Tỉnh Lạng Sơn', '0905678912'),
('GA-019', N'Chu Lễ', N'Xã Chu Lễ, Huyện Nam Giang, Tỉnh Quảng Nam', '0987123456'),
('GA-020', N'Chí Linh', N'Phường Cộng Hòa, Thành phố Chí Linh', '0906655443'),
('GA-021', N'Cổ Loa', N'Xã Việt Hùng, Huyện Đông Anh, Thành phố Hà Nội', '0912345678'),
('GA-022', N'Cẩm Lý', N'Xã Cẩm Lý, Huyện Lục Nam, Tỉnh Bắc Giang', '0923456789'),
('GA-023', N'Cà Ná', N'Xã Cà Ná, Huyện Thuận Nam, Tỉnh Ninh Thuận', '0934567890'),
('GA-024', N'Cổ Phúc', N'Thị trấn Cổ Phúc, Huyện Trấn Yên, Tỉnh Yên Bái', '0945678901'),
('GA-025', N'Chợ Sy', N'Xã Diễn Kỷ, Huyện Diễn Châu, Tỉnh Nghệ An', '0956789012'),
('GA-026', N'Đông Anh', N'Thị trấn Đông Anh, Huyện Đông Anh, Thành phố Hà Nội', '0967890123'),
('GA-027', N'Đa Phúc', N'Xã Mai Đình, Huyện Sóc Sơn, Thành phố Hà Nội', '0978901234'),
('GA-028', N'Đà Lạt', N'Số 1 Quang Trung, Phường 10, Thành phố Đà Lạt, Tỉnh Lâm Đồng', '0989012345'),
('GA-029', N'Đồng Chuối', N'Xã Phước Hưng, Huyện Tuy Phước, Tỉnh Bình Định', '0990123456'),
('GA-030', N'Đồng Đăng', N'Thị trấn Đồng Đăng, Huyện Cao Lộc, Tỉnh Lạng Sơn', '0901234564'),
('GA-031', N'Đồng Hà', N'2 Lê Thánh Tôn, Phường 1, Thành phố Đông Hà, Tỉnh Quảng Trị', '0912345678'),
('GA-032', N'Đồng Hới', N'Thành phố Đồng Hới, Tỉnh Quảng Bình', '0923456789'),
('GA-033', N'Dĩ An', N'Thị xã Dĩ An, Tỉnh Bình Dương', '0934567890'),
('GA-034', N'Đại Lãnh', N'Xã Đại Lãnh, Huyện Vạn Ninh, Tỉnh Khánh Hòa', '0945678901'),
('GA-035', N'Đồng Lê', N'Huyện Tuyên Hóa, Tỉnh Quảng Bình', '0956789012'),
('GA-036', N'Đồng Mỏ', N'Thị trấn Đồng Mỏ, Huyện Chi Lăng, Tỉnh Lạng Sơn', '0967890123'),
('GA-037', N'Đà Nẵng', N'Thành phố Đà Nẵng', '0978901234'),
('GA-038', N'Đông Triều', N'Hà Lôi Hạ, Thị xã Đông Triều, Tỉnh Quảng Ninh', '0989012345'),
('GA-039', N'Đức Phổ', N'Phổ Ninh, Thị xã Đức Phổ, Tỉnh Quảng Ngãi', '0990123456'),
('GA-040', N'Diên Sanh', N'Huyện Hải Lăng, Tỉnh Quảng Trị', '0901234565'),
('GA-041', N'Đông Tác', N'Phường Phú Thạnh, Thành phố Tuy Hòa, Tỉnh Phú Yên', '0912345678'),
('GA-042', N'Đoan Thượng', N'Xã Đan Thượng, Huyện Hạ Hòa, Tỉnh Phú Thọ', '0923456789'),
('GA-043', N'Diêu Trì', N'Thôn Vân Hội 2, Thị trấn Diêu Trì, Huyện Tuy Phước, Tỉnh Bình Định', '0934567890'),
('GA-044', N'Đức Lạc', N'Huyện Đức Thọ, Tỉnh Hà Tĩnh', '0945678901'),
('GA-045', N'Giáp Bát', N'366 Giải Phóng, Phường Định Công, Quận Hoàng Mai, Thành phố Hà Nội', '0956789012'),
('GA-046', N'Gia Huynh', N'Xã Gia Huynh, Huyện Tánh Linh, Tỉnh Bình Thuận', '0967890123'),
('GA-047', N'Giã', N'Xã Phước Thành, Huyện Tuy Phước, Tỉnh Bình Định', '0978901234'),
('GA-048', N'Gia Lâm', N'Phường Gia Thụy, Quận Long Biên, Thành phố Hà Nội', '0989012345'),
('GA-049', N'Gia Ray', N'Thị trấn Gia Ray, Huyện Xuân Lộc, Tỉnh Đồng Nai', '0912345678'),
('GA-050', N'Hải Dương', N'Phường Quang Trung, Thành phố Hải Dương, Tỉnh Hải Dương', '0923456789'),
('GA-051', N'Hoàn Lão', N'Thị trấn Hoàn Lão, Huyện Bố Trạch, Tỉnh Quảng Bình', '0934567890'),
('GA-052', N'Hạ Long', N'Phường Bãi Cháy, Thành phố Hạ Long, Tỉnh Quảng Ninh', '0945678901'),
('GA-053', N'Hà Nội', N'120 Lê Duẩn, Phường Cửa Nam, Quận Hoàn Kiếm, Thành phố Hà Nội', '0956789012'),
('GA-054', N'Hòa Duyệt', N'Xã Đức Hòa, Huyện Đức Thọ, Tỉnh Hà Tĩnh', '0967890123'),
('GA-055', N'Hải Phòng', N'Số 75 Lương Khánh Thiện, Quận Ngô Quyền, Thành phố Hải Phòng', '0978901234'),
('GA-056', N'Hương Phố', N'Thị trấn Hương Khê, Huyện Hương Khê, Tỉnh Hà Tĩnh', '0912345678'),
('GA-057', N'Hiền Sỹ', N'Xã Phong Sơn, Huyện Phong Điền, Tỉnh Thừa Thiên Huế', '0923456789'),
('GA-058', N'Hà Thanh', N'Xã Nhơn Phong, Thị xã An Nhơn, Tỉnh Bình Định', '0934567890'),
('GA-059', N'Huế', N'02 Bùi Thị Xuân, Phường Phường Đúc, Thành phố Huế, Tỉnh Thừa Thiên Huế', '0945678901'),
('GA-060', N'Kép', N'Thị trấn Kép, Huyện Lạng Giang, Tỉnh Bắc Giang', '0956789012'),
('GA-061', N'Kim Liên', N'Phường Hòa Hiệp Bắc, Quận Liên Chiểu, Thành phố Đà Nẵng', '0967890123'),
('GA-062', N'Kim Lũ', N'Xã Kim Hóa, Huyện Tuyên Hóa, Tỉnh Quảng Bình', '0912345678'),
('GA-063', N'Lạc Giao', N'Phường Tự An, Thành phố Buôn Ma Thuột, Tỉnh Đắk Lắk', '0923456789'),
('GA-064', N'La Khê', N'Phường La Khê, Quận Hà Đông, Thành phố Hà Nội', '0934567890'),
('GA-065', N'Lạc Sơn', N'Xã Châu Thái, Huyện Quỳ Hợp, Tỉnh Nghệ An', '0945678901'),
('GA-066', N'Long Biên', N'Phường Đồng Xuân, Quận Hoàn Kiếm, Thành phố Hà Nội', '0956789012'),
('GA-067', N'Lào Cai', N'Phường Phố Mới, Thành phố Lào Cai, Tỉnh Lào Cai', '0967890123'),
('GA-068', N'Lăng Cô', N'Thị trấn Lăng Cô, Huyện Phú Lộc, Tỉnh Thừa Thiên Huế', '0978901234'),
('GA-069', N'Long Đại', N'Xã Hiền Ninh, Huyện Quảng Ninh, Tỉnh Quảng Bình', '0989012345'),
('GA-070', N'Lệ Sơn', N'Xã Văn Hóa, Huyện Tuyên Hóa, Tỉnh Quảng Bình', '0990123456'),
('GA-071', N'Lâm Giang', N'Xã Lâm Giang, Huyện Văn Yên, Tỉnh Yên Bái', '0901234566'),
('GA-072', N'La Hai', N'Thị trấn La Hai, Huyện Đồng Xuân, Tỉnh Phú Yên', '0912345678'),
('GA-073', N'Lim', N'Thị trấn Lim, Huyện Tiên Du, Tỉnh Bắc Ninh', '0923456789'),
('GA-074', N'Lang Khay', N'Xã Sơn Hà, Huyện Hữu Lũng, Tỉnh Lạng Sơn', '0934567890'),
('GA-075', N'Long Khánh', N'Phường Xuân An, Thành phố Long Khánh, Tỉnh Đồng Nai', '0945678901'),
('GA-076', N'Lệ Kỳ', N'Xã Vĩnh Ninh, Huyện Quảng Ninh, Tỉnh Quảng Bình', '0956789012'),
('GA-077', N'Lan Mẫu', N'Xã Lan Mẫu, Huyện Lục Nam, Tỉnh Bắc Giang', '0967890123'),
('GA-078', N'Lạng Sơn', N'Phường Chi Lăng, Thành phố Lạng Sơn, Tỉnh Lạng Sơn', '0978901234'),
('GA-079', N'Lang Thíp', N'Xã Lang Thíp, Huyện Văn Yên, Tỉnh Yên Bái', '0989012345'),
('GA-080', N'Lương Sơn', N'Thị trấn Lương Sơn, Huyện Lương Sơn, Tỉnh Hòa Bình', '0990123456'),
('GA-081', N'Lưu Xá', N'Phường Phú Xá, Thành phố Thái Nguyên, Tỉnh Thái Nguyên', '0901234567'),
('GA-082', N'Mậu A', N'Thị trấn Mậu A, Huyện Văn Yên, Tỉnh Yên Bái', '0912345678'),
('GA-083', N'Mạo Khê', N'Phường Mạo Khê, Thị xã Đông Triều, Tỉnh Quảng Ninh', '0923456789'),
('GA-084', N'Minh Cầm', N'Xã Xuân Ninh, Huyện Quảng Ninh, Tỉnh Quảng Bình', '0934567890'),
('GA-085', N'Mỹ Chánh', N'Xã Hải Chánh, Huyện Hải Lăng, Tỉnh Quảng Trị', '0945678901'),
('GA-086', N'Mỹ Đức', N'Xã Bình Mỹ, Huyện Bình Sơn, Tỉnh Quảng Ngãi', '0956789012'),
('GA-087', N'Minh Khôi', N'Xã Minh Khôi, Huyện Nông Cống, Tỉnh Thanh Hóa', '0967890123'),
('GA-088', N'Ma Lâm', N'Thị trấn Ma Lâm, Huyện Hàm Thuận Bắc, Tỉnh Bình Thuận', '0978901234'),
('GA-089', N'Minh Lệ', N'Xã Quảng Minh, Thị xã Ba Đồn, Tỉnh Quảng Bình', '0989012345'),
('GA-090', N'Bình Thuận', N'Xã Mương Mán, Huyện Hàm Thuận Nam, Tỉnh Bình Thuận', '0990123456'),
('GA-091', N'Mỹ Trạch', N'Xã Mỹ Trạch, Huyện Bố Trạch, Tỉnh Quảng Bình', '0901234568'),
('GA-092', N'Ngã Ba', N'Xã Hưng Thủy, Huyện Lệ Thủy, Tỉnh Quảng Bình', '0912345678'),
('GA-093', N'Ninh Bình', N'Phường Nam Thành, Thành phố Ninh Bình, Tỉnh Ninh Bình', '0923456789'),
('GA-094', N'Nam Định', N'Phường Quang Trung, Thành phố Nam Định, Tỉnh Nam Định', '0934567890'),
('GA-095', N'Ninh Hòa', N'Phường Ninh Hiệp, Thị xã Ninh Hòa, Tỉnh Khánh Hòa', '0945678901'),
('GA-096', N'Ngòi Hóp', N'Xã Văn Phú, Thành phố Yên Bái, Tỉnh Yên Bái', '0956789012'),
('GA-097', N'Nam Khê', N'Phường Nam Khê, Thành phố Uông Bí, Tỉnh Quảng Ninh', '0967890123'),
('GA-098', N'Ngọc Lâm', N'Xã Ngọc Lâm, Huyện Mỹ Hào, Tỉnh Hưng Yên', '0978901234'),
('GA-099', N'Ngân Sơn', N'Thị trấn Nà Phặc, Huyện Ngân Sơn, Tỉnh Bắc Kạn', '0989012345'),
('GA-100', N'Núi Thành', N'Thị trấn Núi Thành, Huyện Núi Thành, Tỉnh Quảng Nam', '0990123456'),
('GA-101', N'Nha Trang', N'17 Thái Nguyên, Phường Phước Tân, Thành phố Nha Trang, Tỉnh Khánh Hòa', '0901234569'),
('GA-102', N'Phú Cang', N'Xã Vạn Phú, Huyện Vạn Ninh, Tỉnh Khánh Hòa', '0912345678'),
('GA-103', N'Phú Diễn', N'Phường Phú Diễn, Quận Bắc Từ Liêm, Thành phố Hà Nội', '0923456789'),
('GA-104', N'Phủ Đức', N'Xã Phước Thạnh, Huyện Củ Chi, Thành phố Hồ Chí Minh', '0934567890'),
('GA-105', N'Phú Hiệp', N'Xã Hòa Hiệp Trung, Thị xã Đông Hòa, Tỉnh Phú Yên', '0945678901'),
('GA-106', N'Phú Hòa', N'Xã Phú Hòa, Huyện Định Quán, Tỉnh Đồng Nai', '0956789012'),
('GA-107', N'Phò Trạch', N'Xã Phong Bình, Huyện Phong Điền, Tỉnh Thừa Thiên Huế', '0967890123'),
('GA-108', N'Phổ Yên', N'Phường Ba Hàng, Thị xã Phổ Yên, Tỉnh Thái Nguyên', '0978901234'),
('GA-109', N'Phước Lãnh', N'Xã Phước Lãnh, Huyện Tuy Phước, Tỉnh Bình Định', '0989012345'),
('GA-110', N'Phố Lu', N'Thị trấn Phố Lu, Huyện Bảo Thắng, Tỉnh Lào Cai', '0990123456'),
('GA-111', N'Phủ Lý', N'Phường Minh Khai, Thành phố Phủ Lý, Tỉnh Hà Nam', '0901234510'),
('GA-112', N'Phường Mỗ', N'Phường Mỗ Lao, Quận Hà Đông, Thành phố Hà Nội', '0912345678'),
('GA-113', N'Phú Thái', N'Thị trấn Phú Thái, Huyện Kim Thành, Tỉnh Hải Dương', '0923456789'),
('GA-114', N'Phan Thiết', N'Phường Phong Nẫm, Thành phố Phan Thiết, Tỉnh Bình Thuận', '0934567890'),
('GA-115', N'Phú Thọ', N'Thị trấn Phú Thọ, Huyện Phù Ninh, Tỉnh Phú Thọ', '0945678901'),
('GA-116', N'Phố Tráng', N'Xã Yên Trạch, Huyện Phú Lương, Tỉnh Thái Nguyên', '0956789012'),
('GA-117', N'Phúc Tự', N'Xã Phúc Tự, Huyện Tân Yên, Tỉnh Bắc Giang', '0967890123'),
('GA-118', N'Phúc Trạch', N'Xã Phúc Trạch, Huyện Bố Trạch, Tỉnh Quảng Bình', '0978901234'),
('GA-119', N'Phố Vị', N'Thị trấn Phố Vị, Huyện Hữu Lũng, Tỉnh Lạng Sơn', '0989012345'),
('GA-120', N'Phúc Yên', N'Phường Phúc Thắng, Thành phố Phúc Yên, Tỉnh Vĩnh Phúc', '0990123456'),
('GA-121', N'Quảng Ngãi', N'Phường Quảng Phú, Thành phố Quảng Ngãi, Tỉnh Quảng Ngãi', '0901234511'),
('GA-122', N'Quán Hành', N'Xã Nghi Trung, Huyện Nghi Lộc, Tỉnh Nghệ An', '0934567890'),
('GA-123', N'Quán Toan', N'Phường Quán Toan, Quận Hồng Bàng, Thành phố Hải Phòng', '0945678901'),
('GA-124', N'Quy Nhơn', N'Phường Trần Hưng Đạo, Thành phố Quy Nhơn, Tỉnh Bình Định', '0912345678'),
('GA-125', N'Quảng Trị', N'Phường 1, Thị xã Quảng Trị, Tỉnh Quảng Trị', '0923456789'),
('GA-126', N'Quán Triều', N'Phường Quán Triều, Thành phố Thái Nguyên, Tỉnh Thái Nguyên', '0934567890'),
('GA-127', N'Sài Gòn', N'01 Nguyễn Thông, Phường 9, Quận 3, Thành phố Hồ Chí Minh', '0945678901'),
('GA-128', N'Sen Hồ', N'Xã Hương Mạc, Thị xã Từ Sơn, Tỉnh Bắc Ninh', '0956789012'),
('GA-129', N'Suối Kiết', N'Xã Suối Kiết, Huyện Tánh Linh, Tỉnh Bình Thuận', '0967890123'),
('GA-130', N'Sa Lung', N'Xã Vĩnh Long, Huyện Vĩnh Linh, Tỉnh Quảng Trị', '0978901234'),
('GA-131', N'Sông Mao', N'Xã Phan Tiến, Huyện Bắc Bình, Tỉnh Bình Thuận', '0989012345'),
('GA-132', N'Sông Hóa', N'Xã Quỳnh Vinh, Thị xã Hoàng Mai, Tỉnh Nghệ An', '0990123456'),
('GA-133', N'Sóng Thần', N'Phường Dĩ An, Thành phố Dĩ An, Tỉnh Bình Dương', '0901234512'),
('GA-134', N'Tiên An', N'Xã Tiên An, Huyện Tiên Phước, Tỉnh Quảng Nam', '0912345678'),
('GA-135', N'Tân Ấp', N'Xã Hương Hóa, Huyện Tuyên Hóa, Tỉnh Quảng Bình', '0923456789'),
('GA-136', N'Tu Bông', N'Xã Vạn Phước, Huyện Vạn Ninh, Tỉnh Khánh Hòa', '0934567890'),
('GA-137', N'Thị Cầu', N'Phường Thị Cầu, Thành phố Bắc Ninh, Tỉnh Bắc Ninh', '0945678901'),
('GA-138', N'Tháp Chàm', N'Phường Đô Vinh, Thành phố Phan Rang-Tháp Chàm, Tỉnh Ninh Thuận', '0956789012'),
('GA-139', N'Trung Giã', N'Xã Trung Giã, Huyện Sóc Sơn, Thành phố Hà Nội', '0967890123'),
('GA-140', N'Tuy Hòa', N'Phường 9, Thành phố Tuy Hòa, Tỉnh Phú Yên', '0978901234'),
('GA-141', N'Thượng Lý', N'Phường Sở Dầu, Quận Hồng Bàng, Thành phố Hải Phòng', '0989012345'),
('GA-142', N'Thanh Hóa', N'Phường Đông Thọ, Thành phố Thanh Hóa, Tỉnh Thanh Hóa', '0990123456'),
('GA-143', N'Trái Hút', N'Xã Yên Hưng, Huyện Văn Yên, Tỉnh Yên Bái', '0901234513'),
('GA-144', N'Tiên Kiên', N'Xã Tiên Kiên, Huyện Lâm Thao, Tỉnh Phú Thọ', '0912345678'),
('GA-145', N'Trà Kiệu', N'Xã Duy Sơn, Huyện Duy Xuyên, Tỉnh Quảng Nam', '0923456789'),
('GA-146', N'Tam Kỳ', N'Phường An Sơn, Thành phố Tam Kỳ, Tỉnh Quảng Nam', '0934567890'),
('GA-147', N'Thượng Lâm', N'Xã Thượng Lâm, Huyện Mỹ Đức, Thành phố Hà Nội', '0945678901'),
('GA-148', N'Thọ Lộc', N'Xã Thọ Lộc, Huyện Phúc Thọ, Thành phố Hà Nội', '0956789012'),
('GA-149', N'Thạch Lỗi', N'Xã Thạch Lỗi, Huyện Mê Linh, Thành phố Hà Nội', '0923456789'),
('GA-150', N'Thanh Luyện', N'Xã Thanh Luyện, Huyện Thanh Liêm, Tỉnh Hà Nam', '0934567890'),
('GA-151', N'Trại Mát', N'Phường 11, Thành phố Đà Lạt, Tỉnh Lâm Đồng', '0945678901'),
('GA-152', N'Thái Nguyên', N'Phường Quang Trung, Thành phố Thái Nguyên, Tỉnh Thái Nguyên', '0956789012'),
('GA-153', N'Thái Niên', N'Xã Thái Niên, Huyện Bảo Thắng, Tỉnh Lào Cai', '0967890123'),
('GA-154', N'Từ Sơn', N'Phường Đông Ngàn, Thị xã Từ Sơn, Tỉnh Bắc Ninh', '0978901234'),
('GA-155', N'Thái Văn', N'Xã Thái Văn, Huyện Thái Thụy, Tỉnh Thái Bình', '0989012345'),
('GA-156', N'Uông Bí', N'Phường Quang Trung, Thành phố Uông Bí, Tỉnh Quảng Ninh', '0990123456'),
('GA-157', N'Vân Canh', N'Xã Vân Canh, Huyện Hoài Đức, Thành phố Hà Nội', '0901234514'),
('GA-158', N'Văn Điển', N'Thị trấn Văn Điển, Huyện Thanh Trì, Thành phố Hà Nội', '0912345678'),
('GA-159', N'Vũ Ẻn', N'Xã Vũ Ẻn, Huyện Thanh Ba, Tỉnh Phú Thọ', '0923456789'),
('GA-160', N'Vinh', N'Phường Quán Bàu, Thành phố Vinh, Tỉnh Nghệ An', '0934567890'),
('GA-161', N'Văn Phú', N'Xã Văn Phú, Huyện Thường Tín, Thành phố Hà Nội', '0945678901'),
('GA-162', N'Vĩnh Thủy', N'Xã Vĩnh Thủy, Huyện Vĩnh Linh, Tỉnh Quảng Trị', '0956789012'),
('GA-163', N'Việt Trì', N'Phường Tiên Cát, Thành phố Việt Trì, Tỉnh Phú Thọ', '0967890123'),
('GA-164', N'Văn Xá', N'Xã Văn Xá, Huyện Kim Bảng, Tỉnh Hà Nam', '0978901234'),
('GA-165', N'Voi Xô', N'Xã Voi Xô, Huyện Sơn Động, Tỉnh Bắc Giang', '0989012345'),
('GA-166', N'Vĩnh Yên', N'Phường Liên Bảo, Thành phố Vĩnh Yên, Tỉnh Vĩnh Phúc', '0990123456'),
('GA-167', N'Yên Bái', N'Phường Yên Ninh, Thành phố Yên Bái, Tỉnh Yên Bái', '0901234515');

-- 5. TÀU
INSERT INTO Tau (maTau, tenTau, soToaTau, ngayHoatDong, trangThai) VALUES
('TAU-001', N'Tàu Thống Nhất SE1', 8, '2015-01-15', 1),
('TAU-002', N'Tàu Thống Nhất SE2', 9, '2015-03-20', 1),
('TAU-003', N'Tàu Thống Nhất SE3', 8, '2015-06-10', 1),
('TAU-004', N'Tàu Thống Nhất SE4', 9, '2015-09-05', 1),
('TAU-005', N'Tàu Thống Nhất SE5', 8, '2016-02-18', 1),
('TAU-006', N'Tàu Thống Nhất SE6', 9, '2016-05-22', 1),
('TAU-007', N'Tàu Thống Nhất SE7', 8, '2016-08-14', 1),
('TAU-008', N'Tàu Thống Nhất SE8', 9, '2016-11-30', 1),
('TAU-009', N'Tàu TN1', 8, '2017-03-12', 1),
('TAU-010', N'Tàu TN2', 9, '2017-06-25', 1),
('TAU-011', N'Tàu Sài Gòn – Nha Trang SNT1', 9, '2018-01-10', 1),
('TAU-012', N'Tàu Sài Gòn – Nha Trang SNT2', 9, '2018-04-15', 1),
('TAU-013', N'Tàu Hà Nội – Lào Cai SP1', 9, '2018-07-20', 1),
('TAU-014', N'Tàu Hà Nội – Lào Cai SP2', 9, '2018-10-08', 1),
('TAU-015', N'Tàu Hà Nội – Vinh NA1', 9, '2019-01-22', 1),
('TAU-016', N'Tàu Hà Nội – Vinh NA2', 9, '2019-04-16', 1),
('TAU-017', N'Tàu Thống Nhất SE9', 9, '2019-07-30', 1),
('TAU-018', N'Tàu Thống Nhất SE10', 9, '2019-10-12', 1),
('TAU-019', N'Tàu Thống Nhất SE11', 9, '2020-02-05', 1),
('TAU-020', N'Tàu Thống Nhất SE12', 9, '2020-05-18', 1);
GO

-- 6. TOA TÀU 
INSERT INTO ToaTau (maToaTau, soKhoangTau, soHieuToa, loaiToa, maTau) VALUES
-- TAU-001
('TT-001-01', 0, 1, 'NGOI_MEM', 'TAU-001'),
('TT-001-02', 0, 2, 'NGOI_MEM', 'TAU-001'),
('TT-001-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-001'),
('TT-001-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-001'),
('TT-001-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-001'),
('TT-001-06', 7, 6, 'GIUONG_NAM_KHOANG_4', 'TAU-001'),
('TT-001-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-001'),
('TT-001-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-001'),

-- TAU-002
('TT-002-01', 0, 1, 'NGOI_MEM', 'TAU-002'),
('TT-002-02', 0, 2, 'NGOI_MEM', 'TAU-002'),
('TT-002-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-002'),
('TT-002-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-002'),
('TT-002-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-002'),
('TT-002-06', 7, 6, 'GIUONG_NAM_KHOANG_6', 'TAU-002'),
('TT-002-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-002'),
('TT-002-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-002'),
('TT-002-09', 7, 9, 'GIUONG_NAM_KHOANG_4', 'TAU-002'),

-- TAU-003
('TT-003-01', 0, 1, 'NGOI_MEM', 'TAU-003'),
('TT-003-02', 0, 2, 'NGOI_MEM', 'TAU-003'),
('TT-003-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-003'),
('TT-003-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-003'),
('TT-003-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-003'),
('TT-003-06', 7, 6, 'GIUONG_NAM_KHOANG_4', 'TAU-003'),
('TT-003-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-003'),
('TT-003-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-003'),

-- TAU-004
('TT-004-01', 0, 1, 'NGOI_MEM', 'TAU-004'),
('TT-004-02', 0, 2, 'NGOI_MEM', 'TAU-004'),
('TT-004-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-004'),
('TT-004-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-004'),
('TT-004-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-004'),
('TT-004-06', 7, 6, 'GIUONG_NAM_KHOANG_6', 'TAU-004'),
('TT-004-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-004'),
('TT-004-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-004'),
('TT-004-09', 7, 9, 'GIUONG_NAM_KHOANG_4', 'TAU-004'),

-- TAU-005
('TT-005-01', 0, 1, 'NGOI_MEM', 'TAU-005'),
('TT-005-02', 0, 2, 'NGOI_MEM', 'TAU-005'),
('TT-005-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-005'),
('TT-005-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-005'),
('TT-005-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-005'),
('TT-005-06', 7, 6, 'GIUONG_NAM_KHOANG_4', 'TAU-005'),
('TT-005-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-005'),
('TT-005-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-005'),

-- TAU-006
('TT-006-01', 0, 1, 'NGOI_MEM', 'TAU-006'),
('TT-006-02', 0, 2, 'NGOI_MEM', 'TAU-006'),
('TT-006-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-006'),
('TT-006-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-006'),
('TT-006-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-006'),
('TT-006-06', 7, 6, 'GIUONG_NAM_KHOANG_6', 'TAU-006'),
('TT-006-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-006'),
('TT-006-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-006'),
('TT-006-09', 7, 9, 'GIUONG_NAM_KHOANG_4', 'TAU-006'),

-- TAU-007
('TT-007-01', 0, 1, 'NGOI_MEM', 'TAU-007'),
('TT-007-02', 0, 2, 'NGOI_MEM', 'TAU-007'),
('TT-007-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-007'),
('TT-007-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-007'),
('TT-007-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-007'),
('TT-007-06', 7, 6, 'GIUONG_NAM_KHOANG_4', 'TAU-007'),
('TT-007-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-007'),
('TT-007-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-007'),

-- TAU-008
('TT-008-01', 0, 1, 'NGOI_MEM', 'TAU-008'),
('TT-008-02', 0, 2, 'NGOI_MEM', 'TAU-008'),
('TT-008-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-008'),
('TT-008-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-008'),
('TT-008-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-008'),
('TT-008-06', 7, 6, 'GIUONG_NAM_KHOANG_6', 'TAU-008'),
('TT-008-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-008'),
('TT-008-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-008'),
('TT-008-09', 7, 9, 'GIUONG_NAM_KHOANG_4', 'TAU-008'),

-- TAU-009
('TT-009-01', 0, 1, 'NGOI_MEM', 'TAU-009'),
('TT-009-02', 0, 2, 'NGOI_MEM', 'TAU-009'),
('TT-009-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-009'),
('TT-009-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-009'),
('TT-009-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-009'),
('TT-009-06', 7, 6, 'GIUONG_NAM_KHOANG_4', 'TAU-009'),
('TT-009-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-009'),
('TT-009-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-009'),

-- TAU-010
('TT-010-01', 0, 1, 'NGOI_MEM', 'TAU-010'),
('TT-010-02', 0, 2, 'NGOI_MEM', 'TAU-010'),
('TT-010-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-010'),
('TT-010-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-010'),
('TT-010-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-010'),
('TT-010-06', 7, 6, 'GIUONG_NAM_KHOANG_6', 'TAU-010'),
('TT-010-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-010'),
('TT-010-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-010'),
('TT-010-09', 7, 9, 'GIUONG_NAM_KHOANG_4', 'TAU-010'),

-- TAU-011
('TT-011-01', 0, 1, 'NGOI_MEM', 'TAU-011'),
('TT-011-02', 0, 2, 'NGOI_MEM', 'TAU-011'),
('TT-011-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-011'),
('TT-011-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-011'),
('TT-011-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-011'),
('TT-011-06', 7, 6, 'GIUONG_NAM_KHOANG_6', 'TAU-011'),
('TT-011-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-011'),
('TT-011-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-011'),
('TT-011-09', 7, 9, 'GIUONG_NAM_KHOANG_4', 'TAU-011'),

-- TAU-012
('TT-012-01', 0, 1, 'NGOI_MEM', 'TAU-012'),
('TT-012-02', 0, 2, 'NGOI_MEM', 'TAU-012'),
('TT-012-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-012'),
('TT-012-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-012'),
('TT-012-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-012'),
('TT-012-06', 7, 6, 'GIUONG_NAM_KHOANG_6', 'TAU-012'),
('TT-012-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-012'),
('TT-012-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-012'),
('TT-012-09', 7, 9, 'GIUONG_NAM_KHOANG_4', 'TAU-012'),

-- TAU-013
('TT-013-01', 0, 1, 'NGOI_MEM', 'TAU-013'),
('TT-013-02', 0, 2, 'NGOI_MEM', 'TAU-013'),
('TT-013-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-013'),
('TT-013-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-013'),
('TT-013-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-013'),
('TT-013-06', 7, 6, 'GIUONG_NAM_KHOANG_6', 'TAU-013'),
('TT-013-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-013'),
('TT-013-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-013'),
('TT-013-09', 7, 9, 'GIUONG_NAM_KHOANG_4', 'TAU-013'),

-- TAU-014
('TT-014-01', 0, 1, 'NGOI_MEM', 'TAU-014'),
('TT-014-02', 0, 2, 'NGOI_MEM', 'TAU-014'),
('TT-014-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-014'),
('TT-014-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-014'),
('TT-014-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-014'),
('TT-014-06', 7, 6, 'GIUONG_NAM_KHOANG_6', 'TAU-014'),
('TT-014-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-014'),
('TT-014-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-014'),
('TT-014-09', 7, 9, 'GIUONG_NAM_KHOANG_4', 'TAU-014'),

-- TAU-015
('TT-015-01', 0, 1, 'NGOI_MEM', 'TAU-015'),
('TT-015-02', 0, 2, 'NGOI_MEM', 'TAU-015'),
('TT-015-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-015'),
('TT-015-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-015'),
('TT-015-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-015'),
('TT-015-06', 7, 6, 'GIUONG_NAM_KHOANG_6', 'TAU-015'),
('TT-015-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-015'),
('TT-015-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-015'),
('TT-015-09', 7, 9, 'GIUONG_NAM_KHOANG_4', 'TAU-015'),

-- TAU-016
('TT-016-01', 0, 1, 'NGOI_MEM', 'TAU-016'),
('TT-016-02', 0, 2, 'NGOI_MEM', 'TAU-016'),
('TT-016-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-016'),
('TT-016-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-016'),
('TT-016-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-016'),
('TT-016-06', 7, 6, 'GIUONG_NAM_KHOANG_6', 'TAU-016'),
('TT-016-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-016'),
('TT-016-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-016'),
('TT-016-09', 7, 9, 'GIUONG_NAM_KHOANG_4', 'TAU-016'),

-- TAU-017
('TT-017-01', 0, 1, 'NGOI_MEM', 'TAU-017'),
('TT-017-02', 0, 2, 'NGOI_MEM', 'TAU-017'),
('TT-017-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-017'),
('TT-017-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-017'),
('TT-017-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-017'),
('TT-017-06', 7, 6, 'GIUONG_NAM_KHOANG_6', 'TAU-017'),
('TT-017-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-017'),
('TT-017-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-017'),
('TT-017-09', 7, 9, 'GIUONG_NAM_KHOANG_4', 'TAU-017'),

-- TAU-018
('TT-018-01', 0, 1, 'NGOI_MEM', 'TAU-018'),
('TT-018-02', 0, 2, 'NGOI_MEM', 'TAU-018'),
('TT-018-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-018'),
('TT-018-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-018'),
('TT-018-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-018'),
('TT-018-06', 7, 6, 'GIUONG_NAM_KHOANG_6', 'TAU-018'),
('TT-018-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-018'),
('TT-018-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-018'),
('TT-018-09', 7, 9, 'GIUONG_NAM_KHOANG_4', 'TAU-018'),

-- TAU-019
('TT-019-01', 0, 1, 'NGOI_MEM', 'TAU-019'),
('TT-019-02', 0, 2, 'NGOI_MEM', 'TAU-019'),
('TT-019-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-019'),
('TT-019-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-019'),
('TT-019-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-019'),
('TT-019-06', 7, 6, 'GIUONG_NAM_KHOANG_6', 'TAU-019'),
('TT-019-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-019'),
('TT-019-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-019'),
('TT-019-09', 7, 9, 'GIUONG_NAM_KHOANG_4', 'TAU-019'),

-- TAU-020
('TT-020-01', 0, 1, 'NGOI_MEM', 'TAU-020'),
('TT-020-02', 0, 2, 'NGOI_MEM', 'TAU-020'),
('TT-020-03', 7, 3, 'GIUONG_NAM_KHOANG_6', 'TAU-020'),
('TT-020-04', 7, 4, 'GIUONG_NAM_KHOANG_6', 'TAU-020'),
('TT-020-05', 7, 5, 'GIUONG_NAM_KHOANG_6', 'TAU-020'),
('TT-020-06', 7, 6, 'GIUONG_NAM_KHOANG_6', 'TAU-020'),
('TT-020-07', 7, 7, 'GIUONG_NAM_KHOANG_4', 'TAU-020'),
('TT-020-08', 7, 8, 'GIUONG_NAM_KHOANG_4', 'TAU-020'),
('TT-020-09', 7, 9, 'GIUONG_NAM_KHOANG_4', 'TAU-020');
GO

-- 7. KHOANG TÀU 
INSERT INTO KhoangTau (maKhoangTau, soHieuKhoang, soGhe, maToaTau) VALUES
('KT-001-01-00', 0, 64, 'TT-001-01'),
('KT-001-02-00', 0, 64, 'TT-001-02'),
-- TAU-001 (Toa 03-08: Giường nằm)
('KT-001-03-01', 1, 6, 'TT-001-03'),
('KT-001-03-02', 2, 6, 'TT-001-03'),
('KT-001-03-03', 3, 6, 'TT-001-03'),
('KT-001-03-04', 4, 6, 'TT-001-03'),
('KT-001-03-05', 5, 6, 'TT-001-03'),
('KT-001-03-06', 6, 6, 'TT-001-03'),
('KT-001-03-07', 7, 6, 'TT-001-03'),
('KT-001-04-01', 1, 6, 'TT-001-04'),
('KT-001-04-02', 2, 6, 'TT-001-04'),
('KT-001-04-03', 3, 6, 'TT-001-04'),
('KT-001-04-04', 4, 6, 'TT-001-04'),
('KT-001-04-05', 5, 6, 'TT-001-04'),
('KT-001-04-06', 6, 6, 'TT-001-04'),
('KT-001-04-07', 7, 6, 'TT-001-04'),
('KT-001-05-01', 1, 6, 'TT-001-05'),
('KT-001-05-02', 2, 6, 'TT-001-05'),
('KT-001-05-03', 3, 6, 'TT-001-05'),
('KT-001-05-04', 4, 6, 'TT-001-05'),
('KT-001-05-05', 5, 6, 'TT-001-05'),
('KT-001-05-06', 6, 6, 'TT-001-05'),
('KT-001-05-07', 7, 6, 'TT-001-05'),
('KT-001-06-01', 1, 4, 'TT-001-06'),
('KT-001-06-02', 2, 4, 'TT-001-06'),
('KT-001-06-03', 3, 4, 'TT-001-06'),
('KT-001-06-04', 4, 4, 'TT-001-06'),
('KT-001-06-05', 5, 4, 'TT-001-06'),
('KT-001-06-06', 6, 4, 'TT-001-06'),
('KT-001-06-07', 7, 4, 'TT-001-06'),
('KT-001-07-01', 1, 4, 'TT-001-07'),
('KT-001-07-02', 2, 4, 'TT-001-07'),
('KT-001-07-03', 3, 4, 'TT-001-07'),
('KT-001-07-04', 4, 4, 'TT-001-07'),
('KT-001-07-05', 5, 4, 'TT-001-07'),
('KT-001-07-06', 6, 4, 'TT-001-07'),
('KT-001-07-07', 7, 4, 'TT-001-07'),
('KT-001-08-01', 1, 4, 'TT-001-08'),
('KT-001-08-02', 2, 4, 'TT-001-08'),
('KT-001-08-03', 3, 4, 'TT-001-08'),
('KT-001-08-04', 4, 4, 'TT-001-08'),
('KT-001-08-05', 5, 4, 'TT-001-08'),
('KT-001-08-06', 6, 4, 'TT-001-08'),
('KT-001-08-07', 7, 4, 'TT-001-08'),


('KT-002-01-00', 0, 64, 'TT-002-01'),
('KT-002-02-00', 0, 64, 'TT-002-02'),
-- TAU-002 (Toa 03-09: Giường nằm)
('KT-002-03-01', 1, 6, 'TT-002-03'),
('KT-002-03-02', 2, 6, 'TT-002-03'),
('KT-002-03-03', 3, 6, 'TT-002-03'),
('KT-002-03-04', 4, 6, 'TT-002-03'),
('KT-002-03-05', 5, 6, 'TT-002-03'),
('KT-002-03-06', 6, 6, 'TT-002-03'),
('KT-002-03-07', 7, 6, 'TT-002-03'),
('KT-002-04-01', 1, 6, 'TT-002-04'),
('KT-002-04-02', 2, 6, 'TT-002-04'),
('KT-002-04-03', 3, 6, 'TT-002-04'),
('KT-002-04-04', 4, 6, 'TT-002-04'),
('KT-002-04-05', 5, 6, 'TT-002-04'),
('KT-002-04-06', 6, 6, 'TT-002-04'),
('KT-002-04-07', 7, 6, 'TT-002-04'),
('KT-002-05-01', 1, 6, 'TT-002-05'),
('KT-002-05-02', 2, 6, 'TT-002-05'),
('KT-002-05-03', 3, 6, 'TT-002-05'),
('KT-002-05-04', 4, 6, 'TT-002-05'),
('KT-002-05-05', 5, 6, 'TT-002-05'),
('KT-002-05-06', 6, 6, 'TT-002-05'),
('KT-002-05-07', 7, 6, 'TT-002-05'),
('KT-002-06-01', 1, 6, 'TT-002-06'),
('KT-002-06-02', 2, 6, 'TT-002-06'),
('KT-002-06-03', 3, 6, 'TT-002-06'),
('KT-002-06-04', 4, 6, 'TT-002-06'),
('KT-002-06-05', 5, 6, 'TT-002-06'),
('KT-002-06-06', 6, 6, 'TT-002-06'),
('KT-002-06-07', 7, 6, 'TT-002-06'),
('KT-002-07-01', 1, 4, 'TT-002-07'),
('KT-002-07-02', 2, 4, 'TT-002-07'),
('KT-002-07-03', 3, 4, 'TT-002-07'),
('KT-002-07-04', 4, 4, 'TT-002-07'),
('KT-002-07-05', 5, 4, 'TT-002-07'),
('KT-002-07-06', 6, 4, 'TT-002-07'),
('KT-002-07-07', 7, 4, 'TT-002-07'),
('KT-002-08-01', 1, 4, 'TT-002-08'),
('KT-002-08-02', 2, 4, 'TT-002-08'),
('KT-002-08-03', 3, 4, 'TT-002-08'),
('KT-002-08-04', 4, 4, 'TT-002-08'),
('KT-002-08-05', 5, 4, 'TT-002-08'),
('KT-002-08-06', 6, 4, 'TT-002-08'),
('KT-002-08-07', 7, 4, 'TT-002-08'),
('KT-002-09-01', 1, 4, 'TT-002-09'),
('KT-002-09-02', 2, 4, 'TT-002-09'),
('KT-002-09-03', 3, 4, 'TT-002-09'),
('KT-002-09-04', 4, 4, 'TT-002-09'),
('KT-002-09-05', 5, 4, 'TT-002-09'),
('KT-002-09-06', 6, 4, 'TT-002-09'),
('KT-002-09-07', 7, 4, 'TT-002-09'),


('KT-003-01-00', 0, 64, 'TT-003-01'),
('KT-003-02-00', 0, 64, 'TT-003-02'),
-- TAU-003 (Toa 03-08: Giường nằm)
('KT-003-03-01', 1, 6, 'TT-003-03'),
('KT-003-03-02', 2, 6, 'TT-003-03'),
('KT-003-03-03', 3, 6, 'TT-003-03'),
('KT-003-03-04', 4, 6, 'TT-003-03'),
('KT-003-03-05', 5, 6, 'TT-003-03'),
('KT-003-03-06', 6, 6, 'TT-003-03'),
('KT-003-03-07', 7, 6, 'TT-003-03'),
('KT-003-04-01', 1, 6, 'TT-003-04'),
('KT-003-04-02', 2, 6, 'TT-003-04'),
('KT-003-04-03', 3, 6, 'TT-003-04'),
('KT-003-04-04', 4, 6, 'TT-003-04'),
('KT-003-04-05', 5, 6, 'TT-003-04'),
('KT-003-04-06', 6, 6, 'TT-003-04'),
('KT-003-04-07', 7, 6, 'TT-003-04'),
('KT-003-05-01', 1, 6, 'TT-003-05'),
('KT-003-05-02', 2, 6, 'TT-003-05'),
('KT-003-05-03', 3, 6, 'TT-003-05'),
('KT-003-05-04', 4, 6, 'TT-003-05'),
('KT-003-05-05', 5, 6, 'TT-003-05'),
('KT-003-05-06', 6, 6, 'TT-003-05'),
('KT-003-05-07', 7, 6, 'TT-003-05'),
('KT-003-06-01', 1, 4, 'TT-003-06'),
('KT-003-06-02', 2, 4, 'TT-003-06'),
('KT-003-06-03', 3, 4, 'TT-003-06'),
('KT-003-06-04', 4, 4, 'TT-003-06'),
('KT-003-06-05', 5, 4, 'TT-003-06'),
('KT-003-06-06', 6, 4, 'TT-003-06'),
('KT-003-06-07', 7, 4, 'TT-003-06'),
('KT-003-07-01', 1, 4, 'TT-003-07'),
('KT-003-07-02', 2, 4, 'TT-003-07'),
('KT-003-07-03', 3, 4, 'TT-003-07'),
('KT-003-07-04', 4, 4, 'TT-003-07'),
('KT-003-07-05', 5, 4, 'TT-003-07'),
('KT-003-07-06', 6, 4, 'TT-003-07'),
('KT-003-07-07', 7, 4, 'TT-003-07'),
('KT-003-08-01', 1, 4, 'TT-003-08'),
('KT-003-08-02', 2, 4, 'TT-003-08'),
('KT-003-08-03', 3, 4, 'TT-003-08'),
('KT-003-08-04', 4, 4, 'TT-003-08'),
('KT-003-08-05', 5, 4, 'TT-003-08'),
('KT-003-08-06', 6, 4, 'TT-003-08'),
('KT-003-08-07', 7, 4, 'TT-003-08'),

('KT-004-01-00', 0, 64, 'TT-004-01'),
('KT-004-02-00', 0, 64, 'TT-004-02'),
-- TAU-004 (Toa 03-09: Giường nằm)
('KT-004-03-01', 1, 6, 'TT-004-03'),
('KT-004-03-02', 2, 6, 'TT-004-03'),
('KT-004-03-03', 3, 6, 'TT-004-03'),
('KT-004-03-04', 4, 6, 'TT-004-03'),
('KT-004-03-05', 5, 6, 'TT-004-03'),
('KT-004-03-06', 6, 6, 'TT-004-03'),
('KT-004-03-07', 7, 6, 'TT-004-03'),
('KT-004-04-01', 1, 6, 'TT-004-04'),
('KT-004-04-02', 2, 6, 'TT-004-04'),
('KT-004-04-03', 3, 6, 'TT-004-04'),
('KT-004-04-04', 4, 6, 'TT-004-04'),
('KT-004-04-05', 5, 6, 'TT-004-04'),
('KT-004-04-06', 6, 6, 'TT-004-04'),
('KT-004-04-07', 7, 6, 'TT-004-04'),
('KT-004-05-01', 1, 6, 'TT-004-05'),
('KT-004-05-02', 2, 6, 'TT-004-05'),
('KT-004-05-03', 3, 6, 'TT-004-05'),
('KT-004-05-04', 4, 6, 'TT-004-05'),
('KT-004-05-05', 5, 6, 'TT-004-05'),
('KT-004-05-06', 6, 6, 'TT-004-05'),
('KT-004-05-07', 7, 6, 'TT-004-05'),
('KT-004-06-01', 1, 6, 'TT-004-06'),
('KT-004-06-02', 2, 6, 'TT-004-06'),
('KT-004-06-03', 3, 6, 'TT-004-06'),
('KT-004-06-04', 4, 6, 'TT-004-06'),
('KT-004-06-05', 5, 6, 'TT-004-06'),
('KT-004-06-06', 6, 6, 'TT-004-06'),
('KT-004-06-07', 7, 6, 'TT-004-06'),
('KT-004-07-01', 1, 4, 'TT-004-07'),
('KT-004-07-02', 2, 4, 'TT-004-07'),
('KT-004-07-03', 3, 4, 'TT-004-07'),
('KT-004-07-04', 4, 4, 'TT-004-07'),
('KT-004-07-05', 5, 4, 'TT-004-07'),
('KT-004-07-06', 6, 4, 'TT-004-07'),
('KT-004-07-07', 7, 4, 'TT-004-07'),
('KT-004-08-01', 1, 4, 'TT-004-08'),
('KT-004-08-02', 2, 4, 'TT-004-08'),
('KT-004-08-03', 3, 4, 'TT-004-08'),
('KT-004-08-04', 4, 4, 'TT-004-08'),
('KT-004-08-05', 5, 4, 'TT-004-08'),
('KT-004-08-06', 6, 4, 'TT-004-08'),
('KT-004-08-07', 7, 4, 'TT-004-08'),
('KT-004-09-01', 1, 4, 'TT-004-09'),
('KT-004-09-02', 2, 4, 'TT-004-09'),
('KT-004-09-03', 3, 4, 'TT-004-09'),
('KT-004-09-04', 4, 4, 'TT-004-09'),
('KT-004-09-05', 5, 4, 'TT-004-09'),
('KT-004-09-06', 6, 4, 'TT-004-09'),
('KT-004-09-07', 7, 4, 'TT-004-09'),

('KT-005-01-00', 0, 64, 'TT-005-01'),
('KT-005-02-00', 0, 64, 'TT-005-02'),
-- TAU-005 (Toa 03-08: Giường nằm)
('KT-005-03-01', 1, 6, 'TT-005-03'),
('KT-005-03-02', 2, 6, 'TT-005-03'),
('KT-005-03-03', 3, 6, 'TT-005-03'),
('KT-005-03-04', 4, 6, 'TT-005-03'),
('KT-005-03-05', 5, 6, 'TT-005-03'),
('KT-005-03-06', 6, 6, 'TT-005-03'),
('KT-005-03-07', 7, 6, 'TT-005-03'),
('KT-005-04-01', 1, 6, 'TT-005-04'),
('KT-005-04-02', 2, 6, 'TT-005-04'),
('KT-005-04-03', 3, 6, 'TT-005-04'),
('KT-005-04-04', 4, 6, 'TT-005-04'),
('KT-005-04-05', 5, 6, 'TT-005-04'),
('KT-005-04-06', 6, 6, 'TT-005-04'),
('KT-005-04-07', 7, 6, 'TT-005-04'),
('KT-005-05-01', 1, 6, 'TT-005-05'),
('KT-005-05-02', 2, 6, 'TT-005-05'),
('KT-005-05-03', 3, 6, 'TT-005-05'),
('KT-005-05-04', 4, 6, 'TT-005-05'),
('KT-005-05-05', 5, 6, 'TT-005-05'),
('KT-005-05-06', 6, 6, 'TT-005-05'),
('KT-005-05-07', 7, 6, 'TT-005-05'),
('KT-005-06-01', 1, 4, 'TT-005-06'),
('KT-005-06-02', 2, 4, 'TT-005-06'),
('KT-005-06-03', 3, 4, 'TT-005-06'),
('KT-005-06-04', 4, 4, 'TT-005-06'),
('KT-005-06-05', 5, 4, 'TT-005-06'),
('KT-005-06-06', 6, 4, 'TT-005-06'),
('KT-005-06-07', 7, 4, 'TT-005-06'),
('KT-005-07-01', 1, 4, 'TT-005-07'),
('KT-005-07-02', 2, 4, 'TT-005-07'),
('KT-005-07-03', 3, 4, 'TT-005-07'),
('KT-005-07-04', 4, 4, 'TT-005-07'),
('KT-005-07-05', 5, 4, 'TT-005-07'),
('KT-005-07-06', 6, 4, 'TT-005-07'),
('KT-005-07-07', 7, 4, 'TT-005-07'),
('KT-005-08-01', 1, 4, 'TT-005-08'),
('KT-005-08-02', 2, 4, 'TT-005-08'),
('KT-005-08-03', 3, 4, 'TT-005-08'),
('KT-005-08-04', 4, 4, 'TT-005-08'),
('KT-005-08-05', 5, 4, 'TT-005-08'),
('KT-005-08-06', 6, 4, 'TT-005-08'),
('KT-005-08-07', 7, 4, 'TT-005-08'),

('KT-006-01-00', 0, 64, 'TT-006-01'),
('KT-006-02-00', 0, 64, 'TT-006-02'),
-- TAU-006 (Toa 03-09: Giường nằm)
('KT-006-03-01', 1, 6, 'TT-006-03'),
('KT-006-03-02', 2, 6, 'TT-006-03'),
('KT-006-03-03', 3, 6, 'TT-006-03'),
('KT-006-03-04', 4, 6, 'TT-006-03'),
('KT-006-03-05', 5, 6, 'TT-006-03'),
('KT-006-03-06', 6, 6, 'TT-006-03'),
('KT-006-03-07', 7, 6, 'TT-006-03'),
('KT-006-04-01', 1, 6, 'TT-006-04'),
('KT-006-04-02', 2, 6, 'TT-006-04'),
('KT-006-04-03', 3, 6, 'TT-006-04'),
('KT-006-04-04', 4, 6, 'TT-006-04'),
('KT-006-04-05', 5, 6, 'TT-006-04'),
('KT-006-04-06', 6, 6, 'TT-006-04'),
('KT-006-04-07', 7, 6, 'TT-006-04'),
('KT-006-05-01', 1, 6, 'TT-006-05'),
('KT-006-05-02', 2, 6, 'TT-006-05'),
('KT-006-05-03', 3, 6, 'TT-006-05'),
('KT-006-05-04', 4, 6, 'TT-006-05'),
('KT-006-05-05', 5, 6, 'TT-006-05'),
('KT-006-05-06', 6, 6, 'TT-006-05'),
('KT-006-05-07', 7, 6, 'TT-006-05'),
('KT-006-06-01', 1, 6, 'TT-006-06'),
('KT-006-06-02', 2, 6, 'TT-006-06'),
('KT-006-06-03', 3, 6, 'TT-006-06'),
('KT-006-06-04', 4, 6, 'TT-006-06'),
('KT-006-06-05', 5, 6, 'TT-006-06'),
('KT-006-06-06', 6, 6, 'TT-006-06'),
('KT-006-06-07', 7, 6, 'TT-006-06'),
('KT-006-07-01', 1, 4, 'TT-006-07'),
('KT-006-07-02', 2, 4, 'TT-006-07'),
('KT-006-07-03', 3, 4, 'TT-006-07'),
('KT-006-07-04', 4, 4, 'TT-006-07'),
('KT-006-07-05', 5, 4, 'TT-006-07'),
('KT-006-07-06', 6, 4, 'TT-006-07'),
('KT-006-07-07', 7, 4, 'TT-006-07'),
('KT-006-08-01', 1, 4, 'TT-006-08'),
('KT-006-08-02', 2, 4, 'TT-006-08'),
('KT-006-08-03', 3, 4, 'TT-006-08'),
('KT-006-08-04', 4, 4, 'TT-006-08'),
('KT-006-08-05', 5, 4, 'TT-006-08'),
('KT-006-08-06', 6, 4, 'TT-006-08'),
('KT-006-08-07', 7, 4, 'TT-006-08'),
('KT-006-09-01', 1, 4, 'TT-006-09'),
('KT-006-09-02', 2, 4, 'TT-006-09'),
('KT-006-09-03', 3, 4, 'TT-006-09'),
('KT-006-09-04', 4, 4, 'TT-006-09'),
('KT-006-09-05', 5, 4, 'TT-006-09'),
('KT-006-09-06', 6, 4, 'TT-006-09'),
('KT-006-09-07', 7, 4, 'TT-006-09'),

('KT-007-01-00', 0, 64, 'TT-007-01'),
('KT-007-02-00', 0, 64, 'TT-007-02'),
-- TAU-007 (Toa 03-08: Giường nằm)
('KT-007-03-01', 1, 6, 'TT-007-03'),
('KT-007-03-02', 2, 6, 'TT-007-03'),
('KT-007-03-03', 3, 6, 'TT-007-03'),
('KT-007-03-04', 4, 6, 'TT-007-03'),
('KT-007-03-05', 5, 6, 'TT-007-03'),
('KT-007-03-06', 6, 6, 'TT-007-03'),
('KT-007-03-07', 7, 6, 'TT-007-03'),
('KT-007-04-01', 1, 6, 'TT-007-04'),
('KT-007-04-02', 2, 6, 'TT-007-04'),
('KT-007-04-03', 3, 6, 'TT-007-04'),
('KT-007-04-04', 4, 6, 'TT-007-04'),
('KT-007-04-05', 5, 6, 'TT-007-04'),
('KT-007-04-06', 6, 6, 'TT-007-04'),
('KT-007-04-07', 7, 6, 'TT-007-04'),
('KT-007-05-01', 1, 6, 'TT-007-05'),
('KT-007-05-02', 2, 6, 'TT-007-05'),
('KT-007-05-03', 3, 6, 'TT-007-05'),
('KT-007-05-04', 4, 6, 'TT-007-05'),
('KT-007-05-05', 5, 6, 'TT-007-05'),
('KT-007-05-06', 6, 6, 'TT-007-05'),
('KT-007-05-07', 7, 6, 'TT-007-05'),
('KT-007-06-01', 1, 4, 'TT-007-06'),
('KT-007-06-02', 2, 4, 'TT-007-06'),
('KT-007-06-03', 3, 4, 'TT-007-06'),
('KT-007-06-04', 4, 4, 'TT-007-06'),
('KT-007-06-05', 5, 4, 'TT-007-06'),
('KT-007-06-06', 6, 4, 'TT-007-06'),
('KT-007-06-07', 7, 4, 'TT-007-06'),
('KT-007-07-01', 1, 4, 'TT-007-07'),
('KT-007-07-02', 2, 4, 'TT-007-07'),
('KT-007-07-03', 3, 4, 'TT-007-07'),
('KT-007-07-04', 4, 4, 'TT-007-07'),
('KT-007-07-05', 5, 4, 'TT-007-07'),
('KT-007-07-06', 6, 4, 'TT-007-07'),
('KT-007-07-07', 7, 4, 'TT-007-07'),
('KT-007-08-01', 1, 4, 'TT-007-08'),
('KT-007-08-02', 2, 4, 'TT-007-08'),
('KT-007-08-03', 3, 4, 'TT-007-08'),
('KT-007-08-04', 4, 4, 'TT-007-08'),
('KT-007-08-05', 5, 4, 'TT-007-08'),
('KT-007-08-06', 6, 4, 'TT-007-08'),
('KT-007-08-07', 7, 4, 'TT-007-08'),

('KT-008-01-00', 0, 64, 'TT-008-01'),
('KT-008-02-00', 0, 64, 'TT-008-02'),
-- TAU-008 (Toa 03-09: Giường nằm)
('KT-008-03-01', 1, 6, 'TT-008-03'),
('KT-008-03-02', 2, 6, 'TT-008-03'),
('KT-008-03-03', 3, 6, 'TT-008-03'),
('KT-008-03-04', 4, 6, 'TT-008-03'),
('KT-008-03-05', 5, 6, 'TT-008-03'),
('KT-008-03-06', 6, 6, 'TT-008-03'),
('KT-008-03-07', 7, 6, 'TT-008-03'),
('KT-008-04-01', 1, 6, 'TT-008-04'),
('KT-008-04-02', 2, 6, 'TT-008-04'),
('KT-008-04-03', 3, 6, 'TT-008-04'),
('KT-008-04-04', 4, 6, 'TT-008-04'),
('KT-008-04-05', 5, 6, 'TT-008-04'),
('KT-008-04-06', 6, 6, 'TT-008-04'),
('KT-008-04-07', 7, 6, 'TT-008-04'),
('KT-008-05-01', 1, 6, 'TT-008-05'),
('KT-008-05-02', 2, 6, 'TT-008-05'),
('KT-008-05-03', 3, 6, 'TT-008-05'),
('KT-008-05-04', 4, 6, 'TT-008-05'),
('KT-008-05-05', 5, 6, 'TT-008-05'),
('KT-008-05-06', 6, 6, 'TT-008-05'),
('KT-008-05-07', 7, 6, 'TT-008-05'),
('KT-008-06-01', 1, 6, 'TT-008-06'),
('KT-008-06-02', 2, 6, 'TT-008-06'),
('KT-008-06-03', 3, 6, 'TT-008-06'),
('KT-008-06-04', 4, 6, 'TT-008-06'),
('KT-008-06-05', 5, 6, 'TT-008-06'),
('KT-008-06-06', 6, 6, 'TT-008-06'),
('KT-008-06-07', 7, 6, 'TT-008-06'),
('KT-008-07-01', 1, 4, 'TT-008-07'),
('KT-008-07-02', 2, 4, 'TT-008-07'),
('KT-008-07-03', 3, 4, 'TT-008-07'),
('KT-008-07-04', 4, 4, 'TT-008-07'),
('KT-008-07-05', 5, 4, 'TT-008-07'),
('KT-008-07-06', 6, 4, 'TT-008-07'),
('KT-008-07-07', 7, 4, 'TT-008-07'),
('KT-008-08-01', 1, 4, 'TT-008-08'),
('KT-008-08-02', 2, 4, 'TT-008-08'),
('KT-008-08-03', 3, 4, 'TT-008-08'),
('KT-008-08-04', 4, 4, 'TT-008-08'),
('KT-008-08-05', 5, 4, 'TT-008-08'),
('KT-008-08-06', 6, 4, 'TT-008-08'),
('KT-008-08-07', 7, 4, 'TT-008-08'),
('KT-008-09-01', 1, 4, 'TT-008-09'),
('KT-008-09-02', 2, 4, 'TT-008-09'),
('KT-008-09-03', 3, 4, 'TT-008-09'),
('KT-008-09-04', 4, 4, 'TT-008-09'),
('KT-008-09-05', 5, 4, 'TT-008-09'),
('KT-008-09-06', 6, 4, 'TT-008-09'),
('KT-008-09-07', 7, 4, 'TT-008-09'),

('KT-009-01-00', 0, 64, 'TT-009-01'),
('KT-009-02-00', 0, 64, 'TT-009-02'),
-- TAU-009 (Toa 03-08: Giường nằm)
('KT-009-03-01', 1, 6, 'TT-009-03'),
('KT-009-03-02', 2, 6, 'TT-009-03'),
('KT-009-03-03', 3, 6, 'TT-009-03'),
('KT-009-03-04', 4, 6, 'TT-009-03'),
('KT-009-03-05', 5, 6, 'TT-009-03'),
('KT-009-03-06', 6, 6, 'TT-009-03'),
('KT-009-03-07', 7, 6, 'TT-009-03'),
('KT-009-04-01', 1, 6, 'TT-009-04'),
('KT-009-04-02', 2, 6, 'TT-009-04'),
('KT-009-04-03', 3, 6, 'TT-009-04'),
('KT-009-04-04', 4, 6, 'TT-009-04'),
('KT-009-04-05', 5, 6, 'TT-009-04'),
('KT-009-04-06', 6, 6, 'TT-009-04'),
('KT-009-04-07', 7, 6, 'TT-009-04'),
('KT-009-05-01', 1, 6, 'TT-009-05'),
('KT-009-05-02', 2, 6, 'TT-009-05'),
('KT-009-05-03', 3, 6, 'TT-009-05'),
('KT-009-05-04', 4, 6, 'TT-009-05'),
('KT-009-05-05', 5, 6, 'TT-009-05'),
('KT-009-05-06', 6, 6, 'TT-009-05'),
('KT-009-05-07', 7, 6, 'TT-009-05'),
('KT-009-06-01', 1, 4, 'TT-009-06'),
('KT-009-06-02', 2, 4, 'TT-009-06'),
('KT-009-06-03', 3, 4, 'TT-009-06'),
('KT-009-06-04', 4, 4, 'TT-009-06'),
('KT-009-06-05', 5, 4, 'TT-009-06'),
('KT-009-06-06', 6, 4, 'TT-009-06'),
('KT-009-06-07', 7, 4, 'TT-009-06'),
('KT-009-07-01', 1, 4, 'TT-009-07'),
('KT-009-07-02', 2, 4, 'TT-009-07'),
('KT-009-07-03', 3, 4, 'TT-009-07'),
('KT-009-07-04', 4, 4, 'TT-009-07'),
('KT-009-07-05', 5, 4, 'TT-009-07'),
('KT-009-07-06', 6, 4, 'TT-009-07'),
('KT-009-07-07', 7, 4, 'TT-009-07'),
('KT-009-08-01', 1, 4, 'TT-009-08'),
('KT-009-08-02', 2, 4, 'TT-009-08'),
('KT-009-08-03', 3, 4, 'TT-009-08'),
('KT-009-08-04', 4, 4, 'TT-009-08'),
('KT-009-08-05', 5, 4, 'TT-009-08'),
('KT-009-08-06', 6, 4, 'TT-009-08'),
('KT-009-08-07', 7, 4, 'TT-009-08'),

('KT-010-01-00', 0, 64, 'TT-010-01'),
('KT-010-02-00', 0, 64, 'TT-010-02'),
-- TAU-010 (Toa 03-09: Giường nằm)
('KT-010-03-01', 1, 6, 'TT-010-03'),
('KT-010-03-02', 2, 6, 'TT-010-03'),
('KT-010-03-03', 3, 6, 'TT-010-03'),
('KT-010-03-04', 4, 6, 'TT-010-03'),
('KT-010-03-05', 5, 6, 'TT-010-03'),
('KT-010-03-06', 6, 6, 'TT-010-03'),
('KT-010-03-07', 7, 6, 'TT-010-03'),
('KT-010-04-01', 1, 6, 'TT-010-04'),
('KT-010-04-02', 2, 6, 'TT-010-04'),
('KT-010-04-03', 3, 6, 'TT-010-04'),
('KT-010-04-04', 4, 6, 'TT-010-04'),
('KT-010-04-05', 5, 6, 'TT-010-04'),
('KT-010-04-06', 6, 6, 'TT-010-04'),
('KT-010-04-07', 7, 6, 'TT-010-04'),
('KT-010-05-01', 1, 6, 'TT-010-05'),
('KT-010-05-02', 2, 6, 'TT-010-05'),
('KT-010-05-03', 3, 6, 'TT-010-05'),
('KT-010-05-04', 4, 6, 'TT-010-05'),
('KT-010-05-05', 5, 6, 'TT-010-05'),
('KT-010-05-06', 6, 6, 'TT-010-05'),
('KT-010-05-07', 7, 6, 'TT-010-05'),
('KT-010-06-01', 1, 6, 'TT-010-06'),
('KT-010-06-02', 2, 6, 'TT-010-06'),
('KT-010-06-03', 3, 6, 'TT-010-06'),
('KT-010-06-04', 4, 6, 'TT-010-06'),
('KT-010-06-05', 5, 6, 'TT-010-06'),
('KT-010-06-06', 6, 6, 'TT-010-06'),
('KT-010-06-07', 7, 6, 'TT-010-06'),
('KT-010-07-01', 1, 4, 'TT-010-07'),
('KT-010-07-02', 2, 4, 'TT-010-07'),
('KT-010-07-03', 3, 4, 'TT-010-07'),
('KT-010-07-04', 4, 4, 'TT-010-07'),
('KT-010-07-05', 5, 4, 'TT-010-07'),
('KT-010-07-06', 6, 4, 'TT-010-07'),
('KT-010-07-07', 7, 4, 'TT-010-07'),
('KT-010-08-01', 1, 4, 'TT-010-08'),
('KT-010-08-02', 2, 4, 'TT-010-08'),
('KT-010-08-03', 3, 4, 'TT-010-08'),
('KT-010-08-04', 4, 4, 'TT-010-08'),
('KT-010-08-05', 5, 4, 'TT-010-08'),
('KT-010-08-06', 6, 4, 'TT-010-08'),
('KT-010-08-07', 7, 4, 'TT-010-08'),
('KT-010-09-01', 1, 4, 'TT-010-09'),
('KT-010-09-02', 2, 4, 'TT-010-09'),
('KT-010-09-03', 3, 4, 'TT-010-09'),
('KT-010-09-04', 4, 4, 'TT-010-09'),
('KT-010-09-05', 5, 4, 'TT-010-09'),
('KT-010-09-06', 6, 4, 'TT-010-09'),
('KT-010-09-07', 7, 4, 'TT-010-09'),

('KT-011-01-00', 0, 64, 'TT-011-01'),
('KT-011-02-00', 0, 64, 'TT-011-02'),
-- TAU-011 (Toa 03-09: Giường nằm)
('KT-011-03-01', 1, 6, 'TT-011-03'),
('KT-011-03-02', 2, 6, 'TT-011-03'),
('KT-011-03-03', 3, 6, 'TT-011-03'),
('KT-011-03-04', 4, 6, 'TT-011-03'),
('KT-011-03-05', 5, 6, 'TT-011-03'),
('KT-011-03-06', 6, 6, 'TT-011-03'),
('KT-011-03-07', 7, 6, 'TT-011-03'),
('KT-011-04-01', 1, 6, 'TT-011-04'),
('KT-011-04-02', 2, 6, 'TT-011-04'),
('KT-011-04-03', 3, 6, 'TT-011-04'),
('KT-011-04-04', 4, 6, 'TT-011-04'),
('KT-011-04-05', 5, 6, 'TT-011-04'),
('KT-011-04-06', 6, 6, 'TT-011-04'),
('KT-011-04-07', 7, 6, 'TT-011-04'),
('KT-011-05-01', 1, 6, 'TT-011-05'),
('KT-011-05-02', 2, 6, 'TT-011-05'),
('KT-011-05-03', 3, 6, 'TT-011-05'),
('KT-011-05-04', 4, 6, 'TT-011-05'),
('KT-011-05-05', 5, 6, 'TT-011-05'),
('KT-011-05-06', 6, 6, 'TT-011-05'),
('KT-011-05-07', 7, 6, 'TT-011-05'),
('KT-011-06-01', 1, 6, 'TT-011-06'),
('KT-011-06-02', 2, 6, 'TT-011-06'),
('KT-011-06-03', 3, 6, 'TT-011-06'),
('KT-011-06-04', 4, 6, 'TT-011-06'),
('KT-011-06-05', 5, 6, 'TT-011-06'),
('KT-011-06-06', 6, 6, 'TT-011-06'),
('KT-011-06-07', 7, 6, 'TT-011-06'),
('KT-011-07-01', 1, 4, 'TT-011-07'),
('KT-011-07-02', 2, 4, 'TT-011-07'),
('KT-011-07-03', 3, 4, 'TT-011-07'),
('KT-011-07-04', 4, 4, 'TT-011-07'),
('KT-011-07-05', 5, 4, 'TT-011-07'),
('KT-011-07-06', 6, 4, 'TT-011-07'),
('KT-011-07-07', 7, 4, 'TT-011-07'),
('KT-011-08-01', 1, 4, 'TT-011-08'),
('KT-011-08-02', 2, 4, 'TT-011-08'),
('KT-011-08-03', 3, 4, 'TT-011-08'),
('KT-011-08-04', 4, 4, 'TT-011-08'),
('KT-011-08-05', 5, 4, 'TT-011-08'),
('KT-011-08-06', 6, 4, 'TT-011-08'),
('KT-011-08-07', 7, 4, 'TT-011-08'),
('KT-011-09-01', 1, 4, 'TT-011-09'),
('KT-011-09-02', 2, 4, 'TT-011-09'),
('KT-011-09-03', 3, 4, 'TT-011-09'),
('KT-011-09-04', 4, 4, 'TT-011-09'),
('KT-011-09-05', 5, 4, 'TT-011-09'),
('KT-011-09-06', 6, 4, 'TT-011-09'),
('KT-011-09-07', 7, 4, 'TT-011-09'),

('KT-012-01-00', 0, 64, 'TT-012-01'),
('KT-012-02-00', 0, 64, 'TT-012-02'),
-- TAU-012 (Toa 03-09: Giường nằm)
('KT-012-03-01', 1, 6, 'TT-012-03'),
('KT-012-03-02', 2, 6, 'TT-012-03'),
('KT-012-03-03', 3, 6, 'TT-012-03'),
('KT-012-03-04', 4, 6, 'TT-012-03'),
('KT-012-03-05', 5, 6, 'TT-012-03'),
('KT-012-03-06', 6, 6, 'TT-012-03'),
('KT-012-03-07', 7, 6, 'TT-012-03'),
('KT-012-04-01', 1, 6, 'TT-012-04'),
('KT-012-04-02', 2, 6, 'TT-012-04'),
('KT-012-04-03', 3, 6, 'TT-012-04'),
('KT-012-04-04', 4, 6, 'TT-012-04'),
('KT-012-04-05', 5, 6, 'TT-012-04'),
('KT-012-04-06', 6, 6, 'TT-012-04'),
('KT-012-04-07', 7, 6, 'TT-012-04'),
('KT-012-05-01', 1, 6, 'TT-012-05'),
('KT-012-05-02', 2, 6, 'TT-012-05'),
('KT-012-05-03', 3, 6, 'TT-012-05'),
('KT-012-05-04', 4, 6, 'TT-012-05'),
('KT-012-05-05', 5, 6, 'TT-012-05'),
('KT-012-05-06', 6, 6, 'TT-012-05'),
('KT-012-05-07', 7, 6, 'TT-012-05'),
('KT-012-06-01', 1, 6, 'TT-012-06'),
('KT-012-06-02', 2, 6, 'TT-012-06'),
('KT-012-06-03', 3, 6, 'TT-012-06'),
('KT-012-06-04', 4, 6, 'TT-012-06'),
('KT-012-06-05', 5, 6, 'TT-012-06'),
('KT-012-06-06', 6, 6, 'TT-012-06'),
('KT-012-06-07', 7, 6, 'TT-012-06'),
('KT-012-07-01', 1, 4, 'TT-012-07'),
('KT-012-07-02', 2, 4, 'TT-012-07'),
('KT-012-07-03', 3, 4, 'TT-012-07'),
('KT-012-07-04', 4, 4, 'TT-012-07'),
('KT-012-07-05', 5, 4, 'TT-012-07'),
('KT-012-07-06', 6, 4, 'TT-012-07'),
('KT-012-07-07', 7, 4, 'TT-012-07'),
('KT-012-08-01', 1, 4, 'TT-012-08'),
('KT-012-08-02', 2, 4, 'TT-012-08'),
('KT-012-08-03', 3, 4, 'TT-012-08'),
('KT-012-08-04', 4, 4, 'TT-012-08'),
('KT-012-08-05', 5, 4, 'TT-012-08'),
('KT-012-08-06', 6, 4, 'TT-012-08'),
('KT-012-08-07', 7, 4, 'TT-012-08'),
('KT-012-09-01', 1, 4, 'TT-012-09'),
('KT-012-09-02', 2, 4, 'TT-012-09'),
('KT-012-09-03', 3, 4, 'TT-012-09'),
('KT-012-09-04', 4, 4, 'TT-012-09'),
('KT-012-09-05', 5, 4, 'TT-012-09'),
('KT-012-09-06', 6, 4, 'TT-012-09'),
('KT-012-09-07', 7, 4, 'TT-012-09'),

('KT-013-01-00', 0, 64, 'TT-013-01'),
('KT-013-02-00', 0, 64, 'TT-013-02'),
-- TAU-013 (Toa 03-09: Giường nằm)
('KT-013-03-01', 1, 6, 'TT-013-03'),
('KT-013-03-02', 2, 6, 'TT-013-03'),
('KT-013-03-03', 3, 6, 'TT-013-03'),
('KT-013-03-04', 4, 6, 'TT-013-03'),
('KT-013-03-05', 5, 6, 'TT-013-03'),
('KT-013-03-06', 6, 6, 'TT-013-03'),
('KT-013-03-07', 7, 6, 'TT-013-03'),
('KT-013-04-01', 1, 6, 'TT-013-04'),
('KT-013-04-02', 2, 6, 'TT-013-04'),
('KT-013-04-03', 3, 6, 'TT-013-04'),
('KT-013-04-04', 4, 6, 'TT-013-04'),
('KT-013-04-05', 5, 6, 'TT-013-04'),
('KT-013-04-06', 6, 6, 'TT-013-04'),
('KT-013-04-07', 7, 6, 'TT-013-04'),
('KT-013-05-01', 1, 6, 'TT-013-05'),
('KT-013-05-02', 2, 6, 'TT-013-05'),
('KT-013-05-03', 3, 6, 'TT-013-05'),
('KT-013-05-04', 4, 6, 'TT-013-05'),
('KT-013-05-05', 5, 6, 'TT-013-05'),
('KT-013-05-06', 6, 6, 'TT-013-05'),
('KT-013-05-07', 7, 6, 'TT-013-05'),
('KT-013-06-01', 1, 6, 'TT-013-06'),
('KT-013-06-02', 2, 6, 'TT-013-06'),
('KT-013-06-03', 3, 6, 'TT-013-06'),
('KT-013-06-04', 4, 6, 'TT-013-06'),
('KT-013-06-05', 5, 6, 'TT-013-06'),
('KT-013-06-06', 6, 6, 'TT-013-06'),
('KT-013-06-07', 7, 6, 'TT-013-06'),
('KT-013-07-01', 1, 4, 'TT-013-07'),
('KT-013-07-02', 2, 4, 'TT-013-07'),
('KT-013-07-03', 3, 4, 'TT-013-07'),
('KT-013-07-04', 4, 4, 'TT-013-07'),
('KT-013-07-05', 5, 4, 'TT-013-07'),
('KT-013-07-06', 6, 4, 'TT-013-07'),
('KT-013-07-07', 7, 4, 'TT-013-07'),
('KT-013-08-01', 1, 4, 'TT-013-08'),
('KT-013-08-02', 2, 4, 'TT-013-08'),
('KT-013-08-03', 3, 4, 'TT-013-08'),
('KT-013-08-04', 4, 4, 'TT-013-08'),
('KT-013-08-05', 5, 4, 'TT-013-08'),
('KT-013-08-06', 6, 4, 'TT-013-08'),
('KT-013-08-07', 7, 4, 'TT-013-08'),
('KT-013-09-01', 1, 4, 'TT-013-09'),
('KT-013-09-02', 2, 4, 'TT-013-09'),
('KT-013-09-03', 3, 4, 'TT-013-09'),
('KT-013-09-04', 4, 4, 'TT-013-09'),
('KT-013-09-05', 5, 4, 'TT-013-09'),
('KT-013-09-06', 6, 4, 'TT-013-09'),
('KT-013-09-07', 7, 4, 'TT-013-09'),

('KT-014-01-00', 0, 64, 'TT-014-01'),
('KT-014-02-00', 0, 64, 'TT-014-02'),
-- TAU-014 (Toa 03-09: Giường nằm)
('KT-014-03-01', 1, 6, 'TT-014-03'),
('KT-014-03-02', 2, 6, 'TT-014-03'),
('KT-014-03-03', 3, 6, 'TT-014-03'),
('KT-014-03-04', 4, 6, 'TT-014-03'),
('KT-014-03-05', 5, 6, 'TT-014-03'),
('KT-014-03-06', 6, 6, 'TT-014-03'),
('KT-014-03-07', 7, 6, 'TT-014-03'),
('KT-014-04-01', 1, 6, 'TT-014-04'),
('KT-014-04-02', 2, 6, 'TT-014-04'),
('KT-014-04-03', 3, 6, 'TT-014-04'),
('KT-014-04-04', 4, 6, 'TT-014-04'),
('KT-014-04-05', 5, 6, 'TT-014-04'),
('KT-014-04-06', 6, 6, 'TT-014-04'),
('KT-014-04-07', 7, 6, 'TT-014-04'),
('KT-014-05-01', 1, 6, 'TT-014-05'),
('KT-014-05-02', 2, 6, 'TT-014-05'),
('KT-014-05-03', 3, 6, 'TT-014-05'),
('KT-014-05-04', 4, 6, 'TT-014-05'),
('KT-014-05-05', 5, 6, 'TT-014-05'),
('KT-014-05-06', 6, 6, 'TT-014-05'),
('KT-014-05-07', 7, 6, 'TT-014-05'),
('KT-014-06-01', 1, 6, 'TT-014-06'),
('KT-014-06-02', 2, 6, 'TT-014-06'),
('KT-014-06-03', 3, 6, 'TT-014-06'),
('KT-014-06-04', 4, 6, 'TT-014-06'),
('KT-014-06-05', 5, 6, 'TT-014-06'),
('KT-014-06-06', 6, 6, 'TT-014-06'),
('KT-014-06-07', 7, 6, 'TT-014-06'),
('KT-014-07-01', 1, 4, 'TT-014-07'),
('KT-014-07-02', 2, 4, 'TT-014-07'),
('KT-014-07-03', 3, 4, 'TT-014-07'),
('KT-014-07-04', 4, 4, 'TT-014-07'),
('KT-014-07-05', 5, 4, 'TT-014-07'),
('KT-014-07-06', 6, 4, 'TT-014-07'),
('KT-014-07-07', 7, 4, 'TT-014-07'),
('KT-014-08-01', 1, 4, 'TT-014-08'),
('KT-014-08-02', 2, 4, 'TT-014-08'),
('KT-014-08-03', 3, 4, 'TT-014-08'),
('KT-014-08-04', 4, 4, 'TT-014-08'),
('KT-014-08-05', 5, 4, 'TT-014-08'),
('KT-014-08-06', 6, 4, 'TT-014-08'),
('KT-014-08-07', 7, 4, 'TT-014-08'),
('KT-014-09-01', 1, 4, 'TT-014-09'),
('KT-014-09-02', 2, 4, 'TT-014-09'),
('KT-014-09-03', 3, 4, 'TT-014-09'),
('KT-014-09-04', 4, 4, 'TT-014-09'),
('KT-014-09-05', 5, 4, 'TT-014-09'),
('KT-014-09-06', 6, 4, 'TT-014-09'),
('KT-014-09-07', 7, 4, 'TT-014-09'),

('KT-015-01-00', 0, 64, 'TT-015-01'),
('KT-015-02-00', 0, 64, 'TT-015-02'),
-- TAU-015 (Toa 03-09: Giường nằm)
('KT-015-03-01', 1, 6, 'TT-015-03'),
('KT-015-03-02', 2, 6, 'TT-015-03'),
('KT-015-03-03', 3, 6, 'TT-015-03'),
('KT-015-03-04', 4, 6, 'TT-015-03'),
('KT-015-03-05', 5, 6, 'TT-015-03'),
('KT-015-03-06', 6, 6, 'TT-015-03'),
('KT-015-03-07', 7, 6, 'TT-015-03'),
('KT-015-04-01', 1, 6, 'TT-015-04'),
('KT-015-04-02', 2, 6, 'TT-015-04'),
('KT-015-04-03', 3, 6, 'TT-015-04'),
('KT-015-04-04', 4, 6, 'TT-015-04'),
('KT-015-04-05', 5, 6, 'TT-015-04'),
('KT-015-04-06', 6, 6, 'TT-015-04'),
('KT-015-04-07', 7, 6, 'TT-015-04'),
('KT-015-05-01', 1, 6, 'TT-015-05'),
('KT-015-05-02', 2, 6, 'TT-015-05'),
('KT-015-05-03', 3, 6, 'TT-015-05'),
('KT-015-05-04', 4, 6, 'TT-015-05'),
('KT-015-05-05', 5, 6, 'TT-015-05'),
('KT-015-05-06', 6, 6, 'TT-015-05'),
('KT-015-05-07', 7, 6, 'TT-015-05'),
('KT-015-06-01', 1, 6, 'TT-015-06'),
('KT-015-06-02', 2, 6, 'TT-015-06'),
('KT-015-06-03', 3, 6, 'TT-015-06'),
('KT-015-06-04', 4, 6, 'TT-015-06'),
('KT-015-06-05', 5, 6, 'TT-015-06'),
('KT-015-06-06', 6, 6, 'TT-015-06'),
('KT-015-06-07', 7, 6, 'TT-015-06'),
('KT-015-07-01', 1, 4, 'TT-015-07'),
('KT-015-07-02', 2, 4, 'TT-015-07'),
('KT-015-07-03', 3, 4, 'TT-015-07'),
('KT-015-07-04', 4, 4, 'TT-015-07'),
('KT-015-07-05', 5, 4, 'TT-015-07'),
('KT-015-07-06', 6, 4, 'TT-015-07'),
('KT-015-07-07', 7, 4, 'TT-015-07'),
('KT-015-08-01', 1, 4, 'TT-015-08'),
('KT-015-08-02', 2, 4, 'TT-015-08'),
('KT-015-08-03', 3, 4, 'TT-015-08'),
('KT-015-08-04', 4, 4, 'TT-015-08'),
('KT-015-08-05', 5, 4, 'TT-015-08'),
('KT-015-08-06', 6, 4, 'TT-015-08'),
('KT-015-08-07', 7, 4, 'TT-015-08'),
('KT-015-09-01', 1, 4, 'TT-015-09'),
('KT-015-09-02', 2, 4, 'TT-015-09'),
('KT-015-09-03', 3, 4, 'TT-015-09'),
('KT-015-09-04', 4, 4, 'TT-015-09'),
('KT-015-09-05', 5, 4, 'TT-015-09'),
('KT-015-09-06', 6, 4, 'TT-015-09'),
('KT-015-09-07', 7, 4, 'TT-015-09'),

('KT-016-01-00', 0, 64, 'TT-016-01'),
('KT-016-02-00', 0, 64, 'TT-016-02'),
-- TAU-016 (Toa 03-09: Giường nằm)
('KT-016-03-01', 1, 6, 'TT-016-03'),
('KT-016-03-02', 2, 6, 'TT-016-03'),
('KT-016-03-03', 3, 6, 'TT-016-03'),
('KT-016-03-04', 4, 6, 'TT-016-03'),
('KT-016-03-05', 5, 6, 'TT-016-03'),
('KT-016-03-06', 6, 6, 'TT-016-03'),
('KT-016-03-07', 7, 6, 'TT-016-03'),
('KT-016-04-01', 1, 6, 'TT-016-04'),
('KT-016-04-02', 2, 6, 'TT-016-04'),
('KT-016-04-03', 3, 6, 'TT-016-04'),
('KT-016-04-04', 4, 6, 'TT-016-04'),
('KT-016-04-05', 5, 6, 'TT-016-04'),
('KT-016-04-06', 6, 6, 'TT-016-04'),
('KT-016-04-07', 7, 6, 'TT-016-04'),
('KT-016-05-01', 1, 6, 'TT-016-05'),
('KT-016-05-02', 2, 6, 'TT-016-05'),
('KT-016-05-03', 3, 6, 'TT-016-05'),
('KT-016-05-04', 4, 6, 'TT-016-05'),
('KT-016-05-05', 5, 6, 'TT-016-05'),
('KT-016-05-06', 6, 6, 'TT-016-05'),
('KT-016-05-07', 7, 6, 'TT-016-05'),
('KT-016-06-01', 1, 6, 'TT-016-06'),
('KT-016-06-02', 2, 6, 'TT-016-06'),
('KT-016-06-03', 3, 6, 'TT-016-06'),
('KT-016-06-04', 4, 6, 'TT-016-06'),
('KT-016-06-05', 5, 6, 'TT-016-06'),
('KT-016-06-06', 6, 6, 'TT-016-06'),
('KT-016-06-07', 7, 6, 'TT-016-06'),
('KT-016-07-01', 1, 4, 'TT-016-07'),
('KT-016-07-02', 2, 4, 'TT-016-07'),
('KT-016-07-03', 3, 4, 'TT-016-07'),
('KT-016-07-04', 4, 4, 'TT-016-07'),
('KT-016-07-05', 5, 4, 'TT-016-07'),
('KT-016-07-06', 6, 4, 'TT-016-07'),
('KT-016-07-07', 7, 4, 'TT-016-07'),
('KT-016-08-01', 1, 4, 'TT-016-08'),
('KT-016-08-02', 2, 4, 'TT-016-08'),
('KT-016-08-03', 3, 4, 'TT-016-08'),
('KT-016-08-04', 4, 4, 'TT-016-08'),
('KT-016-08-05', 5, 4, 'TT-016-08'),
('KT-016-08-06', 6, 4, 'TT-016-08'),
('KT-016-08-07', 7, 4, 'TT-016-08'),
('KT-016-09-01', 1, 4, 'TT-016-09'),
('KT-016-09-02', 2, 4, 'TT-016-09'),
('KT-016-09-03', 3, 4, 'TT-016-09'),
('KT-016-09-04', 4, 4, 'TT-016-09'),
('KT-016-09-05', 5, 4, 'TT-016-09'),
('KT-016-09-06', 6, 4, 'TT-016-09'),
('KT-016-09-07', 7, 4, 'TT-016-09'),

('KT-017-01-00', 0, 64, 'TT-017-01'),
('KT-017-02-00', 0, 64, 'TT-017-02'),
-- TAU-017 (Toa 03-09: Giường nằm)
('KT-017-03-01', 1, 6, 'TT-017-03'),
('KT-017-03-02', 2, 6, 'TT-017-03'),
('KT-017-03-03', 3, 6, 'TT-017-03'),
('KT-017-03-04', 4, 6, 'TT-017-03'),
('KT-017-03-05', 5, 6, 'TT-017-03'),
('KT-017-03-06', 6, 6, 'TT-017-03'),
('KT-017-03-07', 7, 6, 'TT-017-03'),
('KT-017-04-01', 1, 6, 'TT-017-04'),
('KT-017-04-02', 2, 6, 'TT-017-04'),
('KT-017-04-03', 3, 6, 'TT-017-04'),
('KT-017-04-04', 4, 6, 'TT-017-04'),
('KT-017-04-05', 5, 6, 'TT-017-04'),
('KT-017-04-06', 6, 6, 'TT-017-04'),
('KT-017-04-07', 7, 6, 'TT-017-04'),
('KT-017-05-01', 1, 6, 'TT-017-05'),
('KT-017-05-02', 2, 6, 'TT-017-05'),
('KT-017-05-03', 3, 6, 'TT-017-05'),
('KT-017-05-04', 4, 6, 'TT-017-05'),
('KT-017-05-05', 5, 6, 'TT-017-05'),
('KT-017-05-06', 6, 6, 'TT-017-05'),
('KT-017-05-07', 7, 6, 'TT-017-05'),
('KT-017-06-01', 1, 6, 'TT-017-06'),
('KT-017-06-02', 2, 6, 'TT-017-06'),
('KT-017-06-03', 3, 6, 'TT-017-06'),
('KT-017-06-04', 4, 6, 'TT-017-06'),
('KT-017-06-05', 5, 6, 'TT-017-06'),
('KT-017-06-06', 6, 6, 'TT-017-06'),
('KT-017-06-07', 7, 6, 'TT-017-06'),
('KT-017-07-01', 1, 4, 'TT-017-07'),
('KT-017-07-02', 2, 4, 'TT-017-07'),
('KT-017-07-03', 3, 4, 'TT-017-07'),
('KT-017-07-04', 4, 4, 'TT-017-07'),
('KT-017-07-05', 5, 4, 'TT-017-07'),
('KT-017-07-06', 6, 4, 'TT-017-07'),
('KT-017-07-07', 7, 4, 'TT-017-07'),
('KT-017-08-01', 1, 4, 'TT-017-08'),
('KT-017-08-02', 2, 4, 'TT-017-08'),
('KT-017-08-03', 3, 4, 'TT-017-08'),
('KT-017-08-04', 4, 4, 'TT-017-08'),
('KT-017-08-05', 5, 4, 'TT-017-08'),
('KT-017-08-06', 6, 4, 'TT-017-08'),
('KT-017-08-07', 7, 4, 'TT-017-08'),
('KT-017-09-01', 1, 4, 'TT-017-09'),
('KT-017-09-02', 2, 4, 'TT-017-09'),
('KT-017-09-03', 3, 4, 'TT-017-09'),
('KT-017-09-04', 4, 4, 'TT-017-09'),
('KT-017-09-05', 5, 4, 'TT-017-09'),
('KT-017-09-06', 6, 4, 'TT-017-09'),
('KT-017-09-07', 7, 4, 'TT-017-09'),

('KT-018-01-00', 0, 64, 'TT-018-01'),
('KT-018-02-00', 0, 64, 'TT-018-02'),
-- TAU-018 (Toa 03-09: Giường nằm)
('KT-018-03-01', 1, 6, 'TT-018-03'),
('KT-018-03-02', 2, 6, 'TT-018-03'),
('KT-018-03-03', 3, 6, 'TT-018-03'),
('KT-018-03-04', 4, 6, 'TT-018-03'),
('KT-018-03-05', 5, 6, 'TT-018-03'),
('KT-018-03-06', 6, 6, 'TT-018-03'),
('KT-018-03-07', 7, 6, 'TT-018-03'),
('KT-018-04-01', 1, 6, 'TT-018-04'),
('KT-018-04-02', 2, 6, 'TT-018-04'),
('KT-018-04-03', 3, 6, 'TT-018-04'),
('KT-018-04-04', 4, 6, 'TT-018-04'),
('KT-018-04-05', 5, 6, 'TT-018-04'),
('KT-018-04-06', 6, 6, 'TT-018-04'),
('KT-018-04-07', 7, 6, 'TT-018-04'),
('KT-018-05-01', 1, 6, 'TT-018-05'),
('KT-018-05-02', 2, 6, 'TT-018-05'),
('KT-018-05-03', 3, 6, 'TT-018-05'),
('KT-018-05-04', 4, 6, 'TT-018-05'),
('KT-018-05-05', 5, 6, 'TT-018-05'),
('KT-018-05-06', 6, 6, 'TT-018-05'),
('KT-018-05-07', 7, 6, 'TT-018-05'),
('KT-018-06-01', 1, 6, 'TT-018-06'),
('KT-018-06-02', 2, 6, 'TT-018-06'),
('KT-018-06-03', 3, 6, 'TT-018-06'),
('KT-018-06-04', 4, 6, 'TT-018-06'),
('KT-018-06-05', 5, 6, 'TT-018-06'),
('KT-018-06-06', 6, 6, 'TT-018-06'),
('KT-018-06-07', 7, 6, 'TT-018-06'),
('KT-018-07-01', 1, 4, 'TT-018-07'),
('KT-018-07-02', 2, 4, 'TT-018-07'),
('KT-018-07-03', 3, 4, 'TT-018-07'),
('KT-018-07-04', 4, 4, 'TT-018-07'),
('KT-018-07-05', 5, 4, 'TT-018-07'),
('KT-018-07-06', 6, 4, 'TT-018-07'),
('KT-018-07-07', 7, 4, 'TT-018-07'),
('KT-018-08-01', 1, 4, 'TT-018-08'),
('KT-018-08-02', 2, 4, 'TT-018-08'),
('KT-018-08-03', 3, 4, 'TT-018-08'),
('KT-018-08-04', 4, 4, 'TT-018-08'),
('KT-018-08-05', 5, 4, 'TT-018-08'),
('KT-018-08-06', 6, 4, 'TT-018-08'),
('KT-018-08-07', 7, 4, 'TT-018-08'),
('KT-018-09-01', 1, 4, 'TT-018-09'),
('KT-018-09-02', 2, 4, 'TT-018-09'),
('KT-018-09-03', 3, 4, 'TT-018-09'),
('KT-018-09-04', 4, 4, 'TT-018-09'),
('KT-018-09-05', 5, 4, 'TT-018-09'),
('KT-018-09-06', 6, 4, 'TT-018-09'),
('KT-018-09-07', 7, 4, 'TT-018-09'),

('KT-019-01-00', 0, 64, 'TT-019-01'),
('KT-019-02-00', 0, 64, 'TT-019-02'),
-- TAU-019 (Toa 03-09: Giường nằm)
('KT-019-03-01', 1, 6, 'TT-019-03'),
('KT-019-03-02', 2, 6, 'TT-019-03'),
('KT-019-03-03', 3, 6, 'TT-019-03'),
('KT-019-03-04', 4, 6, 'TT-019-03'),
('KT-019-03-05', 5, 6, 'TT-019-03'),
('KT-019-03-06', 6, 6, 'TT-019-03'),
('KT-019-03-07', 7, 6, 'TT-019-03'),
('KT-019-04-01', 1, 6, 'TT-019-04'),
('KT-019-04-02', 2, 6, 'TT-019-04'),
('KT-019-04-03', 3, 6, 'TT-019-04'),
('KT-019-04-04', 4, 6, 'TT-019-04'),
('KT-019-04-05', 5, 6, 'TT-019-04'),
('KT-019-04-06', 6, 6, 'TT-019-04'),
('KT-019-04-07', 7, 6, 'TT-019-04'),
('KT-019-05-01', 1, 6, 'TT-019-05'),
('KT-019-05-02', 2, 6, 'TT-019-05'),
('KT-019-05-03', 3, 6, 'TT-019-05'),
('KT-019-05-04', 4, 6, 'TT-019-05'),
('KT-019-05-05', 5, 6, 'TT-019-05'),
('KT-019-05-06', 6, 6, 'TT-019-05'),
('KT-019-05-07', 7, 6, 'TT-019-05'),
('KT-019-06-01', 1, 6, 'TT-019-06'),
('KT-019-06-02', 2, 6, 'TT-019-06'),
('KT-019-06-03', 3, 6, 'TT-019-06'),
('KT-019-06-04', 4, 6, 'TT-019-06'),
('KT-019-06-05', 5, 6, 'TT-019-06'),
('KT-019-06-06', 6, 6, 'TT-019-06'),
('KT-019-06-07', 7, 6, 'TT-019-06'),
('KT-019-07-01', 1, 4, 'TT-019-07'),
('KT-019-07-02', 2, 4, 'TT-019-07'),
('KT-019-07-03', 3, 4, 'TT-019-07'),
('KT-019-07-04', 4, 4, 'TT-019-07'),
('KT-019-07-05', 5, 4, 'TT-019-07'),
('KT-019-07-06', 6, 4, 'TT-019-07'),
('KT-019-07-07', 7, 4, 'TT-019-07'),
('KT-019-08-01', 1, 4, 'TT-019-08'),
('KT-019-08-02', 2, 4, 'TT-019-08'),
('KT-019-08-03', 3, 4, 'TT-019-08'),
('KT-019-08-04', 4, 4, 'TT-019-08'),
('KT-019-08-05', 5, 4, 'TT-019-08'),
('KT-019-08-06', 6, 4, 'TT-019-08'),
('KT-019-08-07', 7, 4, 'TT-019-08'),
('KT-019-09-01', 1, 4, 'TT-019-09'),
('KT-019-09-02', 2, 4, 'TT-019-09'),
('KT-019-09-03', 3, 4, 'TT-019-09'),
('KT-019-09-04', 4, 4, 'TT-019-09'),
('KT-019-09-05', 5, 4, 'TT-019-09'),
('KT-019-09-06', 6, 4, 'TT-019-09'),
('KT-019-09-07', 7, 4, 'TT-019-09'),

('KT-020-01-00', 0, 64, 'TT-020-01'),
('KT-020-02-00', 0, 64, 'TT-020-02'),
-- TAU-020 (Toa 03-09: Giường nằm)
('KT-020-03-01', 1, 6, 'TT-020-03'),
('KT-020-03-02', 2, 6, 'TT-020-03'),
('KT-020-03-03', 3, 6, 'TT-020-03'),
('KT-020-03-04', 4, 6, 'TT-020-03'),
('KT-020-03-05', 5, 6, 'TT-020-03'),
('KT-020-03-06', 6, 6, 'TT-020-03'),
('KT-020-03-07', 7, 6, 'TT-020-03'),
('KT-020-04-01', 1, 6, 'TT-020-04'),
('KT-020-04-02', 2, 6, 'TT-020-04'),
('KT-020-04-03', 3, 6, 'TT-020-04'),
('KT-020-04-04', 4, 6, 'TT-020-04'),
('KT-020-04-05', 5, 6, 'TT-020-04'),
('KT-020-04-06', 6, 6, 'TT-020-04'),
('KT-020-04-07', 7, 6, 'TT-020-04'),
('KT-020-05-01', 1, 6, 'TT-020-05'),
('KT-020-05-02', 2, 6, 'TT-020-05'),
('KT-020-05-03', 3, 6, 'TT-020-05'),
('KT-020-05-04', 4, 6, 'TT-020-05'),
('KT-020-05-05', 5, 6, 'TT-020-05'),
('KT-020-05-06', 6, 6, 'TT-020-05'),
('KT-020-05-07', 7, 6, 'TT-020-05'),
('KT-020-06-01', 1, 6, 'TT-020-06'),
('KT-020-06-02', 2, 6, 'TT-020-06'),
('KT-020-06-03', 3, 6, 'TT-020-06'),
('KT-020-06-04', 4, 6, 'TT-020-06'),
('KT-020-06-05', 5, 6, 'TT-020-06'),
('KT-020-06-06', 6, 6, 'TT-020-06'),
('KT-020-06-07', 7, 6, 'TT-020-07'),
('KT-020-07-01', 1, 4, 'TT-020-07'),
('KT-020-07-02', 2, 4, 'TT-020-07'),
('KT-020-07-03', 3, 4, 'TT-020-07'),
('KT-020-07-04', 4, 4, 'TT-020-07'),
('KT-020-07-05', 5, 4, 'TT-020-07'),
('KT-020-07-06', 6, 4, 'TT-020-07'),
('KT-020-07-07', 7, 4, 'TT-020-07'),
('KT-020-08-01', 1, 4, 'TT-020-08'),
('KT-020-08-02', 2, 4, 'TT-020-08'),
('KT-020-08-03', 3, 4, 'TT-020-08'),
('KT-020-08-04', 4, 4, 'TT-020-08'),
('KT-020-08-05', 5, 4, 'TT-020-08'),
('KT-020-08-06', 6, 4, 'TT-020-08'),
('KT-020-08-07', 7, 4, 'TT-020-08'),
('KT-020-09-01', 1, 4, 'TT-020-09'),
('KT-020-09-02', 2, 4, 'TT-020-09'),
('KT-020-09-03', 3, 4, 'TT-020-09'),
('KT-020-09-04', 4, 4, 'TT-020-09'),
('KT-020-09-05', 5, 4, 'TT-020-09'),
('KT-020-09-06', 6, 4, 'TT-020-09'),
('KT-020-09-07', 7, 4, 'TT-020-09');

-- 8. LOẠI GHẾ
INSERT INTO LoaiGhe (maLoaiGhe, tenLoaiGhe, moTa, heSoLoaiGhe) VALUES
('LG-NM', N'Ngồi mềm', N'Ghế ngồi mềm có điều hòa', 1.0),
('LG-GN6', N'Giường nằm 6 chỗ', N'Giường nằm 6 chỗ', 1.2),
('LG-GN4', N'Giường nằm 4 chỗ', N'Giường nằm 4 chỗ', 1.4);

-- 10. TUYẾN ĐƯỜNG
INSERT INTO TuyenDuong (maTuyenDuong, thoiGianDiChuyen, quangDuong, soTienMotKm, gaDi, gaDen) VALUES
('TD-GA-001-GA-002', 483, 395, 600, 'GA-001', 'GA-002'),
('TD-GA-001-GA-003', 612, 162, 600, 'GA-001', 'GA-003'),
('TD-GA-001-GA-004', 441, 741, 600, 'GA-001', 'GA-004'),
('TD-GA-001-GA-005', 657, 759, 600, 'GA-001', 'GA-005'),
('TD-GA-001-GA-006', 187, 608, 600, 'GA-001', 'GA-006'),
('TD-GA-001-GA-007', 237, 454, 600, 'GA-001', 'GA-007'),
('TD-GA-001-GA-008', 132, 910, 600, 'GA-001', 'GA-008'),
('TD-GA-001-GA-009', 259, 828, 600, 'GA-001', 'GA-009'),
('TD-GA-001-GA-010', 199, 200, 600, 'GA-001', 'GA-010'),
('TD-GA-001-GA-011', 255, 944, 600, 'GA-001', 'GA-011'),
('TD-GA-001-GA-012', 278, 547, 600, 'GA-001', 'GA-012'),
('TD-GA-001-GA-013', 548, 241, 600, 'GA-001', 'GA-013'),
('TD-GA-001-GA-014', 629, 393, 600, 'GA-001', 'GA-014'),
('TD-GA-001-GA-015', 108, 627, 600, 'GA-001', 'GA-015'),
('TD-GA-001-GA-016', 252, 609, 600, 'GA-001', 'GA-016'),
('TD-GA-001-GA-017', 591, 627, 600, 'GA-001', 'GA-017'),
('TD-GA-001-GA-018', 599, 324, 600, 'GA-001', 'GA-018'),
('TD-GA-001-GA-019', 401, 364, 600, 'GA-001', 'GA-019'),
('TD-GA-001-GA-020', 617, 851, 600, 'GA-001', 'GA-020'),
('TD-GA-001-GA-021', 454, 218, 600, 'GA-001', 'GA-021'),
('TD-GA-001-GA-022', 465, 531, 600, 'GA-001', 'GA-022'),
('TD-GA-001-GA-023', 555, 901, 600, 'GA-001', 'GA-023'),
('TD-GA-001-GA-024', 582, 598, 600, 'GA-001', 'GA-024'),
('TD-GA-001-GA-025', 610, 191, 600, 'GA-001', 'GA-025'),
('TD-GA-001-GA-026', 491, 337, 600, 'GA-001', 'GA-026'),
('TD-GA-001-GA-027', 62, 488, 600, 'GA-001', 'GA-027'),
('TD-GA-001-GA-028', 220, 749, 600, 'GA-001', 'GA-028'),
('TD-GA-001-GA-029', 338, 407, 600, 'GA-001', 'GA-029'),
('TD-GA-001-GA-030', 241, 108, 600, 'GA-001', 'GA-030'),
('TD-GA-001-GA-031', 395, 855, 600, 'GA-001', 'GA-031'),
('TD-GA-001-GA-032', 285, 534, 600, 'GA-001', 'GA-032'),
('TD-GA-001-GA-033', 284, 511, 600, 'GA-001', 'GA-033'),
('TD-GA-001-GA-034', 135, 50, 600, 'GA-001', 'GA-034'),
('TD-GA-001-GA-035', 273, 891, 600, 'GA-001', 'GA-035'),
('TD-GA-001-GA-036', 307, 676, 600, 'GA-001', 'GA-036'),
('TD-GA-001-GA-037', 334, 379, 600, 'GA-001', 'GA-037'),
('TD-GA-001-GA-038', 433, 635, 600, 'GA-001', 'GA-038'),
('TD-GA-001-GA-039', 612, 90, 600, 'GA-001', 'GA-039'),
('TD-GA-001-GA-040', 159, 277, 600, 'GA-001', 'GA-040'),
('TD-GA-001-GA-041', 179, 656, 600, 'GA-001', 'GA-041'),
('TD-GA-001-GA-042', 66, 201, 600, 'GA-001', 'GA-042'),
('TD-GA-001-GA-043', 344, 546, 600, 'GA-001', 'GA-043'),
('TD-GA-001-GA-044', 194, 885, 600, 'GA-001', 'GA-044'),
('TD-GA-001-GA-045', 534, 569, 600, 'GA-001', 'GA-045'),
('TD-GA-001-GA-046', 558, 490, 600, 'GA-001', 'GA-046'),
('TD-GA-001-GA-047', 482, 172, 600, 'GA-001', 'GA-047'),
('TD-GA-001-GA-048', 352, 502, 600, 'GA-001', 'GA-048'),
('TD-GA-001-GA-049', 344, 468, 600, 'GA-001', 'GA-049'),
('TD-GA-001-GA-050', 65, 675, 600, 'GA-001', 'GA-050'),
('TD-GA-001-GA-051', 421, 338, 600, 'GA-001', 'GA-051'),
('TD-GA-001-GA-052', 95, 787, 600, 'GA-001', 'GA-052'),
('TD-GA-001-GA-053', 573, 274, 600, 'GA-001', 'GA-053'),
('TD-GA-001-GA-054', 107, 948, 600, 'GA-001', 'GA-054'),
('TD-GA-001-GA-055', 135, 868, 600, 'GA-001', 'GA-055'),
('TD-GA-001-GA-056', 234, 293, 600, 'GA-001', 'GA-056'),
('TD-GA-001-GA-057', 502, 720, 600, 'GA-001', 'GA-057'),
('TD-GA-001-GA-058', 537, 818, 600, 'GA-001', 'GA-058'),
('TD-GA-001-GA-059', 110, 238, 600, 'GA-001', 'GA-059'),
('TD-GA-001-GA-060', 165, 265, 600, 'GA-001', 'GA-060'),
('TD-GA-001-GA-061', 276, 256, 600, 'GA-001', 'GA-061'),
('TD-GA-001-GA-062', 212, 692, 600, 'GA-001', 'GA-062'),
('TD-GA-001-GA-063', 573, 426, 600, 'GA-001', 'GA-063'),
('TD-GA-001-GA-064', 214, 421, 600, 'GA-001', 'GA-064'),
('TD-GA-001-GA-065', 212, 904, 600, 'GA-001', 'GA-065'),
('TD-GA-001-GA-066', 468, 861, 600, 'GA-001', 'GA-066'),
('TD-GA-001-GA-067', 236, 922, 600, 'GA-001', 'GA-067'),
('TD-GA-001-GA-068', 404, 866, 600, 'GA-001', 'GA-068'),
('TD-GA-001-GA-069', 242, 127, 600, 'GA-001', 'GA-069'),
('TD-GA-001-GA-070', 245, 298, 600, 'GA-001', 'GA-070'),
('TD-GA-001-GA-071', 222, 819, 600, 'GA-001', 'GA-071'),
('TD-GA-001-GA-072', 226, 870, 600, 'GA-001', 'GA-072'),
('TD-GA-001-GA-073', 136, 207, 600, 'GA-001', 'GA-073'),
('TD-GA-001-GA-074', 308, 596, 600, 'GA-001', 'GA-074'),
('TD-GA-001-GA-075', 619, 192, 600, 'GA-001', 'GA-075'),
('TD-GA-001-GA-076', 165, 526, 600, 'GA-001', 'GA-076'),
('TD-GA-001-GA-077', 317, 319, 600, 'GA-001', 'GA-077'),
('TD-GA-001-GA-078', 542, 813, 600, 'GA-001', 'GA-078'),
('TD-GA-001-GA-079', 323, 592, 600, 'GA-001', 'GA-079'),
('TD-GA-001-GA-080', 79, 707, 600, 'GA-001', 'GA-080'),
('TD-GA-001-GA-081', 274, 250, 600, 'GA-001', 'GA-081'),
('TD-GA-001-GA-082', 597, 452, 600, 'GA-001', 'GA-082'),
('TD-GA-001-GA-083', 471, 921, 600, 'GA-001', 'GA-083'),
('TD-GA-001-GA-084', 127, 149, 600, 'GA-001', 'GA-084'),
('TD-GA-001-GA-085', 573, 74, 600, 'GA-001', 'GA-085'),
('TD-GA-001-GA-086', 72, 574, 600, 'GA-001', 'GA-086'),
('TD-GA-001-GA-087', 111, 145, 600, 'GA-001', 'GA-087'),
('TD-GA-001-GA-088', 551, 187, 600, 'GA-001', 'GA-088'),
('TD-GA-001-GA-089', 563, 184, 600, 'GA-001', 'GA-089'),
('TD-GA-001-GA-090', 100, 840, 600, 'GA-001', 'GA-090'),
('TD-GA-001-GA-091', 628, 775, 600, 'GA-001', 'GA-091'),
('TD-GA-001-GA-092', 376, 337, 600, 'GA-001', 'GA-092'),
('TD-GA-001-GA-093', 418, 310, 600, 'GA-001', 'GA-093'),
('TD-GA-001-GA-094', 520, 689, 600, 'GA-001', 'GA-094'),
('TD-GA-001-GA-095', 122, 68, 600, 'GA-001', 'GA-095'),
('TD-GA-001-GA-096', 612, 505, 600, 'GA-001', 'GA-096'),
('TD-GA-001-GA-097', 109, 784, 600, 'GA-001', 'GA-097'),
('TD-GA-001-GA-098', 396, 620, 600, 'GA-001', 'GA-098'),
('TD-GA-001-GA-099', 322, 65, 600, 'GA-001', 'GA-099'),
('TD-GA-001-GA-100', 635, 586, 600, 'GA-001', 'GA-100'),
('TD-GA-001-GA-101', 456, 690, 600, 'GA-001', 'GA-101');
GO

-- 11. CHUYẾN TÀU THÁNG 12/2025 (22-28/12)
INSERT INTO ChuyenTau (maChuyenTau, thoiGianDi, thoiGianDen, soGheDaDat, soGheConTrong, maTau, maTuyenDuong) VALUES
-- Ngày 22/12/2025 (Thứ 2)
('CT-00001', '2025-12-22 06:00:00', '2025-12-22 14:03:00', 120, 218, 'TAU-001', 'TD-GA-001-GA-002'),
('CT-00002', '2025-12-22 07:00:00', '2025-12-22 17:12:00', 145, 235, 'TAU-002', 'TD-GA-001-GA-003'),
('CT-00003', '2025-12-22 08:00:00', '2025-12-22 15:21:00', 95, 243, 'TAU-003', 'TD-GA-001-GA-004'),
('CT-00004', '2025-12-22 09:00:00', '2025-12-22 19:57:00', 165, 215, 'TAU-004', 'TD-GA-001-GA-005'),
('CT-00005', '2025-12-22 10:00:00', '2025-12-22 13:07:00', 88, 250, 'TAU-005', 'TD-GA-001-GA-006'),
('CT-00006', '2025-12-22 06:30:00', '2025-12-22 10:27:00', 155, 225, 'TAU-006', 'TD-GA-001-GA-007'),
('CT-00007', '2025-12-22 07:30:00', '2025-12-22 09:42:00', 102, 236, 'TAU-007', 'TD-GA-001-GA-008'),
('CT-00008', '2025-12-22 08:30:00', '2025-12-22 12:49:00', 138, 242, 'TAU-008', 'TD-GA-001-GA-009'),
('CT-00009', '2025-12-22 09:30:00', '2025-12-22 12:49:00', 110, 228, 'TAU-009', 'TD-GA-001-GA-010'),
('CT-00010', '2025-12-22 10:30:00', '2025-12-22 14:45:00', 172, 208, 'TAU-010', 'TD-GA-001-GA-011'),
('CT-00011', '2025-12-22 11:00:00', '2025-12-22 15:38:00', 148, 232, 'TAU-011', 'TD-GA-001-GA-012'),
('CT-00012', '2025-12-22 12:00:00', '2025-12-22 21:08:00', 185, 195, 'TAU-012', 'TD-GA-001-GA-013'),
('CT-00013', '2025-12-22 13:00:00', '2025-12-22 23:29:00', 158, 222, 'TAU-013', 'TD-GA-001-GA-014'),
('CT-00014', '2025-12-22 14:00:00', '2025-12-22 15:48:00', 125, 255, 'TAU-014', 'TD-GA-001-GA-015'),
('CT-00015', '2025-12-22 15:00:00', '2025-12-22 19:12:00', 142, 238, 'TAU-015', 'TD-GA-001-GA-016'),

-- Ngày 23/12/2025 (Thứ 3)
('CT-00016', '2025-12-23 06:00:00', '2025-12-23 15:51:00', 168, 212, 'TAU-016', 'TD-GA-001-GA-017'),
('CT-00017', '2025-12-23 07:00:00', '2025-12-23 16:59:00', 195, 185, 'TAU-017', 'TD-GA-001-GA-018'),
('CT-00018', '2025-12-23 08:00:00', '2025-12-23 14:41:00', 135, 245, 'TAU-018', 'TD-GA-001-GA-019'),
('CT-00019', '2025-12-23 09:00:00', '2025-12-23 19:17:00', 178, 202, 'TAU-019', 'TD-GA-001-GA-020'),
('CT-00020', '2025-12-23 10:00:00', '2025-12-23 17:34:00', 115, 223, 'TAU-020', 'TD-GA-001-GA-021'),
('CT-00021', '2025-12-23 06:00:00', '2025-12-23 13:45:00', 128, 210, 'TAU-001', 'TD-GA-001-GA-022'),
('CT-00022', '2025-12-23 07:00:00', '2025-12-23 16:15:00', 162, 218, 'TAU-002', 'TD-GA-001-GA-023'),
('CT-00023', '2025-12-23 08:00:00', '2025-12-23 17:42:00', 145, 193, 'TAU-003', 'TD-GA-001-GA-024'),
('CT-00024', '2025-12-23 09:00:00', '2025-12-23 19:10:00', 152, 228, 'TAU-004', 'TD-GA-001-GA-025'),
('CT-00025', '2025-12-23 10:00:00', '2025-12-23 18:11:00', 98, 240, 'TAU-005', 'TD-GA-001-GA-026'),
('CT-00026', '2025-12-23 06:30:00', '2025-12-23 07:32:00', 75, 305, 'TAU-006', 'TD-GA-001-GA-027'),
('CT-00027', '2025-12-23 07:30:00', '2025-12-23 11:10:00', 118, 220, 'TAU-007', 'TD-GA-001-GA-028'),
('CT-00028', '2025-12-23 08:30:00', '2025-12-23 14:08:00', 155, 225, 'TAU-008', 'TD-GA-001-GA-029'),
('CT-00029', '2025-12-23 09:30:00', '2025-12-23 13:31:00', 105, 233, 'TAU-009', 'TD-GA-001-GA-030'),
('CT-00030', '2025-12-23 10:30:00', '2025-12-23 17:05:00', 168, 212, 'TAU-010', 'TD-GA-001-GA-031'),

-- Ngày 24/12/2025 (Thứ 4)
('CT-00031', '2025-12-24 11:00:00', '2025-12-24 15:45:00', 142, 238, 'TAU-011', 'TD-GA-001-GA-032'),
('CT-00032', '2025-12-24 12:00:00', '2025-12-24 16:44:00', 125, 255, 'TAU-012', 'TD-GA-001-GA-033'),
('CT-00033', '2025-12-24 13:00:00', '2025-12-24 15:15:00', 88, 292, 'TAU-013', 'TD-GA-001-GA-034'),
('CT-00034', '2025-12-24 14:00:00', '2025-12-24 18:33:00', 158, 222, 'TAU-014', 'TD-GA-001-GA-035'),
('CT-00035', '2025-12-24 15:00:00', '2025-12-24 20:07:00', 175, 205, 'TAU-015', 'TD-GA-001-GA-036'),
('CT-00036', '2025-12-24 06:00:00', '2025-12-24 11:34:00', 132, 248, 'TAU-016', 'TD-GA-001-GA-037'),
('CT-00037', '2025-12-24 07:00:00', '2025-12-24 14:13:00', 188, 192, 'TAU-017', 'TD-GA-001-GA-038'),
('CT-00038', '2025-12-24 08:00:00', '2025-12-24 18:12:00', 115, 265, 'TAU-018', 'TD-GA-001-GA-039'),
('CT-00039', '2025-12-24 09:00:00', '2025-12-24 11:39:00', 122, 258, 'TAU-019', 'TD-GA-001-GA-040'),
('CT-00040', '2025-12-24 10:00:00', '2025-12-24 12:59:00', 138, 200, 'TAU-020', 'TD-GA-001-GA-041'),
('CT-00041', '2025-12-24 06:30:00', '2025-12-24 07:36:00', 68, 270, 'TAU-001', 'TD-GA-001-GA-042'),
('CT-00042', '2025-12-24 07:30:00', '2025-12-24 13:14:00', 152, 228, 'TAU-002', 'TD-GA-001-GA-043'),
('CT-00043', '2025-12-24 08:30:00', '2025-12-24 11:44:00', 112, 226, 'TAU-003', 'TD-GA-001-GA-044'),
('CT-00044', '2025-12-24 09:30:00', '2025-12-24 18:24:00', 182, 198, 'TAU-004', 'TD-GA-001-GA-045'),
('CT-00045', '2025-12-24 10:30:00', '2025-12-24 19:48:00', 148, 190, 'TAU-005', 'TD-GA-001-GA-046'),

-- Ngày 25/12/2025 (Thứ 5)
('CT-00046', '2025-12-25 11:00:00', '2025-12-25 19:02:00', 135, 245, 'TAU-006', 'TD-GA-001-GA-047'),
('CT-00047', '2025-12-25 12:00:00', '2025-12-25 17:52:00', 105, 233, 'TAU-007', 'TD-GA-001-GA-048'),
('CT-00048', '2025-12-25 13:00:00', '2025-12-25 18:44:00', 142, 238, 'TAU-008', 'TD-GA-001-GA-049'),
('CT-00049', '2025-12-25 14:00:00', '2025-12-25 15:05:00', 78, 260, 'TAU-009', 'TD-GA-001-GA-050'),
('CT-00050', '2025-12-25 15:00:00', '2025-12-25 22:01:00', 165, 215, 'TAU-010', 'TD-GA-001-GA-051'),
('CT-00051', '2025-12-25 06:00:00', '2025-12-25 07:35:00', 85, 295, 'TAU-011', 'TD-GA-001-GA-052'),
('CT-00052', '2025-12-25 07:00:00', '2025-12-25 16:33:00', 172, 208, 'TAU-012', 'TD-GA-001-GA-053'),
('CT-00053', '2025-12-25 08:00:00', '2025-12-25 09:47:00', 95, 285, 'TAU-013', 'TD-GA-001-GA-054'),
('CT-00054', '2025-12-25 09:00:00', '2025-12-25 11:15:00', 118, 262, 'TAU-014', 'TD-GA-001-GA-055'),
('CT-00055', '2025-12-25 10:00:00', '2025-12-25 13:54:00', 128, 252, 'TAU-015', 'TD-GA-001-GA-056'),
('CT-00056', '2025-12-25 06:30:00', '2025-12-25 14:52:00', 175, 205, 'TAU-016', 'TD-GA-001-GA-057'),
('CT-00057', '2025-12-25 07:30:00', '2025-12-25 16:27:00', 192, 188, 'TAU-017', 'TD-GA-001-GA-058'),
('CT-00058', '2025-12-25 08:30:00', '2025-12-25 10:20:00', 92, 288, 'TAU-018', 'TD-GA-001-GA-059'),
('CT-00059', '2025-12-25 09:30:00', '2025-12-25 12:15:00', 125, 255, 'TAU-019', 'TD-GA-001-GA-060'),
('CT-00060', '2025-12-25 10:30:00', '2025-12-25 15:06:00', 138, 200, 'TAU-020', 'TD-GA-001-GA-061'),

-- Ngày 26/12/2025 (Thứ 6)
('CT-00061', '2025-12-26 11:00:00', '2025-12-26 14:32:00', 108, 230, 'TAU-001', 'TD-GA-001-GA-062'),
('CT-00062', '2025-12-26 12:00:00', '2025-12-26 21:33:00', 198, 182, 'TAU-002', 'TD-GA-001-GA-063'),
('CT-00063', '2025-12-26 13:00:00', '2025-12-26 16:34:00', 115, 223, 'TAU-003', 'TD-GA-001-GA-064'),
('CT-00064', '2025-12-26 14:00:00', '2025-12-26 17:32:00', 148, 232, 'TAU-004', 'TD-GA-001-GA-065'),
('CT-00065', '2025-12-26 15:00:00', '2025-12-26 22:48:00', 155, 183, 'TAU-005', 'TD-GA-001-GA-066'),
('CT-00066', '2025-12-26 06:00:00', '2025-12-26 09:56:00', 102, 278, 'TAU-006', 'TD-GA-001-GA-067'),
('CT-00067', '2025-12-26 07:00:00', '2025-12-26 13:44:00', 142, 196, 'TAU-007', 'TD-GA-001-GA-068'),
('CT-00068', '2025-12-26 08:00:00', '2025-12-26 12:02:00', 118, 262, 'TAU-008', 'TD-GA-001-GA-069'),
('CT-00069', '2025-12-26 09:00:00', '2025-12-26 13:05:00', 125, 213, 'TAU-009', 'TD-GA-001-GA-070'),
('CT-00070', '2025-12-26 10:00:00', '2025-12-26 13:42:00', 162, 218, 'TAU-010', 'TD-GA-001-GA-071'),
('CT-00071', '2025-12-26 06:30:00', '2025-12-26 10:16:00', 145, 235, 'TAU-011', 'TD-GA-001-GA-072'),
('CT-00072', '2025-12-26 07:30:00', '2025-12-26 09:46:00', 88, 292, 'TAU-012', 'TD-GA-001-GA-073'),
('CT-00073', '2025-12-26 08:30:00', '2025-12-26 13:38:00', 158, 222, 'TAU-013', 'TD-GA-001-GA-074'),
('CT-00074', '2025-12-26 09:30:00', '2025-12-26 19:49:00', 185, 195, 'TAU-014', 'TD-GA-001-GA-075'),
('CT-00075', '2025-12-26 10:30:00', '2025-12-26 13:15:00', 112, 268, 'TAU-015', 'TD-GA-001-GA-076'),

-- Ngày 27/12/2025 (Thứ 7)
('CT-00076', '2025-12-27 11:00:00', '2025-12-27 16:17:00', 132, 248, 'TAU-016', 'TD-GA-001-GA-077'),
('CT-00077', '2025-12-27 12:00:00', '2025-12-27 21:02:00', 205, 175, 'TAU-017', 'TD-GA-001-GA-078'),
('CT-00078', '2025-12-27 13:00:00', '2025-12-27 18:23:00', 138, 242, 'TAU-018', 'TD-GA-001-GA-079'),
('CT-00079', '2025-12-27 14:00:00', '2025-12-27 15:19:00', 75, 263, 'TAU-019', 'TD-GA-001-GA-080'),
('CT-00080', '2025-12-27 15:00:00', '2025-12-27 19:34:00', 122, 216, 'TAU-020', 'TD-GA-001-GA-081'),
('CT-00081', '2025-12-27 06:00:00', '2025-12-27 15:57:00', 168, 170, 'TAU-001', 'TD-GA-001-GA-082'),
('CT-00082', '2025-12-27 07:00:00', '2025-12-27 14:51:00', 152, 228, 'TAU-002', 'TD-GA-001-GA-083'),
('CT-00083', '2025-12-27 08:00:00', '2025-12-27 10:07:00', 95, 243, 'TAU-003', 'TD-GA-001-GA-084'),
('CT-00084', '2025-12-27 09:00:00', '2025-12-27 18:33:00', 178, 202, 'TAU-004', 'TD-GA-001-GA-085'),
('CT-00085', '2025-12-27 10:00:00', '2025-12-27 11:12:00', 72, 266, 'TAU-005', 'TD-GA-001-GA-086'),
('CT-00086', '2025-12-27 06:30:00', '2025-12-27 08:21:00', 82, 298, 'TAU-006', 'TD-GA-001-GA-087'),
('CT-00087', '2025-12-27 07:30:00', '2025-12-27 16:41:00', 155, 183, 'TAU-007', 'TD-GA-001-GA-088'),
('CT-00088', '2025-12-27 08:30:00', '2025-12-27 17:53:00', 172, 208, 'TAU-008', 'TD-GA-001-GA-089'),
('CT-00089', '2025-12-27 09:30:00', '2025-12-27 11:10:00', 88, 250, 'TAU-009', 'TD-GA-001-GA-090'),
('CT-00090', '2025-12-27 10:30:00', '2025-12-27 20:58:00', 195, 185, 'TAU-010', 'TD-GA-001-GA-091'),

-- Ngày 28/12/2025 (Chủ nhật)
('CT-00091', '2025-12-28 11:00:00', '2025-12-28 17:16:00', 128, 252, 'TAU-011', 'TD-GA-001-GA-092'),
('CT-00092', '2025-12-28 12:00:00', '2025-12-28 18:58:00', 145, 235, 'TAU-012', 'TD-GA-001-GA-093'),
('CT-00093', '2025-12-28 13:00:00', '2025-12-28 21:40:00', 188, 192, 'TAU-013', 'TD-GA-001-GA-094'),
('CT-00094', '2025-12-28 14:00:00', '2025-12-28 16:02:00', 78, 302, 'TAU-014', 'TD-GA-001-GA-095'),
('CT-00095', '2025-12-28 15:00:00', '2025-12-29 01:12:00', 212, 168, 'TAU-015', 'TD-GA-001-GA-096'),
('CT-00096', '2025-12-28 06:00:00', '2025-12-28 07:49:00', 92, 288, 'TAU-016', 'TD-GA-001-GA-097'),
('CT-00097', '2025-12-28 07:00:00', '2025-12-28 13:36:00', 162, 218, 'TAU-017', 'TD-GA-001-GA-098'),
('CT-00098', '2025-12-28 08:00:00', '2025-12-28 13:22:00', 105, 275, 'TAU-018', 'TD-GA-001-GA-099'),
('CT-00099', '2025-12-28 09:00:00', '2025-12-28 19:35:00', 192, 188, 'TAU-019', 'TD-GA-001-GA-100'),
('CT-00100', '2025-12-28 10:00:00', '2025-12-28 17:36:00', 152, 186, 'TAU-020', 'TD-GA-001-GA-101'),
('CT-00101', '2025-12-28 11:00:00', '2025-12-28 19:03:00', 118, 220, 'TAU-001', 'TD-GA-001-GA-002');
GO

-- 12. LOẠI VÉ
INSERT INTO LoaiVe (maLoaiVe, tenLoaiVe, moTa, heSoLoaiVe) VALUES
('LV-NL', N'Người lớn', N'Vé dành cho người lớn (từ 10 tuổi trở lên)', 1.0),
('LV-TE', N'Trẻ em', N'Vé dành cho trẻ em (từ 6-9 tuổi), giảm 25%', 0.75),
('LV-HSSV', N'Sinh viên', N'Vé dành cho học sinh/sinh viên có thẻ, giảm 10%', 0.9),
('LV-NC', N'Người cao tuổi', N'Vé dành cho người cao tuổi (từ 60 tuổi trở lên), giảm 15%', 0.85);

-- 14. KHUYẾN MÃI
-- Khuyến mãi ngày 15/12/2024
INSERT INTO KhuyenMai (MaKhuyenMai, TenKhuyenMai, HeSoKhuyenMai, NgayBatDau, NgayKetThuc, TongTienToiThieu, TienKhuyenMaiToiDa, TrangThai)
VALUES 
('KM15122024001', N'Giảm giá mùa Giáng sinh', 0.15, '2024-12-15', '2024-12-31', 500000, 200000, 1),
('KM15122024002', N'Khuyến mãi Tết dương lịch', 0.20, '2024-12-25', '2025-01-05', 1000000, 500000, 1),
('KM15122024003', N'Ưu đãi cuối năm', 0.10, '2024-12-20', '2024-12-31', 300000, 150000, 1);

-- Khuyến mãi ngày 20/12/2024
INSERT INTO KhuyenMai (MaKhuyenMai, TenKhuyenMai, HeSoKhuyenMai, NgayBatDau, NgayKetThuc, TongTienToiThieu, TienKhuyenMaiToiDa, TrangThai)
VALUES 
('KM20122024001', N'Flash Sale cuối tuần', 0.25, '2024-12-21', '2024-12-22', 800000, 400000, 1),
('KM20122024002', N'Giảm giá sinh viên', 0.12, '2024-12-20', '2025-01-31', 200000, 100000, 1);

-- Khuyến mãi ngày 21/12/2024
INSERT INTO KhuyenMai (MaKhuyenMai, TenKhuyenMai, HeSoKhuyenMai, NgayBatDau, NgayKetThuc, TongTienToiThieu, TienKhuyenMaiToiDa, TrangThai)
VALUES 
('KM21122024001', N'Khuyến mãi Black Friday cuối năm', 0.30, '2024-12-21', '2024-12-25', 1500000, 700000, 1),
('KM21122024002', N'Ưu đãi khách hàng thân thiết', 0.18, '2024-12-21', '2025-01-15', 600000, 300000, 1),
('KM21122024003', N'Giảm giá vé tàu Tết', 0.08, '2024-12-21', '2025-02-10', 400000, 150000, 1);

-- Khuyến mãi đã hết hạn
INSERT INTO KhuyenMai (MaKhuyenMai, TenKhuyenMai, HeSoKhuyenMai, NgayBatDau, NgayKetThuc, TongTienToiThieu, TienKhuyenMaiToiDa, TrangThai)
VALUES 
('KM10122024001', N'Khuyến mãi đầu tháng 12', 0.15, '2024-12-01', '2024-12-10', 500000, 200000, 0),
('KM05122024001', N'Sale cuối tuần đầu tháng', 0.20, '2024-12-05', '2024-12-08', 300000, 120000, 0);
GO


CREATE OR ALTER PROCEDURE sp_TaoGhe
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @maToaTau VARCHAR(20),
            @maKhoangTau VARCHAR(20),
            @soGhe INT,
            @maLoaiGhe VARCHAR(20),
            @loaiToa VARCHAR(100),
            @soGheCounter INT,
            @maGhe VARCHAR(50),
            @tongSoGhe INT,
            @khoangAo VARCHAR(20);
    
    BEGIN TRY
        BEGIN TRANSACTION;
        DELETE FROM Ghe;
        DELETE FROM KhoangTau WHERE maKhoangTau LIKE 'KT-%-00';
        
        DECLARE toa_cursor CURSOR FOR
        SELECT maToaTau, loaiToa FROM ToaTau;
        
        OPEN toa_cursor;
        FETCH NEXT FROM toa_cursor INTO @maToaTau, @loaiToa;
        
        WHILE @@FETCH_STATUS = 0
        BEGIN
            SET @soGheCounter = 1;

            IF @loaiToa = 'NGOI_MEM'
            BEGIN
                SET @khoangAo = 'K' + SUBSTRING(@maToaTau, 2, LEN(@maToaTau) - 1) + '-00';
                
                IF NOT EXISTS (SELECT 1 FROM KhoangTau WHERE maKhoangTau = @khoangAo)
                    INSERT INTO KhoangTau (maKhoangTau, soHieuKhoang, soGhe, maToaTau)
                    VALUES (@khoangAo, 0, 64, @maToaTau);
                
                SET @maLoaiGhe = 'LG-NM';
                
                WHILE @soGheCounter <= 64
                BEGIN
                    SET @maGhe = 'G-' +
                                 SUBSTRING(@maToaTau, CHARINDEX('-', @maToaTau) + 1,
                                           LEN(@maToaTau) - CHARINDEX('-', @maToaTau)) +
                                 '-00-' + RIGHT('000' + CAST(@soGheCounter AS VARCHAR(3)), 3);
                    
                    INSERT INTO Ghe (maGhe, soGhe, maLoaiGhe, maKhoangTau)
                    VALUES (@maGhe, @soGheCounter, @maLoaiGhe, @khoangAo);
                    
                    SET @soGheCounter += 1;
                END
            END
            ELSE
            BEGIN
                DECLARE khoang_cursor CURSOR FOR
                SELECT maKhoangTau, soGhe FROM KhoangTau WHERE maToaTau = @maToaTau;
                
                OPEN khoang_cursor;
                FETCH NEXT FROM khoang_cursor INTO @maKhoangTau, @soGhe;
                
                WHILE @@FETCH_STATUS = 0
                BEGIN
                    IF @soGhe = 6
                        SET @maLoaiGhe = 'LG-GN6';
                    ELSE IF @soGhe = 4
                        SET @maLoaiGhe = 'LG-GN4';
                    ELSE
                        SET @maLoaiGhe = 'LG-GN6';
                    
                    DECLARE @i INT = 1;
                    WHILE @i <= @soGhe
                    BEGIN
                        SET @maGhe = 'G-' +
                                     SUBSTRING(@maKhoangTau, CHARINDEX('-', @maKhoangTau) + 1,
                                               LEN(@maKhoangTau) - CHARINDEX('-', @maKhoangTau)) +
                                     '-' + RIGHT('000' + CAST(@soGheCounter AS VARCHAR(3)), 3);
                        
                        INSERT INTO Ghe (maGhe, soGhe, maLoaiGhe, maKhoangTau)
                        VALUES (@maGhe, @soGheCounter, @maLoaiGhe, @maKhoangTau);
                        
                        SET @soGheCounter += 1;
                        SET @i += 1;
                    END
                    
                    FETCH NEXT FROM khoang_cursor INTO @maKhoangTau, @soGhe;
                END
                
                CLOSE khoang_cursor;
                DEALLOCATE khoang_cursor;
            END
            
            FETCH NEXT FROM toa_cursor INTO @maToaTau, @loaiToa;
        END
        
        CLOSE toa_cursor;
        DEALLOCATE toa_cursor;
        
        COMMIT TRANSACTION;
        
        SELECT @tongSoGhe = COUNT(*) FROM Ghe;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        PRINT N'LỖI: ' + ERROR_MESSAGE();
    END CATCH
END;
GO

EXEC sp_TaoGhe;
GO

 --TẠO DỮ LIỆU CHO CHUYẾN SÀI GÒN - HÀ NỘI VÀ NGƯỢC LẠI
 --1. TUYẾN ĐƯỜNG SÀI GÒN - HÀ NỘI (1726 km, khoảng 30 giờ)

 --Kiểm tra và thêm tuyến đường nếu chưa có
IF NOT EXISTS (SELECT 1 FROM TuyenDuong WHERE maTuyenDuong = 'TD-GA-127-GA-053')
BEGIN
    INSERT INTO TuyenDuong (maTuyenDuong, thoiGianDiChuyen, quangDuong, soTienMotKm, gaDi, gaDen) 
    VALUES ('TD-GA-127-GA-053', 30, 1726, 600, 'GA-127', 'GA-053'); -- Sài Gòn -> Hà Nội (30 giờ)
END

IF NOT EXISTS (SELECT 1 FROM TuyenDuong WHERE maTuyenDuong = 'TD-GA-053-GA-127')
BEGIN
    INSERT INTO TuyenDuong (maTuyenDuong, thoiGianDiChuyen, quangDuong, soTienMotKm, gaDi, gaDen) 
    VALUES ('TD-GA-053-GA-127', 30, 1726, 600, 'GA-053', 'GA-127'); -- Hà Nội -> Sài Gòn (30 giờ)
END

 --2. CHUYẾN TÀU SÀI GÒN - HÀ NỘI

 --Xóa dữ liệu cũ nếu có (để tránh trùng lặp)
DELETE FROM Ve WHERE maChuyenTau IN (
    SELECT maChuyenTau FROM ChuyenTau 
    WHERE maTuyenDuong IN ('TD-GA-127-GA-053', 'TD-GA-053-GA-127')
);

DELETE FROM HoaDon WHERE maHoaDon IN (
    SELECT DISTINCT maHoaDon FROM Ve WHERE maChuyenTau IN (
        SELECT maChuyenTau FROM ChuyenTau 
        WHERE maTuyenDuong IN ('TD-GA-127-GA-053', 'TD-GA-053-GA-127')
    )
);

DELETE FROM ChuyenTau WHERE maTuyenDuong IN ('TD-GA-127-GA-053', 'TD-GA-053-GA-127');

select * from ChuyenTau

 --Chuyến Sài Gòn -> Hà Nội (3 chuyến)
INSERT INTO ChuyenTau (maChuyenTau, thoiGianDi, thoiGianDen, soGheDaDat, soGheConTrong, maTau, maTuyenDuong) VALUES
('CT-00110', '2025-12-22 00:30:00', '2025-12-23 18:00:00', 3, 335, 'TAU-001', 'TD-GA-127-GA-053'),
('CT-00102', '2025-12-23 06:00:00', '2025-12-24 12:00:00', 3, 335, 'TAU-001', 'TD-GA-127-GA-053'),
('CT-00103', '2025-12-23 19:00:00', '2025-12-24 01:00:00', 4, 376, 'TAU-002', 'TD-GA-127-GA-053'),
('CT-00104', '2025-12-24 00:30:00', '2025-12-25 12:00:00', 3, 335, 'TAU-003', 'TD-GA-127-GA-053');

 --Chuyến Hà Nội -> Sài Gòn (3 chuyến)
INSERT INTO ChuyenTau (maChuyenTau, thoiGianDi, thoiGianDen, soGheDaDat, soGheConTrong, maTau, maTuyenDuong) VALUES
('CT-00105', '2025-12-25 12:00:00', '2025-12-26 18:00:00', 3, 377, 'TAU-011', 'TD-GA-053-GA-127'),
('CT-00106', '2025-12-25 19:00:00', '2025-12-26 01:00:00', 4, 376, 'TAU-012', 'TD-GA-053-GA-127'),
('CT-00107', '2025-12-26 06:00:00', '2025-12-27 12:00:00', 3, 377, 'TAU-013', 'TD-GA-053-GA-127');
GO

-- 3. HÀNH KHÁCH
INSERT INTO HanhKhach (maHanhKhach, tenHanhKhach, cccd, ngaySinh) VALUES
-- Hành khách người lớn
('HK-002', N'Trần Thị Mai', '079095002001', '1990-05-12'),
('HK-003', N'Lê Văn Nam', '079095003001', '1985-08-25'),
('HK-004', N'Phạm Thị Lan', '079095004001', '1992-11-30'),
('HK-005', N'Hoàng Văn Đức', '079095005001', '1988-03-15'),

-- Học sinh/sinh viên
('HK-006', N'Nguyễn Minh Anh', '079095006001', '2002-03-10'),
('HK-007', N'Trần Thanh Tùng', '079095007001', '2003-06-15'),

-- Trẻ em
('HK-008', N'Lê Minh Khôi', NULL, '2018-03-15'),

-- Người cao tuổi
('HK-009', N'Nguyễn Văn Già', '079060001001', '1960-03-10');

-- 4. HÓA ĐƠN VÀ VÉ

DECLARE @maHoaDon VARCHAR(20);
DECLARE @maChuyenTau VARCHAR(20);
DECLARE @maHanhKhach VARCHAR(20);
DECLARE @maKhachHang VARCHAR(20);
DECLARE @maNhanVien VARCHAR(20);
DECLARE @maLoaiVe VARCHAR(20);
DECLARE @maGhe VARCHAR(50);
DECLARE @giaVe MONEY;
DECLARE @tongTien MONEY;
DECLARE @thanhTien MONEY;
DECLARE @counter INT = 1;
DECLARE @soVe INT;
DECLARE @maTau VARCHAR(20);

-- Lấy danh sách nhân viên, khách hàng
DECLARE @dsNhanVien TABLE (maNV VARCHAR(20));
INSERT INTO @dsNhanVien SELECT TOP 10 maNV FROM NhanVien WHERE trangThai = 1;

DECLARE @dsKhachHang TABLE (maKH VARCHAR(20));
INSERT INTO @dsKhachHang SELECT TOP 20 maKH FROM KhachHang;

-- Cursor để duyệt qua các chuyến tàu
DECLARE chuyen_cursor CURSOR FOR
SELECT ct.maChuyenTau, ct.maTau 
FROM ChuyenTau ct
WHERE ct.maTuyenDuong IN ('TD-GA-127-GA-053', 'TD-GA-053-GA-127')
ORDER BY ct.thoiGianDi;

OPEN chuyen_cursor;
FETCH NEXT FROM chuyen_cursor INTO @maChuyenTau, @maTau;

WHILE @@FETCH_STATUS = 0
BEGIN
    -- Tạo 2-3 hóa đơn cho mỗi chuyến tàu
    SET @soVe = 2 + (ABS(CHECKSUM(NEWID())) % 2); -- Random từ 2-3
    
    DECLARE @hoaDonCounter INT = 1;
    WHILE @hoaDonCounter <= @soVe
    BEGIN
        -- Tạo mã hóa đơn
        SET @maHoaDon = 'HD-' + RIGHT('00000' + CAST(@counter AS VARCHAR), 5);
        
        -- Chọn ngẫu nhiên nhân viên và khách hàng
        SELECT TOP 1 @maNhanVien = maNV FROM @dsNhanVien ORDER BY NEWID();
        SELECT TOP 1 @maKhachHang = maKH FROM @dsKhachHang ORDER BY NEWID();
        
        -- Chọn hành khách (1-2 vé/hóa đơn)
        DECLARE @soHanhKhach INT = 1 + (ABS(CHECKSUM(NEWID())) % 2);
        
        -- Tính tổng tiền
        SET @tongTien = 0;
        
        -- Tạo vé cho từng hành khách
        DECLARE @hkCounter INT = 1;
        WHILE @hkCounter <= @soHanhKhach
        BEGIN
            -- Chọn hành khách và loại vé phù hợp
            IF @hkCounter = 1
            BEGIN
                SET @maHanhKhach = 'HK-' + RIGHT('000' + CAST(2 + (ABS(CHECKSUM(NEWID())) % 4) AS VARCHAR), 3);
                SET @maLoaiVe = 'LV-NL'; -- Người lớn
            END
            ELSE IF @hkCounter = 2 AND @soHanhKhach = 2
            BEGIN
                DECLARE @loaiRandom INT = ABS(CHECKSUM(NEWID())) % 4;
                IF @loaiRandom = 0
                BEGIN
                    SET @maHanhKhach = 'HK-006'; -- Sinh viên
                    SET @maLoaiVe = 'LV-HSSV';
                END
                ELSE IF @loaiRandom = 1
                BEGIN
                    SET @maHanhKhach = 'HK-008'; -- Trẻ em
                    SET @maLoaiVe = 'LV-TE';
                END
                ELSE IF @loaiRandom = 2
                BEGIN
                    SET @maHanhKhach = 'HK-009'; -- Người cao tuổi
                    SET @maLoaiVe = 'LV-NC';
                END
                ELSE
                BEGIN
                    SET @maHanhKhach = 'HK-' + RIGHT('000' + CAST(2 + (ABS(CHECKSUM(NEWID())) % 4) AS VARCHAR), 3);
                    SET @maLoaiVe = 'LV-NL'; -- Người lớn
                END
            END
            
            -- Chọn ghế ngẫu nhiên từ tàu hiện tại (ưu tiên giường nằm)
            -- Lấy mã tàu từ maChuyenTau để tìm ghế phù hợp
            DECLARE @soHieuTau VARCHAR(10) = RIGHT(@maTau, 3); -- Lấy 3 số cuối của mã tàu (VD: TAU-001 -> 001)
            
            -- Chọn ngẫu nhiên giữa giường nằm 6 và 4 chỗ
            DECLARE @loaiGheRandom INT = ABS(CHECKSUM(NEWID())) % 2;
            DECLARE @soKhoang INT;
            DECLARE @soGheRandom INT;
            
            IF @loaiGheRandom = 0
            BEGIN
                -- Giường nằm 6 chỗ (toa 3, 4, 5)
                SET @soKhoang = 3 + (ABS(CHECKSUM(NEWID())) % 3); -- Toa 3, 4 hoặc 5
                SET @soGheRandom = 1 + (ABS(CHECKSUM(NEWID())) % 6); -- Ghế 1-6
                SET @maGhe = 'G-' + @soHieuTau + '-0' + CAST(@soKhoang AS VARCHAR) + '-01-' + RIGHT('000' + CAST(@soGheRandom AS VARCHAR), 3);
            END
            ELSE
            BEGIN
                -- Giường nằm 4 chỗ (toa 6, 7, 8)
                SET @soKhoang = 6 + (ABS(CHECKSUM(NEWID())) % 3); -- Toa 6, 7 hoặc 8
                SET @soGheRandom = 1 + (ABS(CHECKSUM(NEWID())) % 4); -- Ghế 1-4
                SET @maGhe = 'G-' + @soHieuTau + '-0' + CAST(@soKhoang AS VARCHAR) + '-01-' + RIGHT('000' + CAST(@soGheRandom AS VARCHAR), 3);
            END
            
            -- Kiểm tra xem ghế có tồn tại không, nếu không thì chọn ghế khác
            WHILE NOT EXISTS (SELECT 1 FROM Ghe WHERE maGhe = @maGhe)
            BEGIN
                -- Thử với toa ngồi mềm
                SET @maGhe = 'G-' + @soHieuTau + '-01-00-' + RIGHT('000' + CAST(1 + (ABS(CHECKSUM(NEWID())) % 64) AS VARCHAR), 3);
            END
            
            -- Tính giá vé: quãng đường * giá/km * hệ số loại ghế * hệ số loại vé
            DECLARE @heSoLoaiGhe FLOAT;
            SELECT @heSoLoaiGhe = heSoLoaiGhe FROM LoaiGhe lg
            INNER JOIN Ghe g ON lg.maLoaiGhe = g.maLoaiGhe
            WHERE g.maGhe = @maGhe;
            
            SET @giaVe = 1726 * 600 * @heSoLoaiGhe;
            
            -- Áp dụng hệ số loại vé
            IF @maLoaiVe = 'LV-TE'
                SET @giaVe = @giaVe * 0.75;
            ELSE IF @maLoaiVe = 'LV-HSSV'
                SET @giaVe = @giaVe * 0.9;
            ELSE IF @maLoaiVe = 'LV-NC'
                SET @giaVe = @giaVe * 0.85;
            
            SET @tongTien = @tongTien + @giaVe;
            
            SET @hkCounter = @hkCounter + 1;
        END
        
        -- Tính thành tiền (có VAT 10%)
        SET @thanhTien = @tongTien * 1.1;
        
        -- Thêm hóa đơn
        INSERT INTO HoaDon (maHoaDon, maNhanVien, maKhachHang, ngayLapHoaDon, VAT, maKhuyenMai, tongTien, thanhTien)
        VALUES (@maHoaDon, @maNhanVien, @maKhachHang, GETDATE(), 0.1, NULL, @tongTien, @thanhTien);
        
        -- Thêm vé cho hóa đơn
        SET @hkCounter = 1;
        WHILE @hkCounter <= @soHanhKhach
        BEGIN
            DECLARE @maVe VARCHAR(20) = 'VE-' + RIGHT('00000' + CAST(@counter * 10 + @hkCounter AS VARCHAR), 5);
            
            -- Chọn hành khách
            IF @hkCounter = 1
            BEGIN
                SET @maHanhKhach = 'HK-' + RIGHT('000' + CAST(2 + (ABS(CHECKSUM(NEWID())) % 4) AS VARCHAR), 3);
                SET @maLoaiVe = 'LV-NL';
            END
            ELSE IF @hkCounter = 2
            BEGIN
                SET @loaiRandom = ABS(CHECKSUM(NEWID())) % 4;
                IF @loaiRandom = 0
                BEGIN
                    SET @maHanhKhach = 'HK-006';
                    SET @maLoaiVe = 'LV-HSSV';
                END
                ELSE IF @loaiRandom = 1
                BEGIN
                    SET @maHanhKhach = 'HK-008';
                    SET @maLoaiVe = 'LV-TE';
                END
                ELSE IF @loaiRandom = 2
                BEGIN
                    SET @maHanhKhach = 'HK-009';
                    SET @maLoaiVe = 'LV-NC';
                END
                ELSE
                BEGIN
                    SET @maHanhKhach = 'HK-' + RIGHT('000' + CAST(2 + (ABS(CHECKSUM(NEWID())) % 4) AS VARCHAR), 3);
                    SET @maLoaiVe = 'LV-NL';
                END
            END
            
            -- Chọn ghế với cùng logic như trên
            SET @loaiGheRandom = ABS(CHECKSUM(NEWID())) % 2;
            
            IF @loaiGheRandom = 0
            BEGIN
                SET @soKhoang = 3 + (ABS(CHECKSUM(NEWID())) % 3);
                SET @soGheRandom = 1 + (ABS(CHECKSUM(NEWID())) % 6);
                SET @maGhe = 'G-' + @soHieuTau + '-0' + CAST(@soKhoang AS VARCHAR) + '-01-' + RIGHT('000' + CAST(@soGheRandom AS VARCHAR), 3);
            END
            ELSE
            BEGIN
                SET @soKhoang = 6 + (ABS(CHECKSUM(NEWID())) % 3);
                SET @soGheRandom = 1 + (ABS(CHECKSUM(NEWID())) % 4);
                SET @maGhe = 'G-' + @soHieuTau + '-0' + CAST(@soKhoang AS VARCHAR) + '-01-' + RIGHT('000' + CAST(@soGheRandom AS VARCHAR), 3);
            END
            
            WHILE NOT EXISTS (SELECT 1 FROM Ghe WHERE maGhe = @maGhe)
            BEGIN
                SET @maGhe = 'G-' + @soHieuTau + '-01-00-' + RIGHT('000' + CAST(1 + (ABS(CHECKSUM(NEWID())) % 64) AS VARCHAR), 3);
            END
            
            -- Tính giá vé
            SELECT @heSoLoaiGhe = heSoLoaiGhe FROM LoaiGhe lg
            INNER JOIN Ghe g ON lg.maLoaiGhe = g.maLoaiGhe
            WHERE g.maGhe = @maGhe;
            
            SET @giaVe = 1726 * 600 * @heSoLoaiGhe;
            
            IF @maLoaiVe = 'LV-TE'
                SET @giaVe = @giaVe * 0.75;
            ELSE IF @maLoaiVe = 'LV-HSSV'
                SET @giaVe = @giaVe * 0.9;
            ELSE IF @maLoaiVe = 'LV-NC'
                SET @giaVe = @giaVe * 0.85;
            
            -- Thêm vé (trạng thái 1 = đã đặt)
            INSERT INTO Ve (maVe, maLoaiVe, maChuyenTau, maHanhKhach, maGhe, maHoaDon, trangThai, giaVe)
            VALUES (@maVe, @maLoaiVe, @maChuyenTau, @maHanhKhach, @maGhe, @maHoaDon, 1, @giaVe);
            
            SET @hkCounter = @hkCounter + 1;
        END
        
        SET @counter = @counter + 1;
        SET @hoaDonCounter = @hoaDonCounter + 1;
    END
    
    FETCH NEXT FROM chuyen_cursor INTO @maChuyenTau, @maTau;
END

CLOSE chuyen_cursor;
DEALLOCATE chuyen_cursor;	
GO

CREATE FUNCTION dbo.fn_GenerateKhuyenMaiID()
RETURNS VARCHAR(20)
AS
BEGIN
    DECLARE @Today DATE = CAST(GETDATE() AS DATE);
    DECLARE @DateStr VARCHAR(8) = FORMAT(@Today, 'ddMMyyyy');
    DECLARE @Prefix VARCHAR(12) = 'KM' + @DateStr;
    DECLARE @MaxSeq INT;
    
    SELECT @MaxSeq = ISNULL(MAX(CAST(RIGHT(MaKhuyenMai, 3) AS INT)), 0)
    FROM KhuyenMai
    WHERE MaKhuyenMai LIKE @Prefix + '%'
      AND LEN(MaKhuyenMai) = 15;
    
    RETURN @Prefix + RIGHT('000' + CAST(@MaxSeq + 1 AS VARCHAR), 3);
END;
GO