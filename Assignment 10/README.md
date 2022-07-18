time: 8
sites used: https://cp-algorithms.com/algebra/binary-exp.html

### Run Time Analysis for 2
The input is three matrices with dimensions nxm mxk and nxk, so it has length n+m+k the matrix multiplication is done in O(mnk) time

It has been stated that the setup of the matrices from the input is not the runtime we are analyzing - so the first loop is not being referenced in this run time analysis

1. initializing the final matrix is done in O(nk) time because we are initializing an array of size n, done in O(n) time, but each array also contains an array of size k that must be initialized as well, done in O(k) time so combining them gives us O(nk) time

2. There is an outer for loop of matrix multiplication that will run n times and has O(mnk) runtime
 - there is an inner loop that will run k times that has O(mk) runtime
 - the multiplication for cells occurs in another method that has O(m) runtime 
   - this method initializes a new int, which is done in constant time -> this is the value that will eventually be returned and placed in a cell
   - next a for loop runs m times and has runtime O(m)
     - each time it runs, constant time arithmetic is applied to the value to be returned -> addition, multiplication, and it takes constant time to get the values involved in these calculations as well because we are referencing their indices

3. Next, checking for equality, this is done for two matrices each of size nxk, and has a runtime of O(nk)
 - to do this, I used java's deep equals which will compare each index in the inner array at the same indices between matrix XY and Z for equality
 - comparing to ints is a constant time operation because ints are a primative type
 - however, we are comparing two arrays, each with nk values, because they are both of size nxk, so nk comparisons has runtime of O(nk)
 - printing out 0 or 1 based on the boolean of deep equals is also done in constant time

### Run Time Analysis for 3
The input has four inputs: a,b,c,p so the length is log a + log b + log c + log p, and has an overall runtime of O(logb) if only considering the calculations portion of the code, as we were told not to include any runtime on parsing*
 
1. calculating the a^b%p is done using another method that has a time complexity of O(logb)
   - first, a mode p is calculated in constant time
   - then the final value of a^b%p will be stored in and returned is initialized to 1, a constant time operation
   - the while loop runs at most logb times and has a time complexity of O(logb)
     - the if statement checking if b is odd is done in constant time because mod and checking for equality are both constant time operators
     - updating the new int at the beginning is done in constant time because we are using multiplication and mod, both constant time operators
     - outside of the if statement, a is updated using multiplication and mod, both constant time operators
     - b is also updated using division, also a constant time operator. This update is how the loop runs at most logb times. This division is actually removing the rightmost bit from b
     
2. Testing for equality between the value calculated in step 2 is done in constant time because ints are primative types
   - printing out is also done in constant time

### Run Time Analysis for 4
The program takes an input n verticies and m edges that is length n+m, and has an overall runtime of O(n^2)  + O(mlogn) (parsing and creating Node objects) + O(n^2) (actually comparing Nodes and their connected Nodes color), this would be simplifies to O(n^2)+O(mlogn)** as described below:

1. initializing a list of size n is done in O(n) time

The program has four loops, the first two of which have to do with parsing the input and creating the objects. However, because I was unsure how this data would have been given to us to avoid parsing (as it could be in problem 2), I still included its description in my runtime analysis

2. The first for loop runs n times, for a total run time of O(n^2)
 - a new object (Node) is initialized  - this is done in O(n) time because setting the number node is constant time but initializing the list of n-1 possible edges this node has, n-1 translates to n which gives us O(n)
 - this new initialization is then added to an already initialized array which is done in constant time
 
3. The second loop runs m+1 times, and has a runtime of O(mlogn)
  - integer.parseInt runs in log to the size of the digit, this is done for both values in the pair, the largest input has length n so this is worst case O(logn)
   - isolating each node from the list is done in constant time, O(1) per node for two nodes
   - updating each node to include the other value as node it is connected to is done in constant time because the list has already been initialized
 
4. the third loop runs n times and has a runtime of O(n)
 - Isolating the node from the list is done in constant time
 - adding the color of the Node is done in constant time because this is a single value
 
5. initializing a Boolean is done in constant time
 
6. the last loop runs, worst case, n times and has a time complexity of O(n^2)
 - getting the array of nodes this node is connected to is done in constant time,  because the list has already been initialized in the Node class
 - this loop has an inner for loop that runs the size of this list, max n-1 times for a runtime of O(n)
   - checking two ints for equality is done in constant time since int is a primitive type
   - updating the boolean is done in constant time
   - printing a value is done in constant time
   - this loop will complete less than max cycles if any Node is connected to another Node of same color
   
   
*O(loga) + O(logb) + O(logc) + O(logp) if considering the parsing portion
**for analysis of 4 I was told by TA Reed that it was a good idea to include the parsing runtime portion in analysis because this could contribute to longer runtime that other, algorithmic, portion of code and there is not a clear way this data would have been already stored in the language of our choice
