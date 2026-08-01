import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.*;

public class InventoryManager
{
    private ArrayList<Item> itemList;
    private ArrayList<StockTransaction> transactionList;

    public InventoryManager()
    {
        itemList = new ArrayList<Item>();
        transactionList = new ArrayList<StockTransaction>();
        loadInventory();
        loadTransactions();
    }

    // Auto-generates the next ID for a given prefix ("I" or "M"),
    // e.g. I001, I002 ... or M001, M002 ... based on what already exists.
    private String generateID(String prefix, boolean forMedicine)
    {
        int maxNo = 0;

        for(Item item : itemList)
        {
            if((item instanceof Medicine) == forMedicine)
            {
                int no = Integer.parseInt(item.getItemID().substring(1));

                if(no > maxNo)
                {
                    maxNo = no;
                }
            }
        }

        return prefix + String.format("%03d", maxNo + 1);
    }

    public String generateItemID()
    {
        return generateID("I", false);
    }

    public String generateMedicineID()
    {
        return generateID("M", true);
    }

    public void addItem(Item item)
    {
        itemList.add(item);

        logTransaction("ADD", item.getItemName(), item.getQuantity());

        saveInventory();

        System.out.println("✅ Item added successfully. (ID: " + item.getItemID() + ")");
    }

    public void viewInventory()
    {
        if(itemList.isEmpty())
        {
            System.out.println("Inventory is empty.");
        }
        else
        {
            System.out.println("╔════════╦══════════════════════╦════════════╦══════════╦════════════╗");
            System.out.println("║ ID     ║ Item Name            ║ Category   ║ Quantity ║ Date Added ║");
            System.out.println("╠════════╬══════════════════════╬════════════╬══════════╬════════════╣");

            for(Item item : itemList)
            {
                String category = "General";

                if(item instanceof Medicine)
                {
                    category = "Medicine";
                }

                System.out.printf(
                "║ %-6s ║ %-20s ║ %-10s ║ %-8d ║ %-10s ║\n",
                item.getItemID(),
                item.getItemName(),
                category,
                item.getQuantity(),
                item.getDateAdded());
            }

            System.out.println("╚════════╩══════════════════════╩════════════╩══════════╩════════════╝");
        }
    }

    public void showItemList()
    {
        if(itemList.isEmpty())
        {
            System.out.println("No items available.");
        }
        else
        {
            System.out.println("╔════════╦══════════════════════╗");
            System.out.println("║ ID     ║ Item Name            ║");
            System.out.println("╠════════╬══════════════════════╣");

            for(Item item : itemList)
            {
                System.out.printf(
                "║ %-6s ║ %-20s ║\n",
                item.getItemID(),
                item.getItemName());
            }

            System.out.println("╚════════╩══════════════════════╝");
        }
    }

    // Same as showItemList(), but only lists Medicine items
    // and includes the current Quantity. Used by the Doctor's
    // Take Medicine feature.
    public void showMedicineList()
    {
        boolean hasMedicine = false;

        System.out.println("╔════════╦══════════════════════╦══════════╗");
        System.out.println("║ ID     ║ Item Name            ║ Quantity ║");
        System.out.println("╠════════╬══════════════════════╬══════════╣");

        for(Item item : itemList)
        {
            if(item instanceof Medicine)
            {
                System.out.printf(
                "║ %-6s ║ %-20s ║ %-8d ║\n",
                item.getItemID(),
                item.getItemName(),
                item.getQuantity());

                hasMedicine = true;
            }
        }

        System.out.println("╚════════╩══════════════════════╩══════════╝");

        if(!hasMedicine)
        {
            System.out.println("No medicine available.");
        }
    }

    public void updateStock(String itemID, int newQuantity)
    {
        boolean found = false;

        for(Item item : itemList)
        {
            if(item.getItemID().equals(itemID))
            {
                item.setQuantity(newQuantity);

                logTransaction("UPDATE", item.getItemName(), newQuantity);

                saveInventory();

                System.out.println("\n✅ Stock updated.");
                found = true;
                break;
            }
        }

        if(!found)
        {
            System.out.println("\n❌ Item not found.");
        }
    }

    public Item findItemByID(String itemID)
    {
        for(Item item : itemList)
        {
            if(item.getItemID().equals(itemID))
            {
                return item;
            }
        }

        return null;
    }

