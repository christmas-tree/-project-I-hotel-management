/*
 * Copyright (c) 2019 Nghia Tran.
 * Project I - Library Management System
 */

package controller.nhanVien;

import dao.NhanVienDAO;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import model.NhanVien;
import util.ExHandler;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SuaNhanVien {

    @FXML
    private TextField idField;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label headerLabel;

    @FXML
    private PasswordField matKhauField;

    @FXML
    private Button confirmBtn;

    @FXML
    private ToggleButton gioiTinhToggle;

    @FXML
    private TextArea diaChiField;

    @FXML
    private TextField cmndField;

    @FXML
    private TextField tenDangNhapField;

    @FXML
    private TextField tenField;

    @FXML
    private TextField ghiChuField;

    @FXML
    private TextField emailField;

    @FXML
    private ChoiceBox<String> loaiNvComboBox;

    @FXML
    private TextField dienThoaiField;


    public void init() {
        uiInit();
        
        confirmBtn.setOnAction(event -> {
            if (validate()) {
                if (matKhauField.getText().isBlank()) {
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Xác nhận");
                    confirm.setHeaderText("Mật khẩu trống!");
                    confirm.setContentText("Tài khoản nhân viên sẽ được tạo với mật khẩu mặc định: \"123456\".");
                    confirm.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    confirm.showAndWait();
                    if (confirm.getResult() != ButtonType.OK) {
                        System.out.println();
                        return;
                    }
                }
                add();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public void init(NhanVien nhanVien) {

        uiInit();

        headerLabel.setText("Sửa nhân viên");
        idField.setText(String.format("%06d", nhanVien.getMaNv()));
        loaiNvComboBox.getSelectionModel().select(nhanVien.getLoaiNv());
        tenField.setText(nhanVien.getTenNv());
        cmndField.setText(String.valueOf(nhanVien.getCmnd()));
        gioiTinhToggle.setSelected(nhanVien.getGioiTinh());
        dienThoaiField.setText("0" + nhanVien.getDienThoai());
        emailField.setText(nhanVien.getEmail());
        diaChiField.setText(nhanVien.getDiaChi());
        tenDangNhapField.setText(nhanVien.getTenDangNhap());
        ghiChuField.setText(nhanVien.getGhiChu());


        confirmBtn.setOnAction(event -> {
            if (validate()) {
                if (!matKhauField.getText().isBlank()) {
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Xác nhận");
                    confirm.setHeaderText("Ghi đè mật khẩu?");
                    confirm.setContentText("Bạn sẽ ghi đè mật khẩu của nhân viên này.");
                    confirm.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    confirm.showAndWait();
                    if (confirm.getResult() != ButtonType.OK) {
                        return;
                    }
                }
                update(nhanVien);
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public void uiInit() {
        loaiNvComboBox.getItems().addAll("Quản lý", "Lễ tân");

        gioiTinhToggle.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                gioiTinhToggle.setText("Nam");
            } else {
                gioiTinhToggle.setText("Nữ");
            }
        });

        cmndField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cmndField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        dienThoaiField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cmndField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        tenDangNhapField.textProperty().addListener((observable, oldValue, newValue) -> tenDangNhapField.setText(newValue.replaceAll("[\\s*]", "")));

        cancelBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());
    }

    public void update(NhanVien nhanVien) {

        nhanVien.setTenNv(tenField.getText());
        nhanVien.setLoaiNv(loaiNvComboBox.getSelectionModel().getSelectedIndex());
        if (!cmndField.getText().isBlank())
            nhanVien.setCmnd(Long.parseLong(cmndField.getText()));
        nhanVien.setGioiTinh(gioiTinhToggle.isSelected());
        if (!dienThoaiField.getText().isBlank())
            nhanVien.setDienThoai(Long.parseLong(dienThoaiField.getText()));
        nhanVien.setEmail(emailField.getText().trim());
        nhanVien.setDiaChi(diaChiField.getText());
        nhanVien.setTenDangNhap(tenDangNhapField.getText());
        if (!matKhauField.getText().isBlank())
            nhanVien.setMatKhau(matKhauField.getText());
        nhanVien.setGhiChu(ghiChuField.getText());

        if (NhanVienDAO.getInstance().update(nhanVien)) {
            System.out.println("Sucessfully updated SID " + nhanVien.getMaNv());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công!");
            alert.setHeaderText("Cập nhật nhân viên thành công");
            alert.setContentText("Nhân viên "+ nhanVien.getTenNv() + " đã được cập nhật thành công vào cơ sở dữ liệu.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    public void add() {
        NhanVien nhanVien = new NhanVien();

        nhanVien.setTenNv(tenField.getText());
        nhanVien.setLoaiNv(loaiNvComboBox.getSelectionModel().getSelectedIndex());
        if (!cmndField.getText().isBlank())
            nhanVien.setCmnd(Long.parseLong(cmndField.getText()));
        nhanVien.setGioiTinh(gioiTinhToggle.isSelected());
        if (!dienThoaiField.getText().isBlank())
            nhanVien.setDienThoai(Long.parseLong(dienThoaiField.getText()));
        nhanVien.setEmail(emailField.getText().trim());
        nhanVien.setDiaChi(diaChiField.getText());
        nhanVien.setTenDangNhap(tenDangNhapField.getText());
        if (matKhauField.getText().isBlank())
            nhanVien.setMatKhau("123456");
        else
            nhanVien.setMatKhau(matKhauField.getText());
        nhanVien.setGhiChu(ghiChuField.getText());

        if (NhanVienDAO.getInstance().create(nhanVien)) {
            System.out.println("Sucessfully added new user: " + nhanVien.getTenNv());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công!");
            alert.setHeaderText("Thêm nhân viên thành công");
            alert.setContentText("Nhân viên " + nhanVien.getTenNv() + " đã được thêm thành công vào cơ sở dữ liệu.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    public boolean validate() {
        String err = "";

        if (tenField.getText().isBlank()) {
            err += "Không được bỏ trống họ tên.\n";
        }
        if (tenDangNhapField.getText().isBlank()) {
            err += "Không được bỏ trống tên đăng nhập.\n";
        }
        if (loaiNvComboBox.getSelectionModel().isEmpty()) {
            err += "Không được bỏ trống loại nhân viên.\n";
        }
        if (err.equals("")) {
            return true;
        } else {
            ExHandler.handle(new Exception(err));
            return false;
        }
    }
}
