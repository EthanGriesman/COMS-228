package hw3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Main class to decode a message using an encoding scheme and a binary code.
 * 
 * @author Ethan Griesman 6/29/2023
 */
public class Main {
	
    /**
     * Entry point of the program.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Please enter the filename to decode:");
            String fileName = scanner.nextLine();

            // Read the file content
            String fileContent = new String(Files.readAllBytes(Paths.get(fileName))).trim();

            // Extract the encoding scheme and binary code from the file content
            int newlineIndex = fileContent.lastIndexOf('\n');
            String encodString = fileContent.substring(0, newlineIndex).trim();
            String binaryCode = fileContent.substring(newlineIndex).trim();

            // Collect unique characters from the encoding scheme
            Set<Character> characters = new HashSet<>();
            for (char c : encodString.toCharArray()) {
                if (c != '^') {
                    characters.add(c);
                }
            }

            // Create a string representation of the unique characters
            String characterDict = characters.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining());

            // Build the message tree using the encoding scheme
            MsgTree root = new MsgTree(encodString);

            // Print the character codes
            MsgTree.printCodes(root, characterDict);

            // Decode the binary code using the message tree
            root.decode(root, binaryCode);
        } catch (IOException e) {
            System.out.println("Error: Failed to read the file.");
            e.printStackTrace();
        }
    }
}
