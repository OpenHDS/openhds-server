INSERT INTO `fieldworker` (uuid, extid, firstname, lastname, deleted) VALUES ('FieldWorker1','FWEK1D', 'Editha', 'Kaweza', false);

INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 1','MBI000001001','Brian','','Harold','M','1987-09-02','Unknown Individual','Unknown Individual','User 1','2012-02-28','A',NULL,NULL,NULL,false,'UnknownFieldWorker');
INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 2','MBI000001002','Chris','','Harold','M','1989-02-21','Unknown Individual','Unknown Individual','User 1','2012-02-28','A',NULL,NULL,NULL,false,'UnknownFieldWorker');
INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 3','MBI000001003','Sarah','','Ross','F','1993-01-02','Unknown Individual','Unknown Individual','User 1','2012-02-28','A',NULL,NULL,NULL,false,'UnknownFieldWorker');
INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 4','MBI000001004','Jessica','','Marsh','F','2011-01-03','Indiv 1','Indiv 3','User 1','2012-03-19','A',NULL,NULL,NULL,false,'UnknownFieldWorker');
INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 5','MBI000001005','Tony','','Marsh','M','1988-03-19','Unknown Individual','Unknown Individual','User 1','2012-03-19','A',NULL,NULL,NULL,false,'UnknownFieldWorker');
INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 6','MBI000001006','Henry','','Marsh','M','1998-03-20','Unknown Individual','Unknown Individual','User 1','2012-03-20','A',NULL,NULL,NULL,false,'UnknownFieldWorker');
INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 7','MBI000001007','Abby','','Harold','F','2011-04-15','Unknown Individual','Unknown Individual','User 1','2012-03-22','A',NULL,NULL,NULL,false,'UnknownFieldWorker');
INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 8','MBI000001008','Peter','','Bash','M','2009-01-01','Unknown Individual','Unknown Individual','User 1','2012-04-17','A',NULL,NULL,NULL,false,'UnknownFieldWorker');

INSERT INTO `locationhierarchy` VALUES ('hierarchy1','TAN','TAN','hierarchyLevelId1','hierarchy_root');
INSERT INTO `locationhierarchy` VALUES ('hierarchy2','IFA','IFA','hierarchyLevelId2','hierarchy1');
INSERT INTO `locationhierarchy` VALUES ('hierarchy3','IFB','IFB','hierarchyLevelId3','hierarchy2');
INSERT INTO `locationhierarchy` VALUES ('hierarchy4','IFC','IFC','hierarchyLevelId4','hierarchy3');
INSERT INTO `locationhierarchy` VALUES ('hierarchy5','MBI','MBI','hierarchyLevelId5','hierarchy4');

INSERT INTO `location` (uuid,extId,locationName,locationLevel_uuid,locationType,insertDate,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,insertBy_uuid,status) VALUES ('Location1','MBI000001','Harolds House','hierarchy5','RUR','2012-02-28',NULL,NULL,NULL,false,'FieldWorker1','User 1','A');
INSERT INTO `location` (uuid,extId,locationName,locationLevel_uuid,locationType,insertDate,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,insertBy_uuid,status) VALUES ('Location2','MBI02','Ross House','hierarchy5','RUR','2012-02-28',NULL,NULL,NULL,false,'FieldWorker1','User 1','A');
INSERT INTO `location` (uuid,extId,locationName,locationLevel_uuid,locationType,insertDate,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,insertBy_uuid,status) VALUES ('Location3','MBI03','Marsh House','hierarchy5','RUR','2012-02-28',NULL,NULL,NULL,false,'FieldWorker1','User 1','A');
INSERT INTO `location` (uuid,extId,locationName,locationLevel_uuid,locationType,insertDate,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,insertBy_uuid,status) VALUES ('Location4','MBI04','Bash House','hierarchy5','RUR','2012-02-28',NULL,NULL,NULL,false,'FieldWorker1','User 1','A');

INSERT INTO `round` (uuid,roundNumber,startDate,endDate,remarks) VALUES('ROUND 1',1,'2010-06-30','2010-07-31', 'Test Round');
INSERT INTO `visit` (uuid,extId,visitDate,status,insertDate,collectedBy_uuid,visitLocation_uuid,deleted,roundNumber,insertBy_uuid) VALUES ('Visit1','VMBI0111','2012-02-28','P','2012-03-28','FieldWorker1','Location1',false,1,'User 1');

