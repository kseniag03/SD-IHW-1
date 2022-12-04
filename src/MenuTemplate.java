public abstract class MenuEntry {
    private final String title;
    public MenuEntry(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public abstract void run();
}
