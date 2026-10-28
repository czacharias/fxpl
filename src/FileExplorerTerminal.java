import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class FileExplorerTerminal {

    private String rootDir;
    private JTextArea terminal;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileExplorerTerminal::new);
    }

    public FileExplorerTerminal() {
        // Set an initial root directory
        rootDir = System.getProperty("user.dir");

        // UI setup
        JFrame frame = new JFrame("Java Terminal with File Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        terminal = new JTextArea();
        terminal.setBackground(Color.black);
        terminal.setForeground(Color.green);
        terminal.setFont(new Font("Monospaced", Font.PLAIN, 12));
        terminal.setLineWrap(true);
        terminal.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(terminal);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);

        appendOutput("Welcome to the File Explorer Terminal!\n");
        appendOutput("Your current directory is: " + rootDir + "\n");
        appendOutput("Type 'help' for a list of commands.\n> ");

        terminal.addKeyListener(new KeyAdapter() {
            private String currentCommand = "";

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    currentCommand = terminal.getText().substring(terminal.getText().lastIndexOf("\n") + 1).trim();
                    processCommand(currentCommand);
                }
            }
        });
    }

    private void appendOutput(String message) {
        terminal.append(message);
        terminal.setCaretPosition(terminal.getDocument().getLength());
    }

    private void processCommand(String command) {
        if (command.isEmpty()) return;

        appendOutput("\n");
        String[] parts = command.substring(2).split(" ");
        String cmd = parts[1].toLowerCase();
        try {
            switch (cmd) {
                case "help":
                    appendOutput("""
                            Available commands:
                            - help: Show this help message
                            - read [path]: Read and display a file
                            - readf [path]: List contents of a folder
                            - make [path]: Create a new file
                            - makef [path]: Create a new folder
                            - write [path]: Write contents to a file
                            - delete [path]: Delete a file or folder
                            - cd [path]: Change current directory
                            - clear: Clear the terminal
                            - exit: Close the terminal
                            """);
                    break;

                case "clear":
                    terminal.setText("");
                    appendOutput("Terminal cleared.\n> ");
                    return;

                case "exit":
                    appendOutput("Goodbye!\n");
                    System.exit(0);

                default:
                    appendOutput("Unknown command: " + cmd + "\n");
                    break;
            }
        } catch (Exception e) {
            appendOutput("Error: " + e.getMessage() + "\n");
        }

        appendOutput("> ");
    }

    private void readFile(String path) {
        Path filePath = Path.of(rootDir, path);
        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    appendOutput(line + "\n");
                }
            } catch (IOException e) {
                appendOutput("Error reading file: " + e.getMessage() + "\n");
            }
        } else {
            appendOutput("File does not exist: " + filePath + "\n");
        }
    }

    private void readFolder(String path) {
        File folder = new File(rootDir, path);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    appendOutput(file.getName() + (file.isDirectory() ? "/" : "") + "\n");
                }
            }
        } else {
            appendOutput("Folder does not exist: " + folder.getAbsolutePath() + "\n");
        }
    }

    private void makeFile(String path) {
        File file = new File(rootDir, path);
        try {
            if (file.createNewFile()) {
                appendOutput("File created: " + file.getAbsolutePath() + "\n");
            } else {
                appendOutput("File already exists: " + file.getAbsolutePath() + "\n");
            }
        } catch (IOException e) {
            appendOutput("Error creating file: " + e.getMessage() + "\n");
        }
    }

    private void makeFolder(String path) {
        File folder = new File(rootDir, path);
        if (folder.mkdirs()) {
            appendOutput("Folder created: " + folder.getAbsolutePath() + "\n");
        } else {
            appendOutput("Failed to create folder: " + folder.getAbsolutePath() + "\n");
        }
    }

    private void writeFile(String path) {
        File file = new File(rootDir, path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            appendOutput("Enter file contents (type 'END' to finish):\n");
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while (!(line = consoleReader.readLine()).equalsIgnoreCase("END")) {
                writer.write(line);
                writer.newLine();
            }
            appendOutput("File written successfully: " + file.getAbsolutePath() + "\n");
        } catch (IOException e) {
            appendOutput("Error writing to file: " + e.getMessage() + "\n");
        }
    }

    private void deleteFile(String path) {
        File file = new File(rootDir, path);
        if (file.delete()) {
            appendOutput("Deleted: " + file.getAbsolutePath() + "\n");
        } else {
            appendOutput("Failed to delete: " + file.getAbsolutePath() + "\n");
        }
    }

    private void setRootDir(String path) {
        File newDir = new File(path);
        if (newDir.isDirectory()) {
            rootDir = newDir.getAbsolutePath();
            appendOutput("Current directory changed to: " + rootDir + "\n");
        } else {
            appendOutput("Invalid directory: " + path + "\n");
        }
    }
}
