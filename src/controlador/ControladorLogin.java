package controlador;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.stage.Stage;
import modelo.Cuidador;
import modelo.Medico;
import modelo.Paciente;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.scene.layout.BorderPane;


public class ControladorLogin implements Initializable{

    @FXML
    private BorderPane BorderPaneGlobal;
    
	@FXML
	private Button myButton;
	
	@FXML
	private Label txtBienvenida;
	
	@FXML
	private Separator separatorTop;
	
	@FXML
	private ImageView logo;
	
	@FXML
	private GridPane gridPanelLogin;
	
	@FXML
    private JFXTextField txtInputUsuario;
	
	@FXML
    private  JFXPasswordField txtInputPassword;
	
	@FXML
	private JFXButton buttonAceptar;
	
	@FXML
	private JFXButton buttonCancelar;
	
    @FXML
    private JFXButton buttonRegistrar;
    
    @FXML
    void pressBtnRegistrar(ActionEvent event)  {
    	try {
    	System.out.println("Cargando ventana de Registro...");
		ControladorPacientepp.setPacienteActual(lectorJson.getPaciente(txtInputUsuario.getText().toUpperCase()));
		Parent NuevoRegistro = FXMLLoader.load(getClass().getResource("/vista/registro.fxml"));
		Stage Registro = new Stage();
		Registro.setTitle("Registro de Nuevo Usuario");
		Registro.setScene(new Scene(NuevoRegistro));
		Registro.show();
		Registro.setMinHeight(350);
		Registro.setMinWidth(500);
    	}
    	catch(Exception r){
    		ControladorAvisos.setMensajeError("No se pudo abrir la ventana de Registro.");
		
    	}
    }
		

	@Override
	public void initialize(URL location, ResourceBundle reosurces) {
		
	}
	
	
	
	//Acciones ejecutadas al pulsar el Boton Aceptar
	public void pressBtnAceptar() throws IOException {
		
		try {
			
			//comprobar tipo de usuario, usamos switch para optimizar programa
			int usertype=0;
			if (esPaciente()) {
				usertype=1;
			}
			if (esCuidador()) {
				usertype=2;
			}
			if (esMedico()) {
				usertype=3;
			}
			
			switch (usertype) {
					
				case 1:
					try {
						System.out.println("Cargando ventana principal de Paciente...");
						ControladorPacientepp.setPacienteActual(lectorJson.getPaciente(txtInputUsuario.getText().toUpperCase()));
						Parent PacienteVentana = FXMLLoader.load(getClass().getResource("/vista/menupaciente.fxml"));
						Stage Pacientepp = new Stage();
						Pacientepp.setTitle("Menu Principal Paciente");
						Pacientepp.setScene(new Scene(PacienteVentana));
						Pacientepp.show();
						Pacientepp.setMinHeight(550);
						Pacientepp.setMinWidth(500);

						System.out.println("Cerrando ventana de Login.");
						Stage CerrarVentanaLogin = (Stage) buttonAceptar.getScene().getWindow();
						CerrarVentanaLogin.close();
					}	
					
					catch(ExcepcionUser case1){
						ControladorAvisos.setMensajeError("No se pudo abrir la ventana de Paciente.");
						case1.abrirVentanaAvisos();
					}
					break;
					
				case 2:
					try {
						System.out.println("Cargando ventana principal de Cuidador...");
						ControladorCuidadorpp.setCuidadorActual(lectorJson.getCuidador(txtInputUsuario.getText().toUpperCase()));
						Parent CuidadorVentana = FXMLLoader.load(getClass().getResource("/vista/cuidadorpp.fxml"));
						Stage Cuidadorpp = new Stage();
						Cuidadorpp.setTitle("Menu Principal Cuidador");
						Cuidadorpp.setScene(new Scene(CuidadorVentana));
						Cuidadorpp.show();
						Cuidadorpp.setMinHeight(360);
						Cuidadorpp.setMinWidth(500);

						System.out.println("Cerrando ventana de Login.");
						Stage CerrarVentanaLogin = (Stage) buttonAceptar.getScene().getWindow();
						CerrarVentanaLogin.close();	
					}
					
					catch(ExcepcionUser case2){
						ControladorAvisos.setMensajeError("No se pudo abrir la ventana de Cuidador.");
						case2.abrirVentanaAvisos();
					}
					break;
					
				case 3:
					try {
						System.out.println("Cargando ventana principal de Medico...");
						ControladorMedicopp.setMedicoActual(lectorJson.getMedico(txtInputUsuario.getText().toUpperCase()));
						Parent MedicoVentana = FXMLLoader.load(getClass().getResource("/vista/medicopp.fxml"));
						Stage Medicopp = new Stage();
						Medicopp.setTitle("Menu Principal Medico");
						Medicopp.setScene(new Scene(MedicoVentana));
						Medicopp.show();
						Medicopp.setMinHeight(600);
						Medicopp.setMinWidth(740);
						
						System.out.println("Cerrando ventana de Login.");
						Stage CerrarVentanaLogin = (Stage) buttonAceptar.getScene().getWindow();
						CerrarVentanaLogin.close();
						
					}
					
					catch(ExcepcionUser case3){
						ControladorAvisos.setMensajeError("No se pudo abrir la ventana de Medico.");
						case3.abrirVentanaAvisos();
					}
					break;
					
				default:
					// en caso de que se introduzca un DNI que no se encuentre en la base de datos
					ControladorAvisos.setMensajeError("Datos incorrectos.");
					throw new ExcepcionUser();
					
	
			}	
		}

		catch(ExcepcionUser loginfailure){
			if (!comprobarInputUser() ) {
				ControladorAvisos.setMensajeError("El usuario debe estar compuesto por 8 digitos y una letra.");
			}
			else {
				ControladorAvisos.setMensajeError("Datos incorrectos.");
			}
			loginfailure.abrirVentanaAvisos();
		}
		
	}
	
