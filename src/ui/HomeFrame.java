package ui;

import dao.CartDAO;
import dao.ProductDAO;
import model.Product;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class HomeFrame extends JFrame {

    private User currentUser;
    private ProductDAO productDAO;
    private JTextField searchField;
    private JPanel mainContentPanel;
    private JScrollPane mainScrollPane; // ← made this a class variable

    public HomeFrame(User user) {
        this.currentUser = user;
        this.productDAO = new ProductDAO();

        setTitle("ShopEasy");
        setSize(1100, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
        setLayout(new BorderLayout());

        add(buildNavbar(), BorderLayout.NORTH);

        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(new Color(245, 245, 245));

        // ── MAIN SCROLL PANE ──
        mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setBorder(null);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // ── MOUSE WHEEL SCROLLS PAGE UP/DOWN ──
        addMouseWheelListener(e -> {
            JScrollBar bar = mainScrollPane.getVerticalScrollBar();
            bar.setValue(bar.getValue() + (e.getWheelRotation() * 40));
        });

        add(mainScrollPane, BorderLayout.CENTER);

        loadHomeContent();
    }

    private JPanel buildNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(new Color(33, 150, 243));
        navbar.setPreferredSize(new Dimension(1100, 65));
        navbar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel logo = new JLabel("ShopEasy");
        logo.setFont(new Font("Arial", Font.BOLD, 26));
        logo.setForeground(Color.WHITE);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        centerPanel.setBackground(new Color(33, 150, 243));

        String[] categories = {
            "All Categories", "Electronics", "Clothing",
            "Beauty", "Books", "Sports", "Home & Kitchen"
        };
        JComboBox<String> categoryDropdown = new JComboBox<>(categories);
        categoryDropdown.setFont(new Font("Arial", Font.BOLD, 13));
        categoryDropdown.setPreferredSize(new Dimension(160, 35));
        categoryDropdown.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(280, 35));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(255, 152, 0));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(new Font("Arial", Font.BOLD, 13));
        searchBtn.setFocusPainted(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setPreferredSize(new Dimension(80, 35));
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        centerPanel.add(categoryDropdown);
        centerPanel.add(searchField);
        centerPanel.add(searchBtn);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        rightPanel.setBackground(new Color(33, 150, 243));

        JLabel userLabel = new JLabel("Hi, " + currentUser.getName());
        userLabel.setFont(new Font("Arial", Font.BOLD, 13));
        userLabel.setForeground(Color.WHITE);

        JButton cartBtn = new JButton("My Cart");
        cartBtn.setBackground(Color.WHITE);
        cartBtn.setForeground(new Color(33, 150, 243));
        cartBtn.setFont(new Font("Arial", Font.BOLD, 13));
        cartBtn.setFocusPainted(false);
        cartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton profileBtn = new JButton("Profile");
        profileBtn.setBackground(Color.WHITE);
        profileBtn.setForeground(new Color(33, 150, 243));
        profileBtn.setFont(new Font("Arial", Font.BOLD, 13));
        profileBtn.setFocusPainted(false);
        profileBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(244, 67, 54));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 13));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        rightPanel.add(userLabel);
        rightPanel.add(profileBtn);
        rightPanel.add(cartBtn);
        rightPanel.add(logoutBtn);

        searchBtn.addActionListener(e -> performSearch());
        searchField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) performSearch();
            }
        });

        categoryDropdown.addActionListener(e -> {
            String selected = (String) categoryDropdown.getSelectedItem();
            if (!selected.equals("All Categories")) {
                filterByCategory(selected);
            } else {
                loadHomeContent();
            }
        });

        cartBtn.addActionListener(e -> new CartFrame(currentUser).setVisible(true));
        //profileBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Coming Soon!"));
        profileBtn.addActionListener(e -> {
    new ProfileFrame(currentUser).setVisible(true);
    dispose(); // closes HomeFrame
});
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        navbar.add(logo, BorderLayout.WEST);
        navbar.add(centerPanel, BorderLayout.CENTER);
        navbar.add(rightPanel, BorderLayout.EAST);

        return navbar;
    }

    private void loadHomeContent() {
        mainContentPanel.removeAll();
        mainContentPanel.add(buildHeroBanner());
        mainContentPanel.add(buildSection("Popular Right Now", "popular"));
        mainContentPanel.add(buildSection("For Men", "men"));
        mainContentPanel.add(buildSection("For Women", "women"));
        mainContentPanel.add(buildSection("Books & Learning", "books"));
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) return;
        List<Product> results = productDAO.searchProducts(keyword);
        showSearchResults(results, keyword);
    }

    private void showSearchResults(List<Product> products, String keyword) {
        mainContentPanel.removeAll();

        JLabel resultLabel = new JLabel("Results: " + keyword);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        mainContentPanel.add(resultLabel);

        if (products.isEmpty()) {
            JLabel emptyLabel = new JLabel("No products found!");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            mainContentPanel.add(emptyLabel);
        } else {
            mainContentPanel.add(buildProductGrid(products));
        }

        JButton backBtn = new JButton("Back to Home");
        backBtn.setBackground(new Color(33, 150, 243));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 13));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backBtn.addActionListener(e -> loadHomeContent());
        mainContentPanel.add(backBtn);

        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void filterByCategory(String categoryName) {
        mainContentPanel.removeAll();

        int categoryId = -1;
        switch (categoryName) {
            case "Electronics":    categoryId = 1; break;
            case "Clothing":       categoryId = 2; break;
            case "Books":          categoryId = 3; break;
            case "Beauty":         categoryId = 4; break;
            case "Sports":         categoryId = 5; break;
            case "Home & Kitchen": categoryId = 6; break;
        }

        JLabel resultLabel = new JLabel(categoryName + " Products");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 22));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        mainContentPanel.add(resultLabel);

        List<Product> products = categoryId != -1
            ? productDAO.getProductsByCategory(categoryId)
            : productDAO.getAllProducts();

        if (products.isEmpty()) {
            JLabel emptyLabel = new JLabel("No products found in " + categoryName);
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            mainContentPanel.add(emptyLabel);
        } else {
            mainContentPanel.add(buildProductGrid(products));
        }

        JButton backBtn = new JButton("Back to Home");
        backBtn.setBackground(new Color(33, 150, 243));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 13));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backBtn.addActionListener(e -> loadHomeContent());
        mainContentPanel.add(backBtn);

        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private JPanel buildProductGrid(List<Product> products) {
        JPanel grid = new JPanel(new GridLayout(0, 4, 15, 15));
        grid.setBackground(new Color(245, 245, 245));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        for (int i = 0; i < products.size(); i++) {
            grid.add(buildProductCard(products.get(i), i));
        }
        return grid;
    }

