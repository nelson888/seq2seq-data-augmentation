# Soft Contextual Data Augmentation

This is my implementation/interpretation of the SCDA, explained in [this paper](https://arxiv.org/pdf/1905.10523.pdf).

This method can be used to augment NLP dataset (text datasets), when you can't find a lot of real data.


## Augmentation Strategies

I use 4 of the 5 ways to augment data explained in the paper.

Each of theses strategies are used to add new entries in the dataset. For all methods
(except Swap), we go through each words and there is a probability of `γ` (gamma) that 
the word is modified. Default value is 0.15

### Duplication
Duplicate original entries (useful to increase weight of original entries) n time.

### Swap
Swap words in the entry in a window of size k.

### Dropout
Randomly drop words in the entry.

### Blank/Placeholder
Randomly replace words with a `_` placeholder.

### Smooth
Randomly replace words with words having a similar frequency in the given dataset (use of an Unigram Diagram).
This strategy can raise an exception if a word hasn't been found on the unigram distribution.


There is also a/some duplication of the original entry (unmodified) in order for it to have
more weight.

## How to Use
Here is the list of the parameters of this program

### Required parameters
- `--source-file`: the training set in source language
- `--target-file`: the training set in target language
You must provide on of the two following arguments
- `--dictionary-file`: The dictionary file of the source language. The dictionary file should
contain in each line one word followed by the number of its occurrence in the dataset
- `--distribution-file`: The distribution file of the source language.
The distribution file should contain in each line a word and its distribution (0 < distribution <= 1)

### Optional parameters
- `-g`, `--gamma`: The probability of which to replace a word by an Augmentation Strategy (default: `0.15`)
- `-w`, `--window-size`: The window size to swap words (for the Swap strategy) (default: `3`)
- `-l`, `--log-errors`: whether to log errors or not. Errors can happen on the Smooth strategy, 
if a word is not found on the unigram distribution (default: `false`)
- `-d`, `--duplication`: The number of duplication to write (default: `0`)