package es.um.atica.sai.entities;
// Generated 24-jun-2009 10:06:20 by Hibernate Tools 3.2.2.GA

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "CONSUMO")
@SequenceGenerator(name="SEQ_CONSUMO",sequenceName="SAI_CONSUMO",initialValue=0,allocationSize=1)
@NamedQueries ( {
	@NamedQuery(name = Consumo.GET_CONSUMOS_ESTIMACION, query="SELECT con FROM Consumo con WHERE con.factura IS NULL AND con.estado NOT IN ('Anulado','Rechazado') AND con.producto.facturable='SI' AND con.presupuesto='NO'"),
	@NamedQuery(name = Consumo.GET_CONSUMO_BY_ID, query="SELECT con FROM Consumo con WHERE con.cod=:consumo"),
	@NamedQuery(name = Consumo.GET_FUNGIBLES_BY_DATE_SOLIC, query = "SELECT con FROM Consumo con WHERE con.tipo='F' AND con.fechaSolicitud=:fecha AND con.usuarioSolicitante=:usuariosolicitante"),
	@NamedQuery(name = Consumo.GET_CONSUMOS_HIJOS, query="SELECT con FROM Consumo con WHERE con.consumoPadre=:consumo and con.estado NOT IN ('Anulado','Rechazado') ORDER BY con.tipo"),
	@NamedQuery(name = Consumo.GET_PRESUPUESTOSHIJOS_TIPORESERVA_BY_PRESUPUESTO, query="SELECT con FROM Consumo con WHERE con.consumoPadre=:presupuestoorigen AND con.tipo='R' AND con.presupuesto='SI' AND con.estado NOT IN ('Anulado','Rechazado') ORDER BY con.cod"),
	@NamedQuery(name = Consumo.GET_CONSUMOS_PENDIENTES_IR_SERVICIO_ENTIDAD, query="SELECT con FROM Consumo con WHERE con.factura IS NULL AND con.estado NOT IN ('Anulado','Rechazado') AND con.usuarioIrAsociado=:usuarioir AND con.producto.servicio=:servicio AND con.producto.facturable='SI' AND (con.entidadPagadora=:entidadpagadora OR con.entidadPagadora IS NULL) AND con.presupuesto='NO'" ),
	@NamedQuery(name = Consumo.GET_CONSUMOS_PENDIENTES_IR_SERVICIO_ENTIDAD_SOLO_ESTADOS_FACTURABLES, query="SELECT con FROM Consumo con WHERE con.factura IS NULL AND con.estado = 'Finalizado' AND con.usuarioIrAsociado=:usuarioir AND con.producto.servicio=:servicio AND con.producto.facturable='SI' AND (con.entidadPagadora=:entidadpagadora OR con.entidadPagadora IS NULL) AND con.presupuesto='NO'" ),
	@NamedQuery(name = Consumo.GET_CONSUMOS_ACTIVOS_BY_PROYECTO, query="SELECT con FROM Consumo con WHERE con.proyecto=:proyecto AND con.estado NOT IN ('Anulado','Rechazado') AND con.presupuesto='NO'"),
	@NamedQuery(name = Consumo.GET_CONSUMOS_ANULADOS_BY_PROYECTO, query="SELECT con FROM Consumo con WHERE con.proyecto=:proyecto AND con.estado IN ('Anulado','Rechazado') AND con.presupuesto='NO'"),
	@NamedQuery(name = Consumo.GET_CONSUMOS_ACTIVOS_BY_PROYECTO_PRODUCTO, query="SELECT con FROM Consumo con WHERE con.proyecto=:proyecto AND con.producto=:producto AND con.estado NOT IN ('Anulado','Rechazado') AND con.presupuesto='NO'"),
	@NamedQuery(name = Consumo.GET_CONSUMOS_ANULADOS_BY_PROYECTO_PRODUCTO, query="SELECT con FROM Consumo con WHERE con.proyecto=:proyecto AND con.producto=:producto AND con.estado IN ('Anulado','Rechazado') AND con.presupuesto='NO'"),
	@NamedQuery(name = Consumo.GET_CANTIDAD_CONSUMIDA_BY_PROYECTO, query="SELECT SUM(con.cantidad) FROM Consumo con WHERE con.proyecto=:proyecto AND con.estado NOT IN ('Anulado','Rechazado') AND con.presupuesto='NO'"),
	@NamedQuery(name = Consumo.GET_CANTIDAD_CONSUMIDA_BY_PROYECTO_PRODUCTO, query="SELECT SUM(con.cantidad) FROM Consumo con WHERE con.proyecto=:proyecto AND con.producto=:producto AND con.estado NOT IN ('Anulado','Rechazado') AND con.presupuesto='NO'"),
	@NamedQuery(name = Consumo.GET_CANTIDAD_CONSUMIDA_BY_PROYECTO_PRODUCTO_NOT_LISTACONSUMOS, query="SELECT SUM(con.cantidad) FROM Consumo con WHERE con.proyecto=:proyecto AND con.producto=:producto AND con.estado NOT IN ('Anulado','Rechazado') AND con.cod NOT IN (:listacodsconsumo) AND con.presupuesto='NO'"),
	@NamedQuery(name = Consumo.GET_CONSUMO_GENERADO_FROM_PRESUPUESTO, query="SELECT con FROM Consumo con WHERE con.presupuestoOrigen=:presupuesto AND con.presupuesto='NO'"),
	@NamedQuery(name = Consumo.GET_STOCK_CONSUMIDO_BY_PRODUCTO, query="SELECT SUM(con.cantidad) FROM Consumo con WHERE con.producto=:producto AND con.estado NOT IN ('Anulado','Rechazado') AND con.presupuesto='NO'"),
	@NamedQuery(name = Consumo.GET_STOCK_CONSUMIDO_BY_PRODUCTO_NOT_LISTACONSUMOS, query="SELECT SUM(con.cantidad) FROM Consumo con WHERE con.producto=:producto AND con.estado NOT IN ('Anulado','Rechazado') AND con.cod NOT IN (:listacodsconsumo) AND con.presupuesto='NO'"),
})

