package model;

import java.sql.Date;
import java.time.LocalDate;

public class GiaPhongTroi {
    public static final String[] dsChuKy = {"Không", "Tuần", "Tháng", "Năm"};

    public static final int LAPLAI_KHONG = 0;
    public static final int CHUKY_TUAN = 1;
    public static final int CHUKY_THANG = 2;
    public static final int CHUKY_NAM = 3;

    private int maGiaPhong;
    private LoaiPhong loaiPhong;
    private String ten;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private int lapLai;
    private Long giaTien;
    private String ghiChu;

    private LocalDate ngayBatDauLocalDate;
    private LocalDate ngayKetThucLocalDate;

    public GiaPhongTroi(int maGiaPhong, LoaiPhong loaiPhong, String ten, Date ngayBatDau, Date ngayKetThuc, int lapLai, Long giaTien, String ghiChu) {
        this.maGiaPhong = maGiaPhong;
        this.loaiPhong = loaiPhong;
        this.ten = ten;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.lapLai = lapLai;
        this.giaTien = giaTien;
        this.ghiChu = ghiChu;
    }

    public int getMaGiaPhong() {
        return maGiaPhong;
    }

    public void setMaGiaPhong(int maGiaPhong) {
        this.maGiaPhong = maGiaPhong;
    }

    public LoaiPhong getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(LoaiPhong loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public int getLapLai() {
        return lapLai;
    }

    public void setLapLai(int lapLai) {
        this.lapLai = lapLai;
    }

    public Long getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(Long giaTien) {
        this.giaTien = giaTien;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public LocalDate getNgayBatDauLocalDate() {
        if (this.ngayBatDauLocalDate == null) {
            this.ngayBatDauLocalDate = ngayBatDau.toLocalDate();
        }
        return ngayBatDauLocalDate;
    }

    public LocalDate getNgayKetThucLocalDate() {
        if (this.ngayKetThucLocalDate == null) {
            this.ngayKetThucLocalDate = ngayKetThuc.toLocalDate();
        }
        return ngayKetThucLocalDate;
    }

    public String getChuKyString() {
        return dsChuKy[lapLai];
    }
}
