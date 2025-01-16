defmodule Skyscrapers7x7FastTest do
  @moduledoc """
  Upper level tests from kata
  """

  use ExUnit.Case
  import Skyscrapers7x7Fast
  @moduletag :capture_log

  test "medium" do
    clues = [
      7,0,0,0,2,2,3,
      0,0,3,0,0,0,0,
      3,0,3,0,0,5,0,
      0,0,0,0,5,0,4
    ]

    expected = [
      [1,5,6,7,4,3,2],
      [2,7,4,5,3,1,6],
      [3,4,5,6,7,2,1],
      [4,6,3,1,2,7,5],
      [5,3,1,2,6,4,7],
      [6,2,7,3,1,5,4],
      [7,1,2,4,5,6,3]
    ]

    actual = solve(clues)
    assert actual == expected
  end

  test "hard 1" do
    clues = [
      6,4,0,2,0,0,3,
      0,3,3,3,0,0,4,
      0,5,0,5,0,2,0,
      0,0,0,4,0,0,3
    ]

    expected = [
      [2,1,6,4,3,7,5],
      [3,2,5,7,4,6,1],
      [4,6,7,5,1,2,3],
      [1,3,2,6,7,5,4],
      [5,7,1,3,2,4,6],
      [6,4,3,2,5,1,7],
      [7,5,4,1,6,3,2]
    ]

    actual = solve(clues)
    assert actual == expected
  end

  test "hard 2" do
    clues = [
      0,2,3,0,2,0,0,
      5,0,4,5,0,4,0,
      0,4,2,0,0,0,6,
      5,2,2,2,2,4,1   # for a very very hard puzzle, replace the last 7 values with zeroes
    ]

    expected = [
      [7,6,2,1,5,4,3],
      [1,3,5,4,2,7,6],
      [6,5,4,7,3,2,1],
      [5,1,7,6,4,3,2],
      [4,2,1,3,7,6,5],
      [3,7,6,2,1,5,4],
      [2,4,3,5,6,1,7]
    ]

    actual = solve(clues)
    assert actual == expected
  end

  test "very very hard" do
    clues = [
      0,2,3,0,2,0,0,
      5,0,4,5,0,4,0,
      0,4,2,0,0,0,6,
      0,0,0,0,0,0,0   # as hard2, but with zeros in the last clues
    ]

    expected = [
      [7,6,2,1,5,4,3],
      [1,3,5,4,2,7,6],
      [6,5,4,7,3,2,1],
      [5,1,7,6,4,3,2],
      [4,2,1,3,7,6,5],
      [3,7,6,2,1,5,4],
      [2,4,3,5,6,1,7]
    ]

    actual = solve(clues)
    assert actual == expected
  end

end
