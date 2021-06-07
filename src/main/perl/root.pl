#!/usr/bin/perl
use strict;
use warnings;
use File::Basename;

# From the current directory
# 1. Look for directories beginning with W or w or N
# 2. In each of these directories look for jobj.json
# 3. If found then, continue to next directory

my $dirname = dirname(__FILE__);

opendir(DH, ".");
my @dir = grep { -d $_ && /^[WwNbv]/ } readdir(DH);
for my $d (@dir) {
  opendir(DH1, "./$d");
  # print readdir(DH1);
  my @rd = readdir(DH1);
  # print($d,"\n", @rd,"\n");
  my @json = grep {/jobj.json$/} @rd;
  if (scalar(@json) != 0) {
    print("$d: Found jobj.json\n");
    next;
  }
  my @file = grep {/(root|shoot).png$/} @rd;
  closedir(DH1);
  # print("In directory ${d} scalar:",scalar(@file)," ",@file," ", @rd,"\n");
  if (scalar(@file) != 0) {
    print $d," / ",$file[0], "\n";
    my $cmd = "${dirname}/run.sh -i ${d}/$file[0] -n 10 -d 1,2,3";
    print "$cmd\n";
    system($cmd);
  } else {
    print "${d}: image not found.\n";
  }
}
closedir(DH);
