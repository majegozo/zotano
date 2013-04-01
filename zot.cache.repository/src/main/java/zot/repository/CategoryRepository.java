package zot.repository;

import org.springframework.beans.factory.annotation.Autowired;

import zot.model.domain.Categoria;
import zot.model.service.jpa.ModelDao;

public class CategoryRepository implements Repository<Categoria, Integer>{

	@Autowired
	ModelDao modelDao ;
	
	public Categoria findById(Integer key) {
		// TODO Auto-generated method stub
		return (Categoria) modelDao.lee(key, Categoria.class);
	}

}
