package com.tambapps.nlp.scda;

import com.tambapps.nlp.scda.args.Arguments;
import com.tambapps.nlp.scda.augmentation.modifier.AugmentationModifier;
import com.tambapps.nlp.scda.augmentation.strategy.AugmentationStrategy;
import com.tambapps.nlp.scda.augmentation.strategy.DropoutAugmentationStrategyStrategy;
import com.tambapps.nlp.scda.augmentation.strategy.PlaceholderAugmentationStrategyStrategy;
import com.tambapps.nlp.scda.augmentation.strategy.SmoothAugmentationStrategy;
import com.tambapps.nlp.scda.augmentation.strategy.SwapAugmentationStrategy;
import com.tambapps.nlp.scda.dataset.IODataset;
import com.tambapps.nlp.scda.dictionary.UnigramDistribution;
import com.tambapps.utils.ioutils.IOUtils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    Arguments arguments = new Arguments();

    JCommander jCommander = JCommander.newBuilder()
      .addObject(arguments)
      .build();
    final UnigramDistribution unigramDistribution;
    try {
      jCommander.parse(args);
      LOGGER.info("Loading unigram distribution...");
      unigramDistribution = getUnigramDistribution(arguments.getDictionaryFile(), arguments.getDistributionFile());
      LOGGER.info("Unigram distribution was loaded successfully");
    } catch (ParameterException e) {
      LOGGER.error("Error while parsing arguments: {}", e.getMessage());
      jCommander.usage();
      return;
    } catch (IOException e) {
      LOGGER.error("Error while attempting to read dictionary/distribution file", e);
      return;
    }
    final AugmentationModifier modifier = getAugmentationModifier(arguments, unigramDistribution);

    LOGGER.info("Starting processing dataset");
    try (IODataset dataset = new IODataset(arguments.getSourceFile(), arguments.getTargetFile())) {
      modifier.augmentDataset(dataset);
    } catch (IOException e) {
      LOGGER.error("Error while manipulating the dataset", e);
    }
  }

  private static AugmentationModifier getAugmentationModifier(Arguments arguments,
                                                              UnigramDistribution unigramDistribution) {
    double gamma = arguments.getGamma();
    List<AugmentationStrategy> strategies =
      List.of(new SwapAugmentationStrategy(arguments.getK()),
        new DropoutAugmentationStrategyStrategy(gamma),
        new PlaceholderAugmentationStrategyStrategy(gamma),
        new SmoothAugmentationStrategy(gamma, unigramDistribution));
    return new AugmentationModifier(strategies, arguments.getDuplications());
  }

  private static UnigramDistribution getUnigramDistribution(File dictionaryFile, File distributionFile) throws IOException, ParameterException {
    if (dictionaryFile != null) {
      try (BufferedReader reader = IOUtils.newReader(dictionaryFile)) {
        return UnigramDistribution.loadDictionary(reader);
      }
    } else if (distributionFile != null) {
      try (BufferedReader reader = IOUtils.newReader(distributionFile)) {
        return UnigramDistribution.loadDistribution(reader);
      }
    } else {
      throw new ParameterException("You must provide a dictionary file or a distribution file");
    }
  }
}
