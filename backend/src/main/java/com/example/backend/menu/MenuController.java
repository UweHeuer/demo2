package com.example.backend.menu;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("menus")
@Slf4j
public class MenuController {

	MenuRepository menuRepo;
	
	// implicit constructor-based injection
	public MenuController(MenuRepository menuRepo) {
		this.menuRepo = menuRepo;
	}
	
	@GetMapping("/")
	Menu getRoot() {
		log.info("getMenus() called");
		return menuRepo.findByParentIs(null);
	}
	
	@GetMapping("/{uuid}")
	Menu getMenu(@PathVariable UUID uuid) {
		return menuRepo.findByUuid(uuid);
	}
	
	@DeleteMapping("/{uuid}")
  void deleteEmployee(@PathVariable UUID uuid) {
    menuRepo.deleteByUuid(uuid);
  }
	
  @PostMapping("")
  Menu addMenu(@RequestBody Menu newMenu) {
    return menuRepo.save(newMenu);
  }
	
}
