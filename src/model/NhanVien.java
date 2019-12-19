package model;

import java.util.Objects;

public class NhanVien {

    // LOAI NHAN VIEN
    public static final int QUANLY = 0;
    public static final int LETAN = 1;

    private int maNv;
    private String tenNv;
    private int loaiNv;
    private String tenDangNhap;
    private String matKhau;
    private boolean gioiTinh;
    private long cmnd;
    private long dienThoai;
    private String email;
    private String diaChi;
    private String ghiChu;


    public NhanVien() {
    }

    public NhanVien(int maNv, String tenNv) {
        this.maNv = maNv;
        this.tenNv = tenNv;
    }

    public NhanVien(int maNv, String tenNv, int loaiNv, String tenDangNhap, boolean gioiTinh, long cmnd, long dienThoai, String email, String diaChi, String ghiChu) {
        this.maNv = maNv;
        this.tenNv = tenNv;
        this.loaiNv = loaiNv;
        this.tenDangNhap = tenDangNhap;
        this.gioiTinh = gioiTinh;
        this.cmnd = cmnd;
        this.dienThoai = dienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.ghiChu = ghiChu;
    }

    public int getMaNv() {
        return maNv;
    }

    public void setMaNv(int maNv) {
        this.maNv = maNv;
    }

    public String getTenNv() {
        return tenNv;
    }

    public void setTenNv(String tenNv) {
        this.tenNv = tenNv;
    }


    public int getLoaiNv() {
        return loaiNv;
    }

    public void setLoaiNv(int loaiNv) {
        this.loaiNv = loaiNv;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }


    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }


    public boolean getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public long getCmnd() {
        return cmnd;
    }

    public void setCmnd(long cmnd) {
        this.cmnd = cmnd;
    }


    public long getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(long dienThoai) {
        this.dienThoai = dienThoai;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }


    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NhanVien)) return false;
        NhanVien nhanVien = (NhanVien) o;
        return maNv == nhanVien.maNv &&
                tenNv.equals(nhanVien.tenNv);
    }

    @Override
    public String toString() {
        return maNv + " - " + tenNv;
    }
}
