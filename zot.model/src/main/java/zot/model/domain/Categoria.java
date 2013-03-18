package zot.model.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Categoria {

	@Id
	private Integer id;
	private String seo ;
	private String nombre ;
	
	@OneToMany(mappedBy="categoria")
	private List<Contenido> contenidos ;
	
	@OneToMany(mappedBy="padre")
	private List<Categoria> hijos ;
	
	@ManyToMany(mappedBy="tematicas")
	private List<Contenido> contenidosTematica ;
	
	@ManyToOne
	private Categoria padre;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSeo() {
		return seo;
	}

	public void setSeo(String seo) {
		this.seo = seo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Contenido> getContenidos() {
		return contenidos;
	}

	public void setContenidos(List<Contenido> contenidos) {
		this.contenidos = contenidos;
	}
	
	public void addContenido(Contenido con) {
		if( contenidos == null) contenidos = new ArrayList<Contenido>() ;
		contenidos.add(con) ;
		con.setCategoria(this) ;
	}

	public List<Categoria> getHijos() {
		return hijos;
	}

	public void setHijos(List<Categoria> hijos) {
		this.hijos = hijos;
	}

	public void addHijo(Categoria cat) {
		if( hijos == null) hijos = new ArrayList<Categoria>() ;
		hijos.add(cat) ;
		cat.setPadre(this) ;
	}

	
	public Categoria getPadre() {
		return padre;
	}

	public void setPadre(Categoria padre) {
		this.padre = padre;
	}

	public List<Contenido> getContenidosTematica() {
		return contenidosTematica;
	}

	public void setContenidosTematica(List<Contenido> contenidosTematica) {
		this.contenidosTematica = contenidosTematica;
	}

	public void addContenidosTematica(Contenido contenido) {
		if( contenidosTematica == null ) contenidosTematica = new ArrayList<Contenido>();
		if( !contenidosTematica.contains(contenido ))
			contenidosTematica.add(contenido) ;
		if( contenido.getTematicas() == null || ! contenido.getTematicas().contains(this) )
			contenido.addTematica(this) ;
		
	}
	
	
	
}
