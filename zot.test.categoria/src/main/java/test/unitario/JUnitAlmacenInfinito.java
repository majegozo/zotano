package test.unitario;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import cache.almacen.Almacen;
import cache.almacen.AlmacenInfinito;

public class JUnitAlmacenInfinito {

	@Test
	public void test() {
		int delay = 4 ;
		String primero = "primero" ;
		String segundo = "segundo" ;
		
		Almacen a = new AlmacenInfinito(delay) ;
		
		a.put("first", primero) ;
		a.put("second", segundo) ;
		
		Assert.assertEquals(a.get("first"), primero) ;
		Assert.assertEquals(a.get("second"), segundo) ;
		
		try {
			Thread.currentThread().sleep( (delay + 1)*1000) ;
		} catch(Exception e){
			
		}
		
		Assert.assertEquals(a.get("first"), null) ;
		
	}

}
