package com.vinayaka.hostel.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hostelId;
    private String block;
    private String roomNo;
    private int capacity;
    private int currentOccupancy;
    
    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    public enum RoomStatus {
        AVAILABLE,
        FULL,
        MAINTENANCE
    }

    public Room() {}

    public Room(String hostelId, String block, String roomNo, int capacity) {
        this.hostelId = hostelId;
        this.block = block;
        this.roomNo = roomNo;
        this.capacity = capacity;
        this.currentOccupancy = 0;
        this.status = RoomStatus.AVAILABLE;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getHostelId() { return hostelId; }
    public void setHostelId(String hostelId) { this.hostelId = hostelId; }

    public String getBlock() { return block; }
    public void setBlock(String block) { this.block = block; }

    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getCurrentOccupancy() { return currentOccupancy; }
    public void setCurrentOccupancy(int currentOccupancy) { this.currentOccupancy = currentOccupancy; }

    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }
}
