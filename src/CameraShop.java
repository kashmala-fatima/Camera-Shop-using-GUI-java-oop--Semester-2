import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

// ═══════════════════════════════════════════════════════════════
//  MODEL CLASSES
// ═══════════════════════════════════════════════════════════════

class Product {
    private static int counter = 1;
    private int id;
    private String name, brand, category, description;
    private double price;
    private int quantity;

    public Product(String name, String brand, String category, double price, int quantity, String description) {
        this.id = counter++;
        this.name = name; this.brand = brand; this.category = category;
        this.price = price; this.quantity = quantity; this.description = description;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getDescription() { return description; }

    public void setName(String v) { name = v; }
    public void setBrand(String v) { brand = v; }
    public void setCategory(String v) { category = v; }
    public void setPrice(double v) { price = v; }
    public void setQuantity(int v) { quantity = v; }
    public void setDescription(String v) { description = v; }

    public void reduceQty(int q) { quantity -= q; }
    public void restoreQty(int q) { quantity += q; }
}

class CartItem {
    private Product product;
    private int quantity;
    public CartItem(Product p, int q) { product = p; quantity = q; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int q) { quantity = q; }
    public double getSubtotal() { return product.getPrice() * quantity; }
}

class SoldRecord {
    private static int receiptCounter = 1001;
    private int receiptNo;
    private ArrayList<CartItem> items;
    private double total;
    private String dateTime, status;

    public SoldRecord(ArrayList<CartItem> items, double total) {
        this.receiptNo = receiptCounter++;
        this.items = new ArrayList<>(items);
        this.total = total;
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        this.status = "COMPLETED";
    }

    public int getReceiptNo() { return receiptNo; }
    public ArrayList<CartItem> getItems() { return items; }
    public double getTotal() { return total; }
    public String getDateTime() { return dateTime; }
    public String getStatus() { return status; }
    public void setStatus(String s) { status = s; }
}

class User {
    private String username, password, role;
    public User(String u, String p, String r) { username = u; password = p; role = r; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public boolean isAdmin() { return role.equals("admin"); }
}

// ═══════════════════════════════════════════════════════════════
//  SHARED DATA STORE
// ═══════════════════════════════════════════════════════════════

class DataStore {
    static ArrayList<Product> products = new ArrayList<>();
    static ArrayList<SoldRecord> sales = new ArrayList<>();
    static ArrayList<CartItem> cart = new ArrayList<>();
    static ArrayList<User> users = new ArrayList<>();
    static User loggedInUser = null;

    static {
        users.add(new User("admin", "admin123", "admin"));
        users.add(new User("staff", "staff123", "staff"));

        products.add(new Product("Canon EOS R50",    "Canon",    "DSLR Camera",  85000, 10, "24.2MP Mirrorless"));
        products.add(new Product("Sony A7 IV",        "Sony",     "Mirrorless",  230000,  5, "33MP Full Frame"));
        products.add(new Product("Nikon Z50",         "Nikon",    "Mirrorless",   75000,  8, "20.9MP APS-C"));
        products.add(new Product("Canon 50mm Lens",   "Canon",    "Lens",         12000, 15, "f/1.8 Prime Lens"));
        products.add(new Product("Sony 85mm Lens",    "Sony",     "Lens",         45000,  7, "f/1.4 G Master"));
        products.add(new Product("SD Card 128GB",     "SanDisk",  "Accessory",     3500, 30, "V30 Speed Class"));
        products.add(new Product("Camera Bag",        "Lowepro",  "Accessory",     5500, 20, "Waterproof Bag"));
        products.add(new Product("Tripod Pro",        "Joby",     "Accessory",     8000, 12, "Carbon Fiber"));
    }

    static Product findById(int id) {
        for (Product p : products) if (p.getId() == id) return p;
        return null;
    }

    static void addToCart(Product p, int qty) {
        for (CartItem item : cart) {
            if (item.getProduct().getId() == p.getId()) {
                item.setQuantity(item.getQuantity() + qty);
                return;
            }
        }
        cart.add(new CartItem(p, qty));
    }

