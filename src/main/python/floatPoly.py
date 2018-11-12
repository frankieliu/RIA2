import sys
import math
import numpy as np
import pandas as pd
from scipy.spatial import ConvexHull
from scipy.spatial import Delaunay
import matplotlib.pyplot as plt

import util

debug = False

# Read the points
df = pd.read_csv("floatPoly.csv")
pd.set_option('display.max_columns', 200)
if debug:
    print(df)

# invert y values
df.iloc[:,1] *= -1;

# Extract x and y create list of points
x = df.iloc[:,0].values
y = df.iloc[:,1].values
pts = np.array(list(zip(x,y)))

# find the longest edge, use this as the floor orientation
smax, d, a = util.find_longest(pts)
prot = util.rot(pts, -a)
smax = util.rot(smax, -a)
if debug:
    print(smax)
ctd = util.centroid(prot)

# shift points
prot -= ctd
smax -= ctd

# Calculate convex hull (for its equations)
hull = ConvexHull(prot)

# Use delauney to find points in hull
delaunay = Delaunay(hull.points[hull.vertices])

# Find the middle of segmax
smid = np.mean(smax,axis=0)

# Track radius as a function of depth
rad = []
rad.append(d*0.5)

# Track the mid point as a function of depth
smidm = np.array([smid])
smid -= np.array([0, 1])

# Descend at smaxmid by 1 and find left and right intersepts
l = 1
while True:
    ahitl = util.hit(hull, np.array([-1, 0]), smid)
    ahitr = util.hit(hull, np.array([1, 0]), smid)
    l = ahitr[0] - ahitl[0]
    if l <= 2:
        break
    rad.append(l*0.5)
    if debug:
        print((ahitl, ahitr), l, (ahitr[0] + ahitl[0])*0.5)
    smid = (ahitl + ahitr) / 2
    smidm = np.append(smidm, [smid], axis=0)
    smid -= np.array([0, 1])
    if not util.in_hull([smid], delaunay):
        break

if debug:
    print(smidm)

print(rad)
print(sum([math.pi*r**2 for r in rad]))


df.plot(x="x", y="y")
plt.plot(prot[:,0], prot[:,1], color='red')
plt.plot(smidm[:,0], smidm[:,1], 'go')

# for simplex in hull.simplices:
#     plt.plot(prot[simplex, 0], prot[simplex, 1], 'k-')

plt.axis('equal')
plt.show()
