package controller.phong;

import dao.ChiTietPhongDAO;
import dao.LoaiPhongDAO;
import dao.PhongDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.util.StringConverter;
import model.ChiTietPhong;
import model.LoaiPhong;
import model.Phong;
import util.ExHandler;

import java.util.ArrayList;


public class SuaPhong {

    @FXML
    private ComboBox<String> trangThaiCombo;

    @FXML
    private TextField tangField;

    @FXML
    private TableView<ChiTietPhong> doTable;

    @FXML
    private TextField ghiChuField;

    @FXML
    private TextField slDoField;

    @FXML
    private TextField donViField;

    @FXML
    private TextField donGiaField;

    @FXML
    private ComboBox<String> trangThaiDoCombo;

    @FXML
    private TextField maPhongField;

    @FXML
    private TextField tenDoField;

    @FXML
    private TextField ghiChuDoField;

    @FXML
    private Button themDoBtn;

    @FXML
    private ComboBox<LoaiPhong> loaiPhongCombo;

    @FXML
    private TableColumn<ChiTietPhong, String> tenDoCol;

    @FXML
    private TableColumn<ChiTietPhong, Integer> slDoCol;

    @FXML
    private TableColumn<ChiTietPhong, String> trangThaiDoCol;

    @FXML
    private TableColumn<ChiTietPhong, String> ghiChuDoCol;

    @FXML
    private TableColumn<ChiTietPhong, Integer> indexCol;

    @FXML
    private TableColumn<ChiTietPhong, Long> giaTienCol;

    @FXML
    private TableColumn<ChiTietPhong, String> donViCol;

    @FXML
    private Button xoaDoBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button confirmBtn;

    // VARIABLES

    private Phong phong;
    private ArrayList<ChiTietPhong> pendingDelete = new ArrayList<>();
    private ObservableList<ChiTietPhong> dsChiTietPhong;

    // UI INITIALIZE