    static double cartTotal() {
        double t = 0;
        for (CartItem i : cart) t += i.getSubtotal();
        return t;
    }
}

// ═══════════════════════════════════════════════════════════════
//  THEME HELPER
// ═══════════════════════════════════════════════════════════════

class Theme {
    static final Color BG        = new Color(18, 18, 32);
    static final Color PANEL     = new Color(28, 28, 46);
    static final Color CARD      = new Color(36, 36, 58);
    static final Color ACCENT    = new Color(99, 102, 241);
    static final Color ACCENT2   = new Color(139, 92, 246);
    static final Color SUCCESS   = new Color(34, 197, 94);
    static final Color DANGER    = new Color(239, 68, 68);
    static final Color WARNING   = new Color(234, 179, 8);
    static final Color TEXT      = new Color(241, 245, 249);
    static final Color MUTED     = new Color(148, 163, 184);
    static final Color BORDER    = new Color(55, 55, 80);
    static final Color TABLE_ROW = new Color(32, 32, 52);
    static final Color TABLE_ALT = new Color(40, 40, 62);

    static JButton primaryBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(ACCENT);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(ACCENT2); }
            public void mouseExited(MouseEvent e)  { b.setBackground(ACCENT); }
        });
        return b;
    }

    static JButton dangerBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(DANGER);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        return b;
    }

    static JButton successBtn(String text) {
        JButton b = new JButton(text);
        b.setBackground(SUCCESS);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        return b;
    }

    static JTextField styledField() {
        JTextField f = new JTextField();
        f.setBackground(new Color(45, 45, 70));
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return f;
    }

    static JPasswordField styledPass() {
        JPasswordField f = new JPasswordField();
        f.setBackground(new Color(45, 45, 70));
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return f;
    }

    static JLabel label(String text, int size, boolean bold) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT);
        l.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
        return l;
    }

    static JLabel mutedLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(MUTED);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return l;
    }

    static void styleTable(JTable t) {
        t.setBackground(TABLE_ROW);
        t.setForeground(TEXT);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setRowHeight(32);
        t.setGridColor(BORDER);
        t.setSelectionBackground(ACCENT);
        t.setSelectionForeground(Color.WHITE);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.getTableHeader().setBackground(CARD);
        t.getTableHeader().setForeground(MUTED);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT));
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                setForeground(isSelected ? Color.WHITE : TEXT);
                setBackground(isSelected ? ACCENT : (row % 2 == 0 ? TABLE_ROW : TABLE_ALT));
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
    }

    static JScrollPane scrollPane(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBackground(BG);
        sp.getViewport().setBackground(TABLE_ROW);
        sp.setBorder(BorderFactory.createLineBorder(BORDER));
        sp.getVerticalScrollBar().setBackground(CARD);
        return sp;
    }

    static JPanel card(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        return p;
    }
}

// ═══════════════════════════════════════════════════════════════
//  LOGIN FRAME
// ═══════════════════════════════════════════════════════════════

