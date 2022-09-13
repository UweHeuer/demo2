package com.example.backend.menu;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.menu.Menu;

@Repository // actually not needed if extending
public interface MenuRepository 
  extends JpaRepository<Menu, Long> {
	
	Menu findByUuid(UUID uuid);

	Menu findByParentIs(Menu parent);

	void deleteByUuid(UUID uuid);
}
