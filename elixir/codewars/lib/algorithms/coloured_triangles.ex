defmodule ColouredTriangles do
  @moduledoc """
  Insane Coloured Triangles -- 2 kyu
  https://www.codewars.com/kata/5a331ea7ee1aae8f24000175/train/elixir
  """

  import Bitwise

  def triangle(row) do
#    IO.puts("\ntriangle: row: '#{row}'")
    row
    |> String.to_charlist()
    |> triangulate()
    |> List.to_string()
  end

  # Recursively find solution
  defp triangulate(chars) do
#    IO.puts("triangulate: chars: #{Enum.map_join(chars, ", ", fn c -> Integer.to_string(c) end)}")

    [head | tail] = chars

#    IO.puts("triangulate: head: #{inspect(head)}, tail: #{inspect(tail)}")

    if Enum.empty?(tail) do
      # last char in head, it's a solution
      [head]
    else
      compress(head, tail)
      |> triangulate()
    end
  end

# Table to compress two ints in one
#  R % R -> R
#  R % G -> B
#  R % B -> G
#  G % G -> G
#  G % B -> R
#  B % B -> B
#
# R = 0x52  0b01010010  82
# G = 0x47  0b01000111  71
# B = 0x42  0b01000010  66
#
#a: 82, b: 82 -> 36: 82
#a: 82, b: 71 -> 33: 66
#a: 82, b: 66 -> 32: 71
#a: 71, b: 82 -> 12: 66
#a: 71, b: 71 -> 9:  71
#a: 71, b: 66 -> 8:  82
#a: 66, b: 82 -> 4:  71
#a: 66, b: 71 -> 1:  82
#a: 66, b: 66 -> 0:  66

#       0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20
@comp {66,82, 0, 0,71, 0, 0, 0,82,71, 0, 0,66, 0, 0, 0, 0, 0, 0, 0, 0,
#      21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,71,66, 0, 0,82}

  # Returns list compressed by one step
  defp compress(a, tail) do
    if Enum.empty?(tail) do
      []
    else
      [b | t] = tail
      [rgb(a, b) | compress(b, t)]
    end
  end

  defp rgb(a, b) do
    index = ((a &&& 0b11100) <<< 1) ||| ((b &&& 0b11100) >>> 2)
#    IO.puts("rgb: a: #{a}, b: #{b}, i: #{index}")
    elem(@comp, index)
  end

end
