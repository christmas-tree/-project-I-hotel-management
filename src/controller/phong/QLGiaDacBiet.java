package controller.phong;

import controller.basic.KhungUngDung;
import dao.GiaPhongTroiDAO;
import dao.LoaiPhongDAO;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.GiaPhongTroi;
import model.LoaiPhong;
import util.AlertGenerator;
import util.ExceptionHandler;

import java.io.IOException;
import java.sql.Date;


public class QLGiaDacBiet {

    @FXML
    private TableView<GiaPhongTroi> bangTable;

    @FXML
    private TableColumn<GiaPhongTroi, String> idCol;

    @FXML
    private TableColumn<GiaPhongTroi, String> phongCol;

    @FXML
    private TableColumn<GiaPhongTroi, String> tenCol;

    @FXML
    private TableColumn<GiaPhongTroi, Date> ngayBdCol;

    @FXML
    private TableColumn<GiaPhongTroi, Date> ngayKtCol;

    @FXML
    private TableColumn<GiaPhongTroi, String> lapLaiCol;

    @FXML
    private TableColumn<GiaPhongTroi, String> giaTienCol;

    @FXML
    private TableColumn<GiaPhongTroi, String> ghiChuCol;

    @FXML
    private Button addBtn;

    @FXML
    private Button editBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private ChoiceBox<LoaiPhong> searchConditionChoiceBox;

    @FXML
    private ImageView refreshIcon;

    private ObservableList<GiaPhongTroi> data;

    public void init(KhungUngDung c) {

        idCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%03d", p.getValue().getMaGiaPhong())));
        phongCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getLoaiPhong().getLoaiPhong()));
        tenCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getTen()));
        ngayBdCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getNgayBatDau()));
        ngayKtCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getNgayKetThuc()));
        lapLaiCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getLapLaiString()));
        giaTienCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getGiaTien())));
        ghiChuCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getGhiChu()));

        phongCol.setSortType(TableColumn.SortType.ASCENDING);

        reloadData();

        bangTable.setRowFactory(tv -> {
            TableRow<GiaPhongTroi> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()))
                    edit();
            });
            return row;
        });

        searchBtn.setOnAction(event -> filter());
        editBtn.setOnAction(event -> edit());
        addBtn.setOnAction(event -> add());
        deleteBtn.setOnAction(event -> delete());
        refreshBtn.setOnAction(event -> refresh());

        searchConditionChoiceBox.selectionModelProperty().addListener((observableValue, loaiPhongSingleSelectionModel, t1) -> filter());

//        c.addMenu.setDisable(false);
//        c.editMenu.setDisable(false);
//        c.deleteMenu.setDisable(false);
//        c.exportMenu.setDisable(false);
//        c.importMenu.setDisable(false);
//
//        c.addMenu.setOnAction(e -> addBtn.fire());
//        c.editMenu.setOnAction(e -> editBtn.fire());
//        c.deleteMenu.setOnAction(e -> deleteBtn.fire());
//        c.exportMenu.setOnAction(e -> export());
//        c.importMenu.setOnAction(event -> importData());
    }

    public void reloadData() {
        searchConditionChoiceBox.getSelectionModel().clearSelection();
        searchConditionChoiceBox.getItems().clear();

        searchConditionChoiceBox.setItems(LoaiPhongDAO.getInstance().getAll());
        data = GiaPhongTroiDAO.getInstance().getAll();
        bangTable.setItems(data);
    }

    public void refresh() {
        reloadData();

        RotateTransition rt = new RotateTransition(Duration.millis(750), refreshIcon);
        rt.setByAngle(360 * 3);
        rt.setCycleCount(1);
        rt.setInterpolator(Interpolator.EASE_BOTH);
        rt.play();
    }

    public void filter() {
        Runnable searchTask = () -> {
            data = GiaPhongTroiDAO.getInstance().get(searchConditionChoiceBox.getSelectionModel().getSelectedItem());
            Platform.runLater(() -> {
                bangTable.setItems(data);
            });
        };
        new Thread(searchTask).start();
    }

    public void add() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/phong/suaGiaDacBiet.fxml"));
            Parent editRoot = loader.load();
            new JMetro(editRoot, Style.LIGHT);

            Stage stage = new Stage();
            Scene scene = new Scene(editRoot);

            stage.setTitle("Thêm quy tắc giá");
            stage.setScene(scene);
            stage.setResizable(false);

            SuaGiaDacBiet suaGiaDacBiet = loader.getController();
            suaGiaDacBiet.init();

            stage.showAndWait();
            reloadData();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }

    }

    public void delete() {
        GiaPhongTroi giaDaChon = bangTable.getSelectionModel().getSelectedItem();

        if (giaDaChon == null) {
            AlertGenerator.error("Bạn chưa chọn hàng nào.");
            return;
        }

        if (AlertGenerator.confirm("Bạn có chắc chắn muốn xoá quy tắc giá phòng \"" + giaDaChon.getTen() + "\"?")) {
            if (GiaPhongTroiDAO.getInstance().delete(giaDaChon)) {
                AlertGenerator.success("Xoá thành công!");
                reloadData();
            } else {
                AlertGenerator.error("Xoá thất bại!");
            }
        }
    }

    public void edit() {
        GiaPhongTroi giaDaChon = bangTable.getSelectionModel().getSelectedItem();

        if (giaDaChon == null) {
            AlertGenerator.error("Bạn chưa chọn hàng nào.");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/phong/suaGiaDacBiet.fxml"));
        try {
            Parent editRoot = loader.load();
            new JMetro(editRoot, Style.LIGHT);

            Stage stage = new Stage();
            Scene scene = new Scene(editRoot);
            stage.setTitle("Sửa quy tắc giá");
            stage.setScene(scene);
            stage.setResizable(false);
            SuaGiaDacBiet suaGiaDacBiet = loader.getController();
            suaGiaDacBiet.init(giaDaChon);

            stage.showAndWait();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }

        reloadData();
    }
}
