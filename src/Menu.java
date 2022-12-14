import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Class of the menu with making choice by input
 */
public class Menu {
    /** List of menu items */
    private final ArrayList<MenuTemplate> items = new ArrayList<>();

   /** Flag if chosen exit option */
    private boolean isExit = false;

    /**
     * Constructor of the menu
     * @param text some text to show before exit
     * @param isMainMenu flag if it is main menu (to add exit or return option)
     */
    public Menu(String text, boolean isMainMenu) {
        items.add(new MenuTemplate(isMainMenu ? "Exit" : "Back") {
            @Override
            public void run() {
                System.out.println(text);
                isExit = true;
            }
        });
    }

    /**
     * Adding menu item
     * @param item menu item to add
     */
    public void addMenu(MenuTemplate item) {
        items.add(item);
    }

    /**
     * Running menu (realization of the MenuTemplate abstract method)
     */
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (!isExit) {
            printMenu();
            int choice = 1;
            try {
                String line = reader.readLine();
                if (line != null) {
                    choice = Integer.parseInt(line);
                    if (choice < 1 || choice > items.size()) {
                        System.out.println("Option is out of range!\n");
                        continue;
                    }
                }
            } catch (Throwable e) {
                System.out.println("Wrong input format! " + e.getMessage() + "\n");
                continue;
            }
            MenuTemplate entry = items.get(choice - 1);
            entry.run();
        }
    }

    /**
     * Printing menu and numbers of options
     */
    private void printMenu() {
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i).getTitle());
        }
        System.out.print("Choose the option: ");
    }
}
