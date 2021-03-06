package application;

import baseDatos.FachadaBaseDatos;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

	private static FachadaBaseDatos fbd;

	@Override
	public void start(Stage primaryStage) {

		fbd = Main.getFbd();
		try {
			Parent Login = FXMLLoader.load(getClass().getResource("/vista/login.fxml"));
			primaryStage.setTitle("PDEA Login");
			primaryStage.setScene(new Scene(Login));
			primaryStage.show();
			primaryStage.setMinHeight(340);
			primaryStage.setMinWidth(520);
			primaryStage.setMaxHeight(500);
			primaryStage.setMaxWidth(520);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		launch(args);
	}

	public static FachadaBaseDatos getFbd() {
		if (fbd == null) {
			fbd = new FachadaBaseDatos();
			return fbd;
		} else {
			return fbd;
		}
	}
	

}
