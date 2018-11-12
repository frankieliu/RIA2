import numpy as np
import math

debug = False

def distance(a, b):
    return math.sqrt((a[0]-b[0])**2 + (a[1]-b[1])**2)

def angle(a, b):
    if (b[0] == a[0]):
        return math.pi/2;
    else:
        if a[0] > b[0]:
            return math.atan2(a[1] - b[1], a[0] - b[0]);
        else:
            return math.atan2(b[1] - a[1], b[0] - a[0]);

def find_longest(pts):
    pts2 = np.append(pts[1::],[pts[0]], axis=0)

    maxpair = []
    maxd = 0
    maxa = 0

    for p in zip(pts, pts2):
        pair = (p[0], p[1])
        d = distance(p[0], p[1])
        a = angle(p[0], p[1])
        if debug:
            print(pair, d, a/math.pi*180)
        if d > maxd:
            maxpair = (p[0], p[1])
            maxd = d
            maxa = a

    return (maxpair, maxd, maxa)

def rot(pts, a):
    rotm = np.array(
        [[math.cos(a), -math.sin(a)],
         [math.sin(a), math.cos(a)]])

    return [np.matmul(rotm, p) for p in pts]

def centroid(pts):
    return np.mean(pts, axis=0)

def hit(hull, U, B1=None):
    '''
    V.X + b = 0 -> equations
    X = alpha U
    alpha V.U + b = 0
    alpha = - b / V.U

    if
    X = alpha U + B1
    alpha V.U + V.B1  + b = 0
    - (b + V.B1) / V.U
    '''

    eq = hull.equations.T
    V, b = eq[:-1], eq[-1]

    if B1 is not None:
        b1dot = np.dot(B1, V)
    else:
        b1dot = 0
        B1 = [0, 0]

    alpha = -(b + b1dot)/np.dot(U,V)
    return np.min(alpha[alpha>0])*U + B1


def in_hull(p, hull):
    """
    Test if points in `p` are in `hull`

    `p` should be a `NxK` coordinates of `N` points in `K` dimensions
    `hull` is either a scipy.spatial.Delaunay object or the `MxK` array of the
    coordinates of `M` points in `K`dimensions for which Delaunay triangulation
    will be computed
    """
    from scipy.spatial import Delaunay
    if not isinstance(hull,Delaunay):
        hull = Delaunay(hull)

    return hull.find_simplex(p)>=0
