package cache;

import java.util.Hashtable;

public class CacheTS<K,T> {

	
	private Hashtable<K,Item> ht ;
	private int duracionSegundo = 10 ;
	private int aciertos ;
	private int fallos ;
	private int expirados ;
	
	
	
	public int getDuracionSegundo() {
		return duracionSegundo;
	}

	public void setDuracionSegundo(int duracionSegundo) {
		this.duracionSegundo = duracionSegundo;
	}

	public int getAciertos() {
		return aciertos;
	}

	public void setAciertos(int aciertos) {
		this.aciertos = aciertos;
	}

	public int getFallos() {
		return fallos;
	}

	public void setFallos(int fallos) {
		this.fallos = fallos;
	}

	public int getExpirados() {
		return expirados;
	}

	public void setExpirados(int expirados) {
		this.expirados = expirados;
	}

	private static class Item<T> {
		long timestamp ;
		T value ;
		
		Item(long ts, T value) {
			this.timestamp = ts ;
			this.value = value ;
		}
	}
	
	public void add(K clave, T valor) {
		ht.put(clave, new Item<T>(System.currentTimeMillis(),valor) );
	}
	public CacheTS() {
		ht = new Hashtable<K, Item>() ;
	}
	public T get(K clave) {
		if( ht.containsKey(clave)) {
			Item ite = ht.get(clave) ;
			if( ite.timestamp + ( duracionSegundo * 1000 ) > System.currentTimeMillis() ) {
				aciertos ++ ;
				return (T) ite.value ;
			} else {
				expirados ++ ;
				ht.remove(clave) ;
			}
		}
		fallos ++ ;
		return null ;
	}
}
