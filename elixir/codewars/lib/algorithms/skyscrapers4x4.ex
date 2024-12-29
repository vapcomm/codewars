defmodule Skyscrapers4x4 do
  @moduledoc """
  4 By 4 Skyscrapers -- 4 kyu
  https://www.codewars.com/kata/5671d975d81d6c1c87000022/train/elixir

  **NOTE:** this solution use grid encoded in 48 bits stored in one integer to speedup grid changes.
  Every cell in grid use 3 bits from integer value, starting from the lowest bits r:0, c:0.
  ```
    C 0 1 2 3
  R ---------
  0:  1 2 3 4
  1:  5 6 7 8
  2:  9 A B C
  3:  D E F G

         4         3         2         1         0
  765432109876543210987654321098765432109876543210
    G  F  E  D  C  B  A  9  8  7  6  5  4  3  2  1
  ```
  Use grid's coordinate as row,column pairs, top left cell is 0,0.
  """

  import Bitwise

  @doc """
  Pass the clues in an array of 16 items. This array contains the clues around the clock.
  Return 4x4 matrix, list of rows of ints.
  """
  def solve(clues) do
    grid = empty_grid()
#    IO.puts("Start grid\n" <> grid_to_string(grid))

    # generate hints from clues
    {rows_hints, columns_hints} = make_hints(clues)

    IO.puts("Rows hints: #{inspect(rows_hints)}")
    IO.puts("Columns hints: #{inspect(columns_hints)}")

    grid_to_result(grid)
  end

  @doc """
  Return two tuples with permutations lists, first for rows, second for columns:
  {{[],[],[],[]}, {[],[],[],[]}}
  Clues in given list form the circle around a grid:
  ```
      0  1  2  3
  15| 0  0  0  0 | 4
  14| 0  0  0  0 | 5
  13| 0  0  0  0 | 6
  12| 0  0  0  0 | 7
     11 10  9  8
  ```
  """
  def make_hints(clues) do
    cl = List.to_tuple(clues)
    perms = make_permutations_table()

    rows_hints = {
      perms[clues_to_key(elem(cl, 15), elem(cl, 4))],
      perms[clues_to_key(elem(cl, 14), elem(cl, 5))],
      perms[clues_to_key(elem(cl, 13), elem(cl, 6))],
      perms[clues_to_key(elem(cl, 12), elem(cl, 7))]
    }
    columns_hints = {
      perms[clues_to_key(elem(cl, 0), elem(cl, 11))],
      perms[clues_to_key(elem(cl, 1), elem(cl, 10))],
      perms[clues_to_key(elem(cl, 2), elem(cl, 9))],
      perms[clues_to_key(elem(cl, 3), elem(cl, 8))]
    }

    {rows_hints, columns_hints}
  end

  # Returns map with keys by clues_to_key() to pairF_S and singleF_S permutations list
  defp make_permutations_table do
    %{
      clues_to_key(1, 2) => encode_rows_list([{4, 2, 3, 1}, {4, 1, 2, 3}, {4, 2, 1, 3}]),
      clues_to_key(1, 3) => encode_rows_list([{4, 1, 3, 2}, {4, 3, 1, 2}]),
      clues_to_key(1, 4) => encode_rows_list([{4, 3, 2, 1}]),

      clues_to_key(2, 1) => encode_rows_list([{3, 1, 2, 4}, {3, 2, 1, 4}]),
      clues_to_key(2, 2) => encode_rows_list([{3, 2, 4, 1}, {3, 4, 1, 2}, {3, 1, 4, 2}, {1, 4, 2, 3}, {2, 4, 1, 3}, {2, 1, 4, 3}]),
      clues_to_key(2, 3) => encode_rows_list([{2, 4, 3, 1}, {3, 4, 2, 1}, {1, 4, 3, 2}]),

      clues_to_key(3, 1) => encode_rows_list([{2, 1, 3, 4}, {1, 3, 2, 4}, {2, 3, 1, 4}]),
      clues_to_key(3, 2) => encode_rows_list([{2, 3, 4, 1}, {1, 3, 4, 2}, {1, 2, 4, 3}]),
      clues_to_key(4, 1) => encode_rows_list([{1, 2, 3, 4}])
      #TODO: @single
    }
  end

  #  Return encoded value of first and second clues numbers.
  #  Example: clues_to_key(1, 2) -> 0x12
  defp clues_to_key(first, second) do
    (first <<< 4) ||| second
  end

  defp encode_rows_list(rows) do
    Enum.map(rows, fn row -> encode_row(row) end )
  end

  @doc """
  Main engine to find solution from start grid with some zeros
  Return {:found, grid} if solution was found or {:no, grid} if solution can't be found for given grid.
  """
  def find_solution(grid) do
    IO.puts("1: start\n#{grid_to_string(grid)}")

    rc = find_first_zero(grid)
    if elem(rc, 0) < 0 or elem(rc, 1) < 0 do
      # no zeros, check grid for solution
      if is_solution(grid) do
        IO.puts("2: FOUND")
        {:found, grid}
      else
        IO.puts("3: NO")
        {:no, grid}
      end
    else
      # check all possible permutations variants for rc cell
      variants = get_variants(grid, rc)
      IO.puts("4: variants: #{inspect(variants)}")

      Enum.reduce_while(variants, {:no, grid}, fn v, {_, g} ->
        new_grid = set_cell(g, rc, v)
        IO.puts("5: new\n#{grid_to_string(new_grid)}")

        if is_solution(new_grid) do
          IO.puts("6: FOUND")
          {:halt, {:found, new_grid}}
        else
          {result, solution} = find_solution(new_grid)
          IO.puts("7: result: #{result}")

          if result == :found do
            {:halt, {:found, solution}}
          else
            {:cont, {:no, g}}
          end
        end
      end)
    end
  end

  defp empty_grid() do
    0xFFFFFFFFFFFF &&& 0 # at least 48 bits integer
  end

  # Return cell value in given grid by given row and column.
  defp get_cell(grid, r, c) when r in 0..3 and c in 0..3 do
    (grid >>> (r * 12 + c * 3)) &&& 7
  end

  # Return new grid with new cell value in given row and column
  defp set_cell(grid, {r, c}, value) do
    n = r * 12 + c * 3
    mask = bnot(7 <<< n)
    (grid &&& mask) ||| (value <<< n)
  end

  # Convert grid in integer to string
  defp grid_to_string(grid) do
    Enum.reduce(0..3, "", fn i, sr ->
      sr <> "#{i}: " <>
      Enum.reduce(0..3, "", fn j, sc -> sc <> Integer.to_string(get_cell(grid, i, j)) <> " " end)
      <> "\n"
    end)
  end

  @doc """
  Generate all permutations of 1, 2, 3, 4 and return bits-encoded values list.
  """
  def permutations() do
    # table of permutations taken from Kotlin solution's tests
    source = [
      {1, 2, 3, 4}, {2, 1, 3, 4}, {3, 1, 2, 4}, {1, 3, 2, 4}, {2, 3, 1, 4}, {3, 2, 1, 4},
      {4, 2, 3, 1}, {2, 4, 3, 1}, {3, 4, 2, 1}, {4, 3, 2, 1}, {2, 3, 4, 1}, {3, 2, 4, 1},
      {4, 1, 3, 2}, {1, 4, 3, 2}, {3, 4, 1, 2}, {4, 3, 1, 2}, {1, 3, 4, 2}, {3, 1, 4, 2},
      {4, 1, 2, 3}, {1, 4, 2, 3}, {2, 4, 1, 3}, {4, 2, 1, 3}, {1, 2, 4, 3}, {2, 1, 4, 3}
    ]

    Enum.map(source, fn p -> encode_row(p) end)
  end

  @doc """
  Decode compact grid's representation to matrix of ints as needed for solve()
  """
  def grid_to_result(grid) do
    Enum.reduce(0..3, [], fn i, acc ->
      List.insert_at(acc, -1, [get_cell(grid, i, 0), get_cell(grid, i, 1), get_cell(grid, i, 2), get_cell(grid, i, 3)])
    end)
  end

  @doc """
  Encode source grid from tuple of tuples to integer
  """
  def encode_grid(source) do
    encode_row(elem(source, 0)) |||
    (encode_row(elem(source, 1)) <<< 12) |||
    (encode_row(elem(source, 2)) <<< 24) |||
    (encode_row(elem(source, 3)) <<< 36)
  end

  @doc """
  Encode row tuple to integer.
  One row 1, 2, 3, 4 is encoded in one integer, 3 bits for one number 1..4, the first number in a tuple
  encoded in 3 least significant bits, and so on:
  ```
  109876543210  -- bits numbers
    4  3  2  1  -- encoded numbers from one grid row (one permutation)
  ```
  """
  def encode_row(row) do
    (elem(row, 3) <<< 9) ||| (elem(row, 2) <<< 6) ||| (elem(row, 1) <<< 3) ||| elem(row, 0)
  end

  # Return single row from grid
  defp get_row(grid, r) do
    (grid >>> (r * 12)) &&& 0xFFF
  end

  @doc """
  Decode one row (4 numbers) from bit-encoded to string
  """
  def row_to_string(row) do
    "#{row &&& 7}, #{(row >>> 3) &&& 7}, #{(row >>> 6) &&& 7}, #{(row >>> 9) &&& 7}"
  end

  @doc """
  Check grid for complete solution, return true if solution found.
  """
  def is_solution(grid) do
    row_ok?(get_row(grid, 0)) and row_ok?(get_row(grid, 1)) and row_ok?(get_row(grid, 2)) and row_ok?(get_row(grid, 3))
  end

  # Check if encoded row contains all numbers 1..4
  defp row_ok?(row) do
    r = (1 <<< (row &&& 7)) ||| (1 <<< ((row >>> 3) &&& 7)) ||| (1 <<< ((row >>> 6) &&& 7)) ||| (1 <<< ((row >>> 9) &&& 7))
    r == 0b00011110
  end

  @doc """
  Return tuple with row, column coordinates of first found zero in grid
  """
  @spec find_first_zero(integer()) :: integer()
  def find_first_zero(grid) do
    column = find_zero_in_row(get_row(grid, 0))
    if column >= 0 do
      {0, column}
    else
      column = find_zero_in_row(get_row(grid, 1))
      if column >= 0 do
        {1, column}
      else
        column = find_zero_in_row(get_row(grid, 2))
        if column >= 0 do
          {2, column}
        else
          column = find_zero_in_row(get_row(grid, 3))
          if column >= 0 do
            {3, column}
          else
            {-1, -1}
          end
        end
      end
    end
  end

  # Return number of the first column with zero value or -1 if zero not found
  @spec find_zero_in_row(integer()) :: integer()
  defp find_zero_in_row(row) do
    cond do
      (row &&& 7) == 0 -> 0
      ((row >>> 3) &&& 7) == 0 -> 1
      ((row >>> 6) &&& 7) == 0 -> 2
      ((row >>> 9) &&& 7) == 0 -> 3
      true -> -1
    end
  end

  @doc """
  Return list of numbers suitable to put in row r and column c
  """
  def get_variants(grid, {r, c}) do
    rb = get_row_numbers_bits(grid, r)
    cb = get_column_numbers_bits(grid, c)
    # left absent numbers bits for 1..4 only
    common = bnot(rb ||| cb) &&& 0x1E
    bits_to_list(common)
  end

  @doc """
  Convert bitmask to list with bits numbers from 1st bit.
  If number's bit is 1, this number is added to the result list.
  """
  def bits_to_list(bits) do
    bits_to_list(bits >>> 1, 1)
  end

  defp bits_to_list(bits, n) do
    if bits == 0 do
      []
    else
      if (bits &&& 1) == 1 do
        [n | bits_to_list(bits >>> 1, n + 1)]
      else
        bits_to_list(bits >>> 1, n + 1)
      end
    end
  end

  # Return bitmask of numbers presented in grid's row
  # Examples for rows:
  #  {1, 2, 3, 4} -> 0b00011110
  #  {0, 0, 1, 2} -> 0b00000111
  defp get_row_numbers_bits(grid, r) do
    row = get_row(grid, r)
    (1 <<< (row &&& 7)) ||| (1 <<< ((row >>> 3) &&& 7)) ||| (1 <<< ((row >>> 6) &&& 7)) ||| (1 <<< ((row >>> 9) &&& 7))
  end

  # Return bitmask of numbers in grid's column
  defp get_column_numbers_bits(grid, c) do
    c0 = (grid >>> (c * 3)) &&& 7
    c1 = (grid >>> (c * 3 + 12)) &&& 7
    c2 = (grid >>> (c * 3 + 24)) &&& 7
    c3 = (grid >>> (c * 3 + 36)) &&& 7
    (1 <<< c0) ||| (1 <<< c1) ||| (1 <<< c2) ||| (1 <<< c3)
  end

end
