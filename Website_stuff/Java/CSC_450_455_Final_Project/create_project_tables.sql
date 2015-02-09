#########################################################
drop table if exists DND_character_ability;				#
drop table if exists DND_character_skill;				#
drop table if exists DND_character_gear;				#
drop table if exists DND_character_armor;				#
drop table if exists DND_character_weapon;				#
#########################################################
drop table if exists DND_race_skill_proficiency;	   	#
drop table if exists DND_race_language;			   		#
drop table if exists DND_race_stat_bonus;				#
#########################################################
drop table if exists DND_class_skill_recommendation; 	#
drop table if exists DND_class_recommended_alignment;	#
drop table if exists DND_class_armor_proficiency;    	#
drop table if exists DND_class_weapon_proficiency;   	#
drop table if exists DND_class_stat;				   	#
drop table if exists DND_class_ability;			   		#
#########################################################
drop table if exists DND_character;						#
#########################################################
drop table if exists DND_gear;							#
drop table if exists DND_armor;							#
drop table if exists DND_weapon;						#
#########################################################
drop table if exists DND_ability;						#
drop table if exists DND_skill;							#
drop table if exists DND_race;							#
drop table if exists DND_class;							#
#########################################################
drop table if exists DND_stat;							#
drop table if exists DND_armor_type;					#
drop table if exists DND_weapon_type;					#
drop table if exists DND_alignment;						#
drop table if exists DND_language;						#
drop table if exists DND_dice;							#
#########################################################


################################ This are the base level tables that other tables reference ################################

create table DND_dice(
	dice	varchar(8) not null,
	primary key (dice)
	)TYPE = INNODB;

create table DND_language(
	language_name		varchar(50),
	primary key (language_name)
	)TYPE = INNODB;

create table DND_alignment(
	alignment_name varchar(50),
	primary key (alignment_name)
	)TYPE = INNODB;

create table DND_weapon_type(
	weapon_type 	varchar(50),
	primary key	(weapon_type)
	)TYPE = INNODB;

create table DND_armor_type(
	armor_type 	varchar(50),
	primary key	(armor_type)
	)TYPE = INNODB;

create table DND_stat(
	stat_name		varchar(20),
	description		varchar(500),
	primary key 	(stat_name)
	)TYPE = INNODB;

################################ make base character attribute tables here #################################################

create table DND_class(
	class_name		varchar(50) not null, 
	role			varchar(50) not null, 
	power_source	varchar(50) not null,
	hit_die			varchar(6) not null, 
	description		varchar(500) not null,
	gold_die		varchar(6) not null,
	primary key (class_name),
	foreign key (hit_die) references DND_dice(dice),
	foreign key (gold_die) references DND_dice(dice)
	)TYPE = INNODB;

create table DND_race(
	race_name			varchar(50) , 
	speed				numeric(4,0),
	size				varchar(50) not null,
	description		varchar(500) not null,
	primary key (race_name),
	check ( size > 0)
	)TYPE = INNODB;

create table DND_skill(
	skill_name			varchar(50), 
	stat_modifier		varchar(20), 
	description 		varchar(50),
	primary key (skill_name)
	)TYPE = INNODB;

create table DND_ability(
	ability_name		varchar(50) not null, 
	level_requirement	numeric(2,0) not null,
	proficiency_bonus	varchar(50) not null, 
	features			varchar(500),
	damage				varchar(50),
	spell_slots			numeric(2,0),
	spell_casting_stat	varchar(50),
	primary key (ability_name)
	)TYPE = INNODB;	

################################ now make the three equipment tables #######################################################

create table DND_weapon(
	weapon_name 		varchar(50),
	weapon_type			varchar(50),
	damage				varchar(50),
	properties			varchar(50),
	cost				varchar(50) not null,
	weight				varchar(50) not null,
	description			varchar(250),
	primary key (weapon_name),
	foreign key (weapon_type) references DND_weapon_type(weapon_type)
	)TYPE = INNODB;

create table DND_armor(
	armor_name 			varchar(50),
	armor_class			numeric(4,0),
	armor_type			varchar(50),
	stealth 			varchar(50),
	cost				varchar(50) not null,
	weight				varchar(50) not null,
	description			varchar(250),
	primary key (armor_name),
	foreign key (armor_type) references DND_armor_type(armor_type),
	check (stealth in ("none", "disadvantage", "advantage"))
	)TYPE = INNODB;

create table DND_gear(
	gear_name 			varchar(50),
	cost				varchar(50) not null, 
	weight				varchar(50) not null,
	description			varchar(250),
	primary key (gear_name)
	)TYPE = INNODB;	

################################ now make the character table ##############################################################

