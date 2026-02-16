package com.vinayaka.hostel.dto;

public class StudentSummaryDTO {
    private Long id;
    private String name;
    private String collegePin;
    private String status; // IN_HOSTEL, OUTING, HOME
    private String hostelId;
    private String mobileNumber;
    private String parentMobileNumber;
    private String block;
    private String roomNo;

    // Default Constructor
    public StudentSummaryDTO() {}

    // Constructor with fields
    public StudentSummaryDTO(Long id, String name, String collegePin, String status, String hostelId, String mobileNumber, String parentMobileNumber, String block, String roomNo) {
        this.id = id;
        this.name = name;
        this.collegePin = collegePin;
        this.status = status;
        this.hostelId = hostelId;
        this.mobileNumber = mobileNumber;
        this.parentMobileNumber = parentMobileNumber;
        this.block = block;
        this.roomNo = roomNo;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCollegePin() { return collegePin; }
    public void setCollegePin(String collegePin) { this.collegePin = collegePin; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getHostelId() { return hostelId; }
    public void setHostelId(String hostelId) { this.hostelId = hostelId; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getParentMobileNumber() { return parentMobileNumber; }
    public void setParentMobileNumber(String parentMobileNumber) { this.parentMobileNumber = parentMobileNumber; }

    public String getBlock() { return block; }
    public void setBlock(String block) { this.block = block; }

    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
}
