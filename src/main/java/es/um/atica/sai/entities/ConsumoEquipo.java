package es.um.atica.sai.entities;
// Generated 15-jun-2021 9:48:31 by Hibernate Tools 4.3.5.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table( name = "CONSUMO_EQUIPO" )
@SequenceGenerator( name = "SEQ_CONSUMO_EQUIPO", sequenceName = "SQ_CONSUMO_EQUIPO", initialValue = 0, allocationSize = 1 )
@NamedQueries ( {
	@NamedQuery(name = ConsumoEquipo.GET_EQUIPOS_BY_CONSUMO, query = "SELECT ce FROM ConsumoEquipo ce WHERE ce.consumo=:consumo ORDER BY ce.equipo.descripcion"),
	@NamedQuery(name = ConsumoEquipo.EXISTE_CONSUMOEQUIPO, query = "SELECT COUNT(ce) FROM ConsumoEquipo ce WHERE ce.consumo=:consumo AND ce.equipo=:equipo"),
})


public class ConsumoEquipo implements java.io.Serializable {


	private static final long serialVersionUID = 266164549446361670L;
	
	public static final String GET_EQUIPOS_BY_CONSUMO = "CONSUMO_EQUIPO.GET_EQUIPOS_BY_CONSUMO";
	public static final String EXISTE_CONSUMOEQUIPO = "CONSUMO_EQUIPO.EXISTE_CONSUMOEQUIPO";
	
	private Long cod;
	private Consumo consumo;
	private Equipo equipo;

	public ConsumoEquipo() {}

	public ConsumoEquipo( Long cod, Consumo consumo, Equipo equipo, Long cantidad ) {
		this.cod = cod;
		this.consumo = consumo;
		this.equipo = equipo;
	}

	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SEQ_CONSUMO_EQUIPO" )
	@Column( name = "COD", unique = true, nullable = false, scale = 0 )
	public Long getCod() {
		return cod;
	}

	public void setCod( Long cod ) {
		this.cod = cod;
	}

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "COD_CONSUMO", nullable = false )
	public Consumo getConsumo() {
		return consumo;
	}

	public void setConsumo( Consumo consumo ) {
		this.consumo = consumo;
	}

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "COD_EQUIPO", nullable = false )
	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo( Equipo equipo ) {
		this.equipo = equipo;
	}



}
