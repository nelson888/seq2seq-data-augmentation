package com.tambapps.nlp.scda.args;

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

  @Parameter(names = {"-d", "--duplication" }, description = "The number of duplications to create for the original entry")
  private int duplications = 4;

}
