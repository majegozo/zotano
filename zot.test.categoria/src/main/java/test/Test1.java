//aaa
package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cache.CacheTS;

import zot.model.domain.Categoria;
import zot.model.service.jpa.ModelDao;

public class Test1 {

	private static int times = 1000000 ;
	
	protected static List<Integer> preparaPrueba(List<Integer> ids, int times) {
		List<Integer> prueba = new ArrayList<Integer>() ;
		int numeros  = ids.size()  ;
		Random r = new Random() ;
		r.setSeed(System.currentTimeMillis() * 2) ;
		for( int i = 0 ; i < times ; i++) {
			int posicion =  r.nextInt(numeros-1) ;
			prueba.add(ids.get(posicion) ) ;
		}
		
		return prueba ;
	}
	
	private static Object  f(int i) {
		System.out.println("lala") ;
		return new java.util.ArrayList() ;
	}
	

	
	public static void main(String [] args) {
		
		int t = 10 ;
		
		assert t<0: f(1) ;
		System.out.println("aaa") ;
				
		if( true ) return ;
		ApplicationContext ctx = new ClassPathXmlApplicationContext("model-context.xml");
		
		ModelDao dao = (ModelDao) ctx.getBean("modelDao") ;
		List<Categoria> listado = dao.todasCategorias() ;
		List<Integer> ids = new java.util.ArrayList<Integer>();
		for(Categoria c : listado) ids.add(c.getId()) ;
		
		List<Integer> prueba = preparaPrueba(ids, times) ;
		
		long start = System.currentTimeMillis() ;
		for(Integer id : prueba) {
			Categoria c = (Categoria) dao.lee(id, Categoria.class) ;
		}
		long end = System.currentTimeMillis() ;
		System.out.println( "request " + times + " start " + start + " end " + end + " time spend(ms) " + (end - start ) );
		
		CacheTS<Integer, Categoria> cache = new CacheTS<Integer, Categoria>() ; 
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
	}
	
	
}
