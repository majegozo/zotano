package zot.model.domain;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class Noticia extends Contenido {
	
	private String subtitulo ;
	private String fuente ;
	
	public String getSubtitulo() {
		return subtitulo;
	}
	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}
	
	public String getFuente() {
		return fuente;
	}
	public void setFuente(String fuente) {
		this.fuente = fuente;
	}
	
	
}
