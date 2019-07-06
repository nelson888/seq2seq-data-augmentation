package com.tambapps.nlp.scda.augmentation.strategy;

import com.tambapps.nlp.scda.dictionnary.StringUtils;

import java.util.Random;

public abstract class ReplacementAugmentationStrategy extends AbstractAugmentationStrategy {

  private final double gamma;
  private final Random random = new Random();

  public ReplacementAugmentationStrategy(double gamma) {
    this.gamma = gamma;
  }

  @Override
  String modifySource(String entry) {
    StringBuilder builder = new StringBuilder();
    for (String word : entry.split(" ")) {
      builder.append(!StringUtils.isPunctuation(word) && random.nextDouble() <= gamma ? replaceWord(word) : word)
        .append(" ");
    }
    return builder.toString().trim();
  }

  abstract String replaceWord(String word);
}
