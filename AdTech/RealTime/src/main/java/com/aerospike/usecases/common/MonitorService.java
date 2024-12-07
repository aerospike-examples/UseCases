package com.aerospike.usecases.common;

import java.util.ArrayList;
import java.util.List;

public class MonitorService {
    private List<MonitorMetric> metrics;
    private Thread monitorThread;
    private volatile boolean terminate = false;
    private volatile boolean hasTerminated = false;
    private volatile long startTime;
    
    public MonitorService(MonitorMetric ...metrics) {
        this.metrics = new ArrayList<>();
        for (MonitorMetric thisMetric : metrics) {
            this.metrics.add(thisMetric);
        }
    }
    
    public synchronized void addMetric(MonitorMetric metric) {
        this.metrics.add(metric);
    }
    
    private String getMetrics() {
        StringBuilder sb = new StringBuilder();
        long timeInNs = System.nanoTime() - this.startTime;
        sb.append(String.format("%,5dms: ", timeInNs / 1_000_000));
        for (MonitorMetric metric : metrics) {
            sb.append(metric).append(' ');
        }
        return sb.toString();
    }
    
    public void startMonitoring() {
        this.monitorThread = new Thread(() -> {
            while (!terminate) {
                System.out.println(this.getMetrics());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                    break;
                }
            }
            hasTerminated = true;
        });
        this.monitorThread.setName("Monitor Thread");
        this.monitorThread.setDaemon(true);
        this.startTime = System.nanoTime();
        this.monitorThread.start();
    }
    
    public void endMonitoring() {
        if (this.monitorThread != null) {
            this.terminate = true;
        }
        while (!hasTerminated) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) {
            }
        }
        System.out.println("Run complete:");
        System.out.printf("\t%s\n", getMetrics());
    }
}
