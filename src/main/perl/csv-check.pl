while(<>){
  my @a = split /,/;
  if(scalar(@a) != 607) {
    print($a[3]," ",scalar(@a),"\n");
  }
}

