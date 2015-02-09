package main_panels;

import java.sql.*;
import java.io.Console;
import java.util.ArrayList;

/**
 * Database_Manager: 
 * The main class upon which all classes come together to run and interact with the Database.
 * This class is one of 7 classes of the main panels used in the creation of a new D&D character.
 * @author Devin Simpson, Thomas Man, Victor Mancha
 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
 */
public class Database_Manager {

	private Connection conn;

	// PlaceHolder for the selected character attributes
	// Holds Selected Class Name
	public String character_class;
	// Holds Selected Race Name
	public String character_race;
	// Holds Selected Stars
	public int strength_score;
	public int dexterity_score;
	public int charisma_score;
	public int intelligence_score;
	public int wisdom_score;
	public int constitution_score;
	// Holds Selected Character Details
	public String character_name;
	public int character_level;
	public int health;
	public int strength_saving_throws;
	public int dexterity_saving_throws;
	public int charisma_saving_throws;
	public int intelligence_saving_throws;
	public int wisdom_saving_throws;
	public int constitution_saving_throws;
	public int armor_class;
	public int gold_pieces;
	public int silver_pieces;
	public int copper_pieces;
	public int character_age;
	public String character_height;
	public String character_weight;
	public String character_gender;
	public String character_appearance;
	public String character_backstory;
	public String character_personaility_traits;
	public String character_ideals;
	public String character_bonds;
	public String character_flaws;
	public String character_alignment;
	// Holds Selected Skill Names
	ArrayList<String> character_skills;
	// Holds Selected Abilities Names
	ArrayList<String> character_ability;
	// Holds Selected Equipment
	ArrayList<String> character_weapon;
	ArrayList<String> character_armor;
	ArrayList<String> character_gear;

	// PlaceHolder for the class attributes
	public String newClass_name;
	public String class_role;
	public String class_power_source;
	public String class_hit_die;
	public String class_description;
	public String class_gold_die;

	// PlaceHolder for the race attributes
	public String newRace_name;
	public int race_speed;
	public String race_size;
	public String race_description;

	// PlaceHolder for the skill attributes
	public String newSkill_name;
	public String skill_modifier;
	public String skill_description;

	// PlaceHolder for the ability attributes
	public String newAbility_name;
	public String ability_level;
	public String ability_proficiency_bonus;
	public String ability_features;
	public String ability_damage;
	public String ability_slots;
	public String ability_casting_stat;

	//keep track if lists contain new data
	boolean newSkills;
	boolean newAbilites ;
	boolean newWeapons;
	boolean newArmor;
	boolean newGear;
	
	ArrayList<String> detailLabels = new ArrayList<String>();
	ArrayList<String> detailValues = new ArrayList<String>();

