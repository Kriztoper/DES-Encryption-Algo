package des.main;

import java.util.Scanner;

public class SDES {

	private static final String KEY = "111000111";
	private int round;
	private boolean encryptionMode;
	private String k1;
	private String k2;
	private String l;
	private String r;
	private static final String[][] s1 = {
			{"101", "010", "001", "110", "011", "100", "111", "000"},
			{"001", "100", "110", "010", "000", "111", "101", "011"}
	};
	private static final String[][] s2 = {
			{"100", "000", "110", "101", "111", "001", "011", "010"},
			{"101", "011", "000", "111", "110", "010", "001", "100"}
	};
	
	public static void main(String[] args) {
		System.out.print("Bit String encryption/decryption "
				+ "program using SDES algorithm\n"
				+ "Enter a 12-bit string(ex. 100010110101): ");
		Scanner scanner = new Scanner(System.in);
		String bitString = scanner.nextLine();
		
		SDES sdes = new SDES();
		
		String encryptedText = sdes.encrypt(bitString);
		System.out.println("Encrypted Text: " + encryptedText);
		
		String decryptedText = sdes.decrypt(encryptedText);
		System.out.println("Decrypted Text: " + decryptedText);
	}
	
	public SDES() {
		generateOtherKeys(KEY);
	}
	
	private void generateOtherKeys(String key) {
		k1 = key.substring(0, key.length() - 1);
		k2 = generateK2(key);
	}
	
	private String generateK2(String key) {
		String k2 = "";
		
		k2 += key.charAt(1);
		k2 += key.charAt(2);
		k2 += key.charAt(3);
		k2 += key.charAt(4);
		k2 += key.charAt(5);
		k2 += key.charAt(6);
		k2 += key.charAt(7);
		k2 += key.charAt(8);
		
		return k2;
	}

	public String encrypt(String bitString) {
		encryptionMode = true;
		round = 0;
		
		// Round 1
		round++;
		String tempL = extractLeftBitString(bitString, 6);
		String tempR = extractRightBitString(bitString, 6);
		String rk = function(tempR, getCurrentK());
		r = xor(tempL, rk);
		l = tempR;

		// Round 2
		round++;
		tempR = r;
		rk = function(r, getCurrentK());
		r = xor(l, rk);
		l = tempR;
		
		return r + l;
	}
	
	private String function(String r, String k) {
		r = expand(r);
		
		String rk = xor(r, k);
		
		String left_rk = extractLeftBitString(rk, 4);
		String right_rk = extractRightBitString(rk, 4);
		
		String left_s1 = s1[Integer.parseInt(left_rk.charAt(0) + "")][Integer.parseInt(left_rk.substring(1, 4), 2)];
		String right_s1 = s2[Integer.parseInt(right_rk.charAt(0) + "")][Integer.parseInt(right_rk.substring(1, 4), 2)];
		
		return left_s1 + right_s1;
	}
	
	private String xor(String r,String k) {
		String rk = "";
		
		for (int i = 0; i < r.length(); i++) {
			rk += xor(r.charAt(i) , k.charAt(i)) + "";
		}
		
		return rk;
	}
	
	private char xor(char c1, char c2) {
		return (c1 != c2) ? ('1') : ('0');
	}
	
	private String getCurrentK() {
		if (encryptionMode) {
			return (round == 1) ? (k1) : (k2);
		} else {
			return (round == 1) ? (k2) : (k1);
		}
	}
	
	private String expand(String bitString) {
		String expandedBitString = "";
		
		expandedBitString += bitString.charAt(0);
		expandedBitString += bitString.charAt(1);
		expandedBitString += bitString.charAt(3);
		expandedBitString += bitString.charAt(2);
		expandedBitString += bitString.charAt(3);
		expandedBitString += bitString.charAt(2);
		expandedBitString += bitString.charAt(4);
		expandedBitString += bitString.charAt(5);
		
		return expandedBitString;
	}
	
	private String extractLeftBitString(String bitString, int range) {
		String l = "";
		
		for (int i = 0; i < range; i++) {
			l += bitString.charAt(i);
		}
		
		return l;
	}

	private String extractRightBitString(String bitString, int range) {
		String r = "";
		int len = bitString.length();
		
		for (int i = len / 2; i < (len / 2) + range; i++) {
			r += bitString.charAt(i);
		}
		
		return r;
	}
	
	public String decrypt(String encryptedText) {
		encryptionMode = false;
		round = 0;
		
		// Round 1
		round++;
		String tempL = extractLeftBitString(encryptedText, 6);
		String tempR = extractRightBitString(encryptedText, 6);
		String rk = function(tempR, getCurrentK());
		r = xor(tempL, rk);
		l = tempR;

		// Round 2
		round++;
		tempR = r;
		rk = function(r, getCurrentK());
		r = xor(l, rk);
		l = tempR;
		
		return r + l;
	}
}
