package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpaceEliminate {
    public static String delSpaceAll(String oldStr){
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(oldStr);
        oldStr = m.replaceAll("");
        return oldStr;
    }
}
