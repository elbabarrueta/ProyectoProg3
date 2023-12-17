package BasesDeDatos;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import clases.Evento;
import clases.Usuario;

public class BDEventos {
	private Evento even;
	private static Logger logger = Logger.getLogger("Base de Datos Eventos");
	private static Connection con;
	private static Statement s;
	private static ResultSet rs;
	
//	public static void main(String args[]) {
//		//Decidimos el driver
//		try {
//			Class.forName("org.sqlite.JDBC");
//		} catch (ClassNotFoundException e) {
//			System.out.println("No esta conectada la libreria");
//			return;
//		}
//		//Creamos la conexion
//		try {
//			Connection connection = DriverManager.getConnection("jdbc:sqlite:usuarios.db");
//			//Crear tantas statements como necesitemos
//			Statement s = connection.createStatement();
//			
//			//Creamos tabla usuario
////			if() {
////				s.executeUpdate("create table usuario (nombreUsuario string, correoUsuario string, tipoUsuario string, contrasena string)");
////			}
//			
//			//Rellenamos la tabla usuario con datos
//			s.executeUpdate("insert into usuario values('Laura Lopez','laura.lopez@gmail.com','Usuario corriente','abcABC33')");
//			s.executeUpdate("insert into usuario values('Miguel Diaz','mdiaz@gmail.com','Usuario corriente','mMiaz45#g')"); //terminar de poner comillas simples
//			s.executeUpdate("insert into usuario values('Kepa Galindo','k10galindo@gmail.com','Usuario corriente','GK842aeiou')");
//			s.executeUpdate("insert into usuario values('Discoteca Moma','moma@gmail.com','Usuario entidad','MmMon345627#')");
//			connection.close();
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//			System.out.println("Error en gestion de base de datos");
//		}
//	}
	
	public static void main(String[] args) {
		String com = "";
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:usuarios.db");
			s = con.createStatement();
			try {
				
				// DA IGUAL QUE SEA MAYUSCULA O NO????
				com = "CREATE TABLE EVENTO (codigo int, nombre string, desc string, fecha string,ubicacion string,nEntradas int,precio double,rutaImg string)";
				logger.log(Level.INFO, "BD: " + com);
				s.executeUpdate(com);
			} catch (SQLException e) {
			// Se lanza si la tabla ya existe - no hay problema
			}
		//Estp del admin de la otra base de datos lo quito??
		
		// Ver si existe admin
			
			
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("Último comando: " + com);
			e.printStackTrace();
			}
		}
	public void aniadirNuevoEvento(Evento even) {
		String com = "";
		try {
			// Ver si existe evento
			// Si queremos asegurar el string habría que hacer algo así...
			// String nick = tfUsuario.getText().replaceAll( "'", "''" );
			// ...si no, cuidado con lo que venga en el campo de entrada.
			// "select * from Usuario where nick = 'admin'";
			com = "SELECT *FROM EVENTO WHERE CODIGO = '" + even.getCodigo() + "'";
			logger.log( Level.INFO, "BD: " + com );
			rs = s.executeQuery( com );
			if (!rs.next()) {
				// "insert into Usuario ( nick, pass ) values ('admin', 'admin')";
				com = "INSERT INTO EVENTO (codigo, nombre, desc, fecha,ubicacion,nEntradas,precio,rutaImg) values ('"+ 
						even.getCodigo() +"', '" + even.getNombre() +"', '" + even.getDesc()+"', '" + even.getFecha()+"', '" + even.getUbicacion() +"', '" + even.getnEntradas()+"', '" + even.getRutaImg()+"')";
				logger.log( Level.INFO, "BD: " + com );
				int val = s.executeUpdate( com );
				if (val!=1) {
					JOptionPane.showMessageDialog( null, "Error en inserción" );
				}
			} else {
				JOptionPane.showMessageDialog( null, "Evento " + even.getCodigo() + " ya existe" );
			}
		} catch (SQLException e2) {
			System.out.println( "Último comando: " + com );
			e2.printStackTrace();
		}
}

