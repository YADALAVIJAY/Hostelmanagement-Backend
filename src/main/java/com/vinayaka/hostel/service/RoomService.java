package com.vinayaka.hostel.service;

import com.vinayaka.hostel.entity.Room;
import com.vinayaka.hostel.repository.RoomRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @PostConstruct
    public void initRooms() {
        // Check if we need to re-initialize (e.g. if Block C is missing, which is part of the new schema)
        boolean isNewSchemaPresent = roomRepository.findAll().stream().anyMatch(r -> r.getBlock().equals("C"));

        if (!isNewSchemaPresent) {
            System.out.println("Detecting old or missing room data (New Schema). Resetting database...");
            roomRepository.deleteAll();

            List<Room> rooms = new ArrayList<>();
            
            // 1. Boys Hostel (Blocks A, B)
            createRange(rooms, "BOYS_HOSTEL", "A", 101, 150, 4);
            createRange(rooms, "BOYS_HOSTEL", "B", 151, 200, 4);

            // 2. Girls Hostel (Blocks C, D)
            createRange(rooms, "GIRLS_HOSTEL", "C", 201, 250, 4);
            createRange(rooms, "GIRLS_HOSTEL", "D", 251, 300, 4);
            
            // 3. AC Hostel (Block E)
            createRange(rooms, "AC_HOSTEL", "E", 301, 350, 2);

            roomRepository.saveAll(rooms);
            System.out.println("Initialized 250 sequential rooms (Boys A-B, Girls C-D, AC E).");
        }
    }

    private void createRange(List<Room> rooms, String hostelId, String block, int start, int end, int capacity) {
        for (int i = start; i <= end; i++) {
            rooms.add(new Room(hostelId, block, String.valueOf(i), capacity));
        }
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    
    // Additional methods for updating occupancy can be added here
}
