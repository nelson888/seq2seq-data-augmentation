package com.tambapps.nlp.scda.augmentation.strategy;

import com.tambapps.nlp.scda.dictionnary.StringUtils;

/**
 * Drop random words
 */
public class DropoutAugmentationStrategyStrategy extends ReplacementAugmentationStrategy {


  public DropoutAugmentationStrategyStrategy(double gamma) {
    super(gamma);
  }

  @Override
  String replaceWord(String word) {
    return StringUtils.EMPTY_STRING;
  }

}
