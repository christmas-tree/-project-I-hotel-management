package model;

import java.sql.Date;

public class TienPhong {
    private Phong phong;
    private Date batDau;
    private Date ketThuc;
    private long donGia;
    private int soNgay;
    private long thanhTien;

    public TienPhong(Phong phong, Date batDau, Date ketThuc, long donGia, int soNgay, long thanhTien) {
        this.phong = phong;
        this.batDau = batDau;
        this.ketThuc = ketThuc;
        this.donGia = donGia;
        this.soNgay = soNgay;
        this.thanhTien = thanhTien;
    }

    public Phong getPhong() {
        return phong;
    }

    public void setPhong(Phong phong) {
        this.phong = phong;
    }

    public Date getBatDau() {
        return batDau;
    }

    public void setBatDau(Date batDau) {
        this.batDau = batDau;
    }

    public Date getKetThuc() {
        return ketThuc;
    }

    public void setKetThuc(Date ketThuc) {
        this.ketThuc = ketThuc;
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
}
