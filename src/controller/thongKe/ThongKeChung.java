package controller.thongKe;

import controller.basic.KhungUngDung;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import util.ExceptionHandler;

import java.io.IOException;

public class ThongKeChung {

    @FXML
    private Button xemGiaPhong;

    @FXML
    private ChoiceBox<String> vatTuCombo;

    @FXML
    private Button xemVatTu;

    @FXML
    private Button xemKhachHang;

    @FXML
    private Button xemDoanhThu;

    @FXML
    private Button xemPhong;

    @FXML
    private ChoiceBox<String> dichVuCombo;

    @FXML
    private ChoiceBox<String> giaPhongCombo;

    @FXML
    private Button xemNhanVien;

    @FXML
    private ChoiceBox<String> doanhThuCombo;

    @FXML
    private Button xemDichVu;

    @FXML
    private ChoiceBox<String> khachHangCombo;

    @FXML
    private ChoiceBox<String> phongCombo;

    @FXML
    private ChoiceBox<String> loaiPhongCombo;

    @FXML
    private ChoiceBox<String> nhanVienCombo;

    @FXML
    private Button xemLoaiPhong;

    // Chuong Trinh
    public String[] dsTkPhong = {
            "Thống kê danh sách phòng",
            "Thống kê phòng theo tầng",
            "Thống kê phòng theo trạng thái",
            "Thống kê phòng theo tầng theo trạng thái"
    };
    public String[] dsSqlPhong = {
            "SELECT ma_phong [Mã phòng], ma_loai_phong [Mã loại phòng], tang [Tầng], trang_thai [Trạng thái], ghi_chu [Ghi chú] FROM phong",
            "SELECT tang [Tầng], COUNT(*) [Số lượng] FROM phong GROUP BY tang",
            "SELECT CASE " +
                    "WHEN trang_thai=0 THEN N'Sẵn sàng' " +
                    "WHEN trang_thai=1 THEN N'Đang sử dụng' " +
                    "WHEN trang_thai=2 THEN N'Đang dọn' " +
                    "WHEN trang_thai=2 THEN N'Đang sửa chữa' " +
                    "END AS [Trạng thái], COUNT(*) [Số lượng] FROM phong GROUP BY trang_thai",
            "SELECT tang [Tầng], 0 [Sẵn sàng], 1 [Đang dùng], 2 [Đang dọn], 3 [Đang sửa chữa]  FROM \n" +
                    "(\n" +
                    "SELECT ma_phong, tang, trang_thai\n" +
                    "FROM phong) s\n" +
                    "PIVOT \n" +
                    "(\n" +
                    "COUNT(ma_phong)\n" +
                    "FOR trang_thai IN ([0], [1], [2], [3])) p"
    }; // DONE

    public String[] dsTkLoaiPhong = {
            "Thống kê các loại phòng",
            "Thống kê số phòng theo loại phòng",
            "Thống kê số phòng theo tầng theo loại phòng",
            "Thống kê số phòng theo loại phòng theo trạng thái"
    };
    public String[] dsSqlLoaiPhong = {
            "SELECT ma_loai_phong [Mã loại phòng], loai_phong [Loại phòng], gia_tien [Giá tiền], so_nguoi [Số người], ghi_chu [Ghi chú] FROM loai_phong",
            "SELECT loai_phong [Loại phòng], COUNT(ma_phong) [Số phòng] FROM loai_phong, phong WHERE loai_phong.ma_loai_phong=phong.ma_loai_phong GROUP BY loai_phong",
            "EXEC [tkLoaiPhong2]",
            "EXEC [tkLoaiPhong3]"
    }; // DONE

