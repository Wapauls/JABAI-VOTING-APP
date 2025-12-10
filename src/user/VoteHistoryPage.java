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

public class VoteHistoryPage extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JButton closeButton;
    private JLabel voteHistoryPageLabel;
    private JLabel sectionBar;
    private JButton backButton;
    private JButton voteAgainButton;
    
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

    public VoteHistoryPage() {
        setTitle("Voting System");
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
        
        // Vote History Page Header
        voteHistoryPageLabel = new JLabel("Vote History Page");
        voteHistoryPageLabel.setFont(interRegular.deriveFont(24f));
        voteHistoryPageLabel.setForeground(Color.WHITE);
        voteHistoryPageLabel.setBounds(217, 65, 405, 47);
        voteHistoryPageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(voteHistoryPageLabel);
        
        // Vote History Label
        JLabel voteHistoryLabel = new JLabel("Vote History");
        voteHistoryLabel.setFont(interRegular.deriveFont(16f));
        voteHistoryLabel.setForeground(Color.WHITE);
        voteHistoryLabel.setBounds(29, 112, 154, 29);
        mainPanel.add(voteHistoryLabel);
        
        // Candidates Panel (Vote History Table)
        JPanel candidatesPanel = new JPanel();
        candidatesPanel.setLayout(null);
        candidatesPanel.setBounds(23, 146, 794, 304);
        candidatesPanel.setBackground(new Color(217, 217, 217));
        candidatesPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        
        // Table Headers
        JLabel nameHeader = new JLabel("Name");
        nameHeader.setFont(interRegular.deriveFont(14f));
        nameHeader.setForeground(new Color(1, 1, 1));
        nameHeader.setBounds(33, 5, 94, 24);
        nameHeader.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(nameHeader);
        
        JLabel positionHeader = new JLabel("Position");
        positionHeader.setFont(interRegular.deriveFont(14f));
        positionHeader.setForeground(new Color(1, 1, 1));
        positionHeader.setBounds(239, 5, 179, 24);
        positionHeader.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(positionHeader);
        
        JLabel yearHeader = new JLabel("Year");
        yearHeader.setFont(interRegular.deriveFont(14f));
        yearHeader.setForeground(new Color(1, 1, 1));
        yearHeader.setBounds(418, 5, 101, 24);
        yearHeader.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(yearHeader);
        
        JLabel sectionHeader = new JLabel("Section");
        sectionHeader.setFont(interRegular.deriveFont(14f));
        sectionHeader.setForeground(new Color(1, 1, 1));
        sectionHeader.setBounds(519, 5, 130, 24);
        sectionHeader.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(sectionHeader);
        
        JLabel dateHeader = new JLabel("Date");
        dateHeader.setFont(interRegular.deriveFont(14f));
        dateHeader.setForeground(new Color(1, 1, 1));
        dateHeader.setBounds(649, 5, 158, 24);
        dateHeader.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(dateHeader);
        
        // Separator line
        JSeparator separator = new JSeparator();
        separator.setBackground(new Color(97, 97, 97));
        separator.setForeground(new Color(97, 97, 97));
        separator.setBounds(10, 30, 774, 1);
        candidatesPanel.add(separator);
        
        // Row 1 - Juan E. Dela Cruz
        JLabel candidate1 = new JLabel("Juan E. Dela Cruz");
        candidate1.setFont(interRegular.deriveFont(14f));
        candidate1.setForeground(new Color(1, 1, 1));
        candidate1.setBounds(33, 34, 364, 24);
        candidatesPanel.add(candidate1);
        
        JLabel position1 = new JLabel("President");
        position1.setFont(interRegular.deriveFont(14f));
        position1.setForeground(new Color(1, 1, 1));
        position1.setBounds(239, 34, 179, 24);
        position1.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(position1);
        
        JLabel year1 = new JLabel("1st");
        year1.setFont(interRegular.deriveFont(14f));
        year1.setForeground(new Color(1, 1, 1));
        year1.setBounds(418, 34, 101, 24);
        year1.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(year1);
        
        JLabel section1 = new JLabel("C");
        section1.setFont(interRegular.deriveFont(14f));
        section1.setForeground(new Color(1, 1, 1));
        section1.setBounds(519, 34, 130, 24);
        section1.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(section1);
        
        JLabel date1 = new JLabel("13/09/2025");
        date1.setFont(interRegular.deriveFont(14f));
        date1.setForeground(new Color(1, 1, 1));
        date1.setBounds(649, 34, 158, 24);
        date1.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(date1);
        
        // Row 2 - Jack N. Jill
        JLabel candidate2 = new JLabel("Jack N. Jill");
        candidate2.setFont(interRegular.deriveFont(14f));
        candidate2.setForeground(new Color(1, 1, 1));
        candidate2.setBounds(33, 58, 411, 24);
        candidatesPanel.add(candidate2);
        
        JLabel position2 = new JLabel("Vice President");
        position2.setFont(interRegular.deriveFont(14f));
        position2.setForeground(new Color(1, 1, 1));
        position2.setBounds(239, 58, 179, 24);
        position2.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(position2);
        
        JLabel year2 = new JLabel("3rd");
        year2.setFont(interRegular.deriveFont(14f));
        year2.setForeground(new Color(1, 1, 1));
        year2.setBounds(418, 58, 101, 24);
        year2.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(year2);
        
        JLabel section2 = new JLabel("B");
        section2.setFont(interRegular.deriveFont(14f));
        section2.setForeground(new Color(1, 1, 1));
        section2.setBounds(519, 58, 130, 24);
        section2.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(section2);
        
        JLabel date2 = new JLabel("10/09/2025");
        date2.setFont(interRegular.deriveFont(14f));
        date2.setForeground(new Color(1, 1, 1));
        date2.setBounds(649, 58, 158, 24);
        date2.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(date2);
        
        // Row 3 - Mang E. juan
        JLabel candidate3 = new JLabel("Mang E. juan");
        candidate3.setFont(interRegular.deriveFont(14f));
        candidate3.setForeground(new Color(1, 1, 1));
        candidate3.setBounds(33, 82, 411, 24);
        candidatesPanel.add(candidate3);
        
        JLabel position3 = new JLabel("Secretary");
        position3.setFont(interRegular.deriveFont(14f));
        position3.setForeground(new Color(1, 1, 1));
        position3.setBounds(239, 82, 179, 24);
        position3.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(position3);
        
        JLabel year3 = new JLabel("2nd");
        year3.setFont(interRegular.deriveFont(14f));
        year3.setForeground(new Color(1, 1, 1));
        year3.setBounds(418, 82, 101, 24);
        year3.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(year3);
        
        JLabel section3 = new JLabel("A");
        section3.setFont(interRegular.deriveFont(14f));
        section3.setForeground(new Color(1, 1, 1));
        section3.setBounds(519, 82, 130, 24);
        section3.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(section3);
        
        JLabel date3 = new JLabel("08/09/2025");
        date3.setFont(interRegular.deriveFont(14f));
        date3.setForeground(new Color(1, 1, 1));
        date3.setBounds(649, 82, 158, 24);
        date3.setHorizontalAlignment(SwingConstants.CENTER);
        candidatesPanel.add(date3);
        
        mainPanel.add(candidatesPanel);
        
        // Vote Again Button (#48f8fe background) - exact design
        voteAgainButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(72, 248, 254));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw text
                g2d.setColor(Color.BLACK);
                FontMetrics fm = g2d.getFontMetrics();
                String text = "Vote Again";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };
        voteAgainButton.setBounds(23, 473, 147, 30);
        voteAgainButton.setBackground(new Color(72, 248, 254));
        voteAgainButton.setForeground(Color.BLACK);
        voteAgainButton.setFont(interRegular.deriveFont(16f));
        voteAgainButton.setBorder(BorderFactory.createEmptyBorder());
        voteAgainButton.setFocusPainted(false);
        voteAgainButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        voteAgainButton.addActionListener((ActionEvent e) -> {
            // Navigate to VotingPage
            SwingUtilities.invokeLater(() -> new user.VotingPage());
            dispose();
        });
        mainPanel.add(voteAgainButton);
        
        // Back Button - exact design
        backButton = new JButton("← Back");
        backButton.setBounds(754, 477, 58, 22);
        backButton.setBackground(new Color(20, 20, 20));
        backButton.setForeground(new Color(255, 59, 59));
        backButton.setFont(interRegular.deriveFont(16f));
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener((ActionEvent e) -> {
            // Return to UserApp
            try {
                Class<?> userAppClass = Class.forName("user.UserApp");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new user.VoteHistoryPage());
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