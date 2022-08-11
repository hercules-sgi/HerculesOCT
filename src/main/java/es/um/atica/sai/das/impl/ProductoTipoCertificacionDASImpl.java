package es.um.atica.sai.das.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.ProductoTipoCertificacionDAS;
import es.um.atica.sai.entities.ProductoTipoCertificacion;
import es.um.atica.sai.exceptions.SaiException;

@Name( ProductoTipoCertificacionDASImpl.NAME )
@Stateless
@Local( ProductoTipoCertificacionDAS.class )
public class ProductoTipoCertificacionDASImpl extends DataAccessServiceImpl<ProductoTipoCertificacion>
implements ProductoTipoCertificacionDAS {

	public static final String NAME = "productoTipoCertificacionDAS";

	@Override
	public void guardar( ProductoTipoCertificacion p ) throws SaiException {
		try {
			log.info( "Guardando ProductoTipoCertificacion #0, #1", p.getTipoCertificacion().getNombre(),
					p.getProducto().getDescripcion() );
			this.persist( p, true );
		}catch(final Exception ex) {
			log.error( "Error guardando un ProductoTipoCertificacion #0", ex.toString() );
			throw new SaiException( ex.getMessage() );
		}
	}

	@Override
	public void eliminar( ProductoTipoCertificacion p ) throws SaiException {
		try {
			log.info( "Eliminando ProductoTipoCertificacion #0, #1", p.getTipoCertificacion().getNombre(),
					p.getProducto().getDescripcion() );
			this.delete( p, true );
		}catch(final Exception ex) {
			log.error( "Error eliminando un ProductoTipoCertificacion #0", ex.toString() );
			throw new SaiException( ex.getMessage() );
		}
	}

}
