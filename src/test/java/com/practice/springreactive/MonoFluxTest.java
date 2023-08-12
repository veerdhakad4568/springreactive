package com.practice.springreactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Timed;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.context.Context;
import reactor.util.function.Tuple2;

import java.lang.management.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MonoFluxTest {
    @Test
     public void fluxTest(){
         Flux<String> fluxOfStrings=Flux.just("Spring","Spring Boot","Reactive Programming");
         StepVerifier.create(fluxOfStrings).expectNext("Spring").expectNext("Spring Boot").expectNext("Reactive Programming").verifyComplete();
     }
    @Test
    public void fluxTestCountElement(){
        Flux<String> fluxOfStrings=Flux.just("Spring","Spring Boot","Reactive Programming");
        StepVerifier.create(fluxOfStrings).expectNextCount(1).expectNext("Spring Boot").expectNextCount(1).verifyComplete();

    }

    @Test
    public void fluxTestDelay(){
        List<String> stringList=new ArrayList<String>();
        stringList.add("Spring");
        stringList.add("Spring Boot");
        stringList.add("Spring");

        Flux<String> fluxOfStrings = Flux.fromIterable(stringList).map(this::capitalizeString);
        fluxOfStrings.subscribeOn(Schedulers.parallel()).subscribe(System.out::println);
        fluxOfStrings.blockLast();
    }
    @Test
    public void fluxTestDelay1(){
        List<String> stringList=new ArrayList<String>();
        stringList.add("Spring");
        stringList.add("Spring Boot");
        stringList.add("Spring");

       /* Flux<String> fluxOfStrings = Flux.fromIterable(stringList)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(this::capitalizeString)
                .sequential();*/

       /* fluxOfStrings.subscribe(System.out::println);
        fluxOfStrings.blockLast();*/
    }

    public String capitalizeString(String inputMono) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return inputMono.toUpperCase(Locale.ROOT);
    }


    @Test
    public void monoTest() {
        Mono<String> stringMono = Mono.just("Spring");

        stringMono
                .map(this::capitalizeString)
                .timed()
                .doOnNext(tuple -> {
                    Duration duration = tuple.elapsed();
                    System.out.println("Time taken by capitalizeString: " + duration.toMillis() + "ms");
                })
                .map(Timed::get)
                .subscribe(System.out::println);

        stringMono.block();


    }

    @Test
    void test(){
        // Get the operating system MXBean
        OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
        // Get the thread MXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // Get the memory MXBean
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        // Get the heap memory usage
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        // Get the runtime object
        Runtime runtime = Runtime.getRuntime();
        // Get the total memory available to the JVM
        long totalMemory = runtime.totalMemory();
        // Get the maximum memory available to the JVM
        long maxMemory = runtime.maxMemory();
        // Get the system load averages
        double systemLoadAverage = osMXBean.getSystemLoadAverage();

        // Print CPU usage{
            System.out.println("CPU usage (1 minute): " + systemLoadAverage);
       //print memory usage
        System.out.println("Memory usage: " + heapMemoryUsage.getUsed() / 1024 / 1024 + " MB");
        // Print total system memory
        System.out.println("Total system memory: " + runtime.totalMemory() / 1024 / 1024 + " MB");
    }
}
