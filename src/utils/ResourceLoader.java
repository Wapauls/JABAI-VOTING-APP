package utils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;

public class ResourceLoader {
    
    private static ResourceLoader instance;
    
    private ResourceLoader() {}
    
    public static ResourceLoader getInstance() {
        if (instance == null) {
            instance = new ResourceLoader();
        }
        return instance;
    }
    
    // Load image/icon from src/icons folder
    public ImageIcon loadIcon(String filename) {
        try {
            // Try multiple paths
            String[] possiblePaths = {
                filename,                       // Just filename
                "icons/" + filename,            // Relative from src
                "src/icons/" + filename,        // Full path
                "/icons/" + filename,           // From classpath root
                "src/" + filename               // Direct in src
            };
            
            for (String path : possiblePaths) {
                System.out.println("Trying to load: " + path);
                
                // Try ClassLoader first
                URL url = getClass().getClassLoader().getResource(path);
                if (url != null) {
                    System.out.println("Found via ClassLoader: " + url);
                    return new ImageIcon(url);
                }
                
                // Try as file
                File file = new File(path);
                if (file.exists()) {
                    System.out.println("Found as file: " + file.getAbsolutePath());
                    return new ImageIcon(file.getAbsolutePath());
                }
                
                // Try with src prefix
                File srcFile = new File("src/" + path);
                if (srcFile.exists()) {
                    System.out.println("Found in src: " + srcFile.getAbsolutePath());
                    return new ImageIcon(srcFile.getAbsolutePath());
                }
            }
            
            System.err.println("Icon not found: " + filename);
            return createDefaultIcon();
            
        } catch (Exception e) {
            System.err.println("Error loading icon: " + filename);
            e.printStackTrace();
            return createDefaultIcon();
        }
    }
    
    // Load font from src/fonts folder
    public Font loadFont(String filename, float size) {
        try {
            String[] possiblePaths = {
                filename,
                "fonts/" + filename,
                "src/fonts/" + filename,
                "/fonts/" + filename,
                "src/" + filename
            };
            
            for (String path : possiblePaths) {
                System.out.println("Trying to load font: " + path);
                
                // Try ClassLoader
                InputStream is = getClass().getClassLoader().getResourceAsStream(path);
                if (is != null) {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                    is.close();
                    System.out.println("Font loaded via ClassLoader: " + path);
                    return font.deriveFont(size);
                }
                
                // Try as file
                File file = new File(path);
                if (file.exists()) {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, file);
                    System.out.println("Font loaded as file: " + file.getAbsolutePath());
                    return font.deriveFont(size);
                }
                
                // Try with src prefix
                File srcFile = new File("src/" + path);
                if (srcFile.exists()) {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, srcFile);
                    System.out.println("Font loaded from src: " + srcFile.getAbsolutePath());
                    return font.deriveFont(size);
                }
            }
            
            System.err.println("Font not found: " + filename + ", using default font");
            return new Font("Arial", Font.PLAIN, (int)size);
            
        } catch (Exception e) {
            System.err.println("Error loading font: " + filename);
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, (int)size);
        }
    }
    
    private ImageIcon createDefaultIcon() {
        // Create a simple default icon
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(32, 32, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, 32, 32);
        g2d.dispose();
        return new ImageIcon(img);
    }
    
    // Test method
    public static void main(String[] args) {
        ResourceLoader loader = ResourceLoader.getInstance();
        
        System.out.println("=== Testing Resource Loader ===\n");
        
        // Test loading icons (actual files from your project)
        System.out.println("--- Testing Icons ---");
        ImageIcon userIcon = loader.loadIcon("user.png");
        System.out.println("user.png loaded: " + (userIcon != null && userIcon.getIconWidth() > 0));
        
        ImageIcon arrowIcon = loader.loadIcon("arrow_down.png");
        System.out.println("arrow_down.png loaded: " + (arrowIcon != null && arrowIcon.getIconWidth() > 0));
        
        // Test loading fonts (actual files from your project)
        System.out.println("\n--- Testing Fonts ---");
        Font fontRegular = loader.loadFont("Inter-Regular.otf", 14f);
        System.out.println("Inter-Regular.otf loaded: " + fontRegular.getFontName());
        
        Font fontBold = loader.loadFont("Inter-Bold.otf", 16f);
        System.out.println("Inter-Bold.otf loaded: " + fontBold.getFontName());
        
        System.out.println("\n=== Test Complete ===");
    }
}
