package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import modelo.Aviso;
import modelo.Cita;
import modelo.Medico;
import modelo.Paciente;
import modelo.Mensaje;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXTreeTableView;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ControladorMedicopp implements Initializable {

	@FXML
	private JFXDatePicker campoFecha;

	@FXML
	private JFXTimePicker campoHora;

	@FXML
	private JFXComboBox<String> inputBuscarPaciente;

	@FXML
	private Label campoMedico;

	@FXML
	private Label labelRedactar;

	@FXML
	private Label labelBandejaEntrada;

	@FXML
	private Label labelBandejaSalida;

	@FXML
	private TextField campoBusqueda;

	@FXML
	private ListView<String> listaPacientes;

	@FXML
	private TextField campoDNI;

	@FXML
	private TextField campoNombre;

	@FXML
	private TextField campoTelefono;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnEjercicios;

	@FXML
	private AnchorPane anchorPaneRecibidos;

	@FXML
	private Accordion AccordionMensajesRec;

	@FXML
	private AnchorPane anchorPaneEnviados;

	@FXML
	private AnchorPane anchorPaneAvisos;

	@FXML
	private Accordion AccordionMensajesEnv;

	@FXML
	private TextArea campoRedactar;

	@FXML
	private TextField campoAsunto;

	@FXML
	private TextField notaCita;

	@FXML
	private JFXButton btnConfirmarEnvio;

	@FXML
	private JFXButton btnResponder;

	@FXML
	private JFXButton buttonVolver;

	@FXML
	private JFXTabPane JFXTabPaneMensajeria;

	@FXML
	private JFXComboBox<String> comboBoxElegirDestinatario;

	@FXML
	private JFXTreeTableView<?> tableCitas;

	private ObservableList<String> listaPacientesComboBox = FXCollections.observableArrayList(getNombrePacientes());

	private static Medico medicoActual = new Medico();

	// Metodos

	@Override
	public void initialize(URL location, ResourceBundle reosurces) {
		JFXTabPaneMensajeria.setTabMinWidth(100);
		JFXTabPaneMensajeria.setTabMinHeight(100);
		labelRedactar.setRotate(90);
		labelBandejaEntrada.setRotate(90);
		labelBandejaSalida.setRotate(90);
		comboBoxElegirDestinatario.setItems(listaPacientesComboBox);
		campoMedico.setText("Hola " + ControladorMedicopp.getMedicoActual().getNombre() + ",");

		setAvisos();
		setTitledPanesEnviados();
		setTitledPanesRecibidos();

		inputBuscarPaciente.setItems(listaPacientesComboBox);

	}

	@FXML
	@SuppressWarnings("deprecation")
	void pressBtnCrearCita(ActionEvent event) {
		ArrayList<Cita> addCita = lectorJson.lectorJsonCitas();
		Cita nCita = new Cita();

		String pacienteBuscado = inputBuscarPaciente.getValue();
		Paciente p = coincidencia(pacienteBuscado);
		// asociar mediante dni del paciente
		nCita.setDni(p.getDni());
		// fecha de la cita
		String FechaN[] = campoFecha.getValue().toString().split("-");
		List<String> Fecha = Arrays.asList(FechaN);
		int dia = Integer.parseInt(Fecha.get(0));
		int mes = Integer.parseInt(Fecha.get(1));
		int anho = Integer.parseInt(Fecha.get(2));

		// hora de la cita
		String HoraN[] = campoHora.getValue().toString().split(":");
		List<String> HoraCita = Arrays.asList(HoraN);
		int hora = Integer.parseInt(HoraCita.get(0));
		int mins = Integer.parseInt(HoraCita.get(1));

		// guardar dia y hora de la cita en un Date
		Date calend = new Date(anho, mes, dia, hora, mins);
		nCita.setFecha(calend);

		// aniadir comentario del medico
		nCita.setNota(notaCita.getText());

		// aniadir nCita al Arraylist
		addCita.add(nCita);

		// escribir en el json de citas el Arraylist modificado
		escritorJson.escribirEnJsonCitas(addCita);

		// aviso al usuario de que se ha creado la cita correctamente
		ControladorAvisos.setMensajeError("Se ha a�adido correctamente una nueva cita.");
		abrirVentanaAvisos();
		// limpiar los campos del tab de Citas
		inputBuscarPaciente.setValue(null);
		notaCita.clear();
		campoFecha.setValue(null);
		campoHora.setValue(null);
	}

	@FXML
	void comprobarInput(KeyEvent event) throws Exception {
		// comparar el nombre introducido con los pacientes asignados al medico, para
		// sugerir posibles coincidencias de forma dinamica

		ArrayList<String> nombresPacientes = lectorJson.getNombresCompletosPacientesDe(medicoActual);
		ArrayList<String> sugerencias = new ArrayList<String>();

		boolean sugerenciasEncontradas;
		if (inputBuscarPaciente.getValue() != null) {
			for (int i = 0; i < medicoActual.getPacientes().size(); i++) {
				// bucle que va comparando el input con el nombre de cada paciente
				int longitud = 0;

				for (int a = 0; a < inputBuscarPaciente.getValue().length(); a++) {
					// bucle que va comparando los char del input con los char del nombre de
					// paciente
					if (inputBuscarPaciente.getValue().toLowerCase().charAt(a) == nombresPacientes.get(i).toLowerCase()
							.charAt(a)) {
						longitud++;
					} else {
						break;
					}
				}
				if (longitud == inputBuscarPaciente.getValue().length()) {
					// add nombre en posicion i a un nuevo arraylist que se pasa al comboBox con la
					// observableList listaSugerencias
					sugerencias.add(nombresPacientes.get(i));
					sugerenciasEncontradas = true;
				}
			}

			if (sugerenciasEncontradas = true) {
				inputBuscarPaciente.getItems().clear();
				ObservableList<String> listaSugerencias = FXCollections.observableArrayList(sugerencias);
				inputBuscarPaciente.setItems(listaSugerencias);
			}
		} else {
			// por defecto se muestra la lista entera de pacientes
			ObservableList<String> listaPacientesComboBox = FXCollections.observableArrayList(nombresPacientes);
			inputBuscarPaciente.setItems(listaPacientesComboBox);
		}
		inputBuscarPaciente.autosize();
		inputBuscarPaciente.show();
	}

	private Paciente coincidencia(String pacienteBuscado) {
		for (int i = 0; i < medicoActual.getPacientes().size(); i++) {
			ArrayList<String> nombresPacientes = lectorJson.getNombresCompletosPacientesDe(medicoActual);
			if (nombresPacientes.get(i).equalsIgnoreCase(pacienteBuscado)) {
				Paciente p = lectorJson.getPaciente(medicoActual.getPacientes().get(i));
				return p;
			}
		}
		return null;
	}

	@FXML
	void pressBtnConfirmarEnvio(ActionEvent event) {
		if (campoAsunto.getText().length() > 0) {

			if (campoRedactar.getText().length() > 0) {

				try {
					String pac = comboBoxElegirDestinatario.getValue();
					Integer indice = getIndiceComboBox(pac);
					if (indice != null) {
						String dniPac = medicoActual.getPacientes().get(indice);

						enviarMensaje(dniPac);
					} else {
						ControladorAvisos.setMensajeError("Por favor, seleccione un paciente.");
						abrirVentanaAvisos();

					}
				} catch (Exception a) {
					ControladorAvisos.setMensajeError("Error enviando el mensaje.");
					abrirVentanaAvisos();
				}
			} else {
				ControladorAvisos.setMensajeError("No ha introducido texto alguno en el mensaje que intenta enviar.");
				abrirVentanaAvisos();
			}
		} else {
			ControladorAvisos.setMensajeError("No ha introducido asunto en el mensaje que intenta enviar.");
			abrirVentanaAvisos();
		}
	}

	@FXML
	void pressBtnResponder(ActionEvent event) {
		TitledPane tp = getExpanded();
		if (tp != null) {
			String dni = tp.getId();
			Paciente pac = lectorJson.getPaciente(dni);

			comboBoxElegirDestinatario.setValue(pac.getNombre() + " " + pac.getApellidos());

			JFXTabPaneMensajeria.getSelectionModel().select(2);

		} else {
			ControladorAvisos.setMensajeError("Por favor, seleccione un mensaje.");
			abrirVentanaAvisos();
		}

	}

	@FXML
	void pressBtnVolver(ActionEvent event) throws IOException {
		try {
			System.out.println("Cargando submenu paciente...");
			Parent PacienteVentana = FXMLLoader.load(getClass().getResource("/vista/medico_selector_paciente.fxml"));
			Stage Pacientepp = new Stage();
			Pacientepp.setTitle("Menu Medico - Seleccion Paciente");
			Pacientepp.setScene(new Scene(PacienteVentana));
			Pacientepp.show();
			Pacientepp.setMinHeight(400);
			Pacientepp.setMinWidth(800);

			Stage CerrarVentanaLogin = (Stage) buttonVolver.getScene().getWindow();
			CerrarVentanaLogin.close();
		}

		catch (ControladorExcepciones case1) {
			ControladorAvisos.setMensajeError("No se pudo abrir la ventana de Paciente.");
			case1.abrirVentanaAvisos();
		}
	}

	public class sortByDate implements Comparator<Mensaje> {

		@Override
		public int compare(Mensaje m2, Mensaje m1) {
			return m1.getFecha().compareTo(m2.getFecha());
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

	// Getters y Setters
	public static Medico getMedicoActual() {
		return medicoActual;
	}

	public static void setMedicoActual(Medico MedicoActual) {
		medicoActual = MedicoActual;
	}

	private ArrayList<String> getNombrePacientes() {
		ArrayList<String> pacientes = new ArrayList<String>();
		ArrayList<String> pacientesDnis = medicoActual.getPacientes();

		for (int i = 0; i < pacientesDnis.size(); i++) {

			String nombrePac = lectorJson.getPaciente(pacientesDnis.get(i)).getNombre();
			String apellidosPac = lectorJson.getPaciente(pacientesDnis.get(i)).getApellidos();
			String nombreCompleto = nombrePac + " " + apellidosPac;
			pacientes.add(nombreCompleto);
		}
		return pacientes;
	}

	private Integer getIndiceComboBox(String pac) {
		for (int i = 0; i < getNombrePacientes().size(); i++) {
			if (getNombrePacientes().get(i).equalsIgnoreCase(pac)) {
				return i;
			}
		}
		return null;
	}

	private Integer numeroMensajesRecibidos() {
		Medico m = ControladorMedicopp.getMedicoActual();
		return lectorJson.getMensajesEnviadosA(m.getDni()).size();
	}

	private Integer numeroMensajesEnviados() {
		Medico m = ControladorMedicopp.getMedicoActual();
		return lectorJson.getMensajesEnviadosPor(m.getDni()).size();
	}

	private void enviarMensaje(String dniPac) {
		Mensaje msg = new Mensaje(getMedicoActual().getDni(), dniPac, campoRedactar.getText(), campoAsunto.getText());
		ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();

		mensajes = lectorJson.lectorJsonMensajes();
		mensajes.add(msg);
		escritorJson.escribirEnJsonMensajes(mensajes);

		ControladorAvisos.setMensajeError("Mensaje Enviado.");
		abrirVentanaAvisos();

		AccordionMensajesEnv.getPanes().clear();

		setTitledPanesEnviados();
		campoRedactar.clear();
		campoAsunto.clear();
	}

	private TitledPane getExpanded() {
		ObservableList<TitledPane> panes = AccordionMensajesRec.getPanes();

		for (int i = 0; i < panes.size(); i++) {
			if (panes.get(i).isExpanded()) {
				return panes.get(i);
			}
		}
		return null;
	}

	private void setTitledPanesRecibidos() {
		ArrayList<TitledPane> tpsr = new ArrayList<TitledPane>();

		if (numeroMensajesRecibidos() > 0) {
			for (int i = 0; i < numeroMensajesRecibidos(); i++) {
				ArrayList<Mensaje> mensajesRec = lectorJson
						.getMensajesEnviadosA(ControladorMedicopp.getMedicoActual().getDni());
				List<Mensaje> listMensajesRec = new ArrayList<Mensaje>();
				listMensajesRec.addAll(mensajesRec);
				Collections.sort(listMensajesRec, new sortByDate());
				Mensaje mensajeAct = listMensajesRec.get(i);

				Paciente pacEmisor = lectorJson.getPaciente(mensajeAct.getEmisor());
				Label contenido = new Label(mensajeAct.getMensaje());
				ScrollPane panelContenido = new ScrollPane(contenido);
				contenido.boundsInParentProperty();

				// Label titled pane con asunto fecha y hora
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("De: ");
				Paciente p = lectorJson.getPaciente(mensajeAct.getEmisor());
				stringBuilder.append(p.getNombreCompleto());
				stringBuilder.append("\r");
				stringBuilder.append("Asunto: ");
				stringBuilder.append(mensajeAct.getAsunto());
				stringBuilder.append("\r");
				stringBuilder.append(mensajeAct.getFechaString());

				TitledPane tp = new TitledPane(stringBuilder.toString(), panelContenido);
				tp.setId(pacEmisor.getDni());
				tpsr.add(i, tp);
			}
			AccordionMensajesRec.setLayoutY(60);
			AccordionMensajesRec.setLayoutX(5);
			AccordionMensajesRec.getPanes().addAll(tpsr);
			AnchorPane.setTopAnchor(AccordionMensajesRec, Double.valueOf(30));

		} else {
			Label emptyRec = new Label("No hay mensajes en la bandeja de entrada.");
			emptyRec.setFont(new Font("Arial", 18));
			emptyRec.setLayoutY(60);
			emptyRec.setLayoutX(5);
			anchorPaneRecibidos.getChildren().add(emptyRec);
			AnchorPane.setTopAnchor(emptyRec, Double.valueOf(40));
		}
	}

	private void setTitledPanesEnviados() {
		ArrayList<TitledPane> tpse = new ArrayList<TitledPane>();

		if (numeroMensajesEnviados() > 0) {
			// identificar primero tipo de usuario

			for (int i = 0; i < numeroMensajesEnviados(); i++) {
				ArrayList<Mensaje> mensajesEnv = lectorJson
						.getMensajesEnviadosPor(ControladorMedicopp.getMedicoActual().getDni());
				List<Mensaje> listMensajesEnv = new ArrayList<Mensaje>();
				listMensajesEnv.addAll(mensajesEnv);
				Collections.sort(listMensajesEnv, new sortByDate());
				Mensaje mensajeAct = mensajesEnv.get(i);

				Paciente pacienteReceptor = lectorJson.getPaciente(mensajeAct.getReceptor());
				Label contenido = new Label(mensajeAct.getMensaje());
				ScrollPane panelContenido = new ScrollPane(contenido);
				contenido.minHeight(60);
				contenido.boundsInParentProperty();
				contenido.wrapTextProperty();

				// Label titled pane con asunto fecha y hora
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("A: ");
				Paciente p = lectorJson.getPaciente(mensajeAct.getReceptor());
				stringBuilder.append(p.getNombreCompleto());
				stringBuilder.append("\r");
				stringBuilder.append("Asunto: ");
				stringBuilder.append(mensajeAct.getAsunto());
				stringBuilder.append("\r");
				stringBuilder.append(mensajeAct.getFechaString());

				TitledPane tp = new TitledPane(stringBuilder.toString(), panelContenido);
				tp.setId(pacienteReceptor.getDni());
				tpse.add(i, tp);
			}

			AccordionMensajesEnv.setLayoutY(60);
			AccordionMensajesEnv.setLayoutX(5);
			AccordionMensajesEnv.getPanes().addAll(tpse);
			AnchorPane.setTopAnchor(AccordionMensajesEnv, Double.valueOf(30));
		} else {
			Label emptyEnv = new Label("No hay mensajes enviados.");
			emptyEnv.setFont(new Font("Arial", 18));
			emptyEnv.setLayoutY(60);
			emptyEnv.setLayoutX(5);
			anchorPaneEnviados.getChildren().add(emptyEnv);

			AnchorPane.setTopAnchor(emptyEnv, Double.valueOf(40));
		}

	}

	private void setAvisos() {
		ArrayList<String> pacientes = medicoActual.getPacientes();
		for(int i = 0; i< pacientes.size(); i++) {
			ObservableList<Aviso> avisos = getAvisos(pacientes.get(i));
			Paciente p = lectorJson.getPaciente(pacientes.get(i));
			if(avisos.size()>0) {
				Label tit = new Label("Avisos de "+ p.getNombreCompleto() + ":");
				tit.setFont(new Font("Arial", 18));
				anchorPaneAvisos.getChildren().add(tit);
				
				// Primera columna
				TableColumn<Aviso, String> columnaSensor = new TableColumn<>("Sensor");
				columnaSensor.setMinWidth(200);
				columnaSensor.setCellValueFactory(new PropertyValueFactory<>("nombreSensor"));

				// Segunda columna
				TableColumn<Aviso, String> columnaConcepto = new TableColumn<>("Concepto");
				columnaConcepto.setMinWidth(600);
				columnaConcepto.setCellValueFactory(new PropertyValueFactory<>("concepto"));

				TableView<Aviso> table;

				table = new TableView<>();
				//table.setLayoutX(5);
				//table.setLayoutY(60);
				table.setItems(avisos);
				table.getColumns().addAll(columnaConcepto,columnaSensor);
				anchorPaneAvisos.getChildren().add(table);
//				anchorPaneAvisos.setTopAnchor(table, 0.0);
			}
		}
	}

	public ObservableList<Aviso> getAvisos(String dni) {
		ObservableList<Aviso> avisos = FXCollections.observableArrayList();
		avisos.addAll(lectorJson.crearAvisosSensor1(dni));
		avisos.addAll(lectorJson.crearAvisosSensor2(dni));
		avisos.addAll(lectorJson.crearAvisosSensor3(dni));

		return avisos;
	}
}
