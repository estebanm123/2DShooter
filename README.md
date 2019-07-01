# Submarine Shooter

This is a 2D arcade shooter built in Java.


The goal is to survive survive and to defeat the most amount of enemies possible.
At first, there's only one enemy type that randomly circulates around the screen. But, as the player kills more enemies the second type will spawn - this stalks the player until they reach them. Enemies will have a chance of dropping items which the player can use to upgrade their shooting speed and movement, as well as to replenish health. 

I built this app to excercise OOP design principles I had learned after completing a software design course. The game-loop was inspired by a demo my professor used to exemplify some of these principles. The loop mainly consists of repurposing a Java Swing timer to fire an action every few milliseconds that first updates the game mechanics' interactions, and then the graphics. 
I abstracted a general "GameObject" class, to represent any object that appears in the game (with coordinates, size, etc.), and from there I made specialized abstract classes such as "MoveableObject", "PowerUpItem", and "Enemy" which streamlined the creation of more specific sub-types. 
I used the observer design pattern mainly for the home page ui to allow easy navigation between different ui pages, but also to update the ui from certain game events, such as picking up an upgrade or pausing. This reduces coupling and pressure on computational resources, as the alternative would entail constantly checking for these updates. 
