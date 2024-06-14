/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package j1.l.p0002;

import java.io.*;
import java.util.*;

public class UserManagement {

    private static final String FILE_NAME = "user.dat";
    private static List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        loadUsers();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (choice) {
                case 1:
                    createUser(scanner);
                    break;
                case 2:
                    checkExistUser(scanner);
                    break;
                case 3:
                    searchUser(scanner);
                    break;
                case 4:
                    updateUser(scanner);
                    break;
                case 5:
                    saveUsers();
                    break;
                case 6:
                    printUsers();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n--- User Management ---");
        System.out.println("1. Create user account");
        System.out.println("2. Check exist user");
        System.out.println("3. Search user information by name");
        System.out.println("4. Update user");
        System.out.println("5. Save account to file");
        System.out.println("6. Print list user from file");
        System.out.println("0. Quit");
        System.out.print("Enter your choice: ");
    }

    private static void createUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        if (username.length() < 5 || username.contains(" ") || isUserExists(username)) {
            System.out.println("Invalid username or username already exists.");
            return;
        }

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        if (password.length() < 6 || password.contains(" ")) {
            System.out.println("Invalid password.");
            return;
        }

        System.out.print("Confirm password: ");
        String confirmPassword = scanner.nextLine().trim();
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return;
        }

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine().trim();
        if (!phone.matches("\\d{10}")) {
            System.out.println("Invalid phone number.");
            return;
        }

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        if (!email.matches("[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,4}")) {
            System.out.println("Invalid email format.");
            return;
        }

        users.add(new User(username, firstName, lastName, password, phone, email));
        System.out.println("User created successfully.");
    }

    private static void checkExistUser(Scanner scanner) {
        System.out.print("Enter username to check: ");
        String username = scanner.nextLine().trim();
        if (isUserExists(username)) {
            System.out.println("Exist User");
        } else {
            System.out.println("No User Found!");
        }
    }

    private static void searchUser(Scanner scanner) {
        System.out.print("Enter name to search: ");
        String searchString = scanner.nextLine().trim();
        List<User> foundUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getFirstName().contains(searchString) || user.getLastName().contains(searchString)) {
                foundUsers.add(user);
            }
        }
        if (foundUsers.isEmpty()) {
            System.out.println("Have no any user");
        } else {
            foundUsers.sort(Comparator.comparing(User::getFirstName));
            foundUsers.forEach(System.out::println);
        }
    }

    private static void updateUser(Scanner scanner) {
        System.out.print("Enter username to update: ");
        String username = scanner.nextLine().trim();
        User user = getUserByUsername(username);
        if (user == null) {
            System.out.println("Username does not exist.");
            return;
        }

        System.out.print("Enter new first name (leave blank to keep current): ");
        String firstName = scanner.nextLine().trim();
        if (!firstName.isEmpty()) {
            user.setFirstName(firstName);
        }

        System.out.print("Enter new last name (leave blank to keep current): ");
        String lastName = scanner.nextLine().trim();
        if (!lastName.isEmpty()) {
            user.setLastName(lastName);
        }

        System.out.print("Enter new password (leave blank to keep current): ");
        String password = scanner.nextLine().trim();
        if (!password.isEmpty()) {
            if (password.length() < 6 || password.contains(" ")) {
                System.out.println("Invalid password.");
                return;
            }

            System.out.print("Confirm new password: ");
            String confirmPassword = scanner.nextLine().trim();
            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match.");
                return;
            }
            user.setPassword(password);
        }

        System.out.print("Enter new phone number (leave blank to keep current): ");
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty()) {
            if (!phone.matches("\\d{10}")) {
                System.out.println("Invalid phone number.");
                return;
            }
            user.setPhone(phone);
        }

        System.out.print("Enter new email (leave blank to keep current): ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) {
            if (!email.matches("[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,4}")) {
                System.out.println("Invalid email format.");
                return;
            }
            user.setEmail(email);
        }

        System.out.println("User updated successfully.");
    }

    private static void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (User user : users) {
                writer.println(user.toFileString());
            }
            System.out.println("Users saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving users to file.");
        }
    }

    private static void loadUsers() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                if (data.length == 6) {
                    users.add(new User(data[0], data[1], data[2], data[3], data[4], data[5]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading users from file.");
        }
    }

    private static void printUsers() {
        users.sort(Comparator.comparing(User::getFirstName));
        users.forEach(System.out::println);
    }

    private static boolean isUserExists(String username) {
        return users.stream().anyMatch(user -> user.getUsername().equals(username));
    }

    private static User getUserByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
    }    
}
