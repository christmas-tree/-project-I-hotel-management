package controller.khachSan;

import dao.DatPhongDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.DatPhong;
import model.Phong;
import util.AlertGenerator;
import util.ExceptionHandler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class NhanPhongDaDat {

    @FXML
    private Button huyBtn;

    @FXML
    private Button huyDatPhongBtn;

    @FXML
    private TableView<DatPhong> bangTable;

    @FXML
    private TableColumn<DatPhong, Timestamp> ngayDenCol;

    @FXML
    private Button nhanPhongBtn;

    @FXML
    private TableColumn<DatPhong, String> tenKhachCol;

    @FXML
    private TableColumn<DatPhong, Timestamp> ngayDatCol;

    @FXML
    private TableColumn<DatPhong, Timestamp> ngayDiCol;

    @FXML
    private TableColumn<DatPhong, String> tienCocCol;

    private ObservableList<DatPhong> dsDatPhong;

    public void init(ArrayList<Phong> dsPhong) {
        dsDatPhong = FXCollections.observableArrayList(DatPhongDAO.getInstance().getAllActiveBooking());

        // TABLE
        tenKhachCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getKhachHang().getTenKhach()));
        ngayDatCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getNgayDat()));
        ngayDenCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getNgayCheckinDk()));
        ngayDiCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getNgayCheckoutDk()));
        tienCocCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getTienDatCoc())));

        bangTable.setItems(dsDatPhong);

        nhanPhongBtn.setOnAction(event -> {
            if (bangTable.getSelectionModel().isEmpty()) {
                AlertGenerator.error("Bạn chưa chọn thông tin đặt phòng nào.");
            } else {
                DatPhong datPhong = bangTable.getSelectionModel().getSelectedItem();
                FXMLLoader loader = new FXMLLoader();
                if (datPhong.isKhachDoan()) {
                    loader.setLocation(getClass().getClassLoader().getResource("view/khachSan/nhanKhachDoan.fxml"));
                    try {
                        Parent editRoot = loader.load();
                        new JMetro(editRoot, Style.LIGHT);

                        Stage stage = new Stage();
                        Scene scene = new Scene(editRoot);
                        stage.setTitle("Nhận phòng khách đoàn");
                        stage.setScene(scene);
                        stage.setResizable(false);
                        NhanKhachDoan nhanKhachDoan = loader.getController();
                        nhanKhachDoan.init(datPhong, dsPhong);

                        stage.showAndWait();
                    } catch (IOException e) {
                        ExceptionHandler.handle(e);
                    }
                } else {
                    loader.setLocation(getClass().getClassLoader().getResource("view/khachSan/nhanKhachLe.fxml"));
                    try {
                        Parent editRoot = loader.load();
                        new JMetro(editRoot, Style.LIGHT);
                        Stage stage = new Stage();
                        Scene scene = new Scene(editRoot);
                        stage.setTitle("Nhận phòng khách lẻ");
                        stage.setScene(scene);
                        stage.setResizable(false);
                        NhanKhachLe nhanKhachLe = loader.getController();
                        nhanKhachLe.init(datPhong, dsPhong);

                        stage.showAndWait();
                    } catch (IOException e) {
                        ExceptionHandler.handle(e);
                    }
                }
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
        huyDatPhongBtn.setOnAction(event -> {
            if (bangTable.getSelectionModel().isEmpty()) {
                AlertGenerator.error("Bạn chưa chọn thông tin đặt phòng nào.");
            } else {
                if (AlertGenerator.confirm("Bạn chắc chắn muốn huỷ đơn đặt phòng này?")) {
                    DatPhong datPhong = bangTable.getSelectionModel().getSelectedItem();
                    datPhong.setDaHuy(true);
                    if (DatPhongDAO.getInstance().update(datPhong)) {
                        AlertGenerator.success("Huỷ thành công.");
                    } else {
                        AlertGenerator.error("Huỷ thât bại. Vui lòng thử lại.");
                    }
                }
                refresh();
            }
        });

        huyBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());
    }

    public void refresh() {
        dsDatPhong = FXCollections.observableArrayList(DatPhongDAO.getInstance().getAllActiveBooking());
        bangTable.setItems(dsDatPhong);
        bangTable.refresh();
    }

}
