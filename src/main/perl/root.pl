#!/usr/bin/perl
use strict;
use warnings;
use File::Basename;

my $dirname = dirname(__FILE__);

opendir(DH, ".");
my @dir = grep { -d $_ && /^W/ } readdir(DH);
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
  my @file = grep {/root.png$/} @rd;
  closedir(DH1);
  print $d," / ",$file[0], "\n";
  my $cmd = "${dirname}/run.sh -i ${d}/$file[0] -n 10 -d 1,2,3";
  print "$cmd\n";
  system($cmd);
}
closedir(DH);
