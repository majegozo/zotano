package test.unitario;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import cache.almacen.Almacen;
import cache.almacen.AlmacenInfinito;
import cache.almacen.AlmacenLRU;
import cache.almacen.AlmacenLRUUltimoAcceso;

public class JUnitAlmacenLRUUltimoAcceso {

	@Test
	public void test() {
		int delay = 4 ;
		int size = 2 ;
		String primero = "primero" ;
		String segundo = "segundo" ;
		String tercero = "tercero" ;
		
		Almacen a = new AlmacenLRUUltimoAcceso(delay, size) ;
		
		a.put("first", primero) ;
		a.put("second", segundo) ;
		
		Assert.assertEquals(a.get("first"), primero) ;
		a.put("third", tercero) ;
		
		System.out.println(a.toString());
		Assert.assertEquals(a.get("second"), null) ;
		Assert.assertEquals(a.get("first"), primero) ;
		
		try {
			Thread.currentThread().sleep( (delay + 1)*1000) ;
		} catch(Exception e){
			
		}
		
		Assert.assertEquals(a.get("first"), null) ;
		
	}

}
