defmodule SimpleNumbers do
  @moduledoc """
  Simple katas with numbers
  """

  @doc """
  Elixir #3
  Returns the sum of the absolute value of each of the number's decimal digits.
  https://www.codewars.com/kata/52f3149496de55aded000410/train/elixir
  """
  def sum_digits(number) do
    Enum.sum(Enum.map(Integer.digits(number), fn d -> abs(d) end))
  end

  @doc """
  Elixir #5
  Return the smallest number that could be formed from digits in given list
  https://www.codewars.com/kata/5ac6932b2f317b96980000ca/train/elixir
  """
  def minValue(numbers) do
    MapSet.new(numbers) |> MapSet.to_list |> Enum.sort |> Integer.undigits
  end

end
