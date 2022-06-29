import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import java.io.FileReader;
import java.util.Scanner;
import java.io.StreamTokenizer;

public class BasicFile {

  // Select a file, using a file dialog box
  // Make a copy of the file selected - whether it is a text file or an image file
  // Write to an output file with the option of either appending to a text file,
  // or over-writing hte contents of a text file

  File f;

  public BasicFile() {
    JFileChooser choose = new JFileChooser(".");
    int status = choose.showOpenDialog(null);
    /*
     * new integer named status is created
     * status is equal to the showOpenDialog() function of the JFileChooser "choose"
     */

    // For trying out the exceptions
    try {
      if (status != JFileChooser.APPROVE_OPTION) {
        throw new IOException();
      }

      f = choose.getSelectedFile();

      if (!f.exists())
        throw new FileNotFoundException();
      display(f.getName(), "File has been choosen", JOptionPane.INFORMATION_MESSAGE);

    }

    catch (FileNotFoundException e) {
      display("File not found ....", e.toString(), JOptionPane.WARNING_MESSAGE);
    }

    catch (IOException e) {
      display("Approved option was not selected", e.toString(), JOptionPane.ERROR_MESSAGE);
    }
  }

  static void display(String msg, String s, int t) {
    JOptionPane.showMessageDialog(null, msg, s, t);
  }

  String getPath() {
    return f.getAbsolutePath();
  }

  long getFileSize() {
    return f.length();
  }

  String canRead() {
    return (f.canRead()) ? "This file can be opened for reading" : "Cannot read this file";
  }

  String directoryOrFile() {
    return (f.isDirectory()) ? "This is a directory and not an ordinary file" : "This is a file and not a directory";
  }

  String exists() {
    return (f.exists()) ? "The phsyical file exists" : "The physical file does not exists";
  }

  // Copy method

  static void copyFile(File f) {
    try {
      FileInputStream oldFile = new FileInputStream(f.getAbsolutePath());
      FileOutputStream newFile = new FileOutputStream(f.getAbsolutePath() + "-copy");
      int b;
      while ((b = oldFile.read()) != -1) {
        newFile.write(b);
      }
      newFile.close();
      oldFile.close();
      System.out.println("File has been copied.");
    } catch (Exception e) {
      System.out.println("File not found.");
    }

    return;
  }

  // Append/Overwrite

  static void appendOverwrite(File File1) {
    String dialog = "Choose an option:\n1: Append\n2: overwrite";
    BasicFile basicFile2 = new BasicFile();
    File File2 = basicFile2.f;
    int choice = Integer.parseInt(JOptionPane.showInputDialog(dialog));
    Boolean append = false;
    switch (choice) {
      case 1:
        append = true;
        break;
      default:
        append = false;
        break;
    }
    try {
      FileInputStream read_stream = new FileInputStream(File1);
      FileOutputStream write_stream = new FileOutputStream(File2, append);

      int m;
      while ((m = read_stream.read()) != -1) {
        write_stream.write(m);
      }
      write_stream.close();
      read_stream.close();

    } catch (Exception e) {
      e.getStackTrace();
    }
  }


  static void scrollPane(BasicFile basicfile) {
    // Creates a String representing all the attributes in the file
    String attributes = "Absolute Path: " + basicfile.getPath() + "\n" + "Files/Directories:\n" + returnFiles(basicfile) + "\n" + "File Size: "
        + basicfile.getFileSize() + " KB\n" + "Lines: " +numLines(basicfile)+ "\n";

    // Creates a Scrollable pane with all of the attributes inside of it.
    final JFrame frame = new JFrame("File Attributes");
    frame.setVisible(true);
    JTextArea text = new JTextArea(attributes);
    JScrollPane scrollPane = new JScrollPane(text);
    frame.setSize(500, 300);
    frame.getContentPane().add(scrollPane);

  }
  // Lines

  static int numLines(BasicFile basicfile) {
    String fileName = basicfile.f.getName();
    int lastIndex = fileName.length() - 1;
    int counter = 0;
    if (fileName.substring(lastIndex - 2, lastIndex + 1).equals("txt")) {
      
      try {
        FileReader readFile = new FileReader(fileName);
        Scanner readLines = new Scanner(readFile);

        while (readLines.hasNextLine()) {
          counter++;
          readLines.nextLine();
        }
        readFile.close();
        return counter;
      } catch (IOException e) {
        System.out.println("Error while counting lines");
        e.getStackTrace();
      }
    }
      // What to do when file isn't .txt
    return counter;
    
  }

