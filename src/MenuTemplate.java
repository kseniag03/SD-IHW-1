/**
 * Abstract class of the menu template
 */
public abstract class MenuTemplate {
    /** Title of the menu */
    private final String title;

    /**
     * Constructor of the menu template
     * @param title title of the menu
     */
    public MenuTemplate(String title) {
        this.title = title;
    }

    /**
     * Getting title of the menu
     * @return string title of the menu
     */
    public String getTitle() {
        return title;
    }

    /**
     * Running menu (without realization)
     */
    public abstract void run();
}
