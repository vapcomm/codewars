defmodule Skyscrapers4x4 do
  @moduledoc """
  4 By 4 Skyscrapers -- 4 kyu
  https://www.codewars.com/kata/5671d975d81d6c1c87000022/train/elixir
  """

  import Bitwise

  @doc """
  Pass the clues in an array of 16 items. This array contains the clues around the clock.
  Return 4x4 matrix, list of rows of ints.
  """
  def solve(clues) do
    grid = 0xFFFFFFFF &&& 0
    IO.puts("Start grid\n" <> grid_to_string(grid))

    grid = grid ||| 1
    IO.puts("One grid\n" <> grid_to_string(grid))

    all_permutations = permutations()

    # generate hints from clues


    #+++
    result = [[0, 0, 0, 0], [0, 0, 0, 0], [0, 0, 0, 0], [0, 0, 0, 0],]
    result
    #+++
  end

  # Return cell value in given grid by given row and column.
  # Every cell in grid use 3 bits from integer value, starting from lowest bits x:0, y:0
  #    C 0 1 2 3
  #  R ---------
  #  0:  1 2 3 4
  #  1:  5 6 7 8
  #  2:  9 A B C
  #  3:  D E F G
  #
  #  4         3         2         1         0
  #  765432109876543210987654321098765432109876543210
  #    G  F  E  D  C  B  A  9  8  7  6  5  4  3  2  1
  #
  defp get_cell(grid, row, column) when row in 0..3 and column in 0..3 do
    (grid >>> (row * 12 + column * 3)) &&& 7
  end

  # Convert grid in integer to string
  defp grid_to_string(grid) do
    Enum.reduce(0..3, "", fn i, sr ->
      sr <> "#{i}: " <>
      Enum.reduce(0..3, "", fn j, sc -> sc <> Integer.to_string(get_cell(grid, i, j)) <> " " end)
      <> "\n"
    end)
  end

  # Generate all permutations of 1, 2, 3, 4 and return bits-encoded values list.
  def permutations() do
    source = [
      {1, 2, 3, 4}, {2, 1, 3, 4}, {3, 1, 2, 4}, {1, 3, 2, 4}, {2, 3, 1, 4}, {3, 2, 1, 4},
      {4, 2, 3, 1}, {2, 4, 3, 1}, {3, 4, 2, 1}, {4, 3, 2, 1}, {2, 3, 4, 1}, {3, 2, 4, 1},
      {4, 1, 3, 2}, {1, 4, 3, 2}, {3, 4, 1, 2}, {4, 3, 1, 2}, {1, 3, 4, 2}, {3, 1, 4, 2},
      {4, 1, 2, 3}, {1, 4, 2, 3}, {2, 4, 1, 3}, {4, 2, 1, 3}, {1, 2, 4, 3}, {2, 1, 4, 3}
    ]

    Enum.map(source, fn p -> encode_row(p) end)
  end

  # Encode row tuple to integer.
  # One row 1, 2, 3, 4 is encoded in one integer, 3 bits for one number 1..4, the first number in a tuple
  # encoded in 3 least significant bits, and so on:
  #  109876543210  -- bits numbers
  #    4  3  2  1  -- encoded numbers from one grid row (one permutation)
  def encode_row(row) do
    (elem(row, 3) <<< 9) ||| (elem(row, 2) <<< 6) ||| (elem(row, 1) <<< 3) ||| elem(row, 0)
  end

  # Decode one row (4 numbers) from bit-encoded to string
  def row_to_string(row) do
    "#{row &&& 7}, #{(row >>> 3) &&& 7}, #{(row >>> 6) &&& 7}, #{(row >>> 9) &&& 7}"
  end

  # encode source grid from tuple of tuples to tuple of integers
  def encode_grid(source) do
    { encode_row(elem(source, 0)), encode_row(elem(source, 1)), encode_row(elem(source, 2)), encode_row(elem(source, 3)) }
  end

  # Check grid for complete solution, return true if solution found.
  def check_solution(grid) do
    row_ok?(elem(grid, 0)) and row_ok?(elem(grid, 1)) and row_ok?(elem(grid, 2)) and row_ok?(elem(grid, 3))
  end

  # Check if encoded row contains all numbers 1..4
  defp row_ok?(row) do
    r = (1 <<< (row &&& 7)) ||| (1 <<< ((row >>> 3) &&& 7)) ||| (1 <<< ((row >>> 6) &&& 7)) ||| (1 <<< ((row >>> 9) &&& 7))
    r == 0b00011110
  end

end
