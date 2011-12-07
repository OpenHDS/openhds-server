package org.openhds.ddi.builder;

import edu.umich.icpsr.ddi.AuthEntyType;
import edu.umich.icpsr.ddi.CitationType;
import edu.umich.icpsr.ddi.CodeBookDocument.CodeBook;
import edu.umich.icpsr.ddi.TimePrdType.Event;
import edu.umich.icpsr.ddi.ContactType;
import edu.umich.icpsr.ddi.DataAccsType;
import edu.umich.icpsr.ddi.DistStmtType;
import edu.umich.icpsr.ddi.FileQntyType;
import edu.umich.icpsr.ddi.NationType;
import edu.umich.icpsr.ddi.NotesType;
import edu.umich.icpsr.ddi.RspStmtType;
import edu.umich.icpsr.ddi.SetAvailType;
import edu.umich.icpsr.ddi.StdyDscrType;
import edu.umich.icpsr.ddi.StdyInfoType;
import edu.umich.icpsr.ddi.SumDscrType;
import edu.umich.icpsr.ddi.TimePrdType;
import edu.umich.icpsr.ddi.TitlStmtType;
import edu.umich.icpsr.ddi.TitlType;

/**
 * 
 * Builds the Study Description section of the DDI Schema.
 * 
 * @author Brian
 */

public class StudyDescriptionBuilder {

	public StudyDescriptionBuilder() { }
	
	public void buildStudyDescription(CodeBook codebook, String studyName,
			String studyId, String affiliation, String author, String email,
			String nationAbrv, String startDate, String endDate, String files, 
			String notes) {
		
		StdyDscrType studyDesc = codebook.addNewStdyDscr();
		studyDesc.setID("STUDY-" + studyId);
		
		CitationType citation = studyDesc.addNewCitation();
		TitlStmtType titleStmt = citation.addNewTitlStmt();
		TitlType title = titleStmt.addNewTitl();
		title.newCursor().setTextValue(studyName);
		titleStmt.setTitl(title);
		
		RspStmtType rspStmt = citation.addNewRspStmt();
		AuthEntyType authEnty = rspStmt.addNewAuthEnty();
		authEnty.setAffiliation(affiliation);
		authEnty.newCursor().setTextValue(author);
		
		DistStmtType distStmt = citation.addNewDistStmt();
		ContactType contact = distStmt.addNewContact();
		contact.setEmail(email);
		contact.setAffiliation(affiliation);
		contact.newCursor().setTextValue(author);
		
		StdyInfoType studyInfo = studyDesc.addNewStdyInfo();
		SumDscrType sumDscr = studyInfo.addNewSumDscr();
		TimePrdType timePrdStart = sumDscr.addNewTimePrd();
		timePrdStart.setEvent(Event.START);
		timePrdStart.newCursor().setTextValue(startDate);
		
		TimePrdType timePrdEnd = sumDscr.addNewTimePrd();
		timePrdEnd.setEvent(Event.END);
		timePrdEnd.newCursor().setTextValue(endDate);
		
		NationType nation = sumDscr.addNewNation();
		nation.setAbbr(nationAbrv);
		
		DataAccsType data = studyDesc.addNewDataAccs();
		SetAvailType avail = data.addNewSetAvail();
		FileQntyType fileQnty = avail.addNewFileQnty();
		fileQnty.newCursor().setTextValue(files);
		
		NotesType notesType = studyDesc.addNewNotes();
		notesType.newCursor().setTextValue(notes);
	}
}
