
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    // PRE: Input files exist in the project folder
    // POST: Reads media, processes commands, and creates output files
    public static void main(String[] args) {

        System.out.println("Welcome to Media Library");

        ArrayList<Media> media = new ArrayList<>();

        Media.mediaCounts[0] = 0;
        Media.mediaCounts[1] = 0;
        Media.mediaCounts[2] = 0;

        try (
            PrintWriter report = new PrintWriter("report.txt");
            PrintWriter error = new PrintWriter("error.txt")
        ) {
            readDataFile(media, error);
            readCommandFile(media, report, error);

            System.out.println("Program completed.");
            System.out.println("Check report.txt and error.txt.");

        } catch (FileNotFoundException e) {
            System.out.println("Error opening file: " + e.getMessage());
        }
    }

    // PRE: Accepts a media record, list, and error file
    // POST: Adds valid media or writes errors to the error file
    public static void newContent(String line,
                                  ArrayList<Media> media,
                                  PrintWriter error) {

        String[] data = line.split(",", -1);

        if (data.length < 8) {
            error.println("Incomplete record: " + line);
            return;
        }

        String typeString = data[0].trim().toUpperCase();

        if (typeString.length() != 1 ||
            "MBS".indexOf(typeString.charAt(0)) == -1) {
            error.println("Invalid type: " + line);
            return;
        }

        char type = typeString.charAt(0);

        String title = data[1].trim();
        String name = data[2].trim();
        String genre = data[4].trim();

        int rating;
        int length;
        int year;

        try {
            rating = Integer.parseInt(data[3].trim());
            length = Integer.parseInt(data[5].trim());
            year = Integer.parseInt(data[6].trim());
        } catch (NumberFormatException e) {
            error.println("Invalid numeric field: " + line);
            return;
        }

        if (rating < 1 || rating > 10) {
            error.println("Invalid rating: " + line);
            return;
        }

        if (length <= 0) {
            error.println("Invalid length: " + line);
            return;
        }

        if (year < 1920 || year > 2024) {
            error.println("Invalid year: " + line);
            return;
        }

        Media item;

        if (type == 'M') {

            ArrayList<String> stars = new ArrayList<>();

            for (int i = 7; i < data.length; i++) {
                String star = data[i].trim();

                if (!star.isEmpty()) {
                    stars.add(star);
                }
            }

            item = new Movie(type, title, name, rating,
                             genre, length, year, stars);

        } else if (type == 'B') {

            if (data.length != 8) {
                error.println("Invalid book record: " + line);
                return;
            }

            int weeks;

            try {
                weeks = Integer.parseInt(data[7].trim());
            } catch (NumberFormatException e) {
                error.println("Invalid numeric field: " + line);
                return;
            }

            if (weeks < 0) {
                error.println("Invalid weeks: " + line);
                return;
            }

            item = new Book(type, title, name, rating,
                            genre, length, year, weeks);

        } else {

            if (data.length != 8) {
                error.println("Invalid song record: " + line);
                return;
            }

            int top40Value;

            try {
                top40Value = Integer.parseInt(data[7].trim());
            } catch (NumberFormatException e) {
                error.println("Invalid numeric field: " + line);
                return;
            }

            if (top40Value != 0 && top40Value != 1) {
                error.println("Invalid Top40: " + line);
                return;
            }

            boolean top40 = top40Value == 1;

            item = new Song(type, title, name, rating,
                            genre, length, year, top40);
        }

        media.add(item);

        if (type == 'M') {
            Media.mediaCounts[0]++;
        } else if (type == 'B') {
            Media.mediaCounts[1]++;
        } else {
            Media.mediaCounts[2]++;
        }
    }

    // PRE: mediaList.txt exists
    // POST: Reads valid records into the media list
    public static void readDataFile(ArrayList<Media> media,
                                    PrintWriter error)
                                    throws FileNotFoundException {

        Scanner input = new Scanner(new File("mediaList.txt"));

        while (input.hasNextLine()) {
            String line = input.nextLine().trim();

            if (line.equalsIgnoreCase("Q")) {
                break;
            }

            if (!line.isEmpty()) {
                newContent(line, media, error);
            }
        }

        input.close();
    }

    // PRE: Accepts an output file
    // POST: Prints the totals for each media category
    public static void printTotals(PrintWriter report) {

        int movies = Media.mediaCounts[0];
        int books = Media.mediaCounts[1];
        int songs = Media.mediaCounts[2];

        report.println("+++++++++++++++++");
        report.println("YOUR MEDIA LIBRARY");
        report.println("# TYPE");
        report.println(movies + " Movies");
        report.println(books + " Books");
        report.println(songs + " Songs");
        report.println((movies + books + songs) + " Items");
        report.println();
    }

    // PRE: Accepts media list and optional filters
    // POST: Prints matching media to the report
    public static void printMedia(ArrayList<Media> media,
                                  PrintWriter report,
                                  char type,
                                  int minRating,
                                  String genre) {

        int count = 0;

        report.println();
        report.println("MEDIA LIST");

        report.printf("%-37s%10s%10s     %-23s%7s%n",
                      "Title", "Year", "Rating", "Genre", "Type");

        report.println("------------------------------------------------"
                     + "--------------------------------------------");

        for (Media item : media) {

            boolean typeMatch = type == 'A'
                             || item.getType() == type;

            boolean ratingMatch = item.getRating() >= minRating;

            boolean genreMatch = genre == null
                    || item.getGenre().equalsIgnoreCase(genre);

            if (typeMatch && ratingMatch && genreMatch) {
                report.print(item.print());
                count++;
            }
        }

        if (count == 0) {
            report.println("No matching media found.");
        }

        report.println("Records found: " + count);
        report.println();
    }

    // PRE: Accepts media list and report
    // POST: Prints all media
    public static void printAll(ArrayList<Media> media,
                                PrintWriter report) {
        printMedia(media, report, 'A', 1, null);
    }

    // PRE: Accepts media list and report
    // POST: Prints all movies
    public static void printMovies(ArrayList<Media> media,
                                   PrintWriter report) {
        printMedia(media, report, 'M', 1, null);
    }

    // PRE: Accepts media list and report
    // POST: Prints all books
    public static void printBooks(ArrayList<Media> media,
                                  PrintWriter report) {
        printMedia(media, report, 'B', 1, null);
    }

    // PRE: Accepts media list, movie title, and output files
    // POST: Prints movie stars or writes an error
    public static void listMovieStars(ArrayList<Media> media,
                                      String title,
                                      PrintWriter report,
                                      PrintWriter error) {

        boolean found = false;

        for (Media item : media) {

            if (item instanceof Movie) {
                Movie movie = (Movie) item;

                if (movie.getTitle().equalsIgnoreCase(title)) {
                    found = true;

                    report.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    report.println("THE STARS OF THE MOVIE "
                                   + movie.getTitle() + " ARE:");

                    ArrayList<String> stars = movie.getStars();

                    for (int i = 0; i < stars.size(); i++) {
                        report.print(stars.get(i));

                        if (i < stars.size() - 1) {
                            report.print(", ");
                        }
                    }

                    report.println();
                    report.println();
                }
            }
        }

        if (found) {
            report.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
            report.println();
        } else {
            error.println("The movie: '" + title
                          + "' is not found in your list");
        }
    }

    // PRE: Accepts media list and an actor name
    // POST: Prints movies containing the actor or writes an error
    public static void findStarMovies(ArrayList<Media> media,
                                      String starName,
                                      PrintWriter report,
                                      PrintWriter error) {

        ArrayList<String> movies = new ArrayList<>();

        for (Media item : media) {

            if (item instanceof Movie) {
                Movie movie = (Movie) item;

                for (String star : movie.getStars()) {
                    if (star.equalsIgnoreCase(starName)) {
                        movies.add(movie.getTitle());
                        break;
                    }
                }
            }
        }

        if (movies.size() == 0) {
            error.println("The star: " + starName
                          + " is not found in any movies in your list");
            return;
        }

        report.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
        report.println(starName + " appears in the following movie(s):");

        for (String movieTitle : movies) {
            report.println(movieTitle);
        }

        report.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
        report.println();
    }

    // PRE: Accepts media list and a key name
    // POST: Prints matching media or writes an error
    public static void findKeyName(ArrayList<Media> media,
                                   String keyName,
                                   PrintWriter report,
                                   PrintWriter error) {

        ArrayList<Media> matches = new ArrayList<>();

        for (Media item : media) {
            if (item.getKeyName().equalsIgnoreCase(keyName)) {
                matches.add(item);
            }
        }

        if (matches.size() == 0) {
            error.println("The name: " + keyName
                          + " was not found in your list");
            return;
        }

        report.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
        report.println(keyName + " appears in the following media item(s):");

        report.printf("%-8s%-37s%10s%n",
                      "TYPE", "TITLE", "RATING");

        for (Media item : matches) {
            report.printf("%-8s%-37s%10d%n",
                          item.getType(),
                          item.getTitle(),
                          item.getRating());
        }

        report.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
        report.println();
    }

    // PRE: mediaCommands.txt exists
    // POST: Executes commands and writes reports/errors
    public static void readCommandFile(ArrayList<Media> media,
                                       PrintWriter report,
                                       PrintWriter error)
                                       throws FileNotFoundException {

        Scanner input = new Scanner(new File("mediaCommands.txt"));

        while (input.hasNextLine()) {

            String line = input.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(",", -1);
            String command = parts[0].trim().toUpperCase();

            if (command.equals("Q")) {
                break;
            }

            if (command.equals("N")) {

                if (parts.length < 9) {
                    error.println("Invalid new media: " + line);
                } else {
                    newContent(
                        line.substring(line.indexOf(',') + 1),
                        media, error
                    );
                }

            } else if (command.equals("T")) {

                if (parts.length == 1) {
                    printTotals(report);
                } else {
                    error.println("Invalid command: " + line);
                }

            } else if (command.equals("L") ||
                       command.equals("F") ||
                       command.equals("K")) {

                if (parts.length < 2 ||
                    parts[1].trim().isEmpty()) {
                    error.println("Command " + command
                                  + " does not contain a string.");
                    continue;
                }

                if (parts.length != 2) {
                    error.println("Invalid command: " + line);
                    continue;
                }

                String search = parts[1].trim();

                if (command.equals("L")) {
                    listMovieStars(media, search, report, error);
                } else if (command.equals("F")) {
                    findStarMovies(media, search, report, error);
                } else {
                    findKeyName(media, search, report, error);
                }

            } else if (command.equals("A") ||
                       command.equals("M") ||
                       command.equals("B") ||
                       command.equals("S")) {

                char type = command.charAt(0);

                if (parts.length == 1) {

                    if (type == 'A') {
                        printAll(media, report);
                    } else if (type == 'M') {
                        printMovies(media, report);
                    } else if (type == 'B') {
                        printBooks(media, report);
                    } else {
                        printSongs(media, report);
                    }

                } else if (parts.length == 2) {

                    String value = parts[1].trim();

                    try {
                        int rating = Integer.parseInt(value);

                        if (rating < 1 || rating > 10) {
                            error.println("Error in rating provided: "
                                          + rating);
                        } else {
                            printMedia(media, report, type, rating, null);
                        }

                    } catch (NumberFormatException e) {
                        printMedia(media, report, type, 1, value);
                    }

                } else {
                    error.println("Invalid command: " + line);
                }

            } else {
                error.println("Invalid command: " + command);
            }
        }

        input.close();
    }

    // PRE: Accepts media list and report
    // POST: Prints all songs
    public static void printSongs(ArrayList<Media> media,
                                  PrintWriter report) {
        printMedia(media, report, 'S', 1, null);
    }
}
