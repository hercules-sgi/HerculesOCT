package es.um.atica.sai.backbeans.lazydatamodel;

import java.util.List;
import java.util.Map;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.services.interfaces.ReservableService;

@Name( "reservableLDM" )
public class ReservableLazyDataModel extends LazyDataModel<Equipo> {



	private static final long serialVersionUID = -1062694634307587779L;

	transient ReservableService reservableService;

	private List<Equipo> listaReservables;
	private Long codTipo;

	public ReservableLazyDataModel() {
	}

	public ReservableLazyDataModel( Long codTipo ) {
		this.codTipo = codTipo;
	}

	@Override
	public Object getRowKey(Equipo r) {
		return r.getCod();
	}


	@Override
	public List<Equipo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)
	{
		setRowCount( getReservableService().getCountBusquedaReservables( codTipo ) );
		if (getRowCount()>0)
		{
			listaReservables = getReservableService().busquedaReservables( codTipo, first, pageSize, sortField,
					sortOrder,
					filters );
		}
		else
		{
			listaReservables = null;
		}

		return listaReservables;
	}


	@Override
	public Equipo getRowData( String rowKey )
	{
		for ( final Equipo prod : listaReservables )
		{
			if (Long.toString(prod.getCod()).equals( rowKey ))
			{
				return prod;
			}
		}
		return null;
	}

	private ReservableService getReservableService()
	{
		if ( reservableService == null )
		{
			reservableService = ( ReservableService ) Component.getInstance( "reservableService" );
		}
		return reservableService;
	}

	public List<Equipo> getListaReservables() {
		return listaReservables;
	}

	public void setListaReservables( List<Equipo> listaReservables ) {
		this.listaReservables = listaReservables;
	}
}