public void modificarEventoYaRegistradoFecha(Evento even) {

	String sent = "update Evento  set nombre = '" + secu(even.getNombre()) + "' where fecha = '" + secu(even.getFecha()) + "'";
	logger.log(Level.INFO, "BD: " + sent);
	try {
		s.executeUpdate(sent);
	} catch (SQLException e1) {
		logger.log(Level.WARNING, sent, e1);
		e1.printStackTrace();
	}
}
public void modificarEventoYaRegistrado(Evento even) {		
	String sent = "update Evento set nombre= '"+ secu(even.getNombre())+ "' where fecha = '"+ secu(even.getFecha()) + "'";
	String sentNombre = "update Evento set nombre = '"+ secu(even.getNombre())+"' where fecha ='"+ secu(even.getFecha()) + "'";
	logger.log(Level.INFO, "BD: " + sent);
	logger.log(Level.INFO, "BD: " + sentNombre);

	try {
		s.executeUpdate(sent);
		s.executeUpdate(sentNombre);
	} catch (SQLException e1) {
		logger.log(Level.WARNING, sent, e1);
		logger.log(Level.WARNING, sentNombre, e1);

		e1.printStackTrace();
	}
}

public void borrarEventoYaRegistrado(Evento even) {
	if (!even.getNombre().isEmpty() && !even.getFecha().isEmpty()) {
		String com = "";
		try {
			// Borrar evento
			com = "delete from Evento where nombre = '"+ secu(even.getNombre()) +"'";
			logger.log( Level.INFO, "BD: " + com );
			s.executeUpdate( com );
		} catch (SQLException e2) {
			System.out.println( "Último comando: " + com );
			e2.printStackTrace();
		}
	} else {
		JOptionPane.showMessageDialog( null, "Debes rellenar los dos campos" );
	}
}

public void cerrarConexiones() {
	try {
		rs.close();
		s.close();
		con.close();
	} catch (SQLException e2) {
		e2.printStackTrace();
	}
}

// Posible función de "securización" para evitar errores o ataques
	private static String secu( String sqlInicial ) {
		return sqlInicial;
		// Si lo reemplazamos por esto, es mucho más seguro:
		// return sqlInicial.replaceAll( "'", "''" );
	}

	
//visualizar por consola los eventos registrados	
	public void verUsuarios() {
    String com = "select * from Evento";
    logger.log(Level.INFO, "BD: " + com);

    try {
        rs = s.executeQuery(com);

        System.out.println("Eventos en la base de datos:");

        while (rs.next()) {
            String codigoEvento = rs.getString("codigo");
            String nombreEvento = rs.getString("nombre");
            String descEvento = rs.getString("desc");
            String fechaEvento = rs.getString("fecha");
            String ubicacionEvento = rs.getString("ubicacion");
            String nEntradasEvento = rs.getString("nEntradas");
            String precioEvento = rs.getString("precio");
            String rutaImgEvento = rs.getString("rutaImg");

            System.out.println("codigo: " + codigoEvento +
                    ", nombre: " + nombreEvento +
                    ", desc: " + descEvento +
                    ", fecha: " + fechaEvento
                    +
                    ", ubicacion: " + ubicacionEvento
                    +
                    ", nEntradas: " + nEntradasEvento
                    +
                    ", precio: " + precioEvento
                    +
                    ", rutaImg: " + rutaImgEvento);
        }

    } catch (SQLException e) {
        System.out.println("Último comando: " + com);
        e.printStackTrace();
    }
}
	
	
//	private static ArrayList<Usuario> visualizar( Statement s) throws SQLException{
//		ArrayList<Usuario> u = new ArrayList<>();
//		String sent = "select * from usuario";
//		System.out.println(sent);
//		ResultSet rs = s.executeQuery(sent);
//		while(rs.next()) {
//			String nombreUsuario = rs.getString("nombreUsuario");
//			String correoUsuario = rs.getString("correoUsuario");
//			String tipoUsuario = rs.getString("tipoUsuario");
//			String contrasena = rs.getString("contrasena");
//			System.out.println("nombreUsuario = " + nombreUsuario + ", correo = " + correoUsuario + ", tipoUsuario = " + tipoUsuario + " y contrasena = " + contrasena + ".");
//			Usuario usu = new Usuario();
//			usu.nombreUsuario = nombreUsuario;
//			usu.correoUsuario = correoUsuario;
//			usu.tipoUsuario = tipoUsuario;
//			usu.contrasena = contrasena;
//			u.add(usu);
//		}
//		rs.close();
//		return u;
//	}

}
