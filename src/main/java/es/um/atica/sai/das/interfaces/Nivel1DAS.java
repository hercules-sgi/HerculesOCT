package es.um.atica.sai.das.interfaces;

import java.util.List;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Nivel1;
import es.um.atica.sai.entities.Servicio;

public interface Nivel1DAS extends DataAccessService<Nivel1> {
	public static String NAME = "nivel1DAS";
	
	public List<Nivel1> getNivelesByServicio( Servicio s ) ;
}
