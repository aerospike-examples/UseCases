package com.aerospike.usecases.rtb;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.aerospike.usecases.common.MonitorMetric.TimingMetric;
import com.aerospike.client.Log;
import com.aerospike.usecases.common.MonitorService;
import com.aerospike.usecases.rtb.model.Device;
import com.aerospike.usecases.rtb.model.SegmentInstance;

public class DataPopulator {
    public static final int DAYS_TO_KEEP_SEGMENTS = 30;
    private final AtomicLong devicesInserted = new AtomicLong();
    private final AtomicLong segmentsCreated = new AtomicLong();
    private final AtomicLong deviceInsertsFailed = new AtomicLong();
    private final StorageEngine storageEngine;
    
    public DataPopulator(StorageEngine storageEngine) {
        this.storageEngine = storageEngine;
    }
    
    /**
     * Create an expiry date. Make ~20% of the dates in the past
     * @param now - the baseline time
     * @return A date object in the desired range.
     */
    private Date chooseExpiryDate(long now) {
        long millisInDayRange = TimeUnit.DAYS.toMillis(DAYS_TO_KEEP_SEGMENTS);
        long millisInPast = millisInDayRange / 4;
        long selectedOffset = ThreadLocalRandom.current().nextLong(millisInDayRange + millisInPast);
        long selectedTIme = now + selectedOffset;
        return new Date(selectedTIme);
    }
    
    /**
     * Generate the devices and segments for a range of ids. Note that in a real world scenario the ids would be systematically generated
     * like UUIDs or similar. However, for ease of simulating this process
     * @param numberOfSegments
     * @param startDevice
     * @param endDevice
     * @param avgSegmentsPerDevice
     */
    private void generateDevices(TimingMetric timer, long numberOfSegments, long startDevice, long endDevice, long avgSegmentsPerDevice) {
        if (Log.debugEnabled()) {
            Log.debug(String.format("    generateDevices(%d, %d, %d, %d)\n", 
                numberOfSegments, startDevice, endDevice, avgSegmentsPerDevice));
        }
        Random random = ThreadLocalRandom.current();
        long now = new Date().getTime();
        
        for (long thisDeviceId = startDevice; thisDeviceId < endDevice; thisDeviceId++) {
            Device device = new Device(Device.idToString(thisDeviceId));
            device.setFinished(random.nextBoolean());
            long segmentsToGenerate = Math.max(0, Math.min(1000,(long)(avgSegmentsPerDevice + (avgSegmentsPerDevice / 2) * random.nextGaussian()))); 
            for (long thisSegment = 0; thisSegment < segmentsToGenerate; thisSegment++) {
                long segmentId = Math.abs(random.nextLong()) % numberOfSegments;
                device.getSegments().add(new SegmentInstance(segmentId, chooseExpiryDate(now), 0L, "www.google.com"));
            }

            try {
                long startTime = System.nanoTime();
                this.storageEngine.saveDevice(device);
                timer.addTime(System.nanoTime() - startTime);
                this.devicesInserted.incrementAndGet();
                this.segmentsCreated.addAndGet(device.getSegments().size());
            }
            catch (Exception e) {
                e.printStackTrace();
                this.deviceInsertsFailed.incrementAndGet();
            }
        }
    }
    
    public void generateDevices(long numberOfSegments, long numberOfDevices, long avgSegmentsPerDevice, int numberOfThreads) {
        if (Log.debugEnabled()) {
            Log.debug(String.format("generateDevices(%d, %d, %d, %d)\n", 
                    numberOfSegments, numberOfDevices, avgSegmentsPerDevice, numberOfThreads));
        }
        TimingMetric timer = new TimingMetric("timer", "");
        MonitorService monitor = new MonitorService(timer);
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        long remainingDevices = numberOfDevices;
        monitor.startMonitoring();
        for (int i = 0; i < numberOfThreads; i++) {
            final long thisDevices = remainingDevices / (numberOfThreads-i);
            final long startDevice = numberOfDevices - remainingDevices;
            remainingDevices -= thisDevices;
            executor.submit(() -> {
                this.generateDevices(timer, numberOfSegments, startDevice, startDevice + thisDevices, avgSegmentsPerDevice);
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        }
        catch (InterruptedException ignored) {
            System.out.println("Ignoring InterruptedException");
        }
        monitor.endMonitoring();
        System.out.flush();
    }
}
