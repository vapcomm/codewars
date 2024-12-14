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

end
