package cache.almacen;

import java.util.HashMap;

public abstract class Almacen {

	protected HashMap<String, Item> almacen ;
	protected int segundos ;
	protected int aciertos ;
	protected int fallos ;
	protected int expirados ;
	
	public Almacen(int segundos) {
		this.segundos = segundos ;
		this.almacen = new HashMap<String, Item>() ;
	}
	
	public Object get(String key) {
		if( almacen.containsKey(key)){
			Item e = almacen.get(key) ;
			if( e.getValidUntil() >= System.currentTimeMillis()){
				e.setLastAccess(System.currentTimeMillis()) ;
				aciertos ++ ;
				return e.getElemento() ;
			} else {
				almacen.remove(key) ;
				expirados ++ ;
			}
		} else {
			fallos ++ ;
		} 
		
		return null ;
	}
	
	public void put(String key, Object obj) {
		if( almacen.containsKey(key)){
			Item e = almacen.get(key);
			e.setElemento(obj);
		} else {
			almacen.put(key, new Item(key,obj,System.currentTimeMillis() + segundos * 1000 ) ) ;
			
		}
	}
	
	public void clear() {
		almacen.clear();
	}

	public HashMap<String, Item> getAlmacen() {
		return almacen;
	}

	public int getValidUntil() {
		return segundos;
	}

	public int getAciertos() {
		return aciertos;
	}

	public int getFallos() {
		return fallos;
	}

	public int getExpirados() {
		return expirados;
	}
	
	
}
