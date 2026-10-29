
public class Book extends Media {

    private int weeksNYT;

    // Default constructor
    public Book() {
        super();
        weeksNYT = 0;
    }

    // Overloaded constructor
    public Book(char type, String title, String name,
                int rating, String genre, int length,
                int yearReleased, int weeksNYT) {
        super(type, title, name, rating, genre, length, yearReleased);
        this.weeksNYT = weeksNYT;
    }

    // Getter
    public int getWeeks() {
        return weeksNYT;
    }

    // Setter
    public void setWeeks(int weeks) {
        weeksNYT = weeks;
    }

    // PRE: Book object contains valid information
    // POST: Returns formatted book information and NYT weeks
    @Override
    public String print() {
        return String.format(
            "%-37s%10d%10d     %-23s%7s  NYT Weeks: %d%n",
            title, yearReleased, rating, genre, type, weeksNYT
        );
    }
}
