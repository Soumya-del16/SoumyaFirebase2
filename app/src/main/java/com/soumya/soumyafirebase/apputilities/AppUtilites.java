package com.soumya.soumyafirebase.apputilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtilites {




    public static  Boolean isvalidEmailId(String user_email){
        //Regular Expression
        String regex = "^(.+)@(.+)$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(user_email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String user_password) {
        //Regular Expression
        String regex = "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,16}$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(user_password);
        if(matcher.matches()){
            return true;
        }
        else {
            return false;
        }
    }
}
