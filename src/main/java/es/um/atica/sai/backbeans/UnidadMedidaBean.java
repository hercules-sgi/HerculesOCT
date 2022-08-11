package es.um.atica.sai.backbeans;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Log;

import es.um.atica.sai.entities.UnidadMedida;
import es.um.atica.sai.services.interfaces.ProductoService;

@Name("unidadMedida")
@Scope(ScopeType.CONVERSATION)
public class UnidadMedidaBean {

    private static final String RETORNO_LISTADO_UNIDADESMEDIDA = "unidadMedidaEditOk";

    @Logger
    Log log;
    
    @In(create = true )
    ProductoService productoService;

    @In(create=true) 
    protected FacesMessages facesMessages;
    
    private List<UnidadMedida> listaUnidadesMedida;
    private UnidadMedida unidadMedidaEdit;
    
    
	public List<UnidadMedida> getListaUnidadesMedida() {
		return listaUnidadesMedida;
	}

	
	public void setListaUnidadesMedida( List<UnidadMedida> listaUnidadesMedida ) {
		this.listaUnidadesMedida = listaUnidadesMedida;
	}

	
	public UnidadMedida getUnidadMedidaEdit() {
		return unidadMedidaEdit;
	}

	
	public void setUnidadMedidaEdit( UnidadMedida unidadMedidaEdit ) {
		this.unidadMedidaEdit = unidadMedidaEdit;
	}

	@Create
    public void inicializa()
    {
        log.info("Obteniendo listado de unidades de Medida...");
        this.listaUnidadesMedida = productoService.getListaUnidadMedida();
    }
    
    public String establecerUnidadMedidaEdit(UnidadMedida um)
    {
        this.unidadMedidaEdit = um;
        return "unidadMedidaEdit";
    }

	public String establecerUnidadMedidaCreate() {
		this.unidadMedidaEdit = new UnidadMedida();
		return "unidadMedidaEdit";
	}

	public String crearUnidadMedida() 
	{
		try 
		{
			productoService.guardarUnidadMedida( this.unidadMedidaEdit );
		} 
		catch ( Exception ex ) 
		{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
		   			  			  "unidadmedida.guardar.error",
		   			  			  null, null,
		   			  			  ex.getCause().getMessage()); 
			return null;
		}
		this.facesMessages.add(StatusMessage.Severity.INFO, 
		  			  "unidadmedida.guardar.ok",
		  			  null, null,
		  			  this.unidadMedidaEdit.getDescripcion()); 
		this.listaUnidadesMedida = productoService.getListaUnidadMedida();
		return RETORNO_LISTADO_UNIDADESMEDIDA;
	}

	public String modificarUnidadMedida() 
	{
		try 
		{
			productoService.guardarUnidadMedida( this.unidadMedidaEdit );
		} 
		catch ( Exception ex ) 
		{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
			  			  "unidadmedida.guardar.error",
			  			  null, null,
			  			  ex.getCause().getMessage()); 
			return null;
		}
		this.facesMessages.add(StatusMessage.Severity.INFO, 
	  			  "unidadmedida.guardar.ok",
	  			  null, null,
	  			  this.unidadMedidaEdit.getDescripcion()); 
		this.listaUnidadesMedida = productoService.getListaUnidadMedida();
		return RETORNO_LISTADO_UNIDADESMEDIDA;	
	}

	public void eliminarUnidadMedida(UnidadMedida um)
	{
		String descripcion = um.getDescripcion();
		try 
		{
			productoService.eliminarUnidadMedida( um );
		} 
		catch ( Exception ex ) 
		{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
		  			  "unidadmedida.eliminar.error",
		  			  null, null,
		  			  ex.getCause().getMessage()); 
			return;
		}
		this.facesMessages.add(StatusMessage.Severity.INFO, 
	  			  			  "unidadmedida.eliminar.ok",
	  			  null, null,
	  			  descripcion); 
		this.listaUnidadesMedida = productoService.getListaUnidadMedida();
	}
	
	public String cancelarEdicionUnidadMedida() 
	{
		return RETORNO_LISTADO_UNIDADESMEDIDA;
	}
	
	public String getDescripcionTipoProducto(String tipo)
	{
		return productoService.getDescripcionTipoProducto( tipo );
	}
}
