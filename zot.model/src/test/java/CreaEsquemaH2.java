import static org.junit.Assert.*;

import org.hibernate.annotations.Loader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import zot.model.domain.Categoria;
import zot.model.domain.Contenido;
import zot.model.domain.Ejemplo;
import zot.model.domain.Estado;
import zot.model.domain.Noticia;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:model-context.xml"})
public class CreaEsquemaH2 {

	@Autowired
	zot.model.service.jpa.ModelDao jpaTest;
	static ApplicationContext ctx ;
	
	@Before
	@Transactional 
	public void load() {

		Ejemplo ej = new Ejemplo();
		ej.setId(1) ;
		ej.setNombre("lala") ;
		jpaTest.insert(ej) ;
		
		creaContenido(1) ;
		creaContenido(2) ;
		
		creaCategoria(1, "primer", "lala") ;
		creaCategoria(2, "segundo", "lala") ;
		creaCategoria(3, "primer", "lala") ;
		creaCategoria(4, "primer", "lala") ;
		creaCategoria(5, "primer", "lala") ;
	}
	
	public void creaContenido(Integer id){
		Contenido c = new Noticia () ; 
		c.setId(id) ;
		c.setEstado(Estado.CREADO) ;
		c.setFechaCreacion(new java.util.Date()) ;
		c.setFechaModificacion(new java.util.Date()) ;
		jpaTest.insert(c) ;
	}
	
	public void creaCategoria(Integer id, String nombre, String seo){
		Categoria c = new Categoria () ;
		c.setId(id) ;
		c.setNombre(nombre) ;
		c.setSeo(seo) ;
		jpaTest.insert(c) ;
	}
	
	@Test
	@Transactional
	public void test() {
		
		Ejemplo ej2 = (Ejemplo) jpaTest.lee(1, Ejemplo.class) ;
		System.out.println(ej2.getNombre()) ;
		
		
		Categoria c121 = (Categoria) jpaTest.lee(4, Categoria.class) ;
		Categoria c122 = (Categoria) jpaTest.lee(5, Categoria.class) ;
		
		Categoria c11  = (Categoria) jpaTest.lee(2, Categoria.class) ;
		Categoria c12  = (Categoria) jpaTest.lee(3, Categoria.class) ;
		System.out.println(c12) ;
		c12.addHijo(c121) ;
		c12.addHijo(c122) ;
				
		Categoria c1   = (Categoria) jpaTest.lee(1, Categoria.class) ;
		c1.addHijo(c11) ;
		c1.addHijo(c12) ;
		
		
		Contenido ct1 = (Contenido) jpaTest.lee(1, Contenido.class) ;
		Contenido ct2 = (Contenido) jpaTest.lee(2, Contenido.class) ;
		ct1.addRelacionado(ct2) ;
		System.out.println(ct1);
		c1.addContenido(ct1) ;
		c1.addContenido(ct2) ;
		ct1.addTematica(c11) ;
		ct1.addTematica(c12) ;
		jpaTest.insert(c1) ;
		jpaTest.insert(ct1) ;
		jpaTest.insert(ct2) ;
		
		
		c1 = (Categoria) jpaTest.lee(1, Categoria.class) ;
		System.out.println( c1.getContenidos().size() ) ;
		System.out.println( c1.getContenidos().get(1).getCategoria().getSeo()   ) ;
		
		
		/*pintamos el árbol de categorias*/
		System.out.println(c1.getId()) ;
		for( Categoria c : c1.getHijos()) {
			System.out.println("\t" + c.getId()) ;
			if( c.getHijos() != null )
				for( Categoria c2 : c.getHijos()) 
					System.out.println("\t\t" + c2.getId()) ;
			
		}
		
		c122 = (Categoria) jpaTest.lee(5, Categoria.class) ;
		System.out.println( c122.getId() + " " + c122.getPadre().getId() + " " + c122.getPadre().getPadre().getId()) ;
		
		System.out.println(ct1.getCategoria().getId() ) ;
		System.out.println(ct1.getTematicas() ) ;
		for(Categoria ca : ct1.getTematicas())
			System.out.println(ca.getId());
			
		System.out.println(ct1.getRelacionados().get(0).getId() ) ;
	}

}
