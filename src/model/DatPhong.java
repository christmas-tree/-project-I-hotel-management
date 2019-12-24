package model;


import javafx.collections.ObservableList;

import java.sql.Timestamp;

public class DatPhong {

  private int maDatPhong;
  private Timestamp ngayDat;
  private String phuongThuc;
  private boolean khachDoan;
  private long tienDatCoc;
  private Timestamp ngayCheckinDk;
  private Timestamp ngayCheckoutDk;
  private KhachHang khachHang;
  private NhanVien nvNhanDat;
  private Timestamp ngayCheckinTt;
  private Timestamp ngayCheckoutTt;
  private NhanVien nvLeTan;
  private boolean daHuy;
  private String ghiChu;

  private Long tongThanhToan;

  private ObservableList<ChiTietDatPhong> dsChiTietDatPhong;

  public DatPhong() {
  }

  public DatPhong(int maDatPhong) {
    this.maDatPhong = maDatPhong;
  }

  public DatPhong(int maDatPhong, Timestamp ngayDat, String phuongThuc, boolean khachDoan, long tienDatCoc, Timestamp ngayCheckinDk, Timestamp ngayCheckoutDk, KhachHang khachHang, NhanVien nvNhanDat, Timestamp ngayCheckinTt, Timestamp ngayCheckoutTt, NhanVien nvLeTan, boolean daHuy, String ghiChu) {
    this.maDatPhong = maDatPhong;
    this.ngayDat = ngayDat;
    this.phuongThuc = phuongThuc;
    this.khachDoan = khachDoan;
    this.tienDatCoc = tienDatCoc;
    this.ngayCheckinDk = ngayCheckinDk;
    this.ngayCheckoutDk = ngayCheckoutDk;
    this.khachHang = khachHang;
    this.nvNhanDat = nvNhanDat;
    this.ngayCheckinTt = ngayCheckinTt;
    this.ngayCheckoutTt = ngayCheckoutTt;
    this.nvLeTan = nvLeTan;
    this.daHuy = daHuy;
    this.ghiChu = ghiChu;
  }

  public DatPhong(Timestamp ngayDat, String phuongThuc, boolean khachDoan, long tienDatCoc, Timestamp ngayCheckinDk, Timestamp ngayCheckoutDk, KhachHang khachHang, NhanVien nvNhanDat, Timestamp ngayCheckinTt, NhanVien nvLeTan, String ghiChu) {
    this.ngayDat = ngayDat;
    this.phuongThuc = phuongThuc;
    this.khachDoan = khachDoan;
    this.tienDatCoc = tienDatCoc;
    this.ngayCheckinDk = ngayCheckinDk;
    this.ngayCheckoutDk = ngayCheckoutDk;
    this.khachHang = khachHang;
    this.nvNhanDat = nvNhanDat;
    this.ngayCheckinTt = ngayCheckinTt;
    this.nvLeTan = nvLeTan;
    this.daHuy = false;
    this.ghiChu = ghiChu;
  }

  public DatPhong(int maDatPhong, boolean khachDoan, KhachHang khachHang, Timestamp ngayCheckinTt, Timestamp ngayCheckoutTt, Long tongThanhToan, String ghiChu) {
    this.maDatPhong = maDatPhong;
    this.khachDoan = khachDoan;
    this.khachHang = khachHang;
    this.ngayCheckinTt = ngayCheckinTt;
    this.ngayCheckoutTt = ngayCheckoutTt;
    this.tongThanhToan = tongThanhToan;
    this.ghiChu = ghiChu;
  }

  public void setProps(String phuongThuc, boolean khachDoan, long tienDatCoc, Timestamp ngayCheckinDk, Timestamp ngayCheckoutDk, KhachHang khachHang, NhanVien nvNhanDat, Timestamp ngayCheckinTt, NhanVien nvLeTan, String ghiChu) {
    this.phuongThuc = phuongThuc;
    this.khachDoan = khachDoan;
    this.tienDatCoc = tienDatCoc;
    this.ngayCheckinDk = ngayCheckinDk;
    this.ngayCheckoutDk = ngayCheckoutDk;
    this.khachHang = khachHang;
    this.nvNhanDat = nvNhanDat;
    this.ngayCheckinTt = ngayCheckinTt;
    this.nvLeTan = nvLeTan;
    this.ghiChu = ghiChu;
  }

