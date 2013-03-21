//aaa
package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cache.almacen.AlmacenInfinito;
import cache.almacen.Almacen;
import cache.almacen.AlmacenLRC;
import cache.almacen.AlmacenLRU;
import cache.almacen.NoCache;

import zot.model.domain.Categoria;
import zot.model.service.jpa.ModelDao;

public class TestAlmacenInfinito {

	private static long times = 10 ;
	private static int validUntil = 30 ; 
			
	protected static List<String> preparaPrueba(List<Integer> ids, long times) {
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
	
	public static void test( Almacen cache, List<String> prueba, ModelDao dao){
		long start = System.currentTimeMillis() ;
		Object o;
		for(String id : prueba) {
			o = cache.get(String.valueOf(id)) ; 
			if(  o == null ){
				Categoria c = (Categoria) dao.lee( Integer.parseInt(id), Categoria.class) ;
				cache.put(id,c); 
			} 
		}
		long end = System.currentTimeMillis() ;
		
		System.out.print( cache.getClass().getName() + "," + times + "," + (end - start ) + "," );
		System.out.println( cache.getAciertos()+ "," + cache.getFallos() +
				"," + cache.getExpirados());
		
	}
	public static void main(String [] args) {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("model-context.xml");
		
		ModelDao dao = (ModelDao) ctx.getBean("modelDao") ;
		List<Categoria> listado = dao.todasCategorias() ;
		List<Integer> ids = new java.util.ArrayList<Integer>();
		for(Categoria c : listado) ids.add(c.getId()) ;
		List<String> prueba ;
		
		System.out.println( "name,repeticiones,time(ms),aciertos,fallos,expirados");
		
		for( int i =1 ; i < 7; i++) {
			prueba = preparaPrueba(ids, times) ;
			
			Almacen a =  new NoCache(validUntil) ;
			a.stop() ;
			test(a,prueba,dao) ;
			a.clear() ;
			
			/*a =  new AlmacenInfinito(validUntil) ;
			a.stop() ;
			test(a,prueba,dao) ;
			*/
			a = new AlmacenInfinito(validUntil) ;
			test(a,prueba,dao) ;
			a.stop();
			a.clear() ;

			
			a = new AlmacenLRC(validUntil,3500) ;
			test(a,prueba,dao) ;
			a.stop();
			a.clear() ;

			
			a = new AlmacenLRU(validUntil,3500) ;
			test(a,prueba,dao) ;
			a.stop() ;
			a.clear() ;

			
			times = times * 10 ;
		}
	}
	
	
}
