package es.um.atica.sai.backbeans;


import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.primefaces.model.DefaultStreamedContent;

import es.um.atica.sai.entities.Anexo;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.services.interfaces.ConsumoService;

@Name(AnexoBean.NAME)
@Scope(ScopeType.EVENT)
public class AnexoBean {
    public static final String NAME="anexoBean"; 
    
	@Logger
	private Log logger;

	@In(create = true)
	private ConsumoService consumoService;

	@In( "#{identity.usuarioConectado}" )
	private Usuario usuario;

	public DefaultStreamedContent showDocument( Long cod ) 
	{
		Anexo anexo = consumoService.getAnexoById( cod);
		InputStream stream = new ByteArrayInputStream( anexo.getDocumento() );
		return new DefaultStreamedContent( stream, null, anexo.getNomDocumento() );
    }
}
