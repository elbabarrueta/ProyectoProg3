package ventanas;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import BasesDeDatos.BDEventos;
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
//    private VentanaPrincipal ventanaPrincipa;
    

    public VentanaEntradasEnReventaTabla(Usuario usuario) {
        setTitle("Entradas en Reventa");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        this.usuario = usuario;
//        this.ventanaPrincipa = ventanaPrincipal;

        modeloTabla = new ModeloEntradasReventa();
        tablaEntradas = new JTable(modeloTabla);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(new JScrollPane(tablaEntradas), BorderLayout.CENTER);

        JPanel pInferior = new JPanel();
        JButton bAnyadir = new JButton( "Añadir" );
        JButton bActualizar = new JButton("Actualizar Datos");
        JButton bBorrar = new JButton("Borrar");
        pInferior.add(bAnyadir);
        pInferior.add(bBorrar);
        pInferior.add(bActualizar);
        

        panelPrincipal.add(pInferior, BorderLayout.SOUTH);
        add(panelPrincipal, BorderLayout.CENTER);

        bActualizar.addActionListener(e -> cargarDatosEntradas());
        bBorrar.addActionListener(e -> borrarEntradaReventa());
        bAnyadir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				VentanaReventaUsuario v = new VentanaReventaUsuario(usuario);
				v.setVisible(true);
			}
		});
        cargarDatosEntradas();
        setVisible(true);
        
    }

    private void cargarDatosEntradas() {
        List<EntradaReventa> entradasReventa = BDEventos.obtenerEntradasReventa(usuario.getCorreoUsuario()); // Ajusta este método para obtener solo las entradas del usuario
        modeloTabla.setDatos(entradasReventa);
//        ventanaPrincipa.cargarDatosEntradas();
    }

    private void borrarEntradaReventa() {
        int filaSeleccionada = tablaEntradas.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int codigoEntrada = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Deseas borrar esta entrada de la reventa?", "Confirmar Borrado", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                BDEventos.borrarEntradaReventa(codigoEntrada); // Implementa este método en tu clase de conexión a la base de datos
                modeloTabla.removeRow(filaSeleccionada);
//                ventanaPrincipa.cargarDatosEntradas();
            }
        }
    }
    
   


    private class ModeloEntradasReventa extends AbstractTableModel {
        private List<EntradaReventa> datos;
        private final String[] columnNames = {"Código Entrada", "Precio Reventa", "Correo del Vendedor"};

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
                default:
                    return null;
            }
        }

        public void removeRow(int rowIndex) {
            if (datos != null && rowIndex >= 0 && rowIndex < datos.size()) {
                datos.remove(rowIndex);
                fireTableRowsDeleted(rowIndex, rowIndex);
            }
        }
    }

    public static void main(String[] args) {
        new VentanaEntradasEnReventaTabla(new Usuario()).setVisible(true);
    }
}
