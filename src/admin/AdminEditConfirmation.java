package admin;

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
import javax.imageio.ImageIO;

public class AdminEditConfirmation extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel adminLabel;
    private JButton closeButton;
    private JLabel confirmationLabel;
    private JLabel sectionBar;
    private JLabel messageLabel;
    private JButton yesButton;
    private JButton noButton;
    @SuppressWarnings("unused")
    private String candidateName;
    @SuppressWarnings("unused")
    private String position;
    @SuppressWarnings("unused")
    private JFrame adminEditCandidateFrame;
    
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
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            
            g2d.drawString(text, x, y);
        }
    }

    public AdminEditConfirmation(String candidateName, String position, JFrame adminEditCandidateFrame) {
        this.candidateName = candidateName;
        this.position = position;
        this.adminEditCandidateFrame = adminEditCandidateFrame;
        
        setTitle("Voting System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(424, 259);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        
        // Load custom fonts
        loadCustomFonts();
        
        // Create main panel with dark background (#141414)
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(20, 20, 20));
        mainPanel.setLayout(null);
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // Title - Voting System (Gradient effect: #f9ffff to #48f8fe)
        titleLabel = new GradientLabel();
        titleLabel.setText("Voting System");
        titleLabel.setFont(interBold.deriveFont(24f));
        titleLabel.setBounds(0, 0, 211, 57);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Add dragging to title label
        titleLabel.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent e) {
                dragX = e.getXOnScreen() - getLocationOnScreen().x;
                dragY = e.getYOnScreen() - getLocationOnScreen().y;
            }
            public void mouseClicked(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        
        titleLabel.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - dragX, e.getYOnScreen() - dragY);
            }
            public void mouseMoved(MouseEvent e) {}
        });
        
        mainPanel.add(titleLabel);
        
        // Admin label (cyan color)
        adminLabel = new JLabel("Admin");
        adminLabel.setFont(interBold.deriveFont(15f));
        adminLabel.setForeground(new Color(72, 248, 254));
        adminLabel.setBounds(185, 7, 74, 30);
        adminLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(adminLabel);
        
        // Close Button with icon
        closeButton = new JButton();
        closeButton.setBounds(381, 17, 24, 24);
        closeButton.setBackground(new Color(20, 20, 20));
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Try to load close icon from icons folder
        try {
            BufferedImage closeIcon = ImageIO.read(new File("icons/close.png"));
            closeButton.setIcon(new ImageIcon(closeIcon));
        } catch (Exception e) {
            // Fallback to text if icon not found
            closeButton.setText("✕");
            closeButton.setForeground(Color.WHITE);
            closeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        }
        
        closeButton.addActionListener((ActionEvent e) -> dispose());
        mainPanel.add(closeButton);
        
        // Section Bar divider (#1c1c1c)
        sectionBar = new JLabel();
        sectionBar.setBackground(new Color(28, 28, 28));
        sectionBar.setOpaque(true);
        sectionBar.setBounds(8, 57, 407, 2);
        mainPanel.add(sectionBar);
        
        // Confirmation Header (Bold)
        confirmationLabel = new JLabel("Confirmation");
        confirmationLabel.setFont(interBold.deriveFont(24f));
        confirmationLabel.setForeground(Color.WHITE);
        confirmationLabel.setBounds(95, 67, 233, 46);
        confirmationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(confirmationLabel);
        
        // Confirmation Message
        messageLabel = new JLabel(String.format("Are you sure you want to update this candidate?"));
        messageLabel.setFont(interRegular.deriveFont(16f));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBounds(19, 113, 385, 64);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setVerticalAlignment(SwingConstants.CENTER);
        mainPanel.add(messageLabel);
        
        // Yes Button (#48f8fe background)
        yesButton = new JButton("Yes");
        yesButton.setBounds(48, 205, 118, 30);
        yesButton.setBackground(new Color(72, 248, 254));
        yesButton.setForeground(Color.BLACK);
        yesButton.setFont(interRegular.deriveFont(16f));
        yesButton.setBorder(BorderFactory.createEmptyBorder());
        yesButton.setFocusPainted(false);
        yesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        yesButton.addActionListener(e -> {
            // Just close the confirmation dialog
            dispose();
        });
        mainPanel.add(yesButton);
        
        // No Button (#ff2e12 background)
        noButton = new JButton("No");
        noButton.setBounds(256, 205, 118, 30);
        noButton.setBackground(new Color(255, 46, 18));
        noButton.setForeground(Color.WHITE);
        noButton.setFont(interRegular.deriveFont(16f));
        noButton.setBorder(BorderFactory.createEmptyBorder());
        noButton.setFocusPainted(false);
        noButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        noButton.addActionListener((ActionEvent e) -> {
            dispose();
        });
        mainPanel.add(noButton);
        
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminEditConfirmation("Test Candidate", "President", null));
    }
    
    // Method to load custom fonts
    private void loadCustomFonts() {
        try {
            // Load Inter Bold font
            File boldFontFile = new File("fonts/Inter-Bold.otf");
            interBold = Font.createFont(Font.TRUETYPE_FONT, boldFontFile).deriveFont(24f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(interBold);
        } catch (IOException | FontFormatException e) {
            System.err.println("Could not load Inter Bold font: " + e.getMessage());
            interBold = new Font("Arial", Font.BOLD, 24);
        }
        
        try {
            // Load Inter Regular font
            File regularFontFile = new File("fonts/Inter-Regular.otf");
            interRegular = Font.createFont(Font.TRUETYPE_FONT, regularFontFile).deriveFont(16f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(interRegular);
        } catch (IOException | FontFormatException e) {
            System.err.println("Could not load Inter Regular font: " + e.getMessage());
            interRegular = new Font("Arial", Font.PLAIN, 16);
        }
    }
}
