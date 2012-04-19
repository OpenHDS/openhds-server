-- Defined core privileges
INSERT INTO privilege VALUES ('PRIVILEGE1', 'CREATE_ENTITY')
INSERT INTO privilege VALUES ('PRIVILEGE2', 'EDIT_ENTITY')
INSERT INTO privilege VALUES ('PRIVILEGE3', 'DELETE_ENTITY')
INSERT INTO privilege VALUES ('PRIVILEGE4', 'VIEW_ENTITY')
INSERT INTO privilege VALUES ('PRIVILEGE5', 'CREATE_USER')
INSERT INTO privilege VALUES ('PRIVILEGE6', 'DELETE_USER')
INSERT INTO privilege VALUES ('PRIVILEGE7', 'ACCESS_BASELINE')
INSERT INTO privilege VALUES ('PRIVILEGE8', 'ACCESS_UPDATE')
INSERT INTO privilege VALUES ('PRIVILEGE9', 'ACCESS_AMENDMENT_FORMS')
INSERT INTO privilege VALUES ('PRIVILEGE10', 'ACCESS_REPORTS')
INSERT INTO privilege VALUES ('PRIVILEGE11', 'ACESSS_UTILITY_ROUTINES')

-- Defined  core roles
INSERT INTO role (uuid, name, description, deleted) VALUES ('ROLE1', 'ADMINISTRATOR', 'Administrator of OpenHDS', false)
INSERT INTO role (uuid, name, description, deleted) VALUES ('ROLE2', 'DATA CLERK', 'Data Clerk of OpenHDS', false)
INSERT INTO role (uuid, name, description, deleted) VALUES ('ROLE3', 'DATA MANAGER', 'Data Manager of OpenHDS', false)
INSERT INTO role (uuid, name, description, deleted) VALUES ('ROLE4', 'TEST USER', 'Test User of OpenHDS', false)
INSERT INTO role_privileges (role_uuid, privilege_uuid) VALUES ('ROLE1', 'PRIVILEGE1')
INSERT INTO role_privileges (role_uuid, privilege_uuid) VALUES ('ROLE1', 'PRIVILEGE2')
INSERT INTO role_privileges (role_uuid, privilege_uuid) VALUES ('ROLE1', 'PRIVILEGE3')
INSERT INTO role_privileges (role_uuid, privilege_uuid) VALUES ('ROLE1', 'PRIVILEGE4')
INSERT INTO role_privileges (role_uuid, privilege_uuid) VALUES ('ROLE1', 'PRIVILEGE5')
INSERT INTO role_privileges (role_uuid, privilege_uuid) VALUES ('ROLE1', 'PRIVILEGE6')
INSERT INTO role_privileges (role_uuid, privilege_uuid) VALUES ('ROLE1', 'PRIVILEGE7')
INSERT INTO role_privileges (role_uuid, privilege_uuid) VALUES ('ROLE1', 'PRIVILEGE8')
INSERT INTO role_privileges (role_uuid, privilege_uuid) VALUES ('ROLE1', 'PRIVILEGE9')
INSERT INTO role_privileges (role_uuid, privilege_uuid) VALUES ('ROLE1', 'PRIVILEGE10')
INSERT INTO role_privileges (role_uuid, privilege_uuid) VALUES ('ROLE1', 'PRIVILEGE11')

INSERT INTO role_privileges (role_uuid, privilege_uuid) VALUES ('ROLE2', 'PRIVILEGE4')
INSERT INTO role_privileges (role_uuid, privilege_uuid) VALUES ('ROLE4', 'PRIVILEGE1')
INSERT INTO role_privileges (role_uuid, privilege_uuid) VALUES ('ROLE4', 'PRIVILEGE8')

-- Defined Admin user
INSERT INTO user (uuid, firstName, lastName, fullName, description, username, password, lastLoginTime, deleted) VALUES ('User 1', 'FirstName', 'LastName', 'Administrator', 'Administrator User', 'admin', 'test', 0, false)
INSERT INTO user_roles (user_uuid, role_uuid) VALUES ('User 1', 'ROLE1')
INSERT INTO user (uuid, firstName, lastName, fullName, description, username, password, lastLoginTime, deleted) VALUES ('User 2', 'Test', 'Account', 'Test Account', 'Test User Account', 'test', 'test', 0, false)
INSERT INTO user_roles (user_uuid, role_uuid) VALUES ('User 2', 'ROLE4')
INSERT INTO user (uuid, firstName, lastName, fullName, description, username, password, lastLoginTime, deleted) VALUES ('User 3', 'DataClerk', 'Account', 'Test Account', 'Test User Account', 'dataclerk', 'dataclerk', 0, false)
INSERT INTO user_roles (user_uuid, role_uuid) VALUES ('User 3', 'ROLE2')

