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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
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
    private ArrayList<Phong> dsPhong;
    private ObservableList<KhachHang> dsKhachHang;

    private ObservableList<KhachHang> dsKhachOPhong;
    private ArrayList<KhachHang> dsXoa = new ArrayList<>();
    private DatPhong datPhong;
    private ChiTietDatPhong chiTietDatPhong;

    public void initBookFromTimeline(int phong, ArrayList<Phong> dsPhong) {
        System.out.println("phong = " + phong);
        for (Phong phongItem : dsPhong) {
            if (phongItem.getMaPhong() == phong) {
                initBookFromPhong(phongItem, dsPhong);
                break;
            }
        }
    }

    public void initBookFromPhong(Phong phong, ArrayList<Phong> dsPhong) {
        ngayVaoThucTeDate.setEditable(false);
        ngayVaoThucTeTimeField.setEditable(false);
        nhanVienLeTanCombo.setEditable(false);
        ngayVaoThucTeDate.setDisable(true);
        ngayVaoThucTeTimeField.setDisable(true);
        nhanVienLeTanCombo.setDisable(true);
        layGioBtn.setDisable(true);

        initNew(phong, dsPhong);
    }

    public void initNew(Phong phong, ArrayList<Phong> dsPhong) {
        dsKhachOPhong = FXCollections.observableArrayList();
        uiInit(dsPhong);

        phongCombo.getSelectionModel().select(phong);
        phongCombo.setDisable(true);
        giaField.setText(String.valueOf(phongCombo.getSelectionModel().getSelectedItem().getLoaiPhong().getGiaTien()));
        luuBtn.setOnAction(event -> {
            if (validate()) {
                add();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public void init(DatPhong datPhong, ArrayList<Phong> dsPhong, NhanVien nhanVien) {
        chiTietDatPhong = ChiTietDatPhongDAO.getInstance().get(datPhong, dsPhong);
        dsKhachOPhong = FXCollections.observableArrayList();

        uiInit(dsPhong);

        phongCombo.setDisable(true);
        maDatPhongLabel.setText(String.format("%06d", datPhong.getMaDatPhong()));
        phongCombo.getSelectionModel().select(chiTietDatPhong.getPhong());
        giaField.setText(String.valueOf(chiTietDatPhong.getPhong().getLoaiPhong().getGiaTien()));

        LocalDateTime ngayVao = datPhong.getNgayCheckinDk().toLocalDateTime();
        ngayVaoDuKienDate.setValue(ngayVao.toLocalDate());
        ngayVaoDuKienTimeField.setText(ngayVao.toLocalTime().toString());

        LocalDateTime ngayRa = datPhong.getNgayCheckoutDk().toLocalDateTime();
        ngayRaDuKienDate.setValue(ngayRa.toLocalDate());
        ngayRaDuKienTimeField.setText(ngayRa.toLocalTime().toString());

        phuongThucField.setText(datPhong.getPhuongThuc());
        datCocField.setText(String.valueOf(datPhong.getTienDatCoc()));

        ghiChuField.setText(datPhong.getGhiChu());

        nhanVienDatCombo.getSelectionModel().select(datPhong.getNvNhanDat());
        nhanVienLeTanCombo.getSelectionModel().select(nhanVien);
        layGioBtn.fire();

        khachDatCombo.getSelectionModel().select(datPhong.getKhachHang());

        dsKhachOPhong = FXCollections.observableArrayList(chiTietDatPhong.getDsKhachHang());

        if (datPhong.getNgayCheckinTt() == null)
            huyDatBtn.setVisible(true);

        luuBtn.setOnAction(event -> {
            if (validate()) {
                update();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }



//    public void initOld(Phong phong, ArrayList<Phong> dsPhong) {
//        phongCombo.setDisable(true);
//
//        chiTietDatPhong = ChiTietDatPhongDAO.getInstance().getByPhong(phong);
//        datPhong = chiTietDatPhong.getDatPhong();
//
//        maDatPhongLabel.setText(String.format("%06d", datPhong.getMaDatPhong()));
//        phongCombo.getSelectionModel().select(phong);
//        giaField.setText(String.valueOf(chiTietDatPhong.getPhong().getLoaiPhong().getGiaTien()));
//        LocalDateTime ngayVao = datPhong.getNgayCheckinDk().toLocalDateTime();
//        ngayVaoDuKienDate.setValue(ngayVao.toLocalDate());
//        ngayVaoThucTeTimeField.setText(ngayVao.toLocalTime().toString());
//        LocalDateTime ngayRa = datPhong.getNgayCheckoutDk().toLocalDateTime();
//        ngayRaDuKienDate.setValue(ngayRa.toLocalDate());
//        ngayRaDuKienTimeField.setText(ngayRa.toLocalTime().toString());
//        phuongThucField.setText(datPhong.getPhuongThuc());
//        datCocField.setText(String.valueOf(datPhong.getTienDatCoc()));
//
//        ghiChuField.setText(datPhong.getGhiChu());
//
//        nhanVienDatCombo.getSelectionModel().select(datPhong.getNvNhanDat());
//        nhanVienLeTanCombo.getSelectionModel().select(chiTietDatPhong.getNvLeTan());
//
//        if (chiTietDatPhong.getNgayCheckinTt() != null) {
//            LocalDateTime ngay = chiTietDatPhong.getNgayCheckinTt().toLocalDateTime();
//            ngayVaoThucTeDate.setValue(ngay.toLocalDate());
//            ngayVaoThucTeTimeField.setText(ngay.toLocalTime().toString());
//        }
//
//        khachDatCombo.getSelectionModel().select(datPhong.getKhachHang());
//
//        dsKhachOPhong = FXCollections.observableArrayList(chiTietDatPhong.getDsKhachHang());
//
//        if (chiTietDatPhong.getNgayCheckinTt() == null)
//            huyDatBtn.setVisible(true);
//
//        uiInit(dsPhong);
//
//        luuBtn.setOnAction(event -> {
//            update();
//            ((Node) (event.getSource())).getScene().getWindow().hide();
//        });
//    }

    public void uiInit(ArrayList<Phong> dsPhong) {
        giaField.setDisable(true);
        // COMBOBOX
        Runnable task = () -> {
            dsKhachHang = FXCollections.observableArrayList(KhachHangDAO.getInstance().getAll());
            dsNhanVien = FXCollections.observableArrayList(NhanVienDAO.getInstance().getAll());
            Platform.runLater(() -> {
                phongCombo.getItems().addAll(dsPhong);
                khachDatCombo.setItems(dsKhachHang);
                nhanVienDatCombo.getItems().addAll(dsNhanVien);
                nhanVienLeTanCombo.getItems().addAll(dsNhanVien);
                khachOCombo.setItems(dsKhachHang);
            });
        };
        new Thread(task).start();

        // FIELD CHECKER
        forceNumberOnly(datCocField, giaField);

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

        // FIELD RUNNER
        phongCombo.selectionModelProperty().addListener((observableValue, phongSingleSelectionModel, t1) -> giaField.setText(String.format("%,3d", t1.getSelectedItem().getLoaiPhong().getGiaTien())));

        // TABLE
        tenCol.setCellValueFactory(new PropertyValueFactory<>("tenKhach"));
        gioiTinhCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getGioiTinh() ? "Nam" : "Nữ"));
        cmndCol.setCellValueFactory(new PropertyValueFactory<>("cmnd"));

        khachOTable.setItems(dsKhachOPhong);
    }

    public void add() {
        datPhong = new DatPhong(
                new Timestamp(System.currentTimeMillis()),
                phuongThucField.getText(),
                false,
                Long.parseLong(datCocField.getText()),
                Timestamp.valueOf(LocalDateTime.of(ngayVaoDuKienDate.getValue(), LocalTime.parse(ngayVaoDuKienTimeField.getText()))),
                Timestamp.valueOf(LocalDateTime.of(ngayRaDuKienDate.getValue(), LocalTime.parse(ngayRaDuKienTimeField.getText()))),
                khachDatCombo.getSelectionModel().getSelectedItem(),
                nhanVienDatCombo.getSelectionModel().getSelectedItem(),
                ngayVaoThucTeTimeField.getText().isBlank() ? null : Timestamp.valueOf(LocalDateTime.of(ngayVaoThucTeDate.getValue(), LocalTime.parse(ngayVaoThucTeTimeField.getText()))),
                nhanVienLeTanCombo.getSelectionModel().getSelectedItem(),
                ghiChuField.getText()
        );
        chiTietDatPhong = new ChiTietDatPhong(
                datPhong,
                phongCombo.getSelectionModel().getSelectedItem()
        );

        if (datPhong.getNgayCheckinTt() != null)
            chiTietDatPhong.getPhong().setTrangThai(1);

        if (DatPhongDAO.getInstance().create(datPhong)
                && ChiTietDatPhongDAO.getInstance().create(chiTietDatPhong)
                && ChiTietDatPhongDAO.getInstance().updateDsKhachO(chiTietDatPhong, dsKhachOPhong, dsXoa)
                && PhongDAO.getInstance().update(chiTietDatPhong.getPhong())
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

    public void update() {
        datPhong.setProps(
                phuongThucField.getText(),
                false,
                Long.parseLong(datCocField.getText()),
                Timestamp.valueOf(LocalDateTime.of(ngayVaoDuKienDate.getValue(), LocalTime.parse(ngayVaoDuKienTimeField.getText()))),
                Timestamp.valueOf(LocalDateTime.of(ngayRaDuKienDate.getValue(), LocalTime.parse(ngayRaDuKienTimeField.getText()))),
                khachDatCombo.getSelectionModel().getSelectedItem(),
                nhanVienDatCombo.getSelectionModel().getSelectedItem(),
                ngayVaoThucTeTimeField.getText().isBlank() ? null : Timestamp.valueOf(LocalDateTime.of(ngayVaoThucTeDate.getValue(), LocalTime.parse(ngayVaoThucTeTimeField.getText()))),
                nhanVienLeTanCombo.getSelectionModel().getSelectedItem(),
                ghiChuField.getText()
        );

        if (DatPhongDAO.getInstance().update(datPhong)
                && ChiTietDatPhongDAO.getInstance().updateDsKhachO(chiTietDatPhong, dsKhachOPhong, dsXoa)
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
            ExceptionHandler.handle(new Exception(err));
            return false;
        }
    }
}
