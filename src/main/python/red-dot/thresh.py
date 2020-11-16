import cv2
import sys
file = sys.argv[1]

bgr = cv2.imread(file)
r = bgr.copy()
r[:,:,0] = 0
r[:,:,1] = 0
cv2.imshow('red channel',r)
cv2.waitKey(0)
if False:
    thr = [100,255]
    th,threshed = cv2.threshold(r,
            thr[0],thr[1],
            cv2.THRESH_BINARY_INV|cv2.THRESH_OTSU)
    contours, hierarchy = cv2.findContours(threshed,
            cv2.RETR_LIST,
            cv2.CHAIN_APPROX_SIMPLE)

    cv2.drawContours(bgr,contours,-1,(0,255,0),3)
    cv2.imshow('Contours', bgr)
    cv2.waitKey(0)
    cv2.destroyAllWindows()
