/*
 * Copyright (c) 2019 Nghia Tran.
 * Project I - Library Management System
 */

package controller.khachHang;

import dao.KhachHangDAO;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import model.KhachHang;
import util.ExHandler;

public class SuaKhachHang {

    @FXML
    private TextField idField;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField ghiChuField;

    @FXML
    private Label headerLabel;

    @FXML
    private Button confirmBtn;

    @FXML
    private TextField emailField;

    @FXML
    private ToggleButton gioiTinhToggle;

    @FXML
    private TextField cmndField;

    @FXML
    private TextField tenField;

    @FXML
    private TextArea diaChiField;

    @FXML
    private TextField dienThoaiField;

    KhachHang khachHang = null;

    public void init() {
        uiInit();
        confirmBtn.setOnAction(event -> {
            if (validate()) {
                add();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public void init(KhachHang khachHang) {
        this.khachHang = khachHang;
        uiInit();

        headerLabel.setText("Sửa khách hàng");
        idField.setText(String.format("%06d", khachHang.getMaKh()));
        tenField.setText(khachHang.getTenKhach());
        gioiTinhToggle.setSelected(khachHang.getGioiTinh());
        cmndField.setText(String.valueOf(khachHang.getCmnd()));
        dienThoaiField.setText(String.valueOf(khachHang.getDienThoai()));
        emailField.setText(String.valueOf(khachHang.getEmail()));
        diaChiField.setText(khachHang.getDiaChi());
        ghiChuField.setText(khachHang.getGhiChu());

        confirmBtn.setOnAction(event -> {
            if (validate()) {
                update(khachHang);
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public void uiInit() {
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

        tenField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\\\d+")) {
                cmndField.setText(newValue.replaceAll("[\\d]", ""));
            }
        });

        cancelBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());
    }

    public void update(KhachHang khachHang) {
        khachHang.setTenKhach(tenField.getText());
        khachHang.setGioiTinh(gioiTinhToggle.isSelected());
        if (!cmndField.getText().equals(""))
            khachHang.setCmnd(Long.parseLong(cmndField.getText()));
        if (!dienThoaiField.getText().equals(""))
            khachHang.setDienThoai(Long.parseLong(dienThoaiField.getText()));
        khachHang.setEmail(emailField.getText());
        khachHang.setDiaChi(diaChiField.getText());
        khachHang.setGhiChu(ghiChuField.getText());

        boolean success = KhachHangDAO.getInstance().update(khachHang);

        if (success) {
            System.out.println("Sucessfully updated RID " + khachHang.getMaKh());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công!");
            alert.setHeaderText("Cập nhật khách hàng thành công");
            alert.setContentText("Khách hàng " + khachHang.getMaKh() + ": " + khachHang.getTenKhach() + " đã được cập nhật thành công.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    public void add() {
        khachHang = new KhachHang();

        khachHang.setTenKhach(tenField.getText());
        khachHang.setGioiTinh(gioiTinhToggle.isSelected());
        if (!cmndField.getText().equals(""))
            khachHang.setCmnd(Long.parseLong(cmndField.getText()));
        if (!dienThoaiField.getText().equals(""))
            khachHang.setDienThoai(Long.parseLong(dienThoaiField.getText()));
        khachHang.setEmail(emailField.getText());
        khachHang.setDiaChi(diaChiField.getText());
        khachHang.setGhiChu(ghiChuField.getText());

        boolean success = KhachHangDAO.getInstance().create(khachHang);
        if (success) {
            System.out.println("Sucessfully added new user: " + khachHang.getTenKhach());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công!");
            alert.setHeaderText("Thêm khách hàng thành công");
            alert.setContentText("Khách hàng " + khachHang.getTenKhach() + " đã được thêm thành công.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    public boolean validate() {
        String err = "";
        if (tenField.getText().equals("")) {
            ExHandler.handle(new Exception("Không được bỏ trống họ tên.\n"));
            return false;
        } else return true;
    }

    public KhachHang getKhachHangMoi() {
        return khachHang;
    }
}

