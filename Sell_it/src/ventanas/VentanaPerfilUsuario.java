package ventanas;

import javax.swing.*;



import javax.swing.event.TreeSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import BasesDeDatos.BaseDeDatos;
import clases.Usuario;
import clases.Entrada;
import clases.Notificacion;

import org.mindrot.jbcrypt.BCrypt;
import BasesDeDatos.BaseDeDatos;
import clases.Usuario;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;


public class VentanaPerfilUsuario extends JFrame{

//	private JPanel panelBotones;
//	private JPanel panelPrincipal;
//	private JPanel panelInformacion;
	private JLabel lblFotoPerfil;
	private JTextField nameField;
	private JTextField emailField;
	private ImageIcon imagenPerfil;
	private JTextArea descriptionArea;
	
	private Usuario usuario;
	private LocalDate ultimoCambioContrasena;
	private List<String> entradasCompradas;
//	private JButton btnMisCompras;
//	private JButton btnEnVenta;
	
	public VentanaPerfilUsuario(Usuario usuario, List<String> entradasCompradas) {
		
		this.entradasCompradas = entradasCompradas;
		this.usuario = usuario;
		ultimoCambioContrasena = LocalDate.now(); //para provar ahora, que sea la fecha actual
		
		BaseDeDatos base = new BaseDeDatos();

		this.setTitle("Perfil Usuario");
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    this.setLayout(new BorderLayout());

	    // Parte superior: nombre, correo y botones de información
	    JPanel topPanel = new JPanel(new FlowLayout());
	    
		lblFotoPerfil = new JLabel();
	    if(usuario.getImgPerfil() == null) {
	    	imagenPerfil = new ImageIcon("Sell_it/src/imagenes/perfil.png"); // Ruta de la imagen de perfil
	    	fotoPerfil(imagenPerfil);
	    }else {
	    	String rutaImg = usuario.getImgPerfil();
	    	imagenPerfil = new ImageIcon(rutaImg);
			usuario.setImgPerfil(rutaImg);
			fotoPerfil(imagenPerfil);
	    }
	    
	    
	    JLabel nameLabel = new JLabel("Nombre:");
	    nameField = new JTextField(20);
	    JLabel emailLabel = new JLabel("Correo:");
	    emailField = new JTextField(20);
	    nameField.setText(usuario.getNombreUsuario());
		emailField.setText(usuario.getCorreoUsuario());
		nameField.setEditable(false);
	    emailField.setEditable(false);
	    
	    JButton btnValoraciones = new JButton("Valoraciones");
	    JButton btnNotificaciones = new JButton("Notificaciones");
	    topPanel.add(lblFotoPerfil);
	    topPanel.add(nameLabel);
	    topPanel.add(nameField);
	    topPanel.add(emailLabel);
	    topPanel.add(emailField);
	    topPanel.add(btnValoraciones);
	    topPanel.add(btnNotificaciones);

	    // Parte central: descripción del usuario
//	    descriptionArea = new JTextArea("Ingresa información util sobre ti para completar tu perfil en la aplicación...", 5, 10);
	    descriptionArea = new JTextArea(usuario.getDescripcion());
	    descriptionArea.setMargin(new java.awt.Insets(10, 10, 10, 10));
	    JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
	    descriptionArea.setLineWrap(true);
	    descriptionArea.setWrapStyleWord(true);
	    descriptionArea.setEditable(false);
	    
	    // Parte inferior: más botones
	    JPanel bottomPanel = new JPanel();
	    JButton buttonContrasena = new JButton("Cambiar contraseña");
	    JButton buttonEditar = new JButton("Editar Perfil");
	    JButton btnMisCompras = new JButton("Mis compras");
        JButton botonVentanaP = new JButton("Ventana Principal");
//        btnMisCompras = new JButton("Mis Compras");
        JButton btnEnVenta = new JButton("En Venta");
        
	    topPanel.add(btnEnVenta);
        
//        botonVentanaP.setBackground(Color.LIGHT_GRAY);        
//        //Personalizar la letra del boton
//        Font font = new Font("Montserrat", Font.BOLD, 14);
//        botonVentanaP.setFont(font);
        
	    bottomPanel.add(buttonContrasena);
	    bottomPanel.add(buttonEditar);
	    bottomPanel.add(btnMisCompras);
        bottomPanel.add(botonVentanaP);
	    
	    this.add(topPanel, BorderLayout.NORTH);
	    this.add(descriptionScrollPane, BorderLayout.CENTER);
	    this.add(bottomPanel, BorderLayout.SOUTH);
	    
	    btnValoraciones.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				VentanaPerfilUsuario.this.dispose();
//				QUEDA POR AÑADIR LA VENTANA DONDE SALEN LAS VALORACIONES QUE HEMOS RECIBIDO
			}
		});
	    
	    botonVentanaP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaPerfilUsuario.this.dispose();
				VentanaPrincipal v = new VentanaPrincipal();
				v.setVisible(true);
			}
		});
	    btnMisCompras.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaPerfilUsuario.this.dispose();
				VentanaMisCompras v1 = new VentanaMisCompras(usuario);
				v1.setVisible(true);
			}
		});
	    
	    btnEnVenta.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaPerfilUsuario.this.dispose();
				VentanaReventaUsuario v2 = new VentanaReventaUsuario(usuario);
				v2.setVisible(true);				
			}
		});
	    
	    buttonContrasena.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Botón de cambiar contraseña presionado.");

		        // Verificar en la base de datos la fecha de último cambio de contraseña
		        BaseDeDatos base = new BaseDeDatos();
		        LocalDate ultimaCambioDesdeBD = base.obtenerUltimoCambioContrasena(usuario);

		        if (ultimaCambioDesdeBD != null) {
		            LocalDate hoy = LocalDate.now();
		            long diasDesdeUltimoCambio = ChronoUnit.DAYS.between(ultimaCambioDesdeBD, hoy);
		            System.out.println("Días desde el último cambio: " + diasDesdeUltimoCambio);

		            if (diasDesdeUltimoCambio >= 15) {
		                int respuesta = JOptionPane.showConfirmDialog(VentanaPerfilUsuario.this,
		                        "La contraseña se cambió hace más de 15 días. ¿Seguro que deseas cambiarla ahora?",
		                        "Confirmación de Cambio de Contraseña", JOptionPane.YES_NO_OPTION);

		                if (respuesta == JOptionPane.YES_OPTION) {
		                    try {
		                        // Código para cambiar la contraseña.
		                        String nuevaContrasena = JOptionPane.showInputDialog(VentanaPerfilUsuario.this,
		                                "Introduce la nueva contraseña");

		                        if (nuevaContrasena != null && !nuevaContrasena.isEmpty()) {
		                            usuario.cambiarContrasena(nuevaContrasena);
		                            // Hash de la nueva contraseña
		                            String hashNuevaContrasena = BCrypt.hashpw(nuevaContrasena, BCrypt.gensalt());

		                            // Cambiar la contraseña y actualizar la fecha en el usuario
		                            usuario.cambiarContrasena(hashNuevaContrasena);
		                            usuario.setUltimaCambioContrasena(LocalDate.now());
		                            // Actualizar la contraseña y la fecha en la base de datos
		                            base.modificarUsuarioYaRegistradoContrasena(usuario);

		                            JOptionPane.showMessageDialog(VentanaPerfilUsuario.this,
		                                    "Contraseña cambiada exitosamente.");
		                        } else {
		                            System.out.println("Contraseña vacía o cancelada.");
		                        }
		                    } catch (Exception ex) {
		                        ex.printStackTrace();
		                        JOptionPane.showMessageDialog(VentanaPerfilUsuario.this,
		                                "Error al cambiar contraseña: " + ex.getMessage());
		                    }
		                }
		            } else {
		                JOptionPane.showMessageDialog(VentanaPerfilUsuario.this,
		                        "La contraseña solo se puede cambiar una vez cada 15 días. \nDías desde el último cambio: " + diasDesdeUltimoCambio);
		            }
		        } else {
		            System.out.println("No se pudo obtener la fecha de último cambio desde la base de datos.");
		        }
		    }
		});
	    
	    JButton botonGuardarCambios = new JButton("Guardar cambios");
	    botonGuardarCambios.setVisible(false);
	    botonGuardarCambios.setBackground(Color.gray);
	    botonGuardarCambios.setForeground(Color.black);
	    
	    JButton botonCambiarFoto = new JButton("Cambiar foto de perfil");
	    botonCambiarFoto.setVisible(false);
	    
	    buttonEditar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
		        // Restablece la edición de los campos
				nameField.setEditable(true);
				descriptionArea.setEditable(true);
		        setEditableDescripcion(true);
	            botonCambiarFoto.setVisible(true);
	            // Quitamos botones para que no haya demasiados a la vez
	            botonVentanaP.setVisible(false);
	            buttonContrasena.setVisible(false);
	            buttonEditar.setVisible(false);
	            btnMisCompras.setVisible(false);
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
	            int result = chooser.showOpenDialog(VentanaPerfilUsuario.this);
	            if (result == JFileChooser.APPROVE_OPTION) {
	                File selectedFile = chooser.getSelectedFile();
	                String rutaImg = selectedFile.getAbsolutePath();
	                
	                // Imprimir la ruta de la imagen para verificar
	                System.out.println("Ruta de la imagen seleccionada: " + rutaImg);
	          	                
	                usuario.setImgPerfil(rutaImg);
	                imagenPerfil = new ImageIcon(rutaImg);
	                fotoPerfil(imagenPerfil);
	             // Actualizar la ruta de la imagen en la base de datos
	                BaseDeDatos base = new BaseDeDatos();
	                base.modificarUsuarioImagenPerfil(usuario);
	            }else {
            		JOptionPane.showMessageDialog(VentanaPerfilUsuario.this, "Error al cambiar imagen, vuelve a intentarlo.");
	            }
			}
		});
	    
	    botonGuardarCambios.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String NomNuevo = nameField.getText();
				String imagen = usuario.getImgPerfil();
				String nuevaDescripcion = descriptionArea.getText();
				System.out.println(nuevaDescripcion);
				// Crea un objeto Usuario con los datos actualizados
				Usuario usuarioActualizado = new Usuario(NomNuevo, usuario.getCorreoUsuario(), "tipoUsuario", usuario.getContrasena(), imagen, nuevaDescripcion);
		  
		
