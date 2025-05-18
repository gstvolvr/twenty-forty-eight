Scala AI for [2048](http://gabrielecirulli.github.io/2048/) that uses the [expectimax](https://www.geeksforgeeks.org/expectimax-algorithm-in-game-theory/) algo. It searches the game tree up to a certain depth (2 or 3), alternating between maximizing nodes (the player), and chance nodes (the game). It chooses the path that maximizes the expected value of a weighted objective function that tries to do two things:
1. Place the board into a snake like pattern where the head is at the
   bottom right
2. Minimize the number of open board positions

[Here's](https://youtu.be/Gi8k4x-b-pA) an example of a successful run.

# Accuracy
The AI reaches 2048 about every 2 / 5 runs. It still makes some risky moves that can easily jeopardize the game – and often do.

# Running
Currently, you can only run this using sbt on a mac.

```
> sbt
> testOnly xyz.olvr.twenty.forty.eight.PlayerSpec
```

On Safari, make sure to have the following setting configured:
> Develop > Allow Remote Automation

# Note
I haven't updated this in a long time, and its still running on Java 17. 
