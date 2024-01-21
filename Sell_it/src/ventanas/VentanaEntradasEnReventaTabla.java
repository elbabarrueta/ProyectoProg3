package ventanas;

import javax.swing.*;

import javax.swing.table.AbstractTableModel;

import BasesDeDatos.BaseDeDatos; // Importa tu clase de conexión a la base de datos
import clases.EntradaReventa;
import clases.Usuario;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaEntradasEnReventaTabla extends JFrame {
    private JTable tablaEntradas;
    private ModeloEntradasReventa modeloTabla;
    private Usuario usuario;
    private List<String> entradas;    

    // Constructor de la clase
    public VentanaEntradasEnReventaTabla(Usuario usuario) {
        // Configuración de la ventana
    	setTitle("Entradas en Reventa");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Configuración del tamaño y posición de la ventana
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Tamaño de la ventana
        int ventanaAncho = 700;
        int ventanaAlto = 400;
        // Calcular la posición para centrar la ventana
        int posX = (screenSize.width - ventanaAncho) / 2;
        int posY = (screenSize.height - ventanaAlto) / 2;
        // Establecer la posición de la ventana
        this.setBounds(posX, posY, ventanaAncho, ventanaAlto);
        
        // Inicialización de componentes
        setLayout(new BorderLayout());
        this.usuario = usuario;
        modeloTabla = new ModeloEntradasReventa();
        tablaEntradas = new JTable(modeloTabla);

        // Creación de paneles y botones
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(new JScrollPane(tablaEntradas), BorderLayout.CENTER);
        JPanel pInferior = new JPanel();
        JButton bAnyadir = new JButton( "Añadir" );
        JButton bBorrar = new JButton("Borrar");
        JButton bVolver = new JButton("Volver");
        pInferior.add(bAnyadir);
        pInferior.add(bBorrar);
		pInferior.add(bVolver);
        panelPrincipal.add(pInferior, BorderLayout.SOUTH);
        add(panelPrincipal, BorderLayout.CENTER);

        // Configuración de eventos para los botones
        bBorrar.addActionListener(e -> borrarEntradaReventa());
        bVolver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				VentanaPerfilUsuario vpu = new VentanaPerfilUsuario(usuario, entradas);
				vpu.setVisible(true);
			}
		});
       
        bAnyadir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				VentanaReventaUsuario v = new VentanaReventaUsuario(usuario);
				v.setVisible(true);
			}
		});
        
        // Carga de datos en la tabla
        cargarDatosEntradas();
        setVisible(true);
    }

    // Método para cargar datos de entradas en reventa desde la base de datos
    private void cargarDatosEntradas() {
        List<EntradaReventa> entradasReventa = BaseDeDatos.obtenerEntradasReventa(usuario.getCorreoUsuario()); // Ajusta este método para obtener solo las entradas del usuario
        modeloTabla.setDatos(entradasReventa);
    }

    // Método para borrar una entrada de reventa seleccionada
    private void borrarEntradaReventa() {
        int filaSeleccionada = tablaEntradas.getSelectedRow();
                
        if (filaSeleccionada >= 0) {
            int codigoEntrada = (int) tablaEntradas.getValueAt(filaSeleccionada, 0);
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Deseas borrar esta entrada de la reventa?\n Volveras a ser el propietario", "Confirmar Borrado", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
            	BaseDeDatos.borrarEntradaReventa(codigoEntrada); 
                
            	BaseDeDatos.marcarEntradaComoComprada(codigoEntrada, usuario.getCorreoUsuario()); //propietario otra vez el usuario que la puso en reventa

                modeloTabla.removeRow(filaSeleccionada);

            }
        }
    }
    
    // Clase interna que define el modelo de la tabla
    private class ModeloEntradasReventa extends AbstractTableModel {
        private List<EntradaReventa> datos;
        private final String[] columnNames = {"Código Entrada", "Precio Reventa", "Correo del Vendedor", "Informacion entrada"};

        // Método para establecer los datos en el modelo
        public void setDatos(List<EntradaReventa> datos) {
            this.datos = datos;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return datos != null ? datos.size() : 0;
        }
        @Override
        public int getColumnCount() {
            return columnNames.length;
        }
        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (datos == null || rowIndex >= datos.size()) {
                return null;
            }
            EntradaReventa entrada = datos.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return entrada.getCodigoEntrada();
                case 1:
                    return entrada.getPrecioReventa();
                case 2:
                    return entrada.getCorreoUsuario();
                case 3: 
                	return entrada.getInfo();
                default:
                    return null;
            }
        }

        // Método para eliminar una fila del modelo
        public void removeRow(int rowIndex) {
            if (datos != null && rowIndex >= 0 && rowIndex < datos.size()) {
                datos.remove(rowIndex);
                fireTableRowsDeleted(rowIndex, rowIndex);
            }
        }
    }
    
    // Método main para probar la ventana
    public static void main(String[] args) {
        new VentanaEntradasEnReventaTabla(new Usuario()).setVisible(true);
    }
}