INSERT INTO `residency` (uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('Residency1','Location1','Indiv 2','1989-02-21','BIR',NULL,'NA','FieldWorker1',false,'A','2012-04-17','User 1');
INSERT INTO `residency` (uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('Residency2','Location1','Indiv 4','2011-03-01','BIR',NULL,'NA','FieldWorker1',false,'A','2012-04-17','User 1');
INSERT INTO `residency` (uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('Residency3','Location1','Indiv 3','1993-02-01','IMG',NULL,'NA','FieldWorker1',false,'A','2012-04-17','User 1');
INSERT INTO `residency` (uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('Residency4','Location1','Indiv 7','2012-04-15','BIR',NULL,'NA','FieldWorker1',false,'A','2012-04-17','User 1');
INSERT INTO `residency` (uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('Residency5','Location1','Indiv 8','2009-01-01','BIR',NULL,'NA','FieldWorker1',false,'A','2012-04-17','User 1');
INSERT INTO `residency` (uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('Residency6','Location1','Indiv 5','1993-02-01','ENU',NULL,'NA','FieldWorker1',false,'A','2012-04-17','User 1');
INSERT INTO `residency` (uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('Residency7','Location1','Indiv 1','2009-01-01','BIR',NULL,'NA','FieldWorker1',false,'A','2012-04-17','User 1');
INSERT INTO `residency` (uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('Residency8','Location1','Indiv 6','1993-02-01','ENU',NULL,'NA','FieldWorker1',false,'A','2012-04-17','User 1');


INSERT INTO `socialgroup` (uuid, extId, deleted, insertdate, groupName, collectedby_uuid, insertby_uuid, grouphead_uuid,groupType,status) VALUES ('SocialGroup1','MBI0101',false,'2012-04-17','Harold','FieldWorker1','User 1','Indiv 1','FAM','P');
INSERT INTO `socialgroup` (uuid, extId, deleted, insertdate, groupName, collectedby_uuid, insertby_uuid, grouphead_uuid,groupType,status) VALUES ('SocialGroup2','MBI0102',false,'2012-04-17','Marsh','FieldWorker1','User 1','Indiv 5','FAM','P');
INSERT INTO `socialgroup` (uuid, extId, deleted, insertdate, groupName, collectedby_uuid, insertby_uuid, grouphead_uuid,groupType,status) VALUES ('SocialGroup3','MBI0201',false,'2012-04-17','Ross','FieldWorker1','User 1','Indiv 3','FAM','P');
INSERT INTO `socialgroup` (uuid, extId, deleted, insertdate, groupName, collectedby_uuid, insertby_uuid, grouphead_uuid,groupType,status) VALUES ('SocialGroup4','MBI0202',false,'2012-04-17','Bash','FieldWorker1','User 1','Indiv 8','FAM','P');

INSERT INTO `whitelist` (uuid, address) VALUES ('LOCALHOST1', '127.0.0.1');
INSERT INTO `whitelist` (uuid, address) VALUES ('LOCALHOST2', '10.0.2.10');
INSERT INTO `whitelist` (uuid, address) VALUES ('LOCALHOST3', '10.0.2.14');
INSERT INTO `whitelist` (uuid, address) VALUES ('LOCALHOST4', '141.114.156.167');

INSERT INTO `inmigration` (uuid,deleted,insertDate,recordedDate,voidDate,voidReason,status,origin,reason,migType,insertBy_uuid,voidBy_uuid,collectedBy_uuid,individual_uuid,residency_uuid,visit_uuid,unknownIndividual) VALUES ('Inmigration1',false,'2012-04-17','2011-01-05',NULL,NULL,'P','origin','reason','INTERNAL_INMIGRATION','User 1',NULL,'FieldWorker1','Indiv 5','Residency6','Visit1',false);
INSERT INTO `outmigration`(uuid,deleted,insertDate,voidDate,voidReason,status,destination,reason,recordedDate,insertBy_uuid,voidBy_uuid,collectedBy_uuid,individual_uuid,residency_uuid,visit_uuid) VALUES ('Outmigration1',false,'2012-04-17',NULL,NULL,'P','destination','reason','1997-05-05','User 1',NULL,'FieldWorker1','Indiv 5','Residency6','Visit1');


