package com.aerospike.usecases.model;

import com.aerospike.mapper.annotations.AerospikeRecord;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the demographics of a user in the real-time bidding system. This
 * class includes various attributes such as age range, gender, income level,
 * education level, employment status, and marital status.
 * 
 * <p>
 * This class is annotated with {@link AerospikeRecord} to indicate that it is
 * an Aerospike record. It also uses Lombok annotations for generating
 * boilerplate code such as constructors and toString method.
 * </p>
 * 
 * <p>
 * Attributes:
 * <ul>
 * <li>{@code ageRange} - The age range of the user.</li>
 * <li>{@code gender} - The gender of the user, represented by the
 * {@link Gender} enum.</li>
 * <li>{@code incomeLevel} - The income level of the user.</li>
 * <li>{@code educationLevel} - The education level of the user.</li>
 * <li>{@code employmentStatus} - The employment status of the user.</li>
 * <li>{@code maritalStatus} - The marital status of the user.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * This class provides getter methods for each attribute to allow access to the
 * values.
 * </p>
 * 
 * @see Gender
 * @see AerospikeRecord
 */

@NoArgsConstructor
@AllArgsConstructor
@AerospikeRecord
public class Demographics {
    private String ageRange;
    private Gender gender;
    private String incomeLevel;
    private String educationLevel;
    private String employmentStatus;
    private String maritalStatus;

    public Demographics(Gender gender, String ageRange, String incomeLevel, String educationLevel,
            String employmentStatus, String maritalStatus) {
        this.gender = gender;
        this.ageRange = ageRange;
        this.incomeLevel = incomeLevel;
        this.educationLevel = educationLevel;
        this.employmentStatus = employmentStatus;
        this.maritalStatus = maritalStatus;
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
        return maritalStatus;
    }

    @Override
    public String toString() {
        return "Demographics{" + "ageRange='" + ageRange + '\'' + ", gender=" + gender + ", incomeLevel='" + incomeLevel
                + '\'' + ", educationLevel='" + educationLevel + '\'' + ", employmentStatus='" + employmentStatus + '\''
                + ", maritalStatus='" + maritalStatus + '\'' + '}';
    }

    public Map<String, Object> asMap() {
        // for native storage manager
        Map<String, Object> map = new HashMap<>();
        map.put("ageRange", ageRange);
        map.put("gender", gender.toString());
        map.put("incomeLevel", incomeLevel);
        map.put("educationLevel", educationLevel);
        map.put("employmentStatus", employmentStatus);
        map.put("maritalStatus", maritalStatus);
        return map;
    }

    public static Demographics fromMap(Map<?, ?> map) {
        String ageRange = (String) map.get("ageRange");
        Gender gender = Gender.valueOf((String) map.get("gender"));
        String incomeLevel = (String) map.get("incomeLevel");
        String educationLevel = (String) map.get("educationLevel");
        String employmentStatus = (String) map.get("employmentStatus");
        String maritalStatus = (String) map.get("maritalStatus");
        return new Demographics(gender, ageRange, incomeLevel, educationLevel, employmentStatus, maritalStatus);
    }

}
