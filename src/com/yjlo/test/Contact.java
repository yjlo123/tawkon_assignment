package com.yjlo.test;

public class Contact {
	private String firstName;
	private String lastName;
	private String country;
	private String description;
	private String picture;
	private int id;
	
	public Contact(){
		this("","","","","",0);
	}
	
	public Contact(String firstName, String lastName, String country, String description, String picture, int id){
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
		this.description = description;
		this.picture = picture;
		this.id = id;
	}
	
	public void setFirstName(String newFirstName){
		this.firstName = newFirstName;
	}
	public void setLastName(String newLastName){
		this.lastName = newLastName;
	}
	public void setCountry(String newCountry){
		this.country = newCountry;
	}
	public void setDescription(String newDescription){
		this.description = newDescription;
	}
	public void setPicture(String newPicture){
		this.picture = newPicture;
	}
	
	public String getFirstName(){
		return this.firstName;
	}
	public String getLastName(){
		return this.lastName;
	}
	public String getCountry(){
		return this.country;
	}
	public String getDescription(){
		return this.description;
	}
	public String getPicture(){
		return this.picture;
	}
	public int getId(){
		return this.id;
	}
}
