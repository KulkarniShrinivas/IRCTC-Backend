package Ticket.booking.Services;

import Ticket.booking.entities.Train;
import Ticket.booking.entities.User;
import Ticket.booking.util.UserServiceUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    //when user login app -> book ticket -> again we cannt ask user to authenticate to book ticket
    //so we will store user in session and use it to book ticket
    //we can call this as session management


    //will use objectmapper because to read and write json
    //it will serialize and deserialize json

    private User user;
    private List<User> userList;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String USER_PATH = "../../localDb/users.json";
    private static final String TRAIN_PATH = "../../localDb/train.json";



    public UserBookingService(User user1) throws IOException {
        this.user = user1;
        this.userList = loadUser();
    }

    public UserBookingService() throws IOException {
        this.userList = loadUser();
    }

    public List<User> loadUser() throws IOException {
        File users = new File(USER_PATH);
        return OBJECT_MAPPER.readValue(users, new TypeReference<List<User>>() {});
    }

    public Boolean loginUser(String name, String password) {
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(name) && UserServiceUtil.checkPassword(password, user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1) {
        try {
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USER_PATH);
        OBJECT_MAPPER.writeValue(usersFile, userList);
    }

    public void fetchBooking() {
        user.printTickets();
    }

    public void cancelBooking(String ticketId) throws IOException {
        user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(ticketId));
        saveUserListToFile();
    }

    public List<Train> getTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException e) {
            return null;
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int col) {
        List<List<Integer>> seats = train.getSeats();
        if (seats.get(row).get(col) == 0) {
            seats.get(row).set(col, 1); // Mark the seat as booked
            saveTrainListToFile(train);
            return true;
        } else {
            return false;
        }
    }

    private void saveTrainListToFile(Train train) {
        try {
            List<Train> trainList = loadTrains();
            trainList.stream()
                    .filter(t -> t.getTrainId().equals(train.getTrainId()))
                    .forEach(t -> t.setSeats(train.getSeats()));
            OBJECT_MAPPER.writeValue(new File(TRAIN_PATH), trainList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Train> loadTrains() throws IOException {
        File trainsFile = new File(TRAIN_PATH);
        return OBJECT_MAPPER.readValue(trainsFile, new TypeReference<List<Train>>() {});
    }
}