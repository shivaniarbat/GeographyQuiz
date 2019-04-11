package cs.uga.edu.geographyquiz;

/**
 * POJO class for main table entry object
 */

public class CountryContinentNeighbourTableEntry {

    private long id;
    private String countryName;
    private String question;
    private String continent;
    private String neighbours;

    /* default constructor */

    public CountryContinentNeighbourTableEntry(){
        this.id = -1;
        this.countryName = null;
        this.continent = null;
        this.neighbours = null;
        this.question = null;
    }

    public CountryContinentNeighbourTableEntry(String countryName, String question, String continent, String neighbours) {
        this.id = 1;
        this.countryName = countryName;
        this.question = question;
        this.continent = continent;
        this.neighbours = neighbours;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(String neighbours) {
        this.neighbours = neighbours;
    }

    public String toString() {
        return id + ": " + countryName + " " + continent + " " + question + " " + neighbours;
    }
}
