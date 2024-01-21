package ventanas;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import BasesDeDatos.BaseDeDatos;
import clases.Entrada;
import clases.EntradaReventa;
import clases.Evento;
import clases.Usuario;

public class VentanaPrincipal extends JFrame{

		private JXTable tablaEventos;
		private VentanaInicio vent;
		private List<VentanaEvento> listaEventos;
		private JPanel pnlCentro = new JPanel();
		private JPanel pEventosDestacados = new JPanel(new FlowLayout(FlowLayout.LEFT));
		private JPanel pTodosEventos = new JPanel(new FlowLayout(FlowLayout.LEFT));
		private JPanel pEventosReventa = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    private JXSearchField searchField;
	    private static JLabel lblImagen;
	    private static VentanaPrincipal vPrincipal;
	    private HashMap<Evento, Integer> eventosBuscados = new HashMap<>();
	    	    
	    private static BaseDeDatos baseDeDatos; // Nueva referencia a la clase BaseDeDatos
	    
	    public VentanaPrincipal(){
			
	    	baseDeDatos = new BaseDeDatos();
	    	cargarEventosDesdeBD();
	        cargarEventosBuscados();

			
			JPanel pnlNorte = new JPanel();
			pnlNorte.setLayout(new FlowLayout());
			searchField = new JXSearchField("¡Busca el evento que desees!");
			pnlNorte.add(searchField);

			
			JPanel pnlSur = new JPanel();
			JButton bVenta = new JButton("Venta");
			JButton bPerfil = new JButton("Perfil");
			pnlSur.add(bVenta);
			pnlSur.add(bPerfil);
			
			this.add(pnlNorte, BorderLayout.NORTH);
			this.add(pnlSur, BorderLayout.SOUTH);
			
			JScrollPane scrollCentro = new JScrollPane();
			DefaultTableModel modelo = new DefaultTableModel();
			modelo.addColumn("Código");
			modelo.addColumn("Nombre");

	        // Configuración de la tabla con SwingX
			tablaEventos = new JXTable();
	        tablaEventos.setHighlighters(HighlighterFactory.createSimpleStriping());
	        tablaEventos.setColumnControlVisible(true);
	        add(new JScrollPane(tablaEventos), BorderLayout.CENTER);
			//JTable tbl_buscar = new JTable();
			

	        // Crear paneles para cada sección de eventos
			JPanel pDestacado = crearPanelEventos("Lo más buscado", pEventosDestacados);
			JPanel pEventos = crearPanelEventos("Todos los eventos", pTodosEventos);
			JPanel pReventa = crearPanelEventos("Entradas de reventa", pEventosReventa);
			
			// Crear un panel principal con un BoxLayout y desplazamiento horizontal
			JPanel pnlCentro = new JPanel();
			pnlCentro.setLayout(new BoxLayout(pnlCentro, BoxLayout.Y_AXIS));
	
			// Agregar los paneles de eventos al panel principal
			pnlCentro.add(pDestacado);
			pnlCentro.add(pEventos);
			pnlCentro.add(pReventa);
	
			JScrollPane scrollPane = new JScrollPane(pnlCentro);	
			getContentPane().add(scrollPane, BorderLayout.CENTER);


//SearchField que actualiza los eventos visualizados
			searchField.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                String textoBusqueda = searchField.getText().toLowerCase();
	            	List<Evento> eventosFiltrados = filtrarEventosPorPalabrasClave(textoBusqueda);
	                actualizarVisualizacionEventos(eventosFiltrados);
	            }
	        });
			
