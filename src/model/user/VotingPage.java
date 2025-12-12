package model.user;

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
import connectionDB.DatabaseHelper;
import connectionDB.Candidate;

public class VotingPage extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JButton closeButton;
    private JLabel votingPageLabel;
    private JLabel sectionBar;
    private JComboBox<String> coursesCombo;
    private JComboBox<String> positionCombo;
    private JComboBox<String> yearCombo;
    private JComboBox<String> sectionCombo;
    private JPanel candidatesPanel;
    private JLabel selectedCandidateLabel;
    private JTextArea descriptionArea;
    private JButton voteButton;
    private JButton backButton;
    
    // For dragging
    private int dragX = 0;
    private int dragY = 0;
    
    // Custom fonts
    private Font interBold;
    private Font interRegular;
    
    // Icons
    private ImageIcon arrowDownIcon;
    private ImageIcon closeIcon;
    // current logged-in student ID (may be null)
    private String currentStudentID;
    
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
    
    // Custom ComboBox with exact padding from Figma design
    class PaddedComboBox extends JComboBox<String> {
        public PaddedComboBox(String[] items) {
            super(items);
            setBackground(new Color(217, 217, 217));
            setForeground(Color.BLACK);
            setFont(interRegular.deriveFont(14f));
            
            // Minimal border - just to show the edges clearly
            setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
            
            // Set custom renderer for dropdown items with moderate padding
            setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    // Add moderate padding to dropdown items
                    setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                    return this;
                }
            });
            
            // Remove default padding from the editor component
            if (getEditor() != null) {
                JTextField editor = (JTextField) getEditor().getEditorComponent();
                editor.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0)); // Left padding only for text
            }
            
            // Custom UI for arrow button - positioned exactly like in Figma
            setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton button = new JButton() {
                        @Override
                        public Dimension getPreferredSize() {
                            return new Dimension(24, 24); // Exact size from Figma
                        }
                    };
                    button.setBorder(BorderFactory.createEmptyBorder());
                    button.setContentAreaFilled(false);
                    button.setFocusPainted(false);
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                    // Position arrow properly
                    if (arrowDownIcon != null) {
                        // Scale to 12x7 as in Figma design
                        Image scaledIcon = arrowDownIcon.getImage().getScaledInstance(12, 7, Image.SCALE_SMOOTH);
                        button.setIcon(new ImageIcon(scaledIcon));
                    } else {
                        // Fallback to simple arrow
                        button.setText("▼");
                        button.setForeground(new Color(29, 27, 32));
                        button.setFont(new Font("Arial", Font.PLAIN, 10));
                    }
                    
                    return button;
                }
                
                @Override
                protected void installListeners() {
                    super.installListeners();
                    // Ensure the combobox gets focus properly
                    addFocusListener(new java.awt.event.FocusAdapter() {
                        @Override
                        public void focusGained(java.awt.event.FocusEvent e) {
                            setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
                        }
                        
                        @Override
                        public void focusLost(java.awt.event.FocusEvent e) {
                            setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
                        }
                    });
                }
                
                @Override
                public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                    // Don't paint the default background
                }
            });
        }
    }

    public VotingPage() {
        this.currentStudentID = null;
        buildUI();
    }

    public VotingPage(String studentID) {
        this.currentStudentID = studentID;
        buildUI();
    }

    // Shared UI builder for both constructors
    private void buildUI() {
        setTitle("Voting System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(839, 525);
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
        closeButton.setBounds(799, 16, 24, 24);
        closeButton.setBackground(new Color(20, 20, 20));
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Use custom close icon or fallback
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
        sectionBar.setBounds(9, 57, 821, 2);
        mainPanel.add(sectionBar);

        // Voting Page Header
        votingPageLabel = new JLabel("Voting Page");
        votingPageLabel.setFont(interRegular.deriveFont(24f));
        votingPageLabel.setForeground(Color.WHITE);
        votingPageLabel.setBounds(217, 65, 405, 47);
        votingPageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(votingPageLabel);

        // Courses ComboBox with specified options - NO EXTRA PADDING
        JLabel coursesLabel = new JLabel("Courses:");
        coursesLabel.setFont(interRegular.deriveFont(16f));
        coursesLabel.setForeground(Color.WHITE);
        coursesLabel.setBounds(23, 112, 93, 45);
        mainPanel.add(coursesLabel);

        String[] courses = {"Select a Course", "BSCS", "BSHM", "BAPOLS", "BSTM", "BSBA", "BSED"};
        coursesCombo = new PaddedComboBox(courses);
        coursesCombo.setBounds(116, 120, 286, 29);
        coursesCombo.setSelectedIndex(0);

        // Remove any extra padding from the combobox display
        if (coursesCombo.getEditor() != null) {
            Component editor = coursesCombo.getEditor().getEditorComponent();
            if (editor instanceof JTextField) {
                ((JTextField) editor).setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
            }
        }

        mainPanel.add(coursesCombo);

        // Position ComboBox with specified options - NO EXTRA PADDING
        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setFont(interRegular.deriveFont(16f));
        positionLabel.setForeground(Color.WHITE);
        positionLabel.setBounds(431, 112, 93, 45);
        mainPanel.add(positionLabel);

        String[] positions = {"Select a Position", "President", "Vice President", 
                              "Secretary", "Treasurer", "Auditor"};
        positionCombo = new PaddedComboBox(positions);
        positionCombo.setBounds(530, 120, 286, 29);
        positionCombo.setSelectedIndex(0);

        if (positionCombo.getEditor() != null) {
            Component editor = positionCombo.getEditor().getEditorComponent();
            if (editor instanceof JTextField) {
                ((JTextField) editor).setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
            }
        }

        mainPanel.add(positionCombo);

        // Year ComboBox with specified options - NO EXTRA PADDING
        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setFont(interRegular.deriveFont(16f));
        yearLabel.setForeground(Color.WHITE);
        yearLabel.setBounds(23, 165, 93, 45);
        mainPanel.add(yearLabel);

        String[] years = {"Select a Year Level", "1st", "2nd", "3rd", "4th"};
        yearCombo = new PaddedComboBox(years);
        yearCombo.setBounds(116, 173, 286, 29);
        yearCombo.setSelectedIndex(0);

        if (yearCombo.getEditor() != null) {
            Component editor = yearCombo.getEditor().getEditorComponent();
            if (editor instanceof JTextField) {
                ((JTextField) editor).setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
            }
        }

        mainPanel.add(yearCombo);

        // Section ComboBox with A to Z options - NO EXTRA PADDING
        JLabel sectionLabel = new JLabel("Section:");
        sectionLabel.setFont(interRegular.deriveFont(16f));
        sectionLabel.setForeground(Color.WHITE);
        sectionLabel.setBounds(431, 165, 93, 45);
        mainPanel.add(sectionLabel);

        // Create sections A to Z
        String[] sections = new String[27];
        sections[0] = "Select a Section";
        for (int i = 1; i <= 26; i++) {
            sections[i] = String.valueOf((char) ('A' + i - 1));
        }

        sectionCombo = new PaddedComboBox(sections);
        sectionCombo.setBounds(530, 173, 286, 29);
        sectionCombo.setSelectedIndex(0);

        if (sectionCombo.getEditor() != null) {
            Component editor = sectionCombo.getEditor().getEditorComponent();
            if (editor instanceof JTextField) {
                ((JTextField) editor).setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
            }
        }

        mainPanel.add(sectionCombo);
        
        // Add action listeners to filter candidates based on selected filters
        coursesCombo.addActionListener((ActionEvent e) -> filterAndDisplayCandidates());
        positionCombo.addActionListener((ActionEvent e) -> filterAndDisplayCandidates());
        yearCombo.addActionListener((ActionEvent e) -> filterAndDisplayCandidates());
        sectionCombo.addActionListener((ActionEvent e) -> filterAndDisplayCandidates());

        // List of Candidates Label
        JLabel candidatesLabel = new JLabel("List of Candidates");
        candidatesLabel.setFont(interRegular.deriveFont(16f));
        candidatesLabel.setForeground(Color.WHITE);
        candidatesLabel.setBounds(23, 218, 219, 28);
        mainPanel.add(candidatesLabel);

        // Candidates Panel (exactly as in design)
        candidatesPanel = new JPanel();
        candidatesPanel.setLayout(null);
        candidatesPanel.setBounds(23, 256, 379, 247);
        candidatesPanel.setBackground(new Color(217, 217, 217));

        // Candidate header
        JLabel nameHeader = new JLabel("Name");
        nameHeader.setFont(interRegular.deriveFont(14f));
        nameHeader.setForeground(new Color(1, 1, 1));
        nameHeader.setBounds(5, 0, 45, 28);
        nameHeader.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(nameHeader);

        // Separator line
        JSeparator separator = new JSeparator();
        separator.setBackground(new Color(97, 97, 97));
        separator.setForeground(new Color(97, 97, 97));
        separator.setBounds(5, 29, 369, 1);
        candidatesPanel.add(separator);

        // Dynamically load candidates from the text database
        java.util.List<Candidate> candidates = DatabaseHelper.readCandidates();
        int yPos = 28;
        for (Candidate c : candidates) {
            JLabel lbl = new JLabel(c.name);
            lbl.setFont(interRegular.deriveFont(14f));
            lbl.setForeground(new Color(1, 1, 1));
            lbl.setBounds(5, yPos, 196, 28);
            lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lbl.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    selectCandidate(c.name, lbl);
                }
            });
            candidatesPanel.add(lbl);
            yPos += 19;
        }

        mainPanel.add(candidatesPanel);

        // Selected Candidate Label
        selectedCandidateLabel = new JLabel("Selected Candidate: None");
        selectedCandidateLabel.setFont(interRegular.deriveFont(16f));
        selectedCandidateLabel.setForeground(Color.WHITE);
        selectedCandidateLabel.setBounds(431, 256, 385, 29);
        mainPanel.add(selectedCandidateLabel);

        // Description Label
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(interRegular.deriveFont(16f));
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setBounds(431, 285, 93, 29);
        mainPanel.add(descriptionLabel);

        // Description Area (exact text from design)
        descriptionArea = new JTextArea("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus nec lacinia magna, vitae maximus lacus.");
        descriptionArea.setBounds(540, 291, 276, 140);
        descriptionArea.setBackground(new Color(20, 20, 20));
        descriptionArea.setForeground(Color.WHITE);
        descriptionArea.setFont(interRegular.deriveFont(14f));
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(descriptionArea);

        // Vote Button (#48f8fe background) - exact design
        voteButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(72, 248, 254));
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Draw text
                g2d.setColor(Color.BLACK);
                FontMetrics fm = g2d.getFontMetrics();
                String text = "Vote";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };
        voteButton.setBounds(563, 473, 118, 30);
        voteButton.setBackground(new Color(72, 248, 254));
        voteButton.setForeground(Color.BLACK);
        voteButton.setFont(interRegular.deriveFont(16f));
        voteButton.setBorder(BorderFactory.createEmptyBorder());
        voteButton.setFocusPainted(false);
        voteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        voteButton.addActionListener((ActionEvent e) -> {
            String candidate = selectedCandidateLabel.getText().replace("Selected Candidate: ", "");
            String position = (String) positionCombo.getSelectedItem();
            SwingUtilities.invokeLater(() -> new model.user.VoteConfirmationDialog(candidate, position, VotingPage.this, currentStudentID));
        });
        mainPanel.add(voteButton);

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
            // Return to UserApp
            try {
                Class<?> userAppClass = Class.forName("model.user.UserApp");
                java.lang.reflect.Constructor<?> constructor = userAppClass.getConstructor();
                SwingUtilities.invokeLater(() -> {
                    try {
                        constructor.newInstance();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            } catch (Exception ex) {
                // If UserApp doesn't exist, just close
                System.out.println("UserApp class not found. Closing application.");
            }
            dispose();
        });
        mainPanel.add(backButton);

        add(mainPanel);
        setVisible(true);
    }

    // Method to filter and display candidates based on selected filters
    private void filterAndDisplayCandidates() {
        String selectedCourse = (String) coursesCombo.getSelectedItem();
        String selectedPosition = (String) positionCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        String selectedSection = (String) sectionCombo.getSelectedItem();
        
        // Remove all candidate labels except header and separator
        java.util.List<Component> toRemove = new java.util.ArrayList<>();
        for (Component comp : candidatesPanel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                if (!"Name".equals(lbl.getText())) {
                    toRemove.add(comp);
                }
            }
        }
        for (Component comp : toRemove) {
            candidatesPanel.remove(comp);
        }
        
        // Load all candidates from database
        java.util.List<Candidate> candidates = DatabaseHelper.readCandidates();
        int yPos = 28;
        
        // Filter and display candidates based on selected criteria
        for (Candidate c : candidates) {
            boolean matchesCourse = selectedCourse.equals("Select a Course") || c.course.equals(selectedCourse);
            boolean matchesPosition = selectedPosition.equals("Select a Position") || c.position.equals(selectedPosition);
            boolean matchesYear = selectedYear.equals("Select a Year Level") || c.year.equals(selectedYear);
            boolean matchesSection = selectedSection.equals("Select a Section") || c.section.equals(selectedSection);
            
            if (matchesCourse && matchesPosition && matchesYear && matchesSection) {
                JLabel lbl = new JLabel(c.name);
                lbl.setFont(interRegular.deriveFont(14f));
                lbl.setForeground(new Color(1, 1, 1));
                lbl.setBounds(5, yPos, 196, 28);
                lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
                lbl.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        selectCandidate(c.name, lbl);
                    }
                });
                candidatesPanel.add(lbl);
                yPos += 19;
            }
        }
        
        candidatesPanel.revalidate();
        candidatesPanel.repaint();
    }
    
    // Method to handle candidate selection
    private void selectCandidate(String candidateName, JLabel candidateLabel) {
        selectedCandidateLabel.setText("Selected Candidate: " + candidateName);

        // Try to find candidate description from database
        java.util.List<Candidate> candidates = DatabaseHelper.readCandidates();
        String desc = null;
        for (Candidate c : candidates) {
            if (c.name.equals(candidateName)) {
                desc = c.description;
                break;
            }
        }

        if (desc != null && !desc.isEmpty()) {
            descriptionArea.setText(desc);
        } else {
            descriptionArea.setText(candidateName + "\n\nNo description available.");
        }

        // Reset all candidate labels to default color (skip header and separator by text check)
        for (Component comp : candidatesPanel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lab = (JLabel) comp;
                if (!"Name".equals(lab.getText())) {
                    lab.setForeground(new Color(1, 1, 1));
                }
            }
        }

        // Highlight selected candidate label
        if (candidateLabel != null) {
            candidateLabel.setForeground(new Color(72, 248, 254));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new model.user.VotingPage());
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
    
    // Method to load icons
    private void loadIcons() {
        try {
            // Load arrow down icon
            File arrowDownFile = new File("icons/arrow-down.png");
            if (arrowDownFile.exists()) {
                arrowDownIcon = new ImageIcon(ImageIO.read(arrowDownFile));
            }
        } catch (Exception e) {
            System.err.println("Could not load arrow-down icon: " + e.getMessage());
        }
        
        try {
            // Load close icon
            File closeFile = new File("icons/close.png");
            if (closeFile.exists()) {
                closeIcon = new ImageIcon(ImageIO.read(closeFile));
            }
        } catch (Exception e) {
            System.err.println("Could not load close icon: " + e.getMessage());
        }
    }
}