class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JLabel errorLabel;
    private int attempts = 0;

    public LoginFrame() {
        setTitle("Camera Shop - Login");
        setSize(440, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(Theme.BG);
        setContentPane(root);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 0, 6, 0);
        gc.gridx = 0; gc.weightx = 1;

        // Camera icon label
        JLabel icon = new JLabel("\uD83D\uDCF7", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        gc.gridy = 0; gc.insets = new Insets(0, 0, 4, 0);
        card.add(icon, gc);

        JLabel title = Theme.label("Camera Shop", 24, true);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Theme.ACCENT);
        gc.gridy = 1;
        card.add(title, gc);

        JLabel sub = Theme.mutedLabel("Inventory Management System");
        sub.setHorizontalAlignment(SwingConstants.CENTER);
        gc.gridy = 2; gc.insets = new Insets(0, 0, 24, 0);
        card.add(sub, gc);

        gc.insets = new Insets(6, 0, 4, 0);
        gc.gridy = 3;
        card.add(Theme.label("Username", 13, false), gc);
        gc.gridy = 4;
        userField = Theme.styledField();
        userField.setPreferredSize(new Dimension(300, 40));
        card.add(userField, gc);

        gc.gridy = 5;
        card.add(Theme.label("Password", 13, false), gc);
        gc.gridy = 6;
        passField = Theme.styledPass();
        passField.setPreferredSize(new Dimension(300, 40));
        card.add(passField, gc);

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Theme.DANGER);
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gc.gridy = 7; gc.insets = new Insets(4, 0, 4, 0);
        card.add(errorLabel, gc);

        JButton loginBtn = Theme.primaryBtn("LOGIN");
        loginBtn.setPreferredSize(new Dimension(300, 44));
        gc.gridy = 8; gc.insets = new Insets(8, 0, 8, 0);
        card.add(loginBtn, gc);

        JLabel hint = Theme.mutedLabel("admin / admin123   |   staff / staff123");
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        gc.gridy = 9; gc.insets = new Insets(12, 0, 0, 0);
        card.add(hint, gc);

        root.add(card);

        loginBtn.addActionListener(e -> doLogin());
        passField.addActionListener(e -> doLogin());
        userField.addActionListener(e -> passField.requestFocus());
    }

    private void doLogin() {
        String u = userField.getText().trim();
        String p = new String(passField.getPassword()).trim();
        for (User user : DataStore.users) {
            if (user.getUsername().equals(u) && user.getPassword().equals(p)) {
                DataStore.loggedInUser = user;
                dispose();
                new MainFrame().setVisible(true);
                return;
            }
        }
        attempts++;
        if (attempts >= 3) {
            JOptionPane.showMessageDialog(this, "Too many failed attempts!", "Locked", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        errorLabel.setText("Invalid credentials. " + (3 - attempts) + " attempt(s) left.");
        passField.setText("");
    }
}

// ═══════════════════════════════════════════════════════════════
//  MAIN FRAME (Dashboard with sidebar)
// ═══════════════════════════════════════════════════════════════

class MainFrame extends JFrame {
    private JPanel contentArea;
    private CardLayout cardLayout;
    private JLabel userLabel;

    // panels
    private ProductPanel productPanel;
    private SellPanel sellPanel;
    private SalesHistoryPanel salesPanel;

    public MainFrame() {
        setTitle("Camera Shop Inventory System");
        setSize(1200, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG);
        setContentPane(root);

        root.add(buildSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(Theme.BG);

        productPanel    = new ProductPanel(this);
        sellPanel       = new SellPanel(this);
        salesPanel      = new SalesHistoryPanel();

        contentArea.add(buildDashboard(), "dashboard");
        contentArea.add(productPanel,     "products");
        contentArea.add(sellPanel,        "sell");
        contentArea.add(salesPanel,       "sales");

        root.add(contentArea, BorderLayout.CENTER);
        showPage("dashboard");
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(Theme.PANEL);
        side.setPreferredSize(new Dimension(220, 0));
        side.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER));

        // Logo
        JPanel logo = new JPanel(new BorderLayout());
        logo.setBackground(Theme.PANEL);
        logo.setBorder(BorderFactory.createEmptyBorder(24, 20, 20, 20));
        JLabel logolbl = Theme.label("\uD83D\uDCF7  Camera Shop", 15, true);
        logolbl.setForeground(Theme.ACCENT);
        logo.add(logolbl);
        logo.setMaximumSize(new Dimension(220, 70));
        side.add(logo);

        side.add(sideBtn("  \uD83C\uDFE0  Dashboard",  "dashboard"));
        side.add(sideBtn("  \uD83D\uDCE6  Products",   "products"));
        side.add(sideBtn("  \uD83D\uDED2  Sell / Cart","sell"));
        side.add(sideBtn("  \uD83D\uDCC3  Sales History","sales"));

        side.add(Box.createVerticalGlue());

        // User info + logout
        JPanel userBox = new JPanel(new BorderLayout(8, 0));
        userBox.setBackground(Theme.CARD);
        userBox.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        userBox.setMaximumSize(new Dimension(220, 70));

        userLabel = Theme.label(DataStore.loggedInUser.getUsername(), 13, true);
        JLabel roleTag = new JLabel(DataStore.loggedInUser.getRole().toUpperCase());
        roleTag.setForeground(DataStore.loggedInUser.isAdmin() ? Theme.WARNING : Theme.SUCCESS);
        roleTag.setFont(new Font("Segoe UI", Font.BOLD, 10));

        JPanel nameBox = new JPanel(new GridLayout(2,1));
        nameBox.setBackground(Theme.CARD);
        nameBox.add(userLabel);
        nameBox.add(roleTag);
        userBox.add(nameBox, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("Exit");
        logoutBtn.setBackground(Theme.DANGER);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        logoutBtn.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(this, "Logout and exit?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) System.exit(0);
        });
        userBox.add(logoutBtn, BorderLayout.EAST);
        side.add(userBox);

        return side;
    }

    private JButton sideBtn(String text, String page) {
        JButton b = new JButton(text);
        b.setBackground(Theme.PANEL);
        b.setForeground(Theme.TEXT);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 12));
        b.setMaximumSize(new Dimension(220, 48));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(Theme.CARD); b.setForeground(Theme.ACCENT); }
            public void mouseExited(MouseEvent e)  { b.setBackground(Theme.PANEL); b.setForeground(Theme.TEXT); }
        });
        b.addActionListener(e -> showPage(page));
        return b;
    }

    private JPanel buildDashboard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = Theme.label("Dashboard", 22, true);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));
        p.add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
        cards.setBackground(Theme.BG);
        cards.add(statCard("Total Products", String.valueOf(DataStore.products.size()), Theme.ACCENT));
        cards.add(statCard("Total Sales", String.valueOf(DataStore.sales.size()), Theme.SUCCESS));
        cards.add(statCard("Cart Items", String.valueOf(DataStore.cart.size()), Theme.WARNING));
        cards.add(statCard("Logged In As", DataStore.loggedInUser.getRole().toUpperCase(), Theme.ACCENT2));
        p.add(cards, BorderLayout.CENTER);

        return p;
    }

    private JPanel statCard(String label, String value, Color color) {
        JPanel c = Theme.card(new GridBagLayout());
        c.setPreferredSize(new Dimension(200, 120));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;

        JLabel lbl = Theme.mutedLabel(label);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        g.gridy = 0; c.add(lbl, g);

        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setForeground(color);
        val.setFont(new Font("Segoe UI", Font.BOLD, 30));
        g.gridy = 1; c.add(val, g);

        return c;
    }

    public void showPage(String name) {
        if (name.equals("sell")) sellPanel.refresh();
        if (name.equals("sales")) salesPanel.refresh();
        cardLayout.show(contentArea, name);
    }

    public void refreshSell() { sellPanel.refresh(); }
}

