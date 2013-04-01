package zot.repository.cache;

import java.util.Map;

import zot.model.domain.Categoria;
import zot.repository.Repository;
import zot.repository.almacen.Almacen;

public class Cache {

	private Map<String, Object> repositorios;
	private Almacen almacen ;
	private long datos = 0 ;
	
	public Object getRepository(String key){
		return repositorios.get(key) ;
	}

	public Map<String, Object> getRepositorios() {
		return repositorios;
	}

	public void setRepositorios(Map<String, Object> repositorios) {
		this.repositorios = repositorios;
	}

	public Object get(Class cc, Object o) throws ClassNotFoundException {
		
		Object ret = null  ; 
		if( ( ret = almacen.get(cc.getName()+"_"+o.toString()) ) !=null ){
			return ret ;
		}
		
		long temp = System.currentTimeMillis() ;
		Repository<?, Object> rep = (Repository<?, Object>) repositorios.get(cc.getName())  ;
		ret = rep.findById(o) ; 
		almacen.put(cc.getName()+"_"+o.toString(), ret ) ;
		temp = System.currentTimeMillis()-temp + 1 ;
		System.out.println(temp) ;
		datos = datos + temp  ;
		return ret ;
		
	}

	public Almacen getAlmacen() {
		return almacen;
	}

	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
	}

	public long getDatos() {
		return datos;
	}

	public long getAciertos() {
		return almacen.getAciertos() ;
	}
	
	public long getFallos() {
		return almacen.getFallos() ;
	}
}
