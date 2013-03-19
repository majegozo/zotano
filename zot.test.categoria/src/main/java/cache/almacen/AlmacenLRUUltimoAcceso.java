package cache.almacen;

import java.util.ArrayList;
import java.util.List;

public class AlmacenLRUUltimoAcceso extends AlmacenLimitacionObjetos {

	List<String> lista ;
	public AlmacenLRUUltimoAcceso(int segundos, int maxNumObjetos) {
		super(segundos, maxNumObjetos);
		lista = new ArrayList<String>();	
		// TODO Auto-generated constructor stub
	}

	@Override
	public synchronized void put(String key, Object obj) {
		if( !almacen.containsKey(key) && lista.size()==maxNumObjetos) 
			almacen.remove(lista.remove(0)) ;

		super.put(key, obj) ;
		lista.add(key) ;
	}
	
	@Override
	public Object get(String key) {
		Object o = super.get(key) ;
		if( o != null ){
			lista.remove(key)  ;
			lista.add(key) ;
		}
			
		return o ;
	}
	
	@Override
	public String toString(){
		StringBuffer str = new StringBuffer()  ;
		for( String i : lista)
			str.append("[" + i +"]");
		return str.toString();
	}
}
