defmodule Skyscrapers4x4FastImplTest do
  use ExUnit.Case

  import Skyscrapers4x4Fast
  import Bitwise

  @moduletag :capture_log

# clues:
#    2 2 1 3
# 3| 0 0 0 0 |2
# 2| 0 0 0 0 |2
# 1| 0 0 0 0 |3
# 3| 0 0 0 0 |1
#    3 2 2 1

  # first attempt, first phase
  test "find common 1/1" do
    rows_hints = [
      [{1, 2, 4, 3}, {1, 3, 4, 2}, {2, 3, 4, 1}],
      [{4, 1, 2, 3}, {4, 2, 1, 3}],
      [{1, 4, 3, 2}, {2, 4, 3, 1}, {3, 4, 2, 1}],
      [{1, 3, 2, 4}, {2, 1, 3, 4}, {2, 3, 1, 4}],
    ]

    columns_hints = [
      [{1, 4, 3, 2}, {2, 4, 3, 1}, {3, 4, 2, 1}],
      [{1, 4, 2, 3}, {2, 1, 4, 3}, {2, 4, 1, 3}, {3, 1, 4, 2}, {3, 2, 4, 1}, {3, 4, 1, 2}],
      [{4, 1, 2, 3}, {4, 2, 1, 3}],
      [{1, 3, 2, 4}, {2, 1, 3, 4}, {2, 3, 1, 4}]
    ]

    expected = [
      [{1, 4, 3, 2}, {2, 4, 3, 1}],
      [{2, 1, 4, 3}, {3, 2, 4, 1}],
      [{4, 1, 2, 3}],
      [{1, 3, 2, 4}, {2, 3, 1, 4}]
    ]

    assert find_common(rows_hints, columns_hints) == expected
  end

  # first attempt, second phase
  test "find common 1/2" do
    columns_left = [
      [{1, 4, 3, 2}, {2, 4, 3, 1}],
      [{2, 1, 4, 3}, {3, 2, 4, 1}],
      [{4, 1, 2, 3}],
      [{1, 3, 2, 4}, {2, 3, 1, 4}]
    ]

    rows_hints = [
      [{1, 2, 4, 3}, {1, 3, 4, 2}, {2, 3, 4, 1}],
      [{4, 1, 2, 3}, {4, 2, 1, 3}],
      [{1, 4, 3, 2}, {2, 4, 3, 1}, {3, 4, 2, 1}],
      [{1, 3, 2, 4}, {2, 1, 3, 4}, {2, 3, 1, 4}],
    ]

    # second phase result
    expected = [
      [{1, 3, 4, 2}, {2, 3, 4, 1}],
      [{4, 2, 1, 3}],
      [{3, 4, 2, 1}],
      [{2, 1, 3, 4}]
    ]
    assert find_common(columns_left, rows_hints) == expected
  end

  # second attempt, first phase
  test "find common 2/1" do
    rows_hints = [
      [{1, 3, 4, 2}, {2, 3, 4, 1}],
      [{4, 2, 1, 3}],
      [{3, 4, 2, 1}],
      [{2, 1, 3, 4}]
    ]

    columns_hints = [
      [{1, 4, 3, 2}, {2, 4, 3, 1}],
      [{2, 1, 4, 3}, {3, 2, 4, 1}],
      [{4, 1, 2, 3}],
      [{1, 3, 2, 4}, {2, 3, 1, 4}]
    ]

    expected = [
      [{1, 4, 3, 2}],
      [{3, 2, 4, 1}],
      [{4, 1, 2, 3}],
      [{2, 3, 1, 4}]
    ]

    assert find_common(rows_hints, columns_hints) == expected
  end

  # second attempt, second phase
  test "find common 2/2" do
    columns_left = [
      [{1, 4, 3, 2}],
      [{3, 2, 4, 1}],
      [{4, 1, 2, 3}],
      [{2, 3, 1, 4}]
    ]

    rows_hints = [
      [{1, 3, 4, 2}, {2, 3, 4, 1}],
      [{4, 2, 1, 3}],
      [{3, 4, 2, 1}],
      [{2, 1, 3, 4}]
    ]

    # final result
    expected = [
      [{1, 3, 4, 2}],
      [{4, 2, 1, 3}],
      [{3, 4, 2, 1}],
      [{2, 1, 3, 4}]
    ]

    assert find_common(columns_left, rows_hints) == expected
  end

  test "make solution simple" do
    rows_hints = [
      [{1, 2, 4, 3}, {1, 3, 4, 2}, {2, 3, 4, 1}],
      [{4, 1, 2, 3}, {4, 2, 1, 3}],
      [{1, 4, 3, 2}, {2, 4, 3, 1}, {3, 4, 2, 1}],
      [{1, 3, 2, 4}, {2, 1, 3, 4}, {2, 3, 1, 4}],
    ]

    columns_hints = [
      [{1, 4, 3, 2}, {2, 4, 3, 1}, {3, 4, 2, 1}],
      [{1, 4, 2, 3}, {2, 1, 4, 3}, {2, 4, 1, 3}, {3, 1, 4, 2}, {3, 2, 4, 1}, {3, 4, 1, 2}],
      [{4, 1, 2, 3}, {4, 2, 1, 3}],
      [{1, 3, 2, 4}, {2, 1, 3, 4}, {2, 3, 1, 4}]
    ]

    # final solution
    expected = [
      {1, 3, 4, 2},
      {4, 2, 1, 3},
      {3, 4, 2, 1},
      {2, 1, 3, 4}
    ]

    assert make_solution(rows_hints, columns_hints) == expected
  end

  test "make permutations table" do
    expected = %{
      # pairs
      clues_to_key(1, 2) => [{4, 1, 2, 3}, {4, 2, 1, 3}],
      clues_to_key(1, 3) => [{4, 2, 3, 1}, {4, 1, 3, 2}, {4, 3, 1, 2}],
      clues_to_key(1, 4) => [{4, 3, 2, 1}],

      clues_to_key(2, 1) => [{3, 1, 2, 4}, {3, 2, 1, 4}],
      clues_to_key(2, 2) => [{3, 2, 4, 1}, {3, 4, 1, 2}, {3, 1, 4, 2}, {1, 4, 2, 3}, {2, 4, 1, 3}, {2, 1, 4, 3}],
      clues_to_key(2, 3) => [{2, 4, 3, 1}, {3, 4, 2, 1}, {1, 4, 3, 2}],

      clues_to_key(3, 1) => [{2, 1, 3, 4}, {1, 3, 2, 4}, {2, 3, 1, 4}],
      clues_to_key(3, 2) => [{2, 3, 4, 1}, {1, 3, 4, 2}, {1, 2, 4, 3}],
      clues_to_key(4, 1) => [{1, 2, 3, 4}],

      # singles
      clues_to_key(1, 0) => [{4, 2, 3, 1}, {4, 3, 2, 1}, {4, 1, 3, 2}, {4, 3, 1, 2}, {4, 1, 2, 3}, {4, 2, 1, 3}],
      clues_to_key(2, 0) => [{3, 1, 2, 4}, {3, 2, 1, 4}, {2, 4, 3, 1}, {3, 4, 2, 1}, {3, 2, 4, 1}, {1, 4, 3, 2},
        {3, 4, 1, 2}, {3, 1, 4, 2}, {1, 4, 2, 3}, {2, 4, 1, 3}, {2, 1, 4, 3}],
      clues_to_key(3, 0) => [{2, 1, 3, 4}, {1, 3, 2, 4}, {2, 3, 1, 4}, {2, 3, 4, 1}, {1, 3, 4, 2}, {1, 2, 4, 3}],
      clues_to_key(4, 0) => [{1, 2, 3, 4}],

      clues_to_key(0, 1) => [{1, 2, 3, 4}, {2, 1, 3, 4}, {3, 1, 2, 4}, {1, 3, 2, 4}, {2, 3, 1, 4}, {3, 2, 1, 4}],
      clues_to_key(0, 2) => [{2, 3, 4, 1}, {3, 2, 4, 1}, {3, 4, 1, 2}, {1, 3, 4, 2}, {3, 1, 4, 2}, {4, 1, 2, 3},
        {1, 4, 2, 3}, {2, 4, 1, 3}, {4, 2, 1, 3}, {1, 2, 4, 3}, {2, 1, 4, 3}],
      clues_to_key(0, 3) => [{4, 2, 3, 1}, {2, 4, 3, 1}, {3, 4, 2, 1}, {4, 1, 3, 2}, {1, 4, 3, 2}, {4, 3, 1, 2}],
      clues_to_key(0, 4) => [{4, 3, 2, 1}],

      # zero
      clues_to_key(0, 0) => Enum.map(permutations(), fn p -> List.to_tuple(p) end)
    }

    perms = make_permutations_table()
    perms_str = permutations_table_to_string(perms)
    expected_str = permutations_table_to_string(expected)
    assert perms_str == expected_str
  end

  defp permutations_table_to_string(pt) do
    Enum.map(pt, fn {k, v} ->
      "#{Integer.to_string((k >>> 4) &&& 0xF, 16)}#{Integer.to_string(k &&& 0xF, 16)}: " <>
      (
        v |> Enum.map(fn p -> inspect(p) end)
        |> Enum.sort()
        |> Enum.join(", ")
        )
    end)
    |> Enum.join("\n")
  end

  test "make hints simple" do
    clues = [
      2, 2, 1, 3,
      2, 2, 3, 1,
      1, 2, 2, 3,
      3, 2, 1, 3
    ]

    {rows_hints, columns_hints} = make_hints(clues)

    #NOTE: expected hints are differ in sequence a little bit from previous test hints due different permutations lists.
    #      These hints use our permutations table.
    expected_rows_hints = [
      [{1, 2, 4, 3}, {1, 3, 4, 2}, {2, 3, 4, 1}],
      [{4, 2, 1, 3}, {4, 1, 2, 3}],
      [{1, 4, 3, 2}, {3, 4, 2, 1}, {2, 4, 3, 1}],
      [{2, 3, 1, 4}, {1, 3, 2, 4}, {2, 1, 3, 4}],
    ]

    assert rows_hints == expected_rows_hints

    expected_columns_hints = [
      [{1, 4, 3, 2}, {3, 4, 2, 1}, {2, 4, 3, 1}],
      [{2, 1, 4, 3}, {2, 4, 1, 3}, {1, 4, 2, 3}, {3, 1, 4, 2}, {3, 4, 1, 2}, {3, 2, 4, 1}],
      [{4, 2, 1, 3}, {4, 1, 2, 3}],
      [{2, 3, 1, 4}, {1, 3, 2, 4}, {2, 1, 3, 4}]
    ]

    assert columns_hints == expected_columns_hints
  end

end
