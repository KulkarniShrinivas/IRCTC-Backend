package Ticket.booking.Services;

import Ticket.booking.entities.User;
import Ticket.booking.util.UserServiceUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user;
    //when user login app -> book ticket -> again we cannt ask user to authenticate to book ticket
    //so we will store user in session and use it to book ticket
    //we can call this as session management


    //will use objectmapper because to read and write json
    //it will serialize and deserialize json

    private List<User> userList;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    private static final String USER_PATH = "app/src/main/java/Ticket/booking/data/users.json";


    public UserBookingService(User user1) throws IOException {
        this.user = user1;
        File users = new File(USER_PATH);

        userList = OBJECT_MAPPER.readValue(users, new TypeReference<List<User>>() {});


    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();

    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (Exception e){
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USER_PATH);
        OBJECT_MAPPER.writeValue(usersFile, userList);
    }

    //fetBooking
    public void fetchBooking(){
        user.printTickets();
    }

    //cancel Booking
    public Boolean cancelBooking(String ticketId){
        return user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(ticketId));

    }




}

//json -> Object (User) -> Deserialize
//Object (User) -> json -> Serialize