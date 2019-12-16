package controller.phong;

import controller.basic.IndexController;
import dao.LoaiPhongDAO;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.LoaiPhong;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.ExHandler;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class QLLoaiPhong {

    @FXML
    private TableView<LoaiPhong> loaiPhongTable;

    @FXML
    private TableColumn<LoaiPhong, String> idCol;

    @FXML
    private TableColumn<LoaiPhong, String> loaiPhongCol;

    @FXML
    private TableColumn<LoaiPhong, String> giaTienCol;

    @FXML
    private TableColumn<LoaiPhong, String> soNguoiCol;

    @FXML
    private TableColumn<LoaiPhong, String> ghiChuCol;

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

    private ObservableList<LoaiPhong> data;

    public void init(IndexController c) {

        idCol.setCellValueFactory(new PropertyValueFactory<>("maLoaiPhong"));
        loaiPhongCol.setCellValueFactory(new PropertyValueFactory<>("loaiPhong"));
        giaTienCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getGiaTien())));
        soNguoiCol.setCellValueFactory(new PropertyValueFactory<>("soNguoi"));
        ghiChuCol.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

        reloadData();

        loaiPhongTable.setRowFactory(tv -> {
            TableRow<LoaiPhong> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()))
                    edit();
            });
            return row;
        });

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
        data = LoaiPhongDAO.getInstance().getAll();
        loaiPhongTable.setItems(data);
    }

    public void refresh() {
        reloadData();

        RotateTransition rt = new RotateTransition(Duration.millis(750), refreshIcon);
        rt.setByAngle(360 * 3);
        rt.setCycleCount(1);
        rt.setInterpolator(Interpolator.EASE_BOTH);
        rt.play();
    }

    public void add() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/phong/suaLoaiPhong.fxml"));
            Parent editRoot = loader.load();
            new JMetro(editRoot, Style.LIGHT);

            Stage stage = new Stage();
            Scene scene = new Scene(editRoot);

            stage.setTitle("Thêm loại phòng");
            stage.setScene(scene);
            stage.setResizable(false);

            SuaLoaiPhong suaLoaiPhong = loader.getController();
            suaLoaiPhong.init();

            stage.showAndWait();
            reloadData();
        } catch (IOException e) {
            ExHandler.handle(e);
        }

    }

    public void delete() {
        LoaiPhong focusedLoaiPhong = loaiPhongTable.getSelectionModel().getSelectedItem();

        if (focusedLoaiPhong == null) {
            ExHandler.handle(new RuntimeException("Bạn chưa chọn loại phòng nào."));
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xoá");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xoá?");
        confirmAlert.setContentText("Bạn đang thực hiện xoá Loại phòng " + focusedLoaiPhong.getLoaiPhong() + ".");
        confirmAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> choice = confirmAlert.showAndWait();

        if (choice.get() == ButtonType.OK) {
            if (LoaiPhongDAO.getInstance().delete(focusedLoaiPhong)) {
                Alert resultAlert = new Alert(Alert.AlertType.INFORMATION);
                resultAlert.setTitle("Kết quả xoá");
                resultAlert.setHeaderText("Xoá thành công!");
                resultAlert.setContentText("Đã xoá loại phòng " + focusedLoaiPhong.getLoaiPhong() + " thành công.");
                resultAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                resultAlert.showAndWait();
                reloadData();
            } else {
                Alert resultAlert = new Alert(Alert.AlertType.ERROR);
                resultAlert.setTitle("Kết quả xoá");
                resultAlert.setHeaderText("Xoá thất bại!");
                resultAlert.setContentText("Loại phòng " + focusedLoaiPhong.getLoaiPhong() + " chưa xoá khỏi CSDL.");
                resultAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                resultAlert.showAndWait();
            }
        }
    }

    public void edit() {
        LoaiPhong focusedLoaiPhong = loaiPhongTable.getSelectionModel().getSelectedItem();

        if (focusedLoaiPhong == null) {
            ExHandler.handle(new RuntimeException("Bạn chưa chọn loại phòng nào."));
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/phong/suaLoaiPhong.fxml"));
        try {
            Parent editRoot = loader.load();
            new JMetro(editRoot, Style.LIGHT);

            Stage stage = new Stage();
            Scene scene = new Scene(editRoot);
            stage.setTitle("Sửa loại phòng");
            stage.setScene(scene);
            stage.setResizable(false);
            SuaLoaiPhong suaLoaiPhong = loader.getController();
            suaLoaiPhong.init(focusedLoaiPhong);

            stage.showAndWait();
        } catch (IOException e) {
            ExHandler.handle(e);
        }

        reloadData();
    }

    public void export() {
        if (data.size() == 0) {
            ExHandler.handle(new Exception("Không tìm thấy dữ liệu."));
        } else {
            File file = new File("src/resources/form/DsLoaiPhong.xlsx");

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

            LoaiPhong loaiPhong;

            // DATE
            cell = sheet.getRow(3).getCell(0);
            cell.setCellValue(LocalDate.now().format(DateTimeFormatter.ofPattern("'Ngày 'dd' tháng 'MM' năm 'yyyy")));
            cell.setCellStyle(dateStyle);

            // DATA
            for (int i = 0; i < data.size(); i++) {
                loaiPhong = data.get(i);
                int row = 6 + i;
                sheet.createRow(row);

                cell = sheet.getRow(row).createCell(0);
                cell.setCellValue(i + 1);
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(1);
                cell.setCellValue(loaiPhong.getMaLoaiPhong());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(2);
                cell.setCellValue(loaiPhong.getLoaiPhong());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(3);
                cell.setCellValue(loaiPhong.getGiaTien());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(4);
                cell.setCellValue(loaiPhong.getSoNguoi());
                cell.setCellStyle(tableElementStyle);

                cell = sheet.getRow(row).createCell(5);
                cell.setCellValue(loaiPhong.getGhiChu());
                cell.setCellStyle(tableElementStyle);
            }

            // Ghi file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn vị trí lưu.");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );
            fileChooser.setInitialFileName("Thong Tin Loai Phong " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            File selectedFile = fileChooser.showSaveDialog(loaiPhongTable.getScene().getWindow());

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
//
//        ArrayList<LoaiPhong> dsLoaiPhong = new ArrayList<>();
//    LoaiPhong newLoaiPhong;
//
//    XSSFWorkbook excelWorkBook;
//
//        try {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Chọn file.");
//        fileChooser.getExtensionFilters().add(
//                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
//        );
//        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
//
//        File selectedFile = fileChooser.showOpenDialog(loaiPhongTable.getScene().getWindow());
//
//
//        FileInputStream inputStream = new FileInputStream(selectedFile);
//        excelWorkBook = new XSSFWorkbook(inputStream);
//        inputStream.close();
//    } catch (IOException e) {
//        ExHandler.handle(e);
//        return;
//    }
//
//    XSSFSheet sheet = excelWorkBook.getSheetAt(0);
//
//    // DATA
//    Iterator rows = sheet.rowIterator();
//    XSSFRow row = (XSSFRow) rows.next();
//        if (row.getLastCellNum() >= 6) {
////            {"Họ tên", "Giới tính", "CMND", "Điện thoại", "Email", "Địa chỉ", "Ghi chú"}
//
//        while (rows.hasNext()) {
//            row = (XSSFRow) rows.next();
//            newLoaiPhong = new LoaiPhong();
//
//            newLoaiPhong.setTenKhach(row.getCell(0, CREATE_NULL_AS_BLANK).getStringCellValue());
//            newLoaiPhong.setGioiTinh(row.getCell(1, CREATE_NULL_AS_BLANK).getBooleanCellValue());
//            newLoaiPhong.setCmnd((long) row.getCell(2, CREATE_NULL_AS_BLANK).getNumericCellValue());
//            newLoaiPhong.setDienThoai((long) row.getCell(3, CREATE_NULL_AS_BLANK).getNumericCellValue());
//            newLoaiPhong.setEmail(row.getCell(4, CREATE_NULL_AS_BLANK).getStringCellValue());
//            newLoaiPhong.setDiaChi(row.getCell(5, CREATE_NULL_AS_BLANK).getStringCellValue());
//            newLoaiPhong.setGhiChu(row.getCell(6, CREATE_NULL_AS_BLANK).getStringCellValue());
//
//            dsLoaiPhong.add(newLoaiPhong);
//        }
//    } else
//            ExHandler.handle(new Exception("File không đúng định dạng." + row.getLastCellNum()));
//
//        LoaiPhongDAO.getInstance().importLoaiPhong(dsLoaiPhong);
//    refresh();
    }
}