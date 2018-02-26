Scala AI for [2048](http://gabrielecirulli.github.io/2048/) that uses the expectimax algo. It searches the game tree up to a certain depth, alternating between maximizing nodes (the player), and chance nodes (the game). It chooses the best path based on heuristics.

[Here's](https://youtu.be/Gi8k4x-b-pA) an example of a successful run. 

# Next steps
* Strip away heuristics, and have the AI pick up the strategy on it's own.

# Test
Currently, you can only run this using sbt on a mac. 

```
> sbt
> testOnly xyz.olvr.twenty.forty.eight.PlayerSpec
```

On Safari, make sure to have the following setting configured:
> Develop > Allow Remote Automation
