package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seats {
    @JsonProperty("total_rows")
    private int rows;
    @JsonProperty("total_columns")
    private int columns;

    @JsonProperty("available_seats")
    private List<Seat> availableSeats = new ArrayList<>();

    public Seats(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                availableSeats.add(
                        new Seat(
                                r + 1,
                                c + 1,
                                r < 4 ? 10 : 8,
                                false,
                                null
                        )
                );
            }
        }
    }
}
