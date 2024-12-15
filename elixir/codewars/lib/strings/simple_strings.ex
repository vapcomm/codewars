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

end
