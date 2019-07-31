package com.tambapps.nlp.scda.args.validator;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class ProbabilityValidator implements IParameterValidator {
  @Override
  public void validate(String name, String value) throws ParameterException {
    try {
      double d = Double.parseDouble(value);
      if (d < 0 || d > 1) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      throw new ParameterException(String.format("%s is not a probability", value));
    }
  }
}
