# Rock-Paper-Scissors-AI
A Java-based AI game in which a human player plays Rock, Paper, Scissors against an AI using probabilistic reasoning

## What is Rock, Paper, Scissors?
For the uninitiated, Rock, Paper, Scissors is an extremely simple game, popular amongst children, and for the purpose of simple decision making. The game involves players simultaneously (usually at the end of a countdown) choosing rock, paper, or scissors (usually in the form of a hand gesture). Rock beats scissors, scissors beat paper, and paper beats rock. More complex and 3+ player variants exist, but this is the most common version.

## Why Rock, Paper, Scissors?
Rock, Paper, Scissors is an extremely simple game, and unlike many games, even the "best" Rock, Paper, Scissors players are simply making good probabilistic guesses as to their opponent's plays, a strategy a computer could begin to emulate fairly easily. Rock, Paper, Scissors is a simple game with not a lot of rules or complexities, much like the simple games studied in your average introduction to artificial intelligence class.

## How Does The AI Work?
The AI is extremely simple, all things considered. The core component of the AI is what's called a [Markov Chain](https://en.wikipedia.org/wiki/Markov_chain), which is a process in which a problem is defined as a series of states which are transitioned between. A Markov Chain describes the probability of transitioning to a given state knowing the current state.

For example, consider a very simple Markov Chain containing two states modelling weather: Fair, and Miserable. A Markov Chain modelling this state space could say that, if the current state is Fair, there is a 0.4 chance of the weather remaining Fair for the next time step, and a 0.6 chance of transitioning to a Miserable state during the next time step. Similarly, if the current state is Miserable, there is a 0.2 chance of the weather staying Miserable through the next time step, and a 0.8 chance that the weather will be Fair during the next time step.

The Markov Chain utilized by this program has three states: Rock, Paper, and Scissors. Basically, using the data it collects as a human opponent plays against it, this Markov Chain models the probability of a player using any particular move, given the move that they played last turn.