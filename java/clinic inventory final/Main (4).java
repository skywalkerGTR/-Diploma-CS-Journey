import java.util.Scanner;
public class Main
{
    static Scanner input = new Scanner(System.in);

    public static void clearScreen()
    {
        try
        {
            if(System.getProperty("os.name").contains("Windows"))
            {
                new ProcessBuilder("cmd", "/c", "cls")
                .inheritIO()
                .start()
                .waitFor();
            }
            else
            {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        }
        catch(Exception e)
        {
            System.out.println("Unable to clear screen.");
        }
    }

    public static void pause()
    {
        System.out.println("\nPress ENTER to continue...");
        input.nextLine();
    }

    // Prints a title box sized to match the table/content shown below it.
    public static void printTitleBox(String title, int width)
    {
        int padding = width - 2 - title.length();
        int left = padding / 2;
        int right = padding - left;

        System.out.println("╔" + "═".repeat(width - 2) + "╗");
        System.out.println("║" + " ".repeat(left) + title + " ".repeat(right) + "║");
        System.out.println("╚" + "═".repeat(width - 2) + "╝");
    }

    // New feature: create a new Staff or Doctor account (saved to accounts.txt)
    public static void signUp(UserManager userManager)
    {
        clearScreen();
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║                 SIGN UP                    ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Choose Username: ");
        String username = input.nextLine();

        if(userManager.isUsernameTaken(username))
        {
            System.out.println("\n❌ Username already exists.");
            pause();
            return;
        }

        System.out.print("Choose Password: ");
        String password = input.nextLine();

        System.out.println("\n[1] Staff");
        System.out.println("[2] Doctor");
        System.out.print("Choose Role: ");
        int roleChoice = input.nextInt();
        input.nextLine();

        String role;

        if(roleChoice == 2)
        {
            role = "Doctor";
        }
        else
        {
            role = "Staff";
        }

        boolean success = userManager.registerUser(username, password, role);

        if(success)
        {
            System.out.println("\n✅ Account created successfully. Please login.");
        }
        else
        {
            System.out.println("\n❌ Failed to create account.");
        }

        pause();
    }

    public static void main(String[] args)
    {
        InventoryManager manager = new InventoryManager();
        UserManager userManager = new UserManager();

        clearScreen();

        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║           WELCOME TO UiTM C.I.M.S          ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║ System Purpose:                            ║");
        System.out.println("║ • Manage clinic inventory                  ║");
        System.out.println("║ • Monitor stock levels                     ║");
        System.out.println("║ • Generate inventory reports               ║");
        System.out.println("║                                            ║");
        System.out.println("║ User Guidance:                             ║");
        System.out.println("║ 1. Login using role account                ║");
        System.out.println("║ 2. Select menu using number keys           ║");
        System.out.println("║ 3. Follow instructions on screen           ║");
        System.out.println("║                                            ║");
        System.out.println("║ User Roles:                                ║");
        System.out.println("║ • Staff  - Full inventory access           ║");
        System.out.println("║ • Doctor - View inventory and report       ║");
        System.out.println("╚════════════════════════════════════════════╝");

        pause();

        // Outer loop keeps the system alive so that after Logout,
        // the user is brought back to the Login Page again.
        boolean systemRunning = true;

        while(systemRunning)
        {
            User loggedInUser = null;
            boolean exitRequested = false;

            // Login loop: keeps asking until valid login, exit, or a sign up detour.
            while(loggedInUser == null && !exitRequested)
            {
                clearScreen();

                System.out.println("╔════════════════════════════════════════════╗");
                System.out.println("║                UiTM C.I.M.S                ║");
                System.out.println("║   UiTM Clinic Inventory Management System  ║");
                System.out.println("╠════════════════════════════════════════════╣");
                System.out.println("║                 LOGIN PAGE                 ║");
                System.out.println("╠════════════════════════════════════════════╣");
                System.out.println("║ [0] Exit System                            ║");
                System.out.println("║ [9] Sign Up (New Account)                  ║");
                System.out.println("╚════════════════════════════════════════════╝");

                System.out.print("Username : ");
                String username = input.nextLine();

                // [0] Exit System from the Login Page
                if(username.equals("0"))
                {
                    exitRequested = true;
                    break;
                }

                // [9] Sign Up from the Login Page
                if(username.equals("9"))
                {
                    signUp(userManager);
                    continue;
                }

                System.out.print("Password : ");
                String password = input.nextLine();

                loggedInUser = userManager.authenticateUser(username, password);

                if(loggedInUser == null)
                {
                    System.out.println("\nInvalid login. Please try again.");
                    pause();
                }
            }

            // If Exit was chosen from the Login Page, go straight to exit page.
            if(exitRequested)
            {
                systemRunning = false;
                break;
            }

            if(loggedInUser.getRole().equals("Staff"))
            {
                int choice = 0;

                while(choice != 7)
                {
                    clearScreen();

                    System.out.println("╔════════════════════════════════════════════╗");
                    System.out.println("║                 STAFF MENU                 ║");
                    System.out.println("╠════════════════════════════════════════════╣");
                    System.out.println("║ [1] Add Item                               ║");
                    System.out.println("║ [2] View Inventory                         ║");
                    System.out.println("║ [3] Update Stock                           ║");
                    System.out.println("║ [4] Delete Item                            ║");
                    System.out.println("║ [5] Generate Report                        ║");
                    System.out.println("║ [6] Transaction History                    ║");
                    System.out.println("║ [7] Logout                                 ║");
                    System.out.println("╚════════════════════════════════════════════╝");

                    System.out.print("Enter choice : ");
                    choice = input.nextInt();
                    input.nextLine();

                    switch(choice)
                    {
                        case 1:
                            clearScreen();
                            System.out.println("╔════════════════════════════════════════════╗");
                            System.out.println("║                  ADD ITEM                  ║");
                            System.out.println("╚════════════════════════════════════════════╝");
                            System.out.println("[1] General Item");
                            System.out.println("[2] Medicine");
                            System.out.println("[0] Back");
                            System.out.print("\nChoose item type: ");

                            int itemType = input.nextInt();
                            input.nextLine();

                            if(itemType == 0)
                            {
                                continue;
                            }

                            System.out.print("Item Name: ");
                            String name = input.nextLine();

                            System.out.print("Quantity: ");
                            int qty = input.nextInt();
                            input.nextLine();

                            if(itemType == 2)
                            {
                                System.out.print("Expiry Date (yyyy-mm-dd): ");
                                String expiry = input.nextLine();

                                String id = manager.generateMedicineID();
                                Medicine med =
                                new Medicine(id, name, qty, expiry);
                                manager.addItem(med);
                            }
                            else if(itemType == 1)
                            {
                                String id = manager.generateItemID();
                                Item item = new Item(id, name, qty);
                                manager.addItem(item);
                            }
                            else
                            {
                                System.out.println("Invalid choice.");
                            }

                            pause();
                            break;

                        case 2:
                            clearScreen();
                            printTitleBox("VIEW INVENTORY", 70);
                            manager.viewInventory();
                            pause();
                            break;

                        case 3:
                            clearScreen();
                            printTitleBox("UPDATE STOCK", 33);

                            manager.showItemList();

                            System.out.print("Enter [0] to return ");
                            System.out.print("\nEnter Item ID: ");
                            String updateID = input.nextLine();

                            if(updateID.equals("0"))
                            {
                                continue;
                            }

                            System.out.print("New Quantity: ");
                            int newQty = input.nextInt();
                            input.nextLine();

                            manager.updateStock(updateID, newQty);
                            pause();
                            break;

                        case 4:
                            clearScreen();
                            printTitleBox("DELETE ITEM", 33);

                            manager.showItemList();
                            System.out.print("Enter [0] to return");

                            System.out.println("\nEnter Item ID: ");

                            String deleteID = input.nextLine();

                            if(deleteID.equals("0"))
                            {
                                continue;
                            }

                            manager.deleteItem(deleteID);
                            pause();
                            break;

                        case 5:
                            clearScreen();
                            System.out.println("╔════════════════════════════════════════════╗");
                            System.out.println("║               GENERATE REPORT              ║");
                            System.out.println("╚════════════════════════════════════════════╝");
                            manager.generateReport();
                            pause();
                            break;

                        case 6:
                            clearScreen();
                            printTitleBox("TRANSACTION HISTORY", 66);
                            manager.viewTransactionHistory();
                            pause();
                            break;

                        case 7:
                            // Logout -> back to Login Page (outer while loop continues)
                            break;

                        default:
                            System.out.println("Invalid choice.");
                            pause();
                            break;
                    }
                }
            }
            else if(loggedInUser.getRole().equals("Doctor"))
            {
                int choice = 0;

                while(choice != 4)
                {
                    clearScreen();

                    System.out.println("╔════════════════════════════════════════════╗");
                    System.out.println("║                DOCTOR MENU                 ║");
                    System.out.println("╠════════════════════════════════════════════╣");
                    System.out.println("║ [1] View Inventory                         ║");
                    System.out.println("║ [2] Generate Report                        ║");
                    System.out.println("║ [3] Take Medicine                          ║");
                    System.out.println("║ [4] Logout                                 ║");
                    System.out.println("╚════════════════════════════════════════════╝");

                    System.out.print("Enter choice: ");
                    choice = input.nextInt();
                    input.nextLine();

                    switch(choice)
                    {
                        case 1:
                            clearScreen();
                            printTitleBox("VIEW INVENTORY", 70);
                            manager.viewInventory();
                            pause();
                            break;

                        case 2:
                            clearScreen();
                            System.out.println("╔════════════════════════════════════════════╗");
                            System.out.println("║               GENERATE REPORT              ║");
                            System.out.println("╚════════════════════════════════════════════╝");
                            manager.generateReport();
                            pause();
                            break;

                        case 3:
                            clearScreen();
                            printTitleBox("TAKE MEDICINE", 44);

                            manager.showMedicineList();

                            System.out.print("Enter [0] to return");
                            System.out.print("\nEnter Item ID: ");
                            String takeID = input.nextLine();

                            if(takeID.equals("0"))
                            {
                                continue;
                            }

                            System.out.print("Quantity Taken: ");
                            int qtyTaken = input.nextInt();
                            input.nextLine();

                            manager.takeMedicine(takeID, qtyTaken);
                            pause();
                            break;

                        case 4:
                            // Logout -> back to Login Page (outer while loop continues)
                            break;

                        default:
                            System.out.println("Invalid choice.");
                            pause();
                            break;
                    }
                }
            }
            // Loop back to Login Page after logout (systemRunning is still true)
        }

        // Exit Page
        clearScreen();
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║                                            ║");
        System.out.println("║      Thank You for Using UiTM C.I.M.S      ║");
        System.out.println("║                                            ║");
        System.out.println("╚════════════════════════════════════════════╝");
    }
}