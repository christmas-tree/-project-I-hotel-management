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
    };

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
    };

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


    };

    public String[] dsTkDichVu = {

    };
    public String[] dsSqlDichVu = {

    };

    public String[] dsTkKhachHang = {

    };
    public String[] dsSqlKhachHang = {

    };

    public String[] dsTkVatTu = {

    };
    public String[] dsSqlVatTu = {

    };

    public String[] dsTkNhanVien = {

    };
    public String[] dsSqlNhanVien = {

    };

    public String[] dsTkDoanhThu = {

    };
    public String[] dsSqlDoanhThu = {

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
