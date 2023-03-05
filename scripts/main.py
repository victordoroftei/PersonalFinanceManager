import sys
from timeit import default_timer as timer

import cv2
import pytesseract
from pytesseract.pytesseract import image_to_data, Output

from classes.postprocessor import Postprocessor


# get grayscale image
def get_grayscale(image):
    return cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)


# noise removal
def remove_noise(image):
    return cv2.medianBlur(image, 5)


#no_noise = remove_noise(img)
#img = cv2.resize(img, None, fx=0.6, fy=0.6, interpolation=cv2.INTER_CUBIC)
#gray = get_grayscale(img)
#kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (5, 5))
#gray = remove_noise(gray)
#cv2.imwrite("test.jpg", gray)

#img = cv2.imread('test.jpg')

#run_preprocessing_steps(img)

noArgs = len(sys.argv)

if noArgs == 1:
    startTime = timer()

    fileName = 'bon/IMG_0015.jpg'
    img = cv2.imread(fileName)

    img = cv2.resize(img, None, fx=0.6, fy=0.6, interpolation=cv2.INTER_AREA)
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    blur = cv2.GaussianBlur(gray, (3, 3), 0)
    thresh = cv2.threshold(blur, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)[1]

    # Morph open to remove noise and invert image
    #kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (5, 5))
    kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (3, 3))
    opening = cv2.morphologyEx(thresh, cv2.MORPH_OPEN, kernel, iterations=1)
    #eroded = cv2.erode(thresh, kernel, iterations=1)
    invert = 255 - thresh
    # invert = 255 - opening

    # Perform text extraction
    data = pytesseract.image_to_string(invert, lang='eng', config='--psm 6 -c tessedit_char_whitelist="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ#/*%-.,abcdefghijklmnopqrstuvwxyz "')
    print(data)

    outputFile = open(f"{fileName}-out.txt", "w")
    outputFile.write(data)
    outputFile.close()

    cv2.imwrite('blur.jpg', blur)
    cv2.imwrite("gray.jpg", gray)
    cv2.imwrite('thresh.jpg', thresh)
    cv2.imwrite('opening.jpg', opening)
    cv2.imwrite('invert.jpg', invert)

    postprocessor = Postprocessor(f"{fileName}-out.txt", fileName)
    postprocessor.applyItemRegex()
    postprocessor.applyTotalRegex()
    postprocessor.applyDateAndTimeRegex()
    postprocessor.printJson()

    d = image_to_data(invert, output_type=Output.DICT, lang="eng", config='--psm 6 -c tessedit_char_whitelist="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ#/*%-.,abcdefghijklmnopqrstuvwxyz "')
    for k in d.keys():
        print(k, ": ", d[k])
    n_boxes = len(d['level'])
    for i in range(n_boxes):
        (x, y, w, h) = (d['left'][i], d['top'][i], d['width'][i], d['height'][i])
        cv2.rectangle(invert, (x, y), (x + w, y + h), (0, 255, 0), 2)

    cv2.imwrite('box.jpg', invert)

    print()
    postprocessor.testFormatDate()

    endTime = timer() - startTime
    print(f"\n{endTime}s")

else:
    if sys.argv[1] == "java" and len(sys.argv) >= 3:
        startTime = timer()

        fileName = sys.argv[2]
        img = cv2.imread(fileName)

        img = cv2.resize(img, None, fx=0.6, fy=0.6, interpolation=cv2.INTER_AREA)
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        blur = cv2.GaussianBlur(gray, (3, 3), 0)
        thresh = cv2.threshold(blur, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)[1]
        kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (3, 3))
        opening = cv2.morphologyEx(thresh, cv2.MORPH_OPEN, kernel, iterations=1)
        invert = 255 - thresh

        data = pytesseract.image_to_string(invert, lang='eng',
                                           config='--psm 6 -c tessedit_char_whitelist="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ#/*%-.,abcdefghijklmnopqrstuvwxyz "')

        outputFile = open(f"{fileName}-out.txt", "w")
        outputFile.write(data)
        outputFile.close()

        postprocessor = Postprocessor(f"{fileName}-out.txt", fileName)
        postprocessor.applyItemRegex()
        postprocessor.applyTotalRegex()
        postprocessor.applyDateAndTimeRegex()
        postprocessor.printJson()

        endTime = timer() - startTime

        if len(sys.argv) == 4:
            if sys.argv[3] == "time":
                print(f"\n{endTime}s")
