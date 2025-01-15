import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

//V1
/**
 * Main class for managing
 */
public class Main {

    /**
     * Represents  and their attributes.
     */
    static class Person {
        Integer ID;
        String name;
        House house;
        String ereigniss;
        LocalDate date;


        public Person(LocalDate date, String ereigniss, House house, String name, Integer ID) {
            this.date = date;
            this.ereigniss = ereigniss;
            this.house = house;
            this.name = name;
            this.ID = ID;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public String getEreigniss() {
            return ereigniss;
        }

        public void setEreigniss(String ereigniss) {
            this.ereigniss = ereigniss;
        }

        public House getHouse() {
            return house;
        }

        public void setHouse(House house) {
            this.house = house;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getID() {
            return ID;
        }

        public void setID(Integer ID) {
            this.ID = ID;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "ID=" + ID +
                    ", name='" + name + '\'' +
                    ", house=" + house +
                    ", ereigniss='" + ereigniss + '\'' +
                    ", date=" + date +
                    '}';
        }
    }

    /**
     * Main method for executing the program.
     *
     * @param args Command-line arguments.
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {


        List<Person> people = readFromFile("got.tsv") ;

        Scanner scanner = new Scanner(System.in);
        String userChoice;
        do {
            // Display the menu
            System.out.println("\nChoose an option:");
            System.out.println("1. View Persons");
            System.out.println("2.  Filter by letter");
            System.out.println("3. Stark house ");
            System.out.println("4. write ereignisse in file");
            System.out.println("5. Calculate total points for Persons");
            System.out.println("0. Exit");

            // Get user's choice
            userChoice = scanner.nextLine();

            switch (userChoice) {
                case "0":
                    System.out.println("Exiting the program.");
                    break;
                case "1":
                    people.forEach(System.out::println);
                    break;
                case "2":
                    System.out.println("Enter letter");
                    String letter = scanner.nextLine();
                    peopleWithLetter(people, letter);
                    break;
                case "3":
                    System.out.println("Stark house ereigniss");
                    sortEreigniss(people);

                    break;
                case "4":
                    writeEreignisse("ereignisse.txt",people);
                    break;
                case "5":

                    break;
                default:

                    break;
            }

        } while (!userChoice.equals("0"));
    }

    /**
     * Reads Persons data from a text file.
     *
     * @param filename Name of the text file.
     * @return List of Persons.
     * @throws IOException If an I/O error occurs.
     */

    public static List<Person> readFromFile(String filename) throws IOException {
        List<Person> persons = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filename));
        for (String line : lines) {
            String[] fields = line.split("\t");
            int ID = Integer.parseInt(fields[0]);
            String name = fields[1];
            House house = House.valueOf(fields[2].toUpperCase());
            String ereigniss = fields[3];
            LocalDate date = LocalDate.parse(fields[4]);

            persons.add(new Person(date, ereigniss, house, name, ID));

        }
        return persons;
    }

    /**
     *
     * @param people
     * @param let
     *
     * prints a list of ppl whose name start with letter let
     */
    public static void peopleWithLetter(List<Person> people, String let){
        System.out.println("Pppl with letter" + let);
        people.stream()
                .filter(person -> person.getName().charAt(0) == let.toUpperCase().charAt(0))
                .map(person -> person.getName())
                .distinct()
                .forEach(System.out::println);

    }

    public static void sortEreigniss(List<Person> people){
        System.out.println("Ereigniss fur stark");
        people.stream()
                .filter(person -> person.getHouse().equals(House.STARK))
                .sorted(Comparator.comparing(Person::getDate))
                .forEach(person -> System.out.println( person.getDate() + " "+ person.getName() +  " "+ person.getEreigniss()));
    }

    public static void writeEreignisse(String filename, List<Person> people) throws IOException {
        HashMap<House, Integer> ereignisses = new HashMap<>();
        for(Person person : people){
            ereignisses.put(person.getHouse(), ereignisses.getOrDefault(person.getHouse(), 0) + 1);
        }

        List<Map.Entry<House, Integer>> ergList = new ArrayList<>(ereignisses.entrySet());
        ergList.sort(Comparator.comparing(Map.Entry::getKey));

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename));
        for(Map.Entry<House, Integer> entry : ergList){
            writer.write(entry.getKey() + "#" + entry.getValue());
            writer.newLine();
        }
        writer.close();
        System.out.println("wrote in file " + filename);
    }





}
