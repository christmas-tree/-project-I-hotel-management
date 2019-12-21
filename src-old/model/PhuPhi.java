package model;

import java.sql.Timestamp;

public abstract class PhuPhi {

    private String ten;
    private long gia;
    private Timestamp ngay;
    private int soLuong;
    private int thanhTien;
    private int ghiChu;

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public long getGia() {
        return gia;
    }

    public void setGia(long gia) {
        this.gia = gia;
    }

    public Timestamp getNgay() {
        return ngay;
    }

    public void setNgay(Timestamp ngay) {
        this.ngay = ngay;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(int thanhTien) {
        this.thanhTien = thanhTien;
    }

    public int getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(int ghiChu) {
        this.ghiChu = ghiChu;
    }
}