@NamedNativeQueries({
	@NamedNativeQuery(name = Consumo.GET_ESTADISTICAS_GROUPBY_PRODUCTO, query = "SELECT prod.descripcion AS nombre, AVG( con.fecha_resolucion - con.fecha_solicitud) AS media, AVG( con.fecha_resolucion - con.fecha_aceptacion) AS aceptacion, prod.cod AS codigo FROM sai.consumo con, sai.producto prod WHERE con.cod_producto=prod.cod AND prod.cod_servicio IN (:cod_servicios) AND con.tipo='P' AND con.fecha_resolucion IS NOT NULL AND con.presupuesto='NO' group by prod.descripcion, prod.cod" ),
	@NamedNativeQuery(name = Consumo.GET_ESTADISTICAS_GROUPBY_SERVICIO, query = "SELECT serv.nombre AS nombre, AVG( con.fecha_resolucion - con.fecha_solicitud) AS media, AVG( con.fecha_resolucion - con.fecha_aceptacion) AS aceptacion, serv.cod AS codigo FROM sai.consumo con, sai.producto prod, sai.servicio serv WHERE con.cod_producto=prod.cod AND prod.cod_servicio=serv.cod AND prod.cod_servicio IN ( :cod_servicios ) AND con.tipo='P' AND con.fecha_resolucion IS NOT NULL  AND con.presupuesto='NO' group by serv.nombre, serv.cod" ),
	@NamedNativeQuery(name = Consumo.GET_CONSUMOS_RESUMEN, query = "SELECT PR.cod AS CODPRODUCTO, PR.descripcion AS DESCRIPCIONPRODUCTO, SUM(CO.cantidad) AS CANTIDAD, TR.duracion_minima AS MINUTOS, TR.duracion_minima*SUM(CO.cantidad) AS MINUTOSDURACION, TR.duracion_minima*SUM(CO.cantidad)/60 AS TOTALHORAS FROM sai.consumo CO, sai.producto PR, sai.tipo_reservable TR WHERE CO.cod_producto=PR.cod AND PR.cod_tiporeservable=TR.cod(+) AND CO.estado NOT IN ('Anulado', 'Rechazado') AND CO.presupuesto='NO' GROUP BY PR.cod, PR.descripcion, TR.duracion_minima" ),
	@NamedNativeQuery(name = Consumo.GET_CONSUMOS_RESUMEN_X_IR_ENTIDAD, query = "SELECT CO.ir_asociado AS CODUSUARIOIR, UD.nombre_completo AS NOMBREIR, CO.cod_entidad_pagadora AS CODENTIDADPAGADORA, EP.nombre AS NOMBREENTIDADPAGADORA, SUM(CO.cantidad) AS CANTIDAD FROM sai.consumo CO, sai.producto PR, sai.datos_usuario_view UD, sai.entidad_pagadora EP WHERE CO.cod_producto=PR.cod AND CO.cod_entidad_pagadora=EP.cod AND CO.ir_asociado = UD.cod AND CO.estado NOT IN ('Anulado', 'Rechazado') AND CO.presupuesto='NO' GROUP BY CO.ir_asociado, UD.nombre_completo,CO.cod_entidad_pagadora,EP.nombre"),
	@NamedNativeQuery(name = Consumo.GET_CONSUMOS_RESUMEN_X_IR_GRUPOINVESTIGACION, query = "SELECT CO.ir_asociado AS CODUSUARIOIR, UD.nombre_completo AS NOMBREIR, CO.cod_grupo_investigacion AS CODGRUPOINVESTIGACION, GID.nombre AS NOMBREGRUPOINVESTIGACION, SUM(CO.cantidad) AS CANTIDAD FROM sai.consumo CO, sai.producto PR, sai.datos_usuario_view UD, sai.grupo_investigacion_datos GID WHERE CO.cod_producto=PR.cod AND CO.cod_grupo_investigacion=GID.cod(+) AND CO.ir_asociado = UD.cod AND CO.cod_entidad_pagadora IS NOT NULL AND CO.estado NOT IN ('Anulado', 'Rechazado') AND CO.presupuesto='NO' GROUP BY CO.ir_asociado, UD.nombre_completo,CO.cod_grupo_investigacion,GID.nombre"),
	@NamedNativeQuery(name = Consumo.GET_MIEMBROS_CONSUMOS_RESUMEN,query="SELECT DISTINCT CO.cod_usuario_solic FROM sai.consumo CO, sai.producto PR WHERE CO.cod_producto=PR.cod AND CO.estado NOT IN ('Anulado', 'Rechazado') AND CO.presupuesto='NO'"),
	@NamedNativeQuery(name = Consumo.GET_IRS_CONSUMOS_RESUMEN,query="SELECT DISTINCT CO.ir_asociado FROM sai.consumo CO, sai.producto PR WHERE CO.cod_producto=PR.cod AND CO.ir_asociado IS NOT NULL AND CO.cod_entidad_pagadora IS NOT NULL AND CO.estado NOT IN ('Anulado', 'Rechazado') AND CO.presupuesto='NO'"),
	@NamedNativeQuery(name = Consumo.GET_CONSUMOS_RESUMEN_PENDIENTES_FACTURAR, query = "SELECT PR.cod AS CODPRODUCTO, PR.descripcion AS DESCRIPCIONPRODUCTO, SUM(CO.cantidad) AS CANTIDAD, TR.duracion_minima AS MINUTOS, TR.duracion_minima*SUM(CO.cantidad) AS MINUTOSDURACION, TR.duracion_minima*SUM(CO.cantidad)/60 AS TOTALHORAS FROM sai.consumo CO, sai.producto PR, sai.tipo_reservable TR WHERE CO.cod_producto=PR.cod AND PR.cod_tiporeservable=TR.cod(+) AND CO.estado NOT IN ('Anulado', 'Rechazado') AND CO.presupuesto='NO' AND CO.factura IS NULL GROUP BY PR.cod, PR.descripcion, TR.duracion_minima" ),
	@NamedNativeQuery(name = Consumo.GET_CONSUMOS_RESUMEN_X_IR_ENTIDAD_PENDIENTES_FACTURAR, query = "SELECT CO.ir_asociado AS CODUSUARIOIR, UD.nombre_completo AS NOMBREIR, CO.cod_entidad_pagadora AS CODENTIDADPAGADORA, EP.nombre AS NOMBREENTIDADPAGADORA, SUM(CO.cantidad) AS CANTIDAD FROM sai.consumo CO, sai.producto PR, sai.datos_usuario_view UD, sai.entidad_pagadora EP WHERE CO.cod_producto=PR.cod AND CO.cod_entidad_pagadora=EP.cod AND CO.ir_asociado = UD.cod AND CO.estado NOT IN ('Anulado', 'Rechazado') AND CO.presupuesto='NO' AND CO.factura IS NULL GROUP BY CO.ir_asociado, UD.nombre_completo,CO.cod_entidad_pagadora,EP.nombre"),
	@NamedNativeQuery(name = Consumo.GET_CONSUMOS_RESUMEN_X_IR_GRUPOINVESTIGACION_PENDIENTES_FACTURAR, query = "SELECT CO.ir_asociado AS CODUSUARIOIR, UD.nombre_completo AS NOMBREIR, CO.cod_grupo_investigacion AS CODGRUPOINVESTIGACION, GID.nombre AS NOMBREGRUPOINVESTIGACION, SUM(CO.cantidad) AS CANTIDAD FROM sai.consumo CO, sai.producto PR, sai.datos_usuario_view UD, sai.grupo_investigacion_datos GID WHERE CO.cod_producto=PR.cod AND CO.cod_grupo_investigacion=GID.cod(+) AND CO.ir_asociado = UD.cod AND CO.cod_entidad_pagadora IS NOT NULL AND CO.estado NOT IN ('Anulado', 'Rechazado') AND CO.presupuesto='NO' AND CO.factura IS NULL GROUP BY CO.ir_asociado, UD.nombre_completo,CO.cod_grupo_investigacion,GID.nombre"),
	@NamedNativeQuery(name = Consumo.GET_MIEMBROS_CONSUMOS_RESUMEN_PENDIENTES_FACTURAR,query="SELECT DISTINCT CO.cod_usuario_solic FROM sai.consumo CO, sai.producto PR WHERE CO.cod_producto=PR.cod AND CO.estado NOT IN ('Anulado', 'Rechazado') AND CO.presupuesto='NO' AND CO.factura IS NULL"),
	@NamedNativeQuery(name = Consumo.GET_IRS_CONSUMOS_RESUMEN_PENDIENTES_FACTURAR,query="SELECT DISTINCT CO.ir_asociado FROM sai.consumo CO, sai.producto PR WHERE CO.cod_producto=PR.cod AND CO.ir_asociado IS NOT NULL AND CO.cod_entidad_pagadora IS NOT NULL AND CO.estado NOT IN ('Anulado', 'Rechazado') AND CO.presupuesto='NO' AND CO.factura IS NULL"),
	@NamedNativeQuery(name = Consumo.GET_CONSUMOS_RESUMEN_FACTURADOS, query = "SELECT PR.cod AS CODPRODUCTO, PR.descripcion AS DESCRIPCIONPRODUCTO, SUM(CO.cantidad) AS CANTIDAD, TR.duracion_minima AS MINUTOS, TR.duracion_minima*SUM(CO.cantidad) AS MINUTOSDURACION, TR.duracion_minima*SUM(CO.cantidad)/60 AS TOTALHORAS FROM sai.consumo CO, sai.producto PR, sai.tipo_reservable TR WHERE CO.cod_producto=PR.cod AND PR.cod_tiporeservable=TR.cod(+) AND CO.estado NOT IN ('Anulado', 'Rechazado') AND CO.presupuesto='NO' AND CO.factura IS NOT NULL GROUP BY PR.cod, PR.descripcion, TR.duracion_minima" ),
	@NamedNativeQuery(name = Consumo.GET_CONSUMOS_RESUMEN_X_IR_ENTIDAD_FACTURADOS, query = "SELECT CO.ir_asociado AS CODUSUARIOIR, UD.nombre_completo AS NOMBREIR, CO.cod_entidad_pagadora AS CODENTIDADPAGADORA, EP.nombre AS NOMBREENTIDADPAGADORA, SUM(CO.cantidad) AS CANTIDAD FROM sai.consumo CO, sai.producto PR, sai.datos_usuario_view UD, sai.entidad_pagadora EP WHERE CO.cod_producto=PR.cod AND CO.cod_entidad_pagadora=EP.cod AND CO.ir_asociado = UD.cod AND CO.estado NOT IN ('Anulado', 'Rechazado') AND CO.presupuesto='NO' AND CO.factura IS NOT NULL GROUP BY CO.ir_asociado, UD.nombre_completo,CO.cod_entidad_pagadora,EP.nombre"),
	@NamedNativeQuery(name = Consumo.GET_CONSUMOS_RESUMEN_X_IR_GRUPOINVESTIGACION_FACTURADOS, query = "SELECT CO.ir_asociado AS CODUSUARIOIR, UD.nombre_completo AS NOMBREIR, CO.cod_grupo_investigacion AS CODGRUPOINVESTIGACION, GID.nombre AS NOMBREGRUPOINVESTIGACION, SUM(CO.cantidad) AS CANTIDAD FROM sai.consumo CO, sai.producto PR, sai.datos_usuario_view UD, sai.grupo_investigacion_datos GID WHERE CO.cod_producto=PR.cod AND CO.cod_grupo_investigacion=GID.cod(+) AND CO.ir_asociado = UD.cod AND CO.estado NOT IN ('Anulado', 'Rechazado') AND CO.cod_entidad_pagadora IS NOT NULL AND CO.presupuesto='NO' AND CO.factura IS NOT NULL GROUP BY CO.ir_asociado, UD.nombre_completo,CO.cod_grupo_investigacion,GID.nombre"),
	@NamedNativeQuery(name = Consumo.GET_MIEMBROS_CONSUMOS_RESUMEN_FACTURADOS,query="SELECT DISTINCT CO.cod_usuario_solic FROM sai.consumo CO, sai.producto PR WHERE CO.cod_producto=PR.cod AND CO.estado NOT IN ('Anulado', 'Rechazado') AND CO.presupuesto='NO' AND CO.factura IS NOT NULL"),
	@NamedNativeQuery(name = Consumo.GET_IRS_CONSUMOS_RESUMEN_FACTURADOS,query="SELECT DISTINCT CO.ir_asociado FROM sai.consumo CO, sai.producto PR WHERE CO.cod_producto=PR.cod AND CO.ir_asociado IS NOT NULL AND CO.cod_entidad_pagadora IS NOT NULL AND CO.estado NOT IN ('Anulado', 'Rechazado') AND CO.presupuesto='NO' AND CO.factura IS NOT NULL"),

})

