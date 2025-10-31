package application.modules;

public class Book {
    private String title;
    private String author;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        application.Logger.logStorage(title, "INFO", "Book created: " + title + " by " + author);
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }

    @Override
    public String toString() {
        return title + " by " + author;
    }

	public float getWeightKg() {
		// TODO Auto-generated method stub
		return 0;
	}
}
