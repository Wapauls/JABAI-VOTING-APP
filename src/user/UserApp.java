package user;

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
import user.*;

public class UserApp extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JButton closeButton;
    private JLabel studentInfoLabel;
    private JLabel sectionBar;
    private JTextField nameField;
    private JTextField studentIDField;
    private JTextField emailField;
    private JTextField yearField;
    private JTextField sectionField;
    private JLabel studentStatusLabel;
    private JRadioButton yesRadioButton;
    private JRadioButton noRadioButton;
    private ButtonGroup statusGroup;
    private JLabel errorLabel;
    private JButton proceedButton;
    
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

    public UserApp() {
        setTitle("Voting System");
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
        
        closeButton.addActionListener((ActionEvent e) -> System.exit(0));
        mainPanel.add(closeButton);
        
        // Section Bar divider (#1c1c1c)
        sectionBar = new JLabel();
        sectionBar.setBackground(new Color(28, 28, 28));
        sectionBar.setOpaque(true);
        sectionBar.setBounds(8, 57, 408, 2);
        mainPanel.add(sectionBar);
        
        // Student Information Header (24pt, centered)
        studentInfoLabel = new JLabel("STUDENT INFORMATION");
        studentInfoLabel.setFont(interRegular.deriveFont(24f));
        studentInfoLabel.setForeground(Color.WHITE);
        studentInfoLabel.setBounds(9, 65, 405, 47);
        studentInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(studentInfoLabel);
        
        // Name Label and Field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(interRegular.deriveFont(16f));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(23, 112, 93, 45);
        mainPanel.add(nameLabel);
        
        nameField = new JTextField();
        nameField.setBounds(116, 124, 284, 22);
        nameField.setBackground(new Color(217, 217, 217));
        nameField.setForeground(Color.BLACK);
        nameField.setFont(interRegular.deriveFont(14f));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        mainPanel.add(nameField);
        
        // Student ID Label and Field
        JLabel studentIDLabel = new JLabel("Student ID:");
        studentIDLabel.setFont(interRegular.deriveFont(16f));
        studentIDLabel.setForeground(Color.WHITE);
        studentIDLabel.setBounds(23, 157, 93, 45);
        mainPanel.add(studentIDLabel);
        
        studentIDField = new JTextField();
        studentIDField.setBounds(116, 169, 284, 22);
        studentIDField.setBackground(new Color(217, 217, 217));
        studentIDField.setForeground(Color.BLACK);
        studentIDField.setFont(interRegular.deriveFont(14f));
        studentIDField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        mainPanel.add(studentIDField);
        
        // Email Label and Field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(interRegular.deriveFont(16f));
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setBounds(23, 202, 93, 45);
        mainPanel.add(emailLabel);
        
        emailField = new JTextField();
        emailField.setBounds(116, 214, 284, 22);
        emailField.setBackground(new Color(217, 217, 217));
        emailField.setForeground(Color.BLACK);
        emailField.setFont(interRegular.deriveFont(14f));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        mainPanel.add(emailField);
        
        // Year Label and Field
        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setFont(interRegular.deriveFont(16f));
        yearLabel.setForeground(Color.WHITE);
        yearLabel.setBounds(23, 247, 93, 45);
        mainPanel.add(yearLabel);
        
        yearField = new JTextField();
        yearField.setBounds(116, 259, 95, 22);
        yearField.setBackground(new Color(217, 217, 217));
        yearField.setForeground(Color.BLACK);
        yearField.setFont(interRegular.deriveFont(14f));
        yearField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        mainPanel.add(yearField);
        
        // Section Label and Field
        JLabel sectionLabel = new JLabel("Section:");
        sectionLabel.setFont(interRegular.deriveFont(16f));
        sectionLabel.setForeground(Color.WHITE);
        sectionLabel.setBounds(227, 247, 78, 45);
        mainPanel.add(sectionLabel);
        
        sectionField = new JTextField();
        sectionField.setBounds(305, 259, 95, 22);
        sectionField.setBackground(new Color(217, 217, 217));
        sectionField.setForeground(Color.BLACK);
        sectionField.setFont(interRegular.deriveFont(14f));
        sectionField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        mainPanel.add(sectionField);
        
        // Student Status Question
        studentStatusLabel = new JLabel("Are you currently a student at this school?");
        studentStatusLabel.setFont(interRegular.deriveFont(16f));
        studentStatusLabel.setForeground(Color.WHITE);
        studentStatusLabel.setBounds(23, 281, 325, 48);
        mainPanel.add(studentStatusLabel);
        
        // Radio Buttons
        statusGroup = new ButtonGroup();
        
        // Yes Radio Button
        yesRadioButton = new JRadioButton("Yes");
        yesRadioButton.setBounds(23, 327, 71, 26);
        yesRadioButton.setBackground(new Color(20, 20, 20));
        yesRadioButton.setForeground(Color.WHITE);
        yesRadioButton.setSelected(true);
        yesRadioButton.setFocusPainted(false);
        yesRadioButton.setFont(interRegular.deriveFont(16f));
        statusGroup.add(yesRadioButton);
        mainPanel.add(yesRadioButton);
        
        // No Radio Button
        noRadioButton = new JRadioButton("No");
        noRadioButton.setBounds(101, 327, 71, 26);
        noRadioButton.setBackground(new Color(20, 20, 20));
        noRadioButton.setForeground(Color.WHITE);
        noRadioButton.setFocusPainted(false);
        noRadioButton.setFont(interRegular.deriveFont(16f));
        statusGroup.add(noRadioButton);
        mainPanel.add(noRadioButton);
        
        // Error Label (#e34949)
        errorLabel = new JLabel("Error Label");
        errorLabel.setFont(interRegular.deriveFont(16f));
        errorLabel.setForeground(new Color(227, 73, 73));
        errorLabel.setBounds(141, 429, 141, 29);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(errorLabel);
        
        // Proceed Button (#48f8fe background)
        proceedButton = new JButton("Proceed");
        proceedButton.setBounds(134, 463, 154, 30);
        proceedButton.setBackground(new Color(72, 248, 254));
        proceedButton.setForeground(Color.BLACK);
        proceedButton.setFont(interRegular.deriveFont(16f));
        proceedButton.setBorder(BorderFactory.createEmptyBorder());
        proceedButton.setFocusPainted(false);
        proceedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        proceedButton.addActionListener((ActionEvent e) -> {
            // Open VotingPage and close UserApp
            SwingUtilities.invokeLater(() -> new user.VotingPage());
            dispose();
        });
        mainPanel.add(proceedButton);
        
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new user.UserApp());
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
