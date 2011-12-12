package org.openhds.web.beans;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import org.openhds.web.service.JsfService;
import org.springframework.core.io.ClassPathResource;

public class LocationLevelConfigBean {
	
	String level1;
	String level2;
	String level3;
	String level4;
	String level5;
	String level6;
	String level7;
	String level8;
	String level9;

	JsfService jsfService;

	public void create() {
		Properties properties = readLocationProperties();
		properties.put("locationHierarchyLevel1", level1);
		properties.put("locationHierarchyLevel2", level2);
		properties.put("locationHierarchyLevel3", level3);
		properties.put("locationHierarchyLevel4", level4);
		properties.put("locationHierarchyLevel5", level5);
		properties.put("locationHierarchyLevel6", level6);
		properties.put("locationHierarchyLevel7", level7);
		properties.put("locationHierarchyLevel8", level8);
		properties.put("locationHierarchyLevel9", level9);	
		writePropertyFile(properties);
	}
	
	public Properties readLocationProperties() {
		FileInputStream fis = null;
		Properties prop = null;
		
		try {
			fis = new FileInputStream(new ClassPathResource("location-levels.properties").getFile());
			if (fis != null) {
				prop = new Properties();
				prop.load(fis);
			}
			fis.close();	
		} catch (Exception e) {
			jsfService.addMessage("Error in reading Property file. Exception : " + e.getMessage());
			prop = null;
		}
		return prop;
	}
	
	public void writePropertyFile(Properties props) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("src/main/resources/location-levels.properties");
			props.store(fos, "Location Levels Configuration updated");
		} catch (Exception e) {
			jsfService.addMessage("Error writing Property file. Exception : " + e.getMessage());
			return;
		}
		jsfService.addMessage("Location Level Configuration updated successfully. Redeploy the web application for changes to take effect.");
	}
		
	public JsfService getJsfService() {
		return jsfService;
	}

	public void setJsfService(JsfService jsfService) {
		this.jsfService = jsfService;
	}
	
	public String getLevel1() {
		return level1;
	}

	public void setLevel1(String level1) {
		this.level1 = level1;
	}

	public String getLevel2() {
		return level2;
	}

	public void setLevel2(String level2) {
		this.level2 = level2;
	}

	public String getLevel3() {
		return level3;
	}

	public void setLevel3(String level3) {
		this.level3 = level3;
	}

	public String getLevel4() {
		return level4;
	}

	public void setLevel4(String level4) {
		this.level4 = level4;
	}

	public String getLevel5() {
		return level5;
	}

	public void setLevel5(String level5) {
		this.level5 = level5;
	}

	public String getLevel6() {
		return level6;
	}

	public void setLevel6(String level6) {
		this.level6 = level6;
	}

	public String getLevel7() {
		return level7;
	}

	public void setLevel7(String level7) {
		this.level7 = level7;
	}

	public String getLevel8() {
		return level8;
	}

	public void setLevel8(String level8) {
		this.level8 = level8;
	}

	public String getLevel9() {
		return level9;
	}

	public void setLevel9(String level9) {
		this.level9 = level9;
	}
}
