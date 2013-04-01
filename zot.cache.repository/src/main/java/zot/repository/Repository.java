package zot.repository;

public interface Repository<T,K> {

	public T findById(K key) ;
	
	
	
}
