import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class FileExplorer {

    String rootDir, path;
    Scanner input = new Scanner(System.in);

    public FileExplorer(String rootDir){
        this.rootDir = rootDir;
    }

    public void processCommand(String command) {
        if (command.isEmpty()) return;

        Main.appendOutput("\n");
        switch(command.substring(2)){
            case "read":
                readFile(path);
                break;
            case "readf":
                readFolder(path);
                break;
            case "make":
                makeFile(path);
                break;
            case "makef":
                makeFolder(path);
                break;
            case "write":
                writeFile(path);
                break;
            case "delete":
                deleteFile(path);
                break;
            case "cd":
                setRootDir(path);
                break;
            case "esc":
                return;
            case "help":
               Main.appendOutput("""
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
                            """);
                break;
        }

        Main.appendOutput("> ");
    }

    public void setRootDir(String rootDir){
        if(Files.exists(Path.of(rootDir))){
            this.rootDir = rootDir;
        }
        else{
            Main.appendOutput(rootDir + " Does not exist\nDo you want to create this as a new directory? (Y/N)");
            if(input.next().equalsIgnoreCase("y")) {
                File f = new File(rootDir);
                f.mkdirs();
                System.out.println(rootDir + " created");
            }
        }
    }

    public void makeFile(String path) {
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

    public void writeFile(String path) {
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

    public void readFile(String path){
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
