package model;

import java.sql.Date;

public class GiaPhongTroi {
    private int maGiaPhong;
    private LoaiPhong loaiPhong;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private boolean loaiGia;
    private Long giaTri;
    private Float heSo;
    private boolean lapLai;
    private String ghiChu;

    public GiaPhongTroi(int maGiaPhong, LoaiPhong loaiPhong, Date ngayBatDau, Date ngayKetThuc, boolean lapLai, boolean loaiGia, Long giaTri, Float heSo, String ghiChu) {
        this.maGiaPhong = maGiaPhong;
        this.loaiPhong = loaiPhong;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.loaiGia = loaiGia;
        this.giaTri = giaTri;
        this.heSo = heSo;
        this.lapLai = lapLai;
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

    public boolean getLoaiGia() {
        return loaiGia;
    }

    public void setLoaiGia(boolean loaiGia) {
        this.loaiGia = loaiGia;
    }

    public Long getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(Long giaTri) {
        this.giaTri = giaTri;
    }

    public Float getHeSo() {
        return heSo;
    }

    public void setHeSo(Float heSo) {
        this.heSo = heSo;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public boolean isLapLai() {
        return lapLai;
    }

    public void setLapLai(boolean lapLai) {
        this.lapLai = lapLai;
    }
}
