# Stream Sampler

A program to process an input stream of characters and 
create a random sample of mentioned size.

## The problem statement

The stream-sampler that receives and processes an input stream consisting of single
characters.
The stream is of unknown and possibly very large length and the program should work regardless of the size
of the input.
The program should take the sample size as a parameter and generate a random representative sample
using that many characters

## Sampling Approach

As mentioned in the problem statement, the input stream or iterable is of
possibly unknown size/extremely large or infinite. Reservoir sampling is
used to approach this problem.

Reservoir sampling is a family of randomized algorithms for randomly choosing a
sample of `k` items from a list `S` containing `n` items, where `n` is either a
very large or unknown number. Typically `n` is large enough that the list
doesn't fit into main memory. In this context, the sample of `k` items will
be referred to as ***sample*** and the list `S` as ***stream***.

Reference - https://en.wikipedia.org/wiki/Reservoir_sampling

Algorithm X reference - https://dl.acm.org/doi/10.1145/3147.3165

## Assumptions

The following assumptions were made with regard to the input to this program.

- The input of character stream is terminated by line
endings in an interactive console for stdin. So, the
  input is read line-by-line and each line is converted
  to a stream of characters for sampling.
  
- Also in an interactive console, if the sample size is
greater than the input size of characters in one line then
  the program will wait for the next line of characters.
  
- So the input is fed to the sampler line-by-line, and the
next line is only considered if the desired sample size is not 
  reached by the current state of sample.
  
For example - 

```
if sample size is 5 and the input is provided
in 2 lines where each line has 10 chars, then only
the first line would be considered for sampling.

vofnnv3hf08vnnv0892w4n09vnuw309rvn02
ck0wcwovnjdk39ru2ddgfrfgeorgofnvnffod

Sampling would only consider the first line as input since
it has more chars than the sample size.
```

## Build and Execution

The following instructions can be used to build the program
and execute it - 

### Build

This is a `maven` project, and it's written using `java 11`. 

- maven 3.6+
- Java 11

To build the JAR file -

```shell
mvn clean package

or 

make jar
```

### Execution

The application can be executed using either interactive console
or pipe the input to the program. Any of the following
method could be employed - 

For sample size of `5` with a stream of chars
```shell
echo THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG | java -jar target/random-stream-sampler-1.0.jar 5
```

Using `urandom` and sample size of `10`

```shell
dd if=/dev/urandom count=100 bs=100KB | base64 | java -jar target/random-stream-sampler-1.0.jar 10
```

Pipe file content to stdin with sample size of `echo THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG | java -jar target/random-stream-sampler-1.0.jar 57`

```shell
java -jar target/random-stream-sampler-1.0.jar 7 < sample2.txt
```

For Interactive console with sample size `6` - 

```shell
java -jar target/random-stream-sampler-1.0.jar 6
THEQUICKBROWNFOXJUMPSOVERTHELAZYDOG


```
***NOTE: In an interactive console an additional line ending is
required to reach the limiting condition of the stream.*** 

***So, technically an additional `Enter` (twice) has to be pressed to initiate the sampling.***

## Testing

A few unit and benchmark tests have been incorporated using
Junit5. To run the tests - 

```shell
mvn clean test
```