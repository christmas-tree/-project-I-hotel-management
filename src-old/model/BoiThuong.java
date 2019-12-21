package model;


public class BoiThuong {

  // Trang Thai

  public final int CANTHAY = 1;
  public final int CANSUA = 2;
  public final int MAT = 3;
  public final String[] dsTrangThai = {"Cần thay", "Cần sửa", "Mất"};

  private ChiTietPhong chiTietPhong;
  private ChiTietDatPhong chiTietDatPhong;
  private int soLuong;
  private int trangThai;
  private long boiThuong;
  private String ghiChu;

  public BoiThuong(ChiTietPhong chiTietPhong, ChiTietDatPhong chiTietDatPhong, int soLuong, int trangThai, long boiThuong, String ghiChu) {
    this.chiTietPhong = chiTietPhong;
    this.chiTietDatPhong = chiTietDatPhong;
    this.soLuong = soLuong;
    this.trangThai = trangThai;
    this.boiThuong = boiThuong;
    this.ghiChu = ghiChu;
  }

  public ChiTietPhong getChiTietPhong() {
    return chiTietPhong;
  }

  public void setChiTietPhong(ChiTietPhong chiTietPhong) {
    this.chiTietPhong = chiTietPhong;
  }

  public ChiTietDatPhong getChiTietDatPhong() {
    return chiTietDatPhong;
  }

  public void setChiTietDatPhong(ChiTietDatPhong chiTietDatPhong) {
    this.chiTietDatPhong = chiTietDatPhong;
  }

  public int getSoLuong() {
    return soLuong;
  }

  public void setSoLuong(int soLuong) {
    this.soLuong = soLuong;
  }

  public int getTrangThai() {
    return trangThai;
  }

  public String getTrangThaiString() {
    return dsTrangThai[trangThai];
  }

  public void setTrangThai(int trangThai) {
    this.trangThai = trangThai;
  }

  public long getBoiThuong() {
    return boiThuong;
  }

  public void setBoiThuong(long boiThuong) {
    this.boiThuong = boiThuong;
  }


  public String getGhiChu() {
    return ghiChu;
  }

  public void setGhiChu(String ghiChu) {
    this.ghiChu = ghiChu;
  }

}