	/**
	 * The constructor for the Database_Manager
	 * Called by DnD_Character_Creator
	 * @author Devin Simpson, Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public Database_Manager() {
		loadDriver();

		// set up character detail labels, very important for parsing the list
		detailLabels.add("character_name");
		detailLabels.add("health");
		detailLabels.add("armor_class");
		detailLabels.add("gold_pieces");
		detailLabels.add("silver_pieces");
		detailLabels.add("copper_pieces");
		detailLabels.add("charisma_saving_throws");
		detailLabels.add("constitution_saving_throws");
		detailLabels.add("dexterity_saving_throws");
		detailLabels.add("intelligence_saving_throws");
		detailLabels.add("strength_saving_throws");
		detailLabels.add("wisdom_saving_throws");
		detailLabels.add("character_age");
		detailLabels.add("character_height");
		detailLabels.add("character_weight");
		detailLabels.add("character_appearance");
		detailLabels.add("character_backstory");
		detailLabels.add("character_personaility_traits");
		detailLabels.add("character_ideals");
		detailLabels.add("character_bonds");
		detailLabels.add("character_flaws");

		// read user name, database name, and password
		String database_name = "vmm2312";
		String username = "vmm2312";
		String password = "LATu50BxY";

		conn = establish_connection(database_name, username, password);
	}

	/**
	 * Loads the MySQL driver, makes sure the 
	 * corresponding jar file is included on the java Classpath
	 * @author Devin Simpson and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	private void loadDriver() {
		try {
			// this will load the MySQL driver
			// make sure the corresponding jar file is included on the java
			// CLASSPATH
			Class.forName("com.mysql.jdbc.Driver");

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * Initializes all of the character variables
	 * Called by DnD_Character_Creator
	 * @author Devin Simpson and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void initalizeCharacterVariables() {

		character_class = "";

		character_race = "";

		strength_score = -1000;
		dexterity_score = -1000;
		charisma_score = -1000;
		intelligence_score = -1000;
		wisdom_score = -1000;
		constitution_score = -1000;

		character_name = "";
		character_level = -1000;
		health = -1000;
		strength_saving_throws = -1000;
		dexterity_saving_throws = -1000;
		charisma_saving_throws = -1000;
		intelligence_saving_throws = -1000;
		wisdom_saving_throws = -1000;
		constitution_saving_throws = -1000;
		armor_class = -1000;
		gold_pieces = -1000;
		silver_pieces = -1000;
		copper_pieces = -1000;
		character_age = -1000;
		character_height = "";
		character_weight = "";
		character_gender = "";
		character_appearance = "";
		character_backstory = "";
		character_personaility_traits = "";
		character_ideals = "";
		character_bonds = "";
		character_flaws = "";
		character_alignment = "";

		character_skills = new ArrayList<String>();

	    newSkills = true;
		
		character_ability = new ArrayList<String>();
		newAbilites = true;

		character_weapon = new ArrayList<String>();
		character_armor = new ArrayList<String>();
		character_gear = new ArrayList<String>();
		
		newWeapons = true;
		newArmor = true;
	    newGear = true;

	}//end initializeCharacterVariables

	/**
	 * Establish a connection with specified database.
	 * Called by Class constructor 
	 * @param database_name
	 * @param sql_username
	 * @param sql_passwd
	 * @return Return connection object.
	 * @author Devin Simpson and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	private Connection establish_connection(String database_name,
			String sql_username, String sql_passwd) {
		Connection conn = null;
		try {

			conn = DriverManager
					.getConnection("jdbc:mysql://webdev.cislabs.uncw.edu/"
							+ database_name + "?user=" + sql_username
							+ "&password=" + sql_passwd);

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
		}

		return conn;
	}// end establish_connection method

	/**
	 * Gets the character's names from the database
	 * @author Victor Mancha and Thomas Man
	 * @param conn
	 * @return list of character names
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	// Use the specified connection object to interact with the database
	public ArrayList<String> getCharacter_name() {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select character_name from DND_character;");
			ArrayList<String> characterNames = new ArrayList<String>();
			while (rset.next()) {

				characterNames.add(rset.getString(1));
			}
			return characterNames;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}

	}// end getCharacter_name method

	/**
	 * Get the current character's name
	 * @param class_name
	 * @return character_name
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getCurrentCharacter_name() {

		return character_name;

	}// end getCharacter_name method

	/**
	 * Return the labels for the character details list
	 * @author Devin Simpson 
	 * @return character detail labels
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getDetailLabels() {

		return detailLabels;

	}// end getDetailLabels method

	/**
	 * Get a list of names for classes
	 * @param class_name
	 * @return classNames
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getClass_name() {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Query 1

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select class_name from DND_class;");
			ArrayList<String> classNames = new ArrayList<String>();
			while (rset.next()) {

				classNames.add(rset.getString(1));
			}

			return classNames;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}

	}// end getClass_name method

	/**
	 * Get the class description for a certain class
	 * @param class_name
	 * @return classDescription
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getClass_description(String class_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Query 1

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select role, power_source, hit_die, description from DND_class where class_name = '"
							+ class_name + "';");

			String classDescription = "";
			while (rset.next()) {

				classDescription += "Role:\n";
				classDescription += "  " + rset.getString(1) + "\n";

				classDescription += "Power Source:\n";
				classDescription += "  " + rset.getString(2) + "\n";

				classDescription += "Hit Die:\n";
				classDescription += "  " + rset.getString(3) + "\n";

				classDescription += "Description:\n";
				classDescription += "  " + rset.getString(4) + "\n";

				classDescription += "Class Abilities:\n";
				classDescription += "  "
						+ getClass_description_abilities(class_name) + "\n";

				classDescription += "Primary Stat:\n";
				classDescription += getClass_PrimaryStat(class_name) + "\n";

				classDescription += "Secondary Stat:\n";
				classDescription += "  " + getClass_SecondaryStat(class_name)
						+ "\n";

				classDescription += "Alignment:\n";
				classDescription += "  " + getAlignment_Name(class_name) + "\n";

				classDescription += "Weapon Proficiency:\n";// weapon type
				classDescription += getWeapon_Type(class_name) + "\n";

				classDescription += "Armor Proficiency:\n";// armor type
				classDescription += getArmor_Type(class_name) + "\n";

			}
			return classDescription;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}//end catch
	}//end getClass_description method

	/**
	 * Get the description for a class' abilities
	 * @param class_name
	 * @return classDescriptionAbility
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getClass_description_abilities(String class_name) {
		// Do something with the Connection
		Statement stmt;
		try {
			stmt = conn.createStatement();

			// Execute a query - which will return a result set

			// Query 1

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select ability_name from DND_class_ability where class_name = '"
							+ class_name + "';");
			String classDescriptionAbility = "";
			while (rset.next()) {

				classDescriptionAbility += "  " + rset.getString(1) + "\n";
			}
			return classDescriptionAbility;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}//end getClass_description_abilities

	/**
	 * Get a list of the class' abilities
	 * @param class_name
	 * @return abilityNames
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getClass_ability(String class_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Query 1

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select ability_name from DND_class_ability where class_name = '"
							+ class_name + "';");
			ArrayList<String> abilityNames = new ArrayList<String>();
			while (rset.next()) {

				abilityNames.add(rset.getString(1));
			}
			return abilityNames;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}//end getClass_ability

	/**
	 * Get the primary stat names for a class
	 * @param class_name
	 * @return statName
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getClass_PrimaryStat(String class_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select stat_name from DND_class_stat where class_name = '"
							+ class_name + "' and stat_type = 'primary';");
			String statName = "";
			while (rset.next()) {

				statName += "  " + rset.getString(1) + "\n";
			}
			return statName;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getClass_Stat method

	/**
	 * Get the secondary stat names for a class
	 * @param class_name
	 * @return statName
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getClass_SecondaryStat(String class_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select stat_name from DND_class_stat where class_name = '"
							+ class_name + "' and stat_type = 'secondary';");
			String statName = "";
			while (rset.next()) {

				statName += rset.getString(1) + "\n";
			}
			return statName;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getClass_Stat method

	/**
	 * Get all of the weapon proficiencies for a class
	 * @param class_name
	 * @return weaponType
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getWeapon_Type(String class_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select weapon_type from DND_class_weapon_proficiency where class_name = '"
							+ class_name + "';");
			String weaponType = "";
			while (rset.next()) {

				weaponType += " " + rset.getString(1) + "\n";
			}

			return weaponType;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getWeapon_Type method

	/**
	 * Get all of the armor types for a class
	 * @param class_name
	 * @return armorType
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getArmor_Type(String class_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select armor_type from DND_class_armor_proficiency where class_name = '"
							+ class_name + "';");
			String armorType = "";
			while (rset.next()) {

				armorType += " " + rset.getString(1) + "\n";
			}
			return armorType;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getArmor_Type method

	/**
	 * Get all of the alignments for a class
	 * @param class_name
	 * @return alignmentName
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getAlignment_Name(String class_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select alignment_name from DND_class_recommended_alignment where class_name = '"
							+ class_name + "';");
			String alignmentName = "";
			while (rset.next()) {

				alignmentName += rset.getString(1) + "\n";
			}
			return alignmentName;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getAlignment_Name method

	/**
	 * Get all of the skills recommended for a class
	 * @param class_name
	 * @return skillRecommended
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getSkillRecommended(String class_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select skill_name from DND_class_skill_recommendation where class_name = '"
							+ class_name + "';");
			ArrayList<String> skillRecommended = new ArrayList<String>();
			while (rset.next()) {
				skillRecommended.add(rset.getString(1));
			}
			return skillRecommended;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}

	}// end getSkillRecommended method

	/**
	 * Get all of the race names in the database
	 * @return raceNames
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getRace_name() {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select race_name from DND_race;");
			ArrayList<String> raceNames = new ArrayList<String>();
			while (rset.next()) {
				raceNames.add(rset.getString(1));
			}
			return raceNames;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getRace_name method

	/**
	 * Get the race description for a certain race
	 * @param race_name
	 * @return raceDescription
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getRace_description(String race_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select speed, size, description from DND_race where race_name = '"
							+ race_name + "';");
			String raceDescription = "";

			while (rset.next()) {
				raceDescription += "Size:";
				raceDescription += "  " + rset.getString(1) + "\n";

				raceDescription += "Language:";
				raceDescription += "  " + rset.getString(2) + "\n";

				raceDescription += "Description:\n";
				raceDescription += "  " + rset.getString(3) + "\n";

				raceDescription += "Race Known Language:\n";
				raceDescription += getRace_language(race_name) + "\n";

				raceDescription += "Stat Bonus:\n";
				raceDescription += getRace_statBonus(race_name) + "\n";

				raceDescription += "Race Known Skills:\n";
				raceDescription += getRace_skills(race_name) + "\n";
			}
			return raceDescription;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}//end getRace_description

	/**
	 * Get the stat bonus for a certain race
	 * @param race_name
	 * @return statBonusDescription
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getRace_statBonus(String race_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select stat_name, stat_bonus from DND_race_stat_bonus where race_name = '"
							+ race_name + "';");
			String statBonusDescription = "";

			while (rset.next()) {
				statBonusDescription += rset.getString(1);
				statBonusDescription += " +" + rset.getString(2) + "\n";
			}
			return statBonusDescription;

		}

		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}

	}//end getRace_statBonus method

	/**
	 * Gets the languages a certain race can speak
	 * @param race_name
	 * @return raceLanguages
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getRace_language(String race_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select language_name from DND_race_language where race_name = '"
							+ race_name + "';");
			String raceLanguages = "";
			while (rset.next()) {
				raceLanguages += rset.getString(1) + "\n";
			}
			return raceLanguages;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}//end getRace_language method

	/**
	 * Get the skills that pertain to a particular race
	 * @param race_name
	 * @return skillList
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getRace_skills(String race_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select skill_name from DND_race_skill_proficiency where race_name = '"
							+ race_name + "';");
			ArrayList<String> skillList = new ArrayList<String>();
			while (rset.next()) {

				skillList.add(rset.getString(1));
			}
			return skillList;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}//end getRace_skills

	/**
	 * Gets the list of stats
	 * @return statList
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getStat_name() {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			ResultSet rset = stmt
					.executeQuery("select stat_name from DND_stat;");
			ArrayList<String> statList = new ArrayList<String>();
			while (rset.next()) {

				statList.add(rset.getString(1));
			}
			return statList;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}//end getStat_name method

	/**
	 * Gets the descriptions of stats
	 * @param StatNames
	 * @return statDescriptions
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getStat_description(ArrayList<String> StatNames) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			ArrayList<String> statDescriptions = new ArrayList<String>();
			for (String stat_name : StatNames) {
				ResultSet rset = stmt
						.executeQuery("select description from DND_stat where stat_name = '"
								+ stat_name + "';");
				while (rset.next()) {
					statDescriptions.add(rset.getString(1));
				}
			}
			return statDescriptions;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}//end getStat_description method

	/**
	 * Method lists all of the skill names.
	 * @return skillNames
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getSkill_name() {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select skill_name from DND_skill;");
			ArrayList<String> skillNames = new ArrayList<String>();
			while (rset.next()) {
				skillNames.add(rset.getString(1));
			}
			return skillNames;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}

	}// end getSkill_name method

	/**
	 * Method lists all of the skill descriptions.
	 * @return skillDescriptions
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getSkill_Description(ArrayList<String> skillNames) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set
			ArrayList<String> skillDescriptions = new ArrayList<String>();

			for (String skill_name : skillNames) {
				ResultSet rset = stmt
						.executeQuery("select description from DND_skill where skill_name = '"
								+ skill_name + "';");
				while (rset.next()) {
					skillDescriptions.add(rset.getString(1));
				}
			}
			return skillDescriptions;
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getSkill_Description method

	/**
	 * Get all of the Ability names
	 * @return abilityNames
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getAbility_name() {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			ResultSet rset = stmt
					.executeQuery("select ability_name from DND_ability;");
			ArrayList<String> abilityNames = new ArrayList<String>();
			while (rset.next()) {

				abilityNames.add(rset.getString(1));
			}
			return abilityNames;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}

	}// end getAbility_name method

	/**
	 * Get all of the Ability Descriptions
	 * @return abilityDescription
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getAbility_Description(String abilityNames) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Query 1

			String abilityDescription = "";

			ResultSet rset = stmt
					.executeQuery("select level_requirement, proficiency_bonus, features, damage, spell_slots, spell_casting_stat from DND_ability where ability_name = '"
							+ abilityNames + "';");

			while (rset.next()) {

				abilityDescription += "Level Requirement: \n";
				abilityDescription += " " + rset.getString(1) + "\n";

				abilityDescription += "Proficiency Bonus: \n";
				abilityDescription += " " + rset.getString(2) + "\n";

				abilityDescription += "Features: \n";
				abilityDescription += " " + rset.getString(3) + "\n";

				abilityDescription += "Damage: \n";
				abilityDescription += " " + rset.getString(4) + "\n";

				abilityDescription += "Spell Slots: \n";
				abilityDescription += " " + rset.getString(5) + "\n";

				abilityDescription += "Spell Casting Stat \n";
				abilityDescription += " " + rset.getString(6) + "\n";

			}
			return abilityDescription;

		}// end try
		catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getAbility_Description method

	/**
	 * Get all of the weapon names
	 * @return weaponName
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getWeapon_Name() {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select weapon_name from DND_weapon;");
			ArrayList<String> weaponName = new ArrayList<String>();
			while (rset.next()) {

				weaponName.add(rset.getString(1));
			}
			return weaponName;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getWeapon_Name method

	/**
	 * Get all of the armor names
	 * @return the list of armor names
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getArmor_Name() {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select armor_name from DND_armor;");
			ArrayList<String> armorName = new ArrayList<String>();
			while (rset.next()) {

				armorName.add(rset.getString(1));
			}
			return armorName;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getArmor_Name method

	/**
	 * Get all of the gear names
	 * @return gearName
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getGear_Name() {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			ResultSet rset = stmt
					.executeQuery("select gear_name from DND_gear;");
			ArrayList<String> gearName = new ArrayList<String>();
			while (rset.next()) {

				gearName.add(rset.getString(1));
			}
			return gearName;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getGear_Name method

	/**
	 * Get all of the Weapon Descriptions
	 * @return allWeapons
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getWeapon_Description(
			ArrayList<String> weaponNames, ArrayList<int[]> price) {
		try {

			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Query 1

			// Execute a query - which will return a result set

			ArrayList<String> allWeapons = new ArrayList<String>();

			for (int i = 0; i < weaponNames.size(); i++) {

				ResultSet rset = stmt
						.executeQuery("select weapon_type, damage, properties, cost, weight, description from DND_weapon where weapon_name = '"
								+ weaponNames.get(i) + "';");

				while (rset.next()) {
					String weaponDescription = "";

					weaponDescription += "Weapon Type: \n";

					weaponDescription += " " + rset.getString(1) + "\n";

					weaponDescription += "Damage: \n";

					weaponDescription += " " + rset.getString(2) + "\n";

					weaponDescription += "Properties: \n";

					weaponDescription += " " + rset.getString(3) + "\n";

					weaponDescription += "Cost: \n";
					String itemPrice = rset.getString(4);

					weaponDescription += " " + itemPrice + "\n";

					boolean inGold, inSilver, inCopper;

					int[] currentCost = new int[3];

					inGold = itemPrice.contains("gp");
					inSilver = itemPrice.contains("sp");
					inCopper = itemPrice.contains("cp");

					if (inGold == true) {

						currentCost[0] = Integer
								.parseInt(itemPrice.split(" ")[0]);

					} else if (inSilver == true) {

						currentCost[1] = Integer
								.parseInt(itemPrice.split(" ")[0]);

					} else if (inCopper == true) {

						currentCost[2] = Integer
								.parseInt(itemPrice.split(" ")[0]);

					}

					price.add(currentCost);

					weaponDescription += "Weight: \n";

					weaponDescription += " " + rset.getString(5) + "\n";

					weaponDescription += "Description: \n";

					weaponDescription += " " + rset.getString(6) + "\n";

					allWeapons.add(weaponDescription);

				}// end while loop

			}
			return allWeapons;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}

	}// end getWeapon_Description method

	/**
	 * Get all of the Armor Descriptions
	 * @return allWeapons
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getArmor_Description(ArrayList<String> armorNames,
			ArrayList<int[]> price) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Query 1

			// Execute a query - which will return a result set

			ArrayList<String> allWeapons = new ArrayList<String>();

			for (int i = 0; i < armorNames.size(); i++) {

				ResultSet rset = stmt
						.executeQuery("select armor_class, armor_type, stealth, cost, weight, description from DND_armor where armor_name = '"
								+ armorNames.get(i) + "';");

				while (rset.next()) {
					String armorDescription = "";
					armorDescription += "Armor Class: \n";

					armorDescription += " " + rset.getString(1) + "\n";

					armorDescription += "Armor Type: \n";

					armorDescription += " " + rset.getString(2) + "\n";

					armorDescription += "Stealth: \n";

					armorDescription += " " + rset.getString(3) + "\n";

					armorDescription += "Cost: \n";
					String itemPrice = rset.getString(4);

					armorDescription += " " + itemPrice + "\n";

					boolean inGold, inSilver, inCopper;

					int[] currentCost = new int[3];

					inGold = itemPrice.contains("gp");
					inSilver = itemPrice.contains("sp");
					inCopper = itemPrice.contains("cp");

					if (inGold == true) {

						currentCost[0] = Integer
								.parseInt(itemPrice.split(" ")[0]);

					} else if (inSilver == true) {

						currentCost[1] = Integer
								.parseInt(itemPrice.split(" ")[0]);

					} else if (inCopper == true) {

						currentCost[2] = Integer
								.parseInt(itemPrice.split(" ")[0]);

					}

					price.add(currentCost);

					armorDescription += "Weight: \n";

					armorDescription += " " + rset.getString(5) + "\n";

					armorDescription += "Description: \n";

					armorDescription += " " + rset.getString(6) + "\n";

					allWeapons.add(armorDescription);
				}
			}
			return allWeapons;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getArmor_Description method

	/**
	 * Get all of the Gear Descriptions
	 * @return allGear
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getGear_Description(ArrayList<String> gearNames,
			ArrayList<int[]> price) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Query 1

			// Execute a query - which will return a result set

			ArrayList<String> allGear = new ArrayList<String>();

			for (int i = 0; i < gearNames.size(); i++) {

				ResultSet rset = stmt
						.executeQuery("select cost, weight, description from DND_gear where gear_name = '"
								+ gearNames.get(i) + "';");

				while (rset.next()) {

					String gearDescription = "";

					gearDescription += "Cost: \n";
					String itemPrice = rset.getString(1);

					gearDescription += " " + itemPrice + "\n";

					boolean inGold, inSilver, inCopper;

					int[] currentCost = new int[3];

					inGold = itemPrice.contains("gp");
					inSilver = itemPrice.contains("sp");
					inCopper = itemPrice.contains("cp");

					if (inGold == true) {

						currentCost[0] = Integer
								.parseInt(itemPrice.split(" ")[0]);

					} else if (inSilver == true) {

						currentCost[1] = Integer
								.parseInt(itemPrice.split(" ")[0]);

					} else if (inCopper == true) {

						currentCost[2] = Integer
								.parseInt(itemPrice.split(" ")[0]);

					}

					price.add(currentCost);

					gearDescription += "Weight: \n";

					gearDescription += " " + rset.getString(2) + "\n";

					gearDescription += "Description: \n";

					gearDescription += " " + rset.getString(3) + "\n";

					allGear.add(gearDescription);
				}
			}

			return allGear;
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getGear_Description method

	/**
	 * Get the Gold Dice for selected class
	 * @return gold_dice
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getGold_Dice(String class_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Query 1

			ResultSet rset = stmt
					.executeQuery("select gold_die from DND_class where class_name = '"
							+ class_name + "';");
			String gold_dice = "";
			while (rset.next()) {

				gold_dice += rset.getString(1);
			}
			return gold_dice;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getGold_dice method

	/**
	 * Get the Hit Dice for selected class
	 * @return hit_dice
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getHit_Dice(String class_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Query 1

			ResultSet rset = stmt
					.executeQuery("select hit_die from DND_class where class_name = '"
							+ class_name + "';");
			String hit_dice = "";
			while (rset.next()) {

				hit_dice += rset.getString(1);
			}
			return hit_dice;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getHit_dice method

	/**
	 * Get the All Dice
	 * @return dice
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getAll_Dice() {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Query 1

			ResultSet rset = stmt.executeQuery("select dice from DND_dice;");
			ArrayList<String> dice = new ArrayList();
			while (rset.next()) {

				dice.add(rset.getString(1));
			}
			return dice;

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
			return null;
		}
	}// end getAll_dice method

	/**
	 * Sets the selected_class (from ControlPanel_Class) to String
	 * character_class
	 * @author Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void setCharacter_Class(String selected_class) {
		character_class = selected_class;

	}// end setCharacter_Class method

	/**
	 * Sets the selected_race (from ControlPanel_race) to String character_race
	 * @author Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void setCharacter_Race(String selected_race) {

		character_race = selected_race;

	}// end setCharacter_Race method

	/**
	 * Sets the generated stats (from ControlPanel_Stat) to the six int
	 * variables
	 * @author Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void setCharacter_Stats(int strength, int dexterity, int charisma,
			int intelligence, int wisdom, int constitution) {
		strength_score = strength;
		dexterity_score = dexterity;
		charisma_score = charisma;
		intelligence_score = intelligence;
		wisdom_score = wisdom;
		constitution_score = constitution;
	}// end setCharacter_Stats method

	/**
	 * Sets the character details (from ControlPanel_Details) to their
	 * corresponding variables
	 * @author Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void setCharacter_Details(String characterName, int characterLevel,
			int starting_health, int armorClass, int strength, int dexterity,
			int charisma, int intelligence, int wisdom, int constitution,
			int age, String height, String weight, String gender,
			String appearance, String backstory, String personality_traits,
			String ideals, String bonds, String flaws, String alignment) {
		character_name = characterName;
		character_level = characterLevel;
		health = starting_health;
		armor_class = armorClass;
		strength_saving_throws = strength;
		dexterity_saving_throws = dexterity;
		charisma_saving_throws = charisma;
		intelligence_saving_throws = intelligence;
		wisdom_saving_throws = wisdom;
		constitution_saving_throws = constitution;
		character_age = age;
		character_height = height;
		character_weight = weight;
		character_gender = gender;
		character_appearance = appearance;
		character_backstory = backstory;
		character_personaility_traits = personality_traits;
		character_ideals = ideals;
		character_bonds = bonds;
		character_flaws = flaws;
		character_alignment = alignment;
	}// end setCharacter_Details method

	/**
	 * Sets the selected skills (from ControlPanel_Skill) to the String
	 * ArrayList
	 * @author Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void setCharacter_skills(ArrayList<String> selected_skills) {

		System.out.println("DO NOT SHOW UP");
		
		character_skills = selected_skills;
		newSkills = false;
	}// end setCharacter_skills method

	/**
	 * Sets the selected abilities (from ControlPanel_Ability) to the String
	 * ArrayList
	 * @author Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void setCharacter_ability(ArrayList<String> selected_ability) {
		character_ability = selected_ability;
		newAbilites = false;
		
		System.out.println("SDASDASDFSDFGGGGGGGGGGGGGGGGGGGGGGGG");
		
	}// end setCharacter_ability method

	/**
	 * Sets the selected Equipment (from ControlPanel_Equipment) to the String ArrayList
	 * @param selected_weapon
	 * @param selected_armor
	 * @param selected_gear
	 * @param goldPieces
	 * @param silverPieces
	 * @param copperPieces
	 * @author Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void setCharacter_Equipment(ArrayList<String> selected_weapon,
			ArrayList<String> selected_armor, ArrayList<String> selected_gear,
			int goldPieces, int silverPieces, int copperPieces) {

		gold_pieces = goldPieces;
		silver_pieces = silverPieces;
		copper_pieces = copperPieces;

		character_weapon = selected_weapon;
		character_armor = selected_armor;
		character_gear = selected_gear;
		
		newWeapons = false;
		newArmor = false;
		newGear = false;
		
		System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTT " + character_gear);
		
	}// end setCharacter_Equipment method

	/**
	 * Sets the character to be edited
	 * @param c_name
	 * @author Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void setEditCharacter(String c_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			character_name = c_name;
			
			ResultSet rset = stmt
					.executeQuery("select * from DND_character where character_name = '"
							+ character_name + "';");
			while (rset.next()) {

				character_race = rset.getString(2);
				character_class = rset.getString(3);
				strength_score = rset.getInt(4);
				dexterity_score = rset.getInt(5);
				charisma_score = rset.getInt(6);
				intelligence_score = rset.getInt(7);
				wisdom_score = rset.getInt(8);
				constitution_score = rset.getInt(9);
				character_level = rset.getInt(10);
				health = rset.getInt(11);
				strength_saving_throws = rset.getInt(12);
				dexterity_saving_throws = rset.getInt(13);
				charisma_saving_throws = rset.getInt(14);
				intelligence_saving_throws = rset.getInt(15);
				wisdom_saving_throws = rset.getInt(16);
				constitution_saving_throws = rset.getInt(17);
				armor_class = rset.getInt(18);
				gold_pieces = rset.getInt(19);
				silver_pieces = rset.getInt(20);
				copper_pieces = rset.getInt(21);
				character_personaility_traits = rset.getString(22);
				character_ideals = rset.getString(23);
				character_bonds = rset.getString(24);
				character_flaws = rset.getString(25);
				character_appearance = rset.getString(26);
				character_backstory = rset.getString(27);
				character_age = rset.getInt(28);
				character_height = rset.getString(29);
				character_weight = rset.getString(30);
				character_gender = rset.getString(31);
				character_alignment = rset.getString(32);
			}

			rset = stmt
					.executeQuery("select skill_name from DND_character_skill where character_name = '"
							+ character_name + "';");
			character_skills = new ArrayList<String>();
			while (rset.next()) {

				character_skills.add(rset.getString(1));
				
			}

				
			newSkills = false;
			rset = stmt
					.executeQuery("select ability_name from DND_character_ability where character_name = '"
							+ character_name + "';");
			character_ability = new ArrayList<String>();
			while (rset.next()) {

				character_ability.add(rset.getString(1));
			}
			
			System.out.print(" character_ability "+character_ability + "THE NAME " + character_name);
			
			
			newAbilites = false;
			rset = stmt
					.executeQuery("select weapon_name from DND_character_weapon  where character_name = '"
							+ character_name + "';");
			character_weapon = new ArrayList<String>();
			while (rset.next()) {

				character_weapon.add(rset.getString(1));
			}

			newWeapons = false;
			rset = stmt
					.executeQuery("select armor_name from DND_character_armor  where character_name = '"
							+ character_name + "';");
			character_armor = new ArrayList<String>();
			while (rset.next()) {

				character_armor.add(rset.getString(1));
			}
			newArmor = false;
			
			rset = stmt
					.executeQuery("select gear_name from DND_character_gear  where character_name = '"
							+ character_name + "';");
			character_gear = new ArrayList<String>();
			while (rset.next()) {

				character_gear.add(rset.getString(1));
			}
			newGear = false;
			
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());

		}
	}// end method

	/**
	 * Gets all the Character info and sends it to the database Also updates the
	 * user chanages
	 * @author Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void AddNewCharacter(String mode) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute an insert - which is in the result set

			System.out.println("Character name " + character_name + " mode " + mode);
			
			if (mode == "new") {
				boolean rset = stmt
						.execute("insert into DND_character values('"
								+ character_name + "','" + character_race
								+ "','" + character_class + "','"
								+ strength_score + "','" + dexterity_score
								+ "','" + charisma_score + "','"
								+ intelligence_score + "','" + wisdom_score
								+ "','" + constitution_score + "','"
								+ character_level + "','" + health + "','"
								+ strength_saving_throws + "','"
								+ dexterity_saving_throws + "','"
								+ charisma_saving_throws + "','"
								+ intelligence_saving_throws + "','"
								+ wisdom_saving_throws + "','"
								+ constitution_saving_throws + "','"
								+ armor_class + "','" + gold_pieces + "','"
								+ silver_pieces + "','" + copper_pieces + "','"
								+ character_personaility_traits + "','"
								+ character_ideals + "','" + character_bonds
								+ "','" + character_flaws + "','"
								+ character_appearance + "','"
								+ character_backstory + "','" + character_age
								+ "','" + character_height + "','"
								+ character_weight + "','" + character_gender
								+ "','" + character_alignment + "')");

			}

			if (mode == "edit") {
				
				
				System.out.println("Character name " + character_name);
				
				ResultSet rsets = stmt.executeQuery("select * from DND_character_skill where character_name = '" + character_name+ "';" );

						
				int rsetDD = stmt.executeUpdate("delete from DND_character_skill where character_name = '"
							+ character_name + "';");
				
				System.out.println("delete from DND_character_skill where character_name = '"
						+ character_name + "';");
				
				stmt.executeUpdate("delete from DND_character_ability where character_name = '"
							+ character_name + "';");
				stmt.executeUpdate("delete from DND_character_weapon where character_name = '"
							+ character_name + "';");
				stmt.executeUpdate("delete from DND_character_armor where character_name = '"
							+ character_name + "';");
				stmt.executeUpdate("delete from DND_character_gear where character_name = '"
							+ character_name + "';");

				System.out.println("did this work " + rsetDD);
				
				int rsetD = stmt.executeUpdate("delete from DND_character where character_name = '"
						+ character_name + "';");

				System.out.println("did it work " + rsetD);
				
				boolean rset = stmt
						.execute("insert into DND_character values('"
								+ character_name + "','" + character_race
								+ "','" + character_class + "','"
								+ strength_score + "','" + dexterity_score
								+ "','" + charisma_score + "','"
								+ intelligence_score + "','" + wisdom_score
								+ "','" + constitution_score + "','"
								+ character_level + "','" + health + "','"
								+ strength_saving_throws + "','"
								+ dexterity_saving_throws + "','"
								+ charisma_saving_throws + "','"
								+ intelligence_saving_throws + "','"
								+ wisdom_saving_throws + "','"
								+ constitution_saving_throws + "','"
								+ armor_class + "','" + gold_pieces + "','"
								+ silver_pieces + "','" + copper_pieces + "','"
								+ character_personaility_traits + "','"
								+ character_ideals + "','" + character_bonds
								+ "','" + character_flaws + "','"
								+ character_appearance + "','"
								+ character_backstory + "','" + character_age
								+ "','" + character_height + "','"
								+ character_weight + "','" + character_gender
								+ "','" + character_alignment + "')");

				System.out.println("did it work 2" + rset);
			}

			
			
			for (int i = 0; i < character_skills.size(); i++) {
				stmt.execute("insert into DND_character_skill values('"
						+ character_name + "','" + character_skills.get(i)
						+ "')");
			}

			for (int i = 0; i < character_ability.size(); i++) {
				stmt.execute("insert into DND_character_ability values('"
						+ character_name + "','" + character_ability.get(i)
						+ "')");
			}

			for (int i = 0; i < character_weapon.size(); i++) {
				stmt.execute("insert into DND_character_weapon values('"
						+ character_name + "','" + character_weapon.get(i)
						+ "')");
			}

			for (int i = 0; i < character_armor.size(); i++) {
				stmt.execute("insert into DND_character_armor values('"
						+ character_name + "','" + character_armor.get(i)
						+ "')");
			}

			System.out.println("character_gear " + character_gear);
			
			for (int i = 0; i < character_gear.size(); i++) {
				stmt.execute("insert into DND_character_gear values('"
						+ character_name + "','" + character_gear.get(i) + "')");
			}
			stmt.close();

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
		}
	}// end AddNewCharacter method

	/**
	 * Set Class info for the database insertion
	 * @author Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void setClassInfo(String class_name, String role,
			String power_source, String description, String gold_die) {
		newClass_name = class_name;
		class_role = role;
		class_power_source = power_source;
		class_description = description;
		class_gold_die = gold_die;
	}// end AddNewCharacter method

	/**
	 * Set Race info for the database insertion
	 * @author Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void setRaceInfo(String race_name, int speed, String size,
			String description) {
		newRace_name = race_name;
		race_speed = speed;
		race_size = size;
		race_description = description;
	}// end AddNewCharacter method

	/**
	 * Set Class info for the database insertion
	 * @author Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void setSkillInfo(String skill_name, String stat, String description) {
		newSkill_name = skill_name;
		skill_modifier = stat;
		skill_description = description;

	}// end AddNewCharacter method

	/**
	 * Set Class info for the database insertion
	 * @author Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void setAbilitiesInfo(String ability_name, String level_requirement,
			String proficiency_bonus, String features, String damage,
			String spell_slots, String spell_casting_stat) {
		newAbility_name = ability_name;
		ability_level = level_requirement;
		ability_proficiency_bonus = proficiency_bonus;
		ability_features = features;
		ability_damage = damage;
		ability_slots = spell_slots;
		ability_casting_stat = spell_casting_stat;
	}// end AddNewCharacter method

	/**
	 * Allows the user to change the name of their character
	 * @param Oldname
	 * @param Newname
	 * @author Devin Simpson and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void ChangeNames(String Oldname, String Newname) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			boolean rset = stmt
					.execute("update DND_character set character_name = '"
							+ Newname + "'where character_name = '" + Oldname
							+ "';");

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
		}

	}//end ChangeNames

	/**
	 * Get Class info from character tables
	 * @return character_class
	 * @author Devin Simpson and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getCharacter_class_name(String character_name) {

		if (character_class.equals("")) {
			try {
				// Do something with the Connection
				Statement stmt = conn.createStatement();

				// Execute a query - which will return a result set

				ResultSet rset = stmt
						.executeQuery("select classes from DND_character where character_name = '"
								+ character_name + "';");
				String className = "";
				while (rset.next()) {

					className = rset.getString(1);
				}
				character_class = className;

			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState:     " + e.getSQLState());
				System.out.println("VendorError:  " + e.getErrorCode());
				return null;
			}
		}
		return character_class;
	}// end getCharacter_class_name method

	/**
	 * Get race info from character tables
	 * @return character_race
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public String getCharacter_race_name(String character_name) {

		if (character_race.equals("")) {
			try {
				// Do something with the Connection
				Statement stmt = conn.createStatement();

				// Query 1

				// Execute a query - which will return a result set

				ResultSet rset = stmt
						.executeQuery("select race from DND_character where character_name = '"
								+ character_name + "';");
				String raceName = "";
				while (rset.next()) {

					raceName = rset.getString(1);
				}
				character_race = raceName;

			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState:     " + e.getSQLState());
				System.out.println("VendorError:  " + e.getErrorCode());
				return null;
			}
		}
		return character_race;
	}// end getCharacter_race_name method

	/**
	 * Get stats info from character tables
	 * @return statName
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<Integer> getCharacter_stats(String character_name) {

		ArrayList<Integer> statName = new ArrayList<Integer>();

		if (strength_score == -1000 || dexterity_score == -1000
				|| charisma_score == -1000 || intelligence_score == -1000
				|| wisdom_score == -1000 || constitution_score == -1000) {
			try {
				// Do something with the Connection
				Statement stmt = conn.createStatement();

				ResultSet rset = stmt
						.executeQuery("select charisma_score, constitution_score, dexterity_score, intelligence_score, strength_score, wisdom_score from DND_character where character_name = '"
								+ character_name + "';");

				while (rset.next()) {

					charisma_score = rset.getInt(1);
					constitution_score = rset.getInt(2);
					dexterity_score = rset.getInt(3);
					intelligence_score = rset.getInt(4);
					strength_score = rset.getInt(5);
					wisdom_score = rset.getInt(6);
				}

			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState:     " + e.getSQLState());
				System.out.println("VendorError:  " + e.getErrorCode());
				return null;
			}
		}
		statName.add(charisma_score);
		statName.add(constitution_score);
		statName.add(dexterity_score);
		statName.add(intelligence_score);
		statName.add(strength_score);
		statName.add(wisdom_score);

		return statName;

	}// end method

	/**
	 * Get details info from character tables
	 * @return details
	 * @author Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getCharacter_details(String character_name) {

		ArrayList<String> details = new ArrayList<String>();

		if (character_name == "" || character_level == -1000
				|| health == -1000 || strength_saving_throws == -1000
				|| dexterity_saving_throws == -1000
				|| charisma_saving_throws == -1000
				|| intelligence_saving_throws == -1000
				|| wisdom_saving_throws == -1000
				|| constitution_saving_throws == -1000 || armor_class == -1000
				|| gold_pieces == -1000 || silver_pieces == -1000
				|| copper_pieces == -1000 || character_age == -1000
				|| character_height == "" || character_weight == ""
				|| character_gender == "" || character_appearance == ""
				|| character_backstory == ""
				|| character_personaility_traits == ""
				|| character_ideals == "" || character_bonds == ""
				|| character_flaws == "" || character_alignment == "") {

			try {
				// Do something with the Connection
				Statement stmt = conn.createStatement();

				ResultSet rset = stmt
						.executeQuery("select character_name, health, armor_class, gold_pieces, silver_pieces, copper_pieces, charisma_saving_throws, constitution_saving_throws, dexterity_saving_throws, intelligence_saving_throws, stength_saving_throws, wisdom_saving_throws, age, height, weight, appearance, backstory, personality_traits, ideals, bonds, flaws from DND_character where character_name = '"
								+ character_name + "';");

				while (rset.next()) {

					details.add(rset.getString(1));
					details.add(rset.getString(2));
					details.add(rset.getString(3));
					details.add(rset.getString(4));
					details.add(rset.getString(5));
					details.add(rset.getString(6));
					details.add(rset.getString(7));
					details.add(rset.getString(8));
					details.add(rset.getString(9));
					details.add(rset.getString(10));
					details.add(rset.getString(11));
					details.add(rset.getString(12));
					details.add(rset.getString(13));
					details.add(rset.getString(14));
					details.add(rset.getString(15));
					details.add(rset.getString(16));
					details.add(rset.getString(17));
					details.add(rset.getString(18));
					details.add(rset.getString(19));
					details.add(rset.getString(20));
					details.add(rset.getString(21));

				}

				return details;

			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState:     " + e.getSQLState());
				System.out.println("VendorError:  " + e.getErrorCode());
				return null;
			}
		} else {

			details.add(character_name);
			details.add(Integer.toString(health));
			details.add(Integer.toString(armor_class));
			details.add(Integer.toString(gold_pieces));
			details.add(Integer.toString(silver_pieces));
			details.add(Integer.toString(copper_pieces));
			details.add(Integer.toString(charisma_saving_throws));
			details.add(Integer.toString(constitution_saving_throws));
			details.add(Integer.toString(dexterity_saving_throws));
			details.add(Integer.toString(intelligence_saving_throws));
			details.add(Integer.toString(strength_saving_throws));
			details.add(Integer.toString(wisdom_saving_throws));
			details.add(Integer.toString(character_age));
			details.add(character_height);
			details.add(character_weight);
			details.add(character_appearance);
			details.add(character_backstory);
			details.add(character_personaility_traits);
			details.add(character_ideals);
			details.add(character_bonds);
			details.add(character_flaws);

		}

		return details;
	}//end getCharacter_details method

	/**
	 * Get level and gender info from character tables
	 * @return levGenAlign
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getCharacter_levelGenderAlignment(
			String character_name) {

		ArrayList<String> levGenAlign = new ArrayList<String>();

		if (character_level == -1000 || character_alignment == ""
				|| character_gender == "") {

			try {
				// Do something with the Connection
				Statement stmt = conn.createStatement();

				// Execute a query - which will return a result set

				ResultSet rset = stmt
						.executeQuery("select character_level, gender ,alignment from DND_character where character_name = '"
								+ character_name + "';");

				while (rset.next()) {

					levGenAlign.add(rset.getString(1));
					levGenAlign.add(rset.getString(2));
					levGenAlign.add(rset.getString(3));

				}
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState:     " + e.getSQLState());
				System.out.println("VendorError:  " + e.getErrorCode());
				return null;
			}

		} else {

			levGenAlign.add(Integer.toString(character_level));
			levGenAlign.add(character_gender);
			levGenAlign.add(character_alignment);

		}

		return levGenAlign;
	}// end getCharacter_levelGenderAlignment method

	/**
	 * Get skills info from character tables
	 * @return character_skills
	 * @author Victor Mancha
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getCharacter_skills(String character_name) {

		System.out.println("WHAT IS IT " + newSkills);
		
		if (newSkills == true) {

			try {
				// Do something with the Connection
				Statement stmt = conn.createStatement();

				ResultSet rset = stmt
						.executeQuery("select skill_name from DND_character_skill where character_name = '"
								+ character_name + "';");
				ArrayList<String> skillName = new ArrayList<String>();
				while (rset.next()) {

					skillName.add(rset.getString(1));

					character_skills = skillName;
				}
				
				newSkills = false;
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState:     " + e.getSQLState());
				System.out.println("VendorError:  " + e.getErrorCode());
				return null;
			}
		}

		return character_skills;

	}// end getCharacter_skills method

	/**
	 * Get ability info from character tables
	 * @return character_ability
	 * @author Victor Mancha
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getCharacter_ability(String character_name) {

		
		
		if (newAbilites == true) {
			try {
				// Do something with the Connection
				Statement stmt = conn.createStatement();

				// Execute a query - which will return a result set

				ResultSet rset = stmt
						.executeQuery("select ability_name from DND_character_ability where character_name = '"
								+ character_name + "';");
				ArrayList<String> abilityName = new ArrayList<String>();
				while (rset.next()) {

					abilityName.add(rset.getString(1));

				}
				character_ability = abilityName;
				newAbilites = false;
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState:     " + e.getSQLState());
				System.out.println("VendorError:  " + e.getErrorCode());
				return null;
			}
		}

		
		System.out.print("cvcv cv " + character_ability);
		
		return character_ability;

	}// end getCharacter_ability method

	/**
	 * Get weapon info from character tables
	 * @return character_weapon
	 * @author Victor Mancha
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getCharacter_weapon(String character_name) {

		if (newWeapons == true) {

			try {
				// Do something with the Connection
				Statement stmt = conn.createStatement();

				// Execute a query - which will return a result set

				ResultSet rset = stmt
						.executeQuery("select weapon_name from DND_character_weapon where character_name = '"
								+ character_name + "';");
				ArrayList<String> weaponName = new ArrayList<String>();
				while (rset.next()) {

					weaponName.add(rset.getString(1));

				}
				character_weapon = weaponName;
				newWeapons = false;
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState:     " + e.getSQLState());
				System.out.println("VendorError:  " + e.getErrorCode());
				return null;
			}
		}

		return character_weapon;
	}// end getCharacter_weapon method

	/**
	 * Get armor info from character tables
	 * @return character_armor
	 * @author Victor Mancha
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getCharacter_armor(String character_name) {

		if (newArmor == true) {

			try {
				// Do something with the Connection
				Statement stmt = conn.createStatement();

				// Query 1

				// Execute a query - which will return a result set

				ResultSet rset = stmt
						.executeQuery("select armor_name from DND_character_armor where character_name = '"
								+ character_name + "';");
				ArrayList<String> armorName = new ArrayList<String>();
				while (rset.next()) {
					armorName.add(rset.getString(1));
				}

				character_armor = armorName;
				newArmor = false;
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState:     " + e.getSQLState());
				System.out.println("VendorError:  " + e.getErrorCode());
				return null;
			}
		}

		return character_armor;
	}// end getCharacter_armor method

	/**
	 * Get gear info from character tables
	 * @return character_gear
	 * @author Victor Mancha
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public ArrayList<String> getCharacter_gear(String character_name) {

		
		
		if (newGear == true) {
			try {
				// Do something with the Connection
				Statement stmt = conn.createStatement();

				ResultSet rset = stmt
						.executeQuery("select gear_name from DND_character_gear where character_name = '"
								+ character_name + "';");
				ArrayList<String> gearName = new ArrayList<String>();
				while (rset.next()) {

					gearName.add(rset.getString(1));
				}
				character_gear = gearName;
				newGear = false;
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState:     " + e.getSQLState());
				System.out.println("VendorError:  " + e.getErrorCode());
				return null;
			}
		}

		return character_gear;

	}// end getCharacter_gear method

	/**
	 * Add new class to the database
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void addClass(String class_name, String role, String power_source,
			String hit_die, String description, String gold_die,
			ArrayList<String> primary_stats, String secondary_stats,
			String alignment, ArrayList<String> fightType,
			ArrayList<String> armorType) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			boolean rset = stmt.execute("insert into DND_class values('"
					+ class_name + "', '" + role + "', '" + power_source
					+ "', '" + hit_die + "', '" + description + "', '"
					+ gold_die + "');");

			for (int i = 0; i < primary_stats.size(); i++) {
				rset = stmt.execute("insert into DND_class_stat values('"
						+ class_name + "', '" + primary_stats.get(i)
						+ "', 'primary');");
			}

			rset = stmt.execute("insert into DND_class_stat values('"
					+ class_name + "', '" + secondary_stats
					+ "', 'secondary');");
			rset = stmt
					.execute("insert into DND_class_recommended_alignment values('"
							+ class_name + "', '" + alignment + "');");
			for (int i = 0; i < fightType.size(); i++) {
				rset = stmt
						.execute("insert into DND_class_weapon_proficiency values('"
								+ class_name
								+ "', '"
								+ fightType.get(i)
								+ "');");
			}
			for (int i = 0; i < armorType.size(); i++) {
				rset = stmt
						.execute("insert into DND_class_armor_proficiency values('"
								+ class_name
								+ "', '"
								+ armorType.get(i)
								+ "');");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
		}

	}// end addClass method

	/**
	 * Add new race to the database
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void addRace(String race_name, String speed, String size,
			String description, String skillOne, String skillTwo,
			String language, ArrayList<String> stat_bonuses) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			boolean rset = stmt.execute("insert into DND_race values('"
					+ race_name + "', '" + speed + "', '" + size + "', '"
					+ description + "');");

			rset = stmt.execute("insert into DND_race_language values('"
					+ race_name + "', '" + language + "');");

			rset = stmt.execute("insert into DND_race_language values('"
					+ race_name + "', 'Common');");

			rset = stmt
					.execute("insert into DND_race_skill_proficiency values('"
							+ race_name + "', '" + skillOne + "');");

			if (!skillTwo.equals("None")) {
				rset = stmt
						.execute("insert into DND_race_skill_proficiency values('"
								+ race_name + "', '" + skillTwo + "');");
			}

			for (int i = 0; i < stat_bonuses.size(); i++) {
				rset = stmt
						.execute("insert into DND_race_stat_bonus values('"
								+ race_name + "', '" + stat_bonuses.get(i)
								+ "', '1');");
			}

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
		}

	}// end method

	/**
	 * Add New ability to the database
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void addAbility(String ability_name, String level,
			String stat_bonus, String features, String damage,
			String spell_slots, String spell_casting_stat, String class_name) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			boolean rset = stmt.execute("insert into DND_ability values('"
					+ ability_name + "', '" + level + "', '" + stat_bonus
					+ "', '" + features + "', '" + damage + "', '"
					+ spell_slots + "', '" + spell_casting_stat + "');");
			rset = stmt.execute("insert into DND_class_ability values('"
					+ class_name + "', '" + ability_name + "');");

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
		}

	}// end method

	/**
	 * Add new skill to the database
	 * @author Victor Mancha and Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void addSkill(String skill_name, String statMod, String description,
			ArrayList<String> recommendedClass) {
		try {
			// Do something with the Connection
			Statement stmt = conn.createStatement();

			// Execute a query - which will return a result set

			boolean rset = stmt.execute("insert into DND_skill values('"
					+ skill_name + "', '" + statMod + "', '" + description
					+ "');");

			for (int i = 0; i < recommendedClass.size(); i++) {
				rset = stmt
						.execute("insert into DND_class_skill_recommendation values('"
								+ recommendedClass.get(i)
								+ "','"
								+ skill_name
								+ "');");
			}

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState:     " + e.getSQLState());
			System.out.println("VendorError:  " + e.getErrorCode());
		}
	}// end method

	/**
	 * This method closes the connection between the sql database and the java GUI
	 * @author Thomas Man
	 * @version December 8th, 2014 (most recent version) (Created 9/23/2014)
	 */
	public void close_connection() {

		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// end method

}