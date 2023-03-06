package cinema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    private int row;
    private int column;
    private int price;
    @JsonIgnore
    private boolean purchased;
    @JsonIgnore
    private String token;
}
