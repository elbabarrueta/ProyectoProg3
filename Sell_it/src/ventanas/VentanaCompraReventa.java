package ventanas;

import java.awt.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXLabel;

import BasesDeDatos.BaseDeDatos;
import clases.Entrada;
import clases.Evento;
import clases.Notificacion;
import clases.Usuario;

public class VentanaCompraReventa extends JFrame{
	
	private Usuario usuario;
	private Entrada ent;

	private Evento evento;
	private Evento eventoActual;
	
	//Componentes de la ventana
	private JTextField tfNombre;
	private JTextField tfCorreo;
	private JTextField tfTfno;
	private JTextField tfNtarjeta;
	private JComboBox cbMes;
	private JComboBox cbAnyo;
	private JLabel lTiempo;
	private JTextField tfCCV;

	private JPanel pNTarjeta;

	private Timer timer;
    private int tiempoRestante;
    private JXBusyLabel busyLabel;
    private JXErrorPane errorPane;
    private VentanaReventa vReventa;
	private List<Entrada> entradasEnBD;
    private static BaseDeDatos bd;
    private static VentanaPrincipal vPrincipal;
    private int idReventa;
    
	
	public VentanaCompraReventa(Usuario usuario, VentanaReventa vReventa, Entrada entrada, int idReventa) {
    	this.bd = new BaseDeDatos();
	    this.vReventa = vReventa;
		this.usuario = usuario;
		this.ent = entrada;
		this.idReventa = idReventa;
		vPrincipal = vReventa.vPrincipal;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 500);
		setLocationRelativeTo(null);
		setTitle("Compra de entrada");
		
		//Obtenemos el evento al que hacemos referencia
		this.eventoActual = vReventa.getEvento();
		
		//Creamos los paneles principales
		JPanel pCentral = new JPanel(new GridLayout(2,1));
		this.add(pCentral, BorderLayout.CENTER);
		JPanel pDer = new JPanel(new BorderLayout());
		this.add(pDer, BorderLayout.EAST);
		
		
		//Panel Central
		//Dentro del panel central creamos 2 paneles
		JPanel pDatos = new JPanel(new GridLayout(4,1));
		pCentral.add(pDatos);
		JPanel pPago = new JPanel(new GridLayout(5,1));
		pCentral.add(pPago);
		
		
		//Panel Datos
		JPanel pTitulo = new JPanel();
		JXLabel lTitulo = new JXLabel();
        lTitulo.setText("<html><h2>Tus Datos</h2></html>");
		pTitulo.add(lTitulo, BorderLayout.CENTER);
		pDatos.add(pTitulo);
		
		JPanel pNombre = new JPanel();
		JLabel lNombre = new JLabel("Nombre");
		tfNombre = new JTextField();
		tfNombre.setText(usuario.getNombreUsuario());
		pNombre.add(lNombre);
		pNombre.add(tfNombre);
		pDatos.add(pNombre);
		
		JPanel pCorreo = new JPanel();
		JLabel lCorreo = new JLabel("Correo");
		tfCorreo = new JTextField();
		tfCorreo.setText(usuario.getCorreoUsuario());
		pCorreo.add(lCorreo);
		pCorreo.add(tfCorreo);
		pDatos.add(pCorreo);
		
		JPanel pTfno = new JPanel();
		JLabel lTfno = new JLabel("Teléfono");
		tfTfno = new JTextField();
		pTfno.add(lTfno);
		pTfno.add(tfTfno);
		pDatos.add(pTfno);
		
		
		//Panel Método de Pago
		JPanel pMetodoPago = new JPanel();
		JXLabel lMetodoPago = new JXLabel();
        lMetodoPago.setText("<html><h2>Método de Pago</h2></html>");
        pMetodoPago.add(lMetodoPago);
		pPago.add(pMetodoPago);
		
		pNTarjeta = new JPanel();
		JLabel lNtarjeta = new JLabel("Nº tarjeta");
		tfNtarjeta = new JTextField();
		pNTarjeta.add(lNtarjeta);
		pNTarjeta.add(tfNtarjeta);
		pPago.add(pNTarjeta);
				
		JPanel pFCad = new JPanel();
		JLabel lFechaCad = new JLabel("Fecha de caducidad");
		pFCad.add(lFechaCad);
		pPago.add(pFCad);
		
		//Componente para el CCV
		JPanel pCCV = new JPanel();
		JLabel lCCV = new JLabel("CCV");
		tfCCV = new JTextField();
		pCCV.add(lCCV);
		pCCV.add(tfCCV);
		pPago.add(pCCV);
		
