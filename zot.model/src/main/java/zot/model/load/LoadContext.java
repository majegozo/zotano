package zot.model.load;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ibm.icu.text.Normalizer.Mode;

import zot.model.domain.Categoria;
import zot.model.domain.Estado;
import zot.model.domain.Video;
import zot.model.service.jpa.ModelDao;

public class LoadContext {

	public static Pattern numero = Pattern.compile("\\d+") ;
	public static String catPattern = "http://www.rtve.es/api/tematicas/{0}/videos.xml?size=60" ;
	public static int contador = 0 ;
	public static void main(String [] args) throws Exception {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("model-context.xml") ;
		
		
		ModelDao dao = ctx.getBean(ModelDao.class);
		
		/*
		loadFeed( "http://www.rtve.es/aoa/scrapper/inc/destacados/components/alacarta/destacados/tve/videos.xml", dao ) ;
		loadFeed( "http://www.rtve.es/aoa/scrapper/inc/destacados/components/alacarta/destacados/la1/videos.xml", dao ) ;
		loadFeed( "http://www.rtve.es/aoa/scrapper/inc/destacados/components/alacarta/destacados/la2/videos.xml", dao ) ;
		loadFeed( "http://www.rtve.es/aoa/scrapper/inc/destacados/components/alacarta/destacados/24-horas/videos.xml", dao ) ;
		loadFeed( "http://www.rtve.es/aoa/scrapper/inc/destacados/components/alacarta/destacados/teledeporte/videos.xml", dao ) ;
		loadFeed( "http://www.rtve.es/aoa/agregator/series_xl.xml", dao ) ;
		loadFeed( "http://www.rtve.es/aoa/agregator/programas_xl.xml", dao ) ;
		loadFeed( "http://www.rtve.es/aoa/agregator/deportes_xl.xml", dao ) ;
		loadFeed( "http://www.rtve.es/aoa/agregator/informativos_xl.xml", dao ) ;
		loadFeed( "http://www.rtve.es/aoa/agregator/documentales_xl.xml", dao ) ;
		*/
		List<Categoria> cats =  dao.todasCategorias() ;
		contador = cats.size() ;
		int aaa  = 0;
		for(Categoria c : cats ) {
			aaa++ ;
			System.out.println( aaa + " " + contador-- + " " + MessageFormat.format(catPattern, String.valueOf(c.getId()) ) );
			
			if( aaa < 210 )
				continue ;
			
			
			try {
				loadFeed( MessageFormat.format(catPattern, String.valueOf(c.getId()) ) , dao) ;
			} catch(Exception e){
			}
			Thread.currentThread().sleep(3000) ;
		}
		
		/*Categoria c = cargaCategoria(814, dao) ;
		cargaHijos(814,dao, c) ;
		*/
		
	}
	
	public static void cargaHijos(Integer id, ModelDao dao, Categoria padre) throws DocumentException, IOException{
		
		SAXReader reader = new SAXReader();
        Document document = reader.read(getContent("http://www.rtve.es/api/tematicas/" + id + "/hijos.xml") ) ;
        List<Element> categoria = (List<Element>) document.selectNodes( "//com.irtve.plataforma.rest.model.dto.ViewDTO/page/items/com.irtve.plataforma.rest.model.dto.topic.TopicLiteDTO" );
        System.out.println(categoria.size()) ;
        for(Element e : categoria) {
        	Integer idc = new Integer( ((Element)e.selectObject("id")).getText()) ;
        	Categoria c = cargaCategoria( idc , dao, padre) ;
        	cargaHijos(idc,dao, c) ;
        }
        

	}
	public static Categoria cargaCategoria(Integer id, ModelDao dao, Categoria cp) throws DocumentException, IOException{
		
		SAXReader reader = new SAXReader();
        Document document = reader.read(getContent("http://www.rtve.es/api/tematicas/" + id + ".xml") ) ;
        Element categoria = (Element) document.selectSingleNode( "//com.irtve.plataforma.rest.model.dto.ViewDTO/page/items/com.irtve.plataforma.rest.model.dto.topic.TopicDTO" );
        
        Categoria c = new Categoria();
        c.setId( new Integer( ((Element)categoria.selectObject("id")).getText()) ) ;
        c.setNombre(  ((Element)categoria.selectObject("title")).getText()  ) ;
        c.setSeo(  ((Element)categoria.selectObject("permalink")).getText()  ) ;

        if( cp != null ) {
     		cp.addHijo(c) ;
     	}
        
        dao.insert(c) ;
        return c ;
	}
	
	
	public static void loadFeed(String url, ModelDao dao) throws Exception {
		SAXReader reader = new SAXReader();
        Document document = reader.read(getContent(url) ) ;
        
        List<Element> list = (List<Element>) document.selectNodes( "//com.irtve.plataforma.rest.model.dto.ViewDTO/page/items/com.irtve.plataforma.rest.model.dto.multimedia.VideoDTO" );
        
        for(Element video : list ) {
        	Video v = new Video();
        	v.setDescripcion(((Element)video.selectObject("description")).getText() );
        	v.setEstado(Estado.PUBLICADO);
        	//v.setFechaCreacion(fechaCreacion)
        	v.setId( new Integer( ((Element)video.selectObject("id")).getText()) ) ;
        	
        	v.setThumbnail(((Element)video.selectObject("thumbnail")).getText()) ;
        	v.setTitulo(((Element)video.selectObject("longTitle")).getText()) ;
        	
        	
        	String programa = ((Element)video.selectObject("programRef")).getText() ;
        	Matcher m = numero.matcher(programa);
        	if( m.find() ) {
        		Integer idcategoria = new Integer(m.group());
        		Categoria c = (Categoria) dao.lee(idcategoria, Categoria.class) ;
        		if( c == null ) {
        			c = cargaCategoria(idcategoria, dao) ;
        		}
        		v.setCategoria(c) ;
        	}
        	dao.insert(v) ;
        
        }
	}
	
	
	
	public static Categoria cargaCategoria(Integer id, ModelDao dao) throws DocumentException, IOException{
		
		SAXReader reader = new SAXReader();
        Document document = reader.read(getContent("http://www.rtve.es/api/tematicas/" + id + ".xml") ) ;
        Element categoria = (Element) document.selectSingleNode( "//com.irtve.plataforma.rest.model.dto.ViewDTO/page/items/com.irtve.plataforma.rest.model.dto.topic.TopicDTO" );
        
        Categoria c = new Categoria();
        c.setId( new Integer( ((Element)categoria.selectObject("id")).getText()) ) ;
        c.setNombre(  ((Element)categoria.selectObject("title")).getText()  ) ;
        c.setSeo(  ((Element)categoria.selectObject("permalink")).getText()  ) ;
        System.out.println(id);
        // 814 no tiene padre.... decidido!!!
        if( id != 814 ) {
        	String parent  = ((Element)categoria.selectObject("parentRef")).getText() ;
        	Matcher m = numero.matcher(parent);
        	if( m.find() ) {
        		Integer idpadre = new Integer(m.group());
        		System.out.println("padre " + idpadre ) ;
        		
        		Categoria cp = (Categoria) dao.lee(idpadre, Categoria.class) ;
        		System.out.println(cp);
        		if( cp == null ) {
        			cp = cargaCategoria(idpadre, dao) ;
        			cp.addHijo(c) ;
        		}
        		
        	}
        	
        }
        
        dao.insert(c) ;
        return c ;
	}
	
	public static InputStream getContent(String url) throws IOException {
		java.net.URL u = new URL(url) ;
		
		return u.openStream() ;
	}
}
