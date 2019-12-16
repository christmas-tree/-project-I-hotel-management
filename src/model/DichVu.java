package model;


public class DichVu {

  private int maDv;
  private String tenDv;
  private long giaDv;
  private String donVi;
  private String ghiChu;

  public DichVu() {}

  public DichVu(int maDv, String tenDv, String donVi, long giaDv, String ghiChu) {
    this.maDv = maDv;
    this.tenDv = tenDv;
    this.donVi = donVi;
    this.giaDv = giaDv;
    this.ghiChu = ghiChu;
  }

  public int getMaDv() {
    return maDv;
  }

  public void setMaDv(int maDv) {
    this.maDv = maDv;
  }


  public String getTenDv() {
    return tenDv;
  }

  public void setTenDv(String tenDv) {
    this.tenDv = tenDv;
  }


  public long getGiaDv() {
    return giaDv;
  }

  public void setGiaDv(long giaDv) {
    this.giaDv = giaDv;
  }


  public String getGhiChu() {
    return ghiChu;
  }

  public void setGhiChu(String ghiChu) {
    this.ghiChu = ghiChu;
  }

  public String getDonVi() {
    return donVi;
  }

  public void setDonVi(String donVi) {
    this.donVi = donVi;
  }
}
