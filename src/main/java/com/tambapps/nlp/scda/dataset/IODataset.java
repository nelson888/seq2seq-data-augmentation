package com.tambapps.nlp.scda.dataset;

import com.tambapps.nlp.scda.exception.DatasetException;
import com.tambapps.utils.ioutils.IOUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A dataset is two files:
 * the file in the source language, and the second in the target language
 */
public class IODataset implements Iterator<IODataset.Entry>, Iterable<IODataset.Entry>, Closeable {

  private static final Logger LOGGER = LoggerFactory.getLogger(IODataset.class);
  private static final String OUTPUT_FILE_SUFFIX = ".augmented";

  private final BufferedReader sourceReader;
  private final BufferedReader targetReader;
  private final BufferedWriter sourceWriter;
  private final BufferedWriter targetWriter;
  private String sourceLine;
  private String targetLine;

  public IODataset(File sourceFile, File targetFile) throws IOException {
    File outputSourceFile = new File(sourceFile.getParent(), sourceFile.getName() + OUTPUT_FILE_SUFFIX);
    File outputTargetFile = new File(targetFile.getParent(), targetFile.getName() + OUTPUT_FILE_SUFFIX);
    this.sourceReader = IOUtils.newReader(sourceFile);
    this.targetReader = IOUtils.newReader(targetFile);
    this.sourceWriter = IOUtils.newWriter(outputSourceFile);
    this.targetWriter = IOUtils.newWriter(outputTargetFile);
    sourceLine = sourceReader.readLine();
    targetLine = targetReader.readLine();
    LOGGER.info("Writing output source file in {}", outputSourceFile.getPath());
    LOGGER.info("Writing output target file in {}", outputTargetFile.getPath());
  }

  public Iterator<Entry> iterator() {
    return this;
  }

  public boolean hasNext() {
    return sourceLine != null && targetLine != null;
  }

  private void write(String sourceText, String targetText) throws IOException {
    sourceWriter.write(sourceText + "\n");
    targetWriter.write(targetText + "\n");
  }

  public Entry next() {
    Entry entry = new Entry(sourceLine, targetLine);
    try {
      sourceLine = sourceReader.readLine();
      targetLine = targetReader.readLine();
    } catch (IOException e) {
      throw new DatasetException("An IO Exception occurred while reading dataset", e);
    }
    return entry;
  }

  @Override
  public void close() throws IOException {
    for (Closeable c : Arrays.asList(sourceReader, targetReader, sourceWriter, targetWriter)) {
      c.close();
    }
  }

  @AllArgsConstructor
  @Getter
  public class Entry {
    private String sourceText;
    private String targetText;

    public void write() throws IOException {
      IODataset.this.write(sourceText, targetText);
    }

    /**
     * Write a sourceTextVariation, and its targetText (translation) in the dataset
     * @param sourceTextVariation a variation of the source text
     */
    public void writeVariation(String sourceTextVariation) throws IOException {
      IODataset.this.write(sourceTextVariation, targetText);
    }
  }
}
