import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import zot.model.domain.Categoria;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/**/model-context.xml"})
public class EsquemaOracle {

	@Autowired
	zot.model.service.jpa.ModelDao jpaTest;
	
	public int f(int a) {
		System.out.println("jaja") ;
		return 23 ;
	}
	
	@Test
	public void test() {
		int t = 10 ;
		
		assert t<5  ;
		
		System.out.println("jaja2222") ;

	}
}
