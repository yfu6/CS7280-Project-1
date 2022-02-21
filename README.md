# CS7280-Project-1 

The assignment for CS7280

YU FU-NUID001526550

The BTree code is created based on the source code structure which is provided by the project materials. We set a deafault NODESIZE for 5 and it can be changed.

The Tree structure is shown by splitting'...', different level node would have different number of'...'.


# One example of the Tree shown is :

...  9 //////////////////////first level

......  3 6/////////////////second level

.........  1 2 ///////////// third level

.........  4 5 ///////////// third level

.........  7 8 ///////////// third level

......  12 15 ///////////////second level

.........  10 11 ///////////// third level

.........  13 14 /////////////third level

.........  16 17 18 19 20 // third level


# The code has accomplished these three functions according to the requirements:

• nodeLookup(int value): find the specified value. If the value exists, returning value is 
True.

• nodeInsert(int value): insert the specified value. 

• Display(int node): print out the indexing tree structure under specified node.

# Additional Function:

• nodeSplit(Node j,int m,Node k): split the node when it is filled over the limitation.

• insertValue(Node node, int value): insert the data value to  node. 

• getMid(): calculate the mid index of the array so that we can split the value.


# Reference List:

Tree's Data Structure, https://www.programiz.com/dsa/trees

B Tree's Visualization, https://www.cs.usfca.edu/~galles/visualization/BTree.html

