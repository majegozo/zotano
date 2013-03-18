package zot.model.load;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import zot.model.domain.Categoria;

public class LoadDataDB {
	
	static zot.model.service.jpa.ModelDao jpaTest ;
			
	


	public static String normaliza(String cad){
		cad = cad.toLowerCase() ;
		cad = cad.replace("á", "a") ;
		cad = cad.replace("é", "e") ;
		cad = cad.replace("í", "i") ;
		cad = cad.replace("ó", "o") ;
		cad = cad.replace("ú", "u") ;
		cad = cad.replace("ñ", "n") ;
		cad = cad.replace(" ", "-") ;
		cad = cad.replace("(", "") ;
		cad = cad.replace(")", "") ;
		cad = cad.replace("¿", "") ;
		cad = cad.replace("?", "") ;
		cad = cad.replace("&", "") ;
		return cad ;
	}
	
	public static String leeUrl ( String url ) throws Exception {
		System.out.println(url) ;
		URL u = new URL(url);
		HttpURLConnection con = (HttpURLConnection) u.openConnection();
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream())) ;
		String cad ;
		StringBuffer cadena = new StringBuffer() ;
		while( (cad = br.readLine()) != null ) {
			cadena.append( new String(cad.getBytes(), "utf-8") );
		}
		br.close();
		
		return cadena.toString() ;
	}
	
	
	public static void main(String [] args) throws Exception {
		
		PrintStream ps = new PrintStream("categorias.sql") ;
		
		String cadena = leeUrl( "http://www.rtve.es/api/tematicas.json?size=60" ) ;
		JSONObject root = (JSONObject) JSONValue.parse( cadena.toString() );
		JSONObject page = (JSONObject) root.get("page") ; 
		
		JSONArray elements = (JSONArray) page.get("items") ;
		Integer pa = Integer.parseInt( page.get("totalPages").toString());
		String formato = "insert into categoria (id, parent_id, seo, nombre ) values ({0},{1},{2},{3});";
		for( int contador = 1 ; contador <= pa;  contador ++) {
			
			cadena = leeUrl( "http://www.rtve.es/api/tematicas.json?size=60&page=" + contador ) ;
			root = (JSONObject) JSONValue.parse( cadena.toString() );
			page = (JSONObject) root.get("page") ; 
			elements = (JSONArray) page.get("items") ;
			for( Object o : elements ) {
				JSONObject  jo = (JSONObject) o ;
				String parent = null ;
				if( jo.get("parentRef").toString().trim().length() > 0){
					parent = jo.get("parentRef").toString().replace("http://www.rtve.es/api/tematicas/","");
				} 
				ps.println(
					MessageFormat.format(
						formato, 
						new Object[]{ 
								jo.get("id"),
								parent ,
								"'" + normaliza( jo.get("title").toString().replace("&", "") ) + "'" ,
								"'" + jo.get("title") + "'"}
					) 
				) ;
			}
		} 
		ps.close();
	}
}
