package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import modelo.Paciente;
import modelo.Ejercicio;

public class ControladorEjercicios implements Initializable {

	@FXML
	private AnchorPane anchorGrande;

	@FXML
	private JFXButton btnAnterior;

	@FXML
	private JFXButton btnComenzar;

	@FXML
	private JFXButton btnSiguiente;

	@FXML
	private JFXButton btnVolver;

	@FXML
	private Label cronometro;

	@FXML
	private Label numeroEjercicio;

	@FXML
	private Label nombrePaciente;

	@FXML
	private ImageView pantallaEj;

	private ArrayList<Ejercicio> ejercicios;
	private Integer contador = 0;
	private Integer interval = 0;
	private String[] hacerEjercicios;
	private Paciente p = ControladorPacientepp.getPacienteActual();

	private baseDatos.FachadaBaseDatos fbd = application.Main.getFbd();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		nombrePaciente.setText("Hola " + p.getNombre() + ",");

		this.ejercicios = fbd.obtenerEjerciciosPaciente(p);
		if (ejercicios.size() != 0) {
			pantallaEj.setImage(new Image(
					this.getClass().getResource("/" + this.ejercicios.get(contador).getGif()).toExternalForm()));

			this.interval = this.ejercicios.get(contador).getDuracion();
			numeroEjercicio.setText(" " + (contador + 1) + " de " + this.ejercicios.size());

			// String hacerEjercicios con tamaño array de ejercicios
			hacerEjercicios = new String[this.ejercicios.size()];
			for (int i = 0; i < hacerEjercicios.length; i++) {
				hacerEjercicios[i] = ejercicios.get(i).getNombre();
			}
		} else {
			Label emptyEnv = new Label("No hay ejercicios asignados.");
			emptyEnv.setFont(new Font("Arial", 18));
			emptyEnv.setLayoutY(60);
			emptyEnv.setLayoutX(5);
			anchorGrande.getChildren().add(emptyEnv);
			AnchorPane.setTopAnchor(emptyEnv, Double.valueOf(40));
		}

	}

	// Funcion asociada al boton de comenzar. Inicia la cuenta atras del cronometro.
	@FXML
	void pressBtnComenzar(ActionEvent event) {
		this.btnComenzar.setDisable(true);
		empezarContador(this.ejercicios.get(contador).getDuracion() + 1);

		for (int i = 0; i < hacerEjercicios.length; i++) {
			if (hacerEjercicios[i].equals(ejercicios.get(contador).getNombre())) {
				hacerEjercicios[i] = "ok";
			}
		}
	}

	// Boton anterior
	// ------------------------------------------------
	@FXML
	void pressBtnAnterior(ActionEvent event) {
		ejercicioAnterior();
	}

	// Boton siguiente
	// ------------------------------------------------
	@FXML
	void pressBtnSiguiente(ActionEvent event) {
		siguienteEjercicio();
	}

	// Boton volver
	// ------------------------------------------------
	@FXML
	void pressBtnVolver(ActionEvent event) throws IOException {
		try {
			boolean hechos = true;
			for (int i = 0; i < hacerEjercicios.length; i++) {
				if (!hacerEjercicios[i].equals("ok")) {
					hechos = false;
				}
			}
			p.setEjerciciosHechos(hechos);
			if (hechos == true) {
				String fechaHoy = getFechaString(Calendar.getInstance().getTime());
				p.setCuandoHechos(fechaHoy);
			}
			fbd.modificarPaciente(p);

			Parent PacienteVentana = FXMLLoader.load(getClass().getResource("/vista/menupaciente.fxml"));
			Stage Pacientepp = new Stage();
			Pacientepp.setTitle("Menu Principal Paciente");
			Pacientepp.setScene(new Scene(PacienteVentana));
			Pacientepp.show();
			Pacientepp.setMinHeight(550);
			Pacientepp.setMinWidth(500);

			Stage CerrarVentanaLogin = (Stage) btnVolver.getScene().getWindow();
			CerrarVentanaLogin.close();
		}

		catch (ControladorExcepciones case1) {
			ControladorAvisos.setMensajeError("No se pudo abrir la ventana de Paciente.");
			case1.abrirVentanaAvisos();
		}
	}

	// METODOS
	void empezarContador(Integer duracion) {

		// Cada segundo actualiza el valor del intervalo
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
			if (interval < 0) {
				interval = 0;

			}
			if (interval < 10) {
				cronometro.setText("00:0" + interval);

			} else {
				cronometro.setText("00:" + interval);
			}
			this.interval = interval - 1;
		}));
		// Numero de veces que se ejecuta la funcion
		timeline.setCycleCount(duracion + 1);
		// Empieza
		timeline.play();
		if (!this.btnComenzar.isDisable()) {
			timeline.stop();
		}
		// Acaba y pasa al siguiente ejercicio
		timeline.setOnFinished(e -> siguienteEjercicio());

	}

	void siguienteEjercicio() {
		this.btnComenzar.setDisable(false);
		if (interval > 0) {

			ControladorAvisos.setMensajeError("Porfavor, acabe su ejercicio antes de pasar al siguiente.");
			abrirVentanaAvisos();

		} else {

			if (contador == ejercicios.size() - 1) {
				ControladorAvisos.setMensajeError("Esta usted en el ultimo ejercicio, ¡ha terminado!");
				abrirVentanaAvisos();
			} else {

				this.contador = contador + 1;
				this.interval = this.ejercicios.get(contador).getDuracion();
				cronometro.setText("00:" + interval);

				numeroEjercicio.setText(" " + (contador + 1) + " DE " + this.ejercicios.size() + " ");

				pantallaEj.setImage(new Image(
						this.getClass().getResource("/" + this.ejercicios.get(contador).getGif()).toExternalForm()));
			}
		}
	}

	void ejercicioAnterior() {

		if (contador == 0) {
			ControladorAvisos.setMensajeError("Esta usted en el primer ejercicio.");
			abrirVentanaAvisos();
		} else {

			cronometro.setText("00:" + interval);

			this.contador = contador - 1;
			this.interval = this.ejercicios.get(contador).getDuracion();
			numeroEjercicio.setText(" " + (contador + 1) + " de " + this.ejercicios.size());

			pantallaEj.setImage(new Image(
					this.getClass().getResource("/" + this.ejercicios.get(contador).getGif()).toExternalForm()));
		}
	}

	public void abrirVentanaAvisos() {
		try {
			Parent avisos = FXMLLoader.load(getClass().getResource("../vista/avisos.fxml"));
			Stage VentanaAvisos = new Stage();
			VentanaAvisos.setTitle("Aviso");
			VentanaAvisos.setScene(new Scene(avisos));
			VentanaAvisos.show();
			VentanaAvisos.setMinHeight(200);
			VentanaAvisos.setMinWidth(500);
			VentanaAvisos.setMaxHeight(200);
			VentanaAvisos.setMaxWidth(600);

		} catch (Exception a) {
			System.out.println("Error");
		}
	}

	@SuppressWarnings("deprecation")
	private String getFechaString(Date dummy) {
		// Choose time zone in which you want to interpret your Date
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));

		cal.setTime(dummy);
		Integer dia = ((Integer) dummy.getDate());
		String dias;
		if (dia < 10) {
			dias = "0" + dia.toString();
		} else {
			dias = dia.toString();
		}
		Integer m = (Integer) dummy.getMonth() + 1;
		String mess;
		if (m < 10) {
			mess = "0" + m.toString();
		} else {
			mess = m.toString();
		}
		int year = cal.get(Calendar.YEAR);
		String anho = ((Integer) year).toString();
		Integer hora = ((Integer) dummy.getHours());
		String horas;
		if (hora < 10) {
			horas = "0" + hora.toString();
		} else {
			horas = hora.toString();
		}
		Integer min = (Integer) dummy.getMinutes();
		String mins;
		if (min < 10) {
			mins = "0" + min.toString();
		} else {
			mins = min.toString();
		}
		String f = horas + ":" + mins + "-" + dias + "/" + mess + "/" + anho;
		return f;
	}
}