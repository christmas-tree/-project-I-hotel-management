package controller.khachSan;

import controller.basic.KhungUngDung;
import dao.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.*;
import util.AlertGenerator;
import util.ExceptionHandler;
import util.Exporter;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TraPhongKhachDoan {

    @FXML
    private TableColumn<ChiTietDichVu, String> donGiaDvCol;

    @FXML
    private TableColumn<TienPhong, Integer> soNgayPCol;

    @FXML
    private Button luuBtn;

    @FXML
    private TableView<TienPhong> tienPhongTable;

    @FXML
    private Label maDPLabel;

    @FXML
    private Label gioVaoLabel;

    @FXML
    private Label thanhToanLabel;

    @FXML
    private Button inBtn;

    @FXML
    private TableColumn<ChiTietDichVu, String> donViDvCol;

    @FXML
    private Button huyBtn;

    @FXML
    private TextField datCocField;

    @FXML
    private Button kiemDoBtn;

    @FXML
    private TableColumn<BoiThuong, String> tenDoBtCol;

    @FXML
    private TableColumn<BoiThuong, Integer> sttBtCol;

    @FXML
    private TableColumn<ChiTietDichVu, Integer> soLuongDvCol;

    @FXML
    private Label khachHangLabel;

    @FXML
    private TableColumn<ChiTietDichVu, String> thanhTienDvCol;

    @FXML
    private TableColumn<TienPhong, String> tenPCol;

    @FXML
    private TableColumn<TienPhong, String> thanhTienPCol;

    @FXML
    private TableColumn<TienPhong, Integer> phongPCol;

    @FXML
    private TableColumn<ChiTietDichVu, Integer> phongBtCol;

    @FXML
    private TableColumn<BoiThuong, Integer> phongDvCol;

    @FXML
    private TableColumn<BoiThuong, String> donGiaBtCol;

    @FXML
    private TableColumn<TienPhong, String> donGiaPCol;

    @FXML
    private TableView<BoiThuong> boiThuongTable;

    @FXML
    private Label thanhTienTongLabel;

    @FXML
    private Label tienCocTongLabel;

    @FXML
    private TableColumn<ChiTietDichVu, String> tenDvCol;

    @FXML
    private Label gioRaLabel;

    @FXML
    private TableView<ChiTietDichVu> dichVuTable;

    @FXML
    private TableColumn<ChiTietDichVu, Integer> sttDvCol;

    @FXML
    private TableColumn<BoiThuong, Integer> soLuongBtCol;

    @FXML
    private TableColumn<BoiThuong, String> trangThaiBtCol;

    @FXML
    private TableColumn<BoiThuong, String> thanhTienBtCol;

    @FXML
    private TableColumn<TienPhong, Integer> sttPCol;

    private ObservableList<BoiThuong> dsBoiThuong;
    private ObservableList<TienPhong> dsTienPhong;
    private DatPhong datPhong;
    private ObservableList<ChiTietDatPhong> dsChiTietDatPhong;
    private ObservableList<ChiTietDichVu> dsChiTietDichVu;

    private long tienCoc = 0;
    private long thanhTien = 0;
    private long thanhToan = 0;
    private ArrayList<HoaDon> dsCtHoaDon;
    Timestamp now = new Timestamp(System.currentTimeMillis());
    private NhanVien nhanVien;

    public void init(DatPhong datPhong, ArrayList<Phong> dsPhong, KhungUngDung c) {

        nhanVien = c.currentUser;
        this.datPhong = datPhong;
        dsChiTietDichVu = ChiTietDichVuDAO.getInstance().getAll(datPhong, dsPhong);
        dsChiTietDatPhong = ChiTietDatPhongDAO.getInstance().getAll(datPhong, dsPhong);
        datPhong.setNgayCheckoutTt(now);
        dsTienPhong = FXCollections.observableArrayList();
        for (ChiTietDatPhong chiTietDatPhong: dsChiTietDatPhong) {
            dsTienPhong.addAll(chiTietDatPhong.getDsGia());
        }

        // FIELDS
        datCocField.textProperty().addListener((observableValue, s, t1) -> {
            datCocField.setText(t1.replaceAll("[^\\d]", ""));
            tinhTien();
        });

        // INIT TABLE
        sttBtCol.setCellFactory(col -> new TableCell<>() {
            @Override
            public void updateIndex(int index) {
                super.updateIndex(index);
                if (isEmpty() || index < 0)
                    setText(null);
                else
                    setText(Integer.toString(index + 1));
            }
        });
        phongBtCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPhong().getMaPhong()));
        tenDoBtCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getChiTietPhong().getTenDo()));
        trangThaiBtCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getTrangThaiString()));
        soLuongBtCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getSoLuong()));
        donGiaBtCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getChiTietPhong().getGiaTien())));
        thanhTienBtCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getChiTietPhong().getGiaTien() * p.getValue().getSoLuong())));

        sttPCol.setCellFactory(col -> new TableCell<>() {
            @Override
            public void updateIndex(int index) {
                super.updateIndex(index);
                if (isEmpty() || index < 0)
                    setText(null);
                else
                    setText(Integer.toString(index + 1));
            }
        });
        phongPCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPhong().getMaPhong()));
        tenPCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getTen()));
        donGiaPCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getDonGia())));
        soNgayPCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getSoNgay()));
        thanhTienPCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getThanhTien())));

        sttDvCol.setCellFactory(col -> new TableCell<>() {
            @Override
            public void updateIndex(int index) {
                super.updateIndex(index);
                if (isEmpty() || index < 0)
                    setText(null);
                else
                    setText(Integer.toString(index + 1));
            }
        });
        phongDvCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getChiTietPhong().getPhong().getMaPhong()));
        tenDvCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getDichVu().getTenDv()));
        donGiaDvCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getDichVu().getGiaDv())));
        soLuongDvCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getSoLuong()));
        donViDvCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getDichVu().getDonVi()));
        thanhTienDvCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getThanhTien())));

        tienPhongTable.setItems(dsTienPhong);
        dichVuTable.setItems(dsChiTietDichVu);

        // Fields
        khachHangLabel.setText(datPhong.getKhachHang().getTenKhach());
        gioVaoLabel.setText(datPhong.getNgayCheckinTt().toString());
        gioRaLabel.setText(datPhong.getNgayCheckoutTt().toString());
        maDPLabel.setText(String.format("%06d", datPhong.getMaDatPhong()));

        kiemDoBtn.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/khachSan/kiemTraCheckOut.fxml"));
            try {
                Parent editRoot = loader.load();
                new JMetro(editRoot, Style.LIGHT);

                Stage stage = new Stage();
                Scene scene = new Scene(editRoot);
                stage.setTitle("Kiểm phòng");
                stage.setScene(scene);
                stage.setResizable(false);
                KiemPhong kiemPhong = loader.getController();
                kiemPhong.init(dsChiTietDatPhong);

                stage.showAndWait();
                dsBoiThuong = kiemPhong.getDsBoiThuong();
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
            boiThuongTable.setItems(dsBoiThuong);
            tinhTien();
        });

        luuBtn.setOnAction(event -> save());
        huyBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());
    }

    public void print() {
        Exporter.taoHoaDon(datPhong, dsCtHoaDon, nhanVien, boiThuongTable);
    }

    public void save() {
        for (ChiTietDatPhong chiTietDatPhong : dsChiTietDatPhong) {
            chiTietDatPhong.getPhong().setTrangThai(Phong.DANGDON);
        }

        for (TienPhong tienPhong : dsTienPhong) {
            dsCtHoaDon.add(new HoaDon(
                    datPhong.getMaDatPhong(),
                    tienPhong.getTen(),
                    tienPhong.getDonGia(),
                    tienPhong.getSoNgay(),
                    "ngày",
                    tienPhong.getThanhTien()
            ));
        }

        for (ChiTietDichVu chiTietDichVu : dsChiTietDichVu) {
            dsCtHoaDon.add(new HoaDon(
                    datPhong.getMaDatPhong(),
                    chiTietDichVu.getDichVu().getTenDv() + new SimpleDateFormat("dd.MM.yy HH:mm").format(chiTietDichVu.getNgayDv()) + " Phòng " + chiTietDichVu.getPhong().getMaPhong(),
                    chiTietDichVu.getDichVu().getGiaDv(),
                    chiTietDichVu.getSoLuong(),
                    chiTietDichVu.getDichVu().getDonVi(),
                    chiTietDichVu.getThanhTien()
            ));
        }
        if (dsBoiThuong != null)
            for (BoiThuong boiThuong : dsBoiThuong) {
                dsCtHoaDon.add(new HoaDon(
                        datPhong.getMaDatPhong(),
                        boiThuong.getChiTietPhong().getTenDo() + " " + boiThuong.getTrangThaiString(),
                        null,
                        boiThuong.getSoLuong(),
                        boiThuong.getChiTietPhong().getDonVi(),
                        boiThuong.getBoiThuong()
                ));
            }
        if (PhongDAO.getInstance().update(dsChiTietDatPhong)
                && DatPhongDAO.getInstance().update(datPhong)
                && HoaDonDAO.getInstance().create(dsCtHoaDon)) {
            AlertGenerator.success("Trả phòng thành công");
        } else {
            AlertGenerator.error("Trả phòng thất bại");
        }
    }

    public void tinhTien() {
        thanhTien = 0;
        // Tien phong
        for (TienPhong tienPhong : dsTienPhong) {
            thanhTien += tienPhong.getThanhTien();
        }

        if (dsBoiThuong != null) {
            for (BoiThuong boiThuong : dsBoiThuong) {
                thanhTien += boiThuong.getBoiThuong();
            }
        }
        for (ChiTietDichVu chiTietDichVu : dsChiTietDichVu) {
            thanhTien += chiTietDichVu.getThanhTien();
        }

        tienCoc = Long.parseLong(datCocField.getText());
        thanhToan = thanhTien - tienCoc;

        thanhTienTongLabel.setText(String.format("%,d", thanhTien));
        tienCocTongLabel.setText(String.format("%,d", tienCoc));
        thanhToanLabel.setText(String.format("%,d", thanhToan));
    }
}