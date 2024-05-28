import random
from main.agents.monte_carlo_tree import MonteCarloTree
from main.games.templates.game_2d import Move

class Agent:
    def __init__(self, id: int, tree: MonteCarloTree):
        self.id = id
        self.tree = tree

    def take_turn(self):
        raise NotImplementedError

# Performs a random walk against you
# This is the dumbest agent
class Walker(Agent):
    def take_turn(self) -> Move:
        choice = random.choice(self.tree.head.branches)
        self.tree.move(choice)

        return choice.last_move
    