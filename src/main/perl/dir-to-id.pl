#!/usr/bin/perl
use strict;
use warnings;
use JSON;

opendir(DH,".");
my @dir = grep { -d && /^W1960/ } readdir(DH);
closedir(DH);

for my $d (@dir){
  #print "$d/jobj.json";
  my $json;
  {
    local $/;
    open my $fh, "<", "$d/jobj.json";
    $json = <$fh>;
    close $fh;
  }
  #print $json;
  my $data = decode_json($json);
  print $d." ".$data->{'Diameter'}->{'mean'}."\n";
}
