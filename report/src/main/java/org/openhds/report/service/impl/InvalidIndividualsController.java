package org.openhds.report.service.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.openhds.dao.service.GenericDao;
import org.openhds.domain.service.SitePropertiesService;
import org.openhds.domain.util.CalendarUtil;
import org.openhds.report.beans.InvalidIndividualsBean;
import org.openhds.report.service.InvalidIndividualsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InvalidIndividualsController implements InvalidIndividualsService{

	GenericDao genericDao;
	SitePropertiesService properties;
	CalendarUtil calendarUtil;
	BasicDataSource dataSource;
	String filename;
	
	@Autowired
	public InvalidIndividualsController(GenericDao genericDao, SitePropertiesService properties,
			CalendarUtil calendarUtil, BasicDataSource dataSource){

		this.genericDao = genericDao;
		this.properties = properties;
		this.calendarUtil = calendarUtil;
		this.dataSource = dataSource;
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		this.filename = "individuals-without-memberships" + timeStamp + ".pdf";
	}
	
	@RequestMapping(value = "/invalid.report")
	public ModelAndView getInvalidIndividuals(HttpServletRequest request, HttpServletResponse response) {
				
		List<InvalidIndividualsBean> list = new ArrayList<InvalidIndividualsBean>();
		ResultSet rs = null;
		Connection con = null;
		try{
			Class.forName(dataSource.getDriverClassName());
			con = dataSource.getConnection();
			//Connection con = DriverManager.getConnection("jdbc:mysql://data-management.local/openhds?createDatabaseIfNotExist=true&amp;useEncoding=true&amp;characterEncoding=UTF-8", "data", "data");
			
		    String query = "SELECT i.extId AS InvididualId, l.extId AS LocationId, r.startDate FROM individual i, location l, residency r " +
		    		"WHERE r.individual_uuid=i.uuid AND " +
		    		"l.uuid=r.location_uuid AND i.uuid NOT IN "+ 
		    		"(SELECT individual_uuid FROM membership where endDate is null and deleted=0) AND r.endDate IS null";
		    Statement st = con.createStatement();
		    rs = st.executeQuery(query);

		    while(rs.next()){
				String InvididualId = rs.getString("InvididualId");
				String LocationId = rs.getString("LocationId");
				Date startDate = rs.getDate("startDate");		    	
				InvalidIndividualsBean tc = new InvalidIndividualsBean(InvididualId, LocationId, startDate);
		    	list.add(tc);
		    }
		    
		    rs.close();
			con.close();
		}
		catch(Exception e){
//			System.err.println(e.getMessage());
		}
		finally{
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
//					e.printStackTrace();
				}
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
//					e.printStackTrace();
				}
		}		
		JRDataSource ds = new JRBeanCollectionDataSource(list, false);
		
		Map<String, Object> params = new HashMap<>();		
		params.put("dataSource", ds);
		
		ModelAndView mv = new ModelAndView("invalidReport", params);		
		return mv;
	}
}
