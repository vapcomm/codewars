defmodule ColouredTrianglesTest do
  use ExUnit.Case

  alias ColouredTrianglesTestData
  import ColouredTriangles

  @moduletag :capture_log

  test "Sample tests" do
    Enum.each([
      {"B", "B"},
      {"GB", "R"},
      {"RRR", "R"},
      {"RGBG", "B"},
      {"RBRGBRB", "G"},
      {"RBRGBRBGGRRRBGBBBGG", "G"},
      {"BGRGRBGBRRBBGRBGBBRBRGBRG", "B"},
      {"GRBGRRRBGRBGRGBRGBRBRGBRRGRBGRGBB", "R"},
      {"RBGRBGBRGBRBRGGRBBGRBGBRBBGRBGGBRBGBBGRBGBRGRBGRBB", "G"},
      {"BGBGRBGRRBGRBGGGRBGRGBGRRGGRBGRGRBGBRGBGBGRGBGBGBGRRBRGRRGBGRGBRGRBGRBGRBBGBRGRGRBGRGBRGBBRGGBRBGGRB", "G"}
    ], fn {a,b} -> assert triangle(a) == b end)
  end

  test "long" do
    assert triangle(ColouredTrianglesTestData.long_triangle()) == "B"
  end

  test "nine" do
    assert triangle("RGBRGBRGB") == "G"
  end

end