  public void setProps(Timestamp ngayDat, String phuongThuc, boolean khachDoan, long tienDatCoc, Timestamp ngayCheckinDk, Timestamp ngayCheckoutDk, KhachHang khachHang, NhanVien nvNhanDat, Timestamp ngayCheckinTt, NhanVien nvLeTan, String ghiChu) {
    this.ngayDat = ngayDat;
    this.phuongThuc = phuongThuc;
    this.khachDoan = khachDoan;
    this.tienDatCoc = tienDatCoc;
    this.ngayCheckinDk = ngayCheckinDk;
    this.ngayCheckoutDk = ngayCheckoutDk;
    this.khachHang = khachHang;
    this.nvNhanDat = nvNhanDat;
    this.ngayCheckinTt = ngayCheckinTt;
    this.nvLeTan = nvLeTan;
    this.daHuy = false;
    this.ghiChu = ghiChu;
  }

  public int getMaDatPhong() {
    return maDatPhong;
  }

  public void setMaDatPhong(int maDatPhong) {
    this.maDatPhong = maDatPhong;
  }

  public Timestamp getNgayDat() {
    return ngayDat;
  }

  public void setNgayDat(Timestamp ngayDat) {
    this.ngayDat = ngayDat;
  }

  public String getPhuongThuc() {
    return phuongThuc;
  }

  public void setPhuongThuc(String phuongThuc) {
    this.phuongThuc = phuongThuc;
  }

  public boolean isKhachDoan() {
    return khachDoan;
  }

  public void setKhachDoan(boolean khachDoan) {
    this.khachDoan = khachDoan;
  }

  public long getTienDatCoc() {
    return tienDatCoc;
  }

  public void setTienDatCoc(long tienDatCoc) {
    this.tienDatCoc = tienDatCoc;
  }

  public Timestamp getNgayCheckinDk() {
    return ngayCheckinDk;
  }

  public void setNgayCheckinDk(Timestamp ngayCheckinDk) {
    this.ngayCheckinDk = ngayCheckinDk;
  }

  public Timestamp getNgayCheckoutDk() {
    return ngayCheckoutDk;
  }

  public void setNgayCheckoutDk(Timestamp ngayCheckoutDk) {
    this.ngayCheckoutDk = ngayCheckoutDk;
  }

  public KhachHang getKhachHang() {
    return khachHang;
  }

  public void setKhachHang(KhachHang khachHang) {
    this.khachHang = khachHang;
  }

  public NhanVien getNvNhanDat() {
    return nvNhanDat;
  }

  public void setNvNhanDat(NhanVien nvNhanDat) {
    this.nvNhanDat = nvNhanDat;
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

  public NhanVien getNvLeTan() {
    return nvLeTan;
  }

  public void setNvLeTan(NhanVien nvLeTan) {
    this.nvLeTan = nvLeTan;
  }

  public boolean isDaHuy() {
    return daHuy;
  }

  public void setDaHuy(boolean daHuy) {
    this.daHuy = daHuy;
  }

  public String getGhiChu() {
    return ghiChu;
  }

  public void setGhiChu(String ghiChu) {
    this.ghiChu = ghiChu;
  }

  public ObservableList<ChiTietDatPhong> getDsChiTietDatPhong() {
    return dsChiTietDatPhong;
  }

  public void setDsChiTietDatPhong(ObservableList<ChiTietDatPhong> dsChiTietDatPhong) {
    this.dsChiTietDatPhong = dsChiTietDatPhong;
  }

  public Long getTongThanhToan() {
    return tongThanhToan;
  }

  public void setTongThanhToan(Long tongThanhToan) {
    this.tongThanhToan = tongThanhToan;
  }
}
