#!/usr/bin/perl
use strict;
use warnings;
use File::Basename;

# Opens the current directory
# Find all files with png suffix
# Creates a directory with the same base name
# Moves the file to the directory

opendir(DH, ".");
my @suffixlist = [".root.png"];
my @files = grep { -f $_ && /png$/ } readdir(DH);
for my $f (@files) {
  my $base = basename($f, ".root.png");
  my $cmd1 = "mkdir $base";
  print $cmd1,"\n";
  my $cmd2 = "mv $f $base";
  print $cmd2,"\n";
  system($cmd1);
  system($cmd2);
}
closedir(DH);
