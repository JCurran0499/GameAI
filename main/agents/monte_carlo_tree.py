from games.templates.game_2d import Game2D, Move

class MonteCarloTree:
    class Node:
        bot_agent = None

        def __init__(self, game: Game2D, move: Move, agent):
            self.game_state = game
            self.last_move = move
            self.active_agent = agent
            self.branches = []

            self.heuristic = 0

        def instantiate(self, depth: int):
            if len(self.branches) == 0 and (not self.game_state.game_over()):
                all_moves = self.game_state.all_moves()
                self.branches = [
                    MonteCarloTree.Node(
                        self.game_state.move(self.active_agent, move), move, self.game_state.next_agent()
                    ) for move in all_moves
                ]

            if depth > 0:
                for branch in self.branches:
                    branch.instantiate(depth - 1)

        def propagateMinMax(self):
            for branch in self.branches:
                branch.propagateMinMax()

            if len(self.branches) > 0:
                options = [n.heuristic for n in self.branches]
                if self.active_agent == self.bot_agent:
                    self.heuristic = max(options)
                else:
                    self.heuristic = min(options)
            else:
                self.heuristic = self.game_state.heuristic()

    def __init__(self, game: Game2D, first_turn_agent, bot_agent, depth: int):
        self.Node.bot_agent = bot_agent
        self.head = self.Node(game, None, first_turn_agent)

        self.head.instantiate(depth)
        self.head.propagateMinMax()

        self.depth = depth

    def move(self, node: Node):
        self.head = node
        self.head.instantiate(self.depth)
        self.head.propagateMinMax()

    def winner(self):
        return self.head.game_state.winner()
    
    def draw(self):
        return (self.head.game_state.winner() is None) and len(self.head.branches) == 0
