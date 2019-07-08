package com.tambapps.nlp.scda.augmentation.modifier;

import com.tambapps.nlp.scda.augmentation.strategy.AugmentationStrategy;
import com.tambapps.nlp.scda.dataset.Dataset;
import com.tambapps.nlp.scda.exception.DistributionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class AugmentationModifier {

  private static final Logger LOGGER = LoggerFactory.getLogger(AugmentationModifier.class);
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

  private final List<AugmentationStrategy> augmentationStrategies;

  public AugmentationModifier(List<AugmentationStrategy> augmentationStrategies) {
    this.augmentationStrategies = augmentationStrategies;
  }

  public void modifyDataset(Dataset dataset) throws IOException {
    long totalSize = 0;
    for (Dataset.Entry entry : dataset) {
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
      LOGGER.info("{} entries was added with the strategy {}", DECIMAL_FORMAT.format(strategy.getAddedEntries()), strategy.getName());
    }
    totalSize += totalAddedEntries;
    LOGGER.info("{} entries was added in total", DECIMAL_FORMAT.format(totalAddedEntries));
    LOGGER.info("Finished dataset augmentation. New size: {}", totalSize);
  }
}
