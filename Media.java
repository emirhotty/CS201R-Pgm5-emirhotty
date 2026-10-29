
public abstract class Media {

    protected char type;
    protected String title;
    protected String name;
    protected int rating;
    protected String genre;
    protected int length;
    protected int yearReleased;

    public static int[] mediaCounts = {0, 0, 0};

    // Default constructor
    public Media() {
        type = ' ';
        title = "";
        name = "";
        rating = 0;
        genre = "";
        length = 0;
        yearReleased = 0;
    }

    // Overloaded constructor
    public Media(char type, String title, String name,
                 int rating, String genre, int length,
                 int yearReleased) {
        this.type = type;
        this.title = title;
        this.name = name;
        this.rating = rating;
        this.genre = genre;
        this.length = length;
        this.yearReleased = yearReleased;
    }

    // Getters
    public char getType() { return type; }
    public String getTitle() { return title; }
    public String getKeyName() { return name; }
    public int getRating() { return rating; }
    public String getGenre() { return genre; }
    public int getLength() { return length; }
    public int getYearReleased() { return yearReleased; }

    // Setters
    public void setType(char type) { this.type = type; }
    public void setTitle(String title) { this.title = title; }
    public void setKeyName(String name) { this.name = name; }
    public void setRating(int rating) { this.rating = rating; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setLength(int length) { this.length = length; }
    public void setYearReleased(int yearReleased) {
        this.yearReleased = yearReleased;
    }

    // PRE: Media object contains valid information
    // POST: Each subclass returns its formatted information
    public abstract String print();
}
