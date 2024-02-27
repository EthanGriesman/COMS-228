package hw3;

import java.util.Stack;

/**
 * Represents a message tree used for encoding and decoding messages.
 * 
 * @author Ethan Griesman 6/29/2023
 */
public class MsgTree {
	//
    private char payloadChar;
    //
    private MsgTree left;
    //
    private MsgTree right;
    
    /**
     * Constructor to create a message tree from an encoding string.
     *
     * @param encodingString the encoding string representing the message tree
     */
    public MsgTree(String encodingString) {
    	// If the encodingString is null or its length is less than 2, return early from the constructor
    	if (encodingString == null || encodingString.length() < 2) {
    	    return;
    	}
    	
    	// Create a new stack to store MsgTree nodes
    	Stack<MsgTree> stack = new Stack<>(); 
    	// Initialize the index variable
    	int i = 0; 
    	
        // Set the payload character of the current node
        this.payloadChar = encodingString.charAt(i++);
        // Push the current MsgTree node onto the stack
        stack.push(this);
        // Set the current node to the current instance of MsgTree
        MsgTree curr = this;
        // Initialize the branchType variable to "in"
        String branch = "in"; 

        while (i < encodingString.length()) {
            // Create a new node with the next character from the encoding string
            MsgTree n = new MsgTree(encodingString.charAt(i++));

            if (branch.equals("in")) {
                // Set the left child of the current node
                curr.left = n;

                if (n.payloadChar == '^') {
                    // If the node is an internal node, update the current node and push it to the stack
                    curr = stack.push(n);
                    branch = "in";
                } else {
                    if (!stack.empty()) {
                        // If the stack is not empty, update the current node to the previous node on the stack
                        curr = stack.pop();
                    }
                    branch = "out";
                }
            } else {
                // Set the right child of the current node
                curr.right = n;

                if (n.payloadChar == '^') {
                    // If the node is an internal node, update the current node and push it to the stack
                    curr = stack.push(n);
                    branch = "in";
                } else {
                    if (!stack.empty()) {
                        // If the stack is not empty, update the current node to the previous node on the stack
                        curr = stack.pop();
                    }
                    branch = "out";
                }
            }
        }
    }

    /**
     * Constructor to create a message tree node with the given payload character.
     *
     * @param payloadChar the payload character of the node
     */
    public MsgTree(char payloadChar) {
        this.payloadChar = payloadChar;
        this.left = null;
        this.right = null;
    }

    /**
     * Get the payload character of the node.
     *
     * @return the payload character
     */
    public char getPayloadChar() {
        return payloadChar;
    }

    /**
     * Get the left child of the node.
     *
     * @return the left child node
     */
    public MsgTree getLeft() {
        return left;
    }

    /**
     * Get the right child of the node.
     *
     * @return the right child node
     */
    public MsgTree getRight() {
        return right;
    }

    /**
     * Print the codes (character and corresponding binary code) for the given root node and code string.
     *
     * @param root the root node of the message tree
     * @param code the code string representing the binary code
     */
    public static void printCodes(MsgTree root, String code) {
        System.out.println("character code\n-------------------------");
        for (char ch : code.toCharArray()) {
            getCode(root, ch, "");
            System.out.println("    " + (ch == '\n' ? "\\n" : ch + " ") + "    " + binCode);
        }
    }
    
    //
    private static String binCode;

    /**
     * Recursively find the binary code for the given character in the message tree.
     *
     * @param root the root node of the message tree
     * @param ch   the character to find the binary code for
     * @param path the current binary code path
     * @return true if the character is found, false otherwise
     */
    private static boolean getCode(MsgTree root, char ch, String path) {
    	// Check if the current node is not null
    	if (root != null) {
    		// Check if the payload character of the current node matches the target character
    	    if (root.payloadChar == ch) {
    	    	// Set the binary code to the current path
    	    	binCode = path;
    	        // Return true to indicate a successful match
    	        return true;
    	    }

    	    // Recursively call getCode on the left and right child nodes, appending "0" or "1" to the path
    	    return getCode(root.left, ch, path + "0") || getCode(root.right, ch, path + "1");
    	}
    	// Return false if the current node is null, indicating no match was found
    	return false;
    }

    /**
     * Decode the given message using the provided message tree.
     *
     * @param codes the message tree representing the codes
     * @param msg   the encoded message to decode
     */
    public void decode(MsgTree codes, String msg) {
    	// Print a header indicating the start of the message
        System.out.println("MESSAGE:");
        // Set the current node to the root of the code tree
        MsgTree current = codes;
        // Create a StringBuilder to store the decoded message
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < msg.length(); i++) {
            char ch = msg.charAt(i);
            // Traverse the code tree based on the character
            current = (ch == '0') ? current.left : current.right;

            if (current != null && current.payloadChar != '^') {
            	// Append the payload character to the StringBuilder
                sb.append(current.payloadChar);
                // Reset the current node to the root of the code tree
                current = codes;
            }
        }

        // Print the decoded message
        System.out.println(sb.toString());
        // Calculate and display the statistics for the decoding
        calculateStatistics(msg, sb.toString());
    }

    /**
     * Calculate statistics for the encoded and decoded messages.
     *
     * @param encodeStr the original encoded message
     * @param decodeStr the decoded message
     */
    private void calculateStatistics(String encodeStr, String decodeStr) {
        System.out.println("STATISTICS:");
        System.out.println(String.format("Avg bits/char:\t%.1f", encodeStr.length() / (double) decodeStr.length()));
        System.out.println("Total Characters:\t" + decodeStr.length());
        System.out.println(String.format("Space Saving:\t%.1f%%", (1d - decodeStr.length() / (double) encodeStr.length()) * 100));
    }
}
