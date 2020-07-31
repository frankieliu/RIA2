#!/usr/bin/perl
use strict;
use warnings;
my @list = <>;
chomp(@list);
@list = map { s/ /\%20/rg } @list;
@list = map "![]($_)", @list;
print "
<style type='text/css'>
  img { width: 100px; }
</style>


| Syntax      | Description |
| ----------- | ----------- |
| Header      | Title       |
| Paragraph   | Text        |



";
print "| ".join(" | ", @list)." |";
