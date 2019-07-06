package com.tambapps.nlp.scda.augmentation.modifier;

import com.tambapps.nlp.scda.augmentation.strategy.AugmentationStrategy;
import com.tambapps.nlp.scda.dataset.Dataset;

import java.io.IOException;
import java.util.List;

public class AugmentationModifier {

  private final List<AugmentationStrategy> augmentationStrategies;

  public AugmentationModifier(List<AugmentationStrategy> augmentationStrategies) {
    this.augmentationStrategies = augmentationStrategies;
  }

  public void modifyDataset(Dataset dataset) throws IOException {
    for (Dataset.Entry entry : dataset) {
      for (AugmentationStrategy strategy : augmentationStrategies) {
        strategy.apply(entry);
      }
    }
  }
}
