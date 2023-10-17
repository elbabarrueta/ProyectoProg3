package ventanas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class VEntanaRegistroUsuario extends JFrame {

	
	
	

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public  VEntanaRegistroUsuario() {
		
		
		this.setBounds(300,300, 400, 400);
		this.setTitle("Registro de Usuario");
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		
		JPanel panelRegistroUsuario = new JPanel(new BorderLayout());
		JPanel panelSur = new JPanel(new BorderLayout());
		JPanel panelNorte = new JPanel(new BorderLayout());
		JPanel panelCentro = new JPanel(new GridLayout(1,10));
		panelRegistroUsuario.add(panelCentro,BorderLayout.CENTER);
		panelRegistroUsuario.add(panelNorte,BorderLayout.NORTH);
		panelRegistroUsuario.add(panelSur,BorderLayout.SOUTH);
		panelNorte.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelSur.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		
		this.add(panelRegistroUsuario);
		
		
		JButton btnRegistro = new JButton("Registrarse");
		panelSur.add(btnRegistro);
		
		
		JPasswordField txtContrasenia = new JPasswordField();
		
		JTextField txtMail = new JTextField();
		JTextField txtNombre = new JTextField();
		JTextField txtID = new JTextField();
		JTextField txtApellido = new JTextField();
		
		
		
		JLabel lblMail = new JLabel("Correo electronico:");
		JLabel lblNombre = new JLabel("Nombre:");
		JLabel lblApellido = new JLabel("Apellidos:");
		JLabel lblID = new JLabel("DNI:");
		JLabel lblContrasenia = new JLabel("Contrasenia");
		JLabel lblPanelNorte = new JLabel("Rellene las casillas");
		
		panelNorte.add(lblPanelNorte,BorderLayout.NORTH);
		
		panelCentro.add(lblNombre);
		panelCentro.add(txtNombre);
		panelCentro.add(lblApellido);
		panelCentro.add(txtApellido);
		panelCentro.add(lblID);
		panelCentro.add(txtID);
		panelCentro.add(lblMail);
		panelCentro.add(txtMail);
		panelCentro.add(lblContrasenia);
		panelCentro.add(txtContrasenia);
		
		
		
		
	
		
	}
	
	
	

	
	
	

}
