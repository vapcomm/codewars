defmodule RomanNumerals do
  @moduledoc """
  Roman Numerals Encoder - 6 kyu
  https://www.codewars.com/kata/51b62bf6a9c58071c600001b/elixir
  """

  @doc """
  Elixir #2
  Return roman numeral string of given number
  """
  def arabic_to_roman(number) do
    if number >= 1 and number <= 3999 do
      thousands = {"", "M", "MM", "MMM"}
      hundreds = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"}
      tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"}
      ones = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"}

      elem(thousands, div(number, 1000)) <>
      elem(hundreds, div(rem(number, 1000), 100)) <>
      elem(tens, div(rem(number, 100), 10)) <>
      elem(ones, rem(number, 10))
    else
      ""
    end
  end

end
