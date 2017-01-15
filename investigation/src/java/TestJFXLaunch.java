
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class TestJFXLaunch {
	public static void main(String[] args) {
		
		Application.launch(MyApp.class, args);
//		Application japp = new MyApp();
//		japp.init()
//		japp.start()
	}
}


class MyApp extends Application {
	public void start(Stage stage) {
		Circle circ = new Circle(40,40,30);
		Group root = new Group(circ);
		Scene scene = new Scene(root, 400, 300);

		stage.setTitle("JFXLaunch Test");
		stage.setScene(scene);
		stage.show();
	}
}

