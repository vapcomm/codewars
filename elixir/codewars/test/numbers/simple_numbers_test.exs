defmodule SimpleNumbersTest do
  use ExUnit.Case

  alias SimpleNumbers

  @moduletag :capture_log

  doctest SimpleNumbers

  test "sum_digits fixed tests from kata" do
    assert SimpleNumbers.sum_digits(0) == 0
    assert SimpleNumbers.sum_digits(1234567890) == 45
    assert SimpleNumbers.sum_digits(100000002) == 3
    assert SimpleNumbers.sum_digits(800000009) == 17
  end

  test "sum_digits basic kata tests" do
    assert SimpleNumbers.sum_digits(10) == 1
    assert SimpleNumbers.sum_digits(99) == 18
    assert SimpleNumbers.sum_digits(-32) == 5
  end

  test "base minValue" do
    assert SimpleNumbers.minValue([1, 3, 1]) == 13
    assert SimpleNumbers.minValue([4, 5, 4, 7]) == 457
    assert SimpleNumbers.minValue([4, 8, 1, 4]) == 148
    assert SimpleNumbers.minValue([5, 7, 9, 5, 7, 7]) == 579
    assert SimpleNumbers.minValue([6, 7, 8, 7, 6, 6]) == 678
  end

  test "minValue empty list" do
    assert SimpleNumbers.minValue([]) == 0
  end

end
