defmodule Skyscrapers7x7ImplTest do
  @moduledoc """
  Tests for internal functions of Skyscrapers4x4
  """

  use ExUnit.Case

  import Skyscrapers7x7
  import Bitwise

  @moduletag :capture_log

  # Smoke test for permutations generator.
  test "permutations" do
    perms = permutations()
    assert length(perms) == 5040

    # check all permutations are unique
    set = MapSet.new(perms)
    assert MapSet.size(set) == 5040
  end

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

    # it's was calculated by find_solution() itself, just smoke test
    expected = [
      [1, 2, 3, 4, 5, 6, 7],
      [2, 1, 4, 3, 6, 7, 5],
      [3, 4, 1, 2, 7, 5, 6],
      [4, 5, 6, 7, 1, 2, 3],
      [5, 3, 7, 6, 2, 1, 4],
      [6, 7, 2, 5, 3, 4, 1],
      [7, 6, 5, 1, 4, 3, 2]
    ]

    assert grid_to_result(solution) == expected
  end

  test "grid to result" do
    grid = encode_grid({
      {1,5,6,7,4,3,2},
      {2,7,4,5,3,1,6},
      {3,4,5,6,7,2,1},
      {4,6,3,1,2,7,5},
      {5,3,1,2,6,4,7},
      {6,2,7,3,1,5,4},
      {7,1,2,4,5,6,3}
    })

    expected = [
      [1,5,6,7,4,3,2],
      [2,7,4,5,3,1,6],
      [3,4,5,6,7,2,1],
      [4,6,3,1,2,7,5],
      [5,3,1,2,6,4,7],
      [6,2,7,3,1,5,4],
      [7,1,2,4,5,6,3]
    ]

    assert grid_to_result(grid) == expected
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

  test "encode permutation" do
    assert 0x7654321 == encode_permutation([1, 2, 3, 4, 5, 6, 7])
    assert 0 == encode_permutation([])
  end

  test "find left visibility" do
    left_1 =[ [7, 6, 5, 4, 3, 2, 1], [7, 5, 4, 3, 2, 1, 6], [7, 1, 2, 3, 4, 5, 6]]
    Enum.each(left_1, fn p -> assert find_left_visibility(p) == 1 end)

    left_2 =  [[6, 1, 2, 3, 4, 5, 7], [6, 7, 1, 2, 3, 4, 5], [5, 1, 2, 3, 4, 7, 6]]
    Enum.each(left_2, fn p -> assert find_left_visibility(p) == 2 end)

    left_3 = [[3, 6, 1, 2, 4, 5, 7], [1, 2, 7, 4, 5, 6]]
    Enum.each(left_3, fn p -> assert find_left_visibility(p) == 3 end)

    assert find_left_visibility([1, 2, 3, 4, 5, 6, 7]) == 7
  end

  test "find right visibility" do
    right_1 = [[1, 2, 3, 4, 5, 6, 7], [6, 2, 3, 4, 5, 1, 7]]
    Enum.each(right_1, fn p -> assert find_right_visibility(p) == 1 end)

    right_2 = [[2, 3, 4, 5, 6, 7, 1], [6, 2, 3, 4, 5, 7, 1], [7, 2, 3, 4, 5, 1, 6]]
    Enum.each(right_2, fn p -> assert find_right_visibility(p) == 2 end)

    right_6 = [[4, 7, 6, 5, 3, 2, 1], [6, 7, 5, 4, 3, 2, 1], [7, 6, 5, 4, 3, 1, 2]]
    Enum.each(right_6, fn p -> assert find_right_visibility(p) == 6 end)

    assert find_right_visibility([7, 6, 5, 4, 3, 2, 1]) == 7
  end

  test "unique columns good" do
    good_grids = [
      {
        {0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0}
      },
      {
        {1, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0},
        {0, 0, 1, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 0},
        {0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 0, 0, 1}
      },
      {
        {1, 2, 3, 4, 5, 6, 7},
        {2, 1, 4, 3, 6, 7, 5},
        {3, 4, 1, 2, 7, 5, 6},
        {4, 5, 6, 7, 1, 2, 3},
        {5, 3, 7, 6, 2, 1, 4},
        {6, 7, 2, 5, 3, 4, 1},
        {7, 6, 5, 1, 4, 3, 2}
      }
    ]

    Enum.each(good_grids, fn grid -> assert columns_have_unique_numbers(encode_grid(grid)) == true end )
  end

  test "unique columns bad" do
    bad_grids = [
      {
        {0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0},
        {1, 0, 0, 0, 0, 0, 0},
        {1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0}
      },
      {
        {1, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0},
        {0, 0, 2, 0, 0, 0, 0},
        {0, 0, 2, 1, 0, 0, 0},
        {0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 0, 0, 1}
      },
      {
        {1, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0},
        {0, 0, 2, 1, 0, 0, 0},
        {0, 0, 2, 1, 0, 0, 0},
        {0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 0, 0, 1}
      },
      {
        {1, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0},
        {0, 0, 2, 0, 0, 0, 0},
        {0, 0, 3, 1, 0, 0, 0},
        {0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 1, 7},
        {0, 0, 0, 0, 0, 0, 7}
      },
      {               # v
        {1, 2, 3, 4, 5, 6, 7},
        {2, 1, 4, 3, 6, 7, 5},
        {3, 4, 1, 2, 7, 5, 6},
        {4, 5, 6, 7, 1, 5, 3}, # < two 5
        {5, 3, 7, 6, 2, 1, 4},
        {6, 7, 2, 5, 3, 4, 1},
        {7, 6, 5, 1, 4, 3, 2}
      }
    ]

    Enum.each(bad_grids, fn grid -> assert columns_have_unique_numbers(encode_grid(grid)) == false end )
  end


  test "columns" do
    grid = encode_grid({
      {1, 0, 0, 0, 0, 0, 0},
      {2, 0, 0, 0, 0, 0, 0},
      {3, 0, 0, 0, 0, 0, 0},
      {4, 0, 0, 0, 0, 0, 0},
      {5, 0, 0, 0, 0, 0, 0},
      {6, 0, 0, 0, 0, 0, 0},
      {7, 0, 0, 0, 0, 0, 0}
    })

    column = get_column(grid, 0)
    assert column == 0x7654321

    grid = set_column(grid, column, 1)
    result = grid_to_result(grid)
    expected = [
      [1, 1, 0, 0, 0, 0, 0],
      [2, 2, 0, 0, 0, 0, 0],
      [3, 3, 0, 0, 0, 0, 0],
      [4, 4, 0, 0, 0, 0, 0],
      [5, 5, 0, 0, 0, 0, 0],
      [6, 6, 0, 0, 0, 0, 0],
      [7, 7, 0, 0, 0, 0, 0]
    ]
    assert result == expected

    column = 0x1234567
    grid = set_column(grid, column, 2)
    result = grid_to_result(grid)
    expected = [
      [1, 1, 7, 0, 0, 0, 0],
      [2, 2, 6, 0, 0, 0, 0],
      [3, 3, 5, 0, 0, 0, 0],
      [4, 4, 4, 0, 0, 0, 0],
      [5, 5, 3, 0, 0, 0, 0],
      [6, 6, 2, 0, 0, 0, 0],
      [7, 7, 1, 0, 0, 0, 0]
    ]
    assert result == expected

    column = 0x7123456
    grid = set_column(grid, column, 6)
    result = grid_to_result(grid)
    expected = [
      [1, 1, 7, 0, 0, 0, 6],
      [2, 2, 6, 0, 0, 0, 5],
      [3, 3, 5, 0, 0, 0, 4],
      [4, 4, 4, 0, 0, 0, 3],
      [5, 5, 3, 0, 0, 0, 2],
      [6, 6, 2, 0, 0, 0, 1],
      [7, 7, 1, 0, 0, 0, 7]
    ]
    assert result == expected
  end

  #------------ benchmarks ----------
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

  @tag :skip
  test "replace_at benchmark" do
    grid = encode_grid({
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 0}
    })

    row = 1
    for r <- 0..6 do
      {time, _} = :timer.tc(fn ->
        Enum.each(1..10000000, fn _ ->
          replace_at(grid, r, row)
          replace_at(grid, r, row)
          replace_at(grid, r, row)
          replace_at(grid, r, row)
          replace_at(grid, r, row)
          replace_at(grid, r, row)
          replace_at(grid, r, row)
          replace_at(grid, r, row)
          replace_at(grid, r, row)
          replace_at(grid, r, row)
        end)
      end)
      IO.puts("replace_at #{r} benchmark execution time: #{time / 1000} ms")
    end
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
