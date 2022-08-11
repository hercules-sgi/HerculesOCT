package es.um.atica.sai.backbeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Log;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.HorarioDia;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoHorario;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ReservableService;

@Name(value = TipoHorarioEditBean.NAME )
@Scope( ScopeType.CONVERSATION )
public class TipoHorarioEditBean {

	public static final String NAME = "tipoHorarioEdit";

	@Logger
	private Log log;

	@In(create = true, required = true )
	private ReservableService reservableService;

	@In(create=true)
	protected FacesMessages facesMessages;

	@In(create=true)
	protected SaiIdentity identity;

	private static final String KEY_HORARIODIA_GUARDAR_ERROR = "tipohorario.horariodia.guardar.error";
	private static final String FORMATO_HORA = "HH:mm";
	
	ResourceBundle srb = SeamResourceBundle.getBundle();
	
	private List<Servicio> listaServicios;
	private TipoHorario tipoHorarioEdit;
	private HorarioDia horarioDiaEdit;
	private List<Integer> listaDiasSemana;
	private Integer[] diasSeleccionados;
	private Date horaIniMananaAdd;
	private Date horaFinMananaAdd;
	private Date horaIniTardeAdd;
	private Date horaFinTardeAdd;
	
	public List<Servicio> getListaServicios() {
		return listaServicios;
	}
	
	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}
	
	public TipoHorario getTipoHorarioEdit() {
		return tipoHorarioEdit;
	}
	
	public void setTipoHorarioEdit( TipoHorario tipoHorarioEdit ) {
		this.tipoHorarioEdit = tipoHorarioEdit;
	}
	
	public HorarioDia getHorarioDiaEdit() {
		return horarioDiaEdit;
	}
	
	public void setHorarioDiaEdit( HorarioDia horarioDiaEdit ) {
		this.horarioDiaEdit = horarioDiaEdit;
	}

	public List<Integer> getListaDiasSemana() {
		return listaDiasSemana;
	}
	
	public void setListaDiasSemana( List<Integer> listaDiasSemana ) {
		this.listaDiasSemana = listaDiasSemana;
	}
	
	public Integer[] getDiasSeleccionados() {
		return diasSeleccionados;
	}
	
	public void setDiasSeleccionados( Integer[] diasSeleccionados ) {
		this.diasSeleccionados = diasSeleccionados;
	}
	
	public Date getHoraIniMananaAdd() {
		return horaIniMananaAdd;
	}
	
	public void setHoraIniMananaAdd( Date horaIniMananaAdd ) {
		this.horaIniMananaAdd = horaIniMananaAdd;
	}
	
	public Date getHoraFinMananaAdd() {
		return horaFinMananaAdd;
	}
	
	public void setHoraFinMananaAdd( Date horaFinMananaAdd ) {
		this.horaFinMananaAdd = horaFinMananaAdd;
	}
	
	public Date getHoraIniTardeAdd() {
		return horaIniTardeAdd;
	}
	
	public void setHoraIniTardeAdd( Date horaIniTardeAdd ) {
		this.horaIniTardeAdd = horaIniTardeAdd;
	}
	
	public Date getHoraFinTardeAdd() {
		return horaFinTardeAdd;
	}
	
	public void setHoraFinTardeAdd( Date horaFinTardeAdd ) {
		this.horaFinTardeAdd = horaFinTardeAdd;
	}

	@Create
	public void inicializa() 
	{
		this.listaServicios = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
		this.inicializaDiasSemana();
	}

	private void inicializaDiasSemana()
	{
		this.listaDiasSemana = new ArrayList<>();
		int numDia = 1;
		while (numDia<8)
		{
			this.listaDiasSemana.add(numDia);
			numDia++;
		}
	}
	
	public String establecerTipoHorarioCreate()
	{
		this.tipoHorarioEdit = new TipoHorario();
		if (this.listaServicios.size() == 1)
		{
			this.tipoHorarioEdit.setServicio(this.listaServicios.get(0));
		}
		return NAME;
	}
	
	public String establecerTipoHorarioEdit(TipoHorario tipoHorario)
	{
		this.tipoHorarioEdit = tipoHorario;
		return NAME;
	}
	
	public void guardar() 
	{
		boolean esAlta = this.tipoHorarioEdit.getCod() == null;
		try
		{
			reservableService.guardarTipoHorario(this.tipoHorarioEdit);
		}
		catch(Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.WARN, "tipohorario.guardar.error", null, null, ex.getMessage());
			return;
		}
		if (esAlta)
		{
			facesMessages.add(StatusMessage.Severity.INFO, "tipohorario.guardar.ok.alta", null, null, this.tipoHorarioEdit.getDescripcion());
		}
		else
		{
			facesMessages.add(StatusMessage.Severity.INFO, "tipohorario.guardar.ok", null, null, this.tipoHorarioEdit.getDescripcion());
		}
	}
	
    public List<HorarioDia> getListaHorariosDias()
    {
    	return reservableService.getListaHorarioDiaByTipohorario(this.tipoHorarioEdit);
    }
	
    public List<TipoReservable> getListaTiposReservables()
    {
    	return reservableService.getListaTiposReservableByTipoHorario(this.tipoHorarioEdit);
    }
    
	public String getNombreDiaSemana(int dia) 
	{
		return reservableService.getNombreDiaSemana( dia );
	}
	
	public String getDescripcionTurnoManana(HorarioDia hd)
	{
		return reservableService.getDescripcionTurno( hd, "MAÑANA" );
	}
	
	public String getDescripcionTurnoTarde(HorarioDia hd)
	{
		return reservableService.getDescripcionTurno( hd, "TARDE" );
	}

	public void establecerHorarioDiaCreate()
	{
		this.horarioDiaEdit = new HorarioDia();
		this.horaIniMananaAdd = null;
		this.horaFinMananaAdd = null;
		this.horaIniTardeAdd = null;
		this.horaFinTardeAdd = null;
	}
	
	public void establecerHorarioDiaEdit(HorarioDia hd)
	{
		this.horarioDiaEdit = hd;
		if (hd.getHoraIniManana() != null)
		{
			this.horaIniMananaAdd = UtilDate.convertirStrHoraToDate(new StringBuilder(hd.getHoraIniManana()).toString());
		}
		else
		{
			this.horaIniMananaAdd = null;
		}
		if (hd.getHoraFinManana() != null)
		{
			this.horaFinMananaAdd = UtilDate.convertirStrHoraToDate(new StringBuilder(hd.getHoraFinManana()).toString());
		}
		else
		{
			this.horaFinMananaAdd = null;
		}
		if (hd.getHoraIniTarde() != null)
		{
			this.horaIniTardeAdd = UtilDate.convertirStrHoraToDate(new StringBuilder(hd.getHoraIniTarde()).toString());
		}
		else
		{
			this.horaIniTardeAdd = null;
		}
		if (hd.getHoraFinTarde() != null)
		{
			this.horaFinTardeAdd = UtilDate.convertirStrHoraToDate(new StringBuilder(hd.getHoraFinTarde()).toString());
		}
		else
		{
			this.horaFinTardeAdd = null;
		}
	}
    
	public void guardarHorarioDia() 
	{
		String msjHorasDia = compruebaHorasDia(); 
		if (msjHorasDia != null)
		{
			this.facesMessages.add( StatusMessage.Severity.WARN, KEY_HORARIODIA_GUARDAR_ERROR, null, null, msjHorasDia);
			return;
		}
		String horaIniManana = null;
		String horaFinManana = null;
		String horaIniTarde = null;
		String horaFinTarde = null;
		
		if (this.horaIniMananaAdd != null)
		{
			horaIniManana = UtilDate.dateToString(this.horaIniMananaAdd, FORMATO_HORA);
		}
		if (this.horaFinMananaAdd != null)
		{
			horaFinManana = UtilDate.dateToString(this.horaFinMananaAdd, FORMATO_HORA);
		}
		if (this.horaIniTardeAdd != null)
		{
			horaIniTarde = UtilDate.dateToString(this.horaIniTardeAdd, FORMATO_HORA);
		}
		if (this.horaFinTardeAdd != null)
		{
			horaFinTarde = UtilDate.dateToString(this.horaFinTardeAdd, FORMATO_HORA);
		}

		if (this.horarioDiaEdit.getCod()==null)
		{
				
			// Se trata de un alta de días
			for ( int i = 0; i < this.diasSeleccionados.length; i++ ) 
			{
				HorarioDia horarioDiaCrear = new HorarioDia();
				horarioDiaCrear.setDia(this.diasSeleccionados[i]);
				horarioDiaCrear.setTipoHorario(this.tipoHorarioEdit);
				horarioDiaCrear.setHoraIniManana(horaIniManana);
				horarioDiaCrear.setHoraFinManana(horaFinManana);
				horarioDiaCrear.setHoraIniTarde(horaIniTarde);
				horarioDiaCrear.setHoraFinTarde(horaFinTarde);
				try
				{
					reservableService.guardarHorarioDia(horarioDiaCrear);
					this.facesMessages.add( StatusMessage.Severity.INFO, "tipohorario.horariodia.guardar.ok", null, null, this.getNombreDiaSemana(this.diasSeleccionados[i]));
				}
				catch (Exception ex)
				{
					this.facesMessages.add( StatusMessage.Severity.ERROR, KEY_HORARIODIA_GUARDAR_ERROR, null, null, this.getNombreDiaSemana(this.diasSeleccionados[i]));
				}
			}
			this.diasSeleccionados = null;
		}
		else
		{
			this.horarioDiaEdit.setHoraIniManana(horaIniManana);
			this.horarioDiaEdit.setHoraFinManana(horaFinManana);
			this.horarioDiaEdit.setHoraIniTarde(horaIniTarde);
			this.horarioDiaEdit.setHoraFinTarde(horaFinTarde);
			try
			{
				reservableService.guardarHorarioDia(this.horarioDiaEdit);
				this.facesMessages.add( StatusMessage.Severity.INFO, "tipohorario.horariodia.guardar.ok", null, null, this.getNombreDiaSemana(this.horarioDiaEdit.getDia()));
			}
			catch (Exception ex)
			{
				this.facesMessages.add( StatusMessage.Severity.ERROR, KEY_HORARIODIA_GUARDAR_ERROR, null, null, ex.getMessage());
			}

		}
	}
	
	public void eliminarHorarioDia(HorarioDia hd) 
	{
		try
		{
			reservableService.eliminarHorarioDia(hd);
		}
		catch(Exception ex)
		{
			this.facesMessages.add( StatusMessage.Severity.ERROR, "tipohorario.horariodia.eliminar.error", null, null, this.getNombreDiaSemana(hd.getDia()));
			return;
		}
		this.facesMessages.add( StatusMessage.Severity.INFO, "tipohorario.horariodia.eliminar.ok", null, null, this.getNombreDiaSemana(hd.getDia()));
	}
	
	private String compruebaHorasDia()
	{
        if ((this.horaIniMananaAdd!=null && this.horaFinMananaAdd==null) || (this.horaIniMananaAdd==null && this.horaFinMananaAdd!=null) || (this.horaIniTardeAdd!=null && this.horaFinTardeAdd==null) || (this.horaIniTardeAdd==null && this.horaFinTardeAdd!=null))
        {
            return srb.getString("tipohorario.horariodia.add.error.faltandatos");
        }
        if (this.horaIniMananaAdd!=null && UtilDate.anteriorOrIgual(this.horaFinMananaAdd, this.horaIniMananaAdd))
        {
        	return srb.getString("tipohorario.horariodia.add.error.manana");
        }
        if (this.horaIniTardeAdd!=null && UtilDate.anteriorOrIgual(this.horaFinTardeAdd, this.horaIniTardeAdd))
        {
        	return srb.getString("tipohorario.horariodia.add.error.tarde");
        }
        if (this.horaIniMananaAdd!=null && this.horaIniTardeAdd!=null && UtilDate.anterior(horaIniTardeAdd, horaFinMananaAdd))
        {
        	return srb.getString("tipohorario.horariodia.add.error.mananatarde"); 
        }
        return null;
	}
}
