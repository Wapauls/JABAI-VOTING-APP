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

public class AdminViewVotes extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel adminLabel;
    private JButton closeButton;
    private JLabel viewVotesPageLabel;
    private JLabel sectionBar;
    private JTextField searchField;
    private JPanel candidatesPanel;
    private JPanel votesGraphPanel;
    private JButton backButton;
    private JComboBox<String> coursesCombo;
    private JComboBox<String> positionCombo;
    private JComboBox<String> yearCombo;
    private JComboBox<String> sectionCombo;
    private JLabel selectedCandidateLabel;
    private JLabel courseLabel;
    private JLabel positionLabel;
    private JLabel yearLabel;
    private JLabel sectionLabel;
    private JLabel rankPositionLabel;
    
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

    public AdminViewVotes() {
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
        
        // View Votes Page Header
        viewVotesPageLabel = new JLabel("View Votes Page");
        viewVotesPageLabel.setFont(interRegular.deriveFont(24f));
        viewVotesPageLabel.setForeground(Color.WHITE);
        viewVotesPageLabel.setBounds(217, 65, 405, 47);
        viewVotesPageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(viewVotesPageLabel);
        
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
        
        // Selected Candidate Label
        selectedCandidateLabel = new JLabel("Selected Candidate: None");
        selectedCandidateLabel.setFont(interRegular.deriveFont(16f));
        selectedCandidateLabel.setForeground(Color.WHITE);
        selectedCandidateLabel.setBounds(431, 112, 384, 45);
        mainPanel.add(selectedCandidateLabel);
        
        // Course Label
        courseLabel = new JLabel("Course: None");
        courseLabel.setFont(interRegular.deriveFont(16f));
        courseLabel.setForeground(Color.WHITE);
        courseLabel.setBounds(432, 145, 181, 45);
        mainPanel.add(courseLabel);
        
        // Position Label
        positionLabel = new JLabel("Position: None");
        positionLabel.setFont(interRegular.deriveFont(16f));
        positionLabel.setForeground(Color.WHITE);
        positionLabel.setBounds(613, 145, 198, 45);
        mainPanel.add(positionLabel);
        
        // Year Label
        yearLabel = new JLabel("Year: None");
        yearLabel.setFont(interRegular.deriveFont(16f));
        yearLabel.setForeground(Color.WHITE);
        yearLabel.setBounds(432, 178, 181, 45);
        mainPanel.add(yearLabel);
        
        // Section Label
        sectionLabel = new JLabel("Section: None");
        sectionLabel.setFont(interRegular.deriveFont(16f));
        sectionLabel.setForeground(Color.WHITE);
        sectionLabel.setBounds(613, 178, 198, 45);
        mainPanel.add(sectionLabel);
        
        // Rank Position Label
        rankPositionLabel = new JLabel("Currently Rank Position: N/A");
        rankPositionLabel.setFont(interRegular.deriveFont(16f));
        rankPositionLabel.setForeground(Color.WHITE);
        rankPositionLabel.setBounds(432, 214, 379, 45);
        mainPanel.add(rankPositionLabel);
        
        // List of Candidates Label
        JLabel candidatesLabel = new JLabel("List of Candidates");
        candidatesLabel.setFont(interRegular.deriveFont(16f));
        candidatesLabel.setForeground(Color.WHITE);
        candidatesLabel.setBounds(23, 187, 219, 28);
        mainPanel.add(candidatesLabel);
        
        // Candidates Panel
        candidatesPanel = new JPanel();
        candidatesPanel.setLayout(null);
        candidatesPanel.setBounds(23, 223, 379, 133);
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
        
        // Candidate 1
        JLabel candidate1 = new JLabel("Juan E. Dela Cruz");
        candidate1.setFont(interRegular.deriveFont(14f));
        candidate1.setForeground(new Color(1, 1, 1));
        candidate1.setBounds(5, 28, 196, 28);
        candidate1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        candidate1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectCandidate(candidate1, "Juan E. Dela Cruz");
            }
        });
        candidatesPanel.add(candidate1);
        
        // Candidate 2
        JLabel candidate2 = new JLabel("Jack N. Jill");
        candidate2.setFont(interRegular.deriveFont(14f));
        candidate2.setForeground(new Color(1, 1, 1));
        candidate2.setBounds(5, 47, 196, 28);
        candidate2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        candidate2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectCandidate(candidate2, "Jack N. Jill");
            }
        });
        candidatesPanel.add(candidate2);
        
        // Candidate 3
        JLabel candidate3 = new JLabel("Mang E. juan");
        candidate3.setFont(interRegular.deriveFont(14f));
        candidate3.setForeground(new Color(1, 1, 1));
        candidate3.setBounds(5, 66, 196, 28);
        candidate3.setCursor(new Cursor(Cursor.HAND_CURSOR));
        candidate3.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectCandidate(candidate3, "Mang E. juan");
            }
        });
        candidatesPanel.add(candidate3);
        
        mainPanel.add(candidatesPanel);
        
        // Sort Label
        JLabel sortLabel = new JLabel("Sort");
        sortLabel.setFont(interRegular.deriveFont(16f));
        sortLabel.setForeground(Color.WHITE);
        sortLabel.setBounds(23, 374, 219, 28);
        mainPanel.add(sortLabel);
        
        // Courses ComboBox for Sort
        String[] courses = {"Select a Course", "BSCS", "BSHM", "BAPOLS", "BSTM", "BSBA", "BSED"};
        coursesCombo = new PaddedComboBox(courses);
        coursesCombo.setBounds(23, 418, 177, 29);
        coursesCombo.setSelectedIndex(0);
        mainPanel.add(coursesCombo);
        
        // Position ComboBox for Sort
        String[] positions = {"Select a Position", "President", "Vice President", 
                              "Secretary", "Treasurer", "Auditor"};
        positionCombo = new PaddedComboBox(positions);
        positionCombo.setBounds(225, 418, 177, 29);
        positionCombo.setSelectedIndex(0);
        mainPanel.add(positionCombo);
        
        // Year ComboBox for Sort
        String[] years = {"Select a Year", "1st", "2nd", "3rd", "4th"};
        yearCombo = new PaddedComboBox(years);
        yearCombo.setBounds(23, 473, 177, 29);
        yearCombo.setSelectedIndex(0);
        mainPanel.add(yearCombo);
        
        // Section ComboBox for Sort
        // Create sections A to Z
        String[] sections = new String[27];
        sections[0] = "Select a Section";
        for (int i = 1; i <= 26; i++) {
            sections[i] = String.valueOf((char) ('A' + i - 1));
        }
        
        sectionCombo = new PaddedComboBox(sections);
        sectionCombo.setBounds(225, 473, 177, 29);
        sectionCombo.setSelectedIndex(0);
        mainPanel.add(sectionCombo);
        
        // Votes Graph Panel
        votesGraphPanel = new JPanel();
        votesGraphPanel.setLayout(null);
        votesGraphPanel.setBounds(431, 259, 379, 188);
        votesGraphPanel.setBackground(new Color(217, 217, 217));
        
        // Graph headers
        JLabel graphNameHeader = new JLabel("Name");
        graphNameHeader.setFont(interRegular.deriveFont(14f));
        graphNameHeader.setForeground(new Color(1, 1, 1));
        graphNameHeader.setBounds(5, 0, 45, 28);
        graphNameHeader.setHorizontalAlignment(SwingConstants.CENTER);
        votesGraphPanel.add(graphNameHeader);
        
        JLabel votesHeader = new JLabel("Votes");
        votesHeader.setFont(interRegular.deriveFont(14f));
        votesHeader.setForeground(new Color(1, 1, 1));
        votesHeader.setBounds(201, 0, 77, 28);
        votesHeader.setHorizontalAlignment(SwingConstants.CENTER);
        votesGraphPanel.add(votesHeader);
        
        JLabel percentageHeader = new JLabel("Percentage");
        percentageHeader.setFont(interRegular.deriveFont(14f));
        percentageHeader.setForeground(new Color(1, 1, 1));
        percentageHeader.setBounds(278, 0, 96, 28);
        percentageHeader.setHorizontalAlignment(SwingConstants.CENTER);
        votesGraphPanel.add(percentageHeader);
        
        // Graph separator line
        JSeparator graphSeparator = new JSeparator();
        graphSeparator.setBackground(new Color(97, 97, 97));
        graphSeparator.setForeground(new Color(97, 97, 97));
        graphSeparator.setBounds(5, 29, 369, 1);
        votesGraphPanel.add(graphSeparator);
        
        // Candidate 1 in graph
        JLabel graphCandidate1 = new JLabel("Juan E. Dela Cruz");
        graphCandidate1.setFont(interRegular.deriveFont(14f));
        graphCandidate1.setForeground(new Color(1, 1, 1));
        graphCandidate1.setBounds(5, 28, 196, 28);
        votesGraphPanel.add(graphCandidate1);
        
        JLabel votes1 = new JLabel("34");
        votes1.setFont(interRegular.deriveFont(14f));
        votes1.setForeground(new Color(1, 1, 1));
        votes1.setBounds(201, 28, 77, 28);
        votes1.setHorizontalAlignment(SwingConstants.CENTER);
        votesGraphPanel.add(votes1);
        
        JLabel percentage1 = new JLabel("69.38%");
        percentage1.setFont(interRegular.deriveFont(14f));
        percentage1.setForeground(new Color(1, 1, 1));
        percentage1.setBounds(278, 28, 96, 28);
        percentage1.setHorizontalAlignment(SwingConstants.CENTER);
        votesGraphPanel.add(percentage1);
        
        // Candidate 2 in graph
        JLabel graphCandidate2 = new JLabel("Jack N. Jill");
        graphCandidate2.setFont(interRegular.deriveFont(14f));
        graphCandidate2.setForeground(new Color(1, 1, 1));
        graphCandidate2.setBounds(5, 47, 196, 28);
        votesGraphPanel.add(graphCandidate2);
        
        JLabel votes2 = new JLabel("15");
        votes2.setFont(interRegular.deriveFont(14f));
        votes2.setForeground(new Color(1, 1, 1));
        votes2.setBounds(201, 47, 77, 28);
        votes2.setHorizontalAlignment(SwingConstants.CENTER);
        votesGraphPanel.add(votes2);
        
        JLabel percentage2 = new JLabel("30.61%");
        percentage2.setFont(interRegular.deriveFont(14f));
        percentage2.setForeground(new Color(1, 1, 1));
        percentage2.setBounds(278, 47, 96, 28);
        percentage2.setHorizontalAlignment(SwingConstants.CENTER);
        votesGraphPanel.add(percentage2);
        
        mainPanel.add(votesGraphPanel);
        
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
    private void selectCandidate(JLabel selectedLabel, String candidateName) {
        // Reset all candidates to default color
        for (Component comp : candidatesPanel.getComponents()) {
            if (comp instanceof JLabel && comp != candidatesPanel.getComponent(0) && comp != candidatesPanel.getComponent(1)) {
                ((JLabel) comp).setForeground(new Color(1, 1, 1));
            }
        }
        
        // Highlight selected candidate
        selectedLabel.setForeground(new Color(72, 248, 254));
        
        // Update candidate details
        selectedCandidateLabel.setText("Selected Candidate: " + candidateName);
        
        switch (candidateName) {
            case "Juan E. Dela Cruz":
                courseLabel.setText("Course: BSCS");
                positionLabel.setText("Position: President");
                yearLabel.setText("Year: 3rd");
                sectionLabel.setText("Section: A");
                rankPositionLabel.setText("Currently Rank Position: 1st");
                break;
            case "Jack N. Jill":
                courseLabel.setText("Course: BSHM");
                positionLabel.setText("Position: Vice President");
                yearLabel.setText("Year: 2nd");
                sectionLabel.setText("Section: B");
                rankPositionLabel.setText("Currently Rank Position: 2nd");
                break;
            case "Mang E. juan":
                courseLabel.setText("Course: BSED");
                positionLabel.setText("Position: Secretary");
                yearLabel.setText("Year: 4th");
                sectionLabel.setText("Section: C");
                rankPositionLabel.setText("Currently Rank Position: 3rd");
                break;
            default:
                courseLabel.setText("Course: None");
                positionLabel.setText("Position: None");
                yearLabel.setText("Year: None");
                sectionLabel.setText("Section: None");
                rankPositionLabel.setText("Currently Rank Position: N/A");
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new admin.AdminViewVotes());
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