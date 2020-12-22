# text-matcher-demo
a little HW assignment. testing the design and implementation of data pipeline

# SomeID - Backend Dev Task

Design and implement a simple Java program to find specific strings in a large text. The
program should be composed of the following modules:

1. The main module - reads a large text file in parts (e.g. 1000 lines in each part)  
   and sends each part (as string) to a ​ _matcher_ ​.  
   After all matchers completed, it calls the _aggregator_ ​to combine and print the results
2. The matcher - gets a text string as input and searches for matches of a given set of
    strings. The result is a map from a word to its location(s) in the text
3. The aggregator - aggregates the results from all the matchers and prints the results.
   For this task, please use the text at ​http://norvig.com/big.txt​, and the  
   strings to find should be the 50 most common English first names:
   _James,John,Robert,Michael,William,David,Richard,Charles,Joseph,Thomas,Christopher,  
   Daniel,Paul,Mark,Donald,  George,Kenneth,Steven,Edward,Brian,Ronald,Anthony,Kevin,  
   Jason,Matthew,Gary,Timothy,Jose,Larry,Jeffrey,Frank,Scott,Eric,Stephen,Andrew,Raymond,  
   Gregory,Joshua,Jerry,Dennis,Walter,Patrick,Peter,Harold,Douglas,Henry,Carl,Arthur,Ryan,Roger_
     
     
Example of one line from the program output based on the input above:  
Timothy --> [[lineOffset=13000, charOffset=19775], [lineOffset=13000, charOffset=42023]]  
There should be several concurrent matchers (i.e each matcher should run in a separate
thread).  
The results should be printed (in no particular order) **after all text pieces have been
processed**.  
Please provide a main method that executes a sample run.  
This task should be done alone.​ You ​can ​make use of any written  
or web resource you find,
incl. 3rd party libraries).  
The code should work, and be as clean and efficient as you think necessary.


