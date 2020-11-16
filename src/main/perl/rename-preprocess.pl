#!/usr/bin/perl
use strict;
use warnings;
use File::Basename;

# From the current directory
# 1. Look for directories beginning with W or w
# 2. In each of these directories look for Preprocess.png
# 3. If found then, mv to Original/
# 4. Save the name of the original image
# 5. Move original image to Original
# 6. ln -s Traced.png to original

my $dirname = dirname(__FILE__);
my $outdir = "Original";
my $test = 0;

opendir(DH, ".");
my @dir = grep { -d $_ && /^[Ww]/ } readdir(DH);

for my $d (@dir) {
  opendir(DH1, "./$d");
  # print readdir(DH1);
  my @rd = readdir(DH1);
 
  # make directory
  if (! -d "$d/$outdir") {
    my $cmd = "mkdir -p $d/$outdir";
    print "\n$cmd\n";
    system($cmd) if ($test != 1);
  }

  # move Preprocess.png 
  my $filein = "Preprocess.png";
  my @pp = grep {/${filein}$/} @rd;
  if (scalar(@pp) != 0) {
    print("$d: Found $pp[0]\n");
    my $cmd = "mv $d/$pp[0] $d/$outdir/$pp[0]";
    print "$cmd\n";
    system($cmd) if ($test != 1);
  }

  # move original image
  my @file = grep {/(root|shoot).png$/} @rd;
  if (scalar(@file) != 0) {
    print("$d: Found $file[0]\n");
    my $cmd = "mv $d/$file[0] $d/$outdir/$file[0]";
    print "$cmd\n";
    system($cmd) if ($test != 1);
  }

  # link traced image
  $filein = "Traced.png";
  if (-e "$d/$filein") {
    print("$d: Found $filein\n");
    my $cmd = "ln -s $filein $d/$file[0]";
    print "$cmd\n";
    system($cmd) if ($test != 1);
  }
  closedir(DH1);
}
closedir(DH);
