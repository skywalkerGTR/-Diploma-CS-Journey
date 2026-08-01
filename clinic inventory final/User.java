public class User
{
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role)
    {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean login(String username, String password)
    {
        return this.username.equals(username) &&
               this.password.equals(password);
    }

    public void logout()
    {
        System.out.println("Logout successful.");
    }

    public String getUsername()
    {
        return username;
    }

    public String getRole()
    {
        return role;
    }

    public String toFileString()
    {
        return username + "," + password + "," + role;
    }
}