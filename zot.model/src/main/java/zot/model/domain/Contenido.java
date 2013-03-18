package zot.model.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Contenido {

	@Id
	private Integer id ;
	private String titulo ;
	private Date fechaCreacion ;
	private Date fechaModificacion ;
	
	@Enumerated(EnumType.STRING)
	private Estado estado ;
	
	@ManyToOne
	private Categoria categoria ;
	
	@ManyToMany
	private List<Categoria> tematicas ;
	
	
	@ManyToMany 
	private List<Contenido> relacionados ;
	
	@ManyToMany (mappedBy="relacionados") 
	private List<Contenido> merelaciono;
	
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	public List<Categoria> getTematicas() {
		return tematicas;
	}
	public void setTematicas(List<Categoria> tematicas) {
		this.tematicas = tematicas;
	}
	
	public void addTematica(Categoria c) {
		if( tematicas == null ) tematicas = new ArrayList<Categoria>();
		if( !tematicas.contains(c) )
			tematicas.add(c) ;
		if( c.getContenidosTematica() == null || !c.getContenidosTematica().contains(this) )
			c.addContenidosTematica(this) ;
	}
	public List<Contenido> getRelacionados() {
		return relacionados;
	}
	public void setRelacionados(List<Contenido> relacionados) {
		this.relacionados = relacionados;
	}
	public List<Contenido> getMerelaciono() {
		return merelaciono;
	}
	public void setMerelaciono(List<Contenido> merelaciono) {
		this.merelaciono = merelaciono;
	}

	
	public void addRelacionado(Contenido c) {
		if( relacionados == null ) relacionados = new ArrayList<Contenido>();
		if( !relacionados.contains(c) )
			relacionados.add(c) ;
		
		if( c.getMerelaciono() == null || !c.getMerelaciono().contains(this) )
			c.addMerelaciono(this) ;
		
	}
	
	public void addMerelaciono(Contenido c) {
		if( merelaciono == null ) merelaciono = new ArrayList<Contenido>();
		if( !merelaciono.contains(c) )
			merelaciono.add(c) ;
		
		if( c.getRelacionados() == null || !c.getRelacionados().contains(this) )
			c.addRelacionado(this) ;
		
	}
	
	
}
