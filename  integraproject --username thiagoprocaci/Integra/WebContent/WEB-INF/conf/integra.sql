# SQL Manager 2007 Lite for MySQL 4.2.1.1
# ---------------------------------------
# Host     : localhost
# Port     : 3306
# Database : integra


SET FOREIGN_KEY_CHECKS=0;

DROP DATABASE IF EXISTS `integra`;

CREATE DATABASE `integra`
    CHARACTER SET 'latin1'
    COLLATE 'latin1_swedish_ci';

USE `integra`;

#
# Structure for the `acl_object_identity` table : 
#

DROP TABLE IF EXISTS `acl_object_identity`;

CREATE TABLE `acl_object_identity` (
  `id` int(11) NOT NULL default '0',
  `object_identity` varchar(255) default NULL,
  `parent_object` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `acl_permission` table : 
#

DROP TABLE IF EXISTS `acl_permission`;

CREATE TABLE `acl_permission` (
  `id` int(11) NOT NULL default '0',
  `recipient` varchar(255) default NULL,
  `acl_object_identity` varchar(255) default NULL,
  `mask` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `domain` table : 
#

DROP TABLE IF EXISTS `domain`;

CREATE TABLE `domain` (
  `name` varchar(255) default NULL,
  `domainID` int(11) NOT NULL auto_increment,
  `googleDomainAdmin` varchar(255) NOT NULL default '',
  `googleDomainPassword` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`domainID`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `group` table : 
#

DROP TABLE IF EXISTS `group`;

CREATE TABLE `group` (
  `active` tinyint(1) default NULL,
  `name` varchar(255) NOT NULL default '',
  `superGroupID` int(11) default NULL,
  `groupID` int(11) NOT NULL auto_increment,
  `domainID` int(11) default NULL,
  `description` varchar(255) default NULL,
  `manuallyCreated` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`groupID`),
  UNIQUE KEY `name` (`name`),
  KEY `domainID` (`domainID`),
  KEY `superGroupID` (`superGroupID`),
  KEY `groupID` (`groupID`),
  CONSTRAINT `FK_Group_Domain` FOREIGN KEY (`domainID`) REFERENCES `domain` (`domainID`),
  CONSTRAINT `FK_Group_Group` FOREIGN KEY (`superGroupID`) REFERENCES `group` (`groupID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `calendar` table : 
#

DROP TABLE IF EXISTS `calendar`;

CREATE TABLE `calendar` (
  `calendarID` int(11) NOT NULL default '0',
  `groupID` int(11) default NULL,
  PRIMARY KEY  (`calendarID`),
  KEY `groupID` (`groupID`),
  CONSTRAINT `FK_Calendar_Group` FOREIGN KEY (`groupID`) REFERENCES `group` (`groupID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `emaillist` table : 
#

DROP TABLE IF EXISTS `emaillist`;

CREATE TABLE `emaillist` (
  `emailListID` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `groupID` int(11) default NULL,
  `domainID` int(11) default NULL,
  PRIMARY KEY  (`emailListID`),
  KEY `FKF5D1623AE86286D4` (`domainID`),
  CONSTRAINT `FKF5D1623AE86286D4` FOREIGN KEY (`domainID`) REFERENCES `domain` (`domainID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `grouplog` table : 
#

DROP TABLE IF EXISTS `grouplog`;

CREATE TABLE `grouplog` (
  `groupLogID` int(11) NOT NULL auto_increment,
  `groupName` varchar(255) default NULL,
  `groupDescription` varchar(255) default NULL,
  `domainID` int(11) default NULL,
  `beginTime` datetime default NULL,
  `endTime` datetime default NULL,
  PRIMARY KEY  (`groupLogID`),
  KEY `domainID` (`domainID`),
  CONSTRAINT `grouplog_fk` FOREIGN KEY (`domainID`) REFERENCES `domain` (`domainID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `person` table : 
#

DROP TABLE IF EXISTS `person`;

CREATE TABLE `person` (
  `googleAccount` varchar(255) default NULL,
  `registry` varchar(255) NOT NULL default '',
  `personID` int(11) NOT NULL auto_increment,
  `domainID` int(11) default NULL,
  `appsPassword` varchar(255) default NULL,
  PRIMARY KEY  (`personID`),
  UNIQUE KEY `registry` (`registry`),
  UNIQUE KEY `googleAccount` (`googleAccount`),
  KEY `FK8E488775E86286D4` (`domainID`),
  CONSTRAINT `FK8E488775E86286D4` FOREIGN KEY (`domainID`) REFERENCES `domain` (`domainID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `owner_group` table : 
#

DROP TABLE IF EXISTS `owner_group`;

CREATE TABLE `owner_group` (
  `groupID` int(11) NOT NULL default '0',
  `personID` int(11) NOT NULL default '0',
  PRIMARY KEY  (`groupID`,`personID`),
  KEY `FK66B5F1B3D36AB3B6` (`personID`),
  CONSTRAINT `FK66B5F1B3D36AB3B6` FOREIGN KEY (`personID`) REFERENCES `person` (`personID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `person_emaillist` table : 
#

DROP TABLE IF EXISTS `person_emaillist`;

CREATE TABLE `person_emaillist` (
  `personID` int(11) NOT NULL default '0',
  `emailListID` int(11) NOT NULL default '0',
  PRIMARY KEY  (`personID`,`emailListID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `person_group` table : 
#

DROP TABLE IF EXISTS `person_group`;

CREATE TABLE `person_group` (
  `personID` int(11) NOT NULL default '0',
  `groupID` int(11) NOT NULL default '0',
  KEY `personID` (`personID`),
  KEY `groupID` (`groupID`),
  CONSTRAINT `person_group_fk` FOREIGN KEY (`personID`) REFERENCES `person` (`personID`) ON UPDATE CASCADE,
  CONSTRAINT `person_group_fk1` FOREIGN KEY (`groupID`) REFERENCES `group` (`groupID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `person_usergroup` table : 
#

DROP TABLE IF EXISTS `person_usergroup`;

CREATE TABLE `person_usergroup` (
  `personID` int(11) NOT NULL default '0',
  `usergroupID` int(11) NOT NULL default '0',
  PRIMARY KEY  (`personID`,`usergroupID`),
  KEY `FKE93AE12AD36AB3B6` (`personID`),
  CONSTRAINT `FKE93AE12AD36AB3B6` FOREIGN KEY (`personID`) REFERENCES `person` (`personID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `systemgrouptask` table : 
#

DROP TABLE IF EXISTS `systemgrouptask`;

CREATE TABLE `systemgrouptask` (
  `systemTaskID` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `description` varchar(255) default NULL,
  `groupLogID` int(11) default '0',
  `createTime` datetime default NULL,
  PRIMARY KEY  (`systemTaskID`),
  KEY `groupLogID` (`groupLogID`),
  CONSTRAINT `systemtask_fk` FOREIGN KEY (`groupLogID`) REFERENCES `grouplog` (`groupLogID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `systemgrouperror` table : 
#

DROP TABLE IF EXISTS `systemgrouperror`;

CREATE TABLE `systemgrouperror` (
  `systemErrorID` int(11) NOT NULL auto_increment,
  `systemTaskID` int(11) default '0',
  `time` datetime default NULL,
  `cause` text,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`systemErrorID`),
  KEY `taskID` (`systemTaskID`),
  CONSTRAINT `systemerror_fk` FOREIGN KEY (`systemTaskID`) REFERENCES `systemgrouptask` (`systemTaskID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `transaction` table : 
#

DROP TABLE IF EXISTS `transaction`;

CREATE TABLE `transaction` (
  `transactionID` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`transactionID`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `unit` table : 
#

DROP TABLE IF EXISTS `unit`;

CREATE TABLE `unit` (
  `unitID` int(11) NOT NULL auto_increment,
  `unitName` varchar(255) default NULL,
  `domainID` int(11) default NULL,
  PRIMARY KEY  (`unitID`),
  UNIQUE KEY `unitName` (`unitName`),
  KEY `FK284DA4E86286D4` (`domainID`),
  CONSTRAINT `FK284DA4E86286D4` FOREIGN KEY (`domainID`) REFERENCES `domain` (`domainID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for the `usergroup` table : 
#

DROP TABLE IF EXISTS `usergroup`;

CREATE TABLE `usergroup` (
  `userGroupID` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`userGroupID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

