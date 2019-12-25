package controller.thongKe;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class MauBaoCao {

    @FXML
    private TableView<?> bangTable;

    @FXML
    private Label headerLabel;

    @FXML
    private Button inBtn;

    private ObservableList<ObservableList<String>> data;

    public void init(String sql, String title) {

    }

    public void export() {

    }

}
