package org.inftel.tms.domain;

/**
 * Tipos de personas afectadas. Esta sociado con la minusvalia asociada a cada persona.
 * 
 * @author ibaca
 */
public enum AffectedType {
	/** Violencia de genero */
	VIOLENCIA_DE_GENERO("Violencia de genero"),
	/** Mayores de edad */
	MAYORES_DE_EDAD("Mayores de edad"),
	/** Enfermos cronicos */
	ENFERMOS_CRONICOS("Enfermos cronicos"),
	/** Deficiencia psiquica, fisica o sensorial */
	DEFICIENCIA_PSIQUICA_FISICA_SENSORIAL("Deficiencia psiquica, fisica o sensorial"),
	/** Otras deficiencias */
	OTROS("Otras deficiencias");

	String description;

	private AffectedType(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return description;
	}
}
