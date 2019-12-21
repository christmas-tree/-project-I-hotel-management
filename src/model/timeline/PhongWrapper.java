package model.timeline;
import com.flexganttfx.model.Row;
import model.Phong;

import java.util.ArrayList;

public class PhongWrapper extends Row<PhongWrapper, PhongWrapper, ChiTietDatPhongWrapper> {
    Phong phong;
    public PhongWrapper(Phong phong) {
        super(String.valueOf(phong.getMaPhong()));
        this.phong = phong;
    }

    public static ArrayList<PhongWrapper> from(ArrayList<Phong> dsPhong) {
        ArrayList<PhongWrapper> dsPhongWrapper = new ArrayList<>();
        for (Phong phong: dsPhong) {
            dsPhongWrapper.add(new PhongWrapper(phong));
        }
        return dsPhongWrapper;
    }

    public Phong getPhong() {
        return phong;
    }

    public void setPhong(Phong phong) {
        this.phong = phong;
    }
}