    public void displayItemInfo(Item item)
    {
        String category = "General";

        if(item instanceof Medicine)
        {
            category = "Medicine";
        }

        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║               ITEM INFORMATION             ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.printf ("║ Item ID   : %-32s ║\n", item.getItemID());
        System.out.printf ("║ Item Name : %-32s ║\n", item.getItemName());
        System.out.printf ("║ Category  : %-32s ║\n", category);
        System.out.printf ("║ Quantity  : %-32d ║\n", item.getQuantity());
        System.out.printf ("║ Date Added: %-32s ║\n", item.getDateAdded());

        if(item instanceof Medicine)
        {
            Medicine medicine = (Medicine) item;
            System.out.printf ("║ Expiry    : %-32s ║\n", medicine.getExpiryDate());
        }

        System.out.println("╚════════════════════════════════════════════╝");
    }

    public void deleteItem(String itemID)
    {
        boolean found = false;

        for(int i = 0; i < itemList.size(); i++)
        {
            if(itemList.get(i).getItemID().equals(itemID))
            {
                logTransaction("DELETE", itemList.get(i).getItemName(), itemList.get(i).getQuantity());

                itemList.remove(i);

                saveInventory();

                System.out.println("\n🗑 Item deleted.");
                found = true;
                break;
            }
        }

        if(!found)
        {
            System.out.println("\n❌ Item not found.");
        }
    }

    // Doctor feature: dispense/take medicine, reducing its quantity.
    public void takeMedicine(String itemID, int quantityTaken)
    {
        Item item = findItemByID(itemID);

        if(item == null)
        {
            System.out.println("\n❌ Item not found.");
            return;
        }

        if(quantityTaken <= 0)
        {
            System.out.println("\n❌ Quantity taken must be more than 0.");
            return;
        }

        if(quantityTaken > item.getQuantity())
        {
            System.out.println("\n❌ Not enough stock. Available: " + item.getQuantity());
            return;
        }

        item.setQuantity(item.getQuantity() - quantityTaken);

        logTransaction("TAKE", item.getItemName(), quantityTaken);

        saveInventory();

        System.out.println("\n✅ Medicine taken. Remaining stock: " + item.getQuantity());
    }

    public void generateReport()
    {
        final int LOW_STOCK_THRESHOLD = 10;

        int lowStockCount = 0;
        ArrayList<Item> lowStockItems = new ArrayList<Item>();
        ArrayList<Medicine> expiredMedicines = new ArrayList<Medicine>();

        for(Item item : itemList)
        {
            if(item.getQuantity() <= LOW_STOCK_THRESHOLD)
            {
                lowStockCount++;
                lowStockItems.add(item);
            }

            if(item instanceof Medicine)
            {
                Medicine medicine = (Medicine) item;

                if(medicine.isExpired())
                {
                    expiredMedicines.add(medicine);
                }
            }
        }

        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║               INVENTORY REPORT             ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.printf ("║ Total Items : %-23d      ║\n", itemList.size());
        System.out.printf ("║ Low Stock   : %-23d      ║\n", lowStockCount);
        System.out.printf ("║ Expired Med : %-23d      ║\n", expiredMedicines.size());
        System.out.println("╚════════════════════════════════════════════╝");

        if(lowStockItems.isEmpty())
        {
            System.out.println("\nNo low stock items.");
        }
        else
        {
            System.out.println();
            System.out.println("LOW STOCK WARNING (<= " + LOW_STOCK_THRESHOLD + ")");
            System.out.println("──────────────────────────────────────");

            for(Item item : lowStockItems)
            {
                System.out.println(
                item.getItemID() + " - " +
                item.getItemName() + " (Stock: " +
                item.getQuantity() + ")");
            }
        }

        if(expiredMedicines.isEmpty())
        {
            System.out.println("\nNo expired medicine.");
        }
        else
        {
            System.out.println();
            System.out.println("⚠ EXPIRED MEDICINE WARNING");
            System.out.println("──────────────────────────────────────");

            for(Medicine medicine : expiredMedicines)
            {
                System.out.println(
                medicine.getItemID() + " - " +
                medicine.getItemName() + " (Expired: " +
                medicine.getExpiryDate() + ")");
            }
        }
    }

