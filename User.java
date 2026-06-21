package com.example.tataconnect;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String uid;
    private String email;
    private String password;
    private String userType; // "FAMILY" or "EMPLOYEE"
    private boolean isProfileComplete;
    private boolean isAvailable = true;
    
    // Profile details
    private String fullName;
    private String location;
    private String phone;
    private String profilePicUrl;
    private String about;
    
    // Family specific
    private int familySize;
    private int minBudget;
    private int maxBudget;
    private String servicesNeeded; // Comma separated list
    
    // Employee specific
    private int experience;
    private int expectedSalary;
    private String skills; // Comma separated list
    private String languages; // Comma separated list
    private String availability; // JSON or comma separated string

    // Empty constructor for Firebase
    public User() {}

    public User(String uid, String email, String userType) {
        this.uid = uid;
        this.email = email;
        this.userType = userType;
        this.isProfileComplete = false;
        this.isAvailable = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public boolean isProfileComplete() { return isProfileComplete; }
    public void setProfileComplete(boolean profileComplete) { isProfileComplete = profileComplete; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }

    public String getAbout() { return about; }
    public void setAbout(String about) { this.about = about; }

    public int getFamilySize() { return familySize; }
    public void setFamilySize(int familySize) { this.familySize = familySize; }

    public int getMinBudget() { return minBudget; }
    public void setMinBudget(int minBudget) { this.minBudget = minBudget; }

    public int getMaxBudget() { return maxBudget; }
    public void setMaxBudget(int maxBudget) { this.maxBudget = maxBudget; }

    public String getServicesNeeded() { return servicesNeeded; }
    public void setServicesNeeded(String servicesNeeded) { this.servicesNeeded = servicesNeeded; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public int getExpectedSalary() { return expectedSalary; }
    public void setExpectedSalary(int expectedSalary) { this.expectedSalary = expectedSalary; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getLanguages() { return languages; }
    public void setLanguages(String languages) { this.languages = languages; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("email", email);
        map.put("userType", userType);
        map.put("isProfileComplete", isProfileComplete);
        map.put("isAvailable", isAvailable);
        map.put("fullName", fullName);
        map.put("location", location);
        map.put("phone", phone);
        map.put("profilePicUrl", profilePicUrl);
        map.put("about", about);
        map.put("familySize", familySize);
        map.put("minBudget", minBudget);
        map.put("maxBudget", maxBudget);
        map.put("servicesNeeded", servicesNeeded);
        map.put("experience", experience);
        map.put("expectedSalary", expectedSalary);
        map.put("skills", skills);
        map.put("languages", languages);
        map.put("availability", availability);
        return map;
    }
}
