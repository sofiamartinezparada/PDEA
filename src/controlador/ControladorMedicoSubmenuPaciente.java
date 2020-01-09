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

import com.jfoenix.controls.JFXButton;

import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import modelo.Cuidador;
import modelo.Medico;
import modelo.Mensaje;
import modelo.Paciente;




public class ControladorMedicoSubmenuPaciente implements Initializable {
	   @FXML
	    private Label campoMedico;

	    @FXML
	    private JFXTextField campoDniPac;

	    @FXML
	    private JFXTextField campoNombrePac;

	    @FXML
	    private JFXTextField campoApellidosPac;

	    @FXML
	    private JFXTextField campoFechaNacPac;

	    @FXML
	    private JFXTextField campoTlfPac;

	    @FXML
	    private JFXTextField campoCuidadoresPac;

	    @FXML
	    private JFXButton buttonEditar;

	    @FXML
	    private JFXButton buttonConsultarSensores;

	    @FXML
	    private JFXButton buttonCitarPac;

	    @FXML
	    private JFXButton buttonConsultarEjerciciosPac;

	    @FXML
	    private JFXTabPane JFXTabPaneMensajeria;

	    @FXML
	    private Tab tabRecibidos;

	    @FXML
	    private AnchorPane anchorPaneRecibidos;

	    @FXML
	    private JFXButton btnResponder;

	    @FXML
	    private Accordion AccordionMensajesRec;

	    @FXML
	    private Label labelBandejaEntrada;

	    @FXML
	    private Tab tabEnviados;

	    @FXML
	    private AnchorPane anchorPaneEnviados;

	    @FXML
	    private Accordion AccordionMensajesEnv;

	    @FXML
	    private Label labelBandejaSalida;

	    @FXML
	    private TextArea campoRedactar;

	    @FXML
	    private JFXButton btnConfirmarEnvio;
	    
	    @FXML
	    private JFXButton buttonVolver;

	    @FXML
	    private JFXTextField campoAsunto;

	    @FXML
	    private Label labelRedactar;
	    
    private static Paciente pacienteActual = new Paciente();
    
    private static Medico medicoActual = new Medico();
    
    private static boolean editable = false;

    
    @Override
    public void initialize(URL location, ResourceBundle reosurces) {
    	System.out.println(medicoActual.getNombre());
    	System.out.println(pacienteActual.getNombre());
    	campoMedico.setText("Hola " +ControladorMedicopp.getMedicoActual().getNombre()+",");
    	
    	setCamposDatos();
    	
    	campoNombrePac.setEditable(editable);
    	campoApellidosPac.setEditable(editable);
    	campoFechaNacPac.setEditable(editable);
    	campoTlfPac.setEditable(editable);
    	campoCuidadoresPac.setEditable(editable);
    	
    	setTitledPanesEnviados();
    	setTitledPanesRecibidos();
    }
    
    @FXML
    void pressBtnConfirmarEnvio(ActionEvent event) {
        	
	    	if(campoRedactar.getText().length()>0) {
	    		try {
	    			String pac = pacienteActual.getDni();
		    		enviarMensaje(pac);
	    		}
	    		catch(Exception a) {
	    			ControladorAvisos.setMensajeError("Error enviando el mensaje.");
					abrirVentanaAvisos();
	    		}
	    	}
	    	else{
				ControladorAvisos.setMensajeError("No ha introducido texto alguno en el mensaje que intenta enviar.");
				abrirVentanaAvisos();
			}
    }
    
