package test.unitario;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import cache.almacen.Almacen;
import cache.almacen.AlmacenInfinito;
import cache.almacen.AlmacenLRU;
import cache.almacen.AlmacenLRUUltimoAcceso;

public class JUnitAlmacenLRUUltimoAcceso2 {

	@Test
	public void test() {
		int delay = 4 ;
		int size = 2 ;
		String primero = "primero" ;
		String segundo = "segundo" ;
		String tercero = "tercero" ;
		
		Map cache = new LinkedHashMap(2, .75F, true) {
		    // This method is called just after a new entry has been added
		    public boolean removeEldestEntry(Map.Entry eldest) {
		        return size() > 2;
		    }
		};
		
		
		cache.put("first", primero) ;
		
		cache.put("second", segundo) ;
		
		Assert.assertEquals(cache.get("first"), primero) ;
		
		
		cache.put("third", tercero) ;
		
		Assert.assertEquals(cache.get("second"), null) ;
		
		Assert.assertEquals(cache.get("first"), primero) ;
		
		try {
			Thread.currentThread().sleep( (delay + 1)*1000) ;
		} catch(Exception e){
			
		}
		
		Assert.assertEquals(cache.get("first"), null) ;
		
	}

}
