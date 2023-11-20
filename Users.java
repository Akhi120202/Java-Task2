/*The User class appears to represent a user in a system.
username (Type: String) is to store the name or identifier of the user.
isAdmin (Type: boolean) suggests whether the user has administrative privileges (true if the user is an admin, false otherwise).*/

class User {
    String username;
    boolean isAdmin;

    public User(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
    }
}