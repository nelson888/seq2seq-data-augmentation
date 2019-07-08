package com.tambapps.nlp.scda.augmentation.modifier;

import com.tambapps.nlp.scda.augmentation.strategy.AugmentationStrategy;
import com.tambapps.nlp.scda.dataset.IODataset;
import com.tambapps.nlp.scda.exception.DistributionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Class used to iterate over a {@link IODataset} and increase it size with the SCDA method
 */
public class AugmentationModifier {

  private static final Logger LOGGER = LoggerFactory.getLogger(AugmentationModifier.class);
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

  private final List<AugmentationStrategy> augmentationStrategies;

  public AugmentationModifier(List<AugmentationStrategy> augmentationStrategies) {
    this.augmentationStrategies = augmentationStrategies;
  }


  /**
   * Augment the dataset with the SCDA method
   * @param dataset the dataset to augment
   * @throws IOException in case of IO error while reading/writing
   */
  public void augmentDataset(IODataset dataset) throws IOException {
    long totalSize = 0;
    for (IODataset.Entry entry : dataset) {
      entry.write(); // write the entry itself once
      totalSize++;
      for (AugmentationStrategy strategy : augmentationStrategies) {
        try {
          strategy.apply(entry);
        } catch (DistributionException e) {
          LOGGER.error("Error while performing Smooth: {}", e.getMessage());
          LOGGER.error("Exiting program");
          return;
        }
      }
    }
    long totalAddedEntries = 0;
    for (AugmentationStrategy strategy : augmentationStrategies) {
      totalAddedEntries += strategy.getAddedEntries();
      LOGGER.info("{} entries was added with the {} strategy", DECIMAL_FORMAT.format(strategy.getAddedEntries()), strategy.getName());
    }
    totalSize += totalAddedEntries;
    LOGGER.info("{} entries was added in total", DECIMAL_FORMAT.format(totalAddedEntries));
    LOGGER.info("Finished dataset augmentation. New size: {}", totalSize);
  }
}
