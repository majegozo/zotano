package cache.almacen;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

public abstract class Almacen {

	protected Hashtable<String, Item> almacen ;
	protected int segundos ;
	protected int aciertos ;
	protected int fallos ;
	protected int expirados ;
	protected Hebra cleaner ;
	
	protected class Hebra extends Thread{
		Almacen padre ;
		boolean stop = false ;
		Hebra(Almacen padre){
			this.padre = padre ;
		}
		protected void para(){
			this.stop = true ;
		}
		public void run(){
		
			while ( !stop ) {
				try {
					sleep((segundos + 1 ) * 1000) ;
					padre.clean() ;

				} catch(Exception e){
					e.printStackTrace() ;
				}
				
			}
		}
		
	}  
	
	protected void clean(){
		String [] keys = {};
		keys =  almacen.keySet().toArray(keys) ;
		Item e ;
		for( String key : keys) {
			try {
				e = almacen.get(key) ;
				long time = System.currentTimeMillis() ;
				if( e.getValidUntil() < time){
					almacen.remove(key) ;
				}
			} catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	public Almacen(int segundos) {
		this.segundos = segundos ;
		this.almacen = new Hashtable<String, Item>() ;
		this.cleaner = new Hebra(this) ;
		this.cleaner.start() ;
	}
	
	public void stop(){
		if( this.cleaner != null ) 
			this.cleaner.para();
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

	public Hashtable<String, Item> getAlmacen() {
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
