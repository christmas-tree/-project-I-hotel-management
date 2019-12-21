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
    private Long thanhTien;
    private String ghiChu;

    private Long donGiaSauHeSo;

    private ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
    private ObservableList<ChiTietDichVu> dsDichVuSuDung = FXCollections.observableArrayList();
    private ObservableList<BoiThuong> dsBoiThuong = FXCollections.observableArrayList();

    public ChiTietDatPhong(DatPhong datPhong, Phong phong, Timestamp ngayCheckinTt, Timestamp ngayCheckoutTt, NhanVien nvLeTan, long thanhTien, String ghiChu) {
        this.datPhong = datPhong;
        this.phong = phong;
        this.ngayCheckinTt = ngayCheckinTt;
        this.ngayCheckoutTt = ngayCheckoutTt;
        this.nvLeTan = nvLeTan;
        this.thanhTien = thanhTien;
        this.ghiChu = ghiChu;
    }

    public ChiTietDatPhong(DatPhong datPhong, Phong phong, Timestamp ngayCheckinTt, NhanVien nvLeTan) {
        this.datPhong = datPhong;
        this.phong = phong;
        this.ngayCheckinTt = ngayCheckinTt;
        this.nvLeTan = nvLeTan;
    }

    public void setProps(Timestamp ngayCheckinTt, Timestamp ngayCheckoutTt, NhanVien nvLeTan, String ghiChu) {
        this.ngayCheckinTt = ngayCheckinTt;
        this.ngayCheckoutTt = ngayCheckoutTt;
        this.nvLeTan = nvLeTan;
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

    public Long getDonGiaSauHeSo() {
        return donGiaSauHeSo;
    }

    public void setDonGiaSauHeSo(Long donGiaSauHeSo) {
        this.donGiaSauHeSo = donGiaSauHeSo;
    }

    public ObservableList<TienPhong> tinhTienPhong() {
        ObservableList
        long donGia = Math.round(phong.getLoaiPhong().getGiaTien() * Float.parseFloat(heSoGiamGiaField.getText()) * Float.parseFloat(heSoNgayLeField.getText()) / 1000) * 1000;
        chiTietDatPhong.setDonGiaSauHeSo(donGia);
        chiTietDatPhong.setThanhTien(donGia * soNgay);
    }

    @Override
    public String toString() {
        return phong.toString();
    }
}
