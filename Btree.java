/*
 * CS7280 Special Topics in Database Management
 * Project 1: B-tree implementation.
 *
 * You need to code for the following functions in this program
 *   1. Lookup(int value) -> nodeLookup(int value, int node)
 *   2. Insert(int value) -> nodeInsert(int value, int node)
 *   3. Display(int node)
 *
 */

import java.awt.desktop.SystemEventListener;
import java.util.Arrays;
/*
 YU FU-NUID001526550
 CS7280 Project 1
 */
final class Btree {

    /* Size of Node. */
    private static final int NODESIZE = 5;

    /* Node array, initialized with length = 1. i.e. root node */
    private Node[] nodes = new Node[1];

    /* Number of currently used nodes. */
    private int cntNodes;

    /* Pointer to the root node. */
    private int root;

    /* Number of currently used values. */
    private int cntValues;

    /*
     * B tree Constructor.
     */
    public Btree() {
        root = createLeaf();
//      nodes[root].children[0] = createLeaf();
    }

    /*********** B tree functions for Public ******************/

    /*
     * Lookup(int value)
     *   - True if the value was found.
     */
    public boolean Lookup(int value) {

        return nodeLookup(value, root);
    }

    /*
     * Insert(int value)
     *    - If -1 is returned, the value is inserted and increase cntValues.
     *    - If -2 is returned, the value already exists.
     */
    public void Insert(int value) {

        if(nodeInsert(value, root) == -1) cntValues++;
    }


    /*
     * CntValues()
     *    - Returns the number of used values.
     */
    public int CntValues() {

        return cntValues;
    }

    /*********** B-tree functions for Internal  ******************/

    /*
    This function is to calculate the mid index of the array so that we can split the value.
    If NODESIZE is odd or even, the function will return different mid value;
     */
    private int getMid(){
        if(NODESIZE%2 == 0){
            return NODESIZE/2;
        }else{
            return (NODESIZE+1)/2;
        }
    }

    /*
     * find the specified value. If the value exists, returning value is
True.
     * value is the key we are looking for.
     * pointer will point to the node we start at searching.
     *
     */
    private boolean nodeLookup(int value, int pointer) {
        int i = 0;
        //Choose i as a counter to compare the value in the node;
        Node node = nodes[pointer];
        //Set the node according to pointer. If the node is null, we return false
        if(node == null){
            return false;
        }
        //We search the key value in node, if found return true
        for(i = 0; i < node.size;i++){
            if (value < node.values[i]) {
                break;
            }
            if (value == node.values[i]) {
                return true;
            }
        }
        // if the node is leaf node, return false, else we search in its childern nodes
        if (isLeaf(node)) {
            return false;
        } else {
            return nodeLookup(value, node.children[i]);
        }
    }


    /*
    This function is to split the node when it is filled over the limitation.
     */
    private void nodeSplit(Node j,int m,Node k){
        // First we create new node by creating leaf and get the new node id.
        int new_node_id = createLeaf();
        Node node = nodes[new_node_id];
        int mid = getMid();
        node.size = mid-1;
        // Then we set the new node size according to the mid value.
        for(int i = 0; i<mid-1;i++) {
            node.values[i] = k.values[i+mid];
        }
        // Copy the node value from the node k
        if(!isLeaf((k))){
            // if k node is not leaf node, we copy k's children to node
            node.children = new int[NODESIZE+1];
            for(int i = 0; i < mid; i++){
                node.children[i] = k.children[i+mid];
            }
        }
        // Reshape node k's size
        k.size = mid -1;
        //  Put the new node to node j's children nodes
        for(int i = j.size;i >= m+1; i--){
            j.children[i+1] = j.children[i];
        }
        j.children[m+1] = new_node_id;
        // Re-assign node j's values array and put node k's mid value in it
        for (int i = j.size - 1; i >= m; i--) {
            j.values[i + 1] = j.values[i];
        }
        j.values[m] = k.values[mid - 1];
        j.size = j.size + 1;
    }

