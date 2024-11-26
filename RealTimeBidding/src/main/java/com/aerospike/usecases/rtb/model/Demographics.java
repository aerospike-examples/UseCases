package com.aerospike.usecases.rtb.model;

import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@AerospikeRecord
public class Demographics {
    private String ageRange;
    private Gender gender;
    private String incomeLevel;
    private String educationLevel;
    private String employmentStatus;
    private String matitalStatus;

    public Demographics(Gender gender, String ageRange, String incomeLevel, String educationLevel,
            String employmentStatus, String matitalStatus) {
        this.gender = gender;
        this.ageRange = ageRange;
        this.incomeLevel = incomeLevel;
        this.educationLevel = educationLevel;
        this.employmentStatus = employmentStatus;
        this.matitalStatus = matitalStatus;
    }

    public String getAgeRange() {
        return ageRange;
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