    @FXML
    @SuppressWarnings("deprecation")
    void pressBtnEditar(ActionEvent event) {
    	editable = !editable;
    	//los campos pasan de ser no editables a editables y viceversa
    	campoNombrePac.setEditable(editable);
    	campoApellidosPac.setEditable(editable);
    	campoFechaNacPac.setEditable(editable);
    	campoTlfPac.setEditable(editable);
    	campoCuidadoresPac.setEditable(editable);
    	
    	//el boton editar pasa a mostrar el texto "Confirmar cambios"
    	if(editable==true) {
    		buttonEditar.setText("Confirmar cambios");
    	}
    	else {
    		buttonEditar.setText("Editar Datos Paciente");
    	}
    	
    	//el resto de botones quedan deshabilitados hasta que se confirmen los datos
    	buttonConsultarSensores.setDisable(editable);
    	buttonCitarPac.setDisable(editable);
    	buttonConsultarEjerciciosPac.setDisable(editable);
    	
    	//guardar los datos introducidos en los json
    	Paciente p = pacienteActual;
    	//campo Nombre
    	p.setNombre(campoNombrePac.getText());
    	//campo Apellidos
    	p.setApellidos(campoApellidosPac.getText());
    	//campo Fecha de Nacimiento
    	String FechaNacNew[] = campoFechaNacPac.getText().split("/");
    	List<String> Fecha = Arrays.asList(FechaNacNew);
    	int dia  = Integer.parseInt(Fecha.get(0));
    	int mes  = Integer.parseInt(Fecha.get(1));
    	int anho = Integer.parseInt(Fecha.get(2));
    	Date calend= new Date(anho, mes, dia);
    	p.setFecha_nacimiento(calend);
    	//campo telefono
    	int TlfPacNew = Integer.parseInt(campoTlfPac.getText());
    	p.setTelefono(TlfPacNew);
    	
    	
    	//campo cuidadores
    	String CuidadoresNew[] = campoCuidadoresPac.getText().split(",");
    	List<String> CuidadoresPac = Arrays.asList(CuidadoresNew);
    	
    	ArrayList<Cuidador> cuidadores = new ArrayList<Cuidador>();
		cuidadores = lectorJson.lectorJsonCuidadores();
		
		ArrayList<String> asdf = new ArrayList<>();
		//comprobar que el nombre coincide con un cuidadore existente
    	for(int i=0; i<CuidadoresPac.size(); i++) {
    		String cuidadorComparado = CuidadoresPac.get(i);
    		for(int a=0; a<cuidadores.size(); a++) {
				if(cuidadorComparado.equalsIgnoreCase(cuidadores.get(a).getNombreCompleto())) {
					cuidadorComparado = cuidadores.get(a).getDni();
					asdf.add(cuidadorComparado);
				}
    		}
    	}
    	p.setCuidadores(asdf);
    	
    	escritorJson.modificarPaciente(p);
		
		
	
    }
    
    @FXML
    void pressBtnConsultarSensores(ActionEvent event) throws IOException {
    	try {
			
			Parent SensoresPaciente = FXMLLoader.load(getClass().getResource("/vista/medicopp_sensores_paciente.fxml"));
			Stage MenuSensoresPaciente = new Stage();
			MenuSensoresPaciente.setTitle("Menu Sensores Paciente");
			MenuSensoresPaciente.setScene(new Scene(SensoresPaciente));
			MenuSensoresPaciente.show();
			MenuSensoresPaciente.setMinHeight(620);
			MenuSensoresPaciente.setMinWidth(600);

			System.out.println("Cerrando ventana de Login.");
			Stage CerrarVentanaLogin = (Stage) buttonConsultarSensores.getScene().getWindow();
			CerrarVentanaLogin.close();
		}	
		
		catch(ControladorExcepciones case1){
			ControladorAvisos.setMensajeError("No se pudo abrir el menu de sensores de Paciente.");
			case1.abrirVentanaAvisos();
		}
    }
    
    @FXML
    void pressBtnResponder(ActionEvent event) {
    	JFXTabPaneMensajeria.getSelectionModel().select(2);
    }
    @FXML
    void pressBtnCitarPac(ActionEvent event) {

    }
    @FXML
    void pressBtnConsultarEjerciciosPac(ActionEvent event) {

    }
    
