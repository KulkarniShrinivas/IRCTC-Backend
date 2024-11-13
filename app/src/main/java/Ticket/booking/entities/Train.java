package Ticket.booking.entities;

import org.checkerframework.checker.units.qual.Time;

import java.util.List;
import java.util.Map;

public class Train {

    private String trainId;


    private String trainNo;

    private List<List<Integer>> seats;

    //map for timming of train

    private Map<String, Time> stationTimes;

    private List<String> stations;

}
