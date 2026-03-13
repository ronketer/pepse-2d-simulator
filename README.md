# PEPSE: Procedural 2D Environment Simulator

## Overview

PEPSE (Precise Environmental Procedural Simulator Extraordinaire) is a 2D procedural game engine built in Java using the DanoGameLab engine. This project simulates an infinite, procedurally generated world complete with physics, a dynamic day/night cycle, interactive flora, and an event-driven weather system.

## Architecture & Design Patterns

This project heavily emphasizes clean Object-Oriented Programming (OOP) principles, modularity, and established design patterns to manage a complex, infinite game state.

* **Observer Pattern (Event-Driven Architecture):** To decouple the player character from the environment, the `Avatar` class acts as a Subject. When the player performs specific actions (e.g., jumping), it broadcasts an event to registered observers. The `Cloud` module implements `AvatarObserver` to listen for these events, triggering dynamic weather changes (spawning `Raindrop` objects) without tight coupling between the player and the sky.
* **Factory Method Pattern:** The procedural generation of the environment relies on Factory methods. `Tree.create()` acts as a factory that seamlessly instantiates and assembles complex hierarchies of GameObjects (`Trunk`, `Leafs`, `Fruit`) based on a single coordinate and seed, ensuring the `Flora` manager remains decoupled from the specific implementation details of the tree parts.
* **Procedural Generation & Reproducible Randomness:**
The infinite world is dynamically loaded in chunks using Perlin Noise. To guarantee that the environment is consistent when the player walks back and forth, components like `FruitPlacer` and `Leafs` are seeded via mathematical hashes of their spatial coordinates.

## System Modules

### 🌲 Procedural Flora (`trees` package)

Generates interactive, animated vegetation dynamically based on terrain topography.

* **`Flora` & `Tree`:** Managers responsible for calculating terrain height and applying seed-based randomness to instantiate fully formed trees.
* **`Trunk`, `Leafs`, `Fruit`:** Individual components that make up a tree. Leaves feature wind animations (modifying render angles and dimensions), while fruits override standard physics collisions to allow the player to consume them for energy.
* **`FruitPlacer`:** A utility that implements probability matrixes to distribute interactive elements across the generated canopy.

### 🏃 Player Controller (`avatar` package)

Handles the main interactive entity, managing physics, input, and state.

* **`Avatar`:** The core player entity. Manages complex state logic (energy consumption/regeneration based on movement) and broadcasts state changes to environmental observers.
* **`AnimationHandler`:** A state-machine-driven utility that dynamically swaps rendering assets based on the `MovementState` enum (IDLE, JUMP, RUN).

### 🌧️ Dynamic Weather (`cloud` package)

An event-driven environmental system.

* **`Cloud`:** An autonomous entity that traverses the skybox and listens for player state changes to trigger localized weather events.
* **`Raindrop`:** Physics-enabled objects spawned dynamically via callback functions.