//=================================//
//    PRODUCT CARD COMPONENT       //
//=================================//

    private JPanel buildProductCard(Product p, int colorIndex) {
        Color[] colors = {
            new Color(255, 183, 197), new Color(183, 220, 255),
            new Color(183, 255, 196), new Color(255, 236, 183),
            new Color(220, 183, 255), new Color(255, 218, 183)
        };

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(200, 280));

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(180, 150));
        imagePanel.setMaximumSize(new Dimension(180, 150));
        imagePanel.setBackground(colors[colorIndex % colors.length]);

        ImageIcon icon = new ImageIcon(p.getImagePath());
        if (icon.getIconWidth() > 0) {
            Image scaled = icon.getImage().getScaledInstance(180, 150, Image.SCALE_SMOOTH);
            imagePanel.add(new JLabel(new ImageIcon(scaled)), BorderLayout.CENTER);
        } else {
            JLabel letter = new JLabel(
                String.valueOf(p.getName().charAt(0)).toUpperCase(),
                SwingConstants.CENTER
            );
            letter.setFont(new Font("Arial", Font.BOLD, 50));
            letter.setForeground(Color.WHITE);
            imagePanel.add(letter, BorderLayout.CENTER);
        }

        JLabel nameLabel = new JLabel(p.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel("Rs." + String.format("%.0f", p.getPrice()), SwingConstants.CENTER);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 15));
        priceLabel.setForeground(new Color(33, 150, 243));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addBtn = new JButton("Add to Cart");
        addBtn.setBackground(new Color(33, 150, 243));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        addBtn.addActionListener(e -> {
            CartDAO cartDAO = new CartDAO();
            cartDAO.addToCart(currentUser.getId(), p.getId(), 1);
            JOptionPane.showMessageDialog(this,
                p.getName() + " added to cart!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        card.add(imagePanel);
        card.add(Box.createVerticalStrut(8));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(addBtn);

        return card;
    }
private JPanel buildHeroBanner() {
    String[] bannerImages = {
        "images/banner1.jpg",
        "images/banner2.jpg",
        "images/banner3.jpg"
    };

    // ── MAIN HERO PANEL ──
    JPanel heroPanel = new JPanel(new BorderLayout());
    heroPanel.setPreferredSize(new Dimension(1100, 320));
    heroPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
    heroPanel.setBackground(new Color(20, 20, 40));

    // ── LEFT — Model Girl ──
    JPanel leftPanel = new JPanel(new BorderLayout());
    leftPanel.setBackground(new Color(20, 20, 40));
    leftPanel.setPreferredSize(new Dimension(300, 300));

    ImageIcon leftIcon = new ImageIcon("images/model-left.png");
    if (leftIcon.getIconWidth() > 0) {
        Image leftScaled = leftIcon.getImage()
            .getScaledInstance(340, 330, Image.SCALE_SMOOTH);
        JLabel leftModel = new JLabel(new ImageIcon(leftScaled));
        leftModel.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(leftModel, BorderLayout.CENTER);
    }

    // ── CENTER — Sliding Banner ──
    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.setBackground(new Color(20, 20, 40));

    JLabel bannerLabel = new JLabel();
    bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
    bannerLabel.setBackground(new Color(20, 20, 40));
    bannerLabel.setOpaque(true);

    // Dots
    JPanel dotsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
    dotsPanel.setBackground(new Color(20, 20, 40));

    JLabel[] dots = new JLabel[bannerImages.length];
    for (int i = 0; i < bannerImages.length; i++) {
        dots[i] = new JLabel("●");
        dots[i].setFont(new Font("Arial", Font.PLAIN, 14));
        dots[i].setForeground(i == 0 ? Color.WHITE : new Color(100, 100, 100));
        dotsPanel.add(dots[i]);
    }

    final int[] currentIndex = {0};

    Runnable updateBanner = () -> {
        ImageIcon icon = new ImageIcon(bannerImages[currentIndex[0]]);
        if (icon.getIconWidth() > 0) {
            Image scaled = icon.getImage()
                .getScaledInstance(750, 280, Image.SCALE_SMOOTH);
            bannerLabel.setIcon(new ImageIcon(scaled));
            bannerLabel.setText("");
        } else {
            bannerLabel.setIcon(null);
            bannerLabel.setText("Banner " + (currentIndex[0] + 1));
            bannerLabel.setFont(new Font("Arial", Font.BOLD, 20));
            bannerLabel.setForeground(Color.WHITE);
        }
    };

    updateBanner.run();

    Timer timer = new Timer(3000, e -> {
        dots[currentIndex[0]].setForeground(new Color(100, 100, 100));
        currentIndex[0] = (currentIndex[0] + 1) % bannerImages.length;
        updateBanner.run();
        dots[currentIndex[0]].setForeground(Color.WHITE);
    });
    timer.start();

    bannerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    bannerLabel.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            showSearchResults(productDAO.getAllProducts(), "All Products");
        }
    });

    centerPanel.add(bannerLabel, BorderLayout.CENTER);
    centerPanel.add(dotsPanel, BorderLayout.SOUTH);

    // ── RIGHT — Group Photo ──
    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setBackground(new Color(20, 20, 40));
    rightPanel.setPreferredSize(new Dimension(280, 320));

    ImageIcon rightIcon = new ImageIcon("images/model-right.png");
    if (rightIcon.getIconWidth() > 0) {
        Image rightScaled = rightIcon.getImage()
            .getScaledInstance(350, 340, Image.SCALE_SMOOTH);
        JLabel rightModel = new JLabel(new ImageIcon(rightScaled));
        rightModel.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(rightModel, BorderLayout.CENTER);
    }

    heroPanel.add(leftPanel, BorderLayout.WEST);
    heroPanel.add(centerPanel, BorderLayout.CENTER);
    heroPanel.add(rightPanel, BorderLayout.EAST);

    return heroPanel;
}

    private JPanel buildSection(String title, String section) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(new Color(245, 245, 245));
        sectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 245));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 33, 33));

        JButton seeAllBtn = new JButton("See All");
        seeAllBtn.setBackground(new Color(33, 150, 243));
        seeAllBtn.setForeground(Color.WHITE);
        seeAllBtn.setFont(new Font("Arial", Font.BOLD, 12));
        seeAllBtn.setFocusPainted(false);
        seeAllBtn.setBorderPainted(false);
        seeAllBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        seeAllBtn.addActionListener(e -> {
            List<Product> products = productDAO.getProductsBySection(section);
            showSearchResults(products, title);
        });

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(seeAllBtn, BorderLayout.EAST);

        JPanel productsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        productsRow.setBackground(new Color(245, 245, 245));

        List<Product> products = productDAO.getProductsBySection(section);
        if (products.isEmpty()) {
            productsRow.add(new JLabel("No products in this section yet."));
        } else {
            for (int i = 0; i < products.size(); i++) {
                productsRow.add(buildProductCard(products.get(i), i));
            }
        }

        // ── HORIZONTAL SCROLL with mouse wheel ──
        JScrollPane rowScroll = new JScrollPane(productsRow);
        rowScroll.setBorder(null);
        rowScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rowScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        rowScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));
        rowScroll.setPreferredSize(new Dimension(1060, 320));
        rowScroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));//remove scroal bar
        rowScroll.getHorizontalScrollBar().setUnitIncrement(20);

        // Mouse wheel on section → scroll LEFT/RIGHT
        // When not on section → scroll UP/DOWN on main page
        rowScroll.addMouseWheelListener(e -> {
            if (e.isShiftDown()) {
                // Shift + scroll = horizontal
                JScrollBar bar = rowScroll.getHorizontalScrollBar();
                bar.setValue(bar.getValue() + (e.getWheelRotation() * 30));
                e.consume();
            } else {
                // Normal scroll = pass to main page
                JScrollBar bar = mainScrollPane.getVerticalScrollBar();
                bar.setValue(bar.getValue() + (e.getWheelRotation() * 40));
            }
        });

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(new Color(220, 220, 220));

        sectionPanel.add(headerPanel);
        sectionPanel.add(Box.createVerticalStrut(10));
        sectionPanel.add(rowScroll);
        sectionPanel.add(Box.createVerticalStrut(10));
        sectionPanel.add(separator);

        return sectionPanel;
    }
}