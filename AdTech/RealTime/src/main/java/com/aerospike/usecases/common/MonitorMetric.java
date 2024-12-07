package com.aerospike.usecases.common;

import java.util.concurrent.atomic.AtomicLong;

public abstract class MonitorMetric {
    private final String name;
    private final String description;
    
    public MonitorMetric(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static class TimingMetric extends MonitorMetric {
        private final AtomicLong totalTime = new AtomicLong();
        private final AtomicLong count = new AtomicLong();
        private long lastCount = 0;
        private long lastTime = 0;
        
        public TimingMetric(String name, String description) {
            super(name, description);
        }
        public void addTime(long timeNs) {
            totalTime.addAndGet(timeNs);
            count.incrementAndGet();
        }
        
        public String toString() {
            long totalTime = this.totalTime.get();
            long totalCount = Math.max(1, this.count.get());
            long deltaTime = totalTime - lastTime;
            long deltaCount = this.count.get() - lastCount;
            long deltaLatency = deltaCount > 0 ? deltaTime / deltaCount : 0;
            this.lastCount = totalCount;
            this.lastTime = totalTime;
            return String.format("Avg latency: %,dus, iterations: %,d, last latency: %,dus, last iterations: %,d", 
                    (totalTime/1_000) / totalCount, totalCount,
                    deltaLatency/1_000, deltaCount);
            
        }
    }
}
