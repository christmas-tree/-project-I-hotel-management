package controller.khachHang;

import controller.basic.IndexController;
import dao.KhachHangDAO;
import javafx.animation.*;
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
import model.KhachHang;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;
import util.ExHandler;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;

public class TimKhachHang {

    @FXML
    private TableView<KhachHang> khachHangTable;

    @FXML
    private TableColumn<KhachHang, String> idCol;

    @FXML
    private TableColumn<KhachHang, String> tenCol;

    @FXML
    private TableColumn<KhachHang, String> gioiTinhCol;

    @FXML
    private TableColumn<KhachHang, String> cmndCol;

    @FXML
    private TableColumn<KhachHang, String> dtCol;

    @FXML
    private TableColumn<KhachHang, String> emailCol;

    @FXML
    private TableColumn<KhachHang, String> diaChiCol;

    @FXML
    private TableColumn<KhachHang, String> ghiChuCol;

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

    private ObservableList<KhachHang> data;

    private int searchType = -1;

    public void init(IndexController c) {

        idCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%06d", p.getValue().getMaKh())));
        tenCol.setCellValueFactory(new PropertyValueFactory<>("tenKhach"));
        gioiTinhCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getGioiTinh() ? "Nam" : "Nữ"));
        cmndCol.setCellValueFactory(new PropertyValueFactory<>("cmnd"));
        dtCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%010d", p.getValue().getDienThoai())));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        diaChiCol.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        ghiChuCol.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

        idCol.setSortType(TableColumn.SortType.ASCENDING);

        reloadData();

        String searchChoices[] = {"Mã khách hàng", "Tên", "Giới tính", "CMND", "Điện thoại", "Email"};
        //                              0           1          2          3         4           5

        searchChoiceBox.getItems().addAll(searchChoices);

        searchChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, numPre, numPost) -> {
            dieuKienLabel.setVisible(true);

            searchInputField.setVisible(false);
            searchBtn.setVisible(false);
            searchConditionChoiceBox.setVisible(false);
            searchStartDate.setVisible(false);
            searchEndDate.setVisible(false);

            searchType = numPost.intValue();
            switch (searchType) {
                case 0:
                case 1:
                case 3:
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
            }
        });

        khachHangTable.setRowFactory(tv -> {
            TableRow<KhachHang> row = new TableRow<>();
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
        data = FXCollections.observableArrayList(KhachHangDAO.getInstance().getAll());
        khachHangTable.setItems(data);
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
//        String searchChoices[] = {"Mã khách hàng", "Tên", "Giới tính", "CMND", "Điện thoại", "Email"};
        //                              0           1          2          3         4           5
        Runnable searchTask = () -> {
            switch (searchType) {
                case 0:
                case 1:
                case 3:
                case 4:
                case 5:
                    System.out.println("Searching case " + searchType);
                    System.out.println(searchInputField.getText());
                    data = FXCollections.observableArrayList(KhachHangDAO.getInstance().search(searchType, searchInputField.getText()));
                    break;
                case 2:
                    System.out.println("Searching case " + searchType);
                    String value = searchConditionChoiceBox.getSelectionModel().getSelectedItem();
                    data = FXCollections.observableArrayList(KhachHangDAO.getInstance().search(searchType, value));
                    break;
            }
            Platform.runLater(() -> {
                khachHangTable.setItems(data);
            });
        };

        new Thread(searchTask).start();
    }

    public void add() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/khachHang/suaKhachHang.fxml"));
            Parent editRoot = loader.load();
            new JMetro(editRoot, Style.LIGHT);

            Stage stage = new Stage();
            Scene scene = new Scene(editRoot, 600, 500);

            stage.setTitle("Thêm khách hàng");
            stage.setScene(scene);
            stage.setResizable(false);

            SuaKhachHang suaKhachHang = loader.getController();
            suaKhachHang.init();

            stage.showAndWait();
            reloadData();
        } catch (IOException e) {
            ExHandler.handle(e);
        }

    }

    public void delete() {
        KhachHang focusedKhachHang = khachHangTable.getSelectionModel().getSelectedItem();

        if (focusedKhachHang == null) {
            ExHandler.handle(new RuntimeException("Bạn chưa chọn khách hàng nào."));
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xoá");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xoá?");
        confirmAlert.setContentText("Bạn đang thực hiện xoá Khách hàng ID " + focusedKhachHang.getMaKh() + " - " + focusedKhachHang.getTenKhach() + ".");
        confirmAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> choice = confirmAlert.showAndWait();

        if (choice.get() == ButtonType.OK) {
            if (KhachHangDAO.getInstance().delete(focusedKhachHang)) {
                Alert resultAlert = new Alert(Alert.AlertType.INFORMATION);
                resultAlert.setTitle("Kết quả xoá");
                resultAlert.setHeaderText("Xoá thành công!");
                resultAlert.setContentText("Đã xoá khách hàng ID " + focusedKhachHang.getMaKh() + " - " + focusedKhachHang.getTenKhach() + " thành công.");
                resultAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                resultAlert.showAndWait();
                reloadData();
            } else {
                Alert resultAlert = new Alert(Alert.AlertType.ERROR);
                resultAlert.setTitle("Kết quả xoá");
                resultAlert.setHeaderText("Xoá thất bại!");
                resultAlert.setContentText("Khách hàng ID " + focusedKhachHang.getMaKh() + " - " + focusedKhachHang.getTenKhach() + " chưa xoá khỏi CSDL.");
                resultAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                resultAlert.showAndWait();
            }
        }
    }

    public void edit() {
        KhachHang focusedKhachHang = khachHangTable.getSelectionModel().getSelectedItem();

        if (focusedKhachHang == null) {
            ExHandler.handle(new RuntimeException("Bạn chưa chọn khách hàng nào."));
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/khachHang/suaKhachHang.fxml"));
        try {
            Parent editRoot = loader.load();
            new JMetro(editRoot, Style.LIGHT);

            Stage stage = new Stage();
            Scene scene = new Scene(editRoot, 600, 500);
            stage.setTitle("Sửa khách hàng");
            stage.setScene(scene);
            stage.setResizable(false);
            SuaKhachHang suaKhachHang = loader.getController();
            suaKhachHang.init(focusedKhachHang);

            stage.showAndWait();
        } catch (IOException e) {
            ExHandler.handle(e);
        }

        reloadData();
    }

    public void export() {
        if (data.size() == 0) {
            ExHandler.handle(new Exception("Không tìm thấy dữ liệu phù hợp với lựa chọn tìm kiếm."));
        } else {
            File file = new File("src/resources/form/DsDocGia.xlsx");

            XSSFWorkbook workbook;

            try {
                FileInputStream inputStream = new FileInputStream(file);
                workbook = new XSSFWorkbook(inputStream);
                inputStream.close();
            } catch (IOException e) {
                ExHandler.handle(e);
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

            XSSFCellStyle searchInfoStyle = workbook.createCellStyle();
            searchInfoStyle.setFont(workbook.createFont());
            searchInfoStyle.getFont().setFontHeightInPoints((short) 9);
            searchInfoStyle.setAlignment(HorizontalAlignment.CENTER);
            searchInfoStyle.getFont().setFontName("Arial");

            XSSFCell cell = null;

            KhachHang khachHang;

            // DATE
            cell = sheet.getRow(4).getCell(0);
            cell.setCellValue(LocalDate.now().format(DateTimeFormatter.ofPattern("'Ngày 'dd' tháng 'MM' năm 'yyyy")));
            cell.setCellStyle(dateStyle);

            // SEARCH TYPE
            if (searchType != -1) {
                String searchChoices[] = {"Mã khách hàng", "Tên", "Giới tính", "CMND", "Điện thoại", "Email"};
                //                              0           1          2          3         4           5
                String searchInfoString = searchChoices[searchType];

                switch (searchType) {
                    case 0:
                    case 3:
                    case 4:
                    case 5:
                        searchInfoString += ": " + searchInputField.getText();
                        break;
                    case 1:
                        searchInfoString += " có chữ: " + searchInputField.getText();
                        break;
                    case 2:
                        searchInfoString += ": " + searchConditionChoiceBox.getSelectionModel().getSelectedItem();
                        break;
                }

                cell = sheet.getRow(3).getCell(0);
                cell.setCellStyle(searchInfoStyle);
                cell.setCellValue(searchInfoString);
            }

            // DATA
            for (int i = 0; i < data.size(); i++) {
                khachHang = data.get(i);
                int row = 7 + i;
                sheet.createRow(row);

                cell = sheet.getRow(row).createCell(0);
                cell.setCellValue(i + 1);
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(1);
                cell.setCellValue(String.format("%06d", khachHang.getMaKh()));
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(2);
                cell.setCellValue(khachHang.getTenKhach());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(3);
                cell.setCellValue(khachHang.getGioiTinh() ? "Nam" : "Nữ");
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(4);
                cell.setCellValue(khachHang.getCmnd());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(5);
                cell.setCellValue(khachHang.getDienThoai());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(6);
                cell.setCellValue(khachHang.getEmail());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(7);
                cell.setCellValue(khachHang.getDiaChi());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(8);
                cell.setCellValue(khachHang.getGhiChu());
                cell.setCellStyle(tableElementStyle);
            }

            // Ghi file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn vị trí lưu.");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );
            fileChooser.setInitialFileName("Thong Tin Khach Hang " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            File selectedFile = fileChooser.showSaveDialog(khachHangTable.getScene().getWindow());

            try {
                FileOutputStream output = new FileOutputStream(selectedFile);
                workbook.write(output);
                output.close();
                Desktop.getDesktop().open(selectedFile);
            } catch (IOException e) {
                ExHandler.handle(e);
            }
        }
    }

    public void importData() {

        ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
        KhachHang newKhachHang;

        XSSFWorkbook excelWorkBook;

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn file.");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            File selectedFile = fileChooser.showOpenDialog(khachHangTable.getScene().getWindow());


            FileInputStream inputStream = new FileInputStream(selectedFile);
            excelWorkBook = new XSSFWorkbook(inputStream);
            inputStream.close();
        } catch (IOException e) {
            ExHandler.handle(e);
            return;
        }

        XSSFSheet sheet = excelWorkBook.getSheetAt(0);

        // DATA
        Iterator rows = sheet.rowIterator();
        XSSFRow row = (XSSFRow) rows.next();
        if (row.getLastCellNum() >= 6) {
//            {"Họ tên", "Giới tính", "CMND", "Điện thoại", "Email", "Địa chỉ", "Ghi chú"}

            while (rows.hasNext()) {
                row = (XSSFRow) rows.next();
                newKhachHang = new KhachHang();

                newKhachHang.setTenKhach(row.getCell(0, CREATE_NULL_AS_BLANK).getStringCellValue());
                newKhachHang.setGioiTinh(row.getCell(1, CREATE_NULL_AS_BLANK).getBooleanCellValue());
                newKhachHang.setCmnd((long) row.getCell(2, CREATE_NULL_AS_BLANK).getNumericCellValue());
                newKhachHang.setDienThoai((long) row.getCell(3, CREATE_NULL_AS_BLANK).getNumericCellValue());
                newKhachHang.setEmail(row.getCell(4, CREATE_NULL_AS_BLANK).getStringCellValue());
                newKhachHang.setDiaChi(row.getCell(5, CREATE_NULL_AS_BLANK).getStringCellValue());
                newKhachHang.setGhiChu(row.getCell(6, CREATE_NULL_AS_BLANK).getStringCellValue());

                dsKhachHang.add(newKhachHang);
            }
        } else
            ExHandler.handle(new Exception("File không đúng định dạng." + row.getLastCellNum()));

        KhachHangDAO.getInstance().importKhachHang(dsKhachHang);
        refresh();
    }
}