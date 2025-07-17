package org.example.service.impl.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

  public static String extractSection(String text, String sectionName) {
    // Regex sederhana untuk ambil bagian dari sebuah CV berdasarkan header
    String regex = sectionName + "[:\\n]+(.*?)(?=\\n[A-Z][a-zA-Z ]+:|\\Z)";
    Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(text);
    if (matcher.find()) {
      return matcher.group(1).trim();
    }
    return "";
  }
}
