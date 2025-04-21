import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileListMaker {

    private static List<String> currentList = new ArrayList<>();
    private static boolean needsToBeSaved = false;
    private static String currentFilename = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nMenu:");
            System.out.println("A - Add an item to the list");
            System.out.println("D - Delete an item from the list");
            System.out.println("I - Insert an item into the list");
            System.out.println("M - Move an item");
            System.out.println("V - View the list");
            System.out.println("C - Clear the list");
            System.out.println("O - Open a list file from disk");
            System.out.println("S - Save the current list to disk");
            System.out.println("Q - Quit the program");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A":
                    addItem(scanner);
                    break;
                case "D":
                    deleteItem(scanner);
                    break;
                case "I":
                    insertItem(scanner);
                    break;
                case "M":
                    moveItem(scanner);
                    break;
                case "V":
                    viewList();
                    break;
                case "C":
                    clearList();
                    break;
                case "O":
                    openList(scanner);
                    break;
                case "S":
                    saveList();
                    break;
                case "Q":
                    running = quitProgram(scanner);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addItem(Scanner scanner) {
        System.out.print("Enter an item to add: ");
        String item = scanner.nextLine();
        currentList.add(item);
        needsToBeSaved = true;
    }

    private static void deleteItem(Scanner scanner) {
        viewList();
        System.out.print("Enter the index of the item to delete: ");
        int index = Integer.parseInt(scanner.nextLine());
        if (index >= 0 && index < currentList.size()) {
            currentList.remove(index);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void insertItem(Scanner scanner) {
        System.out.print("Enter an item to insert: ");
        String item = scanner.nextLine();
        System.out.print("Enter the index to insert at: ");
        int index = Integer.parseInt(scanner.nextLine());
        if (index >= 0 && index <= currentList.size()) {
            currentList.add(index, item);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void moveItem(Scanner scanner) {
        viewList();
        System.out.print("Enter the index of the item to move: ");
        int fromIndex = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter the new index to move to: ");
        int toIndex = Integer.parseInt(scanner.nextLine());

        if (fromIndex >= 0 && fromIndex < currentList.size() && toIndex >= 0 && toIndex <= currentList.size()) {
            String item = currentList.remove(fromIndex);
            currentList.add(toIndex, item);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid indices.");
        }
    }

    private static void viewList() {
        System.out.println("\nCurrent List:");
        for (int i = 0; i < currentList.size(); i++) {
            System.out.println(i + ": " + currentList.get(i));
        }
    }

    private static void clearList() {
        currentList.clear();
        needsToBeSaved = true;
    }

    private static void openList(Scanner scanner) {
        if (needsToBeSaved) {
            System.out.print("You have unsaved changes. Save before opening a new file? (Y/N): ");
            String choice = scanner.nextLine().toUpperCase();
            if (choice.equals("Y")) {
                saveList();
            }
        }

        System.out.print("Enter the filename to open: ");
        String filename = scanner.nextLine();
        try {
            currentList = Files.readAllLines(Paths.get(filename));
            currentFilename = filename;
            needsToBeSaved = false;
            System.out.println("File loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    private static void saveList() {
        if (currentFilename.isEmpty()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter a filename to save as: ");
            currentFilename = scanner.nextLine();
            if (!currentFilename.endsWith(".txt")) {
                currentFilename += ".txt";
            }
        }

        try {
            Files.write(Paths.get(currentFilename), currentList);
            needsToBeSaved = false;
            System.out.println("File saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static boolean quitProgram(Scanner scanner) {
        if (needsToBeSaved) {
            System.out.print("You have unsaved changes. Save before quitting? (Y/N): ");
            String choice = scanner.nextLine().toUpperCase();
            if (choice.equals("Y")) {
                saveList();
            }
        }
        System.out.println("Goodbye!");
        return false;
    }
}