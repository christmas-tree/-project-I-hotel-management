package util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reporter {

    public static int getCount(String sql) {
        Connection con;
        int result = 0;

        try {
            con = DbConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery(sql);

            rs.next();
            result = rs.getInt(1);

            rs.close();
            con.close();
        } catch (SQLException e) {
            ExceptionHandler.handle(e);
        }
        return result;

    }

    public static void populateTable(String sql, TableView tableView, ObservableList<ObservableList<String>> data) {

        Connection con = null;
        try {
            con = DbConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery(sql);

            // TABLE COLUMN ADDED DYNAMICALLY
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setMinWidth(125.0);
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));

                tableView.getColumns().addAll(col);
            }

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            tableView.setItems(data);
        } catch (Exception e) {
            ExceptionHandler.handle(e);
        }
    }

    public static XSSFWorkbook getWorkbook(String fileName) {
        File file = new File("src/resources/form/" + fileName);

        XSSFWorkbook workbook;

        try {
            FileInputStream inputStream = new FileInputStream(file);
            workbook = new XSSFWorkbook(inputStream);
            inputStream.close();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
            return null;
        }
        return workbook;
    }

    public static void saveWorkbook(XSSFWorkbook workbook, Node node) {
        // Ghi file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn vị trí lưu.");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );
        fileChooser.setInitialFileName("XuatFile " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy")));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedFile = fileChooser.showSaveDialog(node.getScene().getWindow());

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
