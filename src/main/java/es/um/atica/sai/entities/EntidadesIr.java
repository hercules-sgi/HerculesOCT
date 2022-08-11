package es.um.atica.sai.entities;
// Generated 24-jun-2009 10:06:20 by Hibernate Tools 3.2.2.GA

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ENTIDADES_IR")

@NamedQueries( { 
    @NamedQuery(name = EntidadesIr.QUERY_ENTIDADESIR_X_USUARIO, query = "SELECT eir FROM EntidadesIr eir WHERE eir.usuario.cod=:codusuario ORDER BY eir.entidadPagadora.nombre" ),
    @NamedQuery(name = EntidadesIr.EXISTE_ENTIDADIR, query = "SELECT COUNT(eir) FROM EntidadesIr eir WHERE eir.usuario=:usuario AND eir.entidadPagadora=:entidadpagadora" )
} )
public class EntidadesIr implements java.io.Serializable {

	private static final long serialVersionUID = -2504943099734350289L;

	public static final String QUERY_ENTIDADESIR_X_USUARIO = "entidadesIrByUsuario";
	public static final String EXISTE_ENTIDADIR = "ENTIDADES_IR.EXISTE_ENTIDADIR";
	
	private EntidadesIrId id;
	private EntidadPagadora entidadPagadora;
	private Usuario usuario;

	public EntidadesIr() {
	}

	public EntidadesIr(EntidadesIrId id, EntidadPagadora entidadPagadora,
			Usuario usuario) {
		this.id = id;
		this.entidadPagadora = entidadPagadora;
		this.usuario = usuario;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "codEntidad", column = @Column(name = "COD_ENTIDIAD", nullable = false, precision = 22, scale = 0)),
			@AttributeOverride(name = "codIr", column = @Column(name = "COD_IR", nullable = false, scale = 0))})
	@NotNull
	public EntidadesIrId getId() {
		return this.id;
	}

	public void setId(EntidadesIrId id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_ENTIDIAD", nullable = false, insertable = false, updatable = false)
	@NotNull
	public EntidadPagadora getEntidadPagadora() {
		return this.entidadPagadora;
	}

	public void setEntidadPagadora(EntidadPagadora entidadPagadora) {
		this.entidadPagadora = entidadPagadora;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_IR", nullable = false, insertable = false, updatable = false)
	@NotNull
	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