public class Consumo implements java.io.Serializable, Comparable<Consumo> {

	private static final long serialVersionUID = -8287095922871158593L;

	public static final String GET_CONSUMOS_ESTIMACION = "CONSUMO.GET_CONSUMOS_ESTIMACION";
	public static final String GET_CONSUMO_BY_ID = "CONSUMO.GET_CONSUMO_BY_ID";
	public static final String GET_CONSUMOS_RESUMEN = "CONSUMO.GET_CONSUMOS_RESUMEN";
	public static final String GET_CONSUMOS_RESUMEN_X_IR_ENTIDAD = "CONSUMO.GET_CONSUMOS_RESUMEN_X_IR_ENTIDAD";
	public static final String GET_CONSUMOS_RESUMEN_X_IR_GRUPOINVESTIGACION = "CONSUMO.GET_CONSUMOS_RESUMEN_X_IR_GRUPOINVESTIGACION";
	public static final String GET_MIEMBROS_CONSUMOS_RESUMEN = "CONSUMO.GET_MIEMBROS_CONSUMOS_RESUMEN";
	public static final String GET_IRS_CONSUMOS_RESUMEN = "CONSUMO.GET_IRS_CONSUMOS_RESUMEN";
	public static final String GET_CONSUMOS_RESUMEN_PENDIENTES_FACTURAR = "CONSUMO.GET_CONSUMOS_RESUMEN_PENDIENTES_FACTURAR";
	public static final String GET_CONSUMOS_RESUMEN_X_IR_ENTIDAD_PENDIENTES_FACTURAR = "CONSUMO.GET_CONSUMOS_RESUMEN_X_IR_ENTIDAD_PENDIENTES_FACTURAR";
	public static final String GET_CONSUMOS_RESUMEN_X_IR_GRUPOINVESTIGACION_PENDIENTES_FACTURAR = "CONSUMO.GET_CONSUMOS_RESUMEN_X_IR_GRUPOINVESTIGACION_PENDIENTES_FACTURAR";
	public static final String GET_MIEMBROS_CONSUMOS_RESUMEN_PENDIENTES_FACTURAR = "CONSUMO.GET_MIEMBROS_CONSUMOS_RESUMEN_PENDIENTES_FACTURAR";
	public static final String GET_IRS_CONSUMOS_RESUMEN_PENDIENTES_FACTURAR = "CONSUMO.GET_IRS_CONSUMOS_RESUMEN_PENDIENTES_FACTURAR";
	public static final String GET_CONSUMOS_RESUMEN_FACTURADOS = "CONSUMO.GET_CONSUMOS_RESUMEN_FACTURADOS";
	public static final String GET_CONSUMOS_RESUMEN_X_IR_ENTIDAD_FACTURADOS = "CONSUMO.GET_CONSUMOS_RESUMEN_X_IR_ENTIDAD_FACTURADOS";
	public static final String GET_CONSUMOS_RESUMEN_X_IR_GRUPOINVESTIGACION_FACTURADOS = "CONSUMO.GET_CONSUMOS_RESUMEN_X_IR_GRUPOINVESTIGACION_FACTURADOS";
	public static final String GET_MIEMBROS_CONSUMOS_RESUMEN_FACTURADOS = "CONSUMO.GET_MIEMBROS_CONSUMOS_RESUMEN_FACTURADOS";
	public static final String GET_IRS_CONSUMOS_RESUMEN_FACTURADOS = "CONSUMO.GET_IRS_CONSUMOS_RESUMEN_FACTURADOS";
	public static final String GET_FUNGIBLES_BY_DATE_SOLIC = "CONSUMO.GET_FUNGIBLES_BY_DATE_SOLIC";
	public static final String GET_CONSUMOS_HIJOS = "CONSUMO.GET_CONSUMOS_HIJOS";
	public static final String GET_PRESUPUESTOSHIJOS_TIPORESERVA_BY_PRESUPUESTO = "CONSUMO.GET_PRESUPUESTOSHIJOS_TIPORESERVA_BY_PRESUPUESTO";
	public static final String GET_CONSUMOS_PENDIENTES_IR_SERVICIO_ENTIDAD = "CONSUMO.GET_CONSUMOS_PENDIENTES_IR_SERVICIO_ENTIDAD";
	public static final String GET_CONSUMOS_PENDIENTES_IR_SERVICIO_ENTIDAD_SOLO_ESTADOS_FACTURABLES = "CONSUMO.GET_CONSUMOS_PENDIENTES_IR_SERVICIO_ENTIDAD_SOLO_ESTADOS_FACTURABLES";
	public static final String GET_ESTADISTICAS_GROUPBY_SERVICIO = "CONSUMO.GET_ESTADISTICAS_GROUPBY_SERVICIO";
	public static final String GET_ESTADISTICAS_GROUPBY_PRODUCTO = "CONSUMO.GET_ESTADISTICAS_GROUPBY_PRODUCTO";
	public static final String GET_CONSUMOS_ACTIVOS_BY_PROYECTO = "CONSUMO.GET_CONSUMOS_ACTIVOS_BY_PROYECTO";
	public static final String GET_CONSUMOS_ANULADOS_BY_PROYECTO = "CONSUMO.GET_CONSUMOS_ANULADOS_BY_PROYECTO";
	public static final String GET_CONSUMOS_ACTIVOS_BY_PROYECTO_PRODUCTO = "CONSUMO.GET_CONSUMOS_ACTIVOS_BY_PROYECTO_PRODUCTO";
	public static final String GET_CONSUMOS_ANULADOS_BY_PROYECTO_PRODUCTO = "CONSUMO.GET_CONSUMOS_ANULADOS_BY_PROYECTO_PRODUCTO";
	public static final String GET_CANTIDAD_CONSUMIDA_BY_PROYECTO = "CONSUMO.GET_CANTIDAD_CONSUMIDA_BY_PROYECTO";
	public static final String GET_CANTIDAD_CONSUMIDA_BY_PROYECTO_PRODUCTO = "CONSUMO.GET_CANTIDAD_CONSUMIDA_BY_PROYECTO_PRODUCTO";
	public static final String GET_CANTIDAD_CONSUMIDA_BY_PROYECTO_PRODUCTO_NOT_LISTACONSUMOS = "CONSUMO.GET_CANTIDAD_CONSUMIDA_BY_PROYECTO_PRODUCTO_NOT_LISTACONSUMOS";
	public static final String GET_CONSUMO_GENERADO_FROM_PRESUPUESTO = "CONSUMO.GET_CONSUMO_GENERADO_FROM_PRESUPUESTO";
	public static final String GET_STOCK_CONSUMIDO_BY_PRODUCTO = "CONSUMO.GET_STOCK_CONSUMIDO_BY_PRODUCTO";
	public static final String GET_STOCK_CONSUMIDO_BY_PRODUCTO_NOT_LISTACONSUMOS = "CONSUMO.GET_STOCK_CONSUMIDO_BY_PRODUCTO_NOT_LISTACONSUMOS";
	
