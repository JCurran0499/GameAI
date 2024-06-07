from games.templates.game_2d import Game2D, Move, Square
from typing import List

class TTTMove(Move):
    def __init__(self, row: int, col: int):
        self.r = row
        self.c = col

class TTTSquare(Square):
    def __init__(self, row: int, col: int):
        super().__init__(row, col)
        self.agent = None

    def free(self) -> bool:
        return self.agent is None



class TicTacToe(Game2D):
    def __init__(self):
        super().__init__(3, 3, SquareClass=TTTSquare)
        self.last_placed: TTTSquare = None
        self.players = ["X", "O"]
        self.winning_player = None

        self.board: List[List[TTTSquare]]

    def game_over(self) -> bool:
        if self.last_placed is None:
            return False
        
        for line in [
            [(-2, -2), (-1, -1), (1, 1), (2, 2)],
            [(2, -2), (1, -1), (-1, 1), (-2, 2)],
            [(-2, 0), (-1, 0), (1, 0), (2, 0)],
            [(0, -2), (0, -1), (0, 1), (0, 2)]
        ]:
            count = 0
            for shift_down, shift_right in line:
                sq_pos = self.last_placed.pos(down=shift_down, right=shift_right)
                if (not self.is_out_of_bounds(*sq_pos)) and (self.get(*sq_pos).agent == self.last_placed.agent):
                    count += 1

                if count >= 2:
                    self.winning_player = self.last_placed.agent
                    return True
                    
        return len(self.all_moves()) == 0

    def winner(self):
        return self.winning_player
        
    def draw(self) -> bool:
        return (self.winning_player is None) and (len(self.all_moves()) == 0)
    
    def active_agent(self):
        opponent_map = {
            self.players[0]: self.players[1],
            self.players[1]: self.players[0]
        }
        return opponent_map[self.last_placed.agent]
    
    def next_agent(self):
        return self.last_placed.agent

    def move(self, agent, move: TTTMove):
        if self.move_allowed(agent, move):
            new_game = self.copy()
            new_game.board[move.r][move.c].agent = agent

            self.last_placed = new_game.board[move.r][move.c]
            return new_game
        
        raise RuntimeError("this move is invalid")
    
    def move_allowed(self, agent, move: TTTMove) -> bool:
        if not isinstance(move, TTTMove):
            raise TypeError(f"move argument must be of type {TTTMove}")
        
        r, c = move.r, move.c
        return (agent in self.players) \
            and (agent == self.active_agent()) \
            and (not self.is_out_of_bounds(r, c)) \
            and (self.board[r][c].free())
    
    def all_moves(self) -> List[Move]:
        moves = []
        for r in range(self.rows):
            for c in range(self.cols):
                if self.board[r][c].free():
                    moves.append(TTTMove(r, c))

        return moves
