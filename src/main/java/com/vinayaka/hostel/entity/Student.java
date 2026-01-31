package com.vinayaka.hostel.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String collegePin;

    @Column(nullable = false)
    private String password;

    private String branch;
    private String department;
    private String currentYear;
    private String hostelId;
    private String gender; // MALE, FEMALE
    private String mobileNumber;
    private String parentMobileNumber;
    
    // Room Details
    private String block;
    private String roomNo;

    // Address
    private String village;
    private String mandal;
    private String district;
    private String state;
    private String country;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String profileImage; // Base64 string

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String qrCode; // Base64 string of the QR code

    @Enumerated(EnumType.STRING)
    private StudentStatus status; // IN_HOSTEL, OUTING, HOME

    public enum StudentStatus {
        IN_HOSTEL,
        OUTING,
        HOME
    }

    public Student() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCollegePin() { return collegePin; }
    public void setCollegePin(String collegePin) { this.collegePin = collegePin; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getCurrentYear() { return currentYear; }
    public void setCurrentYear(String currentYear) { this.currentYear = currentYear; }

    public String getHostelId() { return hostelId; }
    public void setHostelId(String hostelId) { this.hostelId = hostelId; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getParentMobileNumber() { return parentMobileNumber; }
    public void setParentMobileNumber(String parentMobileNumber) { this.parentMobileNumber = parentMobileNumber; }

    public String getBlock() { return block; }
    public void setBlock(String block) { this.block = block; }

    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }

    public String getVillage() { return village; }
    public void setVillage(String village) { this.village = village; }

    public String getMandal() { return mandal; }
    public void setMandal(String mandal) { this.mandal = mandal; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public StudentStatus getStatus() { return status; }
    public void setStatus(StudentStatus status) { this.status = status; }
}
