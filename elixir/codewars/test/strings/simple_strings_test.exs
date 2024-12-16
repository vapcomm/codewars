defmodule SimpleStringsTest do
  use ExUnit.Case

  alias SimpleStrings

  @moduletag :capture_log

  doctest SimpleStrings

  defp mx_testing(a1, a2, exp) do
    IO.puts "a1 : #{inspect(a1)}"
    IO.puts "a2 : #{inspect(a2)}"
    act = SimpleStrings.mxdiflg(a1, a2)
    IO.puts "Actual: #{inspect(act)}"
    IO.puts "Expect: #{inspect(exp)}"
    assert act == exp
    IO.puts("#")
  end

  # Maximum Length Difference test
  test "mxdiflg base" do
    s1 = ["hoqq", "bbllkw", "oox", "ejjuyyy", "plmiis", "xxxzgpsssa", "xxwwkktt", "znnnnfqknaz", "qqquuhii", "dvvvwz"]
    s2 = ["cccooommaaqqoxii", "gggqaffhhh", "tttoowwwmmww"]
    mx_testing(s1, s2, 13)
    s1 = ["ejjjjmmtthh", "zxxuueeg", "aanlljrrrxx", "dqqqaaabbb", "oocccffuucccjjjkkkjyyyeehh"]
    s2 = ["bbbaaayddqbbrrrv"]
    mx_testing(s1, s2, 10)
    s1 = ["ccct", "tkkeeeyy", "ggiikffsszzoo", "nnngssddu", "rrllccqqqqwuuurdd", "kkbbddaakkk"]
    s2 = ["tttxxxxxxgiiyyy", "ooorcvvj", "yzzzhhhfffaaavvvpp", "jjvvvqqllgaaannn", "tttooo", "qmmzzbhhbb"]
    mx_testing(s1, s2, 14)
    s1 = []
    s2 = ["cccooommaaqqoxii", "gggqaffhhh", "tttoowwwmmww"]
    mx_testing(s1, s2, -1)
    s2 = []
    s1 = ["cccooommaaqqoxii", "gggqaffhhh", "tttoowwwmmww"]
    mx_testing(s1, s2, -1)
    s1 = []
    s2 = []
    mx_testing(s1, s2, -1)
  end

  # Counting Duplicates test
  test "Counting Duplicates example" do
    assert SimpleStrings.count_duplicates("") == 0
    assert SimpleStrings.count_duplicates("abcde") == 0
    assert SimpleStrings.count_duplicates("aabbcde") == 2
    assert SimpleStrings.count_duplicates("aabBcde") == 2
    assert SimpleStrings.count_duplicates("Indivisibility") == 1
    assert SimpleStrings.count_duplicates("Indivisibilities") == 2
  end

end
