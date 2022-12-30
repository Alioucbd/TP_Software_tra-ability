package OutPut;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
public class Product {
    private static final AtomicInteger entreprise = new AtomicInteger(0);

    @JsonProperty("id")
    private final int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private int price;

    @JsonIgnore
    private LocalDate expirationDate;

    public Product() {
        id = entreprise.incrementAndGet();
        expirationDate = LocalDate.now().plusYears(1);
    }

    public Product(String name, int price) {
        id = entreprise.incrementAndGet();
        this.name = name;
        this.price = price;
        expirationDate = LocalDate.now().plusYears(1);
    }

    @Override
    public String toString() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yy");
        String json = "{ \"id\": %d, \"name\": \"%s\", \"expirationDate\": \"%s\", \"price\": %d }";
        return String.format(json, id, name, df.format(expirationDate), price);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product))
            return false;

        return ((Product) (o)).getId() == id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}