//			    // Llama al método para modificar el usuario en la base de datos
				BaseDeDatos base = new BaseDeDatos();
				base.modificarUsuarioYaRegistrado(usuarioActualizado);
//				base.modificarDescripcionUsuario(usuarioActualizado, nuevaDescripcion);
				base.modificarDescripcionUsuario(usuarioActualizado);
				
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
				botonGuardarCambios.setVisible(false);
				botonCambiarFoto.setVisible(false);
				// Volvemos a poner los botones
				botonVentanaP.setVisible(true);
	            buttonContrasena.setVisible(true);
	            buttonEditar.setVisible(true);
	            btnMisCompras.setVisible(true);
			}
		});
	    bottomPanel.add(botonGuardarCambios);
	    bottomPanel.add(botonCambiarFoto);

	    btnNotificaciones.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
//	    		// Simulemos que aquí se obtienen notificaciones del sistema o de otros usuarios.
//	    		List<String> notificaciones = obtenerNotificaciones();
//
//	    		// Construir un mensaje de notificación a partir de las notificaciones.
//	    		StringBuilder notificacionMessage = new StringBuilder();
//	    		notificacionMessage.append("Notificaciones:\n");
//	    		for (String notificacion : notificaciones) {
//	    			notificacionMessage.append("- ").append(notificacion).append("\n");
//	    		}
	    		mostrarNotificaciones();
	    	}
	    });
	    

	    this.pack();
	    setLocationRelativeTo(null);
	    this.setVisible(true);
	}	
	private void setEditableDescripcion(boolean editable) {
		descriptionArea.setEditable(editable);
	}
	
