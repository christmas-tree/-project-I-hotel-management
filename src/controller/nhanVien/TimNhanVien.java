/*
 * Copyright (c) 2019 Nghia Tran.
 * Project I - Library Management System
 */

package controller.nhanVien;

import controller.basic.KhungUngDung;
import dao.NhanVienDAO;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.NhanVien;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;
import util.ExceptionHandler;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TimNhanVien {

    @FXML
    private TableView<NhanVien> nhanVienTable;

    @FXML
    private ChoiceBox searchChoiceBox;

    @FXML
    private Label dieuKienLabel;

    @FXML
    private ChoiceBox<String> searchConditionChoiceBox;

    @FXML
    private DatePicker searchStartDate;

    @FXML
    private DatePicker searchEndDate;

    @FXML
    private TextField searchInputField;

    @FXML
    private Button searchBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button editBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private ImageView refreshIcon;

    private ObservableList<NhanVien> data;

    private int searchType = -1;

    public void init(KhungUngDung c) {

        TableColumn<NhanVien, String> idCol = new TableColumn<>("ID");
        TableColumn<NhanVien, String> tenCol = new TableColumn<>("Họ tên");
        TableColumn<NhanVien, String> tenDangNhapCol = new TableColumn<>("Tên đăng nhập");
        TableColumn<NhanVien, String> loaiNvCol = new TableColumn<>("Vị trí");
        TableColumn<NhanVien, String> gioiTinhCol = new TableColumn<>("Giới tính");
        TableColumn<NhanVien, Long> cmndCol = new TableColumn<>("CMND/CCCD");
        TableColumn<NhanVien, String> dienThoaiCol = new TableColumn<>("Điện thoại");


        idCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%06d", p.getValue().getMaNv())));
        tenCol.setCellValueFactory(new PropertyValueFactory<>("tenNv"));
        loaiNvCol.setCellValueFactory(p -> {
            if (p.getValue().getLoaiNv() == 0)
                return new ReadOnlyObjectWrapper<>("Quản lý");
            else
                return new ReadOnlyObjectWrapper<>("Lễ tân");
        });
        tenDangNhapCol.setCellValueFactory(new PropertyValueFactory<>("tenDangNhap"));
        gioiTinhCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<String>(p.getValue().getGioiTinh() ? "Nam" : "Nữ"));
        cmndCol.setCellValueFactory(new PropertyValueFactory<>("cmnd"));
        dienThoaiCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%010d", p.getValue().getDienThoai())));

        nhanVienTable.getColumns().addAll(idCol, tenCol, tenDangNhapCol, loaiNvCol, gioiTinhCol, cmndCol, dienThoaiCol);

        reloadData();

        String searchChoices[] = {"Mã nhân viên", "Tên", "Giới tính", "Vị trí", "Tên đăng nhập", "Email"};
        //                              0             1         2         3         4              5

        searchChoiceBox.getItems().addAll(searchChoices);

        searchChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, numPre, numPost) -> {
            searchType = numPost.intValue();

            dieuKienLabel.setVisible(true);
            searchInputField.setVisible(false);
            searchBtn.setVisible(false);
            searchConditionChoiceBox.setVisible(false);
            searchStartDate.setVisible(false);
            searchEndDate.setVisible(false);

            switch (searchType) {
                case 0:
                case 1:
                case 4:
                case 5:
                    searchInputField.setVisible(true);
                    AnchorPane.clearConstraints(searchBtn);
                    AnchorPane.setLeftAnchor(searchBtn, 620.0);
                    AnchorPane.setTopAnchor(searchBtn, 15.0);
                    searchBtn.setVisible(true);
                    break;
                case 2:
                    searchConditionChoiceBox.getItems().clear();
                    searchConditionChoiceBox.getItems().addAll("Nam", "Nữ");
                    searchConditionChoiceBox.setVisible(true);
                    AnchorPane.clearConstraints(searchBtn);
                    AnchorPane.setLeftAnchor(searchBtn, 520.0);
                    AnchorPane.setTopAnchor(searchBtn, 15.0);
                    searchBtn.setVisible(true);
                    break;
                case 3:
                    searchConditionChoiceBox.getItems().clear();
                    searchConditionChoiceBox.getItems().addAll("Quản lý", "Lễ tân");
                    searchConditionChoiceBox.setVisible(true);
                    AnchorPane.clearConstraints(searchBtn);
                    AnchorPane.setLeftAnchor(searchBtn, 520.0);
                    AnchorPane.setTopAnchor(searchBtn, 15.0);
                    searchBtn.setVisible(true);
                    break;
            }
        });

        nhanVienTable.setRowFactory(tv -> {
            TableRow<NhanVien> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()))
                    edit();
            });
            return row;
        });

        searchBtn.setOnAction(event -> search());

        editBtn.setOnAction(event -> edit());

        addBtn.setOnAction(event -> add());

        deleteBtn.setOnAction(event -> delete());

        refreshBtn.setOnAction(event -> refresh());

        c.addMenu.setDisable(false);
        c.deleteMenu.setDisable(false);
        c.editMenu.setDisable(false);
        c.exportMenu.setDisable(false);