//ActionListenner que abre la ventanaPerfilUsuario o ventanaPrefilEntidad
			bPerfil.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					VentanaInicio ventanaI = Main.getVentanaInicio();
					Usuario usuActual = ventanaI.getUsuarioActual();
					String tipoUsu = usuActual.getTipoUsuario();
					if("Usuario corriente".equals(tipoUsu)) {
						dispose();
						VentanaPerfilUsuario ventanaPerfilUsuario = new VentanaPerfilUsuario(usuActual, null);
						
					}else {
						dispose();
						VentanaPerfilEntidad ventanaPerfilEntidad = new VentanaPerfilEntidad(usuActual);	
						
					}
				}
			});
			
			//ActionListenner que abre la ventanaVentaNormal o ventanaVentaEntidad
			bVenta.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e) {
			        VentanaInicio ventanaI = Main.getVentanaInicio();
			        Usuario usuActual = ventanaI.getUsuarioActual();
			        String tipoUsu = usuActual.getTipoUsuario();

			        if ("Usuario corriente".equals(tipoUsu)) {
			            
			            dispose();
			            VentanaReventaUsuario ventanaVentaNormal = new VentanaReventaUsuario(usuActual);
			            ventanaVentaNormal.setVisible(true);
			        } else {
			           
			            dispose();
			            VentanaVentaEntidad ventanaVentaEntidad = new VentanaVentaEntidad(usuActual);
			            ventanaVentaEntidad.setVisible(true);
			        }
			    }
			});
			
			List<Evento> todosLosEventos = BaseDeDatos.obtenerListaEventos();
	    	List<Evento> destacados = getEventosDestacados(5);
	    	List<Evento> eventosReventa = getEventosDeReventa();
	    	
	    	visualizarEventos(destacados, pEventosDestacados);
	    	visualizarEventos(todosLosEventos, pTodosEventos);
	    	visualizarEventos(eventosReventa, pEventosReventa);
	    	
			this.setBounds(55, 50, 1200, 600);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setTitle("Menu Principal");
			
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);

			this.setVisible(true);
			vPrincipal = this;
			
			//Metodo para guardar cuantas veces se han buscado los eventos
			addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent e) {
	            	guardarEventosBuscados();
	            }
	        });
		}	
	    
	    //Metodo que guarda los eventos buscados
	    
	    private void guardarEventosBuscados() {
	    	try { 
	    		FileOutputStream fout = new FileOutputStream("eventosBuscados.dat");
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject( eventosBuscados );
				System.out.println("Eventos buscados guardados correctamente");
				System.out.println("Contenido de eventosBuscados antes de guardar: " + eventosBuscados);
				oos.close();
	    	}catch (IOException e) {
	    		System.out.println("Error al guardar los eventos buscados");
	    		e.printStackTrace();
	    	}
	    }
	    
	    //Metodo que carga los eventos buscados
	    private void cargarEventosBuscados() {
	    	try {
	    		FileInputStream fin = new FileInputStream("eventosBuscados.dat");
	    		ObjectInputStream ois = new ObjectInputStream(fin);
	    		eventosBuscados = (HashMap<Evento, Integer>) ois.readObject();
				System.out.println("Eventos buscados cargados correctamente");
	    		ois.close();
    		} catch (Exception e) { 
    			System.out.println("Error al cargar los eventos buscados");
	    		e.printStackTrace();
    		}
	    }
	    
	    //Metodo que crea el panelEventos y establece sus caracteristicas
	    private JPanel crearPanelEventos(String titulo, JPanel panelEventos) {
	        JPanel panel = new JPanel();
	        panel.setLayout(new BorderLayout());

	        JLabel lblTitulo = new JLabel("       " + titulo);
	        lblTitulo.setFont(new Font("Rockwell", Font.ITALIC, 18));
	        panel.add(lblTitulo, BorderLayout.NORTH);

	        panel.add(panelEventos, BorderLayout.CENTER);

	        return panel;
	    }
	    
	    //metodo que visualiza los eventos
	    public void visualizarEventos(List<Evento> eventos, JPanel panel) {
	    	panel.removeAll();
	    	if(eventos != null) {
	    		for(Evento evento: eventos) {
	    			if(panel.equals(pEventosReventa)) {
			    		Mipanel pnlActual = new Mipanel(evento, true);
			    		panel.add(pnlActual);
	    			}else {
			    		Mipanel pnlActual = new Mipanel(evento, false);
			    		panel.add(pnlActual);
	    			}
		    	}
	    	}
	    	panel.revalidate(); 
	        panel.repaint();
	    }
	    
	    //Metodo que carga los eventos de la base de datos
	    public List<Evento> cargarEventosDesdeBD() {

	        List<Evento> listaEventos = BaseDeDatos.obtenerListaEventos();

	        return listaEventos;

	    }
	    
	    //Metodo que devuelve los eventos destacados
	    private List<Evento> getEventosDestacados(int cantidad) {
	    	return eventosBuscados.entrySet()
	                .stream()
	                .sorted((entry1, entry2) -> {
	                    int comparacionPorBusquedas = Integer.compare(entry2.getValue(), entry1.getValue());
	                    // Si la cantidad de búsquedas es la misma, comparar por el nombre en orden alfabético
	                    if (comparacionPorBusquedas == 0) {
	                        return entry1.getKey().getNombre().compareTo(entry2.getKey().getNombre());
	                    }
	                    return comparacionPorBusquedas;
	                })
	                .limit(cantidad)
	                .map(Map.Entry::getKey)
	                .collect(Collectors.toList());
		}

	    //metodo que filtra los eventos por palabras
	    private List<Evento> filtrarEventosPorPalabrasClave(String palabrasClave) {
	        List<Evento> eventosFiltrados = new ArrayList<>();
	        for (Evento evento : BaseDeDatos.obtenerListaEventos()) {
	            String nombreEvento = evento.getNombre().toLowerCase();
	            String descripcionEvento = evento.getDesc().toLowerCase();

	            // Verificar si el texto de búsqueda está presente en el nombre o la descripción
	            if (nombreEvento.contains(palabrasClave) || descripcionEvento.contains(palabrasClave)) {
	                eventosFiltrados.add(evento);
	            }
	        }
	        return eventosFiltrados;
	    }
	    
	 // Método para actualizar la visualización de eventos en la ventana
	    private void actualizarVisualizacionEventos(List<Evento> eventos) {

	    	List<Evento> destacados = getEventosDestacados(5);
	    	ArrayList<Evento> filtradosDes = new ArrayList<>();
	    	if(destacados != null) {
	    		for(Evento e: destacados) {
		    		if(eventos.contains(e)) {
		    			filtradosDes.add(e);
		    		}
		    	}
	    	}
	        visualizarEventos(filtradosDes, pEventosDestacados);
	    	visualizarEventos(eventos, pTodosEventos);
	    	List<Evento> reventa = getEventosDeReventa();
	    	List<Evento> filtradoRev = new ArrayList<>();
	    	if(reventa != null) {
	    		for(Evento e: eventos) {
		    		if(reventa.contains(e)) {
		    			filtradoRev.add(e);
		    		}
		    	}
	    	}
	    	visualizarEventos(filtradoRev, pEventosReventa);
	    }
	    
	    //metodo que obtiene los eventos de reventa
		private List<Evento> getEventosDeReventa() {
			List<Evento> eventosReventa = new ArrayList<>();
			List<Entrada> entradasReventa = BaseDeDatos.obtenerListaEntradasReventa();
	    	for(Entrada e : entradasReventa) {
	    		Evento eventoAsociado = e.getEventoAsociado();
	    		eventoAsociado.setNombre(eventoAsociado.getNombre() + "-" + e.getCod());
	    		eventosReventa.add(eventoAsociado);
	    	}
			return eventosReventa;
		}
