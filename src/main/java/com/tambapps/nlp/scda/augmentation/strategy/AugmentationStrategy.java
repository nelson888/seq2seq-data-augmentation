package com.tambapps.nlp.scda.augmentation.strategy;

import com.tambapps.nlp.scda.dataset.Dataset;

import java.io.IOException;

/**
 * A strategy to augment entries in a traduction dataset
 */
public interface AugmentationStrategy {

  /**
   * Apply the strategy on a given entry
   * @param entry the entry to apply the strategy to
   * @throws IOException in case of error while reading/writing the entry
   */
  void apply(Dataset.Entry entry) throws IOException;

  /**
   * The number of entries added by this strategy
   * @return the number of entries added by this strategy
   */
  long getAddedEntries();

  /**
   * The name of this strategy
   * @return the name of this strategy
   */
  String getName();

}
