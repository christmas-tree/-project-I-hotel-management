package controller.khachSan;

import com.flexganttfx.view.graphics.GraphicsBase;
import controller.khachSan.NhanKhachLe;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Phong;
import model.timeline.PhongWrapper;
import util.AlertGenerator;
import util.ExHandler;

import java.io.IOException;
import java.util.ArrayList;

public class TimelineDatPhong extends StackPane implements Callback<GraphicsBase.RowControlsParameter<PhongWrapper>, Node> {

    private Button datPhongBtn;
    private ArrayList<Phong> dsPhong;

    public TimelineDatPhong(ArrayList<Phong> dsPhong) {
        this.dsPhong = dsPhong;
        datPhongBtn = new Button("Đặt phòng");
        getChildren().add(datPhongBtn);
    }

    public Node call(GraphicsBase.RowControlsParameter param) {
        datPhongBtn.setOnAction(evt -> {
            System.out.println("Pressed on row " +
                    param.getRow().getName());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/khachSan/nhanKhachLe.fxml"));
            try {
                Parent editRoot = loader.load();
                new JMetro(editRoot, Style.LIGHT);

                Stage stage = new Stage();
                Scene scene = new Scene(editRoot);
                stage.setTitle("Đặt phòng khách lẻ");
                stage.setScene(scene);
                stage.setResizable(false);
                NhanKhachLe nhanKhachLe = loader.getController();
                nhanKhachLe.initBook(Integer.parseInt(param.getRow().getName()), dsPhong);

                stage.showAndWait();
            } catch (IOException e) {
                ExHandler.handle(e);
            }
        });
        return this;
    }
}

