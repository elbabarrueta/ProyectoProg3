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
import datos.DataSetUsuario;

public class VentanaRegistroUsuario extends JFrame {
	
	private JTextField txtNombre,txtCorreo;
	private JPasswordField txtContrasenia;
//	private JComboBox comboTipoU;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataSetUsuario dataSetUsuario;

	
	public VentanaRegistroUsuario() {
		this.setBounds(900,300,400,400);
		this.setTitle("Registro de Usuario");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel panelRegistroUsuario = new JPanel(new BorderLayout());
		JPanel panelSur = new JPanel(new BorderLayout());
		JPanel panelNorte = new JPanel(new BorderLayout());
		JPanel panelCentro = new JPanel(new GridLayout(5,2));
		panelRegistroUsuario.add(panelCentro,BorderLayout.CENTER);
		panelRegistroUsuario.add(panelSur,BorderLayout.SOUTH);
		panelRegistroUsuario.add(panelNorte,BorderLayout.NORTH);
		panelNorte.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelSur.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		this.add(panelRegistroUsuario);
		
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
		
		txtContrasenia = new JPasswordField(20);
		txtContrasenia.setEchoChar('*');
		
		txtNombre = new JTextField();

		txtCorreo = new JTextField();
		
		JLabel lblNombre = new JLabel("Nombre:");

		JLabel lblContrasenia = new JLabel("Contraseña:");
		
		JLabel lblPanelNorte = new JLabel("Rellene las Casillas:");
		JLabel lblCorreo = new JLabel("Direccion de correo:");
		JLabel lblTipo = new JLabel("Tipo de usuario");
		
		panelNorte.add(lblPanelNorte,BorderLayout.NORTH);
		
		JTextField txtTipou = new JTextField();
		txtTipou.setText("Usuario corriente");
		txtTipou.setEditable(false);
//		String[] tipoU = {"Usuario corriente", "Usuario entidad"};
//        comboTipoU = new JComboBox<>(tipoU);
		

		panelCentro.add(lblNombre);
		panelCentro.add(txtNombre);
		panelCentro.add(lblContrasenia);
		panelCentro.add(txtContrasenia);
		panelCentro.add(lblCorreo);
		panelCentro.add(txtCorreo);
		panelCentro.add(lblTipo);
		panelCentro.add(txtTipou);
		

		//Eventos
		
		btnRegistro.addActionListener((e)->{
			
			String contrasenia = txtContrasenia.getText();
			String nombre = txtNombre.getText();
			String correo = txtCorreo.getText();
			String tipo = txtTipou.getText();
			
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
		//	u.cambiarContrasena("contrasenaInicial");
		//	u.cambiarContrasena(contrasenia);
			
			if( DataSetUsuario.buscarUsu(correo)== null) {
				DataSetUsuario.anyadirUsuario(u);
				JOptionPane.showMessageDialog(null,"Bienvenido a Sell-IT");
				System.out.println("\t" + u);
				
				//dataSetUsuario.anyadirUsuario(u);  hay que elegir una clase Usuario
				VentanaInicio v = new VentanaInicio();
				dispose();
		        v.setVisible(true);
		        Main.setVentanaInicio(v);
			}else {
				JOptionPane.showMessageDialog(null,"Usuario existente, compruebe los datos");
				
			}
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
