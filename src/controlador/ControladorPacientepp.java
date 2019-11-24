package controlador;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import modelo.Paciente;


public class ControladorPacientepp implements Initializable {

    @FXML
    private JFXButton btnAvisos;

    @FXML
    private JFXButton btnEjercicios;

    @FXML
    private JFXButton btnMensajes;
    
    @FXML
    private Label campoPaciente;

    private static Paciente pacienteActual = new Paciente();
    
    
    @Override
    public void initialize(URL location, ResourceBundle reosurces) {
    //add controlador para tomar solo el nombre(comprobar si cada caracter es un espacio y cuando lo sea cortar el string ahi).
    	System.out.println("entrado");
    	campoPaciente.setText("Hola " +ControladorPacientepp.getPacienteActual().getNombre() +",");
    	System.out.println("nombre cargado");
	}
    
    @FXML
    void pressBtnMensajes(ActionEvent event)  {
    	try {
        	System.out.println("Cargando ventana de Mensajes...");
    		Parent Mensajeria = FXMLLoader.load(getClass().getResource("/vista/pacientepp_mensajeria.fxml"));
    		Stage MensajeriaPaciente = new Stage();
    		MensajeriaPaciente.setTitle("Mensajeria Paciente");
    		MensajeriaPaciente.setScene(new Scene(Mensajeria));
    		MensajeriaPaciente.show();
    		MensajeriaPaciente.setMinHeight(350);
    		MensajeriaPaciente.setMinWidth(500);
        	}
        	catch(Exception r){
        		ControladorAvisos.setMensajeError("No se pudo abrir la ventana de Mensajeria para Pacientes.");
        	}
    }
    
  //Getters y Setters
    public static Paciente getPacienteActual() {
  		return pacienteActual;
  	}

	public static void setPacienteActual(Paciente PacienteActual) {
  		pacienteActual = PacienteActual;
  	}
	

}
