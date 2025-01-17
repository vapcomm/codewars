defmodule ColouredTrianglesTest do
  use ExUnit.Case

  import ColouredTriangles
  import Bitwise

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

  #R = 0x52  0b01010010
  #G = 0x47  0b01000111
  #B = 0x42  0b01000010
#  @tag :skip
#  test "compressor bits" do
#    for a <- [0x52, 0x47, 0x42], b <- [0x52, 0x47, 0x42] do
#      index = ((a &&& 0b11100) <<< 1) ||| ((b &&& 0b11100) >>> 2)
#      IO.puts("a: #{a}, b: #{b} -> #{Integer.to_string(index)}")
#    end
#  end

end
