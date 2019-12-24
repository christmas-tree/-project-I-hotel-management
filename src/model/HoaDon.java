package model;

public class HoaDon {
    private int maChiTietHoaDon;
    private int maDatPhong;
    private String tenMuc;
    private Long donGia;
    private Integer soLuong;
    private String donVi;
    private long thanhTien;

    public HoaDon(int maChiTietHoaDon, int maDatPhong, String tenMuc, Long donGia, int soLuong, String donVi, Long thanhTien) {
        this.maChiTietHoaDon = maChiTietHoaDon;
        this.maDatPhong = maDatPhong;
        this.tenMuc = tenMuc;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.donVi = donVi;
        this.thanhTien = thanhTien;
    }

    public HoaDon(int maDatPhong, String tenMuc, Long donGia, int soLuong, String donVi, Long thanhTien) {
        this.maDatPhong = maDatPhong;
        this.tenMuc = tenMuc;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.donVi = donVi;
        this.thanhTien = thanhTien;
    }

    public int getMaChiTietHoaDon() {
        return maChiTietHoaDon;
    }

    public void setMaChiTietHoaDon(int maChiTietHoaDon) {
        this.maChiTietHoaDon = maChiTietHoaDon;
    }

    public int getMaDatPhong() {
        return maDatPhong;
    }

    public void setMaDatPhong(int maDatPhong) {
        this.maDatPhong = maDatPhong;
    }

    public String getTenMuc() {
        return tenMuc;
    }

    public void setTenMuc(String tenMuc) {
        this.tenMuc = tenMuc;
    }

    public long getDonGia() {
        return donGia;
    }

    public void setDonGia(long donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public long getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(long thanhTien) {
        this.thanhTien = thanhTien;
    }
}
