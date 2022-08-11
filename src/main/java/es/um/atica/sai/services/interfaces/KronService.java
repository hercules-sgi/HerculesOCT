package es.um.atica.sai.services.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.sai.dtos.TerminalKron;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ProductoPuertakron;
import es.um.atica.sai.entities.PuertaKron;
import es.um.atica.sai.entities.Reservas;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.ServicioPuertakron;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface KronService {

	public static final String NAME = "kronService";
	
	
	List<ServicioPuertakron> getListaServicioPuertakronByService(Servicio s);
	List<ProductoPuertakron> getListaProductoPuertakronByProducto(Producto p);
	List<TerminalKron> getListaTerminalesKron();
	PuertaKron getPuertaKronByTerminalKron(TerminalKron tk) throws SaiException;
	void recargarPuertaKron(PuertaKron pk);
	
	void crearServicioPuertakron(ServicioPuertakron spk) throws SaiException;
	void eliminarServicioPuertakron(ServicioPuertakron spk) throws SaiException;
	void crearProductoPuertakron(ProductoPuertakron ppk) throws SaiException;
	void eliminarProductoPuertakron(ProductoPuertakron ppk) throws SaiException;
	
	void reservaAceptada(Reservas reserva) throws SaiException;
	void reservaCancelada(Reservas reserva) throws SaiException;
	void prestacionAceptada(Consumo consumo) throws SaiException;
	void prestacionCancelada(Consumo consumo) throws SaiException;
	
}
