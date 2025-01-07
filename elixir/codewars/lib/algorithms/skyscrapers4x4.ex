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
    # generate hints from clues
    {rows_hints, columns_hints} = make_hints(clues)
#    IO.puts("Rows hints: #{hints_to_string(rows_hints)}")
#    IO.puts("Columns hints: #{hints_to_string(columns_hints)}")

    {_result, solution} = make_solution(rows_hints, columns_hints)
#    if result == :found do
#      IO.puts("solve: FOUND solution:\n#{grid_to_string(solution)}")
#    else
#      IO.puts("solve: NOT FOUND, result: #{result}, grid:\n#{grid_to_string(solution)}")
#    end

    grid_to_result(solution)
  end

  @doc """
  Return tuple with two tuples with permutations lists sorted by length with their row/column indexes,
  first for rows, second for columns:
  {{{[],i},{[],i},{[],i},{[],i}}, {{[],i},{[],i},{[],i},{[],i}}}
  Example for row_hints in the first test:
  ```
   row_hints: {{[1620, 1676], 1}, {[794, 1305, 1809], 0}, {[675, 738, 1249], 2}, {[2138, 2250, 2201], 3}}
  ```
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

    rows_hints = [
      perms[clues_to_key(elem(cl, 15), elem(cl, 4))],
      perms[clues_to_key(elem(cl, 14), elem(cl, 5))],
      perms[clues_to_key(elem(cl, 13), elem(cl, 6))],
      perms[clues_to_key(elem(cl, 12), elem(cl, 7))]
    ] |> Enum.with_index() |> Enum.sort_by(fn {p, _} -> length(p) end) |> List.to_tuple

    columns_hints = [
      perms[clues_to_key(elem(cl, 0), elem(cl, 11))],
      perms[clues_to_key(elem(cl, 1), elem(cl, 10))],
      perms[clues_to_key(elem(cl, 2), elem(cl, 9))],
      perms[clues_to_key(elem(cl, 3), elem(cl, 8))]
    ] |> Enum.with_index() |> Enum.sort_by(fn {p, _} -> length(p) end) |> List.to_tuple

    {rows_hints, columns_hints}
  end

  @doc """
  Returns map with keys by clues_to_key() to pairF_S and singleF_S permutations list.
  For 0,0 clue uses empty list.
  """
  def make_permutations_table do
    Enum.reduce(permutations(), %{clues_to_key(0, 0) => []}, fn perm, acc ->
      left_clue = find_left_visibility(perm)
      right_clue = find_right_visibility(perm)
      key = clues_to_key(left_clue, right_clue)
      eperm = encode_permutation(perm)
      {_, pair_result} = Map.get_and_update(acc, key, fn perms_list -> update_perms_list(perms_list, eperm) end)

      left_key = clues_to_key(left_clue, 0)
      {_, left_result} = Map.get_and_update(pair_result, left_key, fn perms_list -> update_perms_list(perms_list, eperm) end)

      right_key = clues_to_key(0, right_clue)
      {_, result} = Map.get_and_update(left_result, right_key, fn perms_list -> update_perms_list(perms_list, eperm) end)

      result
    end)
  end

  defp update_perms_list(perms_list, new) do
    if perms_list != nil do
      {perms_list, [new | perms_list]}
    else
      {perms_list, [new]}
    end
  end

  #  Return encoded value of first and second clues numbers.
  #  Example: clues_to_key(1, 2) -> 0x12
  def clues_to_key(first, second) do
    (first <<< 4) ||| second
  end

  @doc """
  Count number of visible skyscrapers (clue) in given permutation from left
  """
  def find_left_visibility(permutation) do
    find_visibility(permutation, &List.foldl/3)
  end

  @doc """
  Count number of visible skyscrapers (clue) in given permutation from right
  """
  def find_right_visibility(permutation) do
    find_visibility(permutation, &List.foldr/3)
  end

  defp find_visibility(permutation, fold) do
    {_, result} = fold.(permutation, {0, 0}, fn n, {max, visibility} ->
      if n > max do
        {n, visibility + 1}
      else
        {max, visibility}
      end
    end)

    result
  end

  @doc """
  Generate all permutations of 1, 2, 3, 4, return list of permutations as numbers lists
  """
  def permutations(), do: permutations([1, 2, 3, 4])
  def permutations([i]), do: [[i]]
  def permutations(list) do
    for h <- list, t <- permutations(list -- [h]), do: [h | t]
  end

  @doc """
  Encode permutation (as list of numbers 1..4) to integer:
  One row 1, 2, 3, 4 is encoded in one integer, 3 bits for one number 1..4, the first number in a permutations list
  encoded in 3 least significant bits, and so on:
  ```
  109876543210  -- bits numbers
    4  3  2  1  -- encoded numbers from one grid row (one permutation)
  ```
  """
  def encode_permutation(permutation) do
    {_, line} = Enum.reduce(permutation, {0, 0}, fn n, {index, acc} ->
      {index + 1, acc ||| ((n &&& 7) <<< (3 * index))}
    end)
    line
  end

  @doc """
  Uses hints to make grid and start to find solution.
  Return {:found, grid} if solution was found or {:no, grid}.
  """
  def make_solution(rows_hints, columns_hints) do
    add_row(rows_hints, columns_hints, empty_grid(), 0)
  end

  defp add_row(rows_hints, columns_hints, grid, index) do
    r = elem(elem(rows_hints, index), 1)
    if index >= 4 do
#      IO.puts("[#{index}] row_#{r}: STOP")
      if is_solution(grid) do
        {:found, grid}
      else
        find_solution(grid)
      end
    else
      hints = elem(elem(rows_hints, index), 0)
      if Enum.empty?(hints) do
        # go to column immediately
#        IO.puts("[#{index}] row_#{r}: empty")
        add_column(rows_hints, columns_hints, grid, index)
      else
        # try to find suitable hint for this row
        Enum.reduce_while(hints, {:no, grid}, fn row, {_, g} ->
#          IO.puts("[#{index}] row_#{r}: #{row_to_string(row)}\n#{grid_to_string(g)}")

          if is_row_suitable(g, row, r) do
            new_grid = set_row(g, row, r)
#            IO.puts("[#{index}] row_#{r}: new\n#{grid_to_string(new_grid)}")

            add_column(rows_hints, columns_hints, new_grid, index)
            |> check_result(grid)
          else
            {:cont, {:no, grid}}
          end
        end)
      end
    end
  end

  defp add_column(rows_hints, columns_hints, grid, index) do
    c = elem(elem(columns_hints, index), 1)
    if index >= 4 do
#      IO.puts("[#{index}] column_#{c}: STOP")
      if is_solution(grid) do
        {:found, grid}
      else
        find_solution(grid)
      end
    else
      hints = elem(elem(columns_hints, index), 0)
      if Enum.empty?(hints) do
#        IO.puts("[#{index}] column_#{c}: empty")
        add_row(rows_hints, columns_hints, grid, index + 1)
      else
        Enum.reduce_while(hints, {:no, grid}, fn column, {_, g} ->
#          IO.puts("[#{index}] column_#{c}: #{row_to_string(column)}\n#{grid_to_string(g)}")

          if is_column_suitable(g, column, c) do
            new_grid = set_column(g, column, c)
#            IO.puts("[#{index}] column_#{c}: new\n#{grid_to_string(new_grid)}")

            if index >= 3 do
              # whole grid is filled up with possible rows and columns,
              # check solution or find one if there are zero cells left
              find_solution(new_grid)
            else
              # continue to fill grid, go deeper on the next level
              add_row(rows_hints, columns_hints, new_grid, index + 1)
            end
            |> check_result(grid)
          else
            {:cont, {:no, grid}}
          end
        end)
      end
    end
  end

  def is_row_suitable(grid, row, r) do
    is_values_suitable(get_row(grid, r), row) and
    columns_have_unique_numbers(set_row(grid, row, r))
  end

  @doc """
  Return true if all columns in the grid have unique numbers or zeros
  """
  def columns_have_unique_numbers(grid) do
    0..3 |> Enum.all?(fn c ->
      c0 = (grid >>> (c * 3)) &&& 7
      c1 = (grid >>> (c * 3 + 12)) &&& 7
      c2 = (grid >>> (c * 3 + 24)) &&& 7
      c3 = (grid >>> (c * 3 + 36)) &&& 7

      cond do
        c0 != 0 and (c0 == c1 or c0 == c2 or c0 == c3) -> false
        c1 != 0 and (c1 == c2 or c1 == c3) -> false
        c2 != 0 and c2 == c3 -> false
        true -> true
      end
    end)
  end

  defp is_column_suitable(grid, column, c) do
    is_values_suitable(get_column(grid, c), column)
  end

  # Check if we can replace old row/column with new value. Only zero cells may be replaced with new numbers,
  # cells with numbers should stay intact.
  defp is_values_suitable(prev, new) do
    ((prev &&& 7) == 0 or (prev &&& 7) == (new &&& 7)) and
    ((prev &&& (7 <<< 3)) == 0 or (prev &&& (7 <<< 3)) == (new &&& (7 <<< 3))) and
    ((prev &&& (7 <<< 6)) == 0 or (prev &&& (7 <<< 6)) == (new &&& (7 <<< 6))) and
    ((prev &&& (7 <<< 9)) == 0 or (prev &&& (7 <<< 9)) == (new &&& (7 <<< 9)))
  end

  defp check_result({result, solution}, grid) do
    if result == :found do
      {:halt, {:found, solution}}
    else
      {:cont, {:no, grid}}
    end
  end

  @doc """
  Second stage engine to find solution from hints filled grid with some zeros.
  Return {:found, grid} if solution was found or {:no, grid} if solution can't be found for given grid.
  """
  def find_solution(grid) do
#    IO.puts("find_solution:\n#{grid_to_string(grid)}")

    rc = find_first_zero(grid)
    if elem(rc, 0) < 0 or elem(rc, 1) < 0 do
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

  @doc """
  Decode compact grid's representation to matrix of ints as needed for solve()
  """
  def grid_to_result(grid) do
    Enum.reduce(0..3, [], fn i, acc ->
      List.insert_at(acc, -1, [get_cell(grid, i, 0), get_cell(grid, i, 1), get_cell(grid, i, 2), get_cell(grid, i, 3)])
    end)
  end

  # Return single row from grid
  defp get_row(grid, r) do
    (grid >>> (r * 12)) &&& 0xFFF
  end

  # Return new grid with new row on index r
  defp set_row(grid, row, r) do
    n = r * 12
    (grid &&& (bnot(0xFFF <<< n))) ||| (row <<< n)
  end

  # Return single column from grid
  def get_column(grid, c) do
    c0 = (grid >>> (c * 3)) &&& 7
    c1 = (grid >>> (c * 3 + 12)) &&& 7
    c2 = (grid >>> (c * 3 + 24)) &&& 7
    c3 = (grid >>> (c * 3 + 36)) &&& 7
    (c3 <<< 9) ||| (c2 <<< 6) ||| (c1 <<< 3) ||| c0
  end

  # Return new grid with new column on index c
  #                   4         3         2         1         0
  #            765432109876543210987654321098765432109876543210
  #   mask = 0b000000000111000000000111000000000111000000000111   -- mask for whole grid (here before inversion)
  # column =                                       DDDCCCBBBAAA		-- column in integer
  # result =            DDD         CCC         BBB         AAA 	-- column's 0 parts spread on grid
  def set_column(grid, column, c) do
    mask = bnot(((7 <<< 36) ||| (7 <<< 24) ||| (7 <<< 12) |||  7) <<< (c * 3))

    (grid &&& mask) |||
    ((
     (column &&& 7) |||
     ((column &&& (7 <<< 3)) <<< (12 - 3)) |||
     ((column &&& (7 <<< 6)) <<< (24 - 6)) |||
     ((column &&& (7 <<< 9)) <<< (36 - 9))
    ) <<< (c * 3))
  end

  @doc """
  Check grid for complete solution, return true if solution found.
  """
  def is_solution(grid) do
    is_line_ok(get_row(grid, 0)) and is_line_ok(get_row(grid, 1)) and is_line_ok(get_row(grid, 2)) and is_line_ok(get_row(grid, 3)) and
    is_line_ok(get_column(grid, 0)) and is_line_ok(get_column(grid, 1)) and is_line_ok(get_column(grid, 2)) and is_line_ok(get_column(grid, 3))
  end

  # Check if encoded row/column contains all numbers 1..4
  defp is_line_ok(line) do
    (
      (1 <<< (line &&& 7)) ||| (1 <<< ((line >>> 3) &&& 7)) |||
      (1 <<< ((line >>> 6) &&& 7)) ||| (1 <<< ((line >>> 9) &&& 7))
    ) == 0b00011110
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

  # --------- extra functions, not for kata solution ---------
  @doc """
  Converts tuple with hints lists to string for logs
  """
  def hints_to_string(hints) do
    hints
    |> Tuple.to_list()
    |> Enum.with_index()
    |> Enum.map(fn {hint, index} -> "[#{index}] #{elem(hint, 1)}-[#{Enum.join(Enum.map(elem(hint, 0), fn h -> row_to_string(h) end), ", ")}]" end)
    |> Enum.join(", ")
  end

  @doc """
  Decode one row (4 numbers) from bit-encoded to string
  """
  def row_to_string(row) do
    "#{row &&& 7} #{(row >>> 3) &&& 7} #{(row >>> 6) &&& 7} #{(row >>> 9) &&& 7}"
  end

  # Convert grid in integer to string
#  defp grid_to_string(grid) do
#    Enum.reduce(0..3, "", fn i, sr ->
#      sr <> "#{i}: " <>
#            Enum.reduce(0..3, "", fn j, sc -> sc <> Integer.to_string(get_cell(grid, i, j)) <> " " end)
#            <> "\n"
#    end)
#  end

end
