package controller.phong;

import com.flexganttfx.extras.GanttChartToolBar;
import com.flexganttfx.model.Layer;
import com.flexganttfx.view.GanttChart;
import com.flexganttfx.view.graphics.GraphicsBase;
import com.flexganttfx.view.timeline.Timeline;
import controller.basic.KhungUngDung;
import controller.khachSan.*;
import dao.ChiTietDatPhongDAO;
import dao.LoaiPhongDAO;
import dao.PhongDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.ChiTietDatPhong;
import model.LoaiPhong;
import model.Phong;
import model.timeline.ChiTietDatPhongWrapper;
import model.timeline.PhongWrapper;
import util.AlertGenerator;
import util.ExceptionHandler;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class QLPhong {

    @FXML
    AnchorPane anchorPane;

    @FXML
    Button khachDoanBtn;

    @FXML
    Button timelineBtn;

    @FXML
    Button checkinBtn;

    @FXML
    Button addBtn;

    @FXML
    Button refreshBtn;

    HashMap<String, LoaiPhong> dsLoaiPhong;
    ArrayList<Phong> dsPhong;

    ArrayList<StackPane> dsPhongUI = new ArrayList<>();
    int soTang = 0;

    public void init(KhungUngDung c) {
        // Init Phong
        dsLoaiPhong = LoaiPhongDAO.getInstance().getMap();
        dsPhong = PhongDAO.getInstance().getAll(dsLoaiPhong);

        for (int i = 0; i < dsPhong.size(); i++) {
            Phong phong = dsPhong.get(i);
            if (phong.getTang() > soTang) {
                soTang = phong.getTang();
                themTangUI(soTang);
            }
            themPhongUI(phong);
        }

        // Button

        addBtn.setOnAction(event -> themPhong());
        refreshBtn.setOnAction(event -> {

        });
        checkinBtn.setOnAction(event -> checkinDaDat(c));
        timelineBtn.setOnAction(event -> timeline());

        MenuItem nhanPhongDoanMenu = new MenuItem("Đặt trước/Nhận trực tiếp khách đoàn");
        MenuItem traPhongDoanMenu = new MenuItem("Trả phòng Khách đoàn");
        ContextMenu khachdoanContextMenu = new ContextMenu();
        khachdoanContextMenu.getItems().addAll(nhanPhongDoanMenu, traPhongDoanMenu);

        nhanPhongDoanMenu.setOnAction(event -> nhanPhongDoan());
        traPhongDoanMenu.setOnAction(event -> traPhongDoan(c));

        khachDoanBtn.setOnAction(event -> khachdoanContextMenu.show(khachDoanBtn, khachDoanBtn.getLayoutX(), khachDoanBtn.getLayoutY()));
    }

    public void themPhongUI(Phong phong) {
        final String[] MAU_TRANG_THAI = {"#359200", "#8a0000", "#1e90ff", "#ffd01f"};

        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.web(MAU_TRANG_THAI[phong.getTrangThai()]));
        rectangle.setStroke(Color.WHITE);
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setHeight(100.0);
        rectangle.setWidth(150.0);
        Label phongLabel = new Label(String.valueOf(phong.getMaPhong()));
        phongLabel.setFont(new Font("System Bold", 14.0));

        Label loaiPhongLabel = new Label(phong.getLoaiPhong().getLoaiPhong());
        loaiPhongLabel.setFont(new Font("System Bold", 16.0));

        StackPane phongUI = new StackPane();
        phongUI.getChildren().addAll(rectangle, phongLabel, loaiPhongLabel);
        StackPane.setMargin(loaiPhongLabel, new Insets(0.0, 0.0, 20.0,0.0));
        StackPane.setMargin(phongLabel, new Insets(20.0, 0.0, 0.0,0.0));

        double firstTopAnchor = 110.0, firstLeftAnchor = 105.0;

        AnchorPane.setLeftAnchor(phongUI, firstLeftAnchor + 155.0 * (phong.getMaPhong() % 100 - 1));
        AnchorPane.setTopAnchor(phongUI, firstTopAnchor + (105.0 * (phong.getTang() - 1)));

        dsPhongUI.add(phongUI);
        anchorPane.getChildren().add(phongUI);

        // Phong Context Menu
        ContextMenu phongContextMenu = new ContextMenu();
        MenuItem nhanPhongMenu = new MenuItem("Đặt trước/Nhận trực tiếp (Khách lẻ)");
        nhanPhongMenu.setOnAction(event -> nhanPhongLeMoi(phong));
        MenuItem traPhongMenu = new MenuItem("Trả phòng");
        traPhongMenu.setOnAction(event -> traPhong(phong));
        MenuItem themDvMenu = new MenuItem("Thêm sử dụng dịch vụ");
        themDvMenu.setOnAction(event -> themDichVu(phong));
        SeparatorMenuItem nganCach1 = new SeparatorMenuItem();
        MenuItem themPhongMenu = new MenuItem("Thêm phòng");
        themPhongMenu.setOnAction(event -> themPhong());
        MenuItem suaPhongMenu = new MenuItem("Sửa phòng");
        suaPhongMenu.setOnAction(event -> suaPhong(phong));
        MenuItem xoaPhongMenu = new MenuItem("Xoá phòng");
        xoaPhongMenu.setOnAction(event -> xoaPhong(phong));

        phongContextMenu.getItems().addAll(nhanPhongMenu, traPhongMenu, themDvMenu, nganCach1, themPhongMenu, suaPhongMenu, xoaPhongMenu);

        phongUI.setOnContextMenuRequested(contextMenuEvent -> phongContextMenu.show(phongUI, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));
        phongUI.setOnMouseClicked(event -> phongContextMenu.hide());
        renderChuotPhaiPhong(phong, phongContextMenu);

        // THAY DOI MAU
        phong.trangThaiProperty().addListener((observableValue, number, t1) -> {
            rectangle.setFill(Color.web(MAU_TRANG_THAI[t1.intValue()]));
            renderChuotPhaiPhong(phong, phongContextMenu);
        });
    }

    public void renderChuotPhaiPhong(Phong phong, ContextMenu phongContextMenu) {
        if (phong.getTrangThai() == Phong.DANGSUDUNG) {
            phongContextMenu.getItems().get(0).setDisable(false);
            phongContextMenu.getItems().get(1).setDisable(false);
            phongContextMenu.getItems().get(2).setDisable(false);
            phongContextMenu.getItems().get(6).setDisable(true);
        } else {
            phongContextMenu.getItems().get(0).setDisable(false);
            phongContextMenu.getItems().get(1).setDisable(true);
            phongContextMenu.getItems().get(2).setDisable(true);
            phongContextMenu.getItems().get(6).setDisable(false);
        }
    }

    private void themDichVu(Phong phong) {
        if (phong.getTrangThai() != Phong.DANGSUDUNG) {
            AlertGenerator.error("Phòng trống. Không thêm được dịch vụ.");
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/khachSan/dungDichVu.fxml"));
            try {
                Parent editRoot = loader.load();
                new JMetro(editRoot, Style.LIGHT);

                Stage stage = new Stage();
                Scene scene = new Scene(editRoot);
                stage.setTitle("Thêm dịch vụ");
                stage.setScene(scene);
                stage.setResizable(false);
                DungDichVu dungDichVu = loader.getController();
                dungDichVu.init(phong);

                stage.showAndWait();
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
        }
    }

    public void themTangUI(int tang) {
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.web("#797979"));
        rectangle.setStroke(Color.WHITE);
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setWidth(65.0);
        rectangle.setHeight(100.0);
        Label label = new Label(String.valueOf(tang));
        label.setFont(new Font("System Bold", 12.0));

        StackPane tangUI = new StackPane();
        tangUI.getChildren().addAll(rectangle, label);

        double firstTopAnchor = 110.0, firstLeftAnchor = 35.0;

        AnchorPane.setLeftAnchor(tangUI, firstLeftAnchor);
        AnchorPane.setTopAnchor(tangUI, firstTopAnchor + 105 * (tang - 1));

        anchorPane.getChildren().add(tangUI);
    }

    public void themPhong() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/phong/suaPhong.fxml"));
        try {
            Parent editRoot = loader.load();
            new JMetro(editRoot, Style.LIGHT);

            Stage stage = new Stage();
            Scene scene = new Scene(editRoot);
            stage.setTitle("Thêm phòng");
            stage.setScene(scene);
            stage.setResizable(false);
            SuaPhong suaPhong = loader.getController();
            suaPhong.init();

            stage.showAndWait();
            Phong phongMoi = suaPhong.getPhongMoi();

            if (phongMoi != null) {
                if (phongMoi.getTang() > soTang)
                    themTangUI(phongMoi.getTang());
                dsPhong.add(phongMoi);
                themPhongUI(phongMoi);
            }
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
    }

    public void suaPhong(Phong phong) {
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
            suaPhong.init(phong);

            stage.showAndWait();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
    }

    public void xoaPhong(Phong phong) {

        if (phong.getTrangThai() == Phong.DANGSUDUNG) {
            Alert resultAlert = new Alert(Alert.AlertType.ERROR);
            resultAlert.setTitle("Xoá thất bại!");
            resultAlert.setContentText("Phòng " + phong.getMaPhong() + " đang được sử dụng. Không được xoá!");
            resultAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            resultAlert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xoá");
        confirmAlert.setContentText("Bạn chắc chắn muốn xoá phòng " + phong.getMaPhong() + "?");
        confirmAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> choice = confirmAlert.showAndWait();

        if (choice.get() == ButtonType.OK) {
            if (PhongDAO.getInstance().delete(phong)) {
                int index = dsPhong.indexOf(phong);
                anchorPane.getChildren().remove(dsPhongUI.get(index));
                dsPhongUI.remove(index);
                dsPhong.remove(index);

                Alert resultAlert = new Alert(Alert.AlertType.INFORMATION);
                resultAlert.setTitle("Kết quả xoá");
                resultAlert.setHeaderText("Xoá thành công!");
                resultAlert.setContentText("Đã xoá phòng " + phong.getMaPhong() + " thành công.");
                resultAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                resultAlert.showAndWait();
            } else {
                Alert resultAlert = new Alert(Alert.AlertType.ERROR);
                resultAlert.setTitle("Kết quả xoá");
                resultAlert.setHeaderText("Xoá thất bại!");
                resultAlert.setContentText("Loại phòng " + phong.getLoaiPhong() + " chưa xoá khỏi CSDL.");
                resultAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                resultAlert.showAndWait();
            }
        }
    }

    public void nhanPhongLeMoi(Phong phong) {
        if (phong.getTrangThai() != Phong.SANSANG
                && AlertGenerator.confirm("Phòng đang ở trạng thái " + phong.getTrangThaiString() + ". Chỉ được đặt phòng trước, không được nhận phòng.")) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/khachSan/nhanKhachLe.fxml"));
            try {
                Parent editRoot = loader.load();
                new JMetro(editRoot, Style.LIGHT);

                Stage stage = new Stage();
                Scene scene = new Scene(editRoot);
                stage.setTitle("Đặt phòng khách lẻ");
                stage.setScene(scene);
                stage.setResizable(false);
                NhanKhachLe nhanKhachLe = loader.getController();
                nhanKhachLe.initBookFromPhong(phong, dsPhong);

                stage.showAndWait();
                refreshBtn.fire();
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
        } else {
            FXMLLoader loader = new FXMLLoader();
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
                nhanKhachLe.initNew(phong, dsPhong);

                stage.showAndWait();
                refreshBtn.fire();
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
        }
    }

    public void traPhong(Phong phong) {
        if (phong.getTrangThai() != Phong.DANGSUDUNG) {
            AlertGenerator.error("Phòng đang không có người sử dụng.");
            return;
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/khachSan/checkoutKhachLe.fxml"));
            try {
                Parent editRoot = loader.load();
                new JMetro(editRoot, Style.LIGHT);

                Stage stage = new Stage();
                Scene scene = new Scene(editRoot);
                stage.setTitle("Checkout");
                stage.setScene(scene);
                TraPhongKhachLe traPhongKhachLe = loader.getController();
                traPhongKhachLe.init(phong);

                stage.showAndWait();
                refreshBtn.fire();
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
        }
    }

    public void nhanPhongDoan() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/khachSan/nhanKhachDoan.fxml"));
        try {
            Parent editRoot = loader.load();
            new JMetro(editRoot, Style.LIGHT);

            Stage stage = new Stage();
            Scene scene = new Scene(editRoot);
            stage.setTitle("Nhận phòng khách lẻ");
            stage.setScene(scene);
            stage.setResizable(false);
            NhanKhachDoan nhanKhachDoan = loader.getController();
            nhanKhachDoan.init(dsPhong);

            stage.showAndWait();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
    }

    public void traPhongDoan(KhungUngDung c) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/khachSan/dsKhachDoan.fxml"));
        try {
            Parent editRoot = loader.load();
            new JMetro(editRoot, Style.LIGHT);

            Stage stage = new Stage();
            Scene scene = new Scene(editRoot);
            stage.setTitle("Danh sách đặt phòng đoàn");
            stage.setScene(scene);
            stage.setResizable(false);
            DSKhachDoan dsKhachDoan = loader.getController();
            dsKhachDoan.init(c, dsPhong);

            stage.showAndWait();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
    }

    public void timeline() {

        Timestamp now = new Timestamp(System.currentTimeMillis());
        ArrayList<ChiTietDatPhong> dsChiTietDatPhongActive = ChiTietDatPhongDAO.getInstance().getAllActive(dsPhong);

        PhongWrapper root = new PhongWrapper("Danh sách phòng");
        GanttChart<PhongWrapper> gantt = new GanttChart<>(root);
        root.setExpanded(true);

        Layer khachLe = new Layer("Khách lẻ");
        Layer khachDoan = new Layer("Khách đoàn");
        gantt.getLayers().addAll(khachLe, khachDoan);

        ArrayList<PhongWrapper> dsPhongWrapper = PhongWrapper.from(dsPhong);
        root.getChildren().addAll(dsPhongWrapper);
        for (ChiTietDatPhong chiTietDatPhong: dsChiTietDatPhongActive) {
            getPhongWrapper(chiTietDatPhong, dsPhongWrapper).addActivity(chiTietDatPhong.getDatPhong().isKhachDoan()?khachDoan:khachLe, new ChiTietDatPhongWrapper(chiTietDatPhong));
        }

        Timeline timeline = gantt.getTimeline();
        timeline.showTemporalUnit(ChronoUnit.HOURS, 10);

        GraphicsBase<PhongWrapper> graphics = gantt.getGraphics();

        graphics.setRowControlsFactory(new TimelineDatPhong(dsPhong));
        graphics.showEarliestActivities();

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(new GanttChartToolBar<>(gantt));
        borderPane.setCenter(gantt);

        Stage stage = new Stage();
        Scene scene = new Scene(borderPane);
        stage.setTitle("Phòng đã đặt");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    public void checkinDaDat(KhungUngDung c) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/khachSan/nhanPhongDaDat.fxml"));
        try {
            Parent editRoot = loader.load();
            new JMetro(editRoot, Style.LIGHT);

            Stage stage = new Stage();
            Scene scene = new Scene(editRoot);
            stage.setTitle("Danh sách đặt phòng");
            stage.setScene(scene);
            stage.setResizable(false);
            NhanPhongDaDat nhanPhongDaDat = loader.getController();
            nhanPhongDaDat.init(c, dsPhong);

            stage.showAndWait();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
    }

    private PhongWrapper getPhongWrapper(ChiTietDatPhong chiTietDatPhong, ArrayList<PhongWrapper> dsPhongWrapper) {
        for (PhongWrapper phongWrapper: dsPhongWrapper) {
            if (phongWrapper.getPhong().equals(chiTietDatPhong.getPhong()))
                return phongWrapper;
        }
        return null;
    }
}
