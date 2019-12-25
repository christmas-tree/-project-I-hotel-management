package util;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import model.DatPhong;
import model.HoaDon;
import model.NhanVien;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Exporter {
    public static void taoHoaDon(DatPhong datPhong, List<HoaDon> dsCtHoaDon, NhanVien nhanVien, Node node) {
        XWPFDocument doc;
        try {
            doc = new XWPFDocument(OPCPackage.open("src/resources/form/HoaDon.docx"));
        } catch (InvalidFormatException | IOException e) {
            ExceptionHandler.handle(e);
            return;
        }

        LocalDate now = LocalDate.now();

        // INFO TABLE
        XWPFTable title = doc.getTables().get(0);
        replaceCellText(title.getRow(1).getCell(1), "Ngày " + now.getDayOfMonth() + " tháng " + now.getMonthValue() + " năm " + now.getYear());
        replaceCellText(title.getRow(0).getCell(3), String.format("%04d", datPhong.getMaDatPhong()));

        XWPFTable basicInfoTable = doc.getTables().get(1);

        replaceCellText(basicInfoTable.getRow(2).getCell(1), datPhong.getKhachHang().getTenKhach());
        replaceCellText(basicInfoTable.getRow(3).getCell(1), datPhong.getKhachHang().getDienThoai() != null ? String.format("%010d", datPhong.getKhachHang().getDienThoai()) : "");
        replaceCellText(basicInfoTable.getRow(4).getCell(1), datPhong.getKhachHang().getDiaChi());

        // SIGNATURE TABLE
        XWPFTable signatureTable = doc.getTables().get(3);

        replaceCellText(signatureTable.getRow(2).getCell(0), datPhong.getKhachHang().getTenKhach());
        replaceCellText(signatureTable.getRow(2).getCell(1), nhanVien.getTenNv());

        // DETAILS TABLE
        XWPFTable detailsTable = doc.getTables().get(2);
        for (int i = 0; i < dsCtHoaDon.size(); i++) {
            HoaDon ctHoaDon = dsCtHoaDon.get(i);
            int j = i + 1;
            replaceCellText(detailsTable.getRow(j).getCell(0), String.valueOf(j));
            replaceCellText(detailsTable.getRow(j).getCell(1), ctHoaDon.getTenMuc());
            replaceCellText(detailsTable.getRow(j).getCell(2), ctHoaDon.getDonVi());
            replaceCellText(detailsTable.getRow(j).getCell(3), String.valueOf(ctHoaDon.getSoLuong()));
            replaceCellText(detailsTable.getRow(j).getCell(4), String.format("%,3d", ctHoaDon.getDonGia()));
            replaceCellText(detailsTable.getRow(j).getCell(5), String.format("%,3d", ctHoaDon.getThanhTien()));

            createNewRowWithFormat(detailsTable);
        }
        detailsTable.removeRow(1);

        int pos = dsCtHoaDon.size()+1;

        replaceCellText(detailsTable.getRow(pos).getCell(2), String.format("%,3d", datPhong.getThanhTien()));
        replaceCellText(detailsTable.getRow(pos+1).getCell(2), String.format("%,3d", datPhong.getTienDatCoc()));
        replaceCellText(detailsTable.getRow(pos+2).getCell(2), String.format("%,3d", datPhong.getThanhTien() - datPhong.getTienDatCoc()));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn vị trí lưu.");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Word Files", "*.docx")
        );
        fileChooser.setInitialFileName("HoaDon - " + now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".docx");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedFile = fileChooser.showSaveDialog(node.getScene().getWindow());

        try {
            FileOutputStream file = new FileOutputStream(selectedFile);
            doc.write(file);
            file.close();
            Desktop.getDesktop().open(selectedFile);
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
    }

    

    private static void replaceCellText(XWPFTableCell cell, String replacement) {
        cell.getParagraphs().forEach(paragraph -> {
            // REMOVE RUNS EXCEPT THE 1st
            int size = paragraph.getRuns().size() - 1;
            if (size >= 0)
                for (int i = size; i > 0; i--)
                    paragraph.removeRun(i);
            else
                paragraph.createRun();
            paragraph.getRuns().get(0).setText(replacement, 0);
        });
    }

    private static void createNewRowWithFormat(XWPFTable table) {
        XWPFTableRow lastRow = table.getRows().get(table.getNumberOfRows() - 4);
        CTRow ctrow;
        try {
            ctrow = CTRow.Factory.parse(lastRow.getCtRow().newInputStream());
            table.addRow(new XWPFTableRow(ctrow, table), table.getNumberOfRows() - 3);

        } catch (XmlException | IOException e) {
            ExceptionHandler.handle(e);
        }
    }

    public static void luuFIle(TableView<ObservableList<String>> tableView, String title) {
        ObservableList<ObservableList<String>> data = tableView.getItems();
        XSSFWorkbook workbook = Reporter.getWorkbook("ThongKe.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);

        // CELL STYLES
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFont(workbook.createFont());
        headerStyle.getFont().setBold(true);
        headerStyle.getFont().setFontHeightInPoints((short) 12);
        headerStyle.getFont().setFontName("Arial");

        XSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setFont(workbook.createFont());
        dateStyle.getFont().setFontHeightInPoints((short) 10);
        dateStyle.getFont().setFontName("Arial");
        dateStyle.setAlignment(HorizontalAlignment.CENTER);

        XSSFCellStyle tableHeaderStyle = workbook.createCellStyle();
        tableHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
        tableHeaderStyle.setBorderLeft(BorderStyle.THIN);
        tableHeaderStyle.setBorderRight(BorderStyle.THIN);
        tableHeaderStyle.setBorderTop(BorderStyle.THIN);
        tableHeaderStyle.setBorderBottom(BorderStyle.THIN);
        tableHeaderStyle.setFont(workbook.createFont());
        tableHeaderStyle.getFont().setBold(true);
        tableHeaderStyle.getFont().setFontHeightInPoints((short) 10);
        tableHeaderStyle.getFont().setFontName("Arial");

        XSSFCellStyle tableElementStyle = workbook.createCellStyle();
        tableElementStyle.setAlignment(HorizontalAlignment.CENTER);
        tableElementStyle.setBorderLeft(BorderStyle.THIN);
        tableElementStyle.setBorderRight(BorderStyle.THIN);
        tableElementStyle.setBorderTop(BorderStyle.THIN);
        tableElementStyle.setBorderBottom(BorderStyle.THIN);
        tableElementStyle.setFont(workbook.createFont());
        tableElementStyle.getFont().setFontHeightInPoints((short) 10);
        tableElementStyle.getFont().setFontName("Arial");

        XSSFCellStyle tableFirstElementStyle = workbook.createCellStyle();
        tableFirstElementStyle.setAlignment(HorizontalAlignment.LEFT);
        tableFirstElementStyle.setBorderLeft(BorderStyle.THIN);
        tableFirstElementStyle.setBorderRight(BorderStyle.THIN);
        tableFirstElementStyle.setBorderTop(BorderStyle.THIN);
        tableFirstElementStyle.setBorderBottom(BorderStyle.THIN);
        tableFirstElementStyle.setFont(workbook.createFont());
        tableFirstElementStyle.getFont().setFontHeightInPoints((short) 10);
        tableFirstElementStyle.getFont().setFontName("Arial");

        XSSFCell cell = null;

        cell = sheet.getRow(2).getCell(0);
        cell.setCellValue(title.toUpperCase());
        cell.setCellStyle(headerStyle);

        // DATE
        cell = sheet.getRow(3).getCell(0);
        cell.setCellValue(LocalDate.now().format(DateTimeFormatter.ofPattern("'Ngày 'dd' tháng 'MM' năm 'yyyy")));
        cell.setCellStyle(dateStyle);

        // BOOK BY PUB BY CAT
        int colNum = tableView.getColumns().size();
        sheet.createRow(5);
        for (int j = 0; j < colNum; j++) {
            cell = sheet.getRow(5).createCell(j + 1);
            cell.setCellValue(tableView.getColumns().get(j).getText());
            cell.setCellStyle(tableHeaderStyle);
        }
        for (int i = 0; i < data.size(); i++) {
            sheet.createRow(6 + i);
            for (int j = 0; j < colNum; j++) {
                cell = sheet.getRow(6 + i).createCell(j + 1);
                if (j == 0) {
                    cell.setCellValue(data.get(i).get(j));
                    cell.setCellStyle(tableFirstElementStyle);
                } else {
                    cell.setCellValue(data.get(i).get(j));
                    cell.setCellStyle(tableElementStyle);
                }
            }
        }

        // Ghi file
        Reporter.saveWorkbook(workbook, tableView);
    }
}
