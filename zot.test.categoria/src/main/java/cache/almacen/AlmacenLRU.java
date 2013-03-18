package cache.almacen;

import java.util.ArrayList;
import java.util.List;

public class AlmacenLRU extends AlmacenLimitacionObjetos {

	List<String> lista ;
	public AlmacenLRU(int segundos, int maxNumObjetos) {
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
	
}
