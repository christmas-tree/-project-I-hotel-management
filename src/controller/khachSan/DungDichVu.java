package controller.khachSan;

import dao.ChiTietDatPhongDAO;
import dao.ChiTietDichVuDAO;
import dao.DichVuDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.*;
import util.AlertGenerator;

import java.sql.Timestamp;
import java.util.ArrayList;

public class DungDichVu {

    @FXML
    private Button luuBtn;

    @FXML
    private TextField slField;

    @FXML
    private TextField phongField;

    @FXML
    private TextField ngayField;

    @FXML
    private ComboBox<DichVu> dichVuCombo;

    @FXML
    private Button themBtn;

    @FXML
    private TableColumn<ChiTietDichVu, String> donGiaCol;

    @FXML
    private TableColumn<ChiTietDichVu, String> thanhTienCol;

    @FXML
    private Button huyBtn;

    @FXML
    private TableColumn<ChiTietDichVu, Timestamp> ngayCol;

    @FXML
    private TableView<ChiTietDichVu> bangTable;

    @FXML
    private TextField donGiaField;

    @FXML
    private TableColumn<ChiTietDichVu, Integer> soLuongCol;

    @FXML
    private Button xoaBtn;

    @FXML
    private TableColumn<ChiTietDichVu, String> donViCol;

    @FXML
    private TableColumn<ChiTietDichVu, String> dichVuCol;

    @FXML
    private TextField thanhTienField;

    private ObservableList<ChiTietDichVu> dsChiTietDichVu;
    private ArrayList<ChiTietDichVu> dsXoa = new ArrayList<>();

    private Timestamp now = new Timestamp(System.currentTimeMillis());

    public void init(Phong phong) {
        ArrayList<DichVu> dsDichVu = DichVuDAO.getInstance().getAll();
        ChiTietDatPhong chiTietDatPhong = ChiTietDatPhongDAO.getInstance().getByPhong(phong);
        dsChiTietDichVu = ChiTietDichVuDAO.getInstance().getAll(chiTietDatPhong);

        // INIT FIELDS
        dichVuCombo.getItems().addAll(dsDichVu);
        phongField.setText(String.valueOf(phong.getMaPhong()));
        ngayField.setText(now.toString());

        dichVuCombo.getSelectionModel().selectedItemProperty().addListener((observableValue, dichVuSingleSelectionModel, t1) -> {
            donGiaField.setText(String.format("%,3d", t1.getGiaDv()));
            thanhTienCol.setText(String.format("%,3d", t1.getGiaDv() * Integer.parseInt(slField.getText())));
        });
        slField.textProperty().addListener((observableValue, s, t1) -> {
            slField.setText(t1.replaceAll("[^\\d]", ""));
            thanhTienField.setText(String.format("%,3d", dichVuCombo.getSelectionModel().getSelectedItem().getGiaDv() * Integer.parseInt(slField.getText())));
        });

        // TABLE

        dichVuCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getDichVu().getTenDv()));
        ngayCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getNgayDv()));
        donGiaCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getDichVu().getGiaDv())));
        donViCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getDichVu().getDonVi()));
        soLuongCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getSoLuong()));
        thanhTienCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getThanhTien())));

        bangTable.setItems(dsChiTietDichVu);

        // BUTTONS
        themBtn.setOnAction(event -> {
            if (validate()) {
                ChiTietDichVu chiTietDichVu = new ChiTietDichVu(
                        dichVuCombo.getSelectionModel().getSelectedItem(),
                        chiTietDatPhong.getDatPhong(),
                        phong,
                        now,
                        Integer.parseInt(slField.getText()),
                        Integer.parseInt(thanhTienField.getText().replaceAll("[^\\d]", ""))
                );
                dsChiTietDichVu.add(chiTietDichVu);
            }
        });

        xoaBtn.setOnAction(event -> {
            if (bangTable.getSelectionModel().isEmpty()) {
                AlertGenerator.error("Bạn chưa chọn chi tiết dịch vụ nào.");
            } else {
                ChiTietDichVu chiTietDichVu = bangTable.getSelectionModel().getSelectedItem();
                dsChiTietDichVu.remove(chiTietDichVu);
                dsXoa.add(chiTietDichVu);
            }
        });

        huyBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());
        luuBtn.setOnAction(event -> {
            if (ChiTietDichVuDAO.getInstance().update(dsChiTietDichVu, dsXoa)) {
                AlertGenerator.success("Cập nhật thông tin sử dụng dịch vụ phòng " + phong.getMaPhong() + "thành công!");
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } else {
                AlertGenerator.error("Cập nhật thông tin sử dụng dịch vụ phòng " + phong.getMaPhong() + "thất bại!");
            }
        });
    }

    public boolean validate() {
        String err = "";
        if (dichVuCombo.getSelectionModel().isEmpty())
            err += "Không được bỏ trống dịch vụ.\n";
        if (soLuongCol.getText().isBlank())
            err += "Không được bỏ trống số lượng.\n";
        if (err.equals("")) {
            return true;
        } else {
            AlertGenerator.error(err);
            return false;
        }
    }

}