		JPanel pBotonD = new JPanel();
		JButton botonVerificar = new JButton("Verificar Tarjeta");
		pBotonD.add(botonVerificar);
		pPago.add(pBotonD);
		
		botonVerificar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (verificarCampoCCV() && verificarCampoTarjeta()) {
		            agregarCheck(tfCCV);
					agregarCheck(tfNtarjeta);
		        }
				if(tfCCV.getBackground() == Color.RED || tfNtarjeta.getBackground() == Color.RED) {
			        mostrarError(errorPane, "El número de TARJETA debe tener 16 dígitos y en CCV 3 dígitos.\n Además solo debe contener números.");
				}else {
					// Símbolo de check (✔)
				    String checkSymbol = "\u2713";
				    JOptionPane.showMessageDialog(null, "Campos Correctos" + " " + checkSymbol, "Éxito", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});	
		
		this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarComponentes();
            }
		});
		
		JPanel pFC = new JPanel(new GridLayout(1,1));
		String[] meses = {"MES", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
		cbMes = new JComboBox<>(meses);
		String[] anyos = {"AÑO", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031"};
		cbAnyo = new JComboBox<>(anyos);

 		pFC.add(cbMes);
		pFC.add(cbAnyo);
		pFCad.add(pFC);
		
		
		//Panel Lateral
		//Creamos 2 paneles
		
		JPanel pTiempo = new JPanel(new GridLayout(3,1));
		pDer.add(pTiempo, BorderLayout.NORTH);
		JPanel pConfirmar = new JPanel();
		pDer.add(pConfirmar, BorderLayout.SOUTH);

		// Añadir enlace a terminus y condiciones usando JXHyperlink
        JXHyperlink hyperlinkTerminos = new JXHyperlink();
        hyperlinkTerminos.setText("Términos y Condiciones");
        hyperlinkTerminos.addActionListener(e -> mostrarTerminos());
        pConfirmar.add(hyperlinkTerminos);

        // Añadir indicador de actividad con JXBusyLabel;
        busyLabel = new JXBusyLabel();
        busyLabel.setBusy(true);
        busyLabel.setPreferredSize(new Dimension(25, 25));
        pConfirmar.add(busyLabel);
		
		//Panel Tiempo restante y Total
		lTiempo = new JLabel("Tiempo restante: 10:00");
		lTiempo.setHorizontalAlignment(SwingConstants.CENTER);
		pTiempo.add(lTiempo);
		
		tiempoRestante = 600; //10 minutos en segundos
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actualizarTiempo();
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				timer.start();
			}
		});
		
		JXLabel lTotal = new JXLabel();
		double precioTotal = bd.obtenerPrecioEntradaReventa(idReventa);