    public String[] dsTkGiaPhong = {
            "Thống kê tất cả giá phòng",
            "Giá trung bình, giá cao nhất của từng loại phòng",
            "Số lượng quy tắc giá mỗi loại phòng",
            "Số lượng quy tắc giá theo loại phòng, theo kiểu lặp lại",
            "Số lượng quy tắc giá theo tháng"
    };
    public String[] dsSqlGiaPhong = {
            "SELECT ma_gia_phong [Mã giá], loai_phong.ma_loai_phong [Mã loại phòng], gia_phong_troi.loai_phong [Loại phòng], ten [Diễn giải], ngay_bd [Ngày BĐ], ngay_kt [Ngày KT], " +
                    "CASE WHEN lap_lai=0 THEN N'Không' WHEN lap_lai=1 THEN N'Tuần' WHEN lap_lai=2 THEN N'Tháng' WHEN lap_lai=3 THEN N'Năm' END AS [Lặp lại], gia_phong_troi.gia_tien [Giá tiền], gia_phong_troi.ghi_chu [Ghi chú] FROM loai_phong, gia_phong_troi WHERE loai_phong.ma_loai_phong = gia_phong_troi.ma_loai_phong",
            "SELECT loai_phong [Loại phòng], AVG(gia_tien) [Giá trung bình], MAX(gia_tien) [Giá cao nhất] FROM ((SELECT loai_phong, gia_tien FROM loai_phong) UNION (SELECT loai_phong, gia_tien_troi.gia_tien FROM loai_phong, gia_phong_troi WHERE loai_phong.ma_loai_phong=gia_phong_troi.ma_loai_phong)) p GROUP BY loai_phong",
            "SELECT ma_loai_phong [Mã loại phòng], COUNT(*) [Số lượng] FROM gia_phong_troi GROUP BY ma_loai_phong",
            "EXEC [tkGiaPhong3]",
            "SELECT FORMAT(dp.ngay_checkout_tt, 'yyyy-mm') [Tháng], SUM(thanh_tien) FROM hoa_don hd, dat_phong dp WHERE hd.ma_dat_phong = dp.ma_dat_phong GROUP BY FORMAT(dp.ngay_checkout_tt, 'yyyy') ORDER BY FORMAT(dp.ngay_checkout_tt, 'yyyy') DESC",
    }; // DONE

    public String[] dsTkDichVu = {
            "Thống kê số lượng dịch vụ theo khoảng giá",
            "Thống kê doanh thu dịch vụ theo tháng",
            "Thống kê doanh thu dịch vụ theo năm",
            "Thống kê doanh thu dịch vụ theo phòng theo tháng",
            "Thống kê doanh thu dịch vụ theo loại phòng theo tháng",
    };
    public String[] dsSqlDichVu = {
           "select t.range as [Khoảng giá], count(*) as [Số ]\n" +
                   "from (\n" +
                   "         select case\n" +
                   "                    when gia_dv between 0 and 50000 then '0-50,000'\n" +
                   "                    when gia_dv between 50001 and 200000 then '50,000-200,000'\n" +
                   "                    when gia_dv between 200001 and 500000 then '200,000-500,000'\n" +
                   "                    when gia_dv between 500001 and 1000000 then '500,000-1,000,000'\n" +
                   "                    else '>1,000,000' end as range\n" +
                   "         from dich_vu) t\n" +
                   "group by t.range",
            "",
            "",
            "",
            ""
    };

    public String[] dsTkKhachHang = {
            "Thống kê khách hàng theo giới tính",
    };
    public String[] dsSqlKhachHang = {
            "SELECT CASE WHEN gioi_tinh=1 THEN N'Nam' WHEN gioi_tinh=0 THEN N'Nu' END AS [Giới tính], COUNT(*) [Số lượng] FROM khach_hang GROUP BY gioi_tinh"
    }; // DONE

    public String[] dsTkVatTu = {
            "Thống kê số lượng vật tư theo phòng",
            "Thống kê số lượng vật tư theo phòng theo trạng thái",
            "Thống kê số tiền bồi thường theo phòng",
            "Thống kê số tiền bồi thường theo loại phòng",
            "Thống kê số tiền bồi thường theo loại phòng theo tháng"
    };
    public String[] dsSqlVatTu = {
            "SELECT ma_phong, SUM(so_luong) FROM chi_tiet_phong GROUP BY ma_phong ORDER BY ma_phong ASC",
            "",
            "SELECT ma_phong, SUM(boi_thuong) FROM boi_thuong GROUP BY ma_phong ORDER BY ma_phong ASC",
            "SELECT loai_phong, SUM(boi_thuong) FROM boi_thuong, loai_phong, phong WHERE boi_thuong.ma_phong=phong.ma_phong AND phong.ma_loai_phong=loai_phong.ma_loai_phong GROUP BY loai_phong"
            ""
    };

    public String[] dsTkNhanVien = {
            "Thống kê nhân viên theo giới tính",
            "Thống kê nhân viên theo loại nhân viên"
    };
    public String[] dsSqlNhanVien = {
            "SELECT CASE WHEN gioi_tinh=1 THEN N'Nam' WHEN gioi_tinh=0 THEN N'Nu' END AS [Giới tính], COUNT(*) [Số lượng] FROM nhan_vien GROUP BY gioi_tinh",
            "SELECT CASE WHEN loai_nv=0 THEN N'Quản lý' WHEN loai_nv=1 THEN N'Lễ tân' END AS [Loại nhân viên], COUNT(*) [Số lượng] FROM nhan_vien GROUP BY loai_nv"
    }; // DONE

