package cinema;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@Slf4j
public class TheaterController {
    private final Seats seats = new Seats(9, 9);

    @GetMapping("/seats")
    public Seats getSeats() {
        return seats;
    }

    @PostMapping("/purchase")
    public ResponseEntity<Object> purchaseTicket(@RequestBody Seat seat) {
        // ToDo Validate rows & columns (400 - The number of a row or a column is out of bounds!)
        if (seat.getRow() > seats.getRows() ||
                seat.getRow() <= 0 ||
                seat.getColumn() > seats.getColumns() ||
                seat.getColumn() <= 0
        ) {
            throw new SeatException("The number of a row or a column is out of bounds!");
        }

        // Get the seat in case it is in the bounds
        Seat s = seats.getAvailableSeats().get(
                (seat.getRow() - 1) * seats.getRows() + seat.getColumn() - 1
        );

        // ToDo Validate already purchased seat (400 - The ticket has been already purchased!)
        UUID token = UUID.randomUUID();
        if (s.isPurchased()) {
            throw new SeatException("The ticket has been already purchased!");
        } else {
            s.setPurchased(true);
            s.setToken(token.toString());
        }

        // Construct response
        Map<String, Object> purchasedTicket = new HashMap<>() {{
            this.put("token", token.toString());
            this.put("ticket", s);
        }};
        return new ResponseEntity<>(purchasedTicket, HttpStatus.OK);
    }

    @PostMapping("/return")
    public ResponseEntity<Object> returnTicket(@RequestBody Map<String, Object> payload) {
        for (Seat s : this.seats.getAvailableSeats()) {
            if (Objects.equals(s.getToken(), payload.get("token"))) {
                // Make the seat available again
                s.setPurchased(false);

                // Erase the purchase token
                s.setToken(null);

                // Return response
                Map<String, Seat> returnedTicket = new HashMap<>() {{
                    this.put("returned_ticket", s);
                }};
                return new ResponseEntity<>(returnedTicket, HttpStatus.OK);
            }
        }
        throw new SeatException("Wrong token!");
    }

    @PostMapping("/stats")
    public ResponseEntity<Object> calculateStatistics(@RequestParam(required = false) String password) {
        // Handle authorization without security plugin
        if (password == null || !Objects.equals("super_secret", password)) {
            throw new UnauthorizedException("The password is wrong!");
        }

        // Calculate statistics
        int income = 0;
        int availableSeats = 0;
        int purchasedTickets = 0;
        for (Seat s : this.seats.getAvailableSeats()) {
            if (s.isPurchased()) {
                purchasedTickets++;
                income += s.getPrice();
            } else {
                availableSeats++;
            }
        }

        // Construct response
        Map<String, Object> stats = new HashMap<>();
        stats.put("current_income", income);
        stats.put("number_of_available_seats", availableSeats);
        stats.put("number_of_purchased_tickets", purchasedTickets);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @ExceptionHandler(SeatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleSeatException(SeatException s) {
        return new Error(s.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Error handleSeatException(UnauthorizedException u) {
        return new Error(u.getMessage());
    }
}
