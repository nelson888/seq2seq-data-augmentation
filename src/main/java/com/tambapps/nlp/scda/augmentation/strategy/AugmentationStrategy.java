package com.tambapps.nlp.scda.augmentation.strategy;

import com.tambapps.nlp.scda.dataset.Dataset;

import java.io.IOException;

public interface AugmentationStrategy {

  void apply(Dataset.Entry entry) throws IOException;

}
