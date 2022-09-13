package com.example.backend.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;

import com.example.backend.baseentity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
@Slf4j
public class Menu 
  extends BaseEntity {
	
  private String name;
	private String comment;
	
	@ManyToOne
	@ToString.Exclude
	@JsonIgnore
	private Menu parent;
	@Type(type="org.hibernate.type.UUIDCharType")
	private UUID parentUUID;  
	
	@OneToMany(mappedBy="parent", cascade = {CascadeType.ALL})
	@ToString.Exclude
  private List<Menu> subMenus = new ArrayList<Menu>();
	
	@ElementCollection
	private List<String> subLinksUUIDs = new ArrayList<String>();
	
	private String URLParameters;
	
	// ctor for new menus
	public Menu(String name, String comment) {
		log.trace("Menu() called");
		this.name = name;
		this.comment = comment;
	}
	
	public void addSubMenu(Menu subMenu) {
		subMenus.add(subMenu);
		subMenu.setParent(this);
		subMenu.setParentUUID(this.uuid);
	}	
}
