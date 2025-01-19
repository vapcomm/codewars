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
    |> encode_chars()
    |> triangulate(0)
    |> decode_chars()
    |> List.to_string()
  end

  # Recursively find solution
  defp triangulate(chars, deep) do
#    IO.puts("triangulate[#{deep}]: chars: #{Enum.map_join(chars, ", ", fn c -> Integer.to_string(c) end)}")

    [head | tail] = chars

#    IO.puts("triangulate: head: #{inspect(head)}, tail: #{inspect(tail)}")

    if Enum.empty?(tail) do
      # last char in head, it's a solution
      [head]
    else
      compress(head, tail)
      |> triangulate(deep + 1)
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
# R = 0x52  82 -> 0b00
# G = 0x47  71 -> 0b01
# B = 0x42  66 -> 0b10
#
#  R % R -> R = 0000  0: 0
#  R % G -> B = 0001  1: 2
#  R % B -> G = 0010  2: 1
#  G % R -> B = 0100  4: 2
#  G % G -> G = 0101  5: 1
#  G % B -> R = 0110  6: 0
#  B % R -> G = 1000  8: 1
#  B % G -> R = 1001  9: 0
#  B % B -> B = 1010 10: 2

#       0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15
@comp { 0, 2, 1, 3, 2, 1, 0, 3, 1, 0, 2, 3, 3, 3, 3, 3}

  # Returns list compressed by one step
  defp compress(a, tail) do
    if Enum.empty?(tail) do
      []
    else
      [b | t] = tail
      [elem(@comp, (a <<< 2) ||| b) | compress(b, t)]
    end
  end

  defp encode_chars(chars) do
    Enum.map(chars, fn char ->
      case char do
        82 -> 0b00  # R
        71 -> 0b01  # G
        66 -> 0b10  # B
         _ -> 0b11
      end
    end)
  end

  defp decode_chars(bits) do
    Enum.map(bits, fn b ->
      case (b &&& 0b11) do
        0b00 -> 82  # R
        0b01 -> 71  # G
        0b10 -> 66  # B
        _ -> 63     # ?
      end
    end)
  end

end
