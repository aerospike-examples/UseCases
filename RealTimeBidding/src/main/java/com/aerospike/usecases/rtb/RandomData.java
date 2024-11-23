package com.aerospike.usecases.rtb;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.aerospike.usecases.rtb.model.Campaign;
import com.aerospike.usecases.rtb.model.Gender;
import com.aerospike.usecases.rtb.model.Lineitem;
import com.aerospike.usecases.rtb.model.LineitemStatus;
import com.aerospike.usecases.rtb.model.Location;
import java.util.UUID;

public class RandomData {
    static List<Location> locations = generateLocations();

    private static List<Location> generateLocations() {
        List<Location> locations = new ArrayList<>();

        locations.add(new Location("United States", "US", "California", "Los Angeles", "90001", 34.0522, -118.2437));
        locations.add(new Location("Canada", "CA", "Ontario", "Toronto", "M5H", 43.65107, -79.347015));
        locations.add(new Location("United Kingdom", "GB", "England", "London", "EC1A", 51.5074, -0.1278));
        locations.add(new Location("Germany", "DE", "Berlin", "Berlin", "10117", 52.5200, 13.4050));
        locations.add(new Location("France", "FR", "Île-de-France", "Paris", "75001", 48.8566, 2.3522));
        locations.add(new Location("Japan", "JP", "Tokyo", "Tokyo", "100-0001", 35.6895, 139.6917));
        locations.add(new Location("Australia", "AU", "New South Wales", "Sydney", "2000", -33.8688, 151.2093));
        locations.add(new Location("Brazil", "BR", "São Paulo", "São Paulo", "01000-000", -23.5505, -46.6333));
        locations.add(new Location("India", "IN", "Maharashtra", "Mumbai", "400001", 19.0760, 72.8777));
        locations.add(new Location("China", "CN", "Beijing", "Beijing", "100000", 39.9042, 116.4074));
        locations.add(new Location("South Africa", "ZA", "Gauteng", "Johannesburg", "2000", -26.2041, 28.0473));
        locations.add(new Location("Russia", "RU", "Moscow", "Moscow", "101000", 55.7558, 37.6176));
        locations.add(new Location("Mexico", "MX", "Mexico City", "Mexico City", "06000", 19.4326, -99.1332));
        locations.add(new Location("South Korea", "KR", "Seoul", "Seoul", "100-011", 37.5665, 126.9780));
        locations.add(new Location("Italy", "IT", "Lazio", "Rome", "00118", 41.9028, 12.4964));
        locations.add(new Location("Spain", "ES", "Madrid", "Madrid", "28001", 40.4168, -3.7038));
        locations.add(new Location("Netherlands", "NL", "North Holland", "Amsterdam", "1012", 52.3676, 4.9041));
        locations.add(new Location("Singapore", "SG", "Singapore", "Singapore", "179431", 1.3521, 103.8198));
        locations.add(new Location("Sweden", "SE", "Stockholm", "Stockholm", "111 29", 59.3293, 18.0686));
        locations.add(new Location("Switzerland", "CH", "Zurich", "Zurich", "8001", 47.3769, 8.5417));
        locations.add(new Location("United Arab Emirates", "AE", "Dubai", "Dubai", "00000", 25.276987, 55.296249));
        locations.add(new Location("Saudi Arabia", "SA", "Riyadh", "Riyadh", "11564", 24.7136, 46.6753));
        locations.add(new Location("Turkey", "TR", "Istanbul", "Istanbul", "34110", 41.0082, 28.9784));
        locations.add(new Location("Poland", "PL", "Masovian Voivodeship", "Warsaw", "00-001", 52.2297, 21.0122));
        locations.add(new Location("Argentina", "AR", "Buenos Aires", "Buenos Aires", "C1002", -34.6037, -58.3816));
        locations.add(new Location("Thailand", "TH", "Bangkok", "Bangkok", "10200", 13.7563, 100.5018));
        locations.add(new Location("Vietnam", "VN", "Hanoi", "Hanoi", "100000", 21.0278, 105.8342));
        locations.add(new Location("Indonesia", "ID", "Jakarta", "Jakarta", "10110", -6.2088, 106.8456));
        locations.add(new Location("Malaysia", "MY", "Kuala Lumpur", "Kuala Lumpur", "50088", 3.1390, 101.6869));
        locations.add(new Location("Philippines", "PH", "National Capital Region, Metro Manila, Quezon City",
                "Quezon City", "1100", 14.6760, 121.0437));
        locations.add(new Location("Egypt", "EG", "Cairo", "Cairo", "11511", 30.0444, 31.2357));
        locations.add(new Location("Nigeria", "NG", "Lagos", "Lagos", "100001", 6.5244, 3.3792));
        locations.add(new Location("Kenya", "KE", "Nairobi", "Nairobi", "00100", -1.2921, 36.8219));
        locations.add(new Location("Ghana", "GH", "Greater Accra", "Accra", "00233", 5.6037, -0.1870));
        locations.add(new Location("Morocco", "MA", "Casablanca-Settat", "Casablanca", "20250", 33.5731, -7.5898));
        locations.add(new Location("Tunisia", "TN", "Tunis", "Tunis", "1000", 36.8065, 10.1815));
        locations.add(new Location("Algeria", "DZ", "Algiers", "Algiers", "16000", 36.7372, 3.0408));
        locations.add(new Location("Peru", "PE", "Lima", "Lima", "15001", -12.0464, -77.0428));
        locations.add(
                new Location("Chile", "CL", "Santiago Metropolitan Region", "Santiago", "8320000", -33.4489, -70.6693));
        locations.add(new Location("Colombia", "CO", "Bogota", "Bogota", "111711", 4.7109, -74.0721));
        locations.add(new Location("Venezuela", "VE", "Caracas", "Caracas", "1010", 10.4806, -66.9036));
        locations.add(new Location("Ecuador", "EC", "Pichincha", "Quito", "170136", -0.1807, -78.4678));
        locations.add(new Location("Uruguay", "UY", "Montevideo", "Montevideo", "11000", -34.9011, -56.1645));
        locations.add(new Location("Paraguay", "PY", "Asuncion", "Asuncion", "1001", -25.2637, -57.5759));
        locations.add(new Location("Bolivia", "BO", "La Paz", "La Paz", "0000", -16.2902, -63.5887));
        locations.add(new Location("Panama", "PA", "Panama", "Panama City", "00000", 8.9824, -79.5199));
        locations.add(new Location("Costa Rica", "CR", "San Jose", "San Jose", "10101", 9.9281, -84.0907));
        locations.add(new Location("El Salvador", "SV", "San Salvador", "San Salvador", "01101", 13.6929, -89.2182));
        locations.add(new Location("Honduras", "HN", "Francisco Morazan", "Tegucigalpa", "11101", 14.0723, -87.1921));
        locations.add(new Location("Nicaragua", "NI", "Managua", "Managua", "11001", 12.1149, -86.2362));
        locations.add(new Location("Guatemala", "GT", "Guatemala", "Guatemala City", "01001", 14.6349, -90.5069));
        locations.add(
                new Location("Dominican Republic", "DO", "Santo Domingo", "Santo Domingo", "10101", 18.4861, -69.9312));
        locations.add(new Location("Puerto Rico", "PR", "San Juan", "San Juan", "00901", 18.4655, -66.1057));
        locations.add(new Location("Jamaica", "JM", "Kingston", "Kingston", "00000", 18.0179, -76.8099));
        locations.add(new Location("Trinidad and Tobago", "TT", "Port of Spain", "Port of Spain", "00000", 10.6918,
                -61.2225));
        locations.add(new Location("Barbados", "BB", "Saint Michael", "Bridgetown", "00000", 13.1132, -59.5988));
        locations.add(new Location("Bahamas", "BS", "New Providence", "Nassau", "00000", 25.0343, -77.3963));
        locations.add(new Location("Cuba", "CU", "Havana", "Havana", "10100", 23.1136, -82.3666));
        locations.add(new Location("Haiti", "HT", "Ouest", "Port-au-Prince", "6110", 18.5944, -72.3074));
        locations.add(new Location("Guyana", "GY", "Demerara-Mahaica", "Georgetown", "00000", 6.8013, -58.1551));
        locations.add(new Location("Suriname", "SR", "Paramaribo", "Paramaribo", "00000", 5.8520, -55.2038));
        locations.add(new Location("French Guiana", "GF", "Cayenne", "Cayenne", "97300", 4.9224, -52.3135));
        locations.add(new Location("Guadeloupe", "GP", "Basse-Terre", "Basse-Terre", "97100", 16.0444, -61.7333));
        locations.add(new Location("Martinique", "MQ", "Fort-de-France", "Fort-de-France", "97200", 14.6161, -61.0586));

        return locations;

    }

