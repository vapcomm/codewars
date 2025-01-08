defmodule Skyscrapers7x7 do
  @moduledoc """
  7 By 7 Skyscrapers -- 1 kyu
  https://www.codewars.com/kata/5917a2205ffc30ec3a0000a8/train/elixir

  Grid in this solution is stored as tuple of 7 integers, each integer contains one row encoded by 4 bits for
  every number:
  ```
         2         1         0
  7654321098765432109876543210    -- bits
     7   6   5   4   3   2   1    -- numbers
  ```
  Grid's coordinates starts from top left (0,0), first row is in elem(grid, 0) and so on.
  """

  import Bitwise

  @doc """
  Pass the clues in an array of 28 items. This array contains the clues around the clock.
  Return 7x7 matrix, list of rows of ints.
  """
  def solve(_clues) do
    # generate hints from clues
#    {rows_hints, columns_hints} = make_hints(clues)
    #    IO.puts("Rows hints: #{hints_to_string(rows_hints)}")
    #    IO.puts("Columns hints: #{hints_to_string(columns_hints)}")

#    {_result, solution} = make_solution(rows_hints, columns_hints)
    #    if result == :found do
    #      IO.puts("solve: FOUND solution:\n#{grid_to_string(solution)}")
    #    else
    #      IO.puts("solve: NOT FOUND, result: #{result}, grid:\n#{grid_to_string(solution)}")
    #    end

#    grid_to_result(solution)
  end

  @empty_grid { 0, 0, 0, 0, 0, 0, 0}

  @doc """
  Second stage engine to find solution from hints filled grid with some zeros.
  Return {:found, grid} if solution was found or {:no, grid} if solution can't be found for given grid.
  """
  def find_solution(grid) do
#    IO.puts("find_solution:\n#{grid_to_string(grid)}")

    rc = find_first_zero(grid)
    if rc < 0 do
      # no zeros, check grid for solution
      if is_solution(grid) do
        {:found, grid}
      else
        {:no, grid}
      end
    else
      # check all possible permutations variants for rc cell
      variants = get_variants(grid, rc)

      Enum.reduce_while(variants, {:no, grid}, fn v, {_, g} ->
        new_grid = set_cell(g, rc, v)
        if is_solution(new_grid) do
          {:halt, {:found, new_grid}}
        else
          {result, solution} = find_solution(new_grid)
          if result == :found do
            {:halt, {:found, solution}}
          else
            {:cont, {:no, g}}
          end
        end
      end)
    end
  end

  @grid_indexes [0, 1, 2, 3, 4, 5, 6]

  @doc """
  Return row, column coordinates of first found zero in grid
  encoded in integer 0xRC or -1 if there is no any zero in the whole grid.
  """
  def find_first_zero(grid) do
    Enum.find_value(@grid_indexes, -1, fn i ->
      column = find_zero_in_row(elem(grid, i))
      if column >= 0, do: (i <<< 4) ||| column
    end)
  end

  # Return number of the first column with zero value or -1 if zero not found
  def find_zero_in_row(row) do
    # this is smaller, but slow
    # Enum.find_value(@grid_indexes, -1, fn i ->
    #   if (row &&& (0xF <<< (i * 4))) == 0, do: i
    # end)

    # this 7.5 times faster then find_value
    cond do
      (row &&& 0xF) == 0 -> 0
      (row &&& 0xF0) == 0 -> 1
      (row &&& 0xF00) == 0 -> 2
      (row &&& 0xF000) == 0 -> 3
      (row &&& 0xF0000) == 0 -> 4
      (row &&& 0xF00000) == 0 -> 5
      (row &&& 0xF000000) == 0 -> 6
      true -> -1
    end
  end

  @doc """
  Check grid for complete solution, return true if solution found.
  """
  def is_solution(grid) do
    is_row_ok(elem(grid, 0)) and is_row_ok(elem(grid, 1)) and is_row_ok(elem(grid, 2)) and is_row_ok(elem(grid, 3)) and
    is_row_ok(elem(grid, 4)) and is_row_ok(elem(grid, 5)) and is_row_ok(elem(grid, 6)) and
    is_column_ok(grid, 0) and is_column_ok(grid, 1) and is_column_ok(grid, 2) and is_column_ok(grid, 3) and
    is_column_ok(grid, 4) and is_column_ok(grid, 5) and is_column_ok(grid, 6)
  end

  # Check if encoded row contains all numbers 1..7
  defp is_row_ok(line) do
    (
      (1 <<< (line &&& 0xF)) ||| (1 <<< ((line >>> 4) &&& 0xF)) ||| (1 <<< ((line >>> 8) &&& 0xF)) |||
      (1 <<< ((line >>> 12) &&& 0xF)) ||| (1 <<< ((line >>> 16) &&& 0xF)) ||| (1 <<< ((line >>> 20) &&& 0xF)) |||
      (1 <<< ((line >>> 24) &&& 0xF))
    ) == 0b11111110
  end

  # Check if column contains all numbers 1..7
  defp is_column_ok(grid, c) do
    get_column_numbers_bits(grid, c) == 0b11111110
  end

  @doc """
  Return list of numbers suitable to put in row r and column c
  """
  def get_variants(grid, rc) do
    rb = get_row_numbers_bits(grid, (rc >>> 4) &&& 0xF)
    cb = get_column_numbers_bits(grid, rc &&& 0xF)
    # left absent numbers bits for 1..7 only
    common = bnot(rb ||| cb) &&& 0xFF
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

  # Return new grid with new cell value in given row and column
  defp set_cell(grid, rc, value) do
    r = rc >>> 4
    n = (rc &&& 0xF) * 4
    mask = bnot(0xF <<< n)
    new_row = (elem(grid, r) &&& mask) ||| (value <<< n)
    replace_at(grid, r, new_row)
  end

  defp replace_at(grid, r, row) do
    grid|> Tuple.delete_at(r) |> Tuple.insert_at(r, row)
  end
