package com.tambapps.nlp.scda.dataset;

import com.tambapps.nlp.scda.exception.DatasetException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * A dataset is two files:
 * the file in the source language, and the second in the target language
 */
public class Dataset implements Iterator<Dataset.Entry>, Iterable<Dataset.Entry> {

  private final BufferedReader sourceReader;
  private final BufferedReader targetReader;
  private final BufferedWriter sourceWriter;
  private final BufferedWriter targetWriter;
  private String sourceLine;
  private String targetLine;

  public Dataset(BufferedReader sourceReader, BufferedReader targetReader,
                 BufferedWriter sourceWriter, BufferedWriter targetWriter) throws IOException {
    this.sourceReader = sourceReader;
    this.targetReader = targetReader;
    this.sourceWriter = sourceWriter;
    this.targetWriter = targetWriter;
    sourceLine = sourceReader.readLine();
    targetLine = targetReader.readLine();
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

  @AllArgsConstructor
  @Getter
  public class Entry {
    private String sourceText;
    private String targetText;

    public void write() throws IOException {
      Dataset.this.write(sourceText, targetText);
    }

    /**
     * Write a sourceTextVariation, and its targetText (translation) in the dataset
     * @param sourceTextVariation a variation of the source text
     */
    public void writeVariation(String sourceTextVariation) throws IOException {
      Dataset.this.write(sourceTextVariation, targetText);
    }
  }
}
