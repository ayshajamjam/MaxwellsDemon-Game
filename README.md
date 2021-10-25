# Programming Assignment 5: Maxwell's Demon

## Goals

In order to complete this assignment, you will need to:
- Understand how to design a graphical user interface
  - Design the look of a graphical interface
  - Implement your design using Swing containers and components
- Understand how to model a simple physical system
  - Choose appropriate data model and storage strategy for your game
  - Understand how to use the model to create your program view
- Understand how to use event-driven programming to update your game
  - Choose appropriate event generators like buttons or timers
  - Create corresponding event listeners to respond to these events
  - Properly update your model in response to events

## Description

The 19th century mathematical physicist, James Maxwell, is best-known for developing a set of four equations (known as Maxwell's equations) that describe classical electormagnetism. He also made contributions to statistical thermodynamics (producing the Maxwell-Boltzmann distribution for ideal gas behavior); during this work, he conceived of a thought experiment in which the second law of thermodynamics could be violated.

> ... if we conceive of a being whose faculties are so sharpened that he can follow every molecule in its course, such as a being, whose attributes are as essentially finite as our own, would be able to do what is impossible to us. For we have seen that molecules in a vessel full of air at uniform temperature are moving with velocities by no means uniform, though the mean velocity of any great number of them, arbitrarily selected, is almost exactly uniform. Now let us suppose that such a vessel is divided into two portions, A and B, by a division in which there is a small hole, and that a being, who can see the individual molecules, opens and closes this hole, so as to allow only the swifter molecules to pass from A to B, and only the slower molecules to pass from B to A. He will thus, without expenditure of work, raise the temperature of B and lower that of A, in contradicition to the second law of thermodynamics.
- Bennet, Charles H. (November 1987). "Demons, Engines, and the Second Law" (PDF). Scientific American. 257(5): 108-116

In other words, Maxwell envisioned a box split into two halves, with a door between the parts that could be opened or closed by a "demon" who observed every particle in the box. By carefully opening and closing the door at the correct moments, the demon could place all the fast-moving (i.e., hot) particles on one side of the door and all the slow-moving (i.e., cold) particles on the other side, reducing the entropy of the system.

## Specification and requirements

### Specification

In this assignment you will create a Java program that lets the user play as Maxwell's demon. You will create a GUI for the program, an appropriate data model to store the state of the game, and controllers to allow the game to update over time and in response to the user.

You are free to add additional features beyond those described below (e.g., high scores, play timers, awesome graphics) so long as they do not break the core functionality required.

#### The Maxwell's Demon Interface

Your game will start by displaying a user interface with the following components:
- A wide panel "playing area" divided in half by a line/wall down the middle (to create two chambers).
  - A collection of color-coded particles (red for hot, fast-moving particles and blue for cold, slow-moving particles).
  - A mouse-responsive "door" in the wall between the two chambers.
    - When the user clicks and holds ("presses") the mouse button, the door should open so particles can move between the two chambers.
    - When the user releases the mouse button, the door should close.
- A button which, when clicked, creates one red particle and one blue particle on each side of the play area (adding four total particles to the game).
- A button which, when clicked, resets the game to its starting state (as if the game had just begun).
- A visual display of the "temperature" of each of the two chambers.

#### Tracking Individual Particles

Throughout the execution of your game, you will need to keep track of all the particles currently "present" in the game, and understand how to draw them on screen and update their positions over time. This will require tracking
- The position and velocity of each particle
- The temperature/color of the particle (to draw it)

For the purpose of this assignment, let 
- "fast" denote particles moving between 4 and 6 centimeters per second
- "slow" denote speeds between 2 and 4 centimeters per second
 
(This is more manageable than the more realistic 200 meters per second that the median particle is moving in your room right now.) These values will permit transitions to screens with different resolutions with reasonable behavior.

#### Computing the Average Temperature

You will also need to compute/track the average temperature of all the particles on each side of the game. You should compute the temperature as proportional to the average of the squares of the speed of the particles on that side of the game.
- It may be exceedingly convenient to choose (i.e., invent) physical units such that the constant of proportionality is equal to one. However, those who have an interest in the numerical simulation of physical systems may find more information [in this Wikipedia article](http://en.wikipedia.org/wiki/Kinetic_theory_of_gases#Temperature_and_kinetic_energy).

#### A Helpful Method for Determining Screen Resolution

As the Java2D Graphics object works in pixel-based coordinates, speeds measured in centimeters per second will need to be converted to pixels per second (or, possibly, pixels per frame or drawing update). The class `java.awt.Toolkit` provides a static method that will help with this conversion:
```
// Returns the pixels per inch of the current display
int resolution = Toolkit.getDefaultToolkit().getScreenResolution();
```

### Requirements

You will create the following design document files and submit them via Canvas:
- `GUIDesign.pdf`, a sketch of the appearance of your game. You should include all of the required components of the interface. This may be done in any medium you like (pencil, crayon, oil paint on canvas) provided you can photograph it legibly and upload it as a PDF.
- `ObjectDiagram.pdf`, a UML object diagram describing the state of your program at the beginning of the game (recall that an object diagram is like a class diagram, but describes all instances of classes rather than only their abstract relationships). Your diagram should include
  - All data objects/structures which manage the state of the game
  - All GUI elements (Swing containers/components) and their heirarchy/relationships
  - All event handlers and their relationships to the objects/events they subscribe to.
In addition, to the usual UML annotations for these relationships, include also a notation (with key, as appropriate) of which objects are part of the Model, which are part of the View, and which are part of the Controller (if an object fills more than one role, denote that as well).

These diagrams will be hand-graded by me.

You will also create the following "code-based" files to submit (by committing them to your repository).
- `Instructions.txt`, a text file describing how to play your game (what class contains main() to start, info on any non-standard features, etc.)
- All Java source code files needed to compile and play your game. There is no required name for these files; your `Instructions.txt` will tell me how to properly begin your game.