// ═══════════════════════════════════════════════════════════════
//  PRODUCT PANEL  (Add / View / Search / Update / Delete)
// ═══════════════════════════════════════════════════════════════

class ProductPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private MainFrame parent;

    public ProductPanel(MainFrame parent) {
        this.parent = parent;
        setBackground(Theme.BG);
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        buildUI();
    }

    private void buildUI() {
        // ── TOP BAR ──
        JPanel top = new JPanel(new BorderLayout(12, 0));
        top.setBackground(Theme.BG);
        top.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JLabel title = Theme.label("Product Inventory", 20, true);
        top.add(title, BorderLayout.WEST);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightTop.setBackground(Theme.BG);

        searchField = Theme.styledField();
        searchField.setPreferredSize(new Dimension(200, 36));
        searchField.putClientProperty("JTextField.placeholderText", "Search...");

        JComboBox<String> searchBy = new JComboBox<>(new String[]{"Name", "Brand", "Category"});
        searchBy.setBackground(Theme.CARD);
        searchBy.setForeground(Theme.TEXT);
        searchBy.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton searchBtn = Theme.primaryBtn("Search");
        JButton clearBtn  = Theme.primaryBtn("Show All");
        clearBtn.setBackground(new Color(55, 55, 80));

        rightTop.add(Theme.mutedLabel("Search by:"));
        rightTop.add(searchBy);
        rightTop.add(searchField);
        rightTop.add(searchBtn);
        rightTop.add(clearBtn);

        if (DataStore.loggedInUser.isAdmin()) {
            JButton addBtn = Theme.successBtn("+ Add Product");
            addBtn.addActionListener(e -> showAddDialog());
            rightTop.add(addBtn);
        }

        top.add(rightTop, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        // ── TABLE ──
        String[] cols = {"ID", "Name", "Brand", "Category", "Price (Rs.)", "Qty", "Description"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        table.getColumnModel().getColumn(5).setMaxWidth(60);
        loadTable(DataStore.products);
        add(Theme.scrollPane(table), BorderLayout.CENTER);

        // ── ACTION BUTTONS ──
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        bottom.setBackground(Theme.BG);

        if (DataStore.loggedInUser.isAdmin()) {
            JButton editBtn   = Theme.primaryBtn("Edit Selected");
            JButton deleteBtn = Theme.dangerBtn("Delete Selected");
            editBtn.addActionListener(e -> showEditDialog());
            deleteBtn.addActionListener(e -> deleteSelected());
            bottom.add(editBtn);
            bottom.add(deleteBtn);
        }
        add(bottom, BorderLayout.SOUTH);

        // Search logic
        searchBtn.addActionListener(e -> {
            String kw = searchField.getText().trim().toLowerCase();
            String by = (String) searchBy.getSelectedItem();
            ArrayList<Product> res = new ArrayList<>();
            for (Product p : DataStore.products) {
                boolean match = false;
                if (by.equals("Name"))     match = p.getName().toLowerCase().contains(kw);
                if (by.equals("Brand"))    match = p.getBrand().toLowerCase().contains(kw);
                if (by.equals("Category")) match = p.getCategory().toLowerCase().contains(kw);
                if (match) res.add(p);
            }
            loadTable(res);
        });
        clearBtn.addActionListener(e -> { searchField.setText(""); loadTable(DataStore.products); });
    }

    void loadTable(ArrayList<Product> list) {
        tableModel.setRowCount(0);
        for (Product p : list) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getName(), p.getBrand(), p.getCategory(),
                    String.format("%.2f", p.getPrice()), p.getQuantity(), p.getDescription()
            });
        }
    }

    private Product getSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a product first."); return null; }
        int id = (int) tableModel.getValueAt(row, 0);
        return DataStore.findById(id);
    }

    // ── ADD DIALOG ──
    private void showAddDialog() {
        JDialog d = styledDialog("Add New Product", 480, 500);
        JPanel form = new JPanel(new GridLayout(7, 2, 10, 12));
        form.setBackground(Theme.CARD);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JTextField fName  = Theme.styledField(), fBrand = Theme.styledField(),
                fCat   = Theme.styledField(), fDesc  = Theme.styledField(),
                fPrice = Theme.styledField(), fQty   = Theme.styledField();

        form.add(Theme.label("Name", 13, false));       form.add(fName);
        form.add(Theme.label("Brand", 13, false));      form.add(fBrand);
        form.add(Theme.label("Category", 13, false));   form.add(fCat);
        form.add(Theme.label("Price (Rs.)", 13, false));form.add(fPrice);
        form.add(Theme.label("Quantity", 13, false));   form.add(fQty);
        form.add(Theme.label("Description", 13, false));form.add(fDesc);

        JButton save = Theme.successBtn("Save Product");
        JPanel btnP = new JPanel(); btnP.setBackground(Theme.CARD);
        btnP.add(save);

        d.add(form, BorderLayout.CENTER);
        d.add(btnP, BorderLayout.SOUTH);

        save.addActionListener(e -> {
            try {
                String name = fName.getText().trim(), brand = fBrand.getText().trim(),
                        cat  = fCat.getText().trim(),  desc  = fDesc.getText().trim();
                double price = Double.parseDouble(fPrice.getText().trim());
                int qty      = Integer.parseInt(fQty.getText().trim());
                if (name.isEmpty() || brand.isEmpty() || cat.isEmpty()) throw new Exception("Fill all fields");
                DataStore.products.add(new Product(name, brand, cat, price, qty, desc));
                loadTable(DataStore.products);
                d.dispose();
                JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        d.setVisible(true);
    }

    // ── EDIT DIALOG ──
    private void showEditDialog() {
        Product p = getSelected();
        if (p == null) return;

        JDialog d = styledDialog("Edit Product - " + p.getName(), 480, 500);
        JPanel form = new JPanel(new GridLayout(7, 2, 10, 12));
        form.setBackground(Theme.CARD);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JTextField fName  = Theme.styledField(), fBrand = Theme.styledField(),
                fCat   = Theme.styledField(), fDesc  = Theme.styledField(),
                fPrice = Theme.styledField(), fQty   = Theme.styledField();

        fName.setText(p.getName());   fBrand.setText(p.getBrand());
        fCat.setText(p.getCategory()); fDesc.setText(p.getDescription());
        fPrice.setText(String.valueOf(p.getPrice())); fQty.setText(String.valueOf(p.getQuantity()));

        form.add(Theme.label("Name", 13, false));       form.add(fName);
        form.add(Theme.label("Brand", 13, false));      form.add(fBrand);
        form.add(Theme.label("Category", 13, false));   form.add(fCat);
        form.add(Theme.label("Price (Rs.)", 13, false));form.add(fPrice);
        form.add(Theme.label("Quantity", 13, false));   form.add(fQty);
        form.add(Theme.label("Description", 13, false));form.add(fDesc);

        JButton save = Theme.primaryBtn("Update Product");
        JPanel btnP = new JPanel(); btnP.setBackground(Theme.CARD);
        btnP.add(save);

        d.add(form, BorderLayout.CENTER);
        d.add(btnP, BorderLayout.SOUTH);

        save.addActionListener(e -> {
            try {
                p.setName(fName.getText().trim());
                p.setBrand(fBrand.getText().trim());
                p.setCategory(fCat.getText().trim());
                p.setDescription(fDesc.getText().trim());
                p.setPrice(Double.parseDouble(fPrice.getText().trim()));
                p.setQuantity(Integer.parseInt(fQty.getText().trim()));
                loadTable(DataStore.products);
                d.dispose();
                JOptionPane.showMessageDialog(this, "Product updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        d.setVisible(true);
    }

    // ── DELETE ──
    private void deleteSelected() {
        Product p = getSelected();
        if (p == null) return;
        int r = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to DELETE:\n" + p.getName() + " (" + p.getBrand() + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r == JOptionPane.YES_OPTION) {
            DataStore.products.remove(p);
            loadTable(DataStore.products);
            JOptionPane.showMessageDialog(this, "Product deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JDialog styledDialog(String title, int w, int h) {
        JDialog d = new JDialog(parent, title, true);
        d.setSize(w, h);
        d.setLocationRelativeTo(parent);
        d.setLayout(new BorderLayout());
        d.getContentPane().setBackground(Theme.CARD);
        return d;
    }
}

// ═══════════════════════════════════════════════════════════════
//  SELL PANEL  (Browse → Add to Cart → Checkout / Cancel)
// ═══════════════════════════════════════════════════════════════

class SellPanel extends JPanel {
    private DefaultTableModel productModel, cartModel;
    private JTable productTable, cartTable;
    private JLabel totalLabel;
    private MainFrame parent;

    public SellPanel(MainFrame parent) {
        this.parent = parent;
        setBackground(Theme.BG);
        setLayout(new BorderLayout(16, 0));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        buildUI();
    }

    private void buildUI() {
        // ── LEFT: product browser ──
        JPanel left = new JPanel(new BorderLayout(0, 10));
        left.setBackground(Theme.BG);

        JLabel lt = Theme.label("Available Products", 16, true);
        lt.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        left.add(lt, BorderLayout.NORTH);

        String[] pCols = {"ID", "Name", "Price (Rs.)", "Stock"};
        productModel = new DefaultTableModel(pCols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        productTable = new JTable(productModel);
        Theme.styleTable(productTable);
        productTable.getColumnModel().getColumn(0).setMaxWidth(50);
        left.add(Theme.scrollPane(productTable), BorderLayout.CENTER);

        JPanel addBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        addBar.setBackground(Theme.BG);
        JTextField qtyField = Theme.styledField();
        qtyField.setPreferredSize(new Dimension(60, 34));
        qtyField.setText("1");
        JButton addBtn = Theme.successBtn("Add to Cart");
        addBar.add(Theme.label("Qty:", 13, false));
        addBar.add(qtyField);
        addBar.add(addBtn);
        left.add(addBar, BorderLayout.SOUTH);

        // ── RIGHT: cart ──
        JPanel right = new JPanel(new BorderLayout(0, 10));
        right.setBackground(Theme.BG);
        right.setPreferredSize(new Dimension(420, 0));

        JLabel ct = Theme.label("Cart", 16, true);
        ct.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        right.add(ct, BorderLayout.NORTH);

        String[] cCols = {"Product", "Qty", "Price", "Subtotal"};
        cartModel = new DefaultTableModel(cCols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        cartTable = new JTable(cartModel);
        Theme.styleTable(cartTable);
        right.add(Theme.scrollPane(cartTable), BorderLayout.CENTER);

        JPanel bottomRight = new JPanel(new BorderLayout(0, 8));
        bottomRight.setBackground(Theme.BG);

        totalLabel = Theme.label("Total:  Rs. 0.00", 15, true);
        totalLabel.setForeground(Theme.SUCCESS);
        totalLabel.setBorder(BorderFactory.createEmptyBorder(8, 4, 4, 4));
        bottomRight.add(totalLabel, BorderLayout.NORTH);

        JPanel btns = new JPanel(new GridLayout(1, 2, 10, 0));
        btns.setBackground(Theme.BG);
        JButton checkoutBtn = Theme.successBtn("Checkout");
        JButton cancelBtn   = Theme.dangerBtn("Cancel Order");
        btns.add(checkoutBtn);
        btns.add(cancelBtn);
        bottomRight.add(btns, BorderLayout.CENTER);
        right.add(bottomRight, BorderLayout.SOUTH);

        add(left, BorderLayout.CENTER);
        add(right, BorderLayout.EAST);

        // ── LOGIC ──
        addBtn.addActionListener(e -> {
            int row = productTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a product first."); return; }
            int pid = (int) productModel.getValueAt(row, 0);
            Product p = DataStore.findById(pid);
            if (p == null) return;
            int qty;
            try { qty = Integer.parseInt(qtyField.getText().trim()); }
            catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Enter a valid quantity."); return; }
            if (qty <= 0) { JOptionPane.showMessageDialog(this, "Quantity must be > 0."); return; }
            if (qty > p.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Only " + p.getQuantity() + " in stock."); return;
            }
            DataStore.addToCart(p, qty);
            refreshCart();
            qtyField.setText("1");
        });

        checkoutBtn.addActionListener(e -> doCheckout());
        cancelBtn.addActionListener(e -> doCancelOrder());
    }

    void refresh() {
        productModel.setRowCount(0);
        for (Product p : DataStore.products) {
            productModel.addRow(new Object[]{
                    p.getId(), p.getName(),
                    String.format("%.2f", p.getPrice()),
                    p.getQuantity() > 0 ? p.getQuantity() : "OUT"
            });
        }
        refreshCart();
    }

    private void refreshCart() {
        cartModel.setRowCount(0);
        for (CartItem item : DataStore.cart) {
            cartModel.addRow(new Object[]{
                    item.getProduct().getName(), item.getQuantity(),
                    String.format("Rs. %.2f", item.getProduct().getPrice()),
                    String.format("Rs. %.2f", item.getSubtotal())
            });
        }
        totalLabel.setText("Total:  Rs. " + String.format("%.2f", DataStore.cartTotal()));
    }

    private void doCheckout() {
        if (DataStore.cart.isEmpty()) { JOptionPane.showMessageDialog(this, "Cart is empty."); return; }
        int r = JOptionPane.showConfirmDialog(this,
                "Confirm checkout?\nTotal: Rs. " + String.format("%.2f", DataStore.cartTotal()),
                "Checkout", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;

        // Deduct stock
        for (CartItem item : DataStore.cart) item.getProduct().reduceQty(item.getQuantity());

        SoldRecord rec = new SoldRecord(DataStore.cart, DataStore.cartTotal());
        DataStore.sales.add(rec);

        showReceipt(rec);
        DataStore.cart.clear();
        refresh();
    }

    private void doCancelOrder() {
        if (DataStore.cart.isEmpty()) { JOptionPane.showMessageDialog(this, "Cart is already empty."); return; }
        int r = JOptionPane.showConfirmDialog(this,
                "Cancel this order?\nCart will be cleared. Inventory will NOT be affected.",
                "Cancel Order", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r == JOptionPane.YES_OPTION) {
            DataStore.cart.clear();
            refreshCart();
            JOptionPane.showMessageDialog(this, "Order cancelled. Inventory unchanged.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showReceipt(SoldRecord rec) {
        JDialog d = new JDialog(parent, "Receipt #" + rec.getReceiptNo(), true);
        d.setSize(500, 500);
        d.setLocationRelativeTo(parent);
        d.getContentPane().setBackground(Theme.CARD);
        d.setLayout(new BorderLayout());

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Theme.CARD);
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 10, 24));

        body.add(centeredLabel("CAMERA SHOP", 18, Theme.ACCENT));
        body.add(centeredLabel("Sales Receipt", 14, Theme.MUTED));
        body.add(Box.createVerticalStrut(10));
        body.add(centeredLabel("Receipt No: " + rec.getReceiptNo(), 13, Theme.TEXT));
        body.add(centeredLabel("Date: " + rec.getDateTime(), 12, Theme.MUTED));
        body.add(centeredLabel("Status: " + rec.getStatus(), 12, Theme.SUCCESS));
        body.add(Box.createVerticalStrut(14));

        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        body.add(sep);
        body.add(Box.createVerticalStrut(10));

        String[] cols = {"Product", "Qty", "Price", "Subtotal"};
        DefaultTableModel m = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r2, int c) { return false; } };
        for (CartItem item : rec.getItems()) {
            m.addRow(new Object[]{
                    item.getProduct().getName(), item.getQuantity(),
                    String.format("Rs.%.2f", item.getProduct().getPrice()),
                    String.format("Rs.%.2f", item.getSubtotal())
            });
        }
        JTable t = new JTable(m);
        Theme.styleTable(t);
        JScrollPane sp = Theme.scrollPane(t);
        sp.setPreferredSize(new Dimension(440, 160));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        body.add(sp);
        body.add(Box.createVerticalStrut(12));

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(Theme.BORDER);
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        body.add(sep2);
        body.add(Box.createVerticalStrut(8));
        body.add(centeredLabel("TOTAL:  Rs. " + String.format("%.2f", rec.getTotal()), 16, Theme.SUCCESS));
        body.add(Box.createVerticalStrut(8));
        body.add(centeredLabel("Thank you for shopping!", 13, Theme.MUTED));

        JButton close = Theme.primaryBtn("Close");
        close.addActionListener(e -> d.dispose());
        JPanel bp = new JPanel(); bp.setBackground(Theme.CARD); bp.add(close);

        d.add(new JScrollPane(body), BorderLayout.CENTER);
        d.add(bp, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private JLabel centeredLabel(String text, int size, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(color);
        l.setFont(new Font("Segoe UI", Font.PLAIN, size));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        return l;
    }
}

// ═══════════════════════════════════════════════════════════════
//  SALES HISTORY PANEL
// ═══════════════════════════════════════════════════════════════

class SalesHistoryPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;

    public SalesHistoryPanel() {
        setBackground(Theme.BG);
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        buildUI();
    }

    private void buildUI() {
        JLabel title = Theme.label("Sales History", 20, true);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        add(title, BorderLayout.NORTH);

        String[] cols = {"Receipt #", "Date / Time", "Total (Rs.)", "Items Sold", "Status"};
        model = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        table = new JTable(model);
        Theme.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(90);
        table.getColumnModel().getColumn(4).setMaxWidth(100);
        add(Theme.scrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        bottom.setBackground(Theme.BG);
        JButton viewBtn = Theme.primaryBtn("View Receipt Details");
        viewBtn.addActionListener(e -> viewDetail());
        bottom.add(viewBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    void refresh() {
        model.setRowCount(0);
        for (SoldRecord r : DataStore.sales) {
            int itemCount = 0;
            for (CartItem i : r.getItems()) itemCount += i.getQuantity();
            model.addRow(new Object[]{
                    r.getReceiptNo(), r.getDateTime(),
                    String.format("%.2f", r.getTotal()),
                    itemCount, r.getStatus()
            });
        }
    }

    private void viewDetail() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a record first."); return; }
        int rno = (int) model.getValueAt(row, 0);
        SoldRecord found = null;
        for (SoldRecord r : DataStore.sales) if (r.getReceiptNo() == rno) { found = r; break; }
        if (found == null) return;

        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Receipt #" + rno, true);
        d.setSize(480, 420);
        d.setLocationRelativeTo(this);
        d.getContentPane().setBackground(Theme.CARD);
        d.setLayout(new BorderLayout());

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Theme.CARD);
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 10, 24));

        JLabel rl = new JLabel("Receipt #" + rno + "  —  " + found.getDateTime(), SwingConstants.CENTER);
        rl.setForeground(Theme.ACCENT); rl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rl.setAlignmentX(Component.CENTER_ALIGNMENT); rl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        body.add(rl);
        body.add(Box.createVerticalStrut(10));

        String[] cols2 = {"Product", "Qty", "Unit Price", "Subtotal"};
        DefaultTableModel m2 = new DefaultTableModel(cols2, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        for (CartItem item : found.getItems()) {
            m2.addRow(new Object[]{
                    item.getProduct().getName(), item.getQuantity(),
                    String.format("Rs.%.2f", item.getProduct().getPrice()),
                    String.format("Rs.%.2f", item.getSubtotal())
            });
        }
        JTable t2 = new JTable(m2);
        Theme.styleTable(t2);
        JScrollPane sp = Theme.scrollPane(t2);
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        body.add(sp);
        body.add(Box.createVerticalStrut(10));

        JLabel tot = new JLabel("TOTAL:  Rs. " + String.format("%.2f", found.getTotal()), SwingConstants.CENTER);
        tot.setForeground(Theme.SUCCESS); tot.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tot.setAlignmentX(Component.CENTER_ALIGNMENT); tot.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        body.add(tot);

        JButton close = Theme.primaryBtn("Close");
        close.addActionListener(e -> d.dispose());
        JPanel bp = new JPanel(); bp.setBackground(Theme.CARD); bp.add(close);

        d.add(body, BorderLayout.CENTER);
        d.add(bp, BorderLayout.SOUTH);
        d.setVisible(true);
    }
}

// ═══════════════════════════════════════════════════════════════
//  ENTRY POINT
// ═══════════════════════════════════════════════════════════════

public class CameraShop {
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}