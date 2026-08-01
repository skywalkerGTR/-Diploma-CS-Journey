import java.time.LocalDate;

public class StockTransaction
{
    private String transactionType;
    private String itemName;
    private int quantity;
    private String date;

    // Used when a NEW transaction happens (date is auto-generated)
    public StockTransaction(String transactionType,
                            String itemName,
                            int quantity)
    {
        this.transactionType = transactionType;
        this.itemName = itemName;
        this.quantity = quantity;
        this.date = LocalDate.now().toString();
    }

    // Used when LOADING a transaction from file (date already exists)
    public StockTransaction(String transactionType,
                            String itemName,
                            int quantity,
                            String date)
    {
        this.transactionType = transactionType;
        this.itemName = itemName;
        this.quantity = quantity;
        this.date = date;
    }

    public String getTransactionType()
    {
        return transactionType;
    }

    public String getItemName()
    {
        return itemName;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public String getDate()
    {
        return date;
    }

    public void displayTransaction()
    {
        System.out.println("Type: " + transactionType);
        System.out.println("Item Name: " + itemName);
        System.out.println("Quantity: " + quantity);
        System.out.println("Date: " + date);
    }

    public String toFileString()
    {
        return transactionType + "," +
               itemName + "," +
               quantity + "," +
               date;
    }
}