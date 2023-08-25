package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Currency {
    private int id;
    private String code;
    private String name;
    private String sign;

    public Currency(String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    @Override
    public String toString() {
        return String.format("Id: %s | Code: %s | Name: %s | Sign: %s",
                this.id, this.code, this.name, this.sign);
    }
}
