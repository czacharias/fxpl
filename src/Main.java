import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    FileExplorer fe;
    static JTextArea terminal = new JTextArea();
    public static void main(String[] args) {
        // Should probably change path before using, use cd in fxpl or just change this path here
//        fileExplorer fxpl = new fileExplorer("NULL DIR");
//        fxpl.startExplore();
        SwingUtilities.invokeLater(Main::new);
    }

    public Main() {
        fe = new FileExplorer("NULL DIR");
        JFrame frame = new JFrame("Java Terminal with Inline Input");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());


        terminal.setBackground(Color.black);
        terminal.setForeground(Color.green);
        terminal.setEditable(false);
        terminal.setFont(new Font("Monospaced", Font.PLAIN, 12));
        terminal.setLineWrap(true);
        terminal.setWrapStyleWord(true);
        terminal.setMargin(new Insets(0, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(terminal);
        scrollPane.setBorder(null);
        scrollPane.setViewportBorder(null);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);

        appendOutput("Welcome to the Java Terminal!\n");
        appendOutput("Type 'help' for a list of commands.\n> ");
        appendOutput("""
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

        terminal.setEditable(true);
        terminal.addKeyListener(new KeyAdapter() {
            private String currentCommand = "";

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    currentCommand = terminal.getText().substring(terminal.getText().lastIndexOf("\n") + 1).trim();
                    fe.processCommand(currentCommand);
                }
            }
        });
    }

    public static void appendOutput(String message) {
        terminal.append(message);
        terminal.setCaretPosition(terminal.getDocument().getLength());
    }


}
