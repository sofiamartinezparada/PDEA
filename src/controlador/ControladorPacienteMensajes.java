package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Accordion;
import javafx.scene.text.Font;
import modelo.Medico;
import modelo.Mensaje;
import modelo.Paciente;

public class ControladorPacienteMensajes implements Initializable{

	
	@FXML
	private JFXButton btnEnviar; 
	
	@FXML
	private JFXButton btnVolver;
	
	@FXML
    private Label campoPaciente;
	
	@FXML
	private Tab tabRecibidos;
	
	@FXML
	private TitledPane structMensajeRecib;
	
	@FXML
	private Label textoMensajeRecib;
	
	@FXML
	private Tab tabEnviados;
	
	@FXML
	private TitledPane structMensajeEnv;
	
	@FXML
	private Label textoMensajeEnv;
	
	@FXML
	private TextArea campoEscritura;
	
	@FXML
    private Accordion AccordionMensajesRec;
	
	@FXML
    private Accordion AccordionMensajesEnv;
	
    @FXML
    private AnchorPane anchorPaneRecibidos;

    @FXML
    private AnchorPane anchorPaneEnviados;

		
	
	private static Paciente pacienteActual = new Paciente();
	
	
	@Override
	public void initialize(URL location, ResourceBundle reosurces) {
		Paciente p = ControladorPacientepp.getPacienteActual();
		campoPaciente.setText("Hola " +p.getNombre()+",");
		
		ArrayList<TitledPane> tps = new ArrayList<TitledPane>();
		
		if (numeroMensajesRecibidos() != 0) {
		
			for (int i = 0; i < numeroMensajesRecibidos(); i++) {
				ArrayList<Mensaje> mensajesRec  = lectorJson.getMensajesEnviadosA(p.getDni());
				Mensaje mensajeAct = mensajesRec.get(i);
				Medico medEmisor = lectorJson.getMedico(mensajeAct.getEmisor());
				Label contenido = new Label(mensajeAct.getMensaje());
				TitledPane tp = new TitledPane("De: " + medEmisor.getNombre() , contenido) ;
						
				tps.add(i, tp);
			}
			AccordionMensajesRec.getPanes().addAll(tps);
			AccordionMensajesRec.setLayoutY(60);
			AccordionMensajesRec.setLayoutX(5);
			AccordionMensajesRec.setMinHeight(100);
		}
		else {
			Label emptyRec = new Label("No hay mensajes en la bandeja de entrada.");
			emptyRec.setFont(new Font("Arial", 18));
			emptyRec.setLayoutY(60);
			emptyRec.setLayoutX(5);
			anchorPaneRecibidos.getChildren().add(emptyRec);
		}
		if (numeroMensajesEnviados() != 0) {
			
			for (int i = 0; i < numeroMensajesEnviados(); i++) {
				ArrayList<Mensaje> mensajesEnv  = lectorJson.getMensajesEnviadosPor(p.getDni());
				Mensaje mensajeAct = mensajesEnv.get(i);
				Medico medReceptor = lectorJson.getMedico(mensajeAct.getReceptor());
				Label contenido = new Label(mensajeAct.getMensaje());
				ScrollPane panelContenido = new ScrollPane(contenido);
				panelContenido.setMinHeight(50);
				contenido.boundsInParentProperty();
				TitledPane tp = new TitledPane("Para: " + medReceptor.getNombre() , panelContenido) ;
				tps.add(i, tp);
			}
			AccordionMensajesEnv.getPanes().addAll(tps);
			AccordionMensajesEnv.setLayoutY(60);
			AccordionMensajesEnv.setLayoutX(5);
			AccordionMensajesEnv.setMinHeight(100);
			
		}
		else {
			Label emptyEnv = new Label("No hay mensajes enviados.");
			emptyEnv.setFont(new Font("Arial", 18));
			emptyEnv.setLayoutY(60);
			emptyEnv.setLayoutX(5);
			anchorPaneEnviados.getChildren().add(emptyEnv);
		}
	}

	@FXML
	void pressBtnEnviar(ActionEvent event) {
		try {
			Paciente p = ControladorPacientepp.getPacienteActual();
			String medPac = p.getMedico();
			Mensaje msg = new Mensaje(p.getDni(), medPac, campoEscritura.getText());
			System.out.println("El mensaje ha sido creado");
			ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
			mensajes= lectorJson.lectorJsonMensajes();
			mensajes.add(msg);
			escritorJson.escribirEnJsonMensajes(mensajes);
		
			ControladorAvisos.setMensajeError("Mensaje Enviado.");
			abrirVentanaAvisos();
		}
		catch(Exception a) {
			ControladorAvisos.setMensajeError("Error enviando el mensaje.");
			abrirVentanaAvisos();
		}
	}
	
    
	@FXML
	void pressBtnVolver(ActionEvent event) throws IOException {
		try {
			System.out.println("Cargando ventana principal de Paciente...");
			Parent PacienteVentana = FXMLLoader.load(getClass().getResource("/vista/menupaciente.fxml"));
			Stage Pacientepp = new Stage();
			Pacientepp.setTitle("Menu Principal Paciente");
			Pacientepp.setScene(new Scene(PacienteVentana));
			Pacientepp.show();
			Pacientepp.setMinHeight(550);
			Pacientepp.setMinWidth(500);

			System.out.println("Cerrando ventana de Login.");
			Stage CerrarVentanaLogin = (Stage) btnVolver.getScene().getWindow();
			CerrarVentanaLogin.close();
		}	
		
		catch(ControladorExcepciones case1){
			ControladorAvisos.setMensajeError("No se pudo abrir la ventana de Paciente.");
			case1.abrirVentanaAvisos();
		}
	}
	 
	//GETTER
	public static Paciente getPacienteActual() {
		return pacienteActual;
	}
	 
	//SETTER
	public static void setPacienteActual(Paciente pacienteActual) {
		ControladorPacienteMensajes.pacienteActual = pacienteActual;
	}
	
	//METODOS
	public Integer numeroMensajesRecibidos() {
		Paciente p = ControladorPacientepp.getPacienteActual();
		return lectorJson.getMensajesEnviadosA(p.getDni()).size();
	}
	
	public Integer numeroMensajesEnviados() {
		Paciente p = ControladorPacientepp.getPacienteActual();
		return lectorJson.getMensajesEnviadosPor(p.getDni()).size();
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

		}
		catch(Exception a) {
			System.out.println("Error");
		}
	}
}
