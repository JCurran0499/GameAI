import pytest

from main.games.tic_tac_toe import TicTacToe

@pytest.fixture
def new_board():
    return TicTacToe("X")

def test_clean_board_agents(new_board):
    assert new_board.active_agent() == "X"
    assert new_board.next_agent() == "O"

