package org.openhds.ddi.builder;

import java.util.Date;
import edu.umich.icpsr.ddi.CitationType;
import edu.umich.icpsr.ddi.CodeBookDocument.CodeBook;
import edu.umich.icpsr.ddi.AuthEntyType;
import edu.umich.icpsr.ddi.ContactType;
import edu.umich.icpsr.ddi.DistStmtType;
import edu.umich.icpsr.ddi.DocDscrType;
import edu.umich.icpsr.ddi.NotesType;
import edu.umich.icpsr.ddi.ProdDateType;
import edu.umich.icpsr.ddi.ProdStmtType;
import edu.umich.icpsr.ddi.RspStmtType;
import edu.umich.icpsr.ddi.SoftwareType;
import edu.umich.icpsr.ddi.TitlStmtType;
import edu.umich.icpsr.ddi.TitlType;

/**
 * 
 * Builds the Document Description section of the DDI Schema.
 * 
 * @author Brian
 */

public class DocumentDescriptionBuilder {
	
	public DocumentDescriptionBuilder() { }
	
	public void buildDocumentDescription(CodeBook codebook, String studyName, 
			String studyId, String date, String authoringEntity, String contactName, 
			String email, String projectVersion, String notes) {
		
		DocDscrType docDscrType = codebook.addNewDocDscr();
		docDscrType.setID("DOC-" + studyId);
		
		CitationType citation = docDscrType.addNewCitation();
		TitlStmtType titleStmt = citation.addNewTitlStmt();
		TitlType title = titleStmt.addNewTitl();
		title.newCursor().setTextValue(studyName);
		titleStmt.setTitl(title);
		citation.setTitlStmt(titleStmt);
		
		RspStmtType rspStmt = citation.addNewRspStmt();
		AuthEntyType authEnty = rspStmt.addNewAuthEnty();
		authEnty.newCursor().setTextValue(authoringEntity);
		
		ProdStmtType prodStmt = citation.addNewProdStmt();
		ProdDateType prodDate = prodStmt.addNewProdDate();
		prodDate.setDate(new Date().toString());
		SoftwareType software = prodStmt.addNewSoftware();
		software.setDate(new Date().toString());
		software.setID("WriteDBToDDIController");
		software.setVersion(projectVersion);
		citation.setProdStmt(prodStmt);
		
		DistStmtType distStmt = citation.addNewDistStmt();
		ContactType contact = distStmt.addNewContact();
		contact.setEmail(email);
		contact.setAffiliation(authoringEntity);
		contact.newCursor().setTextValue(contactName);
		
		docDscrType.setCitation(citation);
		
		NotesType notesType = docDscrType.addNewNotes();
		notesType.newCursor().setTextValue(notes);
	}
}
