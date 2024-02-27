package hw2;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implementation of the list interface based on linked nodes
 * that store multiple items per node. Rules for adding and removing
 * elements ensure that each node (except possibly the last one)
 * is at least half full.
 * 
 * @author Ethan Griesman 6/21/2023
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E> {
	
	/**
     * Default number of elements that may be stored in each node.
     * Could also be referred to as "capacity"
     */
    private static final int DEFAULT_NODE_SIZE = 4;

    /**
     * Number of elements that can be stored in each node.
     */
    private final int nodeSize;

    /**
     * Dummy node for head. It should be private but set to public here only
     * for grading purposes. In practice, you should always make the head of a
     * linked list a private instance variable.
     */
    public Node head;

    /**
     * Dummy node for tail.
     */
    private Node tail;

    /**
     * Number of elements in the list.
     */
    private int listSize;

	/**
     * Constructs an empty list with the default node size.
     */
    public StoutList() {
        this(DEFAULT_NODE_SIZE);
    }

  /**
   * Constructs an empty list with the given node size.
   * 
   * @param nodeSize number of elements that may be stored in each node, must be 
   *   an even number
   */
  public StoutList(int nodeSize) {
	  // Check if the nodeSize is valid
	  if (nodeSize <= 0 || nodeSize % 2 != 0) throw new IllegalArgumentException();
	    
	  // Create dummy nodes
	  head = new Node();
	  tail = new Node();
	  
	  // Set the next and previous references of the dummy nodes
	  head.next = tail;
	  tail.previous = head;
	    
	  // Set the nodeSize
	  this.nodeSize = nodeSize;
	}
  
  /**
   * Constructor for grading only. Fully implemented.
   *
   * @param head     The head node of the list.
   * @param tail     The tail node of the list.
   * @param nodeSize The maximum size of each node in the list.
   * @param size     The current size of the list.
   */
  public StoutList(Node head, Node tail, int nodeSize, int size) {
      this.head = head;
      this.tail = tail;
      this.nodeSize = nodeSize;
      this.listSize = size;
  }
  
  /**
   * Returns number of elements within the list
   * 
   * @return size number of elements in list
   */
  @Override
  public int size() {
	  return listSize;
  }
  
  /**
   * Adds an item to the end of the list if it is not already present.
   *
   * @param item the item to add
   * @return true if the item was added, false if it is already in the list
   * @throws NullPointerException if the item is null
   */
  @Override
  public boolean add(E item) {
	  // Check if item is null
	  if (item == null) {
		  throw new NullPointerException("Item can't be null.");
		  }

	  // Check if item already exists
	  if (contains(item)) {
		  return false;
		  }

	  // Get the last node
	  Node lastNode = tail.previous;

	  // If the last node is the head or it's full, create a new node
	  if (lastNode == head || lastNode.count == nodeSize) {
		  // Create a new node and update the list pointer
		  lastNode = new Node();
		  lastNode.previous = tail.previous;
		  lastNode.next = tail;
		  tail.previous.next = lastNode;
		  tail.previous = lastNode;
		  }

	  // Add the item to the last node
	  lastNode.addItem(item);

	  // Increase the size of the list
	  listSize++;

	  return true;
	  }

  /**
   * Inserts an element at the specified position in the list.
   *
   * @param pos  the position at which to insert the element
   * @param item the element to insert
   * @throws NullPointerException      if the item is null
   * @throws IndexOutOfBoundsException if the position is out of range (pos < 0 || pos > size())
   */
  public void add(int pos, E item) {
      // Check if item is null
      if (item == null) {
          throw new NullPointerException("Item can't be null.");
      }

      // Check if the position is invalid
      if (pos < 0 || pos > listSize) {
          throw new IndexOutOfBoundsException("Invalid position: " + pos);
      }

      // If the position is at the end of the list, simply call the add method
      if (pos == listSize) {
          add(item);
          return;
      }

      // Find the node and offset for the given position
      Node node = head.next;
      int count = 0;
      while (count + node.count <= pos) {
          count += node.count;
          node = node.next;
      }
      int offset = pos - count;

      // Call the add method with the specific node and offset
      add(node, offset, item);
  }

  /**
   * Adds an item to the specified node at the given offset.
   *
   * @param node   the node to add the item to
   * @param offset the offset at which to add the item
   * @param item   the item to add
   */
  private void add(Node node, int offset, E item) {
      if (node.count < nodeSize) {
          // Shift elements to the right to create space at the specified offset
          for (int i = node.count; i > offset; i--) {
              node.data[i] = node.data[i - 1];
          }
          node.data[offset] = item;
          node.count++;
      } else {
          Node newNode = new Node();
          int splitOffset = nodeSize / 2;

          if (offset <= splitOffset) {
              // Move elements from the specified offset to the end of the node to the new node
              moveElementsToNewNode(node, newNode, splitOffset, node.count - 1);
              node.count = splitOffset;

              // Shift elements to the right to create space at the specified offset in node n
              for (int i = node.count; i > offset; i--) {
                  node.data[i] = node.data[i - 1];
              }
              node.data[offset] = item;
              node.count++;
          } else {
              // Move elements from the specified offset to the end of the node to the new node
              moveElementsToNewNode(node, newNode, splitOffset + 1, node.count - 1);
              node.count = splitOffset + 1;

              int newOffset = offset - splitOffset - 1;
              newNode.addItem(newOffset, item);
          }

          newNode.next = node.next;
          newNode.previous = node;
          if (node.next != null) {
              node.next.previous = newNode;
          }
          node.next = newNode;
      }

      // increase size of list
      listSize++;
  }

  /**
   * Moves elements from the source node to the target node within the specified index range.
   *
   * @param sourceNode  the source node to move elements from
   * @param targetNode  the target node to move elements to
   * @param startIndex the starting index of elements to move
   * @param endIndex   the ending index of elements to move
   */
  private void moveElementsToNewNode(Node sourceNode, Node targetNode, int startIndex, int endIndex) {
      for (int i = startIndex; i <= endIndex; i++) {
          targetNode.data[i - startIndex] = sourceNode.data[i];
          sourceNode.data[i] = null;
      }
      targetNode.count = endIndex - startIndex + 1;
      sourceNode.count = startIndex;
  }
  
  /**
   * Removes the element at the specified position in the list.
   *
   * @param pos the position of the element to remove
   * @return the removed element
   * @throws IndexOutOfBoundsException if the position is out of range (pos < 0 || pos >= size())
   */
  public E remove(int pos) {
      // Check if the position is invalid
      if (pos < 0 || pos >= listSize) {
          throw new IndexOutOfBoundsException("Invalid position: " + pos);
      }

      // Find the node and offset for the given position
      Node node = head.next;
      int count = 0;
      while (count + node.count <= pos) {
          count += node.count;
          node = node.next;
      }
      int offset = pos - count;
      E removedItem = node.data[offset];

      // Case 1: Last node with one element
      if (node == tail.previous && node.count == 1) {
          // Remove the node
          node.previous.next = tail;
          tail.previous = node.previous;
      }
      // Case 2: Last node or node with more than M/2 elements
      else if (node == tail.previous || node.count > nodeSize / 2) {
          // Shift the elements after the removed item
          for (int i = offset; i < node.count - 1; i++) {
              node.data[i] = node.data[i + 1];
          }
          node.data[node.count - 1] = null;
          node.count--;
      }
      // Case 3: Node with at most M/2 elements
      else {
          Node succ = node.next;

          if (succ.count > nodeSize / 2) {
              // Move the first element from the successor to the current node
              node.data[offset] = succ.data[0];
              for (int i = 0; i < succ.count - 1; i++) {
                  succ.data[i] = succ.data[i + 1];
              }
              succ.data[succ.count - 1] = null;
              succ.count--;
          } else {
              // Merge the current node with the successor
              for (int i = 0; i < succ.count; i++) {
                  node.data[node.count + i] = succ.data[i];
              }
              node.count += succ.count;
              node.next = succ.next;
              if (succ.next != null) {
                  succ.next.previous = node;
              }
          }
      }
      // Decrease the size of the list
      listSize--;
      return removedItem;
  }
	
  /**
   * Sorts all elements in the stout list in non-decreasing order. The list elements are copied
   * into an array, and the list is then cleared. The array is sorted using the insertion sort
   * algorithm. Finally, the sorted elements are copied back to the stout list, creating new
   * nodes for storage. After sorting, all nodes except (possibly) the last one will be full.
   */
  public void sort() {
      // Create an array to store the elements
      @SuppressWarnings("unchecked")
      E[] sortDataList = (E[]) new Comparable[listSize];
      
      // Index variable for the array
      int tempIndex = 0;
      
      // Start from the first node
      Node temp = head.next;

      // Traverse the list and copy elements to the array
      while (temp != tail) {
          // Iterate over the items in the current node
          for (int i = 0; i < temp.count; i++) {
              // Copy the item to the array
              sortDataList[tempIndex] = (E) temp.data[i];
              // Increment the index
              tempIndex++;
          }
          // Move to the next node
          temp = temp.next;
      }

      // Sort the array using insertion sort
      insertionSort(sortDataList, new ElementComparator());

      // Clear the current list
      listSize = 0;

      // Add the sorted elements back to the list
      for (int i = 0; i < sortDataList.length; i++) {
          add(sortDataList[i]);
      }
  }

  /**
   * Sorts all elements in the StoutList in non-increasing order using bubble sort.
   * After sorting, all but (possibly) the last nodes must be filled with elements.
   * Comparable<? super E> must be implemented for calling bubbleSort().
   */
  public void sortReverse() {
	    // Step 1: Copy elements from the doubly linked list to an array
	    @SuppressWarnings("unchecked")
		E[] sortList = (E[]) new Comparable[listSize];
	    Node current = head.next;
	    int index = 0;
	    while (current != tail) {
	        for (int i = 0; i < current.count; i++) {
	            sortList[index++] = current.data[i];
	        }
	        current = current.next;
	    }

	    // Step 2: Sort the array using the bubbleSort() method
	    bubbleSort(sortList);

	    // Step 3: Clear the current list and add elements in non-increasing order
	    listSize = 0;
	    // Add elements in non-increasing order from the sorted array
	    for (int i = sortList.length - 1; i >= 0; i--) {
	        add(sortList[i]);
	    }
	}

  /**
   * Returns an iterator over the elements in the list.
   *
   * @return an iterator over the elements in the list
   */
  @Override
  public Iterator<E> iterator() {
      return new StoutListIterator();
  }

  /**
   * Returns a list iterator over the elements in the list.
   *
   * @return a list iterator over the elements in the list
   */
  @Override
  public ListIterator<E> listIterator() {
      return new StoutListIterator();
  }

  /**
   * Returns a list iterator over the elements in the list, starting at the specified position.
   *
   * @param index the index to start the iterator from
   * @return a list iterator over the elements in the list, starting at the specified position
   * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size())
   */
  @Override
  public ListIterator<E> listIterator(int index) {
      if (index < 0 || index > listSize) {
          throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + listSize);
      }

      return new StoutListIterator(index);
  }
  
  /**
   * Private class that allows sequential access of elements within a collection 
   * by moving through indices 0 to (size - 1), retrieving each element along the way
   * 
   * @author Ethan Griesman 6/21/2023
   *
   */
  private class StoutIterator implements Iterator<E> {
	  // Keeps track of the current index
	  private int currIndex; 

	  public StoutIterator() {
		  // Initializes the current index to 0
		  this.currIndex = 0; 
		  }

	  @Override
	  public boolean hasNext() {
		  // Returns true if there are more elements in the list
		  return currIndex < size(); 
		  }

	  @Override
	  public E next() {
		  if (!hasNext()) {
			  // Throws an exception if there are no more elements
			  throw new NoSuchElementException("No more elements in the list."); 
			  }
		  // Returns the next element and increments the current index
		  return get(currIndex++); 
		  }
	  }
  
  /**
   * Returns a string representation of this list showing
   * the internal structure of the nodes.
   */
  public String toStringInternal() {
	  return toStringInternal(null);
  }

  /**
   * Returns a string representation of this list showing the internal
   * structure of the nodes and the position of the iterator.
   *
   * @param iter
   *            an iterator for this list
   */
  public String toStringInternal(ListIterator<E> iter) 
  {
      int count = 0;
      int position = -1;
      if (iter != null) {
          position = iter.nextIndex();
      }

      StringBuilder sb = new StringBuilder();
      sb.append('[');
      Node current = head.next;
      while (current != tail) {
          sb.append('(');
          E data = current.data[0];
          if (data == null) {
              sb.append("-");
          } else {
              if (position == count) {
                  sb.append("| ");
                  position = -1;
              }
              sb.append(data.toString());
              ++count;
          }

          for (int i = 1; i < nodeSize; ++i) {
             sb.append(", ");
              data = current.data[i];
              if (data == null) {
                  sb.append("-");
              } else {
                  if (position == count) {
                      sb.append("| ");
                      position = -1;
                  }
                  sb.append(data.toString());
                  ++count;

                  // iterator at end
                  if (position == listSize && count == listSize) {
                      sb.append(" |");
                      position = -1;
                  }
             }
          }
          sb.append(')');
          current = current.next;
          if (current != tail)
              sb.append(", ");
      }
      sb.append("]");
      return sb.toString();
  }

  /**
   * Node type for this list.  Each node holds a maximum
   * of nodeSize elements in an array.  Empty slots
   * are null.
   * 
   * @author Ethan Griesman 6/21/2023
   */
  private class Node {
	  
    /**
     * Array of actual data elements.
     */
    public E[] data = (E[]) new Comparable[nodeSize];
    
    /**
     * Link to next node.
     */
    public Node next;
    
    /**
     * Link to previous node;
     */
    public Node previous;
    
    /**
     * Index of the next available offset in this node, also 
     * equal to the number of elements in this node.
     */
    public int count;

    /**
     * Adds an item to this node at the first available offset.
     * Precondition: count < nodeSize
     * @param item element to be added
     */
    void addItem(E item)
    {
      if (count >= nodeSize)
      {
        return;
      }
      data[count++] = item;
      //useful for debugging
      System.out.println("Added " + item.toString() + " at index " + count + " to node "  + Arrays.toString(data));
    }
 
	/**
     * Adds an item to this node at the indicated offset, shifting
     * elements to the right as necessary.
     * 
     * Precondition: count < nodeSize
     * @param offset array index at which to put the new element
     * @param item element to be added
     */
    void addItem(int offset, E item)
    {
      if (count >= nodeSize)
      {
    	  return;
      }
      for (int i = count - 1; i >= offset; --i)
      {
        data[i + 1] = data[i];
      }
      ++count;
      data[offset] = item;
      //useful for debugging 
      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
    }

    /**
     * Deletes an element from this node at the indicated offset, 
     * shifting elements left as necessary.
     * Precondition: 0 <= offset < count
     * @param offset
     */
    void removeItem(int offset)
    {
      E item = data[offset];
      for (int i = offset + 1; i < nodeSize; ++i)
      {
        data[i - 1] = data[i];
      }
      data[count - 1] = null;
      --count;
    }    
  }
  
  /**
   * Helper class to represent a specific point of the list
   * 
   * @author Ethan Griesman 6/21/2023
   */
  private class NodeInfo {
	  
	  // Node reference
	  public Node node; 	 
	  
	  // Offset within the node
      public int offset;     
      
      /**
       * Constructor with information regarding each node
       * @param node
       * @param offset
       */
      public NodeInfo(Node node, int offset) {
    	  // Assign the node reference
          this.node = node;
          
          // Assign the offset
          this.offset = offset;
      }
  }

  /**
   * Helper method to locate a specific item in the list.
   *
   * @param pos position of the item that needs information
   * @return NodeInfo of the specific point in the list
   * @throws IndexOutOfBoundsException if the position is out of bounds
   */
  private NodeInfo find(int pos) {
      // Check if the position is out of bounds
      if (pos < 0 || pos >= listSize) {
          throw new IndexOutOfBoundsException("Invalid position: " + pos);
      }

      Node currNode = head.next;
      int count = 0;

      // Traverse the list to find the node containing the specified position
      while (currNode != null) {
          if (pos < count + currNode.count) {
              // Found the node, calculate the offset within the node
              return new NodeInfo(currNode, pos - count);
          }

          // Move to the next node
          count += currNode.count;
          currNode = currNode.next;
      }

      // Position not found, throw an exception
      throw new NoSuchElementException("Position not found: " + pos);
  }

  /**
   * Private class for iterator implementation
   * 
   * @param <E> the type of elements in the list
   * @author Ethan Griesman 6/21/2023
   *
   */
  private class StoutListIterator implements ListIterator<E> {
	  
	  // constants for iterator actions   
	  final int ACTION_PREV = 0;
	  final int ACTION_NEXT = 1;
	  
	  // current position
	  private int currPos;
	  
	  // array form of the data list
	  private E[] listData;
	  
	  // Last action performed
	  private int lastAction;
	  
	  /**
	   * Default constructor 
	   */
	  public StoutListIterator() {
		  // Initialize the current position and last action
		  currPos = 0;
		  // Convert the StoutList to an array representation
		  toArray();
		  }
	  
	  /**
	   * Takes the StoutList and puts its data into dataList in an array form.
	   */
	  private void toArray() {
	      // Create a new array to store the data
	      listData = (E[]) new Comparable[listSize];

	      // Initialize the index to 0
	      int i = 0;

	      // Start from the first node (after the head)
	      Node curr = head.next;

	      // Iterate until reaching the tail node or when curr becomes null
	      while (curr != null && curr != tail) {
	          // Get the count of elements in the current node
	          int nodeSize = curr.count;

	          // Check if the destination array has enough capacity
	          if (i + nodeSize > listData.length) {
	              // Expand the capacity of the destination array
	              int newLen = Math.max(i + nodeSize, listData.length * 2);
	              listData = Arrays.copyOf(listData, newLen);
	          }

	          // Copy elements from the current node to the listData array
	          System.arraycopy(curr.data, 0, listData, i, nodeSize);

	          // Increment the index by the node size
	          i += nodeSize;

	          // Move to the next node
	          curr = curr.next;
	      }

	      // Adjust the size of the dataList array to match the final number of elements
	      listData = Arrays.copyOf(listData, i);
	  }

	  /**
	   * Constructor finds node at a given position.
	   * @param pos
	   */
	  public StoutListIterator(int pos) {
		  currPos = pos;
		  toArray();
	  }
    
    /**
     * Checks if there is a next element in the iterator.
     *
     * @return true if there is a next element, false otherwise
     */
    @Override
    public boolean hasNext() {
    	return currPos < listSize;
    }
    
    /**
     * Returns the next element in the iterator and advances the iterator position.
     *
     * @return the next element
     * @throws NoSuchElementException if there are no more elements in the iterator
     */
    @Override
    public E next() {
        // Check if there are more elements in the iterator
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements in the iterator.");
        }

        // Set the last action performed to ACTION_NEXT
        lastAction = ACTION_NEXT;

        // Retrieve the element at the current position and increment the position
        E element = listData[currPos++];
        return element;
    }
    
    /**
     * Removes the last element returned by the iterator. This operation is not supported in this implementation.
     *
     * @throws UnsupportedOperationException always thrown since remove is not supported
     */
    @Override
    public void remove() {
        // Check if the last action was a valid action
        if (lastAction == ACTION_NEXT || lastAction == ACTION_PREV) {
            NodeInfo nodeInfo;

            // Determine the appropriate node and offset based on the last action
            if (lastAction == ACTION_NEXT) {
                nodeInfo = find(currPos - 1);
                lastAction = ACTION_PREV;
            } else {
                nodeInfo = find(currPos);
                lastAction = ACTION_NEXT;
            }

            // Remove the item from the node, update position, and refresh array representation
            nodeInfo.node.removeItem(nodeInfo.offset);
            currPos--;
            toArray();
        } else {
            // Throw an exception if there is no element to remove
            throw new IllegalStateException("No element to remove.");
        }
    }

    /**
     * Checks if there is a previous element in the iterator.
     *
     * @return true if there is a previous element, false otherwise
     */
	@Override
	public boolean hasPrevious() {
		return currPos > 0;
	}
	 
    /**
     * Returns the previous element in the iterator and moves the iterator position backward.
     *
     * @return the previous element
     * @throws NoSuchElementException if there is no previous element in the iterator
     */
	@Override
	public E previous() {
	    // Check if there are no more elements in the iterator
	    if (!hasPrevious()) {
	        throw new NoSuchElementException("No more elements in the iterator.");
	    }

	    // Update the last action performed
	    lastAction = ACTION_PREV;

	    // Retrieve and return the previous element
	    return listData[--currPos];
	}
	
    /**
     * Returns the index of the next element.
     *
     * @return the index of the next element
     */
	@Override
	public int nextIndex() {
		return currPos;
	}
	
    /**
     * Returns the index of the previous element.
     *
     * @return the index of the previous element
     */
	@Override
	public int previousIndex() {
		return currPos - 1;
	}
	
	/**
	 * Replaces the last element returned by the iterator with the specified element.
	 *
	 * @param e the element to replace the last returned element with
	 * @throws UnsupportedOperationException if the set operation is not supported
	 */
	@Override
	public void set(E e) {
	    // Check if the last action was either NEXT or PREV
	    if (lastAction == ACTION_NEXT || lastAction == ACTION_PREV) {
	        // Retrieve the Node and offset for the adjusted current position
	        NodeInfo infoNode = find(getAdjustedCurrPos());
	        Node node = infoNode.node;
	        int offset = infoNode.offset;

	        // Set the specified element in the respective node and update listData
	        node.data[offset] = e;
	        listData[getAdjustedCurrPos()] = e;
	    } else {
	        // No valid element to set, throw an exception
	        throw new IllegalStateException("No element to set.");
	    }
	}

	/**
	 * Helper method for set: returns adjusted current position based on the last action performed.
	 *
	 * @return the adjusted current position
	 * @throws IllegalStateException if there is no element to set
	 */
	private int getAdjustedCurrPos() {
	    // Calculate the adjusted current position based on the last action
	    if (lastAction == ACTION_NEXT) {
	        return currPos - 1;
	    } else if (lastAction == ACTION_PREV) {
	        return currPos;
	    } else {
	        // No valid element to set, throw an exception
	        throw new IllegalStateException("No element to set.");
	    }
	}
	
	/**
	 * Adds the specified element to the list at the current position of the iterator.
	 *
	 * @param e the element to be added
	 * @throws NullPointerException if the specified element is null
	 */
	public void add(E e) {
	    // Add the element to the list at the current position, assuming it isn't null
	    StoutList.this.add(currPos++, Objects.requireNonNull(e, "Element cannot be null"));

	    // Update the array representation of the list
	    toArray();
	}
	
    // Other methods you may want to add or override that could possibly facilitate 
    // other operations, for instance, addition, access to the previous element, etc.
    // 
    // ...
    // 
  }

  /**
   * Performs an insertion sort algorithm on the given array.
   *
   * @param arr the array to be sorted
   * @param comp the comparator used for element comparison
   */
  private void insertionSort(E[] arr, Comparator<? super E> comp) {
      for (int i = 1; i < arr.length; i++) {
          // Store the current element
          E key = arr[i];
          // Initialize the index of the previous element
          int j = i - 1;

          // Shift elements greater than the key to the right
          while (j >= 0 && comp.compare(arr[j], key) > 0) {
              arr[j + 1] = arr[j];
              j--;
          }

          // Insert the key in the correct position
          arr[j + 1] = key;
      }
  }
  
  /**
   * Sorts the given array using the bubble sort algorithm.
   *
   * @param arr the array to be sorted
   */
  private void bubbleSort(E[] arr) {
      int length = arr.length;

      for (int i = 0; i < length - 1; i++) {
          for (int j = 0; j < length - i - 1; j++) {
              // Compare adjacent elements
              if (arr[j].compareTo(arr[j + 1]) < 0) {
                  // Swap elements if they are out of order
                  E temp = arr[j];
                  arr[j] = arr[j + 1];
                  arr[j + 1] = temp;
              }
          }
      }
  }

  /**
   * Custom Comparator class to be used by insertion sort.
   *
   * @param <E> the type of elements to be compared
   * @author Ethan Griesman 6/21/2023
   */
  private class ElementComparator implements Comparator<E> {
      /**
       * Compares two elements for ordering.
       *
       * @param o1 the first element to be compared
       * @param o2 the second element to be compared
       * @return a negative integer, zero, or a positive integer as the first element is less than, equal to,
       *         or greater than the second element
       */
      @Override
      public int compare(E o1, E o2) {
          return o1.compareTo(o2);
      }
  }


}
