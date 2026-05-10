package ui;

import dao.OrderDAO;
import model.User;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class OrdersFrame extends JFrame {

    private User currentUser;
    private OrderDAO orderDAO;
    private JTable ordersTable;
    private DefaultTableModel tableModel;

    public OrdersFrame(User user) {
        this.currentUser = user;
        this.orderDAO = new OrderDAO();

        setTitle("My Orders - " + user.getName());
        setSize(800, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new BorderLayout());

        // ── TOP BAR ──
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(33, 150, 243));
        topBar.setPreferredSize(new Dimension(800, 60));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("My Orders");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(Color.WHITE);
        backBtn.setForeground(new Color(33, 150, 243));
        backBtn.setFont(new Font("Arial", Font.BOLD, 13));
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());

        topBar.add(title, BorderLayout.WEST);
        topBar.add(backBtn, BorderLayout.EAST);

        // ── ORDERS TABLE ──
        String[] columns = {"#", "Order ID", "Total (Rs.)", "Status", "Date", "Action"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        ordersTable = new JTable(tableModel);
        ordersTable.setRowHeight(45);
        ordersTable.setFont(new Font("Arial", Font.PLAIN, 14));
        ordersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        ordersTable.getTableHeader().setBackground(new Color(33, 150, 243));
        ordersTable.getTableHeader().setForeground(Color.WHITE);
        ordersTable.setSelectionBackground(new Color(200, 230, 255));
        ordersTable.setGridColor(new Color(230, 230, 230));

        // Custom renderer for status column (color coding)
        ordersTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("Arial", Font.BOLD, 13));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);

                switch (value.toString()) {
                    case "Active":
                        label.setBackground(new Color(227, 242, 253));
                        label.setForeground(new Color(33, 150, 243));
                        break;
                    case "Completed":
                        label.setBackground(new Color(232, 245, 233));
                        label.setForeground(new Color(76, 175, 80));
                        break;
                    case "Cancelled":
                        label.setBackground(new Color(255, 235, 238));
                        label.setForeground(new Color(244, 67, 54));
                        break;
                    default:
                        label.setBackground(Color.WHITE);
                        label.setForeground(Color.BLACK);
                }
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── BOTTOM BUTTONS ──
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton viewItemsBtn = new JButton("View Items");
        viewItemsBtn.setBackground(new Color(33, 150, 243));
        viewItemsBtn.setForeground(Color.WHITE);
        viewItemsBtn.setFont(new Font("Arial", Font.BOLD, 13));
        viewItemsBtn.setFocusPainted(false);
        viewItemsBtn.setBorderPainted(false);
        viewItemsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton cancelBtn = new JButton("Cancel Order");
        cancelBtn.setBackground(new Color(244, 67, 54));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 13));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottomPanel.add(cancelBtn);
        bottomPanel.add(viewItemsBtn);

        // ── VIEW ITEMS ACTION ──
        viewItemsBtn.addActionListener(e -> {
            int selectedRow = ordersTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                    "Please select an order first!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int orderId = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
            showOrderItems(orderId);
        });

        // ── CANCEL ORDER ACTION ──
        cancelBtn.addActionListener(e -> {
            int selectedRow = ordersTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                    "Please select an order first!",
                    "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String status = tableModel.getValueAt(selectedRow, 3).toString();
            if (status.equals("Completed")) {
                JOptionPane.showMessageDialog(this,
                    "Cannot cancel a completed order!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (status.equals("Cancelled")) {
                JOptionPane.showMessageDialog(this,
                    "Order is already cancelled!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this order?",
                "Confirm Cancel", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                int orderId = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
                orderDAO.updateOrderStatus(orderId, "Cancelled");
                JOptionPane.showMessageDialog(this,
                    "Order cancelled successfully!",
                    "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                loadOrders();
            }
        });

        add(topBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadOrders();
    }

    // ── LOAD ORDERS ──
    private void loadOrders() {
        tableModel.setRowCount(0);
        List<String[]> orders = orderDAO.getOrdersByUser(currentUser.getId());

        if (orders.isEmpty()) {
            JLabel emptyLabel = new JLabel("No orders yet!", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.BOLD, 16));
            emptyLabel.setForeground(Color.GRAY);
        }

        int rowNum = 1;
        for (String[] order : orders) {
            tableModel.addRow(new Object[]{
                rowNum++,
                order[0],  // order id
                order[1],  // total
                order[2],  // status
                order[3]   // date
            });
        }
    }

    // ── SHOW ORDER ITEMS POPUP ──
    private void showOrderItems(int orderId) {
        List<String[]> items = orderDAO.getOrderItems(orderId);

        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No items found for this order!",
                "Order Items", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Build popup
        JDialog dialog = new JDialog(this, "Order #" + orderId + " Details", true);
        dialog.setSize(550, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 150, 243));
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel headerLabel = new JLabel("Order #" + orderId + " Items");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        header.add(headerLabel, BorderLayout.WEST);

        // Items table
        String[] cols = {"Product Name", "Price (Rs.)", "Qty", "Subtotal (Rs.)"};
        DefaultTableModel itemModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        double grandTotal = 0;
        for (String[] item : items) {
            itemModel.addRow(new Object[]{
                item[0],  // name
                item[1],  // price
                item[2],  // qty
                item[3]   // subtotal
            });
            grandTotal += Double.parseDouble(item[3]);
        }

        JTable itemTable = new JTable(itemModel);
        itemTable.setRowHeight(40);
        itemTable.setFont(new Font("Arial", Font.PLAIN, 13));
        itemTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        itemTable.getTableHeader().setBackground(new Color(33, 150, 243));
        itemTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane itemScroll = new JScrollPane(itemTable);
        itemScroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Footer total
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel totalLabel = new JLabel("Grand Total: Rs." + String.format("%.2f", grandTotal));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(33, 150, 243));

        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(new Color(33, 150, 243));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Arial", Font.BOLD, 13));
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(ev -> dialog.dispose());

        footer.add(totalLabel, BorderLayout.WEST);
        footer.add(closeBtn, BorderLayout.EAST);

        dialog.add(header, BorderLayout.NORTH);
        dialog.add(itemScroll, BorderLayout.CENTER);
        dialog.add(footer, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}