import java.time.LocalDate;

public class Medicine extends Item
{
    private String expiryDate;

    // Used when a NEW medicine is created (date added is auto-generated)
    public Medicine(String itemID,
                    String itemName,
                    int quantity,
                    String expiryDate)
    {
        super(itemID, itemName, quantity);
        this.expiryDate = expiryDate;
    }

    // Used when LOADING a medicine from file (date added already exists)
    public Medicine(String itemID,
                    String itemName,
                    int quantity,
                    String dateAdded,
                    String expiryDate)
    {
        super(itemID, itemName, quantity, dateAdded);
        this.expiryDate = expiryDate;
    }

    public String getExpiryDate()
    {
        return expiryDate;
    }

    // Checks whether this medicine has already expired.
    // Expects expiryDate in yyyy-MM-dd format. If the date cannot
    // be parsed, it is treated as "not expired" to stay safe.
    public boolean isExpired()
    {
        try
        {
            LocalDate expiry = LocalDate.parse(expiryDate);
            return expiry.isBefore(LocalDate.now());
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public void displayMedicine()
    {
        displayItem();
        System.out.println("Expiry Date: " + expiryDate);

        if(isExpired())
        {
            System.out.println("Status: ⚠ EXPIRED");
        }
    }

    public String toFileString()
    {
        return getItemID() + "," +
               getItemName() + "," +
               "Medicine," +
               getQuantity() + "," +
               getDateAdded() + "," +
               expiryDate;
    }
}