#  defp replace_at({a, b, c, d, e, f, g}, 0, v), do: {v, b, c, d, e, f, g}
#  defp replace_at({a, b, c, d, e, f, g}, 1, v), do: {a, v, c, d, e, f, g}
#  defp replace_at({a, b, c, d, e, f, g}, 2, v), do: {a, b, v, d, e, f, g}
#  defp replace_at({a, b, c, d, e, f, g}, 3, v), do: {a, b, c, v, e, f, g}
#  defp replace_at({a, b, c, d, e, f, g}, 4, v), do: {a, b, c, d, v, f, g}
#  defp replace_at({a, b, c, d, e, f, g}, 5, v), do: {a, b, c, d, e, v, g}
#  defp replace_at({a, b, c, d, e, f, g}, 6, v), do: {a, b, c, d, e, f, v}


  # Return bitmask of numbers presented in grid's row
  # Examples for rows:
  #  {1, 2, 3, 4, 5, 6, 7} -> 0b11111110
  #  {0, 0, 0, 0, 2, 1, 2} -> 0b00000111
  # If number is doubled, it will be count once in one bit.
  defp get_row_numbers_bits(grid, r) do
    row = elem(grid, r)
    (1 <<< (row &&& 0xF)) ||| (1 <<< ((row >>> 4) &&& 0xF)) ||| (1 <<< ((row >>> 8) &&& 0xF)) |||
    (1 <<< ((row >>> 12) &&& 0xF)) ||| (1 <<< ((row >>> 16) &&& 0xF)) ||| (1 <<< ((row >>> 20) &&& 0xF)) |||
    (1 <<< ((row >>> 24) &&& 0xF))
  end

  # Return bitmask of numbers in grid's column
  defp get_column_numbers_bits(grid, c) do
    bits = c * 4
    (1 <<< ((elem(grid, 0) >>> bits) &&& 0xF)) |||
    (1 <<< ((elem(grid, 1) >>> bits) &&& 0xF)) |||
    (1 <<< ((elem(grid, 2) >>> bits) &&& 0xF)) |||
    (1 <<< ((elem(grid, 3) >>> bits) &&& 0xF)) |||
    (1 <<< ((elem(grid, 4) >>> bits) &&& 0xF)) |||
    (1 <<< ((elem(grid, 5) >>> bits) &&& 0xF)) |||
    (1 <<< ((elem(grid, 6) >>> bits) &&& 0xF))
  end

end