-- Location Hierarchy root
INSERT INTO locationhierarchy(uuid,name,extId,level_uuid,parent_uuid) VALUES('hierarchy_root','', 'HIERARCHY_ROOT', NULL,NULL)

-- Field Worker
INSERT INTO fieldworker (uuid, extid, firstname, lastname, deleted) VALUES ('UnknownFieldWorker','UNK', 'Unknown', 'FieldWorker', false)
INSERT INTO fieldworker (uuid, extid, firstname, lastname, deleted) VALUES ('FieldWorker1','FWEK1D', 'Editha', 'Kaweza', false)

-- Unknown Individual: This should always be pre-populated
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES('Unknown Individual','UNK','Unknown',NULL,'UNKNOWN','M', '1900-12-19 15:07:43', NULL, NULL,'User 1','2009-12-19 15:07:43','P','1',NULL,NULL,NULL,false,'UnknownFieldWorker')
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Individual2','NBAS1I','Nancy',NULL,'Bassey','F', '1959-12-19 15:07:43', 'Unknown Individual', 'Unknown Individual','User 1','2009-12-19 15:07:57','P','1',NULL,NULL,NULL,false,'FieldWorker1')
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Individual3','BJOH1J','Bob',NULL,'Johnson','M', '1965-12-19 15:07:43', 'Unknown Individual', 'Unknown Individual','User 1','2009-12-19 15:07:57','P','1',NULL,NULL,NULL,false,'FieldWorker1')
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Individual4','CBLA1H','Cristen',NULL,'Blake','F', '1960-12-19 15:07:43', 'Unknown Individual', 'Unknown Individual','User 1','2009-12-19 15:07:57','P','1',NULL,NULL,NULL,false,'FieldWorker1')
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Individual5','BHAR1K','Brian',NULL,'Blake','M', '1965-12-19 15:07:43', 'Unknown Individual', 'Unknown Individual','User 1','2009-12-19 15:07:57','P','1',NULL,NULL,NULL,false,'FieldWorker1')
INSERT INTO individual VALUES ('Indiv 1','\0','2012-02-28',NULL,NULL,'A','','1987-09-02','1','BRIHA001','Brian','M','Hartsock','A','User 1',NULL,'UnknownFieldWorker','Unknown Individual','Unknown Individual')
INSERT INTO individual VALUES ('Indiv 2','\0','2012-02-28',NULL,NULL,'A','','1989-02-21','1','CHRHA001','Chris','M','Hartsock','M','User 1',NULL,'UnknownFieldWorker','Unknown Individual','Unknown Individual')
INSERT INTO individual VALUES ('Indiv 3','\0','2012-02-28',NULL,NULL,'A','','1993-01-02','1','SARRO001','Sarah','F','Ross','B','User 1',NULL,'UnknownFieldWorker','Unknown Individual','Unknown Individual')
INSERT INTO individual VALUES ('Indiv 4','\0','2012-03-19',NULL,NULL,'P','','2011-01-03','1','JESMA001','Jessica','F','Marsh','A','User 1',NULL,'UnknownFieldWorker','Indiv 1','Indiv 3')
INSERT INTO individual VALUES ('Indiv 5','\0','2012-03-19',NULL,NULL,'P','','1988-03-19','1','TONMA001','Tony','M','Marsh','A','User 1',NULL,'UnknownFieldWorker','Unknown Individual','Unknown Individual')
INSERT INTO individual VALUES ('Indiv 6','\0','2012-03-20',NULL,NULL,'A','','1998-03-20','1','HENMA001','Henry','M','Marsh','A','User 1',NULL,'UnknownFieldWorker','Unknown Individual','Unknown Individual')
INSERT INTO individual VALUES ('Indiv 7','\0','2012-03-22',NULL,NULL,'P','','2012-04-15','1','ABBHA001','Abby','F','Hartsock','B','User 1',NULL,'UnknownFieldWorker','Unknown Individual','Unknown Individual')
INSERT INTO individual VALUES ('Indiv 8','\0','2012-04-17',NULL,NULL,'P','','2009-01-01','1','PETBA001','Peter','M','Bash','','User 1',NULL,'UnknownFieldWorker','Unknown Individual','Unknown Individual')

