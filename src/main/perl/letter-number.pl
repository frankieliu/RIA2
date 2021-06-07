#!/usr/bin/perl
use strict;
use warnings;

# go through all the directories in present working directory
# convert each dir/jobj.json file into a corresponding csv line
# for each json file create one csv line
# result would be a csv file for the directory

# read the directory
opendir(DH, ".");
my @dh = readdir(DH);
closedir(DH);

# look for section | number . number
# when there is no subsection
my %h;
my %hd;
my $debug = 0;

for my $d (@dh) {
  if ($d =~ /([a-z])([0-9.]+)(\.(left|right|middle|upper|lower|top|bottom))?$/) {
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
    if ($debug) {
      print "$d: no match\n";
    }
  }
}
for my $x (sort(keys %h)) {
  my %hc = map { $_ => 1 } @{$h{$x}};
  my $uni_keys = keys %hc;
  my $num_els = @{$h{$x}};
  if ($uni_keys != $num_els) {
    if ($debug) {
      print "$x:",join(",",@{$h{$x}}),"\n";
    }
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
      if (($brk1[1] eq "") || ($brk2[1] eq "")){
        print "$a $b\n";
      }
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

  if ( -e "${hd{$x}}/jobj.json" ) {
    if (! $debug) { 
      # print "${hd{$x}}\n";
      my $out = `Python/run.sh ${hd{$x}}`;
      print "$x,${hd{$x}},$out\n";
    }
  }
}
