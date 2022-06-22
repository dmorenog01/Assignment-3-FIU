import javax.swing.JOptionPane;

class Main {
  public static void main(String[] arg) {
    boolean done = false;
    BasicFile f;

    String menu = "Enter option\n1. Open File\n2. ....\n4. Quit";
    while (!done) {
      String s = JOptionPane.showInputDialog(menu);
      try {
        int i = Integer.parseInt(s);
        switch (i) {
          case 1:
            f = new BasicFile();
            Integer result = fileMenu();
            System.out.println(result);
            runMethod(f, result);
            break;
          case 4:
            done = true;
            break;
          default:
            display("This option is underfined", "Error");
            break;
        }
      } catch (NumberFormatException | NullPointerException e) {
        display(e.toString(), "Error");
      }
    }
  }

  static void display(String s, String err) {
    JOptionPane.showMessageDialog(null, s, err, JOptionPane.ERROR_MESSAGE);
  }

  static void runMethod(BasicFile f, int option) {
    switch (option) {
      case 1:
        System.out.println("Copy File");
        BasicFile.copyFile(f.f);
        break;
      case 2:
        System.out.println("Append/Overwrite File");
        BasicFile.appendOverwrite(f.f);
        break;
      case 3:
        System.out.println("Display file attributes");
        BasicFile.scrollPane(f);
        break;
      case 4:
        System.out.println("Display file contents");
        BasicFile.displayContents(f);
        break;
      case 5:
        System.out.println("Search for keyword.");
        BasicFile.searchTerm(f);
        break;
      case 6:
        System.out.println("Tokenize File");
        BasicFile.tokenizeFile(f);
        break;
      default:
        System.out.println("Not a valid option");
        break;
    }
  }

  static int fileMenu() {

    String fileMenu = "Enter Option:\n1: Copy File\n2: Write to file\n3: Display Attributes\n4: Display File Contents\n5: Search File\n6: Tokenize File";

    String s = JOptionPane.showInputDialog(fileMenu);
    try {
      int result = Integer.parseInt(s);
      return result;
    } catch (NumberFormatException | NullPointerException e) {
      System.out.println(e.getStackTrace());
      return 0;
    }

  }
}