//Metodo que obtiene el tipo de usuario
		private String obtenerTipoUsuario(String nom) {
			Usuario usuario = baseDeDatos.getUsuarioPorCorreo(nom);
			if(usuario != null) {
				return usuario.getTipoUsuario();
			}else {
	           return JOptionPane.showInputDialog("Usuario no encontrado");
			}
		}
			
		
		
		private int numCol;  
		private final int NUM_COLS = 3;  
		private JPanel pnlActual = null;   
		public void empezarPanel() {
			numCol = 2; 
			pnlCentro.removeAll(); 
		}
		

		public void acabarPanel() {
			pnlCentro.revalidate();
			pnlCentro.repaint();
		}
		


		
		private class Mipanel extends JPanel {
			public Mipanel(Evento evento, boolean esReventa) {
		        setLayout(null);
		        setPreferredSize(new Dimension(350, 250)); // Establece el tamaño preferido del panel principal
		        addMouseListener(new MouseAdapter() {
		        	@Override
					public void mouseClicked(MouseEvent e) {
		        		if(eventosBuscados.containsKey(evento)) {
		                    eventosBuscados.put(evento, eventosBuscados.get(evento) + 1);
		        		}else {
		        			eventosBuscados.put(evento, 1);
		        		}
		        		if(esReventa) {
		        			
		        			VentanaReventa vcer = new VentanaReventa(evento, vPrincipal);
		        			vcer.setVisible(true);
		        			vPrincipal.dispose();

		        		}else {
							VentanaEvento v = new VentanaEvento(evento, vPrincipal);
							v.setVisible(true);
							vPrincipal.dispose();	
		        		}
					}
				});
    			String[] partes = evento.getNombre().split("-");
		        String titulo = partes[0];
		        String fecha = evento.getFecha();
		        String rutaImagen = evento.getRutaImg();
		        
		        JLabel lblNombre = new JLabel(titulo);
				lblNombre.setForeground(Color.BLACK);
				lblNombre.setFont(new Font("Eras Demi ITC", Font.PLAIN, 16));
				lblNombre.setBounds(10, 185, 331, 34);
				add(lblNombre);
				
				JLabel lblFecha = new JLabel(fecha);
				lblFecha.setForeground(Color.BLACK);
				lblFecha.setFont(new Font("Eras Demi ITC", Font.PLAIN, 12));
				lblFecha.setBounds(10, 165, 331, 34);
				add(lblFecha);
				
		        


		        // Mostrar imagen a la derecha
		        lblImagen = new JLabel();
		        cargarImagen(rutaImagen);
//		        lblImagen.setBounds(0, 0, 252, 182);
		        lblImagen.setBounds(10, 14, 331, 150);
				add(lblImagen);
		    }
//Metodo que carga la imagen
		    private void cargarImagen(String rutaImagen) {
		    	ImageIcon imagen;
		    	if (rutaImagen != null) {
		            try {
		                imagen = new ImageIcon(rutaImagen);
		                fotoPerfil(imagen);
		            } catch (Exception e) {
		                System.err.println("Error al cargar la imagen: " + rutaImagen);
		                e.printStackTrace();
		                imagen = obtenerImagenPorDefecto();
		            }
		        } else {
		            // No hay ruta de imagen válida, usar imagen por defecto
		            System.out.println("No hay ruta de imagen válida proporcionada. Usando imagen por defecto.");
	                imagen = obtenerImagenPorDefecto();
		        }
		        fotoPerfil(imagen);

		    }
		    //Metodo que obtiene la imagen por defecto
		    private ImageIcon obtenerImagenPorDefecto() {
		        return new ImageIcon(getClass().getResource("/imagenes/default.png"));
		    }
		}
		//Metodo que establece la foto y sus caracteristicas
		private static  void fotoPerfil(ImageIcon imagenPerfil) {
	        int maxWidth = 350; // Tamaño máximo de ancho
	        int maxHeight = 150; // Tamaño máximo de alto
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
	        lblImagen.setIcon(imagenPerfil);
		}
		
		public static void main(String[] args) throws IOException {
		}

	}