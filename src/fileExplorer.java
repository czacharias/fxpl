import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Formattable;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.awt.Desktop;
import java.net.URI;

public class fileExplorer {

    String rootDir;
    Scanner input = new Scanner(System.in);

    public fileExplorer(String rootDir){
        this.rootDir = rootDir;
    }

    public void startExplore() throws IOException, URISyntaxException {
        System.out.println("""
                                                           \u001B[47m\u001B[31m
                                                           \s
           ▄████████ ▀████    ▐████▀    ▄███████▄  ▄█      \s
          ███    ███   ███▌   ████▀    ███    ███ ███      \s
          ███    █▀     ███  ▐███      ███    ███ ███      \s
         ▄███▄▄▄        ▀███▄███▀      ███    ███ ███      \s
        ▀▀███▀▀▀        ████▀██▄     ▀█████████▀  ███      \s
          ███          ▐███  ▀███      ███        ███      \s
          ███         ▄███     ███▄    ███        ███▌    ▄\s
          ███        ████       ███▄  ▄████▀      █████▄▄██\s
                                                  ▀        \u001B[32m \u001B[0m
                                                 \s
        Enter "help" for methods
       \s""");
        while(true){
            String action, path;
            action = input.next().toLowerCase();
            switch(action){
                case "read":
                    path = input.next();
                    readFile(path);
                    break;
                case "readf":
                    path = input.next();
                    readFolder(path);
                    break;
                case "make":
                    path = input.next();
                    makeFile(path);
                    break;
                case "makef":
                    path = input.next();
                    makeFolder(path);
                    break;
                case "write":
                    path = input.next();
                    writeFile(path);
                    break;
                case "delete":
                    path = input.next();
                    deleteFile(path);
                    break;
                case "cd":
                    path = input.next();
                    setRootDir(path);
                    break;
                case "esc":
                    return;
                case "skibidi":
                    Desktop.getDesktop().browse(new URI("https://github.com/sstoichev22/ChessGame/commit/f180b8aec73ab8a5d45cf5c2a7d5dd0cbc585898"));
                    break;
                case "help":
                    System.out.printf("""
                            your current directory is %s
                            methods:
                            name  | arguments    |  description
                            read   [String path] -> Enter the path from your current directory to the file you want to read
                            readf  [String path] -> Enter the path from your current directory to the folder you want to view
                            make   [String path] -> Enter the path from your current directory to create the file at
                            makef  [String path] -> Enter the path from your current directory to create the folder at
                            write  [String path] -> Enter the path from your current directory to write to the file at, you will then be asked for file contents
                            delete [String path] -> Enter the path from your current directory to delete
                            cd     [String path] -> Enter the path of your new root directory
                            esc    [null]        -> exit program
                            """, rootDir, rootDir, rootDir, rootDir, rootDir, rootDir);
                    break;
            }
        }
    }

    public void setRootDir(String rootDir){
        if(Files.exists(Path.of(rootDir))){
            this.rootDir = rootDir;
        }
        else{
            System.out.println(rootDir + " Does not exist\nDo you want to create this as a new directory? (Y/N)");
            if(input.next().equalsIgnoreCase("y")) {
                File f = new File(rootDir);
                f.mkdirs();
                System.out.println(rootDir + " created");
            }
        }
    }

    public void makeFile(String path) throws IOException {
        String[] pathSplit = path.split("/");
        if (validFilename(pathSplit[pathSplit.length -1])){
            try {
                File f = new File(rootDir + path);
                f.createNewFile();
                System.out.println("File created");
                f.getCanonicalPath();
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println("Invalid Filename!");
        }
    }

    public void makeFolder(String path){
        File f = new File(rootDir + path);
        f.mkdirs();
    }

    public void writeFile(String path) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rootDir + path))) {
            System.out.println("Enter file contents (type ESC to exit)");

            input.nextLine();
            String contents = input.nextLine();

            while (!contents.equalsIgnoreCase("esc")) {
                bw.append(contents).append("\n");
                contents = input.nextLine();
            }
            System.out.println("-- EOF --");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readFile(String path) throws IOException, FileNotFoundException{
        try (BufferedReader br = new BufferedReader(new FileReader(rootDir + path))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("-- EOF --");
        } catch (FileNotFoundException e) {
            String[] pathSplit = path.split("/");
            System.out.println(pathSplit[pathSplit.length-1] + " does not exist at " + rootDir + Arrays.stream(pathSplit, 0, pathSplit.length - 1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readFolder(String path){
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                System.out.println(file.getName());
            }
        }
        System.out.println("--- DONE ---");
    }


    public void deleteFile(String path){
        try{
            File f = new File(rootDir + path);
            f.delete();
        }
        catch (Error e){
            throw new Error();
        }

    }

    public boolean validFilename(String name){
        //i stole this regex from a stackoverflow thread
        return name.matches("\\A(?!(?:COM[0-9]|CON|LPT[0-9]|NUL|PRN|AUX|com[0-9]|con|lpt[0-9]|nul|prn|aux)|\\s|[\\.]{2,})[^\\\\\\/:*\"?<>|]{1,254}(?<![\\s\\.])\\z");
    }

}
