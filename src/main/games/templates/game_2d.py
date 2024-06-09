import numpy as np
from typing import Any
from copy import deepcopy
from abc import ABC, abstractmethod

class Move(ABC):
    pass

class Square(ABC):
    def __init__(self, row: int, col: int):
        self.r = row
        self.c = col

    @abstractmethod
    def free(self) -> bool:
        raise NotImplementedError
    
    def pos(self, down=0, right=0) -> tuple:
        return (self.r+down, self.c+right)

    def __eq__(self, value: object) -> bool:
        return isinstance(value, Square) and self.r == value.r and self.c == value.c


class Game2D(ABC):
    def __init__(self, rows: int, cols: int, first_turn_agent, SquareClass):
        self.board = [
            [SquareClass(r, c) for c in range(cols)] for r in range(rows)
        ]
        self.rows = rows
        self.cols = cols
        self.first_turn_agent = first_turn_agent

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
    @abstractmethod
    def game_over(self) -> tuple[bool, Any]: # must return game over status and winner
        raise NotImplementedError
    
    @abstractmethod
    def active_agent(self):
        raise NotImplementedError
    
    @abstractmethod
    def next_agent(self):
        raise NotImplementedError
    
    @abstractmethod
    def move(self, agent, move):
        raise NotImplementedError
    
    @abstractmethod
    def move_allowed(self, agent, move) -> bool:
        raise NotImplementedError
    
    @abstractmethod
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
