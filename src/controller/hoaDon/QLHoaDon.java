package controller.hoaDon;

import controller.basic.KhungUngDung;
import dao.DatPhongDAO;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.DatPhong;
import model.NhanVien;
import util.AlertGenerator;
import util.ExceptionHandler;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;

public class QLHoaDon {
    @FXML
    private TableView<DatPhong> hoaDonTable;

    @FXML
    private ChoiceBox<String> searchChoiceBox;

    @FXML
    private Label dieuKienLabel;

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
    private Button xemBtn;

    @FXML
    private ImageView refreshIcon;

    private ObservableList<DatPhong> data;

    private int searchType = -1;

    private NhanVien nhanVien;

    public void init(KhungUngDung c) {

        this.nhanVien = c.currentUser;

        TableColumn<DatPhong, String> maDPCol = new TableColumn<>("Mã đặt phòng");
        TableColumn<DatPhong, String> khachDoanCol = new TableColumn<>("Loại đặt phòng");
        TableColumn<DatPhong, String> tenKhachCol = new TableColumn<>("Khách đặt");
        TableColumn<DatPhong, Long> cmndCol = new TableColumn<>("CMND/CCCD");
        TableColumn<DatPhong, String> dienThoaiCol = new TableColumn<>("Điện thoại");
        TableColumn<DatPhong, Timestamp> ngayDenCol = new TableColumn<>("Ngày đến");
        TableColumn<DatPhong, Timestamp> ngayDiCol = new TableColumn<>("Ngày đi");
        TableColumn<DatPhong, String> tongThanhToanCol = new TableColumn<>("Tổng thanh toán");

        maDPCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%04d", p.getValue().getMaDatPhong())));
        khachDoanCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().isKhachDoan() ? "Khách đoàn" : "Khách lẻ"));
        tenKhachCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getKhachHang().getTenKhach()));
        cmndCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getKhachHang().getCmnd()));
        dienThoaiCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%010d", p.getValue().getKhachHang().getDienThoai())));
        ngayDiCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getNgayCheckinTt()));
        ngayDenCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getNgayCheckoutTt()));
        tongThanhToanCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getThanhTien())));


        hoaDonTable.getColumns().addAll(maDPCol, tenKhachCol, cmndCol, dienThoaiCol, ngayDenCol, ngayDiCol, tongThanhToanCol);

        reloadData();

        String searchChoices[] = {"Mã đặt phòng", "Tên khách", "CMND/CCCD", "Điện thoại", "Ngày đến", "Ngày đi"};
        //                              0             1              2             3         4            5

        searchChoiceBox.getItems().addAll(searchChoices);

        searchChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, numPre, numPost) -> {
            searchType = numPost.intValue();

            dieuKienLabel.setVisible(true);
            searchInputField.setVisible(false);
            searchStartDate.setVisible(false);
            searchEndDate.setVisible(false);
            searchBtn.setVisible(false);

            switch (searchType) {
                case 0:
                case 1:
                case 2:
                case 3:
                    searchInputField.setVisible(true);
                    AnchorPane.clearConstraints(searchBtn);
                    AnchorPane.setLeftAnchor(searchBtn, 600.0);
                    AnchorPane.setTopAnchor(searchBtn, 55.0);
                    searchBtn.setVisible(true);
                    break;
                case 4:
                case 5:
                    searchStartDate.setVisible(true);
                    searchEndDate.setVisible(true);
                    AnchorPane.clearConstraints(searchBtn);
                    AnchorPane.setLeftAnchor(searchBtn, 600.0);
                    AnchorPane.setTopAnchor(searchBtn, 15.0);
                    searchBtn.setVisible(true);
                    break;
            }
        });

        hoaDonTable.setRowFactory(tv -> {
            TableRow<DatPhong> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()))
                    show();
            });
            return row;
        });

        searchBtn.setOnAction(event -> search());

        xemBtn.setOnAction(event -> show());

        refreshBtn.setOnAction(event -> refresh());

        c.addMenu.setDisable(false);
        c.deleteMenu.setDisable(false);
        c.editMenu.setDisable(false);
        c.exportMenu.setDisable(false);
        c.importMenu.setDisable(false);

        c.editMenu.setOnAction(event -> xemBtn.fire());
