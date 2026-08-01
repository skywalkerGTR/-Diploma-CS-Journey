import java.time.LocalDate;

public class Item
{
    private String itemID;
    private String itemName;
    private String category;
    private int quantity;
    private String dateAdded;

    // Used when a NEW item is created (date is auto-generated)
    public Item(String itemID,
                String itemName,
                int quantity)
    {
        this.itemID = itemID;
        this.itemName = itemName;
        this.category = "General";
        this.quantity = quantity;
        this.dateAdded = LocalDate.now().toString();
    }

    // Used when LOADING an item from file (date already exists)
    public Item(String itemID,
                String itemName,
                int quantity,
                String dateAdded)
    {
        this.itemID = itemID;
        this.itemName = itemName;
        this.category = "General";
        this.quantity = quantity;
        this.dateAdded = dateAdded;
    }

    public String getItemID()
    {
        return itemID;
    }

    public String getItemName()
    {
        return itemName;
    }

    public String getCategory()
    {
        return category;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public String getDateAdded()
    {
        return dateAdded;
    }

    public void displayItem()
    {
        System.out.println("Item ID: " + itemID);
        System.out.println("Item Name: " + itemName);
        System.out.println("Category: " + category);
        System.out.println("Quantity: " + quantity);
        System.out.println("Date Added: " + dateAdded);
    }

    public String toFileString()
    {
        return itemID + "," +
               itemName + "," +
               category + "," +
               quantity + "," +
               dateAdded;
    }
}