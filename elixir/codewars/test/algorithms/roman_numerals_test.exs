defmodule RomanNumeralsTest do
  use ExUnit.Case

  alias RomanNumerals

  @moduletag :capture_log

  doctest RomanNumerals

  test "small numbers" do
    assert "I" == RomanNumerals.arabic_to_roman(1)
    assert "II" == RomanNumerals.arabic_to_roman(2)
  end

  test "hundreds" do
    assert "C" == RomanNumerals.arabic_to_roman(100)
    assert "CC" == RomanNumerals.arabic_to_roman(200)
    assert "CCC" == RomanNumerals.arabic_to_roman(300)
    assert "CD" == RomanNumerals.arabic_to_roman(400)
    assert "D" == RomanNumerals.arabic_to_roman(500)
  end

  test "big numbers" do
    assert "M" == RomanNumerals.arabic_to_roman(1000)
    assert "MM" == RomanNumerals.arabic_to_roman(2000)
    assert "MMM" == RomanNumerals.arabic_to_roman(3000)
    assert "MMMD" == RomanNumerals.arabic_to_roman(3500)
  end

  test "base kata test" do
    Enum.each([
      {1, "I"},
      {2, "II"},
      {3, "III"},
      {4, "IV"},
      {5, "V"},
      {6, "VI"},
      {7, "VII"},
      {8, "VIII"},
      {9, "IX"},
      {10, "X"},
      {11, "XI"},
      {12, "XII"},
      {13, "XIII"},
      {14, "XIV"},
      {15, "XV"},
      {16, "XVI"},
      {19, "XIX"},
      {20, "XX"},
      {21, "XXI"},
      {40, "XL"},
      {49, "XLIX"},
      {50, "L"},
      {90, "XC"},
      {99, "XCIX"},
      {100, "C"},
      {101, "CI"},
      {144, "CXLIV"},
      {470, "CDLXX"},
      {578, "DLXXVIII"},
      {500, "D"},
      {501, "DI"},
      {1000, "M"},
      {1001, "MI"},
      {1002, "MII"},
      {2000, "MM"},
      {1111, "MCXI"},
      {1666, "MDCLXVI"},
      {1673, "MDCLXXIII"},
      {1990, "MCMXC"}
    ], fn({number, expected}) -> assert RomanNumerals.arabic_to_roman(number) == expected end)
  end

end