    public String[] dsTkDoanhThu = {
            "Thống kê doanh thu theo tháng",
            "Thống kê doanh thu theo năm",
            "Thống kê doanh thu theo loại phòng",
            "Thống kê doanh thu theo phòng",
            "Thống kê doanh thu theo tháng theo loại phòng",
            "Thống kê doanh thu theo năm theo loại phòng",
            "Thống kê doanh thu theo tháng theo phòng",
            "Thống kê doanh thu theo năm theo phòng"
    };
    public String[] dsSqlDoanhThu = {
            "SELECT FORMAT(dp.ngay_checkout_tt, 'yyyy-MM') [Tháng], SUM(thanh_tien) [Doanh thu] FROM hoa_don hd, dat_phong dp WHERE hd.ma_dat_phong = dp.ma_dat_phong GROUP BY FORMAT(dp.ngay_checkout_tt, 'yyyy-MM') ORDER BY FORMAT(dp.ngay_checkout_tt, 'yyyy-MM') DESC",
            "SELECT FORMAT(dp.ngay_checkout_tt, 'yyyy') [Năm], SUM(thanh_tien) [Doanh thu] FROM hoa_don hd, dat_phong dp WHERE hd.ma_dat_phong = dp.ma_dat_phong GROUP BY FORMAT(dp.ngay_checkout_tt, 'yyyy') ORDER BY FORMAT(dp.ngay_checkout_tt, 'yyyy') DESC",
            "SELECT lp.loai_phong [Loại phòng], SUM(ctdp.thanh_tien) [Doanh thu]\n" +
                    "FROM phong p, chi_tiet_dat_phong ctdp, loai_phong lp \n" +
                    "WHERE ctdp.ma_phong=p.ma_phong AND p.ma_loai_phong=lp.ma_loai_phong\n" +
                    "GROUP BY lp.ma_loai_phong",
            "SELECT ma_phong [Phòng], SUM(ctdp.thanh_tien) [Doanh thu] FROM chi_tiet_dat_phong ctdp GROUP BY ma_phong",
            "",
            "",
            "",
            "",
    };


    public void init(KhungUngDung c) {
        // Combo
        phongCombo.getItems().addAll(dsTkPhong);
        loaiPhongCombo.getItems().addAll(dsTkLoaiPhong);
        giaPhongCombo.getItems().addAll(dsTkGiaPhong);
        dichVuCombo.getItems().addAll(dsTkDichVu);
        khachHangCombo.getItems().addAll(dsTkKhachHang);
        vatTuCombo.getItems().addAll(dsTkVatTu);
        nhanVienCombo.getItems().addAll(dsTkNhanVien);
        doanhThuCombo.getItems().addAll(dsTkDoanhThu);

        // Buttons
        setAction(xemPhong, phongCombo, dsSqlPhong, dsTkPhong);
        setAction(xemLoaiPhong, loaiPhongCombo, dsSqlLoaiPhong, dsTkLoaiPhong);
        setAction(xemGiaPhong, giaPhongCombo, dsSqlGiaPhong, dsTkGiaPhong);
        setAction(xemDichVu, dichVuCombo, dsSqlDichVu, dsTkDichVu);
        setAction(xemKhachHang, khachHangCombo, dsSqlKhachHang, dsTkKhachHang);
        setAction(xemVatTu, vatTuCombo, dsSqlVatTu, dsTkVatTu);
        setAction(xemNhanVien, nhanVienCombo, dsSqlNhanVien, dsTkNhanVien);
        setAction(xemDoanhThu, doanhThuCombo, dsSqlDoanhThu, dsTkDoanhThu);

    }

    private void setAction(Button button, ChoiceBox<String> comboBox, String[] sql, String[] titles) {
        button.setOnAction(event -> {
            int index = comboBox.getSelectionModel().getSelectedIndex();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/thongKe/mauBaoCaoChung.fxml"));
            try {
                Parent editRoot = loader.load();
                new JMetro(editRoot, Style.LIGHT);

                Stage stage = new Stage();
                Scene scene = new Scene(editRoot);
                stage.setTitle(titles[index]);
                stage.setScene(scene);
                stage.setResizable(false);
                MauBaoCao mauBaoCao = loader.getController();
                mauBaoCao.init(sql[index], titles[index]);

                stage.showAndWait();
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
        });
    }
}
