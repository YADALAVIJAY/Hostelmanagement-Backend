package com.vinayaka.hostel.repository;

import com.vinayaka.hostel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHostelId(String hostelId);
    Room findByHostelIdAndBlockAndRoomNo(String hostelId, String block, String roomNo);
}
