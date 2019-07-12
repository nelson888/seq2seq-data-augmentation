package com.tambapps.nlp.scda.augmentation.strategy;

import java.util.Arrays;
import java.util.Random;

/**
 * Swap randomly words in a window of given size
 */
public class SwapAugmentationStrategy extends AbstractAugmentationStrategy {

  private static final String SPACE = " ";

  private final Random random = new Random();
  private final int window;

  public SwapAugmentationStrategy(int k) {
    this.window = k;
  }

  @Override
  String modifySource(String entry) {
    String[] words = entry.split("\\s+");
    for (int i = 0; i + window - 1 < words.length; i++) {
      String[] windowWords = Arrays.copyOfRange(words, i, i + window);
      shuffle(windowWords);
      System.arraycopy(windowWords, 0, words, i, windowWords.length);
    }
    return String.join(SPACE, words);
  }

  private void shuffle(String[] windowWords) {
    for (int i = windowWords.length - 1; i > 0; i--) {
      int index = random.nextInt(i + 1);
      String s = windowWords[index];
      windowWords[index] = windowWords[i];
      windowWords[i] = s;
    }
  }
}
