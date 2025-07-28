import java.util.*;

class Product {
    String productId;
    String name;
    double price;
    int stock;

    public Product(String productId, String name, double price, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}

class CartItem {
    Product product;
    int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return product.price * quantity;
    }
}

class ProductCatalog {
    private Map<String, Product> products = new HashMap<>();

    public void addProduct(Product p) {
        products.put(p.productId, p);
    }

    public Product getProduct(String id) {
        return products.get(id);
    }

    public void showCatalog() {
        System.out.println("\nAvailable Products:");
        System.out.printf("%-10s %-20s %-10s %-10s\n", "ID", "Name", "Price", "Stock");
        for (Product p : products.values()) {
            System.out.printf("%-10s %-20s %-10.2f %-10d\n", p.productId, p.name, p.price, p.stock);
        }
    }
}

class Cart {
    List<CartItem> items = new ArrayList<>();

    public void addItem(Product product, int quantity) {
        if (quantity > product.stock) {
            System.out.println("Not enough stock!");
            return;
        }

        for (CartItem item : items) {
            if (item.product.productId.equals(product.productId)) {
                item.quantity += quantity;
                product.stock -= quantity;
                return;
            }
        }

        items.add(new CartItem(product, quantity));
        product.stock -= quantity;
    }

    public void showCart() {
        System.out.println("\nYour Cart:");
        System.out.printf("%-20s %-10s %-10s %-10s\n", "Product", "Price", "Qty", "Subtotal");
        for (CartItem item : items) {
            System.out.printf("%-20s %-10.2f %-10d %-10.2f\n",
                    item.product.name, item.product.price, item.quantity, item.getSubtotal());
        }
    }

    public double calculateSubtotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}

class BillingSystem {
    private static final double TAX_RATE = 0.08;       // 8% tax
    private static final double DISCOUNT_THRESHOLD = 100.00;
    private static final double DISCOUNT_RATE = 0.10;  // 10% discount

    private ProductCatalog catalog = new ProductCatalog();
    private Cart cart = new Cart();
    private Scanner scanner = new Scanner(System.in);

    public void initProducts() {
        catalog.addProduct(new Product("P001", "Apple", 1.50, 100));
        catalog.addProduct(new Product("P002", "Milk", 2.00, 50));
        catalog.addProduct(new Product("P003", "Bread", 2.50, 30));
        catalog.addProduct(new Product("P004", "Eggs", 3.00, 60));
        catalog.addProduct(new Product("P005", "Rice", 10.00, 40));
    }

    public void start() {
        initProducts();
        System.out.println("Welcome to SuperMart Billing System");

        boolean exit = false;
        while (!exit) {
            catalog.showCatalog();
            System.out.println("\n1. Add to Cart\n2. View Cart\n3. Checkout\n4. Exit");
            System.out.print("Select option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addToCart();
                case 2 -> cart.showCart();
                case 3 -> {
                    if (!cart.isEmpty()) checkout();
                    else System.out.println("Cart is empty!");
                }
                case 4 -> {
                    System.out.println("Thank you for shopping!");
                    exit = true;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void addToCart() {
        System.out.print("Enter Product ID: ");
        String id = scanner.nextLine();
        Product product = catalog.getProduct(id);
        if (product == null) {
            System.out.println("Invalid product ID.");
            return;
        }

        System.out.print("Enter Quantity: ");
        int qty = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (qty <= 0) {
            System.out.println("Invalid quantity.");
            return;
        }

        cart.addItem(product, qty);
        System.out.println("Added to cart.");
    }

    private void checkout() {
        cart.showCart();
        double subtotal = cart.calculateSubtotal();
        double discount = (subtotal >= DISCOUNT_THRESHOLD) ? subtotal * DISCOUNT_RATE : 0;
        double taxedAmount = (subtotal - discount) * TAX_RATE;
        double total = subtotal - discount + taxedAmount;

        System.out.println("\n----- Receipt -----");
        System.out.printf("Subtotal      : $%.2f\n", subtotal);
        System.out.printf("Discount      : $%.2f\n", discount);
        System.out.printf("Tax (8%%)      : $%.2f\n", taxedAmount);
        System.out.printf("Total Amount  : $%.2f\n", total);
        System.out.println("-------------------");

        System.out.println("Payment successful. Thank you!");
        cart.clear();
    }
}

public class BillingSystemApp {
    public static void main(String[] args) {
        BillingSystem system = new BillingSystem();
        system.start();
    }
}

