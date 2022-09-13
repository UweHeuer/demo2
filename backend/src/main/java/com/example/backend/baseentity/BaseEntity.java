package com.example.backend.baseentity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import org.hibernate.annotations.Type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@MappedSuperclass // base class w/o separate table
@EqualsAndHashCode(onlyExplicitlyIncluded=true) // generate equals() and hashCode() based on fields marked with @EqualsAndHashCode.Include
@Getter @Setter // generate getter and setter for all non-static fields
@Slf4j // generates a logger instance with default name log (see logback-spring.xml)
public abstract class BaseEntity {

  @Id @GeneratedValue // id to be used in DB
  private Long id;	
  
	@EqualsAndHashCode.Include
	@Type(type="org.hibernate.type.UUIDCharType")
	protected UUID uuid = UUID.randomUUID();;  
	
	// Before the transaction wants to make an update, it checks the version property. If the value has changed in the meantime, an 
	// OptimisticLockException is thrown. Otherwise, the transaction commits the update and increments a value version property.
  @Version
  private Long version;
  
  private Date created;
  private Date updated;
  
  @PrePersist // called before persisting a new entity
  public void setCreationDate() {
  	
  	log.trace("setCreationDate() called");
    this.created = new Date();
  }

  @PreUpdate // called before updating a new entity
  public void setChangeDate() {
  	
  	log.trace("setChangeDate() called");  	
  	this.updated = new Date();
  }  

}  // class BaseEntity
