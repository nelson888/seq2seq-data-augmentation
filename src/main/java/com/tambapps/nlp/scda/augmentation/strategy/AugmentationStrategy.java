package com.tambapps.nlp.scda.augmentation.strategy;

import com.tambapps.nlp.scda.dataset.IODataset;
import com.tambapps.nlp.scda.exception.AugmentationException;

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
  void apply(IODataset.Entry entry) throws IOException, AugmentationException;

  /**
   * The number of entries added by this strategy
   * @return the number of entries added by this strategy
   */
  long getAddedEntries();

  /**
   * The name of this strategy
   * @return the name of this strategy
   */
  default String getName() {
    String name = getClass().getSimpleName();
    return name.substring(0, name.indexOf('A', 1));
  }

}
