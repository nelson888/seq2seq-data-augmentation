package com.tambapps.nlp.scda;

import com.tambapps.nlp.scda.args.Arguments;
import com.tambapps.nlp.scda.augmentation.modifier.AugmentationModifier;
import com.tambapps.nlp.scda.augmentation.strategy.AugmentationStrategy;
import com.tambapps.nlp.scda.augmentation.strategy.DropoutAugmentationStrategyStrategy;
import com.tambapps.nlp.scda.augmentation.strategy.PlaceholderAugmentationStrategyStrategy;
import com.tambapps.nlp.scda.augmentation.strategy.SmoothAugmentationStrategy;
import com.tambapps.nlp.scda.augmentation.strategy.SwapAugmentationStrategy;
import com.tambapps.nlp.scda.dataset.Dataset;
import com.tambapps.nlp.scda.dictionary.UnigramDistribution;
import com.tambapps.nlp.scda.io.IOUtils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

  private static final String OUTPUT_FILE_SUFFIX = ".augmented";
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws IOException {
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
      LOGGER.error("Couldn't parse arguments: {}", e.getMessage());
      jCommander.usage();
      return;
    }
    final AugmentationModifier modifier = getAugmentationModifier(arguments, unigramDistribution);

    File sourceFile = arguments.getSourceFile();
    File targetFile = arguments.getTargetFile();
    File outputSourceFile = new File(sourceFile.getParent(), sourceFile.getName() + OUTPUT_FILE_SUFFIX);
    File outputTargetFile = new File(targetFile.getParent(), targetFile.getName() + OUTPUT_FILE_SUFFIX);

    LOGGER.info("Starting processing dataset");
    try (BufferedReader sourceReader = IOUtils.newReader(arguments.getSourceFile());
         BufferedReader targetReader = IOUtils.newReader(arguments.getTargetFile());
         BufferedWriter sourceWriter = IOUtils.newWriter(outputSourceFile);
         BufferedWriter targetWriter = IOUtils.newWriter(outputTargetFile)) {
      LOGGER.info("Writing output source file in {}", outputSourceFile.getPath());
      LOGGER.info("Writing output target file in {}", outputTargetFile.getPath());
      Dataset dataset = new Dataset(sourceReader, targetReader, sourceWriter, targetWriter);
      modifier.augmentDataset(dataset);
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
    return new AugmentationModifier(strategies);
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
