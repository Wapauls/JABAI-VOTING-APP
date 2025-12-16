import java.awt.*;
import java.io.File;

public class TestResources {
    public static void main(String[] args) {
        System.out.println("=== Testing Resource Structure ===");
        System.out.println("Current directory: " + System.getProperty("user.dir"));
        System.out.println();
        
        // Check src structure
        File srcDir = new File("src");
        System.out.println("src exists: " + srcDir.exists());
        if (srcDir.exists()) {
            listFiles(srcDir, "");
        }
        
        System.out.println("\n=== Checking Specific Files ===");
        
        // Check for icons
        String[] iconFiles = {"vote.png", "user.png", "admin.png", "results.png"};
        for (String icon : iconFiles) {
            checkFile("src/icons/" + icon);
            checkFile("icons/" + icon);
            checkFile(icon);
        }
        
        // Check for fonts
        String[] fontFiles = {"Roboto-Regular.ttf", "Roboto-Bold.ttf"};
        for (String font : fontFiles) {
            checkFile("src/fonts/" + font);
            checkFile("fonts/" + font);
            checkFile(font);
        }
        
        // Test loading via ClassLoader
        System.out.println("\n=== Testing ClassLoader ===");
        ClassLoader cl = TestResources.class.getClassLoader();
        
        System.out.println("Trying to load 'icons/vote.png': " + 
            (cl.getResource("icons/vote.png") != null));
        System.out.println("Trying to load 'fonts/Roboto-Regular.ttf': " + 
            (cl.getResource("fonts/Roboto-Regular.ttf") != null));
        System.out.println("Trying to load 'vote.png': " + 
            (cl.getResource("vote.png") != null));
    }
    
    private static void listFiles(File dir, String indent) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println(indent + "📁 " + file.getName() + "/");
                    listFiles(file, indent + "  ");
                } else {
                    System.out.println(indent + "📄 " + file.getName() + 
                        " (" + file.length() + " bytes)");
                }
            }
        }
    }
    
    private static void checkFile(String path) {
        File file = new File(path);
        System.out.println(path + " - exists: " + file.exists() + 
            ", is file: " + file.isFile() + 
            ", size: " + (file.exists() ? file.length() + " bytes" : "N/A"));
    }
}
