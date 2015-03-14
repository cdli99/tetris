## Problem
Given pre-generated rows of random squares, and 9 (or more) predefined pieces of squares,
try to fill up the rows as much as possible with the randomly chosen pieces while keeping 
number of the rows less than a limit (max_row).

## Rule
* The new piece will be put on top of the rows where you control by using direction keys.
* If any row is filled up, the row will be eliminated, and your score will be increased. 
* If the rows equals or greater than max_row, the game is over.

## Predefined Rows
Predifine rows are generated randomly, and some column may be empty, and some are filled.


## Predefined Pieces
The following 9 pieces are provided randomly:

```

OO
OO

O
OOO

 O
OOO

OO
 OO
  
 OO
OO

OOOO

OO
OOO

O
OO

O

```

## Speed
A new piece will be provided periodically (controlled by a timer, which will speed
up chronically).