    public void init() {
        dsChiTietPhong = FXCollections.observableArrayList();
        doTable.setItems(dsChiTietPhong);

        uiInit();

        confirmBtn.setOnAction(event -> {
            if (validate()) {
                add();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public void init(Phong phong) {
        this.phong = phong;
        dsChiTietPhong = ChiTietPhongDAO.getInstance().getAll(phong);

        uiInit();

        maPhongField.setText(String.valueOf(phong.getMaPhong()));
        loaiPhongCombo.getSelectionModel().select(phong.getLoaiPhong());
        trangThaiCombo.getSelectionModel().select(phong.getTrangThai());
        tangField.setText(String.valueOf(phong.getTang()));
        ghiChuField.setText(phong.getGhiChu());

        doTable.setItems(dsChiTietPhong);

        confirmBtn.setOnAction(event -> {
            if (validate()) {
                update();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public void uiInit() {
        // COMBOBOX VALUES INITIALIZE
        loaiPhongCombo.getItems().addAll(LoaiPhongDAO.getInstance().getAll());
        trangThaiCombo.getItems().addAll(Phong.dsTrangThai);
        trangThaiDoCombo.getItems().addAll(ChiTietPhong.dsTrangThai);

        // TABLE INITIALIZE

        indexCol.setCellFactory(col -> new TableCell<>() {
            @Override
            public void updateIndex(int index) {
                super.updateIndex(index);
                if (isEmpty() || index < 0)
                    setText(null);
                else
                    setText(Integer.toString(index + 1));
            }
        });
        tenDoCol.setCellValueFactory(new PropertyValueFactory<>("tenDo"));
        giaTienCol.setCellValueFactory(new PropertyValueFactory<>("giaTien"));
        slDoCol.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        donViCol.setCellValueFactory(new PropertyValueFactory<>("donVi"));
        trangThaiDoCol.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        ghiChuDoCol.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

        giaTienCol.setCellFactory(p -> {
            TextFieldTableCell<ChiTietPhong, Long> cell = new TextFieldTableCell<>();
            cell.setConverter(new StringConverter<>() {
                @Override
                public String toString(Long value) {
                    return String.format("%,d", value);
                }

                @Override
                public Long fromString(String string) {
                    return Long.parseLong(string.replaceAll("[^\\d]", ""));
                }
            });
            return cell;
        });
        giaTienCol.setOnEditCommit((event) -> {
            TablePosition<ChiTietPhong, Long> pos = event.getTablePosition();
            Long newValue = event.getNewValue();
            int row = pos.getRow();
            ChiTietPhong p = event.getTableView().getItems().get(row);
            p.setGiaTien(newValue);
        });

        slDoCol.setCellFactory(p -> new TextFieldTableCell<>());
        slDoCol.setOnEditCommit((event) -> {
            TablePosition<ChiTietPhong, Integer> pos = event.getTablePosition();
            Integer newValue = event.getNewValue();
            int row = pos.getRow();
            ChiTietPhong p = event.getTableView().getItems().get(row);
            p.setSoLuong(newValue);
        });

        donViCol.setCellFactory(p -> new TextFieldTableCell<>());
        donViCol.setOnEditCommit((event) -> {
            TablePosition<ChiTietPhong, String> pos = event.getTablePosition();
            String newValue = event.getNewValue();
            int row = pos.getRow();
            ChiTietPhong p = event.getTableView().getItems().get(row);
            p.setDonVi(newValue);
        });

        trangThaiDoCol.setCellFactory(p -> {
            ChoiceBoxTableCell<ChiTietPhong, String> cell = new ChoiceBoxTableCell<>();
            cell.getItems().addAll(ChiTietPhong.dsTrangThai);
            return cell;
        });
        trangThaiDoCol.setOnEditCommit((event) -> {
            TablePosition<ChiTietPhong, String> pos = event.getTablePosition();
            String newValue = event.getNewValue();
            int row = pos.getRow();
            ChiTietPhong p = event.getTableView().getItems().get(row);
            p.setTrangThai(newValue);
        });

        ghiChuDoCol.setCellFactory(p -> new TextFieldTableCell<>());
        ghiChuDoCol.setOnEditCommit((event) -> {
            TablePosition<ChiTietPhong, String> pos = event.getTablePosition();
            String newValue = event.getNewValue();
            int row = pos.getRow();
            ChiTietPhong p = event.getTableView().getItems().get(row);
            p.setGhiChu(newValue);
        });

        // FIELDS LISTENERS

        maPhongField.textProperty().addListener((observable, oldValue, newValue) -> maPhongField.setText(newValue.replaceAll("[^\\d]", "")));
        slDoField.textProperty().addListener((observable, oldValue, newValue) -> slDoField.setText(newValue.replaceAll("[^\\d]", "")));
        giaTienCol.textProperty().addListener((observable, oldValue, newValue) -> giaTienCol.setText(newValue.replaceAll("[^\\d]", "")));

        // BUTTON ACTIONS

        themDoBtn.setOnAction(event -> {
            if (validateDetail()) {
                dsChiTietPhong.add(new ChiTietPhong(
                        phong,
                        tenDoField.getText(),
                        Integer.parseInt(slDoField.getText()),
                        Long.parseLong(donGiaField.getText()),
                        donViField.getText(),
                        trangThaiDoCombo.getSelectionModel().getSelectedIndex(),
                        ghiChuDoField.getText()
                ));
            }
        });

        xoaDoBtn.setOnAction(event -> {
            ChiTietPhong toBeDeletedDetail = doTable.getSelectionModel().getSelectedItem();
            pendingDelete.add(toBeDeletedDetail);
            dsChiTietPhong.remove(toBeDeletedDetail);
            doTable.getSelectionModel().clearSelection();
        });

        cancelBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());
    }

    //// END OF UI INITIALIZATION

    public void update() {
        phong.setMaPhong(Integer.parseInt(maPhongField.getText()));
        phong.setLoaiPhong(loaiPhongCombo.getSelectionModel().getSelectedItem());
        phong.setTang(Integer.parseInt(tangField.getText()));
        phong.setTrangThai(trangThaiCombo.getSelectionModel().getSelectedIndex());
        phong.setGhiChu(ghiChuDoField.getText());

        boolean success = PhongDAO.getInstance().update(phong) &&
                ChiTietPhongDAO.getInstance().delete(pendingDelete) &&
                ChiTietPhongDAO.getInstance().update(dsChiTietPhong);
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công!");
            alert.setContentText("Phòng " + phong.getMaPhong() + " đã được cập nhật thành công.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    public void add() {
        phong = new Phong();
        phong.setMaPhong(Integer.parseInt(maPhongField.getText()));
        phong.setLoaiPhong(loaiPhongCombo.getSelectionModel().getSelectedItem());
        phong.setTang(Integer.parseInt(tangField.getText()));
        phong.setTrangThai(trangThaiCombo.getSelectionModel().getSelectedIndex());
        phong.setGhiChu(ghiChuDoField.getText());
        phong.setDsChiTietPhong(dsChiTietPhong);

        boolean success = PhongDAO.getInstance().create(phong) && ChiTietPhongDAO.getInstance().create(dsChiTietPhong);
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công!");
            alert.setContentText("Phòng " + phong.getMaPhong() + " đã được thêm thành công.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    // INPUT VALIDATION METHODS

    public boolean validate() {
        String err = "";

        if (maPhongField.getText().isBlank())
            err += "Không được bỏ trống số phòng.\n";
        if (loaiPhongCombo.getSelectionModel().isEmpty())
            err += "Không được bỏ trống loại phòng.\n";
        if (tangField.getText().isBlank())
            err += "Không được bỏ trống tầng.\n";
        if (trangThaiCombo.getSelectionModel().isEmpty())
            err += "Không được bỏ trống số phòng.\n";
        if (err.equals("")) {
            return true;
        } else {
            ExHandler.handle(new Exception(err));
            return false;
        }
    }

    public boolean validateDetail() {
        String err = "";

        if (tenDoField.getText().isBlank())
            err += "Không được bỏ trống tên đồ.\n";
        if (trangThaiDoCombo.getSelectionModel().isEmpty())
            err += "Không được bỏ trống trạng thái.\n";
        if (slDoField.getText().isBlank())
            err += "Không được bỏ trống số lượng.\n";
        if (donViField.getText().isBlank())
            err += "Không được bỏ trống đơn vị.\n";
        if (donGiaField.getText().isBlank())
            err += "Không được bỏ trống đơn giá.\n";
        if (err.equals("")) {
            return true;
        } else {
            ExHandler.handle(new Exception(err));
            return false;
        }
    }

    public Phong getPhongMoi() {
        return phong;
    }
}
