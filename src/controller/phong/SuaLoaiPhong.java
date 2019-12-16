package controller.phong;

import dao.LoaiPhongDAO;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import model.LoaiPhong;
import util.ExHandler;

public class SuaLoaiPhong {
    
    @FXML
    private Button confirmBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField idField;

    @FXML
    private TextField giaTienField;

    @FXML
    private TextField loaiPhongField;

    @FXML
    private TextField soNguoiField;

    @FXML
    private TextField ghiChuField;

    public void init() {
        uiInit();
        confirmBtn.setOnAction(event -> {
            if (validate()) {
                add();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public void init(LoaiPhong loaiPhong) {

        uiInit();

        idField.setText(loaiPhong.getMaLoaiPhong());
        loaiPhongField.setText(loaiPhong.getLoaiPhong());
        giaTienField.setText(String.valueOf(loaiPhong.getGiaTien()));
        soNguoiField.setText(String.valueOf(loaiPhong.getSoNguoi()));
        ghiChuField.setText(loaiPhong.getGhiChu());

        confirmBtn.setOnAction(event -> {
            if (validate()) {
                update(loaiPhong);
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public void uiInit() {
        idField.textProperty().addListener((observable, oldValue, newValue) -> idField.setText(newValue.substring(0,3)));

        giaTienField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                giaTienField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        soNguoiField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                soNguoiField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        cancelBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());
    }

    public void update(LoaiPhong loaiPhong) {
        loaiPhong.setMaLoaiPhong(idField.getText());
        loaiPhong.setLoaiPhong(loaiPhongField.getText());
        loaiPhong.setGiaTien(Long.parseLong(giaTienField.getText()));
        loaiPhong.setSoNguoi(Integer.parseInt(soNguoiField.getText()));
        loaiPhong.setGhiChu(ghiChuField.getText());

        boolean success = LoaiPhongDAO.getInstance().update(loaiPhong);

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công!");
            alert.setContentText("Loại phòng " + loaiPhong.getLoaiPhong() + " đã được cập nhật thành công.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    public void add() {
        LoaiPhong loaiPhong = new LoaiPhong(
                idField.getText(),
                loaiPhongField.getText(),
                Long.parseLong(giaTienField.getText()),
                Integer.parseInt(soNguoiField.getText()),
                ghiChuField.getText()
        );

        boolean success = LoaiPhongDAO.getInstance().create(loaiPhong);
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công!");
            alert.setContentText("Loại phòng " + loaiPhong.getLoaiPhong() + " đã được thêm thành công.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    public boolean validate() {
        String err = "";
        if (idField.getText().isBlank()) {
            err += "Không được bỏ trống mã loại phòng.\n";
        }
        if (loaiPhongField.getText().isBlank()) {
            err += "Không được bỏ trống loại phòng.\n";
        }
        if (giaTienField.getText().isBlank()) {
            err += "Không được bỏ trống giá.\n";
        } else if (Long.parseLong(giaTienField.getText()) < 10000) {
            err += "Giá loại phòng không hợp lệ.\n";
        }
        if (soNguoiField.getText().isBlank()) {
            err += "Không được bỏ trống số người.\n";
        }
        if (err.isBlank())
            return true;
        else {
            ExHandler.handle(new Exception(err));
            return false;
        }
    }
}

