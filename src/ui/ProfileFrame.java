package ui;

import dao.OrderDAO;
import dao.UserDAO;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProfileFrame extends JFrame {

    private User currentUser;
    private OrderDAO orderDAO;

    public ProfileFrame(User user) {
        this.currentUser = user;
        this.orderDAO = new OrderDAO();

        setTitle("My Profile - ShopEasy");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildNavbar(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);
    }

    // ── NAVBAR ──
    private JPanel buildNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(new Color(33, 150, 243));
        navbar.setPreferredSize(new Dimension(1100, 60));
        navbar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel logo = new JLabel("ShopEasy");
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);

        JButton backBtn = new JButton("Back to Home");
        backBtn.setBackground(Color.WHITE);
        backBtn.setForeground(new Color(33, 150, 243));
        backBtn.setFont(new Font("Arial", Font.BOLD, 13));
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            new HomeFrame(currentUser).setVisible(true);
            dispose();
        });

        navbar.add(logo, BorderLayout.WEST);
        navbar.add(backBtn, BorderLayout.EAST);

        return navbar;
    }

    // ── BODY ──
    private JPanel buildBody() {
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(new Color(245, 245, 245));

        JPanel card = buildProfileCard();
        body.add(card);

        return body;
    }

    // ── PROFILE CARD ──
    private JPanel buildProfileCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(0, 0, 30, 0)
        ));
        card.setPreferredSize(new Dimension(550, 680));
        card.setMaximumSize(new Dimension(550, 680));

        // ── AVATAR HEADER ──
        JPanel avatarPanel = new JPanel();
        avatarPanel.setLayout(new BoxLayout(avatarPanel, BoxLayout.Y_AXIS));
        avatarPanel.setBackground(new Color(33, 150, 243));
        avatarPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 25, 20));
        avatarPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Circle avatar
        JLabel avatar = new JLabel(
            String.valueOf(currentUser.getName().charAt(0)).toUpperCase(),
            SwingConstants.CENTER
        );
        avatar.setFont(new Font("Arial", Font.BOLD, 36));
        avatar.setForeground(new Color(33, 150, 243));
        avatar.setBackground(Color.WHITE);
        avatar.setOpaque(true);
        avatar.setPreferredSize(new Dimension(75, 75));
        avatar.setMaximumSize(new Dimension(75, 75));
        avatar.setMinimumSize(new Dimension(75, 75));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(currentUser.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel(currentUser.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        emailLabel.setForeground(new Color(200, 230, 255));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        avatarPanel.add(avatar);
        avatarPanel.add(Box.createVerticalStrut(10));
        avatarPanel.add(nameLabel);
        avatarPanel.add(Box.createVerticalStrut(4));
        avatarPanel.add(emailLabel);

        // ── STATS ──
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 1, 0));
        statsPanel.setBackground(new Color(240, 240, 240));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        List<String[]> orders = orderDAO.getOrdersByUser(currentUser.getId());
        int active = 0, completed = 0;
        for (String[] o : orders) {
            if (o[2].equals("Active")) active++;
            if (o[2].equals("Completed")) completed++;
        }

        statsPanel.add(buildStatCard(String.valueOf(orders.size()), "Total", new Color(33, 150, 243)));
        statsPanel.add(buildStatCard(String.valueOf(active), "Active", new Color(255, 152, 0)));
        statsPanel.add(buildStatCard(String.valueOf(completed), "Completed", new Color(76, 175, 80)));

        // ── ACCOUNT INFO ──
        JPanel infoSection = new JPanel();
        infoSection.setLayout(new BoxLayout(infoSection, BoxLayout.Y_AXIS));
        infoSection.setBackground(Color.WHITE);
        infoSection.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        infoSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        JLabel infoTitle = new JLabel("Account Information");
        infoTitle.setFont(new Font("Arial", Font.BOLD, 15));
        infoTitle.setForeground(new Color(33, 33, 33));
        infoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(220, 220, 220));

        infoSection.add(infoTitle);
        infoSection.add(Box.createVerticalStrut(10));
        infoSection.add(sep);
        infoSection.add(Box.createVerticalStrut(12));
        infoSection.add(buildInfoRow("Full Name", currentUser.getName()));
        infoSection.add(Box.createVerticalStrut(10));
        infoSection.add(buildInfoRow("Email Address", currentUser.getEmail()));
        infoSection.add(Box.createVerticalStrut(10));
        infoSection.add(buildInfoRow("Password", "••••••••"));

        // ── BUTTONS ──
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 0, 30));

        JButton ordersBtn = buildButton("View My Orders", new Color(33, 150, 243));
        JButton updateBtn = buildButton("Update Profile", new Color(76, 175, 80));
        JButton closeBtn  = buildButton("Back to Home", new Color(244, 67, 54));

        ordersBtn.addActionListener(e -> new OrdersFrame(currentUser).setVisible(true));
        updateBtn.addActionListener(e -> showUpdateDialog());
        closeBtn.addActionListener(e -> {
            new HomeFrame(currentUser).setVisible(true);
            dispose();
        });

        btnPanel.add(ordersBtn);
        btnPanel.add(Box.createVerticalStrut(10));
        btnPanel.add(updateBtn);
        btnPanel.add(Box.createVerticalStrut(10));
        btnPanel.add(closeBtn);

        card.add(avatarPanel);
        card.add(statsPanel);
        card.add(infoSection);
        card.add(btnPanel);

        return card;
    }

    // ── REUSABLE BUTTON ──
    private JButton buildButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 42));
        btn.setPreferredSize(new Dimension(300, 42));
        return btn;
    }

    // ── STAT CARD ──
    private JPanel buildStatCard(String value, String label, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(label, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        nameLabel.setForeground(Color.GRAY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(12));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(nameLabel);

        return card;
    }

    // ── INFO ROW ──
    private JPanel buildInfoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel labelL = new JLabel(label);
        labelL.setFont(new Font("Arial", Font.BOLD, 13));
        labelL.setForeground(Color.GRAY);

        JLabel valueL = new JLabel(value);
        valueL.setFont(new Font("Arial", Font.PLAIN, 13));
        valueL.setForeground(new Color(33, 33, 33));

        row.add(labelL, BorderLayout.WEST);
        row.add(valueL, BorderLayout.EAST);

        return row;
    }

    // ── UPDATE DIALOG ──
    private void showUpdateDialog() {
        JDialog dialog = new JDialog(this, "Update Profile", true);
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 150, 243));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel("Update Your Profile");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        header.add(headerLabel);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField nameField = new JTextField(currentUser.getName());
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel passLabel = new JLabel("New Password (leave blank to keep same)");
        passLabel.setFont(new Font("Arial", Font.BOLD, 13));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField passField = new JPasswordField();
        passField.setFont(new Font("Arial", Font.PLAIN, 14));
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(nameLabel);
        form.add(Box.createVerticalStrut(5));
        form.add(nameField);
        form.add(Box.createVerticalStrut(15));
        form.add(passLabel);
        form.add(Box.createVerticalStrut(5));
        form.add(passField);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 13));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setBackground(new Color(76, 175, 80));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 13));
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        saveBtn.addActionListener(e -> {
            String newName = nameField.getText().trim();
            String newPass = new String(passField.getPassword()).trim();

            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Name cannot be empty!", "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.updateProfile(
                currentUser.getId(), newName,
                newPass.isEmpty() ? currentUser.getPassword() : newPass
            );

            if (success) {
                currentUser.setName(newName);
                if (!newPass.isEmpty()) currentUser.setPassword(newPass);
                JOptionPane.showMessageDialog(dialog,
                    "Profile updated!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                dispose();
                new ProfileFrame(currentUser).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(dialog,
                    "Update failed!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        footer.add(cancelBtn);
        footer.add(saveBtn);

        dialog.add(header, BorderLayout.NORTH);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(footer, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}