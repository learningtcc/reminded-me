package com.homefellas.model.core;

import java.io.Serializable;
import java.util.UUID;

import com.homefellas.dao.core.Persistable;


public abstract class AbstractBaseTO extends AbstractBase implements Persistable{

	/**
	 * ck - Base of all info's to ensure that all are serializable and reuse
	 * shared information
	 */
	private static final long serialVersionUID = -7417934132268259344L;

	private Long id = new Long(0);
	private String error;
	/**
	 * 
	 *  business error codes
	 */
	public static String ERROR_GENERAL_FAILURE_DB = "errorGeneralFailureDB";
	//--------

	//----------------- for business error ----------
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public boolean hasError() {
		return this.getError() == null? false : true;
	}
	///-------------- end business error -----------
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		if (id == null)
			this.id = new Long(0);
		else
			this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AbstractBaseTO other = (AbstractBaseTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Serializable getPrimaryKey() {
		return getId();
	}
	
	protected String generateUUID()
	{
		return UUID.randomUUID().toString();
	}
	
}
