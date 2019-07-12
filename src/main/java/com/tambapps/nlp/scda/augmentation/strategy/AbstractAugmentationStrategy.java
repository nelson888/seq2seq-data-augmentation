package com.tambapps.nlp.scda.augmentation.strategy;

import com.tambapps.nlp.scda.dataset.IODataset;

import java.io.IOException;

public abstract class AbstractAugmentationStrategy implements AugmentationStrategy {

  private long addedEntries = 0;
  @Override
  public final void apply(IODataset.Entry entry) throws IOException {
    String modifiedSource = modifySource(entry.getSourceText());
    if (modifiedSource.equals(entry.getSourceText())) {
      return;
    }
    addedEntries++;
    entry.writeVariation(modifiedSource);
  }

  abstract String modifySource(String entry);

  @Override
  public long getAddedEntries() {
    return addedEntries;
  }

}
