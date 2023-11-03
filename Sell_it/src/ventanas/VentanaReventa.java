package ventanas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

import javax.swing.*;

import clases.Usuario;



public class VentanaReventa extends JFrame{
	private Usuario usuario;
	private List<String> entradasCompradas;

	private JComboBox cbEntradas = new JComboBox();   //Lista con entradas disponibles para vender
	private JTextField tfCant = new JTextField();
	private JTextField tfPrecio = new JTextField();
	
	public VentanaReventa() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(600, 500);
		setLocationRelativeTo(null);
		setTitle("Reventa");
		
		//Creamos los paneles
		JPanel pSuperior = new JPanel(new BorderLayout());
		this.add(pSuperior, BorderLayout.NORTH);
		
		JPanel pCentral = new JPanel(new GridLayout(4,2));
		this.add(pCentral, BorderLayout.CENTER);
		
		JPanel pInferior = new JPanel(new BorderLayout());
		this.add(pInferior, BorderLayout.SOUTH);
		
		//Panel Superior
		JButton bMiperfil = new JButton("MI PERFIL");
		pSuperior.add(bMiperfil, BorderLayout.EAST);
		
		//Panel Central
		JLabel lEntrada = new JLabel("Seleccionar entrada");
		pCentral.add(lEntrada);
		pCentral.add(cbEntradas);
		
		JLabel lCant = new JLabel("Cantidad");
		pCentral.add(lCant);
		pCentral.add(tfCant);
		
		JLabel lPrecio = new JLabel("Precio");
		pCentral.add(lPrecio);
		pCentral.add(tfPrecio);
		
		//Panel Inferior
		JButton bSubir = new JButton("Subir entrada");
		pInferior.add(bSubir, BorderLayout.EAST);
		JButton bVprincipal = new JButton("Ventana Principal");
		pInferior.add(bVprincipal, BorderLayout.WEST);
		
		
	}
	public static void main(String[] args) {
		VentanaReventa v = new VentanaReventa();
		v.setVisible(true);
	}

}
