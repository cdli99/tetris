
class MyPiece < Piece

  def self.next_piece (board)
    Piece.new(All_My_Pieces.sample, board)
  end

  def self.single_block (board)
    Piece.new([[[0,0]]], board)
  end

  All_My_Pieces = [
    [[[0, 0], [1, 0], [0, 1], [1, 1]]], # square (only needs one)
    rotations([[0, 0], [-1, 0], [1, 0], [0, -1]]), # T
    [[[0, 0], [1, 0], [2, 0], [3, 0]], [[0, 0], [0, 1], [0, 2], [0, 3]]], # long (only needs two)
    rotations([[0, 0], [0, -1], [0, 1], [1, 1]]), # L
    rotations([[0, 0], [0, -1], [0, 1], [-1, 1]]), # inverted L
    rotations([[0, 0], [-1, 0], [0, -1], [1, -1]]), # S
    rotations([[0, 0], [1, 0], [0, -1], [-1, -1]]), # Z
    rotations([[0, 0], [0, 1], [1, 0], [1, 1], [2, 1]]), # T+1
    rotations([[0, 0], [0, 1], [1, 0]]), # T-1
    [[[0, 0], [1, 0], [2, 0], [3, 0], [4, 0]], [[0, 0], [0, 1], [0, 2], [0, 3], [0, 4]]] # long+1 (only needs two)
  ]

end

class MyBoard < Board
  attr_accessor :cheat, :score

  def rotate_180_degree
    rotate_counter_clockwise
    rotate_counter_clockwise;
  end

  def try_cheat
    if score >= 100 && !self.cheat
      self.score = self.score-100
      self.cheat= true
    end
  end

  def next_piece
    if cheat
      @cheat=false
      @current_block = MyPiece.single_block(self)
    else
      @current_block = MyPiece.next_piece(self)
    end
    @current_pos = nil
  end

  def store_current
    locations = @current_block.current_rotation
    displacement = @current_block.position
    (0...locations.length).each{|index|
      current = locations[index];
      @grid[current[1]+displacement[1]][current[0]+displacement[0]] =
        @current_pos[index]
    }
    remove_filled
    @delay = [@delay - 2, 80].max
  end
end

class MyTetris < Tetris
  def set_board
    @canvas = TetrisCanvas.new
    @board = MyBoard.new(self)
    @canvas.place(@board.block_size * @board.num_rows + 3,
      @board.block_size * @board.num_columns + 6, 24, 80)
    @board.draw
  end

  def key_bindings
    super
    @root.bind('u', proc {@board.rotate_180_degree})
    @root.bind('c', proc {@board.try_cheat})
  end
end


