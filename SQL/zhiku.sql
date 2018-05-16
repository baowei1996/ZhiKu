/* 创建zhiku数据库*/
CREATE database zhiku;
use zhiku;

/* 学院表 */
CREATE TABLE `college` (
  `xid` int(11) NOT NULL,
  `xname` varchar(100) NOT NULL,
  PRIMARY KEY (`xid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/* 课程表 */
CREATE TABLE `course` (
  `cid` int(11) NOT NULL,
  `cname` varchar(100) NOT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/* 专业表 */
CREATE TABLE `major` (
  `mid` int(11) NOT NULL,
  `mname` varchar(100) NOT NULL,
  `xid` int(11) NOT NULL,
  PRIMARY KEY (`mid`),
  KEY `fk_major_xid_idx` (`xid`),
  CONSTRAINT `fk_major_xid` FOREIGN KEY (`xid`) REFERENCES `college` (`xid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/* 课程和专业对应表 */
CREATE TABLE `mtoc` (
  `mid` int(11) NOT NULL,
  `cid` int(11) NOT NULL,
  PRIMARY KEY (`mid`,`cid`),
  KEY `fk_mtoc_mid_idx` (`mid`),
  KEY `fk_mtoc_cid_idx` (`cid`),
  CONSTRAINT `fk_mtoc_cid` FOREIGN KEY (`cid`) REFERENCES `course` (`cid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_mtoc_mid` FOREIGN KEY (`mid`) REFERENCES `major` (`mid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/* 用户表 */
CREATE TABLE `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `usr` varchar(20) NOT NULL,
  `nick` varchar(20) DEFAULT NULL,
  `pwd` varchar(32) NOT NULL,
  `avator` varchar(50) DEFAULT NULL,
  `sign` varchar(200) DEFAULT NULL,
  `coin` int(11) DEFAULT '0',
  `mail` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `qq` varchar(15) DEFAULT NULL,
  `xid` int(11) DEFAULT NULL,
  `mid` int(11) DEFAULT NULL,
  `auth` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `regip` varchar(16) DEFAULT NULL,
  `regtime` datetime DEFAULT NULL,
  `lastip` varchar(16) DEFAULT NULL,
  `lasttime` datetime DEFAULT NULL,
  `mailtime` datetime DEFAULT NULL,
  `unlktime` datetime DEFAULT NULL,
  `upcnt` int(11) DEFAULT '0',
  `dncnt` int(11) DEFAULT '0',
  `colcnt` int(11) DEFAULT '0',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `usr_UNIQUE` (`usr`),
  UNIQUE KEY `mail_UNIQUE` (`mail`),
  KEY `fk_xid_idx` (`xid`),
  KEY `fk_mid_idx` (`mid`),
  CONSTRAINT `fk_mid` FOREIGN KEY (`mid`) REFERENCES `major` (`mid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_xid` FOREIGN KEY (`xid`) REFERENCES `college` (`xid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8;


/* 文件表 */
CREATE TABLE `file_info` (
  `fid` int(11) NOT NULL AUTO_INCREMENT,
  `path` varchar(200) NOT NULL,
  `name` varchar(200) NOT NULL,
  `module` int(11) DEFAULT NULL,
  `course` int(11) DEFAULT NULL,
  `teacher` varchar(10) DEFAULT NULL,
  `docformat` int(11) DEFAULT NULL,
  `fileformat` varchar(10) DEFAULT NULL,
  `sha` varchar(300) DEFAULT NULL,
  `upuid` int(11) DEFAULT NULL,
  `uptime` datetime DEFAULT NULL,
  `dncnt` int(11) DEFAULT NULL,
  `colcnt` int(11) DEFAULT NULL,
  `descs` varchar(200) DEFAULT NULL,
  `origin` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`fid`),
  KEY `fk_course_idx` (`course`),
  KEY `fk_upuid_idx` (`upuid`),
  CONSTRAINT `fk_course` FOREIGN KEY (`course`) REFERENCES `course` (`cid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_upuid` FOREIGN KEY (`upuid`) REFERENCES `user` (`uid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8;


/* 文件操作 */
CREATE TABLE `file_op` (
  `fid` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `optime` datetime NOT NULL,
  `opip` varchar(16) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `descs` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`fid`,`uid`,`optime`),
  KEY `fk_uid_idx` (`uid`),
  CONSTRAINT `fk_fid` FOREIGN KEY (`fid`) REFERENCES `file_info` (`fid`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_uid` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/* 活动表 */
CREATE TABLE `activity` (
  `aid` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `img` varchar(200) DEFAULT ''null'',
  `linkweb` varchar(100) DEFAULT ''null'',
  `scancnt` int(11) DEFAULT NULL,
  `descs` varchar(200) DEFAULT ''null'',
  `pubtime` datetime DEFAULT NULL,
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/* 通知表 */
CREATE TABLE `notification` (
  `nid` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL DEFAULT ''系统通知'',
  `fromer` varchar(45) DEFAULT NULL,
  `ntime` datetime DEFAULT NULL,
  `content` varchar(500) DEFAULT NULL,
  `isread` int(11) DEFAULT NULL,
  `toer` int(11) DEFAULT NULL,
  PRIMARY KEY (`nid`),
  KEY `fk_n_u_to_idx` (`toer`),
  CONSTRAINT `fk_n_u_to` FOREIGN KEY (`toer`) REFERENCES `user` (`uid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/* 文件操作视图 */
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `file_op_view` AS 
select 
	`file_op`.`fid` AS `fid`,
	`file_op`.`uid` AS `uid`,
	`file_op`.`optime` AS `optime`,
	`file_op`.`opip` AS `opip`,
	`file_op`.`type` AS `type`,
	`file_op`.`descs` AS `op_descs`,
	`user`.`usr` AS `usr`,
	`user`.`nick` AS `nick`,
	`user`.`mail` AS `mail`,
	`user`.`phone` AS `phone`,
	`user`.`qq` AS `qq`,
	`college`.`xname` AS `xname`,
	`major`.`mname` AS `mname`,
	`user`.`auth` AS `auth`,
	`user`.`status` AS `user_status`,
	`file_info`.`name` AS `name`,
	`file_info`.`module` AS `module`,
	`course`.`cname` AS `cname`,
	`file_info`.`docformat` AS `docformat`,
	`file_info`.`uptime` AS `uptime`,
	`file_info`.`dncnt` AS `dncnt`,
	`file_info`.`descs` AS `file_descs`,
	`file_info`.`origin` AS `origin`,
	`file_info`.`status` AS `file_status` 
from (((((`file_op` join `user`) 
		join `file_info`) 
		join `college`) 
		join `major`) 
		join `course`) 
where ((`file_op`.`fid` = `file_info`.`fid`) 
		and (`file_op`.`uid` = `user`.`uid`) 
		and (`user`.`xid` = `college`.`xid`) 
		and (`major`.`mid` = `user`.`mid`) 
		and (`file_info`.`course` = `course`.`cid`));
		
		
/*文件视图 */
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `file_view` AS 
select 
	`user`.`uid` AS `uid`,
	`user`.`usr` AS `usr`,
	`user`.`nick` AS `nick`,
	`college`.`xname` AS `xname`,
	`major`.`mname` AS `mname`,
	`file_info`.`fid` AS `fid`,
	`file_info`.`name` AS `name`,
	`file_info`.`uptime` AS `uptime`,
	`file_info`.`module` AS `module`,
	`course`.`cid` AS `cid`,
	`course`.`cname` AS `cname`,
	`file_info`.`status` AS `status`,
	`file_info`.`docformat` AS `docformat`,
	`file_info`.`fileformat` AS `fileformat`,
	`file_info`.`teacher` AS `teacher`,
	`file_info`.`dncnt` AS `dncnt`,
	`file_info`.`colcnt` AS `colcnt`,
	`file_info`.`origin` AS `origin`,
	`file_info`.`descs` AS `descs` 
from ((((`user` join `file_info`) 
		join `college`) 
		join `major`) 
		join `course`) 
where ((`user`.`uid` = `file_info`.`upuid`) 
		and (`user`.`xid` = `college`.`xid`) 
		and (`user`.`mid` = `major`.`mid`) 
		and (`file_info`.`course` = `course`.`cid`));
		
		
/* 用户视图 */
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `user_view` AS 
select 
	`user`.`uid` AS `uid`,
	`user`.`usr` AS `usr`,
	`user`.`nick` AS `nick`,
	`user`.`pwd` AS `pwd`,
	`user`.`avator` AS `avator`,
	`user`.`sign` AS `sign`,
	`user`.`coin` AS `coin`,
	`user`.`mail` AS `mail`,
	`user`.`phone` AS `phone`,
	`user`.`qq` AS `qq`,
	`college`.`xname` AS `xname`,
	`major`.`mname` AS `mname`,
	`user`.`auth` AS `auth`,
	`user`.`status` AS `status`,
	`user`.`upcnt` AS `upcnt`,
	`user`.`dncnt` AS `dncnt`,
	`user`.`colcnt` AS `colcnt` 
from ((`user` join `college`) 
		join `major`) 
where ((`user`.`xid` = `college`.`xid`) 
		and (`user`.`mid` = `major`.`mid`));