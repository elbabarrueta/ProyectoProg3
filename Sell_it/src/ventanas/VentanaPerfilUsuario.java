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

	private JLabel lblFotoPerfil;
	private JTextField nameField;
	private JTextField emailField;
	private ImageIcon imagenPerfil;
	private JTextArea descriptionArea;
	
	private Usuario usuario;
	private LocalDate ultimoCambioContrasena;
	private List<String> entradasCompradas;
	
	public VentanaPerfilUsuario(Usuario usuario, List<String> entradasCompradas) {
		
		this.entradasCompradas = entradasCompradas;
		this.usuario = usuario;
		ultimoCambioContrasena = LocalDate.now(); 
		
		BaseDeDatos base = new BaseDeDatos();

		this.setTitle("Perfil Usuario");
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    this.setLayout(new BorderLayout());
        this.setSize(500, 700); // Ancho y alto personalizados

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

        JButton btnEnVenta = new JButton("En Venta");
        
	    topPanel.add(btnEnVenta);
        

        
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
				VentanaPerfilUsuario.this.dispose();
				VentanaTablaValoraciones vv = new VentanaTablaValoraciones(null, usuario);
				vv.setVisible(true);
			}
		});
	    
	    botonVentanaP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				VentanaPrincipal v = new VentanaPrincipal();
				v.setVisible(true);
			}
		});
	    btnMisCompras.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				VentanaMisCompras v1 = new VentanaMisCompras(usuario);
				v1.setVisible(true);
			}
		});
	    
	    btnEnVenta.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				VentanaEntradasEnReventaTabla v2 = new VentanaEntradasEnReventaTabla(usuario);
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

				base.modificarDescripcionUsuario(usuarioActualizado);
				
				
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

	    		mostrarNotificaciones();
	    	}
	    });
	    

	    this.pack();
	    setLocationRelativeTo(null); // Centra la ventana
	    this.setVisible(true);
	}	
	private void setEditableDescripcion(boolean editable) {
		descriptionArea.setEditable(editable);
	}
	
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
	
	public static void main(String[] args) {
		
	}

}
