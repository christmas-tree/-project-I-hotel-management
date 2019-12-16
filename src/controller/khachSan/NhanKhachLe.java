package controller.khachSan;

import controller.khachHang.SuaKhachHang;
import dao.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.*;
import util.ExHandler;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class NhanKhachLe {
    @FXML
    private DatePicker ngayVaoDuKienDate;

    @FXML
    private TextField ngayVaoThucTeTimeField;

    @FXML
    private Button luuBtn;

    @FXML
    private TableColumn<KhachHang, Long> cmndCol;

    @FXML
    private TextField ngayRaDuKienTimeField;

    @FXML
    private TextField heSoGiamGiaField;

    @FXML
    private ComboBox<KhachHang> khachDatCombo;

    @FXML
    private Button huyBtn;

    @FXML
    private TextField datCocField;

    @FXML
    private DatePicker ngayRaDuKienDate;

    @FXML
    private TableColumn<KhachHang, String> gioiTinhCol;

    @FXML
    private TableView<KhachHang> khachOTable;

    @FXML
    private ComboBox<NhanVien> nhanVienLeTanCombo;

    @FXML
    private TableColumn<KhachHang, String> tenCol;

    @FXML
    private ComboBox<Phong> phongCombo;

    @FXML
    private ComboBox<NhanVien> nhanVienDatCombo;

    @FXML
    private TextField giaField;

    @FXML
    private ComboBox<KhachHang> khachOCombo;

    @FXML
    private TextArea ghiChuField;

    @FXML
    private TextField heSoNgayLeField;

    @FXML
    private TextField ngayVaoDuKienTimeField;

    @FXML
    private Button themKhachOBtn;

    @FXML
    private DatePicker ngayVaoThucTeDate;

    @FXML
    private TextField phuongThucField;

    @FXML
    private Button themKhachDatBtn;

    @FXML
    private Button layGioBtn;

    @FXML
    private Button themKhachVaoPhongBtn;

    @FXML
    private Button xoaKhachVaoPhongBtn;

    @FXML
    private Label maDatPhongLabel;

    @FXML
    private Button huyDatBtn;

    private ObservableList<NhanVien> dsNhanVien;
    private ObservableList<Phong> dsPhong;
    private ObservableList<KhachHang> dsKhachHang;

    private ObservableList<KhachHang> dsKhachOPhong;
    private ArrayList<KhachHang> dsXoa;
    private DatPhong datPhong;
    private ChiTietDatPhong chiTietDatPhong;

    public void init() {
        uiInit();
        luuBtn.setOnAction(event -> add());

    }

    public void init(Phong phong) {
        phongCombo.setDisable(true);

        chiTietDatPhong = ChiTietDatPhongDAO.getInstance().getByPhong(phong);
        datPhong = chiTietDatPhong.getDatPhong();

        maDatPhongLabel.setText(String.format("%06d", datPhong.getMaDatPhong()));
        phongCombo.getSelectionModel().select(chiTietDatPhong.getPhong());
        giaField.setText(String.valueOf(chiTietDatPhong.getPhong().getLoaiPhong().getGiaTien()));
        LocalDateTime ngayVao = datPhong.getNgayCheckin().toLocalDateTime();
        ngayVaoDuKienDate.setValue(ngayVao.toLocalDate());
        ngayVaoThucTeTimeField.setText(ngayVao.toLocalTime().toString());
        LocalDateTime ngayRa = datPhong.getNgayCheckout().toLocalDateTime();
        ngayRaDuKienDate.setValue(ngayRa.toLocalDate());
        ngayRaDuKienTimeField.setText(ngayRa.toLocalTime().toString());
        phuongThucField.setText(datPhong.getPhuongThuc());
        datCocField.setText(String.valueOf(datPhong.getTienDatCoc()));

        heSoNgayLeField.setText(String.valueOf(chiTietDatPhong.getHeSoNgayLe()));
        heSoGiamGiaField.setText(String.valueOf(chiTietDatPhong.getHeSoKhuyenMai()));

        ghiChuField.setText(datPhong.getGhiChu());

        nhanVienDatCombo.getSelectionModel().select(datPhong.getNvNhan());
        nhanVienLeTanCombo.getSelectionModel().select(chiTietDatPhong.getNvLeTan());

        if (chiTietDatPhong.getNgayCheckinTt() != null) {
            LocalDateTime ngay = chiTietDatPhong.getNgayCheckinTt().toLocalDateTime();
            ngayVaoThucTeDate.setValue(ngay.toLocalDate());
            ngayVaoThucTeTimeField.setText(ngay.toLocalTime().toString());
        }

        khachDatCombo.getSelectionModel().select(datPhong.getKhachHang());

        dsKhachOPhong = FXCollections.observableArrayList(chiTietDatPhong.getDsKhachHang());

        if (chiTietDatPhong.getNgayCheckinTt() == null)
            huyDatBtn.setVisible(true);

        uiInit();

        luuBtn.setOnAction(event -> update());
    }

    public void uiInit() {
        // COMBOBOX
        Runnable task = () -> {
            dsPhong = FXCollections.observableArrayList(PhongDAO.getInstance().getAll(LoaiPhongDAO.getInstance().getMap()));
            dsKhachHang = FXCollections.observableArrayList(KhachHangDAO.getInstance().getAll());
            dsNhanVien = FXCollections.observableArrayList(NhanVienDAO.getInstance().getAll());
            Platform.runLater(() -> {
                phongCombo.getItems().addAll(dsPhong);
                khachDatCombo.getItems().addAll(dsKhachHang);
                nhanVienDatCombo.getItems().addAll(dsNhanVien);
                nhanVienLeTanCombo.getItems().addAll(dsNhanVien);
                khachOCombo.getItems().addAll(dsKhachHang);
            });
        };
        new Thread(task).start();

        // FIELD CHECKER
        forceNumberOnly(giaField, heSoGiamGiaField, heSoNgayLeField);

        // BUTTONS
        themKhachDatBtn.setOnAction(event -> {
            khachDatCombo.getSelectionModel().select(addKhachHang());
        });

        themKhachOBtn.setOnAction(event -> {
            khachOCombo.getSelectionModel().select(addKhachHang());
        });

        layGioBtn.setOnAction(event -> {
            ngayVaoThucTeDate.setValue(LocalDate.now());
            ngayVaoThucTeTimeField.setText(LocalTime.now().toString());
        });

        themKhachVaoPhongBtn.setOnAction(event -> {
            if (khachOCombo.getSelectionModel().isEmpty()) {
                Alert resultAlert = new Alert(Alert.AlertType.ERROR);
                resultAlert.setTitle("Lỗi!");
                resultAlert.setContentText("Bạn chưa chọn khách nào.");
                resultAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                resultAlert.showAndWait();
            } else {
                dsKhachOPhong.add(khachOCombo.getSelectionModel().getSelectedItem());
            }
        });

        xoaKhachVaoPhongBtn.setOnAction(event -> {
            KhachHang focusedKhachHang = khachOTable.getSelectionModel().getSelectedItem();
            if (focusedKhachHang == null) {
                Alert resultAlert = new Alert(Alert.AlertType.ERROR);
                resultAlert.setTitle("Lỗi!");
                resultAlert.setContentText("Bạn chưa chọn khách nào.");
                resultAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                resultAlert.showAndWait();
            } else {
                dsKhachOPhong.remove(focusedKhachHang);
                dsXoa.add(focusedKhachHang);
            }
        });

        huyBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());

        // TABLE
        tenCol.setCellValueFactory(new PropertyValueFactory<>("tenKhach"));
        gioiTinhCol.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        cmndCol.setCellValueFactory(new PropertyValueFactory<>("cmnd"));

        khachOTable.setItems(dsKhachOPhong);
    }

    public void add() {
        if (validate()) {
            datPhong = new DatPhong(
                    new Timestamp(System.currentTimeMillis()),
                    phuongThucField.getText(),
                    Timestamp.valueOf(LocalDateTime.of(ngayVaoDuKienDate.getValue(), LocalTime.parse(ngayVaoDuKienTimeField.getText()))),
                    ngayRaDuKienTimeField.getText().isBlank() ? null : Timestamp.valueOf(LocalDateTime.of(ngayRaDuKienDate.getValue(), LocalTime.parse(ngayRaDuKienTimeField.getText()))),
                    khachDatCombo.getSelectionModel().getSelectedItem(),
                    nhanVienDatCombo.getSelectionModel().getSelectedItem(),
                    0,
                    Long.parseLong(datCocField.getText()),
                    false,
                    false,
                    ghiChuField.getText()
            );
            chiTietDatPhong = new ChiTietDatPhong(
                    datPhong,
                    phongCombo.getSelectionModel().getSelectedItem(),
                    ngayVaoThucTeTimeField.getText().isBlank() ? null : Timestamp.valueOf(LocalDateTime.of(ngayVaoThucTeDate.getValue(), LocalTime.parse(ngayVaoThucTeTimeField.getText()))),
                    nhanVienLeTanCombo.getSelectionModel().getSelectedItem(),
                    heSoNgayLeField.getText().isBlank() ? 0 : Float.parseFloat(heSoNgayLeField.getText()),
                    heSoGiamGiaField.getText().isBlank() ? 0 : Float.parseFloat(heSoGiamGiaField.getText()),
                    ghiChuField.getText()
            );

            if (DatPhongDAO.getInstance().create(datPhong)
                    && ChiTietDatPhongDAO.getInstance().create(chiTietDatPhong)
                    && ChiTietDatPhongDAO.getInstance().updateDsKhachO(chiTietDatPhong, dsKhachOPhong, dsXoa)
            ) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công!");
                alert.setContentText("Đặt phòng thành công.");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.showAndWait();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thất bại!");
            alert.setContentText("Đặt phòng thất bại.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    public void update() {
        if (validate()) {
            datPhong.setProps(
                    phuongThucField.getText(),
                    ngayVaoDuKienTimeField.getText().isBlank() ? null : Timestamp.valueOf(LocalDateTime.of(ngayVaoDuKienDate.getValue(), LocalTime.parse(ngayVaoDuKienTimeField.getText()))),
                    ngayRaDuKienTimeField.getText().isBlank() ? null : Timestamp.valueOf(LocalDateTime.of(ngayRaDuKienDate.getValue(), LocalTime.parse(ngayRaDuKienTimeField.getText()))),
                    khachDatCombo.getSelectionModel().getSelectedItem(),
                    nhanVienDatCombo.getSelectionModel().getSelectedItem(),
                    0,
                    Long.parseLong(datCocField.getText()),
                    false,
                    ghiChuField.getText()
            );

            chiTietDatPhong.setProps(
                    ngayVaoThucTeTimeField.getText().isBlank() ? null : Timestamp.valueOf(LocalDateTime.of(ngayVaoThucTeDate.getValue(), LocalTime.parse(ngayVaoThucTeTimeField.getText()))),
                    null,
                    nhanVienLeTanCombo.getSelectionModel().getSelectedItem(),
                    heSoNgayLeField.getText().isBlank() ? 0 : Float.parseFloat(heSoNgayLeField.getText()),
                    heSoGiamGiaField.getText().isBlank() ? 0 : Float.parseFloat(heSoGiamGiaField.getText()),
                    ghiChuField.getText()
            );

            if (DatPhongDAO.getInstance().update(datPhong)
                    && ChiTietDatPhongDAO.getInstance().update(chiTietDatPhong)
                    && ChiTietDatPhongDAO.getInstance().updateDsKhachO(chiTietDatPhong, dsKhachOPhong, dsXoa)
            ) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công!");
                alert.setContentText("Cập nhật thông tin đặt phòng thành công.");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.showAndWait();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thất bại!");
            alert.setContentText("Cập nhật thông tin đặt phòng thất bại.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    public void forceNumberOnly(TextField... fields) {
        for (int i = 0; i < fields.length; i++) {
            TextField textField = fields[i];
            textField.textProperty().addListener((observableValue, s, t1) -> {
                textField.setText(t1.replaceAll("[^\\.0-9]", ""));
            });
        }
    }

    public KhachHang addKhachHang() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/khachHang/suaKhachHang.fxml"));
            Parent editRoot = loader.load();
            new JMetro(editRoot, Style.LIGHT);

            Stage stage = new Stage();
            Scene scene = new Scene(editRoot);

            stage.setTitle("Thêm khách hàng");
            stage.setScene(scene);
            stage.setResizable(false);

            SuaKhachHang suaKhachHang = loader.getController();
            suaKhachHang.init();

            stage.showAndWait();
            KhachHang khachHangMoi = suaKhachHang.getKhachHangMoi();
            dsKhachHang.add(khachHangMoi);
            return khachHangMoi;
        } catch (IOException e) {
            ExHandler.handle(e);
            return null;
        }
    }

    public boolean validate() {
        String err = "";
        if (phongCombo.getSelectionModel().isEmpty()) {
            err += "Không được bỏ trống số phòng.\n";
        }
        if (giaField.getText().isBlank()) {
            err += "Không được bỏ trống giá.\n";
        }
        if (datCocField.getText().isBlank()) {
            err += "Không được bỏ trống tiền đặt cọc.\n";
        }
        if (ngayVaoDuKienDate.getValue() == null) {
            err += "Không được bỏ trống ngày vào dự kiến.\n";
        }
        if (ngayVaoDuKienTimeField.getText().isBlank()) {
            err += "Không được bỏ trống giờ vào dự kiến.\n";
        }
        if (ngayRaDuKienDate.getValue() == null) {
            err += "Không được bỏ trống ngày ra dự kiến.\n";
        }
        if (ngayRaDuKienTimeField.getText().isBlank()) {
            err += "Không được bỏ trống giờ ra dự kiến.\n";
        }
        if (khachDatCombo.getSelectionModel().isEmpty()) {
            err += "Không được bỏ trống khách đặt.\n";
        }
        if (nhanVienDatCombo.getSelectionModel().isEmpty()) {
            err += "Không được bỏ trống nhân viên đặt.\n";
        }

        if (err.isBlank())
            return true;
        else {
            ExHandler.handle(new Exception(err));
            return false;
        }
    }
}
