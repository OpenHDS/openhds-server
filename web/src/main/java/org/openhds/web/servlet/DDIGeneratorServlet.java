package org.openhds.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openhds.web.controller.DDIController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * A servlet that will serve up a dynamic DDI 2.1 document for OpenHDS. 
 * Navigating to this URL will prompt the user to download the study definition.
 *
 */
public class DDIGeneratorServlet extends HttpServlet {
	
	private static final long serialVersionUID = -7431786922240042765L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// set header to force user to download study xml file
		resp.setHeader("Content-Disposition", "attachment;filename=OpenHDSDDI.xml");
		resp.setContentType("text/xml");
		
		try {
			if(generateStudyDefinition(resp.getOutputStream())) {
				resp.setStatus(HttpServletResponse.SC_OK);
			} else {
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean generateStudyDefinition(ServletOutputStream outputStream) throws Exception {
		// grab the web application context
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

		DDIController generator = (DDIController) context.getBean("ddiController");

		// generate the DDI
		try {
			outputStream.print(generator.buildDDIDocument().toString());
		} catch (IOException e) {
			return false;
		}	
		return true;
	}

	

}
