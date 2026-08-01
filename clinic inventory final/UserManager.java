import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.*;

public class UserManager
{
    private ArrayList<User> userList;

    public UserManager()
    {
        userList = new ArrayList<User>();
        loadUsers();

        // First time running the system: no accounts.txt yet,
        // so create the original default accounts (unchanged from before).
        if(userList.isEmpty())
        {
            userList.add(new ClinicStaff("staff", "123"));
            userList.add(new Doctor("doctor", "123"));
            saveUsers();
        }
    }

    // Checks if a username is already taken.
    public boolean isUsernameTaken(String username)
    {
        for(User user : userList)
        {
            if(user.getUsername().equals(username))
            {
                return true;
            }
        }

        return false;
    }

    // Registers a new account. role must be "Staff" or "Doctor".
    public boolean registerUser(String username, String password, String role)
    {
        if(isUsernameTaken(username))
        {
            return false;
        }

        User newUser;

        if(role.equals("Doctor"))
        {
            newUser = new Doctor(username, password);
        }
        else
        {
            newUser = new ClinicStaff(username, password);
        }

        userList.add(newUser);
        saveUsers();

        return true;
    }

    // Returns the matching User if login is correct, otherwise null.
    public User authenticateUser(String username, String password)
    {
        for(User user : userList)
        {
            if(user.login(username, password))
            {
                return user;
            }
        }

        return null;
    }

    // ---------- File Input / Output ----------
    // Same BufferedReader/FileReader + PrintWriter/BufferedWriter/FileWriter
    // style used in InventoryManager, with StringTokenizer for parsing.

    public void saveUsers()
    {
        try
        {
            PrintWriter pw =
            new PrintWriter(
            new BufferedWriter(
            new FileWriter("accounts.txt")));

            for(User user : userList)
            {
                pw.println(user.toFileString());
            }

            pw.close();
        }
        catch(Exception e)
        {
            System.out.println("Error saving accounts.");
        }
    }

    public void loadUsers()
    {
        File file = new File("accounts.txt");

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

                if(st.countTokens() == 3)
                {
                    String username = st.nextToken();
                    String password = st.nextToken();
                    String role = st.nextToken();

                    if(role.equals("Doctor"))
                    {
                        userList.add(new Doctor(username, password));
                    }
                    else
                    {
                        userList.add(new ClinicStaff(username, password));
                    }
                }

                line = br.readLine();
            }

            br.close();
        }
        catch(Exception e)
        {
            System.out.println("Error loading accounts.");
        }
    }
}