package com.tambapps.nlp.scda.augmentation.strategy;

/**
 * Replace random words with a placeholder '_'
 */
public class PlaceholderAugmentationStrategyStrategy extends ReplacementAugmentationStrategy {

  private static final String PLACEHOLDER = "_";

  public PlaceholderAugmentationStrategyStrategy(double gamma) {
    super(gamma);
  }

  @Override
  String replaceWord(String word) {
    return PLACEHOLDER;
  }

}
