package zot.model.load;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LoadContext {

	public static void main(String [] args) throws Exception {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("model-context.xml") ;
		
	}
	
}
