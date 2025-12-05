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
import admin.*;

public class AdminEditCandidates extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel adminLabel;
    private JButton closeButton;
    private JLabel editCandidatePageLabel;
    private JLabel sectionBar;
    private JTextField searchField;
    private JPanel candidatesPanel;
    private JButton updateButton;
    private JButton backButton;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JComboBox<String> coursesCombo;
    private JComboBox<String> positionCombo;
    private JComboBox<String> yearCombo;
    private JComboBox<String> sectionCombo;
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
    
    // Custom ComboBox with minimal padding
    class PaddedComboBox extends JComboBox<String> {
        public PaddedComboBox(String[] items) {
            super(items);
            setBackground(new Color(217, 217, 217));
            setForeground(Color.BLACK);
            setFont(interRegular.deriveFont(14f));
            setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
            
            // Set custom renderer for dropdown items
            setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                    return this;
                }
            });
            
            // Remove default padding from the editor component
            if (getEditor() != null) {
                Component editor = getEditor().getEditorComponent();
                if (editor instanceof JTextField) {
                    ((JTextField) editor).setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
                }
            }
            
            // Custom UI for arrow button
            setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton button = new JButton() {
                        @Override
                        public Dimension getPreferredSize() {
                            return new Dimension(24, 24);
                        }
                    };
                    button.setBorder(BorderFactory.createEmptyBorder());
                    button.setContentAreaFilled(false);
                    button.setFocusPainted(false);
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                    // Create arrow icon
                    try {
                        // Try to load arrow icon
                        File arrowDownFile = new File("icons/arrow-down.png");
                        if (arrowDownFile.exists()) {
                            ImageIcon arrowIcon = new ImageIcon(ImageIO.read(arrowDownFile));
                            Image scaledIcon = arrowIcon.getImage().getScaledInstance(12, 7, Image.SCALE_SMOOTH);
                            button.setIcon(new ImageIcon(scaledIcon));
                        } else {
                            // Fallback to simple arrow
                            button.setText("▼");
                            button.setForeground(new Color(29, 27, 32));
                            button.setFont(new Font("Arial", Font.PLAIN, 10));
                        }
                    } catch (Exception e) {
                        // Fallback to simple arrow
                        button.setText("▼");
                        button.setForeground(new Color(29, 27, 32));
                        button.setFont(new Font("Arial", Font.PLAIN, 10));
                    }
                    
                    return button;
                }
            });
        }
    }

    public AdminEditCandidates() {
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
        
        // Edit Candidate Page Header
        editCandidatePageLabel = new JLabel("Edit Candidates Page");
        editCandidatePageLabel.setFont(interRegular.deriveFont(24f));
        editCandidatePageLabel.setForeground(Color.WHITE);
        editCandidatePageLabel.setBounds(217, 65, 405, 47);
        editCandidatePageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(editCandidatePageLabel);
        
        // Search Name Field
        JLabel searchLabel = new JLabel("Search Name");
        searchLabel.setFont(interRegular.deriveFont(16f));
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setBounds(23, 112, 167, 45);
        mainPanel.add(searchLabel);
        
        searchField = new JTextField();
        searchField.setBounds(23, 157, 379, 22);
        searchField.setBackground(new Color(217, 217, 217));
        searchField.setForeground(Color.BLACK);
        searchField.setFont(interRegular.deriveFont(14f));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        mainPanel.add(searchField);
        
        // Change Name Field
        JLabel changeNameLabel = new JLabel("Change Name");
        changeNameLabel.setFont(interRegular.deriveFont(16f));
        changeNameLabel.setForeground(Color.WHITE);
        changeNameLabel.setBounds(431, 112, 167, 45);
        mainPanel.add(changeNameLabel);
        
        nameField = new JTextField();
        nameField.setBounds(431, 157, 379, 22);
        nameField.setBackground(new Color(217, 217, 217));
        nameField.setForeground(Color.BLACK);
        nameField.setFont(interRegular.deriveFont(14f));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        mainPanel.add(nameField);
        
        // Change Description Area
        JLabel changeDescriptionLabel = new JLabel("Change Description");
        changeDescriptionLabel.setFont(interRegular.deriveFont(16f));
        changeDescriptionLabel.setForeground(Color.WHITE);
        changeDescriptionLabel.setBounds(431, 178, 167, 45);
        mainPanel.add(changeDescriptionLabel);
        
        descriptionArea = new JTextArea();
        descriptionArea.setBounds(431, 223, 379, 66);
        descriptionArea.setBackground(new Color(217, 217, 217));
        descriptionArea.setForeground(Color.BLACK);
        descriptionArea.setFont(interRegular.deriveFont(14f));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        mainPanel.add(descriptionArea);
        
        // Change Courses ComboBox
        String[] courses = {"Change a Course", "BSCS", "BSHM", "BAPOLS", "BSTM", "BSBA", "BSED"};
        coursesCombo = new PaddedComboBox(courses);
        coursesCombo.setBounds(431, 315, 177, 29);
        coursesCombo.setSelectedIndex(0);
        mainPanel.add(coursesCombo);
        
        // Change Position ComboBox
        String[] positions = {"Change a Position", "President", "Vice President", 
                              "Secretary", "Treasurer", "Auditor"};
        positionCombo = new PaddedComboBox(positions);
        positionCombo.setBounds(634, 314, 177, 29);
        positionCombo.setSelectedIndex(0);
        mainPanel.add(positionCombo);
        
        // Change Year ComboBox
        String[] years = {"Change a Year", "1st", "2nd", "3rd", "4th"};
        yearCombo = new PaddedComboBox(years);
        yearCombo.setBounds(431, 367, 177, 29);
        yearCombo.setSelectedIndex(0);
        mainPanel.add(yearCombo);
        
        // Change Section ComboBox
        // Create sections A to Z
        String[] sections = new String[27];
        sections[0] = "Change a Section";
        for (int i = 1; i <= 26; i++) {
            sections[i] = String.valueOf((char) ('A' + i - 1));
        }
        
        sectionCombo = new PaddedComboBox(sections);
        sectionCombo.setBounds(634, 366, 177, 29);
        sectionCombo.setSelectedIndex(0);
        mainPanel.add(sectionCombo);
        
        // List of Candidates Label
        JLabel candidatesLabel = new JLabel("List of Candidates");
        candidatesLabel.setFont(interRegular.deriveFont(16f));
        candidatesLabel.setForeground(Color.WHITE);
        candidatesLabel.setBounds(23, 187, 219, 28);
        mainPanel.add(candidatesLabel);
        
        // Candidates Panel
        candidatesPanel = new JPanel();
        candidatesPanel.setLayout(null);
        candidatesPanel.setBounds(23, 223, 379, 280);
        candidatesPanel.setBackground(new Color(217, 217, 217));
        
        // Candidate headers
        JLabel nameHeader = new JLabel("Name");
        nameHeader.setFont(interRegular.deriveFont(14f));
        nameHeader.setForeground(new Color(1, 1, 1));
        nameHeader.setBounds(5, 0, 45, 28);
        nameHeader.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(nameHeader);
        
        JLabel statusHeader = new JLabel("Status");
        statusHeader.setFont(interRegular.deriveFont(14f));
        statusHeader.setForeground(new Color(1, 1, 1));
        statusHeader.setBounds(280, 0, 89, 28);
        statusHeader.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(statusHeader);
        
        // Separator line
        JSeparator separator = new JSeparator();
        separator.setBackground(new Color(97, 97, 97));
        separator.setForeground(new Color(97, 97, 97));
        separator.setBounds(5, 29, 369, 1);
        candidatesPanel.add(separator);
        
        // Candidate 1
        JLabel candidate1 = new JLabel("Juan E. Dela Cruz");
        candidate1.setFont(interRegular.deriveFont(14f));
        candidate1.setForeground(new Color(1, 1, 1));
        candidate1.setBounds(5, 28, 196, 28);
        candidate1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        candidate1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectCandidate(candidate1, "Juan E. Dela Cruz", "Updated");
            }
        });
        candidatesPanel.add(candidate1);
        
        JLabel status1 = new JLabel("Updated");
        status1.setFont(interRegular.deriveFont(14f));
        status1.setForeground(new Color(1, 1, 1));
        status1.setBounds(280, 28, 89, 28);
        status1.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(status1);
        
        // Candidate 2
        JLabel candidate2 = new JLabel("Jack N. Jill");
        candidate2.setFont(interRegular.deriveFont(14f));
        candidate2.setForeground(new Color(1, 1, 1));
        candidate2.setBounds(5, 47, 196, 28);
        candidate2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        candidate2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectCandidate(candidate2, "Jack N. Jill", "Original");
            }
        });
        candidatesPanel.add(candidate2);
        
        JLabel status2 = new JLabel("Original");
        status2.setFont(interRegular.deriveFont(14f));
        status2.setForeground(new Color(1, 1, 1));
        status2.setBounds(280, 47, 89, 28);
        status2.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(status2);
        
        // Candidate 3
        JLabel candidate3 = new JLabel("Mang E. juan");
        candidate3.setFont(interRegular.deriveFont(14f));
        candidate3.setForeground(new Color(1, 1, 1));
        candidate3.setBounds(5, 66, 196, 28);
        candidate3.setCursor(new Cursor(Cursor.HAND_CURSOR));
        candidate3.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectCandidate(candidate3, "Mang E. juan", "Original");
            }
        });
        candidatesPanel.add(candidate3);
        
        JLabel status3 = new JLabel("Original");
        status3.setFont(interRegular.deriveFont(14f));
        status3.setForeground(new Color(1, 1, 1));
        status3.setBounds(280, 66, 89, 28);
        status3.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(status3);
        
        mainPanel.add(candidatesPanel);
        
        // Update Button (#48f8fe background)
        updateButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(72, 248, 254));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw text
                g2d.setColor(Color.BLACK);
                FontMetrics fm = g2d.getFontMetrics();
                String text = "Update";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };
        updateButton.setBounds(431, 473, 177, 30);
        updateButton.setBackground(new Color(72, 248, 254));
        updateButton.setForeground(Color.BLACK);
        updateButton.setFont(interRegular.deriveFont(16f));
        updateButton.setBorder(BorderFactory.createEmptyBorder());
        updateButton.setFocusPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.addActionListener((ActionEvent e) -> {
            String position = (String) positionCombo.getSelectedItem();
            SwingUtilities.invokeLater(() -> new admin.AdminEditConfirmation(selectedCandidateName, position, AdminEditCandidates.this));
        });
        mainPanel.add(updateButton);
        
        // Back Button - exact design
        backButton = new JButton("← Back");
        backButton.setBounds(724, 477, 58, 22);
        backButton.setBackground(new Color(20, 20, 20));
        backButton.setForeground(new Color(255, 59, 59));
        backButton.setFont(interRegular.deriveFont(16f));
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener((ActionEvent e) -> {
            // Return to Admin
            SwingUtilities.invokeLater(() -> new admin.Admin());
            dispose();
        });
        mainPanel.add(backButton);
        
        add(mainPanel);
        setVisible(true);
    }
    
    // Method to handle candidate selection
    private void selectCandidate(JLabel selectedLabel, String candidateName, String status) {
        // Store the selected candidate name
        this.selectedCandidateName = candidateName;
        
        // Reset all candidates to default color
        for (Component comp : candidatesPanel.getComponents()) {
            if (comp instanceof JLabel && 
                comp != candidatesPanel.getComponent(0) && // Name header
                comp != candidatesPanel.getComponent(1) && // Status header
                comp != candidatesPanel.getComponent(2) && // Separator
                comp != candidatesPanel.getComponent(4) && // Status 1
                comp != candidatesPanel.getComponent(6) && // Status 2
                comp != candidatesPanel.getComponent(8)) { // Status 3
                ((JLabel) comp).setForeground(new Color(1, 1, 1));
            }
        }
        
        // Highlight selected candidate
        selectedLabel.setForeground(new Color(72, 248, 254));
        
        // Update form fields with candidate data
        switch (candidateName) {
            case "Juan E. Dela Cruz":
                nameField.setText("Juan E. Dela Cruz");
                descriptionArea.setText("President candidate with focus on academic excellence.");
                coursesCombo.setSelectedItem("BSCS");
                positionCombo.setSelectedItem("President");
                yearCombo.setSelectedItem("3rd");
                sectionCombo.setSelectedItem("A");
                break;
            case "Jack N. Jill":
                nameField.setText("Jack N. Jill");
                descriptionArea.setText("Vice President candidate focusing on student welfare.");
                coursesCombo.setSelectedItem("BSHM");
                positionCombo.setSelectedItem("Vice President");
                yearCombo.setSelectedItem("2nd");
                sectionCombo.setSelectedItem("B");
                break;
            case "Mang E. juan":
                nameField.setText("Mang E. juan");
                descriptionArea.setText("Secretary candidate with campus development platform.");
                coursesCombo.setSelectedItem("BSED");
                positionCombo.setSelectedItem("Secretary");
                yearCombo.setSelectedItem("4th");
                sectionCombo.setSelectedItem("C");
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new admin.AdminEditCandidates());
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