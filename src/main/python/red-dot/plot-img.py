import sys
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
img=mpimg.imread(sys.argv[1])
imgplot = plt.imshow(img)
plt.show()
