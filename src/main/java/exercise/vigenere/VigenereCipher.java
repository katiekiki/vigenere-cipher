package exercise.vigenere;

import java.util.HashMap;
import java.util.Map;

public class VigenereCipher {

    private String sourceCharacterSet;
    private String key;
    private Map<Character, Integer> charMap;

    public VigenereCipher(String characterSet, String key) {
        this.sourceCharacterSet = characterSet;
        this.key = key;
        populateMap();
    }


    private void populateMap() {
        this.charMap = new HashMap<>();
        //transform character set into a hashmap
        for (int i = 0; i < sourceCharacterSet.length(); i++) {
            charMap.put(sourceCharacterSet.charAt(i), i);
        }

    }

    private int getPositionInCharArray(char inputChar) {
        if (!charMap.containsKey(inputChar)) {
            return -1;
        } else {
            return charMap.get(inputChar);
        }

    }

    public String encrypt(String input) {
        return execute(input,true);
    }

    public String decrypt(String input) {
        return execute(input,false);
    }

    private String execute(String input, boolean isEncryption) {
        char[] output = new char[input.length()];

        for (int i = 0, j = 0; i < input.length(); i++) {

            int positionInCharArray = getPositionInCharArray(input.charAt(i));
            //if character in input does not exist in charMap, copy input as is
            if (positionInCharArray == -1) {
                output[i] = input.charAt(i);
                continue;
            }

            int keyPositionInCharArray = getPositionInCharArray(key.charAt(j));

            //if character in key does not exist in charMap, copy input as is
            if (keyPositionInCharArray == -1) {
                output[i] = input.charAt(i);
                continue;
            }
            int targetChar = 0;
            if (isEncryption) {
                targetChar = getEncryptedChar(positionInCharArray,keyPositionInCharArray);
            } else {
                targetChar = getDecryptedChar(positionInCharArray,keyPositionInCharArray);
            }

            output[i] = sourceCharacterSet.charAt(targetChar);

            j++;
            //reset j = 0 if j == key.length()
            if (j == key.length()) {
                j = 0;
            }

        }

        return String.valueOf(output);

    }

    private int getEncryptedChar(int inputCharPosition, int keyCharPosition) {
        return (inputCharPosition + keyCharPosition) % sourceCharacterSet.length();

    }

    private int getDecryptedChar(int inputCharPosition, int keyCharPosition) {
        int decryptedCharPosition = inputCharPosition - keyCharPosition;
        if (decryptedCharPosition < 0) {
            decryptedCharPosition = sourceCharacterSet.length() + decryptedCharPosition;
        }
        return decryptedCharPosition;
    }


}
