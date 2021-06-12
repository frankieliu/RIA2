# Depth Profile
1. Walk from top to bottom
2. Obtain the number of non-zero pixels per line
3. Obtain the width occupied by the non-zero pixels per line (extent)
4. To make it depth independent, take a number n
5. Report the depths containing 1/n, 2/n, 3/n, ... of the total
6. For example if n=20, report the depths for 5%, 10%, 15%, ... of the total   pixel count
