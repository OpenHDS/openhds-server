INSERT INTO `fieldworker` (uuid, extid, firstname, lastname, deleted) VALUES ('FieldWorker1','FWEK1D', 'Editha', 'Kaweza', false);

INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 1','BRHA1U','Brian','','Harold','M','1987-09-02','Unknown Individual','Unknown Individual','User 1','2012-02-28','A',NULL,NULL,NULL,false,'UnknownFieldWorker');
INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 2','CHHA12','Chris','','Harold','M','1989-02-21','Unknown Individual','Unknown Individual','User 1','2012-02-28','A',NULL,NULL,NULL,false,'UnknownFieldWorker');
INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 3','SAR01E','Sarah','','Ross','F','1993-01-02','Unknown Individual','Unknown Individual','User 1','2012-02-28','A',NULL,NULL,NULL,false,'UnknownFieldWorker');
INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 4','JEMA1H','Jessica','','Marsh','F','2011-01-03','Indiv 1','Indiv 3','User 1','2012-03-19','A',NULL,NULL,NULL,false,'UnknownFieldWorker');
INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 5','TOMA1M','Tony','','Marsh','M','1988-03-19','Unknown Individual','Unknown Individual','User 1','2012-03-19','A',NULL,NULL,NULL,false,'UnknownFieldWorker');
INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 6','HEMA1L','Henry','','Marsh','M','1998-03-20','Unknown Individual','Unknown Individual','User 1','2012-03-20','A',NULL,NULL,NULL,false,'UnknownFieldWorker');
INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 7','ABHA1C','Abby','','Harold','F','2011-04-15','Unknown Individual','Unknown Individual','User 1','2012-03-22','A',NULL,NULL,NULL,false,'UnknownFieldWorker');
INSERT INTO `individual` (uuid,extId,firstName,middleName,lastName,gender,dob,mother_uuid,father_uuid,insertBy_uuid,insertDate,status,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid) VALUES ('Indiv 8','PEBA1R','Peter','','Bash','M','2009-01-01','Unknown Individual','Unknown Individual','User 1','2012-04-17','A',NULL,NULL,NULL,false,'UnknownFieldWorker');

INSERT INTO `locationhierarchy` VALUES ('hierarchy1','TAN','TAN','hierarchyLevelId3','hierarchy_root');
INSERT INTO `locationhierarchy` VALUES ('hierarchy2','IFA','IFA','hierarchyLevelId2','hierarchy1');
INSERT INTO `locationhierarchy` VALUES ('hierarchy3','MBI','MBI','hierarchyLevelId1','hierarchy2');

INSERT INTO `location` (uuid,extId,locationName,locationLevel_uuid,locationType,insertDate,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,insertBy_uuid,status) VALUES ('Location1','MBI01','Harolds House','hierarchy3','RUR','2012-02-28',NULL,NULL,NULL,false,'FieldWorker1','User 1','A');
INSERT INTO `location` (uuid,extId,locationName,locationLevel_uuid,locationType,insertDate,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,insertBy_uuid,status) VALUES ('Location2','MBI02','Ross House','hierarchy3','RUR','2012-02-28',NULL,NULL,NULL,false,'FieldWorker1','User 1','A');
INSERT INTO `location` (uuid,extId,locationName,locationLevel_uuid,locationType,insertDate,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,insertBy_uuid,status) VALUES ('Location3','MBI03','Marsh House','hierarchy3','RUR','2012-02-28',NULL,NULL,NULL,false,'FieldWorker1','User 1','A');
INSERT INTO `location` (uuid,extId,locationName,locationLevel_uuid,locationType,insertDate,voidDate,voidReason,voidBy_uuid,deleted,collectedBy_uuid,insertBy_uuid,status) VALUES ('Location4','MBI04','Bash House','hierarchy3','RUR','2012-02-28',NULL,NULL,NULL,false,'FieldWorker1','User 1','A');

