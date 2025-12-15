import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Main extends JFrame {
    private JPanel mainPanel;
    private JLabel selectApplicationLabel;
    private JButton closeButton;
    private JLabel sectionBar;
    private JButton voteAppButton;
    private JButton adminAppButton;
    
    // Icons
    private ImageIcon userIcon;
    private ImageIcon usersIcon;
    private ImageIcon closeIcon;
    
    // For dragging
    private int dragX = 0;
    private int dragY = 0;
    
    // Custom fonts
    private Font interBold;
    private Font interRegular;
    
    // Custom JLabel class for gradient text
    class GradientLabel extends JLabel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Create gradient from white (#f9ffff) to cyan (#48f8fe)
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(249, 255, 255),
                getWidth(), 0, new Color(72, 248, 254)
            );
            g2d.setPaint(gradient);
            
            // Draw text with gradient
            FontMetrics fm = g2d.getFontMetrics();
            String text = getText();
            int x = 0; // Left aligned as per Figma
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            
            g2d.drawString(text, x, y);
        }
    }

    public Main() {
        setTitle("Voting System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(380, 259);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        
        // Load custom fonts and icons
        loadCustomFonts();
        loadIcons();
        
        // Create main panel with dark background (#141414)
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(20, 20, 20));
        mainPanel.setLayout(null);
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // Select Application Label (Gradient effect: #f9ffff to #48f8fe)
        selectApplicationLabel = new GradientLabel();
        selectApplicationLabel.setText("Select Application");
        selectApplicationLabel.setFont(interBold.deriveFont(24f));
        selectApplicationLabel.setBounds(18, 0, 320, 57);
        mainPanel.add(selectApplicationLabel);
        
        // Add dragging to title label
        selectApplicationLabel.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent e) {
                dragX = e.getXOnScreen() - getLocationOnScreen().x;
                dragY = e.getYOnScreen() - getLocationOnScreen().y;
            }
            public void mouseClicked(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        
        selectApplicationLabel.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - dragX, e.getYOnScreen() - dragY);
            }
            public void mouseMoved(MouseEvent e) {}
        });
        
        // Close Button with icon
        closeButton = new JButton();
        closeButton.setBounds(347, 16, 24, 24);
        closeButton.setBackground(new Color(20, 20, 20));
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Use loaded close icon or fallback
        if (closeIcon != null) {
            Image scaledCloseIcon = closeIcon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
            closeButton.setIcon(new ImageIcon(scaledCloseIcon));
        } else {
            // Create a simple X icon as fallback
            BufferedImage xIcon = new BufferedImage(14, 14, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = xIcon.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(2, 2, 12, 12);
            g2d.drawLine(12, 2, 2, 12);
            g2d.dispose();
            closeButton.setIcon(new ImageIcon(xIcon));
        }
        
        closeButton.addActionListener((ActionEvent e) -> System.exit(0));
        mainPanel.add(closeButton);
        
        // Section Bar divider (#1c1c1c)
        sectionBar = new JLabel();
        sectionBar.setBackground(new Color(28, 28, 28));
        sectionBar.setOpaque(true);
        sectionBar.setBounds(8, 57, 363, 2);
        mainPanel.add(sectionBar);
        
        // Vote App Button (#48f8fe background with user icon)
        voteAppButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw cyan background
                g2d.setColor(new Color(72, 248, 254));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw "VOTE" text at bottom (properly centered)
                g2d.setColor(Color.BLACK);
                g2d.setFont(interBold.deriveFont(16f));
                FontMetrics fm = g2d.getFontMetrics();
                String text = "VOTE";
                int textX = (getWidth() - fm.stringWidth(text)) / 2;
                int textY = getHeight() - 40; // Position text near bottom with proper padding
                g2d.drawString(text, textX, textY);
            }
        };
        voteAppButton.setBounds(18, 80, 160, 160);
        voteAppButton.setBackground(new Color(72, 248, 254));
        voteAppButton.setBorder(BorderFactory.createEmptyBorder());
        voteAppButton.setFocusPainted(false);
        voteAppButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        voteAppButton.setLayout(new BorderLayout());
        
        // Add user icon to vote button if available
        if (userIcon != null) {
            // Scale icon to appropriate size
            Image scaledUserIcon = userIcon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            JLabel iconLabel = new JLabel(new ImageIcon(scaledUserIcon));
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
            iconLabel.setVerticalAlignment(SwingConstants.TOP);
            iconLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
            
            // Add icon to button with proper layout
            voteAppButton.add(iconLabel, BorderLayout.CENTER);
        } else {
            // Fallback text if no icon
            JLabel fallbackLabel = new JLabel("VOTE");
            fallbackLabel.setFont(interBold.deriveFont(32f));
            fallbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
            fallbackLabel.setVerticalAlignment(SwingConstants.CENTER);
            voteAppButton.add(fallbackLabel, BorderLayout.CENTER);
        }
        
        voteAppButton.addActionListener((ActionEvent e) -> {
            // Navigate to UserApp (or VotingPage)
            SwingUtilities.invokeLater(() -> new model.user.UserApp());
            dispose();
        });
        mainPanel.add(voteAppButton);
        
        // Admin App Button (#48f8fe background with users icon)
        adminAppButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw cyan background
                g2d.setColor(new Color(72, 248, 254));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw "ADMIN" text at bottom (properly centered)
                g2d.setColor(Color.BLACK);
                g2d.setFont(interBold.deriveFont(16f));
                FontMetrics fm = g2d.getFontMetrics();
                String text = "ADMIN";
                int textX = (getWidth() - fm.stringWidth(text)) / 2;
                int textY = getHeight() - 40; // Position text near bottom with proper padding
                g2d.drawString(text, textX, textY);
            }
        };
        adminAppButton.setBounds(202, 80, 160, 160);
        adminAppButton.setBackground(new Color(72, 248, 254));
        adminAppButton.setBorder(BorderFactory.createEmptyBorder());
        adminAppButton.setFocusPainted(false);
        adminAppButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adminAppButton.setLayout(new BorderLayout());
        
        // Add users icon to admin button if available
        if (usersIcon != null) {
            // Scale icon to appropriate size
            Image scaledUsersIcon = usersIcon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            JLabel iconLabel = new JLabel(new ImageIcon(scaledUsersIcon));
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
            iconLabel.setVerticalAlignment(SwingConstants.TOP);
            iconLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
            
            // Add icon to button with proper layout
            adminAppButton.add(iconLabel, BorderLayout.CENTER);
        } else {
            // Fallback text if no icon
            JLabel fallbackLabel = new JLabel("ADMIN");
            fallbackLabel.setFont(interBold.deriveFont(32f));
            fallbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
            fallbackLabel.setVerticalAlignment(SwingConstants.CENTER);
            adminAppButton.add(fallbackLabel, BorderLayout.CENTER);
        }
        
        adminAppButton.addActionListener((ActionEvent e) -> {
            // Navigate to Admin
            SwingUtilities.invokeLater(() -> new model.admin.Admin());
            dispose();
        });
        mainPanel.add(adminAppButton);
        
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
    
    // Method to load custom fonts
    private void loadCustomFonts() {
        // Use system fonts - they work reliably everywhere
        interBold = new Font("Arial", Font.BOLD, 24);
        interRegular = new Font("Arial", Font.PLAIN, 16);
    }
    
    // Method to load icons
    private void loadIcons() {
        // Icons are optional - the application will work without them
        // They will be loaded from classpath if available, but won't break if missing
    }
}