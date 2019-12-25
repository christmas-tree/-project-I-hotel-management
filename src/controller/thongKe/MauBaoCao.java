package controller.thongKe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import util.Exporter;
import util.Reporter;

public class MauBaoCao {

    @FXML
    private TableView<ObservableList<String>> bangTable;

    @FXML
    private Label headerLabel;

    @FXML
    private Button inBtn;

    private ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

    public void init(String sql, String title) {
        headerLabel.setText(title);
        Reporter.populateTable(sql, bangTable, data);
        inBtn.setOnAction(event -> Exporter.luuFIle(bangTable, title));
    }
}
