package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChiTietDatPhong {

    private DatPhong datPhong;
    private Phong phong;
    private Long thanhTien;

    private ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
    private ObservableList<ChiTietDichVu> dsDichVuSuDung = FXCollections.observableArrayList();
    private ObservableList<BoiThuong> dsBoiThuong = FXCollections.observableArrayList();

    public ChiTietDatPhong(DatPhong datPhong, Phong phong, long thanhTien) {
        this.datPhong = datPhong;
        this.phong = phong;
        this.thanhTien = thanhTien;
    }

    public ChiTietDatPhong(DatPhong datPhong, Phong phong) {
        this.datPhong = datPhong;
        this.phong = phong;
    }

    public DatPhong getDatPhong() {
        return datPhong;
    }

    public void setDatPhong(DatPhong datPhong) {
        this.datPhong = datPhong;
    }

    public long getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(long thanhTien) {
        this.thanhTien = thanhTien;
    }

    public Phong getPhong() {
        return phong;
    }

    public void setPhong(Phong phong) {
        this.phong = phong;
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

    public ObservableList<TienPhong> getDsGia() {
        ObservableList<TienPhong> dsTienphong = FXCollections.observableArrayList();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");

        LocalDate ngayBatDau = datPhong.getNgayCheckinTt().toLocalDateTime().toLocalDate();
        LocalDate ngayKetThuc = datPhong.getNgayCheckoutTt().toLocalDateTime().toLocalDate();

        LocalDate ngayTemp = ngayBatDau;
        ArrayList<GiaPhongTroi> dsGiaPhong = phong.getLoaiPhong().getDsGiaTroi();

        do {
            int loaiGia = -1;
            for (int k = 0; k < dsGiaPhong.size(); k++) {
                GiaPhongTroi giaPhongTroi = dsGiaPhong.get(k);
                LocalDate ngayKmBatDau = giaPhongTroi.getNgayBatDauLocalDate();
                LocalDate ngayKmKetThuc = giaPhongTroi.getNgayKetThucLocalDate();
                switch (giaPhongTroi.getLapLai()) {
                    case GiaPhongTroi.LAPLAI_KHONG:
                        if ((ngayTemp.isAfter(ngayKmBatDau) || ngayTemp.isEqual(ngayKmBatDau)) && (ngayTemp.isBefore(ngayKmKetThuc) || ngayTemp.isEqual(ngayKmKetThuc))) {
                            loaiGia = k;
                        }
                        break;
                    case GiaPhongTroi.CHUKY_TUAN:
                        if (Period.between(ngayKmBatDau, ngayTemp).getDays() % 7 <= Period.between(ngayKmBatDau, ngayKmKetThuc.plusDays(1)).getDays()) {
                            loaiGia = k;
                        }
                        break;
                    case GiaPhongTroi.CHUKY_THANG:
                        if (ngayKmKetThuc.getDayOfMonth() >= ngayTemp.getDayOfMonth() && ngayKmBatDau.getDayOfMonth() <= ngayTemp.getDayOfMonth()) {
                            loaiGia = k;
                        }
                        break;
                    case GiaPhongTroi.CHUKY_NAM:
                        LocalDate ngayKetThucTemp = ngayKmKetThuc.plusDays(0);
                        LocalDate ngayBatDauTemp = ngayKmBatDau.plusDays(0);
                        while (ngayBatDauTemp.isAfter(LocalDate.now())) {
                            if ((ngayTemp.isAfter(ngayBatDauTemp) || ngayTemp.isEqual(ngayBatDauTemp)) && (ngayTemp.isBefore(ngayKetThucTemp) || ngayTemp.isEqual(ngayKetThucTemp))) {
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
            if (tienPhong.getLoaiGia() != loaiGiaTruoc && loaiGiaTruoc != -2) {
                dsTienPhongFinal.add(new TienPhong(
                        phong,
                        phong.getLoaiPhong().getLoaiPhong() + " P" + phong.getMaPhong() + " " + dateFormatter.format(ngayVao) + "-" + dateFormatter.format(ngayRa) + ((loaiGiaTruoc != -1) ? " | " + dsGiaPhong.get(loaiGiaTruoc).getTen() : ""),
                        donGia,
                        soNgay,
                        donGia * soNgay));
                ngayVao = tienPhong.getNgay();
                soNgay = 0;
            }
            ngayRa = tienPhong.getNgay().plusDays(1);
            donGia = tienPhong.getDonGia();
            loaiGiaTruoc = tienPhong.getLoaiGia();
            soNgay++;
        }
        dsTienPhongFinal.add(new TienPhong(
                phong,
                phong.getLoaiPhong().getLoaiPhong() + " P" + phong.getMaPhong() + " " + dateFormatter.format(ngayVao) + "-" + dateFormatter.format(ngayRa) + ((loaiGiaTruoc != -1) ? " | " + dsGiaPhong.get(loaiGiaTruoc).getTen() : ""),
                donGia,
                soNgay,
                donGia * soNgay));

        return dsTienPhongFinal;
    }
}
