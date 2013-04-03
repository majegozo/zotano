package zot.model.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Video extends Contenido {
	
	@Column(length=4096)
	private String descripcion ;
	private String thumbnail ;
	
	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@OneToMany(mappedBy="video")
	private List<Calidad> calidades ;

	public List<Calidad> getCalidades() {
		return calidades;
	}

	public void setCalidades(List<Calidad> calidades) {
		this.calidades = calidades;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	

}
