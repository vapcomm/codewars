defmodule SimpleNumbersTest do
  use ExUnit.Case

  alias SimpleNumbers

  @moduletag :capture_log

  doctest SimpleNumbers

  test "fixed tests from kata" do
    assert SimpleNumbers.sum_digits(0) == 0
    assert SimpleNumbers.sum_digits(1234567890) == 45
    assert SimpleNumbers.sum_digits(100000002) == 3
    assert SimpleNumbers.sum_digits(800000009) == 17
  end

  test "Basic kata tests" do
    assert SimpleNumbers.sum_digits(10) == 1
    assert SimpleNumbers.sum_digits(99) == 18
    assert SimpleNumbers.sum_digits(-32) == 5
  end

end
