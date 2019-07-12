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
  private int originalDuplications;

  private final List<AugmentationStrategy> augmentationStrategies;

  public AugmentationModifier(List<AugmentationStrategy> augmentationStrategies, int originalDuplications) {
    this.augmentationStrategies = augmentationStrategies;
    this.originalDuplications = originalDuplications;
  }

  /**
   * Augment the dataset with the SCDA method
   * @param dataset the dataset to augment
   * @throws IOException in case of IO error while reading/writing
   */
  public void augmentDataset(IODataset dataset) throws IOException, DistributionException {
    long nbEntries = dataset.getNbEntries();
    final long progressStep = nbEntries /100L;
    long step = 0;
    LOGGER.info("{} entries was found", nbEntries);
    LOGGER.info("Starting processing entries");
    long processed = 0;
    for (IODataset.Entry entry : dataset) {
      for (int i = 0; i < originalDuplications; i++) {
        entry.write();
      }
      for (AugmentationStrategy strategy : augmentationStrategies) {
        strategy.apply(entry);
      }
      processed++;
      if (processed  >= step * progressStep) {
        step += 1;
        System.out.format("Processed %d lines out of %d\t(%d%%)\r", processed, nbEntries, step);
      }
    }
    LOGGER.info("Finished processing entries");
    long totalAddedEntries = 0;
    for (AugmentationStrategy strategy : augmentationStrategies) {
      totalAddedEntries += strategy.getAddedEntries();
      LOGGER.info("{} entries was added with the {} strategy", DECIMAL_FORMAT.format(strategy.getAddedEntries()), strategy.getName());
    }
    long totalSize = nbEntries + totalAddedEntries;
    LOGGER.info("New size: {} ({} new entries added)", DECIMAL_FORMAT.format(totalSize),
      DECIMAL_FORMAT.format(totalAddedEntries));
  }

  private float percentage(long processed, long nbEntries) {
    return 100L * processed / nbEntries;
  }
}
