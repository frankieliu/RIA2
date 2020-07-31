#!/usr/bin/perl
use strict;
use warnings;
opendir(DH, ".");
my @files = grep { -f $_ && /png$/ } readdir(DH);
for my $f (@files) {
  if ($f =~ /(.+)rootONLY(.+)/) {
    my $pre = $1;
    my $post = $2;
    my $cmd = "mv $f ${pre}root${post}";
    print "$cmd\n";
    system($cmd);
  }
}
closedir(DH);
