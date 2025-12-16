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

public class AdminAddCandidate extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel adminLabel;
    private JButton closeButton;
    private JLabel addCandidatePageLabel;
    private JLabel sectionBar;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JComboBox<String> coursesCombo;
    private JComboBox<String> positionCombo;
    private JComboBox<String> yearCombo;
    private JComboBox<String> sectionCombo;
    private JPanel addedCandidatesPanel;
    private JButton addButton;
    private JButton backButton;
    
    // Track next candidate position
    private int nextCandidateY = 66; // Starting position for new candidates (after 3 sample ones)
    @SuppressWarnings("unused")
    private JLabel currentlySelectedCandidate = null;
    
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

    public AdminAddCandidate() {
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
        
        // Add Candidate Page Header
        addCandidatePageLabel = new JLabel("Add Candidate Page");
        addCandidatePageLabel.setFont(interRegular.deriveFont(24f));
        addCandidatePageLabel.setForeground(Color.WHITE);
        addCandidatePageLabel.setBounds(217, 65, 405, 47);
        addCandidatePageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(addCandidatePageLabel);
        
        // Name Field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(interRegular.deriveFont(16f));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(23, 112, 93, 45);
        mainPanel.add(nameLabel);
        
        nameField = new JTextField();
        nameField.setBounds(23, 157, 379, 22);
        nameField.setBackground(new Color(217, 217, 217));
        nameField.setForeground(Color.BLACK);
        nameField.setFont(interRegular.deriveFont(14f));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        mainPanel.add(nameField);
        
        // Description Area
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(interRegular.deriveFont(16f));
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setBounds(431, 111, 93, 45);
        mainPanel.add(descriptionLabel);
        
        descriptionArea = new JTextArea();
        descriptionArea.setBounds(431, 156, 385, 53);
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
        
        // Courses ComboBox
        JLabel coursesLabel = new JLabel("Courses:");
        coursesLabel.setFont(interRegular.deriveFont(16f));
        coursesLabel.setForeground(Color.WHITE);
        coursesLabel.setBounds(23, 226, 93, 45);
        mainPanel.add(coursesLabel);
        
        String[] courses = {"Select a Course", "BSCS", "BSHM", "BAPOLS", "BSTM", "BSBA", "BSED"};
        coursesCombo = new PaddedComboBox(courses);
        coursesCombo.setBounds(116, 234, 286, 29);
        coursesCombo.setSelectedIndex(0);
        mainPanel.add(coursesCombo);
        
        // Position ComboBox
        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setFont(interRegular.deriveFont(16f));
        positionLabel.setForeground(Color.WHITE);
        positionLabel.setBounds(431, 226, 93, 45);
        mainPanel.add(positionLabel);
        
        String[] positions = {"Select a Position", "President", "Vice President", 
                              "Secretary", "Treasurer", "Auditor"};
        positionCombo = new PaddedComboBox(positions);
        positionCombo.setBounds(530, 234, 286, 29);
        positionCombo.setSelectedIndex(0);
        mainPanel.add(positionCombo);
        
        // Year ComboBox
        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setFont(interRegular.deriveFont(16f));
        yearLabel.setForeground(Color.WHITE);
        yearLabel.setBounds(23, 279, 93, 45);
        mainPanel.add(yearLabel);
        
        String[] years = {"Select a Year Level", "1st", "2nd", "3rd", "4th"};
        yearCombo = new PaddedComboBox(years);
        yearCombo.setBounds(116, 287, 286, 29);
        yearCombo.setSelectedIndex(0);
        mainPanel.add(yearCombo);
        
        // Section ComboBox
        JLabel sectionLabel = new JLabel("Section:");
        sectionLabel.setFont(interRegular.deriveFont(16f));
        sectionLabel.setForeground(Color.WHITE);
        sectionLabel.setBounds(431, 279, 93, 45);
        mainPanel.add(sectionLabel);
        
        // Create sections A to Z
        String[] sections = new String[27];
        sections[0] = "Select a Section";
        for (int i = 1; i <= 26; i++) {
            sections[i] = String.valueOf((char) ('A' + i - 1));
        }
        
        sectionCombo = new PaddedComboBox(sections);
        sectionCombo.setBounds(530, 287, 286, 29);
        sectionCombo.setSelectedIndex(0);
        mainPanel.add(sectionCombo);
        
        // List of Added Candidates Label
        JLabel addedCandidatesLabel = new JLabel("List of Candidates Added");
        addedCandidatesLabel.setFont(interRegular.deriveFont(16f));
        addedCandidatesLabel.setForeground(Color.WHITE);
        addedCandidatesLabel.setBounds(23, 324, 219, 28);
        mainPanel.add(addedCandidatesLabel);
        
        // Added Candidates Panel (EXACTLY like VotingPage)
        addedCandidatesPanel = new JPanel();
        addedCandidatesPanel.setLayout(null);
        addedCandidatesPanel.setBounds(23, 360, 379, 143);
        addedCandidatesPanel.setBackground(new Color(217, 217, 217));
        addedCandidatesPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        
        // Store references to fixed components
        JLabel nameHeader = new JLabel("Name");
        nameHeader.setFont(interRegular.deriveFont(14f));
        nameHeader.setForeground(new Color(1, 1, 1));
        nameHeader.setBounds(5, 0, 369, 28); // Full width
        nameHeader.setHorizontalAlignment(SwingConstants.LEFT);
        nameHeader.setVerticalAlignment(SwingConstants.CENTER);
        nameHeader.setBackground(new Color(217, 217, 217));
        nameHeader.setOpaque(true);
        addedCandidatesPanel.add(nameHeader);
        
        // Create a permanent separator that won't be affected by selection
        JPanel separatorLine = new JPanel();
        separatorLine.setBackground(new Color(97, 97, 97));
        separatorLine.setBounds(5, 29, 369, 1);
        separatorLine.setOpaque(true);
        addedCandidatesPanel.add(separatorLine);
        
        // Load candidates from database instead of mock data
        java.util.List<Candidate> candidates = DatabaseHelper.readCandidates();
        nextCandidateY = 28; // Reset to starting position
        for (Candidate c : candidates) {
            JLabel candidateLabel = createCandidateLabel(c.name, nextCandidateY);
            addedCandidatesPanel.add(candidateLabel);
            nextCandidateY += 19;
        }
        
        mainPanel.add(addedCandidatesPanel);
        
        // Add Button (#48f8fe background)
        addButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(72, 248, 254));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw text
                g2d.setColor(Color.BLACK);
                FontMetrics fm = g2d.getFontMetrics();
                String text = "Add";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };
        addButton.setBounds(431, 473, 250, 30);
        addButton.setBackground(new Color(72, 248, 254));
        addButton.setForeground(Color.BLACK);
        addButton.setFont(interRegular.deriveFont(16f));
        addButton.setBorder(BorderFactory.createEmptyBorder());
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener((ActionEvent e) -> {
        });
        mainPanel.add(addButton);
        
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
            SwingUtilities.invokeLater(() -> new model.admin.Admin());
            dispose();
        });
        mainPanel.add(backButton);
        
        add(mainPanel);
        setVisible(true);
    }
    
    // Helper method to create candidate labels with click functionality (EXACTLY like VotingPage)
    private JLabel createCandidateLabel(String name, int yPosition) {
        JLabel candidateLabel = new JLabel(name);
        candidateLabel.setFont(interRegular.deriveFont(14f));
        candidateLabel.setForeground(new Color(1, 1, 1));
        candidateLabel.setBounds(5, yPosition, 196, 28);
        candidateLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add mouse listener for selection (EXACTLY like VotingPage)
        candidateLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectCandidate(candidateLabel);
            }
        });
        
        return candidateLabel;
    }
    
    // Method to handle candidate selection (EXACTLY like VotingPage)
    private void selectCandidate(JLabel candidateLabel) {
        // Reset all candidates to default color
        for (Component comp : addedCandidatesPanel.getComponents()) {
            if (comp instanceof JLabel && comp != addedCandidatesPanel.getComponent(0) && comp != addedCandidatesPanel.getComponent(1)) {
                ((JLabel) comp).setForeground(new Color(1, 1, 1));
            }
        }
        
        // Highlight selected candidate
        candidateLabel.setForeground(new Color(72, 248, 254));
        currentlySelectedCandidate = candidateLabel;
    }
    
    // Method to handle adding a candidate
    private void addCandidate() {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        String course = (String) coursesCombo.getSelectedItem();
        String position = (String) positionCombo.getSelectedItem();
        String year = (String) yearCombo.getSelectedItem();
        String section = (String) sectionCombo.getSelectedItem();
        
        // Calculate position for new candidate
        int newCandidateY = nextCandidateY;
        nextCandidateY += 28; // Use full height (28) for each candidate
        
        // Create new candidate label with click functionality
        JLabel newCandidate = createCandidateLabel(name, newCandidateY);
        addedCandidatesPanel.add(newCandidate);
        
        // Update panel size if needed
        if (newCandidateY + 28 > addedCandidatesPanel.getHeight()) {
            addedCandidatesPanel.setBounds(23, 360, 379, newCandidateY + 35);
        }
        
        // Revalidate and repaint to show the new candidate
        addedCandidatesPanel.revalidate();
        addedCandidatesPanel.repaint();

        // Persist candidate to text database
        try {
            Candidate c = new Candidate(name, position, course, year, section, description);
            DatabaseHelper.appendCandidate(c);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Warning: could not persist candidate to database.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        
        // Clear form fields
        nameField.setText("");
        descriptionArea.setText("");
        coursesCombo.setSelectedIndex(0);
        positionCombo.setSelectedIndex(0);
        yearCombo.setSelectedIndex(0);
        sectionCombo.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new model.admin.AdminAddCandidate());
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