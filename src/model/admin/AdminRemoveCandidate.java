package model.admin;

import connectionDB.DatabaseHelper;
import connectionDB.Candidate;

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

public class AdminRemoveCandidate extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel adminLabel;
    private JButton closeButton;
    private JLabel removeCandidatePageLabel;
    private JLabel sectionBar;
    private JTextField searchField;
    private JPanel candidatesPanel;
    private JButton removeButton;
    private JButton backButton;
    private JLabel courseLabel;
    private JLabel positionLabel;
    private JLabel yearLabel;
    private JLabel sectionLabel;
    private String selectedCandidateName = "";
    
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

    public AdminRemoveCandidate() {
        setTitle("Voting System - Admin");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(839, 525);
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
        closeButton.setBounds(799, 16, 24, 24);
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
        sectionBar.setBounds(9, 57, 821, 2);
        mainPanel.add(sectionBar);
        
        // Remove Candidate Page Header
        removeCandidatePageLabel = new JLabel("Remove Candidate Page");
        removeCandidatePageLabel.setFont(interRegular.deriveFont(24f));
        removeCandidatePageLabel.setForeground(Color.WHITE);
        removeCandidatePageLabel.setBounds(217, 65, 405, 47);
        removeCandidatePageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(removeCandidatePageLabel);
        
        // Search Name Field
        JLabel searchLabel = new JLabel("Search Name");
        searchLabel.setFont(interRegular.deriveFont(16f));
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setBounds(431, 112, 167, 45);
        mainPanel.add(searchLabel);
        
        searchField = new JTextField();
        searchField.setBounds(431, 157, 379, 22);
        searchField.setBackground(new Color(217, 217, 217));
        searchField.setForeground(Color.BLACK);
        searchField.setFont(interRegular.deriveFont(14f));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        mainPanel.add(searchField);
        
        // Course Label
        courseLabel = new JLabel("Course: None");
        courseLabel.setFont(interRegular.deriveFont(16f));
        courseLabel.setForeground(Color.WHITE);
        courseLabel.setBounds(431, 179, 181, 45);
        mainPanel.add(courseLabel);
        
        // Position Label
        positionLabel = new JLabel("Position: None");
        positionLabel.setFont(interRegular.deriveFont(16f));
        positionLabel.setForeground(Color.WHITE);
        positionLabel.setBounds(612, 179, 198, 45);
        mainPanel.add(positionLabel);
        
        // Year Label
        yearLabel = new JLabel("Year: None");
        yearLabel.setFont(interRegular.deriveFont(16f));
        yearLabel.setForeground(Color.WHITE);
        yearLabel.setBounds(431, 207, 181, 45);
        mainPanel.add(yearLabel);
        
        // Section Label
        sectionLabel = new JLabel("Section: None");
        sectionLabel.setFont(interRegular.deriveFont(16f));
        sectionLabel.setForeground(Color.WHITE);
        sectionLabel.setBounds(612, 207, 198, 45);
        mainPanel.add(sectionLabel);
        
        // List of Added Candidates Label
        JLabel candidatesLabel = new JLabel("List of Candidates Added");
        candidatesLabel.setFont(interRegular.deriveFont(16f));
        candidatesLabel.setForeground(Color.WHITE);
        candidatesLabel.setBounds(23, 121, 219, 28);
        mainPanel.add(candidatesLabel);
        
        // Candidates Panel - EXACT POSITION from Figma: x=23, y=157, width=379, height=346
        candidatesPanel = new JPanel();
        candidatesPanel.setLayout(null);
        candidatesPanel.setBounds(23, 157, 379, 346);
        candidatesPanel.setBackground(new Color(217, 217, 217));
        
        // Candidate header - EXACT POSITION from Figma: x=28, y=157
        JLabel nameHeader = new JLabel("Name");
        nameHeader.setFont(interRegular.deriveFont(14f));
        nameHeader.setForeground(new Color(1, 1, 1));
        nameHeader.setBounds(5, 0, 45, 28); // Offset from panel edge: 5px from left
        nameHeader.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(nameHeader);
        
        // Separator line - EXACT POSITION from Figma: x=28, y=186
        JSeparator separator = new JSeparator();
        separator.setBackground(new Color(97, 97, 97));
        separator.setForeground(new Color(97, 97, 97));
        separator.setBounds(5, 29, 369, 1); // Starting at 5px from left, width 369px
        candidatesPanel.add(separator);
        
        // Dynamically load candidates from database
        java.util.List<Candidate> candidates = DatabaseHelper.readCandidates();
        int yPos = 28;
        for (Candidate c : candidates) {
            JLabel candidateLabel = new JLabel(c.name);
            candidateLabel.setFont(interRegular.deriveFont(14f));
            candidateLabel.setForeground(new Color(1, 1, 1));
            candidateLabel.setBounds(5, yPos, 196, 28);
            candidateLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Capture the candidate data in the listener
            String candName = c.name;
            String candCourse = c.course;
            String candPosition = c.position;
            String candYear = c.year;
            String candSection = c.section;
            
            candidateLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    selectCandidateFromDB(candidateLabel, candName, candCourse, candPosition, candYear, candSection);
                }
            });
            candidatesPanel.add(candidateLabel);
            yPos += 19;
        }
        
        mainPanel.add(candidatesPanel);
        
        // Remove Button (#ff2222 background)
        removeButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 34, 34));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw text
                g2d.setColor(Color.WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                String text = "Remove";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };
        removeButton.setBounds(431, 473, 250, 30);
        removeButton.setBackground(new Color(255, 34, 34));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFont(interRegular.deriveFont(16f));
        removeButton.setBorder(BorderFactory.createEmptyBorder());
        removeButton.setFocusPainted(false);
        removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeButton.addActionListener((ActionEvent e) -> {
            if (selectedCandidateName == null || selectedCandidateName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a candidate to remove.", "No selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Open confirmation dialog with candidate name and position
            String position = positionLabel.getText().replace("Position: ", "");
            SwingUtilities.invokeLater(() -> new model.admin.AdminRemoveConfirmation(selectedCandidateName, position, AdminRemoveCandidate.this));
        });
        mainPanel.add(removeButton);
        
        // Back Button - exact design
        backButton = new JButton("< Back");
        backButton.setBounds(724, 477, 58, 22);
        backButton.setBackground(new Color(20, 20, 20));
        backButton.setForeground(new Color(255, 59, 59));
        backButton.setFont(interRegular.deriveFont(16f));
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener((ActionEvent e) -> {
            // Return to Admin
            SwingUtilities.invokeLater(() -> new model.admin.Admin());
            dispose();
        });
        mainPanel.add(backButton);
        
        add(mainPanel);
        setVisible(true);
    }
    
    // Method to handle candidate selection from database
    private void selectCandidateFromDB(JLabel selectedLabel, String candidateName, String course, String position, String year, String section) {
        // Store the selected candidate name
        this.selectedCandidateName = candidateName;
        
        // Reset all candidates to default color
        for (Component comp : candidatesPanel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lab = (JLabel) comp;
                if (!"Name".equals(lab.getText())) {
                    lab.setForeground(new Color(1, 1, 1));
                }
            }
        }
        
        // Highlight selected candidate
        selectedLabel.setForeground(new Color(72, 248, 254));
        
        // Update candidate details
        courseLabel.setText("Course: " + course);
        positionLabel.setText("Position: " + position);
        yearLabel.setText("Year: " + year);
        sectionLabel.setText("Section: " + section);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new model.admin.AdminRemoveCandidate());
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