package clases;

import java.util.ArrayList;

public class Evento {
	//Atributos
	public static int codigo = 0;
	private String nombre;
	private String desc;
	private String fecha;
	private String ubicacion;
	private ArrayList<Entrada> entradasDisponibles;
	private double precio;
	
	
	//Getters y Setters
	public int getCodigo() {
		return codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public ArrayList<Entrada> getEntradasDisponibles() {
		return entradasDisponibles;
	}
	public void setEntradasDisponibles(ArrayList<Entrada> entradasDisponibles) {
		this.entradasDisponibles = entradasDisponibles;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	
	//Constructores
	public Evento(String nombre, String desc, String fecha, String ubicacion, ArrayList<Entrada> entradasDisponibles, double precio) {
		super();
		this.codigo = codigo++;
		this.nombre = nombre;
		this.desc = desc;
		this.fecha = fecha;
		this.ubicacion = ubicacion;
		this.entradasDisponibles = entradasDisponibles;
		this.precio = precio;
	}
	
	@Override
	public String toString() {
		return "Evento [nombre=" + nombre + ", desc=" + desc + ", fecha=" + fecha + ", ubicacion=" + ubicacion
				+ ", entradasDisponibles=" + entradasDisponibles + ", precio=" + precio + "]";
	}
}
