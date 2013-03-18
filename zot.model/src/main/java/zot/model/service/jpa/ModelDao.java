package zot.model.service.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zot.model.domain.Categoria;
import zot.model.domain.Ejemplo;

@Service("modelDao")
public class ModelDao {

	@PersistenceContext 
	private EntityManager em ;
	
	@Transactional 
	public void insert(Object e){
		em.merge(e) ;
	}
	
	@Transactional(readOnly=true)
	public Object lee(Integer id, Class c){
		return (Object) em.find(c, id);
	}

	@Transactional(readOnly=true)
	public List<Categoria> todasCategorias(){
		
		Query q = em.createQuery("select c from Categoria c") ;
		
		return (List<Categoria>) q.getResultList() ;
	}
	
	
}
