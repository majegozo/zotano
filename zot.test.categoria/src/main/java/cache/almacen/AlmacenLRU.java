package cache.almacen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cache.almacen.Almacen.Hebra;

public class AlmacenLRU extends AlmacenLimitacionObjetos {

	
	
	public AlmacenLRU(int segundos, int maxNumObjetos) {
		super(segundos,maxNumObjetos) ;
		
		almacen = (HashMap<String, Item>) new LinkedHashMap<String,Item>(1, .75f, true) {
		      // (an anonymous inner class)
		      private static final long serialVersionUID = 1;
		      @Override protected boolean removeEldestEntry (Map.Entry<String,Item> eldest) {
		         return size() > AlmacenLRU.this.maxNumObjetos; 
		      }
		}; 
		
	}

	
}
