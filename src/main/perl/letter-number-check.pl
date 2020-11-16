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
    if (scalar(@num) == 1) {
      push(@{$h{$key}},0);
    } else {
      push(@{$h{$key}},$num[1]);
    }
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
