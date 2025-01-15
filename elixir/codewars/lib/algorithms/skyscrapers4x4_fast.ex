defmodule Skyscrapers4x4Fast do
  @moduledoc """
  4 By 4 Skyscrapers -- 4 kyu
  https://www.codewars.com/kata/5671d975d81d6c1c87000022/train/elixir

  **NOTE:** this is fast solution version based on ideas of farhadi solution.
  Instead of the first "additive" algorithm, it uses "subtractive" search of common cells in grid.
  """

  @doc """
  Pass the clues in an array of 16 items. This array contains the clues around the clock.
  Return 4x4 matrix, list of rows of ints.
  """

  import Bitwise

  def solve(clues) do
    {rows_hints, columns_hints} = make_hints(clues)
    make_solution(rows_hints, columns_hints)
    |> Enum.map(fn p -> Tuple.to_list(p) end)
  end

  @doc """
  Return tuple with two lists with permutations lists, first for rows, second for columns:
  {[[{},...], [{},...], [{},...], [{},...]], [[{},...], [{},...], [{},...], [{},...]]}
  Example for a real hints see in unit tests.
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
    ]

    columns_hints = [
      perms[clues_to_key(elem(cl, 0), elem(cl, 11))],
      perms[clues_to_key(elem(cl, 1), elem(cl, 10))],
      perms[clues_to_key(elem(cl, 2), elem(cl, 9))],
      perms[clues_to_key(elem(cl, 3), elem(cl, 8))]
    ]

    {rows_hints, columns_hints}
  end

  @doc """
  Returns map with keys by clues_to_key() to pairF_S and singleF_S permutations list.
  For 0,0 clue uses all permutations.
  """
  def make_permutations_table do
    perms = permutations()
    t_perms = Enum.map(perms, fn p -> List.to_tuple(p) end)
    Enum.reduce(perms, %{clues_to_key(0, 0) => t_perms}, fn perm, acc ->
      left_clue = find_left_visibility(perm)
      right_clue = find_right_visibility(perm)
      key = clues_to_key(left_clue, right_clue)
      eperm = List.to_tuple(perm)
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

  def permutations() do
    [
      [1, 2, 3, 4], [2, 1, 3, 4], [3, 1, 2, 4], [1, 3, 2, 4], [2, 3, 1, 4], [3, 2, 1, 4], [4, 2, 3, 1], [2, 4, 3, 1],
      [3, 4, 2, 1], [4, 3, 2, 1], [2, 3, 4, 1], [3, 2, 4, 1], [4, 1, 3, 2], [1, 4, 3, 2], [3, 4, 1, 2], [4, 3, 1, 2],
      [1, 3, 4, 2], [3, 1, 4, 2], [4, 1, 2, 3], [1, 4, 2, 3], [2, 4, 1, 3], [4, 2, 1, 3], [1, 2, 4, 3], [2, 1, 4, 3]
    ]
  end

  @doc """
  Recursively reduce hints lists to solution grid
  """
  def make_solution(rows_hints, columns_hints) do
    reduced_columns_hints = find_common(rows_hints, columns_hints)
    reduced_rows_hints = find_common(reduced_columns_hints, rows_hints)

    if Enum.all?(reduced_rows_hints, fn row -> length(row) <= 1 end) do
      # found solution
      Enum.concat(reduced_rows_hints)
    else
      make_solution(reduced_rows_hints, reduced_columns_hints)
    end
  end

  @doc """
  Takes rows and column hints and returns filtered list of column hints by common cells values in rows hints
  """
  def find_common(rows_hints, columns_hints) do
    # make bitmasks from rows, one masks' row example:
    # { 0b110, 0b1100, 0b10000, 0b1110}
    # Each bit in mask is for numbers 1..4, i.e. 0b110 means in this grid's cell numbers 1 or 2 are possible
    masks = Enum.map(rows_hints, fn row_hints ->
      Enum.reduce(row_hints, {0, 0, 0, 0}, fn {r0, r1, r2, r3}, {a0, a1, a2, a3} ->
        {(1 <<< r0) ||| a0, (1 <<< r1) ||| a1, (1 <<< r2) ||| a2, (1 <<< r3) ||| a3}
      end)
    end) |> List.to_tuple()

    # filter columns hints by rows masks
    columns_hints
    |> Enum.with_index()
    |> Enum.map(fn {hints, c} ->
      Enum.filter(hints, fn {c0, c1, c2, c3} ->
        ((1 <<< c0) &&& elem(elem(masks, 0), c)) != 0 and
        ((1 <<< c1) &&& elem(elem(masks, 1), c)) != 0 and
        ((1 <<< c2) &&& elem(elem(masks, 2), c)) != 0 and
        ((1 <<< c3) &&& elem(elem(masks, 3), c)) != 0
      end)
    end)
  end

  # --------- extra functions, not for kata solution ---------
  @doc """
  Converts tuple with hints lists to string for logs
  """
  def hints_to_string(hints) do
    hints
    |> Enum.with_index()
    |> Enum.map(fn {row_hints, index} ->
      "#{index}: #{length(row_hints)}-[#{Enum.join(Enum.map(row_hints, fn h -> inspect(h) end), ", ")}]"
    end)
    |> Enum.join("\n")
  end

end
