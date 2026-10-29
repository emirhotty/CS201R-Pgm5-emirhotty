
import java.util.ArrayList;

public class Movie extends Media {

    private ArrayList<String> stars;

    // Default constructor
    public Movie() {
        super();
        stars = new ArrayList<>();
    }

    // Overloaded constructor
    public Movie(char type, String title, String name,
                 int rating, String genre, int length,
                 int yearReleased, ArrayList<String> stars) {
        super(type, title, name, rating, genre, length, yearReleased);
        this.stars = stars;
    }

    // Getter
    public ArrayList<String> getStars() {
        return stars;
    }

    // Setter
    public void setStars(ArrayList<String> stars) {
        this.stars = stars;
    }

    // PRE: Movie object contains valid information
    // POST: Returns formatted movie information and stars
    @Override
    public String print() {
        String output = String.format(
            "%-37s%10d%10d     %-23s%7s",
            title, yearReleased, rating, genre, type
        );

        output += "  Stars: ";

        for (int i = 0; i < stars.size(); i++) {
            output += stars.get(i);

            if (i < stars.size() - 1) {
                output += ", ";
            }
        }

        return output + "\n";
    }
}
