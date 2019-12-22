package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
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

    @Override
    public String toString() {
        return phong.toString();
    }

//    public ObservableList<TienPhong> getDonGia(Timestamp ngayBd, Timestamp ngayKt) {
//        ObservableList<TienPhong>
//
//        LocalDate ngayBatDau = ngayBd.toLocalDateTime().toLocalDate();
//        LocalDate ngayKetThuc = ngayKt.toLocalDateTime().toLocalDate();
//        LocalDate ngayTemp = ngayBatDau;
//
//        while (ngayKetThuc.compareTo(ngayTemp) > 0) {
//            long donGia = phong.getLoaiPhong().getGiaTien();
//            long giaTroiCong = 0;
//
//            String khoangNgayBd = "";
//            String lyDo = "";
//
//            for (GiaPhongTroi giaPhongTroi : phong.getLoaiPhong().getDsGiaTroi()) {
//                if (giaPhongTroi.isLapLai()) {
//                    if (MonthDay.of(giaPhongTroi.getNgayBatDauLocalDate().getMonthValue(), giaPhongTroi.getNgayBatDauLocalDate().getDayOfMonth()).compareTo(MonthDay.of(ngayTemp.getMonthValue(), ngayTemp.getDayOfMonth())) <= 0
//                            && MonthDay.of(giaPhongTroi.getNgayKetThucLocalDate().getMonthValue(), giaPhongTroi.getNgayKetThucLocalDate().getDayOfMonth()).compareTo(MonthDay.of(ngayTemp.getMonthValue(), ngayTemp.getDayOfMonth())) >= 0) {
//                        if (giaPhongTroi.getLoaiGia()) {
//                            giaTroiCong += giaPhongTroi.getGiaTri();
//                            lyDo += giaPhongTroi.getTen() + " +" + String.format("%,3d", giaPhongTroi.getGiaTri());
//                        }
//                        else {
//                            donGia = Math.round(donGia * giaPhongTroi.getHeSo() / 1000) * 1000;
//                        }
//                    }
//                } else {
//                    if (giaPhongTroi.getNgayBatDauLocalDate().compareTo(ngay) <= 0 && giaPhongTroi.getNgayKetThuc().compareTo(ngay) >= 0)
//                        if (giaPhongTroi.getLoaiGia())
//                            giaTroiCong += giaPhongTroi.getGiaTri();
//                        else
//                            donGia = Math.round(donGia * giaPhongTroi.getHeSo() / 1000) * 1000;
//                }
//            }
//            donGia = donGia + giaTroiCong;
//
//        }
//
//        return
//    }

    public ObservableList<TienPhong> getDonGia(Date ngayBd, Date ngayKt) {
        ObservableList<TienPhong> dsTienphong = FXCollections.observableArrayList();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM");

        LocalDate ngayBatDau = ngayBd.toLocalDate();
        LocalDate ngayKetThuc = ngayKt.toLocalDate();

        LocalDate ngayTemp = ngayBatDau;
        ArrayList<GiaPhongTroi> dsGiaPhong = phong.getLoaiPhong().getDsGiaTroi();

        do {
            int loaiGia = -1;
            for (int k = 0; k < dsGiaPhong.size(); k++) {
                GiaPhongTroi giaPhongTroi = dsGiaPhong.get(k);
                switch (giaPhongTroi.getLapLai()) {
                    case GiaPhongTroi.LAPLAI_KHONG:
                        if (ngayTemp.isAfter(ngayBatDau) && ngayTemp.isBefore(ngayKetThuc)) {
                            loaiGia = k;
                        }
                    case GiaPhongTroi.CHUKY_TUAN:
                        if (Period.between(ngayBatDau, ngayTemp).getDays() % 7 < Period.between(ngayBatDau, ngayKetThuc.plusDays(1)).getDays()) {
                            loaiGia = k;
                            break;
                        }
                    case GiaPhongTroi.CHUKY_THANG:
                        if (ngayKetThuc.getDayOfMonth() > ngayTemp.getDayOfMonth() && ngayBatDau.getDayOfMonth() < ngayTemp.getDayOfMonth()) {
                            loaiGia = k;
                            break;
                        }
                    case GiaPhongTroi.CHUKY_NAM:
                        LocalDate ngayKetThucTemp = ngayKetThuc.plusDays(0);
                        LocalDate ngayBatDauTemp = ngayBatDau.plusDays(0);
                        while (ngayBatDauTemp.isAfter(LocalDate.now())) {
                            if (ngayTemp.isAfter(ngayBatDauTemp) && ngayTemp.isBefore(ngayKetThucTemp)) {
                                loaiGia = k;
                                break;
                            } else {
                                ngayBatDauTemp = ngayBatDauTemp.plusYears(1);
                                ngayKetThucTemp = ngayKetThucTemp.plusYears(1);
                            }
                        }
                        break;
                }

                if (loaiGia != -1) {
                    dsTienphong.add(new TienPhong(
                            ngayTemp,
                            giaPhongTroi.getGiaTien(),
                            loaiGia));
                    break;
                }
            }
            if (loaiGia == -1) {
                dsTienphong.add(new TienPhong(
                        ngayTemp,
                        phong.getLoaiPhong().getGiaTien(),
                        loaiGia));
            }
            ngayTemp = ngayTemp.plusDays(1);
        } while (ngayKetThuc.isAfter(ngayTemp));

        int loaiGiaTruoc = -2;
        LocalDate ngayVao = dsTienphong.get(0).getNgay();
        LocalDate ngayRa = dsTienphong.get(0).getNgay().plusDays(1);
        long donGia = 0;
        int soNgay = 0;
        ObservableList<TienPhong> dsTienPhongFinal = FXCollections.observableArrayList();

        for (TienPhong tienPhong : dsTienphong) {
            if (tienPhong.getLoaiGia() == loaiGiaTruoc) {
                ngayRa = tienPhong.getNgay().plusDays(1);
                donGia = tienPhong.getDonGia();
                soNgay++;
            } else {
                dsTienPhongFinal.add(new TienPhong(
                        phong.getMaPhong(),
                        phong.getLoaiPhong() + " " + phong.getMaPhong() + " " + dateFormatter.format(ngayVao) + "-" + dateFormatter.format(ngayRa) + " | " + dsGiaPhong.get(loaiGiaTruoc).getTen(),
                        donGia,
                        soNgay,
                        donGia * soNgay));
                ngayVao = tienPhong.getNgay();
                ngayRa = ngayVao.plusDays(1);
                donGia = tienPhong.getDonGia();
                soNgay++;
            }
        }
        dsTienPhongFinal.add(new TienPhong(
                phong.getMaPhong(),
                phong.getLoaiPhong() + " " + phong.getMaPhong() + " " + dateFormatter.format(ngayVao) + "-" + dateFormatter.format(ngayRa) + " | " + dsGiaPhong.get(loaiGiaTruoc).getTen(),
                donGia,
                soNgay,
                donGia * soNgay));

        return dsTienPhongFinal;
    }
}
