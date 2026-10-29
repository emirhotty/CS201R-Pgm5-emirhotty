
public class Song extends Media {

    private boolean top40;

    // Default constructor
    public Song() {
        super();
        top40 = false;
    }

    // Overloaded constructor
    public Song(char type, String title, String name,
                int rating, String genre, int length,
                int yearReleased, boolean top40) {
        super(type, title, name, rating, genre, length, yearReleased);
        this.top40 = top40;
    }

    // Getter
    public boolean getTop40() {
        return top40;
    }

    // Setter
    public void setTop40(boolean top40) {
        this.top40 = top40;
    }

    // PRE: Song object contains valid information
    // POST: Returns formatted song information and Top 40 status
    @Override
    public String print() {
        String status = "No";

        if (top40) {
            status = "Yes";
        }

        return String.format(
            "%-37s%10d%10d     %-23s%7s  Top 40: %s%n",
            title, yearReleased, rating, genre, type, status
        );
    }
}
