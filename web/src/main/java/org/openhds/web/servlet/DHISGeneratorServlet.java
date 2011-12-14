package org.openhds.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openhds.controller.export.DHISController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class DHISGeneratorServlet extends HttpServlet {

	private static final long serialVersionUID = 8261524251386963564L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// set header to force user to download study xml file
		resp.setHeader("Content-Disposition", "attachment;filename=OpenHDSDHIS2.xml");
		resp.setContentType("text/xml");
		
		try {
			if(generateDHISDefinition(resp.getOutputStream())) {
				resp.setStatus(HttpServletResponse.SC_OK);
			} else {
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean generateDHISDefinition(ServletOutputStream outputStream) throws Exception {
		// grab the web application context
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

		DHISController generator = (DHISController) context.getBean("dhisController");

		// generate the DDI
		try {
			outputStream.print(generator.buildDHISDocument().toString());
		} catch (IOException e) {
			return false;
		}	
		return true;
	}
}