INSERT INTO `round` (uuid,roundNumber,startDate,endDate) VALUES('ROUND 1',1,'2010-06-30','2010-07-31');
INSERT INTO `visit` (uuid,extId,visitDate,status,insertDate,collectedBy_uuid,visitLocation_uuid,deleted,roundNumber,insertBy_uuid) VALUES ('Visit1','VMBI0111','2012-02-28','P','2012-03-28','FieldWorker1','Location1',false,1,'User 1');

INSERT INTO `residency` (uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('Residency1','Location1','Indiv 2','1989-02-21','BIR',NULL,'NA','FieldWorker1',false,'A','2012-04-17','User 1');
INSERT INTO `residency` (uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('Residency2','Location1','Indiv 4','2011-03-01','BIR',NULL,'NA','FieldWorker1',false,'A','2012-04-17','User 1');
INSERT INTO `residency` (uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('Residency3','Location1','Indiv 3','1993-02-01','IMG',NULL,'NA','FieldWorker1',false,'A','2012-04-17','User 1');
INSERT INTO `residency` (uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('Residency4','Location1','Indiv 7','2012-04-15','BIR',NULL,'NA','FieldWorker1',false,'A','2012-04-17','User 1');
INSERT INTO `residency` (uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('Residency5','Location1','Indiv 8','2009-01-01','BIR',NULL,'NA','FieldWorker1',false,'A','2012-04-17','User 1');
INSERT INTO `residency` (uuid,location_uuid,individual_uuid,startDate,startType,endDate,endType,collectedBy_uuid,deleted,status,insertDate,insertBy_uuid) VALUES ('Residency6','Location1','Indiv 5','1993-02-01','ENU',NULL,'NA','FieldWorker1',false,'A','2012-04-17','User 1');

INSERT INTO `socialgroup` (uuid, extId, deleted, insertdate, groupName, collectedby_uuid, insertby_uuid, grouphead_uuid,groupType,status) VALUES ('SocialGroup1','MBI0101',false,'2012-04-17','Harold','FieldWorker1','User 1','Indiv 1','FAM','P');
INSERT INTO `socialgroup` (uuid, extId, deleted, insertdate, groupName, collectedby_uuid, insertby_uuid, grouphead_uuid,groupType,status) VALUES ('SocialGroup2','MBI0102',false,'2012-04-17','Marsh','FieldWorker1','User 1','Indiv 5','FAM','P');
INSERT INTO `socialgroup` (uuid, extId, deleted, insertdate, groupName, collectedby_uuid, insertby_uuid, grouphead_uuid,groupType,status) VALUES ('SocialGroup3','MBI0201',false,'2012-04-17','Ross','FieldWorker1','User 1','Indiv 3','FAM','P');
INSERT INTO `socialgroup` (uuid, extId, deleted, insertdate, groupName, collectedby_uuid, insertby_uuid, grouphead_uuid,groupType,status) VALUES ('SocialGroup4','MBI0202',false,'2012-04-17','Bash','FieldWorker1','User 1','Indiv 8','FAM','P');

INSERT INTO `whitelist` (uuid, address) VALUES ('LOCALHOST1', '127.0.0.1');
INSERT INTO `whitelist` (uuid, address) VALUES ('LOCALHOST2', '10.0.2.10');
INSERT INTO `whitelist` (uuid, address) VALUES ('LOCALHOST3', '10.0.2.14');

INSERT INTO `inmigration` (uuid,deleted,insertDate,recordedDate,voidDate,voidReason,status,origin,reason,migType,insertBy_uuid,voidBy_uuid,collectedBy_uuid,individual_uuid,residency_uuid,visit_uuid,unknownIndividual) VALUES ('Inmigration1',false,'2012-04-17','2011-01-05',NULL,NULL,'P','origin','reason','INTERNAL_INMIGRATION','User 1',NULL,'FieldWorker1','Indiv 5','Residency6','Visit1',false);
INSERT INTO `outmigration`(uuid,deleted,insertDate,voidDate,voidReason,status,destination,reason,recordedDate,insertBy_uuid,voidBy_uuid,collectedBy_uuid,individual_uuid,residency_uuid,visit_uuid) VALUES ('Outmigration1',false,'2012-04-17',NULL,NULL,'P','destination','reason','1997-05-05','User 1',NULL,'FieldWorker1','Indiv 5','Residency6','Visit1');



