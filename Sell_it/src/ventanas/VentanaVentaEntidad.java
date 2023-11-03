package ventanas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
<<<<<<< HEAD
import java.util.ArrayList;
=======
>>>>>>> branch 'master' of https://github.com/elbabarrueta/Sell-it.git

import javax.swing.*;

import clases.Entrada;
import clases.Evento;

public class VentanaVentaEntidad extends JFrame{
	
	private JTextField tfNombre = new JTextField();
	private JTextField tfDesc = new JTextField();
	private JTextField tfFecha = new JTextField();
	private JTextField tfUbicacion = new JTextField();
	private JTextField tfCant = new JTextField();
	private JTextField tfPrecio = new JTextField();
	
	public VentanaVentaEntidad() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(600, 500);
		setLocationRelativeTo(null);
		setTitle("Venta Entidad");
		
		//Creamos los paneles
		JPanel pSuperior = new JPanel(new BorderLayout());
		this.add(pSuperior, BorderLayout.NORTH);
		
		JPanel pCentral = new JPanel(new GridLayout(6,2));
		this.add(pCentral, BorderLayout.CENTER);
		
		JPanel pInferior = new JPanel(new BorderLayout());
		this.add(pInferior, BorderLayout.SOUTH);
		
		//Panel Superior
		JButton bMiperfil = new JButton("MI PERFIL");
		pSuperior.add(bMiperfil, BorderLayout.EAST);
		
		//Panel Cental
		JLabel lNombre = new JLabel("Nombre del evento");
		pCentral.add(lNombre);
		pCentral.add(tfNombre);
		
		JLabel lDesc = new JLabel("Descripción del evento");
		pCentral.add(lDesc);
		pCentral.add(tfDesc);
		
		JLabel lFecha = new JLabel("Fecha del evento");
		pCentral.add(lFecha);
		pCentral.add(tfFecha);
		
		JLabel lUbicacion = new JLabel("Ubicación");
		pCentral.add(lUbicacion);
		pCentral.add(tfUbicacion);
		
		JLabel lCant = new JLabel("Cantidad de entradas disponibles");
		pCentral.add(lCant);
		pCentral.add(tfCant);
		
		JLabel lPrecio = new JLabel("Precio por entrada");
		pCentral.add(lPrecio);
		pCentral.add(tfPrecio);
		
		//Panel Inferior
		JButton bSubir = new JButton("Subir entrada");
		pInferior.add(bSubir, BorderLayout.EAST);
		JButton bVprincipal = new JButton("Ventana Principal");
		pInferior.add(bVprincipal, BorderLayout.WEST);
<<<<<<< HEAD
		
		
		
		bMiperfil.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaPerfilEntidad vPerfilEntidad = new VentanaPerfilEntidad();
			}
		});
		
		bVprincipal.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaPrincipal vPrincipal = new VentanaPrincipal();
			}
		});
		
		bSubir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String nombre = tfNombre.getText();
				String desc = tfDesc.getText();
				String fecha = tfFecha.getText();
				String ubicacion = tfUbicacion.getText();
				ArrayList<Entrada> entradas = new ArrayList<Entrada>();
				String cantText = tfCant.getText();
		        String precioText = tfPrecio.getText();
				
				if (nombre.isEmpty() || desc.isEmpty() || fecha.isEmpty() || ubicacion.isEmpty() || cantText.isEmpty() || precioText.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }
				try {
					int cantidad = Integer.parseInt(cantText);
					double precio = Double.parseDouble(precioText);			            
					Evento evento = new Evento(nombre, desc, fecha, ubicacion, new ArrayList<Entrada>(), precio);
					//Añadir evento
					
					//Crear entradas
					JOptionPane.showMessageDialog(null, "Entrada subida exitosamente");
				}catch(NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Ingresa valores válidos para la cantidad y el precio", "Error", JOptionPane.ERROR_MESSAGE);
				}
								
			}
		});
		
		
=======
	
>>>>>>> branch 'master' of https://github.com/elbabarrueta/Sell-it.git
	}
	public static void main(String[] args) {
		VentanaVentaEntidad v = new VentanaVentaEntidad();
		v.setVisible(true);
	}
}
