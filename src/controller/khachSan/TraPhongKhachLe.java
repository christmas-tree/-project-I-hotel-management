package controller.khachSan;

import dao.ChiTietDatPhongDAO;
import dao.ChiTietDichVuDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.*;

public class TraPhongKhachLe {

    @FXML
    private TableColumn<ChiTietDichVu, String> donGiaDvCol;

    @FXML
    private TableColumn<ChiTietDatPhong, Integer> soNgayPCol;

    @FXML
    private Button luuBtn;

    @FXML
    private TableView<ChiTietDatPhong> tienPhongTable;

    @FXML
    private Label maDPLabel;

    @FXML
    private Label gioVaoLabel;

    @FXML
    private TextField heSoGiamGiaField;

    @FXML
    private Label thanhToanLabel;

    @FXML
    private Button inBtn;

    @FXML
    private TableColumn<ChiTietDichVu, String> donViDvCol;

    @FXML
    private Button huyBtn;

    @FXML
    private TextField datCocField;

    @FXML
    private Button kiemDoBtn;

    @FXML
    private TableColumn<BoiThuong, String> tenDoBtCol;

    @FXML
    private TableColumn<BoiThuong, Integer> sttBtCol;

    @FXML
    private TableColumn<ChiTietDichVu, Integer> soLuongDvCol;

    @FXML
    private Label khachHangLabel;

    @FXML
    private TableColumn<ChiTietDichVu, String> thanhTienDvCol;

    @FXML
    private TableColumn<ChiTietDatPhong, String> loaiPhongPCol;

    @FXML
    private TableColumn<ChiTietDatPhong, String> thanhTienPCol;

    @FXML
    private TableColumn<BoiThuong, String> donGiaBtCol;

    @FXML
    private TextField heSoNgayLeField;

    @FXML
    private TableColumn<ChiTietDatPhong, String> donGiaPCol;

    @FXML
    private TableView<BoiThuong> boiThuongTable;

    @FXML
    private Label thanhTienTongLabel;

    @FXML
    private Label tienCocTongLabel;

    @FXML
    private TableColumn<ChiTietDichVu, String> tenDvCol;

    @FXML
    private Label gioRaLabel;

    @FXML
    private TableView<ChiTietDichVu> dichVuTable;

    @FXML
    private TableColumn<ChiTietDichVu, Integer> sttDvCol;

    @FXML
    private TableColumn<BoiThuong, Integer> soLuongBtCol;

    @FXML
    private TableColumn<BoiThuong, String> trangThaiBtCol;

    @FXML
    private TableColumn<BoiThuong, String> thanhTienBtCol;

    private ObservableList<BoiThuong> dsBoiThuong;
    private ObservableList<ChiTietDatPhong> dsTienPhong;
    private ObservableList<ChiTietDichVu> dsChiTietDichVu;
    private ChiTietDatPhong chiTietDatPhong = null;
    private long donGiaSauHeSo = 0;
    private long thanhTien = 0;
    private long thanhToan = 0;

    private int soNgay;

