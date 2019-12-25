package controller.hoaDon;

import dao.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import model.*;
import util.Exporter;

public class XemHoaDon {

    @FXML
    TableColumn<HoaDon, Integer> sttCol;

    @FXML
    private TableColumn<HoaDon, String> tenDoCol;

    @FXML
    private TableColumn<HoaDon, Integer> slCol;

    @FXML
    private TableColumn<HoaDon, String> donViCol;

    @FXML
    private TableColumn<HoaDon, String> donGiaCol;

    @FXML
    private TableColumn<HoaDon, String> thanhTienCol;

    @FXML
    private Button inBtn;

    @FXML
    private Button huyBtn;

    @FXML
    private TableView<HoaDon> hoaDonTable;

    @FXML
    private Label maDPLabel;

    @FXML
    private Label gioVaoLabel;

    @FXML
    private Label thanhToanLabel;

    @FXML
    private Label khachHangLabel;

    @FXML
    private Label thanhTienTongLabel;

    @FXML
    private Label tienCocTongLabel;

    @FXML
    private Label gioRaLabel;

    private ObservableList<HoaDon> data;

    private long thanhToan;
    private long thanhTien;

    public void init(DatPhong passedDatPhong, NhanVien nhanVien) {
        DatPhong datPhong = DatPhongDAO.getInstance().get(passedDatPhong.getMaDatPhong());
        data = HoaDonDAO.getInstance().get(datPhong);

        // INIT TABLE
        sttCol.setCellFactory(col -> new TableCell<>() {
            @Override
            public void updateIndex(int index) {
                super.updateIndex(index);
                if (isEmpty() || index < 0)
                    setText(null);
                else
                    setText(Integer.toString(index + 1));
            }
        });
        tenDoCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getTenMuc()));
        donViCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getDonVi()));
        slCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getSoLuong()));
        donGiaCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getDonGia())));
        thanhTienCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(String.format("%,3d", p.getValue().getThanhTien())));

        hoaDonTable.setItems(data);

        // Label
        khachHangLabel.setText(datPhong.getKhachHang().getTenKhach());
        gioVaoLabel.setText(datPhong.getNgayCheckinTt().toString());
        gioRaLabel.setText(datPhong.getNgayCheckoutTt().toString());
        maDPLabel.setText(String.format("%04d", datPhong.getMaDatPhong()));
        tienCocTongLabel.setText(String.format("%,3d", datPhong.getTienDatCoc()));

        for (HoaDon hoaDon: data) {
            thanhTien += hoaDon.getThanhTien();
        }

        thanhTienTongLabel.setText(String.format("%,3d", thanhTien));
        thanhToanLabel.setText(String.format("%,3d", thanhTien - datPhong.getTienDatCoc()));

        // BUTTONS
        huyBtn.setOnAction(event -> ((Node) (event.getSource())).getScene().getWindow().hide());
        datPhong.setThanhTien(thanhTien);
        inBtn.setOnAction(event -> Exporter.taoHoaDon(datPhong, data, nhanVien, hoaDonTable));

    }
}