  static String returnFiles(BasicFile basicfile) {
    // Returns a string representation of all the files/directories in the path
      
    String fileNames = "";
    String basicFileName = basicfile.f.getName();
    String directory = basicfile.f.getAbsolutePath();

    int endIndex = directory.length() - basicFileName.length();

    String directoryPath = directory.substring(0, endIndex);
    
    File directoryFile = new File(directoryPath);
    
      for (final File file : directoryFile.listFiles()) {
        fileNames = fileNames + "    " + file.getName()+"\n";
      }
    return fileNames;
  }

  // Display contents

  static void displayContents(BasicFile basicfile) {
    
    String fileName = basicfile.f.getName();

    String fileContents = "";

    try {
        FileReader readFile = new FileReader(fileName);
        Scanner readLines = new Scanner(readFile);

        while (readLines.hasNextLine()) {
          fileContents += readLines.nextLine() + "\n";
        }
        readFile.close();
      } catch (IOException e) {
        System.out.println("Error while counting lines");
        e.getStackTrace();
      }
    
    
    BasicFile.printInScrollPane("File Contents", fileContents);

    
  }

// Search

  static void searchTerm(BasicFile basicfile) {
    String fileName = basicfile.f.getName();

    String fileMenu = "Word to search: ";
    String keyword = JOptionPane.showInputDialog(fileMenu).toLowerCase();
    String resultString = "";
    int counter = 0;
    
    try {
        FileReader readFile = new FileReader(fileName);
        Scanner readLines = new Scanner(readFile);

        while (readLines.hasNextLine()) {
          counter++;
          String currentLine = readLines.nextLine();
          if (currentLine.toLowerCase().contains(keyword)) {
            resultString += counter + ": " + currentLine + "\n";
          }
          
        }

       
        readFile.close();
      } catch (IOException e) {
        System.out.println("Error while counting lines");
        e.getStackTrace();
      }
    BasicFile.printInScrollPane("Keyword Search", resultString);
  }

  // Tokenizer

  static void tokenizeFile(BasicFile basicfile) {
        String fileName = basicfile.f.getName();
    try {
        StreamTokenizer st = new StreamTokenizer(new FileReader(fileName));
        String resultString = "";
      st.eolIsSignificant(true); // Recognize end of line as token
		    st.wordChars('"', '"');    // Recognize double quote (") as token
		    st.wordChars('@', '@');    // Recognize at (@) as token
		    st.wordChars(',', ',');    // Recognize comma (,) as token
		    st.wordChars('\'', '\'');  // Recognize single quote (') as token
		    st.wordChars('!', '!');    // Recognize exclamation(!) as token
		    st.lowerCaseMode(true);		// Ignore case sensitivity 
    
        while (st.nextToken() != StreamTokenizer.TT_EOF) 
        {           
            switch(st.ttype)
            {
            	case StreamTokenizer.TT_WORD: // Test for string
	                resultString += st.sval + "\n";
            	break;
            	case StreamTokenizer.TT_NUMBER: // Test for number
	                resultString += st.sval + "\n";
           		break;
       			case StreamTokenizer.TT_EOL: // Test for end of line
	           		//resultString += "\tNew line ++> " + st.sval + (char) st.ttype + "\n";		
           		break;
           		default: // Display any other values
        		 	resultString += ((char) st.ttype + " --> not recognized\n"  ); 
				break;
                
            }
      }
    BasicFile.printInScrollPane("Tokenized File", resultString);
    } catch (IOException e) {
      System.out.println("Error");
    }
        
  }
  private static void printInScrollPane(String paneTitle, String contents){
    
    final JFrame frame = new JFrame(paneTitle);
    frame.setVisible(true);
    JTextArea text = new JTextArea(contents);
    JScrollPane scrollPane = new JScrollPane(text);
    frame.setSize(500, 300);
    frame.getContentPane().add(scrollPane);
  }
  
}