-- Location Hierarchy Levels, these must be configured
INSERT INTO locationhierarchylevel(uuid,keyIdentifier,name) VALUES('HierarchyLevel1',1,'LGA')
INSERT INTO locationhierarchylevel(uuid,keyIdentifier,name) VALUES('HierarchyLevel2',2,'Ward')
INSERT INTO locationhierarchylevel(uuid,keyIdentifier,name) VALUES('HierarchyLevel3',3,'Village')

INSERT INTO round (uuid,roundNumber,startDate,endDate) VALUES('ROUND 1',1,'2010-06-30 15:07:43','2010-07-31 15:07:43')
INSERT INTO location(uuid,extId,locationName,locationLevel_uuid,locationType,insertDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,insertBy_uuid,status) VALUES ('LOCATION1','NJA001','House 3','hierarchy_root','RUR','2000-12-19 15:07:43',NULL,NULL,false,'FieldWorker1','User 1','P')
INSERT INTO visit(uuid,extId,visitDate,status,insertDate,collectedBy_uuid,visitLocation_uuid,deleted,roundNumber,insertBy_uuid) VALUES ('visit_uuid1', 'VLOCMBI11J', '2010-07-15 15:07:43', 'P', '2010-07-20 15:07:43',  'FieldWorker1', 'LOCATION1',false,1,'User 1');
INSERT INTO death (uuid, deleted, insertDate, status, voidDate, voidReason, deathCause, deathDate, deathPlace,collectedBy_uuid, insertBy_uuid, voidBy_uuid, individual_uuid, visitDeath_uuid) VALUES ('death1','','2010-06-09','P',NULL,NULL,'Cause','2010-06-01','Place','FieldWorker1','User 1',NULL,'Individual4','visit_uuid1');
INSERT INTO socialgroup(uuid, extId, deleted, insertdate, groupName, collectedby_uuid, insertby_uuid, grouphead_uuid,groupType,status) VALUES ('BasseyFamily', 'MBI1', false, '1979-12-19 15:07:43', 'Bassey Family', 'FieldWorker1', 'User 1', 'Individual2','FAM','P')

INSERT INTO residency(uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('residency_uuid1','LOCATION1','Individual5','1965-12-19 09:07:00','ENU',NULL,NULL,'FieldWorker1',false,'P','1990-11-20 09:07:00','User 1')
INSERT INTO residency VALUES ('Residency 1','\0','2012-04-17',NULL,NULL,'P','',NULL,'NA','1989-02-21','BIR','User 1',NULL,'UnknownFieldWorker','Indiv 2','LOCATION1')
INSERT INTO residency VALUES ('Residency 2','\0','2012-04-17',NULL,NULL,'P','',NULL,'NA','2011-03-01','BIR','User 1',NULL,'UnknownFieldWorker','Indiv 4','LOCATION1')
INSERT INTO residency VALUES ('Residency 3','\0','2012-04-17',NULL,NULL,'P','',NULL,'NA','1993-02-01','IMG','User 1',NULL,'UnknownFieldWorker','Indiv 3','LOCATION1')
INSERT INTO residency VALUES ('Residency 4','\0','2012-04-17',NULL,NULL,'P','',NULL,'NA','2012-04-15','BIR','User 1',NULL,'UnknownFieldWorker','Indiv 7','LOCATION1')
INSERT INTO residency VALUES ('Residency 5','\0','2012-04-17',NULL,NULL,'P','',NULL,'NA','2009-01-01','BIR','User 1',NULL,'UnknownFieldWorker','Indiv 8','LOCATION1')
INSERT INTO residency VALUES ('Residency 6','\0','2012-04-17',NULL,NULL,'P','',NULL,'NA','1993-02-01','ENU','User 1',NULL,'UnknownFieldWorker','Indiv 5','LOCATION1')

INSERT INTO `inmigration` VALUES ('Inmigration 1','\0','2012-04-17',NULL,NULL,'P','','INTERNAL_INMIGRATION','origin','reason','1993-02-01','\0','User 1',NULL,'UnknownFieldWorker','Indiv 5','Residency 6','visit_uuid1');
INSERT INTO `outmigration` VALUES ('Outmigration 1','\0','2012-04-17',NULL,NULL,'P','','name','reasone','1997-05-05','User 1',NULL,'UnknownFieldWorker','Indiv 5','Residency 6','visit_uuid1');
INSERT INTO `pregnancyoutcome` VALUES ('Pregnancy Outcome 1', b, '2012-04-17', '', '', 'P', '', 1, 1, '2009-01-01', 'User 1', '', 'UnknownFieldWorker', 'Unknown Individual', 'Indiv 3', 'visit_uuid1')
