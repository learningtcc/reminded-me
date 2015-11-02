CREATE KEYSPACE rm
         WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor': 1};

USE rm;

CREATE TABLE cassandratestmodel (
    id varchar,
    firstName varchar,
    lastName varchar,
    PRIMARY KEY(id));
    

CREATE TABLE webrequestmetric (
	id varchar,
	ipaddress varchar,
	requesturi varchar,
	requestpayload varchar,
	responsepayload varchar,
	status int,
	tgt varchar,
	operation varchar,
	systemtime bigint,
	email varchar,
	PRIMARY KEY(id));

	
CREATE TABLE webrequestmetric_by_email (
	id varchar,
	ipAddress varchar,
	requestURI varchar,
	requestPayload varchar,
	responsePayload varchar,
	status int,
	tgt varchar,
	operation varchar,
	systemTime bigint,
	email varchar,
	PRIMARY KEY(email));
	
CREATE TABLE webrequestmetric_by_tgt (
	id varchar,
	ipAddress varchar,
	requestURI varchar,
	requestPayload varchar,
	responsePayload varchar,
	status int,
	tgt varchar,
	operation varchar,
	systemTime bigint,
	email varchar,
	PRIMARY KEY(tgt));
	