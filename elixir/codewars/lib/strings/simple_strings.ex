defmodule SimpleStrings do
  @moduledoc """
  Simple katas with strings
  """

  @doc """
  Elixir #4
  Maximum Length Difference - 7 kyu
  https://www.codewars.com/kata/5663f5305102699bad000056/train/elixir
  """
  def mxdiflg(a1, a2) do
    if a1 == [] or a2 == [] do
      -1
    else
      comparator = fn x -> String.length(x) end
      max1 = String.length(Enum.max_by(a1, comparator))
      max2 = String.length(Enum.max_by(a2, comparator))
      min1 = String.length(Enum.min_by(a1, comparator))
      min2 = String.length(Enum.min_by(a2, comparator))

      max(abs(max1 - min2), abs(max2 - min1))
    end
  end

  @doc """
  Elixir #6
  Counting Duplicates - 6 kyu
  https://www.codewars.com/kata/54bf1c2cd5b56cc47f0007a1/train/elixir
  Return the count of distinct case-insensitive alphabetic characters and numeric digits that
  occur more than once in the input string. The input string can be assumed to contain only
  alphabets (both uppercase and lowercase) and numeric digits.
  """
  def count_duplicates(str) do
    # make a map with count of every unique char in a given string
    String.upcase(str)
    |> String.to_charlist
    |> Enum.reduce(%{}, fn key, acc -> Map.update(acc, key, 1, fn count -> count + 1 end) end)
    # count number of chars appeared in given string more then once
    |> Enum.reduce(0, fn {_key, val}, acc -> if val > 1 do acc + 1 else acc end end)
  end

end
