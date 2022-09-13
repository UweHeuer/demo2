package com.example.backend.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dilution.Dilution;
import com.example.backend.menu.Menu;
import com.example.backend.menu.MenuRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("admin")
@Slf4j
public class AdminController {

	@Autowired
	Dilution d;
	
	@Autowired
	MenuRepository menuRepo;
	
	@GetMapping("/import")
	public String importData() {
		
		log.trace(" called");
		
		String ret = new String(d.getParole()) + "<br>";
		
		Map<Integer, Menu> menuMap = new HashMap<>();
		Map<Integer, Integer> parentMap = new HashMap();
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/mysql506_2022-08-15", "root", "");
			PreparedStatement pstmt = connection.prepareStatement("select * from menux");
			ResultSet rs = pstmt.executeQuery();

			// read all old menus
			Menu actualImportedMenu, root = null;			
			
			while (rs.next()) {
				ret += d.decrypt(rs.getString("name")) + "<br>";
				
				// create new menu entities
				actualImportedMenu = new Menu(rs.getString("name"), rs.getString("comment"));
				
				// remember new menu entities for old id
				menuMap.put(Integer.valueOf(rs.getString("id")), actualImportedMenu);	
				
				// remember old parent id for old id
				Integer parentId;
				String parentIdString = rs.getString("parent_id");
				if (null != parentIdString) {
					parentId = Integer.parseInt(parentIdString);
				}
				else {
					log.trace("root node old id is " + Integer.valueOf(rs.getString("id")));
					parentId = 0;
					root = actualImportedMenu;
				}
				parentMap.put(Integer.valueOf(rs.getString("id")), parentId);
			}
			
			// set parent and children relation
			menuMap.forEach((oldId, newMenuEntity) -> { 
				if (0 != parentMap.get(oldId)) { menuMap.get(parentMap.get(oldId)).addSubMenu(newMenuEntity);}
			});
						
			// save menu entities
			menuRepo.save(root);
			
			// menuMap.forEach((oldId, newMenuEntity) -> menuRepo.save(newMenuEntity));
		
		}
		catch (Exception e) {
			return e.getMessage();
		}
		return ret;
		
	}
}
