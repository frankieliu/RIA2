#!/usr/bin/perl
use strict;
use warnings;
use File::Basename;

# goes through each directory
# uses file *.png to return image dimensions

opendir(DH, ".");
my @dir = grep { -d $_ && /^W1960/ } readdir(DH);
for my $d (@dir) {
  opendir(DH1, "./$d");
  # print readdir(DH1);
  my @rd = readdir(DH1);
  # print($d,"\n", @rd,"\n");
  my @json = grep {/image-dimensions.org$/} @rd;
  if (scalar(@json) != 0) {
    print("$d: Found image-dimensions.org\n");
    next;
  }
  my @file = grep {/root.png$/} @rd;
  closedir(DH1);
  print $d," / ",$file[0], "\n";
  my $cmd = "file ${d}/$file[0] > ${d}/image-dimensions.org";
  print "$cmd\n";
  system($cmd);
}
closedir(DH);
