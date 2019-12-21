package controller.phong;

import controller.basic.IndexController;
import controller.khachSan.DungDichVu;
import controller.khachSan.NhanKhachLe;
import controller.khachSan.TraPhongKhachLe;
import dao.LoaiPhongDAO;
import dao.PhongDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.LoaiPhong;
import model.Phong;
import util.AlertGenerator;
import util.ExHandler;

import java.io.IOException;
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

    public void init(IndexController c) {
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
    }

    public void themPhongUI(Phong phong) {
        final String[] MAU_TRANG_THAI = {"#359200", "#8a0000", "#1e90ff", "#ffd01f"};

        Rectangle rectangle = new Rectangle();
        rectangle.setArcHeight(5.0);
        rectangle.setArcWidth(5.0);
        rectangle.setFill(Color.web(MAU_TRANG_THAI[phong.getTrangThai()]));
        rectangle.setStroke(Color.WHITE);
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setHeight(80.0);
        rectangle.setWidth(110.0);
        Label label = new Label(String.valueOf(phong.getMaPhong()));
        label.setFont(new Font("System Bold", 14.0));

        StackPane phongUI = new StackPane();
        phongUI.getChildren().addAll(rectangle, label);

        double firstTopAnchor = 110.0, firstLeftAnchor = 105.0;

        AnchorPane.setLeftAnchor(phongUI, firstLeftAnchor + 115.0 * (phong.getMaPhong() % 100 - 1));
        AnchorPane.setTopAnchor(phongUI, firstTopAnchor + (85 * (phong.getTang() - 1)));

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
                ExHandler.handle(e);
            }
        }
    }

    public void themTangUI(int tang) {
        Rectangle rectangle = new Rectangle();
        rectangle.setArcHeight(5.0);
        rectangle.setArcWidth(5.0);
        rectangle.setFill(Color.web("#797979"));
        rectangle.setStroke(Color.WHITE);
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setHeight(80.0);
        rectangle.setWidth(65.0);
        Label label = new Label(String.valueOf(tang));
        label.setFont(new Font("System Bold", 12.0));

        StackPane tangUI = new StackPane();
        tangUI.getChildren().addAll(rectangle, label);

        double firstTopAnchor = 110.0, firstLeftAnchor = 35.0;

        AnchorPane.setLeftAnchor(tangUI, firstLeftAnchor);
        AnchorPane.setTopAnchor(tangUI, firstTopAnchor + 85 * (tang - 1));

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
            ExHandler.handle(e);
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
            ExHandler.handle(e);
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
        if (phong.getTrangThai() != Phong.SANSANG) {
            AlertGenerator.error("Phòng đang ở trạng thái " + phong.getTrangThaiString() + ". Chỉ được đặt phòng trước, không được nhận phòng.");
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
                nhanKhachLe.initBook(phong, dsPhong);

                stage.showAndWait();
                refreshBtn.fire();
            } catch (IOException e) {
                ExHandler.handle(e);
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
                ExHandler.handle(e);
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
                ExHandler.handle(e);
            }
        }
    }

//    public void nhanPhongDoan() {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getClassLoader().getResource("view/khachSan/nhanKhachLe.fxml"));
//        try {
//            Parent editRoot = loader.load();
//            new JMetro(editRoot, Style.LIGHT);
//
//            Stage stage = new Stage();
//            Scene scene = new Scene(editRoot);
//            stage.setTitle("Nhận phòng khách lẻ");
//            stage.setScene(scene);
//            stage.setResizable(false);
//             nhanKhachLe = loader.getController();
//            nhanKhachLe.init();
//
//            stage.showAndWait();
//        } catch (IOException e) {
//            ExHandler.handle(e);
//        }
//    }
}
