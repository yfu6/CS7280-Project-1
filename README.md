# CS7280-Project-1
The assignment for CS7280
YU FU-NUID001526550
The BTree code is created based on the source code structure which is provided by the project materials. We set a deafault NODESIZE for 5 and it can be changed.
The Tree structure is shown by splitting'...', different level node would have different number of'...'.

One example of the Tree shown is :
...  9 //first level
......  3 6  //second level
.........  1 2  // third level
.........  4 5 
.........  7 8 
......  12 15 
.........  10 11 
.........  13 14 
.........  16 17 18 19 20 

The code has accomplished these three functions according to the requirements:
• Lookup(int value): find the specified value. If the value exists, returning value is 
True.
• Insert(int value): insert the specified value. 
• Display(int node): print out the indexing tree structure under specified node.

Reference List:
Tree's Data Structure, https://www.programiz.com/dsa/trees
B Tree's Visualization, https://www.cs.usfca.edu/~galles/visualization/BTree.html
