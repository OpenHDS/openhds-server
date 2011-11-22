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
INSERT INTO privilege VALUES ('PRIVILEGE12', 'ACESSS_CONFIGURATION')

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
INSERT INTO role_privileges (role_uuid, privilege_uuid) VALUES ('ROLE1', 'PRIVILEGE12')

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
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,workStatus,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES('Unknown Individual','UNK','Unknown',NULL,'UNKNOWN','1', '1', '1900-12-19 15:07:43', NULL, NULL,'User 1','2009-12-19 15:07:43','P','1',NULL,NULL,NULL,false,'UnknownFieldWorker')

-- Location Hierarchy Levels, these must be configured
INSERT INTO locationhierarchylevel(uuid,keyIdentifier,name) VALUES('HierarchyLevel1',1,'LGA')
INSERT INTO locationhierarchylevel(uuid,keyIdentifier,name) VALUES('HierarchyLevel2',2,'Ward')
INSERT INTO locationhierarchylevel(uuid,keyIdentifier,name) VALUES('HierarchyLevel3',3,'Village')

INSERT INTO locationhierarchy(uuid,name,extId,level_uuid,parent_uuid) VALUES('Hierarchy1','MOROGORO', 'MOR', 'HierarchyLevel1','hierarchy_root')
INSERT INTO locationhierarchy(uuid,name,extId,level_uuid,parent_uuid) VALUES('Hierarchy2', 'IFAKARA', 'IFA', 'HierarchyLevel2','Hierarchy1')
INSERT INTO locationhierarchy(uuid,name,extId,level_uuid,parent_uuid) VALUES('Hierarchy3', 'MBINGU', 'MBI', 'HierarchyLevel3','Hierarchy2')
INSERT INTO locationhierarchy(uuid,name,extId,level_uuid,parent_uuid) VALUES('Hierarchy4', 'NJAGI', 'NJA', 'HierarchyLevel3','Hierarchy2')
INSERT INTO locationhierarchy(uuid,name,extId,level_uuid,parent_uuid) VALUES('Hierarchy5', 'CHITA', 'CHI', 'HierarchyLevel3','Hierarchy2')
INSERT INTO locationhierarchy(uuid,name,extId,level_uuid,parent_uuid) VALUES('Hierarchy6', 'IDETE', 'IDE', 'HierarchyLevel3','Hierarchy2')
INSERT INTO locationhierarchy(uuid,name,extId,level_uuid,parent_uuid) VALUES('Hierarchy7', 'MGETA', 'MGE', 'HierarchyLevel3','Hierarchy2')
INSERT INTO locationhierarchy(uuid,name,extId,level_uuid,parent_uuid) VALUES('Hierarchy8', 'AKWA ESUK', 'AKE', 'HierarchyLevel3','Hierarchy2')

INSERT INTO round (uuid,roundNumber,startDate,endDate) VALUES('ROUND 1',1,'2010-06-30 15:07:43','2010-07-31 15:07:43')
INSERT INTO round (uuid,roundNumber,startDate,endDate) VALUES('ROUND 2',2,'2010-08-01 15:07:43','2010-09-30 15:07:43')

INSERT INTO fieldworker (uuid, extid, firstname, lastname, deleted) VALUES('FieldWorker2','FWEM1B', 'Endrew', 'Mwarabu', false)
INSERT INTO fieldworker (uuid, extid, firstname, lastname, deleted) VALUES('FieldWorker3','FWSH1N', 'Shabani', 'Hangahanga', false)
INSERT INTO fieldworker (uuid, extid, firstname, lastname, deleted) VALUES('FieldWorker4','FWSK1K', 'Scolastica', 'Kakweche', false)
INSERT INTO fieldworker (uuid, extid, firstname, lastname, deleted) VALUES('FieldWorker5','FWSS1C', 'Sarah', 'Sumka', false)
INSERT INTO fieldworker (uuid, extid, firstname, lastname, deleted) VALUES('FieldWorker6','FWMA17', 'Michael', 'Abdon', false)
INSERT INTO fieldworker (uuid, extid, firstname, lastname, deleted) VALUES('FieldWorker7','FWMM1V', 'Mariam', 'Manzi', false)
INSERT INTO fieldworker (uuid, extid, firstname, lastname, deleted) VALUES('FieldWorker8','FWSM11', 'Shabani', 'Mtolela', false)

INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,workStatus,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Individual2','NBAS1I','Nancy',NULL,'Bassey','2', '1', '1959-12-19 15:07:43', 'Unknown Individual', 'Unknown Individual','User 1','2009-12-19 15:07:57','P','1',NULL,NULL,NULL,false,'FieldWorker1')
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,workStatus,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES('Individual3','ABAS18','Ayo',NULL, 'Bassey','1', '1', '1959-12-19 15:07:43', 'Unknown Individual', 'Unknown Individual','User 1','2009-12-19 15:08:20','P','1',NULL,NULL,NULL,false,'FieldWorker1')
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,workStatus,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES('Individual1','SBAS17','Sani',NULL,'Bassey','1', '1', '1979-12-19 15:07:43', 'Individual2', 'Individual3','User 1','2009-12-19 15:07:43','P','1',NULL,NULL,NULL,false,'FieldWorker1')
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,workStatus,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES('individual_uuid4','BJOH1H','Bayo',NULL,'John','1', '1', '1979-12-19 15:07:43','Unknown Individual', 'Unknown Individual','User 1','2009-12-19 15:07:57','P','1',NULL,NULL,NULL,false,'FieldWorker1')
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,workStatus,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES('individual_uuid5','MGRA1Z','Mary',NULL,'Grape','2', '1', '1979-12-19 15:07:43', 'Unknown Individual', 'Unknown Individual','User 1','2009-12-19 15:07:57','P','1',NULL,NULL,NULL,false,'FieldWorker1')
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,workStatus,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES('individual_uuid111','RNIN1N','Ronny',NULL,'Niner','1', '1', '1990-10-02 15:07:43', 'Unknown Individual', 'Unknown Individual','User 1','2009-12-19 15:07:57','P','1',NULL,NULL,NULL,false,'FieldWorker1')
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,workStatus,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES('individual_uuid6','JMAN1K','John',NULL,'Man','1', '1', '1979-12-19 15:07:43', 'Unknown Individual', 'Unknown Individual','User 1','2009-12-19 15:07:57','P','1',NULL,NULL,NULL,false,'FieldWorker1')
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,workStatus,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES('individual_uuid7','OJOH1R','Obong',NULL,'John','1', '1', '1979-12-19 15:07:43', 'Unknown Individual', 'Unknown Individual','User 1','2009-12-19 15:07:57','P','1',NULL,NULL,NULL,false,'FieldWorker1')
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,workStatus,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES('individual_uuid8','BJUS1T','Baby',NULL,'Justin','2', '1', '2009-12-19 15:07:43','Unknown Individual','Unknown Individual','User 1','2009-12-19 15:07:57','P','1',NULL,NULL,NULL,false,'FieldWorker1')
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,workStatus,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES('individual_uuid9','BBEN13','Baby',NULL,'Ben','1', '1', '2010-02-02 15:07:43', 'Unknown Individual', 'Unknown Individual','User 1','2010-02-02 15:07:57','P','1',NULL,NULL,NULL,false,'FieldWorker1')
INSERT INTO individual(uuid,extId,firstName,middleName,lastName,gender,workStatus,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,dobAspect,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES('individual_uuid10','BBOL1L','Baby',NULL,'Bola','1', '1', '2010-02-02 15:07:43', 'Unknown Individual', 'Unknown Individual','User 1','2010-02-02 15:07:57','P','1',NULL,NULL,NULL,false,'FieldWorker1')

INSERT INTO pregnancyobservation(uuid,deleted,insertDate,status,voidDate,voidReason,expectedDeliveryDate,recordedDate,collectedBy_uuid,voidBy_uuid,mother_uuid,insertBy_uuid) VALUES ('pregObserv1',false,'2009-11-20 09:07:00','P',NULL,NULL,'2009-02-20 09:07:00','2010-06-20 09:07:00','FieldWorker1',NULL,'individual_uuid5','User 1')

INSERT INTO relationship(uuid,individualA_uuid,individualB_uuid,aIsToB,startDate,endDate, endType,insertBy_uuid,insertDate,voidReason,voidBy_uuid,voidDate,deleted,collectedBy_uuid,status) VALUES ('RELATIONSHIP1', 'Individual1','Individual2', '2', '2000-12-19 15:07:43', NULL, 'NA', 'User 1', '2009-12-19 15:07:43', NULL, NULL, NULL, false, 'FieldWorker1','P')

INSERT INTO socialgroup(uuid, extId, deleted, insertdate, groupName, collectedby_uuid, insertby_uuid, grouphead_uuid,groupType,status) VALUES ('BasseyFamily', 'SG1K', false, '1979-12-19 15:07:43', 'Bassey Family', 'FieldWorker1', 'User 1', 'Individual1','FAM','P')
INSERT INTO socialgroup(uuid, extId, deleted, insertdate, groupName, collectedby_uuid, insertby_uuid, grouphead_uuid,groupType,status) VALUES ('JohnsFamily', 'SG2I', false, '1980-12-19 15:07:43', 'Johns Family', 'FieldWorker1', 'User 1', 'Individual1','FAM','P')

INSERT INTO membership(uuid,deleted, insertdate, startdate, starttype, individual_uuid, socialgroup_uuid, endtype, collectedby_uuid, bIsToA, insertBy_uuid,status)  VALUES ('membership1', false, '1979-12-19 15:07:43', '1979-12-20 15:07:43', 'BIR', 'Individual2', 'BasseyFamily', 'NA', 'FieldWorker1', 'WIFE', 'User 1','P')
INSERT INTO membership(uuid,deleted, insertdate, startdate, starttype, individual_uuid, socialgroup_uuid, endtype, collectedby_uuid, bIsToA, insertBy_uuid,status)  VALUES ('membership3', false, '1981-12-19 15:07:43', '1979-12-20 15:07:43', 'BIR', 'Individual3', 'BasseyFamily', 'NA', 'FieldWorker1', 'BROTHER', 'User 1','P')
INSERT INTO membership(uuid,deleted, insertdate, startdate, starttype, individual_uuid, socialgroup_uuid, endtype, collectedby_uuid, bIsToA, insertBy_uuid,status)  VALUES ('membership2', false, '1980-12-19 15:07:43', '1979-12-20 15:07:43', 'BIR', 'individual_uuid4', 'JohnsFamily', 'NA', 'FieldWorker1', 'BROTHER', 'User 1','P')
INSERT INTO membership(uuid,deleted, insertdate, startdate, starttype, individual_uuid, socialgroup_uuid, endtype, collectedby_uuid, bIsToA, insertBy_uuid,status) VALUES ('membership4', false, '1982-12-19 15:07:43', '1979-12-20 15:07:43', 'BIR', 'individual_uuid5', 'JohnsFamily', 'NA', 'FieldWorker1', 'SISTER', 'User 1','P')
INSERT INTO membership(uuid,deleted, insertdate, startdate, starttype, individual_uuid, socialgroup_uuid, endtype, collectedby_uuid, bIsToA, insertBy_uuid,status) VALUES ('membership5', false, '1982-12-19 15:07:43', '1979-12-20 15:07:43', 'BIR', 'individual_uuid5', 'BasseyFamily', 'NA', 'FieldWorker1', 'SISTER', 'User 1','P')

INSERT INTO location(uuid,extId,locationName,locationLevel_uuid,numberofhouseholds,locationType,insertDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,insertBy_uuid,status) VALUES ('LOCATION1','MBI01','House 1','Hierarchy3',5,'RUR','2000-12-19 15:07:43',NULL,NULL,false,'FieldWorker1','User 1','P')
INSERT INTO location(uuid,extId,locationName,locationLevel_uuid,numberofhouseholds,locationType,insertDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,insertBy_uuid,status) VALUES ('LOCATION2','MBI02','House 2','Hierarchy3',3,'RUR','2000-12-19 15:07:43',NULL,NULL,false,'FieldWorker1','User 1','P')
INSERT INTO location(uuid,extId,locationName,locationLevel_uuid,numberofhouseholds,locationType,insertDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,insertBy_uuid,status) VALUES ('LOCATION3','NJA01','House 3','Hierarchy4',4,'RUR','2000-12-19 15:07:43',NULL,NULL,false,'FieldWorker1','User 1','P')
INSERT INTO location(uuid,extId,locationName,locationLevel_uuid,numberofhouseholds,locationType,insertDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,insertBy_uuid,status) VALUES ('LOCATION4','CHI01','House 4','Hierarchy5',1,'RUR','2000-12-19 15:07:43',NULL,NULL,false,'FieldWorker1','User 1','P')
INSERT INTO location(uuid,extId,locationName,locationLevel_uuid,numberofhouseholds,locationType,insertDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,insertBy_uuid,status) VALUES ('LOCATION5','IDE01','House 5','Hierarchy6',2,'RUR','2000-12-19 15:07:43',NULL,NULL,false,'FieldWorker1','User 1','P')
INSERT INTO location(uuid,extId,locationName,locationLevel_uuid,numberofhouseholds,locationType,insertDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,insertBy_uuid,status) VALUES ('LOCATION6','MGE01','House 6','Hierarchy7',3,'RUR','2000-12-19 15:07:43',NULL,NULL,false,'FieldWorker1','User 1','P')

INSERT INTO visit(uuid,extId,visitDate,status,insertDate,collectedBy_uuid,visitLocation_uuid,deleted,roundNumber,insertBy_uuid) VALUES ('visit_uuid1', 'VLOCMBI11J', '2010-07-15 15:07:43', 'P', '2010-07-20 15:07:43',  'FieldWorker1', 'LOCATION1',false,1,'User 1');
INSERT INTO visit(uuid,extId,visitDate,status,insertDate,collectedBy_uuid,visitLocation_uuid,deleted,roundNumber,insertBy_uuid) VALUES ('visit_uuid2', 'VLOCMBI12H', '2010-07-20 15:07:43', 'P', '2010-09-20 15:07:43',  'FieldWorker1', 'LOCATION1',false,2,'User 1');

INSERT INTO death (uuid, deleted, insertDate, status, voidDate, voidReason, deathCause, deathDate, deathPlace,collectedBy_uuid, insertBy_uuid, voidBy_uuid, individual_uuid, visitDeath_uuid) VALUES ('death1','','2010-06-09','P',NULL,NULL,'Cause','2010-06-01','Place','FieldWorker1','User 1',NULL,'individual_uuid10','visit_uuid1');
INSERT INTO death (uuid, deleted, insertDate, status, voidDate, voidReason, deathCause, deathDate, deathPlace, collectedBy_uuid, insertBy_uuid, voidBy_uuid, individual_uuid, visitDeath_uuid) VALUES ('death2','','2010-06-09','P',NULL,NULL,'Cause','2010-06-01','Place','FieldWorker1','User 1',NULL,'individual_uuid9','visit_uuid1');
INSERT INTO death (uuid, deleted, insertDate, status, voidDate, voidReason, deathCause, deathDate, deathPlace, collectedBy_uuid, insertBy_uuid, voidBy_uuid, individual_uuid, visitDeath_uuid) VALUES ('death3','','2010-06-09','P',NULL,NULL,'Cause','2010-06-01','Place','FieldWorker1','User 1',NULL,'individual_uuid7','visit_uuid1');

INSERT INTO residency(uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('residency_uuid1','LOCATION1','Individual2','1990-11-20 09:07:00','ENU',NULL,NULL,'FieldWorker1',false,'P','1990-11-20 09:07:00','User 1')
INSERT INTO residency(uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('residency_uuid9','LOCATION5','individual_uuid8','2009-12-09 09:07:00','ENU',NULL,NULL,'FieldWorker1',false,'P','1990-11-20 09:07:00','User 1')
INSERT INTO residency(uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid)  VALUES ('residency_uuid7_1','LOCATION1','individual_uuid7','2005-09-15 11:45:00','IMG', NULL, NULL,'FieldWorker1',false,'P','1990-11-20 09:07:00','User 1')
INSERT INTO residency(uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('residency_uuid7','LOCATION4','individual_uuid7','1990-11-20 09:07:00','ENU', '1999-01-18 09:07:00', 'OMG','FieldWorker1',false,'P','1990-11-20 09:07:00','User 1')
INSERT INTO residency(uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('residency_uuid6_1','LOCATION4','individual_uuid6','2005-09-15 11:45:00','IMG', NULL, NULL,'FieldWorker1',false,'P','1990-11-20 09:07:00','User 1')
INSERT INTO residency(uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid)  VALUES ('residency_uuid6','LOCATION4','individual_uuid6','1990-11-20 09:07:00','ENU', '1999-01-18 09:07:00', 'OMG','FieldWorker1',false,'P','1990-11-20 09:07:00','User 1')
INSERT INTO residency(uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('residency_uuid111','LOCATION4','individual_uuid111','1990-11-20 09:07:00','IMG',NULL,NULL,'FieldWorker1',false,'P','1990-11-20 09:07:00','User 1')
INSERT INTO residency(uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('residency_uuid3','LOCATION1','Individual1','1990-11-20 09:07:00','ENU',NULL,NULL,'FieldWorker1',false,'P','1990-11-20 09:07:00','User 1')
INSERT INTO residency(uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('residency_uuid5','LOCATION1','individual_uuid5','1990-11-20 09:07:00','ENU',NULL,NULL,'FieldWorker1',false,'P','1990-11-20 09:07:00','User 1')
INSERT INTO residency(uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('residency_uuid4','LOCATION1','individual_uuid4','1990-11-20 09:07:00','ENU',NULL,NULL,'FieldWorker1',false,'P','1990-11-20 09:07:00','User 1')
INSERT INTO residency(uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('residency_uuid2','LOCATION1','Individual3','1990-11-20 09:07:00','ENU',NULL,NULL,'FieldWorker1',false,'P','1990-11-20 09:07:00','User 1')

INSERT INTO inmigration(uuid,residency_uuid,origin,reason,recordedDate,individual_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,visit_uuid,unknownIndividual, migType) VALUES ('INMIG111', 'residency_uuid111','Tanzania', 'Moved', '2005-09-15 11:45:00', 'individual_uuid111', 'User 1', '1979-12-19 15:07:43', 'P', NULL, NULL, NULL, false, 'FieldWorker1', 'visit_uuid1', true, 'INTERNAL_INMIGRATION')
INSERT INTO inmigration(uuid,residency_uuid,origin,reason,recordedDate,individual_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,visit_uuid,unknownIndividual)  VALUES ('INMIG1', 'residency_uuid6_1','Tanzania', 'Moved', '2005-09-15 11:45:00', 'individual_uuid6', 'User 1', '1979-12-19 15:07:43', 'P', NULL, NULL, NULL, false, 'FieldWorker1', 'visit_uuid1', false)
INSERT INTO inmigration(uuid,residency_uuid,origin,reason,recordedDate,individual_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,visit_uuid, unknownIndividual)  VALUES ('INMIG2', 'residency_uuid7_1', 'Tanzania', 'Moved', '2005-09-15 11:45:00', 'individual_uuid7', 'User 1', '1979-12-19 15:07:43', 'P', NULL, NULL, NULL, false, 'FieldWorker1', 'visit_uuid1', false)

INSERT INTO outmigration(uuid, individual_uuid, residency_uuid, visit_uuid, recordedDate, collectedBy_uuid, deleted, insertDate, insertBy_uuid,status,reason,destination) VALUES ('out_migration2', 'individual_uuid7', 'residency_uuid7', 'visit_uuid1', '1998-01-10 15:30:00', 'FieldWorker1', false, '1998-01-10 15:30:00', 'User 1','P','Moving','Some Name')
INSERT INTO outmigration(uuid, individual_uuid, residency_uuid, visit_uuid, recordedDate, collectedBy_uuid, deleted, insertDate, insertBy_uuid,status,reason,destination) VALUES ('out_migration1', 'individual_uuid6', 'residency_uuid5', 'visit_uuid1', '1998-01-10 15:30:00', 'FieldWorker1', false, '2001-01-10 15:30:00', 'User 1','P','Moving','Some Name')

INSERT INTO note(uuid, description, collectedBy_uuid, status, deleted, insertDate) VALUES ('note0001', 'Name change on INDIV2 from Nancy Drew to Nancy Atkinson because of a Marriage to INDIV1, Brent Atkinson', 'FieldWorker1', 'P', '', '2010-06-09');
INSERT INTO whitelist(uuid, address) VALUES ('address0001', '127.0.0.1');
