package controller.khachSan;

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
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.*;
import util.AlertGenerator;
import util.ExceptionHandler;

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
    private TableView<ChiTietDatPhong> newPhongTable;

    @FXML
    private TableColumn<ChiTietDatPhong, Integer> newPhongCol;

    @FXML
    private TableColumn<ChiTietDatPhong, String> newGiaTienCol;

    @FXML
    private TableColumn<ChiTietDatPhong, String> newLoaiPhongCol;

    @FXML
    private TextField phuongThucField;

    @FXML
    private DatePicker ngayRaDKDate;

    @FXML
    private Button layGioBtn;

    private ArrayList<Phong> dsPhong;
    private ObservableList<KhachHang> dsKhachHang;
    private ObservableList<NhanVien> dsNhanVien;

    private DatPhong datPhong;
    private ObservableList<ChiTietDatPhong> dsChiTietDatPhong;

    private ArrayList<ChiTietDatPhong> dsXoa = new ArrayList<>();

//    public void init() {
//        datPhong = new DatPhong();
//        dsChiTietDatPhong = FXCollections.observableArrayList();
//    }
//
//    public void init(DatPhong datPhong, ArrayList<Phong> dsPhong) {
//        dsChiTietDatPhong = ChiTietDatPhongDAO.getInstance().getAll(datPhong, dsPhong);
//    }
//
//    public void uiInit(ArrayList<Phong> dsPhong) {
//        // COMBOBOX
//        Runnable task = () -> {
//            this.dsPhong = dsPhong;
////            dsPhong = PhongDAO.getInstance().getAll(LoaiPhongDAO.getInstance().getMap());
//            dsKhachHang = FXCollections.observableArrayList(KhachHangDAO.getInstance().getAll());
//            dsNhanVien = FXCollections.observableArrayList(NhanVienDAO.getInstance().getAll());
//            Platform.runLater(() -> {
//                for (int i = 0; i < dsPhong.size(); i++) {
//                    if (dsPhong.get(i).getTrangThai() == Phong.SANSANG)
//                        newPhongCombo.getItems().add(dsPhong.get(i));
//                }
//                khachDatCombo.setItems(dsKhachHang);
//                nhanVienDatCombo.getItems().addAll(dsNhanVien);
//                nhanVienLeTanCombo.getItems().addAll(dsNhanVien);
//            });
//        };
//        new Thread(task).start();
//
//        // FIELD CHECKER
//        forceNumberOnly(tienCocField, heSoGiamGiaField, heSoNgayLeField);
//
//        // BUTTONS
//        themKhachDatBtn.setOnAction(event -> {
//            khachDatCombo.getSelectionModel().select(addKhachHang());
//        });
//
//        layGioBtn.setOnAction(event -> {
//            ngayVaoTTDate.setValue(LocalDate.now());
//            gioVaoTTField.setText(LocalTime.now().toString());
//        });
//
//        newThemBtn.setOnAction(event -> {
//            if (validateDetail()) {
//                Phong phong = newPhongCombo.getSelectionModel().getSelectedItem();
//                phong.setTrangThai(Phong.DANGSUDUNG);
//                newPhongCombo.getSelectionModel().clearSelection();
//                newPhongCombo.getItems().remove(phong);
//                dsChiTietDatPhong.add(new ChiTietDatPhong(
//                        datPhong,
//                        phong,
//                        gioVaoTTField.getText().isBlank()? null : Timestamp.valueOf(LocalDateTime.of(ngayVaoTTDate.getValue(), LocalTime.parse(gioVaoTTField.getText()))),
//                        nhanVienLeTanCombo.getSelectionModel().getSelectedItem()
//                ));
//            }
//        });
//
//        newXoaBtn.setOnAction(event -> {
//            ChiTietDatPhong focusedPhong = newPhongTable.getSelectionModel().getSelectedItem();
//            if (focusedPhong == null) {
//                Alert resultAlert = new Alert(Alert.AlertType.ERROR);
//                resultAlert.setTitle("Lỗi!");
//                resultAlert.setContentText("Bạn chưa chọn phòng nào.");
//                resultAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//                resultAlert.showAndWait();
//            } else {
//                dsChiTietDatPhong.remove(focusedPhong);
//                dsXoa.add(focusedPhong);
//            }
//        });
//
//        huyBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());
//
//        // FIELD RUNNER
//        newPhongCombo.selectionModelProperty().addListener((observableValue, phongSingleSelectionModel, t1) -> {
//            newLoaiPhongField.setText(t1.getSelectedItem().getLoaiPhong().getLoaiPhong());
//            newGiaFIeld.setText(String.format("%,3d", t1.getSelectedItem().getLoaiPhong().getGiaTien()));
//        });
//
//        // TABLE
//        newPhongCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPhong().getMaPhong()));
//        newLoaiPhongCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPhong().getLoaiPhong().getLoaiPhong()));
//        newGiaTienCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getPhong().getLoaiPhong().getGiaTien())));
//
//        newPhongTable.setItems(dsChiTietDatPhong);
//    }
//
//    public void forceNumberOnly(TextField... fields) {
//        for (int i = 0; i < fields.length; i++) {
//            TextField textField = fields[i];
//            textField.textProperty().addListener((observableValue, s, t1) -> {
//                textField.setText(t1.replaceAll("[^\\.0-9]", ""));
//            });
//        }
//    }
//
//    public void add() {
//        if (validate()) {
//            datPhong.setProps(
//                    new Timestamp(System.currentTimeMillis()),
//                    phuongThucField.getText(),
//                    Timestamp.valueOf(LocalDateTime.of(ngayVaoDKDate.getValue(), LocalTime.parse(gioVaoDKField.getText()))),
//                    Timestamp.valueOf(LocalDateTime.of(ngayRaDKDate.getValue(), LocalTime.parse(gioRaDKField.getText()))),
//                    khachDatCombo.getSelectionModel().getSelectedItem(),
//                    nhanVienDatCombo.getSelectionModel().getSelectedItem(),
//                    0,
//                    Long.parseLong(tienCocField.getText()),
//                    true,
//                    false,
//                    ghiChuArea.getText()
//            );
//            datPhong.setDsChiTietDatPhong(dsChiTietDatPhong);
//
//            if (DatPhongDAO.getInstance().create(datPhong)
//                    && ChiTietDatPhongDAO.getInstance().create(dsChiTietDatPhong)
//                    && PhongDAO.getInstance().update(dsChiTietDatPhong)
//            ) {
//                AlertGenerator.success("Đặt phòng thành công.");
//                return;
//            }
//            AlertGenerator.error("Đặt phòng thất bại");
//        }
//    }
//
//    public void update() {
//        if (validate()) {
//            datPhong.setProps(
//                    phuongThucField.getText(),
//                    Timestamp.valueOf(LocalDateTime.of(ngayVaoDKDate.getValue(), LocalTime.parse(gioVaoDKField.getText()))),
//                    Timestamp.valueOf(LocalDateTime.of(ngayRaDKDate.getValue(), LocalTime.parse(gioRaDKField.getText()))),
//                    khachDatCombo.getSelectionModel().getSelectedItem(),
//                    nhanVienDatCombo.getSelectionModel().getSelectedItem(),
//                    0,
//                    Long.parseLong(tienCocField.getText()),
//                    false,
//                    ghiChuArea.getText()
//            );
//
//            if (DatPhongDAO.getInstance().create(datPhong)
//                    && ChiTietDatPhongDAO.getInstance().update(dsChiTietDatPhong)
//                    && ChiTietDatPhongDAO.getInstance().delete(dsXoa)
//                    && PhongDAO.getInstance().update(dsChiTietDatPhong)
//            ) {
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Thành công!");
//                alert.setContentText("Cập nhật thông tin đặt phòng thành công.");
//                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//                alert.showAndWait();
//                return;
//            }
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Thất bại!");
//            alert.setContentText("Cập nhật thông tin đặt phòng thất bại.");
//            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//            alert.showAndWait();
//        }
//    }
//
//    public KhachHang addKhachHang() {
//        try {
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getClassLoader().getResource("view/khachHang/suaKhachHang.fxml"));
//            Parent editRoot = loader.load();
//            new JMetro(editRoot, Style.LIGHT);
//
//            Stage stage = new Stage();
//            Scene scene = new Scene(editRoot);
//
//            stage.setTitle("Thêm khách hàng");
//            stage.setScene(scene);
//            stage.setResizable(false);
//
//            SuaKhachHang suaKhachHang = loader.getController();
//            suaKhachHang.init();
//
//            stage.showAndWait();
//            KhachHang khachHangMoi = suaKhachHang.getKhachHangMoi();
//            if (khachHangMoi != null) {
//                dsKhachHang.add(khachHangMoi);
//            }
//            return khachHangMoi;
//        } catch (IOException e) {
//            ExHandler.handle(e);
//            return null;
//        }
//    }
//
//    public boolean validate() {
//        String err = "";
//        if (tienCocField.getText().isBlank()) {
//            err += "Không được bỏ trống tiền đặt cọc.\n";
//        }
//        if (ngayVaoDKDate.getValue() == null) {
//            err += "Không được bỏ trống ngày vào dự kiến.\n";
//        }
//        if (gioVaoDKField.getText().isBlank()) {
//            err += "Không được bỏ trống giờ vào dự kiến.\n";
//        }
//        if (ngayRaDKDate.getValue() == null) {
//            err += "Không được bỏ trống ngày ra dự kiến.\n";
//        }
//        if (gioRaDKField.getText().isBlank()) {
//            err += "Không được bỏ trống giờ ra dự kiến.\n";
//        }
//        if (khachDatCombo.getSelectionModel().isEmpty()) {
//            err += "Không được bỏ trống khách đặt.\n";
//        }
//        if (nhanVienDatCombo.getSelectionModel().isEmpty()) {
//            err += "Không được bỏ trống nhân viên đặt.\n";
//        }
//        if (err.isBlank())
//            return true;
//        else {
//            AlertGenerator.error(err);
//            return false;
//        }
//    }
//
//    public boolean validateDetail() {
//        String err = "";
//        if (newPhongCombo.getSelectionModel().isEmpty()) {
//            err += "Không được bỏ trống phòng.\n";
//        }
//        if (err.isBlank())
//            return true;
//        else {
//            AlertGenerator.error(err);
//            return false;
//        }
//    }

