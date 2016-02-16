# Automatic-Detection-of-Insulting-Text
Using principles of Natural Language Processing

This project was done as a part of the coursework for the class 'Natural Language Processing'. 

The software uses Stanford Natural Language Processing tools for parsing and tagging the sentences. 
The tools used can be found at: http://nlp.stanford.edu/software/

The lexicon of ‘bad’ words was obtained from :https://www.kaggle.com/c/detecting-insults-in-social-commentary/forums/t/2744/what-did-you-use?page=2. 
This list was then modified manually.

Methodology:
- In order to distinguish between information and insult, NLP principles were used to create a set of rules.
- The rules study the structure of the sentence and the relationship between the words in the sentence to perform this classification.
- Annotation [1] is used in order to store some additional information about each word in the sentence.
      Each word in the sentence is given an attribute if it belongs to any of the several predefined categories.
      For the purpose of this class project, a file holding about 5 – 10 words in each category was created. 
      In order to increase the accuracy of the software, additional words should be added to the lexicon.

Reference:

[1]A. Mahmud, K. Z. Ahmed and M. Khan, "Detecting flames and insults in text," in 6th International Conference on Natural Language Processing , 2008.
