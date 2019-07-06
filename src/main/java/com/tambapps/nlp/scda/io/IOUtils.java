package com.tambapps.nlp.scda.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class IOUtils {
  private IOUtils() {}

  public static BufferedReader newReader(File file) throws IOException {
    return new BufferedReader(new FileReader(file));
  }

  public static BufferedWriter newWriter(File file) throws IOException {
    return new BufferedWriter(new FileWriter(file));
  }
}
