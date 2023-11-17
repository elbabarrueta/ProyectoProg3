package ventanas;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import clases.Usuario;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class VentanaPerfilEntidad extends JFrame{
	
	private Usuario usuario;
	private LocalDate ultimoCambioContrasena;
	private List<String> entradasCompradas;
	private JLabel lblFotoPerfil;
	
	public VentanaPerfilEntidad(Usuario usuario) {
		
		this.entradasCompradas = entradasCompradas;
		this.usuario = usuario;
		ultimoCambioContrasena = LocalDate.now();
		
        JFrame frame = new JFrame("Perfil entidad");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Parte superior: nombre, correo y botones de información
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel nameLabel = new JLabel("Nombre:");
        JTextField nameField = new JTextField(20);
        JLabel emailLabel = new JLabel("Correo:");
        JTextField emailField = new JTextField(20);
        nameField.setText(usuario.getNombreUsuario());
		emailField.setText(usuario.getCorreoUsuario());
		nameField.setEditable(false);
	    emailField.setEditable(false);
	    
	    lblFotoPerfil = new JLabel();
        ImageIcon imagenPerfil = new ImageIcon(VentanaPerfilUsuario.class.getResource("perfilE.png")); // Ruta de la imagen de perfil
        
        int maxWidth = 100; // Tamaño máximo de ancho
        int maxHeight = 100; // Tamaño máximo de alto
        int newWidth, newHeight;
        Image img = imagenPerfil.getImage();
        if (imagenPerfil.getIconWidth() > imagenPerfil.getIconHeight()) {
            newWidth = maxWidth;
            newHeight = (maxWidth * imagenPerfil.getIconHeight()) / imagenPerfil.getIconWidth();
        } else {
            newHeight = maxHeight;
            newWidth = (maxHeight * imagenPerfil.getIconWidth()) / imagenPerfil.getIconHeight();
        }
        // Redimensiona la imagen
        Image newImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        imagenPerfil = new ImageIcon(newImg);
        
        lblFotoPerfil.setIcon(imagenPerfil);
        
        JButton infoButton1 = new JButton("En venta");
        JButton infoButton2 = new JButton("Valoraciones");
        JButton infoButton3 = new JButton("Notificaciones");
        topPanel.add(lblFotoPerfil);
        topPanel.add(nameLabel);
        topPanel.add(nameField);
        topPanel.add(emailLabel);
        topPanel.add(emailField);
        topPanel.add(infoButton1);
        topPanel.add(infoButton2);
        topPanel.add(infoButton3);

        // Parte central: descripción del usuario
        JTextArea descriptionArea = new JTextArea("Ingresa información util sobre ti para completar tu perfil en la aplicación...", 5, 10);
	    JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
	    descriptionArea.setLineWrap(true);
	    descriptionArea.setWrapStyleWord(true);
	    descriptionArea.setEditable(true);

        // Parte inferior: más botones
        JPanel bottomPanel = new JPanel();
        JButton botonContrasena = new JButton("Cambiar Contraseña");
        JButton botonEditar = new JButton("Editar Perfil");
        JButton botonCompras = new JButton("Compras");
        JButton botonVentanaP = new JButton("Ventana Principal");
        bottomPanel.add(botonContrasena);
        bottomPanel.add(botonEditar);
        bottomPanel.add(botonCompras);
        bottomPanel.add(botonVentanaP);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(descriptionScrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Agregar acción al botón de información 1
        infoButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "En estos momentos no tienes ningún artículo en venta");
            }
        });
        
        botonVentanaP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				VentanaPrincipal v = new VentanaPrincipal();
			}
		});
        
        botonContrasena.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
		        LocalDate hoy = LocalDate.now();

		        if(usuario.getUltimaCambioContrasena() != null) {
		        	long diasDesdeUltimoCambio = ChronoUnit.DAYS.between(usuario.getUltimaCambioContrasena(), hoy);
			        System.out.println(diasDesdeUltimoCambio);
			        if (diasDesdeUltimoCambio >= 15) {
			            int respuesta = JOptionPane.showConfirmDialog(frame, "La contraseña se cambió hace más de 15 días. ¿Seguro que deseas cambiarla ahora?",
			                    "Confirmación de Cambio de Contraseña", JOptionPane.YES_NO_OPTION);

			            if (respuesta == JOptionPane.YES_OPTION) {
			                // Código para cambiar la contraseña.
			            	String nuevaContrasena = JOptionPane.showInputDialog(frame, "Introduce la nueva contrasena");
			            	if(nuevaContrasena != null && !nuevaContrasena.isEmpty()) {
			            		usuario.cambiarContrasena(nuevaContrasena);
			        //    		ultimaCambio = LocalDate.now(); // Actualizar la fecha
			            		JOptionPane.showMessageDialog(frame, "Contraseña cambiada exitosamente.");   
			            	}else {
			            		JOptionPane.showMessageDialog(frame, "Error al cambiar contraseña, vuelve a intentarlo.");
			            	}
			                
			            }
			        } else {
			            JOptionPane.showMessageDialog(frame, "La contraseña solo se puede cambiar una vez cada 15 días.");
			        }
		        }
		        
			}
		});
        
        JButton botonGuardarCambios = new JButton("Guardar cambios");
	    botonGuardarCambios.setVisible(false);
	    botonGuardarCambios.setBackground(Color.gray);
	    botonGuardarCambios.setForeground(Color.black);
	    
	    JButton botonCambiarFoto = new JButton("Cambiar foto de perfil");
	    botonCambiarFoto.setVisible(false);
	    
	    botonEditar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				nameField.setEditable(true);
	            emailField.setEditable(true);
	            botonCambiarFoto.setVisible(true);
	            // Quitamos botones para que no haya demasiados a la vez
	            botonVentanaP.setVisible(false);
	            botonContrasena.setVisible(false);
	            botonEditar.setVisible(false);
	            botonCompras.setVisible(false);
	         // Después de editar, habilitamos el botón "Guardar Cambios"
	            botonGuardarCambios.setVisible(true);
			}
		});
	    
	    botonCambiarFoto.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

	            JFileChooser chooser = new JFileChooser();
	            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos imagen (.jpg, .png)", "jpg", "png");
	            chooser.setFileFilter(filtro);
	            int result = chooser.showOpenDialog(frame);
	            if (result == JFileChooser.APPROVE_OPTION) {
	                File selectedFile = chooser.getSelectedFile();
	                ImageIcon imagenPerfil = new ImageIcon(selectedFile.getAbsolutePath());
	                int maxWidth = 100; // Tamaño máximo de ancho
	                int maxHeight = 100; // Tamaño máximo de alto
	                int newWidth, newHeight;
	                Image img = imagenPerfil.getImage();
	                if (imagenPerfil.getIconWidth() > imagenPerfil.getIconHeight()) {
	                    newWidth = maxWidth;
	                    newHeight = (maxWidth * imagenPerfil.getIconHeight()) / imagenPerfil.getIconWidth();
	                } else {
	                    newHeight = maxHeight;
	                    newWidth = (maxHeight * imagenPerfil.getIconWidth()) / imagenPerfil.getIconHeight();
	                }
	                // Redimensiona la imagen
	                Image newImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	                imagenPerfil = new ImageIcon(newImg);
	                lblFotoPerfil.setIcon(imagenPerfil);
	                
	            }else {
            		JOptionPane.showMessageDialog(frame, "Error al cambiar imagen, vuelve a intentarlo.");
	            }
			}
		});
	    
	    botonGuardarCambios.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String NomNuevo = nameField.getText();
				String CorreoNuevo = emailField.getText();
				usuario.setNombreUsuario(NomNuevo);
				usuario.setCorreoUsuario(CorreoNuevo);	