create table DND_character(
	character_name				varchar(50) not null,
	race						varchar(50) not null,
	classes						varchar(50) not null,
	strength_score				numeric(2,0) not null,
	dexterity_score				numeric(2,0) not null,
	charisma_score				numeric(2,0) not null,
	intelligence_score			numeric(2,0) not null,
	wisdom_score				numeric(2,0) not null,
	constitution_score			numeric(2,0) not null,
	character_level				numeric(2,0) not null,
	health						numeric(3,0),
	stength_saving_throws		numeric(2,0),
	dexterity_saving_throws		numeric(2,0),
	charisma_saving_throws		numeric(2,0),
	intelligence_saving_throws	numeric(2,0),
	wisdom_saving_throws		numeric(2,0),
	constitution_saving_throws	numeric(2,0),
	armor_class					numeric(2,0),
	gold_pieces					numeric(12,0),
	silver_pieces				numeric(2,0),
	copper_pieces				numeric(2,0),
	personality_traits			varchar(500) not null, 
	ideals						varchar(500) not null, 
	bonds						varchar(500) not null,
	flaws						varchar(500) not null,
	appearance					varchar(500), 
	backstory					varchar(5000), 
	age							numeric(4,0), 
	height						varchar(10),
	weight						numeric(4,0) not null,
	gender						varchar(10) not null,
	alignment					varchar(20) not null,
	primary key (character_name),
	foreign key (race) references DND_race(race_name),
	foreign key (classes) references DND_class(class_name),
	foreign key (alignment) references DND_alignment(alignment_name),
	check (age > 0),
	check (gold_pieces >= 0),
	check (silver_pieces >= 0),
	check (copper_pieces >= 0)
	)TYPE = INNODB;

################################ now make the multivalue attibruite tables for classes #####################################

create table DND_class_ability(
	class_name			varchar(50),
	ability_name		varchar(50),
	foreign key (class_name) references DND_class(class_name),
	foreign key (ability_name) references DND_ability(ability_name),
	primary key (class_name, ability_name)
	)TYPE = INNODB;	

create table DND_class_stat(
	class_name 			varchar(50),
	stat_name			varchar(50),
	stat_type  			varchar(50), 
	foreign key (class_name) references DND_class(class_name),
	foreign key (stat_name) references DND_stat(stat_name),
	primary key (class_name, stat_name),
	check ( stat_type in ("primary", "secondary"))
	)TYPE = INNODB;

create table DND_class_weapon_proficiency(
	class_name 			varchar(50),
	weapon_type			varchar(50),
	foreign key (class_name) references DND_class(class_name),
	foreign key (weapon_type) references DND_weapon_type(weapon_type),
	primary key (class_name, weapon_type)
	)TYPE = INNODB;

create table DND_class_armor_proficiency(
	class_name 			varchar(50),
	armor_type			varchar(50),
	foreign key (class_name) references DND_class(class_name),
	foreign key (armor_type) references DND_armor_type(armor_type),
	primary key (class_name, armor_type)
	)TYPE = INNODB;

create table DND_class_recommended_alignment(
	class_name 			varchar(50),
	alignment_name 		varchar(50),
	foreign key (class_name) references DND_class(class_name),
	foreign key (alignment_name) references DND_alignment(alignment_name),
	primary key (class_name, alignment_name)
	)TYPE = INNODB;

create table DND_class_skill_recommendation(
	class_name 			varchar(50),
	skill_name 			varchar(50),
	foreign key (class_name) references DND_class(class_name),
	foreign key (skill_name) references DND_skill(skill_name),
	primary key (class_name, skill_name)
	)TYPE = INNODB;

################################ now make the multivalue attibruite tables for races #######################################	
	
create table DND_race_stat_bonus(
	race_name  			varchar(50),
	stat_name			varchar(50),
	stat_bonus			numeric(1,0),
	foreign key (race_name) references DND_race(race_name),
	foreign key (stat_name) references DND_stat(stat_name),
	primary key (race_name, stat_name),
	check ( stat_bonus in (1,2))
	)TYPE = INNODB;
	
create table DND_race_language(
	race_name			varchar(50),
	language_name		varchar(50),
	foreign key (race_name) references DND_race(race_name),
	foreign key (language_name) references DND_language(language_name),
	primary key (race_name, language_name)
	)TYPE = INNODB;
	
create table DND_race_skill_proficiency(
	race_name			varchar(50),
	skill_name			varchar(50),
	foreign key (race_name) references DND_race(race_name),
	foreign key (skill_name) references DND_skill(skill_name),
	primary key (race_name, skill_name)
	)TYPE = INNODB;
	
################################ now make the multivalue attibruite tables for characters ##################################
	
create table DND_character_weapon(
	character_name		varchar(50),
	weapon_name			varchar(50),
	foreign key (character_name) references DND_character(character_name),
	foreign key (weapon_name) references DND_weapon(weapon_name),
	primary key (character_name, weapon_name)
	)TYPE = INNODB;

create table DND_character_armor(
	character_name		varchar(50),
	armor_name			varchar(50),
	foreign key (character_name) references DND_character(character_name),
	foreign key (armor_name) references DND_armor(armor_name),
	primary key (character_name, armor_name)
	)TYPE = INNODB;

create table DND_character_gear(
	character_name		varchar(50),
	gear_name			varchar(50),
	foreign key (character_name) references DND_character(character_name),
	foreign key (gear_name) references DND_gear(gear_name),
	primary key (character_name, gear_name)
	)TYPE = INNODB;

create table DND_character_skill(
	character_name		varchar(50),
	skill_name			varchar(50),
	foreign key (character_name) references DND_character(character_name),
	foreign key (skill_name) references DND_skill(skill_name),
	primary key (character_name, skill_name)
	)TYPE = INNODB;

create table DND_character_ability(
	character_name		varchar(50),
	ability_name		varchar(50), 
	foreign key (ability_name) references DND_ability(ability_name),
	foreign key (character_name) references DND_character(character_name),
	primary key (character_name, ability_name)
	)TYPE = INNODB;
	