package model.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageIO;
import utils.ResourceLoader;
import connectionDB.DatabaseHelper;
import connectionDB.Candidate;
import connectionDB.Vote;

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
                    
                    // Create arrow icon using ResourceLoader
                    try {
                        ImageIcon arrowIcon = resourceLoader.loadIcon("arrow_down.png");
                        if (arrowIcon != null && arrowIcon.getIconWidth() > 0) {
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
        
        // Try to load close icon via ResourceLoader
        try {
            ImageIcon closeIcon = resourceLoader.loadIcon("close.png");
            if (closeIcon != null && closeIcon.getIconWidth() > 0) {
                closeButton.setIcon(closeIcon);
            } else {
                throw new Exception("Close icon not found");
            }
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
        viewVotesPageLabel.setFont(interBold.deriveFont(24f));
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
        // Live search: filter candidate list as admin types
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                String query = searchField.getText().trim().toLowerCase();
                filterCandidatesByName(query);
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });
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
        
        // Fixed header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBackground(new Color(217, 217, 217));
        headerPanel.setBounds(23, 223, 379, 30);
        
        // Candidate header
        JLabel nameHeader = new JLabel("Name");
        nameHeader.setFont(interRegular.deriveFont(14f));
        nameHeader.setForeground(new Color(1, 1, 1));
        nameHeader.setBounds(5, 0, 45, 28);
        nameHeader.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(nameHeader);
        
        // Separator line
        JSeparator separator = new JSeparator();
        separator.setBackground(new Color(97, 97, 97));
        separator.setForeground(new Color(97, 97, 97));
        separator.setBounds(5, 29, 369, 1);
        headerPanel.add(separator);
        
        mainPanel.add(headerPanel);
        
        // Candidates content panel (for scrolling)
        candidatesPanel = new JPanel();
        candidatesPanel.setLayout(null);
        candidatesPanel.setBackground(new Color(217, 217, 217));
        candidatesPanel.setPreferredSize(new Dimension(369, 1));
        
        // Dynamically load candidates from database
        java.util.List<Candidate> candidates = DatabaseHelper.readCandidates();
        int yPos = 0;
        for (Candidate c : candidates) {
            JLabel candidateLabel = new JLabel(c.name + " (" + c.position + ")");
            candidateLabel.setFont(interRegular.deriveFont(14f));
            candidateLabel.setForeground(new Color(1, 1, 1));
            candidateLabel.setBounds(5, yPos, 364, 28);
            candidateLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            String candName = c.name;
            candidateLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    selectCandidate(candidateLabel, candName);
                }
            });
            candidatesPanel.add(candidateLabel);
            
            yPos += 19;
        }
        // Set preferred size based on number of candidates
        if (yPos > 0) {
            candidatesPanel.setPreferredSize(new Dimension(369, yPos));
        }
        
        // Wrap only content in scroll pane (header is fixed above)
        JScrollPane candidatesScrollPane = new JScrollPane(candidatesPanel);
        candidatesScrollPane.setBounds(23, 253, 379, 103);
        candidatesScrollPane.setBorder(BorderFactory.createEmptyBorder());
        candidatesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        candidatesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(candidatesScrollPane);
        
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
        
        // Add action listeners to filter candidates based on selected filters
        coursesCombo.addActionListener((ActionEvent e) -> filterAndDisplayCandidates());
        positionCombo.addActionListener((ActionEvent e) -> filterAndDisplayCandidates());
        yearCombo.addActionListener((ActionEvent e) -> filterAndDisplayCandidates());
        sectionCombo.addActionListener((ActionEvent e) -> filterAndDisplayCandidates());
        
        // Votes Graph Header Panel (OUTSIDE scrollpane)
        JPanel votesHeaderPanel = new JPanel();
        votesHeaderPanel.setLayout(null);
        votesHeaderPanel.setBackground(new Color(217, 217, 217));
        votesHeaderPanel.setBounds(431, 259, 379, 30);
        
        // Graph headers
        JLabel graphNameHeader = new JLabel("Name");
        graphNameHeader.setFont(interRegular.deriveFont(14f));
        graphNameHeader.setForeground(new Color(1, 1, 1));
        graphNameHeader.setBounds(5, 0, 45, 28);
        graphNameHeader.setHorizontalAlignment(SwingConstants.CENTER);
        votesHeaderPanel.add(graphNameHeader);
        
        JLabel votesHeader = new JLabel("Votes");
        votesHeader.setFont(interRegular.deriveFont(14f));
        votesHeader.setForeground(new Color(1, 1, 1));
        votesHeader.setBounds(201, 0, 77, 28);
        votesHeader.setHorizontalAlignment(SwingConstants.CENTER);
        votesHeaderPanel.add(votesHeader);
        
        JLabel percentageHeader = new JLabel("Percentage");
        percentageHeader.setFont(interRegular.deriveFont(14f));
        percentageHeader.setForeground(new Color(1, 1, 1));
        percentageHeader.setBounds(278, 0, 96, 28);
        percentageHeader.setHorizontalAlignment(SwingConstants.CENTER);
        votesHeaderPanel.add(percentageHeader);
        
        // Graph separator line
        JSeparator votesGraphSeparator = new JSeparator();
        votesGraphSeparator.setBackground(new Color(97, 97, 97));
        votesGraphSeparator.setForeground(new Color(97, 97, 97));
        votesGraphSeparator.setBounds(5, 29, 369, 1);
        votesHeaderPanel.add(votesGraphSeparator);
        
        mainPanel.add(votesHeaderPanel);
        
        // Votes Graph Panel (wrapped in scrollpane - BELOW header)
        votesGraphPanel = new JPanel();
        votesGraphPanel.setLayout(null);
        votesGraphPanel.setBackground(new Color(217, 217, 217));
        votesGraphPanel.setPreferredSize(new Dimension(369, 1));
        
        JScrollPane votesGraphScrollPane = new JScrollPane(votesGraphPanel);
        votesGraphScrollPane.setBounds(431, 289, 379, 158);
        votesGraphScrollPane.setBorder(BorderFactory.createEmptyBorder());
        votesGraphScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        votesGraphScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Rebuild votes graph with proper categorization
        rebuildVotesGraph();
        
        mainPanel.add(votesGraphScrollPane);
        
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
    
    // Method to filter and display candidates based on selected filters
    private void filterAndDisplayCandidates() {
        String selectedCourse = (String) coursesCombo.getSelectedItem();
        String selectedPosition = (String) positionCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        String selectedSection = (String) sectionCombo.getSelectedItem();
        
        // Rebuild with filters
        String searchQuery = searchField.getText().trim().toLowerCase();
        rebuildCandidatesList(searchQuery, selectedCourse, selectedPosition, selectedYear, selectedSection);
        
        // Also rebuild votes graph with same filters
        rebuildVotesGraph(selectedCourse, selectedPosition, selectedYear, selectedSection);
    }
    
    // Rebuild candidates list with optional name filter
    private void rebuildCandidatesList(String nameFilter) {
        rebuildCandidatesList(nameFilter, null, null, null, null);
    }
    
    private void rebuildCandidatesList(String nameFilter, String selectedCourse, String selectedPosition, String selectedYear, String selectedSection) {
        candidatesPanel.removeAll();
        java.util.List<Candidate> candidates = DatabaseHelper.readCandidates();
        int yPos = 0;
        
        for (Candidate c : candidates) {
            // Apply name filter if provided
            if (nameFilter != null && !nameFilter.isEmpty() && !c.name.toLowerCase().contains(nameFilter)) {
                continue;
            }
            
            // Apply other filters if provided
            boolean matchesCourse = selectedCourse == null || selectedCourse.equals("Select a Course") || c.course.equals(selectedCourse);
            boolean matchesPosition = selectedPosition == null || selectedPosition.equals("Select a Position") || c.position.equals(selectedPosition);
            boolean matchesYear = selectedYear == null || selectedYear.equals("Select a Year") || c.year.equals(selectedYear);
            boolean matchesSection = selectedSection == null || selectedSection.equals("Select a Section") || c.section.equals(selectedSection);
            
            if (matchesCourse && matchesPosition && matchesYear && matchesSection) {
                JLabel candidateLabel = new JLabel(c.name + " (" + c.position + ")");
                candidateLabel.setFont(interRegular.deriveFont(14f));
                candidateLabel.setForeground(new Color(1, 1, 1));
                candidateLabel.setBounds(5, yPos, 364, 28);
                candidateLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                String candName = c.name;
                candidateLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        selectCandidate(candidateLabel, candName);
                    }
                });
                candidatesPanel.add(candidateLabel);
                yPos += 19;
            }
        }
        
        if (yPos > 0) {
            candidatesPanel.setPreferredSize(new Dimension(369, yPos));
        } else {
            candidatesPanel.setPreferredSize(new Dimension(369, 1));
        }
        candidatesPanel.revalidate();
        candidatesPanel.repaint();
    }
    
    private void filterCandidatesByName(String nameFilter) {
        String selectedCourse = (String) coursesCombo.getSelectedItem();
        String selectedPosition = (String) positionCombo.getSelectedItem();
        String selectedYear = (String) yearCombo.getSelectedItem();
        String selectedSection = (String) sectionCombo.getSelectedItem();
        rebuildCandidatesList(nameFilter, selectedCourse, selectedPosition, selectedYear, selectedSection);
    }
    
    // Method to handle candidate selection
    private void selectCandidate(JLabel selectedLabel, String candidateName) {
        // Reset all candidates to default color
        for (Component comp : candidatesPanel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                if (!"Name".equals(lbl.getText())) {
                    lbl.setForeground(new Color(1, 1, 1));
                }
            }
        }
        
        // Highlight selected candidate
        selectedLabel.setForeground(new Color(72, 248, 254));
        
        // Update candidate details from database
        java.util.List<Candidate> candidates = DatabaseHelper.readCandidates();
        java.util.List<Vote> allVotes = DatabaseHelper.readVotes();
        
        Candidate foundCandidate = null;
        for (Candidate c : candidates) {
            if (c.name.equals(candidateName)) {
                foundCandidate = c;
                break;
            }
        }
        
        if (foundCandidate != null) {
            selectedCandidateLabel.setText("Selected Candidate: " + candidateName);
            courseLabel.setText("Course: " + foundCandidate.course);
            positionLabel.setText("Position: " + foundCandidate.position);
            yearLabel.setText("Year: " + foundCandidate.year);
            sectionLabel.setText("Section: " + foundCandidate.section);
            
            // Calculate rank position based on votes within same category (position+course+year+section)
            String candidateCategory = foundCandidate.position + "|" + foundCandidate.course + "|" + foundCandidate.year + "|" + foundCandidate.section;
            int candidateVotes = 0;
            for (Vote v : allVotes) {
                if (v.candidate.equals(candidateName)) {
                    candidateVotes++;
                }
            }
            
            // Count how many candidates in same category have more votes
            int rank = 1;
            for (Candidate c : candidates) {
                String otherCategory = c.position + "|" + c.course + "|" + c.year + "|" + c.section;
                if (otherCategory.equals(candidateCategory) && !c.name.equals(candidateName)) {
                    int otherVotes = 0;
                    for (Vote v : allVotes) {
                        if (v.candidate.equals(c.name)) {
                            otherVotes++;
                        }
                    }
                    if (otherVotes > candidateVotes) {
                        rank++;
                    }
                }
            }
            
            rankPositionLabel.setText("Currently Rank Position: " + getOrdinalSuffix(rank));
        } else {
            selectedCandidateLabel.setText("Selected Candidate: None");
            courseLabel.setText("Course: None");
            positionLabel.setText("Position: None");
            yearLabel.setText("Year: None");
            sectionLabel.setText("Section: None");
            rankPositionLabel.setText("Currently Rank Position: N/A");
        }
    }
    
    // Helper method to convert rank number to ordinal (1st, 2nd, 3rd, 4th, etc.)
    private String getOrdinalSuffix(int rank) {
        if (rank >= 11 && rank <= 13) {
            return rank + "th";
        }
        switch (rank % 10) {
            case 1: return rank + "st";
            case 2: return rank + "nd";
            case 3: return rank + "rd";
            default: return rank + "th";
        }
    }
    
    // Rebuild votes graph with proper categorization by position and course
    private void rebuildVotesGraph() {
        rebuildVotesGraph(null, null, null, null);
    }
    
    private void rebuildVotesGraph(String filterCourse, String filterPosition, String filterYear, String filterSection) {
        votesGraphPanel.removeAll();
        
        java.util.List<Candidate> allCandidates = DatabaseHelper.readCandidates();
        java.util.List<Vote> allVotes = DatabaseHelper.readVotes();
        
        // Group votes by category: position + course + year + section
        java.util.Map<String, java.util.List<Vote>> categoryVotes = new java.util.HashMap<>();
        java.util.Map<String, Candidate> candidateMap = new java.util.HashMap<>();
        
        for (Candidate c : allCandidates) {
            // Apply filters
            if (filterCourse != null && !filterCourse.equals("Select a Course") && !c.course.equals(filterCourse)) {
                continue;
            }
            if (filterPosition != null && !filterPosition.equals("Select a Position") && !c.position.equals(filterPosition)) {
                continue;
            }
            if (filterYear != null && !filterYear.equals("Select a Year") && !c.year.equals(filterYear)) {
                continue;
            }
            if (filterSection != null && !filterSection.equals("Select a Section") && !c.section.equals(filterSection)) {
                continue;
            }
            
            String category = c.position + "|" + c.course + "|" + c.year + "|" + c.section;
            candidateMap.put(c.name, c);
            
            if (!categoryVotes.containsKey(category)) {
                categoryVotes.put(category, new java.util.ArrayList<>());
            }
        }
        
        // Count votes per candidate within each category
        for (Vote v : allVotes) {
            Candidate c = candidateMap.get(v.candidate);
            if (c != null) {
                String category = c.position + "|" + c.course + "|" + c.year + "|" + c.section;
                if (categoryVotes.containsKey(category)) {
                    categoryVotes.get(category).add(v);
                }
            }
        }
        
        // Create candidate-vote pairs for sorting
        java.util.List<CandidateVoteData> candidateVoteDataList = new java.util.ArrayList<>();
        for (Candidate c : candidateMap.values()) {
            String category = c.position + "|" + c.course + "|" + c.year + "|" + c.section;
            java.util.List<Vote> categoryVoteList = categoryVotes.get(category);
            if (categoryVoteList == null) {
                categoryVoteList = new java.util.ArrayList<>();
            }
            
            // Count votes for this specific candidate
            int candidateVotes = 0;
            for (Vote v : categoryVoteList) {
                if (v.candidate.equals(c.name)) {
                    candidateVotes++;
                }
            }
            
            // Calculate percentage within the category (position+course+year+section)
            int totalCategoryVotes = categoryVoteList.size();
            double percentage = totalCategoryVotes > 0 ? (double) candidateVotes / totalCategoryVotes * 100 : 0;
            
            candidateVoteDataList.add(new CandidateVoteData(c, candidateVotes, percentage, category));
        }
        
        // Sort: by position order, then by course, then by votes (descending)
        java.util.Map<String, Integer> positionOrder = new java.util.HashMap<>();
        positionOrder.put("President", 1);
        positionOrder.put("Vice President", 2);
        positionOrder.put("Secretary", 3);
        positionOrder.put("Treasurer", 4);
        positionOrder.put("Auditor", 5);
        
        candidateVoteDataList.sort((a, b) -> {
            int posA = positionOrder.getOrDefault(a.candidate.position, 99);
            int posB = positionOrder.getOrDefault(b.candidate.position, 99);
            if (posA != posB) {
                return Integer.compare(posA, posB);
            }
            // Same position: sort by course
            String courseA = a.candidate.course == null ? "" : a.candidate.course;
            String courseB = b.candidate.course == null ? "" : b.candidate.course;
            int courseCompare = courseA.compareTo(courseB);
            if (courseCompare != 0) {
                return courseCompare;
            }
            // Same position and course: sort by votes (descending - top votes first)
            return Integer.compare(b.votes, a.votes);
        });
        
        int graphYPos = 0;
        for (CandidateVoteData data : candidateVoteDataList) {
            Candidate c = data.candidate;
            int candidateVotes = data.votes;
            double percentage = data.percentage;
            String percentageStr = String.format("%.0f%%", percentage);
            
            // Display format: Show candidate name only (no position)
            String displayName = c.name;
            
            // Candidate name
            JLabel graphCandidate = new JLabel(displayName);
            graphCandidate.setFont(interRegular.deriveFont(14f));
            graphCandidate.setForeground(new Color(1, 1, 1));
            graphCandidate.setBounds(5, graphYPos, 364, 28);
            votesGraphPanel.add(graphCandidate);
            
            // Vote count
            JLabel votes = new JLabel(candidateVotes + " votes");
            votes.setFont(interRegular.deriveFont(14f));
            votes.setForeground(new Color(1, 1, 1));
            votes.setBounds(201, graphYPos, 77, 28);
            votes.setHorizontalAlignment(SwingConstants.CENTER);
            votesGraphPanel.add(votes);
            
            // Percentage
            JLabel percentageLabel = new JLabel(percentageStr);
            percentageLabel.setFont(interRegular.deriveFont(14f));
            percentageLabel.setForeground(new Color(1, 1, 1));
            percentageLabel.setBounds(278, graphYPos, 96, 28);
            percentageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            votesGraphPanel.add(percentageLabel);
            
            graphYPos += 19;
        }
        
        // Update preferred size for scrollpane
        votesGraphPanel.setPreferredSize(new Dimension(369, Math.max(graphYPos, 28)));
        votesGraphPanel.revalidate();
        votesGraphPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new model.admin.AdminViewVotes());
    }
    
    // Helper class to hold candidate vote data for sorting
    private static class CandidateVoteData {
        Candidate candidate;
        int votes;
        double percentage;
        String category;
        
        CandidateVoteData(Candidate candidate, int votes, double percentage, String category) {
            this.candidate = candidate;
            this.votes = votes;
            this.percentage = percentage;
            this.category = category;
        }
    }
    
    // Method to load custom fonts using ResourceLoader
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
}