//        c.exportMenu.setOnAction(event -> export());
    }

    public void reloadData() {
        data = DatPhongDAO.getInstance().getAllReceipts();
        hoaDonTable.setItems(data);
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
        switch (searchType) {
//                    String searchChoices[] = {"Mã đặt phòng", "Tên khách", "CMND/CCCD", "Điện thoại", "Ngày đến", "Ngày đi"};
            //                              0             1              2             3         4            5
            case 0:
            case 1:
            case 2:
            case 3:
                data = FXCollections.observableArrayList(DatPhongDAO.getInstance().searchReceipts(searchType, searchInputField.getText()));
                break;
            case 4:
            case 5:
                LocalDate date1 = searchStartDate.getValue();
                LocalDate date2 = searchEndDate.getValue();
                data = FXCollections.observableArrayList(DatPhongDAO.getInstance().searchReceipts(searchType, date1, date2));
                break;
        }
        Platform.runLater(() -> {
            hoaDonTable.setItems(data);
        });
    }

    public void show() {
        DatPhong focusedDatPhong = hoaDonTable.getSelectionModel().getSelectedItem();

        if (focusedDatPhong == null) {
            AlertGenerator.error("Bạn chưa chọn hoá đơn nào.");
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/thongKe/xemHoaDon.fxml"));
        try {
            Parent editRoot = loader.load();
            new JMetro(editRoot, Style.LIGHT);

            Stage stage = new Stage();
            Scene scene = new Scene(editRoot);
            stage.setScene(scene);
            stage.setResizable(false);
            XemHoaDon xemHoaDon = loader.getController();
            xemHoaDon.init(focusedDatPhong, nhanVien);
            stage.showAndWait();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }

        reloadData();
    }
//
//    public void export() {
//        if (data.size() == 0) {
//            ExHandler.handle(new Exception("Không tìm thấy dữ liệu phù hợp với lựa chọn tìm kiếm."));
//        } else {
//            File file = new File("src/resources/form/DsDatPhong.xlsx");
//
//            XSSFWorkbook workbook;
//
//            try {
//                FileInputStream inputStream = new FileInputStream(file);
//                workbook = new XSSFWorkbook(inputStream);
//                inputStream.close();
//            } catch (IOException e) {
//                ExHandler.handle(e);
//                return;
//            }
//
//            XSSFSheet sheet = workbook.getSheetAt(0);
//
//            // CELL STYLES
//            XSSFCellStyle tableElementStyle = workbook.createCellStyle();
//            tableElementStyle.setBorderLeft(BorderStyle.THIN);
//            tableElementStyle.setBorderRight(BorderStyle.THIN);
//            tableElementStyle.setFont(workbook.createFont());
//            tableElementStyle.getFont().setFontHeightInPoints((short) 9);
//            tableElementStyle.getFont().setFontName("Arial");
//            tableElementStyle.setWrapText(true);
//
//            XSSFCellStyle dateStyle = workbook.createCellStyle();
//            dateStyle.setFont(workbook.createFont());
//            dateStyle.getFont().setFontHeightInPoints((short) 9);
//            dateStyle.getFont().setFontName("Arial");
//            dateStyle.setAlignment(HorizontalAlignment.CENTER);
//            dateStyle.getFont().setItalic(true);
//
//            XSSFCellStyle searchInfoStyle = workbook.createCellStyle();
//            searchInfoStyle.setFont(workbook.createFont());
//            searchInfoStyle.getFont().setFontHeightInPoints((short) 9);
//            searchInfoStyle.setAlignment(HorizontalAlignment.CENTER);
//            searchInfoStyle.getFont().setFontName("Arial");
//
//            XSSFCell cell = null;
//
//            DatPhong datPhong;
//
//            // DATE
//            cell = sheet.getRow(4).getCell(1);
//            cell.setCellValue(LocalDate.now().format(DateTimeFormatter.ofPattern("'Ngày 'dd' tháng 'MM' năm 'yyyy")));
//            cell.setCellStyle(dateStyle);
//
//            // SEARCH TYPE
//            if (searchType != -1) {
//                String searchChoices[] = {"Mã nhân viên", "Tên", "Giới tính", "Vị trí", "Tên đăng nhập", "Email"};
//                //                              0             1         2         3         4              5
//                String searchInfoString = searchChoices[searchType];
//
//                switch (searchType) {
//                    case 0:
//                    case 1:
//                    case 4:
//                    case 5:
//                        searchInfoString += ": " + searchInputField.getText();
//                        break;
//                    case 2:
//                    case 3:
//                        searchInfoString += ": " + searchConditionChoiceBox.getSelectionModel().getSelectedItem();
//                        break;
//                }
//
//                cell = sheet.getRow(5).getCell(0);
//                cell.setCellStyle(searchInfoStyle);
//                cell.setCellValue(searchInfoString);
//            }
//
//            // DATA
//            for (int i = 0; i < data.size(); i++) {
//                datPhong = data.get(i);
//                int row = 8 + i;
//                sheet.createRow(row);
//
//                cell = sheet.getRow(row).createCell(0);
//                cell.setCellValue(i + 1);
//                cell.setCellStyle(tableElementStyle);
//
//                cell = sheet.getRow(row).createCell(1);
//                cell.setCellValue(String.format("%06d", datPhong.getMaNv()));
//                cell.setCellStyle(tableElementStyle);
//
//                cell = sheet.getRow(row).createCell(2);
//                cell.setCellValue(datPhong.getTenNv());
//                cell.setCellStyle(tableElementStyle);
//
//                cell = sheet.getRow(row).createCell(3);
//                cell.setCellValue(datPhong.getGioiTinh() ? "Nam" : "Nữ");
//                cell.setCellStyle(tableElementStyle);
//
//                cell = sheet.getRow(row).createCell(4);
//                cell.setCellValue(datPhong.getLoaiNv()==0 ? "Quản lý" : "Lễ tân");
//                cell.setCellStyle(tableElementStyle);
//
//                cell = sheet.getRow(row).createCell(5);
//                cell.setCellValue(datPhong.getTenDangNhap());
//                cell.setCellStyle(tableElementStyle);
//
//                cell = sheet.getRow(row).createCell(6);
//                cell.setCellValue(datPhong.getCmnd());
//                cell.setCellStyle(tableElementStyle);
//
//                cell = sheet.getRow(row).createCell(7);
//                cell.setCellValue(datPhong.getDienThoai());
//                cell.setCellStyle(tableElementStyle);
//
//                cell = sheet.getRow(row).createCell(8);
//                cell.setCellValue(datPhong.getEmail());
//                cell.setCellStyle(tableElementStyle);
//
//                cell = sheet.getRow(row).createCell(9);
//                cell.setCellValue(datPhong.getDiaChi());
//                cell.setCellStyle(tableElementStyle);
//
//                cell = sheet.getRow(row).createCell(10);
//                cell.setCellValue(datPhong.getGhiChu());
//                cell.setCellStyle(tableElementStyle);
//            }
//
//            // Ghi file
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Chọn vị trí lưu.");
//            fileChooser.getExtensionFilters().add(
//                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
//            );
//            fileChooser.setInitialFileName("Thong Tin Nhan Vien " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
//            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
//
//            File selectedFile = fileChooser.showSaveDialog(hoaDonTable.getScene().getWindow());
//
//            try {
//                FileOutputStream output = new FileOutputStream(selectedFile);
//                workbook.write(output);
//                output.close();
//                Desktop.getDesktop().open(selectedFile);
//            } catch (IOException e) {
//                ExHandler.handle(e);
//            }
//        }
//    }
}
