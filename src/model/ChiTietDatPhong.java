package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.util.ArrayList;

public class ChiTietDatPhong {

    private DatPhong datPhong;
    private Phong phong;
    private Timestamp ngayCheckinTt;
    private Timestamp ngayCheckoutTt;
    private NhanVien nvLeTan;
    private float heSoNgayLe;
    private float heSoKhuyenMai;
    private long thanhTien;
    private String ghiChu;

    private ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
    private ObservableList<ChiTietDichVu> dsDichVuSuDung = FXCollections.observableArrayList();
    private ObservableList<BoiThuong> dsBoiThuong = FXCollections.observableArrayList();

    public ChiTietDatPhong(DatPhong datPhong, Phong phong, Timestamp ngayCheckinTt, Timestamp ngayCheckoutTt, NhanVien nvLeTan, float heSoNgayLe, float heSoKhuyenMai, long thanhTien, String ghiChu) {
        this.datPhong = datPhong;
        this.phong = phong;
        this.ngayCheckinTt = ngayCheckinTt;
        this.ngayCheckoutTt = ngayCheckoutTt;
        this.nvLeTan = nvLeTan;
        this.heSoNgayLe = heSoNgayLe;
        this.heSoKhuyenMai = heSoKhuyenMai;
        this.thanhTien = thanhTien;
        this.ghiChu = ghiChu;
    }

    public ChiTietDatPhong(DatPhong datPhong, Phong phong, Timestamp ngayCheckinTt, NhanVien nvLeTan, float heSoNgayLe, float heSoKhuyenMai, String ghiChu) {
        this.datPhong = datPhong;
        this.phong = phong;
        this.ngayCheckinTt = ngayCheckinTt;
        this.nvLeTan = nvLeTan;
        this.heSoNgayLe = heSoNgayLe;
        this.heSoKhuyenMai = heSoKhuyenMai;
        this.ghiChu = ghiChu;
    }

    public void setProps(Timestamp ngayCheckinTt, Timestamp ngayCheckoutTt, NhanVien nvLeTan, float heSoNgayLe, float heSoKhuyenMai, String ghiChu) {
        this.ngayCheckinTt = ngayCheckinTt;
        this.ngayCheckoutTt = ngayCheckoutTt;
        this.nvLeTan = nvLeTan;
        this.heSoNgayLe = heSoNgayLe;
        this.heSoKhuyenMai = heSoKhuyenMai;
        this.ghiChu = ghiChu;
    }

    public DatPhong getDatPhong() {
        return datPhong;
    }

    public void setDatPhong(DatPhong datPhong) {
        this.datPhong = datPhong;
    }

    public Timestamp getNgayCheckinTt() {
        return ngayCheckinTt;
    }

    public void setNgayCheckinTt(Timestamp ngayCheckinTt) {
        this.ngayCheckinTt = ngayCheckinTt;
    }


    public Timestamp getNgayCheckoutTt() {
        return ngayCheckoutTt;
    }

    public void setNgayCheckoutTt(Timestamp ngayCheckoutTt) {
        this.ngayCheckoutTt = ngayCheckoutTt;
    }

    public float getHeSoNgayLe() {
        return heSoNgayLe;
    }

    public void setHeSoNgayLe(float heSoNgayLe) {
        this.heSoNgayLe = heSoNgayLe;
    }


    public float getHeSoKhuyenMai() {
        return heSoKhuyenMai;
    }

    public void setHeSoKhuyenMai(float heSoKhuyenMai) {
        this.heSoKhuyenMai = heSoKhuyenMai;
    }


    public long getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(long thanhTien) {
        this.thanhTien = thanhTien;
    }


    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public Phong getPhong() {
        return phong;
    }

    public void setPhong(Phong phong) {
        this.phong = phong;
    }

    public NhanVien getNvLeTan() {
        return nvLeTan;
    }

    public void setNvLeTan(NhanVien nvLeTan) {
        this.nvLeTan = nvLeTan;
    }

    public ArrayList<KhachHang> getDsKhachHang() {
        return dsKhachHang;
    }

    public void setDsKhachHang(ArrayList<KhachHang> dsKhachHang) {
        this.dsKhachHang = dsKhachHang;
    }

    public ObservableList<ChiTietDichVu> getDsDichVuSuDung() {
        return dsDichVuSuDung;
    }

    public void setDsDichVuSuDung(ObservableList<ChiTietDichVu> dsDichVuSuDung) {
        this.dsDichVuSuDung = dsDichVuSuDung;
    }

    public ObservableList<BoiThuong> getDsBoiThuong() {
        return dsBoiThuong;
    }

    public void setDsBoiThuong(ObservableList<BoiThuong> dsBoiThuong) {
        this.dsBoiThuong = dsBoiThuong;
    }

    @Override
    public String toString() {
        return phong.toString();
    }
}
