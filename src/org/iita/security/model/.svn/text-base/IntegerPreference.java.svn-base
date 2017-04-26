package org.iita.security.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="5")
public class IntegerPreference extends Preference {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4514571631549445305L;
	
	private Integer preferenceValue;

	/**
	 * 
	 */
	public IntegerPreference() {
		
	}
	
	/**
	 * @param value
	 */
	public IntegerPreference(Integer value) {
		this.preferenceValue=value;
	}


	@Override
	@Column(name="intVal")
	public Integer getPreferenceValue() {
		return preferenceValue;
	}

	
	public void setPreferenceValue(Integer value) {
		this.preferenceValue = value;
	}
	
	/**
	 * @see org.iita.entity.IPreference#castValue(java.lang.Object)
	 */
	@Override
	public void setPreferenceValue(Object value) {
		setPreferenceValue((Integer) value);
	}
}
