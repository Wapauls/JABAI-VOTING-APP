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

public class Admin extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel adminLabel;
    private JButton closeButton;
    private JLabel adminMenuLabel;
    private JLabel sectionBar;
    private JButton addCandidatesButton;
    private JButton removeCandidatesButton;
    private JButton editCandidatesButton;
    private JButton viewVotesButton;
    private JLabel versionLabel;
    
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

    public Admin() {
        setTitle("Voting System - Admin");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(424, 525);
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
        sectionBar.setBounds(8, 57, 407, 2);
        mainPanel.add(sectionBar);
        
        // Admin Menu Header
        adminMenuLabel = new JLabel("Admin Menu");
        adminMenuLabel.setFont(interRegular.deriveFont(24f));
        adminMenuLabel.setForeground(Color.WHITE);
        adminMenuLabel.setBounds(9, 65, 405, 47);
        adminMenuLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(adminMenuLabel);
        
        // Add Candidates Button (#48f8fe background)
        addCandidatesButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(72, 248, 254));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw text
                g2d.setColor(Color.BLACK);
                FontMetrics fm = g2d.getFontMetrics();
                String text = "Add Candidates";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };
        addCandidatesButton.setBounds(22, 120, 178, 30);
        addCandidatesButton.setBackground(new Color(72, 248, 254));
        addCandidatesButton.setForeground(Color.BLACK);
        addCandidatesButton.setFont(interRegular.deriveFont(16f));
        addCandidatesButton.setBorder(BorderFactory.createEmptyBorder());
        addCandidatesButton.setFocusPainted(false);
        addCandidatesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addCandidatesButton.addActionListener((ActionEvent e) -> {
            SwingUtilities.invokeLater(() -> new admin.AdminAddCandidate());
            dispose();
        });
        mainPanel.add(addCandidatesButton);
        
        // Remove Candidates Button (#48f8fe background)
        removeCandidatesButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(72, 248, 254));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw text
                g2d.setColor(Color.BLACK);
                FontMetrics fm = g2d.getFontMetrics();
                String text = "Remove Candidates";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };
        removeCandidatesButton.setBounds(224, 120, 178, 30);
        removeCandidatesButton.setBackground(new Color(72, 248, 254));
        removeCandidatesButton.setForeground(Color.BLACK);
        removeCandidatesButton.setFont(interRegular.deriveFont(16f));
        removeCandidatesButton.setBorder(BorderFactory.createEmptyBorder());
        removeCandidatesButton.setFocusPainted(false);
        removeCandidatesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeCandidatesButton.addActionListener((ActionEvent e) -> {
            SwingUtilities.invokeLater(() -> new admin.AdminRemoveCandidate());
            dispose();
        });
        mainPanel.add(removeCandidatesButton);
        
        // Edit Candidates Button (#48f8fe background)
        editCandidatesButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(72, 248, 254));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw text
                g2d.setColor(Color.BLACK);
                FontMetrics fm = g2d.getFontMetrics();
                String text = "Edit Candidates";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };
        editCandidatesButton.setBounds(22, 168, 178, 30);
        editCandidatesButton.setBackground(new Color(72, 248, 254));
        editCandidatesButton.setForeground(Color.BLACK);
        editCandidatesButton.setFont(interRegular.deriveFont(16f));
        editCandidatesButton.setBorder(BorderFactory.createEmptyBorder());
        editCandidatesButton.setFocusPainted(false);
        editCandidatesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editCandidatesButton.addActionListener((ActionEvent e) -> {
            SwingUtilities.invokeLater(() -> new admin.AdminEditCandidates());
            dispose();
        });
        mainPanel.add(editCandidatesButton);
        
        // View Votes Button (#48f8fe background)
        viewVotesButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(72, 248, 254));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw text
                g2d.setColor(Color.BLACK);
                FontMetrics fm = g2d.getFontMetrics();
                String text = "View Votes";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };
        viewVotesButton.setBounds(224, 168, 178, 30);
        viewVotesButton.setBackground(new Color(72, 248, 254));
        viewVotesButton.setForeground(Color.BLACK);
        viewVotesButton.setFont(interRegular.deriveFont(16f));
        viewVotesButton.setBorder(BorderFactory.createEmptyBorder());
        viewVotesButton.setFocusPainted(false);
        viewVotesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewVotesButton.addActionListener((ActionEvent e) -> {
            SwingUtilities.invokeLater(() -> new admin.AdminViewVotes());
            dispose();
        });
        mainPanel.add(viewVotesButton);
        
        // Version Label
        versionLabel = new JLabel("Version 1.0.0");
        versionLabel.setFont(interRegular.deriveFont(16f));
        versionLabel.setForeground(Color.WHITE);
        versionLabel.setBounds(22, 479, 154, 29);
        mainPanel.add(versionLabel);
        
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Admin());
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