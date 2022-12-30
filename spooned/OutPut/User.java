package OutPut;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.concurrent.atomic.AtomicInteger;
public class User {
    private static final AtomicInteger entreprise = new AtomicInteger(0);

    @JsonProperty("id")
    private final int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("age")
    private int age;

    private String password;

    public User() {
        id = entreprise.incrementAndGet();
    }

    public User(String name, String email, String password, int age) {
        id = entreprise.incrementAndGet();
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    @Override
    public String toString() {
        String json = "{ \"id\": %d, \"name\": \"%s\", \"email\": \"%s\", \"age\": %d }";
        return String.format(json, id, name, email, age);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User))
            return false;

        return ((User) (o)).getId() == id;
    }

    @Override
    public int hashCode() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}