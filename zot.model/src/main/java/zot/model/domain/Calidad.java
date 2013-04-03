package zot.model.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Calidad {

	@Id @GeneratedValue
	private Integer id;
	
	private String url ;
	
	@Enumerated(EnumType.STRING)
	private TipoCalidad tipoCalidad ;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TipoCalidad getTipoCalidad() {
		return tipoCalidad;
	}

	public void setTipoCalidad(TipoCalidad tipoCalidad) {
		this.tipoCalidad = tipoCalidad;
	}
	
	@ManyToOne
	private Video video ;

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	
}
