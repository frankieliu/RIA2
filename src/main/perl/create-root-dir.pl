#!/usr/bin/perl

use strict;
use warnings;

sub usage {
 
 print <<EOF;

  Traced files come in format

  p123.12/p123.12.root.png
  p123.12/p123.12.shoot.png

  We break into two separate 

  Root/p123.12/p123.12.root.png
  Shoot/p123.12/p123.12.shoot.png

  where the Shoot images have been flipped.

  0. Run from directory containing p123.12 directories
  1. mkdir Root
  2. mkdir Shoot
  3. perl perl/create-root-dir.pl

EOF

}

# Find directories ending in \d+\.\d+
opendir(FH, ".");
my @dirs = grep {-d $_ && /\d+\.\d+$/} readdir(FH);
closedir(FH);

# ls -d b* v* | wc
# 213

# Keep track of how many files were read
my $i=0;

# Opening each directory and looking for the presence of a root and a shoot
for my $x (@dirs) {

  print "Opening directory $x\n";
  opendir(FH, $x);
  my @files = grep {/\.png$/} readdir(FH);
  closedir(FH);

  print "Creating subdirectories Root/$x and Shoot/$x\n";
  my $sub1 = "Root/$x";
  my $sub2 = "Shoot/$x";

  my $cmd = "mkdir $sub1";
  print "$cmd\n";
  system($cmd);
  $cmd = "mkdir $sub2";
  print "$cmd\n";
  system($cmd);

  # Add files in directory to a hash
  my %h;
  for my $f (@files) {
    if ($f =~ /\.root\.png/) {
      my $cmd = "mv $x/$f $sub1";
      print "$cmd\n";
      system($cmd);
    } elsif ($f =~ /\.shoot\.png/) {
      # Not necessary to do the flip already flipped 
      # my $cmd = "convert $x/$f -flip $sub2/$f";
      my $cmd = "mv $x/$f $sub2";
      print "$cmd\n";
      system($cmd);
    }
  }
  $i++;
}

print "Number read $i\n"; 
print `ls -d b* v* | wc`;