    public static String randomIncomeLevel() {
        String[] incomeLevels = { "$10,000-$14,999", "$15,000-$19,999", "$20000 - $39999", "$40000 - $49999",
                "$50000 - $74999", "$75000 - $99999", "$100000 - $149999", "$150,000-$174,999", "$175,000-$199,999",
                "$200,000-$249,999", "$250,000+" };
        Random random = new Random();
        int index = random.nextInt(incomeLevels.length);
        return incomeLevels[index];
    }

    public static int randomAge() {
        return ThreadLocalRandom.current().nextInt(21, 76);
    }

    public static String randomEducationLevel() {
        String[] educationLevels = { "High School", "Some College", "Associates Degree", "Bachelors Degree",
                "Masters Degree", "Doctorate" };
        Random random = new Random();
        int index = random.nextInt(educationLevels.length);
        return educationLevels[index];
    }

    public static String randomEmploymentStatus() {
        String[] employmentStatuses = { "Employed", "Unemployed", "Retired", "Student", "Homemaker" };
        Random random = new Random();
        int index = random.nextInt(employmentStatuses.length);
        return employmentStatuses[index];
    }

    public static String randomMaritalStatus() {
        String[] maritalStatuses = { "Single", "Married", "Divorced", "Widowed" };
        Random random = new Random();
        int index = random.nextInt(maritalStatuses.length);
        return maritalStatuses[index];
    }

