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
Randomly replace words with words having a similar frequency in the given dataset (use of an Unigram Diagram)


There is also a/some duplication of the original entry (unmodiffied) in order for it to have
more weight.

## How to Use

compile the program with maven and launch it with the `--help` options to 
have more information