	private Long cod;
	private Producto producto;
	private Factura factura;
	private Usuario usuarioConectado;
	private Usuario usuarioIrAsociado;
	private Usuario usuarioSolicitante;
	private Usuario usuarioTecnicoAsignado;
	private EntidadPagadora entidadPagadora;
	private GrupoInvestigacion grupoInvestigacion;
	private String estado;
	private String estadoPresupuesto;
	private Date fechaSolicitud;
	private String motivoRechazo;
	private String observaciones;
	private String bitacora;
	private String bitacoraUsuario;
	private String tipo;
	private BigDecimal cantidad;
	private BigDecimal importeTarifaFactura;
	private Date fechaAceptacion;
	private Date fechaResolucion;
	private String comentarioResolucion;
	private String comentarioFacturacion;
	private String solicitudTecnico;
	private String interno;
	private String organizacion;
	private String nivelBioseguridad;
	private Proyecto proyecto;
	private String presupuesto;
	private Date finValidezPresupuesto;
	private Consumo presupuestoOrigen;
	private Consumo consumoPadre = null;
	private List<Reservas> reservas = new ArrayList<>(0);
	private List<Muestra> muestras = new ArrayList<>(0);
	private List<Anexo> anexos = new ArrayList<>(0);
	private List<Consumo> consumosHijos = new ArrayList<>(0);
	private List<Consumo> consumosGeneradosDelPresupuesto = new ArrayList<>(0);
	private List<ConsumoEquipo> listaEquipos = new ArrayList<>();