/*				
// hacer algo asi pero con el MAPA de USUARIOS				
				 // Buscar al usuario en la lista y actualizar sus datos
                for (Usuario usuarioEnLista : usuariosBase) {
                    if (usuarioEnLista.getNombreUsuario().equals(usuario.getNombreUsuario())) {
                        usuarioEnLista.setNombreUsuario(nuevoNombre);
                        usuarioEnLista.setCorreoUsuario(nuevoCorreo);
                        break; // Terminar la búsqueda una vez que se haya encontrado el usuario
                    }
                }
*/				
				nameField.setEditable(false);
				emailField.setEditable(false);
				botonGuardarCambios.setVisible(false);
				// Volvemos a poner los botones
				botonVentanaP.setVisible(true);
	            botonContrasena.setVisible(true);
	            botonEditar.setVisible(true);
	            botonCompras.setVisible(true);
			}
		});
	    bottomPanel.add(botonGuardarCambios);
        bottomPanel.add(botonCambiarFoto);

        frame.pack();
        frame.setVisible(true);
        
	}
	
    public static void main(String[] args) {
    	VentanaInicio ventanaI = Main.getVentanaInicio();
		Usuario usuActual = ventanaI.getUsuarioActual();
    	Usuario usuarioEntidad = new Usuario(usuActual.getNombreUsuario(), usuActual.getCorreoUsuario(), usuActual.getTipoUsuario(), usuActual.getContrasena());
    	VentanaPerfilEntidad ventanaPerfilEntidad = new VentanaPerfilEntidad(usuarioEntidad);
    }
}

