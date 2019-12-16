package model;


public class ChiTietPhong {

    private Phong phong;
    private String tenDo;
    private int soLuong;
    private long giaTien;
    private String donVi;
    private int trangThai;
    private String ghiChu;
    public static final String[] dsTrangThai = {"Đầy đủ", "Thiếu/Mất", "Hỏng"};

    public ChiTietPhong(Phong phong, String tenDo, int soLuong, long giaTien, String donVi, int trangThai, String ghiChu) {
        this.phong = phong;
        this.tenDo = tenDo;
        this.soLuong = soLuong;
        this.giaTien = giaTien;
        this.donVi = donVi;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }

    public Phong getPhong() {
        return phong;
    }

    public void setPhong(Phong phong) {
        this.phong = phong;
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

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getTenDo() {
        return tenDo;
    }

    public void setTenDo(String tenDo) {
        this.tenDo = tenDo;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public void setTrangThai(String trangThai) {
        for (int i = 0; i < dsTrangThai.length; i++) {
            if (trangThai.equals(dsTrangThai[i])) {
                this.trangThai = i;
                break;
            }
        }
    }

    public String getTrangThaiString() {
        return dsTrangThai[trangThai];
    }

    public long getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(long giaTien) {
        this.giaTien = giaTien;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }
}
