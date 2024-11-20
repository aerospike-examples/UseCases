package com.aerospike.usecases.rtb.model;

enum Gender {
    MALE, FEMALE, UNKNOWN, OTHER

}

public class Demographics {
    private String ageRange;
    private int age;
    private Gender gender;
    private String incomeLevel;
    private String educationLevel;
    private String employmentStatus;
    private String matitalStatus;

    public Demographics(String ageRange, int age, Gender gender, String incomeLevel, String educationLevel,
            String employmentStatus, String matitalStatus) {
        this.ageRange = ageRange;
        this.age = age;
        this.gender = gender;
        this.incomeLevel = incomeLevel;
        this.educationLevel = educationLevel;
        this.employmentStatus = employmentStatus;
        this.matitalStatus = matitalStatus;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public String getIncomeLevel() {
        return incomeLevel;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public String getMatitalStatus() {
        return matitalStatus;
    }

}