//		double precioTotal = precioReventa;
        lTotal.setText("<html><h2>TOTAL: "+ precioTotal + "€</h2></html>");
        pTiempo.add(lTotal);
		
		
		//Panel Confirmar
		JButton bConfirmar = new JButton("Confirmar compra");
		bConfirmar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		pConfirmar.add(bConfirmar);
		
		JButton bVolver = new JButton("Volver");
		bVolver.setFont(new Font("Tahoma", Font.PLAIN, 15));
		pConfirmar.add(bVolver);
		
		bVolver.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 busyLabel.setBusy(false);
				 VentanaCompraReventa.this.dispose();
				 VentanaReventa vre = new VentanaReventa(eventoActual, vPrincipal);
				 vre.setVisible(true);
			}
		});
		
		bConfirmar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				confirmarCompra();
			}
		});
		
		ajustarComponentes();
	}
	
	
	private boolean verificarCampoTarjeta() {
	    // Verificar longitud y si son solo números
	    String numeroTarjeta = tfNtarjeta.getText();
	    String ccv = tfCCV.getText();

	    if (numeroTarjeta.length() != 16 || !esNumero(numeroTarjeta)) {
	        tfNtarjeta.setBackground(Color.RED);
	        return false;
	    }
	    return true;
	}
	private boolean verificarCampoCCV() {
		// Verificar longitud y si son solo números
	    String numeroTarjeta = tfNtarjeta.getText();
	    String ccv = tfCCV.getText();
		if (ccv.length() != 3 || !esNumero(ccv)) {
	        tfCCV.setBackground(Color.RED);
	        return false;
	    }
	    return true;
	}
	private boolean esNumero(String str) {
	    // Verificar si una cadena contiene solo números
	    return str.matches("\\d+");
	}

	private void mostrarTerminos() {
	    // Crear un JTextArea para mostrar los términos y condiciones
	    JTextArea textArea = new JTextArea(
	            "Términos y Condiciones:\n\n" +
	            		"1.Aceptación de Términos:\r\n"
	                    + "\r\n"
	                    + "Al utilizar esta aplicación, aceptas todos los términos y condiciones establecidos en este documento.\r\n"
	                    + "2.Registro de Usuario:\r\n"
	                    + "\r\n"
	                    + "Debes proporcionar información precisa y actualizada durante el proceso de registro.\r\n"
	                    + "Eres responsable de mantener la confidencialidad de tu cuenta y contraseña.\r\n"
	                    + "3.Eventos y Entradas:\r\n"
	                    + "\r\n"
	                    + "Las empresas son responsables de la precisión de la información sobre eventos y entradas que publican en la plataforma.\r\n"
	                    + "La aplicación no garantiza la disponibilidad de entradas y no es responsable de cambios en eventos o cancelaciones.\r\n"
	                    + "4.Compra y Venta de Entradas:\r\n"
	                    + "\r\n"
	                    + "Los usuarios pueden comprar y vender entradas a través de la plataforma.\r\n"
	                    + "El precio de reventa no debe exceder un límite establecido por la aplicación.\r\n"
	                    + "5.Política de Devoluciones y Reembolsos:\r\n"
	                    + "\r\n"
	                    + "Las políticas de devolución y reembolso son determinadas por las empresas organizadoras de eventos.\r\n"
	                    + "La aplicación puede tener políticas específicas para ciertos casos.\r\n"
	                    + "6.Comisiones y Tarifas:\r\n"
	                    + "\r\n"
	                    + "La aplicación puede cobrar comisiones por transacciones exitosas.\r\n"
	                    + "Las tarifas asociadas con la compra y venta de entradas deben ser transparentes para los usuarios.\r\n"
	                    + "7.Responsabilidad:\r\n"
	                    + "\r\n"
	                    + "La aplicación no se hace responsable de la calidad del evento, la conducta de los asistentes o cualquier problema relacionado con el lugar del evento.\r\n"
	                    + "8.Uso Adecuado:\r\n"
	                    + "\r\n"
	                    + "Los usuarios deben utilizar la aplicación de manera legal y ética.\r\n"
	                    + "No se permite el uso de la aplicación para actividades ilegales o fraudulentas.\r\n"
	                    + "9.Contenido Generado por Usuarios:\r\n"
	                    + "\r\n"
	                    + "Los usuarios son responsables de cualquier contenido que publiquen en la plataforma.\r\n"
	                    + "La aplicación se reserva el derecho de eliminar contenido que viole los términos y condiciones.\r\n"
	                    + "10.Modificaciones de Términos:\r\n"
	                    + "\r\n"
	                    + "La aplicación se reserva el derecho de modificar estos términos y condiciones en cualquier momento.\r\n"
	                    + "11.Terminación de Cuenta:\r\n"
	                    + "\r\n"
	                    + "La aplicación puede suspender o cerrar cuentas que violen los términos y condiciones.\r\n"
	                    + "12.Ley Aplicable:\r\n"
	                    + "\r\n"
	                    + "Estos términos están sujetos a las leyes de España."
	    );

	    // Configurar el JTextArea
	    textArea.setEditable(false);
	    textArea.setLineWrap(true);
	    textArea.setWrapStyleWord(true);

	    // Colocar el JTextArea en un JScrollPane para permitir el desplazamiento
	    JScrollPane scrollPane = new JScrollPane(textArea);
	    scrollPane.setPreferredSize(new Dimension(400, 300));

	    // Mostrar el cuadro de diálogo con los términos y condiciones
	    JOptionPane.showMessageDialog(this, scrollPane, "Términos y Condiciones", JOptionPane.INFORMATION_MESSAGE);
	}

	private void actualizarTiempo() {
		tiempoRestante--;
		if(tiempoRestante >= 0) {
			int minutos = tiempoRestante/60;
			int segundos = tiempoRestante%60;
			
			DecimalFormat formato = new DecimalFormat("00");
			lTiempo.setText("Tiempo restante: " + formato.format(minutos) + ":" + formato.format(segundos));
		}else {
			timer.stop();
			JOptionPane.showMessageDialog(null, "Límite de tiempo excedido. Por favor, inténtalo de nuevo", "Tiempo agotado", JOptionPane.ERROR_MESSAGE);
			VentanaCompraReventa.this.dispose();
			Evento eventoActual = vReventa.getEvento();
			VentanaEvento ve = new VentanaEvento(eventoActual, vPrincipal);
			ve.setVisible(true);
		}
	}
	
	private void pararTemporizador() {
		timer.stop();
	}
	private void confirmarCompra() {

		if (tfNombre.getText().isEmpty() || tfCorreo.getText().isEmpty() || tfTfno.getText().isEmpty() ||
	            tfNtarjeta.getText().isEmpty() || cbMes.getSelectedIndex() == 0 || cbAnyo.getSelectedIndex() == 0) {
	        JOptionPane.showMessageDialog(null, "Para confirmar la compra debe introducir todos los datos.");
	        return;
	    }
		
		String numeroTarjeta = tfNtarjeta.getText();
		String ccv = tfCCV.getText();
	    boolean esNumeroTarjetaValido = numeroTarjeta.length() == 16;
	    boolean esCCVValido = ccv.length() == 3;

	    if (!esNumeroTarjetaValido || !esCCVValido) {
	        mostrarError(errorPane, "Error al confirmar la compra.\nInténtalo de nuevo.\nRecuerda que el número de TARJETA debe tener 16 dígitos y el CCV debe tener 3 dígitos.");
	    }
	    if(!validarCorreo(tfCorreo.getText())) {
	    	mostrarError(errorPane, "Correo incorrecto. Vuelve a provar");
	    }
	    if(verificarCampoTelefono() == true && validarCorreo(tfCorreo.getText())) {
	    	

	    	
	    	// Contador para rastrear cuántas entradas se han marcado como compradas
	    	List<Entrada> reventas = BaseDeDatos.obtenerListaEntradasReventa();
			int codigoEntrada = idReventa;
	         if (existeCodigoEntradaEnReventas(codigoEntrada, reventas)) {
	        	 bd.borrarEntradaReventa(codigoEntrada);
	             bd.marcarEntradaComoComprada(codigoEntrada, usuario.getCorreoUsuario());
	         }

	    	tfTfno.setBackground(new Color(240, 255, 240));
	        JOptionPane.showMessageDialog(null, "Los datos introducidos son correctos", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
	        JOptionPane.showMessageDialog(null, "¡Compra confirmada!", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
	        
	        Notificacion notificacion = new Notificacion("Has vendido 1 entradas del evento: " + eventoActual.getNombre(), false);
	        String correo_vendedor = BaseDeDatos.obtenerCorreoEntradaReventa(codigoEntrada);
	        Usuario vendedor = BaseDeDatos.getUsuarioPorCorreo(correo_vendedor);
	        if(vendedor != null) {
	        	vendedor.agregarNotificacion(notificacion);
	        }
	        
	        pararTemporizador();
	        dispose();
	        vPrincipal = new VentanaPrincipal();
	        vPrincipal.setVisible(true);
	    }
	}
	private static boolean existeCodigoEntradaEnReventas(int codigoEntrada, List<Entrada> reventas) {
	    for (Entrada entrada : reventas) {
	        if (entrada.getCod() == codigoEntrada) {
	            return true;
	        }
	    }
	    return false;
	}
	private boolean verificarCampoTelefono() {
	    String telefono = tfTfno.getText();
	    if (telefono.length() != 9 || !esNumero(telefono)) {
	        tfTfno.setBackground(Color.RED);
	        return false;
	    }
	    return true;
	}
	private void agregarCheck(JTextField textField) {
		String textoCampo = textField.getText();
	   
		textField.setText(textoCampo);
	    textField.setEditable(false);
	    textField.setBackground(new Color(240, 255, 240)); // Cambiar el fondo a verde claro si se cumple la condición   
	}
	
	private void mostrarError(Component parent, String mensaje) {
	    JOptionPane.showMessageDialog(parent, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void ajustarComponentes() {
	    tfNombre.setPreferredSize(new Dimension(200, 40));
	    tfCorreo.setPreferredSize(new Dimension(200, 40));
	    tfTfno.setPreferredSize(new Dimension(200, 40));
	    tfNtarjeta.setPreferredSize(new Dimension(200, 40));
	    tfCCV.setPreferredSize(new Dimension(80, 40));
	    revalidate();
	    repaint();
	}

	private boolean validarCorreo(String correo) {
        if (correo == null || correo.isEmpty()) {
        	tfCorreo.setBackground(Color.RED);
            return false; // Correo nulo o vacío es inválido
        }

        // Expresión regular para validar un correo electrónico
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        
        if(correo.matches(regex)) {
        	return true;
        } else {
        	tfCorreo.setBackground(Color.RED);
        	return false;
        }
    }
	
	public static void main(String[] args) {
		Usuario u = new Usuario("Laura Lopez","laura.lopez@gmail.com","Usuario corriente","abcABC33", "", "");
		VentanaCompraReventa v = new VentanaCompraReventa(u, null, null, 0);
		v.setVisible(true);
	}

}
