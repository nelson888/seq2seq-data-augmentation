package com.tambapps.nlp.scda.dictionary;

import java.util.regex.Pattern;

public final class StringUtils {

  public static final String EMPTY_STRING = "";
  private static final String PUNCTUATION_CHARS = "!\"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~" + "！？･・";
  private static final String PUNCTUATION_PATTERN = "[" + Pattern.quote(PUNCTUATION_CHARS) + "]";

  private StringUtils() {}

  public static String trimPunctuation(String word) {
    return word.trim().replaceAll(PUNCTUATION_PATTERN, EMPTY_STRING).trim();
  }

  public static boolean isPunctuation(String word) {
    return trimPunctuation(word).isEmpty();
  }
}