//        c.backupMenu.setDisable(false);
        c.importMenu.setDisable(false);

        c.addMenu.setOnAction(event -> addBtn.fire());
        c.deleteMenu.setOnAction(event -> deleteBtn.fire());
        c.editMenu.setOnAction(event -> editBtn.fire());
        c.exportMenu.setOnAction(event -> export());
        c.importMenu.setOnAction(event -> restore());
    }

    public void reloadData() {
        data = FXCollections.observableArrayList(NhanVienDAO.getInstance().getAll());
        nhanVienTable.setItems(data);
    }

    public void refresh() {
        reloadData();

        searchChoiceBox.getSelectionModel().clearSelection();
        searchType = -1;

        RotateTransition rt = new RotateTransition(Duration.millis(750), refreshIcon);
        rt.setByAngle(360 * 3);
        rt.setCycleCount(1);
        rt.setInterpolator(Interpolator.EASE_BOTH);
        rt.play();
    }

    public void search() {

        Runnable searchTask = () -> {
            try {
                switch (searchType) {
//                    String searchChoices[] = {"Mã nhân viên", "Tên", "Giới tính", "Vị trí", "Tên đăng nhập", "Email"};
                    //                              0             1         2         3         4              5
                    case 0:
                    case 1:
                    case 4:
                    case 5:
                        data = FXCollections.observableArrayList(NhanVienDAO.getInstance().search(searchType, searchInputField.getText()));
                        break;
                    case 2:
                    case 3:
                        String value = searchConditionChoiceBox.getSelectionModel().getSelectedItem();
                        data = FXCollections.observableArrayList(NhanVienDAO.getInstance().search(searchType, value));
                        break;
                }
                Platform.runLater(() -> {
                    nhanVienTable.setItems(data);
                });
            } catch (SQLException e) {
                Platform.runLater(() -> ExceptionHandler.handle(e));
            }
        };
        new Thread(searchTask).start();
    }

    public void add() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/nhanVien/suaNhanVien.fxml"));
            Parent editRoot = loader.load();
            new JMetro(editRoot, Style.LIGHT);

            Stage stage = new Stage();
            Scene scene = new Scene(editRoot);

            stage.setTitle("Thêm nhân viên");
            stage.setScene(scene);
            stage.setResizable(false);

            SuaNhanVien suaNhanVien = loader.getController();
            suaNhanVien.init();

            stage.showAndWait();
            reloadData();

        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }

    }

    public void delete() {
        NhanVien focusedNhanVien = nhanVienTable.getSelectionModel().getSelectedItem();

        if (focusedNhanVien == null) {
            ExceptionHandler.handle(new RuntimeException("Bạn chưa chọn nhân viên nào."));
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xoá");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xoá?");
        confirmAlert.setContentText("Bạn đang thực hiện xoá Nhân viên ID " + focusedNhanVien.getMaNv() + " - " + focusedNhanVien.getTenNv() + ".");
        confirmAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> choice = confirmAlert.showAndWait();

        if (choice.get() == ButtonType.OK) {
            if (NhanVienDAO.getInstance().delete(focusedNhanVien)) {
                Alert resultAlert = new Alert(Alert.AlertType.INFORMATION);
                resultAlert.setTitle("Kết quả xoá");
                resultAlert.setHeaderText("Xoá thành công!");
                resultAlert.setContentText("Đã xoá nhân viên ID " + focusedNhanVien.getMaNv() + " - " + focusedNhanVien.getTenNv() + " thành công.");
                resultAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                resultAlert.showAndWait();
                reloadData();
            } else {
                Alert resultAlert = new Alert(Alert.AlertType.ERROR);
                resultAlert.setTitle("Kết quả xoá");
                resultAlert.setHeaderText("Xoá thất bại!");
                resultAlert.setContentText("Nhân viên ID " + focusedNhanVien.getMaNv() + " - " + focusedNhanVien.getTenNv() + " chưa xoá khỏi CSDL.");
                resultAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                resultAlert.showAndWait();
            }
        }
    }

    public void edit() {
        NhanVien focusedNhanVien = nhanVienTable.getSelectionModel().getSelectedItem();

        if (focusedNhanVien == null) {
            ExceptionHandler.handle(new RuntimeException("Bạn chưa chọn nhân viên nào."));
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/nhanVien/suaNhanVien.fxml"));
        try {
            Parent editRoot = loader.load();
            new JMetro(editRoot, Style.LIGHT);

            Stage stage = new Stage();
            Scene scene = new Scene(editRoot);
            stage.setTitle("Sửa nhân viên");
            stage.setScene(scene);
            stage.setResizable(false);
            SuaNhanVien suaNhanVien = loader.getController();
            suaNhanVien.init(focusedNhanVien);

            stage.showAndWait();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }

        nhanVienTable.refresh();
    }

    public void export() {
        if (data.size() == 0) {
            ExceptionHandler.handle(new Exception("Không tìm thấy dữ liệu phù hợp với lựa chọn tìm kiếm."));
        } else {
            File file = new File("src/resources/form/DsNhanVien.xlsx");

            XSSFWorkbook workbook;

            try {
                FileInputStream inputStream = new FileInputStream(file);
                workbook = new XSSFWorkbook(inputStream);
                inputStream.close();
            } catch (IOException e) {
                ExceptionHandler.handle(e);
                return;
            }

            XSSFSheet sheet = workbook.getSheetAt(0);

            // CELL STYLES
            XSSFCellStyle tableElementStyle = workbook.createCellStyle();
            tableElementStyle.setBorderLeft(BorderStyle.THIN);
            tableElementStyle.setBorderRight(BorderStyle.THIN);
            tableElementStyle.setFont(workbook.createFont());
            tableElementStyle.getFont().setFontHeightInPoints((short) 9);
            tableElementStyle.getFont().setFontName("Arial");
            tableElementStyle.setWrapText(true);

            XSSFCellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setFont(workbook.createFont());
            dateStyle.getFont().setFontHeightInPoints((short) 9);
            dateStyle.getFont().setFontName("Arial");
            dateStyle.setAlignment(HorizontalAlignment.CENTER);
            dateStyle.getFont().setItalic(true);

            XSSFCellStyle searchInfoStyle = workbook.createCellStyle();
            searchInfoStyle.setFont(workbook.createFont());
            searchInfoStyle.getFont().setFontHeightInPoints((short) 9);
            searchInfoStyle.setAlignment(HorizontalAlignment.CENTER);
            searchInfoStyle.getFont().setFontName("Arial");

            XSSFCell cell = null;

            NhanVien nhanVien;

            // DATE
            cell = sheet.getRow(4).getCell(1);
            cell.setCellValue(LocalDate.now().format(DateTimeFormatter.ofPattern("'Ngày 'dd' tháng 'MM' năm 'yyyy")));
            cell.setCellStyle(dateStyle);

            // SEARCH TYPE
            if (searchType != -1) {
                String searchChoices[] = {"Mã nhân viên", "Tên", "Giới tính", "Vị trí", "Tên đăng nhập", "Email"};
                //                              0             1         2         3         4              5
                String searchInfoString = searchChoices[searchType];

                switch (searchType) {
                    case 0:
                    case 1:
                    case 4:
                    case 5:
                        searchInfoString += ": " + searchInputField.getText();
                        break;
                    case 2:
                    case 3:
                        searchInfoString += ": " + searchConditionChoiceBox.getSelectionModel().getSelectedItem();
                        break;
                }

                cell = sheet.getRow(5).getCell(0);
                cell.setCellStyle(searchInfoStyle);
                cell.setCellValue(searchInfoString);
            }

            // DATA
            for (int i = 0; i < data.size(); i++) {
                nhanVien = data.get(i);
                int row = 8 + i;
                sheet.createRow(row);

                cell = sheet.getRow(row).createCell(0);
                cell.setCellValue(i + 1);
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(1);
                cell.setCellValue(String.format("%06d", nhanVien.getMaNv()));
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(2);
                cell.setCellValue(nhanVien.getTenNv());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(3);
                cell.setCellValue(nhanVien.getGioiTinh() ? "Nam" : "Nữ");
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(4);
                cell.setCellValue(nhanVien.getLoaiNv()==0 ? "Quản lý" : "Lễ tân");
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(5);
                cell.setCellValue(nhanVien.getTenDangNhap());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(6);
                cell.setCellValue(nhanVien.getCmnd());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(7);
                cell.setCellValue(nhanVien.getDienThoai());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(8);
                cell.setCellValue(nhanVien.getEmail());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(9);
                cell.setCellValue(nhanVien.getDiaChi());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(10);
                cell.setCellValue(nhanVien.getGhiChu());
                cell.setCellStyle(tableElementStyle);
            }

            // Ghi file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn vị trí lưu.");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );
            fileChooser.setInitialFileName("Thong Tin Nhan Vien " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            File selectedFile = fileChooser.showSaveDialog(nhanVienTable.getScene().getWindow());

            try {
                FileOutputStream output = new FileOutputStream(selectedFile);
                workbook.write(output);
                output.close();
                Desktop.getDesktop().open(selectedFile);
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
        }
    }

    public void restore() {
//
//        ArrayList<NhanVien> newNhanViens = new ArrayList<>();
//        NhanVien newNhanVien;
//
//        XSSFWorkbook excelWorkBook;
//
//        try {
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Chọn file.");
//            fileChooser.getExtensionFilters().add(
//                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
//            );
//            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
//
//            File selectedFile = fileChooser.showOpenDialog(nhanVienTable.getScene().getWindow());
//
//
//            FileInputStream inputStream = new FileInputStream(selectedFile);
//            excelWorkBook = new XSSFWorkbook(inputStream);
//            inputStream.close();
//        } catch (IOException e) {
//            ExHandler.handle(e);
//            return;
//        }
//
//        XSSFSheet sheet = excelWorkBook.getSheetAt(0);
//
//        // DATA
//        Iterator rows = sheet.rowIterator();
//        XSSFRow row = (XSSFRow) rows.next();
//        if (row.getLastCellNum() >= 8) {
//            while (rows.hasNext()) {
//                row = (XSSFRow) rows.next();
//                newNhanVien = new NhanVien();
//
//                newNhanVien.setAdmin(row.getCell(0, CREATE_NULL_AS_BLANK).getBooleanCellValue());
//                newNhanVien.setUsername(row.getCell(1, CREATE_NULL_AS_BLANK).getStringCellValue());
//                newNhanVien.setPassword(row.getCell(2, CREATE_NULL_AS_BLANK).getStringCellValue());
//                newNhanVien.setName(row.getCell(3, CREATE_NULL_AS_BLANK).getStringCellValue());
//                newNhanVien.setDob(new Date(row.getCell(4, CREATE_NULL_AS_BLANK).getDateCellValue().getTime()));
//                newNhanVien.setGender(row.getCell(5, CREATE_NULL_AS_BLANK).getBooleanCellValue());
//                newNhanVien.setIdCardNum((long) row.getCell(6, CREATE_NULL_AS_BLANK).getNumericCellValue());
//                newNhanVien.setAddress(row.getCell(7, CREATE_NULL_AS_BLANK).getStringCellValue());
//
//                newNhanViens.add(newNhanVien);
//            }
//        } else
//            ExHandler.handle(new Exception("File không đúng định dạng." + row.getLastCellNum()));
//
//        NhanVienDAO.getInstance().importNhanVien(newNhanViens);
//        refresh();
    }
}

