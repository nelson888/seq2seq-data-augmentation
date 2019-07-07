package com.tambapps.nlp.scda.augmentation.strategy;

import java.util.Arrays;
import java.util.Random;

public class SwapAugmentationStrategy extends AbstractAugmentationStrategy {

  private final Random random = new Random();
  private final int window;

  public SwapAugmentationStrategy(int k) {
    this.window = k;
  }

  @Override
  String modifySource(String entry) {
    String[] words = entry.split("\\s+");
    for (int i = 0; i + window - 1 < words.length; i++) {
      String[] windowWords = Arrays.copyOfRange(words, i, i + window - 1);
      assert windowWords.length == window;
      shuffle(windowWords);
      System.arraycopy(words, i, words, 0, windowWords.length);
    }
    return String.join(" ", words);
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
