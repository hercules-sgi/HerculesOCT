package es.um.atica.sai.entities;
// Generated 24-mar-2021 8:40:48 by Hibernate Tools 4.3.5.Final

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import es.um.atica.jpa.listeners.PreventRemove;
import oracle.jdbc.OracleBfile;


@Entity
@EntityListeners( PreventRemove.class )
@Table( name = "CERTIFICACION" )
@NamedQueries( {
		@NamedQuery(	name = Certificacion.GET_CERTIFICACIONES,
						query = "SELECT c FROM Certificacion c WHERE c.tipoCertificacion.fechaBaja IS NULL" ),
		@NamedQuery(	name = Certificacion.GET_CERTIFICACIONES_X_USUARIO,
						query = "SELECT c FROM Certificacion c WHERE c.usuario=:usuario AND c.tipoCertificacion.fechaBaja IS NULL" ),
		@NamedQuery(	name = Certificacion.GET_CERTIFICACIONES_X_LISTAUSUARIOS,
						query = "SELECT c FROM Certificacion c WHERE c.usuario IN (:listausuarios) AND c.tipoCertificacion.fechaBaja IS NULL" ),
		@NamedQuery(	name = Certificacion.EXISTE_CERTIFICACION_PENDIENTE_X_TIPO_USUARIO,
						query = "SELECT COUNT(c) FROM Certificacion c WHERE c.usuario=:usuario AND c.tipoCertificacion=:tipocertificacion AND c.estado='PENDIENTE'" ),
		@NamedQuery(	name = Certificacion.EXISTE_CERTIFICACION_ACTIVA_X_TIPO_USUARIO,
						query = "SELECT COUNT(c) FROM Certificacion c WHERE c.usuario=:usuario AND c.tipoCertificacion=:tipocertificacion AND c.estado='ACTIVA'" ),

} )
@SequenceGenerator( name = "SQ_CERTIFICACION", sequenceName = "SQ_CERTIFICACION", initialValue = 0, allocationSize = 1 )
public class Certificacion implements java.io.Serializable {

	private static final long serialVersionUID = 8289673025841354559L;

	public static final String GET_CERTIFICACIONES = "CERTIFICACION.GET_CERTIFICACIONES";
	public static final String GET_CERTIFICACIONES_X_USUARIO = "CERTIFICACION.GET_CERTIFICACIONES_X_USUARIO";
	public static final String GET_CERTIFICACIONES_X_LISTAUSUARIOS = "CERTIFICACION.GET_CERTIFICACIONES_X_LISTAUSUARIOS";
	public static final String EXISTE_CERTIFICACION_PENDIENTE_X_TIPO_USUARIO = "CERTIFICACION.EXISTE_CERTIFICACION_PENDIENTE_X_TIPO_USUARIO";
	public static final String EXISTE_CERTIFICACION_ACTIVA_X_TIPO_USUARIO = "CERTIFICACION.EXISTE_CERTIFICACION_ACTIVA_X_TIPO_USUARIO";

	private Long cod;
	private TipoCertificacion tipoCertificacion;
	private Usuario usuario;
	private Date fechaAlta;
	private Date fechaCaducidad;
	private Date fechaAvisoCaducidad;
	private String estado;
	private transient OracleBfile documento;
	private Date fechaValidaDeniega;
	private Usuario usuarioValidaDeniega;
	private String motivoDenegacion;

	public Certificacion() {}

	public Certificacion( Long cod, TipoCertificacion tipoCertificacion, Usuario usuario, Date fechaAlta, String estado,
			OracleBfile documento ) {
		this.cod = cod;
		this.tipoCertificacion = tipoCertificacion;
		this.usuario = usuario;
		this.fechaAlta = fechaAlta;
		this.estado = estado;
		this.documento = documento;
	}

	@Id

	@Column( name = "COD", unique = true, nullable = false, scale = 0 )
	public Long getCod() {
		return cod;
	}

