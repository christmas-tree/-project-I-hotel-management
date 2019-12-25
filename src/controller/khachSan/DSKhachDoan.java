package controller.khachSan;

import controller.basic.KhungUngDung;
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

public class DSKhachDoan {

    @FXML
    private Button huyBtn;

    @FXML
    private Button traPhongBtn;

    @FXML
    private TableView<DatPhong> bangTable;

    @FXML
    private TableColumn<DatPhong, Timestamp> ngayDenCol;

    @FXML
    private TableColumn<DatPhong, String> tenKhachCol;

    @FXML
    private TableColumn<DatPhong, Timestamp> ngayDatCol;

    @FXML
    private TableColumn<DatPhong, Timestamp> ngayDiCol;

    private ObservableList<DatPhong> dsDatPhong;

    public void init(KhungUngDung c, ArrayList<Phong> dsPhong) {
        dsDatPhong = FXCollections.observableArrayList(DatPhongDAO.getInstance().getAllRunning());

        // TABLE
        tenKhachCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getKhachHang().getTenKhach()));
        ngayDatCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getNgayDat()));
        ngayDenCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getNgayCheckinDk()));
        ngayDiCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getNgayCheckoutDk()));

        bangTable.setItems(dsDatPhong);

        traPhongBtn.setOnAction(event -> {
            if (bangTable.getSelectionModel().isEmpty()) {
                AlertGenerator.error("Bạn chưa chọn thông tin đặt phòng nào.");
            } else {
                DatPhong datPhong = bangTable.getSelectionModel().getSelectedItem();
                FXMLLoader loader = new FXMLLoader();
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
                    nhanKhachDoan.init(datPhong, dsPhong, c.currentUser);

                    stage.showAndWait();
                } catch (IOException e) {
                    ExceptionHandler.handle(e);
                }
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });

        huyBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());
    }

}
