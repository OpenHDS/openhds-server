create table FormInstance (uuid varchar(32) not null, formInstanceId varchar(255), primary key (uuid)) ENGINE=InnoDB
create table FormInstance_ValidationMessage (FormInstance_uuid varchar(32) not null, validationMessages_uuid varchar(32) not null, unique (validationMessages_uuid)) ENGINE=InnoDB
create table ValidationMessage (uuid varchar(32) not null, message varchar(255), primary key (uuid)) ENGINE=InnoDB
create table classExtension (uuid varchar(32) not null, answers varchar(255), description varchar(255), entityClass varchar(255), name varchar(255) not null, primType varchar(255), roundNumber integer, primary key (uuid)) ENGINE=InnoDB
create table death (uuid varchar(32) not null, deleted bit not null, insertDate date, voidDate datetime, voidReason varchar(255), status varchar(255), statusMessage varchar(255), ageAtDeath bigint, deathCause varchar(255), deathDate date not null, deathPlace varchar(255), insertBy_uuid varchar(32), voidBy_uuid varchar(32), collectedBy_uuid varchar(32) not null, individual_uuid varchar(32), visitDeath_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table extension (uuid varchar(32) not null, entityExtId varchar(255) not null, extensionValue varchar(255) not null, classExtension_uuid varchar(32), entity_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table fieldworker (uuid varchar(32) not null, deleted bit not null, insertDate date, voidDate datetime, voidReason varchar(255), extId varchar(255) not null, firstName varchar(255), lastName varchar(255), insertBy_uuid varchar(32), voidBy_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table individual (uuid varchar(32) not null, deleted bit not null, insertDate date, voidDate datetime, voidReason varchar(255), status varchar(255), statusMessage varchar(255), dob date, dobAspect varchar(255), extId varchar(255) not null unique, firstName varchar(255), gender varchar(255), lastName varchar(255), middleName varchar(255), insertBy_uuid varchar(32), voidBy_uuid varchar(32), collectedBy_uuid varchar(32) not null, father_uuid varchar(32), mother_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table inmigration (uuid varchar(32) not null, deleted bit not null, insertDate date, voidDate datetime, voidReason varchar(255), status varchar(255), statusMessage varchar(255), migType varchar(255), origin varchar(255), reason varchar(255), recordedDate date not null, unknownIndividual bit, insertBy_uuid varchar(32), voidBy_uuid varchar(32), collectedBy_uuid varchar(32) not null, individual_uuid varchar(32) not null, residency_uuid varchar(32) not null, visit_uuid varchar(32) not null, primary key (uuid)) ENGINE=InnoDB
create table location (uuid varchar(32) not null, deleted bit not null, insertDate date, voidDate datetime, voidReason varchar(255), status varchar(255), statusMessage varchar(255), extId varchar(255) not null, locationName varchar(255), locationType varchar(255), insertBy_uuid varchar(32), voidBy_uuid varchar(32), collectedBy_uuid varchar(32) not null, locationLevel_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table locationhierarchy (uuid varchar(32) not null, extId varchar(255) not null, name varchar(255) not null, level_uuid varchar(32), parent_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table locationhierarchylevel (uuid varchar(32) not null, keyIdentifier integer not null, name varchar(255) not null, primary key (uuid)) ENGINE=InnoDB
create table membership (uuid varchar(32) not null, deleted bit not null, insertDate date, voidDate datetime, voidReason varchar(255), status varchar(255), statusMessage varchar(255), bIsToA varchar(255), endDate date, endType varchar(255), startDate date, startType varchar(255), insertBy_uuid varchar(32), voidBy_uuid varchar(32), collectedBy_uuid varchar(32) not null, individual_uuid varchar(32), socialGroup_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table note (uuid varchar(32) not null, deleted bit not null, insertDate date, voidDate datetime, voidReason varchar(255), status varchar(255), statusMessage varchar(255), description varchar(255), insertBy_uuid varchar(32), voidBy_uuid varchar(32), collectedBy_uuid varchar(32) not null, primary key (uuid)) ENGINE=InnoDB
create table outcome (uuid varchar(32) not null, type varchar(255), child_uuid varchar(32), childMembership_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table outmigration (uuid varchar(32) not null, deleted bit not null, insertDate date, voidDate datetime, voidReason varchar(255), status varchar(255), statusMessage varchar(255), destination varchar(255), reason varchar(255), recordedDate date not null, insertBy_uuid varchar(32), voidBy_uuid varchar(32), collectedBy_uuid varchar(32) not null, individual_uuid varchar(32) not null, residency_uuid varchar(32) not null, visit_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table pregnancyobservation (uuid varchar(32) not null, deleted bit not null, insertDate date, voidDate datetime, voidReason varchar(255), status varchar(255), statusMessage varchar(255), expectedDeliveryDate date not null, recordedDate date not null, insertBy_uuid varchar(32), voidBy_uuid varchar(32), collectedBy_uuid varchar(32) not null, mother_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table pregnancyoutcome (uuid varchar(32) not null, deleted bit not null, insertDate date, voidDate datetime, voidReason varchar(255), status varchar(255), statusMessage varchar(255), childEverBorn integer, numberOfLiveBirths integer, outcomeDate date, insertBy_uuid varchar(32), voidBy_uuid varchar(32), collectedBy_uuid varchar(32) not null, father_uuid varchar(32), mother_uuid varchar(32), visit_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table pregnancyoutcome_outcome (pregnancyoutcome_uuid varchar(32) not null, outcomes_uuid varchar(32) not null, unique (outcomes_uuid)) ENGINE=InnoDB
create table privilege (uuid varchar(32) not null, privilege varchar(255), primary key (uuid)) ENGINE=InnoDB
create table relationship (uuid varchar(32) not null, deleted bit not null, insertDate date, voidDate datetime, voidReason varchar(255), status varchar(255), statusMessage varchar(255), aIsToB varchar(255), endDate date, endType varchar(255), startDate date not null, insertBy_uuid varchar(32), voidBy_uuid varchar(32), collectedBy_uuid varchar(32) not null, individualA_uuid varchar(32), individualB_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table residency (uuid varchar(32) not null, deleted bit not null, insertDate date, voidDate datetime, voidReason varchar(255), status varchar(255), statusMessage varchar(255), endDate date, endType varchar(255), startDate date not null, startType varchar(255), insertBy_uuid varchar(32), voidBy_uuid varchar(32), collectedBy_uuid varchar(32) not null, individual_uuid varchar(32), location_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table role (uuid varchar(32) not null, deleted bit not null, insertDate date, voidDate datetime, voidReason varchar(255), description varchar(255), name varchar(255), insertBy_uuid varchar(32), voidBy_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table role_privileges (role_uuid varchar(32) not null, privilege_uuid varchar(32) not null, primary key (role_uuid, privilege_uuid)) ENGINE=InnoDB
create table round (uuid varchar(32) not null, endDate date, remarks varchar(255), roundNumber integer, startDate date, primary key (uuid)) ENGINE=InnoDB
create table socialgroup (uuid varchar(32) not null, deleted bit not null, insertDate date, voidDate datetime, voidReason varchar(255), status varchar(255), statusMessage varchar(255), extId varchar(255) unique, groupName varchar(255), groupType varchar(255), insertBy_uuid varchar(32), voidBy_uuid varchar(32), collectedBy_uuid varchar(32) not null, groupHead_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table user (uuid varchar(32) not null, deleted bit not null, description varchar(255), firstName varchar(255), fullName varchar(255), lastLoginTime bigint not null, lastName varchar(255), password varchar(255), sessionId varchar(255), username varchar(255), primary key (uuid)) ENGINE=InnoDB
create table user_roles (user_uuid varchar(32) not null, role_uuid varchar(32) not null, primary key (user_uuid, role_uuid)) ENGINE=InnoDB
create table visit (uuid varchar(32) not null, deleted bit not null, insertDate date, voidDate datetime, voidReason varchar(255), status varchar(255), statusMessage varchar(255), extId varchar(255), roundNumber integer, visitDate date not null, insertBy_uuid varchar(32), voidBy_uuid varchar(32), collectedBy_uuid varchar(32) not null, visitLocation_uuid varchar(32), primary key (uuid)) ENGINE=InnoDB
create table whitelist (uuid varchar(32) not null, address varchar(255), primary key (uuid)) ENGINE=InnoDB
alter table FormInstance_ValidationMessage add index FK565F30085537B136 (validationMessages_uuid), add constraint FK565F30085537B136 foreign key (validationMessages_uuid) references ValidationMessage (uuid)
alter table FormInstance_ValidationMessage add index FK565F3008A1616787 (FormInstance_uuid), add constraint FK565F3008A1616787 foreign key (FormInstance_uuid) references FormInstance (uuid)
alter table death add index FK5B0927497735AF9 (insertBy_uuid), add constraint FK5B0927497735AF9 foreign key (insertBy_uuid) references user (uuid)
alter table death add index FK5B0927436F4BE6E (collectedBy_uuid), add constraint FK5B0927436F4BE6E foreign key (collectedBy_uuid) references fieldworker (uuid)
alter table death add index FK5B09274A726FBE (voidBy_uuid), add constraint FK5B09274A726FBE foreign key (voidBy_uuid) references user (uuid)
alter table death add index FK5B0927468E8A298 (visitDeath_uuid), add constraint FK5B0927468E8A298 foreign key (visitDeath_uuid) references visit (uuid)
alter table death add index FK5B0927480470E9E (individual_uuid), add constraint FK5B0927480470E9E foreign key (individual_uuid) references individual (uuid)
alter table extension add index FKDB7D1C3FEA9B72FE (entity_uuid), add constraint FKDB7D1C3FEA9B72FE foreign key (entity_uuid) references visit (uuid)
alter table extension add index FKDB7D1C3FE3D6A93E (classExtension_uuid), add constraint FKDB7D1C3FE3D6A93E foreign key (classExtension_uuid) references classExtension (uuid)
alter table fieldworker add index FK528ED0F897735AF9 (insertBy_uuid), add constraint FK528ED0F897735AF9 foreign key (insertBy_uuid) references user (uuid)
alter table fieldworker add index FK528ED0F8A726FBE (voidBy_uuid), add constraint FK528ED0F8A726FBE foreign key (voidBy_uuid) references user (uuid)
alter table individual add index FKFD3DA29997735AF9 (insertBy_uuid), add constraint FKFD3DA29997735AF9 foreign key (insertBy_uuid) references user (uuid)
alter table individual add index FKFD3DA2999E4C80B4 (mother_uuid), add constraint FKFD3DA2999E4C80B4 foreign key (mother_uuid) references individual (uuid)
alter table individual add index FKFD3DA29936F4BE6E (collectedBy_uuid), add constraint FKFD3DA29936F4BE6E foreign key (collectedBy_uuid) references fieldworker (uuid)
alter table individual add index FKFD3DA299A726FBE (voidBy_uuid), add constraint FKFD3DA299A726FBE foreign key (voidBy_uuid) references user (uuid)
alter table individual add index FKFD3DA29930076DBB (father_uuid), add constraint FKFD3DA29930076DBB foreign key (father_uuid) references individual (uuid)
alter table inmigration add index FKD692204997735AF9 (insertBy_uuid), add constraint FKD692204997735AF9 foreign key (insertBy_uuid) references user (uuid)
alter table inmigration add index FKD692204936F4BE6E (collectedBy_uuid), add constraint FKD692204936F4BE6E foreign key (collectedBy_uuid) references fieldworker (uuid)
alter table inmigration add index FKD6922049A726FBE (voidBy_uuid), add constraint FKD6922049A726FBE foreign key (voidBy_uuid) references user (uuid)
alter table inmigration add index FKD6922049851505F6 (residency_uuid), add constraint FKD6922049851505F6 foreign key (residency_uuid) references residency (uuid)
alter table inmigration add index FKD6922049301AAA96 (visit_uuid), add constraint FKD6922049301AAA96 foreign key (visit_uuid) references visit (uuid)
alter table inmigration add index FKD692204980470E9E (individual_uuid), add constraint FKD692204980470E9E foreign key (individual_uuid) references individual (uuid)
alter table location add index FK714F9FB597735AF9 (insertBy_uuid), add constraint FK714F9FB597735AF9 foreign key (insertBy_uuid) references user (uuid)
alter table location add index FK714F9FB536F4BE6E (collectedBy_uuid), add constraint FK714F9FB536F4BE6E foreign key (collectedBy_uuid) references fieldworker (uuid)
alter table location add index FK714F9FB5A726FBE (voidBy_uuid), add constraint FK714F9FB5A726FBE foreign key (voidBy_uuid) references user (uuid)
alter table location add index FK714F9FB5DFDC7027 (locationLevel_uuid), add constraint FK714F9FB5DFDC7027 foreign key (locationLevel_uuid) references locationhierarchy (uuid)
alter table locationhierarchy add index FK6AD2CE20F61555E (level_uuid), add constraint FK6AD2CE20F61555E foreign key (level_uuid) references locationhierarchylevel (uuid)
alter table locationhierarchy add index FK6AD2CE2064C0094C (parent_uuid), add constraint FK6AD2CE2064C0094C foreign key (parent_uuid) references locationhierarchy (uuid)
alter table membership add index FKB01D87D691408DD6 (socialGroup_uuid), add constraint FKB01D87D691408DD6 foreign key (socialGroup_uuid) references socialgroup (uuid)
alter table membership add index FKB01D87D697735AF9 (insertBy_uuid), add constraint FKB01D87D697735AF9 foreign key (insertBy_uuid) references user (uuid)
alter table membership add index FKB01D87D636F4BE6E (collectedBy_uuid), add constraint FKB01D87D636F4BE6E foreign key (collectedBy_uuid) references fieldworker (uuid)
alter table membership add index FKB01D87D6A726FBE (voidBy_uuid), add constraint FKB01D87D6A726FBE foreign key (voidBy_uuid) references user (uuid)
alter table membership add index FKB01D87D680470E9E (individual_uuid), add constraint FKB01D87D680470E9E foreign key (individual_uuid) references individual (uuid)
alter table note add index FK33AFF297735AF9 (insertBy_uuid), add constraint FK33AFF297735AF9 foreign key (insertBy_uuid) references user (uuid)
alter table note add index FK33AFF236F4BE6E (collectedBy_uuid), add constraint FK33AFF236F4BE6E foreign key (collectedBy_uuid) references fieldworker (uuid)
alter table note add index FK33AFF2A726FBE (voidBy_uuid), add constraint FK33AFF2A726FBE foreign key (voidBy_uuid) references user (uuid)
alter table outcome add index FKBE0C075286CD7C22 (childMembership_uuid), add constraint FKBE0C075286CD7C22 foreign key (childMembership_uuid) references membership (uuid)
alter table outcome add index FKBE0C0752948ED5FB (child_uuid), add constraint FKBE0C0752948ED5FB foreign key (child_uuid) references individual (uuid)
alter table outmigration add index FKE109DC4097735AF9 (insertBy_uuid), add constraint FKE109DC4097735AF9 foreign key (insertBy_uuid) references user (uuid)
alter table outmigration add index FKE109DC4036F4BE6E (collectedBy_uuid), add constraint FKE109DC4036F4BE6E foreign key (collectedBy_uuid) references fieldworker (uuid)
alter table outmigration add index FKE109DC40A726FBE (voidBy_uuid), add constraint FKE109DC40A726FBE foreign key (voidBy_uuid) references user (uuid)
alter table outmigration add index FKE109DC40851505F6 (residency_uuid), add constraint FKE109DC40851505F6 foreign key (residency_uuid) references residency (uuid)
alter table outmigration add index FKE109DC40301AAA96 (visit_uuid), add constraint FKE109DC40301AAA96 foreign key (visit_uuid) references visit (uuid)
alter table outmigration add index FKE109DC4080470E9E (individual_uuid), add constraint FKE109DC4080470E9E foreign key (individual_uuid) references individual (uuid)
alter table pregnancyobservation add index FKDB9E9ADF97735AF9 (insertBy_uuid), add constraint FKDB9E9ADF97735AF9 foreign key (insertBy_uuid) references user (uuid)
alter table pregnancyobservation add index FKDB9E9ADF9E4C80B4 (mother_uuid), add constraint FKDB9E9ADF9E4C80B4 foreign key (mother_uuid) references individual (uuid)
alter table pregnancyobservation add index FKDB9E9ADF36F4BE6E (collectedBy_uuid), add constraint FKDB9E9ADF36F4BE6E foreign key (collectedBy_uuid) references fieldworker (uuid)
alter table pregnancyobservation add index FKDB9E9ADFA726FBE (voidBy_uuid), add constraint FKDB9E9ADFA726FBE foreign key (voidBy_uuid) references user (uuid)
alter table pregnancyoutcome add index FKA53B1B8597735AF9 (insertBy_uuid), add constraint FKA53B1B8597735AF9 foreign key (insertBy_uuid) references user (uuid)
alter table pregnancyoutcome add index FKA53B1B859E4C80B4 (mother_uuid), add constraint FKA53B1B859E4C80B4 foreign key (mother_uuid) references individual (uuid)
alter table pregnancyoutcome add index FKA53B1B8536F4BE6E (collectedBy_uuid), add constraint FKA53B1B8536F4BE6E foreign key (collectedBy_uuid) references fieldworker (uuid)
alter table pregnancyoutcome add index FKA53B1B85A726FBE (voidBy_uuid), add constraint FKA53B1B85A726FBE foreign key (voidBy_uuid) references user (uuid)
alter table pregnancyoutcome add index FKA53B1B8530076DBB (father_uuid), add constraint FKA53B1B8530076DBB foreign key (father_uuid) references individual (uuid)
alter table pregnancyoutcome add index FKA53B1B85301AAA96 (visit_uuid), add constraint FKA53B1B85301AAA96 foreign key (visit_uuid) references visit (uuid)
alter table pregnancyoutcome_outcome add index FKC419749895ECD547 (outcomes_uuid), add constraint FKC419749895ECD547 foreign key (outcomes_uuid) references outcome (uuid)
alter table pregnancyoutcome_outcome add index FKC4197498AFA9FEFE (pregnancyoutcome_uuid), add constraint FKC4197498AFA9FEFE foreign key (pregnancyoutcome_uuid) references pregnancyoutcome (uuid)
alter table relationship add index FKF064763897735AF9 (insertBy_uuid), add constraint FKF064763897735AF9 foreign key (insertBy_uuid) references user (uuid)
alter table relationship add index FKF064763836F4BE6E (collectedBy_uuid), add constraint FKF064763836F4BE6E foreign key (collectedBy_uuid) references fieldworker (uuid)
alter table relationship add index FKF0647638A726FBE (voidBy_uuid), add constraint FKF0647638A726FBE foreign key (voidBy_uuid) references user (uuid)
alter table relationship add index FKF0647638995599AF (individualA_uuid), add constraint FKF0647638995599AF foreign key (individualA_uuid) references individual (uuid)
alter table relationship add index FKF06476389B0A724E (individualB_uuid), add constraint FKF06476389B0A724E foreign key (individualB_uuid) references individual (uuid)
alter table residency add index FK7E9A5B1A97735AF9 (insertBy_uuid), add constraint FK7E9A5B1A97735AF9 foreign key (insertBy_uuid) references user (uuid)
alter table residency add index FK7E9A5B1A36F4BE6E (collectedBy_uuid), add constraint FK7E9A5B1A36F4BE6E foreign key (collectedBy_uuid) references fieldworker (uuid)
alter table residency add index FK7E9A5B1AA726FBE (voidBy_uuid), add constraint FK7E9A5B1AA726FBE foreign key (voidBy_uuid) references user (uuid)
alter table residency add index FK7E9A5B1AB374291E (location_uuid), add constraint FK7E9A5B1AB374291E foreign key (location_uuid) references location (uuid)
alter table residency add index FK7E9A5B1A80470E9E (individual_uuid), add constraint FK7E9A5B1A80470E9E foreign key (individual_uuid) references individual (uuid)
alter table role add index FK35807697735AF9 (insertBy_uuid), add constraint FK35807697735AF9 foreign key (insertBy_uuid) references user (uuid)
alter table role add index FK358076A726FBE (voidBy_uuid), add constraint FK358076A726FBE foreign key (voidBy_uuid) references user (uuid)
alter table role_privileges add index FK797EEF4B21E9AC56 (privilege_uuid), add constraint FK797EEF4B21E9AC56 foreign key (privilege_uuid) references privilege (uuid)
alter table role_privileges add index FK797EEF4B1920CBBE (role_uuid), add constraint FK797EEF4B1920CBBE foreign key (role_uuid) references role (uuid)
alter table socialgroup add index FK89CF7B329001C018 (groupHead_uuid), add constraint FK89CF7B329001C018 foreign key (groupHead_uuid) references individual (uuid)
alter table socialgroup add index FK89CF7B3297735AF9 (insertBy_uuid), add constraint FK89CF7B3297735AF9 foreign key (insertBy_uuid) references user (uuid)
alter table socialgroup add index FK89CF7B3236F4BE6E (collectedBy_uuid), add constraint FK89CF7B3236F4BE6E foreign key (collectedBy_uuid) references fieldworker (uuid)
alter table socialgroup add index FK89CF7B32A726FBE (voidBy_uuid), add constraint FK89CF7B32A726FBE foreign key (voidBy_uuid) references user (uuid)
alter table user_roles add index FK73429949195798DE (user_uuid), add constraint FK73429949195798DE foreign key (user_uuid) references user (uuid)
alter table user_roles add index FK734299491920CBBE (role_uuid), add constraint FK734299491920CBBE foreign key (role_uuid) references role (uuid)
alter table visit add index FK6B04D4B97735AF9 (insertBy_uuid), add constraint FK6B04D4B97735AF9 foreign key (insertBy_uuid) references user (uuid)
alter table visit add index FK6B04D4B36F4BE6E (collectedBy_uuid), add constraint FK6B04D4B36F4BE6E foreign key (collectedBy_uuid) references fieldworker (uuid)
alter table visit add index FK6B04D4BEC630DB3 (visitLocation_uuid), add constraint FK6B04D4BEC630DB3 foreign key (visitLocation_uuid) references location (uuid)
alter table visit add index FK6B04D4BA726FBE (voidBy_uuid), add constraint FK6B04D4BA726FBE foreign key (voidBy_uuid) references user (uuid)
