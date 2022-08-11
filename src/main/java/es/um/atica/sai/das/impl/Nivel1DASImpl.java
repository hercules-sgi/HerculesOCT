package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.Nivel1DAS;
import es.um.atica.sai.entities.Nivel1;
import es.um.atica.sai.entities.Servicio;

@Name( value = Nivel1DAS.NAME )
@Stateless
@Local(Nivel1DAS.class)
public class Nivel1DASImpl extends DataAccessServiceImpl<Nivel1> implements Nivel1DAS{

	@Override
	public List<Nivel1> getNivelesByServicio( Servicio s ) 
	{
		try 
		{
			 return entityManager.createNamedQuery( Nivel1.GET_NIVEL1_BY_SERVICIO).setParameter( "servicio", s ).getResultList();
		}
		catch(Exception ex) 
		{
			return new ArrayList<>();
		}
	}

}
