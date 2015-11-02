package com.homefellas.model.core;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.codehaus.jackson.annotate.JsonIgnore;

@MappedSuperclass
public class AbstractSequenceModel extends AbstractModel
{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected long id = 0;
	
	@Override
	@JsonIgnore
	public Serializable getPrimaryKey() {
		return id;
	}

	@Override
	@JsonIgnore
	public boolean isPrimaryKeySet()
	{
		if (id<=0)
			return false;
		else 
			return true;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		AbstractSequenceModel other = (AbstractSequenceModel) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
