defmodule Skyscrapers4x4ImplTest do
  @moduledoc """
  Tests for internal functions of Skyscrapers4x4
  """

  use ExUnit.Case

  import Skyscrapers4x4
  import Bitwise

  @moduletag :capture_log

  doctest Skyscrapers4x4

  # Smoke test for permutations generator.
  # For 4x4 gird we may check all 24 values directly.
  test "permutations" do
    perms = permutations() |> Enum.sort()
    assert length(perms) == 24

    expected = [
      [1, 2, 3, 4],
      [2, 1, 3, 4],
      [3, 1, 2, 4],
      [1, 3, 2, 4],
      [2, 3, 1, 4],
      [3, 2, 1, 4],
      [4, 2, 3, 1],
      [2, 4, 3, 1],
      [3, 4, 2, 1],
      [4, 3, 2, 1],
      [2, 3, 4, 1],
      [3, 2, 4, 1],
      [4, 1, 3, 2],
      [1, 4, 3, 2],
      [3, 4, 1, 2],
      [4, 3, 1, 2],
      [1, 3, 4, 2],
      [3, 1, 4, 2],
      [4, 1, 2, 3],
      [1, 4, 2, 3],
      [2, 4, 1, 3],
      [4, 2, 1, 3],
      [1, 2, 4, 3],
      [2, 1, 4, 3]
    ] |> Enum.sort()

    assert perms == expected
  end

  # check solution checker on grid from kata
  test "solution checker ok" do
    source_grid = {
      {1, 3, 4, 2},
      {4, 2, 1, 3},
      {3, 4, 2, 1},
      {2, 1, 3, 4}
    }

    grid = encode_grid(source_grid)
    assert is_solution(grid) == true
  end

  test "solution checker wrong grid" do
    source_grid = {
      {2, 3, 4, 2},   # double 2
      {4, 2, 1, 3},
      {3, 4, 2, 1},
      {2, 1, 3, 4}
    }

    grid = encode_grid(source_grid)
    assert is_solution(grid) == false
  end

  test "grid to result" do
    source_grid = {
      {1, 3, 4, 2},
      {4, 2, 1, 3},
      {3, 4, 2, 1},
      {2, 1, 3, 4}
    }

    grid = encode_grid(source_grid)
    result = grid_to_result(grid)

    expected = [
      [1, 3, 4, 2],
      [4, 2, 1, 3],
      [3, 4, 2, 1],
      [2, 1, 3, 4]
    ]
    assert result == expected
  end

  test "find zero" do
    source_grid = {
      {0, 3, 4, 2},
      {4, 2, 1, 3},
      {3, 4, 2, 1},
      {2, 1, 3, 4}
    }
    rc = find_first_zero(encode_grid(source_grid))
    assert rc == {0, 0}

    source_grid = {
      {1, 0, 4, 2},
      {4, 2, 1, 3},
      {3, 4, 2, 1},
      {2, 1, 3, 4}
    }
    rc = find_first_zero(encode_grid(source_grid))
    assert rc == {0, 1}

    source_grid = {
      {1, 3, 0, 2},
      {4, 2, 1, 3},
      {3, 4, 2, 1},
      {2, 1, 3, 4}
    }
    rc = find_first_zero(encode_grid(source_grid))
    assert rc == {0, 2}

    source_grid = {
      {1, 3, 4, 0},
      {4, 2, 1, 3},
      {3, 4, 2, 1},
      {2, 1, 3, 4}
    }
    rc = find_first_zero(encode_grid(source_grid))
    assert rc == {0, 3}

    source_grid = {
      {1, 3, 4, 2},
      {4, 0, 1, 3},
      {3, 4, 2, 1},
      {2, 1, 3, 4}
    }
    rc = find_first_zero(encode_grid(source_grid))
    assert rc == {1, 1}

    source_grid = {
      {1, 3, 4, 2},
      {4, 2, 1, 3},
      {3, 4, 0, 1},
      {2, 1, 3, 4}
    }
    rc = find_first_zero(encode_grid(source_grid))
    assert rc == {2, 2}

    source_grid = {
      {1, 3, 4, 2},
      {4, 2, 1, 3},
      {3, 4, 2, 1},
      {2, 1, 3, 0}
    }
    rc = find_first_zero(encode_grid(source_grid))
    assert rc == {3, 3}

    source_grid = {
      {1, 0, 4, 2},
      {0, 2, 1, 3},
      {3, 4, 2, 1},
      {2, 1, 3, 4}
    }
    rc = find_first_zero(encode_grid(source_grid))
    assert rc == {0, 1}
  end

  test "bits to list" do
    assert bits_to_list(0) == []
    assert bits_to_list(1) == []
    assert bits_to_list(0b00000010) == [1]
    assert bits_to_list(0b00000100) == [2]
    assert bits_to_list(0b00001000) == [3]
    assert bits_to_list(0b00010000) == [4]
    assert bits_to_list(0b00011110) == [1, 2, 3, 4]
  end

  # For variants we use partial solutions starting from zero grid
  test "variants finder" do
    grid = encode_grid({
      {0, 0, 0, 0},
      {0, 0, 0, 0},
      {0, 0, 0, 0},
      {0, 0, 0, 0}
    })

    rc = find_first_zero(grid)
    variants = get_variants(grid, rc)
    assert variants == [1, 2, 3, 4]

    grid = encode_grid({
      {1, 2, 3, 0},
      {0, 0, 0, 0},
      {0, 0, 0, 0},
      {0, 0, 0, 0}
    })

    rc = find_first_zero(grid)
    variants = get_variants(grid, rc)
    assert variants == [4]

    grid = encode_grid({
      {1, 2, 3, 4},
      {2, 1, 0, 0},
      {0, 0, 0, 0},
      {0, 0, 0, 0}
    })

    rc = find_first_zero(grid)
    variants = get_variants(grid, rc)
    assert variants == [4]

    grid = encode_grid({
      {1, 2, 3, 4},
      {2, 1, 4, 3},
      {3, 4, 1, 2},
      {4, 3, 2, 0}
    })

    rc = find_first_zero(grid)
    variants = get_variants(grid, rc)
    assert variants == [1]
  end


  # Base algorithm on zero source grid
  test "solution finder" do
    grid = encode_grid({
      {0, 0, 0, 0},
      {0, 0, 0, 0},
      {0, 0, 0, 0},
      {0, 0, 0, 0}
    })
    {res, solution} = find_solution(grid)

    assert res == :found

    result = grid_to_result(solution)
    # hand made solution from zero grid
    expected = [
      [1, 2, 3, 4],
      [2, 1, 4, 3],
      [3, 4, 1, 2],
      [4, 3, 2, 1]
    ]

    assert result == expected
  end

  test "impossible solution" do
    grid = encode_grid({
      {4, 4, 4, 4},   # wrong row on start
      {0, 0, 0, 0},
      {0, 0, 0, 0},
      {0, 0, 0, 0}
    })
    {res, _} = find_solution(grid)

    assert res == :no
  end

  test "columns" do
    grid = encode_grid({
      {1, 0, 0, 0},
      {2, 0, 0, 0},
      {3, 0, 0, 0},
      {4, 0, 0, 0}
    })

    column = get_column(grid, 0)
    assert column == 0b100011010001

    grid = set_column(grid, column, 1)
    result = grid_to_result(grid)
    expected = [
      [1, 1, 0, 0],
      [2, 2, 0, 0],
      [3, 3, 0, 0],
      [4, 4, 0, 0]
    ]
    assert result == expected

    column = 0b001010011100
    grid = set_column(grid, column, 2)
    result = grid_to_result(grid)
    expected = [
      [1, 1, 4, 0],
      [2, 2, 3, 0],
      [3, 3, 2, 0],
      [4, 4, 1, 0]
    ]
    assert result == expected

    column = 0b001100011010
    grid = set_column(grid, column, 3)
    result = grid_to_result(grid)
    expected = [
      [1, 1, 4, 2],
      [2, 2, 3, 3],
      [3, 3, 2, 4],
      [4, 4, 1, 1]
    ]
    assert result == expected
  end

  test "make permutations table" do
    perms = make_permutations_table()
    expected = %{
      # pairs
      clues_to_key(1, 2) => encode_rows_list([{4, 1, 2, 3}, {4, 2, 1, 3}]),
      clues_to_key(1, 3) => encode_rows_list([{4, 2, 3, 1}, {4, 1, 3, 2}, {4, 3, 1, 2}]),
      clues_to_key(1, 4) => encode_rows_list([{4, 3, 2, 1}]),

      clues_to_key(2, 1) => encode_rows_list([{3, 1, 2, 4}, {3, 2, 1, 4}]),
      clues_to_key(2, 2) => encode_rows_list([{3, 2, 4, 1}, {3, 4, 1, 2}, {3, 1, 4, 2}, {1, 4, 2, 3}, {2, 4, 1, 3}, {2, 1, 4, 3}]),
      clues_to_key(2, 3) => encode_rows_list([{2, 4, 3, 1}, {3, 4, 2, 1}, {1, 4, 3, 2}]),

      clues_to_key(3, 1) => encode_rows_list([{2, 1, 3, 4}, {1, 3, 2, 4}, {2, 3, 1, 4}]),
      clues_to_key(3, 2) => encode_rows_list([{2, 3, 4, 1}, {1, 3, 4, 2}, {1, 2, 4, 3}]),
      clues_to_key(4, 1) => encode_rows_list([{1, 2, 3, 4}]),

      # singles
      clues_to_key(1, 0) => encode_rows_list([{4, 2, 3, 1}, {4, 3, 2, 1}, {4, 1, 3, 2}, {4, 3, 1, 2}, {4, 1, 2, 3}, {4, 2, 1, 3}]),
      clues_to_key(2, 0) => encode_rows_list([{3, 1, 2, 4}, {3, 2, 1, 4}, {2, 4, 3, 1}, {3, 4, 2, 1}, {3, 2, 4, 1}, {1, 4, 3, 2},
        {3, 4, 1, 2}, {3, 1, 4, 2}, {1, 4, 2, 3}, {2, 4, 1, 3}, {2, 1, 4, 3}]),
      clues_to_key(3, 0) => encode_rows_list([{2, 1, 3, 4}, {1, 3, 2, 4}, {2, 3, 1, 4}, {2, 3, 4, 1}, {1, 3, 4, 2}, {1, 2, 4, 3}]),
      clues_to_key(4, 0) => encode_rows_list([{1, 2, 3, 4}]),

      clues_to_key(0, 1) => encode_rows_list([{1, 2, 3, 4}, {2, 1, 3, 4}, {3, 1, 2, 4}, {1, 3, 2, 4}, {2, 3, 1, 4}, {3, 2, 1, 4}]),
      clues_to_key(0, 2) => encode_rows_list([{2, 3, 4, 1}, {3, 2, 4, 1}, {3, 4, 1, 2}, {1, 3, 4, 2}, {3, 1, 4, 2}, {4, 1, 2, 3},
        {1, 4, 2, 3}, {2, 4, 1, 3}, {4, 2, 1, 3}, {1, 2, 4, 3}, {2, 1, 4, 3}]),
      clues_to_key(0, 3) => encode_rows_list([{4, 2, 3, 1}, {2, 4, 3, 1}, {3, 4, 2, 1}, {4, 1, 3, 2}, {1, 4, 3, 2}, {4, 3, 1, 2}]),
      clues_to_key(0, 4) => encode_rows_list([{4, 3, 2, 1}]),

      # zero
      clues_to_key(0, 0) => []
    }

    perms_str = permutations_table_to_string(perms)
    expected_str = permutations_table_to_string(expected)
    assert perms_str == expected_str
  end

  test "encode permutation" do
    assert 0b100011010001 == encode_permutation([1, 2, 3, 4])
    assert 0 == encode_permutation([])
  end

  test "find left visibility" do
    left_1 =[ [4, 2, 3, 1], [4, 3, 2, 1], [4, 1, 3, 2], [4, 3, 1, 2], [4, 1, 2, 3], [4, 2, 1, 3]]
    Enum.each(left_1, fn p -> assert find_left_visibility(p) == 1 end)

    left_2 =  [
      [3, 1, 2, 4], [3, 2, 1, 4], [2, 4, 3, 1], [3, 4, 2, 1], [3, 2, 4, 1], [1, 4, 3, 2],
      [3, 4, 1, 2], [3, 1, 4, 2], [1, 4, 2, 3], [2, 4, 1, 3], [2, 1, 4, 3]
    ]
    Enum.each(left_2, fn p -> assert find_left_visibility(p) == 2 end)

    left_3 = [[2, 1, 3, 4], [1, 3, 2, 4], [2, 3, 1, 4], [2, 3, 4, 1], [1, 3, 4, 2], [1, 2, 4, 3]]
    Enum.each(left_3, fn p -> assert find_left_visibility(p) == 3 end)

    assert find_left_visibility([1, 2, 3, 4]) == 4
  end

  test "find right visibility" do
    right_1 = [[1, 2, 3, 4], [2, 1, 3, 4], [3, 1, 2, 4], [1, 3, 2, 4], [2, 3, 1, 4], [3, 2, 1, 4]]
    Enum.each(right_1, fn p -> assert find_right_visibility(p) == 1 end)

    right_2 = [[2, 3, 4, 1], [3, 2, 4, 1], [3, 4, 1, 2], [1, 3, 4, 2], [3, 1, 4, 2], [4, 1, 2, 3],
      [1, 4, 2, 3], [2, 4, 1, 3], [4, 2, 1, 3], [1, 2, 4, 3], [2, 1, 4, 3]]
    Enum.each(right_2, fn p -> assert find_right_visibility(p) == 2 end)

    right_3 = [[4, 2, 3, 1], [2, 4, 3, 1], [3, 4, 2, 1], [4, 1, 3, 2], [1, 4, 3, 2], [4, 3, 1, 2]]
    Enum.each(right_3, fn p -> assert find_right_visibility(p) == 3 end)

    assert find_right_visibility([4, 3, 2, 1]) == 4
  end

  test "row is not suitable" do
    grid = encode_grid({
      {0, 0, 0, 0},
      {1, 2, 4, 3},
      {0, 0, 0, 0},
      {0, 0, 0, 0}
    })

    # with this row 1 and 3 columns will get double 2 and 3, so this row is not suitable for given grid
    row = encode_row({4, 2, 1, 3})

    assert is_row_suitable(grid, row, 2) == false
  end

  test "unique columns good" do
    good_grids = [
      {
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
      },
      {
        {1, 0, 0, 0},
        {0, 1, 0, 0},
        {0, 0, 1, 0},
        {0, 0, 0, 1}
      },
      {
        {1, 2, 3, 4},
        {2, 1, 4, 3},
        {3, 4, 1, 2},
        {4, 3, 2, 1}
      },
    ]

    Enum.each(good_grids, fn grid -> assert columns_have_unique_numbers(encode_grid(grid)) == true end )
  end

  test "unique columns bad" do
    bad_grids = [
      {
        {0, 0, 0, 0},
        {1, 0, 0, 0},
        {1, 0, 0, 0},
        {0, 0, 0, 0}
      },
      {
        {1, 0, 0, 0},
        {0, 2, 0, 0},
        {0, 2, 1, 0},
        {0, 0, 0, 1}
      },
      {
        {1, 0, 0, 0},
        {0, 2, 1, 0},
        {0, 2, 1, 0},
        {0, 0, 0, 1}
      },
      {
        {1, 0, 0, 0},
        {0, 2, 0, 0},
        {0, 3, 1, 4},
        {0, 0, 0, 4}
      },
      {
        {1, 2, 3, 4},
        {0, 1, 0, 0},
        {0, 3, 1, 4},
        {0, 0, 0, 1}
      },
      { # a real case in the old hints generator
        {0, 0, 0, 0},
        {1, 2, 4, 3},
        {4, 2, 1, 3},
        {0, 0, 0, 1}
      },
      {
        {1, 2, 3, 4},
        {2, 4, 4, 3},
        {3, 4, 1, 2},
        {4, 3, 2, 1}
      },
    ]

    Enum.each(bad_grids, fn grid -> assert columns_have_unique_numbers(encode_grid(grid)) == false end )
  end

  #---------- additional functions ----------

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

  def encode_rows_list(rows) do
    Enum.map(rows, fn row -> encode_row(row) end )
  end

  defp permutations_table_to_string(pt) do
    Enum.map(pt, fn {k, v} ->
      "#{Integer.to_string((k >>> 4) &&& 0xF, 16)}#{Integer.to_string(k &&& 0xF, 16)}: " <>
      (
        v |> Enum.map(fn p -> row_to_string(p) end)
        |> Enum.sort()
        |> Enum.join(", ")
        )
    end)
    |> Enum.join("\n")
  end

end