    // Insert the new value to the node.
    private void insertValue(Node node, int value) {
        // if the node is a leaf node, we re-sort the value array and add the new value size(+1) to the node size.
        if (isLeaf(node)) {
            int i = 0;
            for (i = node.size - 1; i >= 0 && value < node.values[i]; i--) {
                node.values[i + 1] = node.values[i];
            }
            node.values[i + 1] = value;
            node.size = node.size + 1;
        } else {
            // if not, we calculate the i value to see if the value is larger than the node value and then break
            int i = 0;
            for (i = node.size - 1; i >= 0; i--) {
                if (value >= node.values[i]) {
                    break;
                }
            }
            i++;
            // if the node size is equal to the NODESIZE, split the node and re-shape the node
            Node new_node = nodes[node.children[i]];
            if (new_node.size == NODESIZE) {
                nodeSplit(node, i, new_node);
                if (value > node.values[i]) {
                    i++;
                }
            }
            // Do the new insert in children node recursively.
            insertValue(nodes[node.children[i]], value);
        }

    }
    /*
     * nodeInsert(int value, int pointer)
     *    - -2 if the value already exists in the specified node
     *    - -1 if the value is inserted into the node or
     *            something else if the parent node has to be restructured
     */

    private int nodeInsert(int value, int pointer) {
        // return -2 if the value already exists
        if(nodeLookup(value,pointer)){
            return -2;
        }
        // Set node_l according to the pointer.
        Node node_l = nodes[pointer];
        // if the node size is filled, we create new root node and split the value to insert, then return -1.
        if(node_l.size == NODESIZE){
            int new_n_index = initNode();
            Node new_n = nodes[new_n_index];
            root = new_n_index;
            new_n.size = 0;
            new_n.children[0] = pointer;
            nodeSplit(new_n,0,node_l);
            insertValue(new_n,value);
            return -1;
        }else{
            // If the node is not filled, we insert value and return -1.
            insertValue(node_l,value);
            return -1;
        }
    }

    /*
      print out the indexing tree structure under specified node.
     */
    private void treeDisplay(Node node, int level){
        assert (node == null);
        System.out.print( "\n");
        for (int i = 0; i < level + 1; i++) {
            System.out.print("...");
        }
        // Split the tree level according to the ...
        System.out.print("  ");
        for (int i = 0; i < node.size; i++) {
            System.out.print(node.values[i] + " ");
        }
        // if x is not leaf node, continue run display on children nodes.
        if (!isLeaf(node)) {
            for (int i = 0; i < node.size + 1; i++) {
                treeDisplay(nodes[node.children[i]], level + 1);
            }
        }

    }

    /*********** Functions for accessing node  ******************/

    /*
     * isLeaf(Node node)
     *    - True if the specified node is a leaf node.
     *         (Leaf node -> a missing children)
     */
    boolean isLeaf(Node node) {
        return node.children == null;
    }

    /*
     * initNode(): Initialize a new node and returns the pointer.
     *    - return node pointer
     */
    int initNode() {
        Node node = new Node();
        node.values = new int[NODESIZE];
        node.children =  new int[NODESIZE + 1];

        checkSize();
        nodes[cntNodes] = node;
        return cntNodes++;
    }

    /*
     * createLeaf(): Creates a new leaf node and returns the pointer.
     *    - return node pointer
     */
    int createLeaf() {
        Node node = new Node();
        node.values = new int[NODESIZE];

        checkSize();
        nodes[cntNodes] = node;
        return cntNodes++;
    }

    /*
     * checkSize(): Resizes the node array if necessary.
     */
    private void checkSize() {
        if(cntNodes == nodes.length) {
            Node[] tmp = new Node[cntNodes << 1];
            System.arraycopy(nodes, 0, tmp, 0, cntNodes);
            nodes = tmp;
        }
    }
/*
 Main function for testing the Tree and display structure
 */
    public static void main(String[] args) {
        Btree b = new Btree();
        int[] testing = new int[] { 10, 20, 30, 40, 50, 15, 60, 85, 95, 100, 11, 12, 13, 22, 32, 33, 34, 1, 2, 3, 4, 5, 6 };

        for (int i: testing) {
            b.Insert(i);
            System.out.println("Tree:");
            b.treeDisplay(b.nodes[b.root], 0);
            System.out.println("\n");
        }
        System.out.println(testing.length);

        if (b.nodeLookup(3, b.root)) {
            System.out.println("\nfound");
        } else {
            System.out.println("\nnot found");
        }
    }

}



/*
 * Node data structure.
 *   - This is the simplest structure for nodes used in B-tree
 *   - This will be used for both internal and leaf nodes.
 */
final class Node {
    /* Node Values (Leaf Values / Key Values for the children nodes).  */
    int[] values;

    /* Node Array, pointing to the children nodes.
     * This array is not initialized for leaf nodes.
     */
    int[] children;

    /* Number of entries
     * (Rule in B Trees:  d <= size <= 2 * d).
     */
    int size;
}
