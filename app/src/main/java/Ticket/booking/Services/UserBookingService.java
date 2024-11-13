package Ticket.booking.Services;

import Ticket.booking.entities.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserBookingService {

    private User user;
    //when user login app -> book ticket -> again we cannt ask user to authenticate to book ticket
    //so we will store user in session and use it to book ticket
    //we can call this as session management


    //will use objectmapper because to read and write json
    //it will serialize and deserialize json

    private List<User> userList;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    private static final String USER_PATH = "../localDb/users.json";


    public UserBookingService(User user1) throws IOException {
        this.user = user1;
        File users = new File(USER_PATH);

        userList = OBJECT_MAPPER.readValue(users, new TypeReference<List<User>>() {});



    }


}
