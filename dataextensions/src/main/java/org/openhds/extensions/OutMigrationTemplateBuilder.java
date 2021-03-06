package org.openhds.extensions;

import org.openhds.domain.util.CalendarAdapter;

import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class OutMigrationTemplateBuilder implements ExtensionTemplate {

	JCodeModel jCodeModel;
	boolean templateBuilt = false;
		
	OutMigrationTemplateBuilder(JCodeModel jCodeModel) {
		this.jCodeModel = jCodeModel;
	}
	
	@Override
	public void buildTemplate(JDefinedClass jc) {
		
		JDocComment jDocComment = jc.javadoc();
		jDocComment.add("Generated by JCodeModel");
		
		jc._extends(org.openhds.domain.model.AuditableCollectedEntity.class);
		jc._implements(java.io.Serializable.class);
		
		buildClassAnnotations(jc);	
		buildFieldsAndMethods(jc);
		
		templateBuilt = true;	
	}

	@Override
	public void buildFieldsAndMethods(JDefinedClass jc) {
		
		// serial uuid
		JFieldVar jfSerial = jc.field(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, long.class, "serialVersionUID");
		jfSerial.init(JExpr.lit(6736599408170070468L));
		
		// individual		
		JFieldVar jfIndividual = jc.field(JMod.PRIVATE , org.openhds.domain.model.Individual.class, "individual");
		jfIndividual.annotate(javax.validation.constraints.NotNull.class);
		jfIndividual.annotate(org.openhds.domain.constraint.Searchable.class);
		jfIndividual.annotate(org.openhds.domain.constraint.CheckEntityNotVoided.class);
		JAnnotationUse jfIndividualCascade = jfIndividual.annotate(javax.persistence.ManyToOne.class);
		JAnnotationArrayMember individualArray = jfIndividualCascade.paramArray("cascade");
		individualArray.param(javax.persistence.CascadeType.MERGE);
		individualArray.param(javax.persistence.CascadeType.PERSIST);
		jfIndividualCascade.param("targetEntity", org.openhds.domain.model.Individual.class);
		jfIndividual.annotate(org.openhds.domain.constraint.CheckIndividualNotUnknown.class);
		JAnnotationUse jaIndividualDesc = jfIndividual.annotate(org.openhds.domain.annotations.Description.class);
		jaIndividualDesc.param("description", "Individual who is outmigrating, identified by external id.");
		
		// getter
		JMethod jmgIndividual = jc.method(JMod.PUBLIC, org.openhds.domain.model.Individual.class, "getIndividual");
		JBlock jmgIndividualBlock = jmgIndividual.body();
		jmgIndividualBlock._return(jfIndividual);
		
		// setter
		JMethod jmsIndividual = jc.method(JMod.PUBLIC, void.class, "setIndividual");
		JVar jvarIndividual = jmsIndividual.param(org.openhds.domain.model.Individual.class, "indiv");
		JBlock jmsIndividualBlock = jmsIndividual.body();
		jmsIndividualBlock.assign(jfIndividual, jvarIndividual);
		
		// residency
		JFieldVar jfResidency = jc.field(JMod.PRIVATE , org.openhds.domain.model.Residency.class, "residency");	
		JAnnotationUse jfResidencyCascade = jfResidency.annotate(javax.persistence.OneToOne.class);
		JAnnotationArrayMember residencyArray = jfResidencyCascade.paramArray("cascade");
		residencyArray.param(javax.persistence.CascadeType.MERGE);
		residencyArray.param(javax.persistence.CascadeType.PERSIST);
		jfResidencyCascade.param("targetEntity", org.openhds.domain.model.Residency.class);
		jfResidency.annotate(javax.validation.constraints.NotNull.class);
		JAnnotationUse jaResidencyDesc = jfResidency.annotate(org.openhds.domain.annotations.Description.class);
		jaResidencyDesc.param("description", "The residency the individual is outmigrating to.");
		
		// getter
		JMethod jmgResidency = jc.method(JMod.PUBLIC, org.openhds.domain.model.Residency.class, "getResidency");
		JBlock jmgResidencyBlock = jmgResidency.body();
		jmgResidencyBlock._return(jfResidency);
		
		// setter
		JMethod jmsResidency = jc.method(JMod.PUBLIC, void.class, "setResidency");
		JVar jvarResidency = jmsResidency.param(org.openhds.domain.model.Residency.class, "res");
		JBlock jmsResidencyBlock = jmsResidency.body();
		jmsResidencyBlock.assign(jfResidency, jvarResidency);
		
		// destination
		JFieldVar jfDestination = jc.field(JMod.PRIVATE , java.lang.String.class, "destination");
		jfDestination.annotate(org.openhds.domain.constraint.Searchable.class);
		JAnnotationUse jaDestinationDesc = jfDestination.annotate(org.openhds.domain.annotations.Description.class);
		jaDestinationDesc.param("description", "Destination of the outmigration.");
		
		// getter
		JMethod jmgDestination = jc.method(JMod.PUBLIC, java.lang.String.class, "getDestination");
		JBlock jmgDestinationBlock = jmgDestination.body();
		jmgDestinationBlock._return(jfDestination);
		
		// setter
		JMethod jmsDestination = jc.method(JMod.PUBLIC, void.class, "setDestination");
		JVar jvarDestination = jmsDestination.param(java.lang.String.class, "name");
		JBlock jmsDestinationBlock = jmsDestination.body();
		jmsDestinationBlock.assign(jfDestination, jvarDestination);
		
		// reason
		JFieldVar jfReason = jc.field(JMod.PRIVATE , java.lang.String.class, "reason");
		jfReason.annotate(org.openhds.domain.constraint.Searchable.class);
		JAnnotationUse jaReasonDesc = jfReason.annotate(org.openhds.domain.annotations.Description.class);
		jaReasonDesc.param("description", "Reason for outmigrating.");
		
		// getter
		JMethod jmgReason = jc.method(JMod.PUBLIC, java.lang.String.class, "getReason");
		JBlock jmgReasonBlock = jmgReason.body();
		jmgReasonBlock._return(jfReason);
		
		// setter
		JMethod jmsReason = jc.method(JMod.PUBLIC, void.class, "setReason");
		JVar jvarReason = jmsReason.param(java.lang.String.class, "name");
		JBlock jmsReasonBlock = jmsReason.body();
		jmsReasonBlock.assign(jfReason, jvarReason);
		
		// visit
		JFieldVar jfVisit = jc.field(JMod.PRIVATE , org.openhds.domain.model.Visit.class, "visit");
		jfVisit.annotate(org.openhds.domain.constraint.Searchable.class);
		jfVisit.annotate(javax.validation.constraints.NotNull.class);
		JAnnotationUse jfVisitCascade = jfVisit.annotate(javax.persistence.ManyToOne.class);
		jfVisitCascade.param("cascade", javax.persistence.CascadeType.MERGE);	
		jfVisitCascade.param("targetEntity", org.openhds.domain.model.Visit.class);
		JAnnotationUse jaVisitDesc = jfVisit.annotate(org.openhds.domain.annotations.Description.class);
		jaVisitDesc.param("description", "The visit associated with the outmigration, identified by external id.");
		
		// getter
		JMethod jmgVisit = jc.method(JMod.PUBLIC, org.openhds.domain.model.Visit.class, "getVisit");
		JBlock jmgVisitBlock = jmgVisit.body();
		jmgVisitBlock._return(jfVisit);
		
		// setter
		JMethod jmsVisit = jc.method(JMod.PUBLIC, void.class, "setVisit");
		JVar jvarVisit = jmsVisit.param(org.openhds.domain.model.Visit.class, "vis");
		JBlock jmsVisitBlock = jmsVisit.body();
		jmsVisitBlock.assign(jfVisit, jvarVisit);
		
		// recordedDate
		JFieldVar jfRecordedDate = jc.field(JMod.PRIVATE , java.util.Calendar.class, "recordedDate");
		jfRecordedDate.annotate(javax.validation.constraints.NotNull.class);
		JAnnotationUse jaRecordedDateTemporal = jfRecordedDate.annotate(javax.persistence.Temporal.class);
		jaRecordedDateTemporal.param("value", javax.persistence.TemporalType.DATE);
		JAnnotationUse jaRecordedDatePast = jfRecordedDate.annotate(javax.validation.constraints.Past.class);
		jaRecordedDatePast.param("message", "The date of the migration must be in the past");
		JAnnotationUse jaRecordedDateDesc = jfRecordedDate.annotate(org.openhds.domain.annotations.Description.class);
		jaRecordedDateDesc.param("description", "Recorded date of the inmigration.");
		
		// getter
		JMethod jmgRecordedDate = jc.method(JMod.PUBLIC, java.util.Calendar.class, "getRecordedDate");
		JAnnotationUse jaXmlRecordedDate = jmgRecordedDate.annotate(javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.class);
		jaXmlRecordedDate.param("value", CalendarAdapter.class);
		JBlock jmgRecordedDateBlock = jmgRecordedDate.body();
		jmgRecordedDateBlock._return(jfRecordedDate);
		
		// setter
		JMethod jmsRecordedDate = jc.method(JMod.PUBLIC, void.class, "setRecordedDate");
		JVar jvarRecordedDate = jmsRecordedDate.param(java.util.Calendar.class, "date");
		JBlock jmsRecordedDateBlock = jmsRecordedDate.body();
		jmsRecordedDateBlock.assign(jfRecordedDate, jvarRecordedDate);
	}

	@Override
	public void buildClassAnnotations(JDefinedClass jc) {
		
		// create Description annotation
		JAnnotationUse jad = jc.annotate(org.openhds.domain.annotations.Description.class);
		jad.param("description", "An OutMigration represents a migration out of the study area." +
				"It contains information about the Individual who is out-migrating to a particular" +
				"Residency. It also contains information about the destination, date, and reason the" +
				"Individual is migrating as well as the Visit associated with the migration.");
				
		// create Entity annotation
		jc.annotate(javax.persistence.Entity.class);
				
		JAnnotationUse jat = jc.annotate(javax.persistence.Table.class);
		jat.param("name", "outmigration");
		
		JAnnotationUse jxmlRoot = jc.annotate(javax.xml.bind.annotation.XmlRootElement.class);
		jxmlRoot.param("name", "outmigration");	
	}
}
