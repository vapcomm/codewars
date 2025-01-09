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
  def solve(clues) do
    # generate hints from clues for the first search stage
    {rows_hints, columns_hints} = make_hints(clues)
    IO.puts("Rows hints: #{hints_to_string(rows_hints)}")
    IO.puts("Columns hints: #{hints_to_string(columns_hints)}")

    {result, solution} = make_solution(rows_hints, columns_hints)
    if result == :found do
      IO.puts("solve: FOUND solution:\n#{grid_to_string(solution)}")
    else
      IO.puts("solve: NOT FOUND, result: #{result}, grid:\n#{grid_to_string(solution)}")
    end

    grid_to_result(solution)
  end

  @empty_grid { 0, 0, 0, 0, 0, 0, 0}

  @doc """
  Return tuple with two tuples with permutations lists sorted by length with their row/column indexes,
  first for rows, second for columns:
  {{{[],i},{[],i}, ... {[],i}}, {{[],i},{[],i}, ... {[],i}}}
  Clues in given list form the circle around a grid:
  ```
      0  1  2  3  4  5  6
  27| 0  0  0  0  0  0  0| 7
  26| 0  0  0  0  0  0  0| 8
  25| 0  0  0  0  0  0  0| 9
  24| 0  0  0  0  0  0  0| 10
  23| 0  0  0  0  0  0  0| 11
  22| 0  0  0  0  0  0  0| 12
  21| 0  0  0  0  0  0  0| 13
     20 19 18 17 16 15 14
  ```
  """
  def make_hints(clues) do
    cl = List.to_tuple(clues)
    perms = make_permutations_table()

    rows_hints = [
      perms[clues_to_key(elem(cl, 27), elem(cl, 7))],
      perms[clues_to_key(elem(cl, 26), elem(cl, 8))],
      perms[clues_to_key(elem(cl, 25), elem(cl, 9))],
      perms[clues_to_key(elem(cl, 24), elem(cl, 10))],
      perms[clues_to_key(elem(cl, 23), elem(cl, 11))],
      perms[clues_to_key(elem(cl, 22), elem(cl, 12))],
      perms[clues_to_key(elem(cl, 21), elem(cl, 13))]
    ] |> Enum.with_index() |> Enum.sort_by(fn {p, _} -> length(p) end) |> List.to_tuple

    columns_hints = [
      perms[clues_to_key(elem(cl, 0), elem(cl, 20))],
      perms[clues_to_key(elem(cl, 1), elem(cl, 19))],
      perms[clues_to_key(elem(cl, 2), elem(cl, 18))],
      perms[clues_to_key(elem(cl, 3), elem(cl, 17))],
      perms[clues_to_key(elem(cl, 4), elem(cl, 16))],
      perms[clues_to_key(elem(cl, 5), elem(cl, 15))],
      perms[clues_to_key(elem(cl, 6), elem(cl, 14))],
    ] |> Enum.with_index() |> Enum.sort_by(fn {p, _} -> length(p) end) |> List.to_tuple

    {rows_hints, columns_hints}
  end

  @doc """
  Returns map with keys by clues_to_key() to pairF_S and singleF_S permutations lists.
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
  defp clues_to_key(first, second) do
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
  Generate all permutations of 1..7, return list of permutations as numbers lists
  """
  def permutations(), do: permutations([1, 2, 3, 4, 5, 6, 7])
  def permutations([i]), do: [[i]]
  def permutations(list) do
    for h <- list, t <- permutations(list -- [h]), do: [h | t]
  end

  @doc """
  Encode permutation (as list of numbers 1..7) to integer:
  One row is encoded in one integer, 4 bits for one number 1..7, the first number in a permutations list
  encoded in 4 least significant bits, and so on:
  ```
  7654321098765432109876543210  -- bits numbers
     7   6   5   4   3   2   1  -- encoded numbers from one grid row (one permutation)
  ```
  """
  def encode_permutation(permutation) do
    {_, line} = Enum.reduce(permutation, {0, 0}, fn n, {index, acc} ->
      {index + 1, acc ||| ((n &&& 0xF) <<< (4 * index))}
    end)
    line
  end

  @doc """
  Uses hints to make grid and start to find solution.
  Return {:found, grid} if solution was found or {:no, grid}.
  """
  def make_solution(rows_hints, columns_hints) do
    add_row(rows_hints, columns_hints, @empty_grid, 0)
  end

  defp add_row(rows_hints, columns_hints, grid, index) do
    r = elem(elem(rows_hints, index), 1)
    if index >= 7 do
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
    if index >= 7 do
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

            if index >= 6 do
              # whole grid is filled up with possible rows and columns,
              # check solution or find one if there are zero cells left
              find_solution(new_grid)
            else
              # continue to fill grid, go deeper to the next level
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
    is_values_suitable(elem(grid, r), row) and
    columns_have_unique_numbers(set_row(grid, row, r))
  end

  @doc """
  Return true if all columns in the grid have unique numbers or zeros
  """
  def columns_have_unique_numbers(grid) do
    0..6 |> Enum.all?(fn c ->
      bits = c * 4
      c0 = (elem(grid, 0) >>> bits) &&& 0xF
      c1 = (elem(grid, 1) >>> bits) &&& 0xF
      c2 = (elem(grid, 2) >>> bits) &&& 0xF
      c3 = (elem(grid, 3) >>> bits) &&& 0xF
      c4 = (elem(grid, 4) >>> bits) &&& 0xF
      c5 = (elem(grid, 5) >>> bits) &&& 0xF
      c6 = (elem(grid, 6) >>> bits) &&& 0xF

      cond do
        c0 != 0 and (c0 == c1 or c0 == c2 or c0 == c3 or c0 == c4 or c0 == c5 or c0 == c6) -> false
        c1 != 0 and (c1 == c2 or c1 == c3 or c1 == c4 or c1 == c5 or c1 == c6) -> false
        c2 != 0 and (c2 == c3 or c2 == c4 or c2 == c5 or c2 == c6) -> false
        c3 != 0 and (c3 == c4 or c3 == c5 or c3 == c6) -> false
        c4 != 0 and (c4 == c5 or c4 == c6) -> false
        c5 != 0 and c5 == c6 -> false
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
    ((prev &&& 0xF) == 0 or (prev &&& 0xF) == (new &&& 0xF)) and
    ((prev &&& 0xF0) == 0 or (prev &&& 0xF0) == (new &&& 0xF0)) and
    ((prev &&& 0xF00) == 0 or (prev &&& 0xF00) == (new &&& 0xF00)) and
    ((prev &&& 0xF000) == 0 or (prev &&& 0xF000) == (new &&& 0xF000)) and
    ((prev &&& 0xF0000) == 0 or (prev &&& 0xF0000) == (new &&& 0xF0000)) and
    ((prev &&& 0xF00000) == 0 or (prev &&& 0xF00000) == (new &&& 0xF00000)) and
    ((prev &&& 0xF000000) == 0 or (prev &&& 0xF000000) == (new &&& 0xF000000))
  end

  defp check_result({result, solution}, grid) do
    if result == :found do
      {:halt, {:found, solution}}
    else
      {:cont, {:no, grid}}
    end
  end

  # Return new grid with new row on index r
  defp set_row(grid, row, r) do
    replace_at(grid, r, row)
  end

  @doc """
  Return single column from grid as an integer
  """
  def get_column(grid, c) do
    bits = c * 4
    ((elem(grid, 0) >>> bits) &&& 0xF) |||
    (((elem(grid, 1) >>> bits) &&& 0xF) <<< 4) |||
    (((elem(grid, 2) >>> bits) &&& 0xF) <<< 8) |||
    (((elem(grid, 3) >>> bits) &&& 0xF) <<< 12) |||
    (((elem(grid, 4) >>> bits) &&& 0xF) <<< 16) |||
    (((elem(grid, 5) >>> bits) &&& 0xF) <<< 20) |||
    (((elem(grid, 6) >>> bits) &&& 0xF) <<< 24)
  end

  # Return new grid with new column on index
  @spec set_column(tuple(), integer(), integer()) :: tuple()
  def set_column({a, b, c, d, e, f, g}, column, index) do
    bits = index * 4
    mask = bnot(0xF <<< bits)
    {
      (a &&& mask) ||| ((column &&& 0xF) <<< bits),
      (b &&& mask) ||| (((column >>> 4) &&& 0xF) <<< bits),
      (c &&& mask) ||| (((column >>> 8) &&& 0xF) <<< bits),
      (d &&& mask) ||| (((column >>> 12) &&& 0xF) <<< bits),
      (e &&& mask) ||| (((column >>> 16) &&& 0xF) <<< bits),
      (f &&& mask) ||| (((column >>> 20) &&& 0xF) <<< bits),
      (g &&& mask) ||| (((column >>> 24) &&& 0xF) <<< bits),
    }
  end

  @doc """
  Decode compact grid's representation to matrix of ints as needed for solve()
  """
  def grid_to_result(grid) do
    Enum.reduce(0..6, [], fn r, acc ->
      List.insert_at(acc, -1, [
        get_cell(grid, r, 0), get_cell(grid, r, 1), get_cell(grid, r, 2), get_cell(grid, r, 3),
        get_cell(grid, r, 4), get_cell(grid, r, 5), get_cell(grid, r, 6)
      ])
    end)
  end

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

    # this 7.5 times faster then with Enum.find_value()
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

  # Return cell value in given grid by given row and column.
  defp get_cell(grid, r, c) do
    (elem(grid, r) >>> (c * 4)) &&& 0xF
  end

  # Return new grid with new cell value in given row and column
  defp set_cell(grid, rc, value) do
    r = rc >>> 4
    n = (rc &&& 0xF) * 4    # it's  c * 4
    mask = bnot(0xF <<< n)
    new_row = (elem(grid, r) &&& mask) ||| (value <<< n)
    replace_at(grid, r, new_row)
  end

  # Return new tuple with replaced element by given index
  def replace_at({_, b, c, d, e, f, g}, 0, v), do: {v, b, c, d, e, f, g}
  def replace_at({a, _, c, d, e, f, g}, 1, v), do: {a, v, c, d, e, f, g}
  def replace_at({a, b, _, d, e, f, g}, 2, v), do: {a, b, v, d, e, f, g}
  def replace_at({a, b, c, _, e, f, g}, 3, v), do: {a, b, c, v, e, f, g}
  def replace_at({a, b, c, d, _, f, g}, 4, v), do: {a, b, c, d, v, f, g}
  def replace_at({a, b, c, d, e, _, g}, 5, v), do: {a, b, c, d, e, v, g}
  def replace_at({a, b, c, d, e, f, _}, 6, v), do: {a, b, c, d, e, f, v}

  #  this is 3 times slow, then seven replace_at() functions
  #  def replace_at(grid, r, row) do
  #    grid |> Tuple.delete_at(r) |> Tuple.insert_at(r, row)
  #  end

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

  # --------- extra functions, not for kata solution ---------
  @doc """
  Converts tuple with hints lists to string for logs
  """
  def hints_to_string(hints) do
    hints
    |> Tuple.to_list()
    |> Enum.with_index()
#full    |> Enum.map(fn {hint, index} -> "[#{index}] #{elem(hint, 1)}-[#{Enum.join(Enum.map(elem(hint, 0), fn h -> row_to_string(h) end), ", ")}]" end)
    |> Enum.map(fn {hint, index} -> "[#{index}] #{elem(hint, 1)}-[#{length(elem(hint, 0))}]" end)   # only sizes
    |> Enum.join(", ")
  end

  @doc """
  Decode one row (4 numbers) from bit-encoded to string
  """
  def row_to_string(row) do
    "#{row &&& 0xF} #{(row >>> 4) &&& 0xF} #{(row >>> 8) &&& 0xF} #{(row >>> 12) &&& 0xF} " <>
    "#{(row >>> 16) &&& 0xF} #{(row >>> 20) &&& 0xF} #{(row >>> 24) &&& 0xF}"
  end

  # Convert grid in integer to string
  defp grid_to_string(grid) do
    Enum.reduce(0..6, "", fn r, acc ->
      acc <> "#{r}: " <>
      Enum.reduce(0..6, "", fn c, sc -> sc <> Integer.to_string(get_cell(grid, r, c)) <> " " end)
      <> "\n"
    end)
  end

end
