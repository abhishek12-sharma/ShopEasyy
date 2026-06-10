package ui;

import dao.OrderDAO;
import dao.CartDAO;
import model.CartItem;
import model.User;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class CartFrame extends JFrame {

    private User currentUser;
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private List<Integer> cartItemIds = new ArrayList<>();

    public CartFrame(User user) {
        this.currentUser = user;

        setTitle("ShopEasy - My Cart");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(33, 150, 243));
        topBar.setPreferredSize(new Dimension(700, 60));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("My Cart");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JButton backBtn = new JButton("Back to Shop");
        backBtn.setBackground(Color.WHITE);
        backBtn.setForeground(new Color(33, 150, 243));
        backBtn.setFont(new Font("Arial", Font.BOLD, 13));
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        topBar.add(title, BorderLayout.WEST);
        topBar.add(backBtn, BorderLayout.EAST);

        // Table — no ID column
        String[] columns = {"Product Name", "Price (Rs.)", "Quantity", "Subtotal (Rs.)"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        cartTable = new JTable(tableModel);
        cartTable.setRowHeight(40);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 14));
        cartTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        cartTable.getTableHeader().setBackground(new Color(33, 150, 243));
        cartTable.getTableHeader().setForeground(Color.WHITE);
        cartTable.setSelectionBackground(new Color(200, 230, 255));

        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        totalLabel = new JLabel("Total: Rs.0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(33, 150, 243));

        JButton removeBtn = new JButton("Remove Selected");
        removeBtn.setBackground(new Color(244, 67, 54));
        removeBtn.setForeground(Color.WHITE);
        removeBtn.setFont(new Font("Arial", Font.BOLD, 14));
        removeBtn.setFocusPainted(false);
        removeBtn.setBorderPainted(false);
        removeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton checkoutBtn = new JButton("Checkout");
        checkoutBtn.setBackground(new Color(76, 175, 80));
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        checkoutBtn.setFocusPainted(false);
        checkoutBtn.setBorderPainted(false);
        checkoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(removeBtn);
        btnPanel.add(checkoutBtn);

        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(btnPanel, BorderLayout.EAST);

        // Remove button action
        removeBtn.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                    "Please select an item to remove!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int cartItemId = cartItemIds.get(selectedRow);
            CartDAO cartDAO = new CartDAO();
            cartDAO.removeFromCart(cartItemId);
            JOptionPane.showMessageDialog(this,
                "Item removed!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
            loadCart();
        });

        // Checkout button action
        checkoutBtn.addActionListener(e -> {
            CartDAO cartDAO = new CartDAO();
            List<CartItem> items = cartDAO.getCartByUser(currentUser.getId());

            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Your cart is empty!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double total = 0;
            for (CartItem item : items) {
                total += item.getPrice() * item.getQuantity();
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                "Place order for Rs." + String.format("%.2f", total) + "?",
                "Confirm Order", JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;

            OrderDAO orderDAO = new OrderDAO();
            int orderId = orderDAO.placeOrder(currentUser.getId(), items, total);

            if (orderId != -1) {
                JOptionPane.showMessageDialog(this,
                    "Order #" + orderId + " placed!\n" +
                    "Total: Rs." + String.format("%.2f", total) + "\n" +
                    "Thank you " + currentUser.getName() + "!",
                    "Order Placed!", JOptionPane.INFORMATION_MESSAGE);
                loadCart();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Order failed! Try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Back button action
        backBtn.addActionListener(e -> dispose());

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadCart();
    }

    private void loadCart() {
        tableModel.setRowCount(0);
        cartItemIds.clear();

        CartDAO cartDAO = new CartDAO();
        List<CartItem> items = cartDAO.getCartByUser(currentUser.getId());

        double total = 0;

        for (CartItem item : items) {
            double subtotal = item.getPrice() * item.getQuantity();
            total += subtotal;
            cartItemIds.add(item.getId());

            tableModel.addRow(new Object[]{
                item.getProductName(),
                String.format("%.2f", item.getPrice()),
                item.getQuantity(),
                String.format("%.2f", subtotal)
            });
        }

        totalLabel.setText("Total: Rs." + String.format("%.2f", total));
    }
}