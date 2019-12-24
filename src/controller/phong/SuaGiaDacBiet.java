package controller.phong;

import dao.GiaPhongTroiDAO;
import dao.LoaiPhongDAO;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import model.GiaPhongTroi;
import model.LoaiPhong;
import util.AlertGenerator;

public class SuaGiaDacBiet {

    @FXML
    private Button luuBtn;

    @FXML
    private TextField tenField;

    @FXML
    private TextArea ghiChuField;

    @FXML
    private ComboBox<String> phepTinhCombo;

    @FXML
    private Button huyBtn;

    @FXML
    private DatePicker ngayKtDate;

    @FXML
    private ComboBox<String> lapLaiCombo;

    @FXML
    private Label idLabel;

    @FXML
    private Label giaGocLabel;

    @FXML
    private ComboBox<LoaiPhong> loaiPhongCombo;

    @FXML
    private TextField giaTienField;

    @FXML
    private DatePicker ngayBdDate;

    @FXML
    private TextField heSoField;

    @FXML
    private Label quyTacLabel;

    private GiaPhongTroi giaPhongTroi;

    public void init(GiaPhongTroi giaPhongTroi) {
        uiInit();
        this.giaPhongTroi = giaPhongTroi;
        idLabel.setText(String.format("%04d", giaPhongTroi.getMaGiaPhong()));
        loaiPhongCombo.getSelectionModel().select(giaPhongTroi.getLoaiPhong());
        tenField.setText(giaPhongTroi.getTen());
        ngayBdDate.setValue(giaPhongTroi.getNgayBatDauLocalDate());
        ngayKtDate.setValue(giaPhongTroi.getNgayKetThucLocalDate());
        lapLaiCombo.getSelectionModel().select(giaPhongTroi.getLapLai());
        giaTienField.setText(String.valueOf(giaPhongTroi.getGiaTien()));
        ghiChuField.setText(giaPhongTroi.getGhiChu());

        luuBtn.setOnAction(event -> {
            if (validate()) {
                update();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public void init() {
        uiInit();
        idLabel.setVisible(false);
        quyTacLabel.setVisible(false);

        luuBtn.setOnAction(event -> {
            if (validate()) {
                add();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public void uiInit() {
        // Combobox
        loaiPhongCombo.getItems().addAll(LoaiPhongDAO.getInstance().getAll());
        lapLaiCombo.getItems().addAll(GiaPhongTroi.dsChuKy);
        phepTinhCombo.getItems().addAll("+", "-", "x", "/");
        phepTinhCombo.getSelectionModel().select(0);

        // Fields
        heSoField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                heSoField.setText(s);
            }
        });
        giaTienField.textProperty().addListener((observableValue, s, t1) -> {
            giaTienField.setText(t1.replaceAll("[^\\d]", ""));
        });

        // Listeners
        phepTinhCombo.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> tinhGiaTien());
        heSoField.textProperty().addListener((observableValue, s, t1) -> {
            System.out.println("Dang tinh gia tien");
            tinhGiaTien();
        });
        loaiPhongCombo.getSelectionModel().selectedItemProperty().addListener((observableValue, loaiPhong, t1) -> {
            giaGocLabel.setText(String.format("%,3d", t1.getGiaTien()) + " đ");
        });

        // Buttons
        huyBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());
    }

    public void tinhGiaTien() {
        if (loaiPhongCombo.getSelectionModel().isEmpty() || phepTinhCombo.getSelectionModel().isEmpty() || heSoField.getText().isBlank()) {
        } else {
            switch (phepTinhCombo.getSelectionModel().getSelectedIndex()) {
                case 0:
                    giaTienField.setText(String.format("%,3d", Long.parseLong(heSoField.getText().replaceAll("[^\\d]", "")) + loaiPhongCombo.getSelectionModel().getSelectedItem().getGiaTien()));
                    break;
                case 1:
                    giaTienField.setText(String.format("%,3d", loaiPhongCombo.getSelectionModel().getSelectedItem().getGiaTien() - Long.parseLong(heSoField.getText().replaceAll("[^\\d]", ""))));
                    break;

                case 2:
                    Double ketQua = loaiPhongCombo.getSelectionModel().getSelectedItem().getGiaTien() * Double.parseDouble(heSoField.getText());
                    giaTienField.setText(String.format("%,3d", Math.round(ketQua / 1000) * 1000));
                    break;
                case 3:
                    Double ketQua2 = loaiPhongCombo.getSelectionModel().getSelectedItem().getGiaTien() / Double.parseDouble(heSoField.getText());
                    giaTienField.setText(String.format("%,3d", String.valueOf(Math.round(ketQua2 / 1000) * 1000)));
                    break;
            }
        }
    }

    public void add() {
        giaPhongTroi = new GiaPhongTroi(
                loaiPhongCombo.getSelectionModel().getSelectedItem(),
                tenField.getText(),
                ngayBdDate.getValue(),
                ngayKtDate.getValue(),
                lapLaiCombo.getSelectionModel().getSelectedIndex(),
                Long.parseLong(giaTienField.getText()),
                ghiChuField.getText()
        );
        if (GiaPhongTroiDAO.getInstance().create(giaPhongTroi))
            AlertGenerator.success("Tạo giá phòng thành công!");
        else
            AlertGenerator.error("Tạo giá phòng thất bại!");
    }

    public void update() {
        giaPhongTroi.setProperties(
                loaiPhongCombo.getSelectionModel().getSelectedItem(),
                tenField.getText(),
                ngayBdDate.getValue(),
                ngayKtDate.getValue(),
                lapLaiCombo.getSelectionModel().getSelectedIndex(),
                Long.parseLong(giaTienField.getText().replaceAll("[^\\d]", "")),
                ghiChuField.getText()
        );
        if (GiaPhongTroiDAO.getInstance().update(giaPhongTroi))
            AlertGenerator.success("Cập nhật giá phòng thành công!");
        else
            AlertGenerator.error("Cập nhật giá phòng thất bại!");
    }

    public boolean validate() {
        String err = "";

        if (loaiPhongCombo.getSelectionModel().isEmpty())
            err += "Không được bỏ trống loại phòng.\n";
        if (tenField.getText().isBlank())
            err += "Không được bỏ trống diễn giải.\n";
        if (ngayBdDate.getValue() == null)
            err += "Không được bỏ trống ngày bắt đầu.\n";
        if (ngayKtDate.getValue() == null)
            err += "Không được bỏ trống ngày kết thúc.\n";
        if (giaTienField.getText().isEmpty())
            err += "Không được bỏ trống giá tiền.\n";
        if (lapLaiCombo.getSelectionModel().isEmpty())
            err += "Không được bỏ trống chu kỳ lặp lại.\n";
        if (err.equals("")) {
            return true;
        } else {
            AlertGenerator.error(err);
            return false;
        }
    }

}
