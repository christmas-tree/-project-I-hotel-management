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
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.GiaPhongTroi;
import model.LoaiPhong;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;
import util.AlertGenerator;
import util.ExceptionHandler;
import util.Reporter;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;


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

    public void export() {
        if (data.size() == 0) {
            ExceptionHandler.handle(new Exception("Không tìm thấy dữ liệu phù hợp với lựa chọn tìm kiếm."));
        } else {
            XSSFWorkbook workbook = Reporter.getWorkbook("DsGiaPhong.xlsx");
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

            GiaPhongTroi giaPhongTroi;

            // DATE
            cell = sheet.getRow(3).getCell(1);
            cell.setCellValue(LocalDate.now().format(DateTimeFormatter.ofPattern("'Ngày 'dd' tháng 'MM' năm 'yyyy")));
            cell.setCellStyle(dateStyle);

            // SEARCH TYPE
            if (!searchConditionChoiceBox.getSelectionModel().isEmpty()) {
                String searchInfoString = "Loại phòng: " + searchConditionChoiceBox.getSelectionModel().getSelectedItem().getLoaiPhong();

                cell = sheet.getRow(5).getCell(0);
                cell.setCellStyle(searchInfoStyle);
                cell.setCellValue(searchInfoString);
            }

            // DATA
            for (int i = 0; i < data.size(); i++) {
                giaPhongTroi = data.get(i);
                int row = 7 + i;
                sheet.createRow(row);

                cell = sheet.getRow(row).createCell(0);
                cell.setCellValue(i + 1);
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(1);
                cell.setCellValue(giaPhongTroi.getLoaiPhong().getLoaiPhong());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(2);
                cell.setCellValue(giaPhongTroi.getTen());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(3);
                cell.setCellValue(giaPhongTroi.getNgayBatDau());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(4);
                cell.setCellValue(giaPhongTroi.getNgayKetThuc());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(5);
                cell.setCellValue(giaPhongTroi.getLapLaiString());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(6);
                cell.setCellValue(String.format("%,3d", giaPhongTroi.getGiaTien()));
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(7);
                cell.setCellValue(giaPhongTroi.getGhiChu());
                cell.setCellStyle(tableElementStyle);
            }

            // Ghi file
            Reporter.saveWorkbook(workbook, bangTable);
        }
    }

    public void importData() {

        ArrayList<GiaPhongTroi> dsGiaPhong = new ArrayList<>();
        GiaPhongTroi giaPhong;
        XSSFWorkbook excelWorkBook;

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn file.");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File selectedFile = fileChooser.showOpenDialog(bangTable.getScene().getWindow());
            FileInputStream inputStream = new FileInputStream(selectedFile);
            excelWorkBook = new XSSFWorkbook(inputStream);
            inputStream.close();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
            return;
        }
        XSSFSheet sheet = excelWorkBook.getSheetAt(0);

        // DATA
        Iterator rows = sheet.rowIterator();
        XSSFRow row = (XSSFRow) rows.next();
        if (row.getLastCellNum() >= 8) {
            while (rows.hasNext()) {
                row = (XSSFRow) rows.next();
                giaPhong = new GiaPhongTroi();

//        {"Mã loại phòng", "Diễn gỉải", "Ngày bắt đầu", "Ngày kết thúc", "Lặp lại", "Giá tiền", "Ghi chú"}
                giaPhong.setMaLoaiPhong(row.getCell(0, CREATE_NULL_AS_BLANK).getStringCellValue());
                giaPhong.setTen(row.getCell(1, CREATE_NULL_AS_BLANK).getStringCellValue());
                giaPhong.setNgayBatDau(new Date(row.getCell(2, CREATE_NULL_AS_BLANK).getDateCellValue().getTime()));
                giaPhong.setNgayKetThuc(new Date(row.getCell(3, CREATE_NULL_AS_BLANK).getDateCellValue().getTime()));
                giaPhong.setLapLai((int) row.getCell(4, CREATE_NULL_AS_BLANK).getNumericCellValue());
                giaPhong.setGiaTien((long) row.getCell(5, CREATE_NULL_AS_BLANK).getNumericCellValue());
                giaPhong.setGhiChu(row.getCell(6, CREATE_NULL_AS_BLANK).getStringCellValue());

                dsGiaPhong.add(giaPhong);
            }
        } else
            ExceptionHandler.handle(new Exception("File không đúng định dạng." + row.getLastCellNum()));

        GiaPhongTroiDAO.getInstance().importData(dsGiaPhong);
        refresh();
    }
}
