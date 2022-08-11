package es.um.atica.sai.utils;

import java.util.Comparator;

import org.umu.atica.servicios.gesper.gente.entity.Departamento;

public class DepartamentoComparator implements Comparator<Departamento>{

	@Override
	public int compare( Departamento dep1, Departamento dep2 )
	{
		return dep1.getNombre().compareTo(dep2.getNombre());
	}

}
