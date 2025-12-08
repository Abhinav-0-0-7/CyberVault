import java.util.Scanner;

public class CyberVault {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PasswordManager pm = new PasswordManager();

        // --------- Predefined users & admin ----------
        String[] users = { "Samrat", "Shreyas" };          // two users
        String[] userPasswords = { "1234", "5678" };  // their passwords

        String adminUser = "sr_230607";   // admin username
        String adminPass = "admin@1234";       // admin password
        // ----------------------------------------------------------------

        System.out.println("=== Welcome to CyberVault ===");
        System.out.println("Login type:");
        System.out.println("1. Admin");
        System.out.println("2. User");
        System.out.print("Choose (1 or 2): ");
        int loginType = sc.nextInt();
        sc.nextLine(); // consume newline

        boolean isAdmin = false;
        String currentUser = ""; // holds logged in username for user mode

        if (loginType == 1) {
            // Admin login
            System.out.print("Enter admin username: ");
            String aUser = sc.nextLine();
            System.out.print("Enter admin password: ");
            String aPass = sc.nextLine();

            if (aUser.equals(adminUser) && aPass.equals(adminPass)) {
                isAdmin = true;
                currentUser = adminUser;
                System.out.println("Admin login successful.");
            } else {
                System.out.println("Admin login failed. Starting as Guest user (no access to admin features).");
                isAdmin = false;
                currentUser = "guest";
            }
        } else {
            // User login
            System.out.print("Enter username: ");
            String u = sc.nextLine();
            System.out.print("Enter password: ");
            String upass = sc.nextLine();

            boolean found = false;
            for (int i = 0; i < users.length; i++) {
                if (users[i].equalsIgnoreCase(u) && userPasswords[i].equals(upass)) {
                    found = true;
                    currentUser = users[i];
                    isAdmin = false;
                    break;
                }
            }
            if (!found) {
                System.out.println("User login failed. Starting as Guest (cannot view/decrypt any user entries).");
                currentUser = "guest";
            } else {
                System.out.println("User login successful. Welcome, " + currentUser);
            }
        }

        // ---------- Main menu ----------
        while (true) {
            System.out.println("\n=== CyberVault Menu ===");
            System.out.println("1. Add Password");
            System.out.println("2. View Passwords");
            System.out.println("3. Search Password");
            System.out.println("4. Delete Password");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    // Add password: owner = currentUser (if admin, ask for owner)
                    System.out.print("Enter account name: ");
                    String account = sc.nextLine();

                    System.out.print("Enter username for the account: ");
                    String accUser = sc.nextLine();

                    System.out.print("Enter password for the account: ");
                    String accPass = sc.nextLine();

                    String ownerForEntry = currentUser;
                    if (isAdmin) {
                        // Admin can add entries for any owner
                        System.out.print("Enter owner for this entry (username): ");
                        String ownerInput = sc.nextLine();
                        // if ownerInput matches a real user or 'admin', accept; otherwise still accept (flexible)
                        ownerForEntry = ownerInput;
                    }

                    pm.addPassword(account, accUser, accPass, ownerForEntry);
                    break;

                case 2:
                    // View passwords: admin sees all, user sees only own
                    pm.viewAll(isAdmin, currentUser);
                    break;

                case 3:
                    System.out.print("Enter account name to search: ");
                    String searchAcc = sc.nextLine();
                    int idx = pm.search(searchAcc, isAdmin, currentUser);

                    if (idx == -1) {
                        System.out.println("Not found or access denied.");
                    } else {
                        PasswordEntry e = pm.getEntry(idx);
                        System.out.println("Account: " + e.getAccount());
                        System.out.println("Username: " + e.getUsername());
                        System.out.println("Owner: " + e.getOwner());
                        if (isAdmin || e.getOwner().equalsIgnoreCase(currentUser)) {
                            System.out.println("Password: " + pm.decrypt(e.getPassword()));
                        } else {
                            System.out.println("Password: ********");
                        }
                    }
                    break;

                case 4:
                    System.out.print("Enter account name to delete: ");
                    String delAcc = sc.nextLine();
                    pm.delete(delAcc, isAdmin, currentUser);
                    break;

                case 5:
                    System.out.println("Depressing. Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
