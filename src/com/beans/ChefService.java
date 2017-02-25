package com.beans;

public class ChefService extends Employe implements LienDirChefSer {
	private String nomService;

	public String getNomService() {
		return nomService;
	}

	public void setNomService(String nomService) {
		this.nomService = nomService;
	}
}
