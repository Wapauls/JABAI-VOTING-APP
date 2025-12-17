package model.user;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import utils.ResourceLoader;
import utils.NormalizationHelper;

public class UserApp extends JFrame {
        private JPanel mainPanel;
        private JLabel titleLabel;
        private JButton closeButton;
        private JLabel studentInfoLabel;
        private JLabel sectionBar;
        private JTextField nameField;
        private JTextField studentIDField;
        private JTextField courseField;
        private JTextField emailField;
        private JTextField yearField;
        private JTextField sectionField;
        private JLabel studentStatusLabel;
        private JRadioButton yesRadioButton;
        private JRadioButton noRadioButton;
        private ButtonGroup statusGroup;
        private JLabel errorLabel;
        private JButton proceedButton;
        private Border defaultFieldBorder;
        
        // For dragging
        private int dragX = 0;
        private int dragY = 0;
        
        // Custom fonts
        private Font interBold;
        private Font interRegular;

        // Shared resource loader
        private final ResourceLoader resourceLoader = ResourceLoader.getInstance();
        
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
            
            // Try to load close icon using ResourceLoader (classpath-safe)
            try {
                ImageIcon closeIcon = resourceLoader.loadIcon("close.png");
                if (closeIcon != null && closeIcon.getIconWidth() > 0) {
                    closeButton.setIcon(closeIcon);
                } else {
                    throw new IOException("Close icon not found");
                }
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
            defaultFieldBorder = nameField.getBorder();
            
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
            // Limit student ID to digits only, max 8 chars
            ((AbstractDocument) studentIDField.getDocument()).setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                    if (string == null) return;
                    if (isDigits(string) && fb.getDocument().getLength() + string.length() <= 8) {
                        super.insertString(fb, offset, string, attr);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if (text == null) return;
                    if (isDigits(text) && fb.getDocument().getLength() - length + text.length() <= 8) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }

                private boolean isDigits(String value) {
                    for (char c : value.toCharArray()) {
                        if (!Character.isDigit(c)) return false;
                    }
                    return true;
                }
            });
            mainPanel.add(studentIDField);
            
            // Course Label and Field
            JLabel courseLabel = new JLabel("Course:");
            courseLabel.setFont(interRegular.deriveFont(16f));
            courseLabel.setForeground(Color.WHITE);
            courseLabel.setBounds(23, 202, 93, 45);
            mainPanel.add(courseLabel);
            
            courseField = new JTextField();
            courseField.setBounds(116, 214, 284, 22);
            courseField.setBackground(new Color(217, 217, 217));
            courseField.setForeground(Color.BLACK);
            courseField.setFont(interRegular.deriveFont(14f));
            courseField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
            ));
            mainPanel.add(courseField);
            
            // Email Label and Field
            JLabel emailLabel = new JLabel("Email:");
            emailLabel.setFont(interRegular.deriveFont(16f));
            emailLabel.setForeground(Color.WHITE);
            emailLabel.setBounds(23, 247, 93, 45);
            mainPanel.add(emailLabel);
            
            emailField = new JTextField();
            emailField.setBounds(116, 259, 284, 22);
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
            yearLabel.setBounds(23, 292, 93, 45);
            mainPanel.add(yearLabel);
            
            yearField = new JTextField();
            yearField.setBounds(116, 304, 95, 22);
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
            sectionLabel.setBounds(227, 292, 78, 45);
            mainPanel.add(sectionLabel);
            
            sectionField = new JTextField();
            sectionField.setBounds(305, 304, 95, 22);
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
            studentStatusLabel.setBounds(23, 324, 325, 48);
            mainPanel.add(studentStatusLabel);
            
            // Radio Buttons
            statusGroup = new ButtonGroup();
            
            // Yes Radio Button
            yesRadioButton = new JRadioButton("Yes");
            yesRadioButton.setBounds(23, 364, 71, 26);
            yesRadioButton.setBackground(new Color(20, 20, 20));
            yesRadioButton.setForeground(Color.WHITE);
            yesRadioButton.setSelected(true);
            yesRadioButton.setFocusPainted(false);
            yesRadioButton.setFont(interRegular.deriveFont(16f));
            statusGroup.add(yesRadioButton);
            mainPanel.add(yesRadioButton);
            
            // No Radio Button
            noRadioButton = new JRadioButton("No");
            noRadioButton.setBounds(101, 364, 71, 26);
            noRadioButton.setBackground(new Color(20, 20, 20));
            noRadioButton.setForeground(Color.WHITE);
            noRadioButton.setFocusPainted(false);
            noRadioButton.setFont(interRegular.deriveFont(16f));
            statusGroup.add(noRadioButton);
            mainPanel.add(noRadioButton);
            
            // Error Label (#e34949) - larger width and initially invisible
            errorLabel = new JLabel("");
            errorLabel.setFont(interRegular.deriveFont(16f));
            errorLabel.setForeground(new Color(227, 73, 73));
            errorLabel.setBounds(12, 406, 400, 60);
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            errorLabel.setVisible(false);
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
                // Validate required fields
                resetFieldBorders();
                String name = nameField.getText().trim();
                String studentID = studentIDField.getText().trim();
                String course = normalizeCourse(courseField.getText().trim());
                String email = emailField.getText().trim();
                String year = normalizeYear(yearField.getText().trim());
                String section = sectionField.getText().trim().toUpperCase();
                boolean missing = false;
                if (name.isEmpty()) { markFieldInvalid(nameField); missing = true; }
                if (studentID.isEmpty() || studentID.length() != 8) { markFieldInvalid(studentIDField); missing = true; }
                if (course.isEmpty()) { markFieldInvalid(courseField); missing = true; }
                if (email.isEmpty() || !email.contains(".scc@")) { markFieldInvalid(emailField); missing = true; }
                if (year.isEmpty()) { markFieldInvalid(yearField); missing = true; }
                if (section.isEmpty()) { markFieldInvalid(sectionField); missing = true; }

                if (missing) {
                    errorLabel.setText("<html><div style='text-align: center;'>Please complete all fields<br>(ID: 8 digits, email with \".scc\")</div></html>");
                    errorLabel.setVisible(true);
                    return;
                }
                errorLabel.setVisible(false);
                
                // Update fields with normalized values
                courseField.setText(course);
                yearField.setText(year);
                sectionField.setText(section);

                // If user opted "No", save credentials but skip voting; redirect to history
                if (noRadioButton.isSelected()) {
                    if (!connectionDB.DatabaseHelper.voterExists(studentID)) {
                        connectionDB.Voter v = new connectionDB.Voter(studentID, name, email, course, year, section, false, "");
                        connectionDB.DatabaseHelper.addVoter(v);
                    }
                    // Mark as handled so they cannot cast votes
                    connectionDB.DatabaseHelper.markVoterAsVoted(studentID);
                    new MessageDialog("No Voting Recorded", "<html><div style='text-align: center;'>You chose No.<br>Your details were saved, but no votes were recorded.</div></html>", MessageDialog.INFO);
                    SwingUtilities.invokeLater(() -> new model.user.VoteHistoryPage());
                    dispose();
                    return;
                }

                // If voter exists, check if they've already voted
                if (connectionDB.DatabaseHelper.voterExists(studentID)) {
                    boolean voted = connectionDB.DatabaseHelper.hasVoted(studentID);
                    if (voted) {
                        // Show label and redirect to vote history
                        errorLabel.setText("<html><div style='text-align: center;'>You’ve already voted.<br>Taking you to your Voting History…</div></html>");
                        errorLabel.setVisible(true);
                        javax.swing.Timer delayTimer = new javax.swing.Timer(3000, evt -> {
                            SwingUtilities.invokeLater(() -> new model.user.VoteHistoryPage(studentID));
                            dispose();
                        });
                        delayTimer.setRepeats(false);
                        delayTimer.start();
                        return;
                    }
                } else {
                    // Register new voter (course left empty)
                    connectionDB.Voter v = new connectionDB.Voter(studentID, name, email, course, year, section, false, "");
                    connectionDB.DatabaseHelper.addVoter(v);
                }

                // Open VotingPage with current student ID
                SwingUtilities.invokeLater(() -> new model.user.VotingPage(studentID));
                dispose();
            });
            mainPanel.add(proceedButton);
            
            add(mainPanel);
            setVisible(true);
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> new model.user.UserApp());
        }
        
        // Method to load custom fonts using ResourceLoader (classpath-safe)
        private void loadCustomFonts() {
            try {
                // Load Inter Bold font
                interBold = resourceLoader.loadFont("Inter-Bold.otf", 24f);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(interBold);
            } catch (Exception e) {
                System.err.println("Could not load Inter Bold font: " + e.getMessage());
                interBold = new Font("Arial", Font.BOLD, 24);
            }

            try {
                // Load Inter Regular font
                interRegular = resourceLoader.loadFont("Inter-Regular.otf", 16f);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(interRegular);
            } catch (Exception e) {
                System.err.println("Could not load Inter Regular font: " + e.getMessage());
                interRegular = new Font("Arial", Font.PLAIN, 16);
            }
        }

        // Helpers for validation styling
        private void markFieldInvalid(JComponent field) {
            Border redLine = BorderFactory.createLineBorder(Color.RED, 1);
            Border padding = BorderFactory.createEmptyBorder(4, 10, 4, 10);
            field.setBorder(BorderFactory.createCompoundBorder(redLine, padding));
        }

        private void resetFieldBorders() {
            nameField.setBorder(defaultFieldBorder);
            studentIDField.setBorder(defaultFieldBorder);
            courseField.setBorder(defaultFieldBorder);
            emailField.setBorder(defaultFieldBorder);
            yearField.setBorder(defaultFieldBorder);
            sectionField.setBorder(defaultFieldBorder);
        }
        
        // Normalize course input: cs/bscs -> BSCS, CS -> BSCS (if needed)
        private String normalizeCourse(String course) {
            return NormalizationHelper.normalizeCourse(course);
        }
        
        // Normalize year input: 1 -> 1st, 2 -> 2nd, etc.
        private String normalizeYear(String year) {
            return NormalizationHelper.normalizeYear(year);
        }
    }
