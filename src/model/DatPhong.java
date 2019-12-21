package model;


import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.util.ArrayList;

public class DatPhong {

  private int maDatPhong;
  private Timestamp ngayDat;
  private String phuongThuc;
  private Timestamp ngayCheckin;
  private Timestamp ngayCheckout;
  private KhachHang khachHang;
  private NhanVien nvNhan;
  private int soNguoi;
  private long tienDatCoc;
  private boolean khachDoan;
  private boolean daHuy;
  private String ghiChu;
  private boolean daXong;

  private ObservableList<ChiTietDatPhong> dsChiTietDatPhong;

  public DatPhong() {
  }

  public DatPhong(int maDatPhong) {
    this.maDatPhong = maDatPhong;
  }

  public DatPhong(int maDatPhong, Timestamp ngayDat, String phuongThuc, Timestamp ngayCheckin, Timestamp ngayCheckout, KhachHang khachHang, NhanVien nvNhan, int soNguoi, long tienDatCoc, boolean khachDoan, boolean daHuy, String ghiChu, boolean daXong) {
    this.maDatPhong = maDatPhong;
    this.ngayDat = ngayDat;
    this.phuongThuc = phuongThuc;
    this.ngayCheckin = ngayCheckin;
    this.ngayCheckout = ngayCheckout;
    this.khachHang = khachHang;
    this.nvNhan = nvNhan;
    this.soNguoi = soNguoi;
    this.tienDatCoc = tienDatCoc;
    this.khachDoan = khachDoan;
    this.daHuy = daHuy;
    this.ghiChu = ghiChu;
    this.daXong = daXong;
  }

  public DatPhong(Timestamp ngayDat, String phuongThuc, Timestamp ngayCheckin, Timestamp ngayCheckout, KhachHang khachHang, NhanVien nvNhan, int soNguoi, long tienDatCoc, boolean khachDoan, boolean daHuy, String ghiChu, boolean daXong) {
    this.ngayDat = ngayDat;
    this.phuongThuc = phuongThuc;
    this.ngayCheckin = ngayCheckin;
    this.ngayCheckout = ngayCheckout;
    this.khachHang = khachHang;
    this.nvNhan = nvNhan;
    this.soNguoi = soNguoi;
    this.tienDatCoc = tienDatCoc;
    this.khachDoan = khachDoan;
    this.daHuy = daHuy;
    this.ghiChu = ghiChu;
    this.daXong = daXong;
  }

  public void setProps(Timestamp ngayDat, String phuongThuc, Timestamp ngayCheckin, Timestamp ngayCheckout, KhachHang khachHang, NhanVien nvNhan, int soNguoi, long tienDatCoc, boolean khachDoan, boolean daHuy, String ghiChu) {
    this.ngayDat = ngayDat;
    this.phuongThuc = phuongThuc;
    this.ngayCheckin = ngayCheckin;
    this.ngayCheckout = ngayCheckout;
    this.khachHang = khachHang;
    this.nvNhan = nvNhan;
    this.soNguoi = soNguoi;
    this.tienDatCoc = tienDatCoc;
    this.khachDoan = khachDoan;
    this.daHuy = daHuy;
    this.ghiChu = ghiChu;
  }

  public void setProps(String phuongThuc, Timestamp ngayCheckin, Timestamp ngayCheckout, KhachHang khachHang, NhanVien nvNhan, int soNguoi, long tienDatCoc, boolean daHuy, String ghiChu) {
    this.phuongThuc = phuongThuc;
    this.ngayCheckin = ngayCheckin;
    this.ngayCheckout = ngayCheckout;
    this.khachHang = khachHang;
    this.nvNhan = nvNhan;
    this.soNguoi = soNguoi;
    this.tienDatCoc = tienDatCoc;
    this.daHuy = daHuy;
    this.ghiChu = ghiChu;
  }

  public boolean isKhachDoan() {
    return khachDoan;
  }

  public void setKhachDoan(boolean khachDoan) {
    this.khachDoan = khachDoan;
  }

  public boolean isDaHuy() {
    return daHuy;
  }

  public void setDaHuy(boolean daHuy) {
    this.daHuy = daHuy;
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


  public Timestamp getNgayCheckin() {
    return ngayCheckin;
  }

  public void setNgayCheckin(Timestamp ngayCheckin) {
    this.ngayCheckin = ngayCheckin;
  }


  public Timestamp getNgayCheckout() {
    return ngayCheckout;
  }

  public void setNgayCheckout(Timestamp ngayCheckout) {
    this.ngayCheckout = ngayCheckout;
  }

  public KhachHang getKhachHang() {
    return khachHang;
  }

  public void setKhachHang(KhachHang khachHang) {
    this.khachHang = khachHang;
  }

  public NhanVien getNvNhan() {
    return nvNhan;
  }

  public void setNvNhan(NhanVien nvNhan) {
    this.nvNhan = nvNhan;
  }

  public int getSoNguoi() {
    return soNguoi;
  }

  public void setSoNguoi(int soNguoi) {
    this.soNguoi = soNguoi;
  }

  public long getTienDatCoc() {
    return tienDatCoc;
  }

  public void setTienDatCoc(long tienDatCoc) {
    this.tienDatCoc = tienDatCoc;
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

  public boolean isDaXong() {
    return daXong;
  }

  public void setDaXong(boolean daXong) {
    this.daXong = daXong;
  }
}
