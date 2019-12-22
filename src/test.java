import javafx.application.Application;
        import javafx.scene.Scene;
        import javafx.stage.Stage;

        import com.flexganttfx.model.Activity;
        import com.flexganttfx.model.Layer;
        import com.flexganttfx.model.Row;
        import com.flexganttfx.model.activity.MutableActivityBase;
        import com.flexganttfx.view.GanttChart;

public class test extends Application {

    /*
     * Common superclass of Fleet, Aircraft, and Crew.
     */
    class ModelObject<
            P extends Row<?,?,?>, // Type of parent row
            C extends Row<?,?,?>, // Type of child rows
            A extends Activity> extends Row<P, C, A> { }

    class Fleet extends ModelObject<Row<?,?,?>, Aircraft, Activity> { }

    class Aircraft extends ModelObject<Fleet, Fleet, Flight> { }

    class Flight extends MutableActivityBase<Object> { }

    @Override
    public void start(Stage stage) throws Exception {

        // Our root object.
        Fleet fleet = new Fleet();
        fleet.setExpanded(true);

        // Create the control.
        GanttChart<ModelObject<?,?,?>> gantt = new GanttChart<>(fleet);

        // Layers based on service type.
        Layer cargoLayer = new Layer("Cargo");
        Layer trainingLayer = new Layer("Training");
        Layer charterLayer = new Layer("Charter");
        gantt.getLayers().addAll(cargoLayer, trainingLayer, charterLayer);

        // Create the aircrafts.
        Aircraft aircraft1 = new Aircraft();
        Aircraft aircraft2 = new Aircraft();

        // Add the aircrafts to the fleet.
        fleet.getChildren().addAll(aircraft1, aircraft2);

        // Create the flights
        Flight flight1 = new Flight(); // a cargo flight
        Flight flight2 = new Flight(); // a training flight
        Flight flight3 = new Flight(); // a charter flight

        aircraft1.addActivity(cargoLayer, flight1);
        aircraft1.addActivity(trainingLayer, flight2);
        aircraft2.addActivity(charterLayer, flight3);

        Scene scene = new Scene(gantt);

        stage.setTitle("Fleet Schedule");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}