defmodule Skyscrapers4x4Test do
  use ExUnit.Case

  alias Skyscrapers4x4

  @moduletag :capture_log

  test "it can solve 4x4 puzzle 1" do
    clues = [
      2, 2, 1, 3,
      2, 2, 3, 1,
      1, 2, 2, 3,
      3, 2, 1, 3
    ]

    expected = [
      [1, 3, 4, 2],
      [4, 2, 1, 3],
      [3, 4, 2, 1],
      [2, 1, 3, 4]
    ]

    actual = Skyscrapers4x4.solve(clues)
    assert actual == expected
  end

  @doc """
  For history:
  first solve() variant on M1: 1M iterations: ~6160 ms
  optimized: on M1: ~8480 ms
  """
  @tag :skip
  test "benchmark 1" do
    clues = [
      2, 2, 1, 3,
      2, 2, 3, 1,
      1, 2, 2, 3,
      3, 2, 1, 3
    ]

    {time, _} = :timer.tc(fn ->
      assert Enum.each(1..1000000, fn _ -> Skyscrapers4x4.solve(clues) end) == :ok
    end)
    IO.puts("Benchmark 1 execution time: #{time / 1000} ms")
  end

  @doc """
  For history:
  first solve() variant on M1: 1M iterations: ~12150 ms
  optimized: on M1: ~9600 ms
  """
  @tag :skip
  test "benchmark 2" do
    clues = [
      0, 0, 1, 2,
      0, 2, 0, 0,
      0, 3, 0, 0,
      0, 1, 0, 0
    ]

    {time, _} = :timer.tc(fn ->
      assert Enum.each(1..1000000, fn _ -> Skyscrapers4x4.solve(clues) end) == :ok
    end)
    IO.puts("Benchmark 2 execution time: #{time / 1000} ms")
  end

  test "it can solve 4x4 puzzle 2" do
    clues = [
      0, 0, 1, 2,
      0, 2, 0, 0,
      0, 3, 0, 0,
      0, 1, 0, 0
    ]

    expected = [
      [2, 1, 4, 3],
      [3, 4, 1, 2],
      [4, 2, 3, 1],
      [1, 3, 2, 4]
    ]

    actual = Skyscrapers4x4.solve(clues)
    assert actual == expected
  end

  test "attempt random test" do
    clues = [0, 2, 0, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 0, 1, 2]

    expected = [
      [3, 2, 1, 4],
      [4, 1, 3, 2],
      [1, 4, 2, 3],
      [2, 3, 4, 1]
    ]

    assert Skyscrapers4x4.solve(clues) == expected
  end

end
