package com.alexnegara.androidproject.unwiredvault;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordGenerator {
    // define possible combination of each types
    private static final String CHAR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPERCASE = CHAR_LOWERCASE.toUpperCase();
    private static final String DIGIT = "0123456789";
    private static final String OTHER_PUNCTUATION = "!@#&()–[{}]:;',?/*";
    private static final String OTHER_SYMBOL = "$^+=<>";
    private static final String OTHER_SPECIAL = OTHER_PUNCTUATION + OTHER_SYMBOL;

    // define default length and the total
    private static int lowCharLength = 4;
    private static int upCharLength = 4;
    private static int symbolLength = 4;
    private static int digitLength = 4;
    private static int PASSWORD_LENGTH = lowCharLength + upCharLength + symbolLength + digitLength;

    // init randomizer object to select at n index.
    private static SecureRandom random = new SecureRandom();

    // define getter and setter methods
    public static int getLowCharLength(){
        return lowCharLength;
    }

    public static int getUpCharLength() {
        return upCharLength;
    }

    public static int getSymbolLength() {
        return symbolLength;
    }

    public static int getDigitLength() {
        return digitLength;
    }

    public static int getPasswordLength() {
        return PASSWORD_LENGTH;
    }

    public static void setLowCharLength(int length){
        lowCharLength = length;
    }

    public static void setUpCharLength(int length){
        upCharLength = length;
    }

    public static void setDigitLength(int length){
        digitLength = length;
    }

    public static void setSymbolLength(int length){
        symbolLength = length;
    }

    // generate string based on the combinations and size by using randomizer
    public static String generateRandomString(String input, int size){
        if (input == null || input.length() <= 0)
            throw new IllegalArgumentException("Invalid input.");
        if (size < 1) throw new IllegalArgumentException("Invalid size.");

        StringBuilder result = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int index = random.nextInt(input.length());
            result.append(input.charAt(index));
        }
        return result.toString();
    }

    // add all the result of randomizer
    public static String generate(){
        StringBuilder result = new StringBuilder(PASSWORD_LENGTH);

        // at least n chars (lowercase)
        String strLowerCase = generateRandomString(CHAR_LOWERCASE, getLowCharLength());
        result.append(strLowerCase);

        // at least n chars (uppercase)
        String strUppercaseCase = generateRandomString(CHAR_UPPERCASE, getUpCharLength());
        result.append(strUppercaseCase);

        // at least n digits
        String strDigit = generateRandomString(DIGIT, getDigitLength());
        result.append(strDigit);

        // at least n special characters (punctuation + symbols)
        String strSpecialChar = generateRandomString(OTHER_SPECIAL, getSymbolLength());
        result.append(strSpecialChar);

        // combine and shuffle
        String password = shuffleString(result.toString());

        return password;
    }

    // join string from List
    private static String joinStringFromList(List<String> input){
        if(input == null || input.size() <= 0) return "";
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<input.size();i++){
            sb.append(input.get(i));
        }
        return sb.toString();
    }

    // shuffle string
    public static String shuffleString(String input){
        List<String> result = Arrays.asList(input.split(""));
        Collections.shuffle(result);
        return joinStringFromList(result);
    }
}
