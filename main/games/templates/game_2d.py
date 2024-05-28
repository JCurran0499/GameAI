import numpy as np
from copy import deepcopy

class Move:
    pass

class Square:
    def __init__(self, row: int, col: int):
        self.r = row
        self.c = col

    def free(self) -> bool:
        raise NotImplementedError
    
    def pos(self, down=0, right=0) -> tuple:
        return (self.r+down, self.c+right)

    def __eq__(self, value: object) -> bool:
        return isinstance(value, Square) and self.r == value.r and self.c == value.c


class Game2D:
    def __init__(self, rows: int, cols: int, SquareClass):
        self.board = [
            [SquareClass(r, c) for c in range(cols)] for r in range(rows)
        ]
        self.rows = rows
        self.cols = cols

    def copy(self, **kwargs):
        new_board = deepcopy(self.board)
        new_game = self.__class__(**kwargs)
        new_game.board = new_board

        return new_game

    def get(self, r: int, c: int) -> Square:
        return self.board[r, c]
    
    def is_out_of_bounds(self, r: int, c: int) -> bool:
        return (r < 0 or r >= self.rows) or (c < 0 or c >= self.cols)

    # -- Abstract Methods -- #
    def game_over(self) -> bool:
        raise NotImplementedError
    
    def winner(self): # can assume is called after game_over()
        raise NotImplementedError
    
    def draw(self) -> bool: # can assume is called after game_over()
        raise NotImplementedError
    
    def active_agent(self):
        raise NotImplementedError
    
    def next_agent(self):
        raise NotImplementedError
    
    def move(self, agent, move):
        raise NotImplementedError
    
    def move_allowed(self, agent, move) -> bool:
        raise NotImplementedError
    
    def all_moves(self) -> list[Move]:
        raise NotImplementedError
    
    # -- Optional Override -- #
    def heuristic(self, agent):
        if self.game_over() and (not self.draw()):
            if self.winner() == agent:
                return np.inf
            else:
                return -np.inf
            
        return 0
