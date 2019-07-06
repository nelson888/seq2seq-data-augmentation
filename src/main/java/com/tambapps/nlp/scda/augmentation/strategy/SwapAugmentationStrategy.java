package com.tambapps.nlp.scda.augmentation.strategy;

public class SwapAugmentationStrategy extends AbstractAugmentationStrategy {

  private final int window;

  public SwapAugmentationStrategy(int k) {
    this.window = k;
  }

  @Override
  String modifySource(String entry) {
    return null;
  }
}