//	private List<String> obtenerNotificaciones() {
//	   
//	    List<String> notificaciones = new ArrayList<>();
//	    notificaciones.add("Nueva oferta para tu producto.");
//	    notificaciones.add("¡Has vendido un artículo!");
//	    notificaciones.add("Nuevo mensaje de un comprador interesado.");
//	    return notificaciones;
//	}
	private void mostrarNotificaciones() {
    	usuario.cargarNotificacionesDesdeBD();
        List<Notificacion> notificaciones = usuario.getNotificaciones();
        StringBuilder notificacionMessage = new StringBuilder();
        notificacionMessage.append("Notificaciones:\n");
        for (Notificacion notificacion : notificaciones) {
        	if(notificacion.isLeido() == false) {
                notificacionMessage.append("- ").append(notificacion.getMensaje()).append("\n");
        	}
        }
        JOptionPane.showMessageDialog(this, notificacionMessage.toString(), "Notificaciones", JOptionPane.INFORMATION_MESSAGE);
        
        for (Notificacion notificacion : notificaciones) {
        	if (notificacion.isLeido() == false) {
                notificacion.setLeido(true);
                int id = notificacion.getId();
                Main.getVentanaInicio().base.marcarLeidoBD(id, usuario.getCorreoUsuario());
            }
       }
    }
	
	private void fotoPerfil(ImageIcon imagenPerfil) {
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
	}
	
//	public static void main(String[] args) {
		
//		List<String> entradasCompradas = new ArrayList<>();
//		VentanaInicio ventanaI = Main.getVentanaInicio();
//		Usuario usuActual = ventanaI.getUsuarioActual();
//    	Usuario usuarioNormal = new Usuario(usuActual.getNombreUsuario(), usuActual.getCorreoUsuario(), usuActual.getTipoUsuario(), usuActual.getContrasena());
//		
//    	VentanaPerfilUsuario vent = new VentanaPerfilUsuario(usuarioNormal, entradasCompradas);

//	}

}
