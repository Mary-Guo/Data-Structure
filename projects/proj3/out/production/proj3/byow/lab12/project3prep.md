# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer:

My way is to look at the sides of hexagons. First, add a hexagon to the center of the graph. There are six sides of this hexagon right now:
topLeft, top, topRight, bottomRight, bottom and bottomLeft. All of them are not connecting to other hexagons. Then I add one hexagon to each
of the free sides. As the new hexagon added, there are some sides that are already connecting to other hexagons, but there still a bunch of 
sides at the outside of drawing. Again add one hexagon to every free side that is not connecting with other hexagons. Then I get the goal 
graph.

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer:

Yes. For the hexagons, each time I generate a new hexagon one the all free sides of the drawing that have enough space. The hexagons that
already exist are the "hallways" and "room" that I can generate a new walkable space (hexagon) on free side if no room or hallways has been 
created next to this side, and the new generated space become a part of the hallway. 

**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer:

I'm thinking to create a world that all the space are unused first, then add a rectangle in a random position as a walkable space.
I also need to create walls around this hallway. Then I can randomly add new hallways into the graph, and rebuild the wall each time.

**What distinguishes a hallway from a room? How are they similar?**

Answer:

A hallway is a narrow tunnel with a width of one, a room is a rectangular with sides larger than one.
