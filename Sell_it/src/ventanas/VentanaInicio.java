package ventanas;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import clases.Usuario;

public class VentanaInicio extends JFrame {
	
	/**
	 * 
	 */
	private DataSetUsuario dataSetUsuario;
	
	private static final long serialVersionUID = 1L;

	public VentanaInicio() {
		super();
		
		//Características de la ventana principal
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setBounds(500,300, 400, 400);
		this.setTitle("Sell-It");
		
		//Ceracion de paneles 
		JPanel panelVentanaInicio = new JPanel(new BorderLayout());
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel panelNorte = new JPanel(new BorderLayout());
		JPanel panelCentro = new JPanel(new GridLayout(3,2));
		
		panelVentanaInicio.add(panelSur, BorderLayout.SOUTH);
		panelVentanaInicio.add(panelNorte, BorderLayout.NORTH);
		panelVentanaInicio.add(panelCentro,BorderLayout.CENTER);
		
		panelNorte.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelSur.setLayout(new FlowLayout(FlowLayout.CENTER));

		
		this.getContentPane().add(panelVentanaInicio);

		//Creacion de los JTextFields, JLabels, JButtons y JPasswordField
		
		JTextField txtUsuario = new JTextField();
		JPasswordField txtContrasenia = new JPasswordField();
		JLabel etiquetaUsuario = new JLabel("Usuario:");
		JLabel etiquetaContrasenia = new JLabel("Contraseña:");

		
		JButton botonRegistroEntidad = new JButton("Registro Entidad");
		JButton botonRegistroUsuario = new JButton("Registro Usuario");
		JButton botonIniciarSesion = new JButton("Iniciar Sesion");
		
		JLabel etiquetaBienvenido = new JLabel("Bienvenido a Sell-It");
		
		JLabel etiquetaPregunta = new JLabel("¿No tienes cuenta?");
		
		
		//Añadimos los elementos previamente creados a los paneles
		
		panelNorte.add(etiquetaBienvenido,BorderLayout.NORTH);
		panelSur.add(etiquetaPregunta);
		panelSur.add(botonRegistroEntidad);
		panelSur.add(botonRegistroUsuario);
	//	panelSur.add(botonIniciarSesion);
		
		panelCentro.add(etiquetaUsuario);
		panelCentro.add(txtUsuario);
		panelCentro.add(etiquetaContrasenia);
		panelCentro.add(txtContrasenia);
		
		JPanel panel = new JPanel();
		panelCentro.add(panel);
		panel.add(botonIniciarSesion);
	
		
	
		
		//Eventos
		
		
		botonRegistroUsuario.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaRegistroUsuario VEntanaRegistroUsuario = new VentanaRegistroUsuario();
				VEntanaRegistroUsuario.setVisible(true);	
			}
		});
		
		botonRegistroEntidad.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				VentanaRegistroEntidad ventanaRegistroEntidad = new VentanaRegistroEntidad();
				ventanaRegistroEntidad.setVisible(true);
			}
		});
	
		botonIniciarSesion.addActionListener((e)->{
			String correo = txtUsuario.getText();
			String contrasenia = txtContrasenia.getText();
			
	/**		Usuario u = 
			if(u == null) {
				JOptionPane.showMessageDialog(null, "Usuario incorrecto");
			}
			else if(u.getContrasena().equals(contrasenia)) {
				JOptionPane.showMessageDialog(null, "Bienvenido de nuevo "+ u.getNombre());
				//VentaPrincipal.setVisible(true);
			}
	**/
			

// Hay una exception porq: DataSetUsuario.getUsuariosGuardados()" because "this.dataSetUsuario" is null
			//NO ESTA TERMINADO
			 if (verificarCredenciales(correo, contrasenia)) {
			        JOptionPane.showMessageDialog(null, "Bienvenido de nuevo " + obtenerNombreUsuario(correo));
			        // Realiza acciones adicionales cuando el inicio de sesión sea exitoso
			 } else {
			        JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
			 }
		});
		
	}
	
//	private boolean verificarCredenciales(String iD, String contrasenia) {
//		 List<Usuario> usuarios = dataSetUsuario.getUsuariosGuardados(); // Obtén la lista de usuarios cargados desde el archivo
//
//		 for (Usuario usuario : usuarios) {
//			 if (usuario.getNombreUsuario().equals(iD) && usuario.getContrasena().equals(contrasenia)) {
//				 return true; // Las credenciales coinciden
//			 }
//		 }
//		 return false;
//	    
//	}
	
	private boolean  verificarCredenciales (String correo, String contrasenia) {
		 if (dataSetUsuario.getMapaUsu().containsKey(correo)) {
	            Usuario u = new Usuario();
	            if (u.getContrasena().equals(contrasenia)) {
	                return true;
	            } else {
	                return false;
	            }
	        } else {
	            return false;
	        }
	}
	
//	private String obtenerNombreUsuario(String iD) {
//		List<Usuario> usuarios = dataSetUsuario.getUsuariosGuardados(); // Obtiene la lista de usuarios
//
//	    for (Usuario usuario : usuarios) {
//	        if (usuario.getNombreUsuario().equals(iD)) {
//	            return usuario.getNombreUsuario();
//	        }
//	    }
//	    return "Nombre de usuario no encontrado";
//	}
	
	public String obtenerNombreUsuario(String correo) {
	    if (dataSetUsuario.getMapaUsu().containsKey(correo)) {
	        Usuario usuario = dataSetUsuario.getMapaUsu().get(correo);
	        return usuario.getNombreUsuario();
	    } else {
	        return "Nombre de usuario no encontrado";
	    }
	}

	public void cargarUsuariosInicio(DataSetUsuario dataset) {
		// TODO Auto-generated method stub
		
	}

}
