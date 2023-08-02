package sky.pro.awas;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Entity
public class Socks {

    @Id
    @GeneratedValue
    private Long socksId;
    private String color;
    private int cottonPart;
    private int quantity; //не может быть отрицательным

    public Socks() {}

    public Socks(Long socksId, String color, int cottonPart, @Positive int quantity) {
        this.socksId = socksId;
        this.color = color;
        this.cottonPart = cottonPart;
        this.quantity = quantity;
    }

    public Long getSocksId() {
        return socksId;
    }

    public String getColor() {
        return color;
    }

    public int getCottonPart() {
        return cottonPart;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setSocksId(Long socksId) {
        this.socksId = socksId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Socks socks = (Socks) o;
        return cottonPart == socks.cottonPart && socksId.equals(socks.socksId) && color.equals(socks.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socksId, color, cottonPart);
    }

    @Override
    public String toString() {
        return "Носки" +
                " Id: " + socksId +
                ", цвет: " + color +
                ", содержание cotton (в %): " + cottonPart +
                ", количество: " + quantity;
    }
}
