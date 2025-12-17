package model.admin;

import connectionDB.DatabaseHelper;
import connectionDB.Candidate;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageIO;
import java.util.Objects;
import utils.ResourceLoader;

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
    private Border defaultFieldBorder;
    private Border defaultComboBorder;
    private java.util.Set<String> updatedCandidates = new java.util.HashSet<>();
    
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
        
        // Edit Candidate Page Header
        editCandidatePageLabel = new JLabel("Edit Candidates Page");
        editCandidatePageLabel.setFont(interBold.deriveFont(24f));
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
        // Live search: filter candidate list as the admin types
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                String query = searchField.getText().trim().toLowerCase();
                rebuildCandidatesList(query);
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });
        
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
        defaultFieldBorder = nameField.getBorder();
        
        // Change Description Area
        JLabel changeDescriptionLabel = new JLabel("Change Description");
        changeDescriptionLabel.setFont(interRegular.deriveFont(16f));
        changeDescriptionLabel.setForeground(Color.WHITE);
        changeDescriptionLabel.setBounds(431, 178, 167, 45);
        mainPanel.add(changeDescriptionLabel);
        
        descriptionArea = new JTextArea();
        descriptionArea.setBackground(new Color(217, 217, 217));
        descriptionArea.setForeground(Color.BLACK);
        descriptionArea.setFont(interRegular.deriveFont(14f));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setBounds(431, 223, 379, 66);
        descriptionScrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        descriptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        descriptionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(descriptionScrollPane);
        
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
        defaultComboBorder = sectionCombo.getBorder();
        
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
        
        // Candidate headers
        JLabel nameHeader = new JLabel("Name");
        nameHeader.setFont(interRegular.deriveFont(14f));
        nameHeader.setForeground(new Color(1, 1, 1));
        nameHeader.setBounds(5, 0, 45, 28);
        nameHeader.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(nameHeader);
        
        JLabel statusHeader = new JLabel("Status");
        statusHeader.setFont(interRegular.deriveFont(14f));
        statusHeader.setForeground(new Color(1, 1, 1));
        statusHeader.setBounds(280, 0, 89, 28);
        statusHeader.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(statusHeader);
        
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
        rebuildCandidatesList("");
        
        // Wrap only content in scroll pane (header is fixed above)
        JScrollPane candidatesScrollPane = new JScrollPane(candidatesPanel);
        candidatesScrollPane.setBounds(23, 253, 379, 250);
        candidatesScrollPane.setBorder(BorderFactory.createEmptyBorder());
        candidatesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        candidatesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(candidatesScrollPane);
        
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
            resetBorders();
            if (selectedCandidateName == null || selectedCandidateName.isEmpty()) {
                new AdminMessageDialog("No selection", "Please select a candidate to update.", AdminMessageDialog.WARNING);
                return;
            }

            String newName = nameField.getText().trim();
            String newDesc = descriptionArea.getText().trim();
            String newCourse = (String) coursesCombo.getSelectedItem();
            String newPosition = (String) positionCombo.getSelectedItem();
            String newYear = (String) yearCombo.getSelectedItem();
            String newSection = (String) sectionCombo.getSelectedItem();

            boolean invalid = false;
            if (newName.isEmpty()) { nameField.setBorder(BorderFactory.createLineBorder(Color.RED, 1)); invalid = true; }
            if ("Change a Course".equals(newCourse)) { coursesCombo.setBorder(BorderFactory.createLineBorder(Color.RED, 1)); invalid = true; }
            if ("Change a Position".equals(newPosition)) { positionCombo.setBorder(BorderFactory.createLineBorder(Color.RED, 1)); invalid = true; }
            if ("Change a Year".equals(newYear)) { yearCombo.setBorder(BorderFactory.createLineBorder(Color.RED, 1)); invalid = true; }
            if ("Change a Section".equals(newSection)) { sectionCombo.setBorder(BorderFactory.createLineBorder(Color.RED, 1)); invalid = true; }

            if (invalid) {
                new AdminMessageDialog("Missing data", "Fill all required fields before updating.", AdminMessageDialog.WARNING);
                return;
            }

            Candidate updated = new Candidate(newName, newPosition, newCourse, newYear, newSection, newDesc);
            AdminEditConfirmation confirmDialog = new AdminEditConfirmation(
                newName,
                newPosition,
                this,
                () -> {
                    boolean ok = DatabaseHelper.updateCandidate(selectedCandidateName, updated);
                    if (!ok) {
                        new AdminMessageDialog("Error", "Could not find candidate in database. Update failed.", AdminMessageDialog.ERROR);
                        return;
                    }

                    updatedCandidates.add(newName);

                    // Update labels in UI candidates panel
                    JLabel selectedLabelRef = null;
                    int selectedY = -1;
                    for (Component comp : candidatesPanel.getComponents()) {
                        if (comp instanceof JLabel) {
                            JLabel lab = (JLabel) comp;
                            if (lab.getText().equals(selectedCandidateName) && lab.getBounds().x == 5) {
                                lab.setText(newName);
                                lab.setForeground(new Color(72, 248, 254));
                                selectedLabelRef = lab;
                                selectedY = lab.getBounds().y;
                            }
                        }
                    }
                    // Update corresponding status label to "Updated"
                    if (selectedY >= 0) {
                        for (Component comp : candidatesPanel.getComponents()) {
                            if (comp instanceof JLabel) {
                                JLabel lab = (JLabel) comp;
                                if (lab.getBounds().x == 280 && lab.getBounds().y == selectedY) {
                                    lab.setText("Updated");
                                    break;
                                }
                            }
                        }
                    }

                    selectedCandidateName = newName;
                    new AdminMessageDialog("Success", "Candidate updated successfully.", AdminMessageDialog.INFO);
                }
            );
            confirmDialog.setVisible(true);
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
            // If there are unsaved changes, confirm with the admin
            if (hasUnsavedChanges()) {
                new model.user.MessageDialog(
                    "Unsaved Changes",
                    "You have unsaved changes.\nLeave without saving?",
                    model.user.MessageDialog.WARNING,
                    () -> {
                        SwingUtilities.invokeLater(() -> new model.admin.Admin());
                        dispose();
                    },
                    () -> { /* cancel, stay on page */ }
                );
            } else {
                SwingUtilities.invokeLater(() -> new model.admin.Admin());
                dispose();
            }
        });
        mainPanel.add(backButton);
        
        add(mainPanel);
        setVisible(true);
    }
    
    // Method to handle candidate selection
    private void selectCandidate(JLabel selectedLabel, String candidateName, String candCourse, String candPosition, String candYear, String candSection) {
        // Store the selected candidate name
        this.selectedCandidateName = candidateName;
        
        // Reset all candidates to default color
        for (Component comp : candidatesPanel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lab = (JLabel) comp;
                if (!"Name".equals(lab.getText()) && !"Status".equals(lab.getText())) {
                    lab.setForeground(new Color(1, 1, 1));
                }
            }
        }
        
        // Highlight selected candidate
        selectedLabel.setForeground(new Color(72, 248, 254));
        
        // Update form fields with candidate data from the database
        java.util.List<Candidate> list = DatabaseHelper.readCandidates();
        Candidate found = null;
        for (Candidate c : list) {
            if (c.name.equals(candidateName)) {
                found = c;
                break;
            }
        }

        if (found != null) {
            nameField.setText(found.name);
            descriptionArea.setText(found.description == null ? "" : found.description);
            coursesCombo.setSelectedItem(found.course == null || found.course.isEmpty() ? coursesCombo.getItemAt(0) : found.course);
            positionCombo.setSelectedItem(found.position == null || found.position.isEmpty() ? positionCombo.getItemAt(0) : found.position);
            yearCombo.setSelectedItem(found.year == null || found.year.isEmpty() ? yearCombo.getItemAt(0) : found.year);
            sectionCombo.setSelectedItem(found.section == null || found.section.isEmpty() ? sectionCombo.getItemAt(0) : found.section);
        } else {
            // fallback to clearing or keeping previous values
            nameField.setText(candidateName);
            descriptionArea.setText("");
        }
    }

    /**
     * Detect whether the current form differs from the selected candidate's stored values.
     */
    private boolean hasUnsavedChanges() {
        if (selectedCandidateName == null || selectedCandidateName.isEmpty()) {
            return false;
        }
        java.util.List<Candidate> list = DatabaseHelper.readCandidates();
        Candidate found = null;
        for (Candidate c : list) {
            if (c.name.equals(selectedCandidateName)) {
                found = c;
                break;
            }
        }
        if (found == null) {
            // Candidate not found; treat edits as unsaved if any field is non-empty
            return !nameField.getText().trim().isEmpty()
                    || !descriptionArea.getText().trim().isEmpty();
        }

        String formName = nameField.getText().trim();
        String formDesc = descriptionArea.getText().trim();
        String formCourse = (String) coursesCombo.getSelectedItem();
        String formPosition = (String) positionCombo.getSelectedItem();
        String formYear = (String) yearCombo.getSelectedItem();
        String formSection = (String) sectionCombo.getSelectedItem();

        return !found.name.equals(formName)
                || !Objects.toString(found.description, "").equals(formDesc)
                || !Objects.toString(found.course, coursesCombo.getItemAt(0)).equals(formCourse)
                || !Objects.toString(found.position, positionCombo.getItemAt(0)).equals(formPosition)
                || !Objects.toString(found.year, yearCombo.getItemAt(0)).equals(formYear)
                || !Objects.toString(found.section, sectionCombo.getItemAt(0)).equals(formSection);
    }

    /**
     * Rebuild the candidates list panel, optionally filtering by a name fragment.
     */
    private void rebuildCandidatesList(String nameFilterLower) {
        candidatesPanel.removeAll();
        java.util.List<Candidate> candidates = DatabaseHelper.readCandidates();
        int yPos = 0;
        for (Candidate c : candidates) {
            if (nameFilterLower != null && !nameFilterLower.isEmpty()
                    && !c.name.toLowerCase().contains(nameFilterLower)) {
                continue;
            }

            JLabel candidateLabel = new JLabel(c.name + " (" + c.position + ")");
            candidateLabel.setFont(interRegular.deriveFont(14f));
            candidateLabel.setForeground(new Color(1, 1, 1));
            candidateLabel.setBounds(5, yPos, 364, 28);
            candidateLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            String candName = c.name;
            String candCourse = c.course;
            String candPosition = c.position;
            String candYear = c.year;
            String candSection = c.section;

            candidateLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    selectCandidate(candidateLabel, candName, candCourse, candPosition, candYear, candSection);
                }
            });
            candidatesPanel.add(candidateLabel);

            JLabel statusLabel = new JLabel(updatedCandidates.contains(c.name) ? "Updated" : "Original");
            statusLabel.setFont(interRegular.deriveFont(14f));
            statusLabel.setForeground(new Color(1, 1, 1));
            statusLabel.setBounds(280, yPos, 89, 28);
            statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
            candidatesPanel.add(statusLabel);

            yPos += 19;
        }
        if (yPos > 0) {
            candidatesPanel.setPreferredSize(new Dimension(369, yPos));
        } else {
            candidatesPanel.setPreferredSize(new Dimension(369, 1));
        }
        candidatesPanel.revalidate();
        candidatesPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new model.admin.AdminEditCandidates());
    }
    
    private void resetBorders() {
        nameField.setBorder(defaultFieldBorder);
        coursesCombo.setBorder(defaultComboBorder);
        positionCombo.setBorder(defaultComboBorder);
        yearCombo.setBorder(defaultComboBorder);
        sectionCombo.setBorder(defaultComboBorder);
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