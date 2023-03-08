package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
        

    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        
        /* Your code goes here */
        StdIn.setFile(fileName);
        Double total = 0.0;
        ArrayList <Character> c = new ArrayList<Character>();
        ArrayList <Double> n = new ArrayList<Double>();
        sortedCharFreqList = new ArrayList<CharFreq>();
        while(StdIn.hasNextChar()) {
            char x = StdIn.readChar();
            boolean exists = false;
            for (int i = 0; i < c.size(); i++) {
                if (x == c.get(i)) {
                    exists = true;
                    Double z = n.get(i);
                    z++;
                    n.set(i, z);
                }
            }
            if (!exists) {
                c.add(x);
                n.add(1.0);
            }
            total++;
          }
          if(c.size() == 1) {
            int value = c.get(0);
            if (value == 127) {
                value = 0;
            } else {
                value++;
            }
            Character k = (char)value;
            CharFreq y = new CharFreq(k, 0.0);
            sortedCharFreqList.add(y);
        }
        for (int i = 0; i < c.size(); i++) {
            CharFreq y = new CharFreq(c.get(i), n.get(i) / total);
            sortedCharFreqList.add(y);
        }
        Collections.sort(sortedCharFreqList);
    }
    
	
    

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {

	/* Your code goes here */
    Queue <TreeNode> Source = new Queue<>();
    Queue <TreeNode> Target = new Queue<>();

    // Populate Source Node 
    for (int i = 0; i < sortedCharFreqList.size(); i++) {
        TreeNode Enqueue = new TreeNode(sortedCharFreqList.get(i), null, null);
        Source.enqueue(Enqueue);
    }
    // Step 4 - Repeat until source is empty or target has 1 node
    while (!Source.isEmpty() || Target.size() != 1) {
      // Make 2 Tree Nodes, Left and Right
      TreeNode Left = new TreeNode(); 
      TreeNode Right = new TreeNode();
      // Make a Char Freq: Parent 
      CharFreq Parent = new CharFreq();
      // target must be empty
        if (Target.isEmpty()) {
          Left = Source.dequeue();
          Right = Source.dequeue();
          Parent = new CharFreq(null, Left.getData().getProbOcc() + Right.getData().getProbOcc());
          huffmanRoot = new TreeNode(Parent, Left, Right);
          Target.enqueue(huffmanRoot);
        } else {
            for (int i = 0; i < 2; i++) {
                // 1
                if (Source.isEmpty() && i == 0) {  
                    Left = Target.dequeue();
                    Right = Target.dequeue();
                    break;
                }
                // 2 
                if (Source.isEmpty() && i == 1) {
                    Right = Target.dequeue();
                    break;
                }
                // 3
                if (Target.isEmpty() && i == 1) {
                    Right = Source.dequeue();
                    break;
                }
                if (i == 0) {
                    if (Source.peek().getData().getProbOcc() <= Target.peek().getData().getProbOcc()) {
                        Left = Source.dequeue();
                    } else {
                        Left = Target.dequeue();
                    }
                }
                if (i == 1) {
                    if  (Source.peek().getData().getProbOcc() <= Target.peek().getData().getProbOcc()) {
                        Right = Source.dequeue();
                    } else {
                        Right = Target.dequeue();
                    }
                }
                
                 
           }
          Parent = new CharFreq(null, Left.getData().getProbOcc() + Right.getData().getProbOcc());
          huffmanRoot = new TreeNode(Parent, Left, Right);
          Target.enqueue(huffmanRoot);
        }
      }
    }
      
    
    

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {
    /* Your code goes here */   
      encodings = new String[128];
      String a = new String("");
      findBS(huffmanRoot, a);
    }

    private void findBS(TreeNode value, String z) {
        if(value.getLeft() == null && value.getRight() == null) {
            encodings[value.getData().getCharacter()] = z;
        }
        if(value.getLeft() != null) {
            String a = new String(z + "0");
            findBS(value.getLeft(), a);
        }
        if (value.getRight() != null) {
            String a = new String(z + "1");
            findBS(value.getRight(), a);
        }
    }
    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);

	/* Your code goes here */
    String z = new String();
    while (StdIn.hasNextChar()){
        Character x = StdIn.readChar();
        z +=encodings[x];
    }
    writeBitString(encodedFile, z); 
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);

	/* Your code goes here */
    String a = new String();
    a = readBitString(encodedFile);
    TreeNode ptr = new TreeNode();
    ptr = huffmanRoot;
    for (int i = 0; i < a.length(); i++) {
        if (a.charAt(i) == '0') {
            ptr = ptr.getLeft();
        }
        if (a.charAt(i) == '1') {
             ptr = ptr.getRight();
        }
        if (ptr.getLeft() == null && ptr.getRight() == null) {
            StdOut.print(ptr.getData().getCharacter());
            ptr = huffmanRoot;
        }
    }

    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