    public void viewTransactionHistory()
    {
        if(transactionList.isEmpty())
        {
            System.out.println("No transaction history.");
        }
        else
        {
            System.out.println("╔══════╦══════════╦══════════════════════╦══════════╦════════════╗");
            System.out.println("║ No   ║ Type     ║ Item Name            ║ Quantity ║ Date       ║");
            System.out.println("╠══════╬══════════╬══════════════════════╬══════════╬════════════╣");

            int no = 1;

            for(StockTransaction transaction : transactionList)
            {
                System.out.printf(
                "║ %-4d ║ %-8s ║ %-20s ║ %-8d ║ %-10s ║\n",
                no,
                transaction.getTransactionType(),
                transaction.getItemName(),
                transaction.getQuantity(),
                transaction.getDate());

                no++;
            }

            System.out.println("╚══════╩══════════╩══════════════════════╩══════════╩════════════╝");
        }
    }

    // Adds a transaction record and immediately persists it to file.
    private void logTransaction(String type, String itemName, int quantity)
    {
        transactionList.add(new StockTransaction(type, itemName, quantity));
        saveTransactions();
    }

    // ---------- File Input / Output ----------
    // Follows the standard BufferedReader / FileReader (read)
    // and PrintWriter / BufferedWriter / FileWriter (write) style,
    // with StringTokenizer used to split each comma-separated line.

    public void saveInventory()
    {
        try
        {
            PrintWriter pw =
            new PrintWriter(
            new BufferedWriter(
            new FileWriter("inventory.txt")));

            for(Item item : itemList)
            {
                pw.println(item.toFileString());
            }

            pw.close();
        }
        catch(Exception e)
        {
            System.out.println("Error saving inventory.");
        }
    }

    public void loadInventory()
    {
        File file = new File("inventory.txt");

        if(!file.exists())
        {
            return;
        }

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            while(line != null)
            {
                StringTokenizer st = new StringTokenizer(line, ",");
                int tokenCount = st.countTokens();

                if(tokenCount == 5)
                {
                    // id, name, category, quantity, dateAdded
                    String id = st.nextToken();
                    String name = st.nextToken();
                    String category = st.nextToken();
                    int quantity = Integer.parseInt(st.nextToken());
                    String dateAdded = st.nextToken();

                    Item item = new Item(id, name, quantity, dateAdded);
                    itemList.add(item);
                }
                else if(tokenCount == 6)
                {
                    // id, name, category, quantity, dateAdded, expiryDate
                    String id = st.nextToken();
                    String name = st.nextToken();
                    String category = st.nextToken();
                    int quantity = Integer.parseInt(st.nextToken());
                    String dateAdded = st.nextToken();
                    String expiryDate = st.nextToken();

                    Medicine medicine = new Medicine(id, name, quantity, dateAdded, expiryDate);
                    itemList.add(medicine);
                }

                line = br.readLine();
            }

            br.close();
        }
        catch(Exception e)
        {
            System.out.println("Error loading inventory.");
        }
    }

    // Rewrites the transaction history file to match transactionList.
    public void saveTransactions()
    {
        try
        {
            PrintWriter pw =
            new PrintWriter(
            new BufferedWriter(
            new FileWriter("transactions.txt")));

            for(StockTransaction transaction : transactionList)
            {
                pw.println(transaction.toFileString());
            }

            pw.close();
        }
        catch(Exception e)
        {
            System.out.println("Error saving transaction history.");
        }
    }

    public void loadTransactions()
    {
        File file = new File("transactions.txt");

        if(!file.exists())
        {
            return;
        }

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            while(line != null)
            {
                StringTokenizer st = new StringTokenizer(line, ",");

                if(st.countTokens() == 4)
                {
                    // type, itemName, quantity, date
                    String type = st.nextToken();
                    String itemName = st.nextToken();
                    int quantity = Integer.parseInt(st.nextToken());
                    String date = st.nextToken();

                    StockTransaction transaction =
                    new StockTransaction(type, itemName, quantity, date);

                    transactionList.add(transaction);
                }

                line = br.readLine();
            }

            br.close();
        }
        catch(Exception e)
        {
            System.out.println("Error loading transaction history.");
        }
    }
}