	@FXML
	void pressBtnVolver(ActionEvent event) throws IOException {
		try {
			System.out.println("Cargando selector paciente...");
			Parent PacienteVentana = FXMLLoader.load(getClass().getResource("/vista/medico_selector_paciente.fxml"));
			Stage Pacientepp = new Stage();
			Pacientepp.setTitle("Menu Medico - Seleccion Paciente");
			Pacientepp.setScene(new Scene(PacienteVentana));
			Pacientepp.show();
			Pacientepp.setMinHeight(400);
			Pacientepp.setMinWidth(800);

			Stage CerrarSubmenuPaciente = (Stage) buttonVolver.getScene().getWindow();
			CerrarSubmenuPaciente.close();
		}	
			
		catch(ControladorExcepciones case1){
			ControladorAvisos.setMensajeError("No se pudo abrir el submenu de Paciente.");
			case1.abrirVentanaAvisos();
		}
	}
    
    public static Paciente getPacienteActual() {
		return pacienteActual;
	}
    public static void setPacienteActual(Paciente p) {
		pacienteActual = p;
	}
    
    public static Medico getMedicoActual() {
    	return medicoActual;
    }
    public static void setMedicoActual(Medico m) {
    	medicoActual = m;
    }
    
  //DATOS
    private void setCamposDatos() {
    	Paciente p = pacienteActual;
    	campoDniPac.setText(p.getDni());
    	campoNombrePac.setText(p.getNombre());
    	campoApellidosPac.setText(p.getApellidos());
    	campoFechaNacPac.setText(p.getFechaNacimientoString());
    	campoTlfPac.setText(p.getTelefono().toString());
    	campoCuidadoresPac.setText(getCampoCuidadores());
    }
    
    
    private String getCampoCuidadores() {
    	Paciente p = pacienteActual;
    	StringBuilder stringBuilder = new StringBuilder();
		Cuidador c = new Cuidador();
		int i = 0 ;
		if (p.getCuidadores().size()==1) {
			c = lectorJson.getCuidador(p.getCuidadores().get(0));
	    	stringBuilder.append(c.getNombre() + " " + c.getApellidos());
	    	return stringBuilder.toString();
		}else if (p.getCuidadores().size()==0){
			stringBuilder.append("Este paciente no tiene cuidadores.");
			return stringBuilder.toString();
		}
		else {
		
	    	for (i = 0 ; i < (p.getCuidadores().size()-1); i++) {
	    		c = lectorJson.getCuidador(p.getCuidadores().get(i));
	    		stringBuilder.append(c.getNombre() + " " + c.getApellidos() + ", ");
	    	}
	    	c = lectorJson.getCuidador(p.getCuidadores().get(i));
	    	stringBuilder.append(c.getNombre() + " " + c.getApellidos());
	    	return stringBuilder.toString();
		}
    }

    
    private void enviarMensaje(String dniPac) {
    	Mensaje msg = new Mensaje(getMedicoActual().getDni(), dniPac, campoRedactar.getText(), campoAsunto.getText());
		ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
		
		mensajes= lectorJson.lectorJsonMensajes();
		mensajes.add(msg);
		escritorJson.escribirEnJsonMensajes(mensajes);

		ControladorAvisos.setMensajeError("Mensaje Enviado.");
		abrirVentanaAvisos();

		AccordionMensajesEnv.getPanes().clear();
		
		setTitledPanesEnviados();
		campoRedactar.clear();
		campoAsunto.clear();
    }
    
