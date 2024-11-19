import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        // Should probably change path before using, use cd in fxpl or just change this path here
        fileExplorer fxpl = new fileExplorer("NULL DIR");
        fxpl.startExplore();
    }
}