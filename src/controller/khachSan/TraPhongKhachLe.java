package controller.khachSan;

import dao.ChiTietDatPhongDAO;
import dao.ChiTietDichVuDAO;
import dao.DatPhongDAO;
import dao.PhongDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.*;
import util.AlertGenerator;
import util.ExHandler;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;

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
    private ObservableList<ChiTietDatPhong> dsChiTietDatPhong;
    private ObservableList<ChiTietDichVu> dsChiTietDichVu;

    private long tienCoc = 0;
    private long thanhTien = 0;
    private long thanhToan = 0;
    private int soNgay;

    private Timestamp now = new Timestamp(System.currentTimeMillis());

    public void init(Phong phong) {
        ChiTietDatPhong chiTietDatPhong = ChiTietDatPhongDAO.getInstance().getByPhong(phong);
        dsChiTietDichVu = ChiTietDichVuDAO.getInstance().getAll(chiTietDatPhong);
        dsChiTietDatPhong = FXCollections.observableArrayList(chiTietDatPhong);

        if (chiTietDatPhong.getDatPhong().isKhachDoan()) {
            AlertGenerator.error("Phòng đặt theo đoàn. Vui lòng checkout bằng nút Checkout khách đoàn.");
            huyBtn.fire();
        }
        chiTietDatPhong.setNgayCheckoutTt(now);
        soNgay = (int) Math.round((now.getTime() - chiTietDatPhong.getNgayCheckinTt().getTime()) / 86400000.0);

        // FIELDS

        datCocField.textProperty().addListener((observableValue, s, t1) -> {
            datCocField.setText(t1.replaceAll("[^\\d]", ""));
            tinhHoaDon();
        });

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
        thanhTienBtCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getBoiThuong())));

        loaiPhongPCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPhong().getLoaiPhong().getLoaiPhong()));
        donGiaPCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getDonGiaSauHeSo())));
        soNgayPCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(soNgay));
        thanhTienPCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getThanhTien())));

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

        tienPhongTable.setItems(dsChiTietDatPhong);
        dichVuTable.setItems(dsChiTietDichVu);

        // Fields
        khachHangLabel.setText(chiTietDatPhong.getDatPhong().getKhachHang().getTenKhach());
        gioVaoLabel.setText(chiTietDatPhong.getNgayCheckinTt().toString());
        gioRaLabel.setText(chiTietDatPhong.getNgayCheckoutTt().toString());
        maDPLabel.setText(String.format("%06d", chiTietDatPhong.getDatPhong().getMaDatPhong()));
        datCocField.setText(String.format("%,3d", chiTietDatPhong.getDatPhong().getTienDatCoc()));

        kiemDoBtn.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/khachSan/kiemTraCheckOut.fxml"));
            try {
                Parent editRoot = loader.load();
                new JMetro(editRoot, Style.LIGHT);

                Stage stage = new Stage();
                Scene scene = new Scene(editRoot);
                stage.setTitle("Kiểm đồ");
                stage.setScene(scene);
                KiemPhong kiemPhong = loader.getController();
                kiemPhong.init(dsChiTietDatPhong);

                stage.showAndWait();
                dsBoiThuong = kiemPhong.getDsBoiThuong();
            } catch (IOException e) {
                ExHandler.handle(e);
            }

            boiThuongTable.setItems(dsBoiThuong);
        });

        luuBtn.setOnAction(event -> {
            save();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });
        huyBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());
    }

    public void print() {

    }

    public void save() {
        dsChiTietDatPhong.get(0).getDatPhong().setDaXong(true);
        dsChiTietDatPhong.get(0).getPhong().setTrangThai(Phong.DANGDON);
        if (PhongDAO.getInstance().update(dsChiTietDatPhong)
            && ChiTietDatPhongDAO.getInstance().update(dsChiTietDatPhong)
            && DatPhongDAO.getInstance().update(dsChiTietDatPhong.get(0).getDatPhong()))
        {
            AlertGenerator.success("Trả phòng thành công");
        } else {
            AlertGenerator.error("Trả phòng thất bại");
        }
    }

    public void tinhHoaDon() {
        thanhTien = 0;
        // Tien phong
        for (ChiTietDatPhong chiTietDatPhong: dsChiTietDatPhong) {
            thanhTien += chiTietDatPhong.getThanhTien();
        }
        if (dsBoiThuong != null) {
            for (BoiThuong boiThuong: dsBoiThuong) {
                thanhTien += boiThuong.getBoiThuong();
            }
        }
        for (ChiTietDichVu chiTietDichVu: dsChiTietDichVu) {
            thanhTien += chiTietDichVu.getThanhTien();
        }

        tienCoc = Long.parseLong(datCocField.getText());
        thanhToan = thanhTien - tienCoc;

        thanhTienTongLabel.setText(String.format("%,d", thanhTien));
        tienCocTongLabel.setText(String.format("%,d", tienCoc));
        thanhToanLabel.setText(String.format("%,d", thanhToan));
    }

//    public void tinhGiaPhongSauHeSo() {
//        for (ChiTietDatPhong chiTietDatPhong: dsChiTietDatPhong) {
//            long donGia = Math.round(chiTietDatPhong.getPhong().getLoaiPhong().getGiaTien() * Float.parseFloat(heSoGiamGiaField.getText()) * Float.parseFloat(heSoNgayLeField.getText()) / 1000) * 1000;
//            chiTietDatPhong.setDonGiaSauHeSo(donGia);
//            chiTietDatPhong.setThanhTien(donGia * soNgay);
//        }
//    }
}
