//aaa
package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cache.almacen.AlmacenInfinito;
import cache.almacen.Almacen;

import zot.model.domain.Categoria;
import zot.model.service.jpa.ModelDao;

public class TestAlmacenLRU {

	private static int times = 100000 ;
	private static int validUntil = 30 ; 
			
	protected static List<String> preparaPrueba(List<Integer> ids, int times) {
		List<String> prueba = new ArrayList<String>() ;
		int numeros  = ids.size()  ;
		Random r = new Random() ;
		r.setSeed(System.currentTimeMillis() * 2) ;
		for( int i = 0 ; i < times ; i++) {
			int posicion =  r.nextInt(numeros-1) ;
			prueba.add(String.valueOf( ids.get(posicion) )  ) ;
		}
		
		return prueba ;
	}
	
	public static void main(String [] args) {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("model-context.xml");
		
		Almacen cache = new AlmacenInfinito(validUntil) ;
		
		ModelDao dao = (ModelDao) ctx.getBean("modelDao") ;
		List<Categoria> listado = dao.todasCategorias() ;
		List<Integer> ids = new java.util.ArrayList<Integer>();
		for(Categoria c : listado) ids.add(c.getId()) ;
		List<String> prueba = preparaPrueba(ids, times) ;
		
		System.out.println("Testing without cache") ;
		long start = System.currentTimeMillis() ;
		for(String id : prueba) {
			Categoria c = (Categoria) dao.lee(Integer.parseInt(id), Categoria.class) ;
		}
		long end = System.currentTimeMillis() ;
		System.out.println( "request " + times + " start " + start + " end " + end + " time spend(ms) " + (end - start ) );
		
		System.out.println("Testing with almacen with valid " + validUntil + " seconds ") ;
		start = System.currentTimeMillis() ;
		Object o;
		for(String id : prueba) {
			o = cache.get(String.valueOf(id)) ; 
			if(  o == null ){
				Categoria c = (Categoria) dao.lee( Integer.parseInt(id), Categoria.class) ;
				cache.put(id,c); 
			} 
		}
		end = System.currentTimeMillis() ;
		
		System.out.println( "request " + times + " start " + start + " end " + end + " time spend(ms) " + (end - start ) );
		System.out.println( "aciertos " + cache.getAciertos()+ " fallos " + cache.getFallos() +
				" expirados " + cache.getExpirados() + " tiempo_cache (sg) " + cache.getValidUntil()  );
		/*
		cache = new CacheTS<Integer, Categoria>() ;
		cache.setDuracionSegundo(1) ;
		start = System.currentTimeMillis() ;
		for(Integer id : prueba) {
			if( cache.get(id) == null ){
				Categoria c = (Categoria) dao.lee(id, Categoria.class) ;
				cache.add(id,c); 
			}
		}
		end = System.currentTimeMillis() ;
		System.out.println( "request " + times + " start " + start + " end " + end + " time spend(ms) " + (end - start ) );
		System.out.println( "aciertos " + cache.getAciertos()+ " fallos " + cache.getFallos() +
				" expirados " + cache.getExpirados() + " tiempo_cache (sg) " + cache.getDuracionSegundo()  );
				*/
	}
	
	
}
