package model;


import java.sql.Timestamp;

public class ChiTietDichVu {

  private DichVu dichVu;
  private DatPhong datPhong;
  private Phong phong;
  private Timestamp ngayDv;
  private int soLuong;
  private long thanhTien;
  private String ghiChu;

  public ChiTietDichVu(DichVu dichVu, DatPhong datPhong, Phong phong, Timestamp ngayDv, int soLuong, long thanhTien, String ghiChu) {
    this.dichVu = dichVu;
    this.datPhong = datPhong;
    this.phong = phong;
    this.ngayDv = ngayDv;
    this.soLuong = soLuong;
    this.thanhTien = thanhTien;
    this.ghiChu = ghiChu;
  }

  public DichVu getDichVu() {
    return dichVu;
  }

  public void setDichVu(DichVu dichVu) {
    this.dichVu = dichVu;
  }

  public DatPhong getDatPhong() {
    return datPhong;
  }

  public void setDatPhong(DatPhong datPhong) {
    this.datPhong = datPhong;
  }

  public Phong getPhong() {
    return phong;
  }

  public void setPhong(Phong phong) {
    this.phong = phong;
  }

  public Timestamp getNgayDv() {
    return ngayDv;
  }

  public void setNgayDv(Timestamp ngayDv) {
    this.ngayDv = ngayDv;
  }


  public int getSoLuong() {
    return soLuong;
  }

  public void setSoLuong(int soLuong) {
    this.soLuong = soLuong;
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

}
