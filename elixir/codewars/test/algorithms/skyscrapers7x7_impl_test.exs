defmodule Skyscrapers7x7ImplTest do
  @moduledoc """
  Tests for internal functions of Skyscrapers4x4
  """

  use ExUnit.Case

  import Skyscrapers7x7
  import Bitwise

  @moduletag :capture_log

  test "solution finder" do
    grid = encode_grid({
      {1,5,6,7,4,3,2},
      {2,7,4,5,3,1,6},
      {3,4,5,6,7,2,1},
      {4,6,3,1,2,7,5},
      {5,3,1,2,6,4,7},
      {6,2,7,3,1,5,4},
      {7,1,2,4,5,6,3}
    })

    {result, solution} = find_solution(grid)
    assert result == :found
    assert solution == grid

    {result, solution} = find_solution(
      encode_grid({
        {0,5,6,7,4,3,2},
        {0,7,4,5,3,1,6},
        {3,4,5,6,7,2,1},
        {4,6,3,1,0,7,5},
        {5,3,1,2,6,4,7},
        {6,2,7,3,1,5,4},
        {7,1,2,4,5,6,0}
      })
    )
    assert result == :found
    assert solution == grid
  end

  test "find solution from zero grid" do
    {result, solution} = find_solution(
      encode_grid({
        {0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0}
      })
    )
    assert result == :found

    expected = encode_grid({
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0}
    })

    assert solution == expected
  end

  test "find zero" do
    source_grid = {
      {1,5,6,7,4,3,2},
      {2,7,4,5,3,1,6},
      {3,4,5,6,7,2,1},
      {4,6,3,1,2,7,5},
      {5,3,1,2,6,4,7},
      {6,2,7,3,1,5,4},
      {7,1,2,4,5,6,3}
    }
    assert find_first_zero(encode_grid(source_grid)) == -1

    source_grid = {
      {0,5,6,7,4,3,2},
      {2,7,4,5,3,1,6},
      {3,4,5,6,7,2,1},
      {4,6,3,1,2,7,5},
      {5,3,1,2,6,4,7},
      {6,2,7,3,1,5,4},
      {7,1,2,4,5,6,3}
    }
    assert find_first_zero(encode_grid(source_grid)) == 0

    source_grid = {
      {1,5,6,7,4,3,2},
      {0,7,4,5,3,1,6},
      {3,4,5,6,7,2,1},
      {4,6,3,1,2,7,5},
      {5,3,1,2,6,4,7},
      {6,2,7,3,1,5,4},
      {7,1,2,4,5,6,3}
    }
    assert find_first_zero(encode_grid(source_grid)) == 0x10

    source_grid = {
      {1,5,6,7,4,3,2},
      {2,7,4,5,3,1,6},
      {3,4,5,0,7,2,1},
      {4,6,3,0,2,7,5},
      {5,3,1,2,0,4,7},
      {6,2,7,3,1,5,4},
      {7,1,2,4,5,6,3}
    }
    assert find_first_zero(encode_grid(source_grid)) == 0x23

    source_grid = {
      {1,5,6,7,4,3,2},
      {2,7,4,5,3,1,6},
      {3,4,5,6,7,2,1},
      {4,6,3,1,2,7,5},
      {5,3,1,2,6,4,7},
      {6,2,7,3,1,5,4},
      {7,1,2,4,5,0,0}
    }
    assert find_first_zero(encode_grid(source_grid)) == 0x65

    source_grid = {
      {1,5,6,7,4,3,2},
      {2,7,4,5,3,1,6},
      {3,4,5,6,7,2,1},
      {4,6,3,1,2,7,5},
      {5,3,1,2,6,4,7},
      {6,2,7,3,1,5,4},
      {7,1,2,4,5,6,0}
    }
    assert find_first_zero(encode_grid(source_grid)) == 0x66

  end

  test "bits to list" do
    assert bits_to_list(0) == []
    assert bits_to_list(1) == []
    assert bits_to_list(0b00000010) == [1]
    assert bits_to_list(0b00000100) == [2]
    assert bits_to_list(0b00001000) == [3]
    assert bits_to_list(0b00010000) == [4]
    assert bits_to_list(0b00011110) == [1, 2, 3, 4]
    assert bits_to_list(0b11111110) == [1, 2, 3, 4, 5, 6, 7]
  end

  # For variants we use partial solutions starting from zero grid
  test "variants finder" do
    grid = encode_grid({
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0}
    })

    rc = find_first_zero(grid)
    variants = get_variants(grid, rc)
    assert variants == [1, 2, 3, 4, 5, 6, 7]

    grid = encode_grid({
      {1, 2, 3, 4, 5, 6, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0}
    })

    rc = find_first_zero(grid)
    variants = get_variants(grid, rc)
    assert variants == [7]

    grid = encode_grid({
      {1, 2, 3, 4, 5, 6, 7},
      {2, 1, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0}
    })

    rc = find_first_zero(grid)
    variants = get_variants(grid, rc)
    assert variants == [4, 5, 6, 7]

    grid = encode_grid({
      {1, 5, 6, 7, 4, 3, 2},
      {2, 7, 4, 5, 3, 1, 6},
      {3, 4, 5, 6, 7, 2, 1},
      {4, 6, 3, 1, 2, 7, 5},
      {5, 3, 1, 2, 6, 4, 7},
      {6, 2, 7, 3, 1, 5, 4},
      {7, 1, 2, 4, 5, 6, 0}
    })

    rc = find_first_zero(grid)
    variants = get_variants(grid, rc)
    assert variants == [3]
  end

  # check solution checker on grid from kata
  test "solution checker ok" do
    source_grid = {
      {1, 5, 6, 7, 4, 3, 2},
      {2, 7, 4, 5, 3, 1, 6},
      {3, 4, 5, 6, 7, 2, 1},
      {4, 6, 3, 1, 2, 7, 5},
      {5, 3, 1, 2, 6, 4, 7},
      {6, 2, 7, 3, 1, 5, 4},
      {7, 1, 2, 4, 5, 6, 3}
    }

    grid = encode_grid(source_grid)
    assert is_solution(grid) == true
  end

  test "solution checker wrong grid" do
    grid = encode_grid( {
      {1, 5, 6, 7, 4, 3, 7},  # double 7
      {2, 7, 4, 5, 3, 1, 6},
      {3, 4, 5, 6, 7, 2, 1},
      {4, 6, 3, 1, 2, 7, 5},
      {5, 3, 1, 2, 6, 4, 7},
      {6, 2, 7, 3, 1, 5, 4},
      {7, 1, 2, 4, 5, 6, 3}
    })
    assert is_solution(grid) == false

    grid = encode_grid({
      {1, 5, 6, 7, 4, 3, 2},
      {2, 7, 4, 5, 3, 1, 6},
      {3, 4, 5, 6, 7, 2, 1},
      {4, 6, 3, 1, 2, 7, 5},
      {5, 3, 1, 2, 6, 4, 7},
      {6, 2, 7, 3, 1, 5, 4},
      {7, 1, 2, 4, 5, 6, 0}   # 0
    })
    assert is_solution(grid) == false
  end

  @tag :skip
  test "find_zero_in_row benchmark" do
    row = encode_row({7, 1, 2, 4, 5, 6, 0})

    {time, _} = :timer.tc(fn ->
      assert Enum.each(1..10000000, fn _ ->
        find_zero_in_row(row)
        find_zero_in_row(row)
        find_zero_in_row(row)
        find_zero_in_row(row)
        find_zero_in_row(row)
        find_zero_in_row(row)
        find_zero_in_row(row)
        find_zero_in_row(row)
        find_zero_in_row(row)
        find_zero_in_row(row)
      end) == :ok
    end)
    IO.puts("find_zero_in_row benchmark execution time: #{time / 1000} ms")
  end


  #---------- additional functions ----------

  @doc """
  Encode source grid from tuple of tuples to grid
  """
  def encode_grid(source) do
    {
      encode_row(elem(source, 0)),
      encode_row(elem(source, 1)),
      encode_row(elem(source, 2)),
      encode_row(elem(source, 3)),
      encode_row(elem(source, 4)),
      encode_row(elem(source, 5)),
      encode_row(elem(source, 6))
    }
  end

  @doc """
  Encode row tuple to integer.
  One row 1, 2, 3, 4, 5, 6, 7 is encoded in one integer, 4 bits for one number 1..7, the first number in a tuple
  encoded in 4 least significant bits, and so on:
  ```
  7654321098765432109876543210  -- bits numbers
     7   6   5   4   3   2   1  -- encoded numbers from one grid row (one permutation)
  ```
  """
  def encode_row(row) do
    (elem(row, 6) <<< 24) ||| (elem(row, 5) <<< 20) ||| (elem(row, 4) <<< 16) |||
    (elem(row, 3) <<< 12) ||| (elem(row, 2) <<< 8) ||| (elem(row, 1) <<< 4) ||| elem(row, 0)
  end



end
