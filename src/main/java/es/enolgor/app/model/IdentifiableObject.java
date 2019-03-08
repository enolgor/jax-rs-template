package es.enolgor.app.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
public class IdentifiableObject implements Serializable{
	
	@ApiModelProperty(required=false) private String id;
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
}
