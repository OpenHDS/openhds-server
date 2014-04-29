package org.openhds.controller.idgeneration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.springframework.stereotype.Component;

/**
 * The Luhn mod N algorithm generates a check character within the same range 
 * of valid characters as the input string. For example, if the algorithm is 
 * applied to a string of lower-case letters (a to z), the check character will 
 * also be a lower-case letter. 
 * 
 * The main idea behind the extension is that the full set of valid input characters 
 * is mapped to a list of code-points (i.e., sequential integers beginning with zero).
 * The algorithm processes the input string by converting each character to its associated 
 * code-point and then performing the computations in mod N (where N is the number of 
 * valid input characters). Finally, the resulting check code-point is mapped back to 
 * obtain its corresponding check character.
 * 
 * @author Brian
 */
@Component
public class LuhnValidator {
	
	private static HashMap<Character, Integer> map = createMap();
	
	private static HashMap<Character, Integer> createMap() {
		 HashMap<Character, Integer> result = new HashMap<Character, Integer>();
		 result.put('A', 0);
		 result.put('B', 1);
		 result.put('C', 2);
		 result.put('D', 3);
		 result.put('E', 4);
		 result.put('F', 5);
		 result.put('G', 6);
		 result.put('H', 7);
		 result.put('I', 8);
		 result.put('J', 9);
		 result.put('K', 10);
		 result.put('L', 11);
		 result.put('M', 12);
		 result.put('N', 13);
		 result.put('O', 14);
		 result.put('P', 15);
		 result.put('Q', 16);
		 result.put('R', 17);
		 result.put('S', 18);
		 result.put('T', 19);
		 result.put('U', 20);
		 result.put('V', 21);
		 result.put('W', 22);
		 result.put('X', 23);
		 result.put('Y', 24);
		 result.put('Z', 25);
		 result.put('0', 26);
		 result.put('1', 27);
		 result.put('2', 28);
		 result.put('3', 29);
		 result.put('4', 30);
		 result.put('5', 31);
		 result.put('6', 32);
		 result.put('7', 33);
		 result.put('8', 34);
		 result.put('9', 35);
	     return result;
	}
	
	public Character generateCheckCharacter(String input) {
		
		int factor = 2;
		int sum = 0;
		int n = getNumberOfValidationCharacters();
	 
		// Starting from the right and working leftwards is easier since 
		// the initial "factor" will always be "2" 
		for (int i = input.length() - 1; i >= 0; i--) {
			int codePoint = getCodePointFromCharacter(input.charAt(i));
			int addend = factor * codePoint;
	 
			// Alternate the "factor" that each "codePoint" is multiplied by
			factor = (factor == 2) ? 1 : 2;
	 
			// Sum the digits of the "addend" as expressed in base "n"
			addend = (addend / n) + (addend % n);
			sum += addend;
		}
	 
		// Calculate the number that must be added to the "sum" 
		// to make it divisible by "n"
		int remainder = sum % n;
		int checkCodePoint = n - remainder;
		checkCodePoint %= n;
	 
		return getCharacterFromCodePoint(checkCodePoint);
	}
	
	public boolean validateCheckCharacter(String input) {
		
		int factor = 1;
		int sum = 0;
		int n = getNumberOfValidationCharacters();
	 
		// Starting from the right, work leftwards
		// Now, the initial "factor" will always be "1" 
		// since the last character is the check character
		for (int i = input.length() - 1; i >= 0; i--) {
			int codePoint = getCodePointFromCharacter(input.charAt(i));
			int addend = factor * codePoint;
	 
			// Alternate the "factor" that each "codePoint" is multiplied by
			factor = (factor == 2) ? 1 : 2;
	 
			// Sum the digits of the "addend" as expressed in base "n"
			addend = (addend / n) + (addend % n);
			sum += addend;
		}
	 
		int remainder = sum % n; 
		return (remainder == 0);
	}
	
	public int getCodePointFromCharacter(Character c) {
		return map.get(Character.toUpperCase(c));
	}
	
	public Character getCharacterFromCodePoint(int n) {
		Set<Character> set = map.keySet();
		Iterator<Character> itr = set.iterator();
		while(itr.hasNext()) {
			Character c = itr.next();
			if (map.get(c) == n)
				return c;
		}
		return null;
	}
	
	public int getNumberOfValidationCharacters() {
		return map.size();
	}
}
