#!/usr/bin/perl
use strict;
use warnings;

sub usage {
 
 print <<EOF;

  Run from a directory containing p123.12
  where p is some prefix followed by two digits
  separated by a period.

  It then checks each directory to figure out if
  there are two files one for root and another for
  shoot.

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
 
  # Add files in directory to a hash
  my %h;
  for my $f (@files) {
    # print "-$x/$f-\n";
    $h{$f} = 1;
  }

  # Check if both extensions exit
  for my $ext ('root.png', 'shoot.png') {
      print "$x.$ext does not exist\n" if (!exists($h{"$x.$ext"}));
  }
  $i++;
}

print "Number read $i\n"; 
print `ls -d b* v* | wc`;
