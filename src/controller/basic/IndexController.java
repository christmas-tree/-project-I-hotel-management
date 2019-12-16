/*
 * Copyright (c) 2019 Nghia Tran.
 * Project I - Library Management System
 */

package controller.basic;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.NhanVien;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.ExHandler;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

public class IndexController {

    @FXML
    private BorderPane window;

    @FXML
    private TreeView sideMenu;

    @FXML
    private MenuItem exitMenu;

    @FXML
    private MenuItem logOutMenu;

    @FXML
    public MenuItem editMenu;

    @FXML
    public MenuItem addMenu;

    @FXML
    public MenuItem deleteMenu;

    @FXML
    public MenuItem exportMenu;

    @FXML
    private MenuItem aboutMenu;

    @FXML
    public MenuItem importMenu;

    @FXML
    private MenuItem formBookMenu;

    @FXML
    private MenuItem formMetaMenu;

    @FXML
    private MenuItem formReaderMenu;

    @FXML
    private MenuItem formStaffMenu;

    public NhanVien currentUser;

    public void init(NhanVien user) {

        this.currentUser = user;

        TreeItem rootItem = new TreeItem("Menu");

        TreeItem transactMenu = new TreeItem("Quản lý mượn trả");
        transactMenu.getChildren().add(new TreeItem("Tìm kiếm giao dịch"));
        transactMenu.getChildren().add(new TreeItem("Thống kê mượn trả"));
        rootItem.getChildren().add(transactMenu);
        transactMenu.setExpanded(true);


        TreeItem bookMenu = new TreeItem("Quản lý sách");
        bookMenu.getChildren().add(new TreeItem("Tìm kiếm sách"));
        bookMenu.getChildren().add(new TreeItem("Thông tin phụ"));
        bookMenu.getChildren().add(new TreeItem("Thống kê sách"));
        rootItem.getChildren().add(bookMenu);
        bookMenu.setExpanded(true);

        TreeItem readerMenu = new TreeItem("Quản lý độc giả");
        readerMenu.getChildren().add(new TreeItem("Tìm kiếm độc giả"));
        readerMenu.getChildren().add(new TreeItem("Thống kê độc giả"));
        rootItem.getChildren().add(readerMenu);
        readerMenu.setExpanded(true);

        if (currentUser.getLoaiNv() == 0) {
            TreeItem staffMenu = new TreeItem("Quản lý nhân viên");
            staffMenu.getChildren().add(new TreeItem("Tìm kiếm nhân viên"));
            staffMenu.getChildren().add(new TreeItem("Thống kê nhân viên"));
            rootItem.getChildren().add(staffMenu);
            staffMenu.setExpanded(true);
        }

        sideMenu.setRoot(rootItem);

        sideMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            Node node = event.getPickResult().getIntersectedNode();

            if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
                String option = (String) ((TreeItem)sideMenu.getSelectionModel().getSelectedItem()).getValue();
                renderMainScene(option);
            }
        });

        // TOP MENU BAR

        // FILE MENU
        exitMenu.setOnAction(event-> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Thoát chương trình");
            alert.setHeaderText("Bạn chắc chắn muốn thoát?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK)
                System.exit(0);
        });

        logOutMenu.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/basic/login.fxml"));
                Scene firstScene = new Scene(root, 1000, 600);
                firstScene.getStylesheets().add(getClass().getResource("/resources/css/style.css").toExternalForm());
                new JMetro(root, Style.DARK);
                Stage stage = new Stage();
                stage.setTitle("Đăng nhập - QLTV");
                stage.getIcons().add(new Image("/resources/icon/app-icon.png"));
                stage.setScene(firstScene);
                stage.setResizable(false);
                stage.setScene(firstScene);
                window.getScene().getWindow().hide();
                stage.show();
            } catch (IOException e) {
                ExHandler.handle(e);
                System.exit(0);
            }
        });

        // EDIT MENU

        editMenu.setDisable(true);
        deleteMenu.setDisable(true);
        addMenu.setDisable(true);

        exportMenu.setDisable(true);
        importMenu.setDisable(true);

        // FORM MENU

//        formBookMenu.setOnAction(event -> {
//            String[] headers = {"MaSach", "TenSach", "Gia", "MaTheLoai", "TacGia", "MaNXB", "NamXB", "MaNN", "ViTri", "SL"};
//            saveForm(headers, "Form Nhap Sach");
//        });
//
//        formMetaMenu.setOnAction(event -> {
//            String[] headers = {"MaTheLoai/NXB/NN", "TenTheLoai/NXB/NN"};
//            saveForm(headers, "Form Thong Tin Phu");
//        });
//
//        formReaderMenu.setOnAction(event -> {
//            String[] headers = {"HoTen", "NgaySinh", "GioiTinh", "CMTND", "DiaChi", "DuocMuon"};
//            saveForm(headers, "Form Nhap Doc Gia");
//        });
//
//        formStaffMenu.setOnAction(event -> {
//            String[] headers = {"LaQuanLy", "TenDangNhap", "MatKhau", "HoTen", "NgaySinh", "GioiTinh", "CMTND", "DiaChi"};
//            saveForm(headers, "Form Nhap Nhan Vien");
//        });

        // ABOUT MENU
        aboutMenu.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Về ứng dụng");
            alert.setHeaderText("PHẦN MỀM QUẢN LÝ THƯ VIỆN");
            alert.setContentText("Trần Trung Nghĩa - 20173281");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        });
    }

    public void renderMainScene(String option) {
        FXMLLoader loader = new FXMLLoader();
        switch (option) {
            case "Tìm kiếm giao dịch":
                try {
                    loader.setLocation(getClass().getClassLoader().getResource("view/transaction/searchtransaction.fxml"));
                    window.setCenter(loader.load());
                    SearchTransactionController searchTransactionController = loader.getController();
                    searchTransactionController.init(this);
                } catch (Exception e) {
                    ExHandler.handle(e);
                }
                break;
        }
    }

    public void saveForm(String[] headers, String name) {

        XSSFWorkbook excelWorkBook = new XSSFWorkbook();
        XSSFSheet sheet = excelWorkBook.createSheet();

        XSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            row.createCell(i).setCellValue(headers[i]);
        }

//             Ghi file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn vị trí lưu.");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName(name);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedFile = fileChooser.showSaveDialog(window.getScene().getWindow());

        try {
            FileOutputStream output = new FileOutputStream(selectedFile);
            excelWorkBook.write(output);
            output.close();
            Desktop.getDesktop().open(selectedFile);
        } catch (IOException e) {
            ExHandler.handle(e);
        }
    }
}


