package cache.almacen;

public class Item {

	private Object elemento ;
	private String key ;
	private long created ;
	private long lastAccess ;
	private long validUntil ;
	
	public Item(String key,Object elemento, long validUntil){
		this.key = key ;
		this.elemento = elemento;
		this.created = System.currentTimeMillis() ;
		this.validUntil = validUntil ;
		
	}

	public Object getElemento() {
		return elemento;
	}

	public void setElemento(Object elemento) {
		this.elemento = elemento;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public long getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(long lastAccess) {
		this.lastAccess = lastAccess;
	}

	public long getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(long validUntil) {
		this.validUntil = validUntil;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	
}
