package com.tambapps.nlp.scda.augmentation.strategy;

import com.tambapps.nlp.scda.dataset.IODataset;

import java.io.IOException;

public class DuplicationAugmentationStrategy implements AugmentationStrategy {

  private final int nbDuplications;
  private long addedEntries = 0;

  public DuplicationAugmentationStrategy(int nbDuplications) {
    this.nbDuplications = nbDuplications;
  }

  @Override
  public void apply(IODataset.Entry entry) throws IOException {
    for (int i = 0; i < nbDuplications; i++) {
      entry.write();
    }
    addedEntries += nbDuplications;
  }

  @Override
  public long getAddedEntries() {
    return addedEntries;
  }

}
