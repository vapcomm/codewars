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
    permutations = permutations()

    assert length(permutations) == 24

    perms_str = permutations |> Enum.map(fn r -> row_to_string(r) end) |> Enum.join("\n")

    assert perms_str == "1 2 3 4\n" <>
      "2 1 3 4\n" <>
      "3 1 2 4\n" <>
      "1 3 2 4\n" <>
      "2 3 1 4\n" <>
      "3 2 1 4\n" <>
      "4 2 3 1\n" <>
      "2 4 3 1\n" <>
      "3 4 2 1\n" <>
      "4 3 2 1\n" <>
      "2 3 4 1\n" <>
      "3 2 4 1\n" <>
      "4 1 3 2\n" <>
      "1 4 3 2\n" <>
      "3 4 1 2\n" <>
      "4 3 1 2\n" <>
      "1 3 4 2\n" <>
      "3 1 4 2\n" <>
      "4 1 2 3\n" <>
      "1 4 2 3\n" <>
      "2 4 1 3\n" <>
      "4 2 1 3\n" <>
      "1 2 4 3\n" <>
      "2 1 4 3"
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

    expected = [ [1, 3, 4, 2],
      [4, 2, 1, 3],
      [3, 4, 2, 1],
      [2, 1, 3, 4] ]
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


end