    public void init(Phong phong) {
        chiTietDatPhong = ChiTietDatPhongDAO.getInstance().getByPhong(phong);
        dsChiTietDichVu = ChiTietDichVuDAO.getInstance().getAll(chiTietDatPhong);
        dsTienPhong = FXCollections.observableArrayList(chiTietDatPhong);
        soNgay = (int) Math.round((chiTietDatPhong.getNgayCheckoutTt().getTime() - chiTietDatPhong.getNgayCheckinTt().getTime()) / 86400000.0);
        donGiaSauHeSo = tinhDonGia(chiTietDatPhong);
        tinhThanhTien();
        tinhThanhToan();

        // INIT TABLE
        sttBtCol.setCellFactory(col -> new TableCell<>() {
            @Override
            public void updateIndex(int index) {
                super.updateIndex(index);
                if (isEmpty() || index < 0)
                    setText(null);
                else
                    setText(Integer.toString(index + 1));
            }
        });
        tenDoBtCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getChiTietPhong().getTenDo()));
        trangThaiBtCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getTrangThaiString()));
        soLuongBtCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getSoLuong()));
        donGiaBtCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getChiTietPhong().getGiaTien())));
        thanhTienBtCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getChiTietPhong().getGiaTien() * p.getValue().getSoLuong())));

        loaiPhongPCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPhong().getLoaiPhong().getLoaiPhong()));
        donGiaPCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", donGiaSauHeSo)));
        soNgayPCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(soNgay));
        thanhTienPCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", donGiaSauHeSo * soNgay)));

        sttDvCol.setCellFactory(col -> new TableCell<>() {
            @Override
            public void updateIndex(int index) {
                super.updateIndex(index);
                if (isEmpty() || index < 0)
                    setText(null);
                else
                    setText(Integer.toString(index + 1));
            }
        });
        tenDvCol.setCellValueFactory(p-> new ReadOnlyObjectWrapper<>(p.getValue().getDichVu().getTenDv()));
        donGiaDvCol.setCellValueFactory(p-> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getDichVu().getGiaDv())));
        soLuongDvCol.setCellValueFactory(p-> new ReadOnlyObjectWrapper<>(p.getValue().getSoLuong()));
        donViDvCol.setCellValueFactory(p-> new ReadOnlyObjectWrapper<>(p.getValue().getDichVu().getDonVi()));
        thanhTienDvCol.setCellValueFactory(p-> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getThanhTien())));

        boiThuongTable.setItems(dsBoiThuong);
        tienPhongTable.setItems(dsTienPhong);
        dichVuTable.setItems(dsChiTietDichVu);

        // Fields
        khachHangLabel.setText(chiTietDatPhong.getDatPhong().getKhachHang().getTenKhach());
        gioVaoLabel.setText(chiTietDatPhong.getNgayCheckinTt().toString());
        gioRaLabel.setText(chiTietDatPhong.getNgayCheckoutTt().toString());
        maDPLabel.setText(String.format("%06d", chiTietDatPhong.getDatPhong().getMaDatPhong()));

        datCocField.textProperty().addListener((observableValue, s, t1) -> {
            datCocField.setText(t1.replaceAll("[^\\d]", ""));
            tienCocTongLabel.setText(String.format("%,3d", Long.parseLong(t1)));
        });

        heSoGiamGiaField.textProperty().addListener(((observableValue, s, t1) -> {
            donGiaSauHeSo = tinhDonGia(chiTietDatPhong);
            tienPhongTable.refresh();
            tinhThanhTien();
            tinhThanhToan();
        }));
        heSoNgayLeField.textProperty().addListener(((observableValue, s, t1) -> {
            donGiaSauHeSo = tinhDonGia(chiTietDatPhong);
            tienPhongTable.refresh();
            tinhThanhTien();
            tinhThanhToan();
        }));

    }

    public void print() {

    }

    public void save() {

    }

    public void tinhThanhTien() {
        thanhTien = donGiaSauHeSo * soNgay;
        for (int i=0; i < dsBoiThuong.size(); i++) {
            thanhTien += dsBoiThuong.get(i).getChiTietPhong().getGiaTien() * dsBoiThuong.get(i).getSoLuong();
        }
        for (int i=0; i < dsChiTietDichVu.size(); i++) {
            thanhTien += dsChiTietDichVu.get(i).getDichVu().getGiaDv() * dsChiTietDichVu.get(i).getSoLuong();
        }
        thanhTienTongLabel.setText(String.format("%,3d", thanhTien));
    }

    public void tinhThanhToan() {
        thanhToan = thanhTien - Long.parseLong(datCocField.getText());
        thanhToanLabel.setText(String.format("%,3d", thanhToan));
    }

    public long tinhDonGia(ChiTietDatPhong chiTietDatPhong) {
        return Math.round(chiTietDatPhong.getPhong().getLoaiPhong().getGiaTien() * Float.parseFloat(heSoGiamGiaField.getText()) * Float.parseFloat(heSoNgayLeField.getText()) / 1000) * 1000;
    }

    xxxx
}
