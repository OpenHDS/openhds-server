INSERT INTO `fieldworker` (uuid, extid, firstname, lastname, deleted) VALUES ('FieldWorker1','FWEK1D', 'Editha', 'Kaweza', false);

INSERT INTO `individual` VALUES ('Indiv1',false,'2012-02-28',NULL,NULL,'A','','1987-09-02','1','BRHA1U','Brian','M','Harold','A','User 1',NULL,'FieldWorker1','Unknown Individual','Unknown Individual');
INSERT INTO `individual` VALUES ('Indiv2',false,'2012-02-28',NULL,NULL,'A','','1989-02-21','1','CHHA12','Chris','M','Harold','M','User 1',NULL,'FieldWorker1','Unknown Individual','Unknown Individual');
INSERT INTO `individual` VALUES ('Indiv3',false,'2012-02-28',NULL,NULL,'A','','1993-01-02','1','SARO1E','Sarah','F','Ross','B','User 1',NULL,'FieldWorker1','Unknown Individual','Unknown Individual');
INSERT INTO `individual` VALUES ('Indiv4',false,'2012-03-19',NULL,NULL,'P','','2011-01-03','1','JEMA1H','Jessica','F','Marsh','A','User 1',NULL,'FieldWorker1','Indiv1','Indiv3');
INSERT INTO `individual` VALUES ('Indiv5',false,'2012-03-19',NULL,NULL,'P','','1988-03-19','1','TOMA1M','Tony','M','Marsh','A','User 1',NULL,'FieldWorker1','Unknown Individual','Unknown Individual');
INSERT INTO `individual` VALUES ('Indiv6',false,'2012-03-20',NULL,NULL,'A','','1998-03-20','1','HEMA1L','Henry','M','Marsh','A','User 1',NULL,'FieldWorker1','Unknown Individual','Unknown Individual');
INSERT INTO `individual` VALUES ('Indiv7',false,'2012-03-22',NULL,NULL,'P','','2012-04-15','1','ABHA1C','Abby','F','Harold','B','User 1',NULL,'FieldWorker1','Unknown Individual','Unknown Individual');
INSERT INTO `individual` VALUES ('Indiv8',false,'2012-04-17',NULL,NULL,'P','','2009-01-01','1','PEBA1R','Peter','M','Bash','','User 1',NULL,'FieldWorker1','Unknown Individual','Unknown Individual');

INSERT INTO `locationhierarchy` VALUES ('hierarchy1','TAN','TAN','hierarchyLevelId3','hierarchy_root');
INSERT INTO `locationhierarchy` VALUES ('hierarchy2','IFA','IFA','hierarchyLevelId2','hierarchy1');
INSERT INTO `locationhierarchy` VALUES ('hierarchy3','MBI','MBI','hierarchyLevelId1','hierarchy2');

INSERT INTO `location` VALUES ('Location1',false,'2012-02-28',NULL,NULL,'A','','','','MBI01','','Harolds House','RUR','','User 1',NULL,'FieldWorker1','hierarchy3');
INSERT INTO `location` VALUES ('Location2',false,'2012-02-28',NULL,NULL,'A','','','','MBI02','','Ross House','RUR','','User 1',NULL,'FieldWorker1','hierarchy3');
INSERT INTO `location` VALUES ('Location3',false,'2012-02-28',NULL,NULL,'A','','','','MBI03','','Marsh House','RUR','','User 1',NULL,'FieldWorker1','hierarchy3');
INSERT INTO `location` VALUES ('Location4',false,'2012-02-28',NULL,NULL,'A','','','','MBI04','','Bash House House','RUR','','User 1',NULL,'FieldWorker1','hierarchy3');

INSERT INTO `visit` VALUES ('Visit1',false,'2012-02-28',NULL,NULL,'P','','VMBI01',1,'2012-03-28','User 1',NULL,'FieldWorker1','Location1');

INSERT INTO `residency` VALUES ('Residency1',false,'2012-04-17',NULL,NULL,'P','',NULL,'NA','1989-02-21','BIR','User 1',NULL,'FieldWorker1','Indiv2','Location1');
INSERT INTO `residency` VALUES ('Residency2',false,'2012-04-17',NULL,NULL,'P','',NULL,'NA','2011-03-01','BIR','User 1',NULL,'FieldWorker1','Indiv4','Location1');
INSERT INTO `residency` VALUES ('Residency3',false,'2012-04-17',NULL,NULL,'P','',NULL,'NA','1993-02-01','IMG','User 1',NULL,'FieldWorker1','Indiv3','Location1');
INSERT INTO `residency` VALUES ('Residency4',false,'2012-04-17',NULL,NULL,'P','',NULL,'NA','2012-04-15','BIR','User 1',NULL,'FieldWorker1','Indiv7','Location1');
INSERT INTO `residency` VALUES ('Residency5',false,'2012-04-17',NULL,NULL,'P','',NULL,'NA','2009-01-01','BIR','User 1',NULL,'FieldWorker1','Indiv8','Location1');
INSERT INTO `residency` VALUES ('Residency6',false,'2012-04-17',NULL,NULL,'P','',NULL,'NA','1993-02-01','ENU','User 1',NULL,'FieldWorker1','Indiv5','Location1');

INSERT INTO `socialgroup` VALUES ('SocialGroup1',false,'2012-04-17',NULL,NULL,'P','','SG01','Harold','FAM','User 1',NULL,'FieldWorker1','Indiv1');
INSERT INTO `socialgroup` VALUES ('SocialGroup2',false,'2012-04-17',NULL,NULL,'P','','SG02','Marsh','FAM','User 1',NULL,'FieldWorker1','Indiv5');
INSERT INTO `socialgroup` VALUES ('SocialGroup3',false,'2012-04-17',NULL,NULL,'P','','SG03','Ross','FAM','User 1',NULL,'FieldWorker1','Indiv3');
INSERT INTO `socialgroup` VALUES ('SocialGroup4',false,'2012-04-17',NULL,NULL,'P','','SG04','Bash','FAM','User 1',NULL,'FieldWorker1','Indiv8');

INSERT INTO `inmigration` VALUES ('Inmigration1',false,'2012-04-17',NULL,NULL,'P','','INTERNAL_INMIGRATION','origin','reason','1993-02-01',false,'User 1',NULL,'FieldWorker1','Indiv5','Residency6','Visit1');
INSERT INTO `outmigration` VALUES ('Outmigration1',false,'2012-04-17',NULL,NULL,'P','','name','reason','1997-05-05','User 1',NULL,'FieldWorker1','Indiv5','Residency6','Visit1');
