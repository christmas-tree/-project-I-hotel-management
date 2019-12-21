package controller.khachSan;

import controller.basic.IndexController;
import controller.khachHang.SuaKhachHang;
import dao.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import util.AlertGenerator;
import util.ExHandler;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class NhanKhachDoan {

    @FXML
    private Button luuBtn;

    @FXML
    private Label maDatPhongLabel;

    @FXML
    private Button newXoaBtn;

    @FXML
    private TextField heSoGiamGiaField;

    @FXML
    private TextField gioVaoTTField;

    @FXML
    private ComboBox<KhachHang> khachDatCombo;

    @FXML
    private Button huyBtn;

    @FXML
    private TextField gioVaoDKField;

    @FXML
    private ComboBox<NhanVien> nhanVienLeTanCombo;

    @FXML
    private ComboBox<Phong> newPhongCombo;

    @FXML
    private TextField newLoaiPhongField;

    @FXML
    private DatePicker ngayVaoDKDate;

    @FXML
    private TextField gioRaDKField;

    @FXML
    private DatePicker ngayVaoTTDate;

    @FXML
    private ComboBox<NhanVien> nhanVienDatCombo;

    @FXML
    private Button newThemBtn;

    @FXML
    private Button themKhachDatBtn;

    @FXML
    private TextField newGiaFIeld;

    @FXML
    private TextField tienCocField;

    @FXML
    private TextArea ghiChuArea;

    @FXML
    private TextField heSoNgayLeField;

    @FXML
    private TableView<ChiTietDatPhong> newPhongTable;

    @FXML
    private TableColumn<ChiTietDatPhong, Integer> newPhongCol;

    @FXML
    private TableColumn<ChiTietDatPhong, String> newGiaTienCol;

    @FXML
    private TableColumn<ChiTietDatPhong, String> newLoaiPhongCol;

    @FXML
    private TableColumn<ChiTietDatPhong, NhanVien> newLeTanCol;
    @FXML
    private TableColumn<ChiTietDatPhong, Timestamp> newNgayCheckinCol;

    @FXML
    private TextField phuongThucField;

    @FXML
    private DatePicker ngayRaDKDate;

    @FXML
    private Button layGioBtn;

    private ObservableList<Phong> dsPhong;
    private ObservableList<KhachHang> dsKhachHang;
    private ObservableList<NhanVien> dsNhanVien;

    private DatPhong datPhong;
    private ObservableList<ChiTietDatPhong> dsChiTietDatPhong;

    private ArrayList<ChiTietDatPhong> dsXoa = new ArrayList<>();

    public void init() {
        datPhong = new DatPhong();
        dsChiTietDatPhong = FXCollections.observableArrayList();
    }

    public void uiInit() {
        // COMBOBOX
        Runnable task = () -> {
            dsPhong = FXCollections.observableArrayList(PhongDAO.getInstance().getAll(LoaiPhongDAO.getInstance().getMap()));
            dsKhachHang = FXCollections.observableArrayList(KhachHangDAO.getInstance().getAll());
            dsNhanVien = FXCollections.observableArrayList(NhanVienDAO.getInstance().getAll());
            Platform.runLater(() -> {
                for (int i = 0; i < dsPhong.size(); i++) {
                    if (dsPhong.get(i).getTrangThai() == Phong.SANSANG)
                        newPhongCombo.getItems().add(dsPhong.get(i));
                }
                khachDatCombo.setItems(dsKhachHang);
                nhanVienDatCombo.getItems().addAll(dsNhanVien);
                nhanVienLeTanCombo.getItems().addAll(dsNhanVien);
            });
        };
        new Thread(task).start();

        // FIELD CHECKER
        forceNumberOnly(tienCocField, heSoGiamGiaField, heSoNgayLeField);

        // BUTTONS
        themKhachDatBtn.setOnAction(event -> {
            khachDatCombo.getSelectionModel().select(addKhachHang());
        });

        layGioBtn.setOnAction(event -> {
            ngayVaoTTDate.setValue(LocalDate.now());
            gioVaoTTField.setText(LocalTime.now().toString());
        });

        newThemBtn.setOnAction(event -> {
            if (validateDetail()) {
                Phong phong = newPhongCombo.getSelectionModel().getSelectedItem();
                phong.setTrangThai(Phong.DANGSUDUNG);
                newPhongCombo.getSelectionModel().clearSelection();
                newPhongCombo.getItems().remove(phong);
                dsChiTietDatPhong.add(new ChiTietDatPhong(
                        datPhong,
                        phong,
                        gioVaoTTField.getText().isBlank()? null : Timestamp.valueOf(LocalDateTime.of(ngayVaoTTDate.getValue(), LocalTime.parse(gioVaoTTField.getText()))),
                        nhanVienLeTanCombo.getSelectionModel().getSelectedItem(),
                        heSoNgayLeField.getText().isEmpty() ? 1 : Float.parseFloat(heSoNgayLeField.getText()),
                        heSoGiamGiaField.getText().isEmpty() ? 1 : Float.parseFloat(heSoGiamGiaField.getText())
                ));
            }
        });

        newXoaBtn.setOnAction(event -> {
            ChiTietDatPhong focusedPhong = newPhongTable.getSelectionModel().getSelectedItem();
            if (focusedPhong == null) {
                Alert resultAlert = new Alert(Alert.AlertType.ERROR);
                resultAlert.setTitle("Lỗi!");
                resultAlert.setContentText("Bạn chưa chọn phòng nào.");
                resultAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                resultAlert.showAndWait();
            } else {
                dsChiTietDatPhong.remove(focusedPhong);
                dsXoa.add(focusedPhong);
            }
        });

        huyBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());

        // FIELD RUNNER
        newPhongCombo.selectionModelProperty().addListener((observableValue, phongSingleSelectionModel, t1) -> {
            newLoaiPhongField.setText(t1.getSelectedItem().getLoaiPhong().getLoaiPhong());
            newGiaFIeld.setText(String.format("%,3d", t1.getSelectedItem().getLoaiPhong().getGiaTien()));
        });

        // TABLE
        newPhongCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPhong().getMaPhong()));
        newLoaiPhongCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPhong().getLoaiPhong().getLoaiPhong()));
        newGiaTienCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getPhong().getLoaiPhong().getGiaTien())));
        newLeTanCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getNvLeTan()));
        newNgayCheckinCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getNgayCheckinTt()));

        newPhongTable.setItems(dsChiTietDatPhong);
    }

    public void forceNumberOnly(TextField... fields) {
        for (int i = 0; i < fields.length; i++) {
            TextField textField = fields[i];
            textField.textProperty().addListener((observableValue, s, t1) -> {
                textField.setText(t1.replaceAll("[^\\.0-9]", ""));
            });
        }
    }

    public void add() {
        if (validate()) {
            datPhong.setProps(
                    new Timestamp(System.currentTimeMillis()),
                    phuongThucField.getText(),
                    Timestamp.valueOf(LocalDateTime.of(ngayVaoDKDate.getValue(), LocalTime.parse(gioVaoDKField.getText()))),
                    Timestamp.valueOf(LocalDateTime.of(ngayRaDKDate.getValue(), LocalTime.parse(gioRaDKField.getText()))),
                    khachDatCombo.getSelectionModel().getSelectedItem(),
                    nhanVienDatCombo.getSelectionModel().getSelectedItem(),
                    0,
                    Long.parseLong(tienCocField.getText()),
                    true,
                    false,
                    ghiChuArea.getText()
            );
            datPhong.setDsChiTietDatPhong(dsChiTietDatPhong);

            if (DatPhongDAO.getInstance().create(datPhong)
                    && ChiTietDatPhongDAO.getInstance().create(dsChiTietDatPhong)
                    && PhongDAO.getInstance().update(dsChiTietDatPhong)
            ) {
                AlertGenerator.success("Đặt phòng thành công.");
                return;
            }
            AlertGenerator.error("Đặt phòng thất bại");
        }
    }

    public void update() {
        if (validate()) {
            datPhong.setProps(
                    phuongThucField.getText(),
                    Timestamp.valueOf(LocalDateTime.of(ngayVaoDKDate.getValue(), LocalTime.parse(gioVaoDKField.getText()))),
                    Timestamp.valueOf(LocalDateTime.of(ngayRaDKDate.getValue(), LocalTime.parse(gioRaDKField.getText()))),
                    khachDatCombo.getSelectionModel().getSelectedItem(),
                    nhanVienDatCombo.getSelectionModel().getSelectedItem(),
                    0,
                    Long.parseLong(tienCocField.getText()),
                    false,
                    ghiChuArea.getText()
            );

            if (DatPhongDAO.getInstance().create(datPhong)
                    && ChiTietDatPhongDAO.getInstance().update(dsChiTietDatPhong)
                    && ChiTietDatPhongDAO.getInstance().delete(dsXoa)
                    && PhongDAO.getInstance().update(dsChiTietDatPhong)
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
            if (khachHangMoi != null) {
                dsKhachHang.add(khachHangMoi);
            }
            return khachHangMoi;
        } catch (IOException e) {
            ExHandler.handle(e);
            return null;
        }
    }

    public boolean validate() {
        String err = "";
        if (tienCocField.getText().isBlank()) {
            err += "Không được bỏ trống tiền đặt cọc.\n";
        }
        if (ngayVaoDKDate.getValue() == null) {
            err += "Không được bỏ trống ngày vào dự kiến.\n";
        }
        if (gioVaoDKField.getText().isBlank()) {
            err += "Không được bỏ trống giờ vào dự kiến.\n";
        }
        if (ngayRaDKDate.getValue() == null) {
            err += "Không được bỏ trống ngày ra dự kiến.\n";
        }
        if (gioRaDKField.getText().isBlank()) {
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
            AlertGenerator.error(err);
            return false;
        }
    }

    public boolean validateDetail() {
        String err = "";
        if (newPhongCombo.getSelectionModel().isEmpty()) {
            err += "Không được bỏ trống phòng.\n";
        }
        if (nhanVienLeTanCombo.getSelectionModel().isEmpty()) {
            err += "Không được bỏ trống nhân viên lễ tân.\n";
        }
        if (err.isBlank())
            return true;
        else {
            AlertGenerator.error(err);
            return false;
        }
    }
}
