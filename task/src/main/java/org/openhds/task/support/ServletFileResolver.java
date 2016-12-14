package org.openhds.task.support;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component
public class ServletFileResolver implements FileResolver, ServletContextAware {

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public File resolveIndividualXmlFile() {
        File generatedXmlFileDir = getGeneratedXmlFolder();
        File individualXmlFile = new File(generatedXmlFileDir, "individual.xml");

        return individualXmlFile;
    }

    protected File getGeneratedXmlFolder() {
        String fullPath = servletContext.getRealPath("/");
        File generatedXmlFileDir = new File(fullPath + File.separator + "generated-xml");
        generatedXmlFileDir.mkdirs();
        return generatedXmlFileDir;
    }

    @Override
    public File resolveLocationXmlFile() {
        File generatedXmlFileDir = getGeneratedXmlFolder();
        File locationXmlFile = new File(generatedXmlFileDir, "location.xml");

        return locationXmlFile;
    }

    @Override
    public File resolveRelationshipXmlFile() {
        File generatedXmlFileDir = getGeneratedXmlFolder();
        File relationshipXmlFile = new File(generatedXmlFileDir, "relationship.xml");

        return relationshipXmlFile;
    }

    @Override
    public File resolvesocialGroupXmlFile() {
        File generatedXmlFileDir = getGeneratedXmlFolder();
        File socialGroupXmlFile = new File(generatedXmlFileDir, "socialgroup.xml");

        return socialGroupXmlFile;
    }

    @Override
    public File resolveVisitXmlFile() {
        File generatedXmlFileDir = getGeneratedXmlFolder();
        File visitXmlFile = new File(generatedXmlFileDir, "visit.xml");

        return visitXmlFile;
    }

	@Override
	public File resolveFormXmlFile() {
		File generatedXmlFileDir = getGeneratedXmlFolder();
		File formXmlFile = new File(generatedXmlFileDir, "forms.xml");
		return formXmlFile;
	}
	   /*resolving respective zip files*/
	   @Override
	   public File resolveIndividualZipFile() {
	       File generatedZipFileDir = getGeneratedXmlFolder();
	       File individualZipFile = new File(generatedZipFileDir, "individual.zip");

	       return individualZipFile;
	   }

	   @Override
	   public File resolveLocationZipFile() {
	       File generatedZipFileDir = getGeneratedXmlFolder();
	       File locationZipFile = new File(generatedZipFileDir, "location.zip");

	       return locationZipFile;
	   }

	   @Override
	   public File resolveRelationshipZipFile() {
	       File generatedZipFileDir = getGeneratedXmlFolder();
	       File relationshipZipFile = new File(generatedZipFileDir, "relationship.zip");

	       return relationshipZipFile;
	   }

	   @Override
	   public File resolvesocialGroupZipFile() {
	       File generatedZipFileDir = getGeneratedXmlFolder();
	       File socialGroupZipFile = new File(generatedZipFileDir, "socialgroup.zip");

	       return socialGroupZipFile;
	   }

	   @Override
	   public File resolveVisitZipFile() {
	       File generatedZipFileDir = getGeneratedXmlFolder();
	       File visitZipFile = new File(generatedZipFileDir, "visit.zip");

	       return visitZipFile;
	   }

	   @Override
	   public File resolveFormZipFile() {
	       File generatedZipFileDir = getGeneratedXmlFolder();
	       File formZipFile = new File(generatedZipFileDir, "forms.zip");
	       return formZipFile;
	   }
}
