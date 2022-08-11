package es.um.atica.sai.entities;
// Generated 24-jun-2009 10:06:20 by Hibernate Tools 3.2.2.GA

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class EntidadesIrId implements java.io.Serializable {

	private static final long serialVersionUID = -1524380552826822208L;

	private Long codEntidad;
	private Long codIr;

	public EntidadesIrId() {
	}

	public EntidadesIrId(Long codEntidiad, Long codIr) {
		this.codEntidad = codEntidiad;
		this.codIr = codIr;
	}

	@Column(name = "COD_ENTIDAD", nullable = false, precision = 19, scale = 0)
	@NotNull
	public Long getCodEntidad() {
		return this.codEntidad;
	}

	public void setCodEntidad(Long codEntidad) {
		this.codEntidad = codEntidad;
	}

	@Column(name = "COD_IR", nullable = false, precision=19, scale = 0)
	@NotNull
	public Long getCodIr() {
		return this.codIr;
	}

	public void setCodIr(Long codIr) {
		this.codIr = codIr;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EntidadesIrId))
			return false;
		EntidadesIrId castOther = (EntidadesIrId) other;

		return (this.getCodEntidad() == castOther.getCodEntidad())
		&& ((this.getCodIr() == castOther.getCodIr()));
	}
	
	public int hashCode() {        
        int result = 17;
        result = 37 * result + (Integer.valueOf(this.codEntidad.toString()));
        result = 37 * result + (Integer.valueOf(this.codIr.toString()));
        return result;
       }

}
