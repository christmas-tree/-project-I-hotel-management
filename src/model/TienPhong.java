package model;

import java.time.LocalDate;

public class TienPhong {
    private int phong;
    private String ten;
    private long donGia;
    private int soNgay;
    private long thanhTien;

    private Integer loaiGia;
    private LocalDate ngay;

    public TienPhong(int phong, String ten, long donGia, int soNgay, long thanhTien) {
        this.ten = ten;
        this.phong = phong;
        this.donGia = donGia;
        this.soNgay = soNgay;
        this.thanhTien = thanhTien;
    }

    public TienPhong(LocalDate ngay, long donGia, Integer loaiGia) {
        this.ngay = ngay;
        this.donGia = donGia;
        this.loaiGia = loaiGia;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getPhong() {
        return phong;
    }

    public void setPhong(int phong) {
        this.phong = phong;
    }

    public long getDonGia() {
        return donGia;
    }

    public void setDonGia(long donGia) {
        this.donGia = donGia;
    }

    public int getSoNgay() {
        return soNgay;
    }

    public void setSoNgay(int soNgay) {
        this.soNgay = soNgay;
    }

    public long getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(long thanhTien) {
        this.thanhTien = thanhTien;
    }

    public Integer getLoaiGia() {
        return loaiGia;
    }

    public void setLoaiGia(Integer loaiGia) {
        this.loaiGia = loaiGia;
    }

    public LocalDate getNgay() {
        return ngay;
    }

    public void setNgay(LocalDate ngay) {
        this.ngay = ngay;
    }
}