	private boolean seleccionado;

	public Consumo() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CONSUMO")
	@Column(name = "COD",  nullable = false, precision=19, scale = 0)
	public Long getCod() {
		return cod;
	}

	public void setCod(Long cod) {
		this.cod = cod;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_PRODUCTO", nullable = false)
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FACTURA")
	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_USUARIO_APLIC", nullable = false)
	@NotNull
	public Usuario getUsuarioConectado() {
		return usuarioConectado;
	}

	public void setUsuarioConectado(Usuario usuarioConectado) {
		this.usuarioConectado = usuarioConectado;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IR_ASOCIADO", nullable = true)
	public Usuario getUsuarioIrAsociado() {
		return usuarioIrAsociado;
	}

	public void setUsuarioIrAsociado(Usuario usuarioIrAsociado) {
		this.usuarioIrAsociado = usuarioIrAsociado;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_USUARIO_SOLIC", nullable = false)
	public Usuario getUsuarioSolicitante() {
		return usuarioSolicitante;
	}

	public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_ENTIDAD_PAGADORA", nullable = true)
	public EntidadPagadora getEntidadPagadora()
	{
		return entidadPagadora;
	}

	public void setEntidadPagadora(EntidadPagadora entidadPagadora) {
		this.entidadPagadora = entidadPagadora;
	}

	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_GRUPO_INVESTIGACION")
	public GrupoInvestigacion getGrupoInvestigacion() {
		return grupoInvestigacion;
	}
	
	public void setGrupoInvestigacion( GrupoInvestigacion grupoInvestigacion ) {
		this.grupoInvestigacion = grupoInvestigacion;
	}

	@Column(name = "ESTADO", length = 20)
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Column(name = "ESTADO_PRESUPUESTO", length = 20)
	public String getEstadoPresupuesto() {
		return estadoPresupuesto;
	}

	public void setEstadoPresupuesto( String estadoPresupuesto ) {
		this.estadoPresupuesto = estadoPresupuesto;
	}

	@Column(name = "COD_ORGANIZACION", length=40)
	public String getOrganizacion() {
		return organizacion;
	}

	public void setOrganizacion(String organizacion) {
		this.organizacion = organizacion;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_SOLICITUD", nullable = false, length = 7)
	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	@Column(name = "MOTIVO_RECHAZO", length = 200)
	public String getMotivoRechazo() {
		return motivoRechazo;
	}

	public void setMotivoRechazo(String motivoRechazo) {
		this.motivoRechazo = motivoRechazo;
	}

	@Column( name = "OBSERVACIONES", length = 4000 )
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Column(name = "BITACORA", length = 4000)
	public String getBitacora() {
		return bitacora;
	}

	public void setBitacora(String bit) {
		bitacora = bit;
	}

	@Column(name = "BITACORA_USUARIO", length = 4000)
	public String getBitacoraUsuario() {
		return bitacoraUsuario;
	}

	public void setBitacoraUsuario(String bit) {
		bitacoraUsuario = bit;
	}

	@Column(name = "TIPO", nullable = false, length = 1)
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Column(name = "CANTIDAD", nullable = false, precision = 14, scale = 2)
	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	@Column(name = "IMPORTE_TARIFA_FACTURA")
	public BigDecimal getImporteTarifaFactura() {
		return importeTarifaFactura;
	}

	public void setImporteTarifaFactura( BigDecimal importeTarifaFactura ) {
		this.importeTarifaFactura = importeTarifaFactura;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_ACEPTACION", length = 7)
	public Date getFechaAceptacion() {
		return fechaAceptacion;
	}

	public void setFechaAceptacion( Date fechaAceptacion ) {
		this.fechaAceptacion = fechaAceptacion;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_RESOLUCION", length = 7)
	public Date getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(Date fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	@Column(name = "COMENTARIO_RESOLUCION", length = 4000)
	@Length(max = 4000)
	public String getComentarioResolucion() {
		return comentarioResolucion;
	}

	public void setComentarioResolucion(String comentarioResolucion) {
		this.comentarioResolucion = comentarioResolucion;
	}

	@Column(name = "COMENTARIO_FACTURACION", length = 4000)
	@Length(max = 4000)
	public String getComentarioFacturacion() {
		return comentarioFacturacion;
	}

	public void setComentarioFacturacion( String comentarioFacturacion ) {
		this.comentarioFacturacion = comentarioFacturacion;
	}

	@Column(name = "SOLICITUD_TECNICO", length = 2)
	@Length(max = 2)
	public String getSolicitudTecnico() {
		return solicitudTecnico;
	}

	public void setSolicitudTecnico(String solicitudTecnico) {
		this.solicitudTecnico = solicitudTecnico;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TECNICO_ASIGNADO")
	public Usuario getUsuarioTecnicoAsignado() {
		return usuarioTecnicoAsignado;
	}

	public void setUsuarioTecnicoAsignado( Usuario usuarioTecnicoAsignado ) {
		this.usuarioTecnicoAsignado = usuarioTecnicoAsignado;
	}

	@Column(name = "INTERNO", nullable=false, length = 2)
	@Length(max = 2)
	@NotNull
	public String getInterno() {
		return interno;
	}

	public void setInterno(String interno) {
		this.interno = interno;
	}

	@Column(name = "NIVEL_BIOSEGURIDAD", length = 4)
	public String getNivelBioseguridad() {
		return nivelBioseguridad;
	}

	public void setNivelBioseguridad( String nivelBioseguridad ) {
		this.nivelBioseguridad = nivelBioseguridad;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_PROYECTO")
	public Proyecto getProyecto() {
		return proyecto;
	}

	public void setProyecto( Proyecto proyecto ) {
		this.proyecto = proyecto;
	}

	@Column(name = "PRESUPUESTO", nullable=false, length = 2)
	public String getPresupuesto() {
		return presupuesto;
	}

	public void setPresupuesto( String presupuesto ) {
		this.presupuesto = presupuesto;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FIN_VALIDEZ_PRESUPUESTO", length = 7)
	public Date getFinValidezPresupuesto() {
		return finValidezPresupuesto;
	}

	public void setFinValidezPresupuesto( Date finValidezPresupuesto ) {
		this.finValidezPresupuesto = finValidezPresupuesto;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_PRESUPUESTO_ORIGEN", nullable = true)
	public Consumo getPresupuestoOrigen() {
		return presupuestoOrigen;
	}

	public void setPresupuestoOrigen( Consumo presupuestoOrigen ) {
		this.presupuestoOrigen = presupuestoOrigen;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_CONSUMO_PADRE", nullable = true)
	public Consumo getConsumoPadre() {
		return consumoPadre;
	}

	public void setConsumoPadre(Consumo consumoPadre) {
		this.consumoPadre = consumoPadre;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "consumo")
	@OrderBy("fechaInicio ASC")
	public List<Reservas> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reservas> reservaList) {
		reservas = reservaList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consumo")
	@OrderBy("cod DESC")
	public List<Muestra> getMuestras() {
		return muestras;
	}

	public void setMuestras(List<Muestra> muestras) {
		this.muestras = muestras;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consumo")
	@OrderBy("cod DESC")
	public List<Anexo> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<Anexo> anexos) {
		this.anexos = anexos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consumoPadre")
	public List<Consumo> getConsumosHijos() {
		return consumosHijos;
	}

	public void setConsumosHijos(List<Consumo> consumosHijos) {
		this.consumosHijos = consumosHijos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "presupuestoOrigen")
	public List<Consumo> getConsumosGeneradosDelPresupuesto() {
		return consumosGeneradosDelPresupuesto;
	}
	
	public void setConsumosGeneradosDelPresupuesto( List<Consumo> consumosGeneradosDelPresupuesto ) {
		this.consumosGeneradosDelPresupuesto = consumosGeneradosDelPresupuesto;
	}

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "consumo" )
	public List<ConsumoEquipo> getListaEquipos() {
		return listaEquipos;
	}

	public void setListaEquipos( List<ConsumoEquipo> listaEquipos ) {
		this.listaEquipos = listaEquipos;
	}

	@Transient
	public boolean isSeleccionado(){
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado){
		this.seleccionado=seleccionado;
	}

	@Override
	public int compareTo(Consumo consumo )
	{
		// Comparamos por producto
		return getProducto().getCod().compareTo(consumo.getProducto().getCod());
	}

	@Override
	public boolean equals(Object other) {
		// Codigo que comprueba si las dos entidades son iguales
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof Consumo)) {
			return false;
		}
		final Consumo castOther = (Consumo) other;
		if (castOther.getCod()!=null) {
			return (getCod() == castOther.getCod());
		}else {
			return (getProducto().getCod() == castOther.getProducto().getCod());
		}
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = ( 37 * result ) + (Integer.valueOf(cod.toString()));
		return result;
	}





}