    public static Gender randomGender() {
        Gender[] genders = Gender.values();
        Random random = new Random();
        int index = random.nextInt(genders.length);
        return genders[index];
    }

    public static Location randomLocation() {
        int index = ThreadLocalRandom.current().nextInt(locations.size());
        return locations.get(index);
    }

    public static Campaign randomCampaign() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startDate = today.minusDays(50);
        LocalDateTime endDate = today.plusDays(365);

        int budget = ThreadLocalRandom.current().nextInt(500, 5001);
        String id = UUID.randomUUID().toString();
        String advertiserId = UUID.randomUUID().toString();
        String name = "Campaign " + ThreadLocalRandom.current().nextInt(1, 101);
        return new Campaign(id, name, advertiserId, startDate, endDate, budget);
    }

    public static List<Lineitem> generateLineitems(int lineitemCount, Campaign campaign) {
        List<Lineitem> lineitems = new ArrayList<>();
        for (int i = 0; i < lineitemCount; i++) {
            LocalDateTime startDate = randomDateBetween(campaign.getStartDate(), campaign.getEndDate().minusDays(30));
            LocalDateTime endDate = startDate.plusDays(30).isBefore(campaign.getEndDate()) ? startDate.plusDays(30)
                    : campaign.getEndDate();
            String id = UUID.randomUUID().toString();
            String campaignId = campaign.getId();
            String name = campaign.getName() + " - Lineitem " + id;
            int budget = ThreadLocalRandom.current().nextInt(20, 100);
            Lineitem lineitem = new Lineitem(id, campaignId, name, startDate, endDate, budget);
            LineitemStatus status = LineitemStatus.values()[ThreadLocalRandom.current()
                    .nextInt(LineitemStatus.values().length)];
            lineitem.setStatus(status);
            lineitems.add(lineitem);
        }
        return lineitems;
    }

    private static LocalDateTime randomDateBetween(LocalDateTime startInclusive, LocalDateTime endExclusive) {
        long startEpoch = startInclusive.atZone(ZoneId.systemDefault()).toEpochSecond();
        long endEpoch = endExclusive.atZone(ZoneId.systemDefault()).toEpochSecond();
        long randomEpoch = ThreadLocalRandom.current().nextLong(startEpoch, endEpoch);
        return LocalDateTime.ofEpochSecond(randomEpoch, 0,
                ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now()));
    }
}
