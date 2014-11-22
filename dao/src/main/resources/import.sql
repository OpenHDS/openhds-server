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
INSERT INTO user (uuid, firstName, lastName, fullName, description, username, password, lastLoginTime, deleted) VALUES ('User 1', 'OpenHDS', 'Administrator', 'Administrator', 'Administrator User', 'admin', 'test', 0, false)
INSERT INTO user_roles (user_uuid, role_uuid) VALUES ('User 1', 'ROLE1')
INSERT INTO user (uuid, firstName, lastName, fullName, description, username, password, lastLoginTime, deleted) VALUES ('User 2', 'Test', 'Account', 'Test Account', 'Test User Account', 'test', 'test', 0, false)
INSERT INTO user_roles (user_uuid, role_uuid) VALUES ('User 2', 'ROLE4')
INSERT INTO user (uuid, firstName, lastName, fullName, description, username, password, lastLoginTime, deleted) VALUES ('User 3', 'DataClerk', 'Account', 'Test Account', 'Test User Account', 'dataclerk', 'dataclerk', 0, false)
INSERT INTO user_roles (user_uuid, role_uuid) VALUES ('User 3', 'ROLE2')

-- Location Hierarchy root
INSERT INTO locationhierarchy(uuid,name,extId,level_uuid,parent_uuid) VALUES('hierarchy_root','', 'HIERARCHY_ROOT', NULL,NULL)

-- Field Worker
INSERT INTO fieldworker (uuid, extid, firstname, lastname, passwordHash, deleted) VALUES ('UnknownFieldWorker','UNK', 'Unknown', 'FieldWorker', 'invalid-password-hash', false);

-- Unknown Individual: This should always be pre-populated		
INSERT INTO individual(uuid, extId, firstName, middleName, lastName, gender, dob, mother_uuid, father_uuid, insertBy_uuid, insertDate, status, voidDate, voidReason, voidBy_uuid, deleted, collectedBy_uuid) VALUES('Unknown Individual','UNK','Unknown',NULL,'UNKNOWN','MALE', '1900-12-19', NULL, NULL,'User 1','2009-12-19','PENDING',NULL,NULL,NULL,false,'UnknownFieldWorker');
