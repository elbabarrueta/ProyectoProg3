package ventanas;

import java.io.IOException;


import java.util.Map;

import javax.swing.JFrame;

import BasesDeDatos.BaseDeDatos;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Handler;
import java.util.logging.FileHandler;
import java.util.logging.StreamHandler;
import java.util.logging.SimpleFormatter;
import clases.Usuario;
import datos.DataSetUsuario;

public class Main {
	
    private static Logger logger = Logger.getLogger(Main.class.getName());

	private static DataSetUsuario dataset;
	private static VentanaInicio ventana1;
	public static void main(String[] args) {

		ventana1 = new VentanaInicio();
		ventana1.setVisible(true);
		cargaUsuarios();
		VentanaInicio.cargarUsuariosInicio(dataset);
		
// visualizar usuarios dentro de la base de datos		
		BaseDeDatos.main(null);
		BaseDeDatos base = new BaseDeDatos();
		base.verUsuarios();
//	
		
		logger.setLevel(Level.ALL);
		try {
		    Handler consoleHandler = new StreamHandler(System.out, new SimpleFormatter());
		    consoleHandler.setLevel(Level.FINEST);

		    Handler fileHandler = new FileHandler("Main.log.xml");
		    fileHandler.setLevel(Level.ALL);

		    logger.addHandler(consoleHandler);
		    logger.addHandler(fileHandler);
		} catch (Exception e) {
		    logger.log(Level.SEVERE, e.toString(), e);
		}
		
	}
	private static void cargaUsuarios() {
		try {
			dataset = new DataSetUsuario( "usuariosBase.txt" );
			System.out.println( "Cargados usuarios:" );
			for (Map.Entry<String, Usuario> entry : dataset.getMapaUsu().entrySet()) {
	            Usuario usu = entry.getValue();
	            System.out.println( " \t" + usu);
	        }	
			
		} catch (IOException e) {
			System.err.println( "Error en carga de usuarios" );
		}
	}
	
	public static void setVentanaInicio(VentanaInicio v) {
		ventana1 = v;
	}
	public static VentanaInicio getVentanaInicio() {
        return ventana1;
    }
	
}
	
