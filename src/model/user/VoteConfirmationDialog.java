package model.user;

import connectionDB.DatabaseHelper;
import connectionDB.Vote;

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

public class VoteConfirmationDialog extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JButton closeButton;
    private JLabel confirmationLabel;
    private JLabel sectionBar;
    private JLabel messageLabel;
    private JButton yesButton;
    private JButton noButton;
    @SuppressWarnings("unused")
    private String candidateName;
    @SuppressWarnings("unused")
    private String position;
    @SuppressWarnings("unused")
    private JFrame votingPageFrame;
    // studentID of the voter who is casting this vote (may be null)
    private String studentID;
    
        // Vote details
    private String voteCandidate;
    private String votePosition;
    private String voteCourse;
    private String voteYear;
    private String voteSection;
    
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
    
    // Custom JLabel for multiline text without HTML
    class MultiLineLabel extends JLabel {
        private String[] lines;
        
        public MultiLineLabel(String text) {
            setForeground(Color.WHITE);
            setBackground(new Color(20, 20, 20));
            setOpaque(false);
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            
            // Split text into lines
            lines = text.split("\n");
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            // Draw background
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            
            // Draw text
            g.setColor(getForeground());
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setFont(getFont());
            
            FontMetrics fm = g2d.getFontMetrics();
            int lineHeight = fm.getHeight();
            int totalHeight = lines.length * lineHeight;
            int startY = (getHeight() - totalHeight) / 2 + fm.getAscent();
            
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                int x = (getWidth() - fm.stringWidth(line)) / 2;
                int y = startY + (i * lineHeight);
                g2d.drawString(line, x, y);
            }
        }
    }

    public VoteConfirmationDialog() {
        this(null, null, null, null, null, null);
    }
    
    public VoteConfirmationDialog(String candidate, String position, String course, String year, String section, String studentID) {
        this.voteCandidate = candidate == null ? "" : candidate;
        this.votePosition = position == null ? "" : position;
        this.voteCourse = course == null ? "" : course;
        this.voteYear = year == null ? "" : year;
        this.voteSection = section == null ? "" : section;
        this.studentID = studentID;
        
        setTitle("Voting System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(424, 259);
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
        
        // Try to load close icon via ResourceLoader
        try {
            ImageIcon closeIcon = resourceLoader.loadIcon("close.png");
            if (closeIcon != null && closeIcon.getIconWidth() > 0) {
                closeButton.setIcon(closeIcon);
            } else {
                throw new Exception("Close icon not found");
            }
        } catch (Exception e) {
            // Fallback to text if icon not found
            closeButton.setText("✕");
            closeButton.setForeground(Color.WHITE);
            closeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        }
        
        closeButton.addActionListener((ActionEvent e) -> dispose());
        mainPanel.add(closeButton);
        
        // Section Bar divider (#1c1c1c)
        sectionBar = new JLabel();
        sectionBar.setBackground(new Color(28, 28, 28));
        sectionBar.setOpaque(true);
        sectionBar.setBounds(8, 57, 407, 2);
        mainPanel.add(sectionBar);
        
        // Confirmation Header (Bold)
        confirmationLabel = new JLabel("Confirmation");
        confirmationLabel.setFont(interBold.deriveFont(24f));
        confirmationLabel.setForeground(Color.WHITE);
        confirmationLabel.setBounds(95, 67, 233, 46);
        confirmationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(confirmationLabel);
        
        // Confirmation Message
        String messageText = "Are you sure you want to vote for\n" +
                             (voteCandidate == null || voteCandidate.isEmpty() ? "this candidate" : voteCandidate) +
                             " for the position of " +
                             (votePosition == null || votePosition.isEmpty() ? "this position" : votePosition) + "?";
        
        messageLabel = new MultiLineLabel(messageText);
        messageLabel.setFont(interRegular.deriveFont(16f));
        messageLabel.setBounds(19, 113, 385, 64);
        mainPanel.add(messageLabel);
        
        // Yes Button (#48f8fe background)
        yesButton = new JButton("Yes");
        yesButton.setBounds(48, 205, 118, 30);
        yesButton.setBackground(new Color(72, 248, 254));
        yesButton.setForeground(Color.BLACK);
        yesButton.setFont(interRegular.deriveFont(16f));
        yesButton.setBorder(BorderFactory.createEmptyBorder());
        yesButton.setFocusPainted(false);
        yesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        yesButton.addActionListener(e -> {
            // Record the vote to the database
            if (!voteCandidate.isEmpty()) {
                // Prevent duplicate vote for same candidate/position by same user
                if (studentID != null && !studentID.isEmpty()
                        && DatabaseHelper.hasUserVotedForCandidate(studentID, voteCandidate, votePosition)) {
                    new MessageDialog("Already Voted",
                            "You have already voted for this candidate and position.",
                            MessageDialog.WARNING);
                } else {
                    Vote voteRecord = new Vote(
                        java.time.Instant.now().toString(),
                        voteCandidate,
                        votePosition,
                        voteCourse,
                        voteYear,
                        voteSection,
                        studentID
                    );
                    DatabaseHelper.recordVote(voteRecord);
                    
                    // Mark voter as voted if studentID is available
                    if (studentID != null && !studentID.isEmpty()) {
                        DatabaseHelper.markVoterAsVoted(studentID);
                    }
                }
            }
            
            // Re-open Vote History Page for this user (if known)
            SwingUtilities.invokeLater(() -> {
                if (studentID != null && !studentID.isEmpty()) {
                    new model.user.VoteHistoryPage(studentID);
                } else {
                    new model.user.VoteHistoryPage();
                }
            });

            // Close VotingPage frame
            if (votingPageFrame != null) {
                votingPageFrame.dispose();
            }

            // Close this dialog
            dispose();
        });


        mainPanel.add(yesButton);
        
        // No Button (#ff2e12 background)
        noButton = new JButton("No");
        noButton.setBounds(256, 205, 118, 30);
        noButton.setBackground(new Color(255, 46, 18));
        noButton.setForeground(Color.WHITE);
        noButton.setFont(interRegular.deriveFont(16f));
        noButton.setBorder(BorderFactory.createEmptyBorder());
        noButton.setFocusPainted(false);
        noButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        noButton.addActionListener((ActionEvent e) -> {
            dispose();
        });
        mainPanel.add(noButton);
        
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new model.user.VoteConfirmationDialog());
    }
    
    public void setVotingPageFrame(JFrame frame) {
        this.votingPageFrame = frame;
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
