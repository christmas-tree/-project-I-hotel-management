package model;


import java.util.Objects;

public class KhachHang {

  private int maKh;
  private String tenKhach;
  private boolean gioiTinh;
  private Long cmnd;
  private Long dienThoai;
  private String diaChi;
  private String email;
  private String ghiChu;

  public KhachHang() {
  }

  public KhachHang(int maKh, String tenKhach, Long cmnd, Long dienThoai) {
    this.maKh = maKh;
    this.tenKhach = tenKhach;
    this.cmnd = cmnd;
    this.dienThoai = dienThoai;
  }

  public KhachHang(int maKh, String tenKhach) {
    this.maKh = maKh;
    this.tenKhach = tenKhach;
  }

  public KhachHang(int maKh, String tenKhach, boolean gioiTinh, Long cmnd, Long dienThoai, String diaChi, String email, String ghiChu) {
    this.maKh = maKh;
    this.tenKhach = tenKhach;
    this.gioiTinh = gioiTinh;
    this.cmnd = cmnd;
    this.dienThoai = dienThoai;
    this.diaChi = diaChi;
    this.email = email;
    this.ghiChu = ghiChu;
  }

  public int getMaKh() {
    return maKh;
  }

  public void setMaKh(int maKh) {
    this.maKh = maKh;
  }

  public String getTenKhach() {
    return tenKhach;
  }

  public void setTenKhach(String tenKhach) {
    this.tenKhach = tenKhach;
  }

  public String getDiaChi() {
    return diaChi;
  }

  public void setDiaChi(String diaChi) {
    this.diaChi = diaChi;
  }

  public boolean getGioiTinh() {
    return gioiTinh;
  }

  public void setGioiTinh(boolean gioiTinh) {
    this.gioiTinh = gioiTinh;
  }

  public Long getCmnd() {
    return cmnd;
  }

  public void setCmnd(Long cmnd) {
    this.cmnd = cmnd;
  }


  public Long getDienThoai() {
    return dienThoai;
  }

  public void setDienThoai(Long dienThoai) {
    this.dienThoai = dienThoai;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public String getGhiChu() {
    return ghiChu;
  }

  public void setGhiChu(String ghiChu) {
    this.ghiChu = ghiChu;
  }

  @Override
  public String toString() {
    return tenKhach;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof KhachHang)) return false;
    KhachHang khachHang = (KhachHang) o;
    return maKh == khachHang.maKh &&
            Objects.equals(tenKhach, khachHang.tenKhach);
  }
}
