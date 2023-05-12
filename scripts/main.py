import sys
from timeit import default_timer as timer

import cv2
import pytesseract
from pytesseract.pytesseract import image_to_data, Output

from classes.postprocessor import Postprocessor

noArgs = len(sys.argv)


class Preprocessor:
    def __init__(self, fileName):
        self.__fileName = fileName
        self.__img = cv2.imread(self.__fileName)

        self.__tesseractConfigString = '--psm 6 -c tessedit_char_whitelist="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ#/*%-.,abcdefghijklmnopqrstuvwxyz "'

    def applyPreprocessingSteps(self):
        self.__img = cv2.resize(self.__img, None, fx=0.4, fy=0.4, interpolation=cv2.INTER_AREA)

        gray = cv2.cvtColor(self.__img, cv2.COLOR_BGR2GRAY)
        blur = cv2.GaussianBlur(gray, (3, 3), 0)
        thresh = cv2.threshold(blur, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)[1]

        kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (3, 3))
        opening = cv2.morphologyEx(thresh, cv2.MORPH_OPEN, kernel, iterations=1)
        invert = 255 - thresh

        self.__img = invert

    def extractData(self):
        data = pytesseract.image_to_string(self.__img, lang='eng+ron',
                                           config=self.__tesseractConfigString)

        outputFile = open(f"{fileName}-out.txt", "w")
        outputFile.write(data)
        outputFile.close()

        return data

    def getImage(self):
        return self.__img


if noArgs == 1:
    startTime = timer()

    fileName = 'bon/IMG_0014.jpg'
    preprocessor = Preprocessor(fileName)

    preprocessor.applyPreprocessingSteps()
    img = preprocessor.getImage()

    preprocessor.extractData()

    # cv2.imwrite('blur.jpg', blur)
    # cv2.imwrite("gray.jpg", gray)
    # cv2.imwrite('thresh.jpg', thresh)
    # cv2.imwrite('opening.jpg', opening)
    # cv2.imwrite('invert.jpg', invert)

    postprocessor = Postprocessor(f"{fileName}-out.txt", fileName)
    postprocessor.applyItemRegex()
    postprocessor.applyTotalRegex()
    postprocessor.applyDateAndTimeRegex()
    postprocessor.printJson()

    d = image_to_data(preprocessor.getImage(), output_type=Output.DICT, lang="eng+ron", config='--psm 6 -c tessedit_char_whitelist="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ#/*%-.,abcdefghijklmnopqrstuvwxyz "')
    for k in d.keys():
        print(k, ": ", d[k])
    n_boxes = len(d['level'])
    for i in range(n_boxes):
        (x, y, w, h) = (d['left'][i], d['top'][i], d['width'][i], d['height'][i])
        cv2.rectangle(preprocessor.getImage(), (x, y), (x + w, y + h), (0, 255, 0), 2)

    cv2.imwrite('box.jpg', preprocessor.getImage())

    print()
    # postprocessor.testFormatDate()

    endTime = timer() - startTime
    print(f"\n{endTime}s")

else:
    if sys.argv[1] == "java" and len(sys.argv) >= 3:
        startTime = timer()

        fileName = sys.argv[2]
        preprocessor = Preprocessor(fileName)

        preprocessor.applyPreprocessingSteps()
        preprocessor.extractData()

        postprocessor = Postprocessor(f"{fileName}-out.txt", fileName)
        postprocessor.applyItemRegex()
        postprocessor.applyTotalRegex()
        postprocessor.applyDateAndTimeRegex()
        postprocessor.printJson()

        endTime = timer() - startTime

        if len(sys.argv) == 4:
            if sys.argv[3] == "time":
                print(f"\n{endTime}s")
