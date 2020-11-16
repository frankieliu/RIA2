#!/usr/bin/perl
use strict;
use warnings;

# read the directory
opendir(DH, ".");
my @dh = readdir(DH);
closedir(DH);

# look for section | number . number
# when there is no subsection
my %h;
my %hd;

for my $d (@dh) {
  if ($d =~ /([a-z])([0-9.]+)(\.(left|right|middle))?$/) {
    my $letter = $1;
    my $number = $2;
    my @num = split(/\./,$number);
    my $num1 = $num[0];
    my $key = "${letter},$num1";
    if (!exists($h{$key})) {
      $h{$key} = [];
    }
    my $num2 = 0;
    if (scalar(@num) > 1) {
      $num2 = $num[1];
    }
    my $key2 = "${letter},$num1,$num2";
    $hd{$key2} = $d;
    # print $da;
  } else {
    print "$d: no match\n"
  }
}
for my $x (sort(keys %h)) {
  my %hc = map { $_ => 1 } @{$h{$x}};
  my $uni_keys = keys %hc;
  my $num_els = @{$h{$x}};
  if ($uni_keys != $num_els) {
    print "$x:",join(",",@{$h{$x}}),"\n";
  }
}
for my $x (sort {
    my @brk1 = split(",", $a);
    my @brk2 = split(",", $b);
    if ($brk1[0] lt $brk2[0]) {
      return -1;
    } elsif ($brk1[0] gt $brk2[0]) {
      return 1;
    } else {
      if ($brk1[1] < $brk2[1]) {
        return -1;
      } elsif ($brk1[1] > $brk2[1]) {
        return 1;
      } else {
        if ($brk1[2] < $brk2[2]) {
          return -1;
        } elsif ($brk1[2] > $brk2[2]) {
          return 1;
        } else {
          return 0;
        }
      }
    }
  }
  keys %hd) {
  print "$x: ${hd{$x}}\n";
}
