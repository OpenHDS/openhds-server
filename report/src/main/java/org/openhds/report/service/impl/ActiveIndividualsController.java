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
import org.openhds.report.beans.ActiveIndividualsBean;
import org.openhds.report.service.ActiveIndividualsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ActiveIndividualsController implements ActiveIndividualsService{

	GenericDao genericDao;
	SitePropertiesService properties;
	CalendarUtil calendarUtil;
	BasicDataSource dataSource;
	String filename;
	
	@Autowired
	public ActiveIndividualsController(GenericDao genericDao, SitePropertiesService properties,
			CalendarUtil calendarUtil, BasicDataSource dataSource){

		this.genericDao = genericDao;
		this.properties = properties;
		this.calendarUtil = calendarUtil;
		this.dataSource = dataSource;
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		this.filename = "active-individuals" + timeStamp + ".pdf";
	}
	
	@RequestMapping(value = "/active-individuals.report")
	public ModelAndView getActiveIndividuals(HttpServletRequest request, HttpServletResponse response) {
				
		List<ActiveIndividualsBean> list = new ArrayList<ActiveIndividualsBean>();
		ResultSet rs = null;
		Connection con = null;
		try{
			Class.forName(dataSource.getDriverClassName());
			con = dataSource.getConnection();
			
			String query = "select i.extId ID, i.firstName, i.middleName, i.lastName, i.gender, i.dob, f.extId " +
					"father, m.extId mother, r.startType, r.startDate, l.extId Location, l.locationName FamilyName, " +
					"h.extId Subvillage, h.name SubVillageName, h1.extId VillageCode, h1.name VillageName, s.extId SocialGroup, sg.extId Head, sg.dob Head_dob " +
					"FROM individual i, individual f, individual m, residency r, location l, locationhierarchy h, locationhierarchy h1, " +
					"socialgroup s, membership me, individual sg " +

					"where i.father_uuid = f.uuid and " + 
					"i.mother_uuid = m.uuid " + 
					"and i.uuid=r.individual_uuid " + 

					"and  i.uuid = me.individual_uuid " + 
					"and r.location_uuid=l.uuid " +
					"and l.locationLevel_uuid=h.uuid " +
					"and h.parent_uuid = h1.uuid " +

					"and me.socialGroup_uuid = s.uuid " +
					"and sg.uuid = s.groupHead_uuid " +
					"and r.endDate is null and me.endDate is null and i.deleted = 0 " +
					"order by 11;";
			
		    Statement st = con.createStatement();
		    rs = st.executeQuery(query);
		    
		    while(rs.next()){
				String InvididualId = rs.getString("ID");
				String LocationId = rs.getString("Location");
				Date startDate = rs.getDate("startDate");		    	
				ActiveIndividualsBean tc = new ActiveIndividualsBean(InvididualId, LocationId, startDate);
		    	list.add(tc);
		    }
		    
		    rs.close();
			con.close();
		}
		catch(Exception e){}
		finally{
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {}
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {}
		}		
		JRDataSource ds = new JRBeanCollectionDataSource(list, false);
		
		Map<String, Object> params = new HashMap<>();		
		params.put("dataSource", ds);
		
		ModelAndView mv = new ModelAndView("activeReport", params);		
		return mv;
	}
}
