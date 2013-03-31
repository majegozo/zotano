//aaa
package test;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cache.almacen.AlmacenInfinito;
import cache.almacen.Almacen;
import cache.almacen.AlmacenFIFO;
import cache.almacen.AlmacenLRU;
import cache.almacen.NoCache;

import zot.model.domain.Categoria;
import zot.model.service.jpa.ModelDao;

public class TestAlmacenDistProbabilistica {

	private static long times = 10 ;
	private static int validUntil = 30 ; 
	private static int numElementos = 3500 ;
	
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
		long datos = 0 ;
		Object o;
		for(String id : prueba) {
			o = cache.get(String.valueOf(id)) ; 
			if(  o == null ){
				long idatos = System.currentTimeMillis() ;
				Categoria c = (Categoria) dao.lee( Integer.parseInt(id), Categoria.class) ;
				cache.put(id,c); 
		//		System.out.println((System.currentTimeMillis()-idatos));
				datos += (System.currentTimeMillis()-idatos) ;
			} 
		}
		long end = System.currentTimeMillis() ;
		
		System.out.print( cache.getClass().getSimpleName() + "," + times + "," + (end - start ) + "," + datos + "," );
		System.out.println( cache.getAciertos()+ "," + cache.getFallos() );
		
	}
	public static void main(String [] args) throws FileNotFoundException {
		boolean fromFile = false ;
		
		for( String s : args ) {
			if( s.startsWith("-validUntil="))
				validUntil = Integer.parseInt(s.replace("-validUntil=","" )) ;
			if( s.startsWith("-numElementos="))
				numElementos = Integer.parseInt(s.replace("-numElementos=","" )) ;
			
			if( s.startsWith("-fromFile"))
				fromFile = true ;
			
		}
		
		ApplicationContext ctx = null ;
		if ( !fromFile )
			ctx = new ClassPathXmlApplicationContext("model-context.xml");
		else 
			ctx = new FileSystemXmlApplicationContext("model-context.xml");
		
		
		//System.setOut(new PrintStream("/test/test_" + validUntil + "_" + numElementos + ".txt")) ;

		System.out.println( validUntil + "_" + numElementos );
		
		ModelDao dao = (ModelDao) ctx.getBean("modelDao") ;
		List<Categoria> listado = dao.todasCategorias() ;
		List<Integer> ids = new java.util.ArrayList<Integer>();
		Random r = new Random() ;
		r.setSeed(System.currentTimeMillis() * 5) ;
		
		for(Categoria c : listado){
			int rep = r.nextInt(50);
			
			for( int i = 0 ; i < rep ; i++)
				ids.add(c.getId()) ;
		}
		
		System.out.println(ids.size()) ;
		System.out.println("media " + (ids.size() / listado.size()));
		List<String> prueba ;
		
		//System.out.println( "name,repeticiones,time(ms),datos(ms),aciertos,fallos");
		
		validUntil = 90 ;
		
		times = 100000;
		for( int i =1 ; i < 5; i++) {
			prueba = preparaPrueba(ids, times) ;
			numElementos = 1000 * i;
			
			Almacen a ;
			/*a=  new NoCache(validUntil) ;
			a.stop() ;
			test(a,prueba,dao) ;
			a.clear() ;
			*/
			/*a =  new AlmacenInfinito(validUntil) ;
			a.stop() ;
			test(a,prueba,dao) ;
			*/
			a = new AlmacenInfinito(validUntil) ;
			test(a,prueba,dao) ;
			a.stop();
			a.clear() ;

			
			a = new AlmacenFIFO(validUntil,numElementos) ;
			test(a,prueba,dao) ;
			a.stop();
			a.clear() ;

			
			a = new AlmacenLRU(validUntil,numElementos) ;
			test(a,prueba,dao) ;
			a.stop() ;
			a.clear() ;

			
			
		}
	}
	
	
}
