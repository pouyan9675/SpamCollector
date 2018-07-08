package com.nahed.pouyan.spam_collector.helpers;

/**
 * Created by Pouyan-PC on 10/27/2017.
 */

public class PersianHelper {

    public static char[] numbers = {'\u06f0',           // Persian numbers
            '\u06f1',
            '\u06f2',
            '\u06f3',
            '\u06f4',
            '\u06f5',
            '\u06f6',
            '\u06f7',
            '\u06f8',
            '\u06f9'};

    public static String convertToPersianNumbers(String number){
        char[] chars = number.toCharArray();
        for(int i=0; i<chars.length; i++){
            if(Character.isDigit(chars[i])){
                chars[i] = numbers[chars[i] - 48];
            }
        }
        return new String(chars);
    }

}
