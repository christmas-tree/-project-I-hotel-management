package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;

public class LoaiPhong {

    public static final boolean THEMTIEN = true;
    public static final boolean HESO = false;

    private String maLoaiPhong;
    private String loaiPhong;
    private long giaTien;
    private int soNguoi;
    private String ghiChu;

    private ArrayList<GiaPhongTroi> dsGiaTroi;

    public LoaiPhong(String maLoaiPhong, String loaiPhong) {
        this.maLoaiPhong = maLoaiPhong;
        this.loaiPhong = loaiPhong;
    }

    public LoaiPhong(String maLoaiPhong, String loaiPhong, long giaTien, int soNguoi, String ghiChu) {
        this.maLoaiPhong = maLoaiPhong;
        this.loaiPhong = loaiPhong;
        this.giaTien = giaTien;
        this.soNguoi = soNguoi;
        this.ghiChu = ghiChu;
    }

    public String getMaLoaiPhong() {
        return maLoaiPhong;
    }

    public void setMaLoaiPhong(String maLoaiPhong) {
        this.maLoaiPhong = maLoaiPhong;
    }


    public String getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(String loaiPhong) {
        this.loaiPhong = loaiPhong;
    }


    public long getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(long giaTien) {
        this.giaTien = giaTien;
    }


    public int getSoNguoi() {
        return soNguoi;
    }

    public void setSoNguoi(int soNguoi) {
        this.soNguoi = soNguoi;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public String toString() {
        return loaiPhong + " - " + String.format("%,3d", giaTien);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoaiPhong)) return false;
        LoaiPhong loaiPhong = (LoaiPhong) o;
        return Objects.equals(maLoaiPhong, loaiPhong.maLoaiPhong);
    }

    public ArrayList<GiaPhongTroi> getDsGiaTroi() {
        return dsGiaTroi;
    }

    public void setDsGiaTroi(ArrayList<GiaPhongTroi> dsQuyTacGia) {
        this.dsGiaTroi = dsQuyTacGia;
    }

    public Long getDonGia(Date ngay) {
        long donGia = giaTien;
        for (GiaPhongTroi giaPhongTroi : dsGiaTroi) {
            if (giaPhongTroi.getNgayBatDau().compareTo(ngay) <= 0 && giaPhongTroi.getNgayKetThuc().compareTo(ngay) >= 0)
                if (giaPhongTroi.getLoaiGia())
                    donGia += giaPhongTroi.getGiaTri();
                else
                    donGia = Math.round(donGia * giaPhongTroi.getHeSo() / 1000) * 1000;

        }
        return donGia;
    }
}