	public void btnAceptarActionPerformed(ActionEvent event) throws IOException {
		try {
			this.pressBtnAceptar();
		} catch (ExcepcionUser e) {
			ControladorAvisos.setMensajeError("El usuario debe estar compuesto por 8 digitos y una letra.");
			e.abrirVentanaAvisos();
		}
	}
	
	public void textoClavePressed(KeyEvent event) throws IOException{
		if(event.getCode().equals(KeyCode.ENTER)) {
			try {
				this.pressBtnAceptar();
			} catch (ExcepcionUser e) {
				ControladorAvisos.setMensajeError("Datos incorrectos.");
				e.abrirVentanaAvisos();
			}
		}
		
	}
	
	//----------------------------------------------
	
	
	// Acciones ejecutadas al pulsar el Boton Cancelar
	public void pressBtnCancelar(ActionEvent event) {
		System.out.println("Saliendo de la aplicacion. Hasta pronto.");
		System.exit(0);
	}	
	//------------------------------------------------
	
	
	// Funciones donde se comprueba que los datos introducidos son correctos
	public boolean comprobarInputUser() {		
		// capturar texto  del TextField de usuario y convierte a mayus.
		String inputUsuario = txtInputUsuario.getText().toUpperCase();
		System.out.println("Usuario introducido: "+inputUsuario);
		System.out.println("Password introducido: "+txtInputPassword.getText());
		
		// comprueba la longitud del string inputUser, que los primeros 8 caracteres son numeros y que el ultimo caracter es una letra
		if(inputUsuario.length()!=9 || comprobarDigitosDNI() == false || Character.isLetter(inputUsuario.charAt(8)) == false ) {
			System.out.println("Usuario no valido.");
			return false;
		}
		

		else {
			System.out.println("Usuario valido.");
			return true;
		}	
	}
	
	// comprobacion de que los primeros 8 caracteres introducidos en el campo de ussuario son digitos 
	public boolean comprobarDigitosDNI() {
		String inputUser = txtInputUsuario.getText().toUpperCase();
		for(int i=0; i<inputUser.length()-1; i++) {
			if(Character.isDigit((inputUser.charAt(i)))==false) {
					return false;
			}
		}
		return true;
	}
	//------------------------------------------------------

	// funciones para comprobar el tipo de usuario
	public boolean esPaciente() {
		ArrayList<Paciente> pacientes= lectorJson.lectorJsonPacientes();
		for (int i=0; i< pacientes.size(); i++) {
			Paciente p = pacientes.get(i);
			String passwordPaciente = p.getContrasena();
			String passwordEncriptada = getMd5(txtInputPassword.getText());
			if(p.getDni().equalsIgnoreCase(txtInputUsuario.getText()) && passwordPaciente.equals(passwordEncriptada) ) {
				return true;
			}
		}
		return false;	
	}
	

	public boolean esCuidador() {
		ArrayList<Cuidador> cuidadores= lectorJson.lectorJsonCuidadores();

		for (int i=0; i< cuidadores.size(); i++) {
			Cuidador p = cuidadores.get(i);
			String passwordCuidador = p.getContrasena();
			String passwordEncriptada = getMd5(txtInputPassword.getText());
			if(p.getDni().equalsIgnoreCase(txtInputUsuario.getText()) && passwordCuidador.equals(passwordEncriptada) ){
				return true;
			}
		}
		return false;	
	}
	
	
	public boolean esMedico() {
		ArrayList<Medico> medicos= lectorJson.lectorJsonMedicos();

		for (int i=0; i< medicos.size(); i++) {
			Medico p = medicos.get(i);
			String passwordMedico = p.getContrasena();
			String passwordEncriptada = getMd5(txtInputPassword.getText());
			if(p.getDni().equalsIgnoreCase(txtInputUsuario.getText()) && passwordMedico.equals(passwordEncriptada) ){
				return true;
			}
		}
		return false;	
	}
	

	
	// funcion para declarar excepciones propias
	public class ExcepcionUser extends IOException {
		
		private static final long serialVersionUID = 2918227521048319923L;
		public ExcepcionUser(){};
		

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
			catch(IOException a) {
				System.out.println("Error");
				 a.printStackTrace();
			}
		}
	}
	//-----------------------------------------
	
	//funcion hash recibe contrasena y devuelve contrasena encriptado
	public static String getMd5(String input) 
    { 
        try { 
  
            // Static getInstance method is called with hashing MD5 
            MessageDigest md = MessageDigest.getInstance("MD5"); 
  
            // digest() method is called to calculate message digest 
            //  of an input digest() return array of byte 
            byte[] messageDigest = md.digest(input.getBytes()); 
  
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 
  
            // Convert message digest into hex value 
            String hashtext = no.toString(16); 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
            
            	
            
            return hashtext; 
        }  
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        } 
    } 
	//-----------------------------------------
        
	//-----------------------------------------
	
}