    public void setTitledPanesRecibidos() {
    	ArrayList<TitledPane> tpsr = new ArrayList<TitledPane>();

		if(numeroMensajesRecibidos()>0) {
    		for(int i=0; i<numeroMensajesRecibidos(); i++) {
    			ArrayList<Mensaje> mensajesRec =lectorJson.getMensajesA(medicoActual.getDni(), pacienteActual.getDni());
    			List<Mensaje> listMensajesRec = new ArrayList<Mensaje>();
				listMensajesRec.addAll(mensajesRec);
				Collections.sort(listMensajesRec, new sortByDate());
    			Mensaje mensajeAct = listMensajesRec.get(i);
    			
    			Paciente pacEmisor = lectorJson.getPaciente(mensajeAct.getEmisor());
    			Label contenido = new Label(mensajeAct.getMensaje());
				ScrollPane panelContenido = new ScrollPane(contenido);
				contenido.boundsInParentProperty();
				
				//Label titled pane con asunto fecha y hora
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("Asunto: ");
				stringBuilder.append(mensajeAct.getAsunto());
				stringBuilder.append("\r");
				stringBuilder.append(mensajeAct.getFechaString());
				
				TitledPane tp = new TitledPane(stringBuilder.toString(), panelContenido) ;
				tp.setId(pacEmisor.getDni());
				tpsr.add(i, tp);
    		}
    		AccordionMensajesRec.setLayoutY(60);
			AccordionMensajesRec.setLayoutX(5);
			AccordionMensajesRec.getPanes().addAll(tpsr);
			AnchorPane.setTopAnchor(AccordionMensajesRec, Double.valueOf(30));

    	}
    	else {
    		Label emptyRec = new Label("No hay mensajes en la bandeja de entrada.");
			emptyRec.setFont(new Font("Arial", 18));
			emptyRec.setLayoutY(60);
			emptyRec.setLayoutX(5);
			anchorPaneRecibidos.getChildren().add(emptyRec);
			AnchorPane.setTopAnchor(emptyRec, Double.valueOf(40));
    	}
	}

	public void setTitledPanesEnviados() {
		ArrayList<TitledPane> tpse = new ArrayList<TitledPane>();

    	if (numeroMensajesEnviados() > 0) {
			//identificar primero tipo de usuario

			for (int i = 0; i < numeroMensajesEnviados(); i++) {
				ArrayList<Mensaje> mensajesEnv  = lectorJson.getMensajesA(medicoActual.getDni(), pacienteActual.getDni());
				List<Mensaje> listMensajesEnv = new ArrayList<Mensaje>();
				listMensajesEnv.addAll(mensajesEnv);
				Collections.sort(listMensajesEnv, new sortByDate());
				Mensaje mensajeAct = mensajesEnv.get(i);
				
				Label contenido = new Label(mensajeAct.getMensaje());
				ScrollPane panelContenido = new ScrollPane(contenido);
				contenido.minHeight(60);
				contenido.boundsInParentProperty();
				contenido.wrapTextProperty();
				
				//Label titled pane con asunto fecha y hora
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("Asunto: ");
				stringBuilder.append(mensajeAct.getAsunto());
				stringBuilder.append("\r");
				stringBuilder.append(mensajeAct.getFechaString());
				
				TitledPane tp = new TitledPane(stringBuilder.toString(), panelContenido) ;
				tp.setId(pacienteActual.getDni());
				tpse.add(i, tp);
			}


			AccordionMensajesEnv.setLayoutY(60);
			AccordionMensajesEnv.setLayoutX(5);
			AccordionMensajesEnv.getPanes().addAll(tpse);
			AnchorPane.setTopAnchor(AccordionMensajesEnv, Double.valueOf(30));
		}
		else {
			Label emptyEnv = new Label("No hay mensajes enviados.");
			emptyEnv.setFont(new Font("Arial", 18));
			emptyEnv.setLayoutY(60);
			emptyEnv.setLayoutX(5);
			anchorPaneEnviados.getChildren().add(emptyEnv);

			AnchorPane.setTopAnchor(emptyEnv, Double.valueOf(40));
		}
	}
	
	

	public Integer numeroMensajesRecibidos() {
		Medico m = ControladorMedicopp.getMedicoActual();
		return lectorJson.getMensajesA(m.getDni(), pacienteActual.getDni()).size();
	}

	public Integer numeroMensajesEnviados() {
		Medico m = ControladorMedicopp.getMedicoActual();
		return lectorJson.getMensajesA(pacienteActual.getDni(), m.getDni()).size();
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

		}
		catch(Exception a) {
			System.out.println("Error");
		}
	}
}