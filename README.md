# TSP - Traveling Salesman Problem Solver (Java)

## Overview
This Java-based project aims to solve the Traveling Salesman Problem (TSP) using a parallelized approach with multithreading and the Partially Mapped Crossover (PMX) technique. The TSP is a classic optimization problem where the goal is to find the most efficient route that visits a set of cities exactly once and returns to the starting city.

![TSP](./assets/tsp.jpg)

## Features
### 1 - Multithreading
The solution utilizes multithreading to concurrently explore different paths in the solution space, improving the overall efficiency of the algorithm. This enables the program to explore multiple potential solutions simultaneously, leading to faster convergence towards an optimal or near-optimal solution.

### 2 - PMX Crossover
The genetic algorithm incorporates the PMX crossover technique to create diverse offspring. PMX ensures that the child solutions inherit parts of their parents' paths, preserving the integrity of the route while introducing variability. This enhances the algorithm's ability to explore and converge towards optimal solutions.

### 3 - Mutation Probability
The genetic algorithm incorporates a mutation mechanism with adjustable probability. Mutation introduces diversity in the population by randomly altering some solutions, preventing premature convergence to suboptimal solutions. Users can fine-tune the mutation probability to strike a balance between exploration and exploitation.

### 4 - Population
The genetic algorithm maintains a population of potential solutions, evolving them over generations. A diverse population helps the algorithm explore a broader solution space. Users can configure the size of the population based on the characteristics of the TSP instance, allowing for flexibility in handling different problem complexities.

### 5 - Max Time of Execution
To control the execution time of the algorithm, a maximum time parameter is provided. This ensures that the algorithm terminates gracefully even if an optimal solution is not found within a specified timeframe. Users can set this parameter to meet specific time constraints, making the solution adaptable to different scenarios.

## Why to Open Source 🌐

1. **Community Collaboration**: Open sourcing your project invites a global community of developers to contribute, share ideas, and enhance its capabilities.

2. **Accelerated Development**: With a collective effort, open source projects progress faster, benefiting from diverse skill sets and constant feedback.

3. **Transparency and Trust**: Open source builds trust by allowing users to inspect code, ensuring security and transparency in software.

4. **Cost Efficiency**: It reduces development costs as contributors volunteer their time and resources, making it an economical choice.

5. **Longevity**: Open source projects tend to have longer lifespans, as they are not reliant on a single entity, ensuring sustainability.

## Installation 📥

> Install TSP Repository with npm *(This will install all files, including this README.md)*
>
> ```bash
>  npm install https://github.com/MiguelRolo/TSP.git
>  cd TSP
> ```

## License 🪪

> **(Apache License, Version 2.0)** You're **free to use** this content and codes in any project, personal or commercial. 
>
> There's no need to ask permission before using theses. Giving attribution is not required, but appreciated.