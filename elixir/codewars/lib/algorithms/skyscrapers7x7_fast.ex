defmodule Skyscrapers7x7Fast do
  @moduledoc """
  7 By 7 Skyscrapers -- 1 kyu
  https://www.codewars.com/kata/5917a2205ffc30ec3a0000a8/train/elixir

  This is a fast "subtractive" variant of the solution, it uses ideas from farhadi hints intersection algorithm.
  See Skyscrapers4x4Fast and tests for details.
  """

  import Bitwise

  @doc """
  Pass the clues in an array of 28 items. This array contains the clues around the clock.
  Return 7x7 matrix, list of rows of ints.
  """
  def solve(clues) do
    {rows_hints, columns_hints} = make_hints(clues)
    make_solution(rows_hints, columns_hints)
    |> Enum.map(fn p -> Tuple.to_list(p) end)
  end

  @doc """
  Return tuple with two lists with permutations lists, first for rows, second for columns:
  first for rows, second for columns:
  {[[{},...], [{},...], ...], [[{},...], [{},...], ...]}
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
    ]

    columns_hints = [
      perms[clues_to_key(elem(cl, 0), elem(cl, 20))],
      perms[clues_to_key(elem(cl, 1), elem(cl, 19))],
      perms[clues_to_key(elem(cl, 2), elem(cl, 18))],
      perms[clues_to_key(elem(cl, 3), elem(cl, 17))],
      perms[clues_to_key(elem(cl, 4), elem(cl, 16))],
      perms[clues_to_key(elem(cl, 5), elem(cl, 15))],
      perms[clues_to_key(elem(cl, 6), elem(cl, 14))],
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
      t_perm = List.to_tuple(perm)
      {_, pair_result} = Map.get_and_update(acc, key, fn perms_list -> update_perms_list(perms_list, t_perm) end)

      left_key = clues_to_key(left_clue, 0)
      {_, left_result} = Map.get_and_update(pair_result, left_key, fn perms_list -> update_perms_list(perms_list, t_perm) end)

      right_key = clues_to_key(0, right_clue)
      {_, result} = Map.get_and_update(left_result, right_key, fn perms_list -> update_perms_list(perms_list, t_perm) end)

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
  Generate all permutations of 1..7, return list of permutations as numbers lists
  """
  def permutations(), do: permutations([1, 2, 3, 4, 5, 6, 7])
  def permutations([i]), do: [[i]]
  def permutations(list) do
    for h <- list, t <- permutations(list -- [h]), do: [h | t]
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
    # { 0b110, 0b1100, 0b10000, 0b1110, 0b110000, 0b11111110, 0b10000000}
    # Each "one" bit in mask is for numbers 1..7, i.e. 0b110 means in this grid's cell numbers 1 or 2 are possible.
    masks = Enum.map(rows_hints, fn row_hints ->
      Enum.reduce(row_hints, {0, 0, 0, 0, 0, 0, 0}, fn {r0, r1, r2, r3, r4, r5, r6}, {a0, a1, a2, a3, a4, a5, a6} ->
        {
          (1 <<< r0) ||| a0, (1 <<< r1) ||| a1, (1 <<< r2) ||| a2, (1 <<< r3) ||| a3,
          (1 <<< r4) ||| a4, (1 <<< r5) ||| a5, (1 <<< r6) ||| a6
        }
      end)
    end) |> List.to_tuple()

    # filter columns hints by rows masks
    columns_hints
    |> Enum.with_index()
    |> Enum.map(fn {hints, c} ->
      Enum.filter(hints, fn {c0, c1, c2, c3, c4, c5, c6} ->
        ((1 <<< c0) &&& elem(elem(masks, 0), c)) != 0 and
        ((1 <<< c1) &&& elem(elem(masks, 1), c)) != 0 and
        ((1 <<< c2) &&& elem(elem(masks, 2), c)) != 0 and
        ((1 <<< c3) &&& elem(elem(masks, 3), c)) != 0 and
        ((1 <<< c4) &&& elem(elem(masks, 4), c)) != 0 and
        ((1 <<< c5) &&& elem(elem(masks, 5), c)) != 0 and
        ((1 <<< c6) &&& elem(elem(masks, 6), c)) != 0
      end)
    end)
  end

end
