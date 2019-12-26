package controller.vatTu;

import controller.basic.KhungUngDung;
import controller.nhanVien.SuaNhanVien;
import controller.phong.SuaPhong;
import dao.ChiTietPhongDAO;
import dao.LoaiPhongDAO;
import dao.NhanVienDAO;
import dao.PhongDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ChiTietPhong;
import model.LoaiPhong;
import model.NhanVien;
import model.Phong;
import util.ExceptionHandler;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class QLVatTu {

    @FXML
    private TextField searchInputField;

    @FXML
    private Button refreshBtn;

    @FXML
    private TableView<ChiTietPhong> vatTuTable;

    @FXML
    private Button searchBtn;

    @FXML
    private ChoiceBox<Phong> phongChoiceBox;

    @FXML
    private ChoiceBox<String> searchChoiceBox;

    @FXML
    private Label dieuKienLabel;

    @FXML
    private ImageView refreshIcon;

    private ObservableList<ChiTietPhong> data;
    private ArrayList<Phong> dsPhong;

    public void init(KhungUngDung c) {
        dsPhong = PhongDAO.getInstance().getAll(LoaiPhongDAO.getInstance().getMap());
        phongChoiceBox.getItems().addAll(dsPhong);

        TableColumn<ChiTietPhong, Integer> phongCol = new TableColumn<>("Phòng");
        TableColumn<ChiTietPhong, String> tenCol = new TableColumn<>("Tên đồ");
        TableColumn<ChiTietPhong, Integer> slCol = new TableColumn<>("Số lượng");
        TableColumn<ChiTietPhong, String> trangThaiCol = new TableColumn<>("Trạng thái");
        TableColumn<ChiTietPhong, String> donViCol = new TableColumn<>("Đơn vị");
        TableColumn<ChiTietPhong, String> giaTienCol = new TableColumn<>("Giá tiền");
        TableColumn<ChiTietPhong, String> ghiChuCol = new TableColumn<>("Ghi chú");


        phongCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPhong().getMaPhong()));
        tenCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getTenDo()));
        slCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getSoLuong()));
        trangThaiCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getTrangThaiString()));
        donViCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getDonVi()));
        giaTienCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getGiaTien())));
        ghiChuCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getGhiChu()));

        vatTuTable.getColumns().addAll(phongCol, tenCol, slCol, trangThaiCol, donViCol, giaTienCol, ghiChuCol);

        vatTuTable.setRowFactory(tv -> {
            TableRow<NhanVien> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ChiTietPhong chiTietPhong = vatTuTable.getSelectionModel().getSelectedItem();

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getClassLoader().getResource("view/phong/suaPhong.fxml"));
                    try {
                        Parent editRoot = loader.load();
                        new JMetro(editRoot, Style.LIGHT);

                        Stage stage = new Stage();
                        Scene scene = new Scene(editRoot);
                        stage.setTitle("Sửa phòng");
                        stage.setScene(scene);
                        stage.setResizable(false);
                        SuaPhong suaPhong = loader.getController();
                        suaPhong.init(chiTietPhong.getPhong());

                        stage.showAndWait();
                        reloadData();
                        vatTuTable.refresh();
                    } catch (IOException e) {
                        ExceptionHandler.handle(e);
                    }
                }
            });
            return row;
        });
    }

    public void export() {
    }

    public void importData() {

    }

    public void reloadData() {
        data = FXCollections.observableArrayList(ChiTietPhongDAO.getInstance().getAll(dsPhong));
        vatTuTable.setItems(data);
    }
}
