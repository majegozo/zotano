
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import zot.model.domain.Categoria;
import zot.model.service.jpa.ModelDao;
import zot.repository.Repository;
import zot.repository.cache.Cache;


public class Test {

	public static void main(String [] args) throws ClassNotFoundException, InterruptedException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("model-repository.xml");
		
		ModelDao dao = (ModelDao) ctx.getBean("modelDao") ;
		List<Categoria> listado = dao.todasCategorias() ;
		
		Cache c = (Cache) ctx.getBean("cache") ;
		
		Repository<Categoria,Integer> rep = (Repository<Categoria,Integer>) c.getRepository("zot.model.domain.Categoria") ;
		/*System.out.println ( c.get(Categoria.class, 1589) ) ;
		System.out.println ( c.get(Categoria.class, 1589) ) ;
		*/
		long temp = System.currentTimeMillis() ;
		
		for( int i = 0; i < 20; i++) {
			Thread.currentThread().sleep(1000) ;
			c.get(Categoria.class, 1589)  ;
		}
		System.out.println("tiempo total " + (System.currentTimeMillis()-temp) + " ms" ) ;
		System.out.println( "tiempo datos " +  c.getDatos() + " ms") ;
		System.out.println("(aciertos,fallos)(" + c.getAciertos()+"," + c.getFallos()+")") ;

	}
}
