from typing import List, Any

from main.games.templates.game_2d import Game2D, Move, Square
from main.domain.exceptions import InvalidBoardConfigurationException

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
    players = ["X", "O"]

    def __init__(self, first_turn_agent):
        super().__init__(3, 3, first_turn_agent, SquareClass=TTTSquare)
        self.last_placed: TTTSquare = None

        if self.first_turn_agent not in self.players:
            raise InvalidBoardConfigurationException("invalid starting player")

        self.player_map = {
            self.players[0]: self.players[1],
            self.players[1]: self.players[0]
        }

        self.board: List[List[TTTSquare]]

    def game_over(self) -> tuple[bool, Any]:
        if self.last_placed is None:
            return (False, None)
        
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
                    return (True, self.last_placed.agent)
                    
        return (len(self.all_moves()) == 0, None)
    
    def active_agent(self):
        if self.last_placed is None:
            return self.first_turn_agent
        else:
            return self.player_map[self.last_placed.agent]
    
    def next_agent(self):
        return self.player_map[self.active_agent()]

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