//    public void initNew(ArrayList<Phong> dsPhong) {
//        uiInit(dsPhong);
//
//        giaField.setText(String.valueOf(phongCombo.getSelectionModel().getSelectedItem().getLoaiPhong().getGiaTien()));
//        luuBtn.setOnAction(event -> {
//            if (validate()) {
//                add();
//                ((Node) (event.getSource())).getScene().getWindow().hide();
//            }
//        });
//    }
//
//    public void init(DatPhong datPhong, ArrayList<Phong> dsPhong) {
//        chiTietDatPhong = ChiTietDatPhongDAO.getInstance().get(datPhong, dsPhong);
//        dsKhachOPhong = FXCollections.observableArrayList();
//
//        uiInit(dsPhong);
//
//        phongCombo.setDisable(true);
//        maDatPhongLabel.setText(String.format("%06d", datPhong.getMaDatPhong()));
//        phongCombo.getSelectionModel().select(chiTietDatPhong.getPhong());
//        giaField.setText(String.valueOf(chiTietDatPhong.getPhong().getLoaiPhong().getGiaTien()));
//
//        LocalDateTime ngayVao = datPhong.getNgayCheckinDk().toLocalDateTime();
//        ngayVaoDuKienDate.setValue(ngayVao.toLocalDate());
//        ngayVaoDuKienTimeField.setText(ngayVao.toLocalTime().toString());
//
//        LocalDateTime ngayRa = datPhong.getNgayCheckoutDk().toLocalDateTime();
//        ngayRaDuKienDate.setValue(ngayRa.toLocalDate());
//        ngayRaDuKienTimeField.setText(ngayRa.toLocalTime().toString());
//
//        phuongThucField.setText(datPhong.getPhuongThuc());
//        datCocField.setText(String.valueOf(datPhong.getTienDatCoc()));
//
//        ghiChuField.setText(datPhong.getGhiChu());
//
//        nhanVienDatCombo.getSelectionModel().select(datPhong.getNvNhanDat());
//        nhanVienLeTanCombo.getSelectionModel().select(datPhong.getNvLeTan());
//
//        if (datPhong.getNgayCheckinTt() != null) {
//            LocalDateTime ngay = datPhong.getNgayCheckinTt().toLocalDateTime();
//            ngayVaoThucTeDate.setValue(ngay.toLocalDate());
//            ngayVaoThucTeTimeField.setText(ngay.toLocalTime().toString());
//        }
//
//        khachDatCombo.getSelectionModel().select(datPhong.getKhachHang());
//
//        dsKhachOPhong = FXCollections.observableArrayList(chiTietDatPhong.getDsKhachHang());
//
//        if (datPhong.getNgayCheckinTt() == null)
//            huyDatBtn.setVisible(true);
//
//        luuBtn.setOnAction(event -> {
//            if (validate()) {
//                update();
//                ((Node) (event.getSource())).getScene().getWindow().hide();
//            }
//        });
//    }

    public void init(ArrayList<Phong> dsPhong) {
        datPhong = new DatPhong();
        dsChiTietDatPhong = FXCollections.observableArrayList();
        uiInit(dsPhong);

        luuBtn.setOnAction(event -> {
            if (validate()) {
                add();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public void init(DatPhong datPhong, ArrayList<Phong> dsPhong, NhanVien nhanVien) {
        this.datPhong = datPhong;
        dsChiTietDatPhong = ChiTietDatPhongDAO.getInstance().getAll(datPhong, dsPhong);
        uiInit(dsPhong);

        maDatPhongLabel.setText(String.format("%06d", datPhong.getMaDatPhong()));

        LocalDateTime ngayVao = datPhong.getNgayCheckinDk().toLocalDateTime();
        ngayVaoDKDate.setValue(ngayVao.toLocalDate());
        gioVaoDKField.setText(ngayVao.toLocalTime().toString());

        LocalDateTime ngayRa = datPhong.getNgayCheckoutDk().toLocalDateTime();
        ngayRaDKDate.setValue(ngayRa.toLocalDate());
        gioRaDKField.setText(ngayRa.toLocalTime().toString());

        phuongThucField.setText(datPhong.getPhuongThuc());
        tienCocField.setText(String.valueOf(datPhong.getTienDatCoc()));

        ghiChuArea.setText(datPhong.getGhiChu());

        nhanVienDatCombo.getSelectionModel().select(datPhong.getNvNhanDat());
        nhanVienLeTanCombo.getSelectionModel().select(nhanVien);
        layGioBtn.fire();

        khachDatCombo.getSelectionModel().select(datPhong.getKhachHang());

        luuBtn.setOnAction(event -> {
            if (validate()) {
                update();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public void uiInit(ArrayList<Phong> dsPhong) {
        // COMBOBOX
        newPhongCombo.getItems().addAll(dsPhong);
        Runnable task = () -> {
            this.dsPhong = dsPhong;
            dsKhachHang = FXCollections.observableArrayList(KhachHangDAO.getInstance().getAll());
            dsNhanVien = FXCollections.observableArrayList(NhanVienDAO.getInstance().getAll());
            Platform.runLater(() -> {
                khachDatCombo.setItems(dsKhachHang);
                nhanVienDatCombo.getItems().addAll(dsNhanVien);
                nhanVienLeTanCombo.getItems().addAll(dsNhanVien);
            });
        };
        new Thread(task).start();

        // FIELD CHECKER
        forceNumberOnly(tienCocField);

        // BUTTONS
        themKhachDatBtn.setOnAction(event -> {
            khachDatCombo.getSelectionModel().select(addKhachHang());
        });

        layGioBtn.setOnAction(event -> {
            ngayVaoTTDate.setValue(LocalDate.now());
            gioVaoTTField.setText(LocalTime.now().toString());
        });

        newThemBtn.setOnAction(event -> {
            if (newPhongCombo.getSelectionModel().isEmpty()) {
                AlertGenerator.error("Bạn chưa chọn phòng nào.");
            } else {
                Phong phong = newPhongCombo.getSelectionModel().getSelectedItem();
                newPhongCombo.getSelectionModel().clearSelection();
                newPhongCombo.getItems().remove(phong);
                dsChiTietDatPhong.add(new ChiTietDatPhong(
                        datPhong,
                        phong
                ));
            }
        });

        newXoaBtn.setOnAction(event -> {
            ChiTietDatPhong focusedPhong = newPhongTable.getSelectionModel().getSelectedItem();
            if (focusedPhong == null) {
                AlertGenerator.error("Bạn chưa chọn phòng nào.");
            } else {
                dsChiTietDatPhong.remove(focusedPhong);
                dsXoa.add(focusedPhong);
            }
        });

        huyBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());

        // FIELD LISTENERS
        newPhongCombo.getSelectionModel().selectedItemProperty().addListener((observableValue, phongSingleSelectionModel, t1) -> {
            newLoaiPhongField.setText(t1.getLoaiPhong().getLoaiPhong());
            newGiaFIeld.setText(String.format("%,3d", t1.getLoaiPhong().getGiaTien()));
        });

        // TABLE
        newPhongCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPhong().getMaPhong()));
        newLoaiPhongCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPhong().getLoaiPhong().getLoaiPhong()));
        newGiaTienCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getPhong().getLoaiPhong().getGiaTien())));

        newPhongTable.setItems(dsChiTietDatPhong);
    }

    public void add() {
        datPhong.setProps(
                new Timestamp(System.currentTimeMillis()),
                phuongThucField.getText(),
                true,
                Long.parseLong(tienCocField.getText()),
                Timestamp.valueOf(LocalDateTime.of(ngayVaoDKDate.getValue(), LocalTime.parse(gioVaoDKField.getText()))),
                Timestamp.valueOf(LocalDateTime.of(ngayRaDKDate.getValue(), LocalTime.parse(gioRaDKField.getText()))),
                khachDatCombo.getSelectionModel().getSelectedItem(),
                nhanVienDatCombo.getSelectionModel().getSelectedItem(),
                gioVaoTTField.getText().isBlank() ? null : Timestamp.valueOf(LocalDateTime.of(ngayVaoTTDate.getValue(), LocalTime.parse(gioVaoTTField.getText()))),
                nhanVienLeTanCombo.getSelectionModel().getSelectedItem(),
                ghiChuArea.getText()
        );
        datPhong.setDsChiTietDatPhong(dsChiTietDatPhong);

        if (datPhong.getNgayCheckinTt() != null) {
            for (ChiTietDatPhong chiTietDatPhong : dsChiTietDatPhong) {
                chiTietDatPhong.getPhong().setTrangThai(Phong.DANGSUDUNG);
            }
        }

        if (DatPhongDAO.getInstance().create(datPhong)
                && ChiTietDatPhongDAO.getInstance().create(dsChiTietDatPhong)
                && PhongDAO.getInstance().update(dsChiTietDatPhong)
        ) {
            AlertGenerator.success("Đặt phòng thành công");
        } else {
            AlertGenerator.error("Đặt phòng thất bại.");
        }
    }

    public void update() {
        datPhong.setProps(
                phuongThucField.getText(),
                true,
                Long.parseLong(tienCocField.getText()),
                Timestamp.valueOf(LocalDateTime.of(ngayVaoDKDate.getValue(), LocalTime.parse(gioVaoDKField.getText()))),
                Timestamp.valueOf(LocalDateTime.of(ngayRaDKDate.getValue(), LocalTime.parse(gioRaDKField.getText()))),
                khachDatCombo.getSelectionModel().getSelectedItem(),
                nhanVienDatCombo.getSelectionModel().getSelectedItem(),
                gioVaoTTField.getText().isBlank() ? null : Timestamp.valueOf(LocalDateTime.of(ngayVaoTTDate.getValue(), LocalTime.parse(gioVaoTTField.getText()))),
                nhanVienLeTanCombo.getSelectionModel().getSelectedItem(),
                ghiChuArea.getText()
        );

        if (datPhong.getNgayCheckinTt() != null) {
            for (ChiTietDatPhong chiTietDatPhong : dsChiTietDatPhong) {
                chiTietDatPhong.getPhong().setTrangThai(Phong.DANGSUDUNG);
            }
        }

        for (ChiTietDatPhong chiTietDatPhong : dsXoa) {
            chiTietDatPhong.getPhong().setTrangThai(Phong.SANSANG);
        }

        if (DatPhongDAO.getInstance().update(datPhong)
                && ChiTietDatPhongDAO.getInstance().update(dsChiTietDatPhong, dsXoa)
                && PhongDAO.getInstance().update(dsChiTietDatPhong)
                && PhongDAO.getInstance().update(dsXoa)
        ) {
            AlertGenerator.success("Cập nhật thông tin đặt phòng thành công.");
            return;
        }
        AlertGenerator.error("Cập nhật thông tin đặt phòng thất bại.");
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
            if (khachHangMoi != null) {
                dsKhachHang.add(khachHangMoi);
            }
            return khachHangMoi;
        } catch (IOException e) {
            ExceptionHandler.handle(e);
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
            ExceptionHandler.handle(new Exception(err));
            return false;
        }
    }
}