	public void setCod( Long cod ) {
		this.cod = cod;
	}

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "COD_TIPO_CERTIFICACION", nullable = false )
	public TipoCertificacion getTipoCertificacion() {
		return tipoCertificacion;
	}

	public void setTipoCertificacion( TipoCertificacion tipoCertificacion ) {
		this.tipoCertificacion = tipoCertificacion;
	}

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "COD_USUARIO", nullable = false )
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario( Usuario usuario ) {
		this.usuario = usuario;
	}

	@Temporal( TemporalType.DATE )
	@Column( name = "FECHA_ALTA", nullable = false, length = 7 )
	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta( Date fechaAlta ) {
		this.fechaAlta = fechaAlta;
	}

	@Temporal( TemporalType.DATE )
	@Column( name = "FECHA_CADUCIDAD", length = 7 )
	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad( Date fechaCaducidad ) {
		this.fechaCaducidad = fechaCaducidad;
	}

	@Temporal( TemporalType.DATE )
	@Column( name = "FECHA_AVISO_CADUCIDAD", length = 7 )
	public Date getFechaAvisoCaducidad() {
		return fechaAvisoCaducidad;
	}

	public void setFechaAvisoCaducidad( Date fechaAvisoCaducidad ) {
		this.fechaAvisoCaducidad = fechaAvisoCaducidad;
	}

	@Column( name = "ESTADO", nullable = false, length = 9 )
	public String getEstado() {
		return estado;
	}

	public void setEstado( String estado ) {
		this.estado = estado;
	}

	@Column( name = "DOCUMENTO", updatable = false, insertable = false )
	@Type( type = "es.um.atica.hibernate.type.BFileType" )
	public OracleBfile getDocumento() {
		return documento;
	}

	public void setDocumento( OracleBfile documento ) {
		this.documento = documento;
	}

	@Temporal( TemporalType.DATE )
	@Column( name = "FECHA_VALIDA_DENIEGA", length = 7 )
	public Date getFechaValidaDeniega() {
		return fechaValidaDeniega;
	}

	public void setFechaValidaDeniega( Date fechaValidaDeniega ) {
		this.fechaValidaDeniega = fechaValidaDeniega;
	}

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "COD_USUARIO_VALIDA_DENIEGA" )
	public Usuario getUsuarioValidaDeniega() {
		return usuarioValidaDeniega;
	}

	public void setUsuarioValidaDeniega( Usuario usuarioValidaDeniega ) {
		this.usuarioValidaDeniega = usuarioValidaDeniega;
	}

	@Column( name = "MOTIVO_DENEGACION", length = 1000 )
	public String getMotivoDenegacion() {
		return motivoDenegacion;
	}

	public void setMotivoDenegacion( String motivoDenegacion ) {
		this.motivoDenegacion = motivoDenegacion;
	}

	@Transient
	public byte[] getFicheroBytes() throws SQLException {

		byte[] result = null;

		try {
			if ( ( documento != null ) && documento.fileExists() ) {
				documento.openFile();
				final ByteArrayOutputStream bao = new ByteArrayOutputStream();
				final InputStream in = documento.getBinaryStream();
				final byte[] buffer = new byte[1024];
				int len = -1;
				while ( ( len = in.read( buffer, 0, buffer.length ) ) > -1 ) {
					bao.write( buffer, 0, len );
					bao.flush();
				}
				bao.close();
				result = bao.toByteArray();
				documento.closeFile();
			}
		} catch ( final Exception ex ) {
			throw new SQLException( "Could not read OracleBfile:", ex );
		}
		return result;
	}

	@Transient
	public String getFicheroName() throws SQLException {

		String result = null;

		if ( ( documento != null ) && documento.fileExists() ) {
			result = documento.getName();
		}

		return result;
	}

	@Transient
	public String getFicheroExt() throws SQLException {

		final String result = getFicheroName();

		return result.substring( result.indexOf( "." ) + 1 );
	}

}
