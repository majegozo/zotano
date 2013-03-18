package cache.almacen;

public abstract class AlmacenLimitacionObjetos extends Almacen {

	protected int maxNumObjetos ;
	
	public AlmacenLimitacionObjetos(int segundos, int maxNumObjetos) {
		super(segundos);
		this.maxNumObjetos = maxNumObjetos ;
	} 
	
}
