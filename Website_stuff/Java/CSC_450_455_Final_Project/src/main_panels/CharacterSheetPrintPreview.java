package main_panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * CharacterSheetPrintPreview:
 * This class is used for the creation of all of the character attribute panels
 * This class is one of 7 classes of the main panels used in the creation of a new D&D character.
 * @author Victor Mancha, Thomas Man, Devin Simpson
 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
 */
public class CharacterSheetPrintPreview extends JPanel implements Printable {

	// keep a reference to all the attributes of the character to be displayed
	// in the character sheet
	private String className, raceName, characterName;
	private ArrayList<Integer> statValues;
	private ArrayList<String> textInputDetails, playerSkills, playerAbilites,
			playerWeapons, playerArmor, playerGear, detailLabels,
			levelGenderAlignment;

	// define the width and height of the character sheet in terms of pixels
	// this will have to be scaled to fit on a typical piece of printer paper
	private int paperWidth = 850, paperHeight = 1100;

	/**
	 * 
	 * @author Devin Simpson
	 * 
	 *         Constructor for the CharacterSheetPrintPreview where all the
	 *         attributes of a character that the user has set must be passed in
	 *         so they may be displayed and printed
	 * 
	 * @param chName
	 * @param clName
	 * @param rName
	 * @param stValues
	 * @param details
	 * @param levGenAl
	 * @param dLabels
	 * @param pSkill
	 * @param pAbility
	 * @param pWeapon
	 * @param pArmor
	 * @param pGear
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public CharacterSheetPrintPreview(String chName, String clName,
			String rName, ArrayList<Integer> stValues,
			ArrayList<String> details, ArrayList<String> levGenAl,
			ArrayList<String> dLabels, ArrayList<String> pSkill,
			ArrayList<String> pAbility, ArrayList<String> pWeapon,
			ArrayList<String> pArmor, ArrayList<String> pGear) {

		this.setPreferredSize(new Dimension(paperWidth, paperHeight));
		this.setMaximumSize(new Dimension(paperWidth, paperHeight));

		characterName = chName;
		className = clName;
		raceName = rName;

		statValues = stValues;

		textInputDetails = details;
		levelGenderAlignment = levGenAl;
		detailLabels = dLabels;

		playerSkills = pSkill;
		playerAbilites = pAbility;

		playerWeapons = pWeapon;
		playerArmor = pArmor;
		playerGear = pGear;

		this.setBackground(Color.WHITE);

	}// end method

	/**
	 * Paints all of the different attributes for the Character Sheet
	 * Called by Print
	 * @param g
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int startX = 18, startY = 40;
		FontMetrics currentFont = g.getFontMetrics();
		int itemHoriziontalOffset = 35;

		int offsetBetweenItems = 25;

		int boxHeightFor500Characters = 247;
		int boxHeightFor1000Characters = 465;

		int fifthOfWidth = paperWidth / 5;

		// set level and gender indices, they are constant
		int levelIntex = 0;
		int genderIndex = 1;
		int alignmentIndex = 2;

		// first level: name

		// print name first
		String nameLabel = "Character Name: ";

		int nameOffset = currentFont.stringWidth(nameLabel);
		int textHeightOffset = 15;

		int nameXPlacement = (paperWidth / 2)
				- (currentFont.stringWidth(nameLabel + characterName) / 2)
				- offsetBetweenItems + 5;

		g.drawString(nameLabel, startX + nameXPlacement, startY);
		g.drawString(characterName, startX + nameXPlacement + nameOffset,
				startY);

		// second level: basic character attributes

		// print labels
		String classLabel = "Class";
		String raceLabel = "Race";
		String healthLabel = "Health";
		String armorClassLabel = "Armor Class";
		String levelLabel = "Level";

		int classOffset = currentFont.stringWidth(classLabel)
				+ itemHoriziontalOffset;
		int raceOffset = currentFont.stringWidth(raceLabel) + classOffset
				+ itemHoriziontalOffset;
		int healthOffset = currentFont.stringWidth(healthLabel) + raceOffset
				+ itemHoriziontalOffset;
		int armorClassOffset = currentFont.stringWidth(armorClassLabel)
				+ healthOffset + itemHoriziontalOffset;
		int levelOffset = currentFont.stringWidth(levelLabel)
				+ armorClassOffset + itemHoriziontalOffset;

		g.drawString(classLabel, startX, startY + offsetBetweenItems);
		g.drawString(raceLabel, startX + classOffset, startY
				+ offsetBetweenItems);
		g.drawString(healthLabel, startX + raceOffset, startY
				+ offsetBetweenItems);
		g.drawString(armorClassLabel, startX + healthOffset, startY
				+ offsetBetweenItems);
		g.drawString(levelLabel, startX + armorClassOffset, startY
				+ offsetBetweenItems);

		// print class name
		g.drawString(className, startX, startY + offsetBetweenItems
				+ textHeightOffset);

		// print race name
		g.drawString(raceName, startX + classOffset, startY
				+ offsetBetweenItems + textHeightOffset);

		// print health value
		String healthValue = "";

		for (int i = 0; i < detailLabels.size(); i++) {
			if (detailLabels.get(i).equals("health")) {
				healthValue = textInputDetails.get(i);
			}
		}
		g.drawString(healthValue, startX + raceOffset, startY
				+ offsetBetweenItems + textHeightOffset);

		// print armor class value
		String armorClassValue = "";
		for (int i = 0; i < detailLabels.size(); i++) {
			if (detailLabels.get(i).equals("armor_class")) {
				armorClassValue = textInputDetails.get(i);
			}
		}
		g.drawString(armorClassValue, startX + healthOffset, startY
				+ offsetBetweenItems + textHeightOffset);

		// print level

		String levelValue = levelGenderAlignment.get(levelIntex);
		g.drawString(levelValue, startX + armorClassOffset, startY
				+ offsetBetweenItems + textHeightOffset);

		// next level: wealth

		int wealthLabelY = startY + offsetBetweenItems + (textHeightOffset * 3);

		// print wealth labels
		String goldLabel = "Gold";
		String silverLabel = "Silver";
		String copperLabel = "Copper";

		int goldOffset = currentFont.stringWidth(goldLabel)
				+ itemHoriziontalOffset;
		int silverOffset = currentFont.stringWidth(silverLabel) + goldOffset
				+ itemHoriziontalOffset;
		int copperOffset = currentFont.stringWidth(copperLabel) + silverOffset
				+ itemHoriziontalOffset;

		int wealthXPlacement = startX + levelOffset;

		g.drawString(goldLabel, wealthXPlacement, startY + offsetBetweenItems);
		g.drawString(silverLabel, wealthXPlacement + goldOffset, startY
				+ offsetBetweenItems);
		g.drawString(copperLabel, wealthXPlacement + silverOffset, startY
				+ offsetBetweenItems);

		// print gold
		String goldValue = "";
		for (int i = 0; i < detailLabels.size(); i++) {
			if (detailLabels.get(i).equals("gold_pieces")) {
				goldValue = textInputDetails.get(i);
			}
		}
		g.drawString(goldValue, wealthXPlacement, startY + offsetBetweenItems
				+ textHeightOffset);

		// print silver
		String silverValue = "";
		for (int i = 0; i < detailLabels.size(); i++) {
			if (detailLabels.get(i).equals("silver_pieces")) {
				silverValue = textInputDetails.get(i);
			}
		}
		g.drawString(silverValue, wealthXPlacement + goldOffset, startY
				+ offsetBetweenItems + textHeightOffset);

		// print copper
		String copperValue = "";
		for (int i = 0; i < detailLabels.size(); i++) {
			if (detailLabels.get(i).equals("copper_pieces")) {
				copperValue = textInputDetails.get(i);
			}
		}
		g.drawString(copperValue, wealthXPlacement + silverOffset, startY
				+ offsetBetweenItems + textHeightOffset);

		// next level: appearance

		// print wealth labels
		String ageLabel = "Age";
		String heightLabel = "Height";
		String weightLabel = "Weight";
		String genderLabel = "Gender";

		int ageOffset = currentFont.stringWidth(ageLabel)
				+ itemHoriziontalOffset;
		int heightOffset = currentFont.stringWidth(heightLabel) + ageOffset
				+ itemHoriziontalOffset;
		int weightOffset = currentFont.stringWidth(weightLabel) + heightOffset
				+ itemHoriziontalOffset;

		int appearanceXPlacementTop = wealthXPlacement + copperOffset;

		int appearanceYPlacementTop = startY + offsetBetweenItems;

		g.drawString(ageLabel, appearanceXPlacementTop, appearanceYPlacementTop);
		g.drawString(heightLabel, appearanceXPlacementTop + ageOffset,
				appearanceYPlacementTop);
		g.drawString(weightLabel, appearanceXPlacementTop + heightOffset,
				appearanceYPlacementTop);
		g.drawString(genderLabel, appearanceXPlacementTop + weightOffset,
				appearanceYPlacementTop);

		// print age
		String ageValue = "";
		for (int i = 0; i < detailLabels.size(); i++) {
			if (detailLabels.get(i).equals("character_age")) {
				ageValue = textInputDetails.get(i);
			}
		}
		g.drawString(ageValue, appearanceXPlacementTop, appearanceYPlacementTop
				+ textHeightOffset);

		// print height
		String heightValue = "";
		for (int i = 0; i < detailLabels.size(); i++) {
			if (detailLabels.get(i).equals("character_height")) {
				heightValue = textInputDetails.get(i);
			}
		}
		g.drawString(heightValue, appearanceXPlacementTop + ageOffset,
				appearanceYPlacementTop + textHeightOffset);

		// print weight
		String weightValue = "";
		for (int i = 0; i < detailLabels.size(); i++) {
			if (detailLabels.get(i).equals("character_weight")) {
				weightValue = textInputDetails.get(i);
			}
		}
		g.drawString(weightValue, appearanceXPlacementTop + heightOffset,
				appearanceYPlacementTop + textHeightOffset);

		// print gender
		String genderValue = levelGenderAlignment.get(genderIndex);
		g.drawString(genderValue, appearanceXPlacementTop + weightOffset,
				appearanceYPlacementTop + textHeightOffset);

		// next level: detailed appearance description

		// print appearance details label
		String detailedApperanceLabel = "Detailed Apperance";

		int appearanceYPlacementBottom = appearanceYPlacementTop
				+ offsetBetweenItems + (textHeightOffset) + 5;

		int deatilApperanceLabelX = fifthOfWidth
				- currentFont.stringWidth(detailedApperanceLabel) / 2 - startX
				+ 4;
		int detailedApperanceLabelY = appearanceYPlacementBottom
				+ textHeightOffset;

		int detailedApperanceParagraphY = detailedApperanceLabelY
				+ textHeightOffset;

		g.drawString(detailedApperanceLabel, deatilApperanceLabelX,
				detailedApperanceLabelY);
		g.drawLine(
				deatilApperanceLabelX,
				detailedApperanceLabelY + 2,
				deatilApperanceLabelX
						+ currentFont.stringWidth(detailedApperanceLabel),
				detailedApperanceLabelY + 2);

		String detailedApperance = "";
		for (int i = 0; i < detailLabels.size(); i++) {
			if (detailLabels.get(i).equals("character_appearance")) {
				detailedApperance = textInputDetails.get(i);
			}
		}
		int apperanceTestLengthLimit = 250;

		int appearanceXPlacementDetails = fifthOfWidth
				- ((apperanceTestLengthLimit - 25) / 2);
		drawTextParagraph(detailedApperance, g, appearanceXPlacementDetails,
				detailedApperanceParagraphY, apperanceTestLengthLimit);

		int genderOffset = currentFont.stringWidth(genderLabel) + weightOffset
				+ itemHoriziontalOffset;

		int detailedApperanceY = appearanceYPlacementBottom - textHeightOffset;

		g.drawRect(startX, detailedApperanceY, genderOffset,
				boxHeightFor500Characters + 3);

		// next level: Stats and modifiers

		int statLabelX = startX + itemHoriziontalOffset / 2 - 3;

		// first Charisma

		String charismaLabel = "Charisma";
		int charismaY = appearanceYPlacementBottom + boxHeightFor500Characters
				+ itemHoriziontalOffset;
		g.drawString(charismaLabel, statLabelX, charismaY);

		// second Constitution

		String constitutionLabel = "Constitution";
		int constitutionY = charismaY + (textHeightOffset * 2);
		g.drawString(constitutionLabel, statLabelX, constitutionY);

		// third Dexterity

		String dexterityLabel = "Dexterity";
		int dexterityY = constitutionY + (textHeightOffset * 2);
		g.drawString(dexterityLabel, statLabelX, dexterityY);

		// fourth Intelligence

		String intelligenceLabel = "Intelligence";
		int intelligenceY = dexterityY + (textHeightOffset * 2);
		g.drawString(intelligenceLabel, statLabelX, intelligenceY);

		// fifth Strength

		String strengthLabel = "Strength";
		int strengthY = intelligenceY + (textHeightOffset * 2);
		g.drawString(strengthLabel, statLabelX, strengthY);

		// sixth Wisdom

		String wisdomLabel = "Wisdom";
		int wisdomY = strengthY + (textHeightOffset * 2);
		g.drawString(wisdomLabel, statLabelX, wisdomY);

		// add all the stat values next

		// Constitution is the longest name for stat, setting offset based on
		// Constitution string length
		int statValueX = statLabelX
				+ currentFont.stringWidth(constitutionLabel)
				+ itemHoriziontalOffset;

		String charismaValue = Integer.toString(statValues.get(0));
		String constitutionValue = Integer.toString(statValues.get(1));
		String dexterityValue = Integer.toString(statValues.get(2));
		String intelligenceValue = Integer.toString(statValues.get(3));
		String strengthValue = Integer.toString(statValues.get(4));
		String wisdomValue = Integer.toString(statValues.get(5));

		g.drawString(charismaValue, statValueX, charismaY);
		g.drawString(constitutionValue, statValueX, constitutionY);
		g.drawString(dexterityValue, statValueX, dexterityY);
		g.drawString(intelligenceValue, statValueX, intelligenceY);
		g.drawString(strengthValue, statValueX, strengthY);
		g.drawString(wisdomValue, statValueX, wisdomY);

		// now add the saving throw values next to the stat values

		int statSavingThrowValueX = statValueX + itemHoriziontalOffset * 3;

		String charismaSavingThrowValue = "";
		String constitutionSavingThrowValue = "";
		String dexteritySavingThrowValue = "";
		String intelligenceSavingThrowValue = "";
		String strengthSavingThrowValue = "";
		String wisdomSavingThrowValue = "";

		for (int i = 0; i < detailLabels.size(); i++) {
			if (detailLabels.get(i).equals("charisma_saving_throws")) {
				charismaSavingThrowValue = textInputDetails.get(i);

			} else if (detailLabels.get(i).equals("constitution_saving_throws")) {
				constitutionSavingThrowValue = textInputDetails.get(i);

			} else if (detailLabels.get(i).equals("dexterity_saving_throws")) {
				dexteritySavingThrowValue = textInputDetails.get(i);

			} else if (detailLabels.get(i).equals("intelligence_saving_throws")) {
				intelligenceSavingThrowValue = textInputDetails.get(i);

			} else if (detailLabels.get(i).equals("strength_saving_throws")) {
				strengthSavingThrowValue = textInputDetails.get(i);

			} else if (detailLabels.get(i).equals("wisdom_saving_throws")) {
				wisdomSavingThrowValue = textInputDetails.get(i);

			}
		}

		g.drawString(charismaSavingThrowValue, statSavingThrowValueX, charismaY);
		g.drawString(constitutionSavingThrowValue, statSavingThrowValueX,
				constitutionY);
		g.drawString(dexteritySavingThrowValue, statSavingThrowValueX,
				dexterityY);
		g.drawString(intelligenceSavingThrowValue, statSavingThrowValueX,
				intelligenceY);
		g.drawString(strengthSavingThrowValue, statSavingThrowValueX, strengthY);
		g.drawString(wisdomSavingThrowValue, statSavingThrowValueX, wisdomY);

		String statLabel = "Stat";
		String statValueLabel = "Value";
		String savingThrowLabel = "Saving Throw";

		g.drawString(statLabel, statLabelX, charismaY - textHeightOffset - 2);
		g.drawString(
				statValueLabel,
				(statValueX - (currentFont.stringWidth(statValueLabel) / 2) + 7),
				charismaY - textHeightOffset - 2);
		g.drawString(
				savingThrowLabel,
				(statSavingThrowValueX
						- (currentFont.stringWidth(savingThrowLabel) / 2) + 5),
				charismaY - textHeightOffset - 2);
		g.drawLine(statLabelX, charismaY - textHeightOffset + 1,
				(statSavingThrowValueX + currentFont
						.stringWidth(savingThrowLabel) / 2) + 5, charismaY
						- textHeightOffset + 1);

		int statBoxY = detailedApperanceY + boxHeightFor500Characters;

		g.drawRect(startX, statBoxY + 3, genderOffset, wisdomY - charismaY
				+ itemHoriziontalOffset * 2 + 7);

		// next level : background
		String backgroundLabel = "Background";

		int backgroundLabelX = deatilApperanceLabelX + startX;
		int backgroundLabelY = wisdomY + textHeightOffset * 4;

		int backgroundBoxY = wisdomY + textHeightOffset * 2;

		g.drawString(backgroundLabel, backgroundLabelX, backgroundLabelY);
		g.drawLine(backgroundLabelX, backgroundLabelY + 3, backgroundLabelX
				+ currentFont.stringWidth(backgroundLabel),
				backgroundLabelY + 3);
		g.drawRect(startX, backgroundBoxY, genderOffset,
				boxHeightFor1000Characters);

		// get the background text
		String backgroundText = "";

		for (int i = 0; i < detailLabels.size(); i++) {
			if (detailLabels.get(i).equals("character_backstory")) {
				backgroundText = textInputDetails.get(i);
			}
		}

		drawTextParagraph(backgroundText, g, appearanceXPlacementDetails,
				backgroundLabelY + textHeightOffset, apperanceTestLengthLimit);

		// last part of the left section is alignment

		int alginmentBoxY = backgroundBoxY + boxHeightFor1000Characters;
		int alginmentBoxHeight = textHeightOffset * 3 + 1;
		String algimentlabel = "Alignment";

		int algimentlabelX = deatilApperanceLabelX + startX;
		int algimentlabelY = alginmentBoxY + textHeightOffset + 3;

		g.drawString(algimentlabel, algimentlabelX, algimentlabelY);
		g.drawLine(algimentlabelX, algimentlabelY + 2, algimentlabelX
				+ currentFont.stringWidth(algimentlabel), algimentlabelY + 2);
		g.drawRect(startX, alginmentBoxY, genderOffset, alginmentBoxHeight);

		String alignmentValue = levelGenderAlignment.get(alignmentIndex);

		int alignmentStringX = algimentlabelX;
		int alighmentStringY = algimentlabelY + textHeightOffset;

		g.drawString(alignmentValue, alignmentStringX, alighmentStringY);

		// now move to middle, the whole middle section will be dedicated to the
		// Personality Traits, Ideals, Bonds and Flaws section

		// first personality traits

		// set up middle panel values
		int thirdOfWidth = paperWidth / 3;
		int middleofThird = thirdOfWidth + thirdOfWidth / 2;
		int middleRowBoxWarpLength = 250;
		int middleBoxLength = thirdOfWidth;

		// set label for the personality traits box
		String personalityTraitsLabel = "Personality Traits";

		// set up personality traits label coordinates
		int personalityTraitsLabelX = middleofThird - itemHoriziontalOffset;
		int personalityTraitsLabelY = wealthLabelY;

		// set up personality traits box coordinates
		int personalityTraitsBoxX = startX + genderOffset;
		int personalityTraitsBoxY = personalityTraitsLabelY - textHeightOffset;

		// set up personality traits paragraph coordinates
		int personalityTraitsParagraphX = middleofThird - itemHoriziontalOffset
				* 3 + 10;
		int personalityTraitsParagraphY = personalityTraitsLabelY
				+ textHeightOffset * 2;

		// draw the label, the box , and the underline for the personality
		// traits area
		g.drawString(personalityTraitsLabel, personalityTraitsLabelX,
				personalityTraitsLabelY + textHeightOffset);
		g.drawRect(personalityTraitsBoxX, personalityTraitsBoxY,
				middleBoxLength, boxHeightFor500Characters);
		g.drawLine(
				personalityTraitsLabelX,
				personalityTraitsLabelY + textHeightOffset + 3,
				personalityTraitsLabelX
						+ currentFont.stringWidth(personalityTraitsLabel),
				personalityTraitsLabelY + textHeightOffset + 3);

		// get the string the comprises the chracter's personality traits
		String personalityTraitsValue = "";
		for (int i = 0; i < detailLabels.size(); i++) {
			if (detailLabels.get(i).equals("character_personaility_traits")) {
				personalityTraitsValue = textInputDetails.get(i);
			}
		}

		// draw the string within the allotted personality traits area
		drawTextParagraph(personalityTraitsValue, g,
				personalityTraitsParagraphX, personalityTraitsParagraphY,
				middleRowBoxWarpLength);

		// next ideals

		String idealsLabel = "Ideals";

		int idealsBoxX = personalityTraitsBoxX;
		int idealsBoxY = personalityTraitsLabelY + boxHeightFor500Characters;

		int idealsLabelX = middleofThird + currentFont.stringWidth(idealsLabel)
				/ 2 - itemHoriziontalOffset + 12;

		// set up ideals paragraph coordinates
		int idealsParagraphX = idealsLabelX - itemHoriziontalOffset * 3 + 10;
		int idealsParagraphY = idealsBoxY + textHeightOffset * 2;

		g.drawString(idealsLabel, idealsLabelX, idealsBoxY + textHeightOffset);
		g.drawRect(idealsBoxX, idealsBoxY - textHeightOffset, middleBoxLength,
				boxHeightFor500Characters);
		g.drawLine(idealsLabelX, idealsBoxY + textHeightOffset + 3,
				idealsLabelX + currentFont.stringWidth(idealsLabel), idealsBoxY
						+ textHeightOffset + 3);

		String idealsValue = "";
		for (int i = 0; i < detailLabels.size(); i++) {
			if (detailLabels.get(i).equals("character_ideals")) {
				idealsValue = textInputDetails.get(i);
			}
		}
		drawTextParagraph(idealsValue, g, idealsParagraphX, idealsParagraphY,
				middleRowBoxWarpLength);

		// next bonds

		String bondsLabel = "Bonds";

		int bondsBoxX = personalityTraitsBoxX;
		int bondsBoxY = idealsBoxY + boxHeightFor500Characters;

		int bondsLabelX = middleofThird + currentFont.stringWidth(bondsLabel)
				/ 2 - itemHoriziontalOffset + 12;

		// set up bonds paragraph coordinates
		int bondsParagraphX = bondsLabelX - itemHoriziontalOffset * 3 + 10;
		int bondsParagraphY = bondsBoxY + textHeightOffset * 2;

		g.drawString(bondsLabel, bondsLabelX, bondsBoxY + textHeightOffset);
		g.drawRect(bondsBoxX, bondsBoxY - textHeightOffset, middleBoxLength,
				boxHeightFor500Characters);
		g.drawLine(bondsLabelX, bondsBoxY + textHeightOffset + 3, bondsLabelX
				+ currentFont.stringWidth(idealsLabel), bondsBoxY
				+ textHeightOffset + 3);

		String bondsValue = "";
		for (int i = 0; i < detailLabels.size(); i++) {
			if (detailLabels.get(i).equals("character_bonds")) {
				bondsValue = textInputDetails.get(i);
			}
		}

		drawTextParagraph(bondsValue, g, bondsParagraphX, bondsParagraphY,
				middleRowBoxWarpLength);

		// lastly flaws

		String flawsLabel = "Flaws";

		int flawsBoxX = personalityTraitsBoxX;
		int flawsBoxY = bondsBoxY + boxHeightFor500Characters;

		int flawsLabelX = middleofThird + currentFont.stringWidth(flawsLabel)
				/ 2 - itemHoriziontalOffset + 12;

		// set up flaws paragraph coordinates
		int flawsParagraphX = flawsLabelX - itemHoriziontalOffset * 3 + 10;
		int flawsParagraphY = flawsBoxY + textHeightOffset * 2;

		g.drawString(flawsLabel, flawsLabelX, flawsBoxY + textHeightOffset);
		g.drawRect(flawsBoxX, flawsBoxY - textHeightOffset, middleBoxLength,
				boxHeightFor500Characters);
		g.drawLine(flawsLabelX, flawsBoxY + textHeightOffset + 3, flawsLabelX
				+ currentFont.stringWidth(idealsLabel), flawsBoxY
				+ textHeightOffset + 3);

		String flawssValue = "";
		for (int i = 0; i < detailLabels.size(); i++) {
			if (detailLabels.get(i).equals("character_flaws")) {
				flawssValue = textInputDetails.get(i);
			}
		}
		drawTextParagraph(flawssValue, g, flawsParagraphX, flawsParagraphY,
				middleRowBoxWarpLength);

		// the right side of the character sheet is devoted to skills,
		// abilities, and equipment

		// set up right panel values
		int twoThirdsOfWidth = thirdOfWidth * 2;
		int middleofTwoThirds = twoThirdsOfWidth + thirdOfWidth / 2;
		int rightRowBoxWarpLength = 200;
		int rightBoxWidth = startX + weightOffset
				+ currentFont.stringWidth(genderLabel) - 4;

		// set up skills area first

		int skillsBoxX = personalityTraitsBoxX + middleBoxLength;
		int skillsBoxY = personalityTraitsBoxY;
		int skillBoxHeight = boxHeightFor500Characters / 2;

		String skillsLabel = "Skills";

		int skillsLabelX = middleofTwoThirds
				- currentFont.stringWidth(skillsLabel) / 2;
		int skillsLableY = skillsBoxY + textHeightOffset * 2;

		int skillsParagraphX = middleofTwoThirds - itemHoriziontalOffset * 3
				+ 10;
		int skillsParagraphY = skillsLableY + textHeightOffset * 2;

		g.drawString(skillsLabel, skillsLabelX, skillsLableY);
		g.drawLine(skillsLabelX, skillsLableY + 2,
				skillsLabelX + currentFont.stringWidth(skillsLabel),
				skillsLableY + 2);
		g.drawRect(skillsBoxX, skillsBoxY, rightBoxWidth, skillBoxHeight);

		// now add the skills into a paragraph and write it to the skills area
		String skillsValue = "";
		for (String skill : playerSkills) {
			skillsValue += skill + ", ";
		}

		// remove last ", " from string
		skillsValue = skillsValue.substring(0, skillsValue.length() - 2);

		drawTextParagraph(skillsValue, g, skillsParagraphX, skillsParagraphY,
				rightRowBoxWarpLength);

		// now set up abilities area

		int abilitiesBoxX = skillsBoxX;
		int abilitiesBoxY = skillsBoxY + skillBoxHeight;
		int abilitiesHeight = boxHeightFor500Characters;

		String abilitiesLabel = "Abilities";

		int abilitiesLabelX = middleofTwoThirds
				- currentFont.stringWidth(abilitiesLabel) / 2;
		int abilitiesLableY = abilitiesBoxY + textHeightOffset * 2;

		int abilitiesParagraphX = middleofTwoThirds - itemHoriziontalOffset * 3
				+ 10;
		int abilitiesParagraphY = abilitiesLableY + textHeightOffset * 2;

		g.drawString(abilitiesLabel, abilitiesLabelX, abilitiesLableY);
		g.drawLine(abilitiesLabelX, abilitiesLableY + 2, abilitiesLabelX
				+ currentFont.stringWidth(abilitiesLabel), abilitiesLableY + 2);
		g.drawRect(abilitiesBoxX, abilitiesBoxY, rightBoxWidth, abilitiesHeight);

		// now add the skills into a paragraph and write it to the skills area
		String abilityValue = "";
		for (String ability : playerAbilites) {
			abilityValue += ability + ", ";
		}

		// remove last ", " from string
		abilityValue = abilityValue.substring(0, abilityValue.length() - 2);

		drawTextParagraph(abilityValue, g, abilitiesParagraphX,
				abilitiesParagraphY, rightRowBoxWarpLength);

		// last character item to print is the equipment

		int equipmentBoxX = skillsBoxX;
		int equipmentBoxY = abilitiesBoxY + abilitiesHeight;
		int equipmentHeight = (flawsBoxY + boxHeightFor500Characters)
				- equipmentBoxY - itemHoriziontalOffset / 2 + 2;

		String equipmentLabel = "Equipment";

		int equipmentLabelX = middleofTwoThirds
				- currentFont.stringWidth(equipmentLabel) / 2;
		int equipmentLableY = equipmentBoxY + textHeightOffset * 2;

		int equipmentParagraphX = middleofTwoThirds - itemHoriziontalOffset * 3
				+ 10;
		int equipmentParagraphY = equipmentLableY + textHeightOffset * 2;

		g.drawString(equipmentLabel, equipmentLabelX, equipmentLableY);
		g.drawLine(equipmentLabelX, equipmentLableY + 2, equipmentLabelX
				+ currentFont.stringWidth(equipmentLabel), equipmentLableY + 2);
		g.drawRect(equipmentBoxX, equipmentBoxY, rightBoxWidth, equipmentHeight);

		// now add the skills into a paragraph and write it to the skills area
		String equipmentValue = "";
		for (String equipment : playerWeapons) {
			equipmentValue += equipment + ", ";
		}

		for (String equipment : playerArmor) {
			equipmentValue += equipment + ", ";
		}

		for (String equipment : playerGear) {
			equipmentValue += equipment + ", ";
		}

		// remove last ", " from string
		equipmentValue = equipmentValue.substring(0,
				equipmentValue.length() - 2);

		drawTextParagraph(equipmentValue, g, equipmentParagraphX,
				equipmentParagraphY, rightRowBoxWarpLength);

	}// end paintComponent method

	/**
	 * Returns the current graphics as they are
	 * Called by DnD_Character_Creator
	 * @return CharacterSheetPrintPreview
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public CharacterSheetPrintPreview returnCurrentGraphics() {

		return this;

	}//end returnCurrentGraphics method

	/**
	 * 
	 * @author Devin Simpson
	 * 
	 *         for string contained in paragraph form, we require a specific
	 *         function to correctly wrap the text around a set size, so the
	 *         text does is not written off the page, this function returns the
	 *         total height of the test block it produced to help positioning of
	 *         other elements below this element
	 * 
	 * @param text
	 * @param g
	 * @param startX
	 * @param startY
	 * @param warpLength
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	private void drawTextParagraph(String text, Graphics g, int startX,
			int startY, int warpLength) {

		// split the string into individual words
		String[] arr = text.split(" ");

		// get the height of each line determined by the current font
		FontMetrics textMetrics = g.getFontMetrics();
		int lineHeight = textMetrics.getHeight();

		// move through the array of word, check if a set of words is within the
		// alloted length
		// and print the list of words if it is
		int nIndex = 0;
		while (nIndex < arr.length) {
			String line = arr[nIndex++];
			while ((nIndex < arr.length)
					&& (textMetrics.stringWidth(line + " " + arr[nIndex]) < warpLength)) {
				line = line + " " + arr[nIndex];
				nIndex++;
			}
			g.drawString(line, startX, startY);
			startY = startY + lineHeight;

		}

	}// end method

	/**
	 * Prints the current graphics as they are.
	 * Called by DnD_Character_Creator
	 * @param g
	 * @param pf
	 * @param page
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	@Override
	public int print(Graphics g, PageFormat pf, int page)
			throws PrinterException {
		if (page > 0) { /* We have only one page, and 'page' is zero-based */
			return NO_SUCH_PAGE;
		}

		/*
		 * User (0,0) is typically outside the imageable area, so we must
		 * translate by the X and Y values in the PageFormat to avoid clipping
		 */

		paintComponent(g);

		Dimension compSize = new Dimension(850, 1100);

		Dimension printSize = new Dimension();
		printSize.setSize(pf.getImageableWidth(), pf.getImageableHeight());
		double scaleFactor = getScaleFactorToFit(compSize, printSize);

		// Don't want to scale up, only want to scale down
		if (scaleFactor > 1d) {
			scaleFactor = 1d;
		}

		// Calcaulte the scaled size...
		double scaleWidth = compSize.width * scaleFactor;
		double scaleHeight = compSize.height * scaleFactor;

		double x = ((pf.getImageableWidth() - scaleWidth) / 2d)
				+ pf.getImageableX();
		double y = ((pf.getImageableHeight() - scaleHeight) / 2d)
				+ pf.getImageableY();

		Graphics2D g2d = (Graphics2D) g.create();

		AffineTransform at = new AffineTransform();
		at.translate(x, y);
		// Set the scaling
		at.scale(scaleFactor, scaleFactor);
		// Apply the transformation

		g2d.transform(at);

		this.paintAll(g2d);

		/* tell the caller that this page is part of the printed document */
		return PAGE_EXISTS;
	}//end print method

	/**
	 * Gets the scale factor to fit the print sheet
	 * Called by print
	 * @param original
	 * @param toFit
	 * @return dScale
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public double getScaleFactorToFit(Dimension original, Dimension toFit) {

		double dScale = 1d;

		if (original != null && toFit != null) {

			double dScaleWidth = getScaleFactor(original.width, toFit.width);
			double dScaleHeight = getScaleFactor(original.height, toFit.height);

			dScale = Math.min(dScaleHeight, dScaleWidth);

		}

		return dScale;

	}//end getScaleFactorToFit

	/**
	 * Gets the scale factor
	 * Called by getScaleFactorToFit
	 * @param iMasterSize
	 * @param iTargetSize
	 * @return dScale
	 * @author Devin Simpson
	 * @version (Most Recent) December 8th, 2014 (Created 9/23/2014)
	 */
	public double getScaleFactor(int iMasterSize, int iTargetSize) {

		double dScale = 1;
		if (iMasterSize > iTargetSize) {

			dScale = (double) iTargetSize / (double) iMasterSize;

		} else {

			dScale = (double) iTargetSize / (double) iMasterSize;

		}

		return dScale;

	}//end getScaleFactor

}// end class