# Akka Streams Playground

This repository contains exercises resolved by me during the course, it is ignoring all copyrighted content from the course, so it is not runnable.This repository contains exercises resolved by me during the course, it ignores all copyrighted content from the course, and is not runnable.

## Fusion Run comparison

| Name                                | Time to complete the order ms |
|-------------------------------------|-------------------------------|
| 0ms                                 | 949                           |
| 16ms run                            | 15469?                        |
| 14ms (async paint)                  | 13579                         |
| 12ms (async paint & install engine) | 11485                         |
| 14ms tail run                       | 14159                         |
| 10 ms 2 asyncs run                  | 10113                         |

> - To improve performance in our stream we need to take the longest fused portion and insert an asynchronous boundary.
> - We insert an async boundary to bisect the stream into two subsections of roughly equal processing time.
> - Where this is not feasible, we try to get as close as we can.
> - Once we have bisected the stream into two subsections, try to further bisect the new longest fused section.
> - Repeat this process until the longest section can't be broken down or bisected further.
>
> **Note**:  You are always limited by the processing time of the longest fused section. So if one section takes 10 ms, then partitioning a section that only takes 4 ms won't provide significant benefit.
> 
> **Note**:  For lower throughput streams, the overhead of an asynchronous boundary may be insignificant. In this case, putting asynchronous boundaries everywhere may not hurt (but also may not help). However, for high throughput streams, the overhead may exceed the benefit and could actually slow down your stream.
> 
> **Best Practice**: Understand the performance of your stream and measure the impact of your asynchronous boundaries.

TLDR; Try to split the stream in sections with maximum time of each one  is closest to the longest stage

## Telemetry (white box) Run comparison

| Name             | Throughput |
|------------------|------------|
| 16ms             | 65         |
| 12ms (one async) | 88         |
| 10ms (two async) | 100        |

![Telemetry Comparison](./resources/img/telemetry-comparison.png)

## Telemetry (black box) Run comparison

### Exercise 1

| Name               | Throughput |
|--------------------|------------|
| Baseline           | 81         |
| 1 async boundaries | 140        |
| 2 async boundaries | 186        |

![Telemetry Comparison Exercise 1](./resources/img/telemetry-comparison-ex1.png)

### Exercise 2

| Name               | Throughput |
|--------------------|------------|
| Baseline           | 56         |
| 1 async boundaries | 100        |
| 2 async boundaries | 130        |
| 3 async boundaries | 165        |

![Telemetry Comparison Exercise 2](./resources/img/telemetry-comparison-ex2.png)
