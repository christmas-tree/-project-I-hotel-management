/*
 * Copyright (c) 2019 Nghia Tran.
 * Project I - Library Management System
 */

package controller.dichVu;

import dao.DichVuDAO;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import model.DichVu;
import util.ExHandler;

public class SuaDichVu {

    @FXML
    private Button confirmBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField idField;

    @FXML
    private TextField giaField;

    @FXML
    private TextField tenDvField;

    @FXML
    private TextArea ghiChuField;

    @FXML
    private TextField donViField;

    public void init() {
        uiInit();
        confirmBtn.setOnAction(event -> {
            if (validate()) {
                add();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public void init(DichVu dichVu) {

        uiInit();

        idField.setText(String.format("%06d", dichVu.getMaDv()));
        tenDvField.setText(dichVu.getTenDv());
        donViField.setText(dichVu.getDonVi());
        giaField.setText(String.valueOf(dichVu.getGiaDv()));
        ghiChuField.setText(dichVu.getGhiChu());

        confirmBtn.setOnAction(event -> {
            if (validate()) {
                update(dichVu);
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public void uiInit() {
        giaField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                giaField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        cancelBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());
    }

    public void update(DichVu dichVu) {
        dichVu.setTenDv(tenDvField.getText());
        dichVu.setGiaDv(Long.parseLong(giaField.getText()));
        dichVu.setDonVi(donViField.getText());
        dichVu.setGhiChu(ghiChuField.getText());

        boolean success = DichVuDAO.getInstance().update(dichVu);

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công!");
            alert.setHeaderText("Cập nhật thông tin dịch vụ thành công");
            alert.setContentText("Dịch vụ " + dichVu.getTenDv() + " đã được cập nhật thành công.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    public void add() {
        DichVu dichVu = new DichVu();

        dichVu.setTenDv(tenDvField.getText());
        dichVu.setGiaDv(Long.parseLong(giaField.getText()));
        dichVu.setDonVi(donViField.getText());
        dichVu.setGhiChu(ghiChuField.getText());

        boolean success = DichVuDAO.getInstance().create(dichVu);
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công!");
            alert.setHeaderText("Thêm Dịch vụ thành công");
            alert.setContentText("Dịch vụ " + dichVu.getTenDv() + " đã được thêm thành công.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    public boolean validate() {
        String err = "";
        if (tenDvField.getText().isBlank()) {
            err += "Không được bỏ trống tên dịch vụ.\n";
        }
        if (donViField.getText().isBlank()) {
            err += "Không được bỏ trống đơn vị.\n";
        }
        if (giaField.getText().isBlank()) {
            err += "Không được bỏ trống giá dịch vụ.\n";
        } else if (Long.parseLong(giaField.getText()) < 1000) {
            err += "Giá dịch vụ không hợp lệ.\n";
        }
        if (err.isBlank())
            return true;
        else {
            ExHandler.handle(new Exception(err));
            return false;
        }
    }
}

