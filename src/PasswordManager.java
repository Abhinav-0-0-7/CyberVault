public class PasswordManager {

    private PasswordEntry[] entries = new PasswordEntry[200];
    private int count = 0;

    // ---------- Encryption (Caesar shift +3) ----------
    public String encrypt(String pass) {
        String result = "";
        for (int i = 0; i < pass.length(); i++) {
            char c = pass.charAt(i);
            c += 3;
            result += c;
        }
        return result;
    }

    // ---------- Decryption (Caesar shift -3) ----------
    public String decrypt(String encrypted) {
        String result = "";
        for (int i = 0; i < encrypted.length(); i++) {
            char c = encrypted.charAt(i);
            c -= 3;
            result += c;
        }
        return result;
    }

    // Add password for a specific owner (owner = username or "admin")
    public void addPassword(String account, String username, String password, String owner) {
        if (count >= entries.length) {
            System.out.println("Storage full. Cannot add more entries.");
            return;
        }
        String encrypted = encrypt(password);
        entries[count] = new PasswordEntry(account, username, encrypted, owner);
        count++;
        System.out.println("Password stored securely for owner: " + owner);
    }

    // View all entries: if isAdmin true -> show all, else show only those where owner == currentUser
    public void viewAll(boolean isAdmin, String currentUser) {
        if (count == 0) {
            System.out.println("No passwords stored yet.");
            return;
        }

        boolean printed = false;
        for (int i = 0; i < count; i++) {
            PasswordEntry e = entries[i];
            if (isAdmin || e.getOwner().equalsIgnoreCase(currentUser)) {
                printed = true;
                System.out.println("------------------------------");
                System.out.println("Account: " + e.getAccount());
                System.out.println("Username: " + e.getUsername());
                // show decrypted password only if admin OR owner matches
                if (isAdmin || e.getOwner().equalsIgnoreCase(currentUser)) {
                    System.out.println("Password: " + decrypt(e.getPassword()));
                } else {
                    System.out.println("Password: ********");
                }
                System.out.println("Owner: " + e.getOwner());
            }
        }
        if (!printed) {
            System.out.println("No entries available for user: " + currentUser);
        }
        System.out.println("------------------------------");
    }

    // Search for an account. If admin -> finds first matching account (any owner).
    // If user -> finds only if owner matches currentUser.
    // Returns index or -1 if not found.
    public int search(String account, boolean isAdmin, String currentUser) {
        for (int i = 0; i < count; i++) {
            PasswordEntry e = entries[i];
            if (e.getAccount().equalsIgnoreCase(account)) {
                if (isAdmin || e.getOwner().equalsIgnoreCase(currentUser)) {
                    return i;
                }
            }
        }
        return -1;
    }

    // Get entry safely by index
    public PasswordEntry getEntry(int index) {
        if (index < 0 || index >= count) return null;
        return entries[index];
    }

    // Delete entry: if admin -> can delete any; if user -> can delete only own
    public void delete(String account, boolean isAdmin, String currentUser) {
        int index = -1;
        for (int i = 0; i < count; i++) {
            PasswordEntry e = entries[i];
            if (e.getAccount().equalsIgnoreCase(account)) {
                if (isAdmin || e.getOwner().equalsIgnoreCase(currentUser)) {
                    index = i;
                    break;
                }
            }
        }

        if (index == -1) {
            System.out.println("No matching entry found (or access denied).");
            return;
        }

        // shift left
        for (int i = index; i < count - 1; i++) {
            entries[i] = entries[i + 1];
        }
        entries[count - 1] = null;
        count--;
        System.out.println("Deleted successfully.");
    }

    // Optional: return how many entries stored (useful)
    public int getCount() {
        return count;
    }
}