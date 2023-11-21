package ventanas;

import java.awt.BorderLayout;


import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import clases.Datos;
import clases.Usuario;

public class VentanaRegistroEntidad extends JFrame{
	
	private JPasswordField txtContrasenia;
	private JTextField txtNombre,txtCorreo;
//	private JComboBox comboTipo;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VentanaRegistroEntidad() {
		
		this.setBounds(300,400,400,300);
		this.setTitle("Registro Entidad");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		JPanel panelRegistroEntidad = new JPanel(new BorderLayout());
		JPanel panelNorte = new JPanel(new BorderLayout());
		JPanel panelSur = new JPanel(new BorderLayout());
		JPanel panelCentro = new JPanel(new GridLayout(5,2));
		
		this.add(panelRegistroEntidad);
		
		
		JLabel lblNombre = new JLabel("Nombre de la Empresa:");
		JLabel lblContrasenia = new JLabel("Contraseña:");
		JLabel lblCorreo = new JLabel("Correo de la empresa:");

		JLabel lblPanelNorte = new JLabel("Rellene las casillas");

		JLabel lblTipo = new JLabel("Tipo de usuario");
		
		JTextField txtTipo = new JTextField();
		txtTipo.setText("Usuario entidad");
		txtTipo.setEditable(false);
//		String[] tipoUsu = {"Usuario entidad", "Usuario corriente"};
//		comboTipo = new JComboBox<>(tipoUsu);
		

		txtNombre = new JTextField();
		txtCorreo = new JTextField();

		
		txtContrasenia = new JPasswordField();
		
		JLabel mostrarContra = new JLabel("Mostrar Contraseña");		
		mostrarContra.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                mostrarOcultarContraseña();
	            }
	    });
		
		JButton btnRegistro = new JButton("Registrarse");
		panelSur.add(btnRegistro);
		panelSur.add(mostrarContra);
		
		panelRegistroEntidad.add(panelCentro,BorderLayout.CENTER);
		panelRegistroEntidad.add(panelSur,BorderLayout.SOUTH);
		panelRegistroEntidad.add(panelNorte,BorderLayout.NORTH);
		
		panelNorte.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelSur.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		panelNorte.add(lblPanelNorte);
	
		panelCentro.add(lblNombre);
		panelCentro.add(txtNombre);
		panelCentro.add(lblCorreo);
		panelCentro.add(txtCorreo);
		panelCentro.add(lblContrasenia);
		panelCentro.add(txtContrasenia);
		panelCentro.add(lblTipo);
		panelCentro.add(txtTipo);

		
		//Eventos
		
		
		btnRegistro.addActionListener((e)->{
			
			String contrasenia = txtContrasenia.getText();
			String nombre = txtNombre.getText();
			String correo = txtCorreo.getText();
			String tipo = txtTipo.getText();
			
			if(nombre.isEmpty() || correo.isEmpty() || contrasenia.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Para registrarse, debe introducir datos en todas las casillas.");
				return;
			}
			if (contrasenia.length() <= 5) {
		        JOptionPane.showMessageDialog(null, "La contraseña debe tener más de 5 caracteres.");
		        return; 
		    }
			if (!correo.contains("@")) {
		        JOptionPane.showMessageDialog(null, "La dirección de correo no es válida. Debe contener el carácter '@'.");
		        return;
		    }
			
			Usuario u = new Usuario(nombre,correo,tipo,contrasenia);
			u.setUltimaCambioContrasena(LocalDate.now());
			
			if( DataSetUsuario.buscarUsu(correo)== null) {
				DataSetUsuario.anyadirUsuario(u);
				JOptionPane.showMessageDialog(null, "Bienvenido a Sell-IT");
				System.out.println("\t" + u);
				// Cerrar la ventana actual
				VentanaInicio v = new VentanaInicio();
				dispose();
		        v.setVisible(true);
		        Main.setVentanaInicio(v);
			}
			else {
				JOptionPane.showMessageDialog(null, "Usuario existente, introduce otro correo y nombre");
			}
		//	System.out.println("\t" + u);
			limpiarCampos();
		});
		
	}
	
	public void mostrarOcultarContraseña() {
        // Obtener la contraseña actual
        char[] contraseña = txtContrasenia.getPassword();

        // Cambiar el estado de visualización de la contraseña
        if (txtContrasenia.getEchoChar() == 0) {
        	txtContrasenia.setEchoChar('*');
        } else {
        	txtContrasenia.setEchoChar((char) 0);
        }
        txtContrasenia.setText(new String(contraseña));
    }
	private void limpiarCampos() {

		txtNombre.setText("");
		txtCorreo.setText("");	
		txtContrasenia.setText("");
		

	}
		


}
