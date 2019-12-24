package controller.khachSan;

import dao.BoiThuongDAO;
import dao.ChiTietPhongDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import model.BoiThuong;
import model.ChiTietDatPhong;
import model.ChiTietPhong;
import util.AlertGenerator;

import java.util.ArrayList;

public class KiemPhong {

    @FXML
    private Button luuBtn;

    @FXML
    private TableView<BoiThuong> boiThuongTable;

    @FXML
    private TableColumn<BoiThuong, Integer> phongCol;

    @FXML
    private TableColumn<BoiThuong, String> tenDoCol;

    @FXML
    private TableColumn<BoiThuong, Integer> soLuongCol;

    @FXML
    private TableColumn<BoiThuong, Long> boiThuongCol;

    @FXML
    private TableColumn<BoiThuong, Integer> slBoiThuongCol;

    @FXML
    private TableColumn<BoiThuong, String> ghiChuCol;

    @FXML
    private TableColumn<BoiThuong, String> trangThaiCol;

    private ObservableList<BoiThuong> dsBoiThuong = FXCollections.observableArrayList();
    private ObservableList<BoiThuong> dsBoiThuongTraVe = FXCollections.observableArrayList();

    public void init(ObservableList<ChiTietDatPhong> dsChiTietDatPhong) {
        for (ChiTietDatPhong chiTietDatPhong : dsChiTietDatPhong) {
            ObservableList<ChiTietPhong> dsDo = ChiTietPhongDAO.getInstance().getAll(chiTietDatPhong.getPhong());
            for (ChiTietPhong chiTietPhong : dsDo) {
                dsBoiThuong.add(new BoiThuong(chiTietPhong, chiTietDatPhong));
            }
        }
        uiInit();
    }

    public void init(ChiTietDatPhong chiTietDatPhong) {
        ObservableList<ChiTietPhong> dsDo = ChiTietPhongDAO.getInstance().getAll(chiTietDatPhong.getPhong());
        for (ChiTietPhong chiTietPhong : dsDo) {
            dsBoiThuong.add(new BoiThuong(chiTietPhong, chiTietDatPhong));
        }
        uiInit();
    }


    public void uiInit() {

        // TABLE INIT
        phongCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getChiTietPhong().getPhong().getMaPhong()));
        tenDoCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getChiTietPhong().getTenDo()));
        soLuongCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getChiTietPhong().getSoLuong()));
        slBoiThuongCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getSoLuong()));
        boiThuongCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getBoiThuong()));

        trangThaiCol.setCellFactory(ComboBoxTableCell.forTableColumn(BoiThuong.dsTrangThai));
        trangThaiCol.setOnEditCommit(t -> {
            BoiThuong boiThuong = t.getTableView().getItems().get(t.getTablePosition().getRow());
            int trangThai = boiThuong.setTrangThai(t.getNewValue());
            boiThuong.getChiTietPhong().setTrangThai(trangThai);
        });

        boiThuongCol.setCellFactory(p -> {
            TextFieldTableCell<BoiThuong, Long> cell = new TextFieldTableCell<>();
            cell.setConverter(new StringConverter<>() {
                @Override
                public String toString(Long boiThuong) {
                    return String.format("%,d", boiThuong);
                }

                @Override
                public Long fromString(String string) {
                    return Long.parseLong(string.replaceAll("[^\\d]", ""));
                }
            });
            return cell;
        });
        boiThuongCol.setOnEditCommit((event) -> {
            TablePosition<BoiThuong, Long> pos = event.getTablePosition();
            Long boiThuong = event.getNewValue();
            int row = pos.getRow();
            event.getTableView().getItems().get(row).setBoiThuong(boiThuong);
        });

        slBoiThuongCol.setCellFactory(p -> {
            TextFieldTableCell<BoiThuong, Integer> cell = new TextFieldTableCell<>();
            cell.setConverter(new StringConverter<>() {
                @Override
                public String toString(Integer value) {
                    return String.valueOf(value);
                }

                @Override
                public Integer fromString(String string) {
                    return Integer.parseInt(string);
                }
            });
            return cell;
        });
        slBoiThuongCol.setOnEditCommit((event) -> {
            TablePosition<BoiThuong, Integer> pos = event.getTablePosition();
            Integer slBoiThuong = event.getNewValue();
            int row = pos.getRow();
            event.getTableView().getItems().get(row).setSoLuong(slBoiThuong);
        });

        ghiChuCol.setCellFactory(TextFieldTableCell.forTableColumn());
        ghiChuCol.setOnEditCommit((event) -> {
            TablePosition<BoiThuong, String> pos = event.getTablePosition();
            String ghiChu = event.getNewValue();
            int row = pos.getRow();
            event.getTableView().getItems().get(row).setGhiChu(ghiChu);
        });

        boiThuongTable.setItems(dsBoiThuong);

        luuBtn.setOnAction(event -> {
            if (validate()) {
                for (BoiThuong boiThuong : dsBoiThuong) {
                    if (boiThuong.getBoiThuong() != 0)
                        dsBoiThuongTraVe.add(boiThuong);
                }
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });
    }

    public boolean validate() {
        for (BoiThuong boiThuong : dsBoiThuong)
            if (boiThuong.getTrangThai() == null) {
                AlertGenerator.error("Chưa kiểm tra hết đồ đạc.");
                return false;
            }
        return true;
    }

    public ObservableList<BoiThuong> getDsBoiThuong() {
        return dsBoiThuongTraVe;
    }

}
