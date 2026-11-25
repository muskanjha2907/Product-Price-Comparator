
import java.io.*;
import java.util.Scanner;

public class ProductPriceComparator {

    static class Product {
        String id;
        String productName;
        String storeName;
        double price;

        Product(String id, String productName,
                String storeName, double price) {
            this.id = id;
            this.productName = productName;
            this.storeName = storeName;
            this.price = price;
        }

        public String toString() {
            return id + " | " + productName + " | "
                    + storeName + " | " + price;
        }
    }

    static Product[] products = new Product[100];
    static int count = 0;
    static int nextId = 101;

    static final String FILE_NAME = "products.txt";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        loadFromFile();

        System.out.println("Data loaded: " + count + " listings");
        System.out.println("File location: "
                + new File(FILE_NAME).getAbsolutePath());

        int choice = 0;

        do {
            System.out.println("\n===== Product Price Comparator =====");
            System.out.println("1. Add Listing");
            System.out.println("2. Display All");
            System.out.println("3. Sort by Price (Low to High)");
            System.out.println("4. Search Product (Cheapest)");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Enter a number.");
                continue;
            }

            switch (choice) {

                case 1:
                    addListing(sc);
                    break;

                case 2:
                    displayAll();
                    break;

                case 3:
                    sortByPrice();
                    break;

                case 4:
                    searchCheapest(sc);
                    break;

                case 5:
                    saveToFile();
                    System.out.println("Data saved successfully!");
                    System.out.println("Exiting program...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 5);

        sc.close();
    }

    // 1. Add Product Listing
    static void addListing(Scanner sc) {

        if (count >= products.length) {
            System.out.println("Storage is full!");
            return;
        }

        System.out.print("Product: ");
        String productName = sc.nextLine().trim();

        System.out.print("Store: ");
        String storeName = sc.nextLine().trim();

        System.out.print("Price: ");

        double price;

        try {
            price = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price!");
            return;
        }

        if (price <= 0) {
            System.out.println(
                "Invalid price. Price must be greater than 0.");
            return;
        }

        if (productName.isEmpty() || storeName.isEmpty()) {
            System.out.println("Product and Store cannot be empty.");
            return;
        }

        String id = "P" + nextId++;

        products[count] = new Product(
                id, productName, storeName, price);

        count++;

        System.out.println("Product added. ID: " + products[count - 1]);

        // Save immediately after adding
        saveToFile();
    }

    // 2. Display All Listings
    static void displayAll() {

        if (count == 0) {
            System.out.println("No listings available.");
            return;
        }

        System.out.println("\nAll Listings:");

        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". " + products[i]);
        }
    }

    // 3. Sort Products using Bubble Sort
    static void sortByPrice() {

        if (count == 0) {
            System.out.println("No listings available.");
            return;
        }

        for (int i = 0; i < count - 1; i++) {

            for (int j = 0; j < count - i - 1; j++) {

                if (products[j].price > products[j + 1].price) {

                    Product temp = products[j];

                    products[j] = products[j + 1];

                    products[j + 1] = temp;
                }
            }
        }

        System.out.println("\nSorted Listings:");

        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". " + products[i]);
        }
    }

    // 4. Search Cheapest Product
    static void searchCheapest(Scanner sc) {

        if (count == 0) {
            System.out.println("No listings available.");
            return;
        }

        System.out.print("Enter Product Name: ");
        String search = sc.nextLine().trim();

        Product cheapest = null;

        for (int i = 0; i < count; i++) {

            if (products[i].productName.equalsIgnoreCase(search)) {

                if (cheapest == null ||
                        products[i].price < cheapest.price) {

                    cheapest = products[i];
                }
            }
        }

        if (cheapest != null) {
            System.out.println("Cheapest: " + cheapest);
        } else {
            System.out.println("Product not found.");
        }
    }

    // 5. Save Data into File
    static void saveToFile() {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(FILE_NAME))) {

            for (int i = 0; i < count; i++) {

                bw.write(products[i].id + ","
                        + products[i].productName + ","
                        + products[i].storeName + ","
                        + products[i].price);

                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    // 6. Load Data from File
    static void loadFromFile() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(
                new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null
                    && count < products.length) {

                try {
                    String[] data = line.split(",", -1);

                    if (data.length != 4) {
                        continue;
                    }

                    String id = data[0];
                    String productName = data[1];
                    String storeName = data[2];
                    double price = Double.parseDouble(data[3]);

                    if (price <= 0) {
                        continue;
                    }

                    products[count] = new Product(
                            id, productName, storeName, price);

                    count++;

                    int numericId =
                            Integer.parseInt(id.substring(1));

                    if (numericId >= nextId) {
                        nextId = numericId + 1;
                    }

                } catch (Exception e) {
                    System.out.println("Skipping invalid file record.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
}
