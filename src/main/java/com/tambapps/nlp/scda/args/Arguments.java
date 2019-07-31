package com.tambapps.nlp.scda.args;

import com.beust.jcommander.validators.PositiveInteger;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;
import lombok.Getter;

import java.io.File;

@Parameters
@Getter
public class Arguments {

  @Parameter(names = "--source-file", description = "The training set in source language", required = true)
  private File sourceFile;

  @Parameter(names = "--target-file", description = "The training set in target language", required = true,
  converter = FileConverter.class)
  private File targetFile;

  @Parameter(names = "--dictionary-file", description = "The dictionary file of the source language",
  converter = FileConverter.class)
  private File dictionaryFile;

  @Parameter(names = "--distribution-file", description = "The distribution file of the source language",
  converter = FileConverter.class)
  private File distributionFile;

  @Parameter(names = {"-g", "--gamma"}, description = "gamma, the probability to replace words")
  private double gamma = 0.16d;

  @Parameter(names = {"-w", "--window-size"}, description = "k, the window size to swap words")
  private int k = 3;

  @Parameter(names = {"-l", "--log-errors"}, description = "Log the errors if any when augmenting dataset")
  private boolean logErrors = false;

  @Parameter(validateWith = PositiveInteger.class, names = {"-d", "--duplication" },
    description = "The number of duplication of each original entry. The original entry is always wrote once. " +
      "This option allows to write it more than once (useful to add more weight on original entries of the dataset). " +
      "In total, there will be d times the number of entries of duplicated entries ")
  private int duplication = 0;

}
