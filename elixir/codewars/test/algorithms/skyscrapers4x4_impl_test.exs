defmodule Skyscrapers4x4ImplTest do
  @moduledoc """
  Tests for internal functions of Skyscrapers4x4
  """

  use ExUnit.Case

  import Skyscrapers4x4

  @moduletag :capture_log

  doctest Skyscrapers4x4

  # Smoke test for permutations generator.
  # For 4x4 gird we may check all 24 values directly.
  test "permutations" do
    permutations = permutations()

    assert length(permutations) == 24

    perms_str = permutations |> Enum.map(fn r -> row_to_string(r) end) |> Enum.join("\n")

    assert perms_str == "1, 2, 3, 4\n" <>
      "2, 1, 3, 4\n" <>
      "3, 1, 2, 4\n" <>
      "1, 3, 2, 4\n" <>
      "2, 3, 1, 4\n" <>
      "3, 2, 1, 4\n" <>
      "4, 2, 3, 1\n" <>
      "2, 4, 3, 1\n" <>
      "3, 4, 2, 1\n" <>
      "4, 3, 2, 1\n" <>
      "2, 3, 4, 1\n" <>
      "3, 2, 4, 1\n" <>
      "4, 1, 3, 2\n" <>
      "1, 4, 3, 2\n" <>
      "3, 4, 1, 2\n" <>
      "4, 3, 1, 2\n" <>
      "1, 3, 4, 2\n" <>
      "3, 1, 4, 2\n" <>
      "4, 1, 2, 3\n" <>
      "1, 4, 2, 3\n" <>
      "2, 4, 1, 3\n" <>
      "4, 2, 1, 3\n" <>
      "1, 2, 4, 3\n" <>
      "2, 1, 4, 3"
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
    assert check_solution(grid) == true
  end

  test "solution checker wrong grid" do
    source_grid = {
      {2, 3, 4, 2},
      {4, 2, 1, 3},
      {3, 4, 2, 1},
      {2, 1, 3, 4}
    }

    grid = encode_grid(source_grid)
    assert check_solution(grid) == false
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

end
